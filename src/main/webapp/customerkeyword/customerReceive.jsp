<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
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
		
		String showType = request.getParameter("showType");	
		String customerUuid = request.getParameter("customerUuid");	
		CustomerVO customer = cm.getCustomerByUuid(datasourceName, customerUuid);
		if (customer == null){
%>
        <script language="javascript">
        	alert("非法的客户请求！")
			window.location.href="/bd.html";
	  	</script>
<%
		return;
		}
		
		String condition = "";
		
		String curPage = request.getParameter("pg");
		
		if (curPage == null || curPage.equals(""))
		{
		    curPage = "1";
		}
		
		int iCurPage = Integer.parseInt(curPage);
    
		if (customerUuid != null && customerUuid.trim() != ""){
    		condition = condition + " and fCustomerUuid = " + customerUuid.trim() + " ";
		}
		
		if (!"showAll".equals(showType)){
			condition = condition + " and EXISTS (SELECT 1 FROM t_ck_account_log l WHERE l.fCustomerKeywordUuid = ck.fUuid AND l.fStatus IN ('UnPaid', 'PaidPartially') AND l.fEffectiveFromTime = ck.fEffectiveFromTime AND l.fEffectiveToTime = ck.fEffectiveToTime) ";
		}
    
	    List itemList = null;
	    
	  	itemList = ckm.searchCustomerKeywords(datasourceName, 30 , iCurPage , condition , " order by fCreateTime desc ",1);
	  	
	  	itemList = ckm.fetchCustomerKeywordAccountLogs(datasourceName, itemList);
	  	
	    int recordCount = ckm.getRecordCount();
	    int pageCount = ckm.getPageCount();
	    
	    String fileName = "/customerkeyword/customerReceive.jsp?customerUuid=" + customerUuid + "&showType=" + showType;
	    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
	    
	    int columnCount = 13;
	    if(user.isVipType()){
	    	columnCount = 15;
	    }
%>

<html>
	<head>
		  <title>关键字列表</title>
		  <style>
			.wrap {word-break: break-all; word-wrap:break-word;}
		  </style>
		  <script language="javascript" type="text/javascript" src="/common.js"></script>
		  <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
		  <script language="javascript" type="text/javascript" src="customerReceive.js"></script>
  	</head>	
