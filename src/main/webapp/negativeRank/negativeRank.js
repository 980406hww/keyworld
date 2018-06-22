/**
 * Created by Administrator on 2018/5/31.
 */
    $(function () {
        $("#centerDiv").css("margin-top", $("#topDiv").height());
        pageLoad();
        $("#searchEngine").val($('#searchEngineHidden').val());
    });

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

    function submitPageNumber() {
        var searchNegativeRankFrom = $("#searchNegativeRankFrom");
        var keyword = searchNegativeRankFrom.find("#keyword").val();
        if (keyword != "") {
            searchNegativeRankFrom.find("#keyword").val($.trim(keyword));
        }
        searchNegativeRankFrom.find("#currentPageNumberHidden").val(1);
    }

    function changePaging(currentPage, pageSize) {
        var searchNegativeRankFrom = $("#searchNegativeRankFrom");
        searchNegativeRankFrom.find("#currentPageNumberHidden").val(currentPage);
        searchNegativeRankFrom.find("#pageSizeHidden").val(pageSize);
        searchNegativeRankFrom.submit();
    }

    function pageLoad() {
        var searchNegativeRankFrom = $("#searchNegativeRankFrom");
        var pageSize = searchNegativeRankFrom.find('#pageSizeHidden').val();
        var pages = searchNegativeRankFrom.find('#pagesHidden').val();
        var currentPageNumber = searchNegativeRankFrom.find('#currentPageNumberHidden').val();
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

    function count(rankTag){
        var negativeCount = 0;
       $.each(rankTag,function (index,value) {
           var index = removeAllSpace($(value).text());
           if(index!=""){
             negativeCount+= index.replace("，",",").split(',').length;
           }
       });
        var index = removeAllSpace(rankTag.context.value);
        if(index!=""){
            negativeCount+= index.replace("，",",").split(',').length;
        }
        return negativeCount;
    }

    function removeAllSpace(str) {
        return str.replace(/\s+/g, "");
    }