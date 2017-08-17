<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html;charset=utf-8" %>
<%@page import="com.keymanager.monitoring.enums.EntryTypeEnum,com.keymanager.util.Utils,com.keymanager.value.CustomerVO,com.keymanager.value.UserVO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
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
    #showAddRuleDialog{

    }
    #showChargeRuleCalculationDiv input{
        font-size: 12px;
        width: 30px;
        height: 20px;
    }
    #showChargeRuleCalculationDiv #pcOperationTypeDiv{

    }
    #showChargeRuleCalculationDiv #phoneOperationTypeDiv{

    }
</style>
<script type="text/javascript">
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
    function triggerDailyReportGeneration() {
        if (confirm("确实要生成当天报表吗?") == false) return;
        var customerUuids = getSelectedClientIDs();

        var postData = {"customerUuids": customerUuids}
        $$$.ajax({
            url: '/internal/dailyReport/triggerReportGeneration',
            data: JSON.stringify(postData),
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (status) {
                if (status) {
                    showInfo("更新成功！", self);
                    window.location.reload();
                } else {
                    showInfo("更新失败！", self);
                }
            },
            error: function () {
                showInfo("更新失败！", self);
                settingDialogDiv.hide();
            }
        });
    }
    function searchCurrentDateCompletedReports() {
        var span = $$$("#dailyReportSpan");
        $$$.ajax({
            url: '/internal/dailyReport/searchCurrentDateCompletedReports',
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (dailyReports) {
                var htmlContent = "";
                if (dailyReports) {
                    $$$.each(dailyReports, function (idx, val) {
                        var date = new Date(val.completeTime);
                        htmlContent = htmlContent + '  <a href="' + val.reportPath + '">下载(' + ((date.getHours() < 10) ? '0'
                                + date.getHours() : date.getHours()) + ':' + ((date.getMinutes() < 10) ? '0' + date.getMinutes()
                                : date.getMinutes()) + ')</a>';
                    });
                } else {
                    htmlContent = "今天没报表";
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
    function doOver(obj) {
        obj.style.backgroundColor = "green";
    }

    function doOut(obj) {
        var rowIndex = obj.rowIndex;
        if ((rowIndex % 2) == 0) {
            obj.style.backgroundColor = "#eeeeee";
        }
        else {
            obj.style.backgroundColor = "#ffffff";
        }
    }

    function delItem(uuid) {
        if (confirm("确实要删除这个客户吗?") == false) return;
        document.location = "delcustomer.jsp?uuid=" + uuid;
    }
    //规则部分
    function addRule(uuid) {
        if (uuid == null) {
            $$$('#mainKeywordForm')[0].reset();
        }
        $$$("#showAddRuleDialog").dialog({
            resizable: false,
            width: 400,
            height: 440,
            modal: true,
            //按钮
            buttons: {
                "保存": function () {
                    savaMainKeyword(uuid);
                },
                "清空": function () {
                    $('#mainKeywordForm')[0].reset();
                },
                "取消": function () {
                    $(this).dialog("close");
                    $('#mainKeywordForm')[0].reset();
                }
            }
        });
    }
    function delCustomer(uuid) {

    }

    <c:if test="${EntryTypeEnum.bc.name().equalsIgnoreCase(entryType)}">
    var intervalId = setInterval(function () {
        searchCurrentDateCompletedReports();
    }, 1000 * 30);
    </c:if>
</script>
    <title>客户列表</title>
</head>

<body>
<table width="100%" style="font-size:12px;" cellpadding=3>
    <tr>
        <td colspan=13 align="left">
            <%@include file="/menu.jsp" %>
        </td>
    </tr>
    <tr>
        <td colspan=13>
            <form method="post" action="customerlist.jsp">
                <table style="font-size:12px;">
                    <tr>
                        <td align="right">联系人:</td>
                        <td><input type="text" name="contactPerson" id="contactPerson" value="${contactPerson}"
                                   style="width:200px;"></td>
                        <td align="right">QQ:</td>
                        <td><input type="text" name="qq" id="qq" value="${qq}" style="width:200px;"></td>
                        <td align="right">联系电话：</td>
                        <td><input type="text" name="telphone" id="telphone" value="${telphone}" style="width:200px;">
                        </td>
                        <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 ">
                        </td>
                        <c:if test="${EntryTypeEnum.bc.name().equalsIgnoreCase(entryType)}">
                        <td align="right" width="100"><a target="_blank"
                                                         href='javascript:triggerDailyReportGeneration()'>触发日报表生成</a>
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

        <td align="center" width=80>联系人</td>
        <td align="center" width=60>QQ</td>
        <td align="center" width=100>Email</td>
        <td align="center" width=100>电话</td>
        <td align="center" width=60>关键字数</td>
        <td align="center" width=60>已付金额</td>
        <td align="center" width=140>备注</td>
        <td align="center" width=60>类型</td>
        <td align="center" width=40>状态</td>
        <td align="center" width=80>创建时间</td>
        <td align="center" width=200>操作
            (<a target="_blank" href="
            <c:choose>
			    <c:when test="${user.vipType}">
				        SuperUserKeywordList.xls
			    </c:when>
	            <c:otherwise>
		                AgentKeywordList.xls
                </c:otherwise>
			</c:choose>">下载Excel模板</a>)
        </td>
        <div id="div2"></div>
    </tr>
    <c:forEach items="${page.records}" var="customer">
        <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
            <td><input type="checkbox" name="customerUuid" value="${customer.uuid}"/></td>
            <c:if test="${user.vipType}">
                <td>${user.userID}</td>
            </c:if>
            <td><a href="/customerkeyword/list.jsp?status=1&customerUuid=${customer.uuid}">${customer.contactPerson}</a>
            </td>
            <td>${customer.qq}</td>
            <td>${customer.email}</td>
            <td>${customer.telphone} </td>
            <td align="right">${customer.keywordCount} </td>
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
                <a href="javascript:updateCustomer(${customer.uuid})">修改</a> |
                <a href="javascript:addCustomerKeyword(${customer.uuid})">增加</a> |
                <a target="_blank"
                   href="/customer/uploaddailyreporttemplate.jsp?customerUuid=${customer.uuid}">上传日报表模板</a>
                <c:if test="${user.vipType}">
                    <a href="javascript:delCustomer('${customer.uuid}">删除</a>
                    <a href="javascript:addRule('${customer.uuid}')">添加规则</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${customerList.size<0}">
        <tr>
            <td colspan=10>
                <br>
                <div>我们还没有添加客户，现在就添加？<a href="addcustomer.jsp" style="color:blue;">增加新客户</a></div>
            </td>
        </tr>
    </c:if>

    <tr>
        <td colspan=9>
            <br>
            <%--<%=pageInfo%>--%>
        </td>
    </tr>
</table>
<br><br><br>
<br>

<br><br><br><br><br><br><br><br>

<%--<div id="showAddRuleDialog">
    <div>
        <input type="radio" id="chargeRuleFixed" >按固定价格收费&nbsp;&nbsp;
        <input type="radio" id="chargeRulePercentage" >按照百分比收费&nbsp;&nbsp;
        <input type="radio" id="chargeRuleInterval" >按照区间收费
    </div>
    <div id="showChargeRuleCalculationDiv">
        <div id="showChargeRuleCalculationDiv">
            <div id="pcOperationTypeDiv">
                <input id="pcOperationType" type="checkbox" value="电脑端"/><br>
                当指数<=0时收的费用: <input id="equalZeraCost" type="text" /><br>
                排名第一应收金额: <input id="chargesOfFirst" type="text" /><br>
                排名第二应收金额: <input id="chargesOfSecond " type="text" /><br>
                排名第三应收金额: <input id="chargesOfThird" type="text" /><br>
                排名第四应收金额: <input id="chargesOfFourth" type="text" /><br>
                排名第五应收金额: <input id="chargesOfFifth" type="text" /><br>
                排名上首页应收金额: <input id="chargesOfFirstPage" type="text" />
            </div>
            <div id="phoneOperationTypeDiv">
                <input id="phoneOperationType" type="checkbox" value="手机端"/><br>
                当指数<=0时收的费用: <input id="equalZeraCost" type="text" /><br>
                排名第一应收金额: <input id="chargesOfFirst" type="text" /><br>
                排名第二应收金额: <input id="chargesOfSecond " type="text" /><br>
                排名第三应收金额: <input id="chargesOfThird" type="text" /><br>
                排名第四应收金额: <input id="chargesOfFourth" type="text" /><br>
                排名第五应收金额: <input id="chargesOfFifth" type="text" /><br>
                排名上首页应收金额: <input id="chargesOfFirstPage" type="text" />
            </div>
    </div>
    <div id="showChargeRuleIntervalDiv">
        <div id="pcOperationTypeDiv">
            <input id="pcOperationType" type="checkbox" value="电脑端"/><br>
        </div>
        <div id="phoneOperationTypeDiv">
            <input id="phoneOperationType" type="checkbox" value="手机端"/><br>
        </div>
    </div>
</div>
</div>--%>



<div style="display:none;">
    <script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>
</div>
</body>
</html>

