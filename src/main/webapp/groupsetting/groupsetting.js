$(function () {
    $("#changeSettingDialog").dialog("close");
    $("#updateGroupSettingDialog").dialog("close");

    $(".datalist-list li").find("div.body").each(function (index, self) {
        var listsize = $(self).attr("listsize");
        var height = 0;
        if (listsize > 0) {
            height = listsize * 30 + 20;
        }
        $(self).css("height", height);
    });
    loadingCheckTerminalType();
    checkHasOperation();
});

function loadingCheckTerminalType() {
    var terminalType = $("#chargeForm").find("#terminalType").val();
    checkTerminalType(terminalType, false);
}

function checkTerminalType(terminalType, isManualSwitch) {
    var a = $(".mytabs .link").find("li.active a");
    if (a[0] != undefined && a[0].innerHTML.substring(2) == terminalType) {
        return;
    }
    $(".mytabs .link").find("li").removeClass("active");
    if (terminalType == "PC") {
        $(".mytabs .link").find("li[name='PC']").addClass("active");
        $("#chargeForm").find("#terminalType").val($.trim(terminalType));
    }
    if (terminalType == "Phone") {
        $(".mytabs .link").find("li[name='Phone']").addClass("active");
        $("#chargeForm").find("#terminalType").val($.trim(terminalType));
    }
    if (isManualSwitch) {
        trimSearchCondition();
    }
}

function enterIn(e) {
    var e = e || event,
        keyCode = e.which || e.keyCode;
    if (keyCode == 13) {
        trimSearchCondition();
    }
}

function checkHasOperation() {
    $(".mytabs div:eq(0)").find("input:checkbox").click(function () {
        var hasOperation, hasRemainingAccount;
        if (!$(this).prop("checked")) {
            hasOperation = null;
            hasRemainingAccount = null;
        } else {
            var parentName = $(this).parent().attr("name");
            switch (parentName) {
                case 'hasOperation':
                    hasOperation = true;
                    break;
                case 'noOperation':
                    hasOperation = false;
                    break;
                case 'hasRemainingAccount':
                    hasRemainingAccount = true;
                    break;
                default:
                    break;
            }
        }
        $("#chargeForm").find("#hasOperation").val(hasOperation);
        $("#chargeForm").find("#hasRemainingAccount").val(hasRemainingAccount);
        trimSearchCondition();
    });
}
function changePaging(currentPage, pageSize) {
    var chargeForm = $("#chargeForm");
    chargeForm.find("#currentPageNumberHidden").val(currentPage);
    chargeForm.find("#pageSizeHidden").val(pageSize);
    chargeForm.submit();
}

function trimSearchCondition() {
    var chargeForm = $("#chargeForm");
    var currentPage = chargeForm.find("#currentPageNumberHidden").val();
    var optimizedGroupName = $(".conn").find("li:first-child input[name='optimizedGroupName']").val();
    var operationType = $(".conn").find("select[name='operationType']").val();
    if (currentPage != '1') {
        chargeForm.find("#currentPageNumberHidden").val(1);
    }
    if (optimizedGroupName != "") {
        chargeForm.find("#optimizedGroupName").val($.trim(optimizedGroupName));
    } else {
        chargeForm.find("#optimizedGroupName").val(null);
    }
    if (operationType != ""){
        chargeForm.find("#operationType").val($.trim(operationType));
    } else {
        chargeForm.find("#operationType").val(null);
    }
    chargeForm.submit();
}

function showGroupDialog() {
    var changeSettingDialog = $("#changeSettingDialog");
    changeSettingDialog.find('#changeSettingDialogForm')[0].reset();
    $("#changeSettingDialog").find("i").text(100);
    changeSettingDialog.show();
    changeSettingDialog.dialog({
        resizable: false,
        width: 820,
        maxHeight: 534,
        title: "新增优化分组(只需指定一个操作类型, 如果还有其他操作类型，后续添加即可)",
        modal: false,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveGroupSetting('add', 1);
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#changeSettingDialog").dialog("close");
                $('#changeSettingDialogForm')[0].reset();
                $("#changeSettingDialog").find("i").text(100);
                resetTrItemColor();
            }
        }],
        onClose: function () {
            $('#changeSettingDialogForm')[0].reset();
            $("#changeSettingDialog").find("i").text(100);
            resetTrItemColor();
        }
    });
    changeSettingDialog.dialog("open");
    changeSettingDialog.window("resize", {top: $(document).scrollTop() + 150});
}

