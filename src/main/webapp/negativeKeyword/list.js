$(function () {
    var date = new Date();
    var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
    var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
    $("#uploadTxtFileForm").find("#groupName").val("company" + month + day);
    $('#uploadTxtFileDialog').dialog("close");
    $("#positionInfoDiv").dialog("close");
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});
function changePaging(currentPage, pageSize) {
    var negativeKeywordForm = $("#negativeKeywordForm");
    negativeKeywordForm.find("#currentPageNumberHidden").val(currentPage);
    negativeKeywordForm.find("#pageSizeHidden").val(pageSize);
    negativeKeywordForm.submit();
}
function resetPageNumber() {
    var negativeKeywordForm = $("#negativeKeywordForm");
    var group = negativeKeywordForm.find("#group").val();
    if(group != "") {
        negativeKeywordForm.find("#group").val($.trim(group));
    }
    negativeKeywordForm.find("#currentPageNumberHidden").val(1);
}
function pageLoad() {
    var negativeKeywordForm = $("#negativeKeywordForm");
    var pageSize = negativeKeywordForm.find('#pageSizeHidden').val();
    var pages = negativeKeywordForm.find('#pagesHidden').val();
    var currentPageNumber = negativeKeywordForm.find('#currentPageNumberHidden').val();
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
function showUploadTxtFileDialog() {
    $("#uploadTxtFileDialog").show();
    $('#uploadTxtFileDialog').dialog({
        resizable: true,
        width: 220,
        modal: true,
        title: '导入负面词',
        buttons: [{
            text: '上传',
            iconCls: 'icon-ok',
            handler: function () {
                var fileValue = $("#uploadTxtFileDialog").find("#file").val();
                if(fileValue == ""){
                    alert("请选择要上传的负面词txt文件!");
                    return false;
                }
                var posIndex = fileValue.indexOf(".txt");
                if (posIndex == -1) {
                    alert("只能上传txt文件！");
                    return false;
                }
                var formData = new FormData();
                formData.append('group', $("#uploadTxtFileForm").find("#groupName").val());
                formData.append('file', $("#uploadTxtFileDialog").find("#file")[0].files[0]);
                $('#uploadTxtFileDialog').dialog("close");
                $.ajax({
                    url: '/internal/negativeKeywordName/uploadTxtFile',
                    type: 'POST',
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result) {
                            $().toastmessage('showSuccessToast', "上传成功",true);
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
function getNegativeExcel() {
    var group = $("#negativeKeywordForm").find("#group").val();
    if(group != '') {
        var negativeKeywordCrilteriaArray = $("#negativeKeywordForm").serializeArray();
        var downloadNegativeKeywordInfoForm = $("#downloadNegativeKeywordInfoForm");
        $.each(negativeKeywordCrilteriaArray, function(idx, val){
            downloadNegativeKeywordInfoForm.find("#"+val.name+"Hidden").val(val.value);
        });
        downloadNegativeKeywordInfoForm.submit();
    } else {
        $().toastmessage('showErrorToast', "请填写要导出数据的分组名称");
    }
}
function toTimeFormat(time) {
    var date = toDateFormat(time);
    var hours = time.getHours() < 10 ? ("0" + time.getHours()) : time.getHours();
    var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
    var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
    return date + " " + hours + ":" + minutes + ":" + seconds;
};
function toDateFormat (time) {
    var m = (time.getMonth() + 1) > 9 ? (time.getMonth() + 1) : "0" + (time.getMonth() + 1);
    var d = time.getDate() > 9 ? time.getDate() : "0" + time.getDate();
    return time.getFullYear() + "-" + m + "-" + d;
};