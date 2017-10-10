<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<%@ include file="/commons/global.jsp" %>
<head>
	<title>重置密码</title>
	<meta name="viewport" content="width=device-width">
	<%@ include file="/commons/basejs.jsp" %>
	<script language="javascript" type="text/javascript" src="/toastmessage/jquery.toastmessage.js"></script>
	<script language="javascript" type="text/javascript" src="/static/login.js"></script>
	<link rel="stylesheet" href="/toastmessage/css/jquery.toastmessage.css">
	<link rel="stylesheet" type="text/css" href="${staticPath }/static/style/css/login.css?v=201612202107" />
	<script>
		function checkPassword() {
			var password = $("#password").val();
			var confirmPassword = $("#confirmPassword").val()
			if(!(password != null && password != '')){
				alert("密码不能为空");
				return;
			}else if(confirmPassword != password) {
                alert("两次密码不一致");
                return;
			}
			var newUserInfo = {};
            newUserInfo.password = password;
			newUserInfo.loginName = $("#loginName").val();
            $.ajax({
                url: '/external/user/resetPassword',
                data: JSON.stringify(newUserInfo),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result) {
                        $().toastmessage('showSuccessToast', "修改密码成功,即将跳转登陆页面...");
                        setTimeout(function(){window.location.href="/login"},4000);
                    } else {
                        $().toastmessage('showErrorToast', "修改密码失败!");
                        captchaForgetPassword();
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "修改密码失败!");
                }
            });
        }
	</script>
</head>
<body <%--onkeydown="enterlogin();"--%>>
<div class="top_div"></div>
<div style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231);border-image:none;width:400px;text-align: center;">
	<form method="post" id="forgetPassword">
		<div style="width: 165px; height: 96px; position: absolute;">
			<div class="tou"></div>
			<div class="initial_left_hand" id="left_hand"></div>
			<div class="initial_right_hand" id="right_hand"></div>
		</div>
		<P style="padding: 30px 0px 10px; position: relative;">
			<span class="p_logo" style="margin-top: 30px;"></span>&nbsp;
			<input class="ipt" type="password" name="password" id="password" placeholder="请输入新密码"/>
			<input type="hidden" name="loginName" id="loginName" value="${loginName}">
		</P>
		<P style="position: relative;">
			<span class="p_logo"></span>&nbsp;&nbsp;
			<input class="ipt" id="confirmPassword"  type="password" name="confirmPassword" placeholder="请再次输入密码"/>
		</P>
		<div style="height: 50px; line-height: 50px; margin-top: 10px;border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
			<P style="margin: 0px 35px 20px 45px;">
				<span style="float: right;">
                    <a style="background: rgb(0, 142, 173); padding: 7px 10px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;" href="javascript:;" onclick="checkPassword();">确定</a>
                </span>
			</P>
		</div>
	</form>
</div>
<div style="text-align:center;">
</div>
</body>
</html>
