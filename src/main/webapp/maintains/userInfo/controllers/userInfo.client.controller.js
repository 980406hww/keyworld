(function () {
	'use strict';
  	angular.module('UserInfoManagement').controller('UserInfoController', UserInfoController);

  	UserInfoController.$inject = ['$scope', '$http', 'toaster', '$cookies', '$timeout', 'UserInfoService'];

	function UserInfoController($scope, $http, toaster, $cookies, $timeout, UserInfoService){
		$scope.vm ={};
        var vm = $scope.vm;
        
	    const TIP_UPDATE_SUCCESS = "更新成功";
	    const TIP_UPDATE_FAILED = "更新失败，请重试";
	    
	    vm.isEditPhone = false;
	    vm.isPhoneCodeBtnDisable=false;
	    vm.phoneCodeText='获取手机验证码';
	    
	    vm.isEditEmail = false;
	    vm.isEmailCodeBtnDisable=false;
	    vm.emailCodeText='获取邮箱验证码';
	    
	    UserInfoService.getCurrentUserInfo(function(err, data){
        	if(err){
        		vm.userInfo = {};
        	}else{
        		vm.userInfo = data;
        	}
        });
	    setPhoneCodeTime();
	    setEmailCodeTime();
	    
	    $scope.updatePhoneNumber = function(){
	    	vm.isEditPhone = true;
	    }
	    
	    $scope.updatePhoneNumberSubmit = function(){
	    	UserInfoService.validatePhone(vm.userInfo.phoneValidate, vm.userInfo.phoneValidateCode, function(err, data){
	    		if (err) {
                    toaster.pop('error', err);
                } else {
                	vm.isEditPhone = false;
                	vm.userInfo.phone = vm.userInfo.phoneValidate
                    toaster.pop('success', TIP_UPDATE_SUCCESS);
                }
	    	});
	    }
	    $scope.updatePhoneNumberCancel = function () {
	    	vm.isEditPhone = false;
	    };
	    
	    
	    $scope.updateEmail = function(){
	    	vm.isEditEmail = true;
	    }
	    
	    $scope.updateEmailSubmit = function(){
	    	UserInfoService.validateEmail(vm.userInfo.emailValidate, vm.userInfo.emailValidateCode, function(err, data){
	    		if (err) {
                    toaster.pop('error', err);
                } else {
                	vm.isEditEmail = false;
                	vm.userInfo.email = vm.userInfo.emailValidate
                    toaster.pop('success', TIP_UPDATE_SUCCESS);
                }
	    	});
	    }
	    $scope.updateEmailCancel = function () {
	    	vm.isEditEmail = false;
	    };
	 
	    $scope.updateUser = function(){
	    	UserInfoService.updateUser(vm.userInfo, function(err, data){
	    		if (err) {
                    toaster.pop('error', err);
                } else {
                	delete vm.userInfo.oldPassword;
                	delete vm.userInfo.newPassword;
                	delete vm.userInfo.againPassword;
                    toaster.pop('success', TIP_UPDATE_SUCCESS);
                }
	    	});
	    };
	    
	    $scope.getEmailValidateCode = function(){
	    	UserInfoService.getEmailValidateCode(vm.userInfo.emailValidate, function(err, data){
	    		if (err) {
                    toaster.pop('error', err);
                } else {          
                    toaster.pop('success', "发送验证码成功");
                    $cookies.put('emailValidateTime', 120000);
                    setEmailCodeTime()
                }
	    	});
	    }
	    
	    $scope.getPhoneValidateCode = function(){
	    	UserInfoService.getPhoneValidateCode(vm.userInfo.phoneValidate, function(err, data){
	    		if (err) {
                    toaster.pop('error', err);
                } else {          
                    toaster.pop('success', "发送验证码成功");
                    $cookies.put('phoneValidateTime', 180000);
                    setPhoneCodeTime();
                }
	    	});
	    }
	    
	    function setPhoneCodeTime() {
	    	var phoneValidateTime = $cookies.get('phoneValidateTime');
	    	if (phoneValidateTime > 0) {
	    		vm.isPhoneCodeBtnDisable=true;
			    vm.phoneCodeText='重新发送('+((phoneValidateTime-1000)/1000)+')';
			    $cookies.put('phoneValidateTime', phoneValidateTime-1000);
			    $timeout(setPhoneCodeTime, 1000);
	    	} else { 
	    		vm.isPhoneCodeBtnDisable=false;
			    vm.phoneCodeText='获取手机验证码';
	    	} 
	    }
	    
	    function setEmailCodeTime() {
	    	var emailValidateTime = $cookies.get('emailValidateTime');
	    	if (emailValidateTime > 0) {
	    		vm.isEmailCodeBtnDisable=true;
			    vm.emailCodeText='重新发送('+((emailValidateTime-1000)/1000)+')';
			    $cookies.put('emailValidateTime', emailValidateTime-1000);
			    $timeout(setEmailCodeTime, 1000);
	    	} else { 
	    		vm.isEmailCodeBtnDisable=false;
			    vm.emailCodeText='获取邮箱验证码';
	    	} 
	    }
	}
}());