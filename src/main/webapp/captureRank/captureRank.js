$(function () {
    $("#crawlRankingDialog").dialog("close");
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
})
function changePaging(currentPage, pageSize) {
    var searchCaptureRankJobForm = $("#searchCaptureRankJobForm");
    searchCaptureRankJobForm.find("#currentPageNumberHidden").val(currentPage);
    searchCaptureRankJobForm.find("#pageSizeHidden").val(pageSize);
    searchCaptureRankJobForm.submit();
}
function pageLoad() {
    var searchCustomerForm = $("#searchCaptureRankJobForm");
    var pageSize = searchCustomerForm.find('#pageSizeHidden').val();
    var pages = searchCustomerForm.find('#pagesHidden').val();
    var currentPageNumber = searchCustomerForm.find('#currentPageNumberHidden').val();
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
function initcustomerUuid() {
    var postData = {};
    postData.groupNames = $('#crawlRankingForm #groupNames').val().split(",");
    $.ajax({
        url: '/internal/customer/searchCustomersWithKeyword',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                $('#crawlRankingForm #customerUuid').combogrid({
                    panelWidth: 460,
                    value: '',
                    idField: 'uuid',
                    textField: 'contactPerson',
                    dataType: 'json',
                    data: data,
                    columns: [[
                        {field: 'contactPerson', title: '联系人', width: 150},
                        {field: 'telphone', title: '电话', width: 100},
                        {field: 'qq', title: 'QQ', width: 100},
                        {field: 'email', title: 'E-mail', width: 100}
                    ]]
                });
            } else {
                $().toastmessage('showErrorToast', "获取失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取失败");
        }
    });

}
function initGroupNames(data) {
    $('#crawlRankingForm #groupNames').combobox({
        url: '/internal/customerKeyword/searchGroups', //后台获取下拉框数据的url
        method: 'post',
        //panelHeight:300,//设置为固定高度，combobox出现竖直滚动条
        valueField: 'name',
        textField: 'name',
        multiple: true,
        dataType: 'json',
        editable: true,
        formatter: function (row) { //formatter方法就是实现了在每个下拉选项前面增加checkbox框的方法
            var opts = $(this).combobox('options');
            return '<input type="checkbox" class="combobox-checkbox">' + row[opts.textField]
        },
        onLoadSuccess: function () {  //下拉框数据加载成功调用
            if (data != null && data != '') {
                var opts = $(this).combobox('options');
                var target = this;
                var values = data.groupNames.split(",");
                $.map(values, function (value) {
                    var el = opts.finder.getEl(target, value);
                    el.find('input.combobox-checkbox')._propAttr('checked', true);
                    el.click();
                })
                showCrawlRankingForm(data.uuid);
                initcustomerUuid();
                if (data.customerUuid != null && data.customerUuid != '') {
                    $('#crawlRankingForm #customerUuid').combogrid("setValue", data.customerUuid);
                }
                $("#crawlRankingForm input[name=exectionType][value=" + data.exectionType + "]").attr("checked", true);
                $("#crawlRankingForm #exectionTime").val(data.exectionTime);
                $('#crawlRankingForm #rowNumber').spinner('setValue', data.rowNumber);
                $('#crawlRankingForm #captureInterval').spinner('setValue', data.captureInterval);
                $('#crawlRankingForm #executionCycle').spinner('setValue', data.executionCycle);
                $('#crawlRankingForm #pageSize').spinner('setValue', data.pageSize);
            }
        },
        onSelect: function (row) { //选中一个选项时调用
            var opts = $(this).combobox('options');
            //获取选中的值的values
            $('#crawlRankingForm #groupNames').val($(this).combobox('getValues'));

            //设置选中值所对应的复选框为选中状态
            var el = opts.finder.getEl(this, row[opts.valueField]);
            el.find('input.combobox-checkbox')._propAttr('checked', true);

            initcustomerUuid();
        },
        onUnselect: function (row) {//不选中一个选项时调用
            var opts = $(this).combobox('options');
            //获取选中的值的values
            $('#crawlRankingForm #groupNames').val($(this).combobox('getValues'));

            var el = opts.finder.getEl(this, row[opts.valueField]);
            el.find('input.combobox-checkbox')._propAttr('checked', false);

            initcustomerUuid();
        }
    });
}
function showCrawlRankingForm(uuid) {
    $("#crawlRankingDialog").dialog({
        resizable: false,
        title: "设置抓排名任务",
        width: 320,
        fitColumns: true,//自动大小
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveCaptureRankJob(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#crawlRankingForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#crawlRankingDialog").dialog("close");
                    $('#crawlRankingForm')[0].reset();
                }
            }]
    });
    if (uuid == null || uuid == '') {
        $('#crawlRankingForm')[0].reset();
        initGroupNames();
        initcustomerUuid();
        $('#crawlRankingForm #rowNumber').spinner('setValue', 100);
        $('#crawlRankingForm #captureInterval').spinner('setValue', 500);
        $('#crawlRankingForm #executionCycle').spinner('setValue', 0);
        if ("${sessionScope.terminalType}" == 'PC') {
            $('#crawlRankingForm #pageSize').spinner('setValue', 10);
        }
        else {
            $('#crawlRankingForm #pageSize').spinner('setValue', 50);
        }
    }
    $('#crawlRankingDialog').window("resize", {top: $(document).scrollTop() + 100});
}
function saveCaptureRankJob(uuid) {
    var CaptureRankJob = {};
    if (uuid != null) {
        CaptureRankJob.uuid = uuid;
    }

    CaptureRankJob.customerUuid = $('#crawlRankingForm #customerUuid').combogrid("getValue");
    if ($("#crawlRankingForm #groupNames").val() == null || $("#crawlRankingForm #groupNames").val() === '') {
        $().toastmessage('showWarningToast', "优化组名不能为空!");
        return;
    }
    var groupNames=$(".combobox-item-selected");
    var groupNamesSelected="";
    $.each(groupNames,function(idx,val){
        if(idx==0) {
            groupNamesSelected=groupNamesSelected+$(val).text();
        }
        else {
            groupNamesSelected=groupNamesSelected+","+$(val).text();
        }
    })
    CaptureRankJob.groupNames = groupNamesSelected;
    CaptureRankJob.exectionType = $("#crawlRankingForm input[name=exectionType]:checked").val();
    if ($("#crawlRankingForm #exectionTime").val() == null || $("#crawlRankingForm #exectionTime").val() === '') {
        $().toastmessage('showWarningToast', "执行时间不能为空!");
        return;
    }
    CaptureRankJob.exectionTime = $("#crawlRankingForm #exectionTime").val();
    CaptureRankJob.rowNumber = $("#crawlRankingForm #rowNumber").val();
    CaptureRankJob.captureInterval = $("#crawlRankingForm #captureInterval").val();
    CaptureRankJob.executionCycle = $("#crawlRankingForm #executionCycle").val();
    CaptureRankJob.pageSize = $("#crawlRankingForm #pageSize").val();
    $.ajax({
        url: '/internal/captureRank/saveCaptureRankJob',
        data: JSON.stringify(CaptureRankJob),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "保存成功", true);
            } else {
                $().toastmessage('showErrorToast', "保存失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "保存失败");
        }
    });
}
function initCrawlRankingForm(uuid) {
    $.ajax({
        url: '/internal/captureRank/getCaptureRankJob?uuid=' + uuid,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                if (data.customerUuid != null && data.customerUuid != '') {
                    $('#crawlRankingForm #customerUuid').combogrid("setValue", data.customerUuid);
                }
                initGroupNames(data);
            }
            else {
                $().toastmessage('showErrorToast', "获取失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取失败");
        }
    });
}
function modifyCaptureRankJob(uuid) {
    $.ajaxSetup({
        async: false
    });
    initCrawlRankingForm(uuid);
    $.ajaxSetup({
        async: true
    });
}
function deleteCaptureRankJob(uuid) {
    $.ajax({
        url: '/internal/captureRank/deleteCaptureRankJob?uuid=' + uuid,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "删除成功", true);
            }
            else {
                $().toastmessage('showErrorToast', "删除失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败");
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
function deleteCaptureRankJobs() {
    var uuids = getUuids();
    if (uuids == null || uuids == '') {
        alert("至少选择一条数据!");
        return;
    }
    if (confirm("确实要删除这些任务吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/captureRank/deleteCaptureRankJobs',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(postData),
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "删除成功", true);
            }
            else {
                $().toastmessage('showErrorToast', "删除失败", true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败", true);
        }
    });
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
    var select = 0;
    for (var i = 0; i < a.length; i++) {
        if (a[i].checked == true) {
            select++;
        }
    }
    if (select == a.length) {
        $("#selectAllChecked").prop("checked", true);
    } else {
        $("#selectAllChecked").prop("checked", false);
    }
}
function customerUuidReset() {
    $('#crawlRankingForm #customerUuid').combogrid("reset");
}

function changeCaptureRankJobStatus(uuid, status) {
    if(status == false) {
        if (confirm("确认要激活选中的任务吗?") == false) return;
    } else {
        if (confirm("确认要暂停的任务吗?") == false) return;
    }
    var changeCaptureRankJobCriteria = {};
    changeCaptureRankJobCriteria.uuid = parseInt(uuid);
    changeCaptureRankJobCriteria.status = status;

    $.ajax({
        url: '/internal/captureRank/changeCaptureRankJobStatus',
        data: JSON.stringify(changeCaptureRankJobCriteria),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (changeStatus) {
            if (changeStatus) {
                $().toastmessage('showSuccessToast', "操作成功", true);
            }
            else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function resetPageNumber() {
    var captureRankJobFormObj = $("#searchCaptureRankJobForm");
    var groupNames = captureRankJobFormObj.find("#groupNames").val();
    var customerUuid = captureRankJobFormObj.find("#customerUuid").val();
    if(groupNames != "") {
        captureRankJobFormObj.find("#groupNames").val($.trim(groupNames));
    }
    if(customerUuid != "") {
        captureRankJobFormObj.find("#customerUuid").val($.trim(customerUuid));
    }
    $("#searchCaptureRankJobForm").find("#currentPageNumberHidden").val(1);
}