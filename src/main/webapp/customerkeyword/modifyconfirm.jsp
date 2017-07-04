<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
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
		String type = (String) session.getAttribute("entry");
		
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
	 String uuid = request.getParameter("uuid");
	 String customerUuid = request.getParameter("customerUuid");
     String keyword = request.getParameter("keyword");
     String optimizeGroupName = request.getParameter("optimizeGroupName");
     
     //PC专用    
     String url = request.getParameter("url");
     String originalUrl = request.getParameter("originalUrl");
     String searchEngine = request.getParameter("searchEngine");
     String currentPosition = request.getParameter("currentPosition");
     String currentIndexCount =  request.getParameter("currentIndexCount");
     String optimizePlanCount = request.getParameter("optimizePlanCount");
     
     String serviceProvider = request.getParameter("serviceProvider");

     String sequence =  request.getParameter("sequence");

     String positionFirstCost = request.getParameter("positionFirstCost");
     String positionSecondCost = request.getParameter("positionSecondCost");
     String positionThirdCost = request.getParameter("positionThirdCost");
     String positionForthCost = request.getParameter("positionForthCost");
     String positionFifthCost = request.getParameter("positionFifthCost");
     
     String positionFirstFee = request.getParameter("positionFirstFee");
     String positionSecondFee = request.getParameter("positionSecondFee");
     String positionThirdFee = request.getParameter("positionThirdFee");
     String positionForthFee = request.getParameter("positionForthFee");
     String positionFifthFee = request.getParameter("positionFifthFee");
     String positionFirstPageFee = request.getParameter("positionFirstPageFee");
     
     String optimizePositionFirstPercentage = request.getParameter("optimizePositionFirstPercentage");
     String optimizePositionSecondPercentage = request.getParameter("optimizePositionSecondPercentage");
     String optimizePositionThirdPercentage = request.getParameter("optimizePositionThirdPercentage");
     
     String effectiveFromTime = request.getParameter("effectiveFromTime");
     String effectiveToTime = request.getParameter("effectiveToTime");
     
     String title = request.getParameter("title");
     String phoneTitle = request.getParameter("phoneTitle");
     
     //手机专用    
     String phoneUrl = request.getParameter("phoneUrl");
     String phoneNsrc = request.getParameter("phoneNsrc");
     String originalPhoneUrl = request.getParameter("originalPhoneUrl");
     String phoneCurrentIndexCount =  request.getParameter("phoneCurrentIndexCount");
     String phoneDescription = request.getParameter("phoneDescription");
     
     String orderNumber = request.getParameter("orderNumber");
     String paymentStatus = request.getParameter("paymentStatus");
     String remarks = request.getParameter("remarks");
     
     String jisuCurrentPosition = request.getParameter("jisuCurrentPosition");
     
     String jisuPositionFirstFee = request.getParameter("jisuPositionFirstFee");
     String jisuPositionSecondFee = request.getParameter("jisuPositionSecondFee");
     String jisuPositionThirdFee = request.getParameter("jisuPositionThirdFee");
     String jisuPositionForthFee = request.getParameter("jisuPositionForthFee");
     String jisuPositionFifthFee = request.getParameter("jisuPositionFifthFee");
     String jisuPositionFirstPageFee = request.getParameter("jisuPositionFirstPageFee");
     
     String jisuEffectiveFromTime = request.getParameter("jisuEffectiveFromTime");
     String jisuEffectiveToTime = request.getParameter("jisuEffectiveToTime");
     
     
     String chupingCurrentPosition = request.getParameter("chupingCurrentPosition");
     
     String chupingPositionFirstFee = request.getParameter("chupingPositionFirstFee");
     String chupingPositionSecondFee = request.getParameter("chupingPositionSecondFee");
     String chupingPositionThirdFee = request.getParameter("chupingPositionThirdFee");
     String chupingPositionForthFee = request.getParameter("chupingPositionForthFee");
     String chupingPositionFifthFee = request.getParameter("chupingPositionFifthFee");
     String chupingPositionFirstPageFee = request.getParameter("chupingPositionFirstPageFee");
     
     
     String chupingEffectiveFromTime = request.getParameter("chupingEffectiveFromTime");
     String chupingEffectiveToTime = request.getParameter("chupingEffectiveToTime");
     
     String phoneOptimizePositionFirstPercentage = request.getParameter("phoneOptimizePositionFirstPercentage");
     String phoneOptimizePositionSecondPercentage = request.getParameter("phoneOptimizePositionSecondPercentage");
     String phoneOptimizePositionThirdPercentage = request.getParameter("phoneOptimizePositionThirdPercentage");
     
     
     
     String collectMethod = request.getParameter("collectMethod");
     String status = request.getParameter("status"); 
     String relatedKeywords = request.getParameter("relatedKeywords");

     
	if (keyword == null || keyword.equals(""))
	{
%>
        <script language="javascript">
        	alert("关键字不能为空");
			window.history.back();
		</script>
<%		  
	       return;  
		} 
		keyword = keyword.replaceAll("　"," ");
		keyword = keyword.replaceAll("'","");
		keyword = keyword.replaceAll("。","");
		keyword = keyword.trim();
		keyword = Utils.trimdot(keyword);
			
	/*	int keywordCount = ckm.getTotalActiveCustomerKeywordCount(datasourceName,keyword);
	
		if (keywordCount >= 3)
		{
			
%>
        <script language="javascript">
        	 alert("每个关键字最大只能刷3个域名，你可以先暂停其他域名！");
			 window.history.back();
		</script>
<%
	       return;
 		 }
		
		*/
     CustomerKeywordVO customerKeyword = new CustomerKeywordVO();
     customerKeyword.setUuid(Integer.parseInt(uuid));
     if (customerUuid != null && !customerUuid.trim().equals("")){ 
     	customerKeyword.setCustomerUuid(Integer.parseInt(customerUuid.trim()));
     }
     customerKeyword.setType(type);
     customerKeyword.setKeyword(keyword.trim());   
     if (optimizeGroupName != null && optimizeGroupName.trim() != ""){
    	 customerKeyword.setOptimizeGroupName(optimizeGroupName.trim()); 
     }
       
     customerKeyword.setUrl(url.trim());
     if (!Utils.isNullOrEmpty(url)){
     	customerKeyword.setUrl(url.trim());
     }else{
     	customerKeyword.setUrl("");
     }  
     
     
     if (!Utils.isNullOrEmpty(title)){
     	customerKeyword.setTitle(title.trim());
     }else{
     	customerKeyword.setTitle("");
     }  
     
     if (!Utils.isNullOrEmpty(originalUrl)){
     	customerKeyword.setOriginalUrl(originalUrl.trim());
     }else{
     	customerKeyword.setOriginalUrl("");
     }  
     customerKeyword.setSearchEngine(searchEngine.trim());
     if (!Utils.isNullOrEmpty(currentPosition)){
     	customerKeyword.setCurrentPosition(Integer.parseInt(currentPosition.trim()));
     }
     if (!Utils.isNullOrEmpty(currentIndexCount)){
    	 customerKeyword.setCurrentIndexCount(Integer.parseInt(currentIndexCount.trim()));
     }
     if(!Utils.isNullOrEmpty(optimizePlanCount)){
     	customerKeyword.setOptimizePlanCount(Integer.parseInt(optimizePlanCount.trim()));
     }
     customerKeyword.setServiceProvider(serviceProvider);

     if (!Utils.isNullOrEmpty(sequence)){
          customerKeyword.setSequence(Integer.parseInt(sequence));
     }

     if (!Utils.isNullOrEmpty(positionFirstCost)){
    	 customerKeyword.setPositionFirstCost(Double.parseDouble(positionFirstCost.trim())); 
     }
     if (!Utils.isNullOrEmpty(positionSecondCost)){
    	 customerKeyword.setPositionSecondCost(Double.parseDouble(positionSecondCost.trim())); 
     }
     if (!Utils.isNullOrEmpty(positionThirdCost)){
    	 customerKeyword.setPositionThirdCost(Double.parseDouble(positionThirdCost.trim())); 
     }
     if (!Utils.isNullOrEmpty(positionForthCost)){
    	 customerKeyword.setPositionForthCost(Double.parseDouble(positionForthCost.trim())); 
     }
     if (!Utils.isNullOrEmpty(positionFifthCost)){
    	 customerKeyword.setPositionFifthCost(Double.parseDouble(positionFifthCost.trim())); 
     }

     
     if (!Utils.isNullOrEmpty(positionFirstFee)){
    	 customerKeyword.setPositionFirstFee(Double.parseDouble(positionFirstFee.trim())); 
     }
     if (!Utils.isNullOrEmpty(positionSecondFee)){
    	 customerKeyword.setPositionSecondFee(Double.parseDouble(positionSecondFee.trim())); 
     }
     if (!Utils.isNullOrEmpty(positionThirdFee)){
    	 customerKeyword.setPositionThirdFee(Double.parseDouble(positionThirdFee.trim())); 
     }
     if (!Utils.isNullOrEmpty(positionForthFee)){
    	 customerKeyword.setPositionForthFee(Double.parseDouble(positionForthFee.trim())); 
     }
     if (!Utils.isNullOrEmpty(positionFifthFee)){
    	 customerKeyword.setPositionFifthFee(Double.parseDouble(positionFifthFee.trim())); 
     }
     if (!Utils.isNullOrEmpty(positionFirstPageFee)){
    	 customerKeyword.setPositionFirstPageFee(Double.parseDouble(positionFirstPageFee.trim())); 
     }
     
     
     if (!Utils.isNullOrEmpty(optimizePositionFirstPercentage)){
    	 customerKeyword.setOptimizePositionFirstPercentage(Integer.parseInt(optimizePositionFirstPercentage.trim())); 
     }else{
    	 customerKeyword.setOptimizePositionFirstPercentage(0);
     }
     
     if (!Utils.isNullOrEmpty(optimizePositionSecondPercentage)){
    	 customerKeyword.setOptimizePositionSecondPercentage(Integer.parseInt(optimizePositionSecondPercentage.trim())); 
     }else{
    	 customerKeyword.setOptimizePositionSecondPercentage(0);
     }
     
     if (!Utils.isNullOrEmpty(optimizePositionThirdPercentage)){
    	 customerKeyword.setOptimizePositionThirdPercentage(Integer.parseInt(optimizePositionThirdPercentage.trim())); 
     }else{
    	 customerKeyword.setOptimizePositionThirdPercentage(0);
     }
     
     if (!Utils.isNullOrEmpty(effectiveFromTime)){
    	 customerKeyword.setEffectiveFromTime(Utils.string2Timestamp(effectiveFromTime.trim())); 
     }
     if (!Utils.isNullOrEmpty(effectiveToTime)){
    	 customerKeyword.setEffectiveToTime(Utils.string2Timestamp(effectiveToTime.trim())); 
     }
     
     if (!Utils.isNullOrEmpty(orderNumber)){
      	customerKeyword.setOrderNumber(orderNumber.trim());
      }else{
      	customerKeyword.setOrderNumber("");
      }
     if (!Utils.isNullOrEmpty(paymentStatus)){
      	customerKeyword.setPaymentStatus(paymentStatus.trim());
      }else{
      	customerKeyword.setPaymentStatus("");
      }
     if (!Utils.isNullOrEmpty(remarks)){
      	customerKeyword.setRemarks(remarks.trim());
      }else{
      	customerKeyword.setRemarks("");
      }
   
     
     if (collectMethod != null && !collectMethod.trim().equals("")){
    	 customerKeyword.setCollectMethod(collectMethod.trim()); 
     }
     if (status != null && !status.trim().equals("")){
    	 customerKeyword.setStatus(Integer.parseInt(status.trim())); 
     }
     if (relatedKeywords != null && relatedKeywords.trim() != ""){
    	 customerKeyword.setRelatedKeywords(relatedKeywords.trim()); 
     }
     
	 ckm.updateCustomerKeyword(customerKeyword, datasourceName);     
%>

<script language="javascript">
	   alert("关键字修改完毕！");
	   document.location.href = "/customerkeyword/list.jsp?status=1&customerUuid=<%=customerUuid%>";
</script>




