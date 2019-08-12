$(function () {
    $('#websiteDialog').dialog("close");
    $("#advertisingDialog").dialog("close");
    $("#friendlyLinkDialog").dialog("close");
    $("#synchronousFriendlyLinkDialog").dialog("close");
    $("#synchronousAdvertisingDialog").dialog("close");
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});
function changePaging(currentPage, pageSize) {
    var searchWebsiteForm = $("#searchWebsiteForm");
    var friendlyLinkUrl = searchWebsiteForm.find("#friendlyLinkUrl").val();
    if ("http://" === friendlyLinkUrl) {
        searchWebsiteForm.find("#friendlyLinkUrl").val('');
    } else {
        searchWebsiteForm.find("#friendlyLinkUrl").val($.trim(friendlyLinkUrl));
    }
    searchWebsiteForm.find("#currentPageNumberHidden").val(currentPage);
    searchWebsiteForm.find("#pageSizeHidden").val(pageSize);
    searchWebsiteForm.submit();
}
function resetPageNumber() {
    var searchWebsiteForm = $("#searchWebsiteForm");
    var websiteName = searchWebsiteForm.find("#websiteName").val();
    var updateSalesInfoSign = searchWebsiteForm.find("#updateSalesInfoSign").val();
    var synchronousFriendlyLinkSign = searchWebsiteForm.find("#synchronousFriendlyLinkSign").val();
    var synchronousAdvertisingSign = searchWebsiteForm.find("#synchronousAdvertisingSign").val();
    var domain = searchWebsiteForm.find("#domain").val();
    var accessFailCount = searchWebsiteForm.find("#accessFailCount").val();
    var friendlyLinkUrl = searchWebsiteForm.find("#friendlyLinkUrl").val();
    var advertisingTagname = searchWebsiteForm.find("#advertisingTagname").val();
    if(websiteName != "") {
        searchWebsiteForm.find("#websiteName").val($.trim(websiteName));
    }
    if(domain != "") {
        searchWebsiteForm.find("#domain").val($.trim(domain));
    }
    if(updateSalesInfoSign != "") {
        searchWebsiteForm.find("#updateSalesInfoSign").val($.trim(updateSalesInfoSign));
    }
    if(synchronousFriendlyLinkSign != "") {
        searchWebsiteForm.find("#synchronousFriendlyLinkSign").val($.trim(synchronousFriendlyLinkSign));
    }
    if(synchronousAdvertisingSign != "") {
        searchWebsiteForm.find("#synchronousAdvertisingSign").val($.trim(synchronousAdvertisingSign));
    }
    if(accessFailCount != "") {
        searchWebsiteForm.find("#accessFailCount").val($.trim(accessFailCount));
    }
    if(friendlyLinkUrl != "") {
        if ('http://' == friendlyLinkUrl) {
            searchWebsiteForm.find("#friendlyLinkUrl").val("");
        }else {
            searchWebsiteForm.find("#friendlyLinkUrl").val($.trim(friendlyLinkUrl));
        }
    }
    if(advertisingTagname != "") {
        searchWebsiteForm.find("#advertisingTagname").val($.trim(advertisingTagname));
    }
    searchWebsiteForm.find("#currentPageNumberHidden").val(1);
}
function pageLoad() {
    var searchWebsiteForm = $("#searchWebsiteForm");
    var pageSize = searchWebsiteForm.find('#pageSizeHidden').val();
    var pages = searchWebsiteForm.find('#pagesHidden').val();
    var currentPageNumber = searchWebsiteForm.find('#currentPageNumberHidden').val();
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
function selectAll(self) {
    var a = document.getElementsByName("uuid");
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
function decideSelectAll() {
    var a = document.getElementsByName("uuid");
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
function getSelectedIDs() {
    var uuids = '';
    $.each($("input[name=uuid]:checkbox:checked"), function () {
        if (uuids === '') {
            uuids = $(this).val();
        } else {
            uuids = uuids + "," + $(this).val();
        }
    });
    return uuids;
}
function editWebsiteInfo(uuid) {
    $.ajax({
        url: '/internal/website/getWebsite/' + uuid,
        type: 'Get',
        success: function (websiteInfo) {
            if (websiteInfo != null) {
                assignment(websiteInfo);
                showWebsiteDialog(websiteInfo.uuid);
            } else {
                $().toastmessage('showErrorToast', "获取信息失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}

function assignment(websiteInfo) {
    var websiteForm = $("#websiteForm");
    websiteForm.find("#websiteName").val(websiteInfo.websiteName);
    websiteForm.find("#industry").val(websiteInfo.industry);
    websiteForm.find("#domain").val(websiteInfo.domain);
    websiteForm.find("#registrar").val(websiteInfo.registrar);
    websiteForm.find("#analysis").val(websiteInfo.analysis);
    websiteForm.find("#backendDomain").val(websiteInfo.backendDomain);
    websiteForm.find("#backendUserName").val(websiteInfo.backendUserName);
    websiteForm.find("#backendPassword").val(websiteInfo.backendPassword);
    websiteForm.find("#databaseName").val(websiteInfo.databaseName);
    websiteForm.find("#databaseUserName").val(websiteInfo.databaseUserName);
    websiteForm.find("#databasePassword").val(websiteInfo.databasePassword);
    websiteForm.find("#serverIP").val(websiteInfo.serverIP);
    websiteForm.find("#serverUserName").val(websiteInfo.serverUserName);
    websiteForm.find("#serverPassword").val(websiteInfo.serverPassword);
    websiteForm.find("#dnsAnalysisStatus").val(websiteInfo.dnsAnalysisStatus);
    websiteForm.find("#expiryTime").val(userDate(websiteInfo.expiryTime));
    $("#websiteForm #websiteType option[value='" + websiteInfo.websiteType + "']").prop("selected", "selected");
}
function userDate(uData){
    var myDate = new Date(uData);
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    return year + '-' + month + '-' + day;
}
function deleteWebsite(uuid) {
    if (confirm("确认要删除这个网站吗?") == false) return;
    $.ajax({
        url: '/internal/website/deleteWebsite/' + uuid,
        type: 'Get',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "删除成功",true);
            } else {
                $().toastmessage('showErrorToast', "删除失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败");
        }
    });
}
function deleteWebsites() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的网站');
        return;
    }
    if (confirm("确定要删除这些网站吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/website/deleteWebsites',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
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
function showWebsiteDialog(uuid) {
    if (uuid == null) {
        $('#websiteForm')[0].reset();
    }
    $("#websiteDialog").show();
    $("#websiteDialog").dialog({
        resizable: false,
        width: 310,
        height: 465,
        modal: true,
        title: '网站信息',
        closed:true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveWebsite(uuid);
            }
        },
        {
            text: '清空',
            iconCls: 'fi-trash',
            handler: function () {
                $('#websiteForm')[0].reset();
            }
        },
        {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $('#websiteDialog').dialog("close");
                $('#websiteForm')[0].reset();
            }
        }]
    });
    $("#websiteDialog").dialog("open");
    $('#websiteDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveWebsite(uuid) {
    var websiteObj = {};
    if(uuid != null) {
        websiteObj.uuid = uuid;
    }
    websiteObj.websiteName = $.trim($("#websiteForm").find("#websiteName").val());
    websiteObj.industry = $.trim($("#websiteForm").find("#industry").val());
    websiteObj.domain = $.trim($("#websiteForm").find("#domain").val());
    websiteObj.registrar = $.trim($("#websiteForm").find("#registrar").val());
    websiteObj.analysis = $.trim($("#websiteForm").find("#analysis").val());
    websiteObj.expiryTime = $.trim($("#websiteForm").find("#expiryTime").val());
    websiteObj.backendDomain = $.trim($("#websiteForm").find("#backendDomain").val());
    websiteObj.backendUserName = $.trim($("#websiteForm").find("#backendUserName").val());
    websiteObj.backendPassword = $.trim($("#websiteForm").find("#backendPassword").val());
    websiteObj.websiteType = $.trim($("#websiteForm").find("#websiteType").val());
    websiteObj.databaseName = $.trim($("#websiteForm").find("#databaseName").val());
    websiteObj.databaseUserName = $.trim($("#websiteForm").find("#databaseUserName").val());
    websiteObj.databasePassword = $.trim($("#websiteForm").find("#databasePassword").val());
    websiteObj.serverIP = $.trim($("#websiteForm").find("#serverIP").val());
    websiteObj.serverUserName = $.trim($("#websiteForm").find("#serverUserName").val());
    websiteObj.serverPassword = $.trim($("#websiteForm").find("#serverPassword").val());
    websiteObj.dnsAnalysisStatus = $.trim($("#websiteForm").find("#dnsAnalysisStatus").val());
    if(websiteObj.domain == null || websiteObj.domain=="" || websiteObj.domain ==''){
        alert("域名不能为空");
        return;
    }
    if(websiteObj.websiteType == null || websiteObj.websiteType == ""){
        alert("请选择网站类型");
        return;
    }
    $.ajax({
        url: '/internal/website/saveWebsite',
        data: JSON.stringify(websiteObj),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
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
function putSalesInfoToWebsite() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要推送的网站');
        return;
    }
    if (confirm("确定要更新销售信息到这些网站吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/website/putSalesInfoToWebsite',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
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

function showBatchAddFriendlyLinkDialog(friendlyLinkUrl) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要增加友链的网站');
        return;
    }
    if (friendlyLinkUrl == null){
        $('#friendlyLinkForm')[0].reset();
    }
    $.ajax({
        url: "/internal/customer/searchCustomerList",
        type: "GET",
        success: function (customerList) {
            $("#customer_list").find('option').remove();
            if (customerList != null) {
                $.each(customerList, function (idx, val) {
                    $("#customer_list").append("<option value='" + val.contactPerson + "_" + val.uuid + "'></option>")
                });
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败1！");
        }
    });
    $("#friendlyLinkDialog").show();
    $("#friendlyLinkDialog").dialog({
        resizable: false,
        width: 290,
        height: 500,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveFriendlyLink(uuids, friendlyLinkUrl);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#friendlyLinkForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#friendlyLinkDialog").dialog("close");
                    $('#friendlyLinkForm')[0].reset();
                }
            }]
    });
    $("#friendlyLinkDialog").dialog("open");
    $('#friendlyLinkDialog').window("resize",{top:$(document).scrollTop() + 100});
}

function saveFriendlyLink(uuids, originalFriendlyLinkUrl) {
    var friendlyLinkForm = $("#friendlyLinkDialog").find("#friendlyLinkForm");
    var customerInfo = friendlyLinkForm.find("#customerInfo").val();
    if (customerInfo == "") {
        alert("请选择用户名称");
        friendlyLinkForm.find("#customerInfo").focus();
        return;
    }
    var customerUuid = customerInfo.split('_')[customerInfo.split('_').length - 1];
    var friendlyLinkWebName = friendlyLinkForm.find("#friendlyLinkWebName").val();
    if (friendlyLinkWebName == "") {
        alert("请输入友链网站名称");
        friendlyLinkForm.find("#friendlyLinkWebName").focus();
        return;
    }
    var friendlyLinkUrl = friendlyLinkForm.find("#friendlyLinkUrl").val();
    if (friendlyLinkUrl == "") {
        alert("请输入友链网址");
        friendlyLinkForm.find("#friendlyLinkUrl").focus();
        return;
    }
    var friendlyLinkLogoFile = friendlyLinkForm.find("#friendlyLinkLogo").val();
    if (friendlyLinkLogoFile != "") {
        var fileTypes = new Array("jpg", "png", "gif");
        var fileTypeFlag = false;
        var newFileName = friendlyLinkLogoFile.split('.');
        newFileName = newFileName[newFileName.length - 1];
        for (var i = 0; i < fileTypes.length; i++) {
            if (fileTypes[i] == newFileName) {
                fileTypeFlag = true;
                break;
            }
        }
        if (!fileTypeFlag) {
            alert("请提交正确图片 .jpg .png .gif");
            return false;
        }
    }
    var friendlyLinkSortRank = friendlyLinkForm.find("#friendlyLinkSortRank").val();
    if (friendlyLinkSortRank  ==  "") {
        friendlyLinkSortRank = -1;
    }
    var expirationTime = friendlyLinkForm.find("#expirationTime").val();
    if (expirationTime == ""){
        alert("请选择到期时间：");
        friendlyLinkForm.find("#expirationTime").focus();
        return;
    }
    var friendlyLinkMsg = friendlyLinkForm.find("#friendlyLinkMsg").val();
    var friendlyLinkEmail = friendlyLinkForm.find("#friendlyLinkEmail").val();
    var friendlyLinkType = friendlyLinkForm.find("#friendlyLinkType").val();
    var friendlyLinkLogo = friendlyLinkForm.find('#friendlyLinkLogo')[0].files[0];
    var friendlyLinkIsCheck = $('input:radio[name="friendlyLinkIsCheck"]:checked').val();
    var formData = new FormData();
    formData.append('file', friendlyLinkForm.find('#friendlyLinkLogo')[0].files[0]);
    formData.append('customerInfo', $.trim(customerInfo));
    formData.append('customerUuid', $.trim(customerUuid));
    formData.append('friendlyLinkWebName', $.trim(friendlyLinkWebName));
    formData.append('friendlyLinkUrl', $.trim(friendlyLinkUrl));
    formData.append('friendlyLinkIsCheck', $.trim(friendlyLinkIsCheck));
    formData.append('friendlyLinkSortRank', $.trim(friendlyLinkSortRank));
    formData.append('friendlyLinkType', $.trim(friendlyLinkType));
    var friendlyLinkTypeId = friendlyLinkType.split('_')[friendlyLinkType.split('_').length - 1];
    formData.append('friendlyLinkTypeId', $.trim(friendlyLinkTypeId));
    formData.append('friendlyLinkMsg', $.trim(friendlyLinkMsg));
    formData.append('expirationTime', $.trim(expirationTime));
    formData.append('friendlyLinkEmail', $.trim(friendlyLinkEmail));
    formData.append('friendlyLinkLogo', $.trim(friendlyLinkLogo));
    formData.append('uuids', $.trim(uuids));
    if (originalFriendlyLinkUrl == null){
        $.ajax({
            url: '/internal/website/batchSaveFriendlyLink',
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "保存成功",true);
                } else {
                    $().toastmessage('showErrorToast', "保存失败");

                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "保存失败");
            }
        });
    } else {
        formData.append('originalFriendlyLinkUrl', $.trim(originalFriendlyLinkUrl));
        $.ajax({
            url: '/internal/website/batchUpdateFriendlyLink',
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "保存成功",true);
                } else {
                    $().toastmessage('showErrorToast', "保存失败");

                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "保存失败");
            }
        });
    }
    $("#friendlyLinkDialog").dialog("close");
    $('#friendlyLinkForm')[0].reset();
}

