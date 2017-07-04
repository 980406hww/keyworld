
<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,javax.sql.*,javax.naming.*,com.keymanager.db.*" errorPage="" %>
<html>
<head></head>
<body>

<%
String uuid = request.getParameter("uuid");

Connection conn = DBUtil.getConnection("resource");  

String sql= "SELECT bc.fTitle, bc.fContent, bc.fKeyword FROM t_blog_content bc WHERE bc.fUuid = " + uuid;

PreparedStatement stmt = conn.prepareStatement(sql, 1003, 1007);

ResultSet rs = stmt.executeQuery(sql);
out.println("<table>");
while(rs.next())
{
	out.println("<tr>");
	out.println("<td id='title0002'>" + rs.getString("fTitle").replaceAll(rs.getString("fKeyword"), "张席龙") + "</td><td id='title0002completed'>sdfsdf</td>");
	out.println("<td id='content0002'>" + rs.getString("fContent").replaceAll(rs.getString("fKeyword"), "张席龙") + "</td><td id='content00022completed'>sdfsdf</td>");
	out.println("</tr>");
}
out.println("</table>");
rs.close();
stmt.close();
conn.close();
%>
</body></html>
