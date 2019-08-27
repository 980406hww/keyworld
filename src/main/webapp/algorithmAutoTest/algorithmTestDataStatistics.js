$(function () {
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});

function changePaging(currentPage, pageSize) {
    var searchAlgorithmTestDataStatisticsForm = $("#searchAlgorithmTestDataStatisticsForm");
    searchAlgorithmTestDataStatisticsForm.find("#currentPageNumberHidden").val(currentPage);
    searchAlgorithmTestDataStatisticsForm.find("#pageSizeHidden").val(pageSize);
    searchAlgorithmTestDataStatisticsForm.submit();
}

function pageLoad() {
    var searchAlgorithmTestDataStatisticsForm = $("#searchAlgorithmTestDataStatisticsForm");
    var pageSize = searchAlgorithmTestDataStatisticsForm.find('#pageSizeHidden').val();
    var pages = searchAlgorithmTestDataStatisticsForm.find('#pagesHidden').val();
    var currentPageNumber = searchAlgorithmTestDataStatisticsForm.find('#currentPageNumberHidden').val();
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