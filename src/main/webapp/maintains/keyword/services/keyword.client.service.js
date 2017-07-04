angular.module('KeywordManagement').factory('KeywordService', KeywordService);

KeywordService.$inject = ['$http'];

function KeywordService($http) {
    return {
        findKeywords: findKeywords,
        saveKeyword: saveKeyword,
        deleteKeyword: deleteKeyword,
        changeStatus: changeStatus,
        findKeywordsForUser: findKeywordsForUser
    };

    function findKeywords(criteria, callBack) {
        $http.post(ctx + '/keyword/find', criteria).then(function (res) {
            if (res) {
                return callBack(null, res.data);
            } else {
                return callBack('rep failed', null);
            }
        });
    }

    function saveKeyword(keyword, callBack) {
        $http.post(ctx + '/keyword/save', keyword).then(function (res) {
            if (res) {
                return callBack(null, res.data);
            } else {
                return callBack('rep failed', null);
            }

        });
    }

    function changeStatus(keyword, callBack) {
        $http.post(ctx + '/keyword/changeStatus', keyword).then(function (res) {
            if (res) {
                return callBack(null, res.data);
            } else {
                return callBack('rep failed', null);
            }

        });
    }

    function deleteKeyword(keywordId, callBack) {
        $http.delete(ctx + '/keyword/' + keywordId).success(function (data) {
            return callBack(null, data);
        }).error(function (error) {
            return callBack(error, null);
        });
    }

    function findKeywordsForUser(callBack) {
        $http.post(ctx + '/keyword/findKeywordsForAutoComplete').then(function (res) {
            if (res) {
                return callBack(null, res.data);
            } else {
                return callBack('rep failed', null);
            }
        });
    }
} 