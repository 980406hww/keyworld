$(function () {
    $("#screenedWebsiteDialog").dialog("close");
    $("#showScreenedWebsiteTableDiv").css("margin-top",$("#topDiv").height());
    pageLoad();
});
function pageLoad() {
    var searchScreenedWebsiteForm = $("#searchScreenedWebsiteForm");
    var pageSize = searchScreenedWebsiteForm.find('#pageSizeHidden').val();
    var pages = searchScreenedWebsiteForm.find('#pagesHidden').val();
    var currentPageNumber = searchScreenedWebsiteForm.find('#currentPageNumberHidden').val();
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
function deleteBatchScreenedWebsite(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除屏蔽网站的优化组');
        return;
    }
    if (confirm("确实要删除这些优化组的屏蔽网站吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/screenedWebsite/deleteBatchScreenedWebsite',
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
                $().toastmessage('showErrorToast', "操作失败",true);

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
function showScreenedWebsiteDialog(uuid) {
    if (uuid == null) {
        $('#screenedWebsiteForm')[0].reset();
    }
    $("#screenedWebsiteDialog").show();
    $("#screenedWebsiteDialog").dialog({
        resizable: false,
        width: 280,
        height: 380,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                savaScreenedWebsite(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#screenedWebsiteForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#screenedWebsiteDialog").dialog("close");
                    $('#screenedWebsiteForm')[0].reset();
                }
            }]
    });
    $("#screenedWebsiteDialog").dialog("open");
    $('#screenedWebsiteDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function savaScreenedWebsite(uuid) {
    var screenedWebsiteForm = $("#screenedWebsiteDialog").find("#screenedWebsiteForm");
    var optimizeGroupName = screenedWebsiteForm.find("#optimizeGroupName").val();
    if (optimizeGroupName == "") {
        alert("请输入优化组名称");
        screenedWebsiteForm.find("#optimizeGroupName").focus();
        return;
    }
    var screenedWebsiteStr = screenedWebsiteForm.find("#screenedWebsite").val();
    if (optimizeGroupName == "") {
        alert("请输入屏蔽网站");
        screenedWebsiteForm.find("#screenedWebsite").focus();
        return;
    }
    screenedWebsiteStr = screenedWebsiteStr.replace(/[，|\r\n]/g, ",").replace(/[\s+]/g, "");
    var keywords = screenedWebsiteStr.split(',');
    var screenedWebsites = keywords.filter(function (keyword, index) {
        return keywords.indexOf(keyword) === index && keyword != '';
    });
    screenedWebsites.join(',');
    var screenedWebsite = {};
    screenedWebsite.uuid = uuid;
    screenedWebsite.optimizeGroupName = $.trim(optimizeGroupName);
    screenedWebsite.screenedWebsite = $.trim(screenedWebsites);
    $.ajax({
        url: '/internal/screenedWebsite/saveScreenedWebsite',
        data: JSON.stringify(screenedWebsite),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
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
    $("#screenedWebsiteDialog").dialog("close");
    $('#screenedWebsiteForm')[0].reset();
}
function modifyScreenedWebsite(uuid) {
    getScreenedWebsite(uuid, function (screenedWebsite) {
        if (screenedWebsite != null) {
            initScreenedWebsiteDialog(screenedWebsite);
            showScreenedWebsiteDialog(screenedWebsite.uuid);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getScreenedWebsite(uuid, callback) {
    $.ajax({
        url: '/internal/screenedWebsite/getScreenedWebsite/' + uuid,
        type: 'Get',
        success: function (screenedWebsite) {
            callback(screenedWebsite);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function delScreenedWebsite(uuid) {
    if (confirm("确实要删除这个组的屏蔽网站吗?") == false)
        return;
    $.ajax({
        url: '/internal/screenedWebsite/delScreenedWebsite/' + uuid,
        type: 'Get',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "删除成功",true);
            } else {
                $().toastmessage('showErrorToast', "删除失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败",true);
        }
    });
}
function initScreenedWebsiteDialog(screenedWebsite) {
    var screenedWebsiteForm = $("#screenedWebsiteForm");
    screenedWebsiteForm.find("#uuid").val(screenedWebsite.uuid);
    screenedWebsiteForm.find("#optimizeGroupName").val(screenedWebsite.optimizeGroupName);
    screenedWebsiteForm.find("#screenedWebsite").val(screenedWebsite.screenedWebsite.replace(/[,]/g, "\n"));
}
function changePaging(currentPage, pageSize) {
    var searchScreenedWebsiteForm = $("#searchScreenedWebsiteForm");
    searchScreenedWebsiteForm.find("#currentPageNumberHidden").val(currentPage);
    searchScreenedWebsiteForm.find("#pageSizeHidden").val(pageSize);
    searchScreenedWebsiteForm.submit();
}
function resetPageNumber() {
    var searchScreenedWebsiteForm = $("#searchScreenedWebsiteForm");
    var optimizeGroupName = searchScreenedWebsiteForm.find("#optimizeGroupName").val();
    if(optimizeGroupName != "") {
        searchScreenedWebsiteForm.find("#optimizeGroupName").val($.trim(optimizeGroupName));
    }
    searchScreenedWebsiteForm.submit();
}