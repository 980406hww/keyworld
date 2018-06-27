$(function () {
    $("#warnListDialog").dialog("close");
    $("#showWarnListDiv").css("margin-top",$("#topDiv").height());
    alignTableHeader();
    window.onresize = function(){
        alignTableHeader();
    }
    var warnListBottomDiv = $('#warnListBottomDiv');
    var pageSize = warnListBottomDiv.find('#pageSizeHidden').val();
    warnListBottomDiv.find('#chooseRecords').val(pageSize);
    var pageCount = warnListBottomDiv.find('#pageCountHidden').val();
    warnListBottomDiv.find('#pageCountHidden').val(pageCount);
    var currentPage = warnListBottomDiv.find('#currentPageHidden').val();
    warnListBottomDiv.find('#currentPageHidden').val(currentPage);
    if(parseInt(currentPage) > 1 && parseInt(currentPage) < parseInt(pageCount)) {
        warnListBottomDiv.find("#firstButton").removeAttr("disabled");
        warnListBottomDiv.find("#upButton").removeAttr("disabled");
        warnListBottomDiv.find("#nextButton").removeAttr("disabled");
        warnListBottomDiv.find("#lastButton").removeAttr("disabled");
    } else if (parseInt(pageCount) <= 1) {
        warnListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        warnListBottomDiv.find("#upButton").attr("disabled", "disabled");
        warnListBottomDiv.find("#nextButton").attr("disabled", "disabled");
        warnListBottomDiv.find("#lastButton").attr("disabled", "disabled");
    } else if (parseInt(currentPage) <= 1) {
        warnListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        warnListBottomDiv.find("#upButton").attr("disabled", "disabled");
    } else {
        warnListBottomDiv.find("#nextButton").attr("disabled", "disabled");
        warnListBottomDiv.find("#lastButton").attr("disabled", "disabled");
    }
});
function alignTableHeader(){
    var td = $("#showWarnListTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function changePaging(currentPageNumber, pageSize) {
    var searchWarnListForm = $("#searchWarnListForm");
    searchWarnListForm.find("#totalHidden").val();
    searchWarnListForm.find("#currentPageNumberHidden").val(currentPageNumber);
    searchWarnListForm.find("#pageSizeHidden").val(pageSize);
    searchWarnListForm.submit();
}
function resetPageNumber() {
    var searchWarnListForm = $("#searchWarnListForm");
    var keyword = searchWarnListForm.find("#keyword").val();
    var title = searchWarnListForm.find("#title").val();
    var url = searchWarnListForm.find("#url").val();
    if(keyword != "") {
        searchWarnListForm.find("#keyword").val($.trim(keyword));
    }
    if(title != "") {
        searchWarnListForm.find("#title").val($.trim(title));
    }
    if(url != "") {
        searchWarnListForm.find("#url").val($.trim(url));
    }
    searchWarnListForm.find("#currentPageNumberHidden").val(1);
}
function editWarnList(uuid) {
    $.ajax({
        url: '/internal/warnlist/getWarnList/' + uuid,
        type: 'Get',
        success: function (warnList) {
            if (warnList != null) {
                var warnListForm = $("#warnListForm");
                warnListForm.find("#keyword").val(warnList.keyword);
                warnListForm.find("#title").val(warnList.title);
                warnListForm.find("#url").val(warnList.url);
                warnListForm.find("#originalUrl").val(warnList.originalUrl);
                warnListForm.find("#desc").val(warnList.desc);
                warnListForm.find("#position").val(warnList.position);
                showWarnListDialog(warnList.uuid);
            } else {
                $().toastmessage('showErrorToast', "获取信息失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
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
function deleteWarnList(uuid) {
    if (confirm("确定要删除这条信息吗?") == false) return;
    $.ajax({
        url: '/internal/warnlist/deleteWarnList/' + uuid,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功",true);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function deleteWarns(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的预警词');
        return;
    }
    if (confirm("确定要删除这些预警词吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/warnlist/deleteWarnLists',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "操作成功",true);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
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
function saveWarnList(uuid) {
    var warnListObj = {};
    warnListObj.uuid = uuid;
    warnListObj.keyword = $("#warnListForm").find("#keyword").val().trim();
    warnListObj.title = $("#warnListForm").find("#title").val().trim();
    warnListObj.url = $("#warnListForm").find("#url").val().trim();
    warnListObj.originalUrl = $("#warnListForm").find("#originalUrl").val().trim();
    warnListObj.desc = $("#warnListForm").find("#desc").val();
    warnListObj.position = $("#warnListForm").find("#position").val();
    if (!(/^[0-9]*$/.test(warnListObj.position)) && (warnListObj.position != '')) {
        alert("排名输入非法!");
        return;
    }
    $.ajax({
        url: '/internal/warnlist/saveWarnList',
        data: JSON.stringify(warnListObj),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功",true);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });

}
function showWarnListDialog(uuid) {
    if (uuid == null) {
        $('#warnListForm')[0].reset();
    }
    $("#warnListDialog").show();
    $("#warnListDialog").dialog({
        resizable: false,
        width: 490,
        height: 290,
        modal: true,
        title: '预警信息',
        closed:true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveWarnList(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#warnListForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#warnListDialog').dialog("close");
                    $('#warnListForm')[0].reset();
                }
            }]
    });
    $('#warnListDialog').dialog("open");
    $('#warnListDialog').window("resize",{top:$(document).scrollTop() + 100});
}