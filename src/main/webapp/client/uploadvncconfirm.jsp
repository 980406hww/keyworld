<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.*,org.apache.commons.fileupload.disk.*,jxl.*,jxl.read.biff.BiffException,com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*,java.io.*,com.keymanager.monitoring.excel.operator.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page" class="com.keymanager.manager.ClientStatusManager" />

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
		  <title>VNC配置文件上传</title>
		  <script type="text/javascript" src="/common.js">
		  </script>
		  <script type="text/javascript" src="add.js">
		  </script>	  	  
  	</head>				
<body>
	
<table width="100%">
	<tr>
		<td align="right">	     
	 	   <div style="text-align:right;width:100%;font-size:12px;"><a href="/client/clientlist.jsp">返回终端列表</a></div>
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
	csm.parseVNCAddressBook(is, datasourceName);
%>
        <script language="javascript">
        	alert("上传成功！");
			window.close();
		</script>
<%%>
                       </td>
		           </tr>
		       </table>
  </td>
  </tr>
</table> 
</body>
<html>




