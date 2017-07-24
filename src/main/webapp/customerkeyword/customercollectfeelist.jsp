<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*,java.net.URLEncoder"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="ckpvm" scope="page" class="com.keymanager.manager.CustomerKeywordPositionViewManager" />
	
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
	width:350; 
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

	String pageUrl = "";	
	String curPage = request.getParameter("pg");
	
	if (curPage == null || curPage.equals(""))
	{
	    curPage = "1";
	}
	
	int iCurPage = Integer.parseInt(curPage);
    
	String date = request.getParameter("date");
	if(date == null){
		date = Utils.getCurrentDate();
	}
	String customerUuid = request.getParameter("customerUuid");
	if(customerUuid == null){
		customerUuid = "";
	}
	String customerName = request.getParameter("customerName");
	if(customerName == null){
		customerName = "";
	}
	String statType = request.getParameter("statType");
	if(statType == null){
		statType = "NeedPay";
	}
	
  	List itemList = ckpvm.searchCustomerKeywordPositionViews(datasourceName, 100000 , iCurPage , customerUuid, customerName, date, statType, 1);
  	
    int recordCount = ckpvm.getRecordCount();
    int pageCount = ckpvm.getPageCount();
    
    String fileName = "customercollectfeelist.jsp?" + pageUrl;
    String pageInfo = Utils.getPageInfo(iCurPage, 100000, recordCount, pageCount, "", fileName);
   // String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
    List customerVOs = null;
    if (user.isVipType()){
		customerVOs = cm.searchCustomer(datasourceName, "");
	}else{
		customerVOs = cm.searchCustomer(datasourceName, " and fUserUuid = " + user.getUserID() + "");
	}
	String customerContactPerson = "";
	if(!Utils.isEmpty(customerVOs) && !Utils.isNullOrEmpty(customerUuid)){
		for (int i = 0; i < customerVOs.size(); i ++){
			CustomerVO customerVO = (CustomerVO)customerVOs.get(i);
			if(customerUuid.equals(customerVO.getUuid() + "")){
				customerContactPerson = customerVO.getContactPerson();
				break;
			}
		}
	}
%>


