$(function () {
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});

function changePaging(currentPage, pageSize) {
    var searchAlgorithmTestTaskForm = $("#searchAlgorithmTestTaskForm");
    searchAlgorithmTestTaskForm.find("#currentPageNumberHidden").val(currentPage);
    searchAlgorithmTestTaskForm.find("#pageSizeHidden").val(pageSize);
    searchAlgorithmTestTaskForm.submit();
}

function pageLoad() {
    var searchAlgorithmTestTaskForm = $("#searchAlgorithmTestTaskForm");
    var pageSize = searchAlgorithmTestTaskForm.find('#pageSizeHidden').val();
    var pages = searchAlgorithmTestTaskForm.find('#pagesHidden').val();
    var currentPageNumber = searchAlgorithmTestTaskForm.find('#currentPageNumberHidden').val();
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