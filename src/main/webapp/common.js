$(function () {
    $("#showUserMessageQueueDialog").dialog("close");
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
            var bgColor = 1;
            if (result == 0) {
                bgColor = 0;
            }
            $("#userMessageText").text(text + "(" + result + ")");
            if (bgColor) {
                var userMessageText = document.getElementById("userMessageText");
                userMessageText.style.backgroundColor = "red";
            }
        },
        error: function () {
            $().toastmessage("showErrorToast", "获取信息量失败");
        }
    });
}
