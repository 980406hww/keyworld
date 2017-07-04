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

function operateDate() {
	alert('test');
	var date = new Date();
	date.setMonth(date.getMonth() + 12);
	alert(formatDate(date, 'yyyy-MM-dd'));
}

function tommorow(str) {
	var date = new Date(str);
	date.setDate(date.getDate() + 1);
	return formatDate(date, 'yyyy-MM-dd');
}

function addDays(str, n) {
	var date = new Date(str);
	date.setDate(date.getDate() + n);
	return formatDate(date, 'yyyy-MM-dd');
}

function nextMonth(str) {
	var date = new Date(str);
	date.setMonth(date.getMonth() + 1);
	return formatDate(date, 'yyyy-MM-dd');
}

function today() {
	var date = new Date();
	return formatDate(date, 'yyyy-MM-dd');
}

function formatDate(date, str) {
	str = str.replace("yyyy", date.getFullYear());
	str = str.replace("MM", GetFullMonth(date));
	str = str.replace("dd", GetFullDate(date));
	return str;
}

// 返回月份（修正为两位数）
function GetFullMonth(date) {
	var v = date.getMonth() + 1;
	if (v > 9)
		return v.toString();
	return "0" + v;
}

// 返回日 （修正为两位数）
function GetFullDate(date) {
	var v = date.getDate();
	if (v > 9)
		return v.toString();
	return "0" + v;
}

function isStartWith(str, substr1) {
	if (str == null || str == "" || substr1 == null || substr1 == ""
			|| str.length < substr1.length)
		return false;

	if (str.substr(0, substr1.length) == substr1) {
		return true;
	} else {
		return false;
	}
}

function isNum(s) {
	return !isNaN(s);
}

function isDigit(s) {
	var r, re;
	re = /\d*/i;
	r = s.match(re);
	return (r == s) ? true : false;
}

function isEmail(s){
	var regEmail = /^[\w]+(\.[\w]+)*@[\w]+(\.[\w]+)+$/;
	return regEmail.test(s);
}
	
function isDate(s) {
	var r, re;
	re = /2[0-9]{3}-[0-9]{2}-[0-9]{2}/i;
	r = s.match(re);
	return (r == s) ? true : false;
}

function checkIsChinese(str) {
	var index = escape(str).indexOf("%u");
	if (index < 0) {
		return false;
	} else {
		return true
	}
}

function trim(val) {
	var re = /\s*((\S+\s*)*)/;
	return val.replace(re, "$1");
}

function createXMLHttpRequest() {
	// 表示当前浏览器不是ie，如firefox
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		return new ActiveXObject("Microsoft.XMLHttp");
	}
}

function jsSelectItemByValue(objSelect, objItemText) {
	for ( var i = 0; i < objSelect.options.length; i++) {
		if (objSelect.options[i].value == objItemText) {
			objSelect.options[i].selected = true;
			break;
		}
	}
}
