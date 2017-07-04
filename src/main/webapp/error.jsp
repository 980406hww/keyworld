<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page contentType="text/html;charset=utf-8" isErrorPage="true" %>
<%@page import="java.io.*,java.util.*" %>


<%
int headFlag = 10;

String msg="";
String info = "";
String exName= "";
if(exception!=null)
{
	msg=exception.getMessage();
	info = exception.toString();
	exName=exception.getClass().getName();
}
if(msg.equals("NO_LOGIN"))
	info = "您没有登录或者所在的页面已经超时，请重新登录后再进行操作！<br/><br/><a href='bd.html'><img src='images/Login.bmp 'align='absmiddle' border='0'></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='Register.jsp'><img src='images/Register.bmp 'align='absmiddle' border='0'></a>";
else if(msg.equals("NO_ADMIN_RIGHT"))
	info = "您没有登录或者没有系统管理员的权限!<br/><br/><a href='houtai.html'>请重新登录后再进行操作！</a>";

%>
<head>
  	<title>提示信息</title>
	<style>
	*{ margin:0; padding:0;}
	a{color:#f60; font-size:14px; text-decoration:underline;}
	a:hover{color:#000;}
	ul,li{list-style:none;}
	.c{height:1%;}
	.c:after {content: ".";display: block;height:0;clear: both;visibility:hidden;}
	#erro_k{ border:1px solid #8ED0F1; background:#F7FCFF; padding:10px 10px 0 10px; width:530px; margin:100px auto; font:normal 14px Arial, Helvetica, sans-serif;}
	h1{ font: normal bold 14px/300% Arial, Helvetica, sans-serif; color:#188EDA;}
	#erro_info{ text-indent:2em; line-height:30px; border-bottom:1px dashed #6397D4; padding:0 0 15px 0;}
	#erro_info a{ font-weight:bold; padding:0 5px;}
	#contornext ul{ padding:0 20px; }
	#contornext li{ line-height:30px;color:#f60;}
	#contornext li span{color:#999; font:normal 12px "宋体"; padding:0 10px;}
	#erro_nav{border-top:1px dashed #6397D4; background:url(images/erro_logo.gif) no-repeat 0 50%; margin:50px 0 0 0;}
	#erro_nav ul{ padding:0 0 0 80px;}
	#erro_nav li{ float:left; line-height:50px; display:inline; margin:0 0 0 6px}
</style>
</head>
<body>
<div id="erro_k" class="c">
	<h1>网页提示信息:</h1>
	<div id="erro_info"><%=info%></div>
</div>
</body>
</html>