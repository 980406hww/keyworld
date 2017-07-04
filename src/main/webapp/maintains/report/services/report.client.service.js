angular.module('ReportManagement').factory('ReportService', ReportService);

ReportService.$inject = ['$http'];

function ReportService($http) {
    return {
        findReports: findReports,
        upload: upload,
        downloadReport: downloadReport
    };

    function findReports(criteria, callBack) {
        $http.post(ctx + '/report/find', criteria).then(function (res) {
            if (res) {
                return callBack(null, res.data);
            } else {
                return callBack('rep failed', null);
            }

        });
    };

    function upload(report, callBack) {
        $http.post(ctx + '/report/upload', report,{
            transformRequest: function(data, headersGetterFunction) {
                return data;
            },
            headers: { 'Content-Type': undefined }}
        ).then(function (res) {
            if (res) {
                return callBack(null, res.data);
            } else {
                return callBack('rep failed', null);
            }

        });
    };

    function downloadReport(criteria, callBack) {
        $http.post(ctx + '/report/downloadReport', criteria, {responseType: "blob"}).then(function (res) {
            if (res) {
                return callBack(null, res.data);
            } else {
                return callBack('rep failed', null);
            }

        });
    };
} 