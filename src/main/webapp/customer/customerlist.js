$(function () {
    $("#customerDialog").dialog("close");
    $("#uploadDailyReportTemplateDialog").dialog("close");
    $("#customerKeywordDialog").dialog("close");
    $("#customerChargeTypeDialog").dialog("close");
    $("#showCustomerTableDiv").css("margin-top",$("#topDiv").height());
    pageLoad();
    onlyNumber();
    autoCheckTerminalType();
});

function pageLoad() {
    var searchCustomerForm = $("#searchCustomerForm");
    var pageSize = searchCustomerForm.find('#pageSizeHidden').val();
    var pages = searchCustomerForm.find('#pagesHidden').val();
    var currentPageNumber = searchCustomerForm.find('#currentPageNumberHidden').val();
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
//只能输入数字
function onlyNumber() {
    var calculationInput = $("#chargeTypeCalculationDiv").find("input[type=text]");
    calculationInput.bind('keyup', function (k, v) {
        var obj = $(this);
        obj.val(obj.val().replace(/[^\d]*/g, ''));
    });
}

function initRangeTable(terminalTypeCheckboxObj) {
    if (terminalTypeCheckboxObj.checked) {
        $("#tab" + terminalTypeCheckboxObj.id + " tr:not(:first)").remove();
        if (terminalTypeCheckboxObj.id == "PC") {
            addRow("PC");
        } else {
            addRow("Phone");
        }
    } else {
        $("#tab" + terminalTypeCheckboxObj.id + " tr:not(:first)").remove();
    }
}

function controlChargeTypeCalculationDiv(terminalTypeCheckboxObj) {
    var chargeTypeCalculationDiv = $("#chargeTypeCalculationDiv");
    if (terminalTypeCheckboxObj.checked) {
        chargeTypeCalculationDiv.find("#chargesLT" + terminalTypeCheckboxObj.id).fadeIn("slow");
        chargeTypeCalculationDiv.find("#chargesGT" + terminalTypeCheckboxObj.id).fadeIn("slow");
    }
    else {
        chargeTypeCalculationDiv.find("#chargesLT" + terminalTypeCheckboxObj.id).fadeOut("slow");
        chargeTypeCalculationDiv.find("#chargesGT" + terminalTypeCheckboxObj.id).fadeOut("slow");
    }
}

function autoCheckTerminalType() {
    var chargeTypeCalculationDiv = $("#chargeTypeCalculationDiv");
    var chargeTypeIntervalDiv = $("#chargeTypeIntervalDiv");

    var calculationInputPC = chargeTypeCalculationDiv.find("#pcOperationTypeDiv").find("input[type=text]");
    calculationInputPC.focus(function () {
        chargeTypeCalculationDiv.find("#PC").prop("checked", true);
    });

    var calculationInputPhone = chargeTypeCalculationDiv.find("#phoneOperationTypeDiv").find("input[type=text]");
    calculationInputPhone.focus(function () {
        chargeTypeCalculationDiv.find("#Phone").prop("checked", true);
    });

    var intervalInputPC = chargeTypeIntervalDiv.find("#pcOperationTypeDiv").find("input[type=text]");
    intervalInputPC.focus(function () {
        chargeTypeIntervalDiv.find("#PC").prop("checked", true);
    });

    var intervalInputPhone = chargeTypeIntervalDiv.find("#phoneOperationTypeDiv").find("input[type=text]");
    intervalInputPhone.focus(function () {
        chargeTypeIntervalDiv.find("#Phone").prop("checked", true);
    });
}

function selectAll(self) {
    var a = document.getElementsByName("customerUuid");
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
    var a = document.getElementsByName("customerUuid");
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

function triggerDailyReportGeneration(self) {
    if (confirm("确实要生成当天报表吗?") == false) return;
    var customerUuids = getSelectedIDs();

    var postData = {"customerUuids": customerUuids}
    $.ajax({
        url: '/internal/dailyReport/triggerReportGeneration',
        data: JSON.stringify(postData),
        type: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (status) {
            if (status) {
                $().toastmessage('showSuccessToast', "更新成功",true);
            } else {
                $().toastmessage('showErrorToast', "更新失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "更新失败");
        }
    });
}

//删除所选
function deleteCustomers(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的客户');
        return;
    }
    if (confirm("确实要删除这些客户吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/customer/deleteCustomers',
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
            $().toastmessage('showErrorToast', "操作失败",true);

        }
    });
}

function getSelectedIDs() {
    var uuids = '';
    $.each($("input[name=customerUuid]:checkbox:checked"), function () {
        if (uuids === '') {
            uuids = $(this).val();
        } else {
            uuids = uuids + "," + $(this).val();
        }
    });
    return uuids;
}
//规则部分
function changeCustomerChargeType(customerUuid) {
    $.ajax({
        url: '/internal/customerChargeType/getCustomerChargeType/' + customerUuid,
        type: 'Get',
        success: function (customerChargeType) {
            $("#tabPC tr:not(:first)").remove();
            $("#tabPhone tr:not(:first)").remove();
            $("#customerChargeTypeDialog").find("#customerChargeTypeUuid").val(null);
            if (customerChargeType != null && customerChargeType !== '') {
                initCustomerChargeTypeDialog(customerChargeType);
                showCustomerChargeTypeDialog(customerChargeType.customerUuid);
            } else {
                $('#showRuleForm')[0].reset();
                initChargeTypePercentage();
                addRow('PC');
                addRow('Phone');
                showCustomerChargeTypeDialog(customerUuid);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}

function showCustomerChargeTypeDialog(customerUuid) {
    $("#customerChargeTypeDialog").dialog({
        resizable: false,
        width: 500,
        height:350,
        modal: true,
        closed: true,
        //按钮
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveCustomerChargeType(customerUuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#showRuleForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#customerChargeTypeDialog").dialog("close");
                    $('#showRuleForm')[0].reset();
                }
            }]
    });
    $("#customerChargeTypeDialog").dialog("open");
    $('#customerChargeTypeDialog').window("resize",{top:$(document).scrollTop() + 100});
}

function saveCustomerChargeType(customerUuid) {
    var customerChargeTypeDialog = $("#customerChargeTypeDialog");
    var chargeTypeCalculationDiv = customerChargeTypeDialog.find("#chargeTypeCalculationDiv");
    var chargeTypeIntervalDiv = customerChargeTypeDialog.find("#chargeTypeIntervalDiv");
    var customerChargeType = {};
    customerChargeType.uuid = customerChargeTypeDialog.find("#customerChargeTypeUuid").val();
    customerChargeType.customerUuid = customerUuid;
    customerChargeType.chargeType = customerChargeTypeDialog.find("#showRuleRadioDiv input:radio:checked").val();
    var switchval = customerChargeType.chargeType;
    var validationFlag = true;
    if (switchval == "Percentage") {
        var checkedTerminalTypeObjs = chargeTypeCalculationDiv.find("input[name=operationType]:checkbox:checked");
        if (checkedTerminalTypeObjs.length == 0) {
            alert("请选择一个操作类型电脑或手机");
            validationFlag = false;
            return;
        }
        customerChargeType.customerChargeTypeCalculations = [];
        $.each(checkedTerminalTypeObjs, function (idx, val) {
            if ($.trim(chargeTypeCalculationDiv.find("#chargesOfFirstLT" + val.id).val()) === '' && $.trim(chargeTypeCalculationDiv.find("#chargesOfFirstGT" + val.id).val()) === '') {
                alert("至少填写一条排名第一的字段");
                validationFlag = false;
                return false;
            }
            if ($.trim(chargeTypeCalculationDiv.find("#chargesOfFirstLT" + val.id).val()) != '') {
                var customerChargeTypeCalculationLT = {};
                customerChargeTypeCalculationLT.uuid = chargeTypeCalculationDiv.find("#chargeTypeCalculationUuid" + val.id).val();
                customerChargeTypeCalculationLT.chargeDataType = "LessThanHundred";
                customerChargeTypeCalculationLT.operationType = val.id;
                customerChargeTypeCalculationLT.chargesOfFirst = chargeTypeCalculationDiv.find("#chargesOfFirstLT" + val.id).val();
                customerChargeTypeCalculationLT.chargesOfSecond = chargeTypeCalculationDiv.find("#chargesOfSecondLT" + val.id).val();
                customerChargeTypeCalculationLT.chargesOfThird = chargeTypeCalculationDiv.find("#chargesOfThirdLT" + val.id).val();
                customerChargeTypeCalculationLT.chargesOfFourth = chargeTypeCalculationDiv.find("#chargesOfFourthLT" + val.id).val();
                customerChargeTypeCalculationLT.chargesOfFifth = chargeTypeCalculationDiv.find("#chargesOfFifthLT" + val.id).val();
                customerChargeTypeCalculationLT.chargesOfFirstPage = chargeTypeCalculationDiv.find("#chargesOfFirstPageLT" + val.id).val();
                customerChargeTypeCalculationLT.uuid = chargeTypeCalculationDiv.find("#chargeTypeCalculationUuid" + val.id).val();
                customerChargeType.customerChargeTypeCalculations.push(customerChargeTypeCalculationLT);
            }

            if ($.trim(chargeTypeCalculationDiv.find("#chargesOfFirstGT" + val.id).val()) != '') {
                var customerChargeTypeCalculationGT = {};
                customerChargeTypeCalculationGT.chargeDataType = "Percentage";
                customerChargeTypeCalculationGT.operationType = val.id;
                customerChargeTypeCalculationGT.chargesOfFirst = chargeTypeCalculationDiv.find("#chargesOfFirstGT" + val.id).val();
                customerChargeTypeCalculationGT.chargesOfSecond = chargeTypeCalculationDiv.find("#chargesOfSecondGT" + val.id).val();
                customerChargeTypeCalculationGT.chargesOfThird = chargeTypeCalculationDiv.find("#chargesOfThirdGT" + val.id).val();
                customerChargeTypeCalculationGT.chargesOfFourth = chargeTypeCalculationDiv.find("#chargesOfFourthGT" + val.id).val();
                customerChargeTypeCalculationGT.chargesOfFifth = chargeTypeCalculationDiv.find("#chargesOfFifthGT" + val.id).val();
                customerChargeTypeCalculationGT.chargesOfFirstPage = chargeTypeCalculationDiv.find("#chargesOfFirstPageGT" + val.id).val();
                customerChargeType.customerChargeTypeCalculations.push(customerChargeTypeCalculationGT);
            }
        });
    } else {
        var checkedTerminalTypeObjs = chargeTypeIntervalDiv.find("input[name=operationType]:checkbox:checked");
        if (checkedTerminalTypeObjs.length == 0) {
            alert("请选择一个操作类型电脑或手机");
            validationFlag = false;
            return;
        }
        customerChargeType.customerChargeTypeIntervals = [];
        $.each(checkedTerminalTypeObjs, function (idx, val) {
            var trRowCount = chargeTypeIntervalDiv.find("#tab" + val.id + ' tr').length;
            if (parseInt(trRowCount) == 1) {
                alert("请至少填一条规则");
                validationFlag = false;
                if (val.id == "PC") {
                    addRow("PC");
                } else {
                    addRow("Phone");
                }
                return false;
            }

            var previousEndIndex = null;
            $.each($("#tab" + val.id + " tr:not(:first)"), function (trIdx, trVal) {
                var customerChargeTypeInterval = {};
                customerChargeTypeInterval.operationType = val.id;
                var startIndexObj = $(trVal).find("input[name=startIndex]");
                customerChargeTypeInterval.startIndex = startIndexObj.val();
                var endIndexObj = $(trVal).find("input[name=endIndex]");
                customerChargeTypeInterval.endIndex = endIndexObj.val();
                var priceObj = $(trVal).find("input[name=price]");
                customerChargeTypeInterval.price = priceObj.val();
                customerChargeType.customerChargeTypeIntervals.push(customerChargeTypeInterval);
                if (customerChargeTypeInterval.endIndex != '' && parseInt(customerChargeTypeInterval.startIndex) >= parseInt(customerChargeTypeInterval.endIndex)) {
                    alert("终止指数需大于起始指数");
                    endIndexObj.focus();
                    validationFlag = false;
                    return false;
                }
                if (trIdx > 0 && parseInt(customerChargeTypeInterval.startIndex) <= parseInt(previousEndIndex)) {
                    alert("起始指数需大于前一条的终止指数");
                    startIndexObj.focus();
                    validationFlag = false;
                    return false;
                }

                if (customerChargeTypeInterval.startIndex === '') {
                    if (trIdx == 1) {
                        customerChargeTypeInterval.startIndex = 0;
                    } else {
                        alert("起始指数不能为空");
                        startIndexObj.focus();
                        validationFlag = false;
                        return false;
                    }
                }
                if (trIdx < parseInt(trRowCount) - 2 && customerChargeTypeInterval.endIndex == '') {
                    alert("终止指数不能为空");
                    endIndexObj.focus();
                    validationFlag = false;
                    return false;
                }
                if (customerChargeTypeInterval.price === '') {
                    alert("价格不能为空");
                    priceObj.focus();
                    validationFlag = false;
                    return false;
                }
                previousEndIndex = customerChargeTypeInterval.endIndex;
            });
            if(!validationFlag){
                return false;
            }
        });
    }
    if (validationFlag) {
        $.ajax({
            url: '/internal/customerChargeType/saveCustomerChargeType',
            data: JSON.stringify(customerChargeType),
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
        $("#customerChargeTypeDialog").dialog("close");
        $('#showRuleForm')[0].reset();
    }
}

function chooseChargeType(val) {
    switch (val) {
        case "Percentage":
            initChargeTypePercentage();
            $('#showRuleForm')[0].reset();
//                    hideOperationTypeDiv();
            $("#chargeTypeIntervalDiv").find("#PC").prop("checked", false);
            $("#chargeTypeIntervalDiv").find("#Phone").prop("checked", false);
            break;
        case "Range":
            initChargeTypeInterval();
            $('#showRuleForm')[0].reset();
            $("#chargeTypeCalculationDiv").find("#PC").prop("checked", false);
            $("#chargeTypeCalculationDiv").find("#Phone").prop("checked", false);
            break;
    }
}

function initChargeTypePercentage() {
    $("#chargeTypePercentage").prop("checked", "checked");
    $("#chargeTypeCalculationDiv").show();
    $("#chargeTypeIntervalDiv").hide();
    $("#chargesGTPC").find("span").html("%");
    $("#chargesGTPhone").find("span").html("%");
    $("#tabPC tr:not(:first)").remove();
    $("#tabPhone tr:not(:first)").remove();
}

function initChargeTypeInterval() {
    $("#chargeTypeInterval").prop("checked", "checked");
    $("#chargeTypeCalculationDiv").hide();
    $("#chargeTypeIntervalDiv").show();
}

//填充到规则DIV"
function initCustomerChargeTypeDialog(customerChargeType) {
    var customerChargeTypeDialog = $("#customerChargeTypeDialog");
    var chargeTypeCalculationDiv = $("#chargeTypeCalculationDiv");
    var chargeTypeIntervalDiv = $("#chargeTypeIntervalDiv");
    customerChargeTypeDialog.find("#customerChargeTypeUuid").val(customerChargeType.uuid);
    var switchType = customerChargeType.chargeType;//判断收费方式
    var customerChargeTypeCalculations = customerChargeType.customerChargeTypeCalculations;
    var customerChargeTypeIntervals = customerChargeType.customerChargeTypeIntervals;
    if (switchType == "Percentage") {
        initChargeTypePercentage();
        $.each(customerChargeTypeCalculations, function (idx, val) {
            chargeTypeCalculationDiv.find("#ChargeRuleCalculationUuid" + val.operationType).val(val.uuid);
            chargeTypeCalculationDiv.find("#" + val.operationType).prop("checked", true);
            if (val.chargeDataType == "LessThanHundred") {
                var chargesLT = chargeTypeCalculationDiv.find("#chargesLT" + val.operationType);
                chargesLT.find("#chargesOfFirstLT" + val.operationType).val(val.chargesOfFirst);
                chargesLT.find("#chargesOfSecondLT" + val.operationType).val(val.chargesOfSecond);
                chargesLT.find("#chargesOfThirdLT" + val.operationType).val(val.chargesOfThird);
                chargesLT.find("#chargesOfFourthLT" + val.operationType).val(val.chargesOfFourth);
                chargesLT.find("#chargesOfFifthLT" + val.operationType).val(val.chargesOfFifth);
                chargesLT.find("#chargesOfFirstPageLT" + val.operationType).val(val.chargesOfFirstPage);
            }
            if (val.chargeDataType == "Percentage") {
                var chargesGT = chargeTypeCalculationDiv.find("#chargesGT" + val.operationType);
                chargesGT.find("#chargesOfFirstGT" + val.operationType).val(val.chargesOfFirst);
                chargesGT.find("#chargesOfSecondGT" + val.operationType).val(val.chargesOfSecond);
                chargesGT.find("#chargesOfThirdGT" + val.operationType).val(val.chargesOfThird);
                chargesGT.find("#chargesOfFourthGT" + val.operationType).val(val.chargesOfFourth);
                chargesGT.find("#chargesOfFifthGT" + val.operationType).val(val.chargesOfFifth);
                chargesGT.find("#chargesOfFirstPageGT" + val.operationType).val(val.chargesOfFirstPage);
            }
            initRangeTable(val.operationType);
        });
    } else {
        initChargeTypeInterval();
        var customerChargeTypeIntervalPC = [];
        var customerChargeTypeIntervalPhone = [];
        $.each(customerChargeTypeIntervals, function (idx, val) {
            chargeTypeIntervalDiv.find("#" + val.operationType).prop("checked", true);
            //如果有一条PC类型就在PCtable中加一"
            if (null == val.endIndex || val.endIndex === '') {
                val.endIndex = '';
            }
            $("#tab" + val.operationType).append(intervalRowStr(val));
        });
        resetSequence($("#tabPC"));
        resetSequence($("#tabPhone"));
        intervalInputOnlyNumberAllow();
    }
}

function intervalRowStr(val) {
    return "<tr  align='center'>"
        + "<td> <span name='sequence'></span> </td>"
        + "<td><input type='text' name='startIndex' value='" + (val == null ? '' : val.startIndex) + "'  style='width: 100%;height: 100%'/></td>"
        + "<td><input type='text' name='endIndex' value='" + (val == null ? '' : val.endIndex ) + "'  style='width: 100%;height: 100%'/></td>"
        + "<td><input type='text' name='price' value='" + (val == null ? '' : val.price ) + "'   style='width: 100%;height: 100%'/></td>"
        + "<td><a href=\'#\' onclick=\"delRow(this)\">删除</a></td>"
        + "</tr>";
}

function resetSequence(tableObj) {
    $.each(tableObj.find("span[name=sequence]"), function (idx, val) {
        $(val).text(idx + 1);
    });
}

function intervalInputOnlyNumberAllow() {
    var intervalInputPC = $("#tabPC").find("input[type=text]");
    intervalInputPC.bind("keyup", function (k, v) {
        var obj = $(this);
        obj.val(obj.val().replace(/[^\d]*/g, ''));
    });

    var intervalInputPhone = $("#tabPhone").find("input[type=text]");
    intervalInputPhone.bind("keyup", function (k, v) {
        var obj = $(this);
        obj.val(obj.val().replace(/[^\d]*/g, ''));
    });
}

function addRow(terminalType) {
    $("#chargeTypeIntervalDiv").find("#" + terminalType).prop("checked", true);
    var trRow = $("#tab" + terminalType + " tr").length;
    $("#tab" + terminalType).append(intervalRowStr(null));
    intervalInputOnlyNumberAllow();
    autoCheckTerminalType();
    resetSequence($("#tabPC"));
    resetSequence($("#tabPhone"));
}

//删除<tr/>
function delRow(rowHref) {
    var currentRow = rowHref.parentNode.parentNode;
    var tableObj = currentRow.parentNode;
    if (tableObj.rows.length < 3) {
        alert("至少填写一条规则");
        return;
    }
    currentRow.remove();
    intervalInputOnlyNumberAllow();
    autoCheckTerminalType();
    resetSequence($("#tabPC"));
    resetSequence($("#tabPhone"));
}

function autoFillPrice(self) {
    var price = $(self).val();
    var idx = $($(self).parent().find("input[type=text]")).index(self);
    if (idx == 0) {
        $(self).parent().find("input[type=text]:gt(" + idx + "):lt(2)").val(price);
    } else {
        $(self).parent().find("input[type=text]:gt(" + idx + "):lt(1)").val(price);
    }
}

//上传日报报表模板
function uploadDailyReportTemplate(uuid, self) {
    $('#dailyReportTemplateForm')[0].reset();
    $("#uploadDailyReportTemplateDialog").dialog({
        resizable: false,
        width: 350,
        height: 150,
        modal: true,
        //按钮
        buttons: [{
            text: '提交',
            iconCls: 'icon-ok',
            handler: function () {
                var uploadForm = $("#dailyReportTemplateForm");
                var uploadFile = uploadForm.find("#uploadFile").val();
                var fileTypes = new Array("xls", "xlsx");  //定义可支持的文件类型数组
                var fileTypeFlag = false;
                var newFileName = uploadFile.split('.');
                newFileName = newFileName[newFileName.length - 1];
                for (var i = 0; i < fileTypes.length; i++) {
                    if (fileTypes[i] == newFileName) {
                        fileTypeFlag = true;
                        break;
                    }
                }
                if (!fileTypeFlag) {
                    alert("请提交表格文 .xls .xlsx");
                    return false;
                }
                var formData = new FormData();
                formData.append('file', uploadForm.find('#uploadFile')[0].files[0]);
                formData.append('customerUuid', uuid);
                if (fileTypeFlag) {
                    $.ajax({
                        url: '/internal/customer/uploadDailyReportTemplate',
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
                        },
                        error: function () {
                            $().toastmessage('showErrorToast', "上传失败");
                        }
                    });
                }
                $(this).dialog("close");
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#uploadDailyReportTemplateDialog").dialog("close");
                    $('#dailyReportTemplateForm')[0].reset();
                }
            }]
    });
    $("#uploadDailyReportTemplateDialog").dialog("open");
    $('#uploadDailyReportTemplateDialog').window("resize",{top:$(document).scrollTop() + 100});
}

//显示添加客户是的DIV
function showCustomerDialog(uuid, loginName) {
    if (uuid == null) {
        $('#customerForm')[0].reset();
    }
    $("#customerDialog").dialog({
        resizable: false,
        width: 280,
        height: 300,
        modal: true,
        //按钮
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                savaCustomer(uuid, loginName);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#customerForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#customerDialog").dialog("close");
                    $('#customerForm')[0].reset();
                }
            }]
    });
    $("#customerDialog").dialog("open");
    $('#customerDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function savaCustomer(uuid, loginName) {
    var customerForm = $("#customerDialog").find("#customerForm");
    var customer = {};
    customer.uuid = uuid;
    customer.loginName = loginName;
    customer.entryType = customerForm.find("#entryTypeHidden").val();
    customer.contactPerson = customerForm.find("#contactPerson").val();
    customer.qq = customerForm.find("#qq").val();
    if (!(/^[1-9]\d{4,14}$/.test(customer.qq)) && (customer.qq != '')) {
        alert("请输入正确的QQ");
        return;
    }
    customer.telphone = customerForm.find("#telphone").val();
    if (!(/^1[34578]\d{9}$/.test(customer.telphone)) && (customer.telphone != '')) {
        alert("请输入正确的手机");
        return;
    }
    if (customer.contactPerson === '') {
        alert("请输入联系人");
        return;
    }
    customer.type = customerForm.find("#type").val();
    customer.status = customerForm.find("#status").val();
    customer.remark = customerForm.find("#remark").val();
    $.ajax({
        url: '/internal/customer/saveCustomer',
        data: JSON.stringify(customer),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "保存成功",true);
                /**/
            } else {

                $().toastmessage('showErrorToast', "保存失败",true);

            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "保存失败");
        }
    });
    $("#customerDialog").dialog("close");
    $('#customerForm')[0].reset();
}

