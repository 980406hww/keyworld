layui.use(['jquery', 'form', 'common', 'table'], function () {
    var $ = layui.jquery;
    var form = layui.form;
    var common = layui.common;
    var table = layui.table;

    window.getKeywordMonData = function (condition) {
        $.ajax({
            url: '/internal/customerkeywordmon/getMonDataByCondition',
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
                    keywordOption.series[3].data = res.data.one;
                    keywordOption.series[2].data = res.data.two;
                    keywordOption.series[1].data = res.data.three;
                    keywordOption.series[0].data = res.data.four;
                    keywordLogShow.setOption(keywordOption);//数据图
                } else if (res.code === 300) {
                    keywordOption.series[3].data = [];
                    keywordOption.series[2].data = [];
                    keywordOption.series[1].data = [];
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
    };

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
            smooth: true,
            areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
            data: [],
        }, {
            name: '前10名',
            type: 'line',
            stack: '总量',
            smooth: true,
            areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
            data: [],
        }, {
            name: '前5名',
            type: 'line',
            stack: '总量',
            smooth: true,
            areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
            data: [],
        }, {
            name: '前3名',
            type: 'line',
            stack: '总量',
            smooth: true,
            areaStyle: {normal: {color: 'rgba(255,255,255,0)'}},
            data: [],
        }]
    };

    var keywordLogShow = echarts.init(document.getElementById('keywordLogShow'));

    if (condition) {
        getKeywordMonData(condition);
        tableInit(condition);
    } else {
        getKeywordMonData({searchEngine: '百度', terminal: 'PC'});
        tableInit({searchEngine: '百度', terminal: 'PC'});
    }

    form.on('select(searchEngine)', function () {
        var condition = common.formToJsonObject('keywordFrom');
        getKeywordMonData(condition);
        reload(condition);
    });

    form.on('select(terminal)', function () {
        var condition = common.formToJsonObject('keywordFrom');
        getKeywordMonData(condition);
        reload(condition);
    });

    form.on('radio(keywordTime)', function () {
        var condition = common.formToJsonObject('keywordFrom');
        getKeywordMonData(condition);
    });

    form.on('select(type)', function () {
        var condition = common.formToJsonObject('keywordFrom');
        getKeywordMonData(condition);
        reload(condition);
    });

    form.on('select(customer)', function () {
        var condition = common.formToJsonObject('keywordFrom');
        getKeywordMonData(condition);
        reload(condition);
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
        let options = document.getElementById('terminal').children;
        let radios = document.getElementsByName('time');
        for (let i = 0; i < 3; i++) {
            options[i].removeAttribute('selected');
            if (options[i].value === condition.terminal) {
                options[i].setAttribute('selected', '');
            }
            radios[i].checked = radios[i].value === condition.time;
        }
        form.render('radio');
        getSeData('searchEngine', condition.searchEngine);
    } else {
        if (searchEngine) {
            handle('searchEngine', searchEngine);
        } else {
            getSeData('searchEngine');
        }
        form.render('select');
    }

    var option = {
            title: {
                text: '近日趋势',
                subtext: '',
                y: '10',
                x: '10',
                show: false
            },
            grid: {
                x: 10,
                y: 5,
                x2: 10,
                y2: 5,
            },
            legend: {
                orient: 'vertical',
                icon: "circle",
                x: 'right',
                y: 'center',
                data: ['排名'],
                show: false
            },
            tooltip: {
                trigger: 'axis',
                show: true,
                axisPointer: {
                    type: 'none'
                },
                confine: true,
                formatter: function (p) {
                    return '<div style="font-size: 6px;line-height: 14px"><span>' + p[0].axisValue + '</span>'
                        + '<br>' + p[0].marker + '<span>排名：' + p[0].value + '</span></div>'
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                axisLabel: {
                    interval: 0,
                    // rotate: -50,
                },
                data: [],
                show: false
            },
            yAxis: {
                type: 'value',
                show: false,
                inverse: true
            },
            series: [{
                name: '排名',
                type: 'line',
                stack: '总量',
                smooth: true,
                symbol: 'none',
                color: '#6daded',
                areaStyle: {
                    color: 'rgba(109,173,237,0.23)',
                    origin: 'end'
                },
                data: []
            }]
        }
    ;

    function tableInit(condition) {
        table.render({
            elem: '#table',
            method: 'post',
            url: '/internal/customerkeywordmon/getMonTableDataByCondition',
            page: true,
            limit: 25,
            limits: [10, 25, 50, 100, 500],
            size: 'md',
            id: 'table',
            even: false,//隔行背景
            where: condition,
            defaultToolbar: [],
            contentType: 'application/json',
            cols: [[
                {field: 'keyword', title: '关键字', width: '23%', align: 'center'},
                {field: 'customer', title: '客户名称', width: '18%', align: 'center'},
                {title: '近日趋势', width: '14%', align: 'center'},
                {
                    field: 'position', title: '现排名', width: '7%', align: 'center', templet: function (d) {
                        let msg = '';
                        if (d.position <= 3) {
                            msg = '<span style="color:black;">' + d.position + '</span>';
                        } else if (d.position <= 5) {
                            msg = '<span style="color:black;">' + d.position + '</span>';
                        } else if (d.position <= 10) {
                            msg = '<span style="color:black;">' + d.position + '</span>';
                        } else if (d.position <= 50) {
                            msg = '<span style="color:black;">' + d.position + '</span>';
                        } else {
                            msg = '<span style="color:black;">' + d.position + '</span>';
                        }
                        return msg;
                    }
                },
                {field: 'searchEngine', title: '搜索引擎', width: '9%', align: 'center'},
                {
                    field: 'terminal', title: '终端', width: '8%', align: 'center', templet: function (d) {
                        if (d.terminal === 'PC') {
                            return '电脑';
                        } else if (d.terminal === 'Phone') {
                            return '手机';
                        } else {
                            return '';
                        }
                    }
                },
                {
                    field: 'type', title: '类型', width: '9%', align: 'center', templet: function (d) {
                        switch (d.type) {
                            case 'pt':
                                return '单词';
                            case 'qz':
                                return '全站';
                            case 'fm':
                                return '负面';
                            default:
                                return '未知';
                        }
                    }
                },
                {
                    field: 'date', title: '统计时间', width: '12%', align: 'center', templet: function (d) {
                        return layui.util.toDateString(d.date, 'yyyy-MM-dd HH:mm:ss');
                    }
                }
            ]],
            height: 'full-385',
            loading: true,
            done: function (res, curr, count) {
                showLogData(res.data);
            }
        });
    }

    function showLogData(data) {
        var boxes = document.getElementById('table').nextElementSibling
        .getElementsByClassName('layui-table-body')[0].getElementsByClassName('laytable-cell-1-0-2');
        for (let i = 0; i < boxes.length; i++) {
            if (data[i].hData) {
                let boxLogShow = echarts.init(boxes[i]);
                option.series[0].data = data[i].hData.split(',');
                option.xAxis.data = data[i].hDate.split(',');
                boxLogShow.setOption(option);
                // boxes[i].children[0].children[0].style.cursor = 'pointer';
            }
        }
    }

    function reload(condition) {
        table.reload('table', {
            where: condition,
            page: {
                curr: 1
            },
            done: function (res, curr, count) {
                showLogData(res.data);
            }
        });
    }

    table.on('tool(table)', function (obj) {
        var data = obj.data, event = obj.event;
        if (event === 'toQzSetting') {
            $.ajax({
                url: '/internal/customer/getCustomerUuidByName',
                type: 'post',
                data: JSON.stringify({name: data.qzCustomer}),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                dataType: 'json'
                , success: function (res) {
                    if (res.code === 200) {
                        let url = '/internal/qzsetting/toQzSetting/' + data.qzDomain + '/' + data.terminalType.split(',')[0] + '/';
                        url += res.data ? res.data : '';
                        url = encodeURI(encodeURI(url));
                        common.updateOrNewTab(url, data.qzCustomer + '整站信息', data.qzCustomer + '整站信息');
                    }
                }
            });
        }
    });
})
;