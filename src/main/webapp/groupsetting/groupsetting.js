$(function () {
    $(".datalist-list li").find("div.body").each(function (index, self) {
        var listsize = $(self).attr("listsize");
        var height = listsize * 75;
        $(self).css("height", height);
    });
    loadingCheckTerminalType();
});

function loadingCheckTerminalType() {
    var terminalType = $("#chargeForm").find("#terminalType").val();
    checkTerminalType(terminalType, false);
}

function checkTerminalType(terminalType, isManualSwitch) {
    var a = $(".mytabs .link").find("li.active a");
    if (a[0] != undefined && a[0].innerHTML.substring(2) == terminalType) {
        return;
    }
    $(".mytabs .link").find("li").removeClass("active");
    if (terminalType == "PC") {
        $(".mytabs .link").find("li[name='PC']").addClass("active");
        $("#chargeForm").find("#terminalType").val($.trim(terminalType));
    }
    if (terminalType == "Phone") {
        $(".mytabs .link").find("li[name='Phone']").addClass("active");
        $("#chargeForm").find("#terminalType").val($.trim(terminalType));
    }
    if (isManualSwitch) {
        trimSearchCondition();
    }
}
function enterIn(e) {
    var e = e || event,
        keyCode = e.which || e.keyCode;
    if (keyCode == 13) {
        trimSearchCondition();
    }
}

function changePaging(currentPage, pageSize) {
    var chargeForm = $("#chargeForm");
    chargeForm.find("#currentPageNumberHidden").val(currentPage);
    chargeForm.find("#pageSizeHidden").val(pageSize);
    chargeForm.submit();
}

function trimSearchCondition() {
    var chargeForm = $("#chargeForm");
    var currentPage = chargeForm.find("#currentPageNumberHidden").val();
    var optimizedGroupName = $(".conn").find("li:first-child input[name='optimizedGroupName']").val();
    var operationType = $(".conn").find("select[name='operationType']").val();
    if (currentPage != '1') {
        chargeForm.find("#currentPageNumberHidden").val(1);
    }
    if (optimizedGroupName != "") {
        chargeForm.find("#optimizedGroupName").val($.trim(optimizedGroupName));
    } else {
        chargeForm.find("#optimizedGroupName").val(null);
    }
    if (operationType != ""){
        chargeForm.find("#operationType").val($.trim(operationType));
    } else {
        chargeForm.find("#operationType").val(null);
    }
    chargeForm.submit();
}

function showGroupSettingDialog(groupUuid) {

}

function delGroupSetting() {
    
}