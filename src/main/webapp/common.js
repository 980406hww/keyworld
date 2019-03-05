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
    $("#user_list_select").multiselect({
        header: false,
        noneSelectedText: "请选择",
        minWidth: 100,
        height: 100,
        selectedList: 2
    });
    $("#user_select").multiselect({
        header: false,
        noneSelectedText: "请选择",
        minWidth: 140,
        height: 100,
        selectedList: 3
    });
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
