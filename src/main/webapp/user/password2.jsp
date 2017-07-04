<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*,com.keymanager.util.mail.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />


<%		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String passwordSerial = request.getParameter("ps");
		
		if (username == null || username.equals(""))
		{
%>
        <script language="javascript">
				   alert("账号为空，不能修改");
				   window.history.back();
			  </script>
<%		  
        return;  
		}
		
		if (password == null || password.equals(""))
		{
%>
        <script language="javascript">
				   alert("密码为空，不能修改");
				   window.history.back();
			  </script>
<%		  
        return;  
		}
		
		if (passwordSerial == null || passwordSerial.equals(""))
		{
%>
        <script language="javascript">
		   alert("密钥为空，不能修改");
		   window.history.back();
	  	</script>
<%		  
        return;  
		}
		
		boolean existPasswordSerial = um.existPasswordSerial("keyword",username,passwordSerial);
    
    if (existPasswordSerial = false)
		{
%>
        <script language="javascript">
		   alert("密钥不对，不能修改密码");
		   window.location.href="/";
	  </script>
<%		  
        return;  
		}
		
		boolean updated = um.updatePasswordWithSerial("keyword",username,password,passwordSerial);
		if(updated){		
%>
<script language="javascript">
    alert("密码已经修改成功,请重新登录");
    window.location.href = "/bd.html"
</script>
<%
		}else{
%>
<script language="javascript">
    alert("密码已经修改失败！");
    window.history.back();
</script>
<%			
		}
%>
