var searchEngine;

layui.use(['jquery', 'form', 'common'], function () {
    var $ = layui.jquery;
    var form = layui.form;
    var common = layui.common;

    var chargeOption = {
        title: {
            text: '每月站点操作监控',
            subtext: '',
            y: '10',
            x: '10'
        },
        grid: {
            x: 40,
            y: 50,
            x2: 90,
            y2: 50,
        },
        legend: {
            orient: 'vertical',
            icon: "circle",
            x: 'right',
            y: 'center',
            data: ['新增', '续费', '暂停', '下架', '删除']
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
            right: 20
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            axisLabel: {
                interval: 0,
                rotate: -30
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
                stack: '总量',
                smooth: true,
                areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
                data: [],
            },
            {
                name: '续费',
                type: 'line',
                stack: '总量',
                smooth: true,
                areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
                data: [],
            },
            {
                name: '暂停',
                type: 'line',
                stack: '总量',
                smooth: true,
                areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
                data: [],
            },
            {
                name: '下架',
                type: 'line',
                stack: '总量',
                smooth: true,
                areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
                data: [],
            },
            {
                name: '删除',
                type: 'line',
                stack: '总量',
                smooth: true,
                areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
                data: [],
            }
        ]
    };

    var chargeLogShow = echarts.init(document.getElementById('chargeLogShow'));

    if (condition) {
        getChargeMonData(condition);
    } else {
        getChargeMonData({searchEngines: '百度', qzTerminal: 'PC'});
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
                console.log(res);
                if (res.code === 200) {
                    chargeOption.xAxis.data = res.data.date;
                    chargeOption.series[0].data = res.data.one;
                    chargeOption.series[1].data = res.data.two;
                    chargeOption.series[2].data = res.data.three;
                    chargeOption.series[3].data = res.data.four;
                    chargeOption.series[4].data = res.data.five;
                    chargeLogShow.setOption(chargeOption); //数据图
                } else if (res.code === 300) {
                    chargeOption.series[0].data = [];
                    chargeOption.series[1].data = [];
                    chargeOption.series[2].data = [];
                    chargeOption.series[3].data = [];
                    chargeOption.series[4].data = [];
                    chargeLogShow.setOption(chargeOption);
                } else {
                    common.showFailMsg('每月站点操作监控数据获取失败');
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

    form.on('radio(time)', function (data) {
        switch (data.value) {
            case '1':
                chargeOption.xAxis.axisLabel.rotate = -30;
                chargeOption.grid.y2 = 50;
                break;
            case '2':
                chargeOption.xAxis.axisLabel.rotate = 0;
                chargeOption.grid.y2 = 20;
                break;
            case '3':
                chargeOption.xAxis.axisLabel.rotate = -30;
                chargeOption.grid.y2 = 50;
                break;
        }
        getChargeMonData(common.formToJsonObject('form'));
    });

    window.handle = function (name, data, sel = '百度') {
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