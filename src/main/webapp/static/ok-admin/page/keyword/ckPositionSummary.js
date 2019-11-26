layui.use(['jquery', 'form', 'common', 'table'], function () {
    var $ = layui.jquery;
    var form = layui.form;
    var common = layui.common;
    var table = layui.table;

    var keywordOption = {
        color: ['#51d02e', '#2aa0ea', '#fac600', '#a951ec'],
        title: {
            text: '每日排名趋势',
            subtext: '',
            y: '10',
            x: '10'
        },
        grid: {
            x: 40,
            y: 70,
            x2: 20,
            y2: 20,
        },
        legend: {
            orient: 'horizontal',
            icon: "rect",
            top: '40',
            x: 'center',
            data: ['前3名', '前5名', '前10名', '前50名']
        },
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            right: 20
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            axisLabel: {
                interval: 10
            },
            data: []
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '前3名',
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
                name: '前5名',
                type: 'line',
                smooth: true,
                symbolSize: 1,
                symbol: 'none',
                lineStyle: {
                    type: "solid",
                    width: 1
                },
                data: []
            },
            {
                name: '前10名',
                type: 'line',
                smooth: true,
                symbolSize: 1,
                symbol: 'none',
                lineStyle: {
                    type: "solid",
                    width: 1
                },
                data: []
            },
            {
                name: '前50名',
                type: 'line',
                smooth: true,
                symbolSize: 1,
                symbol: 'none',
                lineStyle: {
                    type: "solid",
                    width: 1
                },
                data: []
            }
        ]
    };

    var keywordLogShow = echarts.init(document.getElementById('keywordLogShow'));
    keywordLogShow.showLoading({text: '数据加载中'});

    if (condition) {
        getCustomerKeywordPositionSummaryData(condition);
        tableInit(condition);
    } else {
        getCustomerKeywordPositionSummaryData({searchEngine: '百度', terminal: 'PC', time: '-90'});
        tableInit({
            searchEngine: '百度',
            terminal: 'PC',
            dateStart: layui.util.toDateString(new Date(), 'yyyy-MM-dd'),
            dateEnd: layui.util.toDateString(new Date(), 'yyyy-MM-dd') + ' 23:59:59'
        });
    }

    function getCustomerKeywordPositionSummaryData(condition) {
        $.ajax({
            url: '/internal/ckpositionsummary/getCustomerKeywordPositionSummaryData',
            type: 'post',
            dataType: 'json',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(condition),
            success: function (res) {
                keywordLogShow.hideLoading();
                if (res.code === 200) {
                    keywordOption.xAxis.data = res.data.date;
                    keywordOption.series[0].data = res.data.topThreeData;
                    keywordOption.series[1].data = res.data.topFiveData;
                    keywordOption.series[2].data = res.data.topTenData;
                    keywordOption.series[3].data = res.data.topFifthData;
                    keywordLogShow.setOption(keywordOption);
                } else if (res.code === 300) {
                    keywordOption.xAxis.data = [];
                    keywordOption.series[0].data = [];
                    keywordOption.series[1].data = [];
                    keywordOption.series[2].data = [];
                    keywordOption.series[3].data = [];
                    keywordLogShow.setOption(keywordOption);
                    common.showFailMsg(res.msg);
                } else {
                    common.showFailMsg('每日排名趋势数据获取失败');
                }
            },
            error: function () {
                keywordLogShow.hideLoading();
                common.showFailMsg('网络异常请稍后再试');
            }
        });
    }

    form.on('select(searchEngine)', function () {
        var condition = common.formToJsonObject('keywordFrom');
        getCustomerKeywordPositionSummaryData(condition);
        reload(condition);
    });

    form.on('select(terminal)', function () {
        var condition = common.formToJsonObject('keywordFrom');
        getCustomerKeywordPositionSummaryData(condition);
        reload(condition);
    });

    form.on('radio(keywordTime)', function (data) {
        var condition = common.formToJsonObject('keywordFrom');
        switch (data.value) {
            case '-7':
                keywordOption.xAxis.axisLabel.interval = 0;
                break;
            case '-30':
                keywordOption.xAxis.axisLabel.interval = 3;
                break;
            default:
                keywordOption.xAxis.axisLabel.interval = 6;
                break;
        }
        getCustomerKeywordPositionSummaryData(condition);
    });

    form.on('select(type)', function () {
        var condition = common.formToJsonObject('keywordFrom');
        getCustomerKeywordPositionSummaryData(condition);
        reload(condition);
    });

    form.on('select(customer)', function () {
        var condition = common.formToJsonObject('keywordFrom');
        getCustomerKeywordPositionSummaryData(condition);
        reload(condition);
    });

    window.searchTable = function () {
        reload(common.formToJsonObject('keywordFrom'));
    };

    window.handle = function (name, data, searchEngine) {
        $("#" + name).empty();
        $.each(data, function (index, item) {
            if (searchEngine === item) {
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
        for (let i = 0; i < 2; i++) {
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
            getSeData('searchEngine', '百度');
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
            icon: "rect",
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
                interval: 0
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
    };

    function tableInit(condition) {
        table.render({
            elem: '#table',
            method: 'post',
            url: '/internal/ckpositionsummary/getCKPositionSummaryDataInitTable',
            page: true,
            limit: 100,
            limits: [10, 25, 50, 100, 500],
            size: 'md',
            id: 'table',
            even: true,//隔行背景
            where: condition,
            defaultToolbar: [],
            contentType: 'application/json',
            cols: [[
                {field: 'keyword', title: '关键字', width: '20%', align: 'left'},
                {field: 'customer', title: '客户名称', width: '16%', align: 'left'},
                {title: '近日趋势', width: '20%', align: 'center'},
                {
                    field: 'position', title: '现排名', width: '6%', align: 'center', templet: function (d) {
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
                {field: 'searchEngine', title: '搜索引擎', width: '8%', align: 'center'},
                {
                    field: 'terminal', title: '终端', width: '7%', align: 'center', templet: function (d) {
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
                    field: 'type', title: '类型', width: '8%', align: 'center', templet: function (d) {
                        switch (d.type) {
                            case 'pt':
                                return '单词';
                            case 'qz':
                                return '全站';
                            case 'fm':
                                return '负面';
                            case 'qt':
                                return '其他';
                            default:
                                return '未知';
                        }
                    }
                },
                {
                    field: 'date', title: '抓取时间', width: '15%', align: 'center', templet: function (d) {
                        return layui.util.toDateString(d.date, 'yyyy-MM-dd HH:mm:ss');
                    }
                }
            ]],
            height: 'full-425',
            loading: true,
            done: function (res, curr, count) {
                console.log(res);
                showLogData(res.data);
            }
        });
    }

    /**
     * 一周的数据
     */
    function showLogData(data) {
        var boxes = document.getElementsByClassName('layui-table-body')[0].getElementsByTagName('tr');
        for (let i = 0; i < boxes.length; i++) {
            $.ajax({
                url: '/internal/ckpositionsummary/getOneCKPositionSummaryData/' + data[i].customerKeywordUuid,
                timeout: 5000,
                type: 'GET',
                dataType: 'json',
                success: function (result) {
                    if (result.code === 200) {
                        if (result.data !== null && result.data.hData !== null && result.data.hDate !== null) {
                            option.series[0].data = result.data.hData.split(',');
                            option.xAxis.data = result.data.hDate.split(',');
                            echarts.init(boxes[i].children[2].firstElementChild).setOption(option);
                        } else {
                            boxes[i].children[2].firstElementChild.innerHTML = '暂无数据';
                        }
                    } else {
                        boxes[i].children[2].firstElementChild.innerHTML = '暂无数据';
                    }
                }
            });
        }
    }

    function reload(condition) {
        let postData = common.formToJsonObject('searchForm');
        postData.dateStart = layui.util.toDateString(new Date(), 'yyyy-MM-dd');
        postData.dateEnd = layui.util.toDateString(new Date(), 'yyyy-MM-dd') + ' 23:59:59';
        Object.assign(condition, postData);
        table.reload('table', {
            where: condition,
            page: {
                curr: 1
            },
            done: function (res, curr, count) {
                console.log(res);
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
});