angular.module('UserInfoManagement').factory('UserInfoService', UserInfoService);

UserInfoService.$inject = ['$http'];

function UserInfoService($http) {
    return {
        getCurrentUserInfo: getCurrentUserInfo,
        updateUser: updateUser,
        getEmailValidateCode: getEmailValidateCode,
        validateEmail: validateEmail,
        getPhoneValidateCode: getPhoneValidateCode,
        validatePhone: validatePhone,
        getAllActiveConsumerUsers: getAllActiveConsumerUsers
    };

    function getAllActiveConsumerUsers(callBack) {
        $http.get(ctx+'/user/getAllActiveConsumerUsers').then(function (res) {
            if (res) {
                return callBack(null, res.data);
            } else {
                return callBack('rep failed', null);
            }
        });
    }

    function getCurrentUserInfo(callBack) {
        $http.get(ctx+'/user/getCurrentUserInfo').then(function (res) {
            if (res) {
                return callBack(null, res.data);
            } else {
                return callBack('rep failed', null);
            }
        });
    }
    
    function updateUser(userInfo, callBack){
    	$http.put(ctx+'/user', userInfo).success(function(data, header, config, status){
    		if (data) {
                return callBack(null, data);
            } else {
                return callBack('更新失败，请重试', null);
            }
    	}).error(function(data, header, config, status){
    		return callBack(data.errorMsg, null);
    	});
    }
    
    function getEmailValidateCode(email, callBack){
    	$http.post(ctx+'/user/getEmailValidateCode', email).success(function(data, header, config, status){
    		if (data) {
                return callBack(null, data);
            } else {
                return callBack('获取失败，请重试', null);
            }
    	}, 'json').error(function(data, header, config, status){
    		return callBack(data.errorMsg, null);
    	});
    }
    
    function validateEmail(email, emailValidateCode, callBack){
    	$http.post(ctx+'/user/validateEmail', {'email': email, 'emailValidateCode': emailValidateCode}).success(function(data, header, config, status){
    		if (data) {
                return callBack(null, data);
            } else {
            	return callBack('更新失败，请重试', null);
            }
    	}).error(function(data, header, config, status){
    		return callBack(data.errorMsg, null);
    	});
    }
    
    function getPhoneValidateCode(phone, callBack){
    	$http.post(ctx+'/user/getPhoneValidateCode', phone).success(function(data, header, config, status){
    		if (data) {
                return callBack(null, data);
            } else {
                return callBack('获取失败，请重试', null);
            }
    	}, 'json').error(function(data, header, config, status){
    		return callBack(data.errorMsg, null);
    	});
    }
    
    function validatePhone(phone, phoneValidateCode, callBack){
    	$http.post(ctx+'/user/validatePhone', {'phone': phone, 'phoneValidateCode': phoneValidateCode}).success(function(data, header, config, status){
    		if (data) {
                return callBack(null, data);
            } else {
            	return callBack('更新失败，请重试', null);
            }
    	}).error(function(data, header, config, status){
    		return callBack(data.errorMsg, null);
    	});
    }

} 