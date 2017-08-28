<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@page contentType="text/html;charset=utf-8" %>

    <script language="javascript" type="text/javascript" src="/common.js"></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script language="javascript" type="text/javascript" src="/js/slide1.12.4.js"></script>
    <link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link href="/css/menu.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        .wrap {
            word-break: break-all;
            word-wrap: break-word;
        }

        #div2 {
            display: none;
            background-color: #ACF106;
            color: #E80404;
            font-size: 20px;
            line-height: 18px;
            border: 2px solid #104454;
            width: 100px;
            height: 22px;
        }

        #showCustomerTableDiv {
            overflow: scroll;
            width: 100%;
            height: 90%;
            margin: auto;
        }

        #showCustomerBottomDiv {
            margin-right: 2%;
            float: right;
            width: 580px;
        }

        #showChargeRuleCalculationDiv input {
            font-size: 12px;
            width: 70px;
            height: 20px;
            margin-top: 10px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $("#showCustomerDialog").hide();
            $("#uploadDailyReportTemplateDialog").hide();
            $("#addCustomerKeywordDialog").hide();
            $("#showRuleDialog").hide();
            pageLoad();
            onlyNumber()
            getFoucus();
//            showLine();
        });
        function pageLoad() {
            var showCustomerBottomDiv = $('#showCustomerBottomDiv');
            var displaysRecords = showCustomerBottomDiv.find('#displaysRecordsHidden').val();
            showCustomerBottomDiv.find('#chooseRecords').val(displaysRecords);
            var pages = showCustomerBottomDiv.find('#pagesHidden').val();
            showCustomerBottomDiv.find('#pagesHidden').val(pages);
            var currentPage = showCustomerBottomDiv.find('#currentPageHidden').val();
            showCustomerBottomDiv.find('#currentPageHidden').val(currentPage);
            if (parseInt(currentPage) <= 1) {
                currentPage = 1;
                showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
            } else if (parseInt(currentPage) >= parseInt(pages)) {
                currentPage = pages;
                showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
                showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
            } else {
                showCustomerBottomDiv.find("#firstButton").removeAttr("disabled");
                showCustomerBottomDiv.find("#upButton").removeAttr("disabled");
                showCustomerBottomDiv.find("#nextButton").removeAttr("disabled");
                showCustomerBottomDiv.find("#lastButton").removeAttr("disabled");
            }
        }
        //只能输入数字
        function onlyNumber() {
            var calculationInput = $("#showChargeRuleCalculationDiv").find("input[type=text]");
            calculationInput.bind('keyup', function (k, v) {
                var obj = $(this);
                obj.val(obj.val().replace(/[^\d]*/g, ''));
            });
        }

        function showLine(obj) {
            var obj = obj;
            if (obj.checked) {
                $("#tab" + obj.id + " tr:not(:first)").remove();
                if (obj.id == "PC") {
                    addRowPC();
                } else {
                    addRowPhone();
                }
            } else {
                $("#tab" + obj.id + " tr:not(:first)").remove();
            }
        }

        function showOperationTypeDiv(obj) {
            var showChargeRuleCalculationDiv = $("#showChargeRuleCalculationDiv");
            var obj = obj;
            if (obj.checked) {
                showChargeRuleCalculationDiv.find("#chargesLT" + obj.id).fadeIn("slow");
                showChargeRuleCalculationDiv.find("#chargesGT" + obj.id).fadeIn("slow");
            }
            else {
                showChargeRuleCalculationDiv.find("#chargesLT" + obj.id).fadeOut("slow");
                showChargeRuleCalculationDiv.find("#chargesGT" + obj.id).fadeOut("slow");
            }
        }
        function hideOperationTypeDiv() {
            var showChargeRuleCalculationDiv = $("#showChargeRuleCalculationDiv");
            showChargeRuleCalculationDiv.find("#PC").attr("checked",false);
            showChargeRuleCalculationDiv.find("#Phone").attr("checked",false);
            showChargeRuleCalculationDiv.find("#chargesLTPC").hide();
            showChargeRuleCalculationDiv.find("#chargesGTPC").hide();
            showChargeRuleCalculationDiv.find("#chargesLTPhone").hide();
            showChargeRuleCalculationDiv.find("#chargesGTPhone").hide();
        }
        //
        function getFoucus() {
            var showChargeRuleCalculationDiv = $("#showChargeRuleCalculationDiv");
            var showChargeRuleIntervalDiv = $("#showChargeRuleIntervalDiv");
            var calculationInputPC = showChargeRuleCalculationDiv.find("#pcOperationTypeDiv").find("input[type=text]");
            var calculationInputPhone = showChargeRuleCalculationDiv.find("#phoneOperationTypeDiv").find("input[type=text]");
            var intervalInputPC = showChargeRuleIntervalDiv.find("#pcOperationTypeDiv").find("input[type=text]");
            var intervalInputPhone = showChargeRuleIntervalDiv.find("#phoneOperationTypeDiv").find("input[type=text]");

            calculationInputPC.focus(function () {
                showChargeRuleCalculationDiv.find("#PC").attr("checked",true);
//                $("#chargeRulePercentage").attr("checked",true);
            });

            calculationInputPhone.focus(function () {
                showChargeRuleCalculationDiv.find("#Phone").attr("checked",true);
            });

            intervalInputPC.focus(function () {
                showChargeRuleIntervalDiv.find("#PC").attr("checked",true);
            });

            intervalInputPhone.focus(function () {
                showChargeRuleIntervalDiv.find("#Phone").attr("checked",true);
            });
        }


        function selectAll(self) {
            var a = document.getElementsByName("customerUuid");
            if (self.checked) {
                for (var i = 0; i < a.length; i++) {
                    if (a[i].type == "checkbox") {
                        a[i].checked = true;
                    }
                }
            } else {
                for (var i = 0; i < a.length; i++) {
                    if (a[i].type == "checkbox") {
                        a[i].checked = false;
                    }
                }
            }
        }
        function getSelectedClientIDs() {
            var a = document.getElementsByName("customerUuid");
            var clientIDs = '';
            for (var i = 0; i < a.length; i++) {
                //alert(a[i].value);
                if (a[i].checked) {
                    if (clientIDs === '') {
                        clientIDs = a[i].value;
                    } else {
                        clientIDs = clientIDs + "," + a[i].value;
                    }
                }
            }
            return clientIDs;
        }
        function triggerDailyReportGeneration(self) {
            if (confirm("确实要生成当天报表吗?") == false) return;
            var customerUuids = getSelectedClientIDs();

            var postData = {"customerUuids": customerUuids}
            $.ajax({
                url: '/internal/dailyReport/triggerReportGeneration',
                data: JSON.stringify(postData),
                type: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function (status) {
                    if (status) {
                        showInfo("更新成功", self);
                        window.location.reload();
                    } else {
                        showInfo("更新失败", self);
                    }
                },
                error: function () {
                    showInfo("更新失败", self);
                    settingDialogDiv.hide();
                }
            });
        }
        function searchCurrentDateCompletedReports() {
            var span = $("#dailyReportSpan");
            $.ajax({
                url: '/internal/dailyReport/searchCurrentDateCompletedReports',
                type: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function (dailyReports) {
                    var htmlContent = "";
                    if (dailyReports) {
                        $.each(dailyReports, function (idx, val) {
                            var date = new Date(val.completeTime);
                            htmlContent = htmlContent + '  <a href="' + val.reportPath + '">下载(' + ((date.getHours() < 10) ? '0'
                                    + date.getHours() : date.getHours()) + ':' + ((date.getMinutes() < 10) ? '0' + date.getMinutes()
                                    : date.getMinutes()) + ')</a>';
                        });
                    } else {
                        htmlContent = "今天没报";
                    }
                    span.html(htmlContent);
                },
                error: function () {
                    span.html("获取报表清单异常");
                }
            });
        }
        function showInfo(content, e) {
            e = e || window.event;
            var div1 = document.getElementById('div2'); //将要弹出的层
            div1.innerText = content;
            div1.style.display = "block"; //div1初始状态是不可见的，设置可为可"
            div1.style.left = getLeft(e) + 10; //鼠标目前在X轴上的位置，"0是为了向右边移动10个px方便看到内容
            div1.style.top = getTop(e) + 5;
            div1.style.position = "absolute";

            var intervalID = setInterval(function () {
                div1.style.display = "none";
            }, 3000);
        }

        function getTop(e) {
            var offset = e.offsetTop;
            if (e.offsetParent != null) offset += getTop(e.offsetParent);
            return offset;
        }
        //获取元素的横坐标
        function getLeft(e) {
            var offset = e.offsetLeft;
            if (e.offsetParent != null) offset += getLeft(e.offsetParent);
            return offset;
        }

        function delItem(uuid) {
            if (confirm("确实要删除这个客户吗?") == false) return;
            document.location = "delcustomer.jsp?uuid=" + uuid;
        }

        //删除所"
        function deleteCustomers(self) {
            var uuids = getSelectedIDs();
            if (uuids === '') {
                alert('请选择要操作的设置信息');
                return;
            }
            if (confirm("确实要删除这些客户吗?") == false) return;
            var postData = {};
            postData.uuids = uuids.split(",");
            $.ajax({
                url: '/internal/customer/deleteCustomers',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        showInfo("操作成功", self);
                        window.location.reload();
                    } else {
                        showInfo("操作失败", self);
                        window.location.reload();
                    }
                },
                error: function () {
                    showInfo("操作失败", self);
                    window.location.reload();
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
        //规则部分
        function addRule(uuid) {
            $.ajax({
                url: '/internal/customerChargeType/getCustomerChargeType/' + uuid,
                type: 'Get',
                success: function (customerChargeType) {
                    $("#tabPC tr:not(:first)").remove();
                    $("#tabPhone tr:not(:first)").remove();
                    //$("#showRuleDialog").find("#customerChargeTypeUuid").val("");
                    if (customerChargeType != null && customerChargeType != "") {
                        alert(JSON.stringify(customerChargeType));
                        initCustomerChargeTypeDiv(customerChargeType);
                        $("#showRuleDialog").dialog({
                            resizable: false,
                            width: 500,
                            height: 400,
                            modal: true,
                            //按钮
                            buttons: {
                                "保存": function () {
                                    saveCustomerChargeType(customerChargeType.customerUuid);
                                },
                                "清空": function () {
                                    $('#showRuleForm')[0].reset();
                                },
                                "取消": function () {
                                    $(this).dialog("close");
                                    $('#showRuleForm')[0].reset();
                                }
                            }
                        });
                    } else {
                        $('#showRuleForm')[0].reset();
                        showAddRuleDialog(uuid);
                    }
                },
                error: function () {
                    showInfo("获取信息失败", self);
                }
            });
        }

        function showAddRuleDialog(uuid) {
            chargeRulePercentage();
            addRowPC();
            addRowPhone();
//            hideOperationTypeDiv();
            $("#showRuleDialog").dialog({
                resizable: false,
                width: 500,
                height: 400,
                modal: true,
                //按钮
                buttons: {
                    "保存": function () {
                        $("#showRuleDialog").find("#customerChargeTypeUuid").val(null);
                        saveCustomerChargeType(uuid);
                    },
                    "清空": function () {
                        $('#showRuleForm')[0].reset();
                    },
                    "取消": function () {
                        $(this).dialog("close");
                        $('#showRuleForm')[0].reset();
                    }
                }
            });
        }

        function saveCustomerChargeType(uuid) {
            var showChargeRuleCalculationDiv = $("#showChargeRuleCalculationDiv");
            var showChargeRuleIntervalDiv = $("#showChargeRuleIntervalDiv");
            var customerChargeType = {};
            customerChargeType.uuid = $("#showRuleDialog").find("#customerChargeTypeUuid").val();
            customerChargeType.customerUuid = uuid;
            customerChargeType.chargeType = $("#showRuleRadioDiv input:radio:checked").val();
            var switchval = $("#showRuleRadioDiv input:radio:checked").val();
            var validationFlag = true;
            if (switchval == "Percentage") {
                var checkedObjs = showChargeRuleCalculationDiv.find("input[name=operationType]:checkbox:checked");
                if (checkedObjs.length == 0) {
                    alert("请选择一个操作类型电脑或手机");
                    validationFlag = false;
                    return;
                }
                customerChargeType.customerChargeTypeCalculations = [];
                $.each(checkedObjs, function (idx, val) {
                    var customerChargeTypeCalculationLT = {};
                    customerChargeTypeCalculationLT.uuid = showChargeRuleCalculationDiv.find("#chargeRuleCalculationUuid" + val.id).val();
                    customerChargeTypeCalculationLT.chargeDataType = "LessThanHundred";
                    customerChargeTypeCalculationLT.operationType = val.id;
                    customerChargeTypeCalculationLT.chargesOfFirst = showChargeRuleCalculationDiv.find("#chargesOfFirstLT" + val.id).val();
                    customerChargeTypeCalculationLT.chargesOfSecond = showChargeRuleCalculationDiv.find("#chargesOfSecondLT" + val.id).val();
                    customerChargeTypeCalculationLT.chargesOfThird = showChargeRuleCalculationDiv.find("#chargesOfThirdLT" + val.id).val();
                    customerChargeTypeCalculationLT.chargesOfFourth = showChargeRuleCalculationDiv.find("#chargesOfFourthLT" + val.id).val();
                    customerChargeTypeCalculationLT.chargesOfFifth = showChargeRuleCalculationDiv.find("#chargesOfFifthLT" + val.id).val();
                    customerChargeTypeCalculationLT.chargesOfFirstPage = showChargeRuleCalculationDiv.find("#chargesOfFirstPageLT" + val.id).val();
                    customerChargeTypeCalculationLT.uuid = showChargeRuleCalculationDiv.find("#chargeRuleCalculationUuid" + val.id).val();
                    customerChargeType.customerChargeTypeCalculations.push(customerChargeTypeCalculationLT);

                    var customerChargeTypeCalculationGT = {};
                    customerChargeTypeCalculationGT.chargeDataType = "Percentage";
                    customerChargeTypeCalculationGT.operationType = val.id;
                    customerChargeTypeCalculationGT.chargesOfFirst = showChargeRuleCalculationDiv.find("#chargesOfFirstGT" + val.id).val();
                    customerChargeTypeCalculationGT.chargesOfSecond = showChargeRuleCalculationDiv.find("#chargesOfSecondGT" + val.id).val();
                    customerChargeTypeCalculationGT.chargesOfThird = showChargeRuleCalculationDiv.find("#chargesOfThirdGT" + val.id).val();
                    customerChargeTypeCalculationGT.chargesOfFourth = showChargeRuleCalculationDiv.find("#chargesOfFourthGT" + val.id).val();
                    customerChargeTypeCalculationGT.chargesOfFifth = showChargeRuleCalculationDiv.find("#chargesOfFifthGT" + val.id).val();
                    customerChargeTypeCalculationGT.chargesOfFirstPage = showChargeRuleCalculationDiv.find("#chargesOfFirstPageGT" + val.id).val();
                    customerChargeType.customerChargeTypeCalculations.push(customerChargeTypeCalculationGT);
                });
            } else {
                var checkedObjs = showChargeRuleIntervalDiv.find("input[name=operationType]:checkbox:checked");
                if (checkedObjs.length == 0) {
                    alert("请选择一个操作类型电脑或手机");
                    validationFlag = false;
                    return;
                }
                customerChargeType.customerChargeTypeIntervals = [];
                $.each(checkedObjs, function (idx, val) {
                    var interval = showChargeRuleIntervalDiv.find("#" + val.id + "OperationTypeDiv").find("input[type=text]");
                    var trRow = showChargeRuleIntervalDiv.find("#tab" + val.id + ' tr').length;
                    if(parseInt(trRow)==1){
                        alert("请至少填一条规");
                        validationFlag = false;
                        if(val.id=="PC"){
                            addRowPC();
                        }else {
                            addRowPhone();
                        }
                        return false;
                    }
                    for (var i = 1; i < parseInt(trRow); i++) {
                        var customerChargeTypeInterval = {};
                        customerChargeTypeInterval.uuid = showChargeRuleIntervalDiv.find("#intervalUuid" + val.id + i).val();
                        customerChargeTypeInterval.operationType = val.id;
                        customerChargeTypeInterval.startIndex = showChargeRuleIntervalDiv.find("#startIndex" + val.id + i).val();
                        customerChargeTypeInterval.endIndex = showChargeRuleIntervalDiv.find("#endIndex" + val.id + i).val();
                        customerChargeType.customerChargeTypeIntervals.push(customerChargeTypeInterval);
                        if (parseInt(customerChargeTypeInterval.startIndex) >= parseInt(customerChargeTypeInterval.endIndex)) {
                            alert("终止指数需大于起始指数");
                            showChargeRuleIntervalDiv.find("#endIndex" + val.id + i).focus();
                            validationFlag = false;
                            return false ;
                        }
                        if (parseInt(customerChargeTypeInterval.startIndex) <= parseInt(showChargeRuleIntervalDiv.find("#endIndex" + val.id + eval(i - 1)).val())) {
                            alert("起始指数需大于前一条的终止指数");
                            showChargeRuleIntervalDiv.find("#startIndex" + val.id + i).focus();
                            showChargeRuleIntervalDiv.find("#startIndex" + val.id + i).val(eval(parseInt(showChargeRuleIntervalDiv.find("#endIndex" + val.id + eval(i - 1)).val())+1));
                            validationFlag = false;
                            return false ;
                        }

                        if (i == 1 && customerChargeTypeInterval.startIndex === '') {
                            customerChargeTypeInterval.startIndex = 0;
                        }
                        customerChargeTypeInterval.price = showChargeRuleIntervalDiv.find("#price" + val.id + i).val();
                        if (customerChargeTypeInterval.startIndex === '') {
                            alert("起始指数不能为空");
                            showChargeRuleIntervalDiv.find("#startIndex" + val.id + i).focus();
                            validationFlag = false;
                            return false ;
                        }
                        if (i < parseInt(trRow) - 1 && customerChargeTypeInterval.endIndex == '') {
                            alert("终止指数不能为空");
                            showChargeRuleIntervalDiv.find("#endIndex" + val.id + i).focus();
                            validationFlag = false;
                            return false ;
                        }
                        if (customerChargeTypeInterval.price === '') {
                            alert("价格不能为空");
                            showChargeRuleIntervalDiv.find("#price" + val.id + i).focus();
                            validationFlag = false;
                            return false ;
                        }
                        /*if(showChargeRuleIntervalDiv.find("#endIndex" + val.id + eval(parseInt(trRow)-1)).val()!=null){
                            alert("最后的终止指数必须为空");
                            showChargeRuleIntervalDiv.find("#endIndex" + val.id + eval(parseInt(trRow)-1)).focus();
                            validationFlag = false;
                            break;
                        }*/
                    }
                });
            }
            alert(JSON.stringify(customerChargeType));
            if (validationFlag) {
                $.ajax({
                    url: '/internal/customerChargeType/saveCustomerChargeType',
                    data: JSON.stringify(customerChargeType),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result) {
                            showInfo("操作成功", self);
                            window.location.reload();
                        } else {
                            showInfo("操作失败", self);
                        }
                    },
                    error: function () {
                        showInfo("操作失败", self);
                    }
                });
                $("#showRuleDialog").dialog("close");
                $('#showRuleForm')[0].reset();
            }
        }


        function chooseChargeType(val) {
            switch (val) {
                case "Percentage":
                    chargeRulePercentage();
                    $('#showRuleForm')[0].reset();
//                    hideOperationTypeDiv();
                    $("#showChargeRuleIntervalDiv").find("#PC").attr("checked", false);
                    $("#showChargeRuleIntervalDiv").find("#Phone").attr("checked", false);
                    break;
                case "Range":
                    chargeRuleInterval();
                    $('#showRuleForm')[0].reset();
                    $("#showChargeRuleCalculationDiv").find("#PC").attr("checked", false);
                    $("#showChargeRuleCalculationDiv").find("#Phone").attr("checked", false);
                    break;
            }
        }
        function chargeRulePercentage() {
            $("#chargeRulePercentage").attr("checked", true);
            $("#chargeRuleInterval").attr("checked", false);
            $("#showChargeRuleCalculationDiv").show();
            $("#showChargeRuleIntervalDiv").hide();
            $("#chargesGTPC").find("span").html("%");
            $("#chargesGTPhone").find("span").html("%");
            $("#tabPC tr:not(:first)").remove();
            $("#tabPhone tr:not(:first)").remove();
            getFoucus();
        }
        function chargeRuleInterval() {
            $("#chargeRuleInterval").attr("checked", true);
            $("#chargeRulePercentage").attr("checked", false);
            $("#showChargeRuleCalculationDiv").hide();
            $("#showChargeRuleIntervalDiv").show();
            getFoucus();
        }

        //填充到规则DIV"
        function initCustomerChargeTypeDiv(customerChargeType) {
            var showRuleDialog = $("#showRuleDialog");
            var showChargeRuleCalculationDiv = $("#showChargeRuleCalculationDiv");
            var showChargeRuleIntervalDiv = $("#showChargeRuleIntervalDiv");
            showRuleDialog.find("#customerChargeTypeUuid").val(customerChargeType.uuid);
            var switchType = customerChargeType.chargeType;//判断收费方式
            var customerChargeTypeCalculations = customerChargeType.customerChargeTypeCalculations;
            var customerChargeTypeIntervals = customerChargeType.customerChargeTypeIntervals;
            if (switchType == "Percentage") {
                chargeRulePercentage();
                showRuleDialog.find("#chargeRulePercentage").attr("checked", "checked");
                $.each(customerChargeTypeCalculations, function (idx, val) {
                    showChargeRuleCalculationDiv.find("#ChargeRuleCalculationUuid" + val.operationType).val(val.uuid);
                    showChargeRuleCalculationDiv.find("#" + val.operationType).attr("checked", true);
                    if (val.chargeDataType == "LessThanHundred") {
                        var chargesLT = showChargeRuleCalculationDiv.find("#chargesLT" + val.operationType);
                        chargesLT.find("#chargesOfFirstLT" + val.operationType).val(val.chargesOfFirst);
                        chargesLT.find("#chargesOfSecondLT" + val.operationType).val(val.chargesOfSecond);
                        chargesLT.find("#chargesOfThirdLT" + val.operationType).val(val.chargesOfThird);
                        chargesLT.find("#chargesOfFourthLT" + val.operationType).val(val.chargesOfFourth);
                        chargesLT.find("#chargesOfFifthLT" + val.operationType).val(val.chargesOfFifth);
                        chargesLT.find("#chargesOfFirstPageLT" + val.operationType).val(val.chargesOfFirstPage);
                    }
                    if (val.chargeDataType == "Percentage") {
//                        showChargeRuleCalculationDiv.find("#chargesGT" + val.operationType).show();
                        var chargesGT = showChargeRuleCalculationDiv.find("#chargesGT" + val.operationType);
                        chargesGT.find("#chargesOfFirstGT" + val.operationType).val(val.chargesOfFirst);
                        chargesGT.find("#chargesOfSecondGT" + val.operationType).val(val.chargesOfSecond);
                        chargesGT.find("#chargesOfThirdGT" + val.operationType).val(val.chargesOfThird);
                        chargesGT.find("#chargesOfFourthGT" + val.operationType).val(val.chargesOfFourth);
                        chargesGT.find("#chargesOfFifthGT" + val.operationType).val(val.chargesOfFifth);
                        chargesGT.find("#chargesOfFirstPageGT" + val.operationType).val(val.chargesOfFirstPage);
                    }
                    showLine(val.operationType);
                });
            } else {
                chargeRuleInterval();
                showRuleDialog.find("#chargeRuleInterval").attr("checked", "checked");
                var idx = customerChargeTypeIntervals.length;
                var customerChargeTypeIntervalPC = [];
                var customerChargeTypeIntervalPhone = [];
                $.each(customerChargeTypeIntervals, function (idx, val) {
                    showChargeRuleIntervalDiv.find("#" + val.operationType).attr("checked", "checked");
                    //如果有一条PC类型就在PCtable中加一"
                    if (val.operationType == "PC") {
                        customerChargeTypeIntervalPC.push(val);
                    }
                    if (val.operationType == "Phone") {
                        customerChargeTypeIntervalPhone.push(val);
                    }
                });
                $.each(customerChargeTypeIntervalPC, function (idx, val) {
                    if(null==val.endIndex||val.endIndex===''){
                        val.endIndex='';
                    }
                    $("#tab" + val.operationType).append("<tr id=" + eval(idx + 1) + " align='center'>"
                        + "<td>" + eval(idx + 1) + "</td>"
                        + "<input type='hidden' id='intervalUuid" + val.operationType + "" + eval(idx + 1) + "' />"
                        + "<td><input type='text' name='startIndexPC" + eval(idx + 1) + "' id='startIndex" + val.operationType + "" + eval(idx + 1) + "' value='" + val.startIndex + "' style='width: 100%;height: 100%' /></td>"
                        + "<td><input type='text' name='endIndexPC" + eval(idx + 1) + "' id='endIndex" + val.operationType + "" + eval(idx + 1) + "' value='" + val.endIndex + "' style='width: 100%;height: 100%' /></td>"
                        + "<td><input type='text' name='pricePC" + eval(idx + 1) + "' id='price" + val.operationType + "" + eval(idx + 1) + "' value='" + val.price + "'  style='width: 100%;height: 100%' /></td>"
                        + "<td><a href=\'#\' onclick=\'deltr" + val.operationType + "(" + eval(idx + 1) + ")\'>删除</a></td>"
                        + "</tr>");
                });
                $.each(customerChargeTypeIntervalPhone, function (idx, val) {
                    if(null==val.endIndex||val.endIndex===''){
                        val.endIndex='';
                    }
                    $("#tab" + val.operationType).append("<tr id=" + eval(idx + 1) + " align='center'>"
                        + "<td>" + eval(idx + 1) + "</td>"
                        + "<input type='hidden' id='intervalUuid" + val.operationType + "" + eval(idx + 1) + "' />"
                        + "<td><input type='text' name='startIndexPC" + eval(idx + 1) + "' id='startIndex" + val.operationType + "" + eval(idx + 1) + "' value='" + val.startIndex + "'  style='width: 100%;height: 100%'/></td>"
                        + "<td><input type='text' name='endIndexPC" + eval(idx + 1) + "' id='endIndex" + val.operationType + "" + eval(idx + 1) + "' value='" + val.endIndex + "'  style='width: 100%;height: 100%'/></td>"
                        + "<td><input type='text' name='pricePC" + eval(idx + 1) + "' id='price" + val.operationType + "" + eval(idx + 1) + "' value='" + val.price + "'   style='width: 100%;height: 100%'/></td>"
                        + "<td><a href=\'#\' onclick=\'deltr" + val.operationType + "(" + eval(idx + 1) + ")\'>删除</a></td>"
                        + "</tr>");
                });
                intervalInputOnlyNumber();
            }
        }

        function intervalInputOnlyNumber() {
            var intervalInputPC = $("#tabPC").find("input[type=text]");
            intervalInputPC.bind("keyup", function (k, v) {
                var obj = $(this);
                obj.val(obj.val().replace(/[^\d]*/g, ''));
            });

            var intervalInputPhone = $("#tabPhone").find("input[type=text]");
            intervalInputPhone.bind("keyup", function (k, v) {
                var obj = $(this);
                obj.val(obj.val().replace(/[^\d]*/g, ''));
            });
        }

        function addRowPC() {
//            $("#showChargeRuleIntervalDiv").find("#PC").attr("checked",true);
            var trRow = $("#tabPC tr").length;
            $("#tabPC").append("<tr id=" + trRow + " align='center'>"
                + "<td>" + trRow + "</td>"
                + "<input type='hidden' id='intervalUuidPC" + trRow + "' />"
                + "<td><input type='text' name='startIndexPC" + trRow + "' id='startIndexPC" + trRow + "' style='width: 100;height: 100%' /></td>"
                + "<td><input type='text' name='endIndexPC" + trRow + "' id='endIndexPC" + trRow + "'  style='width: 100;height: 100%'/></td>"
                + "<td><input type='text' name='pricePC" + trRow + "' id='pricePC" + trRow + "' style='width: 100;height: 100%'/></td>"
                + "<td><a href=\'#\' onclick=\'deltrPC(" + trRow + ")\'>删除</a></td>"
                + "</tr>");
            intervalInputOnlyNumber();
            getFoucus();
        }

        function addRowPhone() {
//            $("#showChargeRuleIntervalDiv").find("#Phone").attr("checked",true);
            var trRow = $("#tabPhone tr").length;
            $("#tabPhone").append("<tr id=" + trRow + " align='center'>"
                + "<td>" + trRow + "</td>"
                + "<input type='hidden' id='intervalUuidPhone" + trRow + "' style='width: 100;height: 100%' />"
                + "<td><input type='text' name='startIndexPhone" + trRow + "' id='startIndexPhone" + trRow + "'  style='width: 100;height: 100%' /></td>"
                + "<td><input type='text' name='endIndexPhone" + trRow + "' id='endIndexPhone" + trRow + "' style='width: 100;height: 100%' /></td>"
                + "<td><input type='text' name='pricePhone" + trRow + "' id='pricePhone" + trRow + "' style='width: 100;height: 100%'/></td>"
                + "<td><a href=\'#\' onclick=\'deltrPhone(" + trRow + ")\'>删除</a></td>"
                + "</tr>");
            intervalInputOnlyNumber();
            getFoucus();
        }

        //删除<tr/>
        function deltrPC(index) {
            var taRow = $("#tabPC tr").length;
            if (taRow <= 2) {
                alert("至少添加一条规");
                return;
            }
            $("#tabPC tr[id='" + index + "']").remove();//删除当前"
            for (var i = index + 1, j = taRow; i < j; i++) {
                var nextTxtVal = $("#endIndexPC" + i).val();
                var currentIndex = $("#startIndexPC" + i).val();
                var pricePC = $("#pricePC" + i).val();
                $("#tabPC tr[id=\'" + i + "\']")
                    .replaceWith("<tr id=" + (i - 1) + " align='center'>"
                        + "<td>" + (i - 1) + "</td>"
                        + "<td><input type='text' name='startIndexPC" + (i - 1) + "' value='" + currentIndex + "' id='startIndexPC" + (i - 1) + "' style='width: 100;height: 100%'/></td>"
                        + "<td><input type='text' name='endIndexPC" + (i - 1) + "' value='" + nextTxtVal + "' id='endIndexPC" + (i - 1) + "' style='width: 100;height: 100%'/></td>"
                        + "<td><input type='text' name='pricePC" + (i - 1) + "' value='" + pricePC + "' id='pricePC" + (i - 1) + "'  style='width: 100;height: 100%'/></td>"
                        + "<td><a href=\'#\' onclick=\'deltrPC(" + (i - 1) + ")\'>删除</a></td>"
                        + "</tr>");
            }
            intervalInputOnlyNumber();
            getFoucus();
        }

        function deltrPhone(index) {
            var taRow = $("#tabPhone tr").length;
            if (taRow <= 2) {
                alert("至少添加一条规");
                return;
            }
            $("#tabPhone tr[id='" + index + "']").remove();//删除当前"
            for (var i = index + 1, j = taRow; i < j; i++) {
                var nextTxtVal = $("#endIndexPhone" + i).val();
                var currentIndex = $("#startIndexPhone" + i).val();
                var pricePC = $("#pricePhone" + i).val();
                $("#tabPhone tr[id=\'" + i + "\']")
                    .replaceWith("<tr id=" + (i - 1) + " align='center'>"
                        + "<td>" + (i - 1) + "</td>"
                        + "<td><input type='text' name='startIndexPhone" + (i - 1) + "' value='" + currentIndex + "' id='startIndexPhone" + (i - 1) + "' style='width: 100;height: 100%'/></td>"
                        + "<td><input type='text' name='endIndexPhone" + (i - 1) + "' value='" + nextTxtVal + "' id='endIndexPhone" + (i - 1) + "' style='width: 100;height: 100%'/></td>"
                        + "<td><input type='text' name='pricePhone" + (i - 1) + "' value='" + pricePC + "' id='pricePhone" + (i - 1) + "' style='width: 100;height: 100%' /></td>"
                        + "<td><a href=\'#\' onclick=\'deltrPhone(" + (i - 1) + ")\'>删除</a></td>"
                        + "</tr>");
            }
            intervalInputOnlyNumber();
            getFoucus();
        }
        //设置当输入框失去焦点时填充数"
        function theSamePriceFirstToThirdLTPC(obj) {
            var chargesLTPC = $("#chargesLTPC");
            var inputFirstToThirdLT = chargesLTPC.find("input[type=text]:gt(0):lt(2)");
            inputFirstToThirdLT.val(obj);
        }
        function theSamePriceFourthToPageLTPC(obj) {
            var chargesLTPC = $("#chargesLTPC");
            var inputFourthToPageLT = chargesLTPC.find("input[type=text]:gt(2):lt(2)");
            inputFourthToPageLT.val(obj);
        }
        function theSamePriceFirstToThirdGTPC(obj) {
            var chargesGTPC = $("#chargesGTPC");
            var inputFirstToThirdGT = chargesGTPC.find("input[type=text]:gt(0):lt(2)");
            inputFirstToThirdGT.val(obj);
        }
        function theSamePriceFourthToPageGTPC(obj) {
            var chargesGTPC = $("#chargesGTPC");
            var inputFirstToThirdGT = chargesGTPC.find("input[type=text]:gt(2):lt(2)");
            inputFirstToThirdGT.val(obj);
        }


        //手机"
        function theSamePriceFirstToThirdLTPhone(obj) {
            var chargesLTPhone = $("#chargesLTPhone");
            var inputFirstToThirdLT = chargesLTPhone.find("input[type=text]:gt(0):lt(2)");
            inputFirstToThirdLT.val(obj);
        }
        function theSamePriceFourthToPageLTPhone(obj) {
            var chargesLTPhone = $("#chargesLTPhone");
            var inputFourthToPageLT = chargesLTPhone.find("input[type=text]:gt(2):lt(2)");
            inputFourthToPageLT.val(obj);
        }
        function theSamePriceFirstToThirdGTPhone(obj) {
            var chargesGTPhone = $("#chargesGTPhone");
            var inputFirstToThirdGT = chargesGTPhone.find("input[type=text]:gt(0):lt(2)");
            inputFirstToThirdGT.val(obj);
        }
        function theSamePriceFourthToPageGTPhone(obj) {
            var chargesGTPhone = $("#chargesGTPhone");
            var inputFirstToThirdGT = chargesGTPhone.find("input[type=text]:gt(2):lt(2)");
            inputFirstToThirdGT.val(obj);
        }


        //上传日报报表模板
        function uploadDailyReportTemplate(uuid, self) {
            $('#uploadDailyReportTemplateForm')[0].reset();
            $("#uploadDailyReportTemplateDialog").dialog({
                resizable: false,
                width: 400,
                height: 200,
                modal: true,
                //按钮
                buttons: {
                    "提交": function () {
                        $(this).dialog("close");

                        var uploadForm = $("#uploadDailyReportTemplateForm");
                        var formData = new FormData();
                        formData.append('file', uploadForm.find('#uploadFile')[0].files[0]);
                        formData.append('customerUuid', uuid);

                        $.ajax({
                            url: '/internal/customer/uploadDailyReportTemplate',
                            type: 'POST',
                            cache: false,
                            data: formData,
                            processData: false,
                            contentType: false,
                            success: function (result) {
                                if (result) {
                                    showInfo("上传成功", self);
                                } else {
                                    showInfo("上传失败", self);
                                }
                            },
                            error: function () {
                                showInfo("上传失败", self);
                            }
                        });
                    },
                    "取消": function () {
                        $(this).dialog("close");
                        $('#uploadDailyReportTemplateForm')[0].reset();
                    }
                }
            });
        }

        //显示添加客户是的DIV
        function showCustomer(uuid, userID) {
            if (uuid == null) {
                $('#showCustomerForm')[0].reset();
            }
            $("#showCustomerDialog").dialog({
                resizable: false,
                width: 330,
                height: 400,
                modal: true,
                //按钮
                buttons: {
                    "保存": function () {
                        savaCustomer(uuid, userID);
                    },
                    "清空": function () {
                        $('#showCustomerForm')[0].reset();
                    },
                    "取消": function () {
                        $(this).dialog("close");
                        $('#showCustomerForm')[0].reset();
                    }
                }
            });
        }
        function savaCustomer(uuid, userID) {
            var showCustomerForm = $("#showCustomerDialog").find("#showCustomerForm");
            var customer = {};
            customer.uuid = uuid;
            customer.userID = userID;
            customer.entryType = showCustomerForm.find("#entryTypeHidden").val();
            customer.contactPerson = showCustomerForm.find("#contactPerson").val();
            customer.qq = showCustomerForm.find("#qq").val();
            if(!(/^[1-9]\d{4,14}$/.test(customer.qq))&&(customer.qq!='')){
                alert("请输入正确的QQ");
                return;
            }
            customer.telphone = showCustomerForm.find("#telphone").val();
            if(!(/^1[34578]\d{9}$/.test(customer.telphone))&&(customer.telphone!='')){
                alert("请输入正确的手机");
                return ;
            }
            if(customer.contactPerson===''){
                alert("请输入联系人");
                return;
            }
            customer.type = showCustomerForm.find("#type").val();
            customer.status = showCustomerForm.find("#status").val();
            customer.remark = showCustomerForm.find("#remark").val();
            $.ajax({
                url: '/internal/customer/addCustomer',
                data: JSON.stringify(customer),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result) {
                        showInfo("保存成功", self);
                        window.location.reload();
                    } else {
                        showInfo("保存失败", self);
                        window.location.reload();
                    }
                },
                error: function () {
                    showInfo("保存失败", self);
                }
            });
            $("#showCustomerDialog").dialog("close");
            $('#showCustomerForm')[0].reset();
        }
        function getCustomer(uuid) {
            $.ajax({
                url: '/internal/customer/getCustomer/' + uuid,
                type: 'Get',
                success: function (customer) {
                    if (customer != null) {
                        initCustomerDialog(customer);
                        showCustomer(customer.uuid, customer.userId);
                    } else {
                        showInfo("获取信息失败", self);
                    }
                },
                error: function () {
                    showInfo("获取信息失败", self);
                }
            });
        }
        function showCustomerKeyword(uuid) {
            $("#addCustomerKeywordDialog").dialog({
                resizable: false,
                width: 510,
                height: 320,
                modal: true,
                //按钮
                buttons: {
                    "保存": function () {
                        addCustomerKeyword(uuid);
                    },
                    "清空": function () {
                        $('#addCustomerKeywordForm')[0].reset();
                    },
                    "取消": function () {
                        $(this).dialog("close");
                        $('#addCustomerKeywordForm')[0].reset();
                    }
                }
            });
        }
        function addCustomerKeyword(uuid) {
            var customerKeywords = [];
            var customerKeywordTextarea = $("#customerKeywordTextarea").val().trim();
            var group = $("#group").val().trim();
            if (group === '') {
                alert("请输入关键字分组名称");
                $("#group").focus();
                return;
            }
            if (customerKeywordTextarea === '') {
                alert("请输入关键字信息");
                $("#customerKeywordTextarea").focus();
                return;
            }
            var customerKeywordTextArray = customerKeywordTextarea.split("\n");
            if (customerKeywordTextArray.length == 1) {
                customerKeywordTextArray = customerKeywordTextarea.split("\r\n");
            }
            $.each(customerKeywordTextArray, function (idx, val) {
                val = val.trim();
                var customerKeywordAttributes = val.split(" ");
                if (customerKeywordAttributes.length == 1) {
                    customerKeywordAttributes = val.split(" ");
                }
                if (customerKeywordAttributes.length == 1) {
                    customerKeywordAttributes = val.split("	");
                }

                var tmpCustomerKeywordAttributes = [];
                $.each(customerKeywordAttributes, function (idx, val) {
                    if (val !== '') {
                        tmpCustomerKeywordAttributes.push(val);
                    }
                });
                var customerKeyword = {};
                customerKeyword.customerUuid = uuid;
                customerKeyword.keyword = tmpCustomerKeywordAttributes[0].trim();
                customerKeyword.url = tmpCustomerKeywordAttributes[1].trim();
                customerKeyword.optimizeGroupName = group.trim();

                customerKeyword.url = customerKeyword.url.replace("http://", "");
                customerKeyword.url = customerKeyword.url.replace("https://", "");

                customerKeywords.push(customerKeyword);
            });
            alert(JSON.stringify(customerKeywords));

            $.ajax({
                url: '/internal/customerkeyword/saveCustomerKeyword',
                data: JSON.stringify(customerKeywords),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result) {
                        showInfo("添加成功", self);
                        window.location.reload();
                    } else {
                        showInfo("添加失败", self);
                    }
                },
                error: function () {
                    showInfo("添加失败", self);
                    window.location.reload();
                }
            });
        }

        function delCustomer(uuid) {
            if (confirm("确实要删除这个客户吗?") == false) return;
            $.ajax({
                url: '/internal/customer/delCustomer/' + uuid,
                type: 'Get',
                success: function (result) {
                    if (result) {
                        showInfo("删除成功", self);
                        window.location.reload();
                    } else {
                        showInfo("删除失败", self);
                    }
                },
                error: function () {
                    showInfo("删除失败", self);
                    window.location.reload();
                }
            });
        }

        //将客户信息填充DIV"
        function initCustomerDialog(customer) {
            var showCustomerForm = $("#showCustomerForm");
            showCustomerForm.find("#contactPerson").val(customer.contactPerson);
            showCustomerForm.find("#qq").val(customer.qq);
            showCustomerForm.find("#telphone").val(customer.telphone);
            showCustomerForm.find("#type").val(customer.type);
            showCustomerForm.find("#status").val(customer.status);
            showCustomerForm.find("#remark").val(customer.remark);
            showCustomerForm.find("#entryTypeHidden").val(customer.entryType);
        }

        function searchCustomers(currentPage, displaysRecords) {
            var searchCustomerForm = $("#searchCustomerForm");
            searchCustomerForm.append('<input value="' + currentPage + '" id="currentPage" type="hidden" name="currentPageHidden"/>');
            searchCustomerForm.append('<input value="' + displaysRecords + '" id="currentPage" type="hidden" name="displayRerondsHidden"/>');
            searchCustomerForm.submit();
        }
        //改变当前"
        function chooseRecords(currentPage, displayRecords) {
            $('#showMainKeywordBottomDiv').find("#currentPageHidden").val(currentPage);
            $('#showMainKeywordBottomDiv').find("#displaysRecordsHidden").val(displayRecords);
            searchCustomers(currentPage, displayRecords);
        }

        function chooseUploadType() {
            var uploadForm = $("#uploadDailyReportTemplateForm");
            var uploadFile = uploadForm.find("#uploadFile").val();
            var fileTypes = new Array("xls", "xlsx");  //定义可支持的文件类型数组
            var fileTypeFlag = false;
            var newFileName = uploadFile.split('.');
            newFileName = newFileName[newFileName.length - 1];
            for (var i = 0; i < fileTypes.length; i++) {
                if (fileTypes[i] == newFileName) {
                    fileTypeFlag = true;
                    break;
                }
            }
            if (!fileTypeFlag) {
                alert("请提交表格文 .xls .xlsx");
                return false;
            }
        }

        function checkinput() {
            var contactPerson = document.getElementById("contactPerson");
            if (trim(contactPerson.value) == "") {
                alert("没有输入联系");
                contactPerson.focus();
                return false;
            }

            contactPerson.value = trim(contactPerson.value);

            var qq = document.getElementById("qq");
            qq.value = trim(qq.value);
            if (qq.value != "") {
                if (isDigit(qq.value) == false) {
                    alert("无效的QQ号码");
                    qq.focus();
                    return false;
                }
            }
        }

        <c:if test="${'bc'.equalsIgnoreCase(entryType)}">
        var intervalId = setInterval(function () {
            searchCurrentDateCompletedReports();
        }, 1000 * 30);
        </c:if>
    </script>
    <title>客户列表</title>
