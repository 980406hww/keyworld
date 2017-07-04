function checkinput() {
	var keyword = document.getElementById("keyword");
	keyword.value = trim(keyword.value);
	if (keyword.value == "") {
		alert("没有输入关键字");
		keyword.focus();
		return false;
	}

	var url = document.getElementById("url");
	url.value = trim(url.value.toLowerCase());
	if (url.value == "") {
		alert("请输入您网站的域名");
		url.focus();
		return false;
	}

	if (url.value.indexOf("http") != -1) {
		var tmpUrl = url.value;
		var posIndex = tmpUrl.indexOf("http");
		tmpUrl = tmpUrl.substring(posIndex + 4);
		posIndex = tmpUrl.indexOf("//");
		if (posIndex != -1) {
			tmpUrl = tmpUrl.substring(posIndex + 2);
		}
		alert("域名不能含有http://,输入" + tmpUrl + "即可");
		url.focus();
		return false;
	}
	
	if (checkIsChinese(url.value) == true) {
		alert("不要在域名中输入中文字符,请仔细看，要输入的是域名，不是标题");
		url.focus();
		return false;
	}
	
	var phoneUrl = document.getElementById("phoneUrl");
	phoneUrl.value = trim(phoneUrl.value.toLowerCase());

	if (phoneUrl.value.indexOf("http") != -1) {
		var tmpUrl = phoneUrl.value;
		var posIndex = tmpUrl.indexOf("http");
		tmpUrl = tmpUrl.substring(posIndex + 4);
		posIndex = tmpUrl.indexOf("//");
		if (posIndex != -1) {
			tmpUrl = tmpUrl.substring(posIndex + 2);
		}
		alert("域名不能含有http://,输入" + tmpUrl + "即可");
		phoneUrl.focus();
		return false;
	}

	if (checkIsChinese(phoneUrl.value) == true) {
		alert("不要在域名中输入中文字符,请仔细看，要输入的是域名，不是标题");
		phoneUrl.focus();
		return false;
	}

	var positionFirstCost = document.getElementById("positionFirstCost");
	if (positionFirstCost.value != "") {
		if (isNum(positionFirstCost.value) == false) {
			alert("请输入排名第一成本金额，不要输入字母或者其他字符");
			positionFirstCost.focus();
			return false;
		}
	}


	var positionSecondCost = document.getElementById("positionSecondCost");
	if (positionSecondCost.value != "") {
		if (isNum(positionSecondCost.value) == false) {
			alert("请输入排名第二成本金额，不要输入字母或者其他字符");
			positionSecondCost.focus();
			return false;
		}		
	}


	var positionThirdCost = document.getElementById("positionThirdCost");
	if (positionThirdCost.value != "") {
		if (isNum(positionThirdCost.value) == false) {
			alert("请输入排名第三成本金额，不要输入字母或者其他字符");
			positionThirdCost.focus();
			return false;
		}
	}


	var positionFirstFee = document.getElementById("positionFirstFee");
	if (positionFirstFee.value != "") {
		if (isNum(positionFirstFee.value) == false) {
			alert("请输入排名第一报价金额，不要输入字母或者其他字符");
			positionFirstFee.focus();
			return false;
		}
	}


	var positionSecondFee = document.getElementById("positionSecondFee");
	if (positionSecondFee.value != "") {
		if (isNum(positionSecondFee.value) == false) {
			alert("请输入排名第二报价金额，不要输入字母或者其他字符");
			positionSecondFee.focus();
			return false;
		}
	}


	var positionThirdFee = document.getElementById("positionThirdFee");
	if (positionThirdFee.value != "") {
		if (isNum(positionThirdFee.value) == false) {
			alert("请输入排名第三报价金额，不要输入字母或者其他字符");
			positionThirdFee.focus();
			return false;
		}
	}


	
	
	var jisuPositionFirstFee = document.getElementById("jisuPositionFirstFee");
	if (jisuPositionFirstFee.value != "") {
		if (isNum(jisuPositionFirstFee.value) == false) {
			alert("请输入极速第一报价金额，不要输入字母或者其他字符");
			jisuPositionFirstFee.focus();
			return false;
		}
	}


	var jisuPositionSecondFee = document.getElementById("jisuPositionSecondFee");
	if (jisuPositionSecondFee.value != "") {
		if (isNum(jisuPositionSecondFee.value) == false) {
			alert("请输入极速第二报价金额，不要输入字母或者其他字符");
			jisuPositionSecondFee.focus();
			return false;
		}
	}


	var jisuPositionThirdFee = document.getElementById("jisuPositionThirdFee");
	if (jisuPositionThirdFee.value == "") {
		if (isNum(jisuPositionThirdFee.value) == false) {
			alert("请输入极速第三报价金额，不要输入字母或者其他字符");
			jisuPositionThirdFee.focus();
			return false;
		}		
	}


	
	var chupingPositionFirstFee = document.getElementById("chupingPositionFirstFee");
	if (chupingPositionFirstFee.value != "") {
		if (isNum(chupingPositionFirstFee.value) == false) {
			alert("请输入触屏第一报价金额，不要输入字母或者其他字符");
			chupingPositionFirstFee.focus();
			return false;
		}
	}


	var chupingPositionSecondFee = document.getElementById("chupingPositionSecondFee");
	if (chupingPositionSecondFee.value != "") {
		if (isNum(chupingPositionSecondFee.value) == false) {
			alert("请输入触屏第二报价金额，不要输入字母或者其他字符");
			chupingPositionSecondFee.focus();
			return false;
		}
	}


	var chupingPositionThirdFee = document.getElementById("chupingPositionThirdFee");
	if (chupingPositionThirdFee.value == "") {
		if (isNum(chupingPositionThirdFee.value) == false) {
			alert("请输入触屏第三报价金额，不要输入字母或者其他字符");
			chupingPositionThirdFee.focus();
			return false;
		}		
	}
	
	var startOptimizedTime = document.getElementById("startOptimizedTime");
	startOptimizedTime.value = trim(startOptimizedTime.value);
	if (startOptimizedTime.value != "" && !isDate(startOptimizedTime.value)) {
		alert("请输入有效的日期！");
		startOptimizedTime.focus();
		return false;
	}

	var sequence = document.getElementById("sequence");
	if (sequence.value == "") {
		if (isNum(sequence.value) == false) {
			alert("请输入排序，不要输入字母或者其他字符");
			sequence.focus();
			return false;
		}
	}

//	var effectiveFromTime = document.getElementById("effectiveFromTime");
//	effectiveFromTime.value = trim(effectiveFromTime.value);
//	if (effectiveFromTime.value != "" && !isDate(effectiveFromTime.value)) {
//		alert("请输入有效的日期！");
//		effectiveFromTime.focus();
//		return false;
//	}
//
//	var effectiveToTime = document.getElementById("effectiveToTime");
//	effectiveToTime.value = trim(effectiveToTime.value);
//	if (effectiveToTime.value != "" && !isDate(effectiveToTime.value)) {
//		alert("请输入有效的日期！");
//		effectiveToTime.focus();
//		return false;
//	}
	return true;
}

