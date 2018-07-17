
$(function () {
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});

function submitPageNumber() {
    var searchNegativeStandardSetting = $("#searchNegativeStandardSetting");
    var keyword = searchNegativeStandardSetting.find("#keyword").val();
    if (keyword != "") {
        searchNegativeStandardSetting.find("#keyword").val($.trim(keyword));
    }
    searchNegativeStandardSetting.find("#currentPageNumberHidden").val(1);
}

function changePaging(currentPage, pageSize) {
    var searchNegativeStandardSetting = $("#searchNegativeStandardSetting");
    searchNegativeStandardSetting.find("#currentPageNumberHidden").val(currentPage);
    searchNegativeStandardSetting.find("#pageSizeHidden").val(pageSize);
    searchNegativeStandardSetting.submit();
}

function pageLoad() {
    var searchNegativeStandardSetting = $("#searchNegativeStandardSetting");
    var pageSize = searchNegativeStandardSetting.find('#pageSizeHidden').val();
    var pages = searchNegativeStandardSetting.find('#pagesHidden').val();
    var currentPageNumber = searchNegativeStandardSetting.find('#currentPageNumberHidden').val();
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
    $("#searchEngine").val($('#searchEngineHidden').val());
    $("#reachStandard").val($('#reachStandardHidden').val());
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

function  updateNegativeStandardSetting(uuid,reachStandard) {
     var negativeStandardSettingCriteriaNot = {};
    $('#keywordDiv').val($('#'+uuid).find("td[name = 'keyword']").text());
    $('#searchEngineDiv').val($('#'+uuid).find("td[name = 'searchEngine']").text());
    $('#topOnePageNegativeCountDiv').spinner('setValue',$('#'+uuid).find("td[name = 'topOnePageNegativeCount']").text());
    $('#topTwoPageNegativeCountDiv').spinner('setValue',$('#'+uuid).find("td[name = 'topTwoPageNegativeCount']").text());
    $('#topThreePageNegativeCountDiv').spinner('setValue',$('#'+uuid).find("td[name = 'topThreePageNegativeCount']").text());
    $('#topFourPageNegativeCountDiv').spinner('setValue',$('#'+uuid).find("td[name = 'topFourPageNegativeCount']").text());
    $('#topFivePageNegativeCountDiv').spinner('setValue',$('#'+uuid).find("td[name = 'topFivePageNegativeCount']").text());
    negativeStandardSettingCriteriaNot.uuid = uuid;
    $('#keywordDiv').attr('disabled',true);
    $('#searchEngineDiv').attr('disabled',true);
    $('#showNegativeStandardSettingDiv').show();
    $('#showNegativeStandardSettingDiv').dialog({
        resizable: false,
        width: 300,
        modal: true,
        title: '负面达标设置',
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var negativeStandardSettingCriteria = {};
                negativeStandardSettingCriteria.keyword = $('#keywordDiv').val();
                negativeStandardSettingCriteria.searchEngine = $('#searchEngineDiv').val();
                negativeStandardSettingCriteria.topOnePageNegativeCount = $('#topOnePageNegativeCountDiv').val();
                negativeStandardSettingCriteria.topTwoPageNegativeCount = $('#topTwoPageNegativeCountDiv').val();
                negativeStandardSettingCriteria.topThreePageNegativeCount = $('#topThreePageNegativeCountDiv').val();
                negativeStandardSettingCriteria.topFourPageNegativeCount = $('#topFourPageNegativeCountDiv').val();
                negativeStandardSettingCriteria.topFivePageNegativeCount = $('#topFivePageNegativeCountDiv').val();
                negativeStandardSettingCriteria.uuid = uuid;
                negativeStandardSettingCriteria.reachStandard = reachStandard;
                    $.ajax({
                    url:'/internal/negativeStandardSetting/updateNegativeStandardSetting',
                    data:JSON.stringify(negativeStandardSettingCriteria),
                    headers:{
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type:'POST',
                    success:function (result) {
                        if(result){
                            $().toastmessage('showSuccessToast',"更新成功",true);
                        }else {
                            $().toastmessage('showErrorToast',"更新失败")
                        }
                    },
                    error:function () {
                        $().toastmessage('showErrorToast',"更新失败");
                    }
                })
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#showNegativeStandardSettingDiv').dialog("close");

                }
            }]
    });
    $('#showNegativeStandardSettingDiv').window("resize",{top:$(document).scrollTop() + 150});
}

function addNegativeStandardSetting(customerUuid) {
    $('#keywordDiv').val("");
    $('#topOnePageNegativeCountDiv').spinner('setValue',0);
    $('#topTwoPageNegativeCountDiv').spinner('setValue',0);
    $('#topThreePageNegativeCountDiv').spinner('setValue',0);
    $('#topFourPageNegativeCountDiv').spinner('setValue',0);
    $('#topFivePageNegativeCountDiv').spinner('setValue',0);
    $("#keywordDiv").removeAttr("disabled");
    $("#searchEngineDiv").removeAttr("disabled");
    $('#showNegativeStandardSettingDiv').show();
    $('#showNegativeStandardSettingDiv').dialog({
        resizable: false,
        width: 300,
        modal: true,
        title: '添加负面达标设置',
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var negativeStandardSettingCriteria = {};
                negativeStandardSettingCriteria.keyword = $('#keywordDiv').val();
                negativeStandardSettingCriteria.searchEngine = $('#searchEngineDiv').val();
                negativeStandardSettingCriteria.topOnePageNegativeCount = $('#topOnePageNegativeCountDiv').val();
                negativeStandardSettingCriteria.topTwoPageNegativeCount = $('#topTwoPageNegativeCountDiv').val();
                negativeStandardSettingCriteria.topThreePageNegativeCount = $('#topThreePageNegativeCountDiv').val();
                negativeStandardSettingCriteria.topFourPageNegativeCount = $('#topFourPageNegativeCountDiv').val();
                negativeStandardSettingCriteria.topFivePageNegativeCount = $('#topFivePageNegativeCountDiv').val();
                negativeStandardSettingCriteria.customerUuid = customerUuid;
                if($('#keywordDiv').val()==""){
                    $().toastmessage('showErrorToast',"关键字不能为空");
                    return false;
                }
                $.ajax({
                    url:'/internal/negativeStandardSetting/addNegativeStandardSetting',
                    data:JSON.stringify(negativeStandardSettingCriteria),
                    headers:{
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type:'POST',
                    dataType:'json',
                    success:function (result) {
                        if(result){
                            $().toastmessage('showSuccessToast',"添加成功",true);
                        }else {
                            $().toastmessage('showErrorToast',"负面达标词已经存在!");
                        }
                    },
                    error:function () {
                        $().toastmessage('showErrorToast',"更新失败")
                    }
                })
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#showNegativeStandardSettingDiv').dialog("close");
                }
            }]
    });
    $('#showNegativeStandardSettingDiv').window("resize",{top:$(document).scrollTop() + 150});
}

function deleteNegativeStandardSetting(uuid) {
    if (confirm("确定删除这条负面达标词吗?") == false) return;
    $.ajax({
        url: '/internal/negativeStandardSetting/deleteNegativeStandardSetting/'+ uuid,
        type: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
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

function deleteNegativeStandardSettings(){
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的负面达标词');
        return;
    }
    if (confirm("确定删除这些负面达标词吗?") == false) return;
    var negativeStandardSetting = {};
    negativeStandardSetting.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/negativeStandardSetting/deleteNegativeStandardSettings',
        type: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data:JSON.stringify(negativeStandardSetting),
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
