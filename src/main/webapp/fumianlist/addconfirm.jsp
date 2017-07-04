<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="flm" scope="page" class="com.keymanager.manager.FumianListManager" />
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
		String type = (String) session.getAttribute("entry");

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
     
     String keyword = request.getParameter("keyword");
     String url = request.getParameter("url");
     String title = request.getParameter("title");
     String desc = request.getParameter("desc");
     String position = request.getParameter("position");
		
	 keyword = keyword.replaceAll("　"," ");
	 keyword = keyword.replaceAll("'","");
	 keyword = keyword.replaceAll("。","");
	 keyword = keyword.trim();
	 keyword = Utils.trimdot(keyword);
		
     FumianListVO fumianListVO = new FumianListVO();
     fumianListVO.setKeyword(keyword.trim());     
     fumianListVO.setUrl(Utils.isNullOrEmpty(url) ? "" : url.trim());
     fumianListVO.setTitle(Utils.isNullOrEmpty(title) ? "" : title.trim()); 
     fumianListVO.setDesc(desc.trim());
	 fumianListVO.setTerminalType(terminalType);
     fumianListVO.setPosition(Integer.parseInt(position.trim()));
	 flm.addFumainRecordVO(datasourceName, fumianListVO);     
%>

<script language="javascript">
	   alert("关键字负面清单添加完毕！");
	   document.location.href = "/fumianlist/list.jsp";
</script>




