$(function () {
    $("#showUserMessageListDialog").dialog("close");
    $("#showUserMessageDialog").dialog("close");
    $("#message_status_select").multiselect({
        header: false,
        noneSelectedText: "请选择",
        minWidth: 100,
        height: 100,
        selectedList: 2
    });
    checkMessageInbox();
});
function doOver(obj) {
	obj.style.backgroundColor = "green";
}

function doOut(obj) {
	var rowIndex = obj.rowIndex;
	if ((rowIndex % 2) == 0) {
		obj.style.backgroundColor = "#eeeeee";
	} else {
		obj.style.backgroundColor = "#ffffff";
	}
}
function checkMessageInbox() {
    var text = $("#userMessageText").text();
    $.ajax({
        url: '/internal/usermessage/checkMessageInbox',
        type: 'POST',
        headers: {
            "Accept": 'application/json',
            "Content-Type": 'application/json'
        },
        success: function (result) {
            $("#userMessageText").text(text + "(" + result + ")");
        },
        error: function () {
            $().toastmessage("showErrorToast", "获取信息量失败");
        }
    });
}
