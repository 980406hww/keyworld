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

            var chargeOption = {
                title: {
                    text: '整站收费统计图',
                    subtext: '',
                    y: '10',
                    x: '10'
                },
                grid: {
                    x: 40,
                    y: 50,
                    x2: 90,
                    y2: 20,
                },
                legend: {
                    orient: 'vertical',
                    icon: "circle",
                    x: 'right',
                    y: 'center',
                    data: ['首次收费', '续费', '暂停', '下架']
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        label: {
                            backgroundColor: '#337ab7'
                        }
                    }
                },
                toolbox: {
                    show: true,
                    right: 20,
                    feature: {
                        // mark: {show: true},
                        // dataView: {show: true, readOnly: false},
                        // magicType: {show: true, type: ['line', 'bar']},
                        // restore: {show: true},
                        // myTool1: {
                        //     show: true,
                        //     title: '自定义扩展方法',
                        //     icon: 'image:///images/favicon.png',
                        //     onclick: function () {
                        //         alert('myToolHandler2')
                        //     }
                        // },
                        // saveAsImage: {show: true}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    axisLabel: {
                        interval: 0,
                        // rotate: -50,
                    },
                    data: []
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    name: '首次收费',
                    type: 'line',
                    stack: '总量',
                    areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
                    data: [],
                }, {
                    name: '续费',
                    type: 'line',
                    stack: '总量',
                    areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
                    data: [],
                }, {
                    name: '暂停',
                    type: 'line',
                    stack: '总量',
                    areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
                    data: [],
                }, {
                    name: '下架',
                    type: 'line',
                    stack: '总量',
                    areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
                    data: [],
                }]
            };

            var chargeLogShow = echarts.init(document.getElementById('chargeLogShow'));

            getChargeMonData({searchEngines: null});

            function getChargeMonData(condition) {
                $.ajax({
                    url: '/internal/qzchargemon/getMonDataBySe',
                    type: 'post',
                    dataType: 'json',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    data: JSON.stringify(condition),
                    success: function (res) {
                        if (res.code === 200) {
                            chargeOption.xAxis.data = res.data.date;
                            chargeOption.series[0].data = res.data.one;
                            chargeOption.series[1].data = res.data.two;
                            chargeOption.series[2].data = res.data.three;
                            chargeOption.series[3].data = res.data.four;
                            chargeLogShow.setOption(chargeOption);//数据图
                        } else if (res.code === 300) {
                            chargeOption.series[0].data = [];
                            chargeOption.series[1].data = [];
                            chargeOption.series[2].data = [];
                            chargeOption.series[3].data = [];
                            chargeLogShow.setOption(chargeOption);
                        } else {
                            common.showFailMsg('获取数据失败');
                        }
                    },
                    error: function () {
                        common.showFailMsg('网络异常请稍后再试');
                    }
                });
            }

            form.on('select(searchEngines)', function () {
                getChargeMonData(common.formToJsonObject('form'));
            });

            form.on('select(qzTerminal)', function () {
                getChargeMonData(common.formToJsonObject('form'));
            });

            form.on('radio(time)', function () {
                getChargeMonData(common.formToJsonObject('form'));
            });

            window.toChargeMon = function () {
                common.updateOrNewTab('/internal/qzchargemon/toQzChargeMon', '整站收费操作统计', 'qzChargeMon');
            };

            okUtils.echartsResize([mapTree, mapChina, chargeLogShow]);

            $.ajax({
                url: '/internal/common/getSearchEngines',
                dataType: 'json',
                type: 'post',
                async: false,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({'terminalType': 'All'}),
                success: function (data) {
                    $("#searchEngines").empty();
                    $('#searchEngines').append('<option value>搜索引擎</option>');// 下拉菜单里添加元素
                    $.each(data.data, function (index, item) {
                        $('#searchEngines').append('<option value="' + item + '">' + item + '</option>');// 下拉菜单里添加元素
                    });
                    form.render("select");
                }
            });

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


