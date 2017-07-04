<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<jsp:useBean id="scsgm" scope="page" class="com.keymanager.manager.SwitchClientStatusGroupManager" />
<%
	scsgm.switchGroup();
%>


