<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<%@ page import="com.keymanager.util.TerminalTypeMapping" %>
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
		String customerUuid = request.getParameter("customerUuid");	
		String uuids = request.getParameter("uuids");
		String deleteType = request.getParameter("deleteType");

		UserVO user = um.login(datasourceName,username,password);
		if ((user.getStatus() == 0) || !(user.isVipType() || user.getUserLevel() == 1))
		{
%>
	      <script language="javascript">
	      	alert("你没有该权限！");
	      	window.history.back();
		  </script>
<%	
		return;
		}
		if(uuids == null || uuids.equals("")){
			if("emptyTitleAndUrl".equals(deleteType)){
				ckm.deleteEmptyTitleAndUrlCustomerKeyword(datasourceName, terminalType, Integer.parseInt(customerUuid), type);
			}else{
				ckm.deleteEmptyTitleCustomerKeyword(datasourceName, terminalType, Integer.parseInt(customerUuid), type);
			}
		}else{
			String[] uuidArray = uuids.split(",");
			for(int i=0; i < uuidArray.length; i++){
				String uuid = uuidArray[i];		
				CustomerKeywordVO customerKeyword = ckm.getCustomerKeywordByUuid(datasourceName, uuid);
				
				if (!customerUuid.equals(customerKeyword.getCustomerUuid() + ""))
				{
		%>
			      <script language="javascript">
			      	alert("你没有该权限！");
			      	window.history.back();
				  </script>
		<%			
				return;
				}
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

			for(int i=0; i < uuidArray.length; i++){
				String uuid = uuidArray[i];
				ckm.deleteCustomerKeyword(datasourceName, Integer.parseInt(uuid), type);
			}
		}
		out.println("1");
%>