function batchModifyFriendlyLink(friendlyLinkUrl) {
    if (friendlyLinkUrl == null || friendlyLinkUrl === "") {
        alert("请选择先查询含有指定友链网站");
        var searchWebsiteForm = $("#searchWebsiteForm");
        searchWebsiteForm.find("#friendlyLinkUrl").focus();
        return;
    }
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要增加友链的网站');
        return;
    }
    var uuid = uuids.split(",")[0];
    getFriendlyLink(uuid, friendlyLinkUrl, function (friendlyLink) {
        if (friendlyLink != null) {
            initFriendlyLinkDialog(friendlyLink);
            showBatchAddFriendlyLinkDialog(friendlyLinkUrl);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getFriendlyLink(uuid, friendlyLinkUrl, callback) {
    var postData = {};
    postData.uuid = uuid;
    postData.friendlyLinkUrl = friendlyLinkUrl;
    $.ajax({
        url: '/internal/website/getFriendlyLinkByUrl',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (friendlyLink) {
            callback(friendlyLink);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function initFriendlyLinkDialog(friendlyLink) {
    var friendlyLinkForm = $("#friendlyLinkDialog").find("#friendlyLinkForm");
    friendlyLinkForm.find("#customerInfo").val(friendlyLink.customerInfo + "_" + friendlyLink.customerUuid);
    friendlyLinkForm.find("#friendlyLinkId").val(friendlyLink.friendlyLinkId);
    friendlyLinkForm.find("#friendlyLinkWebName").val(friendlyLink.friendlyLinkWebName);
    friendlyLinkForm.find("#friendlyLinkUrl").val(friendlyLink.friendlyLinkUrl);
    friendlyLinkForm.find("#friendlyLinkSortRank").val(friendlyLink.friendlyLinkSortRank);
    friendlyLinkForm.find("#originalSortRank").val(friendlyLink.friendlyLinkSortRank);
    friendlyLinkForm.find("#friendlyLinkEmail").val(friendlyLink.friendlyLinkEmail);
    friendlyLinkForm.find($('input:radio[name="friendlyLinkIsCheck"]:checked').val(friendlyLink.friendlyLinkIsCheck));
    friendlyLinkForm.find("#expirationTime").val(userDate(friendlyLink.expirationTime));
    friendlyLinkForm.find("#friendlyLinkMsg").val(friendlyLink.friendlyLinkMsg);
}

function batchDelFriendlyLink(friendlyLinkUrl) {
    if (friendlyLinkUrl == null || friendlyLinkUrl === "") {
        alert("请选择先查询含有指定友链网站");
        var searchWebsiteForm = $("#searchWebsiteForm");
        searchWebsiteForm.find("#friendlyLinkUrl").focus();
        return;
    }
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的友情链接');
        return;
    }
    if (confirm("确实要删除所选网站指定友情链接吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.friendlyLinkUrl = friendlyLinkUrl;
    $.ajax({
        url: '/internal/website/batchDelFriendlyLink',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "删除成功",true);
            } else {
                $().toastmessage('showErrorToast', "删除失败",true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}


function showBatchAddAdvertisingDialog(advertisingTagname) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要增加广告的网站');
        return;
    }
    if (advertisingTagname == null){
        $('#advertisingForm')[0].reset();
    }
    $.ajax({
        url: "/internal/customer/searchCustomerList",
        type: "GET",
        success: function (customerList) {
            $("#customer_list").find('option').remove();
            if (customerList != null) {
                $.each(customerList, function (idx, val) {
                    $("#customer_list").append("<option value='" + val.contactPerson + "_" + val.uuid + "'></option>")
                });
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
    if (advertisingTagname == null){
        changeAdvertisingBodySubmit("code");
    }else {
        changeAdvertisingBodySubmit("all");
    }
    $("#advertisingDialog").show();
    $("#advertisingDialog").dialog({
        resizable: false,
        width: 320,
        height: 600,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveAdvertising(uuids, advertisingTagname);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#advertisingForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#advertisingDialog").dialog("close");
                    $('#advertisingForm')[0].reset();
                }
            }]
    });
    $("#advertisingDialog").dialog("open");
    $('#advertisingDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveAdvertising(uuids, originalAdvertisingTagname) {
    var advertisingForm = $("#advertisingDialog").find("#advertisingForm");
    var customerInfo = advertisingForm.find("#customerInfo").val();
    if (customerInfo == "") {
        alert("请选择用户名称");
        advertisingForm.find("#customerInfo").focus();
        return;
    }
    var customerUuid = customerInfo.split('_')[customerInfo.split('_').length - 1];
    var advertisingTagname = advertisingForm.find("#advertisingTagname").val();
    if (advertisingTagname == "") {
        alert("请输入广告标识");
        advertisingForm.find("#advertisingTagname").focus();
        return;
    }
    var advertisingAdName = advertisingForm.find("#advertisingAdName").val();
    if (advertisingAdName == "") {
        alert("请输入广告名称");
        advertisingForm.find("#advertisingAdName").focus();
        return;
    }
    var advertisingType = advertisingForm.find("#advertisingType").val();
    var advertisingTypeId = advertisingType.split('_')[advertisingType.split('_').length - 1];
    var advertisingArcType = advertisingForm.find("#advertisingArcType").val();
    var advertisingArcTypeId = advertisingArcType.split('_')[advertisingArcType.split('_').length - 1];
    var advertisingTimeSet = $('input:radio[name="advertisingTimeSet"]:checked').val();
    var advertisingBodyChecked = $('input:radio[name="advertisingBodyChecked"]:checked').val();
    var advertisingStarttime = advertisingForm.find("#advertisingStarttime").val();
    var advertisingEndtime = advertisingForm.find("#advertisingEndtime").val();
    var advertisingExpbody = advertisingForm.find("#advertisingExpbody").val();
    var advertising = {};
    advertising.uuids = $.trim(uuids);
    advertising.customerInfo = $.trim(customerInfo);
    advertising.customerUuid = $.trim(customerUuid);
    advertising.advertisingAdName = $.trim(advertisingAdName);
    advertising.advertisingTagname = $.trim(advertisingTagname);
    advertising.advertisingType = $.trim(advertisingType);
    advertising.advertisingTypeId = $.trim(advertisingTypeId);
    advertising.advertisingArcType = $.trim(advertisingArcType);
    advertising.advertisingArcTypeId = $.trim(advertisingArcTypeId);
    advertising.advertisingTimeSet = $.trim(advertisingTimeSet);
    advertising.advertisingStarttime = userDate(advertisingStarttime);
    advertising.advertisingEndtime = userDate(advertisingEndtime);
    advertising.advertisingExpbody = $.trim(advertisingExpbody);
    if (originalAdvertisingTagname == null){
        advertising.advertisingBodyChecked = $.trim(advertisingBodyChecked);
        var normbody = {};
        if (advertisingBodyChecked == 0){
            normbody.style = "code";
            normbody.htmlcode = advertisingForm.find("#htmlcode").val();
        }else if (advertisingBodyChecked == 1){
            normbody.style = "txt";
            normbody.title = advertisingForm.find("#txtTitle").val();
            normbody.link = advertisingForm.find("#txtLink").val();
            normbody.color = advertisingForm.find("#txtColor").val();
            normbody.size = advertisingForm.find("#txtSize").val();
        }else if (advertisingBodyChecked == 2){
            normbody.style = "img";
            normbody.url = advertisingForm.find("#imgUrl").val();
            normbody.link = advertisingForm.find("#imgLink").val();
            normbody.width = advertisingForm.find("#imgWidth").val();
            normbody.height = advertisingForm.find("#imgHeight").val();
        }else {
            normbody.style = "flash";
            normbody.link = advertisingForm.find("#flashLink").val();
            normbody.width = advertisingForm.find("#flashWidth").val();
            normbody.height = advertisingForm.find("#flashHeight").val();
        }
        advertising.normbody = normbody;
        $.ajax({
            url: '/internal/website/batchSaveAdvertising',
            data: JSON.stringify(advertising),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "保存成功",true);
                } else {
                    $().toastmessage('showErrorToast', "保存失败, 请检查指定网站该广告标识是否唯一！");

                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "保存失败");
            }
        });
    } else {
        advertising.originalAdvertisingTagname = $.trim(originalAdvertisingTagname);
        var advertisingNormbody = advertisingForm.find("#advertisingNormbody").val();
        advertising.advertisingNormbody = $.trim(advertisingNormbody);
        $.ajax({
            url: '/internal/website/batchUpdateAdvertising',
            data: JSON.stringify(advertising),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "保存成功",true);
                } else {
                    $().toastmessage('showErrorToast', "保存失败");

                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "保存失败");
            }
        });
    }
    $("#advertisingDialog").dialog("close");
    $('#advertisingForm')[0].reset();
}
function batchModifyAdvertising(advertisingTagname) {
    if (advertisingTagname == null || advertisingTagname === "") {
        alert("请选择先查询含有指定广告标识的网站");
        var searchWebsiteForm = $("#searchWebsiteForm");
        searchWebsiteForm.find("#advertisingTagname").focus();
        return;
    }
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要增加广告的网站');
        return;
    }
    var uuid = uuids.split(",")[0];
    getAdvertising(uuid, advertisingTagname, function (advertising) {
        if (advertising != null) {
            initAdvertisingDialog(advertising);
            showBatchAddAdvertisingDialog(advertisingTagname);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getAdvertising(uuid, advertisingTagname, callback) {
    var postData = {};
    postData.uuid = uuid;
    postData.advertisingTagname = advertisingTagname;
    $.ajax({
        url: '/internal/website/getAdvertisingByAdvertisingTagname',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        success: function (advertising) {
            callback(advertising);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function batchDelAdvertising(advertisingTagname) {
    if (advertisingTagname == null || advertisingTagname === "") {
        alert("请选择先查询含有指定广告标识的网站");
        var searchWebsiteForm = $("#searchWebsiteForm");
        searchWebsiteForm.find("#friendlyLinkUrl").focus();
        return;
    }
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的广告');
        return;
    }
    if (confirm("确实要删除这些广告吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.advertisingTagname = advertisingTagname;
    $.ajax({
        url: '/internal/website/batchDelAdvertising',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "删除成功",true);
            } else {
                $().toastmessage('showErrorToast', "删除失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function initAdvertisingDialog(advertising) {
    var advertisingForm = $("#advertisingDialog").find("#advertisingForm");
    advertisingForm.find("#customerInfo").val(advertising.customerInfo + "_" + advertising.customerUuid);
    advertisingForm.find("#advertisingId").val(advertising.advertisingId);
    advertisingForm.find("#advertisingTagname").val(advertising.advertisingTagname);
    advertisingForm.find("#advertisingAdName").val(advertising.advertisingAdName);
    advertisingForm.find($('input:radio[name="advertisingTimeSet"]:checked').val(advertising.advertisingTimeSet));
    advertisingForm.find("#advertisingStarttime").val(userDate(advertising.advertisingStarttime));
    advertisingForm.find("#advertisingEndtime").val(userDate(advertising.advertisingEndtime));
    advertisingForm.find("#advertisingNormbody").val(advertising.advertisingNormbody);
    advertisingForm.find("#advertisingExpbody").val(advertising.advertisingExpbody);
}

function changeAdvertisingBodySubmit(type) {
    var advertisingForm = $("#advertisingDialog").find("#advertisingForm");
    var code = advertisingForm.find("#code");
    var txt = advertisingForm.find("#txt");
    var img = advertisingForm.find("#img");
    var flash = advertisingForm.find("#flash");
    var advertisingBodyCheckedTable = advertisingForm.find("#advertisingBodyCheckedTable");
    var advertisingNormbodyTable = advertisingForm.find("#advertisingNormbodyTable");
    var advertisingTagname = advertisingForm.find('#advertisingTagname');
    code.css("display","none");
    txt.css("display","none");
    img.css("display","none");
    flash.css("display","none");
    advertisingBodyCheckedTable.css("display","none");
    advertisingNormbodyTable.css("display","none");
    advertisingTagname.removeAttr("readonly");
    if ("code" == type){
        advertisingBodyCheckedTable.css("display","block");
        code.css("display","block");
    } else if ("txt" == type){
        advertisingBodyCheckedTable.css("display","block");
        txt.css("display","block");
    } else if ("img" == type){
        advertisingBodyCheckedTable.css("display","block");
        img.css("display","block");
    } else if ("flash" == type) {
        advertisingBodyCheckedTable.css("display","block");
        flash.css("display","block");
    }else {
        advertisingNormbodyTable.css("display","block");
        advertisingTagname.attr("readonly","readonly");
    }
}

function synchronousFriendlyLink() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要同步友链信息的网站');
        return;
    }
    $.ajax({
        url: "/internal/customer/searchCustomerList",
        type: "GET",
        success: function (customerList) {
            $("#customer_list").find('option').remove();
            if (customerList != null) {
                $.each(customerList, function (idx, val) {
                    $("#customer_list").append("<option value='" + val.contactPerson + "_" + val.uuid + "'></option>")
                });
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
    $("#synchronousFriendlyLinkDialog").show();
    $("#synchronousFriendlyLinkDialog").dialog({
        resizable: false,
        width: 280,
        height: 150,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                if (confirm("确定要使用所填信息同步远程友链信息到所选网站吗?") == false) return;
                var synchronousFriendlyLinkForm = $("#synchronousFriendlyLinkDialog").find("#synchronousFriendlyLinkForm");
                var customerInfo = synchronousFriendlyLinkForm.find("#customerInfo").val();
                if (customerInfo == "") {
                    alert("请选择同步数据默认用户名称");
                    synchronousFriendlyLinkForm.find("#customerInfo").focus();
                    return;
                }
                var customerUuid = customerInfo.split('_')[customerInfo.split('_').length - 1];
                var expirationTime = synchronousFriendlyLinkForm.find("#expirationTime").val();
                var renewTime = synchronousFriendlyLinkForm.find("#renewTime").val();
                var postData = {};
                postData.uuids = uuids.split(",");
                postData.customerInfo =  $.trim(customerInfo);
                postData.customerUuid =  $.trim(customerUuid);
                postData.expirationTime =  userDate(expirationTime);
                postData.renewTime =  userDate(renewTime);
                $.ajax({
                    url: '/internal/website/synchronousFriendlyLink',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: 'POST',
                    success: function (data) {
                        if (data) {
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
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#synchronousFriendlyLinkForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#synchronousFriendlyLinkDialog").dialog("close");
                    $('#synchronousFriendlyLinkForm')[0].reset();
                }
            }]
    });
    $("#synchronousFriendlyLinkDialog").dialog("open");
    $('#synchronousFriendlyLinkDialog').window("resize",{top:$(document).scrollTop() + 100});
}

function synchronousAdvertising() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要同步广告信息的网站');
        return;
    }
    $.ajax({
        url: "/internal/customer/searchCustomerList",
        type: "GET",
        success: function (customerList) {
            $("#customer_list").find('option').remove();
            if (customerList != null) {
                $.each(customerList, function (idx, val) {
                    $("#customer_list").append("<option value='" + val.contactPerson + "_" + val.uuid + "'></option>")
                });
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
    $("#synchronousAdvertisingDialog").show();
    $("#synchronousAdvertisingDialog").dialog({
        resizable: false,
        width: 280,
        height: 150,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                if (confirm("确定要使用所填信息同步远程友链信息到所选网站吗?") == false) return;
                var synchronousAdvertisingForm = $("#synchronousAdvertisingDialog").find("#synchronousAdvertisingForm");
                var customerInfo = synchronousAdvertisingForm.find("#customerInfo").val();
                if (customerInfo == "") {
                    alert("请选择同步数据默认用户名称");
                    synchronousAdvertisingForm.find("#customerInfo").focus();
                    return;
                }
                var customerUuid = customerInfo.split('_')[customerInfo.split('_').length - 1];
                var renewTime = synchronousAdvertisingForm.find("#renewTime").val();
                var postData = {};
                postData.uuids = uuids.split(",");
                postData.customerInfo =  $.trim(customerInfo);
                postData.customerUuid =  $.trim(customerUuid);
                postData.renewTime =  userDate(renewTime);
                $.ajax({
                    url: '/internal/website/synchronousAdvertising',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: 'POST',
                    success: function (data) {
                        if (data) {
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
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#synchronousAdvertisingForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#synchronousAdvertisingDialog").dialog("close");
                    $('#synchronousAdvertisingForm')[0].reset();
                }
            }]
    });
    $("#synchronousAdvertisingDialog").dialog("open");
    $('#synchronousAdvertisingDialog').window("resize",{top:$(document).scrollTop() + 100});
}

