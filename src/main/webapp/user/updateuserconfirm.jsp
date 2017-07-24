<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

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
		 
     String userID = request.getParameter("userID");
     String userName = request.getParameter("userName");
     String gender = request.getParameter("gender");
     String qq = request.getParameter("qq");
     String phone = request.getParameter("phone");
     String percentage =  request.getParameter("percentage");
     String userLevel = request.getParameter("userLevel");
     String autoPay = request.getParameter("autoPay");
     String collectMethod = request.getParameter("collectMethod");
     String isDelete = request.getParameter("isDelete");
     
		if (userName == null || userName.equals(""))
		{
%>
        <script language="javascript">
        	 alert("用户名称不能为空");
			 window.history.back();
		</script>
<%		  
        return;  
		}     
		
		 userName = userName.trim();
		
	     UserVO value = um.getUserByUserID("keyword", userID);
	     value.setUserName(userName);
	     value.setGender(gender);
	     value.setQq(qq);
	     value.setCollectMethod(collectMethod);
	     value.setAutoPay(Boolean.parseBoolean(autoPay));
	     value.setPhone(phone);
	     if(!Utils.isNullOrEmpty(percentage)){
	     	value.setPercentage(Integer.parseInt(percentage.trim()));
	     }
	     value.setUserLevel(Integer.parseInt(userLevel.trim()));
	     value.setStatus(Integer.parseInt(isDelete.trim()));
		 
	     boolean updated = um.updateUser(datasourceName, value);
	     if(updated){
%>
<script language="javascript">
	   alert("修改完毕！");
	   document.location.href = "userlist.jsp";
</script>
<%
	     }else{
%>
<script language="javascript">
	   alert("修改失败！");
	   window.history.back();
</script>
<%	    	 	    	 
	     }
%>



