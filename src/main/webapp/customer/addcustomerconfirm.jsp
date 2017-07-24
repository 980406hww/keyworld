<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

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
		String entryType = (String) session.getAttribute("entry");

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
     
     String contactPerson = request.getParameter("contactPerson");
     String qq = request.getParameter("qq");
     String telphone = request.getParameter("telphone");
     String status = request.getParameter("status");
     String type = request.getParameter("type");
     String remark = request.getParameter("remark");
     
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
	     value.setUserID(username);
	     value.setEntryType(entryType);
	     value.setContactPerson(contactPerson);
	     value.setQq(qq);
	     if (!Utils.isNullOrEmpty(qq)){
	     	value.setEmail(qq.trim() + "@qq.com");
	     }
	     value.setTelphone(telphone);
	     value.setAlipay("");	     
	     value.setPaidFee(0);
	     value.setType(type);
	     if (status.trim() != ""){
	     	value.setStatus(Integer.parseInt(status));
	     }
	     value.setRemark(remark);
		 
	     int customerUuid = cm.addCustomer(value,datasourceName); 
%>

<script language="javascript">
	   alert("添加完毕！");
	   document.location.href = "/customerkeyword/list.jsp?status=1&customerUuid=<%=customerUuid%>";
</script>




