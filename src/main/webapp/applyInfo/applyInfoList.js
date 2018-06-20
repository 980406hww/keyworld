$(function () {
    $('#applyInfoDialog').dialog("close");
    $("#showApplyInfoListDiv").css("margin-top",$("#showApplyInfoTableDiv").height());
    alignTableHeader();
    pageLoad();
});
function pageLoad() {
    var searchApplyInfoForm = $("#searchApplyInfoForm");
    var pages = searchApplyInfoForm.find('#pagesHidden').val();
    var pageSize = searchApplyInfoForm.find('#pageSizeHidden').val();
    var currentPageNumber = searchApplyInfoForm.find('#currentPageNumberHidden').val();
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
    var searchApplyInfoForm = $("#searchApplyInfoForm");
    var appName = searchApplyInfoForm.find("#appName").val();
    if(appName != "") {
        searchApplyInfoForm.find("#appName").val($.trim(appName));
    }
    searchApplyInfoForm.find("#currentPageNumberHidden").val(1);
}
function changePaging(currentPage, pageSize) {
    var searchApplyInfoForm = $("#searchApplyInfoForm");
    searchApplyInfoForm.find("#currentPageNumberHidden").val(currentPage);
    searchApplyInfoForm.find("#pageSizeHidden").val(pageSize);
    searchApplyInfoForm.submit();
}
function alignTableHeader(){
    var td = $("#showApplyInfoListTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function deleteApplyInfo(uuid) {
    if (confirm("确实要删除这条记录?") == false) return;
    $.ajax({
        url: '/internal/applyInfo/deleteApplyInfo/' + uuid,
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
function deleteApplyInfos(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要操作的设置信息');
        return;
    }
    if (confirm("确实要删除这些记录?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/applyInfo/deleteApplyInfoList',
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
function showApplyInfoDialog(uuid) {
    if (uuid == null) {
        $('#applyInfoForm')[0].reset();
    }
    $("#applyInfoDialog").show();
    $("#applyInfoDialog").dialog({
        resizable: false,
        width: 320,
        height: 220,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveApplyInfo(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#applyInfoForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#applyInfoDialog').dialog("close");
                    $('#applyInfoForm')[0].reset();
                }
            }]
    });
    $("#applyInfoDialog").dialog("open");
    $('#applyInfoDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveApplyInfo(uuid) {
    var applyInfoForm = $("#applyInfoDialog").find("#applyInfoForm");
    var applyInfo = {};
    applyInfo.uuid = uuid;
    applyInfo.applicationMarketUuid = applyInfoForm.find("#applicationName").val();
    applyInfo.applicationMarketName = applyInfoForm.find("select option:selected").text();
    applyInfo.appName = applyInfoForm.find("#appName").val();
    applyInfo.packageName = applyInfoForm.find("#packageName").val();
    applyInfo.id = applyInfoForm.find("#id").val();
    applyInfo.color = applyInfoForm.find("#color").val();
    applyInfo.posandcolor = applyInfoForm.find("#posandcolor").val();
    if(applyInfo.appName == null || applyInfo.appName== '' || applyInfo.appName == ""){
        alert("应用名不能为空!");
        return;
    }
    if(!(applyInfo.applicationMarketUuid != "" && applyInfo.applicationMarketUuid != null && applyInfo.applicationMarketUuid != '')){
        alert("所属应用不能为空！");
        return;
    }
    if(applyInfo.packageName == null || applyInfo.packageName== '' || applyInfo.packageName == ""){
        alert("包名不能为空!");
        return;
    }
    if(applyInfo.id == null || applyInfo.id== '' || applyInfo.id == ""){
        alert("应用市场ID不能为空!");
        return;
    }
    $.ajax({
        url: '/internal/applyInfo/saveApplyInfo',
        data: JSON.stringify(applyInfo),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
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
    $("#applyInfoDialog").dialog("close");
    $('#applyInfoForm')[0].reset();
}
function modifyApplyInfo(uuid) {
    getApplyInfo(uuid, function (applyInfo) {
        if (applyInfo != null) {
            initApplyInfoDialog(applyInfo);
            showApplyInfoDialog(uuid);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getApplyInfo(uuid, callback) {
    $.ajax({
        url: '/internal/applyInfo/getApplyInfo/' + uuid,
        type: 'Get',
        success: function (applyInfo) {
            callback(applyInfo);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function initApplyInfoDialog(applyInfo) {
    var applyInfoForm = $("#applyInfoForm");
    applyInfoForm.find("#appName").val(applyInfo.appName);
    applyInfoForm.find("#packageName").val(applyInfo.packageName);
    applyInfoForm.find("#id").val(applyInfo.id);
    applyInfoForm.find("#color").val(applyInfo.color);
    applyInfoForm.find("#posandcolor").val(applyInfo.posandcolor);
    $("#applyInfoForm select").children("option").each(function(){
        var temp_value = $(this).val();
        if(temp_value == applyInfo.applicationMarketUuid){
            $(this).attr("selected","selected");
        }
    });
}