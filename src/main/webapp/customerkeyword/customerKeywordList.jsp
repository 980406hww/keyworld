<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@page contentType="text/html;charset=utf-8" %>

    <script language="javascript" type="text/javascript" src="/common.js"></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript" src="/js/slide1.12.4.js"></script>
    <link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link href="/css/menu.css" rel="stylesheet" type="text/css"/>

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

            #changeOptimizationGroupDialog {

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

            #customerKeywordDialog ul{list-style: none;margin: 0px;padding: 0px;}
            #customerKeywordDialog li{margin: 10px 0;}
            #customerKeywordDialog .customerKeywordSpanClass{width: 100px;display: inline-block;text-align: right;}
        </style>

        <script language="javascript">
            $(function () {
                $("#changeOptimizationGroupDialog").hide();
                $("#updateCustomerKeywordGroupNameDialog").hide();
                $("#uploadSimpleconDailog").hide();
                $("#uploadFullconDailog").hide();
                $("#customerKeywordDialog").hide();
                $("#customerKeywordDiv").css("margin-top",$("#customerKeywordTopDiv").height());
                pageLoad();
            });
            function pageLoad() {
                var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
                var searchCustomerKeywordTable = searchCustomerKeywordForm.find("#searchCustomerKeywordTable");
                searchCustomerKeywordTable.find("#orderElement").val('${customerKeywordCrilteria.orderElement}');
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

            function delItem(customerKeywordUuid) {
                if (confirm("确实要删除这个关键字吗?") == false) return;
                $.ajax({
                    url: '/internal/customerKeyword/deleteCustomerKeyword/' + customerKeywordUuid,
                    type: 'GET',
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

            function delAllItems(deleteType,customerUuid) {
                var uuids = getUuids();
                switch (deleteType){
                    case null :
                        if (uuids === '') {
                            alert('请选择要操作的信息');
                            return;
                        }
                        if (confirm("确实要删除这些关键字吗?") == false) return;break;
                    case 'emptyTitleAndUrl' :if (confirm("确实要删除标题和网址为空的关键字吗?") == false) return;break;
                    case 'emptyTitle' :if (confirm("确实要删除标题为空的关键字吗?") == false) return;break;
                }
                var postData = {};
                if(uuids ===''){

                }else{
                    postData.uuids = uuids.split(",");
                }
                postData.deleteType = deleteType;
                postData.customerUuid = customerUuid;
                $.ajax({
                    url: '/internal/customerKeyword/deleteCustomerKeywords',
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

            function resetTitle(customerUuid,resetType) {
                if(resetType == 'captureTitle'){
                    if (confirm("确实要重新采集标题?") == false) return;
                }else {
                    if (confirm("确实要清除所有标题?") == false) return;
                }
                var postData = {};
                postData.customerUuid = customerUuid;
                postData.status = '${customer.status}';
                postData.resetType = resetType;
//        alert(JSON.stringify(postData));
                $.ajax({
                    url: '/internal/customerKeyword/resetTitle',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (status) {
                        if (status) {
                            showInfo("操作成功！", self);
                            window.location.reload();
                        } else {
                            showInfo("操作失败！", self);
                            window.location.reload();
                        }
                    },
                    error: function () {
                        showInfo("操作失败！", self);
                        window.location.reload();
                    }
                });
            }

            function clearTitle(customerUuid, clearType) {
                var uuids = getUuids();
                if (clearType == null) {
                    if (uuids.trim() === '') {
                        alert("请选中要操作的关键词！");
                        return;
                    }
                }
                if (confirm("确认要清空标题吗?") == false) return;
                var postData = {};
                postData.uuids = uuids;
                postData.clearType = clearType;
                postData.customerUuid = customerUuid;

                $.ajax({
                    url: '/internal/customerKeyword/clearTitle',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (status) {
                        if (status) {
                            showInfo("操作成功！", self);
                            window.location.reload();
                        } else {
                            showInfo("操作失败！", self);
                        }
                    },
                    error: function () {
                        showInfo("操作失败！", self);
                    }
                });
            }

            function showInfo(content, e) {
                e = e || window.event;
                var div1 = document.getElementById('div2'); //将要弹出的层
                div1.innerText = content;
                div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见
                div1.style.left = getLeft(e) + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
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

            //弹出部分
            function checkinput(){
                var groupName = document.getElementById("groupName");
                if(trim(groupName.value) == ""){
                    alert("请输入目标组名");
                    return false;
                }
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

            //修改该用户关键字组名
            function updateCustomerKeywordGroupNameDialog(customerUuid,updateType) {
                $("#updateCustomerKeywordGroupNameDialog").dialog({
                    resizable: false,
                    width: 260,
                    height: 150,
                    modal: true,
                    //按钮
                    buttons: {
                        "保存": function () {
                            updateCustomerKeywordGroupName(customerUuid,updateType);
                            $(this).dialog("close");
                        },
                        "清空": function () {
                            $('#updateCustomerKeywordGroupNameFrom')[0].reset();
                        },
                        "取消": function () {
                            $(this).dialog("close");
                            $('#updateCustomerKeywordGroupNameFrom')[0].reset();
                        }
                    }
                });
            }

            function updateCustomerKeywordGroupName(customerUuid, updateType) {
                if (updateType == 'stop') {
                    if (confirm("确实要下架客户关键字吗?") == false) return;
                    var customerKeyword = {};
                    customerKeyword.customerUuid = customerUuid;
                    customerKeyword.optimizeGroupName = updateType;
                } else {
                    var customerKeyword = {};
                    var optimizeGroupName = $("#updateCustomerKeywordGroupNameFrom").find("#groupName").val();
                    customerKeyword.customerUuid = customerUuid;
                    customerKeyword.optimizeGroupName = optimizeGroupName;
                    if (optimizeGroupName == null || optimizeGroupName === '') {
                        alert("请输入分组名");
                        return;
                    }
                }
//        alert(JSON.stringify(customerKeyword));
                $.ajax({
                    url:'/internal/customerKeyword/updateCustomerKeywordGroupName',
                    data:JSON.stringify(customerKeyword),
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
                            window.location.reload();
                        }
                    },
                    error: function () {
                        showInfo("操作失败", self);
                        window.location.reload();
                    },
                });
            }

            function changeOptimizationGroupDialog(customerUuid) {
                var uuids = getUuids();
                if (uuids.trim() === '') {
                    alert("请选中要操作的关键词！");
                    return;
                }
                $("#changeOptimizationGroupDialog").dialog({
                    resizable: false,
                    width: 260,
                    height: 150,
                    modal: true,
                    //按钮
                    buttons: {
                        "保存": function () {
                            saveChangeOptimizationGroup();
                        },
                        "清空": function () {
                            $('#changeOptimizationGroupFrom')[0].reset();
                        },
                        "取消": function () {
                            $(this).dialog("close");
                            $('#changeOptimizationGroupFrom')[0].reset();
                        }
                    }
                });
            }
            function saveChangeOptimizationGroup() {
                var settingDialogDiv = $("#changeOptimizationGroupDialog");
                var optimizeGroupName = settingDialogDiv.find("#optimizationGroup").val();
                var uuids = getUuids();
                if (uuids==='') {
                    alert("请选中要操作的关键词！");
                    return;
                }
                if ($.trim(optimizationGroup)=== '') {
                    alert("请输入分组名称！");
                    return;
                }
                var postData = {};
                postData.customerKeywordUuids = uuids.split(",");
                postData.optimizeGroupName = optimizeGroupName;
                $.ajax({
                    url: '/internal/customerKeyword/changeOptimizationGroup',
                    data:JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if(result){
                            showInfo("操作成功", self);
                            window.location.reload();
                        } else {
                            showInfo("操作失败", self);
                            window.location.reload();
                        }
                    },
                    error: function () {
                        showInfo("更新失败！", self);
                        window.location.reload();
                    }
                });
                $("#changeOptimizationGroupDialog").dialog("close");
            }


            //增加新关键字
            function addCustomerKeyword(customerKeywordUuid) {
                if (customerKeywordUuid == null) {
                    $("#customerKeywordForm")[0].reset();
                    $("#customerKeywordForm").find("#uuid").val('');
                    $("#initialPositionSpan").html('');
                }
                $( "#customerKeywordDialog").dialog({
                    width: 600,
                    height: 630,
                    /*position: {
                     my: "center",
//                     at: "c",
                     of: window},*/
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
                customerKeyword.uuid =$("#customerKeywordDialog #uuid").val();
                customerKeyword.customerUuid=customerUuid;
                customerKeyword.searchEngine = $("#customerKeywordDialog #searchEngine").val();

                var initialIndexCount = $("#customerKeywordDialog #initialIndexCount").val();
                customerKeyword.initialIndexCount = initialIndexCount;
                var reg = /^www\..*\.com/;

                var keyword = $.trim($("#customerKeywordDialog #keyword").val());
                if (keyword == '') {
                    alert("关键字不能为空");
                    $("#customerKeywordDialog #keyword").focus();
                    return;
                }
                else {
                    customerKeyword.keyword = keyword;

                }

                var url = $.trim($("#customerKeywordDialog #url").val())
                if (!reg.test(url)) {
                    alert("网址不符合要求！");
                    $("#customerKeywordDialog #url").focus();
                    return;
                }
                else {
                    customerKeyword.url = url;

                }
                var originalUrl = $.trim($("#customerKeywordDialog #originalUrl").val());
                if (originalUrl!=''&&!reg.test(originalUrl)) {
                    alert("网址不符合要求！");
                    $("#customerKeywordDialog #originalUrl").focus();
                    return;
                }
                else {
                    customerKeyword.originalUrl = originalUrl;
                }

                //var regNumber=/^([1-9]\d*)|0$/;
                //var regu = /^[1-9]\d*|0$/
                var regNumber = /^\d+$/;

                var positionFirstFee = $.trim($("#customerKeywordDialog #positionFirstFee").val());

                if (positionFirstFee != '' && (!regNumber.test(positionFirstFee))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionFirstFee").focus();
                    return;
                }
                else {
                    customerKeyword.positionFirstFee = positionFirstFee;
                }
                var positionSecondFee = $.trim($("#customerKeywordDialog #positionSecondFee").val());

                if (positionSecondFee != '' && (!regNumber.test(positionSecondFee))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionSecondFee").focus();
                    return;
                }
                else {
                    customerKeyword.positionSecondFee = positionSecondFee;
                }


                var positionThirdFee = $.trim($("#customerKeywordDialog #positionThirdFee").val());

                if (positionThirdFee != '' && (!regNumber.test(positionThirdFee))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #thirdFee").focus();
                    return;
                }
                else {
                    customerKeyword.positionThirdFee = positionThirdFee;
                }
                var positionForthFee = $.trim($("#customerKeywordDialog #positionForthFee").val());

                if (positionForthFee != '' && (!regNumber.test(positionForthFee))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionForthFee").focus();
                    return;
                }
                else {
                    customerKeyword.positionForthFee = positionForthFee;
                }
                var positionFifthFee = $.trim($("#customerKeywordDialog #positionFifthFee").val());

                if (positionFifthFee != '' && (!regNumber.test(positionFifthFee))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionFifthFee").focus();
                    return;
                }
                else {
                    customerKeyword.positionFifthFee = positionFifthFee;
                }
                var positionFirstPageFee = $.trim($("#customerKeywordDialog #positionFirstPageFee").val());

                if (positionFirstPageFee != '' && (!regNumber.test(positionFirstPageFee))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionFirstPageFee").focus();
                    return;
                }
                else {
                    customerKeyword.positionFirstPageFee = positionFirstPageFee;
                }


                var positionFirstCost = $.trim($("#customerKeywordDialog #positionFirstCost").val());

                if (positionFirstCost != '' && (!regNumber.test(positionFirstCost))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionFirstCost").focus();
                    return;
                }
                else {
                    customerKeyword.positionFirstCost = positionFirstCost;
                }
                var positionSecondCost = $.trim($("#customerKeywordDialog #positionSecondCost").val());

                if (positionSecondCost != '' && (!regNumber.test(positionSecondCost))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionSecondCost").focus();
                    return;
                }
                else {
                    customerKeyword.positionSecondCost = positionSecondCost;
                }


                var positionThirdCost = $.trim($("#customerKeywordDialog #positionThirdCost").val());

                if (positionThirdCost != '' && (!regNumber.test(positionThirdCost))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #thirdCost").focus();
                    return;
                }
                else {
                    customerKeyword.positionThirdCost = positionThirdCost;
                }
                var positionForthCost = $.trim($("#customerKeywordDialog #positionForthCost").val());

                if (positionForthCost != '' && (!regNumber.test(positionForthCost))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionForthCost").focus();
                    return;
                }
                else {
                    customerKeyword.positionForthCost = positionForthCost;
                }
                var positionFifthCost = $.trim($("#customerKeywordDialog #positionFifthCost").val());

                if (positionFifthCost != '' && (!regNumber.test(positionFifthCost))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionFifthCost").focus();
                    return;
                }
                else {
                    customerKeyword.positionFifthCost = positionFifthCost;
                }
                var positionFirstPageCost = $.trim($("#customerKeywordDialog #firstPageCost").val());

                if (positionFirstPageCost != '' && (!regNumber.test(positionFirstPageCost))) {
                    alert("只能输入正整数");
                    $("#customerKeywordDialog #positionFirstPageCost").focus();
                    return;
                }
                else {
                    customerKeyword.positionFirstPageCost = positionFirstPageCost;
                }

                var sequence = $.trim($("#customerKeywordDialog #sequence").val());
                customerKeyword.sequence = sequence;
                var title = $.trim($("#customerKeywordDialog #title").val());
                customerKeyword.title = title;
                var optimizeGroupName=$.trim($("#customerKeywordDialog #optimizeGroupName").val());
                customerKeyword.optimizeGroupName=optimizeGroupName;
                var collectMethod=$.trim($("#customerKeywordDialog #collectMethod").val());
                customerKeyword.collectMethod=collectMethod;
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
                $(this).dialog("close");
            }

            //修改关键字
            function modifyCustomerKeyword(customerKeywordUuid) {
                $.ajax({
                    url: '/internal/customerKeyword/getCustomerKeywordByCustomerKeywordUuid/' + customerKeywordUuid,
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 50000,
                    type: 'POST',
                    success: function (customerKeyword) {
//                alert(JSON.(customerKeyword));
                        if (customerKeyword!=null) {
                            $("#customerKeywordDialog #uuid").val(customerKeyword.uuid);
                            $("#customerKeywordDialog #keyword").val(customerKeyword.keyword);
                            $("#customerKeywordDialog #searchEngine").val(customerKeyword.searchEngine);
                            $("#customerKeywordDialog #initialIndexCount").val(customerKeyword.currentIndexCount);
                            $("#customerKeywordDialog #initialPositionSpan").text(customerKeyword.currentPosition==null?'':customerKeyword.currentPosition);
                            $("#customerKeywordDialog #url").val(customerKeyword.url);
                            $("#customerKeywordDialog #originalUrl").val(customerKeyword.originalUrl);
                            $("#customerKeywordDialog #positionFirstFee").val(customerKeyword.positionFirstFee);
                            $("#customerKeywordDialog #positionSecondFee").val(customerKeyword.positionSecondFee);
                            $("#customerKeywordDialog #positionThirdFee").val(customerKeyword.positionThirdFee);
                            $("#customerKeywordDialog #positionForthFee").val(customerKeyword.positionForthFee);
                            $("#customerKeywordDialog #positionFifthFee").val(customerKeyword.positionFifthFee);
                            $("#customerKeywordDialog #positionFirstPageFee").val(customerKeyword.positionFirstPageFee);
                            $("#customerKeywordDialog #positionFirstCost").val(customerKeyword.positionFirstCost);
                            $("#customerKeywordDialog #positionSecondCost").val(customerKeyword.positionSecondCost);
                            $("#customerKeywordDialog #positionThirdCost").val(customerKeyword.positionThirdCost);
                            $("#customerKeywordDialog #positionForthCost").val(customerKeyword.positionForthCost);
                            $("#customerKeywordDialog #positionFifthCost").val(customerKeyword.positionFifthCost);

                            $("#customerKeywordDialog #serviceProvider").val(customerKeyword.serviceProvider);
                            $("customerKeywordDialog #sequence").val(customerKeyword.sequence);
                            $("#customerKeywordDialog #title").val(customerKeyword.title);
                            $("#customerKeywordDialog #optimizeGroupName").val(customerKeyword.optimizeGroupName);
                            $("#customerKeywordDialog #relatedKeywords").val(customerKeyword.relatedKeywords);
                            addCustomerKeyword(customerKeywordUuid);
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
            function showCustomerKeywordCost() {
                $("#customerKeywordCost #customerKeywordCostFrom").toggle();
            }
            //关键字Excel上传(简化版)
            function uploadsimple(uuid,self){
                $('#uploadsimpleconForm')[0].reset();
                $("#uploadSimpleconDailog").dialog({
                    resizable: false,
                    width: 400,
                    height: 200,
                    modal: true,
                    //按钮
                    buttons: {
                        "提交": function () {
                            var uploadForm = $("#uploadsimpleconForm");
                            var uploadFile = uploadForm.find("#uploadsimpleconFile").val();
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
                            formData.append('file', uploadForm.find('#uploadsimpleconFile')[0].files[0]);
                            formData.append('customerUuid', uuid);
                            if (fileTypeFlag) {
                                $.ajax({
                                    url: '/internal/customerKeyword/uploadsimplecon',
                                    type: 'POST',
                                    cache: false,
                                    data: formData,
                                    processData: false,
                                    contentType: false,
                                    success: function (result) {
                                        if (result) {
                                            showInfo("上传成功", self);
                                            window.location.reload();
                                        } else {
                                            showInfo("上传失败", self);
                                            window.location.reload();
                                        }
                                    },
                                    error: function () {
                                        showInfo("上传失败", self);
                                        window.location.reload();
                                    }
                                });
                            }
                            $(this).dialog("close");
                        },
                        "取消": function () {
                            $(this).dialog("close");
                            $('#uploadsimpleconForm')[0].reset();
                        }
                    }
                });
            }
            //简化版模板下载

            //关键字Excel上传(完整版)
            function uploadFull(customerUuid,self) {
                $('#uploadFullconForm')[0].reset();
                $("#uploadFullconDailog").dialog({
                    resizable: false,
                    width: 400,
                    height: 200,
                    modal: true,
                    title: '关键字Excel上传(完整版)',
                    //按钮
                    buttons: {
                        "提交": function () {
                            var uploadForm = $("#uploadFullconForm");
                            var uploadFile = uploadForm.find("#uploadFullconFile").val();
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
                            formData.append('file', uploadForm.find('#uploadFullconFile')[0].files[0]);
                            formData.append('customerUuid', customerUuid);
                            if (fileTypeFlag) {
                                $.ajax({
                                    url: '/internal/customerKeyword/uploadFullcon',
                                    type: 'POST',
                                    cache: false,
                                    data: formData,
                                    processData: false,
                                    contentType: false,
                                    success: function (result) {
                                        if (result) {
                                            showInfo("上传成功", self);
                                            window.location.reload();
                                        } else {
                                            showInfo("上传失败", self);
                                            window.location.reload();
                                        }
                                    },
                                    error: function () {
                                        showInfo("上传失败", self);
                                        window.location.reload();
                                    }
                                });
                            }
                            $(this).dialog("close");
                        },
                        "取消": function () {
                            $(this).dialog("close");
                            $('#uploadsimpleconForm')[0].reset();
                        }
                    }
                });
            }
            //导出结果
            function downloadCustomerKeywordInfo() {
                var customerKeywordCrilteria = $("#searchCustomerKeywordForm").serialize().trim();
                location.href='/internal/customerKeyword/downloadCustomerKeywordInfo/?'+customerKeywordCrilteria;
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
        <a href="javascript:updateCustomerKeywordGroupNameDialog('${customerKeywordCrilteria.customerUuid}',null)">修改所有分组</a>
        | <a target="_blank" href="javascript:changeOptimizationGroupDialog(this)">修改选中分组</a>
        | <a href="javascript:updateCustomerKeywordGroupName('${customerKeywordCrilteria.customerUuid}','stop')">下架所有关键字</a>
        | <a target="_blank" href="javascript:uploadsimple('${customerKeywordCrilteria.customerUuid}',this)"/>Excel上传(简化版)</a>
        | <a target="_blank" href="/SuperUserSimpleKeywordList.xls">简化版下载</a>
        | <a target="_blank" href="javascript:uploadFull('${customerKeywordCrilteria.customerUuid}')">Excel上传(完整版)</a>
        | <a target="_blank" href="/SuperUserFullKeywordList.xls">完整版下载</a>
        | <a target="_blank" href="/internal/dailyReport/downloadSingleCustomerReport/${customerKeywordCrilteria.customerUuid}">导出日报表</a>
        | <a target="_blank" href="javascript:downloadCustomerKeywordInfo()">导出结果</a>
        <br/>
        <a href="javascript:delAllItems('emptyTitleAndUrl','${customerKeywordCrilteria.customerUuid}')">删除标题和网址为空的关键字</a> |
        <a href="javascript:delAllItems('emptyTitle','${customerKeywordCrilteria.customerUuid}')">删除标题为空的关键字</a> |
        <a href="javascript:resetTitle('${customerKeywordCrilteria.customerUuid}','captureTitle')">重采标题</a> |
        <a href="javascript:clearTitle('${customerKeywordCrilteria.customerUuid}', null)">清空所选标题</a>|
        <a href="javascript:resetTitle('${customerKeywordCrilteria.customerUuid}', 'all')">清空客户标题</a>
    </div>
    <br/>
    <form id="searchCustomerKeywordForm" style="font-size:12px; width: 100%;" action="/internal/customerKeyword/searchCustomerKeywords" method="post">
        <div id="searchCustomerKeywordTable">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            <input id="customerUuid" name="customerUuid" type="hidden" value="${customerKeywordCrilteria.customerUuid} ">
            关键字:<input type="text" name="keyword" id="keyword" value="${customerKeywordCrilteria.keyword}"
                       style="width:90px;">
            URL:<input type="text" name="url" id="url" value="${customerKeywordCrilteria.url}"
                       style="width:120px;">
            添加时间:<input name="creationFromTime" id="creationFromTime" class="Wdate"
                        type="text" style="width:90px;" onClick="WdatePicker()"
                        value="${customerKeywordCrilteria.creationFromTime}">
            到<input
                name="creationToTime"
                id="creationToTime"
                class="Wdate" type="text"
                style="width:90px;"
                onClick="WdatePicker()"
                value="${customerKeywordCrilteria.creationToTime} ">
            关键字状态:
            <select name="status" id="status">
                <option value='1'>激活</option>
                <option value='2'>新增</option>
                <option value='0'>过期</option>
            </select>
            优化组名:
            <input type="text" name="optimizeGroupName" id="optimizeGroupName"
                   value="${customerKeywordCrilteria.optimizeGroupName} " style="width:60px;">
            <%--<c:if test="${!user.vipType}">--%>
            显示前:
            <input type="text" name="position" id="position" value="${customerKeywordCrilteria.position}"
                   style="width:50px;">
            排序:
            <select name="orderElement" id="orderElement">
                <option value="">--请选择排序--</option>
                <option value="fCreateTime">创建日期</option>
                <option value="fCurrentPosition">当前排名</option>
            </select>
            无效点击数:
            <input type="text" name="invalidRefreshCount" id="invalidRefreshCount"
                   value="${customerKeywordCrilteria.invalidRefreshCount} " style="width:50px;">
            <%--</c:if>--%>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()"
                   value=" 查询 ">&nbsp;&nbsp;
            <input type="button" class="ui-button ui-widget ui-corner-all" onclick="addCustomerKeyword()"
                   value=" 增加 ">&nbsp;&nbsp;
            <input type="button" class="ui-button ui-widget ui-corner-all"
                   onclick="delAllItems(null,'${customerKeywordCrilteria.customerUuid}')"
                   value=" 删除所选 ">
        </div>
    </form>
    <%--</c:if>--%>
    <table style="font-size:12px; width: 100%;">
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
                    <font color="<%--<%=keywordColor%>--%>">${customerKeyword.keyword}
                    </font>
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
                            href="/internal/customerKeyword/historyPositionAndIndex/${customerKeyword.uuid}/30"
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


                <td align="center" width=60>${value.feeString}
                </td>
                <td align="center" width=80><fmt:formatDate value="${customerKeyword.startOptimizedTime}" pattern="yyyy-MM-dd"/></td>
                <td align="center" width=80><fmt:formatDate value="${customerKeyword.lastOptimizeDateTime}" pattern="yyyy-MM-dd  HH:mm"/></td>

                <td align="center" width=50>${customerKeyword.orderNumber}
                </td>
                <td align="center" width=100>${customerKeyword.remarks==null?"":customerKeyword.remarks}
                </td>
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

<div style="display:none;">
    <script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>
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
<div id="changeOptimizationGroupDialog"  style="text-align: center;" title="修改选中关键字组名">
    <form id="changeOptimizationGroupFrom" style="text-align: center;margin-top: 10px;">
        分组名称<input type="text" name="optimizationGroup" id="optimizationGroup" style="width:150px"/>
    </form>
</div>

<div id="updateCustomerKeywordGroupNameDialog"  style="text-align: center;" title="修改客户关键字组名">
    <form id="updateCustomerKeywordGroupNameFrom" style="text-align: center;margin-top: 10px;">
        目标组名称:<input type="text" id="groupName" name="groupName" style="width:150px">
    </form>
</div>
<div id="uploadSimpleconDailog"  style="text-align: center;height: 60px;"  title="Excel上传(简化版)">
    <form method="post" id="uploadsimpleconForm"  enctype="multipart/form-data" >
        <input type="hidden" id="customerUuid" name="customerUuid" value="${customerKeywordCrilteria.customerUuid}">
        请选择要上传的文件(<font color="red">简化版</font>):<input type="file" id="uploadsimpleconFile" name="file" >
    </form>
</div>
<div id="uploadFullconDailog" style="text-align: center;height: 60px;"   title="Excel上传(完整版)">
    <form method="post" id="uploadFullconForm" enctype="multipart/form-data">
        <input type="hidden" id="customerUuid" name="customerUuid" value="${customerKeywordCrilteria.customerUuid}">
        请选择要上传的文件(<font color="red">完整版</font>):<input type="file" id="uploadFullconFile" name="file">
    </form>
</div>
<div id="customerKeywordDialog">
    <form id="customerKeywordForm">
        <ul>
            <input type="hidden" name="uuid" id="uuid" value="" style="width:300px;">
            <li><span class="customerKeywordSpanClass">关键字:</span><input type="text" name="keyword" id="keyword" value=""
                                                                         style="width:300px;">
            </li>
            <li><span class="customerKeywordSpanClass">搜索引擎:</span>
                <select name="searchEngine" id="searchEngine" onChange="searchEngineChanged();">
                    <option value="百度" selected>百度</option>
                    <option value="搜狗">搜狗</option>
                    <option value="360">360</option>
                    <option value="谷歌">谷歌</option>
                </select>
                <input type="hidden" id="initialPosition" name="initialPosition" value="">
                当前指数:<input type="text" id="initialIndexCount" size="6" name="initialIndexCount" value="100">
                <font color="red"><span id="initialIndexCountSpan"></span></font> 当前排名:<font color="red"><span
                        id="initialPositionSpan"></span></font></li>

            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>

            <li><span class="customerKeywordSpanClass">域名:</span><input type="text" name="url" id="url" value="" style="width:300px;">
                </li>
            <li><span class="customerKeywordSpanClass">原始域名:</span><input type="text" name="originalUrl" id="originalUrl" value=""
                                                                            style="width:300px;"><span style="color: red;text-transform: none">
                </li>
            <li>
                <ul style="float: left"><li>
                    <span class="customerKeywordSpanClass">第一报价:</span><input name="positionFirstFee" id="positionFirstFee" value=""
                                                                                onBlur="setSecondThirdDefaultFee();" style="width:100px;"
                                                                                type="text">元 </li>
                    <li><span class="customerKeywordSpanClass">第二报价:</span><input name="positionSecondFee" onBlur="setThirdDefaultFee();"
                                                                                    id="positionSecondFee" value="" style="width:100px;" type="text">元
                    </li>
                    <li><span class="customerKeywordSpanClass">第三报价:</span><input name="positionThirdFee" id="positionThirdFee" value=""
                                                                                    style="width:100px;" type="text">元
                    </li>
                    <li><span class="customerKeywordSpanClass">第四报价:</span><input name="positionForthFee" id="positionForthFee" value="0"
                                                                                    style="width:100px;" type="text">元
                    </li>
                    <li><span class="customerKeywordSpanClass">第五报价:</span><input name="positionFifthFee" id="positionFifthFee" value="0"
                                                                                    style="width:100px;" type="text">元
                    </li>
                    <li><span class="customerKeywordSpanClass">首页报价:</span><input name="positionFirstPageFee" id="positionFirstPageFee" value="0"
                                                                                    style="width:100px;" type="text">元
                    </li></ul>
                <c:if test="${user.vipType}">

                    <ul id="customerKeywordCost" style="float: left; width: 300px;height:200px;">
                        <li><span class="customerKeywordSpanClass"></span><a href="javascript:showCustomerKeywordCost()">显示成本(再次点击关闭)</a></li>
                        <ul id="customerKeywordCostFrom" style="margin-left: 50px;display: none;">
                            <li><span class="customerKeywordSpanClass">第一成本:</span><input name="positionFirstCost" id="positionFirstCost"
                                                                                            onBlur="setSecondThirdDefaultCost();" value="0"
                                                                                            style="width:100px;" type="text">元 </li>
                            <li><span class="customerKeywordSpanClass">第二成本:</span><input name="positionSecondCost" id="positionSecondCost"
                                                                                            onBlur="setThirdDefaultCost();" value="0"
                                                                                            style="width:100px;" type="text">元
                            </li>
                            <li><span class="customerKeywordSpanClass">第三成本:</span><input name="positionThirdCost" id="positionThirdCost"
                                                                                            value="0" style="width:100px;" type="text">元
                            </li>
                            <li><span class="customerKeywordSpanClass">第四成本:</span><input name="positionForthCost" id="positionForthCost"
                                                                                            value="0" style="width:100px;" type="text">元
                            </li>
                            <li><span class="customerKeywordSpanClass">第五成本:</span><input name="positionFifthCost" id="positionFifthCost"
                                                                                            value="0" style="width:100px;" type="text">元
                            </li>
                        </ul>
                    </ul>
                </c:if>
            </li>
            <c:if test="${user.vipType}">
                <li><span class="customerKeywordSpanClass">服务提供商:</span>
                    <select name="serviceProvider" id="serviceProvider">
                        <option value=""></option>
                        <c:forEach items="${serviceProviders}" var="serviceProvider">
                            <option value="${serviceProvider.serviceProviderName}">${serviceProvider.serviceProviderName}</option>
                        </c:forEach>

                    </select></li>
            </c:if>
            <li><span class="customerKeywordSpanClass">排序:</span><input type="text" name="sequence" id="sequence" value="0"
                                                                        style="width:300px;"></li>
            <li><span class="customerKeywordSpanClass">标题:</span><input type="text" name="title" id="title" value="" style="width:300px;">
            </li>
            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            <li><span class="customerKeywordSpanClass">优化组名:</span><input name="optimizeGroupName" id="optimizeGroupName" type="text"
                                                                          style="width:200px;" value="">
                <input type="hidden" name="relatedKeywords" id="relatedKeywords" value=""></li>
            <li><span class="customerKeywordSpanClass">收费方式:</span>
                <select name="collectMethod" id="collectMethod" onChange="setEffectiveToTime();">
                    <option value="PerMonth" selected>按月</option>
                    <option value="PerTenDay">十天</option>
                    <option value="PerSevenDay">七天</option>
                    <option value="PerDay">按天</option>
                </select>
                <input type="hidden" id="status" name="status" value="1">
            </li>
        </ul>
    </form>
</div>
</body>
</html>
