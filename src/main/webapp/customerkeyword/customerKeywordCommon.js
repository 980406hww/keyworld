$(function () {
    $("#saveCustomerKeywordDialog").dialog("close");
});

function trim(val)
{
    var re = /\s*((\S+\s*)*)/;
    return val.replace(re, "$1");
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
function setSecondThirdDefaultFee(self){
    $(self).val($(self).val().replace(/[^\d]*/g, ''));
    var positionFirstFee = document.getElementById("positionFirstFee");
    var positionSecondFee = document.getElementById("positionSecondFee");
    var positionThirdFee = document.getElementById("positionThirdFee");
    positionFirstFee.value = trim(positionFirstFee.value);
    positionSecondFee.value = trim(positionSecondFee.value);
    positionThirdFee.value = trim(positionThirdFee.value);
    if (positionFirstFee.value != "") {
        if (positionSecondFee.value == ""){
            positionSecondFee.value = positionFirstFee.value;
        }

        if (positionThirdFee.value == ""){
            positionThirdFee.value = positionFirstFee.value;
        }
    }
}
function setThirdDefaultFee(self){
    $(self).val($(self).val().replace(/[^\d]*/g, ''));
    var positionSecondFee = document.getElementById("positionSecondFee");
    var positionThirdFee = document.getElementById("positionThirdFee");
    positionSecondFee.value = trim(positionSecondFee.value);
    positionThirdFee.value = trim(positionThirdFee.value);
    if (positionSecondFee.value != "") {
        if (positionThirdFee.value == ""){
            positionThirdFee.value = positionSecondFee.value;
        }
    }
}
function setForthDefaultFee(self){
    $(self).val($(self).val().replace(/[^\d]*/g, ''));
    var positionForthFee = document.getElementById("positionForthFee");
    var positionTFifthFee = document.getElementById("positionFifthFee");
    positionForthFee.value = trim(positionForthFee.value);
    positionTFifthFee.value = trim(positionTFifthFee.value);
    if (positionForthFee.value != "") {
        if (positionTFifthFee.value == ""){
            positionTFifthFee.value = positionForthFee.value;
        }
    }
}


function addCustomerKeyword(customerKeyword, customerUuid) {
    if (customerKeyword == null) {
        $("#customerKeywordForm")[0].reset();
        $("#customerKeywordForm").find("#uuid").val('');
        $("#customerKeywordForm").find("#status").val('');
    }
    $("#saveCustomerKeywordDialog").show();
    $("#saveCustomerKeywordDialog").dialog({
        width: 410,
        height: 605,
        title : "添加关键字",
        modal: true,
        resizable: false,
        closed: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveCustomerKeyword(customerKeyword, customerUuid);
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
                    $("#saveCustomerKeywordDialog").dialog("close");
                    $('#customerKeywordForm')[0].reset();
                }
            }]
    });
    $("#saveCustomerKeywordDialog").dialog("open");
    $('#saveCustomerKeywordDialog').window("resize",{top:$(document).scrollTop() + 100});
}


