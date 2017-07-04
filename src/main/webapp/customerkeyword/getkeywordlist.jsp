<%@page contentType="text/html;charset=utf-8"  %> <%@page import="com.keymanager.manager.*,java.util.*,com.keymanager.value.*,com.keymanager.util.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="km" scope="page" class="com.keymanager.manager.KeywordManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String captureIndex = request.getParameter("captureindex");
	String type = request.getParameter("type");
	String group = request.getParameter("group");
	String customerName = request.getParameter("customername");
	String count = request.getParameter("count");
	String timeDuration = request.getParameter("timeDuration");
    username = Utils.parseParam(username);
    password = Utils.parseParam(password);
	String dsName = "keyword";
	UserVO user = um.login(dsName,username,password);
	String returnInfo = "Invalid User"; //invalid user
	if(user != null){
		try{
			if ("1".equals(captureIndex)){
				returnInfo = ckm.searchCustomerKeywordForCaptureIndex(dsName, 60*24*2, type, Utils.isNullOrEmpty(count) ? 30 : Integer.parseInt(count.trim()));
			}else{
				returnInfo = ckm.searchCustomerKeywordForCapturePosition(dsName, (timeDuration == null) ? 30 : Integer.parseInt(timeDuration), group,
						customerName, type, 1);
			}
		}catch(Exception ex){
			returnInfo = "error";
		}
	}
	out.print(returnInfo);
%>