function setEffectiveToTime(){
	var effectiveFromTime = document.getElementById("effectiveFromTime");
	effectiveFromTime.value = trim(effectiveFromTime.value);
	if (effectiveFromTime.value != ""){
		var collectMethod = document.getElementById("collectMethod");
		var effectiveToTime = document.getElementById("effectiveToTime");
		switch(collectMethod.value){
			case "PerDay":
				effectiveToTime.value = tommorow(effectiveFromTime.value);
				break;
			case "PerTenDay":
				effectiveToTime.value = addDays(effectiveFromTime.value, 10);
				break;
			case "PerSevenDay":
				effectiveToTime.value = addDays(effectiveFromTime.value, 7);
				break;
			case "PerMonth":
				effectiveToTime.value = nextMonth(effectiveFromTime.value);
				break;
			default:
				break;			
		}
	}	
}

function setSecondThirdDefaultCost(){
	var positionFirstCost = document.getElementById("positionFirstCost");
	var positionSecondCost = document.getElementById("positionSecondCost");
	var positionThirdCost = document.getElementById("positionThirdCost");
	positionFirstCost.value = trim(positionFirstCost.value);
	positionSecondCost.value = trim(positionSecondCost.value);
	positionThirdCost.value = trim(positionThirdCost.value);
	if (positionFirstCost.value != "") {
		if (positionSecondCost.value == ""){
			positionSecondCost.value = positionFirstCost.value; 
		}
		
		if (positionThirdCost.value == ""){
			positionThirdCost.value = positionFirstCost.value; 
		}
	}
}

function setThirdDefaultCost(){
	var positionSecondCost = document.getElementById("positionSecondCost");
	var positionThirdCost = document.getElementById("positionThirdCost");
	positionSecondCost.value = trim(positionSecondCost.value);
	positionThirdCost.value = trim(positionThirdCost.value);
	if (positionSecondCost.value != "") {		
		if (positionThirdCost.value == ""){
			positionThirdCost.value = positionSecondCost.value; 
		}
	}
}

