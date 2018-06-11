$(function () {
    $('#applicationMarketDialog').dialog("close");
    $("#showApplicationMarketListDiv").css("margin-top",$("#showApplicationMarketTableDiv").height());
    alignTableHeader();
    pageLoad();
});
function pageLoad() {
    var searchApplicationMarketForm = $("#searchApplicationMarketForm");
    var pages = searchApplicationMarketForm.find('#pagesHidden').val();
    var pageSize = searchApplicationMarketForm.find('#pageSizeHidden').val();
    var currentPageNumber = searchApplicationMarketForm.find('#currentPageNumberHidden').val();
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
    var searchApplicationMarketForm = $("#searchApplicationMarketForm");
    var marketName = searchApplicationMarketForm.find("#marketName").val();
    if(marketName != "") {
        searchApplicationMarketForm.find("#marketName").val($.trim(marketName));
    }
    searchApplicationMarketForm.find("#currentPageNumberHidden").val(1);
}
function changePaging(currentPage, pageSize) {
    var searchApplicationMarketForm = $("#searchApplicationMarketForm");
    searchApplicationMarketForm.find("#currentPageNumberHidden").val(currentPage);
    searchApplicationMarketForm.find("#pageSizeHidden").val(pageSize);
    searchApplicationMarketForm.submit();
}
function alignTableHeader(){
    var td = $("#showApplicationMarketListTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function deleteApplicationMarket(uuid) {
    if (confirm("确实要删除这条记录?") == false) return;
    $.ajax({
        url: '/internal/applicationMarket/deleteApplicationMarket/' + uuid,
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
function deleteApplicationMarkets(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要操作的设置信息');
        return;
    }
    if (confirm("确实要删除这些记录 ?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/applicationMarket/deleteApplicationMarketList',
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
function showApplicationMarketDialog(uuid) {
    if (uuid == null) {
        $('#applicationMarketForm')[0].reset();
    }
    $("#applicationMarketDialog").show();
    $("#applicationMarketDialog").dialog({
        resizable: false,
        width: 310,
        height: 300,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveApplication(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#applicationMarketForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#applicationMarketDialog').dialog("close");
                    $('#applicationMarketForm')[0].reset();
                }
            }]
    });
    $("#applicationMarketDialog").dialog("open");
    $('#applicationMarketDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveApplication(uuid) {
    var applicationMarketForm = $("#applicationMarketDialog").find("#applicationMarketForm");
    var applicationMarket = {};
    applicationMarket.uuid = uuid;
    applicationMarket.marketName = applicationMarketForm.find("#marketName").val();
    applicationMarket.marketPackageName = applicationMarketForm.find("#marketPackageName").val();
    applicationMarket.tmpPath = applicationMarketForm.find("#tmpPath").val();
    applicationMarket.apkPath = applicationMarketForm.find("#apkPath").val();
    applicationMarket.dataDBPath = applicationMarketForm.find("#dataDBPath").val();
    applicationMarket.storageDBPath = applicationMarketForm.find("#storageDBPath").val();
    applicationMarket.fileType = applicationMarketForm.find("#fileType").val();
    if(applicationMarket.marketName == null || applicationMarket.marketName== '' || applicationMarket.marketName == ""){
        alert("应用市场名称不能为空!");
        return;
    }
    if(applicationMarket.marketPackageName == null || applicationMarket.marketPackageName== '' || applicationMarket.marketPackageName == ""){
        alert("应用市场包名不能为空!");
        return;
    }
    if(applicationMarket.tmpPath == null || applicationMarket.tmpPath== '' || applicationMarket.tmpPath == ""){
        alert("临时文件地址不能为空!");
        return;
    }
    if(applicationMarket.apkPath == null || applicationMarket.apkPath== '' || applicationMarket.apkPath == ""){
        alert("安装包地址不能为空!");
        return;
    }
    if(applicationMarket.dataDBPath == null || applicationMarket.dataDBPath== '' || applicationMarket.dataDBPath == ""){
        alert("App数据库地址不能为空!");
        return;
    }
    if(applicationMarket.storageDBPath == null || applicationMarket.storageDBPath== '' || applicationMarket.storageDBPath == ""){
        alert("App存储数据库地址不能为空!");
        return;
    }
    if(applicationMarket.fileType == null || applicationMarket.fileType== '' || applicationMarket.fileType == ""){
        alert("文件类型不能为空!");
        return;
    }
    $.ajax({
        url: '/internal/applicationMarket/saveApplicationMarket',
        data: JSON.stringify(applicationMarket),
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
    $("#applicationMarketDialog").dialog("close");
    $('#applicationMarketForm')[0].reset();
}
function modifyApplicationMarket(uuid) {
    getApplicationMarket(uuid, function (applicationMarket) {
        if (applicationMarket != null) {
            initApplicationMarketDialog(applicationMarket);
            showApplicationMarketDialog(uuid);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getApplicationMarket(uuid, callback) {
    $.ajax({
        url: '/internal/applicationMarket/getApplicationMarket/' + uuid,
        type: 'Get',
        success: function (applicationMarket) {
            callback(applicationMarket);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function initApplicationMarketDialog(applicationMarket) {
    var applicationMarketForm = $("#applicationMarketForm");
    applicationMarketForm.find("#marketName").val(applicationMarket.marketName);
    applicationMarketForm.find("#marketPackageName").val(applicationMarket.marketPackageName);
    applicationMarketForm.find("#tmpPath").val(applicationMarket.tmpPath);
    applicationMarketForm.find("#apkPath").val(applicationMarket.apkPath);
    applicationMarketForm.find("#dataDBPath").val(applicationMarket.dataDBPath);
    applicationMarketForm.find("#storageDBPath").val(applicationMarket.storageDBPath);
    applicationMarketForm.find("#fileType").val(applicationMarket.fileType);
}