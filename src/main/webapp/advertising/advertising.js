$(function () {
    $("#advertisingDialog").dialog("close");
    $("#showAdvertisingTableDiv").css("margin-top",$("#topDiv").height());
    pageLoad();
});
function pageLoad() {
    var searchAdvertisingForm = $("#searchAdvertisingForm");
    var pageSize = searchAdvertisingForm.find('#pageSizeHidden').val();
    var pages = searchAdvertisingForm.find('#pagesHidden').val();
    var currentPageNumber = searchAdvertisingForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    showCustomerBottomDiv.find("#chooseRecords").val(pageSize);

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
function deleteBatchAdvertising(websiteUuid) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的广告');
        return;
    }
    if (confirm("确实要删除这些广告吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.websiteUuid = websiteUuid;
    $.ajax({
        url: '/internal/advertising/delAdvertisings',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "删除成功",true);
            } else {
                $().toastmessage('showErrorToast', "删除失败",true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");

        }
    });
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
function showAdvertisingDialog(websiteUuid, uuid) {
    if (websiteUuid == null) {
        $('#advertisingForm')[0].reset();
    }
    $.ajax({
        url: "/internal/advertising/searchAdvertisingAllTypeAndCustomerList/" + websiteUuid,
        type: "GET",
        success: function (advertisingAllTypeAndCustomerList) {
            $("#customer_list").find('option').remove();
            $("#advertisingType_list").find('option').remove();
            $("#advertisingArcType_list").find('option').remove();
            if (advertisingAllTypeAndCustomerList.customerList != null) {
                $.each(advertisingAllTypeAndCustomerList.customerList, function (idx, val) {
                    $("#customer_list").append("<option value='" + val.contactPerson + "_" + val.uuid + "'></option>")
                });
            }
            if (advertisingAllTypeAndCustomerList.advertisingType != null) {
                $("#advertisingType_list").append("<option value='默认分类_0'></option>");
                $.each(advertisingAllTypeAndCustomerList.advertisingType, function (idx, val) {
                    $("#advertisingType_list").append("<option value='"  +val.typename + "_"  + val.id + "'></option>")
                });
            }
            if (advertisingAllTypeAndCustomerList.advertisingArcType != null) {
                $("#advertisingArcType_list").append("<option value='没有同名标识所有栏目_0'></option>");
                $.each(advertisingAllTypeAndCustomerList.advertisingArcType, function (idx, val) {
                    $("#advertisingArcType_list").append("<option value='" + val.typename + "_" + val.id + "'></option>")
                });
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
    if (uuid == 0){
        changeAdvertisingBodySubmit("code");
    }else {
        changeAdvertisingBodySubmit("all");

    }
    $("#advertisingDialog").show();
    $("#advertisingDialog").dialog({
        resizable: false,
        width: 320,
        height: 600,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveAdvertising(websiteUuid, uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#advertisingForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#advertisingDialog").dialog("close");
                    $('#advertisingForm')[0].reset();
                }
            }]
    });
    $("#advertisingDialog").dialog("open");
    $('#advertisingDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveAdvertising(websiteUuid, uuid) {
    var advertisingForm = $("#advertisingDialog").find("#advertisingForm");
    var customerInfo = advertisingForm.find("#customerInfo").val();
    if (customerInfo == "") {
        alert("请选择用户名称");
        advertisingForm.find("#customerInfo").focus();
        return;
    }
    var customerUuid = customerInfo.split('_')[customerInfo.split('_').length - 1];
    var advertisingTagname = advertisingForm.find("#advertisingTagname").val();
    if (advertisingTagname == "") {
        alert("请输入广告标识");
        advertisingForm.find("#advertisingTagname").focus();
        return;
    }
    var advertisingAdName = advertisingForm.find("#advertisingAdName").val();
    if (advertisingAdName == "") {
        alert("请输入广告名称");
        advertisingForm.find("#advertisingAdName").focus();
        return;
    }
    var advertisingType = advertisingForm.find("#advertisingType").val();
    var advertisingTypeId = advertisingType.split('_')[advertisingType.split('_').length - 1];
    var advertisingArcType = advertisingForm.find("#advertisingArcType").val();
    var advertisingArcTypeId = advertisingArcType.split('_')[advertisingArcType.split('_').length - 1];
    var advertisingTimeSet = $('input:radio[name="advertisingTimeSet"]:checked').val();
    var advertisingBodyChecked = $('input:radio[name="advertisingBodyChecked"]:checked').val();
    var advertisingStarttime = advertisingForm.find("#advertisingStarttime").val();
    var advertisingEndtime = advertisingForm.find("#advertisingEndtime").val();
    var advertisingExpbody = advertisingForm.find("#advertisingExpbody").val();
    var advertising = {};
    advertising.websiteUuid = $.trim(websiteUuid);
    advertising.customerInfo = $.trim(customerInfo);
    advertising.customerUuid = $.trim(customerUuid);
    advertising.advertisingAdName = $.trim(advertisingAdName);
    advertising.advertisingTagname = $.trim(advertisingTagname);
    advertising.advertisingType = $.trim(advertisingType);
    advertising.advertisingTypeId = $.trim(advertisingTypeId);
    advertising.advertisingArcType = $.trim(advertisingArcType);
    advertising.advertisingArcTypeId = $.trim(advertisingArcTypeId);
    advertising.advertisingTimeSet = $.trim(advertisingTimeSet);
    advertising.advertisingStarttime = userDate(advertisingStarttime);
    advertising.advertisingEndtime = userDate(advertisingEndtime);
    advertising.advertisingExpbody = $.trim(advertisingExpbody);
    if (uuid == 0){
        advertising.advertisingBodyChecked = $.trim(advertisingBodyChecked);
        var normbody = {};
        if (advertisingBodyChecked == 0){
            normbody.style = "code";
            normbody.htmlcode = advertisingForm.find("#htmlcode").val();
        }else if (advertisingBodyChecked == 1){
            normbody.style = "txt";
            normbody.title = advertisingForm.find("#txtTitle").val();
            normbody.link = advertisingForm.find("#txtLink").val();
            normbody.color = advertisingForm.find("#txtColor").val();
            normbody.size = advertisingForm.find("#txtSize").val();
        }else if (advertisingBodyChecked == 2){
            normbody.style = "img";
            normbody.url = advertisingForm.find("#imgUrl").val();
            normbody.link = advertisingForm.find("#imgLink").val();
            normbody.width = advertisingForm.find("#imgWidth").val();
            normbody.height = advertisingForm.find("#imgHeight").val();
        }else {
            normbody.style = "flash";
            normbody.link = advertisingForm.find("#flashLink").val();
            normbody.width = advertisingForm.find("#flashWidth").val();
            normbody.height = advertisingForm.find("#flashHeight").val();
        }
        advertising.normbody = normbody;
        $.ajax({
            url: '/internal/advertising/saveAdvertising',
            data: JSON.stringify(advertising),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "保存成功",true);
                } else {
                    $().toastmessage('showErrorToast', "保存失败, 请检查广告标识是否唯一！");

                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "保存失败");
            }
        });
    } else {
        advertising.uuid = $.trim(uuid);
        advertising.advertisingId = $.trim(advertisingForm.find("#advertisingId").val());
        var advertisingNormbody = advertisingForm.find("#advertisingNormbody").val();
        advertising.advertisingNormbody = $.trim(advertisingNormbody);
        $.ajax({
            url: '/internal/advertising/updateAdvertising',
            data: JSON.stringify(advertising),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "保存成功",true);
                } else {
                    $().toastmessage('showErrorToast', "保存失败");

                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "保存失败");
            }
        });
    }
    $("#advertisingDialog").dialog("close");
    $('#advertisingForm')[0].reset();
}
function modifyAdvertising(uuid) {
    getAdvertising(uuid, function (advertising) {
        if (advertising != null) {
            initAdvertisingDialog(advertising);
            showAdvertisingDialog(advertising.websiteUuid, uuid);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getAdvertising(uuid, callback) {
    $.ajax({
        url: '/internal/advertising/getAdvertising/' + uuid,
        type: 'Get',
        success: function (advertising) {
            callback(advertising);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function delAdvertising(uuid) {
    if (confirm("确实要删除这个广告吗?") == false)
        return;
    $.ajax({
        url: '/internal/advertising/delAdvertising/' + uuid,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'Get',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "删除成功",true);
            } else {
                $().toastmessage('showErrorToast', "删除失败",true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败",true);
        }
    });
}
function initAdvertisingDialog(advertising) {
    var advertisingForm = $("#advertisingDialog").find("#advertisingForm");
    advertisingForm.find("#customerInfo").val(advertising.customerInfo + "_" + advertising.customerUuid);
    advertisingForm.find("#advertisingId").val(advertising.advertisingId);
    advertisingForm.find("#advertisingTagname").val(advertising.advertisingTagname);
    advertisingForm.find("#advertisingAdName").val(advertising.advertisingAdName);
    advertisingForm.find("#advertisingType").val(advertising.advertisingType);
    advertisingForm.find("#advertisingArcType").val(advertising.advertisingArcType);
    advertisingForm.find($('input:radio[name="advertisingTimeSet"]:checked').val(advertising.advertisingTimeSet));
    $("input[type=radio][name=advertisingTimeSet][value="+advertising.advertisingTimeSet+"]").attr("checked",true);
    advertisingForm.find("#advertisingStarttime").val(userDate(advertising.advertisingStarttime));
    advertisingForm.find("#advertisingEndtime").val(userDate(advertising.advertisingEndtime));
    advertisingForm.find("#advertisingNormbody").val(advertising.advertisingNormbody);
    advertisingForm.find("#advertisingExpbody").val(advertising.advertisingExpbody);
}

function changePaging(currentPage, pageSize) {
    var searchAdvertisingForm = $("#searchAdvertisingForm");
    searchAdvertisingForm.find("#currentPageNumberHidden").val(currentPage);
    searchAdvertisingForm.find("#pageSizeHidden").val(pageSize);
    searchAdvertisingForm.submit();
}

function resetPageNumber() {
    var searchAdvertisingForm = $("#searchAdvertisingForm");
    var customerInfo = searchAdvertisingForm.find("#customerInfo").val();
    if(customerInfo != "") {
        searchAdvertisingForm.find("#customerInfo").val($.trim(customerInfo));
    }
    var advertisingAdName = searchAdvertisingForm.find("#advertisingAdName").val();
    if(advertisingAdName != "") {
        searchAdvertisingForm.find("#advertisingAdName").val($.trim(advertisingAdName));
    }
    searchAdvertisingForm.submit();
}

function userDate(uData){
    var myDate = new Date(uData);
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    return year + '-' + month + '-' + day;
}

function changeAdvertisingBodySubmit(type) {
    var advertisingForm = $("#advertisingDialog").find("#advertisingForm");
    var code = advertisingForm.find("#code");
    var txt = advertisingForm.find("#txt");
    var img = advertisingForm.find("#img");
    var flash = advertisingForm.find("#flash");
    var advertisingBodyCheckedTable = advertisingForm.find("#advertisingBodyCheckedTable");
    var advertisingNormbodyTable = advertisingForm.find("#advertisingNormbodyTable");
    var advertisingTagname = advertisingForm.find('#advertisingTagname');
    code.css("display","none");
    txt.css("display","none");
    img.css("display","none");
    flash.css("display","none");
    advertisingBodyCheckedTable.css("display","none");
    advertisingNormbodyTable.css("display","none");
    advertisingTagname.removeAttr("readonly");
    if ("code" == type){
        advertisingBodyCheckedTable.css("display","block");
        code.css("display","block");
    } else if ("txt" == type){
        advertisingBodyCheckedTable.css("display","block");
        txt.css("display","block");
    } else if ("img" == type){
        advertisingBodyCheckedTable.css("display","block");
        img.css("display","block");
    } else if ("flash" == type) {
        advertisingBodyCheckedTable.css("display","block");
        flash.css("display","block");
    }else {
        advertisingNormbodyTable.css("display","block");
        advertisingTagname.attr("readonly","readonly");
    }
}

function pushAdvertising() {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要推送的友情链接');
        return;
    }
    if (confirm("确实要推送这些友情链接到远程吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/advertising/pushAdvertising',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "推送成功",true);
            } else {
                $().toastmessage('showErrorToast', "推送失败",true);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}