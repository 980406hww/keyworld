$(function () {
    $('#uploadTxtFileDialog').dialog("close");
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    alignTableHeader();
    pageLoad();
    window.onresize = function(){
        alignTableHeader();
    }
    $("#searchCookieFrom").find("#searchEngine").val($("#searchEngineHidden").val());
});
function submitPageNumber() {
    var searchCookieFrom = $("#searchCookieFrom");
    var cookieStr = searchCookieFrom.find("#cookieStr").val();
    if(cookieStr != "") {
        searchCookieFrom.find("#cookieStr").val($.trim(cookieStr));
    }
    searchCookieFrom.find("#currentPageNumberHidden").val(1);
}
function alignTableHeader(){
    var td = $("#showCookieTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function changePaging(currentPage, pageSize) {
    var searchCookieFrom = $("#searchCookieFrom");
    searchCookieFrom.find("#currentPageNumberHidden").val(currentPage);
    searchCookieFrom.find("#pageSizeHidden").val(pageSize);
    searchCookieFrom.submit();
}
function pageLoad() {
    var searchCookieFrom = $("#searchCookieFrom");
    var pageSize = searchCookieFrom.find('#pageSizeHidden').val();
    var pages = searchCookieFrom.find('#pagesHidden').val();
    var currentPageNumber = searchCookieFrom.find('#currentPageNumberHidden').val();
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
function uploadCookieTxt() {
    $("#uploadTxtFileDiv").css("display", "block");
    $('#uploadTxtFileDialog').dialog({
        resizable: true,
        width: 220,
        modal: true,
        buttons: [{
            text: '上传',
            iconCls: 'icon-ok',
            handler: function () {
                var fileValue = $("#uploadTxtFileDialog").find("#file").val();
                if(fileValue == ""){
                    alert("请选择要上传的cookie txt文件!");
                    return false;
                }
                var posIndex = fileValue.indexOf(".txt");
                if (posIndex == -1) {
                    alert("只能上传txt文件！");
                    return false;
                }
                var formData = new FormData();
                formData.append('searchEngine', $("#uploadTxtFileDialog").find("#searchEngine").val());
                formData.append('file', $("#uploadTxtFileDialog").find("#file")[0].files[0]);
                $('#uploadTxtFileDialog').dialog("close");
                $.ajax({
                    url: '/internal/cookie/saveCookieByFile',
                    type: 'POST',
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result) {
                            $().toastmessage('showSuccessToast', "上传成功", true);
                        } else {
                            $().toastmessage('showErrorToast', "上传失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "上传失败");
                        $('#uploadTxtFileDialog').dialog("close");
                    }
                });
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#uploadTxtFileDialog').dialog("close");
                }
            }]
    });
    $('#uploadTxtFileDialog').window("resize",{top:$(document).scrollTop() + 100});
}