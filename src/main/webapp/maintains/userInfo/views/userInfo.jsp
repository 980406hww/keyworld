<%@ page contentType="text/html;charset=UTF-8"%>
<style type="text/css">
    .form-group-line {
  	    width: 100%;
	    height: 2px;
	    margin: 10px 0;
	    overflow: hidden;
	    font-size: 0;
	    border-bottom: 1px solid #dee5e7;
	    margin-top: 15px;
	    margin-bottom: 15px;
	}
</style>

<section class="content-header">
	<div class="col-md-6 col-sm-6">
		<span style="font-size: x-large">我的信息</span>
	</div>
	<div class="col-md-6 col-sm-6">
		<ol class="breadcrumb" style="margin: 5px 0 5px 0; float: right">
			<li><a ui-sref="home"><i class="fa fa-home"></i> 主页</a></li>
			<li class="active"><i class="fa fa-key"></i> 我的信息</li>
		</ol>
	</div>
</section>

<section class="content col-md-12 col-sm-12" ng-app="UserInfoManagement" ng-controller="UserInfoController" style="margin-top: 2px; padding-top: 0;">
	<div class="panel panel-default">
		<div class="panel-heading font-bold">信息详情</div>
		<div class="panel-body">
			<form class="form-horizontal ng-pristine ng-valid ng-valid-date ng-valid-required ng-valid-parse ng-valid-date-disabled">
				<div class="form-group">
					<label class="col-sm-2 control-label">用户名</label>
					<div class="col-sm-6">
						{{vm.userInfo.username}}
					</div>
				</div>
				<div class="form-group-line"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">旧密码</label>
					<div class="col-sm-6">
						<input type="password" class="form-control rounded" ng-model="vm.userInfo.oldPassword">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">新密码</label>
					<div class="col-sm-6">
						<input type="password" class="form-control rounded" ng-model="vm.userInfo.newPassword">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">确认密码</label>
					<div class="col-sm-6">
						<input type="password" class="form-control rounded" ng-model="vm.userInfo.againPassword">
					</div>
				</div>
				<div class="form-group-line"></div>

				<div class="form-group-line"></div>
				
				<div class="form-group">
					<div class="col-sm-4 col-sm-offset-2">
						<button type="submit" class="btn btn-primary" ng-click="updateUser()" >更新</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<toaster-container toaster-options="{'time-out': 3000, 'animation-class': 'toast-center'}"></toaster-container>
</section>