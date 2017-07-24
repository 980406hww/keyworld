<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="ckalm" scope="page" class="com.keymanager.manager.CustomerKeywordAccountLogManager" />

<style>
   .hiddentr {display:none;}
   .displaytr {dislay:;}
</style>


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
		if(!user.isVipType() && user.getUserLevel() < 2){
%>
	      <script language="javascript">
	      	alert("你没有该权限！");
	      	window.history.back();
		  </script>
<%	
		return;
		}
		String customerKeywordUuid = request.getParameter("customerKeywordUuid");
		CustomerKeywordVO customerKeywordVO = ckm.getCustomerKeywordByUuid(datasourceName, customerKeywordUuid);
		CustomerVO customer = cm.getCustomerByUuid(datasourceName, customerKeywordVO.getCustomerUuid() + "");		
		
		String customerKeywordAccountLogUuid = request.getParameter("uuid");
		CustomerKeywordAccountLogVO customerKeywordAccountLogVO = null;
		if (Utils.isNullOrEmpty(customerKeywordAccountLogUuid)){
			customerKeywordAccountLogVO = ckalm.findLastNonPayAllCustomerKeywordAccountLogVO(datasourceName, customerKeywordUuid);
			String addNew = request.getParameter("addNew");
			if("true".equals(addNew)){
				customerKeywordAccountLogVO = customerKeywordVO.generateAccountLog(1);
			}
		}else{
			customerKeywordAccountLogVO = ckalm.getCustomerKeywordAccountLogByUuid(datasourceName, customerKeywordAccountLogUuid);
		}
%>

<html>
	<head>
		  <title>关键字收款</title>
		  <script type="text/javascript" src="/common.js">
		  </script>	  	  
		  <script type="text/javascript" src="receive.js">
		  </script>
		  <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
  	</head>	
			
<body>
	
<form method="post" onsubmit="return checkinput();" action="receiveconfirm.jsp">
	
<table width="100%">
   <tr>
  	<td>
  		<%@include file="/customer/customerinfo.jsp" %>	
  	</td>
  </tr>
  <tr>
  	<td>
  		<%@include file="/customerkeyword/customerKeywordInfo.jsp" %>	
  	</td>
  </tr>
  <tr>
  	<td>
  		<div style="text-align:left;width:100%;font-size:12px;">收款记录</div>
  		<%@include file="/customerkeyword/accountLog.jsp" %>
  		<br/>
  		<div style="text-align:right;width:100%;font-size:12px;"><%if(customerKeywordAccountLogVO == null){ %>
  			<a href="receive.jsp?customerUuid=<%=customer.getUuid()%>&customerKeywordUuid=<%=customerKeywordUuid%>&addNew=true">产生收款记录</a> <%} %> | <a href="list.jsp?isDelete=1&customerUuid=<%=customer.getUuid()%>">返回关键字列表</a></div>
  	</td>
  </tr>
<%
		if (customerKeywordAccountLogVO != null){
			customerKeywordAccountLogUuid = customerKeywordAccountLogVO.getUuid() + "";
%>  
	<tr>
		<td align="right">	 	   
	 	   		   <input type="hidden" name="customerUuid" value="<%=customerKeywordVO.getCustomerUuid()%>">
	 	   		   <input type="hidden" name="customerKeywordUuid" value="<%=customerKeywordUuid%>">
	 	   		   <input type="hidden" name="customerKeywordAccountLogUuid" value="<%=customerKeywordAccountLogUuid%>">
	 	   		   
	 	   		   <input type="hidden" name="firstRealCollection" value="<%=customerKeywordAccountLogVO.getFirstRealCollection()%>">
			 	   <table width=80% style="font-size:12px;" cellpadding=3>
			 	   	  <tr height="30"><td align="right">关键字:</td> <td><%=customerKeywordVO.getKeyword()%></td></tr>
			 	   	  <tr height="30"><td align="right">账单月份:</td> <td><%=customerKeywordAccountLogVO.getMonth()%></td></tr>
			 	   	  <tr height="30"><td align="right">账单开始日期:</td> <td><input name="effectiveFromTime" id="effectiveFromTime" class="Wdate" type="text" style="width:200px;"  onClick="WdatePicker()" value="<%=Utils.formatDatetime(customerKeywordAccountLogVO.getEffectiveFromTime(), "yyyy-MM-dd")%>">格式：2014-10-23</td></tr>
			 	   	  <tr height="30"><td align="right">账单结束日期:</td> <td><input name="effectiveToTime" id="effectiveToTime" class="Wdate" type="text" style="width:200px;"  onClick="WdatePicker()" value="<%=Utils.formatDatetime(customerKeywordAccountLogVO.getEffectiveToTime(), "yyyy-MM-dd")%>">格式：2014-10-23</td></tr>
			 	   	  <tr height="30"><td align="right">应收款:</td> <td><input type="text" name="receivable" id="receivable" onBlur="setPayAll();" value="<%=customerKeywordAccountLogVO.getReceivable()%>" style="width:200px;" ></td></tr>			 	   	  
			 	   	  <tr height="30"><td align="right">还需收款:</td> <td><input type="text" name="realCollection" id="realCollection" onBlur="setPayAll();" value="<%=(customerKeywordAccountLogVO.getReceivable()-customerKeywordAccountLogVO.getFirstRealCollection())%>" style="width:200px;" >
			 	   	  	<% if (customerKeywordAccountLogVO.getFirstRealCollection() > 0){
			 	   	  	%>
			 	   	  		(于<%=Utils.formatDatetime(customerKeywordAccountLogVO.getFirstReceivedTime(), "yyyy-MM-dd")%>已收<%=customerKeywordAccountLogVO.getFirstRealCollection()%>)
			 	   	  	<%
				 	   	  	}
			 	   	  	%>			 	   	  
			 	   	  	</td></tr>
			 	   	  <tr height="30"><td align="right">收款日期:</td> <td><input name="receivedTime" id="receivedTime" class="Wdate" type="text" style="width:200px;"  onClick="WdatePicker()" value="<%=Utils.getCurrentDate()%>">格式：2014-10-23</td></tr>
 					  <tr height="30"><td align="right">备注:</td> <td><textarea name="remarks" id="remarks" style="width:200px;height:100px"><%=customerKeywordAccountLogVO.getRemarks() == null ? "" : customerKeywordAccountLogVO.getRemarks()%></textarea></td></tr>
		           	  <tr height="30"><td align="right"></td> 
		            	<td>
		            	    <input type="submit" name="btnsub" id="btnsub" value=" 收款 ">&nbsp;&nbsp;&nbsp;<input id="payAll" name="payAll" type="checkbox" value="<%=Constants.ACCOUNT_LOG_STATUS_PAID_ALL%>" checked=true>结清</input>
		            </td>
		           </tr>
		       </table>
  </td>
  </tr>
	
<%			
		}  
%>
</table>
</form>	 
</body>
<html>




