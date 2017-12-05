$(function () {
    $("#negativeListDialog").dialog("close");
    $("#showNegativeListDiv").css("margin-top",$("#topDiv").height());
    alignTableHeader();
    window.onresize = function(){
        alignTableHeader();
    }
    var negativeListBottomDiv = $('#negativeListBottomDiv');
    var pageSize = negativeListBottomDiv.find('#pageSizeHidden').val();
    negativeListBottomDiv.find('#chooseRecords').val(pageSize);
    var pageCount = negativeListBottomDiv.find('#pageCountHidden').val();
    negativeListBottomDiv.find('#pageCountHidden').val(pageCount);
    var currentPage = negativeListBottomDiv.find('#currentPageHidden').val();
    negativeListBottomDiv.find('#currentPageHidden').val(currentPage);
    if(parseInt(currentPage) > 1 && parseInt(currentPage) < parseInt(pageCount)) {
        negativeListBottomDiv.find("#firstButton").removeAttr("disabled");
        negativeListBottomDiv.find("#upButton").removeAttr("disabled");
        negativeListBottomDiv.find("#nextButton").removeAttr("disabled");
        negativeListBottomDiv.find("#lastButton").removeAttr("disabled");
    } else if (parseInt(pageCount) <= 1) {
        negativeListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        negativeListBottomDiv.find("#upButton").attr("disabled", "disabled");
        negativeListBottomDiv.find("#nextButton").attr("disabled", "disabled");
        negativeListBottomDiv.find("#lastButton").attr("disabled", "disabled");
    } else if (parseInt(currentPage) <= 1) {
        negativeListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        negativeListBottomDiv.find("#upButton").attr("disabled", "disabled");
    } else {
        negativeListBottomDiv.find("#nextButton").attr("disabled", "disabled");
        negativeListBottomDiv.find("#lastButton").attr("disabled", "disabled");
    }
});
function alignTableHeader(){
    var td = $("#showNegativeListTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function changePaging(currentPageNumber, pageSize) {
    var searchNegativeListForm = $("#searchNegativeListForm");
    searchNegativeListForm.find("#totalHidden").val();
    searchNegativeListForm.find("#currentPageNumberHidden").val(currentPageNumber);
    searchNegativeListForm.find("#pageSizeHidden").val(pageSize);
    searchNegativeListForm.submit();
}

function resetPageNumber() {
    $("#searchNegativeListForm").find("#currentPageNumberHidden").val(1);
}

function editNegativeList(uuid) {
    $.ajax({
        url: '/internal/negativelist/getNegativeList/' + uuid,
        type: 'Get',
        success: function (negativeList) {
            if (negativeList != null) {
                var negativeListForm = $("#negativeListForm");
                negativeListForm.find("#terminalType").val(negativeList.terminalType);
                negativeListForm.find("#keyword").val(negativeList.keyword);
                negativeListForm.find("#title").val(negativeList.title);
                negativeListForm.find("#url").val(negativeList.url);
                negativeListForm.find("#originalUrl").val(negativeList.originalUrl);
                negativeListForm.find("#desc").val(negativeList.desc);
                negativeListForm.find("#position").val(negativeList.position);
                showNegativeListDialog(negativeList.uuid);
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

function deleteNegativeList(uuid) {
    if (confirm("确定要删除这条信息吗?") == false) return;
    $.ajax({
        url: '/internal/negativelist/deleteNegativeList/' + uuid,
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

function deleteNegatives(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要删除的负面词');
        return;
    }
    if (confirm("确定要删除这些负面词吗?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/negativelist/deleteNegativeLists',
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

function saveNegativeList(uuid) {
    var negativeListObj = {};
    negativeListObj.uuid = uuid;
    negativeListObj.keyword = $("#negativeListForm").find("#keyword").val().trim();
    negativeListObj.title = $("#negativeListForm").find("#title").val().trim();
    negativeListObj.url = $("#negativeListForm").find("#url").val().trim();
    negativeListObj.originalUrl = $("#negativeListForm").find("#originalUrl").val().trim();
    negativeListObj.desc = $("#negativeListForm").find("#desc").val();
    negativeListObj.position = $("#negativeListForm").find("#position").val();
    if (!(/^[0-9]*$/.test(negativeListObj.position)) && (negativeListObj.position != '')) {
        alert("排名输入非法!");
        return;
    }
    $.ajax({
        url: '/internal/negativelist/saveNegativeList',
        data: JSON.stringify(negativeListObj),
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

function showNegativeListDialog(uuid) {
    if (uuid == null) {
        $('#negativeListForm')[0].reset();
    }
    $("#negativeListDialog").dialog({
        resizable: false,
        width: 490,
        height: 290,
        modal: true,
        title: '负面信息',
        closed:true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveNegativeList(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#negativeListForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#negativeListDialog').dialog("close");
                    $('#negativeListForm')[0].reset();
                }
            }]
    });
    $('#negativeListDialog').dialog("open");
    $('#negativeListDialog').window("resize",{top:$(document).scrollTop() + 100});
}