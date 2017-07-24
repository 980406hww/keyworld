<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
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
		
		String uuid = request.getParameter("uuid");
		CustomerKeywordVO customerKeyword = ckm.getCustomerKeywordByUuid(datasourceName, uuid);
		
		String customerUuid = request.getParameter("customerUuid");	
		UserVO user = um.login(datasourceName,username,password);		
		if ((user.getStatus() == 0) || !(user.isVipType() || user.getUserLevel() == 1) || !customerUuid.equals(customerKeyword.getCustomerUuid() + ""))
		{
%>
	      <script language="javascript">
	      	alert("你没有该权限！");
	      	window.history.back();
		  </script>
<%			
		return;
		}
		if (username == null || username.equals(""))
		{
%>
        <script language="javascript">
		   window.location.href="/bd.html";
	  	</script>
<%		  
        return;  
		}
		
		
		ckm.deleteCustomerKeyword(datasourceName, Integer.parseInt(uuid), type);
		
%>
<script language="javascript">
	alert("删除关键字成功！");
    window.location.href="list.jsp?isDelete=1&customerUuid=<%=customerUuid%>";
</script>

