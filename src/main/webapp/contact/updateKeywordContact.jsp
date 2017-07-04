<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="kcm" scope="page" class="com.keymanager.manager.KeywordContactManager" />
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
		
<%@include file="/check.jsp" %>

<%
		if(loginState == 0)
		{
%>
        <script language="javascript">
        	 alert("没有登录或登录超时,请重新登录");
			 window.location.href="/bd.html";
		</script>
<%
        return;
		}
		
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		
		if (username == null || username.equals(""))
		{
%>
        <script language="javascript">
        	 alert("没有登录或登录超时,请重新登录");
			 window.location.href="/bd.html";
		</script>
<%		  
        return;  
		}
		 		
    	UserVO user = um.login(datasourceName,username,password);
		
		if (user == null)
		{
%>
        <script language="javascript">
        	 alert("没有登录或登录超时,请重新登录");
			 window.location.href="/bd.html";
		</script>
<%		  
        return;  
		}
		
		String uuid = request.getParameter("uuid");
		if(!Utils.isNullOrEmpty(uuid)){
			kcm.markAsClick("weibo", Integer.parseInt(uuid));
		}
		String fileName = request.getParameter("fileName");
%>

<script language="javascript">
    window.location.href="<%=fileName%>";
</script>