<body>
      <table width=1240 style="font-size:12px;" cellpadding=3>
            <tr>
				<td colspan=13 align="left">
					<%@include file="/menu.jsp" %>	
				</td>
			</tr>
      	  <tr>
      	  	 <td colspan=13>
      	  	 	<form method="post" action="customercollectfeelist.jsp">
	      	  	 	<table style="font-size:12px;">
	      	  	 		<tr>
				 	   	  <td align="right">统计日期:<input name="date" id="date" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=date %>"></td>
	      	  	 		  <td align="right">客户名称:</td>
	      	  	 			<td>
	      	  	 			  <select name="customerUuid" id="customerUuid">
	      	  	 			  	   <option value="">选择客户名称</option>
			          	  	 	   <%
				          	  	     for (int i = 0; i < customerVOs.size(); i ++)
				          	  	     {
				          	  	     	CustomerVO customerVO = (CustomerVO)customerVOs.get(i);
				          	  	         if ((customerVO.getUuid() + "").equals(customerUuid))
				          	  	         {
				          	  	              out.println("<option selected value='" + customerVO.getUuid() + "'>" + customerVO.getContactPerson() + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + customerVO.getUuid() + "'>" + customerVO.getContactPerson() + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>
			          	  	 </select>
			          	  </td>
			          	  <td align="right">客户名称:<input name="customerName" id="customerName" type="text" style="width:90px;" value="<%=customerName %>"></td>
			          	  <td align="right">总计类型:</td>
	      	  	 			<td>
	      	  	 			  <select name="statType" id="statType">		
			          	  	 	   <%
			          	  	 	   	 String []statTypeValues = {"NeedPay","All"};
				          	  	     String []statTypes = {"可收费","所有"};						          	  	     
				          	  	     for (int i = 0; i < statTypes.length; i ++)
				          	  	     {
				          	  	         if (statTypeValues[i].equals(statType))
				          	  	         {
				          	  	              out.println("<option selected value='" + statTypeValues[i] + "'>" + statTypes[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + statTypeValues[i] + "'>" + statTypes[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>
			          	  	 </select>
			          	  </td>
			          	  <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
			          	  <td><a href="/customerkeyword/DownloadCustomerCollectFeeListWithLog.jsp?statType=<%=statType%>&fileName=CustomerCollectFeeListWithLog<%=Utils.isNullOrEmpty(customerContactPerson) ? "" : "_" + URLEncoder.encode(customerContactPerson, "GBK")%><%=Utils.isNullOrEmpty(date) ? "" : "_" + date%>.xls&customerUuid=<%=customerUuid%>&date=<%=date%>&customerName=<%=customerName%>">下载统计结果带日志</a></td>
			          	  <td><a href="/customerkeyword/DownloadCustomerCollectFeeList.jsp?statType=<%=statType%>&fileName=CustomerCollectFeeList<%=Utils.isNullOrEmpty(customerContactPerson) ? "" : "_" + URLEncoder.encode(customerContactPerson, "GBK")%><%=Utils.isNullOrEmpty(date) ? "" : "_" + date%>.xls&customerUuid=<%=customerUuid%>&date=<%=date%>&customerName=<%=customerName%>">下载统计结果</a></td>
			          	  <td><a href="/customerkeyword/DownloadPositionHistoryLog.jsp?statType=<%=statType%>&fileName=CustomerPositionHistoryLog<%=Utils.isNullOrEmpty(customerContactPerson) ? "" : "_" + URLEncoder.encode(customerContactPerson, "GBK")%><%=Utils.isNullOrEmpty(date) ? "" : "_" + date%>.xls&customerUuid=<%=customerUuid%>&date=<%=date%>&customerName=<%=customerName%>">下载24小时内排名日志</a></td>
	      	  	 		</tr>
		      	  	</table>
      	  	 	</form>
      	  	 </td>
      	  </tr>      	  
          <tr bgcolor="#eeeeee" height=30>
          	   <td align="center" width=60>客户</td>
		       <td align="center" width=120>关键字</td>
		       <td align="center" width=200>原始网址</td>
		       <td align="center" width=200>手机原始网址</td>
		       <td align="center" width=60>收费方式</td>
		       <td align="center" width=150>电脑/触屏/极速排名</td>
		       <td align="center" width=60>电脑收费</td>
		       <td align="center" width=60>触屏收费</td>
		       <td align="center" width=60>极速收费</td>
		       <td align="center" width=60>小计</td>
		   </tr>
          <%
              String trClass = "";
          	  String keywordColor = "";
              String webUrl = "";
              double total = 0;
              for (int i = 0; i < itemList.size(); i ++)
              {
                  CustomerKeywordPositionView viewVO = (CustomerKeywordPositionView) itemList.get(i);                  
                  trClass= "";
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
                  total = total + viewVO.getSubTotal();
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                 	 <td><a href="/customerkeyword/list.jsp?status=1&customerUuid=<%=viewVO.getCustomerUuid()%>" target="_blank"><%=viewVO.getContactPerson()%></a></td>                                            
                     <td><%=viewVO.getKeyword()%></td>
                     <td><%=viewVO.getApplicableUrl()%></td>
                     <td><%=viewVO.getApplicablePhoneUrl()%></td>
			         <td><%=viewVO.getCollectMethodName()%></td>
			         <td>
			         	<div style="height:16;">最优:<%=viewVO.getPcPosition()%>/<%=viewVO.getChupingPosition()%>/<%=viewVO.getJisuPosition()%></div>
			         	<div style="height:16;">最新:<%=viewVO.getCurrentPosition()%>/<%=viewVO.getChupingCurrentPosition()%>/<%=viewVO.getJisuCurrentPosition()%></div>
			         </td>
			         <td nowrap=true><%=viewVO.getPcFeeString()%>
			         <td nowrap=true><%=viewVO.getChupingFeeString()%>
			         <td nowrap=true><%=viewVO.getJisuFeeString()%>
			         <td nowrap=true><%=viewVO.getSubTotalString()%>
                  </tr>
          <%
              }
          %>
          
          <tr>
          	<td colspan=7>
          		  <br>
          		  <%=pageInfo%>
          	</td>
          	<td colspan=3>
          		  <br>
          		  总计应收: <%=Utils.formatDouble(total)%>
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
		e = e||window.event;
		var div1 = document.getElementById('div1'); //将要弹出的层 
		div1.innerText = content;
		div1.style.display="block"; //div1初始状态是不可见的，设置可为可见  
		div1.style.left=e.clientX+10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容 
		div1.style.top=e.clientY+5; 
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
</div>
</body>
</html>

