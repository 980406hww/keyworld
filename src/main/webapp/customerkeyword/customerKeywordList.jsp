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
        width: 100%;
        height: 25%;
        margin: auto;
        z-index: 10;
    }

    #customerKeywordDiv {
        overflow: scroll;
        width: 100%;
        height: 80%;
        margin: auto;
    }

    #showCustomerBottomDiv {
        margin-right: 2%;
        float: right;
        width: 580px;
        height: 5%;
    }

    #customerKeywordDialog ul{list-style: none;margin: 0px;padding: 0px}
    #customerKeywordDialog li{margin: 10px 0;}
    #customerKeywordDialog .customerKeywordSpanClass{width: 100px;display: inline-block;text-align: right;}
</style>

<script language="javascript">
    $(function () {
        $("#changeOptimizationGroupDialog").hide();
        $("#updateCustomerKeywordGroupNameDialog").hide();
        $("#uploadSimpleconDailog").hide();
        $("#uploadFullconDailog").hide();
//        $("#customerKeywordDialog").hide();
        pageLoad();

        $( "#customerKeywordDialog").dialog({
            autoOpen : false,
            width: 600,
            height: 630,
            position: {
                my: "center",
                at: "center center-250px",
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
                    saveCustomerKeyword("${customer.uuid}");
                    $(this).dialog("close");
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
    });
    function pageLoad() {
        var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
        var searchCustomerKeywordTable = searchCustomerKeywordForm.find("#searchCustomerKeywordTable");
         searchCustomerKeywordTable.find("#orderElement").val(${customerKeywordCrilteria.orderElement});
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

    function delItem(customerKeywordUuid) {
        if (confirm("确实要删除这个关键字吗?") == false) return;
        $.ajax({
            url: '/internal/customerKeyword/delelteCustomerKeyword/' + customerKeywordUuid,
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
        var total = searchCustomerKeywordForm.find("#totalHidden").val();
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
            width: 500,
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
            width: 500,
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
    function addCustomerKeyword(customerUuid) {
        /*$( "#customerKeywordDialog").dialog({
            width: 600,
            height: 630,
            position: {
                my: "center",
                at: "center center-250px",
                of: window},
            title : "添加关键字",
            show: {
                effect: "blind",
                /!* duration: 1000*!/
            },
            hide: {
                effect: "blind",
                /!*duration: 1000*!/
            },
            modal: true,
            resizable: false,
            buttons: {
                "保存": function () {
                    saveCustomerKeyword("<%--${customer.uuid}--%>");
                },
                "清空": function () {
                    $('#customerKeywordForm')[0].reset();
                },
                "取消": function () {
                    $(this).dialog("close");
                    $('#customerKeywordForm')[0].reset();
                }
            }
        });*/
        $("#customerKeywordForm")[0].reset();
        $( "#customerKeywordDialog").dialog("open");

    }

    function saveCustomerKeyword(customerUuid) {
        var customerKeyword = {};

        customerKeyword.uuid =$("#customerKeywordDialog #uuid").val();
        customerKeyword.customerUuid=customerUuid;
        customerKeyword.searchEngine = $("#customerKeywordDialog #searchEngine").val();

        var initialIndexCount = $("#customerKeywordDialog #initialIndexCount").val();
        customerKeyword.initialIndexCount = initialIndexCount;
        var reg = /^www\..*\.com$/;

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
        if (!reg.test(originalUrl)) {
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
        //var postDate = [];
        //postDate.customerKeyword = customerKeyword;
        alert(JSON.stringify(customerKeyword));
        $.ajax({
            url: '/internal/customerKeyword/saveCustomerKeyword',
//            data: JSON.stringify(postDate),
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
                }
            },
            error: function () {
                showInfo("操作失败", self);
            }
        });
    }

    //修改关键字
    function modifyCustomerKeyword(customerKeywordUuid) {
        $("#customerKeywordForm")[0].reset();
        $.ajax({
            url: '/internal/customerKeyword/getCustomerKeywordByCustomerKeywordUuid/' + customerKeywordUuid,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (customerKeyword) {
                if (customerKeyword!=null) {
                    $("#customerKeywordDialog #uuid").val(customerKeyword.uuid);
                    $("#customerKeywordDialog #keyword").val(customerKeyword.keyword);
                    $("#customerKeywordDialog #searchEngine").val(customerKeyword.searchEngine);
                    $("#customerKeywordDialog #initialIndexCount").val(customerKeyword.currentIndexCount);
                    $("#customerKeywordDialog #initialPositionSpan").text(customerKeyword.currentPosition);
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
                    $("customerKeywordDialog #title").val(customerKeyword.title);
                    $("customerKeywordDialog #optimizeGroupName").val(customerKeyword.optimizeGroupName);
                    $("customerKeywordDialog #relatedKeywords").val(customerKeyword.relatedKeywords);

                    $("#customerKeywordDialog").dialog();

                    showInfo("操作成功", self);
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
        $("#customerKeywordCost").toggle();
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
                                } else {
                                    showInfo("上传失败", self);
                                }
                            },
                            error: function () {
                                showInfo("上传失败", self);
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
                        formData.append('customerUuid', uuid);
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
                                    } else {
                                        showInfo("上传失败", self);
                                    }
                                },
                                error: function () {
                                    showInfo("上传失败", self);
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
    //完整版模板下载

    //上传日报表模板
    function uploaddailyreporttemplate(customerUuid) {

    }
    //导出日报表
    function downloadSingleCustomerReport(customerUuid) {

    }
    //导出结果

</script>

</head>
<body>
<div id="customerKeywordTopDiv">
    <table width=100% style="font-size:12px;" cellpadding=3>
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
    <table>
        <tr>
            <td colspan=18 align="right">
                <a href="javascript:updateCustomerKeywordGroupNameDialog('${customer.uuid}',null)">修改所有分组</a>
                | <a target="_blank" href="javascript:changeOptimizationGroupDialog(this)">修改选中分组</a>
                | <a href="javascript:updateCustomerKeywordGroupName('${customer.uuid}','stop')">下架所有关键字</a>
                | <a href="javascript:addCustomerKeyword('${customer.uuid}')">增加</a>
                | <a target="_blank" href="javascript:uploadsimple('${customer.uuid}',this)"/>Excel上传(简化版)</a>
                | <a target="_blank" href="/SuperUserSimpleKeywordList.xls">简化版模板下载</a>
                | <a target="_blank" href="javascript:uploadFull('${customer.uuid}')">xcel上传(完整版)</a>
                | <a target="_blank" href="/SuperUserFullKeywordList.xls">完整版模板下载</a>
                | <a target="_blank" href="javascript:uploaddailyreporttemplate('${customer.uuid}')">上传日报表模板</a>
                | <a target="_blank" href="javascript:downloadSingleCustomerReport('${customer.uuid}')">导出日报表</a>
                | <a target="_blank"
                     href='/internal/customerKeyword/DownloadCustomerKeywordInfo.jsp?fileName=CustomerKeywordInfo<%--<%="_" + Utils.getCurrentDate()%>.xls&<%=pageUrl%>--%>'>导出结果</a>
            </td>
        </tr>
    </table>
    <form id="searchCustomerKeywordForm" action="/internal/customerKeyword/searchCustomerKeywords" method="post">
        <table style="font-size:12px;" id="searchCustomerKeywordTable">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            <tr>
                <td align="right">关键字:</td>
                <td><input type="text" name="keyword" id="keyword" value="${customerKeywordCrilteria.keyword}"
                           style="width:90px;">
                </td>
                <td align="right">URL:</td>
                <td><input type="text" name="url" id="url" value="${customerKeywordCrilteria.url}"
                           style="width:120px;"></td>
                <td>
                    <input id="customerUuid" name="customerUuid" type="hidden"
                           value="${customer.uuid} ">
                </td>
                <td align="right">添加时间:<input name="creationFromTime" id="creationFromTime" class="Wdate"
                                              type="text" style="width:90px;" onClick="WdatePicker()"
                                              value="${customerKeywordCrilteria.creationFromTime}">
                    <%--<fmt:formatDate value='${customerKeywordCrilteria.creationFromTime}' pattern='yyyy-MM-dd' />--%>
                    到<input
                            name="creationToTime"
                            id="creationToTime"
                            class="Wdate" type="text"
                            style="width:90px;"
                            onClick="WdatePicker()"
                            value="${customerKeywordCrilteria.creationToTime} ">
                </td>
                <td align="right">关键字状态:</td>
                <td>
                    <select name="status" id="status">
                        <option value='1'>激活</option>
                        <option value='2'>新增</option>
                        <option value='0'>过期</option>
                    </select>
                </td>
                <td align="right">优化组名:</td>
                <td><input type="text" name="optimizeGroupName" id="optimizeGroupName"
                           value="${customerKeywordCrilteria.optimizeGroupName} " style="width:60px;"></td>
            </tr>
            <tr>
                <%--<c:if test="${!user.vipType}">--%>
                <td align="right">显示前:</td>
                <td><input type="text" name="position" id="position" value="${customerKeywordCrilteria.position}"
                           style="width:20px;">
                </td>
                <td align="right">排序:</td>
                <td>
                    <select name="orderElement" id="orderElement">
                        <option value="">--请选择排序--</option>
                        <option value="0">创建日期</option>
                        <option value="1">当前排名</option>
                    </select>
                </td>
                <td align="right">无效点击数:</td>
                <td><input type="text" name="invalidRefreshCount" id="invalidRefreshCount"
                           value="${customerKeywordCrilteria.invalidRefreshCount} " style="width:160px;"></td>
                <%--</c:if>--%>
                <td align="right" width="100">
                    <input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()"
                           value=" 查询 ">
                </td>
            </tr>
        </table>
    </form>
    <table>
        <tr>
            <td align="right"><a href="javascript:delAllItems(null,'${customer.uuid}')">删除所选</a> |
                <a href="javascript:delAllItems('emptyTitleAndUrl','${customer.uuid}')">删除标题和网址为空的关键字</a> |
                <a href="javascript:delAllItems('emptyTitle','${customerUuid}')">删除标题为空的关键字</a> |
                <a href="javascript:resetTitle('${customer.uuid}','captureTitle')">重采标题</a> |
                <a href="javascript:clearTitle('${customer.uuid}', null)">清空所选标题</a>
                |
                <a href="javascript:resetTitle('${customer.uuid}', 'all')">清空当前客户标题</a>
            </td>
        </tr>
    </table>
    <%--</c:if>--%>
</div>
    <hr>
    <div id="customerKeywordDiv">
        <table>
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
            <c:forEach items="${page.records}" var="customerKeyword">
                <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                    <td><input type="checkbox" name="uuid" value="${customerKeyword.uuid}"/></td>
                    <td>
                        <font color="<%--<%=keywordColor%>--%>">${customerKeyword.keyword}
                        </font>
                    </td>
                    <td class="wrap"
                        onMouseMove="showTip('原始URL:${customerKeyword.originalUrl != null ?customerKeyword.originalUrl : customerKeyword.url}')"
                        onMouseOut="closeTip()">
                        <div style="height:16;">
                               <%-- ${isNullOrEmpty(customerKeyword.url)? '' : '' };--%>
                        </div>
                    </td>

                    <td>
                            ${customerKeyword.title == null ? "" : customerKeyword.title.trim()}
                    </td>

                    <td>
                        <div style="height:16;"><a
                                href="/internal/customerKeyword/historyPositionAndIndex.jsp?type=PC&uuid=${customerKeyword.uuid}>"
                                target="_blank">${customerKeyword.currentIndexCount}
                        </a></div>
                    </td>

                    <td>
                        <div style="height:16;">${customerKeyword.initialPosition}
                        </div>
                    </td>

                    <td>
                        <div style="height:16;"><%--<a
                                href="${customerKeyword.searchEngineUrl}${customerKeyword.Keyword}&pn=&lt;%&ndash;<%=Utils.prepareBaiduPageNumber(customerKeyword.CurrentPosition())%>&ndash;%&gt;"
                                target="_blank"></a>--%>${customerKeyword.currentPosition}
                        </div>
                    </td>

                    <td> <%--onMouseMove="showTip('优化日期：<fmt:formatDate value="${customer.optimizeDate}" pattern="yyyy-MM-dd"/> ，要刷：${customerKeyword.OptimizePlanCount}，已刷：${customerKeyword.optimizedCount}')"
                        onMouseOut="closeTip()">${customerKeyword.collectMethodName}--%>
                    </td>

                    <td>${customerKeyword.optimizePlanCount}
                    </td>
                    <td>${customerKeyword.optimizedCount}
                    </td>
                    <td>${customerKeyword.invalidRefreshCount}
                    </td>

                    <td>${value.feeString}
                    </td>
                    <td><fmt:formatDate value="${customerKeyword.startOptimizedTime}" pattern="yyyy-MM-dd"/></td>
                    <td><fmt:formatDate value="${customerKeyword.lastOptimizeDateTime}" pattern="yyyy-MM-dd  HH:mm"/></td>

                    <td>${customerKeyword.orderNumber}
                    </td>
                    <td>${customerKeyword.remarks}
                    </td>
                    <c:choose>
                        <c:when test="${user.vipType}">
                            <td>${customerKeyword.optimizeGroupName == null ? "" : customerKeyword.optimizeGroupName}
                            </td>
                            <td>
                                <a href="javascript:modifyCustomerKeyword('${customerKeyword.uuid}')">修改</a>|
                                <a href="javascript:delItem('${customerKeyword.uuid}')">删除</a>
                            </td>
                        </c:when>
                        <c:when test="${user.userLevel==1}">
                            <td>
                                <a href="modify.jsp?uuid=${customerKeyword.uuid}">修改</a> |
                                <a href="javascript:delItem('${customerKeyword.uuid}')">删除</a>
                            </td>
                        </c:when>
                    </c:choose>
                </tr>
            </c:forEach>
            <tr>
                <td colspan=18 align="right">
                    <br>
                    <a href="javascript:delAllItems()">删除所选关键字</a>
                </td>
            </tr>
            <tr>
                <td colspan=18>
                    <br>
                </td>
            </tr>
        </table>
    </div>


    <%--<div style="display:none;">
        <script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>
    </div>--%>
<hr>
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
    <%--<option value="10">10</option>
    <option value="25">25</option>
    <option value="50">50</option>
    <option value="75">75</option>
    <option value="100">100</option>--%>
    <option>10</option>
    <option>25</option>
    <option>50</option>
    <option>75</option>
    <option>100</option>
</select>
</div>

<%--Dialog部分--%>
<div id="changeOptimizationGroupDialog" title="修改选中关键字组名">
    <form id="changeOptimizationGroupFrom">
        分组名称<input type="text" name="optimizationGroup" id="optimizationGroup" style="width:200px"/>
    </form>
</div>

<div id="updateCustomerKeywordGroupNameDialog" title="修改该用户关键字组名">
    <form id="updateCustomerKeywordGroupNameFrom">
        目标组名称:<input type="text" id="groupName" name="groupName" size=50>
    </form>
</div>
<div id="uploadSimpleconDailog">
    <form method="post" id="uploadsimpleconForm"  enctype="multipart/form-data">
        <input type="hidden" id="customerUuid" name="customerUuid" value="${customer.uuid}">
        请选择要上传的文件(<font color="red">简化版</font>):<input type="file" id="uploadsimpleconFile" name="file" size=50>
    </form>
</div>
<div id="uploadFullconDailog">
    <form method="post" id="uploadFullconForm" enctype="multipart/form-data">
        <input type="hidden" id="customerUuid" name="customerUuid" value="${customer.uuid}">
        请选择要上传的文件(<font color="red">完整版</font>):<input type="file" id="uploadFullconFile" name="file" size=50>
    </form>
</div>
<div id="customerKeywordDialog">
    <form id="customerKeywordForm">
        <ul>
            <input type="hidden" name="uuid" id="uuid" value="" style="width:300px;">
            <li><span class="customerKeywordSpanClass">关键字:</span><input type="text" name="keyword" id="keyword" value=""
                                                    style="width:300px;"> 输入您要刷的关键字
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

            <li><span class="customerKeywordSpanClass">PC域名:</span><input type="text" name="url" id="url" value="" style="width:300px;">
                <font color="red"> 格式(www.baidu.com)即可</font></li>
            <li><span class="customerKeywordSpanClass">PC原始域名:</span><input type="text" name="originalUrl" id="originalUrl" value=""
                                                       style="width:300px;"><font color="red">
                格式(www.baidu.com)即可</font></li>
            <li><span class="customerKeywordSpanClass">PC第一报价:</span><input name="positionFirstFee" id="positionFirstFee" value=""
                                                       onBlur="setSecondThirdDefaultFee();" style="width:100px;"
                                                       type="text">元 </li>
            <li><span class="customerKeywordSpanClass">PC第二报价:</span><input name="positionSecondFee" onBlur="setThirdDefaultFee();"
                                                       id="positionSecondFee" value="" style="width:100px;" type="text">元
            </li>
            <li><span class="customerKeywordSpanClass">PC第三报价:</span><input name="positionThirdFee" id="positionThirdFee" value=""
                                                       style="width:100px;" type="text">元
            </li>
            <li><span class="customerKeywordSpanClass">PC第四报价:</span><input name="positionForthFee" id="positionForthFee" value="0"
                                                       style="width:100px;" type="text">元
            </li>
            <li><span class="customerKeywordSpanClass">PC第五报价:</span><input name="positionFifthFee" id="positionFifthFee" value="0"
                                                       style="width:100px;" type="text">元
            </li>
            <li><span class="customerKeywordSpanClass">PC首页报价:</span><input name="positionFirstPageFee" id="positionFirstPageFee" value="0"
                                                       style="width:100px;" type="text">元
            </li>
            <c:if test="${user.vipType}">
                <li><span class="customerKeywordSpanClass"></span><a href="javascript:showCustomerKeywordCost()">显示PC成本(再次点击关闭)</a></li>
                <li>
                    <ul id="customerKeywordCost" style="display: none">
                        <li><span class="customerKeywordSpanClass">PC第一成本:</span><input name="positionFirstCost" id="positionFirstCost"
                                                                   onBlur="setSecondThirdDefaultCost();" value="0"
                                                                   style="width:100px;" type="text">元 </li>
                        <li><span class="customerKeywordSpanClass">PC第二成本:</span><input name="positionSecondCost" id="positionSecondCost"
                                                                   onBlur="setThirdDefaultCost();" value="0"
                                                                   style="width:100px;" type="text">元
                        </li>
                        <li><span class="customerKeywordSpanClass">PC第三成本:</span><input name="positionThirdCost" id="positionThirdCost"
                                                                   value="0" style="width:100px;" type="text">元
                        </li>
                        <li><span class="customerKeywordSpanClass">PC第四成本:</span><input name="positionForthCost" id="positionForthCost"
                                                                   value="0" style="width:100px;" type="text">元
                        </li>
                        <li><span class="customerKeywordSpanClass">PC第五成本:</span><input name="positionFifthCost" id="positionFifthCost"
                                                                   value="0" style="width:100px;" type="text">元
                        </li>
                    </ul>
                </li>
            </c:if>
            <c:if test="${user.vipType}">
                <li><span class="customerKeywordSpanClass">PC服务提供商:</span>
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
                                                     style="width:200px;" value="">比如：shouji
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
