"use strict";
var useModel = ["form", "okUtils", "table", "laytpl", "laydate", "element", "jquery", "countUp", "echartsData", "form", "common"];//需要引入的模块
layui.config({
    base: "/static/ok-admin/js/"
}).use(useModel, function () {
        var $form = layui.form,
            countUp = layui.countUp,
            laydate = layui.laydate,
            element = layui.element,
            table = layui.table,
            okUtils = layui.okUtils,
            $ = layui.jquery,
            laytpl = layui.laytpl,
            common = layui.common,
            form = layui.form;
        /**静态数据**/
        var echartsData = layui.echartsData;
        init();

        function init() {
            /**今日访问量**/
            var elem_nums = $(".stat-text");
            elem_nums.each(function (i, j) {
                let ran = parseInt(Math.random() * 99 + 1);
                !new countUp({
                    target: j,
                    endVal: 20 * ran
                }).start();
            });

            /**图表**/
            var mapTree = echarts.init($("#mapOne")[0], "mytheme");
            var mapChina = echarts.init($('#mapChina')[0]);
            mapTree.setOption(echartsData.mapTree);//数据图
            echartsData.mapChina.series[0].data = echartsData.Address;//地图数据
            // visualMap
            mapChina.setOption(echartsData.mapChina);//地图

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

            okUtils.echartsResize([mapTree, mapChina, chargeLogShow]);

            /**日历**/
            laydate.render({
                elem: '#calendar',
                position: 'static',
                show: true,
                btns: ['now'],
                calendar: true,//显示节日
                change: function (value, date) { //监听日期
                }
            });
        }
    }
);