</head>

<body>
<div id="showCustomerTableDiv">
    <table width="100%" style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=13 align="left">
                <%@include file="/menu.jsp" %>
            </td>
        </tr>
        <tr>
            <td colspan=13>
                <form method="post" id="searchCustomerForm" action="/internal/customer/searchCustomers">
                    <table style="font-size:12px;">
                        <tr>
                            <td align="right">联系"</td>
                            <td><input type="text" name="contactPerson" id="contactPerson"
                                       value="${customerCriteria.contactPerson}"
                                       style="width:200px;"></td>
                            <td align="right">QQ:</td>
                            <td><input type="text" name="qq" id="qq" value="${customerCriteria.qq}"
                                       style="width:200px;"></td>
                            <td align="right">联系电话</td>
                            <td><input type="text" name="telphone" id="telphone" value="${customerCriteria.telphone}"
                                       style="width:200px;">
                            </td>
                            <td align="right" width="100"><input type="submit" class="ui-button ui-widget ui-corner-all"
                                                                 name="btnQuery" id="btnQuery" value=" 查询 ">
                            </td>
                            <td align="right" width="100"><input type="button" class="ui-button ui-widget ui-corner-all"
                                                                 value=" 添加客户 "
                                                                 onclick="showCustomer(null,'${user.userID}')"/>
                            </td>
                            <td align="right" width="100"><input type="button" class="ui-button ui-widget ui-corner-all"
                                                                 value=" 删除所选" onclick="deleteCustomers(this)"/>
                            </td>
                            <c:if test="${'bc'.equalsIgnoreCase(entryType)}">
                                <td align="right" width="100"><a target="_blank"
                                                                 href='javascript:triggerDailyReportGeneration(this)'>触发日报表生成</a>
                                </td>
                                <td><span id="dailyReportSpan"></span></td>
                            </c:if>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=10><input type="checkbox" onclick="selectAll(this)"/></td>

            <td align="center" width=80>用户名称</td>
            <td align="center" width=80>联系"/td>
            <td align="center" width=60>QQ</td>
            <td align="center" width=100>电话</td>
            <td align="center" width=60>已付金额</td>
            <td align="center" width=140>备注</td>
            <td align="center" width=60>类型</td>
            <td align="center" width=40>状态</td>
            <td align="center" width=80>创建时间</td>
            <td align="center" width=200>操作</td>
            <div id="div2"></div>
        </tr>
        <c:forEach items="${page.records}" var="customer">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td><input type="checkbox" name="uuid" value="${customer.uuid}"/></td>
                <c:if test="${user.vipType}">
                    <td>${user.userID}</td>
                </c:if>
                <td>
                    <a href="/customerkeyword/list.jsp?status=1&customerUuid=${customer.uuid}">${customer.contactPerson}</a>
                </td>
                <td>${customer.qq}</td>
                <td>${customer.telphone} </td>
                <td align="right">${customer.paidFee} </td>
                <td>${customer.remark}</td>
                <td>${customer.type}</td>
                <td>
                    <c:choose>
                        <c:when test="${customer.status ==1}">
                            激活
                        </c:when>
                        <c:otherwise>
                            暂停
                        </c:otherwise>
                    </c:choose>
                </td>
                <td><fmt:formatDate value="${customer.createTime}" pattern="yyyy-MM-dd"/></td>
                <td>
                    <a href="javascript:getCustomer(${customer.uuid})">修改</a> |
                    <a href="javascript:delCustomer('${customer.uuid}')">删除</a> |
                    <a href="javascript:addRule('${customer.uuid}')">客户规则</a> |
                    <a href="javascript:showCustomerKeyword(${customer.uuid})">快速加关键字</a> |
                    <a target="_blank" href="javascript:uploadDailyReportTemplate('${customer.uuid}', this)">上传日报表模板</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <%--<br><br><br>--%>
    <%--<br>--%>
