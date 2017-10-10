<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<%@ include file="/commons/global.jsp" %>
<head>
	<title>重置密码</title>
	<meta name="viewport" content="width=device-width">
	<%@ include file="/commons/basejs.jsp" %>
	<script language="javascript" type="text/javascript" src="/toastmessage/jquery.toastmessage.js"></script>
	<link rel="stylesheet" href="/toastmessage/css/jquery.toastmessage.css">
	<link rel="stylesheet" type="text/css" href="${staticPath }/static/style/css/login.css?v=201612202107" />

	<script>
        // 验证码
        function captchaForgetPassword() {
            var captchaForgetPassword = $("#forgetPasswordCaptcha");
            var url = captchaForgetPassword.data("src") + new Date().getTime();
            captchaForgetPassword.attr("src", url);
        }

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
            forgetPassword();
            $("#forgetPassword").submit();
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
                    captchaForgetPassword();
                }
            });
        }
        function forgetPassword() {
            $('#forgetPassword').form({
                url: basePath + '/forgetPassword'
            })
        }
	</script>
</head>
<body <%--onkeydown="enterlogin();"--%>>
<div class="top_div"></div>
<div style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231);border-image:none;width:400px;text-align: center;">
	<form method="post" id="forgetPassword">
		<P style="padding: 30px 0px 10px; position: relative;">
			<span class="u_logo"></span>&nbsp;
			<input class="ipt" type="password" name="password" id="password" placeholder="请输入新密码"/>
			<input type="hidden" name="loginName" id="loginName" value="${loginName}">
		</P>
		<P style="position: relative;">
			<span class="p_logo"></span>&nbsp;&nbsp;
			<input class="ipt" id="confirmPassword"  type="password" name="confirmPassword" placeholder="请再次输入密码"/>
		</P>
		<P style="padding: 10px 0px 10px; position: relative;">
			<input class="captcha" type="text" name="captcha" placeholder="请输入验证码"/>
			<img id="forgetPasswordCaptcha" onclick="captchaForgetPassword()" alt="验证码" src="${path }/captcha" data-src="${path }/captcha?t=" style="vertical-align:middle;border-radius:4px;width:94.5px;height:35px;cursor:pointer;">
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
