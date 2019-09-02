<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext['request'].contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>登录</title>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet" href="${ctx}/resources/bootstrap/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${ctx}/resources/font-awesome/css/font-awesome.min.css">
<!-- Ionicons -->
<!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css"> -->
<!-- Theme style -->
<link rel="stylesheet" href="${ctx}/resources/AdminLTE/css/AdminLTE.min.css">
<!-- iCheck -->
<link rel="stylesheet" href="${ctx}/resources/iCheck/square/blue.css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>
<body class="hold-transition login-page" style="overflow-y: hidden;">
<script>
	if(window !=top){
		top.location.href=location.href;
	};
</script>
	<div class="login-box">
		<div class="login-box-body">
			<div class="row" style="padding-left: 20px; padding-right: 20px; margin-bottom: 20px;">
				<h1 style="font-family: 'Microsoft YaHei'; font-size: 24px; font-weight: 400; float: left;">欢迎登录</h1>
				<div style="color: #787d82; font-size: 14px; margin-top: 23px; float: right; font-family: 'Microsoft YaHei';">
					没有账号？<a href="${ctx}/register.jsp"  style="color: #f01400;">立即注册</a>
				</div>
			</div>
			<form action="${ctx}/login" method="post">
				<div class="form-group has-feedback">
					<span style="color: #f01400;">${loginInfo}</span>
				</div>
				<div class="form-group has-feedback">
					<div class="input-group">
	                  <div class="input-group-addon">
	                    <i class="glyphicon glyphicon-user"></i>
	                  </div>
	                  <input type="text" class="form-control" name="username" placeholder="用户名">
	                </div>
             	</div>
             	<div class="form-group has-feedback">
					<div class="input-group">
	                  <div class="input-group-addon">
	                    <i class="glyphicon glyphicon-lock"></i>
	                  </div>
	                  <input type="password" class="form-control" name="password" placeholder="密码">
	                </div>
             	</div>
				<div class="row" style="margin-bottom: 10px;">
					<div class="col-xs-6">
						<input type="text" class="form-control" name="captcha" placeholder="验证码">
					</div>
					<div class="col-xs-6">
						<img id="pcrimg" src="${ctx}/login/pcrimg" style="height: 35px;" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
					</div>
				</div>
			</form>
		</div>
		<!-- /.login-box-body -->
	</div>
	<!-- /.login-box -->

	<!-- jQuery 2.2.3 -->
	<script src="${ctx}/resources/jquery/dist/jquery.min.js"></script>
	<!-- Bootstrap 3.3.6 -->
	<script src="${ctx}/resources/bootstrap/js/bootstrap.min.js"></script>

	<script type="text/javascript">
		window.ctx="${ctx}";
		$(function(){
			$('#pcrimg').bind("click",function(){         
				$('#pcrimg').attr("src", ctx+"/login/pcrimg"+'?t='+(new Date()).valueOf()); 
			});
		});
	</script>
</body>
</html>
