<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="ualm" scope="page" class="com.keymanager.manager.UserAccountLogManager" />

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
	String condition = " and fUserID = '" + user.getUserID() + "' ";
		
	String curPage = request.getParameter("pg");
	if (curPage == null || curPage.equals(""))
	{
	    curPage = "1";
	}
	
	int iCurPage = Integer.parseInt(curPage);
    
    ArrayList itemList = ualm.searchUserAccountLogs("keyword", 30 , iCurPage , condition , " order by fCreateTime desc ",1);
    int recordCount = ualm.getRecordCount();
    int pageCount = ualm.getPageCount();
    String fileName = "/user/userlist.jsp";
    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
%>

<head>
	<title>用户账号清单</title>
  <script language="javascript" src="js/calendar.js"></script>
  <script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
  <script language="javascript" type="text/javascript" src="/js/slide.js"></script>
  <link href="/css/menu.css" rel="stylesheet" type="text/css" />	
</head>
<body>
      <table width="100%" style="font-size:12px;" cellpadding=3>
      	  <tr>
      	  	 <td colspan=5> 
      	  	 	<table style="font-size:12px;">
      	  	 		<tr>
      	  	 			<td width="40">账号:</td>
      	  	 			<td width="120"><%=user.getUserID()%></td>
      	  	 			<td width="80">用户名称:</td>
      	  	 			<td width="80"><%=user.getUserName()%></td>
      	  	 			<td width="40">余额:</td>
      	  	 			<td width="40"><%=user.getAccountAmount()%></td>
      	  	 		</tr>
      	  	 	</table>
      	  	 	</td>
      	  </tr>
          <tr bgcolor="#eeeeee" height=30>
              <td width=60>交易类型</td>
              <td width=80>金额</td>
              <td width=80>结余</td>
              <td width="60">交易时间</td>
              <td width=700>备注</td>
          </tr>
          <%
          	String trClass = "";
            String webUrl = "";
            
            for (int i = 0; i < itemList.size(); i ++)
            {
            	UserAccountLogVO value = (UserAccountLogVO) itemList.get(i);
                trClass= "";
                if ((i % 2) != 0)
                {
                   trClass = "bgcolor='#eeeeee'";
                }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>                      
		              <td><%=value.getType()%></td>
		              <td><%=value.getAmount()%></td>
		              <td><%=value.getAccountAmount()%></td>
		              <td><%=value.getCreateTime()%></td>
		              <td><%=value.getRemarks()%></td>
                  </tr>
          <%
              }
          %>
          <tr>
          	<td colspan=7>
          		  <br>
          		  <%=pageInfo%>
          	</td>
          </tr>   
      </table>
   <br><br><br>
  <br><br><br><br><br><br><br><br>
<script language="javascript">
	 function doOver(obj)
	 {
	 	  obj.style.backgroundColor = "green";
	 }
	 
	 function doOut(obj)
	 {
	 	   var rowIndex = obj.rowIndex;
	 	   if ((rowIndex % 2) == 0) 
	 	   {
	 	   	  obj.style.backgroundColor = "#eeeeee";
	 	   }
	 	   else
	 	   {
	 	   	  obj.style.backgroundColor = "#ffffff";
	 	   }
	 }
	 
   function delItem(uuid)
   {
   	   if (confirm("确实要删除这个关键字?") == false) return;
   	   document.location = "deladminkey.jsp?uuid=" + uuid;
   }
</script>
</body>
</html>

