<%@page contentType="text/html;charset=utf-8"  %> <%@page import="com.download.manager.*,java.util.*,com.download.value.*,com.keymanager.value.*,com.keymanager.util.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="rdm" scope="page" class="com.download.manager.RefreshDownloadManager" />
<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String imei = request.getParameter("imei");
	String type = request.getParameter("type");
	String computerID = request.getParameter("clientid");
	String folder = request.getParameter("folder");
	String number = request.getParameter("number");
	String offsetX = request.getParameter("offsetx");
	String offsetY = request.getParameter("offsety");
	String applicationGroupID = request.getParameter("group");

    username = Utils.parseParam(username);
    password = Utils.parseParam(password);
    
	String dsName = "keyword";
	
	UserVO user = um.login(dsName,username,password);
	
	String returnInfo = "Invalid User"; //invalid user
	if(user != null){
		try{
			PhoneInfoVO phoneInfoVO = new PhoneInfoVO();
			phoneInfoVO.setApplicationGroupID(Integer.parseInt(applicationGroupID));
			phoneInfoVO.setPhoneID(imei);
			phoneInfoVO.setPhoneNumber(number);
			phoneInfoVO.setPhoneType(type);
			phoneInfoVO.setOffsetX(offsetX);
			phoneInfoVO.setOffsetY(offsetY);
			phoneInfoVO.setFolder(folder);
			phoneInfoVO.setComputerID(computerID);
			returnInfo = "" + rdm.addPhoneInfo(dsName, phoneInfoVO);
		}catch(Exception ex){
			returnInfo = "error" + ex.getMessage();
		}
	}
	out.println(returnInfo);
%>
