    $(function () {
        $('#websiteDialog').dialog("close");
        $("#centerDiv").css("margin-top", $("#topDiv").height());
        pageLoad();
    });

    function changePaging(currentPage, pageSize) {
        var searchwordInfoForm = $("#searchwordInfoForm");
        searchwordInfoForm.find("#currentPageNumberHidden").val(currentPage);
        searchwordInfoForm.find("#pageSizeHidden").val(pageSize);
        searchwordInfoForm.submit();
    }

    function resetPageNumber() {
        var searchwordInfoForm = $("#searchwordInfoForm");
        var userName = searchwordInfoForm.find("#userName").val();
        var monitoring = searchwordInfoForm.find("#monitoring").val();
        var operationType = searchwordInfoForm.find("#operationType").val();
        var keywordInfo = searchwordInfoForm.find("#keywordInfo").val();
        var createTime = searchwordInfoForm.find("#createTime").val();

        if (userName != "") {
            searchwordInfoForm.find("#userName").val($.trim(userName));
        }
        if (monitoring != "") {
            searchwordInfoForm.find("#monitoring").val($.trim(monitoring));
        }
        if (operationType != "") {
            searchwordInfoForm.find("#operationType").val($.trim(operationType));
        }
        if (keywordInfo != "") {
            searchwordInfoForm.find("#keywordInfo").val($.trim(keywordInfo));
        }
        if (createTime != "") {
            searchwordInfoForm.find("#createTime").val($.trim(createTime));
        }
        searchwordInfoForm.find("#currentPageNumberHidden").val(1);
    }

    function resetDate(){
        $("#userName").val(null);
        $("#monitoring").val(null);
        $("#operationType").val(null);
        $("#keywordInfo").val(null);
        window.location.href ='/internal/keywordInfo/keywordInfos';
    };

    function pageLoad() {
        $('#keywordInfos').dialog("close");
        var searchwordInfoForm = $("#searchwordInfoForm");
        var pageSize = searchwordInfoForm.find('#pageSizeHidden').val();
        var pages = searchwordInfoForm.find('#pagesHidden').val();
        var currentPageNumber = searchwordInfoForm.find('#currentPageNumberHidden').val();
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
        var passflag=document.getElementById("autoflag");
        $("#operationType").val(passflag.defaultValue);
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

    function openUrls(keywordInfo,spliterStr) {
        $("#keywordInfos").attr("style","display:block;");
        var urls = keywordInfo.split(spliterStr);
        var key=document.getElementById('typeKeyword');
        key.value=null;
        for (var i = 0; i<1; i++) {
            key.value+=urls[i]+('\r\n');
        }
        var inp=document.getElementById('remark');
            inp.value=null;
        for (var i = 1; i<urls.length; i++) {
            inp.value+=urls[i]+('\r\n');
        }

        $("#keywordInfos").dialog({
                    resizable: false,
                    width: 500,
                    height: 300,
                    modal: true,
                    title: '关键字网站',
                    closed: true,
                    buttons: [
                        {
                            text: '确定',
                            position: '25%',
                            handler: function () {
                                $('#keywordInfos').dialog("close");
                            }
                        }]
        });
        $("#keywordInfos").dialog("open");
        $('#keywordInfos').window("resize", {top: $(document).scrollTop() + 150});
    }
    