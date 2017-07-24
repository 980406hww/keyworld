<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="ckplm" scope="page" class="com.keymanager.manager.CustomerKeywordPaymentLogManager" />

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
		if(!user.isVipType()){
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
		CustomerKeywordPaymentLogVO paymentLog = customerKeywordVO.generatePaymentLog();
%>

<html>
	<head>
		  <title>关键字付款</title>
		  <script type="text/javascript" src="/common.js">
		  </script>	  	  
		  <script type="text/javascript" src="pay.js">
		  </script>
		  <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
  	</head>	
			
<body>
	
<form method="post" onsubmit="return checkinput();" action="payconfirm.jsp">
	
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
  		<div style="text-align:left;width:100%;font-size:12px;">付款记录</div>
  		<%@include file="/customerkeyword/paymentlog.jsp" %>
  		<br/>
  		<div style="text-align:right;width:100%;font-size:12px;"><a href="list.jsp?status=1&customerUuid=<%=customer.getUuid()%>">返回关键字列表</a></div>	
  	</td>
  </tr>
<%
		if (paymentLog != null){
%>  
	<tr>
		<td align="right">	 	   
	 	   		   <input type="hidden" name="customerUuid" value="<%=customerKeywordVO.getCustomerUuid()%>">
	 	   		   <input type="hidden" name="customerKeywordUuid" value="<%=customerKeywordUuid%>">
	 	   		   
			 	   <table width=80% style="font-size:12px;" cellpadding=3>
			 	   	  <tr height="30"><td align="right">关键字:</td> <td><%=customerKeywordVO.getKeyword()%></td></tr>
			 	   	  <tr height="30"><td align="right">账单开始日期:</td> <td><input name="effectiveFromTime" id="effectiveFromTime" class="Wdate" type="text" style="width:200px;"  onClick="WdatePicker()" value="<%=Utils.formatDatetime(paymentLog.getEffectiveFromTime(), "yyyy-MM-dd")%>">格式：2014-10-23</td></tr>
			 	   	  <tr height="30"><td align="right">账单结束日期:</td> <td><input name="effectiveToTime" id="effectiveToTime" class="Wdate" type="text" style="width:200px;"  onClick="WdatePicker()" value="<%=Utils.formatDatetime(paymentLog.getEffectiveToTime(), "yyyy-MM-dd")%>">格式：2014-10-23</td></tr>
			 	   	  <tr height="30"><td align="right">应付款:</td> <td><input type="text" name="payable" id="payable" onBlur="setPayAll();" value="<%=paymentLog.getPayable()%>" style="width:200px;" ></td></tr>			 	   	  
			 	   	  <tr height="30"><td align="right">实际付款:</td> <td><input type="text" name="realPaid" id="realPaid" onBlur="setPayAll();" value="<%=paymentLog.getPayable()%>" style="width:200px;" >			 	   	  		 	   	 
			 	   	  	</td></tr>
			 	   	  <tr height="30"><td align="right">付款日期:</td> <td><input name="paidTime" id="paidTime" class="Wdate" type="text" style="width:200px;"  onClick="WdatePicker()" value="<%=Utils.getCurrentDate()%>">格式：2014-10-23</td></tr>
 					  <tr height="30"><td align="right">备注:</td> <td><textarea name="remarks" id="remarks" style="width:200px;height:100px"></textarea></td></tr>
		           	  <tr height="30"><td align="right"></td> 
		            	<td>
		            	    <input type="submit" name="btnsub" id="btnsub" value=" 付款 ">
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




