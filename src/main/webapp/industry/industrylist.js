$(function () {
    $("#industryDialog").dialog("close");
    $("#updateIndustryUserIDDialog").dialog("close");
    $("#uploadExcelDialog").dialog("close");
    $("#showIndustryTableDiv").css("margin-top",$("#topDiv").height());
    pageLoad();
});

function pageLoad() {
    var searchIndustryForm = $("#searchIndustryForm");
    var pageSize = searchIndustryForm.find('#pageSizeHidden').val();
    var pages = searchIndustryForm.find('#pagesHidden').val();
    var currentPageNumber = searchIndustryForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    showCustomerBottomDiv.find("#chooseRecords").val(pageSize);

    if(parseInt(currentPageNumber) > 1 && parseInt(currentPageNumber) < parseInt(pages)) {
        showCustomerBottomDiv.find("#firstButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#upButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#nextButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#lastButton").removeAttr("disabled");
    } else if (parseInt(pages) <= 1) {
        showCustomerBottomDiv.find("#firstButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
    } else if (parseInt(currentPageNumber) <= 1) {
        showCustomerBottomDiv.find("#firstButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
    } else {
        showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
    }
}

function selectAll(self) {
    var a = document.getElementsByName("industryUuid");
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
    var a = document.getElementsByName("industryUuid");
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

function deleteIndustries() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        $.messager.alert('提示', '请选择要删除的行业信息！！', 'info');
        return;
    }
    parent.$.messager.confirm('确认', "确实要删除这些行业信息吗?", function (b) {
       if(b) {
           var postData = {};
           postData.uuids = uuids
           $.ajax({
               url: '/internal/industry/deleteIndustries',
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
    });
}

function getSelectedIDs() {
    var uuids = '';
    $.each($("input[name=industryUuid]:checkbox:checked"), function () {
        if (uuids === '') {
            uuids = $(this).val();
        } else {
            uuids = uuids + "," + $(this).val();
        }
    });
    return uuids;
}

function getUsefulSelectedIDs() {
    var uuids = '';
    $.each($("input[name=industryUuid]:checkbox:checked"), function () {
        if ($(this).parent().parent().find("td[name='status'] span").text() === '爬取完成') {
            if (uuids === '') {
                uuids = $(this).val();
            } else {
                uuids = uuids + "," + $(this).val();
            }
        }
    });
    return uuids;
}

function saveIndustry(uuid, loginName) {
    var industryForm = $("#industryDialog").find("#industryForm");
    var industryInfo = {};
    industryInfo.uuid = uuid;
    if (null === uuid) {
        industryInfo.terminalType = $("#searchIndustryForm").find("#terminalType").val();
    }
    industryInfo.userID = loginName;
    industryInfo.industryName = industryForm.find("#industryName").val();
    if (industryInfo.industryName === '') {
        $.messager.alert('提示', '请输入行业名称！！', 'warning');
        industryForm.find("#industryName").focus();
        return;
    }
    industryInfo.targetUrl = industryForm.find("#targetUrl").val();
    industryInfo.pageNum = industryForm.find("#pageNum").val();
    industryInfo.pagePerNum = industryForm.find("#pagePerNum").val();
    if(industryInfo.targetUrl !== ""){
        if(industryInfo.targetUrl.indexOf("http://") === -1 || industryInfo.targetUrl.indexOf("https://") === -1){
            industryInfo.targetUrl = "http://" + industryInfo.targetUrl
        }
    }
    if (industryInfo.pageNum === '') {
        $.messager.alert('提示', '请输入爬取页数！！', 'warning');
        return;
    }
    if (industryInfo.pagePerNum === '') {
        $.messager.alert('提示', '请输入每页条数！！', 'warning');
        return;
    }
    industryInfo.searchEngine = industryForm.find("#searchEngine").val();
    industryInfo.status = industryForm.find("#status").val();
    $.ajax({
        url: '/internal/industry/saveIndustryInfo',
        data: JSON.stringify(industryInfo),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
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

function showIndustryDialog(uuid, loginName) {
    if (uuid == null) {
        $('#industryForm')[0].reset();
        $('#industryForm').find('#searchEngine').val("百度");
        $('#industryForm #pageNum').spinner('setValue', 2);
        $('#industryForm #pagePerNum').spinner('setValue', 10);
    }
    $("#industryDialog").show();
    $("#industryDialog").dialog({
        resizable: false,
        width: 280,
        height: 240,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveIndustry(uuid, loginName);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#industryForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#industryDialog").dialog("close");
                    $('#industryForm')[0].reset();
                }
            }],
        onClose: function(){
            $('#industryForm')[0].reset();
        }
    });
    $("#industryDialog").dialog("open");
    $('#industryDialog').window("resize",{
        top: $(document).scrollTop() + 150,
        left: $(document).scrollLeft() + $(window).width() / 2 - 140
    });
}

function modifyIndustry(uuid) {
    getIndustry(uuid, function (industryInfo) {
        if (industryInfo != null) {
            initIndustryDialog(industryInfo);
            showIndustryDialog(industryInfo.uuid, industryInfo.userID);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}

function getIndustry(uuid, callback) {
    $.ajax({
        url: '/internal/industry/getIndustry/' + uuid,
        type: 'GET',
        success: function (industryInfo) {
            callback(industryInfo);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}

function delIndustry(uuid) {
    parent.$.messager.confirm('确认', "确实要删除这个行业信息吗?", function (b) {
        if (b) {
            $.ajax({
                url: '/internal/industry/delIndustryInfo/' + uuid,
                type: 'Get',
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
    });
}

function initIndustryDialog(industryInfo) {
    var industryForm = $("#industryForm");
    industryForm.find("#industryName").val(industryInfo.industryName);
    industryForm.find("#searchEngine").val(industryInfo.searchEngine);
    industryForm.find("#targetUrl").val(industryInfo.targetUrl);
    industryForm.find('#pageNum').spinner('setValue', industryInfo.pageNum);
    industryForm.find('#pagePerNum').spinner('setValue', industryInfo.pagePerNum);
    industryForm.find("#status").val(industryInfo.status);
}

function setPageNumAndPagePerNumByTargetUrl(url){
    if (url !== '') {
        $("#industryForm #pageNum").spinner('setValue', 0);
        $("#industryForm #pagePerNum").spinner('setValue', 0);
    }
}

function changePaging(currentPage, pageSize) {
    var searchIndustryForm = $("#searchIndustryForm");
    searchIndustryForm.find("#currentPageNumberHidden").val(currentPage);
    searchIndustryForm.find("#pageSizeHidden").val(pageSize);
    searchIndustryForm.submit();
}

function resetPageNumber() {
    var searchIndustryFormObj = $("#searchIndustryForm");
    var industryName = searchIndustryFormObj.find("#industryName").val();
    var searchEngine = searchIndustryFormObj.find("#searchEngine").val();
    var status = searchIndustryFormObj.find("#status").val();
    if(industryName !== "") {
        searchIndustryFormObj.find("#industryName").val($.trim(industryName));
    }
    if(searchEngine !== "") {
        searchIndustryFormObj.find("#searchEngine").val($.trim(searchEngine));
    }
    if(status !== "") {
        searchIndustryFormObj.find("#status").val($.trim(status));
    }
    searchIndustryFormObj.find("#currentPageNumberHidden").val(1);
}

function updateIndustryUserID() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        $.messager.alert('提示', '请选择要修改所属用户的行业信息！！', 'info');
        return;
    }

    $("#updateIndustryUserIDDialog").show();
    $("#updateIndustryUserIDDialog").dialog({
        resizable: false,
        width: 250,
        height: 100,
        modal: true,
        closed: true,
        buttons: [{
            text: '确定',
            handler: function () {
                parent.$.messager.confirm('询问', "确认修改行业所属人吗？", function(b) {
                    if (b) {
                        var data = {};
                        data.uuids = uuids.split(",");
                        data.userID = $("#userID").val();
                        $.ajax({
                            url: '/internal/industry/updateIndustryUserID',
                            data: JSON.stringify(data),
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
                });
            }
        }]
    });
    $("#updateIndustryUserIDDialog").dialog("open");
    $('#updateIndustryUserIDDialog').window("resize", {
        top: $(document).scrollTop() + 150,
        left: $(document).scrollLeft() + $(window).width() / 2 - 125
    });
}

function updateIndustryStatus() {
    var uuids = getUsefulSelectedIDs();
    if (uuids === '') {
        $.messager.alert('提示', '请选择要重新爬取网站联系信息的行业信息！！', 'info');
        return;
    }
    parent.$.messager.confirm('询问', "确认要重新爬取行业的网站联系信息吗？", function(b) {
        if (b) {
            var data = {};
            data.uuids = uuids.split(",");
            $.ajax({
                url: '/internal/industry/updateIndustryStatus',
                data: JSON.stringify(data),
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
    });
}

//表格上传
function uploadIndustryInfos(excelType) {
    $("#uploadExcelDialog").show();
    $("#uploadExcelDialog").dialog({
        resizable: false,
        width: 280,
        height: 160,
        modal: true,
        closed: true,
        buttons: [{
            text: '上传',
            iconCls: 'icon-ok',
            handler: function () {
                var uploadForm = $("#uploadExcelForm");
                var uploadFileTxt = uploadForm.find("#uploadExcelFile").val();
                var uploadFileNames = uploadFileTxt.split(".");
                var uploadFileType = uploadFileNames[uploadFileNames.length - 1];
                var fileTypeArr = new Array("xls", "xlsx");
                var fileUploadFlag = false;
                for (var i = 0; i < fileTypeArr.length; i++) {
                    if (fileTypeArr[i] === uploadFileType) {
                        fileUploadFlag = true;
                        break;
                    }
                }
                if (!fileUploadFlag) {
                    $.messager.alert('提示', '请选择表格文件, 格式: .xls, .xlsx', 'warning');
                    return false;
                }
                var formData = new FormData();
                formData.append('file', uploadForm.find("#uploadExcelFile")[0].files[0]);
                formData.append('excelType', excelType);
                if (fileUploadFlag) {
                    $.ajax({
                        url: '/internal/industry/uploadIndustryInfos',
                        type: 'POST',
                        cache: false,
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (result) {
                            if (result) {
                                $().toastmessage('showSuccessToast', '上传成功！！！', true);
                            } else {
                                $().toastmessage('showErrorToast', '上传失败！！！');
                            }
                        },
                        error: function () {
                            $().toastmessage('showErrorToast', '上传失败！！！');
                        }
                    });
                }
                $("#uploadExcelDialog").dialog("close");
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#uploadExcelDialog").dialog("close");
                    $('#uploadExcelForm')[0].reset();
                }
            }],
        onClose: function () {
            $('#uploadExcelForm')[0].reset();
        }
    });
    $("#uploadExcelDialog").dialog("open");
    $('#uploadExcelDialog').window("resize",{
        top: $(document).scrollTop() + 100,
        left: $(document).scrollLeft() + $(window).width() / 2 - 140
    });
}

// 导出网站联系信息
function downloadIndustryInfo() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        $.messager.alert('提示', '请选择要要导出网站联系信息的行业！！', 'info');
        return;
    }
    parent.$.messager.confirm('询问', "确认要导出这些行业的网站联系信息吗？", function(b) {
        if (b) {
            var postData = {};
            postData.uuids = uuids.split(",");
            $.ajax({
                url: '/internal/industry/downloadIndustryInfo',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (!result) {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "操作失败");
                }
            });
        }
    });
}


