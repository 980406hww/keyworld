<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
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

		UserVO user = um.login(datasourceName,username,password);
		
		String uuid = request.getParameter("uuid");
		String status = request.getParameter("status");
		
		ckm.updateCustomerKeywordStatus(datasourceName, terminalType, Integer.parseInt(uuid), status);
		
%>
<script language="javascript">
    window.location.href="keywordList.jsp";
</script>

