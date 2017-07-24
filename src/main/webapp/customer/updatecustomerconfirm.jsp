<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
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
		 
     String uuid = request.getParameter("uuid");
     String contactPerson = request.getParameter("contactPerson");
     String qq = request.getParameter("qq");
     String userID = request.getParameter("userID");
     String email = request.getParameter("email");
     String telphone = request.getParameter("telphone");
     String alipay =  request.getParameter("alipay");
     String paidFee = request.getParameter("paidFee");
     String isDelete = request.getParameter("isDelete");
     String remark = request.getParameter("remark");
     String type = request.getParameter("type");
     
		if (contactPerson == null || contactPerson.equals(""))
		{
%>
        <script language="javascript">
        	 alert("联系人不能为空");
			 window.history.back();
		</script>
<%		  
        return;  
		}     
		
		contactPerson = contactPerson.trim();
		
	     CustomerVO value = new CustomerVO();
	     value.setUuid(Integer.parseInt(uuid));
	     value.setContactPerson(contactPerson);
	     value.setQq(qq);
	     value.setEmail(email);
	     value.setUserID(userID);
	     value.setTelphone(telphone);
	     value.setType(type);
	     value.setAlipay(alipay);
	     if (paidFee != null && !paidFee.trim().equals("")){
	     	value.setPaidFee(Integer.parseInt(paidFee.trim()));
	     }
	     if (isDelete.trim() != ""){
	     	value.setStatus(Integer.parseInt(isDelete));
	     }
	     value.setRemark(remark);
		 
	     cm.updateCustomer(datasourceName,value); 
%>

<script language="javascript">
	   alert("修改完毕！");
	   document.location.href = "customerlist.jsp";
</script>



