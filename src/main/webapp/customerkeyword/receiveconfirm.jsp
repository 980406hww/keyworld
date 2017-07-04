<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="ckalm" scope="page" class="com.keymanager.manager.CustomerKeywordAccountLogManager" />
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
	 String customerKeywordAccountLogUuid = request.getParameter("customerKeywordAccountLogUuid");
	 
     String receivable = request.getParameter("receivable");
     String realCollection = request.getParameter("realCollection");
     String receivedTime = request.getParameter("receivedTime");
     String remarks = request.getParameter("remarks");
     
     String effectiveFromTime = request.getParameter("effectiveFromTime");
     String effectiveToTime = request.getParameter("effectiveToTime");
     
     String firstRealCollection = request.getParameter("firstRealCollection");
     String[] payAll = request.getParameterValues("payAll");
     
	if (receivable == null || receivable.trim().equals(""))
	{
%>
        <script language="javascript">
        	alert("应收款不能为空！");
			window.history.back();
		</script>
<%		  
	       return;  
		}     
		
	if (!((Utils.isNullOrEmpty(realCollection)&& Utils.isNullOrEmpty(receivedTime)) || (!Utils.isNullOrEmpty(realCollection) && !Utils.isNullOrEmpty(receivedTime)))){
%>
	    <script language="javascript">
        	alert("实收款和收款日期要同时填或者同时不填，不填只算修改！");
			window.history.back();
		</script>
<%		
	}
     CustomerKeywordAccountLogVO customerKeywordAccountLogVO = new CustomerKeywordAccountLogVO();
     if (customerKeywordAccountLogUuid != null && customerKeywordAccountLogUuid.trim() != ""){ 
    	 customerKeywordAccountLogVO.setUuid(Integer.parseInt(customerKeywordAccountLogUuid.trim()));
     }
     if (customerKeywordUuid != null && customerKeywordUuid.trim() != ""){
    	 customerKeywordAccountLogVO.setCustomerKeywordUuid(Integer.parseInt(customerKeywordUuid.trim())); 
     }
     
     if (receivable != null && receivable.trim() != ""){
    	 customerKeywordAccountLogVO.setReceivable(Integer.parseInt(receivable.trim())); 
     }     
     if (remarks != null && remarks.trim() != ""){
    	 customerKeywordAccountLogVO.setRemarks(remarks.trim()); 
     }
     if (payAll != null && Constants.ACCOUNT_LOG_STATUS_PAID_ALL.equals(payAll[0])){
    	 customerKeywordAccountLogVO.setStatus(Constants.ACCOUNT_LOG_STATUS_PAID_ALL);
     }else if (!Utils.isNullOrEmpty(realCollection)){
    	 customerKeywordAccountLogVO.setStatus(Constants.ACCOUNT_LOG_STATUS_PAID_PARTIALLY);
     }else{
    	 customerKeywordAccountLogVO.setStatus(Constants.ACCOUNT_LOG_STATUS_UN_PAID);
     }
     
     if (effectiveFromTime != null && effectiveFromTime.trim() != ""){
   		customerKeywordAccountLogVO.setEffectiveFromTime(Utils.string2Timestamp(effectiveFromTime.trim())); 
     } 
     
     if (effectiveToTime != null && effectiveToTime.trim() != ""){
   		customerKeywordAccountLogVO.setEffectiveToTime(Utils.string2Timestamp(effectiveToTime.trim())); 
     } 
     customerKeywordAccountLogVO.setMonth(Utils.formatDatetime(customerKeywordAccountLogVO.getEffectiveFromTime(), "yyyy-MM"));
     
     if(Utils.isNullOrEmpty(firstRealCollection) || "0".equals(firstRealCollection)){
    	 if (realCollection != null && realCollection.trim() != ""){
      	 	customerKeywordAccountLogVO.setFirstRealCollection(Integer.parseInt(realCollection.trim())); 
     	}
    	if (receivedTime != null && receivedTime.trim() != ""){
      		customerKeywordAccountLogVO.setFirstReceivedTime(Utils.string2Timestamp(receivedTime.trim())); 
        } 
    	if (customerKeywordAccountLogVO.getUuid() > 0){
    		ckalm.updateFirstCustomerKeywordAccountLog(customerKeywordAccountLogVO, datasourceName);
    	}else{
    		ckalm.payForNewAddedCustomerKeywordAccountLog(customerKeywordAccountLogVO, datasourceName);
    	}
     }else{
	     if (realCollection != null && realCollection.trim() != ""){
	    	 customerKeywordAccountLogVO.setSecondRealCollection(Integer.parseInt(realCollection.trim())); 
	     }
	     if (receivedTime != null && receivedTime.trim() != ""){
	    	 customerKeywordAccountLogVO.setSecondReceivedTime(Utils.string2Timestamp(receivedTime.trim())); 
	     }
	     ckalm.updateSecondCustomerKeywordAccountLog(customerKeywordAccountLogVO, datasourceName);
     }
%>

<script language="javascript">
	   alert("收款成功！");
	   document.location.href = "/customerkeyword/receive.jsp?customerKeywordUuid=<%=customerKeywordUuid%>";
</script>




