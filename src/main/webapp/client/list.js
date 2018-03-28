$(function () {
    $('#uploadVNCDialog').dialog("close");
    $('#uploadVPSDialog').dialog("close");
    $("#changeSettingDialog").dialog("close");
    $("#targetVersionSettingDialog").dialog("close");
    $("#renewalSettingDialog").dialog("close");
    $("#reopenClientDiv").dialog("close");
    $("#clientStatusDiv").css("margin-top",$("#topDiv").height());
    pageLoad();
    alignTableHeader();
    window.onresize = function(){
        alignTableHeader();
    }
});
function pageLoad() {
    var searchCustomerForm = $("#searchClientStatusForm");
    var pageSize = searchCustomerForm.find('#pageSizeHidden').val();
    var pages = searchCustomerForm.find('#pagesHidden').val();
    var currentPageNumber = searchCustomerForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    showCustomerBottomDiv.find("#chooseRecords").val(pageSize);

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
    var td = $("#clientStatusTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function changePaging(currentPageNumber, pageSize) {
    var searchClientStatusForm = $("#searchClientStatusForm");
    searchClientStatusForm.find("#currentPageNumberHidden").val(currentPageNumber);
    searchClientStatusForm.find("#pageSizeHidden").val(pageSize);
    searchClientStatusForm.submit();
}
function resetPageNumber() {
    var clientStatusFormObj = $("#searchClientStatusForm");
    var clientId = clientStatusFormObj.find("#clientID").val();
    var groupName = clientStatusFormObj.find("#groupName").val();
    var version = clientStatusFormObj.find("#version").val();
    var city = clientStatusFormObj.find("#city").val();
    var upgradeFailedReason = clientStatusFormObj.find("#upgradeFailedReason").val();
    var vpsBackendSystemComputerID = clientStatusFormObj.find("#vpsBackendSystemComputerID").val();
    var switchGroupName = clientStatusFormObj.find("#switchGroupName").val();
    var startUpStatus = clientStatusFormObj.find("#startUpStatus").val();
    if(clientId != "") {
        clientStatusFormObj.find("#clientID").val($.trim(clientId));
    }
    if(groupName != "") {
        clientStatusFormObj.find("#groupName").val($.trim(groupName));
    }
    if(version != "") {
        clientStatusFormObj.find("#version").val($.trim(version));
    }
    if(city != "") {
        clientStatusFormObj.find("#city").val($.trim(city));
    }
    if(upgradeFailedReason != "") {
        clientStatusFormObj.find("#upgradeFailedReason").val($.trim(upgradeFailedReason));
    }
    if(vpsBackendSystemComputerID != "") {
        clientStatusFormObj.find("#vpsBackendSystemComputerID").val($.trim(vpsBackendSystemComputerID));
    }
    if(switchGroupName != "") {
        clientStatusFormObj.find("#switchGroupName").val($.trim(switchGroupName));
    }
    if(startUpStatus != "") {
        clientStatusFormObj.find("#startUpStatus").val($.trim(startUpStatus));
    }
    $("#searchClientStatusForm").find("#currentPageNumberHidden").val(1);
}
function downloadVNCFile() {
    $.ajax({
        url: '/internal/clientstatus/downloadVNCFile',
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
        url: '/internal/clientstatus/downloadFullVNCFile',
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
                    url: '/internal/clientstatus/uploadVNCFile',
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
function showUploadVPSDialog(clientStatusType) {
    if(clientStatusType == "startUp") {
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
                formData.append('clientStatusType', clientStatusType);
                if(clientStatusType == "startUp") {
                    var downloadProgramType = $("#programType").find("input[name=downloadProgramType]:checked").val();
                    formData.append('downloadProgramType', downloadProgramType);
                }
                $.ajax({
                    url: '/internal/clientstatus/uploadVPSFile',
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
                    url: '/internal/clientstatus/reopenClientStatus',
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
        url: '/internal/clientstatus/deleteClientStatus/' + clientID,
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
        url: '/internal/clientstatus/deleteClientStatuses',
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
        url: '/internal/clientstatus/resetRestartStatusForProcessing' ,
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
        url: '/internal/clientstatus/changeStatus/' + clientID,
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
    var clientStatus = {};
    clientStatus.clientID = self.id;
    clientStatus.group = self.value.trim();
    $.ajax({
        url: '/internal/clientstatus/updateGroup',
        data: JSON.stringify(clientStatus),
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
    var postData = {};
    postData.clientID = clientID;
    $.ajax({
        url: '/internal/clientstatus/changeTerminalType',
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
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
        }
    });
}
function updateOperationType(self){
    var clientStatus = {};
    clientStatus.clientID = self.id.replace("operationType", "");
    clientStatus.operationType = self.value.trim();
    $.ajax({
        url: '/internal/clientstatus/updateOperationType',
        data: JSON.stringify(clientStatus),
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
    $.ajax({
        url: '/internal/clientstatus/getClientStatus/' + clientID,
        type: 'POST',
        success: function (clientStatus) {
            if(clientStatus == null) {
                $().toastmessage('showErrorToast', "获取信息失败");
            } else {
                initSettingDialog(clientStatus, self);
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
                            saveChangeSetting(this);
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
function initSettingDialog(clientStatus, self){
    var settingDialogDiv = $("#changeSettingDialog");
    settingDialogDiv.find("#settingClientID").val(clientStatus.clientID);
    settingDialogDiv.find("#settingGroup").val(clientStatus.group != null ? clientStatus.group : "");
    settingDialogDiv.find("#settingOperationType").val(clientStatus.operationType != null ? clientStatus.operationType : "");
    if(clientStatus.pageSize != null){
        settingDialogDiv.find("#pageSize").val(clientStatus.pageSize);
    }
    if(clientStatus.page != null){
        settingDialogDiv.find("#page").val(clientStatus.page);
    }
    if(clientStatus.zhanneiPercent != null){
        settingDialogDiv.find("#zhanneiPercent").val(clientStatus.zhanneiPercent);
    }
    if(clientStatus.zhanwaiPercent != null){
        settingDialogDiv.find("#zhanwaiPercent").val(clientStatus.zhanwaiPercent);
    }
    if(clientStatus.dragPercent  != null){
        settingDialogDiv.find("#dragPercent").val(clientStatus.dragPercent );
    }
    if(clientStatus.kuaizhaoPercent != null){
        settingDialogDiv.find("#kuaizhaoPercent").val(clientStatus.kuaizhaoPercent);
    }
    if(clientStatus.baiduSemPercent != null){
        settingDialogDiv.find("#baiduSemPercent").val(clientStatus.baiduSemPercent);
    }
    if(clientStatus.multiBrowser != null){
        settingDialogDiv.find("#multiBrowser").val(clientStatus.multiBrowser);
    }
    if(clientStatus.clearCookie != null){
        settingDialogDiv.find("#clearCookie").val(clientStatus.clearCookie);
    }
    settingDialogDiv.find("#allowSwitchGroup ").val(clientStatus.allowSwitchGroup );
    settingDialogDiv.find("#disableStatistics ").val(clientStatus.disableStatistics );
    settingDialogDiv.find("#disableVisitWebsite ").val(clientStatus.disableVisitWebsite );

    settingDialogDiv.find("#entryPageMinCount").val(clientStatus.entryPageMinCount);
    settingDialogDiv.find("#entryPageMaxCount").val(clientStatus.entryPageMaxCount);
    settingDialogDiv.find("#pageRemainMinTime").val(clientStatus.pageRemainMinTime);
    settingDialogDiv.find("#pageRemainMaxTime").val(clientStatus.pageRemainMaxTime);
    settingDialogDiv.find("#inputDelayMinTime").val(clientStatus.inputDelayMinTime);
    settingDialogDiv.find("#inputDelayMaxTime").val(clientStatus.inputDelayMaxTime);
    settingDialogDiv.find("#slideDelayMinTime").val(clientStatus.slideDelayMinTime);
    settingDialogDiv.find("#slideDelayMaxTime").val(clientStatus.slideDelayMaxTime);
    settingDialogDiv.find("#titleRemainMinTime").val(clientStatus.titleRemainMinTime);
    settingDialogDiv.find("#titleRemainMaxTime").val(clientStatus.titleRemainMaxTime);
    settingDialogDiv.find("#waitTimeAfterOpenBaidu").val(clientStatus.waitTimeAfterOpenBaidu);
    settingDialogDiv.find("#waitTimeBeforeClick").val(clientStatus.waitTimeBeforeClick);
    settingDialogDiv.find("#waitTimeAfterClick").val(clientStatus.waitTimeAfterClick);
    settingDialogDiv.find("#maxUserCount").val(clientStatus.maxUserCount);
    settingDialogDiv.find("#optimizeKeywordCountPerIP").val(clientStatus.optimizeKeywordCountPerIP);

    settingDialogDiv.find("#oneIPOneUser")[0].checked = (clientStatus.oneIPOneUser == 1) ? true : false;
    settingDialogDiv.find("#randomlyClickNoResult")[0].checked = (clientStatus.randomlyClickNoResult == 1) ? true : false;
    settingDialogDiv.find("#justVisitSelfPage")[0].checked = (clientStatus.justVisitSelfPage == 1) ? true : false;
    settingDialogDiv.find("#sleepPer2Words")[0].checked = (clientStatus.sleepPer2Words == 1) ? true : false;
    settingDialogDiv.find("#supportPaste")[0].checked = (clientStatus.supportPaste == 1) ? true : false;
    settingDialogDiv.find("#moveRandomly")[0].checked = (clientStatus.moveRandomly == 1) ? true : false;
    settingDialogDiv.find("#parentSearchEntry")[0].checked = (clientStatus.parentSearchEntry == 1) ? true : false;
    settingDialogDiv.find("#clearLocalStorage")[0].checked = (clientStatus.clearLocalStorage == 1) ? true : false;
    settingDialogDiv.find("#lessClickAtNight")[0].checked = (clientStatus.lessClickAtNight == 1) ? true : false;
    settingDialogDiv.find("#sameCityUser")[0].checked = (clientStatus.sameCityUser == 1) ? true : false;
    settingDialogDiv.find("#locateTitlePosition")[0].checked = (clientStatus.locateTitlePosition == 1) ? true : false;
    settingDialogDiv.find("#baiduAllianceEntry")[0].checked = (clientStatus.baiduAllianceEntry == 1) ? true : false;
    settingDialogDiv.find("#justClickSpecifiedTitle")[0].checked = (clientStatus.justClickSpecifiedTitle == 1) ? true : false;
    settingDialogDiv.find("#randomlyClickMoreLink")[0].checked = (clientStatus.randomlyClickMoreLink == 1) ? true : false;
    settingDialogDiv.find("#moveUp20")[0].checked = (clientStatus.moveUp20 == 1) ? true : false;
    settingDialogDiv.find("#optimizeRelatedKeyword")[0].checked = (clientStatus.optimizeRelatedKeyword == 1) ? true : false;

    settingDialogDiv.find("#waitTimeAfterOpenBaidu").val(clientStatus.waitTimeAfterOpenBaidu);
    settingDialogDiv.find("#waitTimeBeforeClick").val(clientStatus.waitTimeBeforeClick);
    settingDialogDiv.find("#waitTimeAfterClick").val(clientStatus.waitTimeAfterClick);
    settingDialogDiv.find("#maxUserCount").val(clientStatus.maxUserCount);

    settingDialogDiv.find("#switchGroupName").val(clientStatus.switchGroupName != null ? clientStatus.switchGroupName : "");
    settingDialogDiv.find("#host").val(clientStatus.host != null ? clientStatus.host : "");
    settingDialogDiv.find("#port").val(clientStatus.port != null ? clientStatus.port : "");
    settingDialogDiv.find("#csUserName").val(clientStatus.userName != null ? clientStatus.userName : "Administrator");
    settingDialogDiv.find("#password").val(clientStatus.password != null ? clientStatus.password : "doshows123");
    settingDialogDiv.find("#broadbandAccount").val(clientStatus.broadbandAccount != null ? clientStatus.broadbandAccount : "");
    settingDialogDiv.find("#broadbandPassword").val(clientStatus.broadbandPassword != null ? clientStatus.broadbandPassword : "");
    settingDialogDiv.find("#vpsBackendSystemComputerID").val(clientStatus.vpsBackendSystemComputerID != null ? clientStatus.vpsBackendSystemComputerID :
        "");
    settingDialogDiv.find("#vpsBackendSystemPassword").val(clientStatus.vpsBackendSystemPassword != null ? clientStatus.vpsBackendSystemPassword : "doshows123");
    settingDialogDiv.show();
}
function saveChangeSetting(self){
    var settingDialogDiv = $("#changeSettingDialog");
    var clientStatus = {};
    clientStatus.clientID = settingDialogDiv.find("#settingClientID").val();
    clientStatus.group = settingDialogDiv.find("#settingGroup").val();
    clientStatus.operationType = settingDialogDiv.find("#settingOperationType").val();
    clientStatus.pageSize = settingDialogDiv.find("#pageSize").val();
    clientStatus.page = settingDialogDiv.find("#page").val();
    clientStatus.dragPercent = settingDialogDiv.find("#dragPercent").val();
    clientStatus.zhanneiPercent = settingDialogDiv.find("#zhanneiPercent").val();
    clientStatus.zhanwaiPercent = settingDialogDiv.find("#zhanwaiPercent").val();
    clientStatus.kuaizhaoPercent = settingDialogDiv.find("#kuaizhaoPercent").val();
    clientStatus.baiduSemPercent = settingDialogDiv.find("#baiduSemPercent").val();
    clientStatus.multiBrowser = settingDialogDiv.find("#multiBrowser").val();
    clientStatus.clearCookie = settingDialogDiv.find("#clearCookie").val();
    clientStatus.allowSwitchGroup = settingDialogDiv.find("#allowSwitchGroup").val();
    clientStatus.disableStatistics = settingDialogDiv.find("#disableStatistics").val();
    clientStatus.disableVisitWebsite = settingDialogDiv.find("#disableVisitWebsite").val();

    clientStatus.switchGroupName = settingDialogDiv.find("#switchGroupName").val();
    clientStatus.host = settingDialogDiv.find("#host").val();
    clientStatus.port = settingDialogDiv.find("#port").val();
    clientStatus.userName = settingDialogDiv.find("#csUserName").val();
    clientStatus.password = settingDialogDiv.find("#password").val();
    clientStatus.broadbandAccount = settingDialogDiv.find("#broadbandAccount").val();
    clientStatus.broadbandPassword = settingDialogDiv.find("#broadbandPassword").val();
    clientStatus.vpsBackendSystemComputerID = settingDialogDiv.find("#vpsBackendSystemComputerID").val();
    clientStatus.vpsBackendSystemPassword = settingDialogDiv.find("#vpsBackendSystemPassword").val();

    clientStatus.entryPageMinCount = settingDialogDiv.find("#entryPageMinCount").val();
    clientStatus.entryPageMaxCount = settingDialogDiv.find("#entryPageMaxCount").val();
    clientStatus.disableVisitWebsite = settingDialogDiv.find("#disableVisitWebsite").val();
    clientStatus.pageRemainMinTime = settingDialogDiv.find("#pageRemainMinTime").val();
    clientStatus.pageRemainMaxTime = settingDialogDiv.find("#pageRemainMaxTime").val();
    clientStatus.inputDelayMinTime = settingDialogDiv.find("#inputDelayMinTime").val();
    clientStatus.inputDelayMaxTime = settingDialogDiv.find("#inputDelayMaxTime").val();
    clientStatus.slideDelayMinTime = settingDialogDiv.find("#slideDelayMinTime").val();
    clientStatus.slideDelayMaxTime = settingDialogDiv.find("#slideDelayMaxTime").val();
    clientStatus.titleRemainMinTime = settingDialogDiv.find("#titleRemainMinTime").val();
    clientStatus.titleRemainMaxTime = settingDialogDiv.find("#titleRemainMaxTime").val();
    clientStatus.waitTimeAfterOpenBaidu = settingDialogDiv.find("#waitTimeAfterOpenBaidu").val();
    clientStatus.waitTimeBeforeClick = settingDialogDiv.find("#waitTimeBeforeClick").val();
    clientStatus.waitTimeAfterClick = settingDialogDiv.find("#waitTimeAfterClick").val();
    clientStatus.maxUserCount = settingDialogDiv.find("#maxUserCount").val();
    clientStatus.optimizeKeywordCountPerIP = settingDialogDiv.find("#optimizeKeywordCountPerIP").val();

    clientStatus.oneIPOneUser = settingDialogDiv.find("#oneIPOneUser:checked").val() === '1' ? 1 : 0;
    clientStatus.randomlyClickNoResult = settingDialogDiv.find("#randomlyClickNoResult:checked").val() === '1' ? 1 : 0;
    clientStatus.justVisitSelfPage = settingDialogDiv.find("#justVisitSelfPage:checked").val() === '1' ? 1 : 0;
    clientStatus.sleepPer2Words = settingDialogDiv.find("#sleepPer2Words:checked").val() === '1' ? 1 : 0;
    clientStatus.supportPaste = settingDialogDiv.find("#supportPaste:checked").val() === '1' ? 1 : 0;
    clientStatus.moveRandomly = settingDialogDiv.find("#moveRandomly:checked").val() === '1' ? 1 : 0;
    clientStatus.parentSearchEntry = settingDialogDiv.find("#parentSearchEntry:checked").val() === '1' ? 1 : 0;
    clientStatus.clearLocalStorage = settingDialogDiv.find("#clearLocalStorage:checked").val() === '1' ? 1 : 0;
    clientStatus.lessClickAtNight = settingDialogDiv.find("#lessClickAtNight:checked").val() === '1' ? 1 : 0;
    clientStatus.sameCityUser = settingDialogDiv.find("#sameCityUser:checked").val() === '1' ? 1 : 0;
    clientStatus.locateTitlePosition = settingDialogDiv.find("#locateTitlePosition:checked").val() === '1' ? 1 : 0;
    clientStatus.baiduAllianceEntry = settingDialogDiv.find("#baiduAllianceEntry:checked").val() === '1' ? 1 : 0;
    clientStatus.justClickSpecifiedTitle = settingDialogDiv.find("#justClickSpecifiedTitle:checked").val() === '1' ? 1 : 0;
    clientStatus.randomlyClickMoreLink = settingDialogDiv.find("#randomlyClickMoreLink:checked").val() === '1' ? 1 : 0;
    clientStatus.moveUp20 = settingDialogDiv.find("#moveUp20:checked").val() === '1' ? 1 : 0;
    clientStatus.optimizeRelatedKeyword = settingDialogDiv.find("#optimizeRelatedKeyword:checked").val() === '1' ? 1 : 0;
    $.ajax({
        url: '/internal/clientstatus/saveClientStatus',
        data: JSON.stringify(clientStatus),
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
function showTargetVersionSettingDialog(self){
    if(getSelectedClientIDs().trim() === ''){
        alert("请选择要更新的终端！");
        return;
    }
    $("#targetVersionSettingDialog").find("#settingTargetVersion").val("");
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
function saveTargetVersionSetting(self){
    var settingDialogDiv = $("#targetVersionSettingDialog");
    var clientStatus = {};
    clientStatus.clientIDs = getSelectedClientIDs();
    clientStatus.targetVersion = settingDialogDiv.find("#settingTargetVersion").val();
    if(clientStatus.targetVersion.trim() === ''){
        alert("请输入目标版本！");
        return;
    }
    clientStatus.clientIDs = clientStatus.clientIDs.split(",");
    $.ajax({
        url: '/internal/clientstatus/updateClientStatusTargetVersion',
        data: JSON.stringify(clientStatus),
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
    var clientStatus = {};
    clientStatus.clientIDs = getSelectedClientIDs();
    clientStatus.settingType = settingDialogDiv.find("input[name=settingType]:checked").val();
    clientStatus.renewalDate = settingDialogDiv.find("#renewalDate").val();
    if(clientStatus.settingType.trim() === 'specificDate'){
        if(clientStatus.renewalDate.trim() === ''){
            alert("请输入日期！");
            return;
        }
        if(!isDate(clientStatus.renewalDate.trim())){
            alert("日期格式不对！");
            return;
        }
    }
    $.ajax({
        url: '/internal/clientstatus/updateClientStatusRenewalDate',
        data: JSON.stringify(clientStatus),
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
function connectVNC(clientID){
    var obj = new ActiveXObject("wscript.shell");
    obj.run("file:///C:/vnc/" + clientID + ".vnc");
    obj = null;
}

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
        url: '/internal/clientstatus/updateStartUpStatusForCompleted',
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