</div>
<div id="showRuleDialog" title="客户规则">
    <input type="hidden" id="customerChargeTypeUuid"/>
        <div id="showRuleRadioDiv"  style="text-align: center">
            <input type="radio" id="chargeRulePercentage" onclick="chooseChargeType(this.value)" value="Percentage"
                   name="textbox">按照百分比收 &nbsp;&nbsp;
            <input type="radio" id="chargeRuleInterval" onclick="chooseChargeType(this.value)" value="Range"
                   name="textbox">按照区间收费
        </div>
        <hr>
    <form id="showRuleForm">
        <div id="showChargeRuleCalculationDiv" style="float: left;width: 100%;">
            <div id="pcOperationTypeDiv" style="float: left;width: 50%;">
                <input id="PC" type="checkbox" name="operationType"
                       style="width: 13px;height: 13px; margin-left: 130px" onchange="showOperationTypeDiv(this)"/>电脑<br>
                <input id="chargeRuleCalculationUuidPC" type="hidden"/>
                <%--指数小于0: <input id="equalZeroCostPC" type="text" /><br>--%>
                <div id="chargesLTPC" style="float: left;width: 50%;margin-top: 5px;">
                    <h6 style="text-align: center">指数小于100</h6>
                    第一 <input id="chargesOfFirstLTPC" type="text"
                              onblur="theSamePriceFirstToThirdLTPC(this.value)"/><span></span><br>
                    第二 <input id="chargesOfSecondLTPC" type="text"/><span></span><br>
                    第三 <input id="chargesOfThirdLTPC" type="text"/><span></span><br>
                    第四 <input id="chargesOfFourthLTPC" type="text"
                              onblur="theSamePriceFourthToPageLTPC(this.value)"/><span></span><br>
                    第五 <input id="chargesOfFifthLTPC" type="text"/><span></span><br>
                    首页 <input id="chargesOfFirstPageLTPC" type="text"/><span></span>
                </div>
                <div id="chargesGTPC" style="float: left;width: 50%;margin-top: 5px;">
                    <h6 style="text-align: center">指数大于100</h6>
                    第一 <input id="chargesOfFirstGTPC" type="text"
                              onblur="theSamePriceFirstToThirdGTPC(this.value)"/><span></span><br>
                    第二 <input id="chargesOfSecondGTPC" type="text"/><span></span><br>
                    第三 <input id="chargesOfThirdGTPC" type="text"/><span></span><br>
                    第四 <input id="chargesOfFourthGTPC" type="text"
                              onblur="theSamePriceFourthToPageGTPC(this.value)"/><span></span><br>
                    第五 <input id="chargesOfFifthGTPC" type="text"/><span></span><br>
                    首页 <input id="chargesOfFirstPageGTPC" type="text"/><span></span>
                </div>
            </div>
            <div id="phoneOperationTypeDiv" style="float: left;width: 48%;;margin-top: 5px;">
                <input id="Phone" type="checkbox" name="operationType"
                       style="width: 13px;height: 13px;margin-left: 130px" onchange="showOperationTypeDiv(this)"/>手机<br>
                <input id="chargeRuleCalculationUuidPhone" type="hidden"/>
                <div id="chargesLTPhone" style="float: left;width: 50%">
                    <h6 style="text-align: center">指数小于100</h6>
                    第一 <input id="chargesOfFirstLTPhone" type="text"
                              onblur="theSamePriceFirstToThirdLTPhone(this.value)"/><span></span><br>
                    第二 <input id="chargesOfSecondLTPhone" type="text"/><span></span><br>
                    第三 <input id="chargesOfThirdLTPhone" type="text"/><span></span><br>
                    第四 <input id="chargesOfFourthLTPhone" type="text"
                              onblur="theSamePriceFourthToPageLTPhone(this.value)"/><span></span><br>
                    第五 <input id="chargesOfFifthLTPhone" type="text"/><span></span><br>
                    首页 <input id="chargesOfFirstPageLTPhone" type="text"/><span></span>
                </div>
                <div id="chargesGTPhone" style="float: left;width: 50%;margin-top: 5px;">
                    <h6 style="text-align: center">指数大于100</h6>
                    第一 <input id="chargesOfFirstGTPhone" type="text"
                              onblur="theSamePriceFirstToThirdGTPhone(this.value)"/><span></span><br>
                    第二 <input id="chargesOfSecondGTPhone" type="text"/><span></span><br>
                    第三 <input id="chargesOfThirdGTPhone" type="text"/><span></span><br>
                    第四 <input id="chargesOfFourthGTPhone" type="text"
                              onblur="theSamePriceFourthToPageGTPhone(this.value)"/><span></span><br>
                    第五 <input id="chargesOfFifthGTPhone" type="text"/><span></span><br>
                    首页 <input id="chargesOfFirstPageGTPhone" type="text"/><span></span>
                </div>
            </div>
        </div>

        <div id="showChargeRuleIntervalDiv">
            <input id="chargeRuleIntervalUuid" type="hidden"/>
            <input id="PC" type="checkbox" name="operationType" onclick="showLine(this)"/>PC
            <div id="pcOperationTypeDiv" >
                <table id="tabPC" border="1" width="100%" align="center" style="margin-top:10px" cellspacing="0">
                    <tr style="text-align: center">
                        <td width="20%">序号</td>
                        <td width="20%">起始指数</td>
                        <td width="20%">终止指数</td>
                        <td width="20%">价格</td>
                        <td width="20%">操作</td>
                    </tr>
                </table>
                <div style="border:1px;
                border-color:#00CC00;
                margin-top:20px">
                    <input type="button" id="but" value="增加" onclick="addRowPC()"/>
                </div>
            </div>
            <br>
            <input id="Phone" type="checkbox" name="operationType" onclick="showLine(this)"/>Phone
            <div id="phoneOperationTypeDiv">
                <table id="tabPhone" border="1" width="100%" align="center" style="margin-top:10px" cellspacing="0">
                    <tr style="text-align: center">
                        <td width="20%">序号</td>
                        <td width="20%">起始指数</td>
                        <td width="20%">终止指数</td>
                        <td width="20%">价格</td>
                        <td width="20%">操作</td>
                    </tr>
                </table>
                <div style="border:1px;border-color:#00CC00;margin-top:20px">
                    <input type="button" id="but" value="增加" onclick="addRowPhone()"/>
                </div>
            </div>
        </div>
    </form>
