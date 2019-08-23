$(function () {
    $("#groupChangeNameDialog").dialog("close");
    $("#groupChangeNameByRankDialog").dialog("close");
    $("#changeSearchEngineDialog").dialog("close");
    $("#uploadExcelDailog").dialog("close");
    $("#saveCustomerKeywordDialog").dialog("close");
    $("#optimizePlanCountDialog").dialog("close");
    $('#customerList').dialog("close");
    $("#showUserMessageDialog").dialog("close");
});

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

function keywordAndURLExport() {
    var customerKeywordCrilteriaArray = $("#searchCustomerKeywordForm").serializeArray();
    var customerKeywordAndUrlCsvExportFrom = $("#customerKeywordAndUrlCsvExportFrom");
    $.each(customerKeywordCrilteriaArray, function (idx, val) {
        if (val.name == "customerUuid") {
            customerKeywordAndUrlCsvExportFrom.find("#" + val.name + "Temp").val(val.value == '' ? null : val.value);
        }
    });
    customerKeywordAndUrlCsvExportFrom.submit();
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
    var excludeUrl = searchCustomerKeywordForm.find("#excludeUrl").val();
    var bearPawNumber = searchCustomerKeywordForm.find("#bearPawNumber").val();
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
    if(excludeUrl != "") {
        searchCustomerKeywordForm.find("#excludeUrl").val($.trim(excludeUrl));
    }
    if(bearPawNumber != "") {
        searchCustomerKeywordForm.find("#bearPawNumber").val($.trim(bearPawNumber));
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
    $("#groupChangeNameByRankDialog").show();
    $("#groupChangeNameByRankDialog").dialog({
        resizable: false,
        width: 240,
        height: 170,
        closed: true,
        modal: true,
        title: "通过排名修改优化组",
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
                    alert("请输入优化组名");
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
    $("#groupChangeNameDialog").show();
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
                    alert("请输入优化组名");
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
    $("#changeSearchEngineDialog").show();
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
    } else if(status == 1){
        if (confirm("确认要上线选中的关键字吗?") == false) return;
    }else{
        if (confirm("确认要下架选中的关键字吗?") == false) return;
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
//暂停所有关键字
function stopOptimization(customerUuid,status){
    $.messager.confirm('提示','确定暂停该用户的所有关键字吗?',function(r){
        if(r){
            $.ajax({
                url:'/internal/customerKeyword/updateCustomerKeywordStatusByCustomerUuid',
                data:JSON.stringify({"customerUuid": customerUuid, "status": status}),
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
    })
}

function updateSpecifiedCustomerKeywordGroupName() {
    var customerKeywordUuids = getUuids();
    if (customerKeywordUuids.trim() === '') {
        alert("请选中要操作的关键词！");
        return;
    }
    var changeGroupCriteria = {"title" : "修改选中关键字优化组", "customerKeywordUuids":customerKeywordUuids.split(",")};
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

//关键字Excel上传(简化版)
function uploadCustomerKeywords(customerUuid, excelType){
    $('#uploadExcelForm')[0].reset();
    if(excelType=='SuperUserSimple'){
        $('#uploadExcelForm').find("#excelType").html("(简易版)");
    }else{
        $('#uploadExcelForm').find("#excelType").html("(完整版)");
    }
    $("#uploadExcelDailog").show();
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
        downloadCustomerKeywordInfoForm.find("#"+val.name+"Hidden").val(val.value == '' ? null : val.value);
    });
    downloadCustomerKeywordInfoForm.submit();
}

function showOptimizePlanCountDialog() {
    var customerUuid = null;
    var uuids = null;

    $("#optimizePlanCountDialog").show();
    $("#optimizePlanCountDialog").dialog({
        resizable: false,
        title: "修改刷量",
        height:145,
        width: 225,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var range = $("#optimizePlanCountDialog").find("input[name=range]:checked").val();
                if(range == "all") {
                    customerUuid = $("#searchCustomerKeywordTable").find("#customerUuid").val();
                } else {
                    uuids = getUuids();
                    if (uuids === '') {
                        alert('请选择要修改刷量的关键字');
                        return;
                    }
                }
                editOptimizePlanCount(customerUuid, uuids);
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

function editOptimizePlanCount(customerUuid, uuids) {
    var settingType = $("#optimizePlanCountDialog").find("input[name=settingType]:checked").val();
    var optimizePlanCount = $("#optimizePlanCountDialog").find("#optimizePlanCount").val();
    if(optimizePlanCount == "") {
        alert("请输入刷量");
        return;
    }
    var postData = {};
    postData.customerUuid = customerUuid;
    postData.uuids = uuids == null ? uuids : uuids.split(",");
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

function deleteDuplicateCustomerKeyword(customerUuid) {
    $.ajax({
        url: '/internal/customerKeyword/deleteDuplicateCustomerKeyword/' + customerUuid,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            console.log(result);
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功", true);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}

/**
 * 批量修改关键字机器分组
 * @param changeType
 */
function updateMachineGroupName(changeType) {
    $("#targetMachineGroupDialog").css("display", "block");
    $("#targetMachineGroupDialog").dialog({
        resizable: false,
        width: 260,
        height: 100,
        title:"修改关键字机器分组",
        closed: true,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var targetMachineGroup = $("#targetMachineGroupForm").find("#machineGroup").val();
                if (targetMachineGroup == null || targetMachineGroup == '') {
                    alert("请输入目标机器分组!");
                    return;
                }
                var obj = {};
                if ("selected" === changeType){
                    var uuids = getUuids();
                    if(uuids === ''){
                        alert('请选择要修改机器分组的关键字！');
                        return ;
                    }
                    if (confirm("确定要修改选中关键字的机器分组吗?") == false) return;
                    obj['uuids'] = uuids.split(",");
                }else{
                    if (confirm("确定要修改当前查询条件下所有关键字的机器分组吗?") == false) return;
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
                obj.targetMachineGroup = targetMachineGroup;
                $.ajax({
                    url: '/internal/customerKeyword/updateCustomerKeywordMachineGroup',
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
                $("#targetMachineGroupDialog").dialog("close");
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#targetMachineGroupDialog").dialog("close");
                    $('#targetMachineGroupForm')[0].reset();
                }
            }]
    });
    $("#targetMachineGroupDialog").dialog("open");
    $('#targetMachineGroupDialog').window("resize",{top:$(document).scrollTop() + 200});
}









