<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext['request'].contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>验证</title>
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
<body class="hold-transition register-page" style="overflow-y: hidden;">
	<c:choose>
		<c:when test="${status == 'success'}">
			<div id="registerBoxSuccess" class="register-box">
				<div class="register-box-body">
					<div class="row" style="padding-left: 20px; padding-right: 20px; margin-bottom: 20px;">
						<h1 style="font-family: 'Microsoft YaHei'; font-size: 24px; font-weight: 400; float: left;">恭喜</h1>
					</div>
					<p>验证成功，5秒后将返回登录页面。（如果浏览器没有响应，请<a href="${ctx}/login" class="fg_btn">点击这里</a>）</p>
				</div>
			</div>
		</c:when>
		<c:when test="${status == 'failed'}">
			<div id="registerBoxSuccess" class="register-box">
				<div class="register-box-body">
					<div class="row" style="padding-left: 20px; padding-right: 20px; margin-bottom: 20px;">
						<h1 style="font-family: 'Microsoft YaHei'; font-size: 24px; font-weight: 400; float: left;">验证失败</h1>
					</div>
					<p>验证失败， 请联系系统管理员</p>
					<a href="${ctx}/login" class="fg_btn">登录</a>
				</div>
			</div>
		</c:when>
	</c:choose>
	<!-- jQuery 2.2.3 -->
	<script src="${ctx}/resources/jquery/dist/jquery.min.js"></script>
	<!-- Bootstrap 3.3.6 -->
	<script src="${ctx}/resources/bootstrap/js/bootstrap.min.js"></script>

	<script src="${ctx}/resources/jquery-validation/jquery.validate.min.js"></script>

	<script src="${ctx}/resources/jquery-validation/messages_zh.min.js"></script>
	
	<script src="${ctx}/resources/jquery-restful/jquery.restful.js"></script>

	<c:choose>
		<c:when test="${status == 'success'}">
			<script type="text/javascript">
				window.ctx = "${ctx}";
				$(function() {
					setTimeout(function(){
						location.href=ctx+"/login"; 
					}, 5000);
				});
				
			</script>
		</c:when>
	</c:choose>
</body>
</html>
