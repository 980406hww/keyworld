<%@page contentType="text/html;charset=utf-8"%>
<%@page
	import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="um" scope="page"
	class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page"
	class="com.keymanager.manager.ClientStatusManager" />

<%@include file="/check.jsp"%>

<%
	if (loginState == 0) {
%>
<script language="javascript">
	window.location.href = "/bd.html";
</script>
<%
	return;
	}

	String username = (String) session.getAttribute("username");
	String password = (String) session.getAttribute("password");

	username = Utils.parseParam(username);
	password = Utils.parseParam(password);

	if (username == null || username.equals("")) {
%>
<script language="javascript">
	window.location.href = "/bd.html";
</script>
<%
	return;
	}
	UserVO user = um.login(datasourceName, username, password);
	if ((user.getStatus() == 0) || !(user.isVipType())) {
%>
<script language="javascript">
	alert("你没有该权限！");
	window.history.back();
</script>
<%
	return;
	}

	String condition = "";

	String curPage = request.getParameter("pg");

	if (curPage == null || curPage.equals("")) {
		curPage = "1";
	}

	int iCurPage = Integer.parseInt(curPage);

	List itemList = csm.getClientStatusGroupSummary(datasourceName);
%>

<html>
<head>
<title>客户端统计</title>
<style>
.wrap {
	word-break: break-all;
	word-wrap: break-word;
}
</style>
<script language="javascript" type="text/javascript"
	src="/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="/js/jquery142.js"></script>
<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
<link href="/css/menu.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<table width=50% style="font-size: 12px;" cellpadding=3>
		<tr>
			<td colspan=14 align="left"><%@include file="/menu.jsp"%>
			</td>
		</tr>

		<tr bgcolor="#eeeeee" height=30>
			<td align="center" width=100>分组名称</td>
			<td align="center" width=100>终端类型</td>
			<td align="center" width=50>数量</td>
			<div id="div1"></div>
			<div id="div2"></div>
		</tr>
		<%
			String trClass = "";
			String webUrl = "";
			String keywordColor = "";
			boolean validClient = true;
			for (int i = 0; i < itemList.size(); i++) {
				ClientStatusGroupSummaryVO value = (ClientStatusGroupSummaryVO) itemList.get(i);
				trClass = "";
				validClient = true;
				keywordColor = "";

				trClass = "bgcolor='#eeeeee'";
//				if (value.getClientIDPrefixCount() > 0) {
//				}
		%>
		<tr <%=trClass%> >
			<td><%=value.getGroup()%></td>
			<td><%=value.getTerminalType()%></td>
			<td><%=value.getCount()%></td>
		</tr>
		<%
			}
		%>
	</table>
	</form>
	<br>
	<br>

	<script language="javascript">

	</script>
</body>
</html>

