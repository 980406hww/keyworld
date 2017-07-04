(function () {
	'use strict';
  	angular.module('ReportManagement').controller('ReportTemplateController', ReportTemplateController);

  	ReportTemplateController.$inject = ['instanceEntity', 'refreshForCreateOrUpdate', '$scope', '$http', '$uibModal', '$uibModalInstance', 'toaster', '$cookies', 'ReportService', 'UserInfoService'];

	function ReportTemplateController(instanceEntity, refreshForCreateOrUpdate, $scope, $http, $uibModal, $uibModalInstance, toaster, $cookies, ReportService, UserInfoService){
		$scope.vm ={};
        var vm = $scope.vm;
        
	    const TIP_CREATE_SUCCESS = "创建成功";
	    const TIP_CREATE_FAILED = "创建失败，请重试";

		vm.criteria = {};
		vm.allActiveConsumerUsers = [];

	    $scope.keyword = instanceEntity;

		UserInfoService.getAllActiveConsumerUsers(function(err, data){
			if(err){
				vm.allActiveConsumerUsers = [];
			}else{
				vm.allActiveConsumerUsers = data;
			}
		});

	    $scope.submit = function(isValid){
	        if(isValid){
				var formData=new FormData();
				formData.append("file", file.files[0]);
				formData.append("userId", vm.criteria.userId);
	            ReportService.upload(formData, function (err, data) {
	                if (err) {
	                    toaster.pop('error', TIP_CREATE_FAILED);
	                } else {
	                    $uibModalInstance.close('ok');
	                    toaster.pop('success', TIP_CREATE_SUCCESS);
	                    refreshForCreateOrUpdate();
						$uibModalInstance.close('cancel');
	                }
	            });
	        }
	    };
	
	    $scope.cancel = function () {
	        $uibModalInstance.close('cancel');
	    };
	
	}

}());