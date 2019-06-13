$(function () {
    $('#websiteDialog').dialog("close");
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});
function changePaging(currentPage, pageSize) {
    var searchWebsiteForm = $("#searchWebsiteForm");
    searchWebsiteForm.find("#currentPageNumberHidden").val(currentPage);
    searchWebsiteForm.find("#pageSizeHidden").val(pageSize);
    searchWebsiteForm.submit();
}
function resetPageNumber() {
    var searchWebsiteForm = $("#searchWebsiteForm");
    var websiteName = searchWebsiteForm.find("#websiteName").val();
    var domain = searchWebsiteForm.find("#domain").val();
    var accessFailCount = searchWebsiteForm.find("#accessFailCount").val();
    if(websiteName != "") {
        searchWebsiteForm.find("#websiteName").val($.trim(websiteName));
    }
    if(domain != "") {
        searchWebsiteForm.find("#domain").val($.trim(domain));
    }
    if(accessFailCount != "") {
        searchWebsiteForm.find("#accessFailCount").val($.trim(accessFailCount));
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
    websiteForm.find("#backgroundDomain").val(websiteInfo.backgroundDomain);
    websiteForm.find("#backgroundUserName").val(websiteInfo.backgroundUserName);
    websiteForm.find("#backgroundPassword").val(websiteInfo.backgroundPassword);
    websiteForm.find("#databaseName").val(websiteInfo.databaseName);
    websiteForm.find("#databaseUserName").val(websiteInfo.databaseUserName);
    websiteForm.find("#databasePassword").val(websiteInfo.databasePassword);
    websiteForm.find("#serverIP").val(websiteInfo.serverIP);
    websiteForm.find("#serverUserName").val(websiteInfo.serverUserName);
    websiteForm.find("#serverPassword").val(websiteInfo.serverPassword);
    websiteForm.find("#expiryTime").val(userDate(websiteInfo.expiryTime));
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
        width: 300,
        height: 365,
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
    websiteObj.backgroundDomain = $.trim($("#websiteForm").find("#backgroundDomain").val());
    websiteObj.backgroundUserName = $.trim($("#websiteForm").find("#backgroundUserName").val());
    websiteObj.backgroundPassword = $.trim($("#websiteForm").find("#backgroundPassword").val());
    websiteObj.databaseName = $.trim($("#websiteForm").find("#databaseName").val());
    websiteObj.databaseUserName = $.trim($("#websiteForm").find("#databaseUserName").val());
    websiteObj.databasePassword = $.trim($("#websiteForm").find("#databasePassword").val());
    websiteObj.serverIP = $.trim($("#websiteForm").find("#serverIP").val());
    websiteObj.serverUserName = $.trim($("#websiteForm").find("#serverUserName").val());
    websiteObj.serverPassword = $.trim($("#websiteForm").find("#serverPassword").val());
    if(websiteObj.domain == null || websiteObj.domain=="" || websiteObj.domain ==''){
        alert("域名不能为空");
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
function updateSalesInfo() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要更新的网站');
        return;
    }
    if (confirm("确定要更新销售信息到这些网站吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/website/updateSalesInfo',
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