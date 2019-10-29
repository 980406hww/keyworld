"use strict";
var useModel = ["common"];//需要引入的模块
layui.config({base: "/static/ok-admin/js/"}).use(useModel, function () {
        var common = layui.common;

        window.toChargeMon = function () {
            let condition = common.formToJsonObject('form');
            if (!condition.qzTerminal) {
                condition.qzTerminal = 'null';
            }
            if (!condition.searchEngines) {
                condition.searchEngines = 'null';
            }
            let url = '/internal/qzchargemon/toQzChargeMonWithParam/' + condition.qzTerminal + '/' + condition.searchEngines + '/' + condition.time;
            url = encodeURI(encodeURI(url));
            common.updateOrNewTab(url, '每日站点操作监控', 'qzChargeMon');
        };

        window.toCustomerKeywordMon = function () {
            let condition = common.formToJsonObject('keywordFrom');
            if (!condition.terminal) {
                condition.terminal = 'null';
            }
            if (!condition.searchEngine) {
                condition.searchEngine = 'null';
            }
            let url = '/internal/customerkeywordmon/toCustomerKeywordMon/' + condition.terminal + '/' + condition.searchEngine + '/' + condition.time;
            url = encodeURI(encodeURI(url));
            common.updateOrNewTab(url, '关键字整体排名涨幅情况', 'customerKeywordsMon');
        };

    }
);


