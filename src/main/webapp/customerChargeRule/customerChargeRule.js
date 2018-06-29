$(function () {
    $('#customerChargeRuleDialog').dialog("close");
    $("#confirmChargeDialog").dialog("close");
    $("#customerChargeLogDialog").dialog("close");
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});
function changePaging(currentPage, pageSize) {
    var searchCustomerChargeRuleForm = $("#searchCustomerChargeRuleForm");
    searchCustomerChargeRuleForm.find("#currentPageNumberHidden").val(currentPage);
    searchCustomerChargeRuleForm.find("#pageSizeHidden").val(pageSize);
    searchCustomerChargeRuleForm.submit();
}
function resetPageNumber(chargeDay) {
    var searchCustomerChargeRuleForm = $("#searchCustomerChargeRuleForm");
    var customerInfo = searchCustomerChargeRuleForm.find("#customerInfo").val();
    var customerUuid = customerInfo.substring(customerInfo.lastIndexOf("_") + 1);
    searchCustomerChargeRuleForm.find("#customerUuid").val(customerUuid);
    searchCustomerChargeRuleForm.find("#chargeDays").val(chargeDay)
    searchCustomerChargeRuleForm.find("#currentPageNumberHidden").val(1);
    searchCustomerChargeRuleForm.submit();
}
function pageLoad() {
    var searchCustomerChargeRuleForm = $("#searchCustomerChargeRuleForm");
    var pageSize = searchCustomerChargeRuleForm.find('#pageSizeHidden').val();
    var pages = searchCustomerChargeRuleForm.find('#pagesHidden').val();
    var currentPageNumber = searchCustomerChargeRuleForm.find('#currentPageNumberHidden').val();
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
function getCustomerUuids() {
    var customerUuids = '';
    $.each($("input[name=uuid]:checkbox:checked"), function () {
        if (customerUuids === '') {
            customerUuids = $(this).parent().parent().find("input[name=customerUuid]").val();
        } else {
            customerUuids = customerUuids + "," + $(this).parent().parent().find("input[name=customerUuid]").val();
        }
    });
    return customerUuids;
}
function showCustomerChargeRuleDialog() {
    $('#addCustomerChargeRuleForm')[0].reset();
    $("#customerChargeRuleDialog").show();
    $("#customerChargeRuleDialog").dialog({
        resizable: false,
        width: 320,
        height: 425,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var addCustomerChargeRuleForm = $("#addCustomerChargeRuleForm");
                var customerChargeRule = {};
                var customerInfo = addCustomerChargeRuleForm.find("#customerInfo").val();
                if(customerInfo == "") {
                    alert("客户不能为空！");
                    return;
                }
                var chargeTotal = addCustomerChargeRuleForm.find("#chargeTotal").val();
                if(chargeTotal == "") {
                    alert("收费总额不能为空！");
                    return;
                }
                var nextChargeDate = addCustomerChargeRuleForm.find("#nextChargeDate").val();
                if(nextChargeDate == "") {
                    alert("下次收费日期不能为空！");
                    return;
                }
                var customerUuid = customerInfo.substr(customerInfo.lastIndexOf("_") + 1);
                customerChargeRule.customerUuid = customerUuid;
                customerChargeRule.chargeTotal = chargeTotal;
                customerChargeRule.nextChargeDate = nextChargeDate;
                customerChargeRule.januaryFee = addCustomerChargeRuleForm.find("#januaryFee").val();
                customerChargeRule.februaryFee = addCustomerChargeRuleForm.find("#februaryFee").val();
                customerChargeRule.marchFee = addCustomerChargeRuleForm.find("#marchFee").val();
                customerChargeRule.aprilFee = addCustomerChargeRuleForm.find("#aprilFee").val();
                customerChargeRule.mayFee = addCustomerChargeRuleForm.find("#mayFee").val();
                customerChargeRule.juneFee = addCustomerChargeRuleForm.find("#juneFee").val();
                customerChargeRule.julyFee = addCustomerChargeRuleForm.find("#julyFee").val();
                customerChargeRule.augustFee = addCustomerChargeRuleForm.find("#augustFee").val();
                customerChargeRule.septemberFee = addCustomerChargeRuleForm.find("#septemberFee").val();
                customerChargeRule.octoberFee = addCustomerChargeRuleForm.find("#octoberFee").val();
                customerChargeRule.novemberFee = addCustomerChargeRuleForm.find("#novemberFee").val();
                customerChargeRule.decemberFee = addCustomerChargeRuleForm.find("#decemberFee").val();
                customerChargeRule.januaryRate = addCustomerChargeRuleForm.find("#januaryRate").val();
                customerChargeRule.februaryRate = addCustomerChargeRuleForm.find("#februaryRate").val();
                customerChargeRule.marchRate = addCustomerChargeRuleForm.find("#marchRate").val();
                customerChargeRule.aprilRate = addCustomerChargeRuleForm.find("#aprilRate").val();
                customerChargeRule.mayRate = addCustomerChargeRuleForm.find("#mayRate").val();
                customerChargeRule.juneRate = addCustomerChargeRuleForm.find("#juneRate").val();
                customerChargeRule.julyRate = addCustomerChargeRuleForm.find("#julyRate").val();
                customerChargeRule.augustRate = addCustomerChargeRuleForm.find("#augustRate").val();
                customerChargeRule.septemberRate = addCustomerChargeRuleForm.find("#septemberRate").val();
                customerChargeRule.octoberRate = addCustomerChargeRuleForm.find("#octoberRate").val();
                customerChargeRule.novemberRate = addCustomerChargeRuleForm.find("#novemberRate").val();
                customerChargeRule.decemberRate = addCustomerChargeRuleForm.find("#decemberRate").val();
                saveCustomerChargeRule(customerChargeRule, "add");
                $("#customerDialog").dialog("close");
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#addCustomerChargeRuleForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#customerChargeRuleDialog").dialog("close");
                }
            }]
    });
    $("#customerChargeRuleDialog").dialog("open");
    $('#customerChargeRuleDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveCustomerChargeRule(customerChargeRule, type) {
    $.ajax({
        url: '/internal/customerChargeRule/saveCustomerChargeRule',
        data: JSON.stringify(customerChargeRule),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
                if(type == "add") {
                    $().toastmessage('showSuccessToast', "保存成功", true);
                } else {
                    $().toastmessage('showSuccessToast', "保存成功");
                }
            } else {
                if(type == "add") {
                    $().toastmessage('showErrorToast', "保存失败");
                } else {
                    $().toastmessage('showErrorToast', "保存失败", true);
                }
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "保存失败", true);
        }
    });
}
function deleteCustomerChargeRule(uuid) {
    if (confirm("确认要删除这条收费规则吗?") == false) return;
    $.ajax({
        url: '/internal/customerChargeRule/deleteCustomerChargeRule/' + uuid,
        type: 'Get',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "删除成功", true);
            } else {
                $().toastmessage('showErrorToast', "删除失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败");
        }
    });
}
function deleteCustomerChargeRules() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的收费规则');
        return;
    }
    if (confirm("确定要删除这些收费规则吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/customerChargeRule/deleteCustomerChargeRules',
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
                $().toastmessage('showErrorToast', "删除失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败");
        }
    });
}
function addCustomerChargeLog(loginName) {
    var customerUuids = getCustomerUuids();
    if (customerUuids === '') {
        alert("请选择要收费的项目");
        return;
    }
    $("#confirmChargeForm")[0].reset();
    $("#confirmChargeDialog").show();
    $("#confirmChargeDialog").dialog({
        resizable: false,
        width: 248,
        height: 145,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var postData = {};
                var confirmChargeForm = $("#confirmChargeForm");
                postData.customerUuids = customerUuids.split(",");
                var planChargeAmount = confirmChargeForm.find("#planChargeAmount").val();
                var actualChargeAmount = confirmChargeForm.find("#actualChargeAmount").val();
                postData.planChargeAmount = planChargeAmount == "" ? 0 : parseInt(planChargeAmount);
                postData.actualChargeAmount = actualChargeAmount == "" ? 0 : parseInt(actualChargeAmount);
                postData.cashier = loginName;
                postData.nextChargeDate = confirmChargeForm.find("#nextChargeDate").val();
                $.ajax({
                    url: '/internal/customerChargeLog/addCustomerChargeLog',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (data) {
                        if (data) {
                            $().toastmessage('showSuccessToast', "操作成功", true);
                        } else {
                            $().toastmessage('showErrorToast', "操作失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                });
                $("#confirmChargeDialog").dialog("close");
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#confirmChargeDialog").dialog("close");
                }
            }]
    });
    $("#confirmChargeDialog").dialog("open");
    $('#confirmChargeDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function findCustomerChargeLogs(customerUuid) {
    $("#customerChargeLogTable").find("tr:eq(0)").nextAll().remove();
    $.ajax({
        url: '/internal/customerChargeLog/findCustomerChargeLogs/' + customerUuid,
        type: 'Get',
        success: function (customerChargeLogs) {
            if (customerChargeLogs.length > 0) {
                $.each(customerChargeLogs, function (index, value) {
                    var date = new Date(value.createTime).toLocaleString();
                    $("#customerChargeLogTable").append("<tr><td>" + date + "</td><td>" + value.planChargeAmount + "</td><td>" + value.actualChargeAmount + "</td><td>" + value.cashier + "</td></tr>");
                });
                $("#customerChargeLogDialog").show();
                $("#customerChargeLogDialog").dialog({
                    resizable: false,
                    minWidth: 345,
                    maxHeight: 200,
                    modal: true,
                    buttons: [
                        {
                            text: '取消',
                            iconCls: 'icon-cancel',
                            handler: function () {
                                $("#customerChargeLogDialog").dialog("close");
                            }
                        }]
                });
                $("#customerChargeLogDialog").dialog("open");
                $('#customerChargeLogDialog').window("resize",{top:$(document).scrollTop() + 100});
            } else {
                alert("暂无数据");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "查询失败");
        }
    });
}