</div>


</div>
<div id="showCustomerDialog" title="客户信息">
    <form id="showCustomerForm" method="post" onsubmit="return checkinput();" action="customerlist.jsp">
        <table style="font-size:14px;" cellpadding=5>
            <tr>
                <td align="right">联系"</td>
                <td><input type="text" name="contactPerson" id="contactPerson" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">QQ:</td>
                <td><input type="text" name="qq" id="qq" style="width:200px;">
                    <div class="hiddentr"
                         id="qqExisting"><%--<font color="red">该QQ在系统中已经存在"/font>--%></div>
                </td>
            </tr>
            <tr>
                <td align="right">联系电话</td>
                <td><input type="text" name="telphone" id="telphone" style="width:200px;">
                    <div class="hiddentr"
                         id="telphoneExisting"><%--<font color="red">该电话在系统中已经存在！</font>--%></div>
                </td>
            </tr>
            <tr>
                <td align="right">客户类型:</td>
                <td>
                    <select name="type" id="type">
                        <option value="普通客">普通客户</option>
                        <option value="代理">代理</option>
                    </select>
                </td>
            </tr>
            <input type="hidden" id="entryTypeHidden" value="${entryType}">
            <tr>
                <td align="right">客户状态:</td>
                <td>
                    <select name="status" id="status">
                        <option value="1">激活</option>
                        <option value="2">暂停</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right">备注</td>
                <td><textarea name="remark" id="remark" value="" placeholder="写下备注!"
                              style="width:200px;height:100px;resize: none"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<%--上传日报表模"onsubmit="return checkinput();"--%>
