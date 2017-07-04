<%@ page contentType="text/html;charset=UTF-8"%>
<style type="text/css">
	.modal-backdrop.in {
		opacity: 0.1;
	}
	.modal-dialog {
		width: 720px;
		top: 150px;
	}
</style>
<section class="content-header">
	<div class="col-md-6 col-sm-6">
		<span style="font-size: x-large">关键词管理</span>
	</div>
	<div class="col-md-6 col-sm-6">
		<ol class="breadcrumb" style="margin: 5px 0 5px 0; float: right">
			<li><a ui-sref="home"><i class="fa fa-home"></i> 主页</a></li>
			<li class="active"><i class="fa fa-key"></i> 关键词管理</li>
		</ol>
	</div>
</section>

<section class="content col-md-12 col-sm-12" ng-app="KeywordManagement" ng-controller="KeywordController" style="margin-top: 2px; padding-top: 0;">
    <div class="carrier-section-position col-md-12 col-sm-12" style="padding: 0">
        <div class="panel panel-default">
            <div class="panel-heading">
                <label class="control-label">关键词列表</label>
                <div class="text-right" style="float: right; margin-top: -6px;">
                    <button class="btn btn-primary" ng-click="addKeyword()">添加新词</button>
                    <button class="btn btn-primary" ng-disabled="disableModifyOrDelete()" ng-click="updateKeyword()">修改
					</button>
                    <button class="btn btn-primary" ng-disabled="disableModifyOrDelete()" ng-click="deleteKeyword()">删除</button>
					<button class="btn btn-primary" ng-hide="vm.userInfo.type == 'Normal'" ng-disabled="disableModifyOrDelete()"
							ng-click="changeStatus()">上线</button>
                </div>
            </div>
            <div class="panel-body">
                <div ui-grid="vm.keywordGrid" style="height: 500px" ui-grid-pagination ui-grid-cellnav ui-grid-resize-columns ui-grid-pinning
					 ui-grid-selection
					 ui-grid-move-columns ui-grid-exporter></div>
            </div>
        </div>
    </div>
    <toaster-container toaster-options="{'time-out': 3000, 'animation-class': 'toast-center'}"></toaster-container>
</section>