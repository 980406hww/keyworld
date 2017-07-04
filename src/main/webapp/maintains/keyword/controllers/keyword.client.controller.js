(function () {
    'use strict';
    angular.module('KeywordManagement').controller('KeywordController', KeywordController);
    KeywordController.$inject = ['$scope', 'i18nService', '$uibModal', 'toaster',  'KeywordService', 'UserInfoService'];
    function KeywordController($scope, i18nService, $uibModal, toaster, KeywordService, UserInfoService) {
    	$scope.vm ={};
        var vm = $scope.vm;
        const TIP_ONLY_ONE_ROW_SELECT = "请选中一行记录进行操作";
        const TIP_DELETE_SUCCESS = "删除成功";
        const TIP_DELETE_FAILED = "删除失败";

        const TIP_CHANGE_STATUS_SUCCESS = "更改成功";
        const TIP_CHANGE_STATUS_FAILED = "更改失败";
        
        vm.collapsed = false;
        vm.keywords = [];
        vm.criteria = {};
        i18nService.setCurrentLang("zh-cn");

        var updateTimeTemplate = '<div class="ui-grid-cell-contents" style="font-size: 13px;">' +
            '{{row.entity.update_time|date:"yyyy-MM-dd HH:mm" }}'+
            '</div>';

        var keywordColumnDefs = [
            {field: 'username', allowCellFocus: false, displayName: '用户名', width: '10%',enableColumnMenu: false, enableHiding: false},
            {field: 'keyword', allowCellFocus: false, displayName: '关键词', width: '65%',enableColumnMenu: false, enableHiding: false},
            {field: 'status', allowCellFocus: false, displayName: '状态', width: '10%',enableColumnMenu: false, enableHiding: false},
            {name: 'updateTime', allowCellFocus: false, displayName: '更新时间',
                cellTemplate: updateTimeTemplate, width: '15%',enableColumnMenu: false, enableHiding: false}];

        vm.keywordGrid = {
            enableSorting: false,
            enableColumnResizing: true,
            enableGridMenu: false,
            paginationPageSizes: [10, 25, 50, 75],
            paginationPageSize: 25,
            enableFullRowSelection: true,
            exporterOlderExcelCompatibility: true,
            exporterMenuPdf: false,
            data: 'vm.keywords',
            multiSelect: false,
            modifierKeysToMultiSelect: true,
            enableColumnMenu: false,
            enableVerticalScrollbar: 0,
            enableHorizontalScrollbar: 0,
            columnDefs: keywordColumnDefs
        };

        vm.keywordGrid.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
        };

        UserInfoService.getCurrentUserInfo(function(err, data){
            if(err){
                vm.userInfo = {};
            }else{
                vm.userInfo = data;
            }
        });

        $scope.findKeywords = function () {
        	KeywordService.findKeywords(vm.criteria, function (err, data) {
                if (err) {
                    console.log(err);
                } else {
                    if (data === null) {
                        data = [];
                    }
                    vm.keywords = data;
                }
            });
        };
        
        $scope.addKeyword = function(){
        	$uibModal.open({
                templateUrl: ctx+'/maintains/keyword/views/keywordTemplate.html',
                controller: 'KeywordTemplateController',
                backdrop: 'static',
                resolve:{
                    instanceEntity : function(){
                    	return {};
                    },
                    refreshForCreateOrUpdate : function(){
                    	return $scope.findKeywords;
                    }
                }
            });
        };

        $scope.findKeywords();
        
        $scope.resetSearch = function () {
        	vm.criteria = {};
        };

        $scope.disableModifyOrDelete = function() {
            return ($scope.gridApi.grid.selection.selectedCount != 1 || $scope.gridApi.grid.selection.lastSelectedRow.entity.status == '上线');
        };

        $scope.updateKeyword = function () {
            if ($scope.gridApi.grid.selection.selectedCount != 1) {
                toaster.pop('error', TIP_ONLY_ONE_ROW_SELECT);
            } else {
                $uibModal.open({
                	templateUrl: ctx+'/maintains/keyword/views/keywordTemplate.html',
                    controller: 'KeywordTemplateController',
                    size: 'lg',
                    backdrop: 'static',
                    resolve:{
                        instanceEntity : function(){
                        	var entity = $scope.gridApi.grid.selection.lastSelectedRow.entity;
                        	return entity;
                        },
                        refreshForCreateOrUpdate : function(){
                        	return $scope.findKeywords;
                        }
                    }
                });
            }
        };

        $scope.changeStatus = function () {
            if ($scope.gridApi.grid.selection.selectedCount != 1) {
                toaster.pop('error', TIP_ONLY_ONE_ROW_SELECT);
            } else {
                var row = $scope.gridApi.grid.selection.lastSelectedRow.entity;
                KeywordService.changeStatus(row, function (err, data) {
                    if (err) {
                        toaster.pop('error', TIP_CHANGE_STATUS_FAILED);
                        $scope.findKeywords();
                    } else {
                        toaster.pop('success', TIP_CHANGE_STATUS_SUCCESS);
                        $scope.findKeywords();
                    }
                });
                $scope.gridApi.selection.clearSelectedRows();
            }
        };

        $scope.deleteKeyword = function () {
            if ($scope.gridApi.grid.selection.selectedCount != 1) {
                toaster.pop('error', TIP_ONLY_ONE_ROW_SELECT);
            } else {
                $.confirm({
                    title: '确定删除选中的关键词？',
                    content: false,
                    confirmButton: '确定',
                    cancelButton: '取消',
                    confirmButtonClass: 'btn-info',
                    cancelButtonClass: 'btn-default',
                    theme: 'black',
                    keyboardEnabled: true,
                    confirm: function () {
                        var row = $scope.gridApi.grid.selection.lastSelectedRow.entity;
                        KeywordService.deleteKeyword(row.id, function (err, data) {
                            if (err) {
                                toaster.pop('error', TIP_DELETE_FAILED);
                                $scope.findKeywords();
                            } else {
                                toaster.pop('success', TIP_DELETE_SUCCESS);
                                $scope.findKeywords();
                            }
                        })
                        $scope.gridApi.selection.clearSelectedRows();
                    },
                    cancel: function () {
                    }
                });
            }
        }
    }
}());