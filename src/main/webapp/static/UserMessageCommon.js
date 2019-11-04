$(function () {
    $("#showUserNoteBookDialog").dialog("close");
    var showUserMessageDialog = $("#showUserMessageDialog");
    var openDialogStatus =  showUserMessageDialog.find("input[name='openDialogStatus']").val();
    if (openDialogStatus === 1) {
        $("#customerKeywordBtnInput").click();
    } else if (openDialogStatus === 2) {
    }
});

function openNoteBookDialog(customerUuid, terminalType) {
    var showUserNoteBookDialog = $("#showUserNoteBookDialog");
    getUserNoteBooks(customerUuid, 0, terminalType);
    $("#userNoteBookDialogToolBar").find("input[name='customerUuid']").val(customerUuid);
    $("#userNoteBookDialogToolBar").find("input[name='terminalType']").val(terminalType);
    showUserNoteBookDialog.show();
    showUserNoteBookDialog.dialog({
        resizable: false,
        bgiframe: true,
        draggable: false,
        height: 300,
        width: 560,
        title: '记事本',
        modal: false,
        onClose: function() {
            $("#showUserNoteBookForm")[0].reset();
            $("#userNoteBookTable tr:not(:first)").remove();
            $("#showUserNoteBookDialog").find("#addUserNote").css("display", "none");
        }
    });
    showUserNoteBookDialog.dialog("open");
    showUserNoteBookDialog.window("resize", {
        top: $(document).scrollTop() + 150,
        left: $(document).scrollLeft() + $(window).width() / 2 - 280
    });
}

function getUserNoteBooks(customerUuid, searchAll, terminalType) {
    $("#showUserNoteBookDialog").find("#addUserNote").css("display", "none");
    $("#userNoteBookTable tr:not(:first)").remove();
    var postData = {};
    postData.customerUuid = customerUuid;
    postData.searchAll = searchAll;
    postData.terminalType = terminalType;
    $.ajax({
        url: '/internal/usernotebook/searchUserNoteBooks',
        type: 'POST',
        headers: {
            "Accept": 'application/json',
            "Content-Type": 'application/json'
        },
        data: JSON.stringify(postData),
        success: function (data) {
            if (data != null) {
                $.each(data, function(idx, val){
                    $("#userNoteBookTable tbody").append("<tr>" +
                        "<td name='index'>"+ (idx+1) +"</td>" +
                        "<td>"+ val.notesPerson +"</td>" +
                        "<td>"+ val.content +"</td>" +
                        "<td>"+ new Date(val.createTime).format("yyyy-MM-dd") +"</td>" +
                        "<td>" +
                        "<input class='ui-button ui-widget ui-corner-all' type='button' onclick='updateUserNoteBook(this)' idx='"+ (idx+1) +"' uuid='"+ val.uuid +"' value='修改内容' >" +
                        "</td>" +
                    "</tr>" +
                    "<tr id='note_book_"+ (idx+1) +"' style='display: none'>" +
                        "<td colspan='4'>" +
                        "<p>"+ val.content +"</p>" +
                        "</td>" +
                    "</tr>");
                });
            }
        },
        error: function () {
            $().toastmessage("showErrorToast", "查询失败");
        }
    });
}

function showAddUserNoteDiv() {
    var uuid = $("#addUserNote").find("input[name='userNoteBookUuid']").val();
    if (uuid !== "") {
        $("#addUserNote").find("input[name='userNoteBookUuid']").val("");
        $("#addUserNote").find("textarea").val("");
        $("#showUserNoteBookDialog").find("#addUserNote").css("display", "block");
    } else {
        $("#showUserNoteBookDialog").find("#addUserNote").toggle();
    }
}

function saveUserNoteBook() {
    var postData = {};
    var customerUuid = $("#userNoteBookDialogToolBar").find("input[name='customerUuid']").val();
    postData.customerUuid = customerUuid;
    var terminalType = $("#userNoteBookDialogToolBar").find("input[name='terminalType']").val();
    postData.terminalType = terminalType;
    var uuid = $("#addUserNoteTable").find("input[name='userNoteBookUuid']").val();
    if (uuid === "") {
        uuid = null;
    }
    postData.uuid = uuid;
    var content = $("#addUserNoteTable").find("textarea").val();
    if (content === '') {
        alert("请输入内容");
        return;
    }
    content = content.replace(/[\r\n]/g, '');
    postData.content = content;
    $.ajax({
        url: '/internal/usernotebook/saveUserNoteBook',
        type: 'POST',
        headers: {
            "Accept": 'application/json',
            "Content-Type": 'application/json'
        },
        data: JSON.stringify(postData),
        success: function (data) {
            if (data) {
                $().toastmessage("showSuccessToast", "保存成功");
            } else {
                $().toastmessage("showErrorToast", "保存失败");
            }
        },
        error: function () {
            $().toastmessage("showErrorToast", "保存失败");
        }
    });
    $("#showUserNoteBookDialog").find("#addUserNote").css("display", "none");
    $("#showUserNoteBookForm")[0].reset();
    setTimeout(function () {
        getUserNoteBooks(customerUuid, 0, terminalType);
    }, 300);
}

function searchUserNoteBooks(searchAll) {
    var customerUuid = $("#userNoteBookDialogToolBar").find("input[name='customerUuid']").val();
    var terminalType = $("#userNoteBookDialogToolBar").find("input[name='terminalType']").val();
    getUserNoteBooks(customerUuid, searchAll, terminalType);
}

function updateUserNoteBook(self) {
    if ($(self).val() === "修改内容") {
        var btn = $(self).parent().parent().parent().find("input[value='取消修改']");
        if (btn.length > 0) {
            btn[0].value = "修改内容";
        }
        $(self).val("取消修改");
        var idx = $(self).attr("idx");
        var uuid = $(self).attr("uuid");
        var content = $(self).parent().parent().parent().find("#note_book_" + idx).find("p")[0].innerText;
        $("#addUserNote").find("input[name='userNoteBookUuid']").val(uuid);
        $("#showUserNoteBookDialog").find("#addUserNote").css("display", "block");
        $("#addUserNote").find("textarea").val(content);
    } else  if ($(self).val() == "取消修改") {
        $(self).val("修改内容");
        $("#showUserNoteBookDialog").find("#addUserNote").css("display", "none");
        $("#showUserNoteBookForm")[0].reset();
        $("#addUserNote").find("input[name='userNoteBookUuid']").val("");
    }
}

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};