function saveCustomerKeyword(customerKeyword, customerUuid) {
    if(customerKeyword == null) {
        customerKeyword = {};
    }
    var saveCustomerKeywordDialog= $("#saveCustomerKeywordDialog");
    customerKeyword.uuid = saveCustomerKeywordDialog.find("#uuid").val();
    customerKeyword.customerUuid = customerUuid;
    customerKeyword.searchEngine = saveCustomerKeywordDialog.find("#searchEngine").val();
    var keyword = $.trim(saveCustomerKeywordDialog.find("#keyword").val());
    if (keyword === '') {
        $().toastmessage('showWarningToast', "关键字不能为空");
        saveCustomerKeywordDialog.find("#keyword").focus();
        return;
    }
    customerKeyword.keyword = keyword;
    customerKeyword.recommendKeywords = $.trim(saveCustomerKeywordDialog.find("#recommendKeywords").val());
    customerKeyword.negativeKeywords = $.trim(saveCustomerKeywordDialog.find("#negativeKeywords").val());
    customerKeyword.excludeKeywords = $.trim(saveCustomerKeywordDialog.find("#excludeKeywords").val());
    customerKeyword.url = $.trim(saveCustomerKeywordDialog.find("#url").val());
    customerKeyword.originalUrl = $.trim(saveCustomerKeywordDialog.find("#originalUrl").val());
    customerKeyword.bearPawNumber = $.trim(saveCustomerKeywordDialog.find("#bearPawNumber").val());
    customerKeyword.optimizePlanCount = $.trim(saveCustomerKeywordDialog.find("#optimizePlanCount").val());
    customerKeyword.positionFirstFee = $.trim(saveCustomerKeywordDialog.find("#positionFirstFee").val());
    customerKeyword.positionSecondFee = $.trim(saveCustomerKeywordDialog.find("#positionSecondFee").val());
    customerKeyword.positionThirdFee = $.trim(saveCustomerKeywordDialog.find("#positionThirdFee").val());
    customerKeyword.positionForthFee = $.trim(saveCustomerKeywordDialog.find("#positionForthFee").val());
    customerKeyword.positionFifthFee = $.trim(saveCustomerKeywordDialog.find("#positionFifthFee").val());
    customerKeyword.positionFirstPageFee = $.trim(saveCustomerKeywordDialog.find("#positionFirstPageFee").val());
    customerKeyword.positionFirstCost = $.trim(saveCustomerKeywordDialog.find("#positionFirstCost").val());
    customerKeyword.positionSecondCost = $.trim(saveCustomerKeywordDialog.find("#positionSecondCost").val());
    customerKeyword.positionThirdCost = $.trim(saveCustomerKeywordDialog.find("#positionThirdCost").val());
    customerKeyword.positionForthCost = $.trim(saveCustomerKeywordDialog.find("#positionForthCost").val());
    customerKeyword.positionFifthCost = $.trim(saveCustomerKeywordDialog.find("#positionFifthCost").val());
    var initialPosition = $.trim(saveCustomerKeywordDialog.find("#initialPosition").val());
    customerKeyword.initialPosition = initialPosition;
    customerKeyword.currentPosition = initialPosition;
    customerKeyword.keywordEffect = $.trim(saveCustomerKeywordDialog.find("#keywordEffect").val());
    var initialIndexCount = $.trim(saveCustomerKeywordDialog.find("#initialIndexCount").val());
    customerKeyword.initialIndexCount = initialIndexCount;
    customerKeyword.currentIndexCount = initialIndexCount;
    customerKeyword.sequence = $.trim(saveCustomerKeywordDialog.find("#sequence").val());
    customerKeyword.title = $.trim(saveCustomerKeywordDialog.find("#title").val());
    customerKeyword.optimizeGroupName = $.trim(saveCustomerKeywordDialog.find("#optimizeGroupName").val());

    customerKeyword.machineGroup = $.trim(saveCustomerKeywordDialog.find("#machineGroup").val());
    customerKeyword.collectMethod = $.trim(saveCustomerKeywordDialog.find("#collectMethod").val());
    customerKeyword.serviceProvider = $.trim(saveCustomerKeywordDialog.find("#serviceProvider").val());
    customerKeyword.orderNumber = $.trim(saveCustomerKeywordDialog.find("#orderNumber").val());
    customerKeyword.paymentStatus = $.trim(saveCustomerKeywordDialog.find("#paymentStatus").val());

    customerKeyword.operateSelectKeyword = $("#operateSelectKeyword")[0].checked;
    customerKeyword.operateRelatedKeyword = $("#operateRelatedKeyword")[0].checked;
    customerKeyword.operateRecommendKeyword = $("#operateRecommendKeyword")[0].checked;
    customerKeyword.operateSearchAfterSelectKeyword = $("#operateSearchAfterSelectKeyword")[0].checked;

    customerKeyword.clickUrl = $("input[name='clickUrl']:checked").val();
    customerKeyword.showPage = $.trim(saveCustomerKeywordDialog.find("#showPage").val());
    customerKeyword.relatedKeywordPercentage = $.trim(saveCustomerKeywordDialog.find("#relatedKeywordPercentage").val());

    customerKeyword.remarks = $.trim(saveCustomerKeywordDialog.find("#remarks").val());
    customerKeyword.manualCleanTitle = true;
    var optimizedCount = saveCustomerKeywordDialog.find("#optimizedCount").val();
    var optimizeRemainingCount = customerKeyword.optimizePlanCount - optimizedCount;
    customerKeyword.optimizeRemainingCount = optimizeRemainingCount > 0 ? optimizeRemainingCount : 0;
    $.ajax({
        url: '/internal/customerKeyword/saveCustomerKeyword',
        data: JSON.stringify(customerKeyword),
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
    $("#saveCustomerKeywordDialog").dialog("close");
}

function modifyCustomerKeyword(customerKeywordUuid, customerUuid) {
    resetliItemColor();
    var saveCustomerKeywordDialog= $("#saveCustomerKeywordDialog");
    $.ajax({
        url: '/internal/customerKeyword/getCustomerKeywordByCustomerKeywordUuid/' + customerKeywordUuid,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (customerKeyword) {
            if (customerKeyword != null) {
                saveCustomerKeywordDialog.find("#uuid").val(customerKeyword.uuid);
                saveCustomerKeywordDialog.find("#optimizedCount").val(customerKeyword.optimizedCount);
                saveCustomerKeywordDialog.find("#keyword").val(customerKeyword.keyword);
                saveCustomerKeywordDialog.find("#bearPawNumber").val(customerKeyword.bearPawNumber);
                saveCustomerKeywordDialog.find("#recommendKeywords").val(customerKeyword.recommendKeywords);
                saveCustomerKeywordDialog.find("#negativeKeywords").val(customerKeyword.negativeKeywords);
                saveCustomerKeywordDialog.find("#excludeKeywords").val(customerKeyword.excludeKeywords);
                saveCustomerKeywordDialog.find("#searchEngine").val(customerKeyword.searchEngine);
                saveCustomerKeywordDialog.find("#initialIndexCount").val(customerKeyword.currentIndexCount);
                saveCustomerKeywordDialog.find("#initialPosition").val(customerKeyword.currentPosition == null ? '' : customerKeyword.currentPosition);
                saveCustomerKeywordDialog.find("#url").val(customerKeyword.url);
                saveCustomerKeywordDialog.find("#originalUrl").val(customerKeyword.originalUrl);
                saveCustomerKeywordDialog.find("#optimizePlanCount").val(customerKeyword.optimizePlanCount);
                saveCustomerKeywordDialog.find("#positionFirstFee").val(customerKeyword.positionFirstFee);
                saveCustomerKeywordDialog.find("#positionSecondFee").val(customerKeyword.positionSecondFee);
                saveCustomerKeywordDialog.find("#positionThirdFee").val(customerKeyword.positionThirdFee);
                saveCustomerKeywordDialog.find("#positionForthFee").val(customerKeyword.positionForthFee);
                saveCustomerKeywordDialog.find("#positionFifthFee").val(customerKeyword.positionFifthFee);
                saveCustomerKeywordDialog.find("#positionFirstPageFee").val(customerKeyword.positionFirstPageFee);
                saveCustomerKeywordDialog.find("#positionFirstCost").val(customerKeyword.positionFirstCost);
                saveCustomerKeywordDialog.find("#positionSecondCost").val(customerKeyword.positionSecondCost);
                saveCustomerKeywordDialog.find("#positionThirdCost").val(customerKeyword.positionThirdCost);
                saveCustomerKeywordDialog.find("#positionForthCost").val(customerKeyword.positionForthCost);
                saveCustomerKeywordDialog.find("#positionFifthCost").val(customerKeyword.positionFifthCost);
                saveCustomerKeywordDialog.find("#serviceProvider").val(customerKeyword.serviceProvider);
                saveCustomerKeywordDialog.find("#keywordEffect").val(customerKeyword.keywordEffect);
                saveCustomerKeywordDialog.find("#sequence").val(customerKeyword.sequence);
                saveCustomerKeywordDialog.find("#title").val(customerKeyword.title);
                saveCustomerKeywordDialog.find("#optimizeGroupName").val(customerKeyword.optimizeGroupName);
                saveCustomerKeywordDialog.find("#machineGroup").val(customerKeyword.machineGroup);
                saveCustomerKeywordDialog.find("#collectMethod").val(customerKeyword.collectMethod);
                saveCustomerKeywordDialog.find("#orderNumber").val(customerKeyword.orderNumber);
                saveCustomerKeywordDialog.find("#paymentStatus").val(customerKeyword.paymentStatus);

                if(customerKeyword.clickUrl == "clickPositiveUrl") {
                    $("#clickPositiveUrl").prop("checked", true);
                } else if(customerKeyword.clickUrl == "clickCommonUrl") {
                    $("#clickCommonUrl").prop("checked", true);
                } else {
                    $("#clickPositiveUrl").prop("checked", false);
                    $("#clickCommonUrl").prop("checked", false);
                }
                $("#operateSelectKeyword").prop("checked", customerKeyword.operateSelectKeyword);
                $("#operateRelatedKeyword").prop("checked", customerKeyword.operateRelatedKeyword);
                $("#operateRecommendKeyword").prop("checked", customerKeyword.operateRecommendKeyword);
                $("#operateSearchAfterSelectKeyword").prop("checked", customerKeyword.operateSearchAfterSelectKeyword);
                saveCustomerKeywordDialog.find("#showPage").val(customerKeyword.showPage);
                saveCustomerKeywordDialog.find("#relatedKeywordPercentage").spinner('setValue', customerKeyword.relatedKeywordPercentage);
                saveCustomerKeywordDialog.find("#remarks").val(customerKeyword.remarks);
                if(customerKeyword.positionFirstCost!=null||customerKeyword.positionSecondCost!=null||customerKeyword.positionThirdCost!=null||customerKeyword.positionForthCost!=null||customerKeyword.positionFifthCost!=null){
                    showCustomerKeywordCost();
                }
                addCustomerKeyword(customerKeyword, customerUuid);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function showCustomerKeywordCost() {
    $("#customerKeywordCost #customerKeywordCostFrom").toggle();
}
function onlyNumber(self) {
    $(self).val($(self).val().replace(/[^\d]*/g, ''));
}

//批量设置
function CustomerKeywordBatchUpdate(entryType) {
    var CustomerUuids = getSelectedCustomerUuids();

    if (CustomerUuids === '') {
        alert('请选择关键字进行修改');
        return;
    }
    resetliItemColor();
    $("#saveCustomerKeywordDialog").find('input[type=text],select,input[type=hidden]').each(function() {
        $(this).val('');
    });
    $("#saveCustomerKeywordDialog").find('input[type=checkbox]').each(function() {
        $(this).prop("checked",false);
    });
    $("#saveCustomerKeywordDialog").show();
    if(entryType !== "fm" ){
        $("#KeywordDiv").hide();
    }
    $("#saveCustomerKeywordDialog").dialog({
        resizable: false,
        title: "关键字批量设置(请将需要修改的字段点击标记为红色)",
        width: 410,
        maxHeight: 605,
        modal: true,
        onBeforeClose:function(){
            $("#KeywordDiv").show();
        },
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                if(isChecked("keyword")=="1" && $("#saveCustomerKeywordDialog").find("input[name='keyword']").val()==""){
                    alert("关键字不能为空");
                    return;
                }else {
                    saveChangeSetting(CustomerUuids);
                }
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#saveCustomerKeywordDialog").dialog("close");
                }
            }]
    });
    $('#saveCustomerKeywordDialog').window("resize",{top:$(document).scrollTop() + 100});
}

//得到选中的uuid
function getSelectedCustomerUuids() {
    var CustomerUuids = '';
    $.each($("input[name=uuid]:checkbox:checked"), function () {
        if (CustomerUuids === '') {
            CustomerUuids = $(this).val();
        } else {
            CustomerUuids = CustomerUuids + "," + $(this).val();
        }
    });
    return CustomerUuids;
}
//将li标签的文字默认为黑色
function resetliItemColor() {
    $("li").each(function () {
        $("li").css("color", "black");
    });
    $(".customerKeywordSpanClassa").each(function () {
        $(".customerKeywordSpanClassa").css("color", "black");
    });
}
function checkItem(self) {
    var color = $(self).css("color");
    if(color == "rgb(0, 0, 0)") {
        $(self).css("color", "red");
    } else {
        $(self).css("color", "black");
    }
}
function saveChangeSetting(CustomerUuids){
    var KeywordDialogDiv = $("#saveCustomerKeywordDialog");
    var keywordStatus = {};
    keywordStatus.keyword = KeywordDialogDiv.find("#keyword").val();
    keywordStatus.title = KeywordDialogDiv.find("#title").val();
    keywordStatus.bearPawNumber = KeywordDialogDiv.find("#bearPawNumber").val();
    keywordStatus.url = KeywordDialogDiv.find("#url").val();
    keywordStatus.originalUrl = KeywordDialogDiv.find("#originalUrl").val();
    keywordStatus.optimizePlanCount = KeywordDialogDiv.find("#optimizePlanCount").val();
    keywordStatus.optimizeGroupName = KeywordDialogDiv.find("#optimizeGroupName").val();
    keywordStatus.machineGroup =KeywordDialogDiv.find("#machineGroup").val();
    keywordStatus.initialIndexCount = KeywordDialogDiv.find("#initialIndexCount").val();
    keywordStatus.initialPosition = KeywordDialogDiv.find("#initialPosition").val();
    keywordStatus.positionFirstFee = KeywordDialogDiv.find("#positionFirstFee").val();
    keywordStatus.positionSecondFee = KeywordDialogDiv.find("#positionSecondFee").val();
    keywordStatus.positionThirdFee = KeywordDialogDiv.find("#positionThirdFee").val();
    keywordStatus.positionForthFee = KeywordDialogDiv.find("#positionForthFee").val();
    keywordStatus.positionFifthFee = KeywordDialogDiv.find("#positionFifthFee").val();
    keywordStatus.positionFirstPageFee = KeywordDialogDiv.find("#positionFirstPageFee").val();
    keywordStatus.serviceProvider = KeywordDialogDiv.find("#serviceProvider").val();
    keywordStatus.keywordEffect = KeywordDialogDiv.find("#keywordEffect").val();
    keywordStatus.collectMethod = KeywordDialogDiv.find("#collectMethod").val();
    keywordStatus.searchEngine = KeywordDialogDiv.find("#searchEngine").val();
    keywordStatus.sequence = KeywordDialogDiv.find("#sequence").val();
    keywordStatus.orderNumber = KeywordDialogDiv.find("#orderNumber").val();
    keywordStatus.paymentStatus = KeywordDialogDiv.find("#paymentStatus").val();
    keywordStatus.clickUrl = $("input[name='clickUrl']:checked").val();
    keywordStatus.recommendKeywords = KeywordDialogDiv.find("#recommendKeywords").val();
    keywordStatus.negativeKeywords = KeywordDialogDiv.find("#negativeKeywords").val();
    keywordStatus.excludeKeywords = KeywordDialogDiv.find("#excludeKeywords").val();
    keywordStatus.showPage = KeywordDialogDiv.find("#showPage").val();
    keywordStatus.relatedKeywordPercentage = KeywordDialogDiv.find("#relatedKeywordPercentage").val();
    keywordStatus.remarks = KeywordDialogDiv.find("#remarks").val();
    keywordStatus.positionFirstCost = KeywordDialogDiv.find("#positionFirstCost").val();
    keywordStatus.positionSecondCost = KeywordDialogDiv.find("#positionSecondCost").val();
    keywordStatus.positionThirdCost = KeywordDialogDiv.find("#positionThirdCost").val();
    keywordStatus.positionForthCost = KeywordDialogDiv.find("#positionForthCost").val();
    keywordStatus.positionFifthCost = KeywordDialogDiv.find("#positionFifthCost").val();
    keywordStatus.operateSelectKeyword = KeywordDialogDiv.find("#operateSelectKeyword:checked").val() === '1' ? 1 : 0;
    keywordStatus.operateRelatedKeyword = KeywordDialogDiv.find("#operateRelatedKeyword:checked").val() === '1' ? 1 : 0;
    keywordStatus.operateRecommendKeyword = KeywordDialogDiv.find("#operateRecommendKeyword:checked").val() === '1' ? 1 : 0;
    keywordStatus.operateSearchAfterSelectKeyword = KeywordDialogDiv.find("#operateSearchAfterSelectKeyword:checked").val() === '1' ? 1 : 0;
    if(CustomerUuids != null) {
        var fCustomerUuids = CustomerUuids;
        var keywordChecks = {};
        keywordChecks.keyword =isChecked("keyword");
        keywordChecks.title = isChecked("title");
        keywordChecks.bearPawNumber = isChecked("bearPawNumber");
        keywordChecks.url = isChecked("url");
        keywordChecks.originalUrl = isChecked("originalUrl");
        keywordChecks.optimizePlanCount = isChecked("optimizePlanCount");
        keywordChecks.optimizeGroupName =isChecked("optimizeGroupName");
        keywordChecks.machineGroup =isChecked("machineGroup");
        keywordChecks.initialIndexCount = isChecked("initialIndexCount");
        keywordChecks.initialPosition =isChecked("initialPosition");
        keywordChecks.positionFirstFee =isChecked("positionFirstFee");
        keywordChecks.positionSecondFee = isChecked("positionSecondFee");
        keywordChecks.positionThirdFee = isChecked("positionThirdFee");
        keywordChecks.positionForthFee = isChecked("positionForthFee");
        keywordChecks.positionFifthFee = isChecked("positionFifthFee");
        keywordChecks.positionFirstPageFee =isChecked("positionFirstPageFee");
        keywordChecks.serviceProvider = isChecked("serviceProvider");
        keywordChecks.keywordEffect = isChecked("keywordEffect");
        keywordChecks.collectMethod =isChecked("collectMethod");
        keywordChecks.searchEngine =isChecked("searchEngine");
        keywordChecks.sequence =isChecked("sequence");
        keywordChecks.orderNumber = isChecked("orderNumber");
        keywordChecks.paymentStatus = isChecked("paymentStatus");
        keywordChecks.clickUrl = isChecked($("input[name='clickUrl']:checked").val());
        keywordChecks.recommendKeywords = isChecked("recommendKeywords");
        keywordChecks.negativeKeywords = isChecked("negativeKeywords");
        keywordChecks.excludeKeywords = isChecked("excludeKeywords");
        keywordChecks.showPage =  isChecked("showPage");
        keywordChecks.relatedKeywordPercentage = isChecked("relatedKeywordPercentage");
        keywordChecks.remarks =isChecked("remarks");
        keywordChecks.positionFirstCost = isChecked("positionFirstCost");
        keywordChecks.positionSecondCost = isChecked("positionSecondCost");
        keywordChecks.positionThirdCost = isChecked("positionThirdCost");
        keywordChecks.positionForthCost = isChecked("positionForthCost");
        keywordChecks.positionFifthCost = isChecked("positionFifthCost");
        keywordChecks.operateSelectKeyword = isChecked("operateSelectKeyword") === '1' ? 1 : 0;
        keywordChecks.operateRelatedKeyword =isChecked("operateRelatedKeyword") === '1' ? 1 : 0;
        keywordChecks.operateRecommendKeyword = isChecked("operateRecommendKeyword") === '1' ? 1 : 0;
        keywordChecks.operateSearchAfterSelectKeyword = isChecked("operateSearchAfterSelectKeyword") === '1' ? 1 : 0;
        var postData = {};
        postData.keywordChecks = keywordChecks;
        postData.keywordStatus = keywordStatus;
        postData.customerUuids = fCustomerUuids;
        $.ajax({
            url: '/internal/customerKeyword/batchUpdateKeywordStatus',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if(result){
                    $().toastmessage('showSuccessToast', "更新成功",true);
                }else{
                    $().toastmessage('showErrorToast', "更新失败");
                }
                $("#saveCustomerKeywordDialog").dialog("close");
            },
            error: function () {
                $().toastmessage('showErrorToast', "更新失败");
                $("#saveCustomerKeywordDialog").dialog("close");
            }
        });
    } else {
        keywordStatus.CustomerUuid = settingDialogDiv.find("#uuid").val();
        $.ajax({
            url: '/internal/customerKeyword/saveCustomerKeyword',
            data: JSON.stringify(keywordStatus),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if(result){
                    $().toastmessage('showSuccessToast', "更新成功");
                }else{
                    $().toastmessage('showErrorToast', "更新失败");
                }
                $("#saveCustomerKeywordDialog").dialog("close");
            },
            error: function () {
                $().toastmessage('showErrorToast', "更新失败");
                $("#saveCustomerKeywordDialog").dialog("close");
            }
        });
    }
}
function isChecked(id) {
    var color = $("#saveCustomerKeywordDialog").find("#" + id).parent().css("color");
    if(color == "rgb(255, 0, 0)") {
        return "1";
    } else {
        return "0";
    }
}
// 修改熊掌号
function updateBearPawNumber(changeType, customerUuid) {
    $("#targetBearPawNumberDialog").css("display", "block");
    $("#targetBearPawNumberDialog").dialog({
        resizable: false,
        width: 260,
        height: 100,
        title:"修改关键字熊掌号",
        closed: true,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var targetBearPawNumber = $("#bearPawNumberChangeForm").find("#targetBearPawNumber").val();
                if (targetBearPawNumber == null || targetBearPawNumber == '') {
                    alert("请输入目标熊掌号!");
                    return;
                }
                var obj = {};
                if ("selected" === changeType){
                    var uuids = getSelectedCustomerUuids();
                    if(uuids === ''){
                        alert('请选择要修改熊掌号的关键字！');
                        return ;
                    }
                    if (confirm("确定要修改选中关键字的熊掌号吗?") == false) return;
                    obj['uuids'] = uuids.split(",");
                }else{
                    if (confirm("确定要修改当前查询条件下所有关键字的熊掌号吗?") == false) return;
                    var postData = $("#searchCustomerKeywordForm").serializeArray();
                    $.each(postData, function() {
                        if (obj[this.name]) {
                            if (!obj[this.name].push) {
                                obj[this.name] = [obj[this.name]];
                            }
                            obj[this.name].push(this.value || '');
                        } else {
                            obj[this.name] = this.value || '';
                        }
                    });
                }
                obj.targetBearPawNumber = targetBearPawNumber;
                obj.customerUuid = customerUuid;

                $.ajax({
                    url: '/internal/customerKeyword/updateBearPawNumber',
                    data: JSON.stringify(obj),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (data) {
                        if(data){
                            $().toastmessage('showSuccessToast',"操作成功", true);
                        }else{
                            $().toastmessage('showErrorToast', "操作失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                });
                $("#targetBearPawNumberDialog").dialog("close");
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#targetBearPawNumberDialog").dialog("close");
                    $('#bearPawNumberChangeForm')[0].reset();
                }
            }],
        onClose: function () {
            $('#bearPawNumberChangeForm')[0].reset();
        }
    });
    $("#targetBearPawNumberDialog").dialog("open");
    $('#targetBearPawNumberDialog').window("resize",{top:$(document).scrollTop() + 200});
}










