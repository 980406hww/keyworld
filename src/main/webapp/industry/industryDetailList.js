$(function () {
    $("#industryDetailDialog").dialog("close");
    $("#showIndustryDetailTableDiv").css("margin-top",$("#topDiv").height());
    pageLoad();
});

function pageLoad() {
    var searchIndustryDetailForm = $("#searchIndustryDetailForm");
    var pageSize = searchIndustryDetailForm.find('#pageSizeHidden').val();
    var pages = searchIndustryDetailForm.find('#pagesHidden').val();
    var currentPageNumber = searchIndustryDetailForm.find('#currentPageNumberHidden').val();
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
    var a = document.getElementsByName("industryDetailUuid");
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
    var a = document.getElementsByName("industryDetailUuid");
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

function deleteIndustryDetails() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        $.messager.alert('提示', '请选择要删除的网站联系信息！！', 'info');
        return;
    }
    parent.$.messager.confirm('确认', "确实要删除这些网站联系信息吗?", function (b) {
        if (b) {
            var postData = {};
            postData.uuids = uuids;
            $.ajax({
                url: '/internal/industryDetail/deleteIndustryDetails',
                data: JSON.stringify(postData),
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
        }
    });
}

function getSelectedIDs() {
    var uuids = '';
    $.each($("input[name=industryDetailUuid]:checkbox:checked"), function () {
        if (uuids === '') {
            uuids = $(this).val();
        } else {
            uuids = uuids + "," + $(this).val();
        }
    });
    return uuids;
}

function saveIndustryDetail(uuid) {
    var industryDetailForm = $("#industryDetailDialog").find("#industryDetailForm");
    var industryDetail = {};
    industryDetail.uuid = uuid;
    industryDetail.industryID = $("#searchIndustryDetailForm").find("#industryID").val();
    industryDetail.website = industryDetailForm.find("#website").val();
    if (industryDetail.website === '') {
        $.messager.alert('提示', '请输入网站域名！！', 'warning');
        industryDetailForm.find("#website").focus();
        return;
    }
    industryDetail.qq = industryDetailForm.find("#qq").val();
    if ((industryDetail.qq !== '') && !(/^[1-9]\d{4,10}$/.test(parseInt(industryDetail.qq)))) {
        industryDetailForm.find("#qq").focus();
        $.messager.alert('提示', '请输入正确的qq号码！！', 'warning');
        return;
    }
    industryDetail.telephone = industryDetailForm.find("#telephone").val();
    if (industryDetail.telephone.indexOf('-') === -1) {
        if ((industryDetail.telephone !== '') && !(/^[1-9]\d{6,11}$/.test(parseInt(industryDetail.telephone)))) {
            industryDetailForm.find("#telephone").focus();
            $.messager.alert('提示', '请输入正确的电话号码！！', 'warning');
            return;
        }
    }
    industryDetail.weight = industryDetailForm.find("#weight").val();
    industryDetail.remark = industryDetailForm.find("#remark").val();
    industryDetail.level = industryDetailForm.find("#level").val();
    $.ajax({
        url: '/internal/industryDetail/saveIndustryDetail',
        data: JSON.stringify(industryDetail),
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

function showIndustryDetailDialog(uuid) {
    if (uuid == null) {
        $('#industryDetailForm')[0].reset();
    }
    $("#industryDetailDialog").show();
    $("#industryDetailDialog").dialog({
        resizable: false,
        width: 280,
        height: 240,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveIndustryDetail(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#industryDetailForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#industryDetailDialog").dialog("close");
                    $('#industryDetailForm')[0].reset();
                }
            }],
        onClose: function(){
            $('#industryDetailForm')[0].reset();
        }
    });
    $("#industryDetailDialog").dialog("open");
    $('#industryDetailDialog').window("resize",{
        top: $(document).scrollTop() + 150,
        left: $(document).scrollLeft() + $(window).width() / 2 - 140
    });
}

function modifyIndustryDetail(uuid) {
    getIndustryDetail(uuid, function (industryDetail) {
        if (industryDetail != null) {
            console.log(industryDetail);
            initIndustryDetailDialog(industryDetail);
            showIndustryDetailDialog(industryDetail.uuid);
        } else {
            $().toastmessage('showErrorToast', "获取网站联系信息失败!!!");
        }
    })
}

function getIndustryDetail(uuid, callback) {
    $.ajax({
        url: '/internal/industryDetail/getIndustryDetail/' + uuid,
        type: 'GET',
        success: function (industryDetail) {
            callback(industryDetail);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取网站联系信息失败!!!");
        }
    });
}

function delIndustryDetail(uuid) {
    parent.$.messager.confirm('确认', "确实要删除这个网站联系信息吗?", function (b) {
        if (b) {
            $.ajax({
                url: '/internal/industryDetail/delIndustryDetail/' + uuid,
                type: 'Get',
                success: function (result) {
                    if (result) {
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

function initIndustryDetailDialog(industryDetail) {
    var industryDetailForm = $("#industryDetailForm");
    industryDetailForm.find("#website").val(industryDetail.website);
    industryDetailForm.find("#qq").val(industryDetail.qq);
    industryDetailForm.find("#telephone").val(industryDetail.telephone);
    industryDetailForm.find("#weight").val(industryDetail.weight);
    industryDetailForm.find("#remark").val(industryDetail.remark);
    industryDetailForm.find("#level").val(industryDetail.level);
}

function changePaging(currentPage, pageSize) {
    var searchIndustryDetailForm = $("#searchIndustryDetailForm");
    searchIndustryDetailForm.find("#currentPageNumberHidden").val(currentPage);
    searchIndustryDetailForm.find("#pageSizeHidden").val(pageSize);
    searchIndustryDetailForm.submit();
}

function resetPageNumber() {
    var searchIndustryDetailFormObj = $("#searchIndustryDetailForm");
    var website = searchIndustryDetailFormObj.find("#website").val();
    var weight = searchIndustryDetailFormObj.find("#weight").val();
    var remark = searchIndustryDetailFormObj.find("#remark").val();
    if(website != "") {
        searchIndustryDetailFormObj.find("#website").val($.trim(website));
    }
    if(weight != "") {
        searchIndustryDetailFormObj.find("#weight").val($.trim(weight));
    }
    if(remark != "") {
        searchIndustryDetailFormObj.find("#remark").val($.trim(remark));
    }
    searchIndustryDetailFormObj.find("#currentPageNumberHidden").val(1);
}

function updateIndustryDetailRemark(self) {
    var newRemark = self.value.trim();
    var oldRemarkInput = $(self).parent().find("input[name='hiddenRemark']");
    var uuid = $(self).parent().parent().find("input[name='industryDetailUuid']").val();
    var postData = {};
    postData.uuid = uuid;
    postData.remark = newRemark;
    $.ajax({
        url: '/internal/industryDetail/updateIndustryDetailRemark',
        type: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(postData),
        success: function (result) {
            if (result) {
                oldRemarkInput.val(newRemark);
                $().toastmessage('showSuccessToast', '修改成功');
            } else {
                input.val(oldRemarkInput.val());
                $().toastmessage('showErrorToast', '修改失败');
            }
        },
        error: function () {
            input.val(oldRemarkInput.val());
            $().toastmessage('showErrorToast', '修改失败');
        }
    });
}

function removeUselessIndustryDetail(industryID) {
    parent.$.messager.confirm('确认', "确实要移除没有联系方式的网站信息吗?", function (b) {
        if (b) {
            $.ajax({
                url: '/internal/industryDetail/removeUselessIndustryDetail/' + industryID,
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
        }
    });
}