<body>
<form method="post" onsubmit="return checkinput(<%=itemList.size()%>);" action="customerReceiveConfirm.jsp">
	  <input type="hidden" id="customerUuid" name="customerUuid" value="<%=customerUuid%>">	 
      <table width=1280 style="font-size:12px;" cellpadding=3>
      	  <tr>
      	  	<td colspan=<%=columnCount%>>
      	  		<%@include file="/customer/customerinfo.jsp" %>	
      	  	</td>
      	  </tr>
      	  <tr>
      	  	 <td colspan=<%=columnCount%> align="right">
      	  	 	<a href="customerReceive.jsp?customerUuid=<%=customerUuid%>&showType=showAll">显示所有</a>       	  	 	
      	  	  	|  
      	  	 	<a href="customerReceive.jsp?customerUuid=<%=customerUuid%>">显示欠费</a>       	  	 	
      	  	 	|  
      	  	 	<a href="keywordfinder.jsp?isDelete=1">关键字统计</a>
      	  	 	| <a href="add.jsp?customerUuid=<%=customerUuid.trim()%>">增加新关键字</a> |        	  	 	
      	  	 	<a href="/customer/customerlist.jsp">客户清单</a> | <a href="/user/updatepassword.jsp">修改密码</a> | <a href="/user/logout.jsp">退出</a>
      	  	 	</td>
      	  </tr>
          <tr bgcolor="#eeeeee" height=30>
          	  <td align="center" width=20>选择</td>
              <td align="center" width=90>关键字</td>
              <td align="center" width=100>URL</td>
              <td align="center" width=190>开始日期 / 结束日期</td>
              <td align="center" width=40>应收款</td>
              <td align="center" width=100>还需收款</td>
              <td align="center" width=90>收款日期</td>
              <td align="center" width=80>金额/收款日期</td>
              <td align="center" width=26>引擎</td>
              <td align="center" width=85>指数/初始/当前</td>
              <td align="center" width=26>计价方式</td>
              <%if(user.isVipType()){ %>
              <td align="center" width=90>成本</td>
              <%} %>
              <td align="center" width=90>报价</td>
              <td align="center" width=25>状态</td>              
              <%if(user.isVipType()){ %>
              <td align="center" width=50>服务商</td>
			  <%} %>                            
          </tr>
          <%
              String trClass = "";
              String webUrl = "";
              String keywordColor = "";
              double total = 0;
              for (int i = 0; i < itemList.size(); i ++)
              {
                  CustomerKeywordVO value = (CustomerKeywordVO) itemList.get(i);   
                  CustomerKeywordAccountLogVO customerKeywordAccountLogVO = value.getLatestCustomerKeywordAccountLog();
                  total = total + (customerKeywordAccountLogVO.getReceivable()-customerKeywordAccountLogVO.getFirstRealCollection());
                  trClass= "";
                  keywordColor = "";
                  if (value.isUnpaid()){
                  	keywordColor = "#FF0000";
                  }else if(value.isPushPay()){
                  	keywordColor = "#FF9966";
                  }else{
                  	keywordColor = "#669900";
                  }
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">                 	  
                 	  <td>
                 	  	<input type="hidden" id="complexValue_<%=i%>" name="complexValue" value="<%=value.generateComplexValue()%>">
                 	    <input type="hidden" id="customerKeywordUuid_<%=i%>" name="customerKeywordUuid_<%=i%>" value="<%=value.getUuid()%>">
                 	    <input type="hidden" id="customerKeywordAccountLogUuid_<%=i%>" name="customerKeywordAccountLogUuid_<%=i%>" value="<%=customerKeywordAccountLogVO.getUuid()%>">
                 	  	<input id="payFlag_<%=i%>" name="payFlag_<%=i%>" type="checkbox" value="1" onClick="calculateTotalPay(<%=itemList.size()%>);" checked=true></input>
                 	  </td>                                            
                      <td class="wrap"><font color="<%=keywordColor%>"><%=value.getKeyword()%></font></td>
                      <td  class="wrap"><%=value.getUrl()%></td>
                      <td>
                      	<input name="effectiveFromTime_<%=i%>" id="effectiveFromTime_<%=i%>" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=Utils.formatDatetime(customerKeywordAccountLogVO.getEffectiveFromTime(), "yyyy-MM-dd") %>">
                      	<input name="effectiveToTime_<%=i%>" id="effectiveToTime_<%=i%>" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=Utils.formatDatetime(customerKeywordAccountLogVO.getEffectiveToTime(), "yyyy-MM-dd") %>">
                      </td>
                      <td><input type="text" name="receivable_<%=i%>" id="receivable_<%=i%>" onBlur="setPayAll(<%=i%>);" value="<%=customerKeywordAccountLogVO.getReceivable()%>" style="width:40px;" ></td>
                      <td>
                      	<input type="text" name="realCollection_<%=i%>" id="realCollection_<%=i%>" onBlur="setPayAll(<%=i%>);calculateTotalPay(<%=itemList.size()%>);" value="<%=(customerKeywordAccountLogVO.getReceivable()-customerKeywordAccountLogVO.getFirstRealCollection())%>" style="width:40px;" >
                      	<input type="hidden" name="firstRealCollection_<%=i%>" id="firstRealCollection_<%=i%>" value="<%=customerKeywordAccountLogVO.getFirstRealCollection()%>" style="width:40px;" >
                      	<input id="payAll_<%=i%>" name="payAll_<%=i%>" type="checkbox" value="1" checked=true>结清</input>                      	
                      </td>
                      <td><input name="receivedTime_<%=i%>" id="receivedTime_<%=i%>" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=Utils.getCurrentDate()%>"></td>   
                      <td>
                      	<% if (customerKeywordAccountLogVO.getFirstRealCollection() > 0){
			 	   	  	%>
			 	   	  	<%=customerKeywordAccountLogVO.getFirstRealCollection()%>/<%=Utils.formatDatetime(customerKeywordAccountLogVO.getFirstReceivedTime(), "yyyy-MM-dd")%>
			 	   	  	<%
				 	   	  	}
			 	   	  	%>		
                      </td>                   
                      <td><%=value.getSearchEngine()%></td>
                      <td>
                      	<a href="historyPositionAndIndex.jsp?uuid=<%=value.getUuid()%>" target="_blank"><%=value.getCurrentIndexCount()%></a>/
                      	<%=value.getInitialPosition()%>/
                      	<a href="<%=value.searchEngineUrl()%><%=value.getKeyword()%>" target="_blank"><%=value.getCurrentPosition()%></a>                      	     
                      </td>     
                      <td><%=value.getCollectMethodName()%></td>
                      <%if(user.isVipType()){ %>                 
                      <td nowrap=true><%=value.costString()%>
                      </td nowrap=true>
                      <%} %>
                      <td><%=value.feeString()%>
                      </td>
                      <td><%=value.getStatusName()%></td>
                      <%if(user.isVipType()){ %>                                          
                      <td><%=value.getServiceProvider()%></td>
					  <%} %>                            
                  </tr>
          <%
              }          	  
          %>
          <tr>
          	<td colspan=14 style="font-size:12px;" align="right">
          		  需要收款总计：<strong><font color="red"><div style="display:inline;" id="totalPay"><%=total%></div></font></strong>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="btnsub" id="btnsub" value=" 收款 ">
          	</td>
          </tr>      
          <tr>
          	<td colspan=14>
          		  <br>
          		  <%=pageInfo%>
          	</td>
          </tr>          
      </table>
      
      
   <br><br><br>
  <br>
  
  <br><br><br><br><br><br><br><br>
<script language="javascript">
function doOver(obj) {
	obj.style.backgroundColor = "green";
}

function doOut(obj) {
	var rowIndex = obj.rowIndex;
	if ((rowIndex % 2) == 0) {
		obj.style.backgroundColor = "#eeeeee";
	} else {
		obj.style.backgroundColor = "#ffffff";
	}
}
function delItem(uuid)
{
   if (confirm("确实要删除这个关键字吗?") == false) return;
   document.location = "delete.jsp?uuid=" + uuid + "&customerUuid=<%=customerUuid%>";
}
</script>


<div style="display:none;">
<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>

</div>
</form>
</body>
</html>

