<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext['request'].contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>注册</title>
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

<style type="text/css">
	ul {
		margin: 0;
		padding: 0;
	}
	
	li {
		margin: 0;
		padding: 0;
	}
	
	#myTab li {
		width: 50%;
		float: left;
		height: 40px;
		list-style: none;
		margin: 0;
		padding: 0;
	}
	
	#myTab li img {
		float: left;
		height: 40px;
	}
	
	#myTab li a {
		color: white;
		text-align: center;
		position: relative;
		display: block;
		padding: 10px 15px;
	}
	
	.blue {
		background: #0f9af2;
	}
	
	.gray {
		background: #dfdfdf;
	}
	
	.tabPaneUl {
		width: 700px;
		margin: 0 auto;
		list-style: none;
	}
	
	.tabPaneUl li {
		height: 40px;
		line-height: 40px;
	}
	
	.tab-pane {
		margin-top: 50px;
	}
</style>

</head>
<body class="hold-transition register-page" style="overflow-y: hidden;">
	<div id="registerBox" class="register-box" style="width: 720px;">
		<div class="register-box-body">
			<div class="row" style="padding-left: 20px; padding-right: 20px;;">
				<h1 style="font-family: 'Microsoft YaHei'; font-size: 24px; font-weight: 400; float: left;">欢迎注册</h1>
			</div>
			<div class="row">
				<div id="myTabContent" class="tab-content">
					<form id="registerFormStepTwo"  class="col-xs-10 col-xs-offset-1">
						<div class="form-group">
							<span style="color: #f01400;" id="registerInfoStepTwo" >${registerInfo}</span>
						</div>
						<div class="form-group">
							<label class="control-label" for="username" hidden style="display: inline-block;"></label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="glyphicon glyphicon-user"></i>
								</div>
								<input type="text" class="form-control" id="username" name="username" placeholder="用户名" value="${userVO.username}" required>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label" for="password" hidden style="display: inline-block;"></label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="glyphicon glyphicon-lock"></i>
								</div>
								<input type="password" class="form-control" id="password" name="password" placeholder="密码" value="${userVO.password}" required>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label" for="againPassword" hidden style="display: inline-block;"></label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="glyphicon glyphicon-lock"></i>
								</div>
								<input type="password" class="form-control" id="againPassword" name="againPassword" placeholder="确认密码" value="${userVO.againPassword}" equalTo="#password" required>
							</div>
						</div>
						<div class="row" style="margin-bottom: 10px;">
							<label class="control-label" for="captcha" hidden style="display: inline-block;"></label>
							<div class="col-xs-6">
								<input type="text" class="form-control" id="captcha" name="captcha" placeholder="验证码" value="${userVO.captcha}">
							</div>
							<div class="col-xs-6">
								<img id="pcrimg" src="${ctx}/login/pcrimg" style="height: 35px;" />
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12">
								<button type="button" id="registerBtn" class="btn btn-primary btn-block btn-flat">注册</button>
							</div>
						</div>
						<div class="form-group" style="margin-top: 5px;">
							<span>已有帐号？<a href="${ctx}/login" class="fg_btn">立即登录</a></span>
						</div>
					</form>
				</div>
			</div>
		</div>
		<!-- /.login-box-body -->
	</div>
	<!-- /.login-box -->

	<div id="registerBoxSuccess" class="register-box">
		<div class="register-box-body">
			<p>注册成功，请返回登录页面登录。</p>
			<a href="${ctx}/login" class="fg_btn">立即登录</a>
		</div>
	</div>


	<!-- jQuery 2.2.3 -->
	<script src="${ctx}/resources/jquery/dist/jquery.min.js"></script>
	<!-- Bootstrap 3.3.6 -->
	<script src="${ctx}/resources/bootstrap/js/bootstrap.min.js"></script>

	<script src="${ctx}/resources/jquery-validation/jquery.validate.min.js"></script>

	<script src="${ctx}/resources/jquery-validation/messages_zh.min.js"></script>
	
	<script src="${ctx}/resources/jquery-restful/jquery.restful.js"></script>
	
	<script src="${ctx}/resources/jquery-cookie/jquery.cookie.js"></script>

	<script type="text/javascript">
		window.ctx = "${ctx}";
		
		
		// 手机号码验证    
	    jQuery.validator.addMethod("isMobile", function(value, element) {    
	      	var length = value.length;    
	      	return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/.test(value));    
	    }, "请正确填写您的手机号码。");
		
		//事件操作
		var eventFun={
			setStep:function(index){				
				for(var i=2;i<=index;i++){
					$("#step"+i+"Li").addClass("blue").removeClass("gray");
					$("#step"+i+"Img").attr("src","images/blue_blue.png");
				}
				for(var i=index+1;i<=4;i++){
					$("#step"+i+"Li").addClass("gray").removeClass("blue");
					$("#step"+i+"Img").attr("src","images/gray_gray.png");
				}
				$("#step"+(index+1)+"Img").attr("src","images/blue_gray.png");
			}
		};
		
		$(function() {
			$('#registerBoxSuccess').hide();
			
			function setPhoneCodeBtnTime() {
				var val = $('#phoneCodeBtn');
				var countdown = $.cookie('countdown')=='NaN'? 0: $.cookie('countdown');
				if (countdown == 0) { 
					val.removeAttr("disabled"); 
					val.text("获取验证码"); 
					$.cookie('countdown', 180);
				} else { 
					val.attr("disabled", true); 
					val.text("重新发送(" + countdown + ")");
					$.cookie('countdown', --countdown);
					setTimeout(setPhoneCodeBtnTime, 1000); 
				} 
			} 
			
			$('#pcrimg').bind("click", function() {
				$('#pcrimg').attr("src", ctx + "/login/pcrimg"+'?t='+(new Date()).valueOf());
			});
			
			$('#myTab li a').click(function (e) { 
				e.stopImmediatePropagation();
	        }); 
			
			$('#phoneCodeBtn').bind("click", function() {
				var phone = $('#phone').val();
				if(!$.isEmptyObject(phone)){
					$.get(ctx + "/register/getPhoneCode?phone="+phone, function(result){
					    console.log(result);
					    setPhoneCodeBtnTime();
				  	});
				}
			});
			
			$('#registerFormStepOneBtn').bind("click", function() {
				if(!$("#registerFormStepOne").valid()){
					return;
				}
				var phone = $('#phone').val();
				var phoneCode = $('#phoneCode').val();
				if(!$.isEmptyObject(phone) && !$.isEmptyObject(phoneCode)){
					$.restful.post(ctx + "/register/validatePhone", {'phone': phone, 'phoneCode': phoneCode}).done(function(data){ 
						$('#tabStep2').tab('show');
						eventFun.setStep(2);
					}).fail(function(data){ 
						$("#registerInfo").text(data.errorMsg);
					});
				}
			});
			
			$("#registerFormStepOne").validate({
				rules : {
					phone : {
						remote : ctx + "/register/isPhoneCanUse",
						isMobile: true
					},
					phoneCode :{
						required: true
					}
				},
				messages : {
					phone : {
						remote : "该手机号码已经被注册",
					}
				},
				errorPlacement : function(error, element) {
					element.parent().parent().addClass('has-error');
					element.parent().parent().children(":first").show();
					element.parent().parent().children(":first").text(error.text());
				},
				success : function(label, element) {
					$(element).parent().parent().removeClass('has-error');
				}
			});
			
			$("#registerFormStepTwo").validate({
				rules : {
					email : {
						remote : ctx + "/register/isEmailCanUse"
					},
					username: {
						required: true
					},
					password: {
						required: true
					},
					againPassword: {
						required: true
					}
				},
				messages : {
					email : {
						remote : "该邮箱已被使用"
					}
				},
				errorPlacement : function(error, element) {
					element.parent().parent().addClass('has-error');
					element.parent().parent().children(":first").show();
					element.parent().parent().children(":first").text(error.text());
				},
				success : function(label, element) {
					$(element).parent().parent().removeClass('has-error');
				}
			});
			
			$('#registerBtn').bind("click", function() {
				if(!$("#registerFormStepTwo").valid()){
					return;
				}
				var userVO = {
					'phone': $('#phone').val(),
					'phoneCode': $('#phoneCode').val(),
					'email': $('#email').val(),
					'username': $('#username').val(),
					'password': $('#password').val(),
					'againPassword': $('#againPassword').val(),
					'captcha': $('#captcha').val()
				};
				$.restful.post(ctx + "/register", userVO).done(function(data){ 
					$('#registerBox').hide();
					$('#registerBoxSuccess').show();
				}).fail(function(data){ 
					$("#registerInfoStepTwo").text(data.errorMsg);
				});
			});
			
		});
	</script>
</body>
</html>