function setSecondThirdDefaultFee(){
	var positionFirstFee = document.getElementById("positionFirstFee");
	var positionSecondFee = document.getElementById("positionSecondFee");
	var positionThirdFee = document.getElementById("positionThirdFee");
	positionFirstFee.value = trim(positionFirstFee.value);
	positionSecondFee.value = trim(positionSecondFee.value);
	positionThirdFee.value = trim(positionThirdFee.value);
	if (positionFirstFee.value != "") {
		if (positionSecondFee.value == ""){
			positionSecondFee.value = positionFirstFee.value; 
		}
		
		if (positionThirdFee.value == ""){
			positionThirdFee.value = positionFirstFee.value; 
		}
	}
}

function setThirdDefaultFee(){
	var positionSecondFee = document.getElementById("positionSecondFee");
	var positionThirdFee = document.getElementById("positionThirdFee");
	positionSecondFee.value = trim(positionSecondFee.value);
	positionThirdFee.value = trim(positionThirdFee.value);
	if (positionSecondFee.value != "") {		
		if (positionThirdFee.value == ""){
			positionThirdFee.value = positionSecondFee.value; 
		}
	}
}

function showMoreCost(){
	var secondCost = document.getElementById("secondCost");	
	var thirdCost = document.getElementById("thirdCost");
	var forthCost = document.getElementById("forthCost");
	var fifthCost = document.getElementById("fifthCost");
	if (costShowHideLabel.innerHTML == "隐藏"){
		secondCost.className = "hiddentr";
		thirdCost.className = "hiddentr";
		forthCost.className = "hiddentr";
		fifthCost.className = "hiddentr";
		
		costShowHideLabel.innerHTML = "更多";
	}else{
		secondCost.className = "displaytr";
		thirdCost.className = "displaytr";
		forthCost.className = "displaytr";
		fifthCost.className = "displaytr";
		costShowHideLabel.innerHTML = "隐藏";		
	}
}

function showMoreFee(){
	var secondFee = document.getElementById("secondFee");	
	var thirdFee = document.getElementById("thirdFee");
	var forthFee = document.getElementById("forthFee");
	var fifthFee = document.getElementById("fifthFee");
	var firstPageFee = document.getElementById("firstPageFee");
	if (feeShowHideLabel.innerHTML == "隐藏"){
		secondFee.className = "hiddentr";
		thirdFee.className = "hiddentr";
		forthFee.className = "hiddentr";
		fifthFee.className = "hiddentr";
		firstPageFee.className = "hiddentr";
		feeShowHideLabel.innerHTML = "更多";
	}else{
		secondFee.className = "displaytr";
		thirdFee.className = "displaytr";
		forthFee.className = "displaytr";
		fifthFee.className = "displaytr";
		firstPageFee.className = "displaytr";
		feeShowHideLabel.innerHTML = "隐藏";		
	}
}




function setSecondThirdDefaultJisuFee(){
	var jisuPositionFirstFee = document.getElementById("jisuPositionFirstFee");
	var jisuPositionSecondFee = document.getElementById("jisuPositionSecondFee");
	var jisuPositionThirdFee = document.getElementById("jisuPositionThirdFee");
	jisuPositionFirstFee.value = trim(jisuPositionFirstFee.value);
	jisuPositionSecondFee.value = trim(jisuPositionSecondFee.value);
	jisuPositionThirdFee.value = trim(jisuPositionThirdFee.value);
	if (jisuPositionFirstFee.value != "") {
		if (jisuPositionSecondFee.value == ""){
			jisuPositionSecondFee.value = jisuPositionFirstFee.value; 
		}
		
		if (jisuPositionThirdFee.value == ""){
			jisuPositionThirdFee.value = jisuPositionFirstFee.value; 
		}
	}
}

function setThirdDefaultJisuFee(){
	var jisuPositionSecondFee = document.getElementById("jisuPositionSecondFee");
	var jisuPositionThirdFee = document.getElementById("jisuPositionThirdFee");
	jisuPositionSecondFee.value = trim(jisuPositionSecondFee.value);
	jisuPositionThirdFee.value = trim(jisuPositionThirdFee.value);
	if (jisuPositionSecondFee.value != "") {		
		if (jisuPositionThirdFee.value == ""){
			jisuPositionThirdFee.value = jisuPositionSecondFee.value; 
		}
	}
}

function showMoreJisuFee(){
	var jisuSecondFee = document.getElementById("jisuSecondFee");	
	var jisuThirdFee = document.getElementById("jisuThirdFee");
	var jisuForthFee = document.getElementById("jisuForthFee");
	var jisuFifthFee = document.getElementById("jisuFifthFee");
	var jisuFirstPageFee = document.getElementById("jisuFirstPageFee");
	if (jisuFeeShowHideLabel.innerHTML == "隐藏"){
		jisuSecondFee.className = "hiddentr";
		jisuThirdFee.className = "hiddentr";
		jisuForthFee.className = "hiddentr";
		jisuFifthFee.className = "hiddentr";
		jisuFirstPageFee.className = "hiddentr";
		jisuFeeShowHideLabel.innerHTML = "更多";
	}else{
		jisuSecondFee.className = "displaytr";
		jisuThirdFee.className = "displaytr";
		jisuForthFee.className = "displaytr";
		jisuFifthFee.className = "displaytr";
		jisuFirstPageFee.className = "displaytr";
		jisuFeeShowHideLabel.innerHTML = "隐藏";		
	}
}


