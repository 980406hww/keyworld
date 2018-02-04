function changePaging(currentPage, pageSize) {
    var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
    searchCustomerKeywordForm.find("#currentPageNumberHidden").val(currentPage);
    searchCustomerKeywordForm.find("#pageSizeHidden").val(pageSize);
    searchCustomerKeywordForm.submit();
}
function resetPageNumber() {
    var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
    var keyword = searchCustomerKeywordForm.find("#keyword").val();
    var qq = searchCustomerKeywordForm.find("#qq").val();
    var url = searchCustomerKeywordForm.find("#url").val();
    var optimizeGroupName = searchCustomerKeywordForm.find("#optimizeGroupName").val();
    var optimizedCount = searchCustomerKeywordForm.find("#optimizedCount").val();
    var position = searchCustomerKeywordForm.find("#position").val();
    var orderNumber = searchCustomerKeywordForm.find("#orderNumber").val();
    var invalidRefreshCount = searchCustomerKeywordForm.find("#invalidRefreshCount").val();
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
    if(optimizeGroupName != "") {
        searchCustomerKeywordForm.find("#optimizeGroupName").val($.trim(optimizeGroupName));
    }
    if(optimizedCount != "") {
        searchCustomerKeywordForm.find("#optimizedCount").val($.trim(optimizedCount));
    }
    if(position != "") {
        searchCustomerKeywordForm.find("#position").val($.trim(position));
    }
    if(orderNumber != "") {
        searchCustomerKeywordForm.find("#orderNumber").val($.trim(orderNumber));
    }
    if(invalidRefreshCount != "") {
        searchCustomerKeywordForm.find("#invalidRefreshCount").val($.trim(invalidRefreshCount));
    }
    if(remarks != "") {
        searchCustomerKeywordForm.find("#remarks").val($.trim(remarks));
    }
    searchCustomerKeywordForm.find("#currentPageNumberHidden").val(1);
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