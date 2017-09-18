<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>
<%@ page import="com.keymanager.util.TerminalTypeMapping" %>
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
     
	 String customerUuid = request.getParameter("customerUuid");
     String keyword = request.getParameter("keyword");
     String url = request.getParameter("url");
     String originalUrl = request.getParameter("originalUrl");

     String sequence = request.getParameter("sequence");

     String snapshotDateTime = request.getParameter("snapshotDateTime");
     String title = request.getParameter("title");
     String searchEngine = request.getParameter("searchEngine");
     String initialIndexCount = request.getParameter("initialIndexCount");
     String initialPosition =  request.getParameter("initialPosition");
     String serviceProvider = request.getParameter("serviceProvider");
     String relatedKeywords = request.getParameter("relatedKeywords");
     String optimizeGroupName = request.getParameter("optimizeGroupName");
     
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
     
     
     String collectMethod = request.getParameter("collectMethod");
     String startOptimizedTime = request.getParameter("startOptimizedTime");
     String effectiveFromTime = request.getParameter("effectiveFromTime");
     String effectiveToTime = request.getParameter("effectiveToTime");
     String status = request.getParameter("status");
     
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
		
		//int keywordCount = ckm.getTotalActiveCustomerKeywordCount(datasourceName,keyword);
	
     CustomerKeywordVO customerKeyword = new CustomerKeywordVO();
     if (!Utils.isNullOrEmpty(customerUuid)){ 
     	customerKeyword.setCustomerUuid(Integer.parseInt(customerUuid.trim()));
     }
     customerKeyword.setType(type);
     customerKeyword.setKeyword(keyword.trim());     
     customerKeyword.setUrl(Utils.isNullOrEmpty(url) ? "" : url.trim());
     customerKeyword.setOriginalUrl(Utils.isNullOrEmpty(originalUrl) ? "" : originalUrl.trim());
     customerKeyword.setTitle(Utils.isNullOrEmpty(title) ? "" : title.trim());
     customerKeyword.setTerminalType(terminalType);
     customerKeyword.setSearchEngine(searchEngine.trim());
     if (!Utils.isNullOrEmpty(initialIndexCount)){
     	customerKeyword.setInitialIndexCount(Integer.parseInt(initialIndexCount.trim()));
     	customerKeyword.setCurrentIndexCount(Integer.parseInt(initialIndexCount.trim()));
     }
     if (!Utils.isNullOrEmpty(initialPosition)){
    	 customerKeyword.setInitialPosition(Integer.parseInt(initialPosition.trim())); 
    	 customerKeyword.setCurrentPosition(Integer.parseInt(initialPosition.trim()));
     }

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
     
     if (!Utils.isNullOrEmpty(collectMethod)){
    	 customerKeyword.setCollectMethod(collectMethod.trim()); 
     }
     if (!Utils.isNullOrEmpty(startOptimizedTime)){
    	 customerKeyword.setStartOptimizedTime(Utils.string2Timestamp(startOptimizedTime.trim())); 
     }
     if (!Utils.isNullOrEmpty(effectiveFromTime)){
    	 customerKeyword.setEffectiveFromTime(Utils.string2Timestamp(effectiveFromTime.trim())); 
     }
     if (!Utils.isNullOrEmpty(effectiveToTime)){
    	 customerKeyword.setEffectiveToTime(Utils.string2Timestamp(effectiveToTime.trim())); 
     }
     if (!Utils.isNullOrEmpty(serviceProvider)){
    	 customerKeyword.setServiceProvider(serviceProvider.trim()); 
     }else{
    	 customerKeyword.setServiceProvider("");
     }
     if (!Utils.isNullOrEmpty(status)){
    	 customerKeyword.setStatus(Integer.parseInt(status.trim())); 
     }
     
     if (!Utils.isNullOrEmpty(optimizeGroupName)){
    	 customerKeyword.setOptimizeGroupName(optimizeGroupName.trim()); 
     }
     
     if (!Utils.isNullOrEmpty(relatedKeywords)){
    	 customerKeyword.setRelatedKeywords(relatedKeywords.trim()); 
     }
     
	 ckm.addCustomerKeyword(customerKeyword, datasourceName);     
%>

<script language="javascript">
	   alert("关键字添加完毕！");
	   document.location.href = "/customerkeyword/list.jsp?status=1&customerUuid=<%=customerUuid%>";
</script>




