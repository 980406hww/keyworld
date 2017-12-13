<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>验证邮箱</title>
	<meta name="viewport" content="width=device-width">
	<link rel="stylesheet" href="${staticPath }/static/toastmessage/css/jquery.toastmessage.css">
	<link rel="stylesheet" type="text/css" href="${staticPath }/static/style/css/login.css?v=201612202107" />
</head>
<body>
<div class="top_div"></div>
<div style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231);border-image:none;width:400px;text-align: center;">
	<div id="forgetPasswordDialog">
		<form method="post" id="forgetPassword">
			<div style="width: 165px; height: 96px; position: absolute;">
				<div class="tou"></div>
				<div class="initial_left_hand" id="left_hand"></div>
				<div class="initial_right_hand" id="right_hand"></div>
			</div>
			<P style="padding: 30px 0px 10px; position: relative;">
				<span>账号</span>
				<input class="ipt" type="text" id="loginName" name="loginName" value="${loginName}" placeholder="请输入登录名" style="margin-left: 25px;"/>
			</P>
			<P style="padding: 0px 0px 10px;position: relative;">
				<span>邮箱</span>
				<input class="ipt" id="email" type="email" name="email" placeholder="请输入邮箱" style="margin-left: 25px;"/>
			</P>
			<div style="height: 50px; line-height: 50px; margin-top: 10px;border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
				<P style="margin: 0px 35px 20px 45px;">
                <span style="float: right;">
                    <a style="background: rgb(0, 142, 173); padding: 7px 10px; border-radius: 4px;
                    border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255);
                    font-weight: bold;" href="javascript:;" onclick="checkLoginNameEmail()">确认</a>
                </span>
				</P>
			</div>
		</form>
	</div>
</div>
<div style="text-align:center;"></div>
<script>
    document.write("<scr"+"ipt src=\"${staticPath }/static/easyui/jquery.min.js\"></sc"+"ript>");
    document.write("<scr"+"ipt src=\"${staticPath }/static/toastmessage/jquery.toastmessage.js\"></sc"+"ript>");
    function checkLoginNameEmail() {
        var forgetPassword = $("#forgetPasswordDialog").find("#forgetPassword");
        var loginNameEmail = {};
        loginNameEmail.loginName = forgetPassword.find("#loginName").val();
        loginNameEmail.email = forgetPassword.find("#email").val();
        if(!(loginNameEmail.loginName!=null && loginNameEmail.loginName != '')){
            alert( "请输入登录名");
            return;
        }
        if(loginNameEmail.email == '') {
            alert("请输入邮箱");
            return;
		}
        if(!(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(loginNameEmail.email)) && loginNameEmail.email != ''){
            alert("请输入正确的邮箱");
            return;
        }
        $.ajax({
            url: '/internal/user/forgetPassword',
            data: JSON.stringify(loginNameEmail),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "请注意查收邮件",true);
                } else {
                    $().toastmessage('showErrorToast', "身份验证失败");
                }
            },
            error: function () {
                $().toastmessage('showSuccessToast', "请注意查收邮件");
            }
        });
        $('#forgetPassword')[0].reset();
    }
</script>
</body>
</html>
