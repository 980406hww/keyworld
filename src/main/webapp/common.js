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
