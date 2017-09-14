<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<%
    String path = request.getContextPath();
    String basePath =
            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <script language="javascript" type="text/javascript" src="/common.js"></script>
    <script src="${pageContext.request.contextPath}/customerkeyword/add.js"  type="text/javascript" language="javascript"></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript" src="/js/slide1.12.4.js"></script>
    <link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link href="/css/menu.css" rel="stylesheet" type="text/css"/>
    <%--toastmessage插件--%>
    <script language="javascript" type="text/javascript" src="/toastmessage/jquery.toastmessage.js"></script>
    <link rel="stylesheet" href="/toastmessage/css/jquery.toastmessage.css">
    <head>
        <title>关键字列表</title>
        <style>

            .wrap {
                word-break: break-all;
                word-wrap: break-word;
            }

            <!--
            #div1 {
                display: none;
                background-color: #f6f7f7;
                color: #333333;
                font-size: 12px;
                line-height: 18px;
                border: 1px solid #e1e3e2;
                width: 450px;
                height: 50px;
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

            #customerKeywordTopDiv {
                position: fixed;
                top: 0px;
                left: 0px;
                background-color: white;
                width: 100%;
            }

            #customerKeywordDiv {
                /*overflow: scroll;*/
                width: 100%;
                margin-bottom: 47PX;
            }

            #customerKeywordTable {
                width: 100%;
            }
            #customerKeywordTable tr:nth-child(odd){background:#EEEEEE;}

            #customerKeywordTable td{
                text-align: left;
            }

            #showCustomerBottomPositioneDiv{
                position: fixed;
                bottom: 0px;
                right: 0px;
                background-color: white;
                padding-top: 10px;
                padding-bottom: 10px;
                width: 100%;
            }
            #showCustomerBottomDiv {
                float: right;
                margin-right: 20px;
            }

            body{
                margin: 0;
                padding: 0;
            }

            #saveCustomerKeywordDialog ul{list-style: none;margin: 0px;padding: 0px;}
            #saveCustomerKeywordDialog li{margin: 5px 0;}
            #saveCustomerKeywordDialog .customerKeywordSpanClass{width: 70px;display: inline-block;text-align: right;}
        </style>

        <script language="javascript">
            $(function () {
                $("#groupChangeNameDialog").hide();
                $("#uploadExcelDailog").hide();
                $("#saveCustomerKeywordDialog").hide();
                $("#customerKeywordDiv").css("margin-top",$("#customerKeywordTopDiv").height());
                initPaging();
                initNoPositionChecked();//初始化排名为0的初始值
                alignTableHeader();
                window.onresize = function(){
                    alignTableHeader();
                }
            });
            function initPaging() {
                var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
                var searchCustomerKeywordTable = searchCustomerKeywordForm.find("#searchCustomerKeywordTable");
                searchCustomerKeywordTable.find("#orderElement").val('${orderElement}');
                searchCustomerKeywordTable.find("#status").val(${customerKeywordCrilteria.status});
                var pages = searchCustomerKeywordForm.find('#pagesHidden').val();
                var currentPageNumber = searchCustomerKeywordForm.find('#currentPageNumberHidden').val();
                var showCustomerBottomDiv = $('#showCustomerBottomDiv');
                showCustomerBottomDiv.find("#chooseRecords").val(${page.size});
                if (parseInt(currentPageNumber) <= 1) {
                    currentPageNumber = 1;
                    showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                    showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
                } else if (parseInt(currentPageNumber) >= parseInt(pages)) {
                    currentPageNumber = pages;
                    showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
                    showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
                } else {
                    showCustomerBottomDiv.find("#firstButton").removeAttr("disabled");
                    showCustomerBottomDiv.find("#upButton").removeAttr("disabled");
                    showCustomerBottomDiv.find("#nextButton").removeAttr("disabled");
                    showCustomerBottomDiv.find("#lastButton").removeAttr("disabled");
                }
            }

            function selectAll(self) {
                var a = document.getElementsByName("uuid");
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

            function doOver(obj) {
                obj.style.backgroundColor = "#73B1E0";
            }

            function doOut(obj) {
                var rowIndex = obj.rowIndex;
                if ((rowIndex % 2) == 0) {
                    obj.style.backgroundColor = "#eeeeee";
                } else {
                    obj.style.backgroundColor = "#ffffff";
                }
            }

            function setSecondThirdDefaultFee(self){
                $(self).val($(self).val().replace(/[^\d]*/g, ''));
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

            function setThirdDefaultFee(self){
                $(self).val($(self).val().replace(/[^\d]*/g, ''));
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

            function delItem(customerKeywordUuid) {
                if (confirm("确实要删除这个关键字吗?") == false) return;
                $.ajax({
                    url: '/internal/customerKeyword/deleteCustomerKeyword/' + customerKeywordUuid,
                    type: 'GET',
                    success: function (result) {
                        if (result) {
                            $().toastmessage('showSuccessToast', "删除成功");
                            window.location.reload();
                        } else {
                            $().toastmessage('showErrorToast', "删除失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "删除失败");
                    }
                });
            }

            function delAllItems(deleteType,customerUuid) {
                var customerKeywordUuids = getUuids();
                switch (deleteType){
                    case 'ByUuid' :
                        if (customerKeywordUuids === '') {
                            alert('请选择要操作的信息');
                            return;
                        }
                        if (confirm("确实要删除这些关键字吗?") == false) return;
                        break;
                    case 'EmptyTitleAndUrl' :
                        if (confirm("确实要删除标题和网址为空的关键字吗?") == false) return;
                        break;
                    case 'EmptyTitle' :
                        if (confirm("确实要删除标题为空的关键字吗?") == false) return;
                        break;
                }
                var deletionCriteria = {};
                if(customerKeywordUuids !== ''){
                    deletionCriteria.uuids = customerKeywordUuids.split(",");
                }
                deletionCriteria.deleteType = deleteType;
                deletionCriteria.customerUuid = customerUuid;
                $.ajax({
                    url: '/internal/customerKeyword/deleteCustomerKeywords',
                    data: JSON.stringify(deletionCriteria),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (data) {
                        if (data) {
                            $().toastmessage('showSuccessToast', "操作成功");
                            window.location.reload();
                        } else {
                            $().toastmessage('showErrorToast', "操作失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                });
            }

            function getUuids() {
                var a = document.getElementsByName("uuid");
                var uuids = '';
                for (var i = 0; i < a.length; i++) {
                    //alert(a[i].value);
                    if (a[i].checked) {
                        if (uuids === '') {
                            uuids = a[i].value;
                        } else {
                            uuids = uuids + ',' + a[i].value;
                        }
                    }
                }
                return uuids;
            }

            function cleanTitle(customerUuid, cleanType) {
                var customerKeywordCleanCriteria = {};
                if (cleanType == 'SelectedCustomerKeywordTitle') {
                    var customerKeywordUuids = getUuids();
                    if (customerKeywordUuids.trim() === '') {
                        alert("请选中要操作的关键词！");
                        return;
                    }
                    if (confirm("确认要清空标题吗?") == false) return;
                    customerKeywordCleanCriteria.customerKeywordUuids = customerKeywordUuids.split(",");
                }else if(cleanType == 'CaptureTitleFlag'){
                    if (confirm("确认要重新采集标题?") == false) return;
                }else {
                    if (confirm("确认要清除所有标题?") == false) return;
                }
                customerKeywordCleanCriteria.cleanType = cleanType;
                customerKeywordCleanCriteria.customerUuid = customerUuid;

                $.ajax({
                    url: '/internal/customerKeyword/cleanTitle',
                    data: JSON.stringify(customerKeywordCleanCriteria),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (status) {
                        if (status) {
                            $().toastmessage('showSuccessToast', "操作成功");
                            window.location.reload();
                        } else {
                            $().toastmessage('showErrorToast', "操作失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                });
            }

            function showTip(content, e) {
                var event = e || window.event;
                var pageX = event.pageX;
                var pageY = event.pageY;
                if (pageX == undefined) {
                    pageX = event.clientX + document.body.scrollLeft || document.documentElement.scrollLeft;
                }
                if (pageY == undefined) {
                    pageY = event.clientY + document.body.scrollTop || document.documentElement.scrollTop;
                }
                var div1 = document.getElementById('div1'); //将要弹出的层
                div1.innerText = content;
                div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见
                div1.style.left = pageX + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
                div1.style.top = pageY + 5;
                div1.style.position = "absolute";
            }

            //关闭层div1的显示
            function closeTip() {
                var div1 = document.getElementById('div1');
                div1.style.display = "none";
            }

            //重构部分
            //查询
            function changePaging(currentPage, pageSize) {
                var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
                searchCustomerKeywordForm.find("#currentPageNumberHidden").val(currentPage);
                searchCustomerKeywordForm.find("#pageSizeHidden").val(pageSize);
                searchCustomerKeywordForm.submit();
            }

            function resetPageNumber() {
                var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
                searchCustomerKeywordForm.find("#currentPageNumberHidden").val(1);
            }

            //修改所有组名
            function showGroupNameChangeDialog(changeGroupCriteria) {
                $("#groupChangeNameDialog").dialog({
                    resizable: false,
                    width: 260,
                    height: 150,
                    modal: true,
                    title: changeGroupCriteria.title,
                    position:{
                        my:"center top",
                        at:"center top+150",
                        of:window
                    },
                    //按钮
                    buttons: {
                        "保存": function () {
                            var targetGroupName = $("#groupNameChangeFrom").find("#groupName").val();
                            if (targetGroupName == null || targetGroupName === '') {
                                alert("请输入分组名");
                                return;
                            }
                            changeGroupCriteria.targetGroupName = targetGroupName;
                            changeGroupName(changeGroupCriteria);
                            $(this).dialog("close");
                        },
                        "清空": function () {
                            $('#groupNameChangeFrom')[0].reset();
                        },
                        "取消": function () {
                            $(this).dialog("close");
                            $('#groupNameChangeFrom')[0].reset();
                        }
                    }
                });
            }

            //下架
            function stopOptimization(customerUuid){
                changeGroupName({"customerUuid": customerUuid, "targetGroupName": "stop"});
            }

            //修改部分
            function updateSpecifiedCustomerKeywordGroupName() {
                var customerKeywordUuids = getUuids();
                if (customerKeywordUuids.trim() === '') {
                    alert("请选中要操作的关键词！");
                    return;
                }
                var changeGroupCriteria = {"title" : "修改选中关键字分组", "customerKeywordUuids":customerKeywordUuids.split(",")};
                showGroupNameChangeDialog(changeGroupCriteria);
            }

            function changeGroupName(customerKeywordUpdateGroupCriteria) {
                $.ajax({
                    url:'/internal/customerKeyword/updateCustomerKeywordGroupName',
                    data:JSON.stringify(customerKeywordUpdateGroupCriteria),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result) {
                            $().toastmessage('showSuccessToast', "操作成功");
                            window.location.reload();
                        } else {
                            $().toastmessage('showErrorToast', "操作失败");
                            window.location.reload();
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "操作失败");
                        window.location.reload();
                    },
                });
            }


            //增加新关键字
            function addCustomerKeyword(customerKeywordUuid) {
                if (customerKeywordUuid == null) {
                    $("#customerKeywordForm")[0].reset();
                    $("#customerKeywordForm").find("#uuid").val('');
                    $("#customerKeywordForm").find("#status").val('');
                }
                $( "#saveCustomerKeywordDialog").dialog({
                    width: 440,
                    height: 610,
                    position: {
                        my: "center top",
                        at: "center top+50px",
                        of: window},
                    title : "添加关键字",
                    show: {
                        effect: "blind",
                        /* duration: 1000*/
                    },
                    hide: {
                        effect: "blind",
                        /*duration: 1000*/
                    },
                    modal: true,
                    resizable: false,
                    buttons: {
                        "保存": function () {
                            saveCustomerKeyword("${customerKeywordCrilteria.customerUuid}");
                        },
                        "清空": function () {
                            $('#customerKeywordForm')[0].reset();
                        },
                        "取消": function () {
                            $(this).dialog("close");
                            $('#customerKeywordForm')[0].reset();
                        }
                    }
                });
            }

            function saveCustomerKeyword(customerUuid) {
                var customerKeyword = {};
                var saveCustomerKeywordDialog= $("#saveCustomerKeywordDialog");
                customerKeyword.uuid = saveCustomerKeywordDialog.find("#uuid").val();
                customerKeyword.customerUuid = customerUuid;
                customerKeyword.searchEngine = saveCustomerKeywordDialog.find("#searchEngine").val();
                var keyword = $.trim(saveCustomerKeywordDialog.find("#keyword").val());
                if (keyword === '') {
                    $().toastmessage('showWarningToast', "关键字不能为空");
                    saveCustomerKeywordDialog.find("#keyword").focus();
                    return;
                }
                customerKeyword.keyword = keyword;
                var url = $.trim(saveCustomerKeywordDialog.find("#url").val())
                if (url.length == 0) {
                    $().toastmessage('showWarningToast', "网址不能为空！");
                    saveCustomerKeywordDialog.find("#url").focus();
                    return;
                }
                customerKeyword.url = url;
                var originalUrl = $.trim(saveCustomerKeywordDialog.find("#originalUrl").val());
                customerKeyword.originalUrl = originalUrl;
                var regNumber = /^\d+$/;
                var positionFirstFee = $.trim(saveCustomerKeywordDialog.find("#positionFirstFee").val());
                customerKeyword.positionFirstFee = positionFirstFee;
                var positionSecondFee = $.trim(saveCustomerKeywordDialog.find("#positionSecondFee").val());
                customerKeyword.positionSecondFee = positionSecondFee;
                var positionThirdFee = $.trim(saveCustomerKeywordDialog.find("#positionThirdFee").val());
                customerKeyword.positionThirdFee = positionThirdFee;
                var positionForthFee = $.trim(saveCustomerKeywordDialog.find("#positionForthFee").val());
                customerKeyword.positionForthFee = positionForthFee;
                var positionFifthFee = $.trim(saveCustomerKeywordDialog.find("#positionFifthFee").val());
                customerKeyword.positionFifthFee = positionFifthFee;
                var positionFirstPageFee = $.trim(saveCustomerKeywordDialog.find("#positionFirstPageFee").val());
                customerKeyword.positionFirstPageFee = positionFirstPageFee;
                var positionFirstCost = $.trim(saveCustomerKeywordDialog.find("#positionFirstCost").val());
                customerKeyword.positionFirstCost = positionFirstCost;
                var positionSecondCost = $.trim(saveCustomerKeywordDialog.find("#positionSecondCost").val());
                customerKeyword.positionSecondCost = positionSecondCost;
                var positionThirdCost = $.trim(saveCustomerKeywordDialog.find("#positionThirdCost").val());
                customerKeyword.positionThirdCost = positionThirdCost;
                var positionForthCost = $.trim(saveCustomerKeywordDialog.find("#positionForthCost").val());
                customerKeyword.positionForthCost = positionForthCost;
                var positionFifthCost = $.trim(saveCustomerKeywordDialog.find("#positionFifthCost").val());
                customerKeyword.positionFifthCost = positionFifthCost;
                var initialPosition = $.trim(saveCustomerKeywordDialog.find("#initialPosition").val());
                customerKeyword.initialPosition = initialPosition;
                customerKeyword.currentPosition = initialPosition;
                var initialIndexCount = $.trim(saveCustomerKeywordDialog.find("#initialIndexCount").val());
                customerKeyword.initialIndexCount = initialIndexCount;
                customerKeyword.currentIndexCount = initialIndexCount;
                var sequence = $.trim(saveCustomerKeywordDialog.find("#sequence").val());
                customerKeyword.sequence = sequence;
                var title = $.trim(saveCustomerKeywordDialog.find("#title").val());
                customerKeyword.title = title;
                var optimizeGroupName = $.trim(saveCustomerKeywordDialog.find("#optimizeGroupName").val());
                customerKeyword.optimizeGroupName = optimizeGroupName;
                var collectMethod = $.trim(saveCustomerKeywordDialog.find("#collectMethod").val());
                customerKeyword.collectMethod = collectMethod;
                var serviceProvider = $.trim(saveCustomerKeywordDialog.find("#serviceProvider").val());
                customerKeyword.serviceProvider = serviceProvider;
                var orderNumber = $.trim(saveCustomerKeywordDialog.find("#orderNumber").val());
                customerKeyword.orderNumber = orderNumber;
                var paymentStatus = $.trim(saveCustomerKeywordDialog.find("#paymentStatus").val());
                customerKeyword.paymentStatus = paymentStatus;
                var remarks = $.trim(saveCustomerKeywordDialog.find("#remarks").val());
                customerKeyword.remarks = remarks;
//                alert(JSON.stringify(customerKeyword));
                $.ajax({
                    url: '/internal/customerKeyword/saveCustomerKeyword',
                    data: JSON.stringify(customerKeyword),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result) {
                            $().toastmessage('showSuccessToast', "操作成功");
                            window.location.reload();
                        } else {
                            $().toastmessage('showErrorToast', "操作失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                });
                $(this).dialog("close");
            }

            //修改关键字
            function modifyCustomerKeyword(customerKeywordUuid) {
                var saveCustomerKeywordDialog= $("#saveCustomerKeywordDialog");
                $.ajax({
                    url: '/internal/customerKeyword/getCustomerKeywordByCustomerKeywordUuid/' + customerKeywordUuid,
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (customerKeyword) {
//                alert(customerKeyword);
                        if (customerKeyword != null) {
                            saveCustomerKeywordDialog.find("#uuid").val(customerKeyword.uuid);
                            saveCustomerKeywordDialog.find("#keyword").val(customerKeyword.keyword);
                            saveCustomerKeywordDialog.find("#searchEngine").val(customerKeyword.searchEngine);
                            saveCustomerKeywordDialog.find("#initialIndexCount").val(customerKeyword.currentIndexCount);
                            saveCustomerKeywordDialog.find("#initialPosition").val(customerKeyword.currentPosition == null ? '' : customerKeyword.currentPosition);
                            saveCustomerKeywordDialog.find("#url").val(customerKeyword.url);
                            saveCustomerKeywordDialog.find("#originalUrl").val(customerKeyword.originalUrl);
                            saveCustomerKeywordDialog.find("#positionFirstFee").val(customerKeyword.positionFirstFee);
                            saveCustomerKeywordDialog.find("#positionSecondFee").val(customerKeyword.positionSecondFee);
                            saveCustomerKeywordDialog.find("#positionThirdFee").val(customerKeyword.positionThirdFee);
                            saveCustomerKeywordDialog.find("#positionForthFee").val(customerKeyword.positionForthFee);
                            saveCustomerKeywordDialog.find("#positionFifthFee").val(customerKeyword.positionFifthFee);
                            saveCustomerKeywordDialog.find("#positionFirstPageFee").val(customerKeyword.positionFirstPageFee);
                            saveCustomerKeywordDialog.find("#positionFirstCost").val(customerKeyword.positionFirstCost);
                            saveCustomerKeywordDialog.find("#positionSecondCost").val(customerKeyword.positionSecondCost);
                            saveCustomerKeywordDialog.find("#positionThirdCost").val(customerKeyword.positionThirdCost);
                            saveCustomerKeywordDialog.find("#positionForthCost").val(customerKeyword.positionForthCost);
                            saveCustomerKeywordDialog.find("#positionFifthCost").val(customerKeyword.positionFifthCost);
                            saveCustomerKeywordDialog.find("#serviceProvider").val(customerKeyword.serviceProvider);
                            saveCustomerKeywordDialog.find("#sequence").val(customerKeyword.sequence);
                            saveCustomerKeywordDialog.find("#title").val(customerKeyword.title);
                            saveCustomerKeywordDialog.find("#optimizeGroupName").val(customerKeyword.optimizeGroupName);
                            saveCustomerKeywordDialog.find("#collectMethod").val(customerKeyword.collectMethod);
                            saveCustomerKeywordDialog.find("#orderNumber").val(customerKeyword.orderNumber);
                            saveCustomerKeywordDialog.find("#paymentStatus").val(customerKeyword.paymentStatus);
                            saveCustomerKeywordDialog.find("#remarks").val(customerKeyword.remarks);
                            if(customerKeyword.positionFirstCost!=null||customerKeyword.positionSecondCost!=null||customerKeyword.positionThirdCost!=null||customerKeyword.positionForthCost!=null||customerKeyword.positionFifthCost!=null){
                                showCustomerKeywordCost();
                            }
                            addCustomerKeyword(customerKeywordUuid);
                        } else {
                            $().toastmessage('showErrorToast', "操作失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                });
            }
            function showCustomerKeywordCost() {
                $("#customerKeywordCost #customerKeywordCostFrom").toggle();
            }
            //关键字Excel上传(简化版)
            function uploadCustomerKeywords(customerUuid, excelType){
                $('#uploadExcelForm')[0].reset();
                if(excelType=='SuperUserSimple'){
                    $('#uploadExcelForm').find("#excelType").html("(简易版)");
                }else{
                    $('#uploadExcelForm').find("#excelType").html("(完整版)");
                }
                $("#uploadExcelDailog").dialog({
                    resizable: false,
                    width: 260,
                    height: 180,
                    modal: true,
                    position:{
                        my:"center top",
                        at:"center top+150",
                        of:window
                    },
                    //按钮
                    buttons: {
                        "上传": function () {
                            var uploadForm = $("#uploadExcelForm");
                            var uploadFile = uploadForm.find("#uploadExcelFile").val();
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
                            var formData = new FormData();
                            formData.append('file', uploadForm.find('#uploadExcelFile')[0].files[0]);
                            formData.append('customerUuid', customerUuid);
                            formData.append('excelType', excelType);
                            if (fileTypeFlag) {
                                $.ajax({
                                    url: '/internal/customerKeyword/uploadCustomerKeywords',
                                    type: 'POST',
                                    cache: false,
                                    data: formData,
                                    processData: false,
                                    contentType: false,
                                    success: function (result) {
                                        if (result) {
                                            $().toastmessage('showSuccessToast', "上传成功");
                                            window.location.reload();
                                        } else {
                                            $().toastmessage('showErrorToast', "上传失败");
                                        }
                                    },
                                    error: function () {
                                        $().toastmessage('showErrorToast', "上传失败");
                                    }
                                });
                            }
                            $(this).dialog("close");
                        },
                        "取消": function () {
                            $(this).dialog("close");
                            $('#uploadExcelForm')[0].reset();
                        }
                    }
                });
            }

            //导出结果
            function downloadCustomerKeywordInfo() {
//                var customerKeywordCrilteriaArray = $("#searchCustomerKeywordForm").serializeArray();
//                var formData = new FormData();
//                $.each(customerKeywordCrilteriaArray, function(idx, val){
//                    formData.append(val.name, val.value);
//                });
//                $.ajax({
//                    url: '/internal/customerKeyword/downloadCustomerKeywordInfo',
//                    type: 'POST',
//                    cache: false,
//                    data: formData,
//                    processData: false,
//                    contentType: false,
//                    success: function (result) {
//                        if (result) {
//                            $().toastmessage('showSuccessToast', "导出成功");
//                        } else {
//                            $().toastmessage('showErrorToast',"导出失败");
//                        }
//                    },
//                    error: function () {
//                        $().toastmessage('showErrorToast',"导出失败");
//                    }
//                });
                var customerKeywordCrilteriaArray = $("#searchCustomerKeywordForm").serialize();
                location.href='/internal/customerKeyword/downloadCustomerKeywordInfo/?'+customerKeywordCrilteriaArray;
            }
            //显示排名为0
            function noPositionValue() {
                if($("#noPosition").is(":checked")){
                    $("#noPosition").val("1")
                }else {
                    $("#noPosition").val("0");
                }
            }

            function initNoPositionChecked() {
                if(${customerKeywordCrilteria.noPosition == 1}){
                    $("#noPosition").prop("checked",true);
                }else{
                    $("#noPosition").prop("checked",false);
                }
                noPositionValue();
            }

            function alignTableHeader() {
                var td = $("#customerKeywordTable tr:first td");
                var ctd = $("#headerTable tr:first td");
                $.each(td, function (idx, val) {
                    ctd.eq(idx).width($(val).width());
                });
            }
            function onlyNumber(self) {
                $(self).val($(self).val().replace(/[^\d]*/g, ''));
            }
        </script>

    </head>
<body>
<div id="customerKeywordTopDiv">
    <table style="font-size:12px;margin-right: 10px;" cellpadding=3>
        <tr>
            <td colspan=18 align="left">
                <%@include file="/menu.jsp" %>
            </td>
        </tr>
        <tr>
            <table width=100% style="border:1px solid #000000;font-size:12px;" cellpadding=3>
                <tr border="1" height=30>
                    <td width=250>联系人: ${customer.contactPerson}</td>
                    <td width=200>QQ: ${customer.qq}</td>
                    <td width=200>电话: ${customer.telphone}</td>
                    <td width=120>关键字数: ${customer.keywordCount}</td>
                    <td width=250>创建时间: <fmt:formatDate value="${customer.createTime}" pattern=" yyyy-MM-dd HH:mm"/></td>
                </tr>
            </table>
        </tr>
    </table>

    <%--<c:if test="${!user.vipType}">--%>
    <div style="text-align: right">
        <a target="_blank" href="javascript:uploadCustomerKeywords('${customerKeywordCrilteria.customerUuid}', 'SuperUserSimple')"/>Excel上传(简化版)</a>
        | <a target="_blank" href="/SuperUserSimpleKeywordList.xls">简化版下载</a>
        | <a target="_blank" href="javascript:uploadCustomerKeywords('${customerKeywordCrilteria.customerUuid}', 'SuperUserFull')">Excel上传(完整版)</a>
        | <a target="_blank" href="/SuperUserFullKeywordList.xls">完整版下载</a>
        | <a target="_blank" href="/internal/dailyReport/downloadSingleCustomerReport/${customerKeywordCrilteria.customerUuid}">导出日报表</a>
        | <a target="_blank" href="javascript:downloadCustomerKeywordInfo()">导出结果</a>
        <br/><br/>
        <a href="javascript:showGroupNameChangeDialog({'title': '修改客户关键字分组', 'customerUuid':'${customerKeywordCrilteria.customerUuid}'})">修改所有分组</a>|
        <a href="javascript:updateSpecifiedCustomerKeywordGroupName(${customerKeywordCrilteria.customerUuid})">修改选中分组</a>|
        <a href="javascript:stopOptimization(${customerKeywordCrilteria.customerUuid})">下架所有关键字</a>|
        <a href="javascript:delAllItems('EmptyTitleAndUrl','${customerKeywordCrilteria.customerUuid}')">删除标题和网址为空的关键字</a> |
        <a href="javascript:delAllItems('EmptyTitle','${customerKeywordCrilteria.customerUuid}')">删除标题为空的关键字</a> |
        <a href="javascript:cleanTitle('${customerKeywordCrilteria.customerUuid}','CaptureTitleFlag')">重采标题</a> |
        <a href="javascript:cleanTitle('${customerKeywordCrilteria.customerUuid}', 'SelectedCustomerKeywordTitle')">清空所选标题</a>|
        <a href="javascript:cleanTitle('${customerKeywordCrilteria.customerUuid}', 'CustomerTitle')">清空客户标题</a>
    </div>
    <br/>
    <form id="searchCustomerKeywordForm" style="font-size:12px; width: 100%;" action="/internal/customerKeyword/searchCustomerKeywords" method="post">
        <div id="searchCustomerKeywordTable">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            <input id="customerUuid" name="customerUuid" type="hidden" value="${customerKeywordCrilteria.customerUuid}">
            关键字:<input type="text" name="keyword" id="keyword" value="${customerKeywordCrilteria.keyword}"
                       style="width:80px;">
            URL:<input type="text" name="url" id="url" value="${customerKeywordCrilteria.url}"
                       style="width:80px;">
            关键字状态:
            <select name="status" id="status">
                <option value='1'>激活</option>
                <option value='2'>新增</option>
                <option value='0'>过期</option>
            </select>
            优化组名:
            <input type="text" name="optimizeGroupName" id="optimizeGroupName"
                   value="${customerKeywordCrilteria.optimizeGroupName}" style="width:100px;">
            <%--<c:if test="${!user.vipType}">--%>
            显示前:
            <input type="text" name="position" id="position" value="${customerKeywordCrilteria.position}"
                   style="width:40px;"/>
            <input id="noPosition" name="noPosition" type="checkbox"  onclick="noPositionValue()"/>显示0 &nbsp;
            无效点击数:
            <input type="text" name="invalidRefreshCount" id="invalidRefreshCount"
                   value="${customerKeywordCrilteria.invalidRefreshCount}" style="width:20px;">
            创建日期:<input name="creationFromTime" id="creationFromTime" class="Wdate"
                        type="text" style="width:90px;" onClick="WdatePicker()"
                        value="${customerKeywordCrilteria.creationFromTime}">
            到<input
                name="creationToTime"
                id="creationToTime"
                class="Wdate" type="text"
                style="width:90px;"
                onClick="WdatePicker()"
                value="${customerKeywordCrilteria.creationToTime}">
            排序:
            <%--<select name="orderElement" id="orderElement">--%>
                <%--<option value="">--请选择排序--</option>--%>
                <%--<option value="fCreateTime">创建日期</option>--%>
                <%--<option value="fCurrentPosition">当前排名</option>--%>
                <%--<option value="fSequence">添加序号</option>--%>
            <%--</select>--%>
            <select name="orderElement" id="orderElement">
                <option value="0">关键字</option>
                <option value="1">创建日期</option>
                <option value="2">当前排名</option>
                <option value="3">添加序号</option>
            </select>
            <%--</c:if>--%>
            &nbsp;&nbsp;
            <input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()"
                   value=" 查询 ">&nbsp;&nbsp;
            <input type="button" class="ui-button ui-widget ui-corner-all" onclick="addCustomerKeyword()"
                   value=" 增加 ">&nbsp;&nbsp;
            <input type="button" class="ui-button ui-widget ui-corner-all"
                   onclick="delAllItems('ByUuid','${customerKeywordCrilteria.customerUuid}')"
                   value=" 删除所选 ">
        </div>
    </form>
    <%--</c:if>--%>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=10><input type="checkbox" onclick="selectAll(this)"/></td>
            <td align="center" width=100>关键字</td>
            <td align="center" width=200>URL</td>
            <td align="center" width=250>标题</td>
            <td align="center" width=30>指数</td>
            <td align="center" width=50>原排名</td>
            <td align="center" width=50>现排名</td>
            <td align="center" width=30>计价方式</td>
            <td align="center" width=30>要刷</td>
            <td align="center" width=30>已刷</td>
            <td align="center" width=30>无效</td>
            <td align="center" width=60>报价</td>
            <td align="center" width=80>开始优化日期</td>
            <td align="center" width=80>最后优化时间</td>
            <td align="center" width=50>订单号</td>
            <td align="center" width=100>备注</td>
            <c:choose>
                <c:when test="${user.vipType}">
                    <td align="center" width=60>优化组名</td>
                    <td align="center" width=80>操作</td>
                </c:when>
                <c:when test="${user.userLevel == 1}">
                    <td align="center" width=80>操作</td>
                </c:when>
            </c:choose>
            <div id="div1"></div>
            <div id="div2"></div>
        </tr>
    </table>
    <hr>
</div>
<div id="customerKeywordDiv">
    <table id="customerKeywordTable">
        <c:forEach items="${page.records}" var="customerKeyword">
            <tr style="" height=30 onmouseover="doOver(this);" onmouseout="doOut(this);" ondblclick="modifyCustomerKeyword('${customerKeyword.uuid}')" height=30>
                <td  align="center" width=10><input type="checkbox" name="uuid" value="${customerKeyword.uuid}"/></td>
                <td align="center" width=100>
                    <font color="<%--<%=keywordColor%>--%>">${customerKeyword.keyword}</font>
                </td>
                <td  align="center" width=200 class="wrap"
                     onMouseMove="showTip('原始URL:${customerKeyword.originalUrl != null ?customerKeyword.originalUrl : customerKeyword.url}')"
                     onMouseOut="closeTip()">
                    <div style="height:16;">
                            ${customerKeyword.url==null?'':customerKeyword.url};
                    </div>
                </td>

                <td align="center" width=250>
                        ${customerKeyword.title == null ? "" : customerKeyword.title.trim()}
                </td>
                <td align="center" width=30>
                    <div style="height:16;"><a
                            href="/internal/customerKeywordPositionIndexLog/historyPositionAndIndex/${customerKeyword.uuid}/30"
                            target="_blank">${customerKeyword.currentIndexCount}
                    </a></div>
                </td>
                <td align="center" width=50>
                    <div style="height:16;">${customerKeyword.initialPosition}
                    </div>
                </td>
                <td align="center" width=50>
                    <div style="height:16;"><a
                            href="${customerKeyword.searchEngineUrl}${customerKeyword.keyword}&pn=${customerKeyword.getPrepareBaiduPageNumber(customerKeyword.currentPosition)}"
                            target="_blank">${customerKeyword.currentPosition}</a>
                    </div>
                </td>
                <td align="center" width=30 onMouseMove="showTip('优化日期：<fmt:formatDate value="${customerKeyword.optimizeDate}" pattern="yyyy-MM-dd"/> ，要刷：${customerKeyword.optimizePlanCount}，已刷：${customerKeyword.optimizedCount}')"
                    onMouseOut="closeTip()">${customerKeyword.collectMethodName}
                </td>
                <td align="center" width=30>${customerKeyword.optimizePlanCount}</td>
                <td align="center" width=30>${customerKeyword.optimizedCount} </td>
                <td align="center" width=30>${customerKeyword.invalidRefreshCount}</td>
                <td align="center" width=60>${customerKeyword.feeString}</td>
                <td align="center" width=80><fmt:formatDate value="${customerKeyword.startOptimizedTime}" pattern="yyyy-MM-dd"/></td>
                <td align="center" width=80><fmt:formatDate value="${customerKeyword.lastOptimizeDateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td align="center" width=50>${customerKeyword.orderNumber}</td>
                <td align="center" width=100>${customerKeyword.remarks==null?"":customerKeyword.remarks} </td>
                <c:choose>
                    <c:when test="${user.vipType}">
                        <td align="center" width=60>${customerKeyword.optimizeGroupName == ''? "" : customerKeyword.optimizeGroupName}
                        </td>
                        <td align="center" width=80>
                            <a href="javascript:modifyCustomerKeyword('${customerKeyword.uuid}')">修改</a>|
                            <a href="javascript:delItem('${customerKeyword.uuid}')">删除</a>
                        </td>
                    </c:when>
                    <c:when test="${user.userLevel==1}">
                        <td align="center" width=80>
                            <a href="modify.jsp?uuid=${customerKeyword.uuid}">修改</a> |
                            <a href="javascript:delItem('${customerKeyword.uuid}')">删除</a>
                        </td>
                    </c:when>
                </c:choose>
            </tr>
        </c:forEach>
    </table>
</div>

<div id="showCustomerBottomPositioneDiv">
    <div id="showCustomerBottomDiv">
        <input id="fisrtButton" class="ui-button ui-widget ui-corner-all" type="button"
               onclick="changePaging(1,'${page.size}')" value="首页"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="upButton" type="button" class="ui-button ui-widget ui-corner-all"
               onclick="changePaging('${page.current-1}','${page.size}')" value="上一页"/>&nbsp;&nbsp;&nbsp;&nbsp;
        ${page.current}/${page.pages}&nbsp;&nbsp;
        <input id="nextButton" type="button" class="ui-button ui-widget ui-corner-all"
               onclick="changePaging('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')"
               value="下一页">&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="lastButton" type="button" class="ui-button ui-widget ui-corner-all"
               onclick="changePaging('${page.pages}','${page.size}')" value="末页">&nbsp;&nbsp;&nbsp;&nbsp;
        总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;
        每页显示条数:<select id="chooseRecords" onchange="changePaging(${page.current},this.value)">
        <option>10</option>
        <option>25</option>
        <option>50</option>
        <option>75</option>
        <option>100</option>
    </select>
    </div>
</div>
<%--Dialog部分--%>

<div id="groupChangeNameDialog"  style="text-align: center;" title="修改客户关键字组名">
    <form id="groupNameChangeFrom" style="text-align: center;margin-top: 10px;">
        目标组名称:<input type="text" id="groupName" name="groupName" style="width:150px">
    </form>
</div>
<div id="uploadExcelDailog"  style="text-align: left;height: 60px;" title="Excel文件上传">
    <form method="post" id="uploadExcelForm" style="margin-top: 10px"  enctype="multipart/form-data" >
        <input type="hidden" id="customerUuid" name="customerUuid" value="${customerKeywordCrilteria.customerUuid}">
        <span>请选择要上传的文件<label id="excelType" style="color: red"></label></span>
        <div style="height: 10px;"></div>
        <input type="file" id="uploadExcelFile" name="file" >
    </form>
</div>

<div id="saveCustomerKeywordDialog">
    <form id="customerKeywordForm">
        <ul>
            <input type="hidden" name="uuid" id="uuid" value="" style="width:300px;">
            <li><span class="customerKeywordSpanClass">关键字:</span><input type="text" name="keyword" id="keyword" value="" style="width:300px;"/>
            </li>

            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            <li><span class="customerKeywordSpanClass">标题:</span><input type="text" name="title" id="title" value="" style="width:300px;">
            </li>
            <li><span class="customerKeywordSpanClass">域名:</span><input type="text" name="url" id="url" value="" style="width:300px;">
            </li>
            <li><span class="customerKeywordSpanClass">原始域名:</span><input type="text" name="originalUrl" id="originalUrl" value=""
                                                                          style="width:300px;">
            </li>
            <li><span class="customerKeywordSpanClass">优化组名:</span>
                <input name="optimizeGroupName" id="optimizeGroupName" type="text"
                       style="width:115px;" value="">

                指数:<input type="text" id="initialIndexCount" size="5" name="initialIndexCount" value="100" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">
                排名:<input type="text" id="initialPosition" size="5" name="initialPosition" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)" >

            </li>
            <li>
                <ul style="float: left">
                    <li>
                        <span class="customerKeywordSpanClass">第一报价:</span><input name="positionFirstFee" id="positionFirstFee" value=""
                                                                                  style="width:100px;"
                                                                                  type="text" onkeyup="onlyNumber(this)" onblur="setSecondThirdDefaultFee(this)">元 </li>
                    <li><span class="customerKeywordSpanClass">第二报价:</span><input name="positionSecondFee"
                                                                                  id="positionSecondFee" value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li>
                    <li><span class="customerKeywordSpanClass">第三报价:</span><input name="positionThirdFee" id="positionThirdFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="setThirdDefaultFee(this)">元
                    </li>
                    <li><span class="customerKeywordSpanClass">第四报价:</span><input name="positionForthFee" id="positionForthFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li>
                    <li><span class="customerKeywordSpanClass">第五报价:</span><input name="positionFifthFee" id="positionFifthFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li>
                    <li><span class="customerKeywordSpanClass">首页报价:</span><input name="positionFirstPageFee" id="positionFirstPageFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li></ul>
                <c:if test="${user.vipType}">

                    <ul id="customerKeywordCost" style="float: left; width: 200px;height:170px;text-align: center">
                        <li><a href="javascript:showCustomerKeywordCost()">&nbsp;显示成本(再次点击关闭)</a></li>
                        <ul id="customerKeywordCostFrom" style="display: none;">
                            <li><span class="customerKeywordSpanClass">第一成本:</span><input name="positionFirstCost" id="positionFirstCost"
                                                                                          onBlur="setSecondThirdDefaultCost();" value=""
                                                                                          style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元 </li>
                            <li><span class="customerKeywordSpanClass">第二成本:</span><input name="positionSecondCost" id="positionSecondCost"
                                                                                          onBlur="setThirdDefaultCost();" value=""
                                                                                          style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                            <li><span class="customerKeywordSpanClass">第三成本:</span><input name="positionThirdCost" id="positionThirdCost"
                                                                                          value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                            <li><span class="customerKeywordSpanClass">第四成本:</span><input name="positionForthCost" id="positionForthCost"
                                                                                          value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                            <li><span class="customerKeywordSpanClass">第五成本:</span><input name="positionFifthCost" id="positionFifthCost"
                                                                                          value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                        </ul>
                    </ul>
                </c:if>
            </li>
            <c:if test="${user.vipType}">
                <li style="float:none"><span class="customerKeywordSpanClass">服务提供商:</span>
                    <select name="serviceProvider" id="serviceProvider" style="width: 296px">
                        <c:forEach items="${serviceProviders}" var="serviceProvider">
                            <option value="${serviceProvider.serviceProviderName}" <c:if test="${serviceProvider.serviceProviderName=='baidutop123'}">selected="selected"</c:if>>${serviceProvider.serviceProviderName}</option>
                        </c:forEach>
                    </select></li>
            </c:if>

            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            <li><span class="customerKeywordSpanClass">收费方式:</span>
                <select name="collectMethod" id="collectMethod" onChange="setEffectiveToTime()">
                    <option value="PerMonth" selected>按月</option>
                    <option value="PerTenDay">十天</option>
                    <option value="PerSevenDay">七天</option>
                    <option value="PerDay">按天</option>
                </select>
                搜索引擎:
                <select name="searchEngine" id="searchEngine" onChange="searchEngineChanged()" >
                    <option value="百度" selected>百度</option>
                    <option value="搜狗">搜狗</option>
                    <option value="360">360</option>
                    <option value="谷歌">谷歌</option>
                </select>
                <input type="hidden" id="status" name="status" value="1">
                排序:<input type="text" name="sequence" id="sequence" value="0" size="6" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">
            </li>
            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            <li>
                <span class="customerKeywordSpanClass">订单号:</span><input name="orderNumber" id="orderNumber" type="text" style="width:120px;" />
                <span class="customerKeywordSpanClass">收费状态:</span><select name="paymentStatus" id="paymentStatus">
                <option value="0"></option>
                <option value="1">担保中</option>
                <option value="2">已付费</option>
                <option value="3">未担保</option>
                <option value="4">跑路</option>
            </select>
            </li>
            <li>
                <span class="customerKeywordSpanClass" style="display: inline-block;float: left;height: 80px;">备注:</span><textarea name="remarks" id="remarks" style="width:300px;height:80px;resize: none" placeholder="请写备注吧!"></textarea>
            </li>
        </ul>
    </form>
</div>
</body>
</html>