function modifyCustomer(uuid) {
    getCustomer(uuid, function (customer) {
        if (customer != null) {
            initCustomerDialog(customer);
            showCustomerDialog(customer.uuid, customer.userId);
        } else {

            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}

function getCustomer(uuid, callback) {
    $.ajax({
        url: '/internal/customer/getCustomer/' + uuid,
        type: 'Get',
        success: function (customer) {
            callback(customer);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}

function showCustomerKeywordDialog(uuid) {
    $("#customerKeywordDialog").dialog({
        resizable: false,
        /* width: 510,
         height: 320,*/
        modal: true,
        //按钮
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                addCustomerKeyword(uuid);
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
                    $("#customerKeywordDialog").dialog("close");
                    $('#customerKeywordForm')[0].reset();
                }
            }]
    });
    $("#customerKeywordDialog").dialog("open");
    $('#customerKeywordDialog').window("resize",{top:$(document).scrollTop() + 100});
}

function addCustomerKeyword(uuid) {
    var customerKeywords = [];
    var customerKeywordTextarea = $("#customerKeywordTextarea").val().trim();
    var group = $("#group").val().trim();
    if (group === '') {
        alert("请输入关键字分组名称");
        $("#group").focus();
        return;
    }
    if (customerKeywordTextarea === '') {
        alert("请输入关键字信息");
        $("#customerKeywordTextarea").focus();
        return;
    }
    var customerKeywordTextArray = customerKeywordTextarea.split("\n");
    if (customerKeywordTextArray.length == 1) {
        customerKeywordTextArray = customerKeywordTextarea.split("\r\n");
    }
    $.each(customerKeywordTextArray, function (idx, val) {
        val = val.trim();
        if(val !== ''){
            var customerKeywordAttributes = val.split(" ");
            if (customerKeywordAttributes.length == 1) {
                customerKeywordAttributes = val.split(" ");
            }
            if (customerKeywordAttributes.length == 1) {
                customerKeywordAttributes = val.split("	");
            }

            var tmpCustomerKeywordAttributes = [];
            $.each(customerKeywordAttributes, function (idx, val) {
                if (val !== '') {
                    tmpCustomerKeywordAttributes.push(val);
                }
            });
            var customerKeyword = {};
            customerKeyword.customerUuid = uuid;
            customerKeyword.keyword = tmpCustomerKeywordAttributes[0].trim();
            customerKeyword.url = tmpCustomerKeywordAttributes[1].trim();
            customerKeyword.optimizeGroupName = group.trim();

            customerKeyword.url = customerKeyword.url.replace("http://", "");
            customerKeyword.url = customerKeyword.url.replace("https://", "");

            if (customerKeyword.url.length > 25) {
                customerKeyword.url = customerKeyword.url.substring(0, 25);
            }

            customerKeywords.push(customerKeyword);
        }
    });

    $.ajax({
        url: '/internal/customerKeyword/saveCustomerKeywords',
        data: JSON.stringify(customerKeywords),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "添加成功");
            } else {
                $().toastmessage('showErrorToast', "添加失败");

            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "添加失败");
        }
    });
    $("#customerKeywordDialog").dialog("close");
}

