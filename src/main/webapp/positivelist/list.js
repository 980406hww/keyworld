$(function () {
    $("#positiveListDialog").dialog("close");
    $("#showPositiveListDiv").css("margin-top",$("#topDiv").height());
    alignTableHeader();
    window.onresize = function(){
        alignTableHeader();
    }
    var positiveListBottomDiv = $('#positiveListBottomDiv');
    var pageSize = positiveListBottomDiv.find('#pageSizeHidden').val();
    positiveListBottomDiv.find('#chooseRecords').val(pageSize);
    var pageCount = positiveListBottomDiv.find('#pageCountHidden').val();
    positiveListBottomDiv.find('#pageCountHidden').val(pageCount);
    var currentPage = positiveListBottomDiv.find('#currentPageHidden').val();
    positiveListBottomDiv.find('#currentPageHidden').val(currentPage);
    if(parseInt(currentPage) > 1 && parseInt(currentPage) < parseInt(pageCount)) {
        positiveListBottomDiv.find("#firstButton").removeAttr("disabled");
        positiveListBottomDiv.find("#upButton").removeAttr("disabled");
        positiveListBottomDiv.find("#nextButton").removeAttr("disabled");
        positiveListBottomDiv.find("#lastButton").removeAttr("disabled");
    } else if (parseInt(pageCount) <= 1) {
        positiveListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        positiveListBottomDiv.find("#upButton").attr("disabled", "disabled");
        positiveListBottomDiv.find("#nextButton").attr("disabled", "disabled");
        positiveListBottomDiv.find("#lastButton").attr("disabled", "disabled");
    } else if (parseInt(currentPage) <= 1) {
        positiveListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        positiveListBottomDiv.find("#upButton").attr("disabled", "disabled");
    } else {
        positiveListBottomDiv.find("#nextButton").attr("disabled", "disabled");
        positiveListBottomDiv.find("#lastButton").attr("disabled", "disabled");
    }
});
function alignTableHeader(){
    var td = $("#showPositiveListTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function changePaging(currentPageNumber, pageSize) {
    var searchPositiveListForm = $("#searchPositiveListForm");
    searchPositiveListForm.find("#totalHidden").val();
    searchPositiveListForm.find("#currentPageNumberHidden").val(currentPageNumber);
    searchPositiveListForm.find("#pageSizeHidden").val(pageSize);
    searchPositiveListForm.submit();
}
function resetPageNumber() {
    var searchPositiveListForm = $("#searchPositiveListForm");
    var keyword = searchPositiveListForm.find("#keyword").val();
    var title = searchPositiveListForm.find("#title").val();
    var url = searchPositiveListForm.find("#url").val();
    if(keyword != "") {
        searchPositiveListForm.find("#keyword").val($.trim(keyword));
    }
    if(title != "") {
        searchPositiveListForm.find("#title").val($.trim(title));
    }
    if(url != "") {
        searchPositiveListForm.find("#url").val($.trim(url));
    }
    searchPositiveListForm.find("#currentPageNumberHidden").val(1);
}
function editPositiveList(uuid) {
    $.ajax({
        url: '/internal/positivelist/getPositiveList/' + uuid,
        type: 'Get',
        success: function (positiveList) {
            if (positiveList != null) {
                var positiveListForm = $("#positiveListForm");
                positiveListForm.find("#keyword").val(positiveList.keyword);
                positiveListForm.find("#title").val(positiveList.title);
                positiveListForm.find("#url").val(positiveList.url);
                positiveListForm.find("#originalUrl").val(positiveList.originalUrl);
                positiveListForm.find("#desc").val(positiveList.desc);
                positiveListForm.find("#position").val(positiveList.position);
                showPositiveListDialog(positiveList.uuid);
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
function deletePositiveList(uuid) {
    if (confirm("确定要删除这条信息吗?") == false) return;
    $.ajax({
        url: '/internal/positivelist/deletePositiveList/' + uuid,
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
function deletePositives(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的优质词');
        return;
    }
    if (confirm("确定要删除这些优质词吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/positivelist/deletePositiveLists',
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
function savePositiveList(uuid) {
    var positiveListObj = {};
    positiveListObj.uuid = uuid;
    positiveListObj.keyword = $("#positiveListForm").find("#keyword").val().trim();
    positiveListObj.title = $("#positiveListForm").find("#title").val().trim();
    positiveListObj.url = $("#positiveListForm").find("#url").val().trim();
    positiveListObj.originalUrl = $("#positiveListForm").find("#originalUrl").val().trim();
    positiveListObj.desc = $("#positiveListForm").find("#desc").val();
    positiveListObj.position = $("#positiveListForm").find("#position").val();
    if (!(/^[0-9]*$/.test(positiveListObj.position)) && (positiveListObj.position != '')) {
        alert("排名输入非法!");
        return;
    }
    $.ajax({
        url: '/internal/positivelist/savePositiveList',
        data: JSON.stringify(positiveListObj),
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
function showPositiveListDialog(uuid) {
    if (uuid == null) {
        $('#positiveListForm')[0].reset();
    }
    $("#positiveListDialog").show();
    $("#positiveListDialog").dialog({
        resizable: false,
        width: 490,
        height: 290,
        modal: true,
        title: '优质信息',
        closed:true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                savePositiveList(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#positiveListForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#positiveListDialog').dialog("close");
                    $('#positiveListForm')[0].reset();
                }
            }]
    });
    $('#positiveListDialog').dialog("open");
    $('#positiveListDialog').window("resize",{top:$(document).scrollTop() + 100});
}