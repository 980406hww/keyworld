<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>
<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="spm" scope="page" class="com.keymanager.manager.ServiceProviderManager" />
	
<%@include file="/check.jsp" %>

<html>
<header>
<style>
.wrap {word-break: break-all; word-wrap:break-word;}

<!--
#div1{ 
	display:none; 
	background-color :#f6f7f7; 
	color:#333333; 
	font-size:12px; 
	line-height:18px; 
	border:1px solid #e1e3e2; 
	width:450; 
	height:50;
}
-->
</style>

	<script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	<link href="/css/menu.css" rel="stylesheet" type="text/css" />	
</header>

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
	
	
	UserVO user = new UserVO();
	
	user = um.login(datasourceName,username,password);
	
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
	
	List serviceProviders = spm.getAllActiveServiceProvider(datasourceName);
	
	String condition = " and fTerminalType = '" + terminalType + "' and ck.fType = '" + type + "' ";
	String pageUrl = "";	
	String curPage = request.getParameter("pg");
	
	if (curPage == null || curPage.equals(""))
	{
	    curPage = "1";
	}
	
	int iCurPage = Integer.parseInt(curPage);
    
	String keyword = request.getParameter("keyword");
	String orderNumber = request.getParameter("orderNumber");
	String url = request.getParameter("url");
	String qq = request.getParameter("qq");
	String telphone = request.getParameter("telphone");
	String creationFromTime = request.getParameter("creationFromTime");
	String creationToTime = request.getParameter("creationToTime");
	String orderElement = request.getParameter("orderElement");
	String status = request.getParameter("status");
	String userName  = request.getParameter("userName");
	String serviceProvider  = request.getParameter("serviceProvider");
	String optimizeGroupName  = request.getParameter("optimizeGroupName");
	String invalidRefreshCount  = request.getParameter("invalidRefreshCount");
	String displayStop = request.getParameter("displayStop");

	String position = request.getParameter("position");
	String[] unPaidAll = request.getParameterValues("unPaidAll");
	String[] pushPay = request.getParameterValues("pushPay");
	String[] noPosition = request.getParameterValues("noPosition");
	
	if (userName == null){
		userName = "justMine";
	}
	pageUrl = pageUrl + "userName=" + userName;
	if (!user.isVipType()){		
		condition = condition + " and fUserID = '" + user.getUserID() + "' ";		
	}else{
		if ("justMine".equals(userName)){
			condition = condition + " and fUserID = '" + user.getUserID() + "' ";
		}
	}
	
	if (!Utils.isNullOrEmpty(keyword)){
		condition = condition + " and fKeyword like '%" + keyword.trim() + "%' ";
		pageUrl = pageUrl + "&keyword=" + keyword;
	}else{
		keyword = "";	
	}
	
	if (!Utils.isNullOrEmpty(url)){
		condition = condition + " and fUrl = '" + url.trim() + "' ";
		pageUrl = pageUrl + "&url=" + url;
	}else{
		url = "";	
	}
    
	if (!Utils.isNullOrEmpty(orderNumber)){
		condition = condition + " and fOrderNumber = '" + orderNumber.trim() + "' ";
		pageUrl = pageUrl + "&orderNumber=" + orderNumber;
	}else{
		orderNumber = "";	
	}
	
	if (!Utils.isNullOrEmpty(position)){
		condition = condition + " and (fCurrentPosition > 0 and fCurrentPosition <= " + position.trim() + ") ";
	
		pageUrl = pageUrl + "&position=" + position;
	}else{
		position = "";	
	}
	
	
	if (!Utils.isNullOrEmpty(qq)){
		condition = condition + " and fQq = '" + qq.trim() + "' ";
		pageUrl = pageUrl + "&qq=" + qq;
	}else{
		qq = "";	
	}
	
	if (!Utils.isNullOrEmpty(optimizeGroupName)){
		condition = condition + " and fOptimizeGroupName= '" + optimizeGroupName.trim() + "' ";
		pageUrl = pageUrl + "&optimizeGroupName=" + optimizeGroupName;
	}else{
		if (displayStop == null){
			condition = condition + " and fOptimizeGroupName <> 'stop' ";
		}
		optimizeGroupName = "";
	}
	
	if (!Utils.isNullOrEmpty(serviceProvider)){
		condition = condition + " and fServiceProvider = '" + serviceProvider.trim() + "' ";
		pageUrl = pageUrl + "&serviceProvider=" + serviceProvider;
	}else{
		serviceProvider = "";	
	}
	
	if (!Utils.isNullOrEmpty(invalidRefreshCount)){
		condition = condition + " and fInvalidRefreshCount >= " + invalidRefreshCount.trim() + " ";
		pageUrl = pageUrl + "&invalidRefreshCount=" + invalidRefreshCount;
	}else{
		invalidRefreshCount = "";	
	}
	
	
	if (!Utils.isNullOrEmpty(telphone)){
		condition = condition + " and fTelphone = '" + telphone.trim() + "' ";
		pageUrl = pageUrl + "&telphone=" + telphone;
	}else{
		telphone = "";	
	}
	
	if (!Utils.isNullOrEmpty(creationFromTime)){
		condition = condition + " and ck.fCreateTime >= STR_TO_DATE('" + creationFromTime.trim() + "', '%Y-%m-%d') ";
		pageUrl = pageUrl + "&creationFromTime=" + creationFromTime;
	}else{
		creationFromTime = "";	
	}
	if (!Utils.isNullOrEmpty(creationToTime)){
		condition = condition + " and ck.fCreateTime <= STR_TO_DATE('" + creationToTime.trim() + "', '%Y-%m-%d') ";
		pageUrl = pageUrl + "&creationToTime=" + creationToTime;
	}else{
		creationToTime = "";	
	}	
	
	if (!Utils.isNullOrEmpty(status)){
		condition = condition + " and ck.fStatus = " + status.trim() + " ";
		pageUrl = pageUrl + "&status=" + status;
	}else{
		status = "";	
	}
	
	if (!"All".equals(userName)){
		if(!"justMine".equals(userName)){
			condition = condition + " AND EXISTS (SELECT 1 FROM t_user u WHERE u.fUserID = c.fUserID AND u.fUserName = '" + userName + "') ";
		}
	}
	
	
	if (pushPay != null){
		condition = condition + " AND ((fCollectMethod = 'PerDay' and ck.fEffectiveToTime is not null) or ck.fEffectiveToTime <= STR_TO_DATE('" + Utils.formatDatetime(Utils.addDay(Utils.getCurrentTimestamp(), 3), "yyyy-MM-dd") + "', '%Y-%m-%d')) ";
		pageUrl = pageUrl + "&pushPay=pushPay";
	}

	if (noPosition != null){
		condition = condition + " AND (ck.fCurrentPosition = 0) ";
		pageUrl = pageUrl + "&noPosition=noPosition";
	}

    List itemList = new ArrayList();
    
    String order = " order by ck.fCreateTime desc ";
    if ("账单日期".equals(orderElement)){    
    	order = " order by ck.fEffectiveFromTime desc ";
    } else if("当前排名".equals(orderElement)){
    	order = " order by ck.fCurrentPosition ";
    }
    pageUrl = pageUrl + "&orderElement=" + orderElement;
    
  	itemList = ckm.searchCustomerKeywordAssociations(datasourceName, 30 , iCurPage , condition , order,1);
  	
    int recordCount = ckm.getRecordCount();
    int pageCount = ckm.getPageCount();
    
    String fileName = "keywordfinder.jsp?" + pageUrl;
    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
