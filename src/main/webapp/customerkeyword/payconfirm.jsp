<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="ckplm" scope="page" class="com.keymanager.manager.CustomerKeywordPaymentLogManager" />
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
	 String customerKeywordUuid = request.getParameter("customerKeywordUuid");
	 String customerUuid = request.getParameter("customerUuid");
	 
     String payable = request.getParameter("payable");
     String realPaid = request.getParameter("realPaid");
     String paidTime = request.getParameter("paidTime");
     String remarks = request.getParameter("remarks");
     
     String effectiveFromTime = request.getParameter("effectiveFromTime");
     String effectiveToTime = request.getParameter("effectiveToTime");
     
	if (payable == null || payable.trim().equals(""))
	{
%>
        <script language="javascript">
        	alert("应付款不能为空！");
			window.history.back();
		</script>
<%		  
	       return;  
		}     
		
	if (!((Utils.isNullOrEmpty(realPaid)&& Utils.isNullOrEmpty(paidTime)) || (!Utils.isNullOrEmpty(realPaid) && !Utils.isNullOrEmpty(paidTime)))){
%>
	    <script language="javascript">
        	alert("实付款和付款日期要同时填或者同时不填，不填只算修改！");
			window.history.back();
		</script>
<%		
	}
     CustomerKeywordPaymentLogVO customerKeywordPaymentLogVO = new CustomerKeywordPaymentLogVO();
     if (customerKeywordUuid != null && customerKeywordUuid.trim() != ""){
    	 customerKeywordPaymentLogVO.setCustomerKeywordUuid(Integer.parseInt(customerKeywordUuid.trim())); 
     }
     
     if (payable != null && payable.trim() != ""){
    	 customerKeywordPaymentLogVO.setPayable(Integer.parseInt(payable.trim())); 
     }     
     
     if (realPaid != null && realPaid.trim() != ""){
    	 customerKeywordPaymentLogVO.setRealPaid(Integer.parseInt(realPaid.trim())); 
     }     
     
     if (remarks != null && remarks.trim() != ""){
    	 customerKeywordPaymentLogVO.setRemarks(remarks.trim()); 
     }
     
     if (effectiveFromTime != null && effectiveFromTime.trim() != ""){
    	 customerKeywordPaymentLogVO.setEffectiveFromTime(Utils.string2Timestamp(effectiveFromTime.trim())); 
     } 
     
     if (effectiveToTime != null && effectiveToTime.trim() != ""){
    	 customerKeywordPaymentLogVO.setEffectiveToTime(Utils.string2Timestamp(effectiveToTime.trim())); 
     } 
     
     if (paidTime != null && paidTime.trim() != ""){
    	 customerKeywordPaymentLogVO.setPaidTime(Utils.string2Timestamp(paidTime.trim())); 
     }
	 
     ckplm.addCustomerKeywordPaymentLog(customerKeywordPaymentLogVO, datasourceName);
%>

<script language="javascript">
	   alert("付款成功！");
	   document.location.href = "/customerkeyword/pay.jsp?customerKeywordUuid=<%=customerKeywordUuid%>";
</script>