<div id="uploadDailyReportTemplateDialog" title="上传日报表模板">
    <form method="post" id="uploadDailyReportTemplateForm" onsubmit="return chooseUploadType()" action=""
          enctype="multipart/form-data">
        <table width="100%" style="margin-top: 10px;margin-left: 10px">
            <tr>
                <td></td>
            </tr>
            <tr>
                <td></td>
            </tr>
            <tr>
                <td align="right">
                    <table width="100%" style="font-size:14px;">
                        <tr>
                            <td>
                                <input type="hidden" id="customerUuidHidden" name="customerUuid">
                                <input type="file" id="uploadFile" name="file" size=50 height="50px" style="width: 350px;">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
</div>
<%--添加客户关键字--%>
<div id="addCustomerKeywordDialog" title="客户关键字">
    <form id="addCustomerKeywordForm">
   <textarea id="customerKeywordTextarea" style="width:480px;height:180px;"
             placeholder="关键字 域名  关键字与域名以空格作为分割，一行一组"></textarea>
        <c:choose>
            <c:when test="${'PC'.equalsIgnoreCase(terminalType)}">
                <input type="input" id="group" style="width:480px" value="pc_pm_xiaowu"/>
            </c:when>
            <c:otherwise>
                <input type="input" id="group" style="width:480px" value="m_pm_xiaowu"/>
            </c:otherwise>
        </c:choose>
    </form>
