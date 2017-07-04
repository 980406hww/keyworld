<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*,com.keymanager.util.mail.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />

<%
	String datasourceName = "keyword";
	String email = request.getParameter("email");

	if (email == null || email.trim().equals("")) {
%>
        <script language="javascript">
			alert("注册账号不正确，请重新输入");
			window.history.back();
		</script>
<%
	return;
	}

	UserVO user = um.getUserByUserID(datasourceName, email);

	if (user == null) {
%>
         <script language="javascript">
		   alert("请确认您输入的账号是否正确");
		   window.history.back();
	  	</script>
<%
	return;
	}

	String passwordSerial = um.updatePasswordSerial(datasourceName, email);

	String html = "";

	html += "";
	html += "<div>";
	html += "	<p style='color:red;font-size:20px;'><b>要修改密码，请点击下面链接(如果不能打开,请复制到地址栏打开)</b></p>";
	html += " <a target='_blank' href='http://www.baidutop123.com/user/password.jsp?user=" + email + "&ps="
			+ passwordSerial + "'>http://www.baidutop123.com/user/password.jsp?user=" + email + "&ps=" + passwordSerial
			+ "</a>";
	html += "</div><br>";
	html += "<br><br><br>";

	String title = "刷百度排名找回密码,请查收";

	MailSender.sendMail(email, title, html);
%>
<script language="javascript">
    alert("修改密码链接已经发到您邮箱，请查收邮件修改你的用户密码");
    window.location.href = "http://www.baidutop123.com/"
</script>

