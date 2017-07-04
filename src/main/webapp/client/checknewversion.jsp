<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page" class="com.keymanager.manager.ClientStatusManager" />

<%
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String clientID = request.getParameter("clientID");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		if (username == null || username.equals(""))
		{
%>
        <script language="javascript">
		   window.location.href="/bd.html";
	  	</script>
<%		  
        return;  
		}
		UserVO user = um.login("keyword",username,password);
		if(user == null){
%>
        <script language="javascript">
		   window.location.href="/bd.html";
	  	</script>
<%		
			return;			
		}
		try{
			String clientStatusVOJson = csm.getClientStatusTargetVersion("keyword", clientID, terminalType);
			out.print(clientStatusVOJson);
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>