function showUpdateGroupDialog(groupUuid, groupName, remainingAccount) {
    var updateGroupSettingDialog = $("#updateGroupSettingDialog");
    updateGroupSettingDialog.find('#updateGroupSettingDialogForm')[0].reset();
    updateGroupSettingDialog.find('#remainAccount').val(remainingAccount);
    updateGroupSettingDialog.find("i").text(remainingAccount);
    findGroup(groupUuid, groupName);
    updateGroupSettingDialog.show();
    updateGroupSettingDialog.dialog({
        resizable: false,
        width: 820,
        maxHeight: 534,
        title: "修改优化分组下的操作类型(需要修改的信息请标红!!!)",
        modal: false,
        buttons: [{
            text: '修改',
            iconCls: 'icon-ok',
            handler: function () {
                saveGroupSetting('update', 0, 1, groupUuid);
                updateGroupSettingDialog.find('#remainAccount').val(updateGroupSettingDialog.find("i").text());
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#updateGroupSettingDialog").dialog("close");
                $('#updateGroupSettingDialogForm')[0].reset();
                resetTrItemColor();
                location.reload();
            }
        }],
        onClose: function () {
            $('#updateGroupSettingDialogForm')[0].reset();
            resetTrItemColor();
            location.reload();
        }
    });
    updateGroupSettingDialog.dialog("open");
    updateGroupSettingDialog.window("resize", {top: $(document).scrollTop() + 150});
}

function delGroup(groupUuid) {
    if (confirm("确定删除此优化分组吗？")) {
        $.ajax({
            url: '/internal/group/delGroup/' + groupUuid,
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "删除成功!", true);
                } else {
                    $().toastmessage('showErrorToast', "删除失败!");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "删除失败!");
            }
        });
    }
}

function findGroup(groupUuid, groupName) {
    $.ajax({
        url: '/internal/group/findGroup/' + groupUuid,
        type: 'POST',
        success: function (result) {
            if(null !== result) {
                var tableTr = $("#updateGroupSettingDialog").find("#groupSettingVos tr");
                $(tableTr).find("td").remove();
                $.each(result.groupResultVos, function (idx, val) {
                    tableTr.append("<td>" +
                        "<a class='blue' href='javascript:coverGroupSettingDialog("+ val.uuid +")'>"+ val.operationType +"</a>" +
                        "</td>");
                });
                $("#updateGroupSettingDialog").find("#settingGroup").val(groupName != null ? groupName : "");
                $("#updateGroupSettingDialog").find('#groupSettingUuid').val(result.groupSetting.uuid);
                initSettingDialog(result.groupSetting, 1);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "查询失败");
        }
    });
}

function coverGroupSettingDialog(id) {
    $("#updateGroupSettingDialog").find('#groupSettingUuid').val(id);
    findGroupSetting(id, 1);
    resetTrItemColor();
}

function showGroupSettingDialog(type, id, groupName, remainingAccount, groupUuid) {
    var changeSettingDialog = $("#changeSettingDialog");
    changeSettingDialog.find('#changeSettingDialogForm')[0].reset();
    changeSettingDialog.find('#settingGroup').val(groupName);
    changeSettingDialog.find('#remainAccount').val(remainingAccount);
    changeSettingDialog.find("i").text(remainingAccount);
    var title;
    if (type == "add") {
        title = "新增操作类型";
        changeSettingDialog.find('#groupUuid').val(id);
    } else if (type == "update") {
        title = "修改操作类型 (需要修改的信息请标红!!!)";
        changeSettingDialog.find('#groupSettingUuid').val(id);
        findGroupSetting(id);
    }
    changeSettingDialog.show();
    changeSettingDialog.dialog({
        resizable: false,
        width: 820,
        maxHeight: 534,
        title: title,
        modal: false,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveGroupSetting(type, 0, 0, groupUuid);
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#changeSettingDialog").dialog("close");
                $('#changeSettingDialogForm')[0].reset();
                $("#changeSettingDialog").find("i").text(100);
                resetTrItemColor();
            }
        }],
        onClose: function () {
            $('#changeSettingDialogForm')[0].reset();
            $("#changeSettingDialog").find("i").text(100);
            resetTrItemColor();
        }
    });
    changeSettingDialog.dialog("open");
    changeSettingDialog.window("resize", {top: $(document).scrollTop() + 150});
}

