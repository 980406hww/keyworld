<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.*,org.apache.commons.fileupload.disk.*,jxl.*,jxl.read.biff.BiffException,com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*,java.io.*,com.keymanager.monitoring.excel.operator.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />

<style>
   .hiddentr {display:none;}
   .displaytr {dislay:;}
</style>




<%@include file="/check.jsp" %>

<%
	if (loginState == 0) {
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
	String type = (String) session.getAttribute("entry");

	String customerUuid = (String) session.getAttribute("customerUuid");

	CustomerVO customer = cm.getCustomerByUuid(datasourceName, customerUuid);
	if (customer == null) {
%>
        <script language="javascript">
        	 alert("<%=customerUuid%>");
        	 alert("没有登录或登录超时,请重新登录");
			 window.location.href="/bd.html";
		</script>
<%
	return;
	}

	UserVO user = um.login(datasourceName, username, password);

	if (user == null) {
%>
        <script language="javascript">
        	alert("没有登录或登录超时,请重新登录");
			window.location.href="/bd.html";
		</script>
<%
	return;
	}
%>

<html>
	<head>
		  <title>简化版关键字上传</title>
		  <script type="text/javascript" src="/common.js">
		  </script>
		  <script type="text/javascript" src="add.js">
		  </script>	  	  
  	</head>				
<body>
	
<table width="100%">
	<tr>
		<td align="right">	     
	 	   <div style="text-align:right;width:100%;font-size:12px;"><a href="/customer/customerlist.jsp">返回客户列表</a></div>
	 	   
			 	   <table width=80% style="font-size:14px;">
			 	   	<tr><td>
<%
	FileItemFactory factory = new DiskFileItemFactory();
	//Create a new file upload handler
	ServletFileUpload upload = new ServletFileUpload(factory);
	//Parse the request
	List items = upload.parseRequest(request);
	InputStream is = null;
	//Process the uploaded items
	Iterator iter = items.iterator();
	while (iter.hasNext()) {
		FileItem item = (FileItem) iter.next();
		if (!item.isFormField()) {
			is = item.getInputStream();
			break;
		}
	}
	boolean uploaded = ckm.handleExcel(is, Constants.EXCEL_TYPE_SUPER_USER_SIMPLE, customer.getUuid(), datasourceName, type, terminalType);
	if (uploaded){
%>
        <script language="javascript">
        	alert("上传成功！");
			window.close();
		</script>
<%}%>
                       </td>
		           </tr>
		       </table>
  </td>
  </tr>
</table> 
</body>
<html>




