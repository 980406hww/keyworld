<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<%@ page import="com.keymanager.util.Utils" %>
<%@ page import="com.keymanager.util.TerminalTypeMapping" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />

<%@include file="/check.jsp" %>

<%
		if(loginState == 0)
		{
%>
        <script language="javascript">
			window.location.href="/bd.html";
		</script>
<%
        return;
		}
		
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		String type = (String) session.getAttribute("entry");

		String customerUuid = request.getParameter("customerUuid");

		String invalidRefreshCount  = request.getParameter("invalidRefreshCount");
		String keyword = request.getParameter("keyword");
		String url = request.getParameter("url");
		String creationFromTime = request.getParameter("creationFromTime");
		String creationToTime = request.getParameter("creationToTime");
		String status = request.getParameter("status");
		String serviceProvider  = request.getParameter("serviceProvider");
		String optimizeGroupName  = request.getParameter("optimizeGroupName");

		String position = request.getParameter("position");

		String condition = " and fTerminalType = '" + terminalType + "' and fType = '" + type + "' ";
		String pageUrl = "customerUuid=" + customerUuid;
		condition = condition + " and fCustomerUuid=" + customerUuid;

		if (!Utils.isNullOrEmpty(keyword)){
			condition = condition + " and fKeyword like '%" + keyword.trim() + "%' ";
			pageUrl = pageUrl + "&keyword=" + java.net.URLEncoder.encode(keyword,"UTF-8");
		}

		if (!Utils.isNullOrEmpty(url)){
			condition = condition + " and fUrl = '" + url.trim() + "' ";
			pageUrl = pageUrl + "&url=" + url;
		}

		if (!Utils.isNullOrEmpty(position)){
			condition = condition + " and (fCurrentPosition > 0 and fCurrentPosition <= " + position.trim() + ") ";
			pageUrl = pageUrl + "&position=" + position;
		}

		if (!Utils.isNullOrEmpty(optimizeGroupName)){
			condition = condition + " and fOptimizeGroupName= '" + optimizeGroupName.trim() + "' ";
			pageUrl = pageUrl + "&optimizeGroupName=" + java.net.URLEncoder.encode(optimizeGroupName,"UTF-8");
		}

		if (!Utils.isNullOrEmpty(serviceProvider)){
			condition = condition + " and fServiceProvider = '" + serviceProvider.trim() + "' ";
			pageUrl = pageUrl + "&serviceProvider=" + serviceProvider;
		}

		if (!Utils.isNullOrEmpty(creationFromTime)){
			condition = condition + " and fCreateTime >= STR_TO_DATE('" + creationFromTime.trim() + "', '%Y-%m-%d') ";
			pageUrl = pageUrl + "&creationFromTime=" + creationFromTime;
		}

		if (!Utils.isNullOrEmpty(creationToTime)){
			condition = condition + " and fCreateTime <= STR_TO_DATE('" + creationToTime.trim() + "', '%Y-%m-%d') ";
			pageUrl = pageUrl + "&creationToTime=" + creationToTime;
		}

		if (!Utils.isNullOrEmpty(status)){
			condition = condition + " and fStatus = " + status.trim() + " ";
			pageUrl = pageUrl + "&status=" + status;
		}

		if (!Utils.isNullOrEmpty(invalidRefreshCount)){
			condition = condition + " and fInvalidRefreshCount >= " + invalidRefreshCount.trim() + " ";
			pageUrl = pageUrl + "&invalidRefreshCount=" + invalidRefreshCount;
		}

		UserVO user = um.login(datasourceName, username, password);
		if ((user.getStatus() == 0) || !(user.isVipType() || user.getUserLevel() == 1))
		{
%>
	      <script language="javascript">
	      	alert("你没有该权限！");
	      	window.history.back();
		  </script>
<%	
		return;
		}
	    ckm.resetTitle(datasourceName, condition);
%>
<script language="javascript">
	alert("清除成功！");
    window.location.href="list.jsp?status=1&<%=pageUrl%>";
</script>

