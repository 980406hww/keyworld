layui.use(['jquery', 'form', 'common'], function () {
    var $ = layui.jquery;
    var form = layui.form;
    var common = layui.common;

    var keywordOption = {
        title: {
            text: '关键字整体排名涨幅',
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
            data: ['前3名', '前5名', '前10名', '前50名']
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#337ab7'
                }
            },
            formatter: function (params) {
                let msg = params[0].name + '<br>';
                for (let i = 3; i >= 0; i--) {
                    msg += params[i].marker;
                    msg += params[i].seriesName;
                    msg += '：';
                    msg += params[i].value;
                    msg += '<br>';
                }
                return msg;
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
            name: '前50名',
            type: 'line',
            stack: '总量',
            areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
            data: [],
        }, {
            name: '前10名',
            type: 'line',
            stack: '总量',
            areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
            data: [],
        }, {
            name: '前5名',
            type: 'line',
            stack: '总量',
            areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
            data: [],
        }, {
            name: '前3名',
            type: 'line',
            stack: '总量',
            areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
            data: [],
        }]
    };

    var keywordLogShow = echarts.init(document.getElementById('keywordLogShow'));

    if (condition) {
        getChargeMonData(condition);
    } else {
        getChargeMonData({searchEngines: '百度', qzTerminal: 'PC'});
    }

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
                    keywordOption.xAxis.data = res.data.date;
                    keywordOption.series[3].data = res.data.two;
                    keywordOption.series[2].data = res.data.three;
                    keywordOption.series[1].data = res.data.four;
                    keywordOption.series[0].data = res.data.one;
                    keywordLogShow.setOption(keywordOption);//数据图
                } else if (res.code === 300) {
                    keywordOption.series[1].data = [];
                    keywordOption.series[2].data = [];
                    keywordOption.series[3].data = [];
                    keywordOption.series[0].data = [];
                    keywordLogShow.setOption(keywordOption);
                } else {
                    common.showFailMsg('获取数据失败');
                }
            },
            error: function () {
                common.showFailMsg('网络异常请稍后再试');
            }
        });
    }

    // form.on('select(searchEngines)', function () {
    //     getChargeMonData(common.formToJsonObject('keywordFrom'));
    // });
    //
    // form.on('select(qzTerminal)', function () {
    //     getChargeMonData(common.formToJsonObject('keywordFrom'));
    // });
    //
    form.on('radio(keywordTime)', function () {
        getChargeMonData(common.formToJsonObject('keywordFrom'));
    });

    if (condition) {
        // let options = document.getElementById('qzTerminal').children;
        // let radios = document.getElementsByName('time');
        // for (let i = 0; i < 3; i++) {
        //     options[i].removeAttribute('selected');
        //     if (options[i].value === condition.qzTerminal) {
        //         options[i].setAttribute('selected', '');
        //     }
        //     radios[i].checked = radios[i].value === condition.time;
        // }
        // form.render('radio');
        // getSeData('searchEngines', condition.searchEngines);
    } else {
        if (searchEngine) {
            // handle();
        } else {
            // getSeData();
        }
    }
});