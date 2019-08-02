function changePaging(currentPage, pageSize) {
    var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
    searchCustomerKeywordForm.find("#currentPageNumberHidden").val(currentPage);
    searchCustomerKeywordForm.find("#pageSizeHidden").val(pageSize);
    searchCustomerKeywordForm.submit();
}
function resetPageNumber(days) {
    var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
    var keyword = searchCustomerKeywordForm.find("#keyword").val();
    var qq = searchCustomerKeywordForm.find("#qq").val();
    var url = searchCustomerKeywordForm.find("#url").val();
    var bearPawNumber = searchCustomerKeywordForm.find("#bearPawNumber").val();
    var optimizeGroupName = searchCustomerKeywordForm.find("#optimizeGroupName").val();
    var machineGroup = searchCustomerKeywordForm.find("#machineGroup").val();
    var gtOptimizedCount = searchCustomerKeywordForm.find("#gtOptimizedCount").val();
    var ltOptimizedCount = searchCustomerKeywordForm.find("#ltOptimizedCount").val();
    var gtOptimizePlanCount = searchCustomerKeywordForm.find("#gtOptimizePlanCount").val();
    var ltOptimizePlanCount = searchCustomerKeywordForm.find("#ltOptimizePlanCount").val();
    var gtPosition = searchCustomerKeywordForm.find("#gtPosition").val();
    var ltPosition = searchCustomerKeywordForm.find("#ltPosition").val();
    var orderNumber = searchCustomerKeywordForm.find("#orderNumber").val();
    var invalidRefreshCount = searchCustomerKeywordForm.find("#invalidRefreshCount").val();
    var noReachStandardDays = searchCustomerKeywordForm.find("#noReachStandardDays").val();
    var remarks = searchCustomerKeywordForm.find("#remarks").val();
    if(keyword != "") {
        searchCustomerKeywordForm.find("#keyword").val($.trim(keyword));
    }
    if(qq != "") {
        searchCustomerKeywordForm.find("#qq").val($.trim(qq));
    }
    if(url != "") {
        searchCustomerKeywordForm.find("#url").val($.trim(url));
    }
    if(bearPawNumber != "") {
        searchCustomerKeywordForm.find("#bearPawNumber").val($.trim(bearPawNumber));
    }
    if(optimizeGroupName != "") {
        searchCustomerKeywordForm.find("#optimizeGroupName").val($.trim(optimizeGroupName));
    }
    if(machineGroup != "") {
        searchCustomerKeywordForm.find("#machineGroup").val($.trim(machineGroup));
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
    if(orderNumber != "") {
        searchCustomerKeywordForm.find("#orderNumber").val($.trim(orderNumber));
    }
    if(invalidRefreshCount != "") {
        searchCustomerKeywordForm.find("#invalidRefreshCount").val($.trim(invalidRefreshCount));
    }
    if(days > 0) {
        searchCustomerKeywordForm.find("#noReachStandardDays").val(days);
    } else if(noReachStandardDays != "") {
        searchCustomerKeywordForm.find("#noReachStandardDays").val($.trim(noReachStandardDays));
    } else {
        searchCustomerKeywordForm.find("#noReachStandardDays").val("");
    }
    if(remarks != "") {
        searchCustomerKeywordForm.find("#remarks").val($.trim(remarks));
    }

    searchCustomerKeywordForm.find("#currentPageNumberHidden").val(1);
    if(days != 0) {
        searchCustomerKeywordForm.submit();
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
function deleteCustomerKeywords() {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        alert('请选择要删除的关键字！');
        return ;
    }
    if (confirm("确实要删除这些关键字吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.deleteType = "ByUuid";
    $.ajax({
        url: '/internal/customerKeyword/deleteCustomerKeywords',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if(data){
                $().toastmessage('showSuccessToast',"操作成功！", true);
            }else{
                $().toastmessage('showErrorToast', "操作失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败！");
        }
    });
}
function updateCustomerKeywordStatus(status) {
    var customerKeyword = {};
    var customerKeywordUuids = getSelectedIDs();
    if (customerKeywordUuids === '') {
        alert('请选择要操作的关键字');
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
function updateOptimizeGroupName(changeType) {
    $("#targetGroupNameDialog").css("display", "block");
    $("#targetGroupNameDialog").dialog({
        resizable: false,
        width: 260,
        height: 100,
        title:"修改关键字优化组名",
        closed: true,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var targetGroupName = $("#targetGroupNameFrom").find("#groupName").val();
                if (targetGroupName == null || targetGroupName == '') {
                    alert("请输入目标优化组名!");
                    return;
                }
                var obj = {};
                if ("selected" === changeType){
                    var uuids = getSelectedIDs();
                    if(uuids === ''){
                        alert('请选择要修改优化组的关键字！');
                        return ;
                    }
                    if (confirm("确定要修改选中关键字的优化组名吗?") == false) return;
                    obj['uuids'] = uuids.split(",");
                }else{
                    if (confirm("确定要修改当前查询条件下所有关键字的优化组名吗?") == false) return;
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
                obj.targetOptimizeGroupName = targetGroupName;
                $.ajax({
                    url: '/internal/customerKeyword/updateOptimizeGroupName',
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
                $("#targetGroupNameDialog").dialog("close");
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#targetGroupNameDialog").dialog("close");
                    $('#groupNameChangeFrom')[0].reset();
            }
        }]
    });
    $("#targetGroupNameDialog").dialog("open");
    $('#targetGroupNameDialog').window("resize",{top:$(document).scrollTop() + 200});
}
function searchCustomerKeywordForNoReachStandard(entryType, terminalType) {
    var postData = {};
    postData.entryType = entryType;
    postData.terminalType = terminalType;
    $.ajax({
        url: '/internal/customerKeyword/searchCustomerKeywordForNoReachStandard',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            var noReachStandardDiv = $("#noReachStandardDiv");
            noReachStandardDiv.find("a").eq(0).text("超过30天(" + data.thirtyDaysNoReachStandard + ")");
            noReachStandardDiv.find("a").eq(1).text("超过15天(" + data.fifteenDaysNoReachStandard + ")");
            noReachStandardDiv.find("a").eq(2).text("超过7天(" + data.sevenDaysNoReachStandard + ")");
            var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
            searchCustomerKeywordForm.find("#thirtyDaysNoReachStandard").val(data.thirtyDaysNoReachStandard);
            searchCustomerKeywordForm.find("#fifteenDaysNoReachStandard").val(data.fifteenDaysNoReachStandard);
            searchCustomerKeywordForm.find("#sevenDaysNoReachStandard").val(data.sevenDaysNoReachStandard);
        },
        error: function () {
            $().toastmessage('showErrorToast', "未达标统计失败");
        }
    });
}


function deleteDuplicateQZKeyword() {
    $.ajax({
        url: '/internal/customerKeyword/deleteDuplicateQZKeyword',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
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
                    var uuids = getSelectedIDs();
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