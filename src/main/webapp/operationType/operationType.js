$(function () {
    $("#operationTypeDialog").dialog("close");
    $("#showOperationTypeTableDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});

function pageLoad() {
    var searchOperationTypeForm = $("#searchOperationTypeForm");
    var pageSize = searchOperationTypeForm.find('#pageSizeHidden').val();
    var pages = searchOperationTypeForm.find('#pagesHidden').val();
    var currentPageNumber = searchOperationTypeForm.find('#currentPageNumberHidden').val();
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
function initOperationTypeDialog(operationType) {
    var operationTypeForm = $("#operationTypeForm");
    operationTypeForm.find("#uuid").val(operationType.uuid);
    operationTypeForm.find("#operationTypeName").val(operationType.operationTypeName);
    operationTypeForm.find("input[name='terminalType'][value='" + operationType.terminalType + "']").prop("checked", "checked");
    operationTypeForm.find("#terminalType").val(operationType.terminalType);
    operationTypeForm.find("#description").val(operationType.description.replace(/[,]/g, "\n"));
    operationTypeForm.find("#remark").val(operationType.remark);
    operationTypeForm.find("input[name='status'][value='" + operationType.status + "']").prop("checked", "checked");
}

// 获取修改信息
function getOperationType(uuid, callback) {
    $.ajax({
        url: '/internal/operationType/getOperationType/' + uuid,
        type: 'Get',
        success: function (operationType) {
            callback(operationType);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}

// 显示弹框
function showOperationTypeDialog(uuid) {
    if (uuid == null) {
        $('#operationTypeForm')[0].reset();
    }
    $("#operationTypeDialog").show();
    $("#operationTypeDialog").dialog({
        resizable: false,
        width: 360,
        height: 360,
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
                    $('#operationTypeForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#operationTypeDialog").dialog("close");
                    $('#operationTypeForm')[0].reset();
                }
            }]
    });
    $("#operationTypeDialog").dialog("open");
    $('#operationTypeDialog').window("resize", {
        top: $(document).scrollTop() + $(window).height() / 2 - 200,
        left: $(document).scrollLeft() + $(window).width() / 2 - 180
    });
}

// 添加
function saveOperationType(uuid) {
    var operationTypeForm = $("#operationTypeDialog").find("#operationTypeForm");
    var operationTypeName = operationTypeForm.find("#operationTypeName").val();
    if (operationTypeName == null || operationTypeName == "") {
        alert("请输入操作类型名称");
        operationTypeName.find("#operationTypeName").focus();
        return;
    }
    var terminalType = operationTypeForm.find("input[name='terminalType']:checked").val();
    if (terminalType == null || terminalType == "") {
        alert("请选择终端类型");
        operationTypeForm.find("#terminalType").focus();
        return;
    }
    var description = operationTypeForm.find("#description").val();
    var remark = operationTypeForm.find("#remark").val();
    var status = operationTypeForm.find("input[name='status']:checked").val();
    var operationType = {};
    operationType.uuid = uuid;
    operationType.operationTypeName = $.trim(operationTypeName);
    operationType.terminalType = $.trim(terminalType);
    operationType.description = $.trim(description);
    operationType.remark = $.trim(remark);
    operationType.status = $.trim(status);
    $.ajax({
        url: '/internal/operationType/saveOperationType',
        data: JSON.stringify(operationType),
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
                $().toastmessage('showErrorToast', "清除缓存失败", true);

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
function modifyOperationType(uuid) {
    getOperationType(uuid, function (operationType) {
        if (operationType != null) {
            initOperationTypeDialog(operationType);
            showOperationTypeDialog(operationType.uuid);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}

// 删除单个
function deleteOperationType(uuid, operationTypeName) {
    if (confirm("确定删除当前行的操作类型吗?") == false)
        return;
    var postDate = {};
    postDate.uuid = uuid;
    postDate.operationTypeName = operationTypeName;
    postDate.deleteType = "single";
    $.ajax({
        url: '/internal/operationType/deleteOperationType',
        data: JSON.stringify(postDate),
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
function deleteBatchOperationType(self) {
    var uuids = getSelectedIDs();
    var optimizeGroupNameList = getSelectedOptimizeGroupNameList();
    if (uuids === '') {
        alert('请选择要删除的操作类型！');
        return;
    }
    if (confirm("确实要删除所选中的操作类型吗?") == false) {
        return;
    }
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.optimizeGroupNameList = optimizeGroupNameList.split(",");
    $.ajax({
        url: '/internal/operationType/deleteOperationType',
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
    var searchOperationTypeForm = $("#searchOperationTypeForm");
    searchOperationTypeForm.find("#currentPageNumberHidden").val(currentPage);
    searchOperationTypeForm.find("#pageSizeHidden").val(pageSize);
    searchOperationTypeForm.submit();
}

function resetPageNumber() {
    var searchOperationTypeForm = $("#searchOperationTypeForm");
    var operationTypeName = searchOperationTypeForm.find("#searchOperationTypeName").val();
    if (operationTypeName !== "") {
        searchOperationTypeForm.find("#searchOperationTypeName").val($.trim(operationTypeName));
    }
    searchOperationTypeForm.submit();
}

function getSelectedOptimizeGroupNameList() {
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