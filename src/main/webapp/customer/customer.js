function checkinput() {
	var contactPerson = document.getElementById("contactPerson");
	if (trim(contactPerson.value) == "") {
		alert("没有输入联系人");
		contactPerson.focus();
		return false;
	}

	contactPerson.value = trim(contactPerson.value);

	var qq = document.getElementById("qq");
	qq.value = trim(qq.value);
	if (qq.value != "") {
		if (isDigit(qq.value) == false) {
			alert("无效的QQ号码！");
			qq.focus();
			return false;
		}
	}
	
	//var qqExisting = document.getElementById("qqExisting");
	//var qqExistingClassName = qqExisting.className;
	//if(qqExistingClassName == "displaytr"){
	//	alert("该QQ系统已经存在！");
	//	return false;
	//}
	//var telphoneExisting = document.getElementById("telphoneExisting");
	//var telphoneExistingClassName = telphoneExisting.className;
	//if(telphoneExistingClassName == "displaytr"){
	//	alert("该电话系统已经存在！");
	//	return false;
	//}
//	var paidFee = document.getElementById("paidFee");
//	if (paidFee.value != "") {
//		if (isNum(paidFee.value) == false) {
//			alert("金额请用阿拉伯数字！");
//			paidFee.focus();
//			return false;
//		}
//	}
}
function checkTelphoneExisting(){
	var telphone = document.getElementById("telphone");
	
	var telphoneExisting = document.getElementById("telphoneExisting");
	if (trim(telphone.value).length != 0) {
		// 创建XMLHttpRequest
		var xmlHttp = createXMLHttpRequest();
		var url = "checkCustomerExisting.jsp?telphone=" + trim(telphone.value);
		xmlHttp.open("GET", url, true);

		// 方法地址。处理完成后自动调用，回调。一定不要加括号，否则就变成调用了
		// xmlHttp.onreadystatechange = callback;
		xmlHttp.onreadystatechange = function() { // 匿名函数
			if (xmlHttp.readyState == 4) { // ajax引擎初始化成功
				if (xmlHttp.status == 200) { // Http协议成功
					if (xmlHttp.responseText.indexOf("Existing") >= 0){
						telphoneExisting.className = "displaytr";						
					}else{
						telphoneExisting.className = "hiddentr";
					}
				} else {
					alert("请求失败，错误码=" + xmlHttp.status);
				}
			}
		};
		// 将参数发送到ajax引擎,不是执行
		xmlHttp.send(null);
	} else {
		telphoneExisting.setAttribute("class", "hiddentr");
	}	
}

function checkQQExisting() {
	var qq = document.getElementById("qq");
	
	var qqExisting = document.getElementById("qqExisting");
	if (trim(qq.value).length != 0) {
		// 创建XMLHttpRequest
		var xmlHttp = createXMLHttpRequest();
		var url = "checkCustomerExisting.jsp?qq=" + trim(qq.value);
		xmlHttp.open("GET", url, true);

		// 方法地址。处理完成后自动调用，回调。一定不要加括号，否则就变成调用了
		// xmlHttp.onreadystatechange = callback;
		xmlHttp.onreadystatechange = function() { // 匿名函数
			if (xmlHttp.readyState == 4) { // ajax引擎初始化成功
				if (xmlHttp.status == 200) { // Http协议成功
					if (xmlHttp.responseText.indexOf("Existing") >= 0){
						qqExisting.className = "displaytr";						
					}else{
						qqExisting.className = "hiddentr";
					}
				} else {
					alert("请求失败，错误码=" + xmlHttp.status);
				}
			}
		};
		// 将参数发送到ajax引擎,不是执行
		xmlHttp.send(null);
	} else {
		qqExisting.setAttribute("class", "hiddentr");
	}
}