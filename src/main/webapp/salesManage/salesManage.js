$(function () {
    $("#salesManageDialog").dialog("close");
    $("#showOperationTypeTableDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});

function pageLoad() {
    var searchSalesManageForm = $("#searchSalesManageForm");
    var pageSize = searchSalesManageForm.find('#pageSizeHidden').val();
    var pages = searchSalesManageForm.find('#pagesHidden').val();
    var currentPageNumber = searchSalesManageForm.find('#currentPageNumberHidden').val();
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

// 初始化弹框
function initSalesManageDialog(salesManage) {
    var salesManageForm = $("#salesManageForm");
    salesManageForm.find("#uuid").val(salesManage.uuid);
    salesManageForm.find("#formSalesName").val(salesManage.salesName);
    salesManageForm.find("#telephone").val(salesManage.telephone);
    salesManageForm.find("#qq").val(salesManage.qq);
    salesManageForm.find("#weChat").val(salesManage.weChat);
    salesManageForm.find("#email").val(salesManage.email);
    salesManageForm.find("#managePart").find("option[value='" + salesManage.managePart + "']").attr("selected", true);
}

// 获取修改信息
function getSalesManage(uuid, callback) {
    $.ajax({
        url: '/internal/salesManage/getSalesManage/' + uuid,
        type: 'Get',
        success: function (salesManage) {
            callback(salesManage);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}

// 显示弹框
function showSalesManageDialog(uuid) {
    if (uuid == null) {
        $('#salesManageForm')[0].reset();
    }
    $("#salesManageDialog").show();
    $("#salesManageDialog").dialog({
        resizable: false,
        width: 300,
        height: 240,
        modal: true,
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-ok',
                handler: function () {
                    saveOperationType(uuid);
                }
            },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#salesManageForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#salesManageDialog").dialog("close");
                    $('#salesManageForm')[0].reset();
                }
            }]
    });
    $("#salesManageDialog").dialog("open");
    $('#salesManageDialog').window("resize", {
        top: $(document).scrollTop() + $(window).height() / 2 - 200,
        left: $(document).scrollLeft() + $(window).width() / 2 - 180
    });
}

// 添加
function saveOperationType(uuid) {
    var salesManageForm = $("#salesManageDialog").find("#salesManageForm");
    var salesName = salesManageForm.find("#formSalesName").val();
    if (salesName == null || salesName == "") {
        alert("请输入销售名称");
        salesName.find("#formSalesName").focus();
        return;
    }

    var telephone = salesManageForm.find("#telephone").val();
    var qq = salesManageForm.find("#qq").val();
    var weChat = salesManageForm.find("#weChat").val();
    var email = salesManageForm.find("#email").val();
    var managePart = salesManageForm.find("#managePart").val();

    if (telephone == "" || qq == "" || weChat == "" || email == "") {
        $().toastmessage('showSuccessToast', "手机号，QQ号，微信号，邮箱须填一个以上", true);
        return;
    }

    var salesManage = {};
    salesManage.uuid = uuid;
    salesManage.salesName = $.trim(salesName);
    salesManage.telephone = $.trim(telephone);
    salesManage.qq = $.trim(qq);
    salesManage.weChat = $.trim(weChat);
    salesManage.email = $.trim(email);
    salesManage.weChat = $.trim(weChat);
    salesManage.managePart = $.trim(managePart);
    $.ajax({
        url: '/internal/salesManage/saveSalesManage',
        data: JSON.stringify(salesManage),
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
                $().toastmessage('showErrorToast', "保存失败", true);

            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "保存失败", true);
        }
    });
    $("#screenedWebsiteDialog").dialog("close");
    $('#screenedWebsiteForm')[0].reset();
}

// 修改操作
function modifySalesManage(uuid) {
    getSalesManage(uuid, function (salesManage) {
        if (salesManage != null) {
            initSalesManageDialog(salesManage);
            showSalesManageDialog(salesManage.uuid);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}

// 删除单个
function deleteSalesManage(uuid) {
    if (confirm("确定删除当前行吗?") == false)
        return;
    $.ajax({
        url: '/internal/salesManage/deleteSalesManage/' + uuid,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        timeout: 5000,
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "删除成功", true);
            } else {
                $().toastmessage('showErrorToast', "清除缓存失败", true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败", true);
        }
    });
}

// 删除所选
function deleteBatchOperationType() {
    var uuids = getSelectedIDs();
    var salesManageList = getSelectedSalesManageList();
    if (uuids === '') {
        alert('请选择要删除的销售信息！');
        return;
    }
    if (confirm("确实要删除所选中的销售信息吗?") == false) {
        return;
    }
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.salesManageList = salesManageList.split(",");
    $.ajax({
        url: '/internal/salesManage/deleteBeachSalesManage',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "删除成功", true);
            } else {
                $().toastmessage('showErrorToast', "删除失败或清除缓存失败", true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}

// 更新至SEO站点
function updateInfoToOther() {
    $.ajax({
        url: '/internal/salesManage/updateInfoToOther',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "更新成功", true);
            } else {
                $().toastmessage('showErrorToast', "更新失败", true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
        }
    });
}

// 获取选中的UUID
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

// 全选
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

function changePaging(currentPage, pageSize) {
    var searchSalesManageForm = $("#searchSalesManageForm");
    searchSalesManageForm.find("#currentPageNumberHidden").val(currentPage);
    searchSalesManageForm.find("#pageSizeHidden").val(pageSize);
    searchSalesManageForm.submit();
}

function resetPageNumber() {
    var searchSalesManageForm = $("#searchSalesManageForm");
    var salesName = searchSalesManageForm.find("#salesName").val();
    if (salesName !== "") {
        searchSalesManageForm.find("#salesName").val($.trim(salesName));
    }
    searchSalesManageForm.submit();
}

function getSelectedSalesManageList() {
    var optimizeGroupNameList = '';
    $.each($("input[name=uuid]:checkbox:checked"), function () {
        if (optimizeGroupNameList === '') {
            optimizeGroupNameList = $(this).parent().parent().find("td:eq(1)").text();
        } else {
            optimizeGroupNameList = optimizeGroupNameList + "," + $(this).parent().parent().find("td:eq(1)").text();
        }
    });
    return optimizeGroupNameList;
}