<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="flm" scope="page" class="com.keymanager.manager.FumianListManager" />

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
		String uuids = request.getParameter("uuids");
		
		System.out.print("uuids == " + uuids);
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
			String[] uuidArray = uuids.split(",");
			for(int i=0; i < uuidArray.length; i++){
				String uuid = uuidArray[i];				
				flm.deleteFumianListVO(datasourceName, uuid);
			}
		
%>
<script language="javascript">
	alert("删除关键字清单成功！");
    window.location.href="list.jsp";
</script>

