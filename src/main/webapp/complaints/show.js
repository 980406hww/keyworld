$(function() {
    pageLoad();
    $("#showMainKeywordTableDiv").css("margin-top",$("#showMainKeywordTopDiv").height());
    $("#showAddMainKeywordDialog").dialog("close");
    alignTableHeader();
    window.onresize = function(){
        alignTableHeader();
    }
});
function pageLoad() {
    var showMainKeywordBottomDiv = $('#showMainKeywordBottomDiv');
    var pageSize = $("#serachMainKeywordForm").find("#displaysRecordsHidden").val();
    $("#showMainKeywordBottomDiv").find('#chooseRecords').val(pageSize);
    var selectGroup = $('#serachMainKeywordForm').find("#itemGroupHidden").val();
    $('#serachMainKeywordForm').find("#itemGroup").val(selectGroup);
    var pages = showMainKeywordBottomDiv.find('#pagesHidden').val();
    showMainKeywordBottomDiv.find('#pagesHidden').val(pages);
    var currentPage  = showMainKeywordBottomDiv.find('#currentPageHidden').val();
    showMainKeywordBottomDiv.find('#currentPageHidden').val(currentPage);
    if(currentPage<=1){
        currentPage=1;
        showMainKeywordBottomDiv.find("#fisrtButton").attr("disabled","disabled");
        showMainKeywordBottomDiv.find("#upButton").attr("disabled","disabled");
    }else if(currentPage>=pages){
        currentPage=pages;
        showMainKeywordBottomDiv.find("#nextButton").attr("disabled","disabled");
        showMainKeywordBottomDiv.find("#lastButton").attr("disabled","disabled");
    }else {
        showMainKeywordBottomDiv.find("#firstButton").removeAttr("disabled");
        showMainKeywordBottomDiv.find("#upButton").removeAttr("disabled");
        showMainKeywordBottomDiv.find("#nextButton").removeAttr("disabled");
        showMainKeywordBottomDiv.find("#lastButton").removeAttr("disabled");
    }
}
function alignTableHeader(){
    var td = $("#headerTable tr:first td");
    var ctd = $("#showMainKeywordTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function showAddMainKeywordDialog(uuid) {
    if(uuid==null){
        $('#mainKeywordForm')[0].reset();
    }
    $("#showAddMainKeywordDialog").dialog({
        resizable: false,
        width: 350,
        height: 365,
        modal: true,
        buttons:[{
            text:"保存",
            iconCls: 'icon-ok',
            handler : function() {
                savaMainKeyword(uuid);
            }
        },{
            text: '清空',
            iconCls: 'fi-trash',
            handler: function () {
                $('#mainKeywordForm')[0].reset();
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#showAddMainKeywordDialog").dialog("close");
                $('#mainKeywordForm')[0].reset();
            }
        }]
    });
    $("#showAddMainKeywordDialog").dialog("open");
    $("#showAddMainKeywordDialog").window("resize",{top:$(document).scrollTop() + 100});
}
function savaMainKeyword(uuid) {
    var mainKeywordObj = {};
    mainKeywordObj.uuid = uuid;
    var mainKeywordForm = $('#mainKeywordForm');
    mainKeywordObj.keyword = mainKeywordForm.find('#mKeyword').val().trim();
    mainKeywordObj.group = mainKeywordForm.find('#mGroup').val();

    var ngKeywords = mainKeywordForm.find('#ngKeyword').val().split(',');
    if (mainKeywordObj.keyword === "") {
        alert("关键字不能为空");
        mainKeywordForm.find('#mKeyword').focus();
        return false;
    }
    if (mainKeywordObj.keyword === "") {
        alert("请选择有效城市");
        mainKeywordForm.find('#mGroup').focus();
        return false;
    }
    if (ngKeywords === "" || ngKeywords === "null") {
        alert("请输入需要投诉的负面词汇");
        mainKeywordForm.find('#ngKeyword').focus();
        return false;
    }
    mainKeywordObj.tsNegativeKeywords = [];
    $.each(ngKeywords, function (idx, val) {
        var ngKeywordObj = {"keyword": val.trim()};
        mainKeywordObj.tsNegativeKeywords.push(ngKeywordObj);
    });
    $.ajax({
        url: '/internal/complaints/save',
        data: JSON.stringify(mainKeywordObj),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "保存成功!", true);
            } else {
                $().toastmessage('showErrorToast',"保存失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast',"保存失败");
        }
    });
    $("#showAddMainKeywordDialog").dialog("close");
    $('#mainKeywordForm')[0].reset();
}
function getMainKeyword(uuid) {
    $.ajax({
        url: '/internal/complaints/findTSMainKeywordById/' + uuid,
        type: 'Get',
        success: function (tsMainKeyword) {
            if (tsMainKeyword != null ) {
                initMainKeywordDialog(tsMainKeyword);
                showAddMainKeywordDialog(tsMainKeyword.uuid);
            } else {
                $().toastmessage('showErrorToast',"获取信息失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast',"获取信息失败！");
        }
    });
}
function initMainKeywordDialog(mainKeyword) {
    var mainKeywordForm = $("#mainKeywordForm");
    mainKeywordForm.find("#mUuid").val(mainKeyword.uuid);
    mainKeywordForm.find("#mKeyword").val(mainKeyword.keyword);
    mainKeywordForm.find("#mGroup").val(mainKeyword.group);
    var ngKeyword =  mainKeyword.tsNegativeKeywords;
    var tmpNegativeKeywords = '';
    $.each(ngKeyword,function (idx,val) {
        tmpNegativeKeywords = tmpNegativeKeywords + val.keyword+',';
    });
    var negativeKeywords = tmpNegativeKeywords.substring(0,tmpNegativeKeywords.length-1);
    mainKeywordForm.find("#ngKeyword").val(negativeKeywords);
}
function deleteMainKeyword(uuid) {
    if (confirm("确实要删除这个主关键字吗?") == false) return;
    $.ajax({
        url: '/internal/complaints/delete/' + uuid,
        type: 'Get',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast',"删除成功！", true);
            } else {
                $().toastmessage('showErrorToast',"删除失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast',"删除失败！");
        }
    });
}
function getSelectedIDs() {
    var uuids = '';
    $.each($("input[name=uuid]:checkbox:checked"), function(){
        if(uuids === ''){
            uuids = $(this).val();
        }else{
            uuids = uuids + "," + $(this).val();
        }
    });
    return uuids;
}
function deleteMainKeywords(self) {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        alert('请选择要操作的设置信息！');
        return ;
    }
    if (confirm("确实要删除这些投诉关键字吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/complaints/deleteTSMainKeywords',
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
function serachMainKeywords(currentPage, displaysRecords) {
    var keyword = $("#serachMainKeyword").find("#itemKeyword").val();
    var group = $("#serachMainKeyword").find("#itemgroup").val();
    var showMainKeywordBottomDiv = $("#showMainKeywordBottomDiv");
    var pages = showMainKeywordBottomDiv.find("#pagesHidden").val();
    var url= '/internal/complaints/findTSMainKeywords?currentPage='+currentPage+'&displaysRecords='+displaysRecords+'&keyword='+ keyword+'&group='+ group;
    window.location.href=url;
}
function chooseRecords(currentPage, displayRecords) {
    $('#showMainKeywordBottomDiv').find("#currentPageHidden").val(currentPage);
    $('#showMainKeywordBottomDiv').find("#displaysRecordsHidden").val(displayRecords);
    serachMainKeywords(currentPage, displayRecords);
}
function selectAll(self){
    var a = document.getElementsByName("uuid");
    if(self.checked){
        for(var i = 0;i<a.length;i++){
            a[i].checked = true;
        }
    }else{
        for(var i = 0;i<a.length;i++){
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
    var mainKeywordFormObj = $("#serachMainKeywordForm");
    var itemKeyword = mainKeywordFormObj.find("#itemKeyword").val();
    if(itemKeyword != "") {
        mainKeywordFormObj.find("#itemKeyword").val($.trim(itemKeyword));
    }
    $("#serachMainKeywordForm").find("#currentPageHidden").val(1);
}