var searchEngine;

layui.use(['jquery', 'form', 'common'], function () {
    var $ = layui.jquery;
    var form = layui.form;
    var common = layui.common;

    var chargeOption = {
        color: ['#51d02e', '#2aa0ea', '#fac600', '#ff3701', '#a951ec'],
        title: {
            text: '每月站点监控',
            subtext: '',
            y: '10',
            x: '10'
        },
        grid: {
            x: 40,
            y: 50,
            x2: 60,
            y2: 20,
        },
        legend: {
            orient: 'vertical',
            icon: "rect",
            x: 'right',
            y: 'center',
            data: ['新增', '续费', '暂停', '下架', '删除']
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            axisLabel: {
                interval: 1
            },
            data: []
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '新增',
                type: 'line',
                smooth: true,
                symbolSize: 1,
                symbol: 'none',
                lineStyle: {
                    type: "solid",
                    width: 1
                },
                data: [],
            },
            {
                name: '续费',
                type: 'line',
                smooth: true,
                symbolSize: 1,
                symbol: 'none',
                lineStyle: {
                    type: "solid",
                    width: 1
                },
                data: [],
            },
            {
                name: '暂停',
                type: 'line',
                smooth: true,
                symbolSize: 1,
                symbol: 'none',
                lineStyle: {
                    type: "solid",
                    width: 1
                },
                data: [],
            },
            {
                name: '下架',
                type: 'line',
                smooth: true,
                symbolSize: 1,
                symbol: 'none',
                lineStyle: {
                    type: "solid",
                    width: 1
                },
                data: [],
            },
            {
                name: '删除',
                type: 'line',
                smooth: true,
                symbolSize: 1,
                symbol: 'none',
                lineStyle: {
                    type: "solid",
                    width: 1
                },
                data: [],
            }
        ]
    };

    var chargeLogShow = echarts.init(document.getElementById('chargeLogShow'));
    chargeLogShow.showLoading({text: '数据加载中'});

    if (condition) {
        getChargeMonData(condition);
    } else {
        getChargeMonData({searchEngines: '', qzTerminal: '', time: "1"});
    }

    function getChargeMonData(condition) {
        $.ajax({
            url: '/internal/qzchargemon/getQZChargeMonData',
            type: 'post',
            dataType: 'json',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(condition),
            success: function (res) {
                chargeLogShow.hideLoading();
                if (res.code === 200) {
                    chargeOption.xAxis.data = res.data.date;
                    chargeOption.series[0].data = res.data.addQzDataCount;  // 新增
                    chargeOption.series[1].data = res.data.renewalQzDataCount;  // 续费
                    chargeOption.series[2].data = res.data.stopQzDataCount; // 暂停
                    chargeOption.series[3].data = res.data.obtainedQzDataCount;  // 下架
                    chargeOption.series[4].data = res.data.deleteQzDataCount;  // 删除
                    chargeLogShow.setOption(chargeOption, true); //数据图
                } else if (res.code === 300) {
                    chargeOption.series[0].data = [];
                    chargeOption.series[1].data = [];
                    chargeOption.series[2].data = [];
                    chargeOption.series[3].data = [];
                    chargeOption.series[4].data = [];
                    chargeLogShow.setOption(chargeOption, true);
                } else {
                    common.showFailMsg('每月站点监控数据获取失败');
                }
            },
            error: function () {
                chargeLogShow.hideLoading();
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

    form.on('radio(time)', function (data) {
        switch (data.value) {
            case '1':
                chargeOption.xAxis.axisLabel.interval = 1;
                break;
            case '2':
                chargeOption.xAxis.axisLabel.interval = 3;
                break;
            default:
                chargeOption.xAxis.axisLabel.interval = 3;
                break;
        }
        getChargeMonData(common.formToJsonObject('form'));
    });

    window.handle = function (name, data, sel) {
        $("#" + name).empty();
        $('#' + name).append('<option value>所有</option>');// 下拉菜单里添加元素
        $.each(data, function (index, item) {
            if (sel === item) {
                $('#' + name).append('<option value="' + item + '" selected>' + item + '</option>');// 下拉菜单里添加元素
            } else {
                $('#' + name).append('<option value="' + item + '">' + item + '</option>');// 下拉菜单里添加元素
            }
        });
    };

    window.getSeData = function (name, sel) {
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
                searchEngine = data.data;
                handle(name, searchEngine, sel);
                form.render("select");
            }
        });
    };

    if (condition) {
        let options = document.getElementById('qzTerminal').children;
        let radios = document.getElementsByName('time');
        for (let i = 0; i < 3; i++) {
            options[i].removeAttribute('selected');
            if (options[i].value === condition.qzTerminal) {
                options[i].setAttribute('selected', '');
            }
            radios[i].checked = radios[i].value === condition.time;
        }
        form.render('radio');
        getSeData('searchEngines', condition.searchEngines);
    } else {
        getSeData('searchEngines');
    }
});