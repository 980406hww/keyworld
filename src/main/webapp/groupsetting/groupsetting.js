$(function () {
    $("#changeSettingDialog").dialog("close");
    $(".datalist-list li").find("div.body").each(function (index, self) {
        var listsize = $(self).attr("listsize");
        var height = listsize * 75;
        $(self).css("height", height);
    });
    loadingCheckTerminalType();
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

function showGroupDialog(groupUuid) {
    
}

function delGroup(groupUuid) {

}

function showGroupSettingDialog(type, id, groupName) {
    var changeSettingDialog = $("#changeSettingDialog");
    changeSettingDialog.find('#changeSettingDialogForm')[0].reset();
    changeSettingDialog.find('#settingGroup').val(groupName);
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
                saveGroupSetting(type);
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#changeSettingDialog").dialog("close");
                $('#changeSettingDialogForm')[0].reset();
                resetTrItemColor();
            }
        }],
        onClose: function () {
            $('#changeSettingDialogForm')[0].reset();
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

function findGroupSetting(id) {
    $.ajax({
        url: '/internal/groupsetting/findGroupSetting/' + id,
        type: 'POST',
        success: function (obj) {
            if (null === obj) {
                $().toastmessage('showErrorMessage', "获取操作类型信息失败！");
            } else {
                initSettingDialog(obj);
            }
        },
        error: function () {
            $().toastmessage('showErrorMessage', "获取操作类型信息失败！");
        }
    });
}

function initSettingDialog(groupSetting){
    var changeSettingDialogDiv = $("#changeSettingDialog");
    changeSettingDialogDiv.find("#settingOperationType").val(groupSetting.operationType != null ? groupSetting.operationType : "");
    if(groupSetting.page != null){
        changeSettingDialogDiv.find("#page").val(groupSetting.page);
    }
    if(groupSetting.pageSize != null){
        changeSettingDialogDiv.find("#pageSize").val(groupSetting.pageSize);
    }
    if(groupSetting.zhanneiPercent != null){
        changeSettingDialogDiv.find("#zhanneiPercent").val(groupSetting.zhanneiPercent);
    }
    if(groupSetting.zhanwaiPercent != null){
        changeSettingDialogDiv.find("#zhanwaiPercent").val(groupSetting.zhanwaiPercent);
    }
    if(groupSetting.kuaizhaoPercent != null){
        changeSettingDialogDiv.find("#kuaizhaoPercent").val(groupSetting.kuaizhaoPercent);
    }
    if(groupSetting.baiduSemPercent != null){
        changeSettingDialogDiv.find("#baiduSemPercent").val(groupSetting.baiduSemPercent);
    }
    if(groupSetting.specialCharPercent != null){
        changeSettingDialogDiv.find("#specialCharPercent").val(groupSetting.specialCharPercent);
    }
    if(groupSetting.dragPercent  != null){
        changeSettingDialogDiv.find("#dragPercent").val(groupSetting.dragPercent );
    }
    if(groupSetting.multiBrowser != null){
        changeSettingDialogDiv.find("#multiBrowser").val(groupSetting.multiBrowser);
    }
    if(groupSetting.clearCookie != null){
        changeSettingDialogDiv.find("#clearCookie").val(groupSetting.clearCookie);
    }
    changeSettingDialogDiv.find("#disableStatistics ").val(groupSetting.disableStatistics );
    changeSettingDialogDiv.find("#disableVisitWebsite ").val(groupSetting.disableVisitWebsite );

    changeSettingDialogDiv.find("#entryPageMinCount").val(groupSetting.entryPageMinCount);
    changeSettingDialogDiv.find("#entryPageMaxCount").val(groupSetting.entryPageMaxCount);
    changeSettingDialogDiv.find("#pageRemainMinTime").val(groupSetting.pageRemainMinTime);
    changeSettingDialogDiv.find("#pageRemainMaxTime").val(groupSetting.pageRemainMaxTime);
    changeSettingDialogDiv.find("#inputDelayMinTime").val(groupSetting.inputDelayMinTime);
    changeSettingDialogDiv.find("#inputDelayMaxTime").val(groupSetting.inputDelayMaxTime);
    changeSettingDialogDiv.find("#slideDelayMinTime").val(groupSetting.slideDelayMinTime);
    changeSettingDialogDiv.find("#slideDelayMaxTime").val(groupSetting.slideDelayMaxTime);
    changeSettingDialogDiv.find("#titleRemainMinTime").val(groupSetting.titleRemainMinTime);
    changeSettingDialogDiv.find("#titleRemainMaxTime").val(groupSetting.titleRemainMaxTime);
    changeSettingDialogDiv.find("#optimizeKeywordCountPerIP").val(groupSetting.optimizeKeywordCountPerIP);

    changeSettingDialogDiv.find("#oneIPOneUser")[0].checked = (groupSetting.oneIPOneUser == 1) ? true : false;
    changeSettingDialogDiv.find("#randomlyClickNoResult")[0].checked = (groupSetting.randomlyClickNoResult == 1) ? true : false;
    changeSettingDialogDiv.find("#justVisitSelfPage")[0].checked = (groupSetting.justVisitSelfPage == 1) ? true : false;
    changeSettingDialogDiv.find("#sleepPer2Words")[0].checked = (groupSetting.sleepPer2Words == 1) ? true : false;
    changeSettingDialogDiv.find("#supportPaste")[0].checked = (groupSetting.supportPaste == 1) ? true : false;
    changeSettingDialogDiv.find("#moveRandomly")[0].checked = (groupSetting.moveRandomly == 1) ? true : false;
    changeSettingDialogDiv.find("#parentSearchEntry")[0].checked = (groupSetting.parentSearchEntry == 1) ? true : false;
    changeSettingDialogDiv.find("#clearLocalStorage")[0].checked = (groupSetting.clearLocalStorage == 1) ? true : false;
    changeSettingDialogDiv.find("#lessClickAtNight")[0].checked = (groupSetting.lessClickAtNight == 1) ? true : false;
    changeSettingDialogDiv.find("#sameCityUser")[0].checked = (groupSetting.sameCityUser == 1) ? true : false;
    changeSettingDialogDiv.find("#locateTitlePosition")[0].checked = (groupSetting.locateTitlePosition == 1) ? true : false;
    changeSettingDialogDiv.find("#baiduAllianceEntry")[0].checked = (groupSetting.baiduAllianceEntry == 1) ? true : false;
    changeSettingDialogDiv.find("#justClickSpecifiedTitle")[0].checked = (groupSetting.justClickSpecifiedTitle == 1) ? true : false;
    changeSettingDialogDiv.find("#randomlyClickMoreLink")[0].checked = (groupSetting.randomlyClickMoreLink == 1) ? true : false;
    changeSettingDialogDiv.find("#moveUp20")[0].checked = (groupSetting.moveUp20 == 1) ? true : false;
    changeSettingDialogDiv.find("#optimizeRelatedKeyword")[0].checked = (groupSetting.optimizeRelatedKeyword == 1) ? true : false;

    changeSettingDialogDiv.find("#waitTimeAfterOpenBaidu").val(groupSetting.waitTimeAfterOpenBaidu);
    changeSettingDialogDiv.find("#waitTimeBeforeClick").val(groupSetting.waitTimeBeforeClick);
    changeSettingDialogDiv.find("#waitTimeAfterClick").val(groupSetting.waitTimeAfterClick);
    changeSettingDialogDiv.find("#maxUserCount").val(groupSetting.maxUserCount);

    changeSettingDialogDiv.show();
}

function isChecked(id) {
    var color = $("#changeSettingDialog").find("#" + id).parent().css("color");
    if(color == "rgb(255, 0, 0)") {
        return "1";
    } else {
        return "0";
    }
}

function saveGroupSetting(type){
    var changeSettingDialogDiv = $("#changeSettingDialog");
    var groupSetting = {};
    groupSetting.operationType = changeSettingDialogDiv.find("#settingOperationType").val();
    groupSetting.disableStatistics = changeSettingDialogDiv.find("#disableStatistics").val();
    groupSetting.disableVisitWebsite = changeSettingDialogDiv.find("#disableVisitWebsite").val();
    groupSetting.page = changeSettingDialogDiv.find("#page").val();
    groupSetting.pageSize = changeSettingDialogDiv.find("#pageSize").val();
    groupSetting.zhanneiPercent = changeSettingDialogDiv.find("#zhanneiPercent").val();
    groupSetting.zhanwaiPercent = changeSettingDialogDiv.find("#zhanwaiPercent").val();
    groupSetting.kuaizhaoPercent = changeSettingDialogDiv.find("#kuaizhaoPercent").val();
    groupSetting.baiduSemPercent = changeSettingDialogDiv.find("#baiduSemPercent").val();
    groupSetting.dragPercent = changeSettingDialogDiv.find("#dragPercent").val();
    groupSetting.specialCharPercent = changeSettingDialogDiv.find("#specialCharPercent").val();

    groupSetting.multiBrowser = changeSettingDialogDiv.find("#multiBrowser").val();
    groupSetting.clearCookie = changeSettingDialogDiv.find("#clearCookie").val();
    groupSetting.maxUserCount = changeSettingDialogDiv.find("#maxUserCount").val();
    groupSetting.entryPageMinCount = changeSettingDialogDiv.find("#entryPageMinCount").val();
    groupSetting.entryPageMaxCount = changeSettingDialogDiv.find("#entryPageMaxCount").val();
    groupSetting.pageRemainMinTime = changeSettingDialogDiv.find("#pageRemainMinTime").val();
    groupSetting.pageRemainMaxTime = changeSettingDialogDiv.find("#pageRemainMaxTime").val();
    groupSetting.inputDelayMinTime = changeSettingDialogDiv.find("#inputDelayMinTime").val();
    groupSetting.inputDelayMaxTime = changeSettingDialogDiv.find("#inputDelayMaxTime").val();
    groupSetting.slideDelayMinTime = changeSettingDialogDiv.find("#slideDelayMinTime").val();
    groupSetting.slideDelayMaxTime = changeSettingDialogDiv.find("#slideDelayMaxTime").val();
    groupSetting.titleRemainMinTime = changeSettingDialogDiv.find("#titleRemainMinTime").val();
    groupSetting.titleRemainMaxTime = changeSettingDialogDiv.find("#titleRemainMaxTime").val();
    groupSetting.optimizeKeywordCountPerIP = changeSettingDialogDiv.find("#optimizeKeywordCountPerIP").val();
    groupSetting.waitTimeAfterOpenBaidu = changeSettingDialogDiv.find("#waitTimeAfterOpenBaidu").val();
    groupSetting.waitTimeBeforeClick = changeSettingDialogDiv.find("#waitTimeBeforeClick").val();
    groupSetting.waitTimeAfterClick = changeSettingDialogDiv.find("#waitTimeAfterClick").val();

    groupSetting.oneIPOneUser = changeSettingDialogDiv.find("#oneIPOneUser:checked").val() === '1' ? 1 : 0;
    groupSetting.randomlyClickNoResult = changeSettingDialogDiv.find("#randomlyClickNoResult:checked").val() === '1' ? 1 : 0;
    groupSetting.justVisitSelfPage = changeSettingDialogDiv.find("#justVisitSelfPage:checked").val() === '1' ? 1 : 0;
    groupSetting.sleepPer2Words = changeSettingDialogDiv.find("#sleepPer2Words:checked").val() === '1' ? 1 : 0;
    groupSetting.supportPaste = changeSettingDialogDiv.find("#supportPaste:checked").val() === '1' ? 1 : 0;
    groupSetting.moveRandomly = changeSettingDialogDiv.find("#moveRandomly:checked").val() === '1' ? 1 : 0;
    groupSetting.parentSearchEntry = changeSettingDialogDiv.find("#parentSearchEntry:checked").val() === '1' ? 1 : 0;
    groupSetting.clearLocalStorage = changeSettingDialogDiv.find("#clearLocalStorage:checked").val() === '1' ? 1 : 0;
    groupSetting.lessClickAtNight = changeSettingDialogDiv.find("#lessClickAtNight:checked").val() === '1' ? 1 : 0;
    groupSetting.sameCityUser = changeSettingDialogDiv.find("#sameCityUser:checked").val() === '1' ? 1 : 0;
    groupSetting.locateTitlePosition = changeSettingDialogDiv.find("#locateTitlePosition:checked").val() === '1' ? 1 : 0;
    groupSetting.baiduAllianceEntry = changeSettingDialogDiv.find("#baiduAllianceEntry:checked").val() === '1' ? 1 : 0;
    groupSetting.justClickSpecifiedTitle = changeSettingDialogDiv.find("#justClickSpecifiedTitle:checked").val() === '1' ? 1 : 0;
    groupSetting.randomlyClickMoreLink = changeSettingDialogDiv.find("#randomlyClickMoreLink:checked").val() === '1' ? 1 : 0;
    groupSetting.moveUp20 = changeSettingDialogDiv.find("#moveUp20:checked").val() === '1' ? 1 : 0;
    groupSetting.optimizeRelatedKeyword = changeSettingDialogDiv.find("#optimizeRelatedKeyword:checked").val() === '1' ? 1 : 0;

    if(type === "update") {
        var groupSettingUuid = changeSettingDialogDiv.find('#groupSettingUuid').val();
        groupSetting.uuid = groupSettingUuid;
        var gs = {};
        gs.operationType = isChecked("settingOperationType");
        gs.disableStatistics = isChecked("disableStatistics");
        gs.disableVisitWebsite = isChecked("disableVisitWebsite");
        gs.page = isChecked("page");
        gs.pageSize = isChecked("pageSize");
        gs.zhanneiPercent = isChecked("zhanneiPercent");
        gs.zhanwaiPercent = isChecked("zhanwaiPercent");
        gs.kuaizhaoPercent = isChecked("kuaizhaoPercent");
        gs.baiduSemPercent = isChecked("baiduSemPercent");
        gs.dragPercent = isChecked("dragPercent");
        gs.specialCharPercent = isChecked("specialCharPercent");

        gs.multiBrowser = isChecked("multiBrowser");
        gs.clearCookie = isChecked("clearCookie");
        gs.maxUserCount = isChecked("maxUserCount");
        gs.entryPageMinCount = isChecked("entryPageMinCount");
        gs.entryPageMaxCount = isChecked("entryPageMaxCount");
        gs.pageRemainMinTime = isChecked("pageRemainMinTime");
        gs.pageRemainMaxTime = isChecked("pageRemainMaxTime");
        gs.inputDelayMinTime = isChecked("inputDelayMinTime");
        gs.inputDelayMaxTime = isChecked("inputDelayMaxTime");
        gs.slideDelayMinTime = isChecked("slideDelayMinTime");
        gs.slideDelayMaxTime = isChecked("slideDelayMaxTime");
        gs.titleRemainMinTime = isChecked("titleRemainMinTime");
        gs.titleRemainMaxTime = isChecked("titleRemainMaxTime");
        gs.optimizeKeywordCountPerIP = isChecked("optimizeKeywordCountPerIP");
        gs.waitTimeAfterOpenBaidu = isChecked("waitTimeAfterOpenBaidu");
        gs.waitTimeBeforeClick = isChecked("waitTimeBeforeClick");
        gs.waitTimeAfterClick = isChecked("waitTimeAfterClick");

        gs.oneIPOneUser = isChecked("oneIPOneUser") === '1' ? 1 : 0;
        gs.randomlyClickNoResult = isChecked("randomlyClickNoResult") === '1' ? 1 : 0;
        gs.justVisitSelfPage = isChecked("justVisitSelfPage") === '1' ? 1 : 0;
        gs.sleepPer2Words = isChecked("sleepPer2Words") === '1' ? 1 : 0;
        gs.supportPaste = isChecked("supportPaste") === '1' ? 1 : 0;
        gs.moveRandomly = isChecked("moveRandomly") === '1' ? 1 : 0;
        gs.parentSearchEntry = isChecked("parentSearchEntry") === '1' ? 1 : 0;
        gs.clearLocalStorage = isChecked("clearLocalStorage") === '1' ? 1 : 0;
        gs.lessClickAtNight = isChecked("lessClickAtNight") === '1' ? 1 : 0;
        gs.sameCityUser = isChecked("sameCityUser") === '1' ? 1 : 0;
        gs.locateTitlePosition = isChecked("locateTitlePosition") === '1' ? 1 : 0;
        gs.baiduAllianceEntry = isChecked("baiduAllianceEntry") === '1' ? 1 : 0;
        gs.justClickSpecifiedTitle = isChecked("justClickSpecifiedTitle") === '1' ? 1 : 0;
        gs.randomlyClickMoreLink = isChecked("randomlyClickMoreLink") === '1' ? 1 : 0;
        gs.moveUp20 = isChecked("moveUp20") === '1' ? 1 : 0;
        gs.optimizeRelatedKeyword = isChecked("optimizeRelatedKeyword") === '1' ? 1 : 0;

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
                    $().toastmessage('showSuccessToast', "更新成功", true);
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
        var groupUuid = changeSettingDialogDiv.find('#groupUuid').val();
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