function delGroupSetting(operationTypeUuid) {
    if (confirm("确定删除此操作类型？")) {
        $.ajax({
            url: '/internal/groupsetting/delGroupSetting/' + operationTypeUuid,
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "删除成功!", true);
                } else {
                    $().toastmessage('showErrorToast', "删除失败!");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "删除失败!");
            }
        });
    }
}

function findGroupSetting(id, status) {
    $.ajax({
        url: '/internal/groupsetting/findGroupSetting/' + id,
        type: 'POST',
        success: function (obj) {
            if (null === obj) {
                $().toastmessage('showErrorMessage', "获取操作类型信息失败！");
            } else {
                initSettingDialog(obj, status);
            }
        },
        error: function () {
            $().toastmessage('showErrorMessage', "获取操作类型信息失败！");
        }
    });
}

function initSettingDialog(groupSetting, status){
    var dialogDiv;
    if (status === 1) {
        dialogDiv = $("#updateGroupSettingDialog");
    } else {
        dialogDiv = $("#changeSettingDialog");
    }
    dialogDiv.find("#settingOperationType").val(groupSetting.operationType != null ? groupSetting.operationType : "");
    dialogDiv.find("#currentAccount").val(groupSetting.machineUsedPercent);
    dialogDiv.find("#machineUsedPercent").val(groupSetting.machineUsedPercent);
    if(groupSetting.page != null){
        dialogDiv.find("#page").val(groupSetting.page);
    }
    if(groupSetting.pageSize != null){
        dialogDiv.find("#pageSize").val(groupSetting.pageSize);
    }
    if(groupSetting.zhanneiPercent != null){
        dialogDiv.find("#zhanneiPercent").val(groupSetting.zhanneiPercent);
    }
    if(groupSetting.zhanwaiPercent != null){
        dialogDiv.find("#zhanwaiPercent").val(groupSetting.zhanwaiPercent);
    }
    if(groupSetting.kuaizhaoPercent != null){
        dialogDiv.find("#kuaizhaoPercent").val(groupSetting.kuaizhaoPercent);
    }
    if(groupSetting.baiduSemPercent != null){
        dialogDiv.find("#baiduSemPercent").val(groupSetting.baiduSemPercent);
    }
    if(groupSetting.specialCharPercent != null){
        dialogDiv.find("#specialCharPercent").val(groupSetting.specialCharPercent);
    }
    if(groupSetting.dragPercent  != null){
        dialogDiv.find("#dragPercent").val(groupSetting.dragPercent );
    }
    if(groupSetting.multiBrowser != null){
        dialogDiv.find("#multiBrowser").val(groupSetting.multiBrowser);
    }
    if(groupSetting.clearCookie != null){
        dialogDiv.find("#clearCookie").val(groupSetting.clearCookie);
    }
    dialogDiv.find("#disableStatistics ").val(groupSetting.disableStatistics );
    dialogDiv.find("#disableVisitWebsite ").val(groupSetting.disableVisitWebsite );

    dialogDiv.find("#entryPageMinCount").val(groupSetting.entryPageMinCount);
    dialogDiv.find("#entryPageMaxCount").val(groupSetting.entryPageMaxCount);
    dialogDiv.find("#pageRemainMinTime").val(groupSetting.pageRemainMinTime);
    dialogDiv.find("#pageRemainMaxTime").val(groupSetting.pageRemainMaxTime);
    dialogDiv.find("#inputDelayMinTime").val(groupSetting.inputDelayMinTime);
    dialogDiv.find("#inputDelayMaxTime").val(groupSetting.inputDelayMaxTime);
    dialogDiv.find("#slideDelayMinTime").val(groupSetting.slideDelayMinTime);
    dialogDiv.find("#slideDelayMaxTime").val(groupSetting.slideDelayMaxTime);
    dialogDiv.find("#titleRemainMinTime").val(groupSetting.titleRemainMinTime);
    dialogDiv.find("#titleRemainMaxTime").val(groupSetting.titleRemainMaxTime);
    dialogDiv.find("#optimizeKeywordCountPerIP").val(groupSetting.optimizeKeywordCountPerIP);

    dialogDiv.find("#oneIPOneUser")[0].checked = (groupSetting.oneIPOneUser == 1) ? true : false;
    dialogDiv.find("#randomlyClickNoResult")[0].checked = (groupSetting.randomlyClickNoResult == 1) ? true : false;
    dialogDiv.find("#justVisitSelfPage")[0].checked = (groupSetting.justVisitSelfPage == 1) ? true : false;
    dialogDiv.find("#sleepPer2Words")[0].checked = (groupSetting.sleepPer2Words == 1) ? true : false;
    dialogDiv.find("#supportPaste")[0].checked = (groupSetting.supportPaste == 1) ? true : false;
    dialogDiv.find("#moveRandomly")[0].checked = (groupSetting.moveRandomly == 1) ? true : false;
    dialogDiv.find("#parentSearchEntry")[0].checked = (groupSetting.parentSearchEntry == 1) ? true : false;
    dialogDiv.find("#clearLocalStorage")[0].checked = (groupSetting.clearLocalStorage == 1) ? true : false;
    dialogDiv.find("#lessClickAtNight")[0].checked = (groupSetting.lessClickAtNight == 1) ? true : false;
    dialogDiv.find("#sameCityUser")[0].checked = (groupSetting.sameCityUser == 1) ? true : false;
    dialogDiv.find("#locateTitlePosition")[0].checked = (groupSetting.locateTitlePosition == 1) ? true : false;
    dialogDiv.find("#baiduAllianceEntry")[0].checked = (groupSetting.baiduAllianceEntry == 1) ? true : false;
    dialogDiv.find("#justClickSpecifiedTitle")[0].checked = (groupSetting.justClickSpecifiedTitle == 1) ? true : false;
    dialogDiv.find("#randomlyClickMoreLink")[0].checked = (groupSetting.randomlyClickMoreLink == 1) ? true : false;
    dialogDiv.find("#moveUp20")[0].checked = (groupSetting.moveUp20 == 1) ? true : false;
    dialogDiv.find("#optimizeRelatedKeyword")[0].checked = (groupSetting.optimizeRelatedKeyword == 1) ? true : false;

    dialogDiv.find("#waitTimeAfterOpenBaidu").val(groupSetting.waitTimeAfterOpenBaidu);
    dialogDiv.find("#waitTimeBeforeClick").val(groupSetting.waitTimeBeforeClick);
    dialogDiv.find("#waitTimeAfterClick").val(groupSetting.waitTimeAfterClick);
    dialogDiv.find("#maxUserCount").val(groupSetting.maxUserCount);

    dialogDiv.show();
}

