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
		if (!user.isVipType())
		{
%>
        <script language="javascript">
        	alert("你没有该权限！");
        	window.history.back();
	  	</script>
<%		  
        return;  
		}		
		
		String userID = request.getParameter("userID");
		
		UserVO tmpUserVO = um.getUserByUserID("keyword", userID);
		if (tmpUserVO == null)
		{
%>
        <script language="javascript">
        	 alert("没有这个数据，不能修改");
			 window.location.href="userlist.jsp";
		</script>
<%
	return;  
		} 
        String amount = request.getParameter("amount");
        String remarks = request.getParameter("remarks");
		 
	    boolean updated = um.refund(datasourceName, tmpUserVO, amount, remarks);
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



