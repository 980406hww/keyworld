<%@page contentType="text/html;charset=utf-8"   errorPage="error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,java.util.*"%>

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
		 
     String newpassword = request.getParameter("newpassword");
     String newpassword2 = request.getParameter("newpassword2");
     String oldpassword = request.getParameter("oldpassword");
     
    
		if (oldpassword == null || oldpassword.equals(""))
		{
%>
        <script language="javascript">
        	 alert("旧密码不能为空");
				   window.history.back();
			  </script>
<%		  
        return;  
		} 
		
		if (newpassword == null || newpassword.equals(""))
		{
%>
        <script language="javascript">
        	 alert("新密码不能为空");
				   window.history.back();
			  </script>
<%		  
        return;  
		}   
		
		if (newpassword2 == null || newpassword2.equals(""))
		{
%>
        <script language="javascript">
        	 alert("新密码2不能为空");
				   window.history.back();
			  </script>
<%		  
        return;  
		}  
		
		if (!newpassword2.equals(newpassword))
		{
%>
        <script language="javascript">
        	 alert("两次输入密码不一致，请重新输入");
			 window.history.back();
		</script>
<%		  
        return;  
		}       
		
		 
		boolean updated = um.updatePassword(datasourceName,username,newpassword, oldpassword);
		if(updated){
     		session.removeAttribute("username");
     		session.removeAttribute("password");		
%>

<script language="javascript">
	   alert("修改密码完毕,请重新登录");
	   document.location.href = "/bd.html";
</script>
<%
		}else{
%>
<script language="javascript">
	   alert("密码修改失败，原始密码错误！");
	   window.history.back();
</script>
<%	
	  } 
%>


