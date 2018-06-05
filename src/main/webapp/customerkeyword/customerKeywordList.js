$(function () {
    $("#groupChangeNameDialog").dialog("close");
    $("#groupChangeNameByRankDialog").dialog("close");
    $("#changeSearchEngineDialog").dialog("close");
    $("#uploadExcelDailog").dialog("close");
    $("#saveCustomerKeywordDialog").dialog("close");
    $("#optimizePlanCountDialog").dialog("close");
    $("#customerKeywordDiv").css("margin-top",$("#customerKeywordTopDiv").height());
    $('#customerList').dialog("close");
    alignTableHeader();
    window.onresize = function(){
        alignTableHeader();
    }
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
function delItem(customerKeywordUuid) {
    if (confirm("确实要删除这个关键字吗?") == false) return;
    $.ajax({
        url: '/internal/customerKeyword/deleteCustomerKeyword/' + customerKeywordUuid,
        type: 'GET',
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
function delAllItems(deleteType,customerUuid) {
    var customerKeywordUuids = getUuids();
    switch (deleteType){
        case 'ByUuid' :
            if (customerKeywordUuids === '') {
                alert('请选择要操作的信息');
                return;
            }
            if (confirm("确实要删除这些关键字吗?") == false) return;
            break;
        case 'EmptyTitleAndUrl' :
            if (confirm("确实要删除标题和网址为空的关键字吗?") == false) return;
            break;
        case 'EmptyTitle' :
            if (confirm("确实要删除标题为空的关键字吗?") == false) return;
            break;
    }
    var deletionCriteria = {};
    if(customerKeywordUuids !== ''){
        deletionCriteria.uuids = customerKeywordUuids.split(",");
    }
    deletionCriteria.deleteType = deleteType;
    deletionCriteria.customerUuid = customerUuid;
    $.ajax({
        url: '/internal/customerKeyword/deleteCustomerKeywords',
        data: JSON.stringify(deletionCriteria),
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
function getUuids() {
    var a = document.getElementsByName("uuid");
    var uuids = '';
    for (var i = 0; i < a.length; i++) {
        if (a[i].checked) {
            if (uuids === '') {
                uuids = a[i].value;
            } else {
                uuids = uuids + ',' + a[i].value;
            }
        }
    }
    return uuids;
}
function cleanTitle(customerUuid, cleanType) {
    var customerKeywordCleanCriteria = {};
    if (cleanType == 'SelectedCustomerKeywordTitle' || cleanType == 'CaptureTitleBySelected') {
        var customerKeywordUuids = getUuids();
        if (customerKeywordUuids.trim() === '') {
            alert("请选中要操作的关键词！");
            return;
        }
        if (confirm("确认要清空所选关键词的标题吗?") == false) return;
        customerKeywordCleanCriteria.customerKeywordUuids = customerKeywordUuids.split(",");
    }else if(cleanType == 'CaptureTitleFlag'){
        if (confirm("确认要重新采集自动导入的关键词的标题吗?") == false) return;
    }else if(cleanType == 'CaptureTitleBySelected'){
        if (confirm("确认要重新采集所选关键词的标题吗?") == false) return;
    }else {
        if (confirm("确认要清空客户下所有关键词的标题吗?") == false) return;
    }
    customerKeywordCleanCriteria.cleanType = cleanType;
    customerKeywordCleanCriteria.customerUuid = customerUuid;

    $.ajax({
        url: '/internal/customerKeyword/cleanTitle',
        data: JSON.stringify(customerKeywordCleanCriteria),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (status) {
            if (status) {
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
function changePaging(currentPage, pageSize) {
    var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
    searchCustomerKeywordForm.find("#currentPageNumberHidden").val(currentPage);
    searchCustomerKeywordForm.find("#pageSizeHidden").val(pageSize);
    searchCustomerKeywordForm.submit();
}
function resetPageNumber() {
    var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
    var keyword = searchCustomerKeywordForm.find("#keyword").val();
    var url = searchCustomerKeywordForm.find("#url").val();
    var optimizeGroupName = searchCustomerKeywordForm.find("#optimizeGroupName").val();
    var gtOptimizedCount = searchCustomerKeywordForm.find("#gtOptimizedCount").val();
    var ltOptimizedCount = searchCustomerKeywordForm.find("#ltOptimizedCount").val();
    var gtOptimizePlanCount = searchCustomerKeywordForm.find("#gtOptimizePlanCount").val();
    var ltOptimizePlanCount = searchCustomerKeywordForm.find("#ltOptimizePlanCount").val();
    var gtPosition = searchCustomerKeywordForm.find("#gtPosition").val();
    var ltPosition = searchCustomerKeywordForm.find("#ltPosition").val();
    var invalidRefreshCount = searchCustomerKeywordForm.find("#invalidRefreshCount").val();
    var gtCurrentIndexCount = searchCustomerKeywordForm.find("#gtCurrentIndexCount").val();
    var ltCurrentIndexCount = searchCustomerKeywordForm.find("#ltCurrentIndexCount").val();
    var remarks = searchCustomerKeywordForm.find("#remarks").val();
    if(keyword != "") {
        searchCustomerKeywordForm.find("#keyword").val($.trim(keyword));
    }
    if(url != "") {
        searchCustomerKeywordForm.find("#url").val($.trim(url));
    }
    if(optimizeGroupName != "") {
        searchCustomerKeywordForm.find("#optimizeGroupName").val($.trim(optimizeGroupName));
    }
    if(gtOptimizedCount != "") {
        searchCustomerKeywordForm.find("#gtOptimizedCount").val($.trim(gtOptimizedCount));
    }
    if(ltOptimizedCount != "") {
        searchCustomerKeywordForm.find("#ltOptimizedCount").val($.trim(ltOptimizedCount));
    }
    if(gtOptimizePlanCount != "") {
        searchCustomerKeywordForm.find("#gtOptimizePlanCount").val($.trim(gtOptimizePlanCount));
    }
    if(ltOptimizePlanCount != "") {
        searchCustomerKeywordForm.find("#ltOptimizePlanCount").val($.trim(ltOptimizePlanCount));
    }
    if(gtPosition != "") {
        searchCustomerKeywordForm.find("#gtPosition").val($.trim(gtPosition));
    }
    if(ltPosition != "") {
        searchCustomerKeywordForm.find("#ltPosition").val($.trim(ltPosition));
    }
    if(invalidRefreshCount != "") {
        searchCustomerKeywordForm.find("#invalidRefreshCount").val($.trim(invalidRefreshCount));
    }
    if(gtCurrentIndexCount != "") {
        searchCustomerKeywordForm.find("#gtCurrentIndexCount").val($.trim(gtCurrentIndexCount));
    }
    if(ltCurrentIndexCount != "") {
        searchCustomerKeywordForm.find("#ltCurrentIndexCount").val($.trim(ltCurrentIndexCount));
    }
    if(remarks != "") {
        searchCustomerKeywordForm.find("#remarks").val($.trim(remarks));
    }
    searchCustomerKeywordForm.find("#currentPageNumberHidden").val(1);
}
function showGroupNameChangeByRankDialog(customerUuid) {
    $('#groupNameChangeByRankFrom')[0].reset();
    $("#groupChangeNameByRankDialog").dialog({
        resizable: false,
        width: 240,
        height: 170,
        closed: true,
        modal: true,
        title: "通过排名修改分组",
        position:{
            my:"center top",
            at:"center top+150",
            of:window
        },
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var targetGroupName = $("#groupNameChangeByRankFrom").find("#groupName").val();
                var position = $("#groupNameChangeByRankFrom").find("#position").val();
                var day = $("#groupNameChangeByRankFrom").find("#day").val();
                if (targetGroupName == null || targetGroupName === '') {
                    alert("请输入分组名");
                    return;
                }
                if (position == null || position === '') {
                    alert("请输入排名");
                    return;
                }
                if (day == null || day === '') {
                    alert("请输入天数");
                    return;
                }
                $.ajax({
                    url:'/internal/customerKeyword/updateCustomerKeywordGroupNameByRank',
                    data:JSON.stringify({"customerUuid":customerUuid,"targetGroupName":targetGroupName,"position":position,"day":day}),
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
                $("#groupChangeNameByRankDialog").dialog("close");
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#groupNameChangeByRankFrom')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#groupChangeNameByRankDialog").dialog("close");
                    $('#groupNameChangeByRankFrom')[0].reset();
                }
            }]
    });
    $("#groupChangeNameByRankDialog").dialog("open");
    $('#groupChangeNameByRankDialog').window("resize",{top:$(document).scrollTop() + 200});
}
function showGroupNameChangeDialog(changeGroupCriteria) {
    $("#groupChangeNameDialog").dialog({
        resizable: false,
        width: 260,
        height: 100,
        closed: true,
        modal: true,
        title: changeGroupCriteria.title,
        position:{
            my:"center top",
            at:"center top+150",
            of:window
        },
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var targetGroupName = $("#groupNameChangeFrom").find("#groupName").val();
                if (targetGroupName == null || targetGroupName === '') {
                    alert("请输入分组名");
                    return;
                }
                changeGroupCriteria.targetGroupName = targetGroupName;
                changeGroupName(changeGroupCriteria);
                $("#groupChangeNameDialog").dialog("close");
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#groupNameChangeFrom')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#groupChangeNameDialog").dialog("close");
                    $('#groupNameChangeFrom')[0].reset();
                }
            }]
    });
    $("#groupChangeNameDialog").dialog("open");
    $('#groupChangeNameDialog').window("resize",{top:$(document).scrollTop() + 200});
}
function showSearchEngineChangeDialog(searchEngineCriteria) {
    $("#changeSearchEngineDialog").dialog({
        resizable: false,
        width: 220,
        height: 100,
        closed: true,
        modal: true,
        title: searchEngineCriteria.title,
        position:{
            my:"center top",
            at:"center top+150",
            of:window
        },
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var targetSearchEngine = $("#changeSearchEngineForm").find("#searchEngineSelect").val();
                searchEngineCriteria.targetSearchEngine = targetSearchEngine;
                changeSearchEngine(searchEngineCriteria);
                $("#changeSearchEngineDialog").dialog("close");
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#changeSearchEngineDialog").dialog("close");
                }
            }]
    });
    $("#changeSearchEngineDialog").dialog("open");
    $('#changeSearchEngineDialog').window("resize",{top:$(document).scrollTop() + 200});
}
function updateCustomerKeywordStatus(status) {
    var customerKeyword = {};
    var customerKeywordUuids = getUuids();
    if (customerKeywordUuids.trim() === '') {
        alert("请选中要操作的关键词！");
        return;
    }

    if(status == 0) {
        if (confirm("确认要暂停选中的关键字吗?") == false) return;
    } else {
        if (confirm("确认要上线选中的关键字吗?") == false) return;
    }
    customerKeyword.uuids = customerKeywordUuids.split(",");
    customerKeyword.status = status;
    $.ajax({
        url: '/internal/customerKeyword/updateCustomerKeywordStatus',
        data: JSON.stringify(customerKeyword),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (status) {
            if (status) {
                $().toastmessage('showSuccessToast', "操作成功");
                window.location.reload();
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
//下架
function stopOptimization(customerUuid){
    changeGroupName({"customerUuid": customerUuid, "targetGroupName": "stop"});
}
function updateSpecifiedCustomerKeywordGroupName() {
    var customerKeywordUuids = getUuids();
    if (customerKeywordUuids.trim() === '') {
        alert("请选中要操作的关键词！");
        return;
    }
    var changeGroupCriteria = {"title" : "修改选中关键字分组", "customerKeywordUuids":customerKeywordUuids.split(",")};
    showGroupNameChangeDialog(changeGroupCriteria);
}
function changeGroupName(customerKeywordUpdateGroupCriteria) {
    $.ajax({
        url:'/internal/customerKeyword/updateCustomerKeywordGroupName',
        data:JSON.stringify(customerKeywordUpdateGroupCriteria),
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
                $().toastmessage('showErrorToast', "操作失败",true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败",true);
        }
    });
}
function updateSpecifiedCustomerKeywordSearchEngine() {
    var customerKeywordUuids = getUuids();
    if (customerKeywordUuids.trim() === '') {
        alert("请选中要操作的关键词！");
        return;
    }
    var changeSearchEngineCriteria = {"title" : "修改选中关键字搜索引擎", "customerKeywordUuids":customerKeywordUuids.split(",")};
    showSearchEngineChangeDialog(changeSearchEngineCriteria);
}
function changeSearchEngine(searchEngineCriteria) {
    $.ajax({
        url:'/internal/customerKeyword/updateCustomerKeywordSearchEngine',
        data:JSON.stringify(searchEngineCriteria),
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
                $().toastmessage('showErrorToast', "操作失败",true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败",true);
        }
    });
}
function addCustomerKeyword(customerKeywordUuid, customerUuid) {
    if (customerKeywordUuid == null) {
        $("#customerKeywordForm")[0].reset();
        $("#customerKeywordForm").find("#uuid").val('');
        $("#customerKeywordForm").find("#status").val('');
    }
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
                saveCustomerKeyword(customerUuid);
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
function saveCustomerKeyword(customerUuid) {
    var customerKeyword = {};
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
    var recommendKeywords = $.trim(saveCustomerKeywordDialog.find("#recommendKeywords").val());
    customerKeyword.recommendKeywords = recommendKeywords;
    var negativeKeywords = $.trim(saveCustomerKeywordDialog.find("#negativeKeywords").val());
    customerKeyword.negativeKeywords = negativeKeywords;
    var excludeKeywords = $.trim(saveCustomerKeywordDialog.find("#excludeKeywords").val());
    customerKeyword.excludeKeywords = excludeKeywords;
    var url = $.trim(saveCustomerKeywordDialog.find("#url").val())
    customerKeyword.url = url;
    var originalUrl = $.trim(saveCustomerKeywordDialog.find("#originalUrl").val());
    customerKeyword.originalUrl = originalUrl;
    var optimizePlanCount = $.trim(saveCustomerKeywordDialog.find("#optimizePlanCount").val());
    customerKeyword.optimizePlanCount = optimizePlanCount;
    var regNumber = /^\d+$/;
    var positionFirstFee = $.trim(saveCustomerKeywordDialog.find("#positionFirstFee").val());
    customerKeyword.positionFirstFee = positionFirstFee;
    var positionSecondFee = $.trim(saveCustomerKeywordDialog.find("#positionSecondFee").val());
    customerKeyword.positionSecondFee = positionSecondFee;
    var positionThirdFee = $.trim(saveCustomerKeywordDialog.find("#positionThirdFee").val());
    customerKeyword.positionThirdFee = positionThirdFee;
    var positionForthFee = $.trim(saveCustomerKeywordDialog.find("#positionForthFee").val());
    customerKeyword.positionForthFee = positionForthFee;
    var positionFifthFee = $.trim(saveCustomerKeywordDialog.find("#positionFifthFee").val());
    customerKeyword.positionFifthFee = positionFifthFee;
    var positionFirstPageFee = $.trim(saveCustomerKeywordDialog.find("#positionFirstPageFee").val());
    customerKeyword.positionFirstPageFee = positionFirstPageFee;
    var positionFirstCost = $.trim(saveCustomerKeywordDialog.find("#positionFirstCost").val());
    customerKeyword.positionFirstCost = positionFirstCost;
    var positionSecondCost = $.trim(saveCustomerKeywordDialog.find("#positionSecondCost").val());
    customerKeyword.positionSecondCost = positionSecondCost;
    var positionThirdCost = $.trim(saveCustomerKeywordDialog.find("#positionThirdCost").val());
    customerKeyword.positionThirdCost = positionThirdCost;
    var positionForthCost = $.trim(saveCustomerKeywordDialog.find("#positionForthCost").val());
    customerKeyword.positionForthCost = positionForthCost;
    var positionFifthCost = $.trim(saveCustomerKeywordDialog.find("#positionFifthCost").val());
    customerKeyword.positionFifthCost = positionFifthCost;
    var initialPosition = $.trim(saveCustomerKeywordDialog.find("#initialPosition").val());
    customerKeyword.initialPosition = initialPosition;
    customerKeyword.currentPosition = initialPosition;
    var initialIndexCount = $.trim(saveCustomerKeywordDialog.find("#initialIndexCount").val());
    customerKeyword.initialIndexCount = initialIndexCount;
    customerKeyword.currentIndexCount = initialIndexCount;
    var sequence = $.trim(saveCustomerKeywordDialog.find("#sequence").val());
    customerKeyword.sequence = sequence;
    var title = $.trim(saveCustomerKeywordDialog.find("#title").val());
    customerKeyword.title = title;
    var optimizeGroupName = $.trim(saveCustomerKeywordDialog.find("#optimizeGroupName").val());
    customerKeyword.optimizeGroupName = optimizeGroupName;
    var collectMethod = $.trim(saveCustomerKeywordDialog.find("#collectMethod").val());
    customerKeyword.collectMethod = collectMethod;
    var serviceProvider = $.trim(saveCustomerKeywordDialog.find("#serviceProvider").val());
    customerKeyword.serviceProvider = serviceProvider;
    var orderNumber = $.trim(saveCustomerKeywordDialog.find("#orderNumber").val());
    customerKeyword.orderNumber = orderNumber;
    var paymentStatus = $.trim(saveCustomerKeywordDialog.find("#paymentStatus").val());
    customerKeyword.paymentStatus = paymentStatus;

    var operateSelectKeyword = $("#operateSelectKeyword")[0].checked;
    customerKeyword.operateSelectKeyword = operateSelectKeyword;
    var operateRelatedKeyword = $("#operateRelatedKeyword")[0].checked;
    customerKeyword.operateRelatedKeyword = operateRelatedKeyword;
    var operateRecommendKeyword = $("#operateRecommendKeyword")[0].checked;
    customerKeyword.operateRecommendKeyword = operateRecommendKeyword;
    var operateSearchAfterSelectKeyword = $("#operateSearchAfterSelectKeyword")[0].checked;
    customerKeyword.operateSearchAfterSelectKeyword = operateSearchAfterSelectKeyword;

    var clickUrl = $("input[name='clickUrl']:checked").val();
    customerKeyword.clickUrl = clickUrl;
    var showPage = $.trim(saveCustomerKeywordDialog.find("#showPage").val());
    customerKeyword.showPage = showPage;
    var relatedKeywordPercentage = $.trim(saveCustomerKeywordDialog.find("#relatedKeywordPercentage").val());
    customerKeyword.relatedKeywordPercentage = relatedKeywordPercentage;

    var remarks = $.trim(saveCustomerKeywordDialog.find("#remarks").val());
    customerKeyword.remarks = remarks;
    customerKeyword.manualCleanTitle = true;
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
                saveCustomerKeywordDialog.find("#keyword").val(customerKeyword.keyword);
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
                saveCustomerKeywordDialog.find("#sequence").val(customerKeyword.sequence);
                saveCustomerKeywordDialog.find("#title").val(customerKeyword.title);
                saveCustomerKeywordDialog.find("#optimizeGroupName").val(customerKeyword.optimizeGroupName);
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
                addCustomerKeyword(customerKeywordUuid, customerUuid);
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
//关键字Excel上传(简化版)
function uploadCustomerKeywords(customerUuid, excelType){
    $('#uploadExcelForm')[0].reset();
    if(excelType=='SuperUserSimple'){
        $('#uploadExcelForm').find("#excelType").html("(简易版)");
    }else{
        $('#uploadExcelForm').find("#excelType").html("(完整版)");
    }
    $("#uploadExcelDailog").dialog({
        resizable: false,
        width: 260,
        height: 180,
        modal: true,
        closed: true,
        buttons: [{
            text: '上传',
            iconCls: 'icon-ok',
            handler: function () {
                var uploadForm = $("#uploadExcelForm");
                var uploadFile = uploadForm.find("#uploadExcelFile").val();
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
                formData.append('file', uploadForm.find('#uploadExcelFile')[0].files[0]);
                formData.append('customerUuid', customerUuid);
                formData.append('excelType', excelType);
                if (fileTypeFlag) {
                    $.ajax({
                        url: '/internal/customerKeyword/uploadCustomerKeywords',
                        type: 'POST',
                        cache: false,
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (result) {
                            if (result) {
                                $().toastmessage('showSuccessToast', "上传成功",true);
                                /*window.location.reload();*/
                            } else {
                                $().toastmessage('showErrorToast', "上传失败");
                            }
                        },
                        error: function () {
                            $().toastmessage('showErrorToast', "上传失败");
                        }
                    });
                }
                $("#uploadExcelDailog").dialog("close");
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#uploadExcelDailog").dialog("close");
                    $('#uploadExcelForm')[0].reset();
                }
            }]
    });
    $("#uploadExcelDailog").dialog("open");
    $('#uploadExcelDailog').window("resize",{top:$(document).scrollTop() + 100});
}
//导出结果
function downloadCustomerKeywordInfo() {
    var customerKeywordCrilteriaArray = $("#searchCustomerKeywordForm").serializeArray();
    var downloadCustomerKeywordInfoForm = $("#downloadCustomerKeywordInfoForm");
    $.each(customerKeywordCrilteriaArray, function(idx, val){
        downloadCustomerKeywordInfoForm.find("#"+val.name+"Hidden").val(val.value);
    });
    downloadCustomerKeywordInfoForm.submit();
}
//显示下架
function displayStopValue() {
    if($("#displayStop").is(":checked")){
        $("#displayStop").val("1")
    }else {
        $("#displayStop").val("");
    }
}
function alignTableHeader() {
    var td = $("#customerKeywordTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function onlyNumber(self) {
    $(self).val($(self).val().replace(/[^\d]*/g, ''));
}
function showOptimizePlanCountDialog() {
    var uuids = getUuids();
    if (uuids === '') {
        alert('请选择要修改刷量的关键字');
        return;
    }
    $("#optimizePlanCountDialog").dialog({
        resizable: false,
        title: "修改刷量",
        height:120,
        width: 225,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                editOptimizePlanCount(uuids);
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#optimizePlanCountDialog").dialog("close");
                }
            }]
    });
    $('#optimizePlanCountDialog').window("resize",{top:$(document).scrollTop() + 150});
}
function editOptimizePlanCount(uuids) {
    var settingType = $("#optimizePlanCountDialog").find("input[name=settingType]:checked").val();
    var optimizePlanCount = $("#optimizePlanCountDialog").find("#optimizePlanCount").val();
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.settingType = settingType;
    postData.optimizePlanCount = $.trim(optimizePlanCount);
    $.ajax({
        url: '/internal/customerKeyword/editOptimizePlanCount',
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

    function updateKeywordCustomerUuid() {
        var customerKeywordUuids = getUuids();
        if (customerKeywordUuids === '') {
            alert('请选择要操作的信息');
            return;
        }
        $("#customerList").show();
        var keywordUuids = customerKeywordUuids.split(",");
        $("#customerList").dialog({
            resizable: false,
            width: 450,
            height: 100,
            modal: true,
            title: '更新关键字所属客户',
            closed: true,
            buttons: [{
                    text: '确定',
                    position: '25%',
                    handler: function () {
                        updateKeywordCustomerUuidRequest(keywordUuids);
                    }
                }]
        });
        $("#customerList").dialog("open");
        $('#customerList').window("resize", {top: $(document).scrollTop() + 150});
    }

    function updateKeywordCustomerUuidRequest(keywordUuids) {
        var condition = {};
        var customerItem = $("#customerItem").val();
        var customer_list = $("#customer_list").find("option");
        if(customerItem == ''||customerItem == null){
            alert('没有选择移动目标客户');
            return;
        }
        var isFull=false;
        customer_list.each(function (){
            if (this.innerHTML == customerItem){
                isFull=true;
            }
        });
        if(isFull) {
            var customerUuid = customerItem.split("_____")[1];
            condition.customerUuid = customerUuid;
            condition.keywordUuids = keywordUuids;
            $.ajax({
                url: '/internal/customerKeyword/updateKeywordCustomerUuid',
                data: JSON.stringify(condition),
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
        }else {
            alert('数据不完整重新选择！');
            return;
        }
    }







