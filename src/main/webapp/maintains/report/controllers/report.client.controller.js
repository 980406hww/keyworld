(function () {
    'use strict';
    angular.module('ReportManagement').controller('ReportController', ReportController);
    ReportController.$inject = ['$scope', 'i18nService', '$uibModal', 'toaster',  'ReportService', 'UserInfoService'];
    function ReportController($scope, i18nService, $uibModal, toaster, ReportService, UserInfoService) {
    	$scope.vm ={};
        var vm = $scope.vm;
        const TIP_ONLY_ONE_ROW_SELECT = "请选中一行记录进行操作";
        const TIP_DELETE_SUCCESS = "删除成功";
        const TIP_DELETE_FAILED = "删除失败";
        
        vm.collapsed = false;
        vm.reports = [];
        vm.criteria = {};
        i18nService.setCurrentLang("zh-cn");

        UserInfoService.getCurrentUserInfo(function(err, data){
            if(err){
                vm.userInfo = {};
            }else{
                vm.userInfo = data;
            }
        });

        var updateTimeTemplate = '<div class="ui-grid-cell-contents" style="font-size: 13px;">' +
            '{{row.entity.create_time|date:"yyyy-MM-dd" }}' +
            '</div>';
        var downloadTemplate = '<div class="ui-grid-cell-contents" style="font-size: 13px;">' +
            '<a ng-click="grid.appScope.downloadReport(row.entity)" style="cursor: pointer;color: blue;" target="_blank">下载</a>' +
            '</div>';

        var reportColumnDefs = [
            {field: 'username', allowCellFocus: false, displayName: '用户名', width: '20%',enableColumnMenu: false, enableHiding: false},
            {name: 'updateTime', allowCellFocus: false, displayName: '报表日期', cellTemplate: updateTimeTemplate,width: '30%',enableColumnMenu: false, enableHiding: false},
            {name: 'download', allowCellFocus: false, displayName: '下载', cellTemplate: downloadTemplate, width: '50%',enableColumnMenu: false, enableHiding: false}
        ];

        vm.reportGrid = {
            enableSorting: false,
            enableColumnResizing: true,
            enableGridMenu: false,
            paginationPageSizes: [10, 25, 50, 75],
            paginationPageSize: 25,
            enableFullRowSelection: true,
            exporterOlderExcelCompatibility: true,
            exporterMenuPdf: false,
            data: 'vm.reports',
            multiSelect: false,
            modifierKeysToMultiSelect: true,
            enableColumnMenu: false,
            enableVerticalScrollbar: 0,
            enableHorizontalScrollbar: 0,
            columnDefs: reportColumnDefs
        };

        vm.reportGrid.onRegisterApi = function (gridApi) {
        	$scope.gridApi = gridApi;
        };

        $scope.downloadReport = function (entity) {
            var reportId = entity.id;
            var fileName = entity.fileName;
            ReportService.downloadReport({"reportId" : reportId}, function (err, data) {
                if (err) {
                    console.log(err);
                } else {
                    if (data === null) {
                        data = [];
                    }
                    var blob = new Blob([data], {type: "application/octet-stream"});
                    var a = document.createElement("a");
                    document.body.appendChild(a);
                    a.download = fileName;
                    a.href = URL.createObjectURL(blob);
                    a.click();
                }
            });
        };

        $scope.findReports = function () {
        	ReportService.findReports(vm.criteria, function (err, data) {
                if (err) {
                    console.log(err);
                } else {
                    if (data === null) {
                        data = [];
                    }
                    vm.reports = data;
                }
            });
        };
        
        $scope.uploadReport = function(){
        	$uibModal.open({
                templateUrl: ctx+'/maintains/report/views/reportTemplate.html',
                controller: 'ReportTemplateController',
                backdrop: 'static',
                resolve:{
                    instanceEntity : function(){
                    	return {};
                    },
                    refreshForCreateOrUpdate : function(){
                    	return $scope.findReports;
                    }
                }
            });
        }

        $scope.findReports();
        
        $scope.resetSearch = function () {
        	vm.criteria = {};
        };
    }
}());