$(function () {
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});

function changePaging(currentPage, pageSize) {
    var searchAlgorithmTestPlanForm = $("#searchAlgorithmTestPlanForm");
    searchAlgorithmTestPlanForm.find("#currentPageNumberHidden").val(currentPage);
    searchAlgorithmTestPlanForm.find("#pageSizeHidden").val(pageSize);
    searchAlgorithmTestPlanForm.submit();
}

function pageLoad() {
    var searchAlgorithmTestPlanForm = $("#searchAlgorithmTestPlanForm");
    var pageSize = searchAlgorithmTestPlanForm.find('#pageSizeHidden').val();
    var pages = searchAlgorithmTestPlanForm.find('#pagesHidden').val();
    var currentPageNumber = searchAlgorithmTestPlanForm.find('#currentPageNumberHidden').val();
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

function deleteAlgorithmTestPlan(uuid) {
    $.messager.confirm('确认', "确定要删除此计划吗?", function (b) {
        if (b) {
            $.ajax({
                url: '/internal/algorithmAutoTest/deleteAlgorithmTestPlan?uuid=' + uuid,
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

function deleteAlgorithmTestPlans() {
    var uuids = getUuids();
    if (uuids == null || uuids == '') {
        alert("至少选择一条数据!");
        return;
    }
    $.messager.confirm('确认', "确定要删除这些计划吗?", function (b) {
        if (b) {
            var postData = {};
            postData.uuids = uuids.split(",");
            $.ajax({
                url: '/internal/algorithmAutoTest/deleteAlgorithmTestPlans',
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
                    } else {
                        $().toastmessage('showErrorToast', "删除失败", true);
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "删除失败", true);
                }
            });
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

function changeAlgorithmTestPlanStatus(uuid, status) {
    if(status == "0") {
        if (confirm("确认要暂停的计划吗") == false) return;
    } else {
        if (confirm("确认要激活选中的计划吗?") == false) return;
    }
    var changeAlgorithmTestPlanCriteria = {};
    changeAlgorithmTestPlanCriteria.uuid = uuid;
    changeAlgorithmTestPlanCriteria.status = status;
    $.ajax({
        url: '/internal/algorithmAutoTest/changeAlgorithmTestPlanStatus',
        data: JSON.stringify(changeAlgorithmTestPlanCriteria),
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

function updateAlgorithmTestPlansStatus(status) {
    var uuids = getUuids();
    if (uuids == null || uuids === '') {
        $.messager.alert("提示", "至少选择一条数据!", 'info');
        return;
    }
    var msg = "确定要暂停这些计划吗";
    var successMsg = "计划暂停成功";
    var errorMsg = "计划暂停失败";
    if(status == "1"){
       msg = "确定要激活这些计划吗?";
        successMsg = "计划激活成功";
        errorMsg = "计划激活失败";
    }
    $.messager.confirm('提示', msg, function (b) {
       if (b) {
           var postData = {};
           postData.uuids = uuids.split(",");
           postData.status = status;
           $.ajax({
               url: '/internal/algorithmAutoTest/updateAlgorithmTestPlansStatus',
               headers: {
                   'Accept': 'application/json',
                   'Content-Type': 'application/json'
               },
               data: JSON.stringify(postData),
               timeout: 5000,
               type: 'POST',
               success: function (data) {
                   if (data) {
                       $().toastmessage('showSuccessToast', successMsg, true);
                   }
                   else {
                       $().toastmessage('showErrorToast', errorMsg);
                   }
               },
               error: function () {
                   $().toastmessage('showErrorToast', errorMsg);
               }
           });
       }
    });
}

function resetPageNumber() {
    var searchAlgorithmTestPlanForm = $("#searchAlgorithmTestPlanForm");
    var algorithmTestPlanName = searchAlgorithmTestPlanForm.find("#algorithmTestPlanName").val();
    var operationCombineName = searchAlgorithmTestPlanForm.find("#operationCombineName").val();
    var machineGroup = searchAlgorithmTestPlanForm.find("#machineGroup").val();
    if(algorithmTestPlanName != "") {
        captureRankJobFormObj.find("#algorithmTestPlanName").val($.trim(algorithmTestPlanName));
    }
    if(operationCombineName != "") {
        captureRankJobFormObj.find("#operationCombineName").val($.trim(operationCombineName));
    }
    if(machineGroup != "") {
        captureRankJobFormObj.find("#machineGroup").val($.trim(machineGroup));
    }
    searchAlgorithmTestPlanForm.find("#currentPageNumberHidden").val(1);
}

// 打开弹框
function openDialog(uuid) {
    $("#select2DialogDiv").show().dialog({
        resizable: false,
        title: "设置算法测试计划",
        width: 320,
        fitColumns: true,//自动大小
        modal: true,
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-ok',
                handler: function () {
                    saveData(uuid);
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#select2DialogDiv").dialog("close");
                    clearFromDom();
                }
            }
        ]
    }).window("resize", {
        top: $(document).scrollTop() + $(window).height() / 2 - 200,
        left: $(document).scrollLeft() + $(window).width() / 2 - 180
    });
}

// 新增时的数据显示
function addAlgorithmTestPlans() {
    // 打开弹框
    openDialog();
    // 初始化数据
    $('#algorithmTestPlanForm')[0].reset();
    $('#algorithmTestPlanForm #testIntervalDay').spinner('setValue', 2);
    $('#algorithmTestPlanForm #testKeywordCount').spinner('setValue', 50);
    $('#algorithmTestPlanForm #testkeywordRankBegin').spinner('setValue', 20);
    $('#algorithmTestPlanForm #testkeywordRankEnd').spinner('setValue', 50);
    $('#algorithmTestPlanForm #optimizePlanCount').spinner('setValue', 20);

}

// 修改时的数据显示
function updateAlgorithmTestPlan(uuid) {
    // 请求数据当前行数据（同步）
    $.ajax({
        url: '/internal/algorithmAutoTest/getAlgorithmTestPlan?uuid=' + uuid,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        async: false,
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                $("#algorithmTestPlanForm #algorithmTestPlanName").val(data.algorithmTestPlanName);
                $("#algorithmTestPlanForm #operationCombineName").val(data.operationCombineName);
                $("#algorithmTestPlanForm #terminalType").val(data.terminalType);
                $("#algorithmTestPlanForm #searchEngine").val(data.searchEngine);
                $("#algorithmTestPlanForm #machineGroup").val(data.machineGroup);
                $("#algorithmTestPlanForm #testkeywordRankInterval").val(data.testkeywordRankInterval);
                $('#algorithmTestPlanForm #testIntervalDay').spinner('setValue', data.testIntervalDay);
                $('#algorithmTestPlanForm #testKeywordCount').spinner('setValue', data.testKeywordCount);
                $('#algorithmTestPlanForm #optimizePlanCount').spinner('setValue', data.optimizePlanCount);
                $('#algorithmTestPlanForm #testkeywordRankBegin').spinner('setValue', data.testkeywordRankBegin);
                $('#algorithmTestPlanForm #testkeywordRankEnd').spinner('setValue', data.testkeywordRankEnd);
                // 打开弹框
                openDialog(uuid);
            } else {
                $().toastmessage('showErrorToast', "获取失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取失败");
        }
    });
}

// 清空表单（针对多个执行时间）
function clearFromDom() {
    $('#algorithmTestPlanForm')[0].reset();
}

// 保存按钮
function saveData(uuid) {
    var algorithmTestPlan = {};
    if(uuid != null && uuid != ''){
        algorithmTestPlan.uuid = uuid;
    }

    var algorithmTestPlanName = $('#algorithmTestPlanForm #algorithmTestPlanName').val();
    if(algorithmTestPlanName == null || algorithmTestPlanName == ''){
        $().toastmessage('showWarningToast', "测试计划名称不能为空!");
        return;
    }
    var operationCombineName = $('#algorithmTestPlanForm #operationCombineName').val();
    if(operationCombineName == null || operationCombineName == ''){
        $().toastmessage('showWarningToast', "操作组合名称不能为空!");
        return;
    }
    var machineGroup = $('#algorithmTestPlanForm #machineGroup').val();
    if(machineGroup == null || machineGroup == ''){
        $().toastmessage('showWarningToast', "机器分组不能为空!");
        return;
    }
    var testIntervalDay = $('#algorithmTestPlanForm #testIntervalDay').val();
    if(testIntervalDay == null || testIntervalDay == ''){
        $().toastmessage('showWarningToast', "时间间隔为空!");
        return;
    }
    var testKeywordCount = $('#algorithmTestPlanForm #testKeywordCount').val();
    if(testKeywordCount == null || testKeywordCount == ''){
        $().toastmessage('showWarningToast', "测试词数不能为空!");
        return;
    }
    var optimizePlanCount = $('#algorithmTestPlanForm #optimizePlanCount').val();
    if(machineGroup == null || machineGroup == ''){
        $().toastmessage('showWarningToast', "刷量不能为空!");
        return;
    }
    var testkeywordRankBegin = $('#algorithmTestPlanForm #testkeywordRankBegin').val();
    if(testkeywordRankBegin == null || testkeywordRankBegin == ''){
        $().toastmessage('showWarningToast', "排名区间起始不能为空!");
        return;
    }
    var testkeywordRankEnd = $('#algorithmTestPlanForm #testkeywordRankEnd').val();
    if(testkeywordRankEnd == null || testkeywordRankEnd == ''){
        $().toastmessage('showWarningToast', "排名区间结束不能为空!");
        return;
    }

    algorithmTestPlan.algorithmTestPlanName = algorithmTestPlanName;
    algorithmTestPlan.operationCombineName = operationCombineName;
    algorithmTestPlan.terminalType = $('#algorithmTestPlanForm #terminalType').val();
    algorithmTestPlan.searchEngine = $('#algorithmTestPlanForm #searchEngine').val();
    algorithmTestPlan.machineGroup = machineGroup;
    algorithmTestPlan.testIntervalDay = testIntervalDay;
    algorithmTestPlan.testKeywordCount =testKeywordCount;
    algorithmTestPlan.optimizePlanCount = optimizePlanCount;
    algorithmTestPlan.testkeywordRankBegin = testkeywordRankBegin;
    algorithmTestPlan.testkeywordRankEnd = testkeywordRankEnd;

    $.ajax({
        url: '/internal/algorithmAutoTest/saveAlgorithmTestPlan',
        data: JSON.stringify(algorithmTestPlan),
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



