(function () {
	'use strict';
  	angular.module('KeywordManagement').controller('KeywordTemplateController', KeywordTemplateController);

  	KeywordTemplateController.$inject = ['instanceEntity', 'refreshForCreateOrUpdate', '$scope', '$http', '$uibModal', '$uibModalInstance', 'toaster', '$cookies', 'KeywordService'];

	function KeywordTemplateController(instanceEntity, refreshForCreateOrUpdate, $scope, $http, $uibModal, $uibModalInstance, toaster, $cookies, KeywordService){
		$scope.vm ={};
        var vm = $scope.vm;
        
	    const TIP_CREATE_SUCCESS = "创建成功";
	    const TIP_CREATE_FAILED = "创建失败，请重试";
		const TIP_UPDATE_SUCCESS = "更新成功";
		const TIP_UPDATE_FAILED = "更新失败，请重试";
	    $scope.keyword = instanceEntity;

	    $scope.submit = function(isValid){
	        if(isValid){
	            KeywordService.saveKeyword($scope.keyword, function (err, data) {
	                if (err) {
	                    toaster.pop('error', $scope.keyword.id == null ? TIP_CREATE_FAILED : TIP_UPDATE_FAILED);
	                } else {
	                    $uibModalInstance.close('ok');
	                    toaster.pop('success',  $scope.keyword.id == null ? TIP_CREATE_SUCCESS : TIP_UPDATE_SUCCESS);
	                    refreshForCreateOrUpdate(null);
	                }
	            });
	        }
	    };
	
	    $scope.cancel = function () {
	        $uibModalInstance.close('cancel');
	    };
	
	}

}());