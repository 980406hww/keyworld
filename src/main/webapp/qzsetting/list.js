$(function () {
    $("#chargeLogListDiv").dialog("close");
    $("#chargeDialog").dialog("close");
    $("#changeSettingDialog").dialog("close");
    $("#showQZSettingDiv").css("margin-top",$("#topDiv").height());
    alignTableHeader();
    window.onresize = function(){
        alignTableHeader();
    }
    var searchCustomerForm = $("#chargeForm");
    var pageSize = searchCustomerForm.find('#pageSizeHidden').val();
    var pages = searchCustomerForm.find('#pagesHidden').val();
    var currentPageNumber = searchCustomerForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    showCustomerBottomDiv.find('#chooseRecords').val(pageSize);
    showCustomerBottomDiv.find('#pagesHidden').val(pages);

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
});
function alignTableHeader(){
    var td = $("#headerTable tr:first td");
    var ctd = $("#showQZSettingTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function changePaging(currentPage, pageSize) {
    var chargeForm = $("#chargeForm");
    chargeForm.find("#currentPageNumberHidden").val(currentPage);
    chargeForm.find("#pageSizeHidden").val(pageSize);
    chargeForm.submit();
}
function resetSearchCondition(days) {
    var chargeForm = $("#chargeForm");
    var customerInfo = chargeForm.find("#customerInfo").val();
    var customerUuid = customerInfo.substr(customerInfo.lastIndexOf("_") + 1);
    chargeForm.find("#customerUuid").val(customerUuid);
    chargeForm.find("#dateRangeType").val(days);
    chargeForm.find("#currentPageNumberHidden").val(1);
    chargeForm.submit();
}
function selectAll(self){
    var a = document.getElementsByName("uuid");
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
function delQZSetting(uuid) {
    if (confirm("确实要删除这个全站设置吗?") == false) return;
    $.ajax({
        url: '/internal/qzsetting/delete/' + uuid,
        type: 'Get',
        success: function (data) {
            if(data){
                $().toastmessage('showSuccessToast', "删除成功！", true);
            }else{
                $().toastmessage('showErrorToast', "删除失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败");
        }
    });
}
function getSelectedIDs() {
    var uuids = '';
    $.each($("input[name=uuid]:checkbox:checked"), function(){
        if(uuids === ''){
            uuids = $(this).val();
        }else{
            uuids = uuids + "," + $(this).val();
        }
    });
    return uuids;
}
function delSelectedQZSettings(self) {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        alert('请选择要操作的设置信息！');
        return ;
    }
    if (confirm("确实要删除这些关键字吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/qzsetting/deleteQZSettings',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if(data){
                $().toastmessage('showSuccessToast', "操作成功", true);
            }else{
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function updateImmediately(self) {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        alert('请选择要操作的设置信息！');
        return ;
    }
    if (confirm("确实要马上更新这些设置吗？") == false) return;
    var postData = {};
    postData.uuids = uuids;
    $.ajax({
        url: '/internal/qzsetting/updateImmediately',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if(data){
                $().toastmessage('showSuccessToast', "操作成功", true);
            }else{
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function toTimeFormat(time) {
    var date = toDateFormat(time);
    var hours = time.getHours() < 10 ? ("0" + time.getHours()) : time.getHours();
    var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
    var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
    return date + " " + hours + ":" + minutes + ":" + seconds;
};
function toDateFormat (time) {
    return time.getFullYear() + "-" +
        (time.getMonth() + 1) + "-" +
        time.getDate();
};
function saveChargeLog(self) {
    var chargeDialog = $("#chargeDialog");
    var selectedOperationTypes = chargeDialog.find("input[name=operationType]:checkbox:checked");
    var saveChargeLogFlag = true;
    if (selectedOperationTypes.length == 0) {
        alert("必须选择一个收费项才能收费");
        saveChargeLogFlag = false;
        return;
    }
    var chargeLogs = [];
    $.each(selectedOperationTypes, function (index, val) {
        var chargeLog = {};
        chargeLog.qzOperationTypeUuid = chargeDialog.find(
            "#qzOperationTypeUuid" + val.id).val();
        chargeLog.planChargeDate = chargeDialog.find("#planChargeDate" + val.id).val();
        chargeLog.actualChargeDate = chargeDialog.find("#actualChargeDate" + val.id).val();
        chargeLog.receivableAmount = chargeDialog.find("#receivableAmount" + val.id).val();
        chargeLog.actualAmount = chargeDialog.find("#actualAmount" + val.id).val();
        chargeLog.nextChargeDate = chargeDialog.find("#nextChargeDate" + val.id).val();
        if (chargeLog.nextChargeDate == "" || chargeLog.nextChargeDate == null) {
            alert("下次收费日期为必填");
            chargeDialog.find("#nextChargeDate" + val.id).focus();
            saveChargeLogFlag = false;
            return;
        }
        if (chargeLog.actualAmount == "" || chargeLog.actualAmount == null) {
            alert("实收金额为必填");
            chargeDialog.find("#actualAmount" + val.id).focus();
            saveChargeLogFlag = false;
            return;
        }
        if (!reg.test(chargeLog.actualAmount)) {
            alert("请输入合理的金额");
            chargeDialog.find("#actualAmount" + val.id).focus();
            saveChargeLogFlag = false;
            return;
        }
        if (chargeLog.actualChargeDate == "" || chargeLog.actualChargeDate == null) {
            alert("实际收费日期为必填");
            chargeDialog.find("#actualChargeDate" + val.id).focus();
            saveChargeLogFlag = false;
            return;
        }
        chargeLogs.push(chargeLog);
    });
    if(saveChargeLogFlag) {
        if (window.confirm("确认收费?")) {
            $.ajax({
                url: '/internal/qzchargelog/save',
                data: JSON.stringify(chargeLogs),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    resetChargeDialog();
                    if (data != null && data != "") {
                        $().toastmessage('showSuccessToast', "收费成功！", true);
                        $("#chargeDialog").dialog("close");
                    } else {
                        $().toastmessage('showErrorToast', "收费失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "收费失败");
                }
            });
        } else {
            alert("取消收费");
        }
    }
}
function calTotalAmount() {
    var totalAmount = 0;
    var chargeDialog = $("#chargeDialog");
    $.each(chargeDialog.find("input[name=operationType]:checkbox:checked"), function(idx, val){
        totalAmount = totalAmount + Number(chargeDialog.find("#actualAmount" + val.id).val());
    });
    var str = new String(totalAmount);
    var total = str.replace( /\B(?=(?:\d{3})+$)/g, ',' );
    chargeDialog.find("#totalAmount").html(total + "元");
}
function showChargeLog(uuid, self) {
    $("#chargeLogListTable  tr:not(:first)").remove();
    $.ajax({
        url: '/internal/qzchargelog/chargesList/' + uuid,
        type: 'Get',
        success: function (qzChargeLogs) {
            if (qzChargeLogs != null && qzChargeLogs.length > 0) {
                $.each(qzChargeLogs, function (idx, val) {
                    var newTr = document.createElement("tr")
                    var chargeLogElements = [
                        toDateFormat(new Date(val.actualChargeDate)),
                        val.operationType,
                        val.actualAmount,
                        val.userName,
                        toTimeFormat(new Date(val.createTime))
                    ];
                    $.each(chargeLogElements, function () {
                        var newTd = document.createElement("td");
                        newTr.appendChild(newTd);
                        newTd.innerHTML = this;
                    });
                    $("#chargeLogListTable")[0].lastChild.appendChild(newTr);
                });
                $("#chargeLogListDiv").dialog({
                    resizable: false,
                    width: 370,
                    title:"收费记录",
                    modal: true,
                    buttons: [{
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#chargeLogListDiv").dialog("close");
                        }
                    }]
                });
                $("#chargeLogListDiv").dialog("open");
                $("#chargeLogListDiv").window("resize",{top:$(document).scrollTop() + 100});
            } else {
                alert("暂无收费记录");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
}
function showChargeDialog(uuid,contactPerson,domain,self) {
    var chargeDialogObj = $("#chargeDialog");
    chargeDialogObj.find("#qzSettingCustomer").val(contactPerson);
    chargeDialogObj.find("#qzSettingDomain").val(domain);
    $.ajax({
        url: '/internal/qzchargelog/getQZChargeLog/' + uuid,
        type: 'Get',
        success: function (chargeInfos) {
            if(chargeInfos != null && chargeInfos.length > 0){
                resetChargeDialog();
                var totalAmount = 0;
                var showFlag = false;
                $.each(chargeInfos, function(idx, val){
                    var checkbox = chargeDialogObj.find("#" + val.operationType);
                    if(val.qzOperationTypeUuid != null) {
                        // 存在此类操作类型
                        chargeDialogObj.find("#qzOperationTypeUuid" + val.operationType).val(val.qzOperationTypeUuid);
                        chargeDialogObj.find("#initialKeywordCount" + val.operationType).val(val.initialKeywordCount);
                        chargeDialogObj.find("#currentKeywordCount" + val.operationType).val(val.currentKeywordCount);
                        chargeDialogObj.find("#receivableAmount" + val.operationType).val(val.receivableAmount);
                        chargeDialogObj.find("#actualAmount" + val.operationType).val(val.receivableAmount);
                        totalAmount = totalAmount + Number(val.receivableAmount == null ? 0 : val.receivableAmount);
                        chargeDialogObj.find("#checkCharge" + val.operationType).css("display","block");
                        // 达标
                        if(val.planChargeDate != null) {
                            checkbox[0].checked = true;
                            chargeDialogObj.find("#" + val.operationType + "ChargeInfo").css("display","block");
                            var date = new Date(val.planChargeDate);
                            var year = date.getFullYear();
                            var month = date.getMonth() + 2;
                            if(month < 10) {
                                month = "0" + month;
                            } else {
                                if(month > 12) {
                                    month = "01";
                                    year = year + 1;
                                }
                            }
                            var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
                            var nextChargeDate = year + "-" + month + "-" + day;
                            chargeDialogObj.find("#receivableAmount" + val.operationType).val(val.receivableAmount);
                            chargeDialogObj.find("#planChargeDate" + val.operationType).val(val.planChargeDate);
                            chargeDialogObj.find("#nextChargeDate" + val.operationType).val(nextChargeDate);
                            showFlag = true;
                        } else {
                            checkbox[0].checked = false;
                            chargeDialogObj.find("#" + val.operationType + "ChargeInfo").css("display","none");
                        }
                        chargeDialogObj.find("#actualChargeDate" + val.operationType).val(today);
                    } else {
                        chargeDialogObj.find("#checkCharge" + val.operationType).css("display","none");
                        chargeDialogObj.find("#" + val.operationType + "ChargeInfo").css("display","none");
                    }
                });

                if(!showFlag){
                    chargeDialogObj.find("#chargeInfoTable").css("height",0);
                }

                var s = new String(totalAmount);
                var total = s.replace(/\B(?=(?:\d{3})+$)/g, ',');
                chargeDialogObj.find("#totalAmount").html(total+"元");
                $("#chargeDialog").dialog({
                    resizable: false,
                    modal: true,
                    width: 390,
                    height:360,
                    title: '收费',
                    buttons: [{
                        text: '收费',
                        iconCls: 'icon-ok',
                        handler: function () {
                            saveChargeLog(this);
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#chargeDialog").dialog("close");
                        }
                    }]
                });
                $("#chargeDialog").dialog("open");
                $("#chargeDialog").window("resize",{top:$(document).scrollTop() + 100});
            }else{
                $().toastmessage('showErrorToast', "获取信息失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
}
function resetChargeDialog() {
    var chargeDialogObj = $("#chargeDialog");
    chargeDialogObj.find("#PCChargeInfo").css("display","none");
    chargeDialogObj.find("#PhoneChargeInfo").css("display","none");
    chargeDialogObj.find("#checkChargePC").css("display","none");
    chargeDialogObj.find("#checkChargePhone").css("display","none");
    var operationTypes = chargeDialogObj.find("input[name=operationType]");
    $.each(operationTypes,function (idx, val) {
        val.checked = false;
        chargeDialogObj.find("#qzOperationTypeUuid" + val.id).val("");
        chargeDialogObj.find("#initialKeywordCount" + val.id).val("");
        chargeDialogObj.find("#currentKeywordCount" + val.id).val("");
        chargeDialogObj.find("#receivableAmount" + val.id).val("");
        chargeDialogObj.find("#planChargeDate" + val.id).val("");
        chargeDialogObj.find("#nextChargeDate" + val.id).val("");
        chargeDialogObj.find("#actualAmount" + val.id).val("");
        chargeDialogObj.find("#actualChargeDate" + val.id).val("");
    });
}
function dealChargeTable(operationType) {
    var chargeDialog = $("#chargeDialog");
    var checkboxObj = chargeDialog.find("#" + operationType);
    var chargeInfoObj = chargeDialog.find("#" + operationType + "ChargeInfo");
    if (chargeInfoObj.css("display") == "none" || checkboxObj[0].checked == true) {
        chargeInfoObj.css("display", "block");
    } else {
        chargeInfoObj.css("display", "none");
    }
    calTotalAmount();
    var selectedOperationTypes = chargeDialog.find("input[name=operationType]:checkbox:checked");
    if (selectedOperationTypes.length == 0) {
        chargeDialog.find("#chargeInfoTable").css("height",0);
    }
}
function createSettingDialog() {
    $("#changeSettingDialog").dialog({
        resizable: false,
        height: 450,
        width: 340,
        title: '全站设置',
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveChangeSetting(this);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#changeSettingForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#changeSettingDialog").dialog("close");
                    $('#changeSettingForm')[0].reset();
                }
            }]
    });
    $("#changeSettingDialog").dialog("open");
    $("#changeSettingDialog").window("resize",{top:$(document).scrollTop() + 100});

}
function resetSettingDialog() {
    var settingDialogDiv = $("#changeSettingDialog");
    settingDialogDiv.find("#qzSettingUuid").val("");
    settingDialogDiv.find("#qzSettingCustomer").val("");
    settingDialogDiv.find("#qzSettingDomain").val("");
    settingDialogDiv.find("#qzSettingIgnoreNoIndex").val("1");
    settingDialogDiv.find("#qzSettingIgnoreNoOrder").val("1");
    settingDialogDiv.find("#qzSettingInterval").val("2");
    clearInfo("Both");
}
function clearInfo(type) {
    var settingDialogObj = $("#changeSettingDialog");
    if(type == "Both") {
        clearInfo("PC");
        clearInfo("Phone");
    } else {
        // 清空分组表格信息
        settingDialogObj.find("#group" + type).val("");
        settingDialogObj.find("#initialKeywordCount" + type).val("");
        settingDialogObj.find("#currentKeywordCount" + type).val("");
        settingDialogObj.find("#qzOperationTypeUuid" + type).val("");
        // 清空规则表格信息
        settingDialogObj.find("#chargeRule" + type).find("tr:not(:first,:last)").remove();
        settingDialogObj.find("#" + type)[0].checked = false;
        settingDialogObj.find("#operationTypeSummaryInfo" + type).css("display","none");
        settingDialogObj.find("#chargeRule" + type).css("display","none");
    }
}
function showSettingDialog(uuid, self) {
    resetSettingDialog();
    if(uuid == null){
        createSettingDialog();
        return;
    }
    $.ajax({
        url: '/internal/qzsetting/getQZSetting/' + uuid,
        type: 'Get',
        success: function (qzSetting) {
            if(qzSetting != null){
                initSettingDialog(qzSetting, self);
                createSettingDialog();
            }else{
                $().toastmessage('showErrorToast', "获取信息失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
}
function initSettingDialog(qzSetting, self) {
    var PCType = false;
    var PhoneType = false;
    var settingDialogDiv = $("#changeSettingDialog");
    settingDialogDiv.find("#qzSettingUuid").val(qzSetting.uuid);
    settingDialogDiv.find("#qzSettingCustomer").val(
        qzSetting.contactPerson + "_____" + qzSetting.customerUuid);
    settingDialogDiv.find("#qzSettingDomain").val(
        qzSetting.domain != null ? qzSetting.domain : "");
    settingDialogDiv.find("#qzSettingIgnoreNoIndex").val(qzSetting.ignoreNoIndex ? "1" : "0");
    settingDialogDiv.find("#qzSettingIgnoreNoOrder").val(qzSetting.ignoreNoOrder ? "1" : "0");
    settingDialogDiv.find("#qzSettingInterval").val(
        qzSetting.updateInterval != null ? qzSetting.updateInterval : "");
    settingDialogDiv.find("#qzSettingEntryType").val(
        qzSetting.type != null ? qzSetting.type : "");
    // 操作类型表填充数据
    $.each(qzSetting.qzOperationTypes, function (idx, val) {
        settingDialogDiv.find("#group" + val.operationType).val(val.group);
        settingDialogDiv.find("#subDomainName" + val.operationType).val(val.subDomainName);
        settingDialogDiv.find("#initialKeywordCount" + val.operationType).val(
            val.initialKeywordCount);
        settingDialogDiv.find("#currentKeywordCount" + val.operationType).val(
            val.currentKeywordCount);
        settingDialogDiv.find("#qzSettingUuid" + val.operationType).val(val.uuid);
        // 构造规则表
        $.each(val.qzChargeRules, function (chargeRuleIdx, chargeRuleVal) {
            addRow("chargeRule" + val.operationType, chargeRuleVal);
            if (val.operationType === 'PC') {
                PCType = true;
            }
            if (val.operationType === 'Phone') {
                PhoneType = true;
            }
        });
    });
    if (PCType) {
        dealSettingTable("PC");
    }

    if (PhoneType) {
        dealSettingTable("Phone");
    }
}
//规则表验证
var reg = /^[1-9]\d*$/;
function saveChangeSetting(self) {
    var settingDialogDiv = $("#changeSettingDialog");
    var qzSetting = {};
    qzSetting.uuid = settingDialogDiv.find("#qzSettingUuid").val();

    var customer = settingDialogDiv.find("#qzSettingCustomer").val();
    if (customer == null || customer === "") {
        alert("请选择客户");
        settingDialogDiv.find("#qzSettingCustomer").focus();
        return;
    }
    qzSetting.domain = settingDialogDiv.find("#qzSettingDomain").val();
    if (qzSetting.domain == null || qzSetting.domain === "") {
        alert("请输入域名");
        settingDialogDiv.find("#qzSettingDomain").focus();
        return;
    }
    qzSetting.ignoreNoIndex = settingDialogDiv.find("#qzSettingIgnoreNoIndex").val() === "1"
        ? true : false;
    qzSetting.ignoreNoOrder = settingDialogDiv.find("#qzSettingIgnoreNoOrder").val() === "1"
        ? true : false;
    qzSetting.updateInterval = settingDialogDiv.find("#qzSettingInterval").val();
    qzSetting.pcGroup = settingDialogDiv.find("#groupPC").val();
    qzSetting.phoneGroup = settingDialogDiv.find("#groupPhone").val();
    if(qzSetting.pcGroup == "") {
        qzSetting.pcGroup = null;
    }
    if(qzSetting.phoneGroup == "") {
        qzSetting.phoneGroup = null;
    }

    if (customer != null && customer != '') {
        var customerArray = customer.split("_____");
        if (customerArray.length == 2) {
            qzSetting.customerUuid = customerArray[1];
        } else {
            alert("请从列表中选择客户");
            settingDialogDiv.find("#qzSettingCustomer").focus();
            return;
        }
    }
    var entryType = settingDialogDiv.find("#qzSettingEntryType").val();
    qzSetting.type = entryType;
    qzSetting.qzOperationTypes = [];//操作类型表
    qzSetting.qzOperationTypes.qzChargeRules = [];//收费规则

    var checkedObjs = settingDialogDiv.find("input[name=operationType]:checkbox:checked");
    var validationFlag = true;
    $.each(checkedObjs, function (idx, val) {
        var ruleObj = settingDialogDiv.find("#chargeRule" + val.id);
        var operationType = {};
        operationType.qzChargeRules = [];
        operationType.operationType = val.id;
        operationType.group = settingDialogDiv.find("#group" + val.id).val();
        operationType.initialKeywordCount = settingDialogDiv.find(
            "#initialKeywordCount" + val.id).val();
        operationType.currentKeywordCount = settingDialogDiv.find(
            "#currentKeywordCount" + val.id).val();
        operationType.subDomainName = settingDialogDiv.find("#subDomainName" + val.id).val();

        if (operationType.group == null || operationType.group === "") {
            alert("请输入分组");
            settingDialogDiv.find("#group" + val.id).focus();
            validationFlag = false;
            return false;
        }
//        if (operationType.initialKeywordCount == null || operationType.initialKeywordCount === "") {
//          alert("请输入初始词量");
//          settingDialogDiv.find("#initialKeywordCount" + val.id).focus();
//          validationFlag = false;
//          return false;
//        }
        if (operationType.initialKeywordCount != "" && !reg.test(operationType.initialKeywordCount)) {
            alert("请输入数字");
            settingDialogDiv.find("#initialKeywordCount" + val.id).focus();
            validationFlag = false;
            return false;
        }
        //多条规则
        var endKeyWordCountValue = -1;
        var trObjs = ruleObj.find("tr:not(:first,:last)");
        $.each(trObjs, function (idx, val) {
            var startKeywordCountObj = $(val).find("input[name=startKeywordCount]");
            var endKeywordCountObj = $(val).find("input[name=endKeywordCount]");
            var amountObj = $(val).find("input[name=amount]");

            var chargeRule = {};
            chargeRule.startKeywordCount = startKeywordCountObj.val();
            chargeRule.endKeywordCount = endKeywordCountObj.val();
            chargeRule.amount = amountObj.val();
            operationType.qzChargeRules.push(chargeRule);

            if (startKeywordCountObj.val() == null || startKeywordCountObj.val() == "") {
                alert("请输入起始词数");
                startKeywordCountObj[0].focus();
                validationFlag = false;
                return false;
            }
            if (!reg.test(startKeywordCountObj.val())) {
                alert("请输入数字");
                startKeywordCountObj.focus();
                validationFlag = false;
                return false;
            }
            var skc = Number(startKeywordCountObj.val());
            if (skc <= endKeyWordCountValue) {
                alert("起始词数过小");
                startKeywordCountObj.focus();
                validationFlag = false;
                return false;
            }
            if (idx < (trObjs.length - 1)) {
                if (endKeywordCountObj.val() == null || endKeywordCountObj.val() == "") {
                    alert("请输入终止词数");
                    endKeywordCountObj.focus();
                    validationFlag = false;
                    return false;
                }
            } else {
                if(endKeywordCountObj.val() != "" && operationType.currentKeywordCount>Number(endKeywordCountObj.val())){
                    alert("最后一条规则中的终止词量必须大于当前词量");
                    endKeywordCountObj.focus();
                    validationFlag = false;
                    return false;
                }
            }
            if (endKeywordCountObj.val() != "") {
                if (!reg.test(endKeywordCountObj.val())) {
                    alert("请输入数字");
                    endKeywordCountObj.focus();
                    validationFlag = false;
                    return false;
                }
                if (Number(endKeywordCountObj.val()) <= skc) {
                    alert("终止词数必须大于起始词数");
                    endKeywordCountObj.focus();
                    validationFlag = false;
                    return false;
                }
            }
            if (amountObj.val() == null || amountObj.val() == "") {
                alert("请输入价格");
                amountObj.focus();
                validationFlag = false;
                return false;
            }
            if (!reg.test(amountObj.val())) {
                alert("输入的价格不合理");
                amountObj.focus();
                validationFlag = false;
                return false;
            }
            endKeyWordCountValue = Number(endKeywordCountObj.val());
        });
        if (!validationFlag) {
            return false;
        }
        qzSetting.qzOperationTypes.push(operationType);
    });

    if (validationFlag) {
        if (checkedObjs.length == 0) {
            alert("保存失败，必须要增加一条规则");
            return;
        }
        $.ajax({
            url: '/internal/qzsetting/save',
            data: JSON.stringify(qzSetting),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (data) {
                if (data != null && data != "") {
                    $().toastmessage('showSuccessToast', "更新成功", true);
                } else {
                    $().toastmessage('showErrorToast', "更新失败！");
                }
                $("#changeSettingDialog").dialog("close");
            },
            error: function () {
                $().toastmessage('showErrorToast', "更新失败！");
            }
        });
    }
}
function addRow(tableID){
    addRow(tableID, null);
}
function addRow(tableID, chargeRule){
    var tableObj = $("#" + tableID);
    var rowCount = tableObj.find("tr").length;
    var newRow = tableObj[0].insertRow(rowCount - 1); //插入新行

    var col1 = newRow.insertCell(0);
    col1.innerHTML="<input type='text' name='sequenceID' value='"+(rowCount - 1)+"' style='width:100%'/>";
    var col2 = newRow.insertCell(1);
    col2.innerHTML = "<input type='text' name='startKeywordCount' value='"+(chargeRule != null ? chargeRule.startKeywordCount : '')+"' style='width:100%'/>";
    var col3 = newRow.insertCell(2);
    col3.innerHTML = "<input type='text' name='endKeywordCount' value='"+((chargeRule != null && chargeRule.endKeywordCount != null) ? chargeRule.endKeywordCount : '')+"'  style='width:100%'/>";
    var col4 = newRow.insertCell(3);
    col4.innerHTML = "<input type='text' name='amount' value='"+(chargeRule != null ? chargeRule.amount : '')+"'  style='width:100%'/>";
    var col5 = newRow.insertCell(4);
    col5.innerHTML = "<input style='width:100%' type='button' value='删除' onclick='deleteCurrentRow(this.parentNode.parentNode)' />";
}
function deleteCurrentRow(currentRow) {
    var index = currentRow.rowIndex;
    var tableObj = currentRow.parentNode.parentNode;
    if(tableObj.rows.length > 3) {
        tableObj.deleteRow(index);
        $.each($("#"+tableObj.id).find("input[name=sequenceID]"), function(idx, val){
            $(val).val(idx + 1);
        });
    } else {
        alert("删除失败，规则表不允许为空");
    }
}
function dealSettingTable(operationType) {
    var settingDialogDiv = $("#changeSettingDialog");
    var groupObj = settingDialogDiv.find('#operationTypeSummaryInfo' + operationType);
    var ruleObj = settingDialogDiv.find('#chargeRule' + operationType);
    var chargeRuleRowCount = ruleObj.find("tr").length;
    var checkboxObj = settingDialogDiv.find('#' + operationType);

    if (ruleObj.css("display") == "none" || checkboxObj[0].checked == true) {
        // 保证必须有一条规则
        if (chargeRuleRowCount <= 2) {
            addRow("chargeRule" + operationType);
        }
        groupObj.css("display","block");
        ruleObj.css("display","block");
        checkboxObj[0].checked = true;
    } else {
        clearInfo(operationType);
        groupObj.css("display","none");
        ruleObj.css("display","none");
        checkboxObj[0].checked = false;
    }
}