<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="ckalm" scope="page" class="com.keymanager.manager.CustomerKeywordAccountLogManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
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
	 String customerUuid = request.getParameter("customerUuid");
     String[] complexValues = request.getParameterValues("complexValue");
     boolean  paidSuccessed = ckm.payAll(datasourceName, customerUuid, complexValues);
	if (paidSuccessed){
%>
<script language="javascript">
   alert("收款成功！");
   window.close();
</script>
<%
	}else{
%>
<script language="javascript">
   alert("收款失败，重新再试，或者联系客户！");
   window.close();
</script>
<%		
	}
%>