</div>
<hr>
<div id="showCustomerBottomDiv">
    <input id="fisrtButton" class="ui-button ui-widget ui-corner-all" type="button"
           onclick="searchCustomers(1,'${page.size}')" value="首页"/>&nbsp;&nbsp;&nbsp;&nbsp;
    <input id="upButton" type="button" class="ui-button ui-widget ui-corner-all"
           onclick="searchCustomers('${page.current-1}','${page.size}')" value="上一页"/>&nbsp;&nbsp;&nbsp;&nbsp;
    ${page.current}/${page.pages}&nbsp;&nbsp;
    <input id="nextButton" type="button" class="ui-button ui-widget ui-corner-all"
           onclick="searchCustomers('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')"
           value="下一页">&nbsp;&nbsp;&nbsp;&nbsp;
    <input id="lastButton" type="button" class="ui-button ui-widget ui-corner-all"
           onclick="searchCustomers('${page.pages}','${page.size}')" value="末页">&nbsp;&nbsp;&nbsp;&nbsp;
    总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;
    每页显示条数:<select id="chooseRecords" onchange="chooseRecords(${page.current},this.value)">
    <option>10</option>
    <option>15</option>
    <option>30</option>
    <option>45</option>
</select>
    <%--用于存储pageInfo--%>
    <input type="hidden" id="currentPageHidden" value="${page.current}"/>
    <input type="hidden" id="displaysRecordsHidden" value="${page.size}"/>
    <input type="hidden" id="pagesHidden" value="${page.pages}"/>
</div>

</body>
</html>