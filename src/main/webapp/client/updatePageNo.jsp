<%@ page import="com.keymanager.value.UserVO" %>
<%@ page contentType="text/html;charset=utf-8" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page" class="com.keymanager.manager.ClientStatusManager" />
<%
		String datasourceName = "keyword";
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String clientID = request.getParameter("clientID");
		String pageNo = request.getParameter("pageNo");

		UserVO user = um.login(datasourceName,username,password);

		if (user == null)
		{
%>
        <script language="javascript">
		   window.location.href="/bd.html";
	  	</script>
<%		  
        return;
		}
		try{
			csm.updatePageNo(datasourceName, clientID, Integer.parseInt(pageNo));
			out.print("1");
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>