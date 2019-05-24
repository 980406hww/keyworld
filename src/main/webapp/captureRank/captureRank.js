$(function () {
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
    $('#customers').select2({});
    $('#groups').select2({});
});
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

function changeCaptureRankJobStatus(uuid, status) {
    if(status == "false") {
        if (confirm("确认要暂停的任务吗") == false) return;
    } else {
        if (confirm("确认要激活选中的任务吗?") == false) return;
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
    captureRankJobFormObj.find("#currentPageNumberHidden").val(1);
}

// 优化组名的Change事件
$('#groups').on('change', function () {
    var group = $('#groups option:selected').text();
    var groupList = [];
    groupList.push(group);
    var postData = {};
    postData.groupNames = groupList;
    $.ajax({
        url: '/internal/customer/searchCustomersWithKeyword',
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(postData),
        success: function (data) {
            var r = [];
            if (data.length != 0) {
                r[0] = {"id": "NaN", "text": "请选择客户"};
                $.each(data, function () {
                    r.push({
                        "id": this.uuid,
                        "contactPerson": this.contactPerson,
                        "text": "<div class='select2-result-repository__title'>" +
                            "客户名：<span style='color: green'>" + this.contactPerson + "</span></div> " +
                            "<div class='select2-result-repository__description'>" +
                            "手机号：<span style='color: red'>" + (this.telphone == null ? "空" : this.telphone) + "</span>" +
                            " | QQ号：<span style='color: red'>" + (this.qq == null ? "空" : this.qq) + "</span>" +
                            " | 邮箱号：<span style='color: red'>" + (this.email == null ? "空" : this.email) + "</span></div>"
                    })
                });
            } else {
                r[0] = {"id": "NaN", "text": "当前组无客户"};
            }
            $('#customers').empty().select2({
                data: r,
                dropdownAutoWidth: true,
                escapeMarkup: function (markup) {
                    return markup;
                },
                templateResult: function (repo) {
                    if (repo.loading) {
                        return repo.contactPerson;
                    }
                    return repo.text;
                },
                templateSelection: function (repo) {
                    return repo.contactPerson || repo.text;
                }
            });
        }
    });
});

// 打开弹框
function openDialog(uuid) {
    $("#select2DialogDiv").show().dialog({
        resizable: false,
        title: "设置抓排名任务",
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
                    $('#groups').empty();
                    $('#customers').empty();
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
function addCaptureRankJobs() {
    $("#exectionTime1").css("width", "150px"); // 时间选择框缩短
    $("#nextExecuteTimeBtn").css('display', 'block'); // 增加时间框按钮
    // 打开弹框
    openDialog();
    // 提前填充，显示不会停顿
    $('#groups').empty().select2({
        data: [{"id": "NaN", "text": "请选择优化组名"}]
    });
    $('#customers').empty().select2({
        data: [{"id": "NaN", "text": "请选择客户"}]
    });
    // 优化组名显示
    $.ajax({
        url: '/internal/customerKeyword/searchGroupsByTerminalType',
        method: 'post',
        dataType: "json",
        success: function (data) {
            var r = [];
            r[0] = {"id": "NaN", "text": "请选择优化组名"};
            $.each(data, function (i, item) {
                r.push({"id": i, "text": item.name})
            });
            $('#groups').select2({
                data: r,
                dropdownAutoWidth: true
            });
        }
    });
    // 初始化数据
    $('#crawlRankingForm')[0].reset();
    $('#crawlRankingForm #rowNumber').spinner('setValue', 100);
    $('#crawlRankingForm #captureDaysInterval').spinner('setValue', 1);
    $('#crawlRankingForm #captureInterval').spinner('setValue', 500);
    $('#crawlRankingForm #executionCycle').spinner('setValue', 0);
    $('#crawlRankingForm #pageSize').spinner('setValue', "${sessionScope.terminalType}" == 'PC' ? 10 : 50);
}

// 修改时的数据显示
function updateCaptureRankJobs(uuid) {
    $("#exectionTime1").css("width", "200px"); // 恢复时间选择框长度
    $("#nextExecuteTimeBtn").css('display', 'none'); // 隐藏增加时间选择器按钮
    var globalData = {};
    // 请求数据当前行数据（同步）
    $.ajax({
        url: '/internal/captureRank/getCaptureRankJob?uuid=' + uuid,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        async: false,
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                globalData = data;
                $('#groups').empty().select2({
                    data: (data.groupNames == null || data.groupNames == '') ? [{
                        "id": "NaN",
                        "text": "当前组不存在，重新选择"
                    }] : [{"id": 0, "text": data.groupNames}]
                });
                $('#customers').empty().select2({
                    data: data.customerUuid != null ? [{
                        "id": data.customerUuid,
                        "text": data.contactPerson
                    }] : [{"id": "NaN", "text": "当前未选择客户"}]
                });
                $("#crawlRankingForm input[name=exectionType][value=" + data.exectionType + "]").attr("checked", true);
                $("#crawlRankingForm #exectionTime1").val(data.exectionTime);
                $('#crawlRankingForm #rowNumber').spinner('setValue', data.rowNumber);
                $('#crawlRankingForm #captureDaysInterval').spinner('setValue', data.captureDaysInterval);
                $('#crawlRankingForm #captureInterval').spinner('setValue', data.captureInterval);
                $('#crawlRankingForm #executionCycle').spinner('setValue', data.executionCycle);
                $('#crawlRankingForm #pageSize').spinner('setValue', data.pageSize);
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
    // 优化组名显示
    $.ajax({
        url: '/internal/customerKeyword/searchGroupsByTerminalType',
        method: 'post',
        dataType: "json",
        success: function (data) {
            var r = [];
            $.each(data, function (i, item) {
                r.push({"id": i, "text": item.name});
            });
            $('#groups').select2({
                data: r,
                dropdownAutoWidth: true
            });
            // 显示当前组名的客户
            $.ajax({
                url: '/internal/customer/searchCustomersWithKeyword',
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({"groupNames": [globalData.groupNames]}),
                success: function (data) {
                    var r = [];
                    if(data.length != 0){
                        $.each(data, function (i, item) {
                            r.push({
                                "id": item.uuid,
                                "contactPerson": item.contactPerson,
                                "text": "<div class='select2-result-repository__title'>" +
                                    "客户名：<span style='color: green'>" + item.contactPerson + "</span></div> " +
                                    "<div class='select2-result-repository__description'>" +
                                    "手机号：<span style='color: red'>" + (item.telphone == null ? "空" : item.telphone) + "</span>" +
                                    " | QQ号：<span style='color: red'>" + (item.qq == null ? "空" : item.qq) + "</span>" +
                                    " | 邮箱号：<span style='color: red'>" + (item.email == null ? "空" : item.email) + "</span></div>"
                            });
                        });
                        if (globalData.customerUuid == null) {
                            r.splice(0, 0, {"id": "NaN", "text": "当前未选择客户"});
                        } else {
                            r.splice(1, 0, {"id": "NaN", "text": "置空"});
                        }
                    }else {
                        r[0] = {"id": "NaN", "text": "当前组无客户"};
                    }
                    $('#customers').empty().select2({
                        data: r,
                        dropdownAutoWidth: true,
                        escapeMarkup: function (markup) {
                            return markup;
                        },
                        templateResult: function (repo) {
                            if (repo.loading) {
                                return repo.contactPerson;
                            }
                            return repo.text;
                        },
                        templateSelection: function (repo) {
                            return repo.contactPerson || repo.text;
                        }
                    });
                }
            });
        }
    });
}

// 新增执行时间
function nextExecuteTime() {
    var liNum = $("#formData li").length;
    var newLi = $("#formData li").eq(liNum - 6);
    newLi.after('' +
        '<li id="li' + (liNum - 6) + '">' +
        '<span style="margin-left: 3px">执行时间:&nbsp;</span>' +
        '<input type="text" class="Wdate" id="exectionTime' + (liNum - 6) + '" autocomplete="off" onfocus="WdatePicker({lang:\'zh-cn\',dateFmt:\'HH:mm:ss\'})" required style="width: 150px">' +
        '<input type="button" value="取消" onclick="clearThisTime(' + (liNum - 6) + ')" style="margin-left: 14px;"/>' +
        '</li>')
}

// 去除时间
function clearThisTime(liNum) {
    var index = $("#li" + liNum).index();
    $("#formData li:eq(" + (index - 1) + ")").remove();
}

// 清空表单（针对多个执行时间）
function clearFromDom() {
    $('#crawlRankingForm')[0].reset();
    var start = $("#start").index();
    var end = $("#end").index();
    while ((end - 1) !== start) {
        $("#formData li:eq(4)").remove();
        start += 1;
    }
}

// 保存按钮
function saveData(uuid) {
    var CaptureRankJob = {};
    if(uuid != null && uuid != ''){
        CaptureRankJob.uuid = uuid;
    }

    var groupName = $('#groups option:selected').val();
    if(groupName == null || groupName == 'NaN'){
        $().toastmessage('showWarningToast', "优化组名不能为空!");
        return;
    }

    CaptureRankJob.groupNames = $('#groups option:selected').text();
    CaptureRankJob.customerUuid = $('#customers option:selected').val() == "NaN" ? null : $('#customers option:selected').val();

    var start = $("#start").index();
    var end = $("#end").index();
    var executeTimes = [];
    for (var i = 0; i < end - start; i++) {
        var temp = $("#formData li:eq(" + (start - 1 + i) + ") input").val();
        if(temp == '' || temp == null){
            $().toastmessage('showWarningToast', "第" + (i + 1) + "个执行时间为空，请选择");
            return;
        }
        executeTimes.push(temp);
    }

    CaptureRankJob.exectionType = $("#crawlRankingForm input[name=exectionType]:checked").val();
    CaptureRankJob.rowNumber = $("#crawlRankingForm #rowNumber").val();
    CaptureRankJob.captureInterval = $("#crawlRankingForm #captureInterval").val();
    CaptureRankJob.captureDaysInterval = $("#crawlRankingForm #captureDaysInterval").val();
    CaptureRankJob.executionCycle = $("#crawlRankingForm #executionCycle").val();
    CaptureRankJob.pageSize = $("#crawlRankingForm #pageSize").val();

    $.ajax({
        url: '/internal/captureRank/saveCaptureRankJob',
        data: JSON.stringify({"captureRankJob": CaptureRankJob, "executeTimes": executeTimes}),
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