function delCustomer(uuid) {
    if (confirm("确实要删除这个客户吗?") == false) return;
    $.ajax({
        url: '/internal/customer/delCustomer/' + uuid,
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

//将客户信息填充DIV"
function initCustomerDialog(customer) {
    var customerForm = $("#customerForm");
    customerForm.find("#contactPerson").val(customer.contactPerson);
    customerForm.find("#qq").val(customer.qq);
    customerForm.find("#telphone").val(customer.telphone);
    customerForm.find("#type").val(customer.type);
    customerForm.find("#status").val(customer.status);
    customerForm.find("#remark").val(customer.remark);
    customerForm.find("#entryTypeHidden").val(customer.entryType);
}

function changePaging(currentPage, pageSize) {
    var searchCustomerForm = $("#searchCustomerForm");
    var total = searchCustomerForm.find("#totalHidden").val();
    searchCustomerForm.find("#currentPageNumberHidden").val(currentPage);
    searchCustomerForm.find("#pageSizeHidden").val(pageSize);
    searchCustomerForm.submit();
}

function resetPageNumber() {
    var searchCustomerForm = $("#searchCustomerForm");
    searchCustomerForm.find("#currentPageNumberHidden").val(1);
}

function viewAizhanRank(contactPerson) {
    var index = contactPerson.indexOf("整站");
    if(index > -1) {
        contactPerson = contactPerson.substr(0, index);
    }
    window.open("https://baidurank.aizhan.com/baidu/" + contactPerson);
}