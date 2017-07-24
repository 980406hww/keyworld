<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
	
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
	String entryType = (String) session.getAttribute("entry");

	username = Utils.parseParam(username);
	password = Utils.parseParam(password);
	
	if (username == null || username.equals(""))
	{
%>
       <script language="javascript">
			window.location.href="/bd.html";
		</script>
<%
	return;  
	}
	
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
	String condition = " and fEntryType = '" + entryType + "' ";
		
	String curPage = request.getParameter("pg");
	
	if (curPage == null || curPage.equals(""))
	{
	    curPage = "1";
	}
	
	int iCurPage = Integer.parseInt(curPage);
    
	String contactPerson = request.getParameter("contactPerson");
	String qq = request.getParameter("qq");
	String telphone = request.getParameter("telphone");
	
	if (!Utils.isNullOrEmpty(contactPerson)){
		condition = " and fContactPerson like '%" + contactPerson.trim() + "%' ";
	}else{
		contactPerson = "";	
	}
    
	if (!user.isVipType()){
		condition = condition + " and fUserID = '" + user.getUserID() + "' ";
	}
	
	if (!Utils.isNullOrEmpty(qq)){
		condition = condition + " and fQq = '" + qq.trim() + "' ";
	}else{
		qq = "";	
	}
	
	if (!Utils.isNullOrEmpty(telphone)){
		condition = condition + " and fTelphone = '" + telphone.trim() + "' ";
	}else{
		telphone = "";	
	}
	
	
    List itemList = new ArrayList();
    
  	itemList = cm.searchCustomer(datasourceName, 30 , iCurPage , condition , " order by fCreateTime desc ",1);
  	
    int recordCount = cm.getRecordCount();
    int pageCount = cm.getPageCount();
    
    String fileName = "customerlist.jsp";
    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
%>
<header>
	  <script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	  <script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	  <link href="/css/menu.css" rel="stylesheet" type="text/css" />	
</header>

<body>
      <table width=1280 style="font-size:12px;" cellpadding=3>
      	     <tr>
				<td colspan=12 align="left">
					<%@include file="/menu.jsp" %>	
				</td>
			</tr>
      	  <tr>
      	  	 <td colspan=12>
      	  	 	<form method="post" action="customerlist.jsp">
	      	  	 	<table style="font-size:12px;">
	      	  	 		<tr>
	      	  	 			<td align="right">联系人:</td> <td><input type="text" name="contactPerson" id="contactPerson" value="<%=contactPerson%>" style="width:200px;"></td>
	      	  	 			<td align="right">QQ:</td> <td><input type="text" name="qq" id="qq" value="<%=qq%>" style="width:200px;" ></td>
				 	   	  <td align="right">联系电话：</td><td><input type="text" name="telphone" id="telphone" value="<%=telphone%>" style="width:200px;"></td>
				 	   	  <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td> 
	      	  	 		</tr>
	      	  	 	</table>
      	  	 	</form>
      	  	 </td>
      	  </tr>      	  
          <tr bgcolor="#eeeeee" height=30>
          	  <% if(user.isVipType()){ %>
          	  <td align="center" width=80>用户名称</td>
          	  <%} %>
              <td align="center" width=80>联系人</td>
		      <td align="center"  width=60>QQ</td>
		      <td align="center"  width=100>Email</td>
		      <td align="center"  width=100>电话</td>
		      <td align="center"  width=60>关键字数</td> 
		      <td align="center"  width=60>已付金额</td>              
		      <td align="center"  width=140>备注</td>
		      <td align="center"  width=60>类型</td>
		      <td align="center"  width=40>状态</td>
		      <td align="center"  width=80>创建时间</td>	
              <td align="center"  width=200>操作(<a target="_blank" href="/<%if(user.isVipType()){out.print("SuperUserKeywordList.xls");}else{out.print("AgentKeywordList.xls");};%>">下载Excel模板</a>)</td>              
          </tr>
          <%
              String trClass = "";
              String webUrl = "";
              
              for (int i = 0; i < itemList.size(); i ++)
              {
                  CustomerVO value = (CustomerVO) itemList.get(i);                  
                  trClass= "";
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);" height=30> 
                 	  <% if(user.isVipType()){ %>
		          	  <td><%=value.getUserID() %></td>
		          	  <%} %>                                           
                      <td><a href="/customerkeyword/list.jsp?isDelete=1&customerUuid=<%=value.getUuid()%>"><%=value.getContactPerson()%></a></td>
                      <td><%=value.getQq()%></td>
                      <td><%=value.getEmail()%></td>
                      <td><%=value.getTelphone()%></td>
                      <td align="right"><%=value.getKeywordCount()%></td>
                      <td align="right"><%=value.getPaidFee()%></td>
                      <td><%if (value.getRemark() != null){out.println(value.getRemark());}%></td>
                      <td><%=value.getType()%></td>
                      <td><%if (value.getStatus() == 1){out.println("激活");}else{out.println("暂停");}%></td>   
                      <td><%=Utils.formatDatetime(value.getCreateTime(), "yyyy-MM-dd")%></td>
                      <td>
                      	  <a href="updatecustomer.jsp?uuid=<%=value.getUuid()%>">修改</a> | 
                      	  <a href="/customerkeyword/add.jsp?customerUuid=<%=value.getUuid()%>">增加</a> | 
                      	  <a target="_blank" href="/customerkeyword/upload.jsp?customerUuid=<%=value.getUuid()%>">上传</a>
                      	  <%if(user.isVipType()){ %>
                      	   |
                      	  <a href="javascript:delItem('<%=value.getUuid()%>')">删除</a>
                      	  <%}else if(user.getUserLevel() > 1){ %>
                      	  <%} %>
                      </td>
                  </tr>
          <%
              }
              
              if (itemList.size() == 0) 
              {
         %>
                  <tr>
		          	<td colspan=10>
		          		  <br>
		          		  <div>我们还没有添加客户，现在就添加？<a href="addcustomer.jsp" style="color:blue;">增加新客户</a></div>
		          	</td>
		          </tr>
         <%
              }
          %>
          
          <tr>
          	<td colspan=9>
          		  <br>
          		  <%=pageInfo%>
          	</td>
          </tr>          
      </table>
   <br><br><br>
  <br>
  
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
   	   if (confirm("确实要删除这个客户吗?") == false) return;
   	   document.location = "delcustomer.jsp?uuid=" + uuid;
   }
</script>


<div style="display:none;">
<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>

</div>
</body>
</html>

