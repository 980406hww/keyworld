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
                var websiteForm = $("#websiteForm");
                websiteForm.find("#websiteName").val(websiteInfo.websiteName);
                websiteForm.find("#domain").val(websiteInfo.domain);
                websiteForm.find("#industry").val(websiteInfo.industry);
                websiteForm.find("#accessFailCount").val(websiteInfo.accessFailCount);
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
    $("#websiteDialog").dialog({
        resizable: false,
        width: 320,
        height: 170,
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
function resetAccessFailCount() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要重置失败次数的网站');
        return;
    }
    if (confirm("确定要重置失败次数吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/website/resetAccessFailCount',
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
function saveWebsite(uuid) {
    var websiteObj = {};
    if(uuid != null) {
        websiteObj.uuid = uuid;
    }
    websiteObj.websiteName = $.trim($("#websiteForm").find("#websiteName").val());
    websiteObj.domain = $.trim($("#websiteForm").find("#domain").val());
    websiteObj.industry = $.trim($("#websiteForm").find("#industry").val());
    websiteObj.accessFailCount = $.trim($("#websiteForm").find("#accessFailCount").val());
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