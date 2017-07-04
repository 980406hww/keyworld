<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%
String driverName = "com.mysql.jdbc.Driver";
String userName = "root";
String userPasswd = "mancy**do$2";

String dbName = "db_baiduup";

String tableName = "t_keyword";
String url = "jdbc:mysql://localhost/" + dbName + "?user=" + userName + "&password=" + userPasswd;
Class.forName("com.mysql.jdbc.Driver").newInstance();
Connection conn = DriverManager.getConnection(url);   
Statement stmt = conn.createStatement();
String sql="select * from " + tableName;
ResultSet rs = stmt.executeQuery(sql);
while(rs.next())
{
	out.println(rs.getString("fKeyword"));
	out.println(rs.getString("fUuid"));
	out.println(rs.getString("fTitle"));
}
rs.close();
stmt.close();
conn.close();
%>