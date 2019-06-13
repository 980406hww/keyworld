$(function () {
    $("#friendlyLinkDialog").dialog("close");
    $("#showFriendlyLinkTableDiv").css("margin-top",$("#topDiv").height());
    pageLoad();
});
function pageLoad() {
    var searchFriendlyLinkForm = $("#searchFriendlyLinkForm");
    var pageSize = searchFriendlyLinkForm.find('#pageSizeHidden').val();
    var pages = searchFriendlyLinkForm.find('#pagesHidden').val();
    var currentPageNumber = searchFriendlyLinkForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    showCustomerBottomDiv.find("#chooseRecords").val(pageSize);

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
function deleteBatchFriendlyLink(websiteUuid) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的友情链接');
        return;
    }
    if (confirm("确实要删除这些友情链接吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.websiteUuid = websiteUuid;
    $.ajax({
        url: '/internal/friendlyLink/delFriendlyLinks',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
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
function showFriendlyLinkDialog(websiteUuid, uuid) {
    if (websiteUuid == null) {
        $('#friendlyLinkForm')[0].reset();
    }else {
        $.ajax({
            url: "/internal/friendlyLink/searchCustomerList",
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
    }
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
                saveFriendlyLink(websiteUuid, uuid);
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
function saveFriendlyLink(websiteUuid, uuid) {
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
    formData.append('websiteUuid', websiteUuid);
    formData.append('customerInfo', $.trim(customerInfo));
    formData.append('customerUuid', $.trim(customerUuid));
    formData.append('friendlyLinkWebName', $.trim(friendlyLinkWebName));
    formData.append('friendlyLinkUrl', $.trim(friendlyLinkUrl));
    formData.append('friendlyLinkIsCheck', $.trim(friendlyLinkIsCheck));
    formData.append('friendlyLinkSortRank', $.trim(friendlyLinkSortRank));
    formData.append('friendlyLinkType', $.trim(friendlyLinkType));
    formData.append('friendlyLinkMsg', $.trim(friendlyLinkMsg));
    formData.append('expirationTime', $.trim(expirationTime));
    formData.append('friendlyLinkEmail', $.trim(friendlyLinkEmail));
    formData.append('friendlyLinkLogo', $.trim(friendlyLinkLogo));
    if (uuid == 0){
        $.ajax({
            url: '/internal/friendlyLink/saveFriendlyLink',
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false,
            timeout: 5000,
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
        formData.append('uuid', $.trim(uuid));
        formData.append('originalSortRank', friendlyLinkForm.find("#originalSortRank").val());
        formData.append('friendlyLinkId', friendlyLinkForm.find("#friendlyLinkId").val());
        $.ajax({
            url: '/internal/friendlyLink/updateFriendlyLink',
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
function modifyFriendlyLink(uuid) {
    getFriendlyLink(uuid, function (friendlyLink) {
        if (friendlyLink != null) {
            initFriendlyLinkDialog(friendlyLink);
            showFriendlyLinkDialog(friendlyLink.websiteUuid, uuid);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getFriendlyLink(uuid, callback) {
    $.ajax({
        url: '/internal/friendlyLink/getFriendlyLink/' + uuid,
        type: 'Get',
        success: function (friendlyLink) {
            callback(friendlyLink);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function delFriendlyLink(uuid) {
    if (confirm("确实要删除这个友情链接吗?") == false)
        return;
    $.ajax({
        url: '/internal/friendlyLink/delFriendlyLink/' + uuid,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'Get',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "删除成功",true);
            } else {
                $().toastmessage('showErrorToast', "删除失败",true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败",true);
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
    friendlyLinkForm.find("#friendlyLinkType").val(friendlyLink.friendlyLinkType);
    friendlyLinkForm.find($('input:radio[name="friendlyLinkIsCheck"]:checked').val(friendlyLink.friendlyLinkIsCheck));
    friendlyLinkForm.find("#expirationTime").val(userDate(friendlyLink.expirationTime));
    friendlyLinkForm.find("#friendlyLinkMsg").val(friendlyLink.friendlyLinkMsg);
}

function changePaging(currentPage, pageSize) {
    var searchFriendlyLinkForm = $("#searchFriendlyLinkForm");
    searchFriendlyLinkForm.find("#currentPageNumberHidden").val(currentPage);
    searchFriendlyLinkForm.find("#pageSizeHidden").val(pageSize);
    searchFriendlyLinkForm.submit();
}

function resetPageNumber() {
    var searchFriendlyLinkForm = $("#searchFriendlyLinkForm");
    var customerInfo = searchFriendlyLinkForm.find("#customerInfo").val();
    if(customerInfo != "") {
        searchFriendlyLinkForm.find("#customerInfo").val($.trim(customerInfo));
    }
    var friendlyLinkName = searchFriendlyLinkForm.find("#friendlyLinkName").val();
    if(friendlyLinkName != "") {
        searchFriendlyLinkForm.find("#friendlyLinkName").val($.trim(friendlyLinkName));
    }
    var friendlyLinkUrl = searchFriendlyLinkForm.find("#friendlyLinkUrl").val();
    if(friendlyLinkUrl != "") {
        searchFriendlyLinkForm.find("#friendlyLinkUrl").val($.trim(friendlyLinkUrl));
    }
    searchFriendlyLinkForm.submit();
}

function userDate(uData){
    var myDate = new Date(uData);
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    return year + '-' + month + '-' + day;
}