function setSecondThirdDefaultChupingFee(){
	var chupingPositionFirstFee = document.getElementById("chupingPositionFirstFee");
	var chupingPositionSecondFee = document.getElementById("chupingPositionSecondFee");
	var chupingPositionThirdFee = document.getElementById("chupingPositionThirdFee");
	chupingPositionFirstFee.value = trim(chupingPositionFirstFee.value);
	chupingPositionSecondFee.value = trim(chupingPositionSecondFee.value);
	chupingPositionThirdFee.value = trim(chupingPositionThirdFee.value);
	if (chupingPositionFirstFee.value != "") {
		if (chupingPositionSecondFee.value == ""){
			chupingPositionSecondFee.value = chupingPositionFirstFee.value; 
		}
		
		if (chupingPositionThirdFee.value == ""){
			chupingPositionThirdFee.value = chupingPositionFirstFee.value; 
		}
	}
}

function setThirdDefaultChupingFee(){
	var chupingPositionSecondFee = document.getElementById("chupingPositionSecondFee");
	var chupingPositionThirdFee = document.getElementById("chupingPositionThirdFee");
	chupingPositionSecondFee.value = trim(chupingPositionSecondFee.value);
	chupingPositionThirdFee.value = trim(chupingPositionThirdFee.value);
	if (chupingPositionSecondFee.value != "") {		
		if (chupingPositionThirdFee.value == ""){
			chupingPositionThirdFee.value = chupingPositionSecondFee.value; 
		}
	}
}

function showMoreChupingFee(){
	var chupingSecondFee = document.getElementById("chupingSecondFee");	
	var chupingThirdFee = document.getElementById("chupingThirdFee");
	var chupingForthFee = document.getElementById("chupingForthFee");
	var chupingFifthFee = document.getElementById("chupingFifthFee");
	var chupingFirstPageFee = document.getElementById("chupingFirstPageFee");
	if (chupingFeeShowHideLabel.innerHTML == "隐藏"){
		chupingSecondFee.className = "hiddentr";
		chupingThirdFee.className = "hiddentr";
		chupingForthFee.className = "hiddentr";
		chupingFifthFee.className = "hiddentr";
		chupingFirstPageFee.className = "hiddentr";
		chupingFeeShowHideLabel.innerHTML = "更多";
	}else{
		chupingSecondFee.className = "displaytr";
		chupingThirdFee.className = "displaytr";
		chupingForthFee.className = "displaytr";
		chupingFifthFee.className = "displaytr";
		chupingFirstPageFee.className = "displaytr";
		chupingFeeShowHideLabel.innerHTML = "隐藏";		
	}
}

function searchEngineChanged(){
	var searchEngine = document.getElementById("searchEngine");	
	if (searchEngine.value == "搜狗") {
		chooseSogouDefaultServiceProvider();
		setDefaultFeeForSogou();
		setDefaultCostForSogou();
	}
}

function chooseSogouDefaultServiceProvider() {
	var searchEngine = document.getElementById("searchEngine");
	var serviceprovider = document.getElementById("serviceprovider");
	
	if (searchEngine.value == "搜狗") {
		jsSelectItemByValue(serviceprovider, "baidutop123");
	}
}

function setDefaultFeeForSogou(){
	var positionFirstFee =  document.getElementById("positionFirstFee");
	var positionSecondFee = document.getElementById("positionSecondFee");	
	var positionThirdFee = document.getElementById("positionThirdFee");
	if (positionFirstFee.value == ""){
		positionFirstFee.value = "60";
	}
	if (positionSecondFee.value == ""){
		positionSecondFee.value = "60";
	}
	if (positionThirdFee.value == ""){
		positionThirdFee.value = "60";
	}
}

function setDefaultCostForSogou(){
	var positionFirstCost =  document.getElementById("positionFirstCost");
	var positionSecondCost = document.getElementById("positionSecondCost");	
	var positionThirdCost = document.getElementById("positionThirdCost");
	if (positionFirstCost.value == ""){
		positionFirstCost.value = "0";
	}
	if (positionSecondCost.value == ""){
		positionSecondCost.value = "0";
	}
	if (positionThirdCost.value == ""){
		positionThirdCost.value = "0";
	}
}
