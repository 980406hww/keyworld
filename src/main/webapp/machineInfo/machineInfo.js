$(function () {
    $('#uploadVNCDialog').dialog("close");
    $('#uploadVPSDialog').dialog("close");
    $("#changeSettingDialog").dialog("close");
    $("#targetVersionSettingDialog").dialog("close");
    $("#targetVPSPasswordSettingDialog").dialog("close");
    $("#renewalSettingDialog").dialog("close");
    $("#reopenClientDiv").dialog("close");
    $("#headerTableDialog").dialog("close");
    pageLoad();
    alignTableHeader();
    window.onresize = function(){
        alignTableHeader();
    }

    // var menuHeight = $(".venus-menu").height();
    // $("#searchClientStatusForm").css("margin-top", menuHeight + 5);
    // $("#clientStatusDiv").css("margin-top", menuHeight + 171);;

});
function pageLoad() {
    var searchCustomerForm = $("#searchMachineInfoForm");
    var pageSize = searchCustomerForm.find('#pageSizeHidden').val();
    var pages = searchCustomerForm.find('#pagesHidden').val();
    var currentPageNumber = searchCustomerForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    showCustomerBottomDiv.find("#chooseRecords").val(pageSize);
    searchCustomerForm.find("#startUpStatus").val($("#startUpStatusHidden").val());
    searchCustomerForm.find("#runningProgramType").val($("#runningProgramTypeHidden").val());

    if (parseInt(currentPageNumber) > 1 && parseInt(currentPageNumber) < parseInt(pages)) {
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
}
function alignTableHeader(){
    var columns = $("#hiddenColumns").val().split(",");
    $.each(columns, function (index, value) {
        if(value != "") {
            $("#headerTable tr").each(function () {
                $(this).find("td").eq(value).css("display", "none");
            });
            $("#machineInfoTable tr").each(function () {
                $(this).find("td").eq(value).css("display", "none");
            });
        }
    });
    var td = $("#machineInfoTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
    $("#machineInfoDiv").css("margin-top",$("#topDiv").height());
}
function changePaging(currentPageNumber, pageSize) {
    var searchMachineInfoForm = $("#searchMachineInfoForm");
    searchMachineInfoForm.find("#currentPageNumberHidden").val(currentPageNumber);
    searchMachineInfoForm.find("#pageSizeHidden").val(pageSize);
    searchMachineInfoForm.submit();
}
function resetPageNumber() {
    var machineInfoFormObj = $("#searchMachineInfoForm");
    var clientId = machineInfoFormObj.find("#clientID").val();
    var groupName = machineInfoFormObj.find("#groupName").val();
    var version = machineInfoFormObj.find("#version").val();
    var targetVersion = machineInfoFormObj.find("#targetVersion").val();
    var city = machineInfoFormObj.find("#city").val();
    var upgradeFailedReason = machineInfoFormObj.find("#upgradeFailedReason").val();
    var vpsBackendSystemComputerID = machineInfoFormObj.find("#vpsBackendSystemComputerID").val();
    var switchGroupName = machineInfoFormObj.find("#switchGroupName").val();
    var startUpStatus = machineInfoFormObj.find("#startUpStatus").val();
    if(clientId != "") {
        machineInfoFormObj.find("#clientID").val($.trim(clientId));
    }
    if(groupName != "") {
        machineInfoFormObj.find("#groupName").val($.trim(groupName));
    }
    if(version != "") {
        machineInfoFormObj.find("#version").val($.trim(version));
    }
    if(targetVersion != "") {
        machineInfoFormObj.find("#targetVersion").val($.trim(targetVersion));
    }
    if(city != "") {
        machineInfoFormObj.find("#city").val($.trim(city));
    }
    if(upgradeFailedReason != "") {
        machineInfoFormObj.find("#upgradeFailedReason").val($.trim(upgradeFailedReason));
    }
    if(vpsBackendSystemComputerID != "") {
        machineInfoFormObj.find("#vpsBackendSystemComputerID").val($.trim(vpsBackendSystemComputerID));
    }
    if(switchGroupName != "") {
        machineInfoFormObj.find("#switchGroupName").val($.trim(switchGroupName));
    }
    if(startUpStatus != "") {
        machineInfoFormObj.find("#startUpStatus").val($.trim(startUpStatus));
    }
    $("#searchMachineInfoForm").find("#currentPageNumberHidden").val(1);
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
function downloadVNCFile() {
    $.ajax({
        url: '/internal/machineInfo/downloadVNCFile',
        type: 'POST',
        success: function (result) {
            if (result) {
                window.location.href="/vnc.zip?t=" + new Date().getTime();
            } else {
                $().toastmessage('showErrorToast', "下载失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "下载失败");
        }
    });
}
function downloadFullVNCFile() {
    $.ajax({
        url: '/internal/machineInfo/downloadFullVNCFile',
        type: 'POST',
        success: function (result) {
            if (result) {
                window.location.href="/vncAll.zip?t=" + new Date().getTime();
            } else {
                $().toastmessage('showErrorToast', "下载失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "下载失败");
        }
    });
}
function selectAll(self){
    var a = document.getElementsByName("clientID");
    if(self.checked){
        for(var i = 0;i<a.length;i++){
            a[i].checked = true;
        }
    }else{
        for(var i = 0;i<a.length;i++){
            a[i].checked = false;
        }
    }
}
function showUploadVNCDialog() {
    $("#uploadVNCDialog").show();
    $('#uploadVNCDialog').dialog({
        resizable: false,
        width: 300,
        modal: true,
        title: '上传VNC文件',
        buttons: [{
            text: '上传',
            iconCls: 'icon-ok',
            handler: function () {
                var fileValue = $("#uploadVNCDialog").find("#file").val();
                if(fileValue == ""){
                    alert("请选择要上传的VNC配置文件!");
                    return false;
                }
                var posIndex = fileValue.indexOf(".xml");
                if (posIndex == -1) {
                    alert("只能上传XML文件！");
                    return false;
                }
                var formData = new FormData();
                formData.append('file', $("#uploadVNCDialog").find("#file")[0].files[0]);
                $.ajax({
                    url: '/internal/machineInfo/uploadVNCFile',
                    type: 'POST',
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result) {
                            $().toastmessage('showSuccessToast', "上传成功");
                        } else {
                            $().toastmessage('showErrorToast', "上传失败");
                        }
                        $('#uploadVNCDialog').dialog("close");
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "上传失败");
                        $('#uploadVNCDialog').dialog("close");
                    }
                });
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#customerKeywordForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#uploadVNCDialog').dialog("close");
                }
            }]
    });
    $('#uploadVNCDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function showUploadVPSDialog(machineInfoType) {
    $("#uploadVPSDialog").show();
    if(machineInfoType == "startUp") {
        $("#uploadVPSDialog").find("#programType").css("display", "block");
    } else {
        $("#uploadVPSDialog").find("#programType").css("display", "none");
    }
    $('#uploadVPSDialog').dialog({
        resizable: false,
        width: 300,
        modal: true,
        title: '上传VPS文件',
        buttons: [{
            text: '上传',
            iconCls: 'icon-ok',
            handler: function () {
                var fileValue = $("#uploadVPSDialog").find("#file").val();
                if(fileValue == ""){
                    alert("请选择要上传的VPS文件!");
                    return false;
                }
                var posIndex = fileValue.indexOf(".txt");
                if (posIndex == -1) {
                    alert("只能上传txt文件！");
                    return false;
                }

                var formData = new FormData();
                formData.append('file', $("#uploadVPSDialog").find("#file")[0].files[0]);
                formData.append('machineInfoType', machineInfoType);
                if(machineInfoType == "startUp") {
                    var downloadProgramType = $("#programType").find("input[name=downloadProgramType]:checked").val();
                    formData.append('downloadProgramType', downloadProgramType);
                }
                $.ajax({
                    url: '/internal/machineInfo/uploadVPSFile',
                    type: 'POST',
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result) {
                            $().toastmessage('showSuccessToast', "上传成功", true);
                        } else {
                            $().toastmessage('showErrorToast', "上传失败");
                        }
                        $('#uploadVPSDialog').dialog("close");
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "上传失败");
                        $('#uploadVPSDialog').dialog("close");
                    }
                });
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#uploadVPSDialog').dialog("close");
                }
            }]
    });
    $('#uploadVPSDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function showReopenClientDialog() {
    var clientIDs = getSelectedClientIDs();
    if (clientIDs === '') {
        alert('请选择要重开的终端');
        return;
    }
    $("#reopenClientDiv").show();
    $('#reopenClientDiv').dialog({
        resizable: false,
        width: 300,
        modal: true,
        title: '重开机器',
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var downloadProgramType = $("#reopenClientDiv").find("input[name=downloadProgramType]:checked").val();
                var postData = {};
                postData.clientIDs = clientIDs.split(",");
                postData.downloadProgramType = downloadProgramType;
                $.ajax({
                    url: '/internal/machineInfo/reopenMachineInfo',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result) {
                            $().toastmessage('showSuccessToast', "保存成功", true);
                        } else {
                            $().toastmessage('showErrorToast', "保存失败");
                        }
                        $('#reopenClientDiv').dialog("close");
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "保存失败");
                        $('#reopenClientDiv').dialog("close");
                    }
                });
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#reopenClientDiv').dialog("close");
                }
            }]
    });
    $('#reopenClientDiv').window("resize",{top:$(document).scrollTop() + 100});
}
function decideSelectAll() {
    var a = document.getElementsByName("clientID");
    var select=0;
    for(var i = 0; i < a.length; i++){
        if (a[i].checked == true){
            select++;
        }
    }
    if(select == a.length){
        $("#selectAllChecked").prop("checked",true);
    }else {
        $("#selectAllChecked").prop("checked",false);
    }
}
function delItem(clientID) {
    if (confirm("确定要删除这台终端吗?") == false) return;
    $.ajax({
        url: '/internal/machineInfo/deleteMachineInfo/' + clientID,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功",true);
                /* window.location.reload();*/
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function delAllItems(self) {
    var clientIDs = getSelectedClientIDs();
    if (clientIDs === '') {
        alert('请选择要删除的终端');
        return;
    }
    if (confirm("确定要删除这些终端吗?") == false) return;
    var postData = {};
    postData.clientIDs = clientIDs.split(",");
    $.ajax({
        url: '/internal/machineInfo/deleteMachineInfos',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功",true);
                /* window.location.reload();*/
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function getSelectedClientIDs() {
    var clientIDs = '';
    $.each($("input[name=clientID]:checkbox:checked"), function () {
        if (clientIDs === '') {
            clientIDs = $(this).val();
        } else {
            clientIDs = clientIDs + "," + $(this).val();
        }
    });
    return clientIDs;
}
function resetRestartStatus() {
    if (confirm("确实要重设状态为Processing或者Logging终端的重启状态吗?") == false) return;
    $.ajax({
        url: '/internal/machineInfo/resetRestartStatusForProcessing' ,
        type: 'POST',
        success: function (result) {
            if(result){
                $().toastmessage('showSuccessToast', "更新成功");
                /* window.location.reload();*/
            }else{
                $().toastmessage('showErrorToast', "更新失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
        }
    });
}
function changeStatus(clientID, status) {
    if(status) {
        if (confirm("确实要开始监控这台终端吗?") == false) {
            return;
        }
    } else {
        if (confirm("确实要暂停监控这台终端吗?") == false) {
            return;
        }
    }
    $.ajax({
        url: '/internal/machineInfo/changeStatus/' + clientID,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功",true);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });

}

function updateGroup(self){
    var machineInfo = {};
    machineInfo.clientID = self.id;
    machineInfo.group = self.value.trim();
    $.ajax({
        url: '/internal/machineInfo/updateGroup',
        data: JSON.stringify(machineInfo),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if(result){
                $().toastmessage('showSuccessToast', "更新成功");
            }else{
                $().toastmessage('showErrorToast', "更新失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
        }
    });
}
function changeTerminalType(clientID){
    if (confirm("确定要变更终端类型吗?") == false) return;
    var postData = {};
    postData.clientID = clientID;
    $.ajax({
        url: '/internal/machineInfo/changeTerminalType',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if(result){
                $().toastmessage('showSuccessToast', "更新成功",true);

            }else{
                $().toastmessage('showErrorToast', "更新失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
        }
    });
}
function updateOperationType(self){
    var machineInfo = {};
    machineInfo.clientID = self.id.replace("operationType", "");
    machineInfo.operationType = self.value.trim();
    $.ajax({
        url: '/internal/machineInfo/updateOperationType',
        data: JSON.stringify(machineInfo),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if(result){
                $().toastmessage('showSuccessToast', "更新成功");

            }else{
                $().toastmessage('showErrorToast', "更新失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
        }
    });
}
function showSettingDialog(clientID, self){
    resetTrItemColor();
    $.ajax({
        url: '/internal/machineInfo/getMachineInfo/' + clientID,
        type: 'POST',
        success: function (machineInfo) {
            if(machineInfo == null) {
                $().toastmessage('showErrorToast', "获取信息失败");
            } else {
                initSettingDialog(machineInfo, self);
                $("#changeSettingDialog").show();
                $("#changeSettingDialog").dialog({
                    resizable: false,
                    title: "设置",
                    width: 820,
                    maxHeight: 534,
                    modal: true,
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            saveChangeSetting(null);
                        }
                    },
                        {
                            text: '取消',
                            iconCls: 'icon-cancel',
                            handler: function () {
                                $("#changeSettingDialog").dialog("close");
                            }
                        }]
                });
                $('#changeSettingDialog').window("resize",{top:$(document).scrollTop() + 100});
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function initSettingDialog(machineInfo, self){
    var settingDialogDiv = $("#changeSettingDialog");
    settingDialogDiv.find("#settingClientID").val(machineInfo.clientID);
    settingDialogDiv.find("#settingGroup").val(machineInfo.group != null ? machineInfo.group : "");
    settingDialogDiv.find("#settingOperationType").val(machineInfo.operationType != null ? machineInfo.operationType : "");
    if(machineInfo.pageSize != null){
        settingDialogDiv.find("#pageSize").val(machineInfo.pageSize);
    }
    if(machineInfo.page != null){
        settingDialogDiv.find("#page").val(machineInfo.page);
    }
    if(machineInfo.zhanneiPercent != null){
        settingDialogDiv.find("#zhanneiPercent").val(machineInfo.zhanneiPercent);
    }
    if(machineInfo.zhanwaiPercent != null){
        settingDialogDiv.find("#zhanwaiPercent").val(machineInfo.zhanwaiPercent);
    }
    if(machineInfo.dragPercent  != null){
        settingDialogDiv.find("#dragPercent").val(machineInfo.dragPercent );
    }
    if(machineInfo.kuaizhaoPercent != null){
        settingDialogDiv.find("#kuaizhaoPercent").val(machineInfo.kuaizhaoPercent);
    }
    if(machineInfo.baiduSemPercent != null){
        settingDialogDiv.find("#baiduSemPercent").val(machineInfo.baiduSemPercent);
    }
    if(machineInfo.specialCharPercent != null){
        settingDialogDiv.find("#specialCharPercent").val(machineInfo.specialCharPercent);
    }

    if(machineInfo.multiBrowser != null){
        settingDialogDiv.find("#multiBrowser").val(machineInfo.multiBrowser);
    }
    if(machineInfo.clearCookie != null){
        settingDialogDiv.find("#clearCookie").val(machineInfo.clearCookie);
    }
    settingDialogDiv.find("#allowSwitchGroup ").val(machineInfo.allowSwitchGroup );
    settingDialogDiv.find("#disableStatistics ").val(machineInfo.disableStatistics );
    settingDialogDiv.find("#disableVisitWebsite ").val(machineInfo.disableVisitWebsite );

    settingDialogDiv.find("#entryPageMinCount").val(machineInfo.entryPageMinCount);
    settingDialogDiv.find("#entryPageMaxCount").val(machineInfo.entryPageMaxCount);
    settingDialogDiv.find("#pageRemainMinTime").val(machineInfo.pageRemainMinTime);
    settingDialogDiv.find("#pageRemainMaxTime").val(machineInfo.pageRemainMaxTime);
    settingDialogDiv.find("#inputDelayMinTime").val(machineInfo.inputDelayMinTime);
    settingDialogDiv.find("#inputDelayMaxTime").val(machineInfo.inputDelayMaxTime);
    settingDialogDiv.find("#slideDelayMinTime").val(machineInfo.slideDelayMinTime);
    settingDialogDiv.find("#slideDelayMaxTime").val(machineInfo.slideDelayMaxTime);
    settingDialogDiv.find("#titleRemainMinTime").val(machineInfo.titleRemainMinTime);
    settingDialogDiv.find("#titleRemainMaxTime").val(machineInfo.titleRemainMaxTime);
    settingDialogDiv.find("#waitTimeAfterOpenBaidu").val(machineInfo.waitTimeAfterOpenBaidu);
    settingDialogDiv.find("#waitTimeBeforeClick").val(machineInfo.waitTimeBeforeClick);
    settingDialogDiv.find("#waitTimeAfterClick").val(machineInfo.waitTimeAfterClick);
    settingDialogDiv.find("#maxUserCount").val(machineInfo.maxUserCount);
    settingDialogDiv.find("#optimizeKeywordCountPerIP").val(machineInfo.optimizeKeywordCountPerIP);

    settingDialogDiv.find("#oneIPOneUser")[0].checked = (machineInfo.oneIPOneUser == 1) ? true : false;
    settingDialogDiv.find("#randomlyClickNoResult")[0].checked = (machineInfo.randomlyClickNoResult == 1) ? true : false;
    settingDialogDiv.find("#justVisitSelfPage")[0].checked = (machineInfo.justVisitSelfPage == 1) ? true : false;
    settingDialogDiv.find("#sleepPer2Words")[0].checked = (machineInfo.sleepPer2Words == 1) ? true : false;
    settingDialogDiv.find("#supportPaste")[0].checked = (machineInfo.supportPaste == 1) ? true : false;
    settingDialogDiv.find("#moveRandomly")[0].checked = (machineInfo.moveRandomly == 1) ? true : false;
    settingDialogDiv.find("#parentSearchEntry")[0].checked = (machineInfo.parentSearchEntry == 1) ? true : false;
    settingDialogDiv.find("#clearLocalStorage")[0].checked = (machineInfo.clearLocalStorage == 1) ? true : false;
    settingDialogDiv.find("#lessClickAtNight")[0].checked = (machineInfo.lessClickAtNight == 1) ? true : false;
    settingDialogDiv.find("#sameCityUser")[0].checked = (machineInfo.sameCityUser == 1) ? true : false;
    settingDialogDiv.find("#locateTitlePosition")[0].checked = (machineInfo.locateTitlePosition == 1) ? true : false;
    settingDialogDiv.find("#baiduAllianceEntry")[0].checked = (machineInfo.baiduAllianceEntry == 1) ? true : false;
    settingDialogDiv.find("#justClickSpecifiedTitle")[0].checked = (machineInfo.justClickSpecifiedTitle == 1) ? true : false;
    settingDialogDiv.find("#randomlyClickMoreLink")[0].checked = (machineInfo.randomlyClickMoreLink == 1) ? true : false;
    settingDialogDiv.find("#moveUp20")[0].checked = (machineInfo.moveUp20 == 1) ? true : false;
    settingDialogDiv.find("#optimizeRelatedKeyword")[0].checked = (machineInfo.optimizeRelatedKeyword == 1) ? true : false;

    settingDialogDiv.find("#waitTimeAfterOpenBaidu").val(machineInfo.waitTimeAfterOpenBaidu);
    settingDialogDiv.find("#waitTimeBeforeClick").val(machineInfo.waitTimeBeforeClick);
    settingDialogDiv.find("#waitTimeAfterClick").val(machineInfo.waitTimeAfterClick);
    settingDialogDiv.find("#maxUserCount").val(machineInfo.maxUserCount);

    settingDialogDiv.find("#switchGroupName").val(machineInfo.switchGroupName != null ? machineInfo.switchGroupName : "");
    settingDialogDiv.find("#host").val(machineInfo.host != null ? machineInfo.host : "");
    settingDialogDiv.find("#port").val(machineInfo.port != null ? machineInfo.port : "");
    settingDialogDiv.find("#csUserName").val(machineInfo.userName != null ? machineInfo.userName : "Administrator");
    settingDialogDiv.find("#broadbandAccount").val(machineInfo.broadbandAccount != null ? machineInfo.broadbandAccount : "");
    settingDialogDiv.find("#broadbandPassword").val(machineInfo.broadbandPassword != null ? machineInfo.broadbandPassword : "");
    settingDialogDiv.find("#vpsBackendSystemComputerID").val(machineInfo.vpsBackendSystemComputerID != null ? machineInfo.vpsBackendSystemComputerID :
        "");
    settingDialogDiv.find("#vpsBackendSystemPassword").val(machineInfo.vpsBackendSystemPassword != null ? machineInfo.vpsBackendSystemPassword : "doshows123");
    settingDialogDiv.show();
}
function isChecked(id) {
    var color = $("#changeSettingDialog").find("#" + id).parent().css("color");
    if(color == "rgb(255, 0, 0)") {
        return "1";
    } else {
        return "0";
    }
}
function saveChangeSetting(clientIDs){
    var settingDialogDiv = $("#changeSettingDialog");
    var machineInfo = {};
    machineInfo.group = settingDialogDiv.find("#settingGroup").val();
    machineInfo.operationType = settingDialogDiv.find("#settingOperationType").val();
    machineInfo.pageSize = settingDialogDiv.find("#pageSize").val();
    machineInfo.page = settingDialogDiv.find("#page").val();
    machineInfo.dragPercent = settingDialogDiv.find("#dragPercent").val();
    machineInfo.zhanneiPercent = settingDialogDiv.find("#zhanneiPercent").val();
    machineInfo.zhanwaiPercent = settingDialogDiv.find("#zhanwaiPercent").val();
    machineInfo.kuaizhaoPercent = settingDialogDiv.find("#kuaizhaoPercent").val();
    machineInfo.baiduSemPercent = settingDialogDiv.find("#baiduSemPercent").val();
    machineInfo.specialCharPercent = settingDialogDiv.find("#specialCharPercent").val();
    machineInfo.multiBrowser = settingDialogDiv.find("#multiBrowser").val();
    machineInfo.clearCookie = settingDialogDiv.find("#clearCookie").val();
    machineInfo.allowSwitchGroup = settingDialogDiv.find("#allowSwitchGroup").val();
    machineInfo.disableStatistics = settingDialogDiv.find("#disableStatistics").val();
    machineInfo.disableVisitWebsite = settingDialogDiv.find("#disableVisitWebsite").val();

    machineInfo.switchGroupName = settingDialogDiv.find("#switchGroupName").val();
    machineInfo.host = settingDialogDiv.find("#host").val();
    machineInfo.port = settingDialogDiv.find("#port").val();
    machineInfo.userName = settingDialogDiv.find("#csUserName").val();
    machineInfo.broadbandAccount = settingDialogDiv.find("#broadbandAccount").val();
    machineInfo.broadbandPassword = settingDialogDiv.find("#broadbandPassword").val();
    machineInfo.vpsBackendSystemComputerID = settingDialogDiv.find("#vpsBackendSystemComputerID").val();
    machineInfo.vpsBackendSystemPassword = settingDialogDiv.find("#vpsBackendSystemPassword").val();

    machineInfo.entryPageMinCount = settingDialogDiv.find("#entryPageMinCount").val();
    machineInfo.entryPageMaxCount = settingDialogDiv.find("#entryPageMaxCount").val();
    machineInfo.disableVisitWebsite = settingDialogDiv.find("#disableVisitWebsite").val();
    machineInfo.pageRemainMinTime = settingDialogDiv.find("#pageRemainMinTime").val();
    machineInfo.pageRemainMaxTime = settingDialogDiv.find("#pageRemainMaxTime").val();
    machineInfo.inputDelayMinTime = settingDialogDiv.find("#inputDelayMinTime").val();
    machineInfo.inputDelayMaxTime = settingDialogDiv.find("#inputDelayMaxTime").val();
    machineInfo.slideDelayMinTime = settingDialogDiv.find("#slideDelayMinTime").val();
    machineInfo.slideDelayMaxTime = settingDialogDiv.find("#slideDelayMaxTime").val();
    machineInfo.titleRemainMinTime = settingDialogDiv.find("#titleRemainMinTime").val();
    machineInfo.titleRemainMaxTime = settingDialogDiv.find("#titleRemainMaxTime").val();
    machineInfo.waitTimeAfterOpenBaidu = settingDialogDiv.find("#waitTimeAfterOpenBaidu").val();
    machineInfo.waitTimeBeforeClick = settingDialogDiv.find("#waitTimeBeforeClick").val();
    machineInfo.waitTimeAfterClick = settingDialogDiv.find("#waitTimeAfterClick").val();
    machineInfo.maxUserCount = settingDialogDiv.find("#maxUserCount").val();
    machineInfo.optimizeKeywordCountPerIP = settingDialogDiv.find("#optimizeKeywordCountPerIP").val();

    machineInfo.oneIPOneUser = settingDialogDiv.find("#oneIPOneUser:checked").val() === '1' ? 1 : 0;
    machineInfo.randomlyClickNoResult = settingDialogDiv.find("#randomlyClickNoResult:checked").val() === '1' ? 1 : 0;
    machineInfo.justVisitSelfPage = settingDialogDiv.find("#justVisitSelfPage:checked").val() === '1' ? 1 : 0;
    machineInfo.sleepPer2Words = settingDialogDiv.find("#sleepPer2Words:checked").val() === '1' ? 1 : 0;
    machineInfo.supportPaste = settingDialogDiv.find("#supportPaste:checked").val() === '1' ? 1 : 0;
    machineInfo.moveRandomly = settingDialogDiv.find("#moveRandomly:checked").val() === '1' ? 1 : 0;
    machineInfo.parentSearchEntry = settingDialogDiv.find("#parentSearchEntry:checked").val() === '1' ? 1 : 0;
    machineInfo.clearLocalStorage = settingDialogDiv.find("#clearLocalStorage:checked").val() === '1' ? 1 : 0;
    machineInfo.lessClickAtNight = settingDialogDiv.find("#lessClickAtNight:checked").val() === '1' ? 1 : 0;
    machineInfo.sameCityUser = settingDialogDiv.find("#sameCityUser:checked").val() === '1' ? 1 : 0;
    machineInfo.locateTitlePosition = settingDialogDiv.find("#locateTitlePosition:checked").val() === '1' ? 1 : 0;
    machineInfo.baiduAllianceEntry = settingDialogDiv.find("#baiduAllianceEntry:checked").val() === '1' ? 1 : 0;
    machineInfo.justClickSpecifiedTitle = settingDialogDiv.find("#justClickSpecifiedTitle:checked").val() === '1' ? 1 : 0;
    machineInfo.randomlyClickMoreLink = settingDialogDiv.find("#randomlyClickMoreLink:checked").val() === '1' ? 1 : 0;
    machineInfo.moveUp20 = settingDialogDiv.find("#moveUp20:checked").val() === '1' ? 1 : 0;
    machineInfo.optimizeRelatedKeyword = settingDialogDiv.find("#optimizeRelatedKeyword:checked").val() === '1' ? 1 : 0;
    if(clientIDs != null) {
        machineInfo.clientID = clientIDs;
        var cs = {};
        cs.group = isChecked("settingGroup");
        cs.operationType = isChecked("settingOperationType");
        cs.pageSize = isChecked("pageSize");
        cs.page = isChecked("page");
        cs.dragPercent = isChecked("dragPercent");
        cs.zhanneiPercent = isChecked("zhanneiPercent");
        cs.zhanwaiPercent = isChecked("zhanwaiPercent");
        cs.kuaizhaoPercent = isChecked("kuaizhaoPercent");
        cs.baiduSemPercent = isChecked("baiduSemPercent");
        cs.specialCharPercent = isChecked("specialCharPercent");
        cs.multiBrowser = isChecked("multiBrowser");
        cs.clearCookie = isChecked("clearCookie");
        cs.allowSwitchGroup = isChecked("allowSwitchGroup");
        cs.disableStatistics = isChecked("disableStatistics");
        cs.disableVisitWebsite = isChecked("disableVisitWebsite");

        cs.switchGroupName = isChecked("switchGroupName");
        cs.host = isChecked("host");
        cs.port = isChecked("port");
        cs.userName = isChecked("csUserName");
        cs.broadbandAccount = isChecked("broadbandAccount");
        cs.broadbandPassword = isChecked("broadbandPassword");
        cs.vpsBackendSystemComputerID = isChecked("vpsBackendSystemComputerID");
        cs.vpsBackendSystemPassword = isChecked("vpsBackendSystemPassword");

        cs.entryPageMinCount = isChecked("entryPageMinCount");
        cs.entryPageMaxCount = isChecked("entryPageMaxCount");
        cs.disableVisitWebsite = isChecked("disableVisitWebsite");
        cs.pageRemainMinTime = isChecked("pageRemainMinTime");
        cs.pageRemainMaxTime = isChecked("pageRemainMaxTime");
        cs.inputDelayMinTime = isChecked("inputDelayMinTime");
        cs.inputDelayMaxTime = isChecked("inputDelayMaxTime");
        cs.slideDelayMinTime = isChecked("slideDelayMinTime");
        cs.slideDelayMaxTime = isChecked("slideDelayMaxTime");
        cs.titleRemainMinTime = isChecked("titleRemainMinTime");
        cs.titleRemainMaxTime = isChecked("titleRemainMaxTime");
        cs.waitTimeAfterOpenBaidu = isChecked("waitTimeAfterOpenBaidu");
        cs.waitTimeBeforeClick = isChecked("waitTimeBeforeClick");
        cs.waitTimeAfterClick = isChecked("waitTimeAfterClick");
        cs.maxUserCount = isChecked("maxUserCount");
        cs.optimizeKeywordCountPerIP = isChecked("optimizeKeywordCountPerIP");

        cs.oneIPOneUser = isChecked("oneIPOneUser") === '1' ? 1 : 0;
        cs.randomlyClickNoResult = isChecked("randomlyClickNoResult") === '1' ? 1 : 0;
        cs.justVisitSelfPage = isChecked("justVisitSelfPage") === '1' ? 1 : 0;
        cs.sleepPer2Words = isChecked("sleepPer2Words") === '1' ? 1 : 0;
        cs.supportPaste = isChecked("supportPaste") === '1' ? 1 : 0;
        cs.moveRandomly = isChecked("moveRandomly") === '1' ? 1 : 0;
        cs.parentSearchEntry = isChecked("parentSearchEntry") === '1' ? 1 : 0;
        cs.clearLocalStorage = isChecked("clearLocalStorage") === '1' ? 1 : 0;
        cs.lessClickAtNight = isChecked("lessClickAtNight") === '1' ? 1 : 0;
        cs.sameCityUser = isChecked("sameCityUser") === '1' ? 1 : 0;
        cs.locateTitlePosition = isChecked("locateTitlePosition") === '1' ? 1 : 0;
        cs.baiduAllianceEntry = isChecked("baiduAllianceEntry") === '1' ? 1 : 0;
        cs.justClickSpecifiedTitle = isChecked("justClickSpecifiedTitle") === '1' ? 1 : 0;
        cs.randomlyClickMoreLink = isChecked("randomlyClickMoreLink") === '1' ? 1 : 0;
        cs.moveUp20 = isChecked("moveUp20") === '1' ? 1 : 0;
        cs.optimizeRelatedKeyword = isChecked("optimizeRelatedKeyword") === '1' ? 1 : 0;
        var postData = {};
        postData.cs = cs;
        postData.machineInfo = machineInfo;
        $.ajax({
            url: '/internal/machineInfo/batchUpdateMachineInfo',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if(result){
                    $().toastmessage('showSuccessToast', "更新成功");
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
    } else {
        machineInfo.clientID = settingDialogDiv.find("#settingClientID").val();
        $.ajax({
            url: '/internal/machineInfo/saveMachineInfo',
            data: JSON.stringify(machineInfo),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if(result){
                    $().toastmessage('showSuccessToast', "更新成功");
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
    }
}
function showTargetVersionSettingDialog(self){
    if(getSelectedClientIDs().trim() === ''){
        alert("请选择要更新的终端！");
        return;
    }
    $("#targetVersionSettingDialog").find("#settingTargetVersion").val("");
    $("#targetVersionSettingDialog").show();
    $("#targetVersionSettingDialog").dialog({
        resizable: false,
        title: "设定目标版本",
        height:100,
        width: 240,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveTargetVersionSetting(this);
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#targetVersionSettingDialog').dialog("close");
                }
            }]
    });
    $('#targetVersionSettingDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function showTargetVPSPasswordSettingDialog(self){
    if(getSelectedClientIDs().trim() === ''){
        alert("请选择要更新的终端！");
        return;
    }
    $("#targetVPSPasswordSettingDialog").show();
    $("#targetVPSPasswordSettingDialog").find("#settingTargetVPSPassword").val("");
    $("#targetVPSPasswordSettingDialog").dialog({
        resizable: false,
        title: "设定目标密码",
        height:100,
        width: 240,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveTargetVPSPasswordSetting(this);
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#targetVPSPasswordSettingDialog').dialog("close");
                }
            }]
    });
    $('#targetVPSPasswordSettingDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveTargetVersionSetting(self){
    var settingDialogDiv = $("#targetVersionSettingDialog");
    var machineInfo = {};
    machineInfo.clientIDs = getSelectedClientIDs();
    machineInfo.targetVersion = settingDialogDiv.find("#settingTargetVersion").val();
    if(machineInfo.targetVersion.trim() === ''){
        alert("请输入目标版1本！");
        return;
    }
    machineInfo.clientIDs = machineInfo.clientIDs.split(",");
    $.ajax({
        url: '/internal/machineInfo/updateMachineInfoTargetVersion',
        data: JSON.stringify(machineInfo),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if(result){
                $().toastmessage('showSuccessToast', "更新成功",true);

            }else{
                $().toastmessage('showErrorToast', "更新失败");
            }
            settingDialogDiv.dialog("close");
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
            settingDialogDiv.dialog("close");
        }
    });
}

function saveTargetVPSPasswordSetting(self){
    var settingDialogDiv = $("#targetVPSPasswordSettingDialog");
    var machineInfo = {};
    machineInfo.clientIDs = getSelectedClientIDs();
    machineInfo.targetVPSPassword = settingDialogDiv.find("#settingTargetVPSPassword").val();
    if(machineInfo.targetVPSPassword.trim() === ''){
        alert("请输入目标密码！");
        return;
    }
    if(machineInfo.targetVPSPassword.trim().length != 8){
        alert("请输入8位密码！");
        return;
    }
    machineInfo.clientIDs = machineInfo.clientIDs.split(",");
    $.ajax({
        url: '/internal/machineInfo/updateMachineInfoTargetVPSPassword',
        data: JSON.stringify(machineInfo),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if(result){
                $().toastmessage('showSuccessToast', "更新成功",true);

            }else{
                $().toastmessage('showErrorToast', "更新失败");
            }
            settingDialogDiv.dialog("close");
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
            settingDialogDiv.dialog("close");
        }
    });
}
function showRenewalSettingDialog(self){
    if(getSelectedClientIDs().trim() === ''){
        alert("请先选择要续费的终端！");
        return;
    }
    $("#renewalSettingDialog").find("#renewalSettingDialog").val("");
    $("#renewalSettingDialog").show();
    $("#renewalSettingDialog").dialog({
        resizable: false,
        title: "续费",
        height:130,
        width: 225,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveRenewalSetting(this);
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#renewalSettingDialog").dialog("close");
                }
            }]
    });
    $('#renewalSettingDialog').window("resize",{top:$(document).scrollTop() + 100});
}

function isDate(s) {
    var r, re;
    re = /2[0-9]{3}-[0-9]{2}-[0-9]{2}/i;
    r = s.match(re);
    return (r == s) ? true : false;
}
function saveRenewalSetting(self){
    var settingDialogDiv = $("#renewalSettingDialog");
    var machineInfo = {};
    machineInfo.clientIDs = getSelectedClientIDs();
    machineInfo.settingType = settingDialogDiv.find("input[name=settingType]:checked").val();
    machineInfo.renewalDate = settingDialogDiv.find("#renewalDate").val();
    if(machineInfo.settingType.trim() === 'specificDate'){
        if(machineInfo.renewalDate.trim() === ''){
            alert("请输入日期！");
            return;
        }
        if(!isDate(machineInfo.renewalDate.trim())){
            alert("日期格式不对！");
            return;
        }
    }
    $.ajax({
        url: '/internal/machineInfo/updateMachineInfoRenewalDate',
        data: JSON.stringify(machineInfo),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if(result){
                $().toastmessage('showSuccessToast', "更新成功");

            }else{
                $().toastmessage('showErrorToast', "更新失败");
            }
            settingDialogDiv.dialog("close");
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
            settingDialogDiv.dialog("close");
        }
    });
}
function machineInfoBatchUpdate() {
    var clientIDs = getSelectedClientIDs();
    if (clientIDs.indexOf(",") == -1) {
        alert('请选择多个终端进行设置');
        return;
    }
    resetTrItemColor();
    $("#changeSettingDialog").find('input[type=text],select,input[type=hidden]').each(function() {
        $(this).val('');
    });
    $("#changeSettingDialog").find('input[type=checkbox]').each(function() {
        $(this).prop("checked",false);
    });
    $("#changeSettingDialog").show();
    $("#changeSettingDialog").dialog({
        resizable: false,
        title: "终端批量设置(请将需要修改的字段点击标记为红色)",
        width: 820,
        maxHeight: 534,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveChangeSetting(clientIDs);
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#changeSettingDialog").dialog("close");
                }
            }]
    });
    $('#changeSettingDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function headerTableSetting() {
    var columnArray = $("#hiddenColumns").val().split(",");
    $("#headerTableDialog").find(":input[name=columnName]").each(function () {
        if($.inArray(this.id, columnArray) > -1) {
            $(this).prop("checked", true);
        }
    });

    $("#headerTableDialog").show();
    $("#headerTableDialog").dialog({
        resizable: false,
        title: "表格设置",
        width: 320,
        height: 420,
        modal: true,
        buttons: [{
            text: '隐藏列',
            iconCls: 'icon-ok',
            handler: function () {
                var columns = "";
                $("#headerTableDialog").find(":input[name=columnName]:checked").each(function () {
                    if(columns == "") {
                        columns = this.id;
                    } else {
                        columns = columns + "," + this.id;
                    }
                });
                if(columnArray==columns){
                    alert('数据没有产生任何变化');
                    return;
                }
                $("#hiddenColumns").val(columns);
                $("#haveHiddenColumns").val(true);
                $("#searchMachineInfoForm").submit();
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#headerTableDialog").dialog("close");
                }
            }]
    });
    $('#headerTableDialog').window("resize",{top:$(document).scrollTop() + 100});
}
// function connectVNC(clientID, urlPrefix){
//     var obj = new ActiveXObject("wscript.shell");
//     obj.run("file:///C:/vnc/" + urlPrefix + "/" + clientID + ".vnc");
//     obj = null;
// }

$(document).ready(function(){
    if($("#showFetchKeywordStatus").attr("checked") === "checked"){
        var clientIDs = [];
        $("span[name=invalidClient]").each(function(){
            clientIDs.push(this.id.replace("span_", ""));
            if(clientIDs.length == 20){
                haveCustomerKeywordForOptimization(clientIDs);
                clientIDs = [];
            }
        });
        if(clientIDs.length > 0){
            haveCustomerKeywordForOptimization(clientIDs);
        };
    }
});
function haveCustomerKeywordForOptimization(clientIDs) {
    $.ajax({
        url: '/internal/customerKeyword/haveCustomerKeywordForOptimization',
        data: JSON.stringify({"clientIDs": clientIDs}),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 60000,
        type: 'POST',
        success: function (result) {
            $("span[name=invalidClient]").each(function(){
                var val = $(this);
                var clientID = val.attr("id").replace("span_", "");
                if (result[clientID] !== undefined) {
                    if (result[clientID]) {
                        val.html("<br>取词正常");
                        val.css("color", "green");
                    } else {
                        val.html("<br>取不到词");
                        val.css("color", "red");
                    }
                }
            });
        },
        error: function () {
            $().toastmessage('showErrorToast', "取词异常");
        }
    });
}
function finishStartUp() {
    var clientIDs = getSelectedClientIDs();
    if (clientIDs === '') {
        alert('请选择要设置的终端');
        return;
    }
    if (confirm("确定要将这些终端的开机状态修改为Completed吗?") == false) return;
    var postData = {};
    postData.clientIDs = clientIDs.split(",");
    $.ajax({
        url: '/internal/machineInfo/updateStartUpStatusForCompleted',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功", true);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}

function batchChangeTerminalType(){
    var clientIDs = getSelectedClientIDs();
    if (clientIDs.indexOf(",") == -1) {
        alert('请选择多个终端进行设置');
        return;
    }
    if (confirm("确定要批量变更终端类型吗?") == false) return;
    var postData = {};
    postData.clientID = clientIDs;
    $.ajax({
        url: '/internal/machineInfo/batchChangeTerminalType',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            console.log(JSON.stringify(postData));
            if(result){
                $().toastmessage('showSuccessToast', "操作成功",true);

            }else{
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}

function connectVNC(host, port, vncUser, vncPwd) {
    try {
        var fso = new ActiveXObject("Scripting.FileSystemObject");
        if (!fso.FolderExists("C:\\vnc")) {
            fso.CreateFolder ("C:\\vnc");
        }

        var fileName = "C:\\vnc\\temp_openVNC_file.vnc";
        if (fso.FileExists(fileName)) {
            var f1 = fso.GetFile(fileName);
            f1.Delete();
        }
        var tf = fso.CreateTextFile(fileName, true, false);
        tf.WriteLine("[Connection]");
        tf.WriteLine("Host=" + host);
        tf.WriteLine("Port=" + port);
        tf.WriteLine("Username=" + vncUser);
        tf.WriteLine("Password=" + vncPwd);
        tf.WriteLine("[Options]");
        tf.WriteLine("UseLocalCursor=1");
        tf.WriteLine("UseDesktopResize=1");
        tf.WriteLine("FullScreen=0");
        tf.WriteLine("FullColour=0");
        tf.WriteLine("LowColourLevel=1");
        tf.WriteLine("PreferredEncoding=ZRLE");
        tf.WriteLine("AutoSelect=1");
        tf.WriteLine("Shared=0");
        tf.WriteLine("SendPtrEvents=1");
        tf.WriteLine("SendKeyEvents=1");
        tf.WriteLine("SendCutText=1");
        tf.WriteLine("AcceptCutText=1");
        tf.WriteLine("Emulate3=0");
        tf.WriteLine("PointerEventInterval=0");
        tf.WriteLine("Monitor=");
        tf.WriteLine("MenuKey=F8");
        tf.close();
        tf = null;
        // 运行
        var obj = new ActiveXObject("wscript.shell");
        obj.run(fileName);
        obj = null;
    } catch (e) {
        if (e.name === "ReferenceError") {
            alert("请更换到带IE内核的浏览器！")
        } else {
            alert("未找到对应的.vnc文件或其他的错误")
        }
    }
}

function batchChangeStatus(status) {
    var clientIDs = getSelectedClientIDs();
    if (clientIDs.indexOf(",") == -1) {
        alert('请选择多个终端进行设置');
        return;
    }
    if (status){
        if (confirm("确定要批量开始监控这些终端吗?") == false) {
            return;
        }
    }else {
        if (confirm("确定要批量停止监控这些终端吗?") == false) {
            return;
        }
    }

    $.ajax({
        url: '/internal/machineInfo/batchChangeStatus',
        type: 'POST',
        data: {"clientIDs":clientIDs,"status":status},
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功",true);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}