%>


<body>
      <table width="100%" style="font-size:12px;" cellpadding=3>
          <tr>
			<td colspan=13 align="left">
				<%@include file="/menu.jsp" %>	
			</td>
		  </tr>
      	  <tr>
      	  	 <td colspan=16>
      	  	 	<form method="post" action="keywordfinder.jsp">
	      	  	 	<table style="font-size:12px;">
	      	  	 		<tr>
	      	  	 			<td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="<%=keyword%>"
																	style="width:90px;"></td>
	      	  	 			<td align="right">URL:</td> <td><input type="text" name="url" id="url" value="<%=url%>" style="width:120px;"></td>
	      	  	 		  	<td align="right">QQ:</td> <td><input type="text" name="qq" id="qq" value="<%=qq%>" style="width:80px;" ></td>
				 	   	  <td align="right">添加时间:<input name="creationFromTime" id="creationFromTime" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=creationFromTime %>">到<input name="creationToTime" id="creationToTime" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=creationToTime %>"></td>
				 	   	  <td align="right">关键字状态:</td>
	      	  	 			<td>
	      	  	 			  <select name="status" id="status">		
			          	  	 	   <%
			          	  	 	  	 String []statusNames = {"", "新增","激活","过期"};	
			          	  	 		 String []statusValues = {"","2","1","0"};
			          	  	 		 for (int i = 0; i < statusNames.length; i ++)
				          	  	     {
				          	  	         if (statusValues[i].equals(status))
				          	  	         {
				          	  	              out.println("<option selected value='" + statusValues[i] + "'>" + statusNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + statusValues[i] + "'>" + statusNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>
			          	  	 </select>
			          	  </td>
			          	  <td align="right">优化组名:</td><td><input type="text" name="optimizeGroupName" id="optimizeGroupName" value="<%=optimizeGroupName%>" style="width:200px;"></td>
			          	  <%if (user.isVipType()){
	      	  	 			List userNames = um.getAllUserNames(datasourceName);
	      	  	 			List userNameKeys = new ArrayList();
	      	  	 			userNameKeys.add("All");
	      	  	 			userNameKeys.add("justMine");
	      	  	 			userNameKeys.addAll(userNames);
	      	  	 			List userNameValues = new ArrayList();
	      	  	 			userNameValues.add("所有");
	      	  	 			userNameValues.add("只显示自己的");
	      	  	 			userNameValues.addAll(userNames);
	      	  	 		%>
	      	  	 		  <td align="right">用户名称:</td>
	      	  	 			<td>
	      	  	 			  <select name="userName" id="userName">
			          	  	 	   <%
				          	  	     for (int i = 0; i < userNameValues.size(); i ++)
				          	  	     {
				          	  	         if (userNameKeys.get(i).equals(userName))
				          	  	         {
				          	  	              out.println("<option selected value='" + userNameKeys.get(i) + "'>" + userNameValues.get(i) + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + userNameKeys.get(i) + "'>" + userNameValues.get(i) + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>
			          	  	 </select>
			          	  </td>
			          	  <%if (!user.isVipType()){ %>
				 	   	  <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
				 	   	  <%} %> 
	      	  	 		</tr>
	      	  	 		</table>
	      	  	 		
	      	  	 		<table style="font-size:12px;">
	      	  	 		<tr>
			          	  <td align="right">显示前:</td> <td><input type="text" name="position" id="position" value="<%=position%>" style="width:20px;" ></td>
							<td align="right"><input id="noPosition" name="noPosition" type="checkbox" value="noPosition" <%if (noPosition != null){
							out.print(" checked=true");}%>>显示排名为0</input></td>
	      	  	 		  <td align="right"><input id="unPaidAll" name="unPaidAll" type="checkbox" value="unPaidAll" <%if (unPaidAll != null){
	      	  	 		  out.print(" checked=true");}%>>欠费</input></td>
				 	   	  <td align="right"><input id="pushPay" name="pushPay" type="checkbox" value="pushPay" <%if (pushPay != null){
				 	   	  out.print(" checked=true");}%>>催缴</input></td>
							<td align="right"><input id="displayStop" name="displayStop" type="checkbox" value="displayStop" <%if (displayStop != null){
							out.print(" checked=true");}%>>显示下架    </input></td>
				 	   	  <td align="right">订单号:</td> <td><input type="text" name="orderNumber" id="orderNumber" value="<%=orderNumber%>" style="width:160px;"></td>
				 	   	  <td align="right">无效点击数:</td> <td><input type="text" name="invalidRefreshCount" id="invalidRefreshCount" value="<%=invalidRefreshCount%>" style="width:160px;"></td>
							<td align="right">排序:</td>
							<td>
								<select name="orderElement" id="orderElement">
									<%
										String []orderElements = {"创建日期","当前排名","账单日期"};
										for (int i = 0; i < orderElements.length; i ++)
										{
											if (orderElements[i].equals(orderElement))
											{
												out.println("<option selected value='" + orderElements[i] + "'>" + orderElements[i] + "</option>");
											}
											else
											{
												out.println("<option value='" + orderElements[i] + "'>" + orderElements[i] + "</option>");
											}
										}
									%>
								</select>
							</td>
			          	  <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
	      	  	 		</tr>
		      	  	 	</table>
	      	  	 		<%} %>
      	  	 	</form>
      	  	 </td>
      	  </tr>      	  
          <tr bgcolor="#eeeeee" height=30>
          	   <td align="center" width=60>联系人</td>
		       <td align="center" width=120>关键字</td>
		       <td align="center" width=250>URL</td>
		       <td align="center" width="100">标题</td>
		       <td align="center" width=30>指数</td>
		       <td align="center" width=40>原排名</td>
		       <td align="center" width=40>现排名</td>
		       <td align="center" width=35>计价方式</td>
		       <td align="center" width=30>要刷</td>
		       <td align="center" width=30>已刷</td>
		       <td align="center" width=30>无效</td>
		       <td align="center" width=80>报价</td>
		       <td align="center" width=80>订单号</td>
		       <td align="center" width=100>付费状态</td>
		   	   <td align="center" width=100>备注</td>	
		       <% if(user.isVipType()){ %> 
		       <td align="center" width=40>操作</td>
		       <%}else if (user.getUserLevel() > 1) {
		       %>
		       <td align="center" width=40>操作</td>
		       <%} %>
		       <div id="div1"></div> 
		   </tr>
          <%
              String trClass = "";
          	  String keywordColor = "";
              String webUrl = "";
              
              for (int i = 0; i < itemList.size(); i ++)
              {
                  CustomerKeywordVO customerKeywordVO = (CustomerKeywordVO) itemList.get(i);                  
                  trClass= "";
                  keywordColor = "";
                 /*
                  if (customerKeywordVO.isUnpaid()){
                  	keywordColor = "#FF0000";
                  }else if(customerKeywordVO.isPushPay()){
                  	keywordColor = "#FF9966";
                  }else{
                  	keywordColor = "#669900";
                  }
                 */
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                 	 <td><a href="/customerkeyword/list.jsp?status=1&customerUuid=<%=customerKeywordVO.getCustomerUuid()%>" target="_blank"><%=customerKeywordVO.getContactPerson()%></a></td>                                            
                     <td>
                     	<font color="<%=keywordColor%>"><%=customerKeywordVO.getKeyword()%></font>
                     </td>
			         <td class="wrap"  onMouseMove="showTip('原始URL:<%=customerKeywordVO.getOriginalUrl() != null ? customerKeywordVO.getOriginalUrl() : customerKeywordVO.getUrl()%>')" onMouseOut="closeTip()">
			         	<div style="height:16;"><%=Utils.isNullOrEmpty(customerKeywordVO.getUrl())? "" : customerKeywordVO.getUrl()%></div>
			         </td>
			         <td>
			         	<%=(customerKeywordVO.getTitle() == null) ? "" : customerKeywordVO.getTitle().trim()%>
			         </td>
			         <td>
			         	<div style="height:16;"><a href="/customerkeyword/historyPositionAndIndex.jsp?type=PC&uuid=<%=customerKeywordVO.getUuid()%>" target="_blank"><%=customerKeywordVO.getCurrentIndexCount()%></a></div>			         	
			         </td>
			         <td>
			         	<div style="height:16;"><%=customerKeywordVO.getInitialPosition()%></div>			         	
			         </td>
			         <td>
			         	<div style="height:16;"><a href="<%=customerKeywordVO.searchEngineUrl()%><%=customerKeywordVO.getKeyword()%>&pn=<%=Utils.prepareBaiduPageNumber(customerKeywordVO.getCurrentPosition())%>" target="_blank"><%=customerKeywordVO.getCurrentPosition()%></a></div>			         	
			         </td>
			         
			         <td onMouseMove="showTip('优化日期：<%=Utils.formatDatetime(customerKeywordVO.getOptimizeDate(), "yyyy-MM-dd")%>，要刷：<%=customerKeywordVO.getOptimizePlanCount()%>，已刷：<%=customerKeywordVO.getOptimizedCount()%>')" onMouseOut="closeTip()"><%=customerKeywordVO.getCollectMethodName()%></td>
			         <td><%=customerKeywordVO.getOptimizePlanCount()%></td>
			         <td><%=customerKeywordVO.getOptimizedCount()%></td>
			         <td><%=customerKeywordVO.getInvalidRefreshCount()%></td>
			         <td><%=customerKeywordVO.feeString()%>
			         </td>
			         <td><%=Utils.trimValue(customerKeywordVO.getOrderNumber())%></td>
			         <td><%=Utils.trimValue(customerKeywordVO.getPaymentStatus())%></td>
			         <td><%=Utils.trimValue(customerKeywordVO.getRemarks())%></td>
			     
			         <% if(user.isVipType()){ %>                       
			         <td>
                      	  <a href="/customerkeyword/modify.jsp?uuid=<%=customerKeywordVO.getUuid()%>">修改</a>
                      </td> 
                      <%}else if(user.getUserLevel() > 1) {
                      %>
                      <%
                      }
			         %>
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
	 
	 function showTip(content,e) 
	 {
		var event = e||window.event;
		var pageX = event.pageX;
	    var pageY = event.pageY;
	    if(pageX==undefined)
	    {
	    	pageX=event.clientX+document.body.scrollLeft||document.documentElement.scrollLeft;
	    }
	    if(pageY==undefined)
	    {
	        pageY = event.clientY+document.body.scrollTop||document.documentElement.scrollTop;
	    }

	     
		var div1 = document.getElementById('div1'); //将要弹出的层 
		div1.innerText = content;
		div1.style.display="block"; //div1初始状态是不可见的，设置可为可见  
		div1.style.left=pageX+10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容 
		div1.style.top=pageY+5; 
		div1.style.position="absolute"; 
	  }
	 
	 //关闭层div1的显示 
	 function closeTip() 
	 { 
		 var div1 = document.getElementById('div1'); 
		 div1.style.display="none"; 
	 } 
</script>


<div style="display:none;">
<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>

</div>
</body>
</html>

