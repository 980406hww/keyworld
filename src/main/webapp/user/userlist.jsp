<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

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
		if (!user.isVipType())
		{
%>
        <script language="javascript">
        	alert("你没有该权限！");
        	window.history.back();
	  	</script>
<%		  
        return;  
		}		
		String condition = "";
			
		String curPage = request.getParameter("pg");
		String userID = Utils.parseParam(request.getParameter("userID"));
		String userFullName = Utils.parseParam(request.getParameter("userFullName"));
		String qq = Utils.parseParam(request.getParameter("qq"));
		String phone = Utils.parseParam(request.getParameter("phone"));
		
		if (userFullName != null && !userFullName.equals(""))
		{
		    condition += " and fUserName like '%" + userFullName + "%'" ;
		}
	  	else
		{
	  		userFullName = "";
		}
		
		if (userID != null && !userID.equals(""))
		{
		    condition += " and fUserID = '" + userID + "'" ;
		}
	  	else
		{
	  		userID = "";
		}
		
		if (qq != null && !qq.equals(""))
		{
		    condition += " and fQq = '" + qq + "'" ;
		}
	  	else
		{
	  		qq = "";
		}
		
		if (phone != null && !phone.equals(""))
		{
		    condition += " and fPhone = '" + phone + "'" ;
		}
	  	else
		{
	  		phone = "";
		}
		
		if (curPage == null || curPage.equals(""))
		{
		    curPage = "1";
		}
		
		
		int iCurPage = Integer.parseInt(curPage);
    
    
    
    ArrayList itemList = new ArrayList();
    
    itemList = um.searchUser("keyword", 30 , iCurPage , condition , " order by fCreateTime ",1);
    
    int recordCount = um.getRecordCount();
    int pageCount = um.getPageCount();
    
    String fileName = "/user/userlist.jsp?userID=" + userID + "&userFullName=" + userFullName;
      
    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
%>

<head>
	<title>用户列表</title>
  <script language="javascript" src="js/calendar.js"></script>
  <script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
  <script language="javascript" type="text/javascript" src="/js/slide.js"></script>
  <link href="/css/menu.css" rel="stylesheet" type="text/css" />	
</head>
<body>
	 
      <table width="100%" style="font-size:12px;" cellpadding=3>
          <tr>
			<td colspan=12 align="left">
				<%@include file="/menu.jsp" %>	
			</td>
		  </tr>
      	  <tr>
      	  	 <td colspan=12> 
      	  	 	 <form method="post" action="userlist.jsp?isDelete=1" style="display:inline;height:25px;">
      	  	 	 邮箱账号:<input  type="text" value="<%=userID%>" name="userID" id="userID">
      	  	 	 用户名称:<input  type="text" value="<%=userFullName%>" name="userFullName" id="userFullName"> 
      	  	 	 QQ:<input  type="text" value="<%=qq%>" name="qq" id="qq"> 
      	  	 	 电话:<input  type="text" value="<%=phone%>" name="phone" id="phone"> 
      	  	 	  
      	  	 	               <input type="submit" name="btnsub" id="btnsub" value=" 确定 ">
      	  	 	</form>
      	  	 	</td>
      	  </tr>
          <tr bgcolor="#eeeeee" height=30>
              <td width=100>邮箱账号</td>
              <td width=100>用户名称</td>
              <td width=30>性别</td>
              <td width=60>QQ</td>
              <td width=60>电话</td>
              <td width=100>用户级别</td>
              <td width=30>提成比例</td>
              <td width=40>客户IP</td>
              <td width=70>最后登陆时间</td>
              <td width=60>注册时间</td>
              <td width=40>状态</td>
              <td width=140>操作</td>
          </tr>
          <%
          	String trClass = "";
                        String webUrl = "";
                        
                        for (int i = 0; i < itemList.size(); i ++)
                        {
                            UserVO value = (UserVO) itemList.get(i);
                            
                            
                            trClass= "";
                            if ((i % 2) != 0)
                            {
                               trClass = "bgcolor='#eeeeee'";
                            }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>                      
                      <td><%=value.getUserID()%></td>
				              <td><%=value.getUserName()%></td>
				              <td><%=value.getGender()%></td>
				              <td><%=value.getQq()%></td>
				              <td><%=value.getPhone()%></td>
				              <td><%=value.getUserLevelName()%></td>
				              <td><%=value.getPercentage()%></td>
				              <td><%=value.getClientIp()%></td>
				              <td><%=value.getCreateTime()%></td>
				              <td><%=value.getUpdateTime()%></td>
				              <td><%=value.getStatusName()%></td>
				              <td>
				              	 <%
				              	 	if (value.getStatus() == 1) 
				              	 		              	     {
				              	 %>
				              	         <a href="updateuserstatus.jsp?userID=<%=value.getUserID()%>&isDelete=0">暂停</a>
				              	 <%
 				              	 	}
 				              	 		              	     else
 				              	 		              	   	 {
 				              	 %>
				              	        <a href="updateuserstatus.jsp?userID=<%=value.getUserID()%>&isDelete=1">激活</a>
				              	 <%
 				              	 	}
 				              	 %>
				              	| <a href="updateuser.jsp?userID=<%=value.getUserID()%>">修改</a>
				              	| <a href="recharge.jsp?userID=<%=value.getUserID()%>">充值</a>
				              	| <a href="refund.jsp?userID=<%=value.getUserID()%>">退款</a></td>
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