function isChecked(id, dialogDiv) {
    var color = $(dialogDiv).find("#" + id).parent().css("color");
    if(color == "rgb(255, 0, 0)") {
        return "1";
    } else {
        return "0";
    }
}

function saveGroupSetting(type, status, isUpdateGroup, groupUuid){
    var dialogDiv;
    if (isUpdateGroup === 1) {
        dialogDiv = $("#updateGroupSettingDialog");
    } else {
        dialogDiv = $("#changeSettingDialog");
    }
    var group = {};
    if (status) {
        var groupName = dialogDiv.find("#settingGroup").val().trim();
        if (groupName === "") {
            alert("请输入优化组名");
            return false;
        }
        group.groupName = groupName;
        type = "groupadd";
    }
    var groupSetting = {}
    var operationType = dialogDiv.find("#settingOperationType").val();
    if (operationType == '') {
        alert("请选择操作类型！！！");
        return false;
    }
    var remainingAccount = dialogDiv.find("#machineUsedPercent").parent().find("i").text();
    if (remainingAccount < 0) {
        alert("机器占比超出范围，请修改");
        return false;
    }
    groupSetting.operationType = operationType;
    groupSetting.machineUsedPercent = dialogDiv.find("#machineUsedPercent").val();
    groupSetting.remainingAccount = remainingAccount;
    groupSetting.disableStatistics = dialogDiv.find("#disableStatistics").val();
    groupSetting.disableVisitWebsite = dialogDiv.find("#disableVisitWebsite").val();
    groupSetting.page = dialogDiv.find("#page").val();
    groupSetting.pageSize = dialogDiv.find("#pageSize").val();
    groupSetting.zhanneiPercent = dialogDiv.find("#zhanneiPercent").val();
    groupSetting.zhanwaiPercent = dialogDiv.find("#zhanwaiPercent").val();
    groupSetting.kuaizhaoPercent = dialogDiv.find("#kuaizhaoPercent").val();
    groupSetting.baiduSemPercent = dialogDiv.find("#baiduSemPercent").val();
    groupSetting.dragPercent = dialogDiv.find("#dragPercent").val();
    groupSetting.specialCharPercent = dialogDiv.find("#specialCharPercent").val();

    groupSetting.multiBrowser = dialogDiv.find("#multiBrowser").val();
    groupSetting.clearCookie = dialogDiv.find("#clearCookie").val();
    groupSetting.maxUserCount = dialogDiv.find("#maxUserCount").val();
    groupSetting.entryPageMinCount = dialogDiv.find("#entryPageMinCount").val();
    groupSetting.entryPageMaxCount = dialogDiv.find("#entryPageMaxCount").val();
    groupSetting.pageRemainMinTime = dialogDiv.find("#pageRemainMinTime").val();
    groupSetting.pageRemainMaxTime = dialogDiv.find("#pageRemainMaxTime").val();
    groupSetting.inputDelayMinTime = dialogDiv.find("#inputDelayMinTime").val();
    groupSetting.inputDelayMaxTime = dialogDiv.find("#inputDelayMaxTime").val();
    groupSetting.slideDelayMinTime = dialogDiv.find("#slideDelayMinTime").val();
    groupSetting.slideDelayMaxTime = dialogDiv.find("#slideDelayMaxTime").val();
    groupSetting.titleRemainMinTime = dialogDiv.find("#titleRemainMinTime").val();
    groupSetting.titleRemainMaxTime = dialogDiv.find("#titleRemainMaxTime").val();
    groupSetting.optimizeKeywordCountPerIP = dialogDiv.find("#optimizeKeywordCountPerIP").val();
    groupSetting.waitTimeAfterOpenBaidu = dialogDiv.find("#waitTimeAfterOpenBaidu").val();
    groupSetting.waitTimeBeforeClick = dialogDiv.find("#waitTimeBeforeClick").val();
    groupSetting.waitTimeAfterClick = dialogDiv.find("#waitTimeAfterClick").val();

    groupSetting.oneIPOneUser = dialogDiv.find("#oneIPOneUser:checked").val() === '1' ? 1 : 0;
    groupSetting.randomlyClickNoResult = dialogDiv.find("#randomlyClickNoResult:checked").val() === '1' ? 1 : 0;
    groupSetting.justVisitSelfPage = dialogDiv.find("#justVisitSelfPage:checked").val() === '1' ? 1 : 0;
    groupSetting.sleepPer2Words = dialogDiv.find("#sleepPer2Words:checked").val() === '1' ? 1 : 0;
    groupSetting.supportPaste = dialogDiv.find("#supportPaste:checked").val() === '1' ? 1 : 0;
    groupSetting.moveRandomly = dialogDiv.find("#moveRandomly:checked").val() === '1' ? 1 : 0;
    groupSetting.parentSearchEntry = dialogDiv.find("#parentSearchEntry:checked").val() === '1' ? 1 : 0;
    groupSetting.clearLocalStorage = dialogDiv.find("#clearLocalStorage:checked").val() === '1' ? 1 : 0;
    groupSetting.lessClickAtNight = dialogDiv.find("#lessClickAtNight:checked").val() === '1' ? 1 : 0;
    groupSetting.sameCityUser = dialogDiv.find("#sameCityUser:checked").val() === '1' ? 1 : 0;
    groupSetting.locateTitlePosition = dialogDiv.find("#locateTitlePosition:checked").val() === '1' ? 1 : 0;
    groupSetting.baiduAllianceEntry = dialogDiv.find("#baiduAllianceEntry:checked").val() === '1' ? 1 : 0;
    groupSetting.justClickSpecifiedTitle = dialogDiv.find("#justClickSpecifiedTitle:checked").val() === '1' ? 1 : 0;
    groupSetting.randomlyClickMoreLink = dialogDiv.find("#randomlyClickMoreLink:checked").val() === '1' ? 1 : 0;
    groupSetting.moveUp20 = dialogDiv.find("#moveUp20:checked").val() === '1' ? 1 : 0;
    groupSetting.optimizeRelatedKeyword = dialogDiv.find("#optimizeRelatedKeyword:checked").val() === '1' ? 1 : 0;

    if(type === "update") {
        var groupSettingUuid = dialogDiv.find('#groupSettingUuid').val();
        groupSetting.uuid = groupSettingUuid;
        groupSetting.groupUuid = groupUuid;
        var gs = {};
        gs.operationType = isChecked("settingOperationType", dialogDiv);
        gs.machineUsedPercent = isChecked("machineUsedPercent", dialogDiv);
        gs.disableStatistics = isChecked("disableStatistics", dialogDiv);
        gs.disableVisitWebsite = isChecked("disableVisitWebsite", dialogDiv);
        gs.page = isChecked("page", dialogDiv);
        gs.pageSize = isChecked("pageSize", dialogDiv);
        gs.zhanneiPercent = isChecked("zhanneiPercent", dialogDiv);
        gs.zhanwaiPercent = isChecked("zhanwaiPercent", dialogDiv);
        gs.kuaizhaoPercent = isChecked("kuaizhaoPercent", dialogDiv);
        gs.baiduSemPercent = isChecked("baiduSemPercent", dialogDiv);
        gs.dragPercent = isChecked("dragPercent", dialogDiv);
        gs.specialCharPercent = isChecked("specialCharPercent", dialogDiv);

        gs.multiBrowser = isChecked("multiBrowser", dialogDiv);
        gs.clearCookie = isChecked("clearCookie", dialogDiv);
        gs.maxUserCount = isChecked("maxUserCount", dialogDiv);
        gs.entryPageMinCount = isChecked("entryPageMinCount", dialogDiv);
        gs.entryPageMaxCount = isChecked("entryPageMaxCount", dialogDiv);
        gs.pageRemainMinTime = isChecked("pageRemainMinTime", dialogDiv);
        gs.pageRemainMaxTime = isChecked("pageRemainMaxTime", dialogDiv);
        gs.inputDelayMinTime = isChecked("inputDelayMinTime", dialogDiv);
        gs.inputDelayMaxTime = isChecked("inputDelayMaxTime", dialogDiv);
        gs.slideDelayMinTime = isChecked("slideDelayMinTime", dialogDiv);
        gs.slideDelayMaxTime = isChecked("slideDelayMaxTime", dialogDiv);
        gs.titleRemainMinTime = isChecked("titleRemainMinTime", dialogDiv);
        gs.titleRemainMaxTime = isChecked("titleRemainMaxTime", dialogDiv);
        gs.optimizeKeywordCountPerIP = isChecked("optimizeKeywordCountPerIP", dialogDiv);
        gs.waitTimeAfterOpenBaidu = isChecked("waitTimeAfterOpenBaidu", dialogDiv);
        gs.waitTimeBeforeClick = isChecked("waitTimeBeforeClick", dialogDiv);
        gs.waitTimeAfterClick = isChecked("waitTimeAfterClick", dialogDiv);

        gs.oneIPOneUser = isChecked("oneIPOneUser", dialogDiv) === '1' ? 1 : 0;
        gs.randomlyClickNoResult = isChecked("randomlyClickNoResult", dialogDiv) === '1' ? 1 : 0;
        gs.justVisitSelfPage = isChecked("justVisitSelfPage", dialogDiv) === '1' ? 1 : 0;
        gs.sleepPer2Words = isChecked("sleepPer2Words", dialogDiv) === '1' ? 1 : 0;
        gs.supportPaste = isChecked("supportPaste", dialogDiv) === '1' ? 1 : 0;
        gs.moveRandomly = isChecked("moveRandomly", dialogDiv) === '1' ? 1 : 0;
        gs.parentSearchEntry = isChecked("parentSearchEntry", dialogDiv) === '1' ? 1 : 0;
        gs.clearLocalStorage = isChecked("clearLocalStorage", dialogDiv) === '1' ? 1 : 0;
        gs.lessClickAtNight = isChecked("lessClickAtNight", dialogDiv) === '1' ? 1 : 0;
        gs.sameCityUser = isChecked("sameCityUser", dialogDiv) === '1' ? 1 : 0;
        gs.locateTitlePosition = isChecked("locateTitlePosition", dialogDiv) === '1' ? 1 : 0;
        gs.baiduAllianceEntry = isChecked("baiduAllianceEntry", dialogDiv) === '1' ? 1 : 0;
        gs.justClickSpecifiedTitle = isChecked("justClickSpecifiedTitle", dialogDiv) === '1' ? 1 : 0;
        gs.randomlyClickMoreLink = isChecked("randomlyClickMoreLink", dialogDiv) === '1' ? 1 : 0;
        gs.moveUp20 = isChecked("moveUp20", dialogDiv) === '1' ? 1 : 0;
        gs.optimizeRelatedKeyword = isChecked("optimizeRelatedKeyword", dialogDiv) === '1' ? 1 : 0;

        var postData = {};
        postData.gs = gs;
        postData.groupSetting = groupSetting;
        $.ajax({
            url: '/internal/groupsetting/updateGroupSetting',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if(result){
                    if (isUpdateGroup) {
                        $().toastmessage('showSuccessToast', "更新成功");
                    } else{
                        $().toastmessage('showSuccessToast', "更新成功", true);
                    }
                }else{
                    $().toastmessage('showErrorToast', "更新失败");
                }
                $("#changeSettingDialog").dialog("close");
            },
            error: function () {
                $().toastmessage('showErrorToast', "更新失败");
                $("#changeSettingDialog").dialog("close");
            }
        });
    } else if (type === "add") {
        var groupUuid = dialogDiv.find('#groupUuid').val();
        groupSetting.groupUuid = groupUuid;
        $.ajax({
            url: '/internal/groupsetting/saveGroupSetting',
            data: JSON.stringify(groupSetting),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if(result){
                    $().toastmessage('showSuccessToast', "添加成功", true);
                }else{
                    $().toastmessage('showErrorToast', "添加失败");
                }
                $("#changeSettingDialog").dialog("close");
            },
            error: function () {
                $().toastmessage('showErrorToast', "添加失败");
                $("#changeSettingDialog").dialog("close");
            }
        });
    } else if (type === "groupadd") {
        var terminalType = $("#chargeForm").find("#terminalType").val();
        group.terminalType = terminalType;
        group.groupSetting = groupSetting;
        $.ajax({
            url: '/internal/group/saveGroup',
            data: JSON.stringify(group),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if(result){
                    $().toastmessage('showSuccessToast', "添加成功", true);
                }else{
                    $().toastmessage('showErrorToast', "添加失败");
                }
                $("#changeSettingDialog").dialog("close");
            },
            error: function () {
                $().toastmessage('showErrorToast', "添加失败");
                $("#changeSettingDialog").dialog("close");
            }
        });
    }
}

function checkItem(self) {
    var color = $(self).css("color");
    if(color == "rgb(0, 0, 0)") {
        $(self).css("color", "red");
    } else {
        $(self).css("color", "black");
    }
}

function resetTrItemColor() {
    $("tr[name='trItem']").each(function () {
        $(this).css("color", "black");
    });
    $("td[name='trItem']").each(function () {
        $(this).css("color", "black");
    });
}

function showRemainingAccount(self) {
    $(self).parent().find("label").toggle();
}

function hiddenRemainingAccount(self) {
    $(self).parent().find("label").toggle();
}

function changeRemainingAccount(self) {
    var remainAccount = $(self).parent().find("input[name='remainAccount']").val();
    var machineAccount = $(self).parent().find("input[name='currentAccount']").val();
    var totalCount = parseInt(remainAccount) + parseInt(machineAccount);
    var machineUsedPercent = $(self).parent().find("input[name='machineUsedPercent']").val();
    if (machineUsedPercent === '') {
        machineUsedPercent = '0';
    }
    var result = parseInt(totalCount) - parseInt(machineUsedPercent);
    if (result < 0) {
        alert("机器占比超出范围，请修改");
    }
    $(self).parent().find("i").text(result);
}