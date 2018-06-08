$(function () {
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