<%@ page contentType="text/html;charset=UTF-8"%>
<style type="text/css">
	.modal-backdrop.in {
		opacity: 0.1;
	}
	.modal-dialog {
		width: 500px;
		top: 150px;
	}
</style>
<section class="content-header">
	<div class="col-md-6 col-sm-6">
		<span style="font-size: x-large">报表管理</span>
	</div>
	<div class="col-md-6 col-sm-6">
		<ol class="breadcrumb" style="margin: 5px 0 5px 0; float: right">
			<li><a ui-sref="home"><i class="fa fa-home"></i> 主页</a></li>
			<li class="active"><i class="fa fa-key"></i> 报表管理</li>
		</ol>
	</div>
</section>

<section class="content col-md-12 col-sm-12" ng-app="ReportManagement" ng-controller="ReportController" style="margin-top: 2px; padding-top: 0;">
    <div class="carrier-section-position col-md-12 col-sm-12" style="padding: 0">
        <div class="panel panel-default">
            <div class="panel-heading">
                <label class="control-label">报表列表</label>
                <div class="text-right" style="float: right; margin-top: -6px;">
                    <button  ng-hide="vm.userInfo.type == 'Normal'" class="btn btn-primary" ng-click="uploadReport()">上传报表</button>
                </div>
            </div>
            <div class="panel-body">
                <div ui-grid="vm.reportGrid" style="height: 500px" ui-grid-pagination ui-grid-cellnav ui-grid-resize-columns ui-grid-pinning
					 ui-grid-selection
					 ui-grid-move-columns ui-grid-exporter></div>
            </div>
        </div>
    </div>
    <toaster-container toaster-options="{'time-out': 3000, 'animation-class': 'toast-center'}"></toaster-container>
</section>