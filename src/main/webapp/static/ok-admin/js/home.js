"use strict";
var useModel = ["common"];//需要引入的模块
layui.config({base: "/static/ok-admin/js/"}).use(useModel, function () {
        var common = layui.common;

        window.qzRateDetail = function () {
            let postData = common.formToJsonObject('qzRateSearchForm');
            let qzRateRange = postData.qzRateRange === '' ? '1' : postData.qzRateRange;
            let url = '/internal/qzRateStatistics/toQZRateStatisticsDetail/' + qzRateRange + '/' + postData.terminalType + '/' + postData.searchEngine;
            url = encodeURI(encodeURI(url));
            common.updateOrNewTab(url, '站点趋势详情', 'qzRateStatisticsDetail');
        };

        window.toChargeMon = function () {
            let condition = common.formToJsonObject('form');
            let url = '/internal/qzchargemon/toQzChargeMonWithParam/' + condition.time;
            url = encodeURI(encodeURI(url));
            common.updateOrNewTab(url, '站点监控详情', 'qzChargeMon');
        };

        window.toCKPositionSummary = function () {
            let condition = common.formToJsonObject('keywordFrom');
            let url = '/internal/ckpositionsummary/toCustomerKeywordPositionSummary/' + condition.time + '/' + condition.terminal + '/' + condition.searchEngine;
            url = encodeURI(encodeURI(url));
            common.updateOrNewTab(url, '排名涨幅详情', 'customerKeywordsMon');
        };
    }
);


