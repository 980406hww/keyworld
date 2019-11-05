$(function () {
    $("#showUserMessageQueueDialog").dialog("close");
});
function doOver(obj) {
	obj.style.backgroundColor = "#c3dfb7";
}

function doOut(obj) {
	var rowIndex = obj.rowIndex;
	if ((rowIndex % 2) == 0) {
		obj.style.backgroundColor = "#eeeeee";
	} else {
		obj.style.backgroundColor = "#ffffff";
	}
}
