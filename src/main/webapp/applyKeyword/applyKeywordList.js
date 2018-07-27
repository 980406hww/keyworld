$(function () {
    $('#applyKeywordDialog').dialog("close");
    $("#showApplyKeywordListDiv").css("margin-top",$("#showApplyKeywordTableDiv").height());
    alignTableHeader();
    pageLoad();
});
function pageLoad() {
    var searchApplyKeywordForm = $("#searchApplyKeywordForm");
    var pages = searchApplyKeywordForm.find('#pagesHidden').val();
    var pageSize = searchApplyKeywordForm.find('#pageSizeHidden').val();
    var currentPageNumber = searchApplyKeywordForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    $("#showCustomerBottomDiv").find("#chooseRecords").val(pageSize);
    if(parseInt(currentPageNumber) > 1 && parseInt(currentPageNumber) < parseInt(pages)) {
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
function resetPageNumber() {
    var searchApplyKeywordForm = $("#searchApplyKeywordForm");
    var keyword = searchApplyKeywordForm.find("#keyword").val();
    var applyName = searchApplyKeywordForm.find("#applyName").val();
    if(keyword != "") {
        searchApplyKeywordForm.find("#applyKeyword").val($.trim(keyword));
    }
    if(applyName != "") {
        searchApplyKeywordForm.find("#applyName").val($.trim(applyName));
    }
    searchApplyKeywordForm.find("#currentPageNumberHidden").val(1);
}
function changePaging(currentPage, pageSize) {
    var searchApplyKeywordForm = $("#searchApplyKeywordForm");
    searchApplyKeywordForm.find("#currentPageNumberHidden").val(currentPage);
    searchApplyKeywordForm.find("#pageSizeHidden").val(pageSize);
    searchApplyKeywordForm.submit();
}
function alignTableHeader(){
    var td = $("#showApplyKeywordListTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
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
function deleteApplyKeyword(uuid) {
    if (confirm("确实要删除这个关键词?") == false) return;
    $.ajax({
        url: '/internal/applyKeyword/deleteApplyKeyword/' + uuid,
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
function deleteApplyKeywordList(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要操作的设置信息');
        return;
    }
    if (confirm("确实要删除这些客户吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/applyKeyword/deleteApplyKeywordList',
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
function showApplyKeywordDialog(uuid,type) {
    if (uuid == null) {
        $('#applyKeywordForm')[0].reset();
    }
    $("#applyKeywordDialog").dialog({
        resizable: false,
        width: 265,
        height: 150,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveApplyKeyword(uuid,type);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#applyKeywordForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#applyKeywordDialog').dialog("close");
                    $('#applyKeywordForm')[0].reset();
                }
            }]
    });
    $("#applyKeywordDialog").dialog("open");
    $('#applyKeywordDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveApplyKeyword(uuid,types) {
    var applyKeywordForm = $("#applyKeywordDialog").find("#applyKeywordForm");
    // var keywords = [];
    var keywordsStr = applyKeywordForm.find("#keyword").val();
    if(types == "edit" && keywordsStr.split(",").length > 1){
        alert("修改时,只允许输入一个关键词！");
        return;
    }
    if(!(keywordsStr != "" && keywordsStr != null && keywordsStr != '')){
        alert("关键词不能为空！");
        return;
    }
    var applyUuid = applyKeywordForm.find("#applyName").val();
    if(!(applyUuid != "" && applyUuid != null && applyUuid != '')){
        alert("应用名不能为空！");
        return;
    }
    var postMan = {};
    postMan.uuid = uuid;
    postMan.keywords = keywordsStr;
    postMan.applyUuid = applyUuid;
    postMan.applyName = applyKeywordForm.find("select option:selected").text();
    $.ajax({
        url: '/internal/applyKeyword/saveapplyKeyword',
        data: postMan,
        headers: {
            'Accept': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "保存成功",true);
            } else {
                $().toastmessage('showErrorToast', "保存失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "保存失败");
        }
    });
    $("#applyKeywordDialog").dialog("close");
    $('#applyKeywordForm')[0].reset();
}
function modifyApplyKeyword(uuid,type) {
    getApplyKeyword(uuid, function (applyKeyword) {
        if (applyKeyword != null) {
            initApplyKeywordDialog(applyKeyword);
            showApplyKeywordDialog(uuid,type);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getApplyKeyword(uuid,callback) {
    $.ajax({
        url: '/internal/applyKeyword/getApplyKeyword/' + uuid,
        type: 'Get',
        success: function (applyKeyword) {
            callback(applyKeyword);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}

function initApplyKeywordDialog(applyKeyword) {
    var applyKeywordForm = $("#applyKeywordForm");
    applyKeywordForm.find("#keyword").val(applyKeyword.keyword);
    $("#applyKeywordForm select").children("option").each(function(){
        var temp_value = $(this).val();
        if(temp_value == applyKeyword.applyUuid){
            $(this).attr("selected","selected");
        }
    });
}