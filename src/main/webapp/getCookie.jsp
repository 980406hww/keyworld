<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*,javax.sql.*,javax.naming.*"
	%>
	
	test
<%
/*
	request.getCookies();
	out.println("this is www.csdn.net/<br>");
	out.println("<br>");
	for (int i = 0; i < request.getCookies().length; i++) {
		String name = request.getCookies()[i].getUserName();
		String value = request.getCookies()[i].getvalue();
		int maxage = request.getCookies()[i].getMaxAge();
		out.println("name=" + request.getCookies()[i].getUserName() + "<br>");
		//out.println("value=" + request.getCookies()[i].getvalue() + "<br>");
		out.println("maxage=" + request.getCookies()[i].getMaxAge() + "<br>");
		//out.println("setMaxAge为30"); 
		//cookie .setMaxAge(30); 
		//out.println("之后为maxage="+cookie .getMaxAge()+"<br>"); 
		out.println("domain=" + request.getCookies()[i].getDomain() + "<br>");
		out.println("secure=" + request.getCookies()[i].getSecure() + "<br>");
		out.println("path=" + request.getCookies()[i].getPath() + "<br>");
	}
	*/
%>
<script type="text/javascript">
window.open('http://www.baidu.com');
alert('Alert不行哟');
window.showModalDialog('http://www.baidu.com');
<!--

//-->
</script>

