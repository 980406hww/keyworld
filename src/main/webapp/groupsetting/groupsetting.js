$(function () {
    $("#changeSettingDialog").dialog("close");
    $("#updateGroupSettingDialog").dialog("close");
    $("#getAvailableOptimizationGroups").dialog("close");
    $("#showGroupQueueDialog").dialog("close");
    $("#uselessOptimizationGroupDialog").dialog("close");

    var searchCustomerForm = $("#chargeForm");
    var pageSize = searchCustomerForm.find('#pageSizeHidden').val();
    var pages = searchCustomerForm.find('#pagesHidden').val();
    $("#chargeForm").find("#status").val($("#statusHidden").val());
    var currentPageNumber = searchCustomerForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    showCustomerBottomDiv.find('#chooseRecords').val(pageSize);
    showCustomerBottomDiv.find('#pagesHidden').val(pages);

    if(parseInt(currentPageNumber) > 1 && parseInt(currentPageNumber) < parseInt(pages)) {
        showCustomerBottomDiv.find("#firstButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#upButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#nextButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#lastButton").removeAttr("disabled");
    } else if (parseInt(pages) <= 1) {
        showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
    } else if (parseInt(currentPageNumber) <= 1) {
        showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
    } else {
        showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
    }

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
    if (a[0] !== undefined && a[0].innerHTML === terminalType) {
        return;
    }
    $(".mytabs .link").find("li").removeClass("active");
    $(".mytabs .link").find("li[name='"+ terminalType +"']").addClass("active");
    $("#chargeForm").find("#terminalType").val($.trim(terminalType));

    if (isManualSwitch) {
        trimSearchCondition();
    }
    setTimeout(function (){
        getGroupNames();
    }, 200);
}

function enterIn(e) {
    var e = e || event,
        keyCode = e.which || e.keyCode;
    if (keyCode === 13) {
        trimSearchCondition();
    }
}

function checkHasOperation() {
    $(".mytabs div:eq(0)").find("input:checkbox").click(function () {
        var parentName = $(this).parent().attr("name");
        var hasOperation, hasRemainingAccount;
        if (!$(this).prop("checked")) {
            hasOperation = null;
            hasRemainingAccount = null;
        } else {
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

function changeOperationType() {
    $("div.conn label[name='hasOperation'] > input").attr("checked", true);
    $("div.conn label[name='noOperation'] > input").attr("checked", false);
    $("div.conn label[name='hasRemainingAccount'] > input").attr("checked", false);
    $("#chargeForm").find("#hasOperation").val(true);
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
    var operationCombineName = $(".conn").find("li.operationCombineName input[name='operationCombineName']").val();
    var optimizedGroupName = $(".conn").find("li.optimizedGroupName input[name='optimizedGroupName']").val();
    var operationType = $(".conn").find("select[name='operationType']").val();
    if (currentPage !== '1') {
        chargeForm.find("#currentPageNumberHidden").val(1);
    }
    if (operationCombineName !== "") {
        chargeForm.find("#operationCombineName").val($.trim(operationCombineName));
    } else {
        chargeForm.find("#operationCombineName").val(null);
    }
    if (optimizedGroupName !== "") {
        chargeForm.find("#optimizedGroupName").val($.trim(optimizedGroupName));
    } else {
        chargeForm.find("#optimizedGroupName").val(null);
    }
    if (operationType !== ""){
        chargeForm.find("#operationType").val($.trim(operationType));
    } else {
        chargeForm.find("#operationType").val(null);
    }
    chargeForm.submit();
}

function showOperationCombineDialog() {
    var changeSettingDialog = $("#changeSettingDialog");
    changeSettingDialog.find('#changeSettingDialogForm')[0].reset();
    changeSettingDialog.find("#settingOperationCombineName").removeAttr("disabled");
    changeSettingDialog.find("#settingGroup").removeAttr("disabled");
    changeSettingDialog.find("i").text(100);
    changeSettingDialog.find("#maxInvalidCount").removeAttr("disabled");
    changeSettingDialog.show();
    changeSettingDialog.dialog({
        resizable: false,
        width: 500,
        maxHeight: 550,
        title: "新增操作组合(只需指定一个操作类型, 如果还有其他操作类型, 后续添加即可)",
        modal: true,
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
                $("#changeSettingDialogForm")[0].reset();
                $("#changeSettingDialog").find("i").text(100);
                resetTrItemColor();
                changeSettingDialog.find("#maxInvalidCount").attr("disabled", true);
            }
        }],
        onClose: function () {
            $('#changeSettingDialogForm')[0].reset();
            $("#changeSettingDialog").find("i").text(100);
            resetTrItemColor();
            changeSettingDialog.find("#maxInvalidCount").attr("disabled", true);
        }
    });
    changeSettingDialog.dialog("open");
    changeSettingDialog.window("resize", {
        top: $(document).scrollTop() + 150,
        left: $(document).scrollLeft() + $(window).width() / 2 - 250
    });
}

function showUpdateGroupDialog(operationCombineUuid, operationCombineName) {
    var updateGroupSettingDialog = $("#updateGroupSettingDialog");
    updateGroupSettingDialog.find('#updateGroupSettingDialogForm')[0].reset();
    getGroupSettingCount(operationCombineUuid, operationCombineName);
    updateGroupSettingDialog.show();
    updateGroupSettingDialog.dialog({
        resizable: false,
        width: 500,
        maxHeight: 350,
        title: "批量修改优化组设置(需要修改的信息请标红!!!)",
        modal: false,
        buttons: [{
            text: '修改',
            iconCls: 'icon-ok',
            handler: function () {
                saveGroupSetting('update', 0, 1, operationCombineUuid);
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#updateGroupSettingDialog").dialog("close");
            }
        }],
        onClose: function () {
            $('#updateGroupSettingDialogForm')[0].reset();
            resetTrItemColor();
        }
    });
    updateGroupSettingDialog.dialog("open");
    updateGroupSettingDialog.window("resize", {
        top: $(document).scrollTop() + 150,
        left: $(document).scrollLeft() + $(window).width() / 2 - 250
    });
}

function delOperationCombine(operationCombineUuid) {
    checkGroupNames(operationCombineUuid, function (flag) {
        if (flag) {
            parent.$.messager.confirm('询问', "确定删除此操作组合吗?", function (b) {
                if (b) {
                    $.ajax({
                        url: '/internal/operationCombine/delOperationCombine/' + operationCombineUuid,
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
            });
        } else {
            $().toastmessage('showErrorToast', "请先将要删除的操作组合下的分组移除！！！");
        }
    });
}

function checkGroupNames(uuid, callback) {
    var flag = true;
    $.ajax({
        url: '/internal/operationCombine/getGroupNames/' + uuid,
        type: 'POST',
        success: function (data) {
            if (JSON.parse(data).length > 0) {
                flag = false;
            }
            callback(flag);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取操作组合下的分组数据失败！！");
        }
    });
}

function getGroupSettingCount(operationCombineUuid, operationCombineName) {
    $.ajax({
        url: '/internal/groupsetting/getGroupSettingCount/' + operationCombineUuid,
        type: 'POST',
        success: function (result) {
            if(null !== result) {
                $("#updateGroupSettingDialog").find("strong").text(result);
                $("#updateGroupSettingDialog").find("#settingOperationCombineName").val(operationCombineName != null ? operationCombineName : "");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取操作数失败");
        }
    });
}

/*
type: 操作类型 增加还是修改 id: 操作设置id或者操作组合id  operationCombineName:操作组合名称
remainingAccount: 剩余百分比   operationCombineUuid: 操作组合id
 */
function showGroupSettingDialog(liIndex, type, id, operationCombineName, remainingAccount, operationCombineUuid) {
    var changeSettingDialog = $("#changeSettingDialog");
    changeSettingDialog.find('#changeSettingDialogForm')[0].reset();
    if (type === "add") {
        $("#changeSettingDialog").find("#settingOperationCombineName").attr("disabled", true);
    }
    $("#changeSettingDialog").find("#settingGroup").attr("disabled", true);
    changeSettingDialog.find('#settingOperationCombineName').val(operationCombineName);
    changeSettingDialog.find('#remainAccount').val(remainingAccount);
    changeSettingDialog.find("i").text(remainingAccount);
    var title;
    if (type === "add") {
        title = "新增操作类型";
        changeSettingDialog.find('#operationCombineUuid').val(operationCombineUuid);
    } else if (type === "update") {
        title = "修改操作类型 (需要修改的信息请标红!!!)";
        changeSettingDialog.find('#groupSettingUuid').val(id);
        findGroupSetting(id);
    }
    var maxInvalidCount = $("#li_" + liIndex).find("div.header .maxInvalidCountStr").text();
    changeSettingDialog.find("#maxInvalidCount").val(maxInvalidCount);
    changeSettingDialog.show();
    changeSettingDialog.dialog({
        resizable: false,
        width: 500,
        maxHeight: 350,
        title: title,
        modal: false,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveGroupSetting(type, 0, 0, operationCombineUuid);
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
    changeSettingDialog.window("resize", {
        top: $(document).scrollTop() + 150,
        left: $(document).scrollLeft() + $(window).width() / 2 - 250
    });
}

function delGroupSetting(self, operationTypeUuid) {
    var operationSpans = $($(self).parents()[6]).find("span.operation");
    if (operationSpans.length === 1) {
        $.messager.alert('提示', '这是最后一个优化组设置，请直接删除操作组合！！！', 'warning');
        return false;
    }
    parent.$.messager.confirm('询问', "确定删除此操作类型?", function (b) {
        if (b) {
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
    });
}

function findGroupSetting(id, status) {
    $.ajax({
        url: '/internal/groupsetting/findGroupSetting/' + id,
        type: 'POST',
        success: function (obj) {
            if (null === obj) {
                $().toastmessage('showErrorToast', "获取操作类型信息失败！");
            } else {
                initSettingDialog(obj, status);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取操作类型信息失败！");
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
    if(groupSetting.clearCookie != null){
        dialogDiv.find("#clearCookie").val(groupSetting.clearCookie);
    }
    dialogDiv.find("#disableStatistics").val(groupSetting.disableStatistics);
    dialogDiv.find("#disableVisitWebsite").val(groupSetting.disableVisitWebsite);

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

    dialogDiv.find("#randomlyClickNoResult")[0].checked = groupSetting.randomlyClickNoResult == 1 ? true : false;
    dialogDiv.show();
}

function isChecked(id, dialogDiv) {
    var color = $(dialogDiv).find("#" + id).parent().css("color");
    if(color === "rgb(255, 0, 0)") {
        return "1";
    } else {
        return "0";
    }
}

/*
type: 控制方式 status: 分组分割 isBatchUpdate: 批量修改标志 operationCombineUuid: 操作组合uuid
 */
function saveGroupSetting(type, status, isBatchUpdate, operationCombineUuid){
    var dialogDiv;
    if (isBatchUpdate === 1) {
        dialogDiv = $("#updateGroupSettingDialog");
    } else {
        dialogDiv = $("#changeSettingDialog");
    }
    var operationCombine = {};
    var operationCombineName = dialogDiv.find("#settingOperationCombineName").val().trim();
    if (operationCombineName === "") {
        $.messager.alert('提示', '请输入操作组合名', 'warning');
        dialogDiv.find("#settingOperationCombineName")[0].focus();
        return false;
    }
    if (status) {
        operationCombine.operationCombineName = operationCombineName;
        var groupNames = dialogDiv.find("#settingGroup").val().trim();
        if (groupNames === "") {
            $.messager.alert('提示', '请输入分组名', 'warning');
            dialogDiv.find("#settingGroup")[0].focus();
            return false;
        }
        groupNames = groupNames.replace(/[，|\r\n]/g, ',').replace(/[\s+]/g, '');
        if (groupNames.substring(groupNames.length - 1) === ',') {
            groupNames = groupNames.substring(0, groupNames.length - 1);
        }
        var groupNameArr = groupNames.split(',');
        groupNameArr = groupNameArr.filter(function (groupName, index) {
            return groupNameArr.indexOf(groupName) === index && groupName !== '';
        });
        if (groupNameArr.length > 0) {
            operationCombine.groupNames = groupNameArr;
        }
        type = "addOperationCombine";
        if (dialogDiv.find("#machineUsedPercent").val() === '0' || dialogDiv.find("#machineUsedPercent").val() === '') {
            $.messager.alert('提示', '每个操作的机器占比都应该大于0！！！', 'warning');
            return false;
        }
    }
    var groupSetting = {};
    var operationType = dialogDiv.find("#settingOperationType").val();
    if (isBatchUpdate === 0 && operationType === '') {
        $.messager.alert('提示', '请选择操作类型！！！', 'warning');
        return false;
    }
    var remainingAccount = dialogDiv.find("#machineUsedPercent").parent().find("i").text();
    var hasChangedZhanbi = true;
    if (isBatchUpdate === 1) {
        if (isChecked("machineUsedPercent", dialogDiv) !== "1") {
            hasChangedZhanbi = false;
        }
    }
    if (hasChangedZhanbi) {
        if (remainingAccount < 0 || remainingAccount >= 100) {
            $.messager.alert('提示', '机器占比不合理，请修改！！！', 'warning');
            return false;
        }
    }
    groupSetting.operationType = operationType;
    groupSetting.machineUsedPercent = parseInt(dialogDiv.find("#machineUsedPercent").val() === '' ? 0 : dialogDiv.find("#machineUsedPercent").val());
    groupSetting.remainingAccount = remainingAccount;
    groupSetting.disableStatistics = dialogDiv.find("#disableStatistics").val();
    groupSetting.disableVisitWebsite = dialogDiv.find("#disableVisitWebsite").val();
    groupSetting.page = dialogDiv.find("#page").val();
    groupSetting.pageSize = dialogDiv.find("#pageSize").val();

    groupSetting.clearCookie = dialogDiv.find("#clearCookie").val();
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
    groupSetting.maxInvalidCount = dialogDiv.find("#maxInvalidCount").val();
    groupSetting.randomlyClickNoResult = dialogDiv.find("#randomlyClickNoResult:checked").val() === '1' ? 1 : 0;

    if(type === "update") {
        groupSetting.uuid = dialogDiv.find('#groupSettingUuid').val();
        groupSetting.operationCombineUuid = operationCombineUuid;
        groupSetting.operationCombineName = operationCombineName;
        var gs = {};
        gs.operationType = isChecked("settingOperationType", dialogDiv);
        if (isChecked("machineUsedPercent", dialogDiv) === "1") {
            if (groupSetting.machineUsedPercent === 0){
                $.messager.alert('提示', '每个操作的机器占比都应该大于0！！！', 'warning');
                return false;
            }
        }
        gs.machineUsedPercent = isChecked("machineUsedPercent", dialogDiv);
        gs.disableStatistics = isChecked("disableStatistics", dialogDiv);
        gs.disableVisitWebsite = isChecked("disableVisitWebsite", dialogDiv);
        gs.page = isChecked("page", dialogDiv);
        gs.pageSize = isChecked("pageSize", dialogDiv);

        gs.clearCookie = isChecked("clearCookie", dialogDiv);
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
        gs.maxInvalidCount = isChecked("maxInvalidCount", dialogDiv);
        gs.randomlyClickNoResult = isChecked("randomlyClickNoResult", dialogDiv) === '1' ? 1 : 0;
        gs.operationCombineName = isChecked("settingOperationCombineName", dialogDiv) === '1' ? 1 : 0;

        var postData = {};
        postData.gs = gs;
        postData.groupSetting = groupSetting;
        if (isBatchUpdate === 1) { // 批量修改
            $.ajax({
                url: '/internal/operationCombine/updateOperationCombine/' + operationCombineUuid,
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                type: 'POST',
                success: function (result) {
                    if(result){
                        $().toastmessage('showSuccessToast', "更新成功", true);
                    } else {
                        $().toastmessage('showErrorToast', "更新失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "更新失败");
                }
            });
        } else { // 单个修改
            $.ajax({
                url: '/internal/groupsetting/updateGroupSetting',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                type: 'POST',
                success: function (result) {
                    if(result){
                        $().toastmessage('showSuccessToast', "更新成功", true);
                    }else{
                        $().toastmessage('showErrorToast', "更新失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "更新失败");

                }
            });
        }
    } else if (type === "add") { // 增加单个操作设置
        if (groupSetting.machineUsedPercent === 0) {
            $.messager.alert('提示', '每个操作的机器占比都应该大于0！！！', 'warning');
            return false;
        }
        groupSetting.operationCombineUuid = operationCombineUuid;
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
            },
            error: function () {
                $().toastmessage('showErrorToast', "添加失败");
            }
        });
    } else if (type === "addOperationCombine") { // 增加操作组合
        operationCombine.terminalType = $("#chargeForm").find("#terminalType").val();
        operationCombine.groupSetting = groupSetting;
        var maxInvalidCount = dialogDiv.find("#maxInvalidCount").val();
        operationCombine.maxInvalidCount = maxInvalidCount === "" ? 8 : maxInvalidCount;
        $.ajax({
            url: '/internal/operationCombine/saveOperationCombine',
            data: JSON.stringify(operationCombine),
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
            },
            error: function () {
                $().toastmessage('showErrorToast', "添加失败");
            }
        });
    }
    $(dialogDiv).dialog("close");
}

function checkItem(self) {
    var color = $(self).css("color");
    if(color === "rgb(0, 0, 0)") {
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
    var count = $(self).parent().find("strong").text();
    if (count === '' || count === undefined) {
        count = 1;
    }
    var result = parseInt(totalCount) - parseInt(machineUsedPercent) * parseInt(count);
    if (result < 0 || result >= 100) {
        $().toastmessage("showErrorToast", "机器占比不合理，请修改！！！");
    }
    $(self).parent().find("i").text(result);
}

function getAvailableOptimizationGroups() {
    $("#getAvailableOptimizationGroups tbody tr").remove();
    $("#getAvailableOptimizationGroups").show();
    $("#getAvailableOptimizationGroups").dialog({
        resizable: false,
        height: 450,
        width: 240,
        title: '查询需要添加的优化组',
        modal: true,
        buttons: [{
            text: '批量添加优化组',
            iconCls: 'icon-ok',
            handler: function(){
                batchAddGroups();
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#getAvailableOptimizationGroups").dialog("close");
            }
        }]
    });
    $("#getAvailableOptimizationGroups").dialog("open");
    $("#getAvailableOptimizationGroups").window("resize",{top:$(document).scrollTop() + 100});
}

function getSelectedIDs(inputName) {
    var uuids = '';
    $.each($("input[name='"+ inputName +"']:checkbox:checked"), function(){
        if(uuids === ''){
            uuids = $(this).val();
        }else{
            uuids = uuids + "," + $(this).val();
        }
    });
    return uuids;
}

function selectAllChecked(self, elementName) {
    var a = document.getElementsByName(elementName);
    if (self.checked) {
        for (var i = 0; i < a.length; i++) {
            a[i].checked = true;
        }
    } else {
        for (var i = 0; i < a.length; i++) {
            a[i].checked = false;
        }
    }
}

function batchAddGroups() {
    var postData = {};
    postData.groupNames = [];
    var operationCombineName = $("#getAvailableOptimizationGroups").find("select[name='operationCombineName']").val();
    postData.operationCombineUuid = operationCombineName.split('_____')[1];
    var a = document.getElementsByName("checkOptimizationGroup");
    for (var i = 0; i < a.length; i++) {
        if (a[i].checked === true) {
            postData.groupNames.push($(a[i]).parent().parent().find("input[name='optimizationGroup']").val());
        }
    }
    if (postData.groupNames.length === 0) {
        $().toastmessage("showErrorToast", "请选择需要添加的优化组！！！");
        return false;
    }
    postData.onlySaveStatus = true;
    $.ajax({
        url: '/internal/group/batchAddGroups',
        headers: {
            "Accept": 'application/json',
            "Content-Type": 'application/json'
        },
        type: 'POST',
        data: JSON.stringify(postData),
        success: function (result) {
            if (result) {
                $().toastmessage("showSuccessToast", "批量添加分组成功！！！", true);
            } else {
                $().toastmessage("showErrorToast", "批量添加分组失败！！！")
            }
        },
        error: function () {
            $().toastmessage("showErrorToast", "批量添加分组失败！！！")
        }
    });
    $("#getAvailableOptimizationGroups").dialog("close");
}

function searchAvailableOptimizationGroups() {
    $("#getAvailableOptimizationGroups").find("table tbody").empty();
    var terminalType = $("#chargeForm").find("#terminalType").val();
    var optimizedGroupNameSearchSource = $("#getAvailableOptimizationGroups").find('input:radio[name="sourceRadio"]:checked').val();
    if (optimizedGroupNameSearchSource == null){
        $.messager.alert('提示', '请选择组名来源!!!', 'warning');
        return false;
    }
    var optimizedGroupName = $("#getAvailableOptimizationGroups").find($("input[name='optimizedGroupName']")).val();
    var groupSetting = {};
    groupSetting.terminalType = terminalType;
    groupSetting.optimizedGroupNameSearchSource = optimizedGroupNameSearchSource;
    groupSetting.optimizedGroupName = optimizedGroupName;
    $.ajax({
        url: '/internal/group/getAvailableOptimizationGroups',
        headers: {
            "Accept": 'application/json',
            "Content-Type": 'application/json'
        },
        type: 'POST',
        data: JSON.stringify(groupSetting),
        success: function (result) {
            if (result !== null) {
                $().toastmessage("showSuccessToast", "查询成功！！！");
                var tbody = $("#getAvailableOptimizationGroups").find("table tbody");
                $.each(result, function(index, element) {
                    tbody.append("<tr>" +
                        "<td width='20px'  style=\"text-align: center;\"><input type='checkbox' name='checkOptimizationGroup' checked='checked'></td>" +
                        "<td width='208px'><input type='text' style='width: 200px;' name='optimizationGroup' disabled='disabled' value='"+ element +"'/></td>" +
                        "</tr>");
                });
            }
        },
        error: function () {
            $().toastmessage("showErrorToast", "查询失败！！！");
        }
    });
}

function editMaxInvalidCountStr(self, edit){
    if (edit) {
        var uuid = $(self).parent().parent().find("input[name='operationCombineUuid']").val();
        self.innerHTML = '<input type="text" value="' + self.innerHTML + '" style="width: 40px;" label="' + self.innerHTML + '" operationCombineId= "'+ uuid +'" onBlur="editMaxInvalidCountStr(this)" />';
        self.getElementsByTagName('input')[0].focus();
    } else {
        var isChange = true;
        var label = $(self).attr("label");
        if ($.trim(self.value) === $.trim(label)) {
            isChange = false;
        }
        if (isChange) {
            var groupSetting = {};
            groupSetting.uuid = $(self).attr("operationCombineId");
            groupSetting.maxInvalidCount = $.trim(self.value);
            console.log(groupSetting);
            $.ajax({
                url: "/internal/operationCombine/updateMaxInvalidCount",
                type: "POST",
                data: JSON.stringify(groupSetting),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "保存成功！", true);
                    } else {
                        $().toastmessage('showErrorToast', "保存失败！");
                        self.value = $.trim(label);
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "保存失败！");
                    self.value = $.trim(label);
                }
            });
        }
        setTimeout(function(){
            if ($.trim(self.value) == "") {
                self.value = "暂无";
            }
            self.parentNode.innerHTML = $.trim(self.value);
        }, 100);
    }
}

function getGroupNames() {
    $(".datalist ul li").each(function () {
        var li = $(this);
        var uuid = li.find("input[name='operationCombineUuid']").val();
        $.ajax({
            url: '/internal/operationCombine/getGroupNames/' + uuid,
            type: 'POST',
            success: function (data) {
                if (data != null) {
                    var groupNameStr = "";
                    var groupNameArr = JSON.parse(data);
                    if (groupNameArr.length === 0) {
                        groupNameStr = '暂无';
                    } else {
                        $.each(groupNameArr, function (idx, value) {
                            groupNameStr += value + ",";
                        });
                        groupNameStr = groupNameStr.substring(0, groupNameStr.length-1);
                    }
                    $(li).find(".header span.groupNames").find("label.groupNameStr").html(groupNameStr);
                } else {
                    $().toastmessage('showErrorToast', "获取操作组合下的分组数据失败！！");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "获取操作组合下的分组数据失败！！");
            }
        });
    });
}

function editGroupNameStr(o, edit, maxInvalidCount){
    if (edit) {
        o.innerHTML = o.innerHTML.replace(/(暂无)/g, '');
        var uuid = $(o).parent().parent().find("input[name='operationCombineUuid']").val();
        o.innerHTML = '<input type="text" label="'+ o.innerHTML +'" uuid="'+ uuid +'" style="width: 800px;" value="' + o.innerHTML + '" onblur="editGroupNameStr(this)">';
        o.getElementsByTagName('input')[0].focus();
    } else {
        var isChange = true;
        var groups = [];
        var groupNames = o.value.replace(/( )+/g,"").replace(/(，)+|(,)+/g, ",").split(",");
        groupNames = unique(groupNames);
        if (o.value !== "") {
            o.value = "";
            $.each(groupNames, function (idx, val) {
                if (val !== "") {
                    groups.push($.trim(val));
                    o.value += val + ",";
                }
            });
            o.value = o.value.substring(0, o.value.length-1);
        }
        var label = $(o).attr("label");
        if ($.trim(o.value) === $.trim(label)) {
            isChange = false;
        }
        if (isChange) {
            var postData = {};
            var operationCombineUuid = $(o).attr("uuid");
            postData.operationCombineUuid = $.trim(operationCombineUuid);
            postData.groupNames = groups;
            postData.terminalType = $("#chargeForm").find("input[name='terminalType']").val();
            postData.maxInvalidCount = maxInvalidCount;
            $.ajax({
                url: "/internal/group/saveGroupsBelowOperationCombine",
                type: "POST",
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "保存成功！", true);
                    } else {
                        $().toastmessage('showErrorToast', "保存失败！");
                        o.value = $.trim(label);
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "保存失败！");
                    o.value = $.trim(label);
                }
            });
        }
        setTimeout(function(){
            if ($.trim(o.value) === "") {
                o.value = "暂无";
            }
            o.parentNode.innerHTML = $.trim(o.value);
        }, 100);
    }
}

function unique(a) {
    var seen = {};
    return a.filter(function(item) {
        return seen.hasOwnProperty(item) ? false : (seen[item] = true);
    });
}

function showGroupQueueDialog(operationCombineUuid, maxInvalidCount) {
    var showGroupQueueDialog = $("#showGroupQueueDialog");
    showGroupQueueDialog.find("input[name='operationCombineUuid']").val(operationCombineUuid);
    showGroupQueueDialog.show();
    showGroupQueueDialog.dialog({
        resizable: false,
        width: 250,
        height: 550,
        title: '分组详情',
        modal: false,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveGroupsBelowOperationCombine(operationCombineUuid, maxInvalidCount);
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#showGroupQueueDialog").dialog('close');
                $('#showGroupQueueForm')[0].reset();
            }
        }],
        onClose: function () {
            $('#showGroupQueueForm')[0].reset();
            $('#showGroupQueueForm tbody tr').remove();
        }
    });
    showGroupQueueDialog.dialog("open");
    showGroupQueueDialog.window("resize", {
        top: $(document).scrollTop() + 150,
        left: $(document).scrollLeft() + $(window).width() / 2 - 125
    });
}

function searchGroupsBelowOperationCombine() {
    var showGroupQueueDialog = $("#showGroupQueueDialog");
    showGroupQueueDialog.find("table tbody tr").remove();
    var uuid = showGroupQueueDialog.find("input[name='operationCombineUuid']").val();
    var optimizationGroupName = showGroupQueueDialog.find("input[name='optimizedGroupName']").val();
    var operationCombine = {};
    operationCombine.uuid = uuid;
    operationCombine.groupName = $.trim(optimizationGroupName);
    $.ajax({
       url: '/internal/operationCombine/searchGroupsBelowOperationCombine',
       type: 'POST',
       data: JSON.stringify(operationCombine),
       headers: {
           'Accept': 'application/json',
           'Content-Type': 'application/json'
       },
       success: function (result) {
           if (result !== null) {
               var tbody = $("#showGroupQueueDialog").find("table tbody");
               $.each(result, function(index, element) {
                   tbody.append("<tr>" +
                       "<td width='20px'  style='text-align: center;'><input type='checkbox' name='checkGroup' groupUuid='"+ element.groupUuid +"' checked='checked'></td>" +
                       "<td width='208px'><input type='text' style='width: 200px;' name='Group' disabled='disabled' value='"+ element.groupName +"'/></td>" +
                       "</tr>");
               });
           } else {
               $().toastmessage('showErrorToast', '没有数据！！');
           }
       },
       error: function () {
           $().toastmessage('showErrorToast', '获取优化组数据失败！！');
       }
    });
}

function saveGroupsBelowOperationCombine(operationCombineUuid, maxInvalidCount) {
    var showGroupQueueDialog = $("#showGroupQueueDialog");
    var addFlag = true;
    var updateFlag = true;
    var groupNameArr = [];
    var groupNames = showGroupQueueDialog.find("input[name='newOptimizationGroups']").val();
    if (groupNames !== '') {
        groupNames = groupNames.replace(/[，|\r\n]/g, ',').replace(/[\s+]/g, '');
        if (groupNames.substring(groupNames.length - 1) === ',') {
            groupNames = groupNames.substring(0, groupNames.length - 1);
        }
        groupNameArr = groupNames.split(',');
        groupNameArr = groupNameArr.filter(function (groupName, index) {
           return groupNameArr.indexOf(groupName) === index && groupName !== '';
        });
        if (groupNameArr.length === 0) {
            addFlag = false;
        }
    } else {
        addFlag = false;
    }
    var unChecked = showGroupQueueDialog.find("table tbody").find("input[name='checkGroup']:not(:checked)");
    var groupUuids = [];
    if (unChecked.length > 0) {
        $.each(unChecked, function (idx, v) {
            groupUuids.push($(v).attr("groupUuid"));
        });
    } else {
        updateFlag = false;
    }
    var postData = {};
    if (addFlag) {
        postData.operationCombineUuid = operationCombineUuid;
        postData.groupNames = groupNameArr;
        postData.terminalType = $("#chargeForm").find("input[name='terminalType']").val();
        postData.maxInvalidCount = maxInvalidCount;
        postData.onlySaveStatus = true;
        $.ajax({
            url: '/internal/group/saveGroupsBelowOperationCombine',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(postData),
            success: function(result) {
                if (result) {
                    $().toastmessage('showSuccessToast', '操作组合增加分组成功！！！', true);
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', '操作组合增加分组失败！！！');
                addFlag = false;
            }
        });
    }
    if (updateFlag) {
        postData.groupUuids = groupUuids;
        $.ajax({
            url: '/internal/group/deleteGroupsBelowOperationCombine',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(postData),
            success: function(result) {
                if (result) {
                    $().toastmessage('showSuccessToast', '操作组合移除分组成功！！！', true);
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', '操作组合移除分组失败！！！');
                updateFlag = false;
            }
        });
    }
    $("#showGroupQueueDialog").dialog("close");
}