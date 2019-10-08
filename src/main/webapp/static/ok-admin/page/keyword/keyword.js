var sign = false;
// 进度条加载提示
NProgress.start();
window.onload = function () {
    NProgress.done();
};
// layui相关
layui.use(['element', 'table', 'form', 'jquery', 'laydate', 'okLayer', 'layer'], function () {
    var element = layui.element;
    var table = layui.table;
    var form = layui.form;
    var $ = layui.jquery;
    var laydate = layui.laydate;

    //日期范围
    laydate.render({
        elem: '#gtCreateTime',
    });
    laydate.render({
        elem: '#ltCreateTime',
    });
    init_search();

    function init_search() {
        init_keyword_type();
        init_belong_user();
        init_searchEngine();
        get_keywords(formToJsonObject('searchForm'));
    }

    function init_keyword_type() {
        $.ajax({
            url: '/internal/common/getBusinessTypeByUserRole',
            dataType: 'json',
            async: false,
            type: 'get',
            success: function (res) {
                if (res.code === 200) {
                    // $("#tabItem").empty();
                    let i = 0;
                    $.each(res.data, function (index, item) {
                        let businessItem = item.split("#");
                        if (i === 0) {
                            $('#tabItem').append(
                                '<li data-type="' + businessItem[0] + '" data-terminal="PC" class="layui-this">' + businessItem[1] + '电脑</li>' +
                                '<li data-type="' + businessItem[0] + '" data-terminal="Phone">' + businessItem[1] + '手机</li>');
                            $('#type').val(businessItem[0]);
                            $('#terminalType').val('PC');
                        }else {
                            $('#tabItem').append(
                                '<li data-type="' + businessItem[0] + '" data-terminal="PC">' + businessItem[1] + '电脑</li>' +
                                '<li data-type="' + businessItem[0] + '" data-terminal="Phone">' + businessItem[1] + '手机</li>');
                        }
                        i++;
                    });
                    form.render("select");
                }
            }
        });
    }

    function init_belong_user() {
        $.ajax({
            url: '/internal/customer/getActiveUsers',
            dataType: 'json',
            type: 'get',
            success: function (data) {
                $("#userName").empty();
                $("#userName").append('<option value="">所属用户</option>');
                $.each(data, function (index, item) {
                    $('#userName').append(
                        '<option value="' + item.loginName + '">'
                        + item.userName
                        + '</option>');// 下拉菜单里添加元素
                });
                form.render("select");
            }
        });
    }

    function init_searchEngine() {
        $.ajax({
            url: '/internal/common/getSearchEngines',
            dataType: 'json',
            type: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify({'terminalType': 'All'}),
            success: function (data) {
                $("#searchEngine").empty();
                $("#searchEngine").append('<option value="">搜索引擎</option>');
                $.each(data.data, function (index, item) {
                    $('#searchEngine').append(
                        '<option value="' + item + '">' + item + '</option>');// 下拉菜单里添加元素
                });
                form.render("select");
            }
        });
    }

    function get_keywords(whereCondition) {
        var keywordTable = table.render({
            elem: '#keywordTable',
            method: 'post',
            url: '/internal/customerKeyword/getKeywords',
            autoSort: false,
            limit: 25,
            limits: [10, 25, 50, 75, 100, 500, 1000],
            page: true,
            size: 'sm',
            id: 'keywordTable',
            even: true,//隔行背景
            where: whereCondition,
            toolbar: "#toolbarTpl",
            defaultToolbar: ['filter'],
            contentType: 'application/json',
            cols: [[
                {filed: 'uuid', type: 'checkbox', width: '35'},
                {field: 'userID', title: '用户', width: '120',},
                {field: 'contactPerson', title: '客户名称', width: '120', templet: '#toCustomerKeywordTpl' },
                {field: 'keyword', title: '关键字', width: '150', sort: true},
                {field: 'url', title: '链接', width: '120'},
                {field: 'bearPawNumber', title: '熊掌号', width: '100'},
                {field: 'title', title: '标题', width: '220'},
                {field: 'currentIndexCount', title: '指数', width: '80', templet: '#indexCountTpl'},
                {field: 'initialPosition', title: '初始排名', width: '80'},
                {field: 'currentPosition', title: '现排名', width: '80', sort: true},
                {field: 'searchEngine', title: '搜索引擎', width: '80'},
                {field: 'optimizeGroupName', title: '优化分组', width: '100'},
                {field: 'machineGroup', title: '机器分组', width: '100'},
                {field: 'city', title: '目标城市', width: '80', hide: true},
                {field: 'collectMethod', title: '收费方式', width: '80', templet: '#collectMethodTpl' },
                {field: 'optimizePlanCount', title: '要刷', width: '60',},
                {field: 'optimizedCount', title: '已刷', width: '60'},
                {field: 'invalidRefreshCount', title: '无效', width: '60', hide: true },
                {field: 'status', title: '状态', width: '60', templet: '#statusTpl' },
                {field: 'paymentStatus', title: '付费状态', width: '80', hide: true },
                {field: 'remarks', title: '备注', width: '100', hide: true},
                {field: 'failedCause', title: '失败原因', width: '80', hide: true},
            ]],
            height: 'full-150',
            done: function (res, curr, count) {
                let tables = document.getElementsByTagName('table');
                if ((tables[2].offsetHeight || tables[2].clientHeight || tables[2].scrollHeight) > (tables[2].parentElement.offsetHeight
                    || tables[2].parentElement.clientHeight || tables[2].scrollHeight)) {
                    document.getElementsByClassName('layui-table-header')[0].classList.add('details-header');
                } else {
                    document.getElementsByClassName('layui-table-header')[0].classList.remove('details-header');
                }
            }
        });

    }
    table.on('sort(tableFilter)', function(obj){
        let postData = formToJsonObject('searchForm');
        postData.orderBy = obj.field;
        postData.orderMode = obj.type === 'desc' ? '0' : '1';
        table.reload('keywordTable', {
            initSort: obj,
            where: postData
        });
    });
    // 监听表头鼠标按下事件
    $(document).on('onMouseUp', 'thead', function (e) {
            let tables = document.getElementsByTagName('table');
            if ((tables[2].offsetHeight || tables[2].clientHeight || tables[2].scrollHeight) > (tables[2].parentElement.offsetHeight
                || tables[2].parentElement.clientHeight || tables[2].scrollHeight)) {
                document.getElementsByClassName('layui-table-header')[0].classList.add('details-header');
            } else {
                document.getElementsByClassName('layui-table-header')[0].classList.remove('details-header');
            }
        }
    );
    //监听工具条
    var active = {
        reload: function () {
            table.reload('keywordTable', {
                where: formToJsonObject('searchForm'),
                page: {
                    curr: 1 //从第一页开始
                }
            });
        },
    };

    function get_selected_uuid_arr() {
        var checkStatus = table.checkStatus('keywordTable')
            , data = checkStatus.data;
        var uuidArr = [];
        $.each(data, function (index, item) {
            uuidArr.push(item.uuid);
        });
        return uuidArr;
    }

    function show_layer_msg(msg, icon, status, time, title) {
        layer.msg(msg, {
            icon: icon,
            title: title === undefined ? null : title,
            anim: 5,
            time: time === undefined ? 2000 : time,
            isOutAnim: false
        }, function () {
            if (status) {
                active['reload'].call(this);
            }
        });
    }

    form.on("submit(search)", function (data) {
        if (!data.field.noPosition){
            data.field.noPosition = '';
        }
        if (!data.field.pushPay){
            data.field.pushPay = '';
        }
        if (!data.field.requireDelete){
            data.field.requireDelete = '';
        }
        table.reload('keywordTable', {
            where: data.field,
            page: {
                curr: 1 //从第一页开始
            }
        });
        if (!open) {
            showCondition();
        }
        return false;
    });

    table.on('toolbar(tableFilter)', function (obj) {
        var data = obj.data, event = obj.event;
        switch (event) {
            case 'pause_keyword':
                updateCustomerKeywordStatus(0);
                break;
            case 'down_keyword':
                updateCustomerKeywordStatus(3);
                break;
            case 'active_keyword':
                updateCustomerKeywordStatus(1);
                break;
            case 'change_selected_optimizedGroup':
                change_selected_optimizedGroup();
                break;
            case 'change_selected_machineGroup':
                change_selected_machineGroup();
                break;
            case 'change_selected_bearPawNumber':
                change_selected_bearPawNumber();
                break;
            case 'batch_modify':
                batch_modify();
                break;
            case 'batch_delete':
                batch_delete();
                break;
            default:
                break;
        }
    });

    element.on('tab(keywordTab)', function (data) {
        let d = data.elem.context.dataset;
        $('#type').val(d.type);
        $('#terminalType').val(d.terminal);
        active['reload'].call(this);
    });

    function updateCustomerKeywordStatus(status) {
        //获取选中数据

        var uuidArr = get_selected_uuid_arr();

        if (uuidArr.length <= 0) {
            show_layer_msg('请选择要操作的词', 5);

            return
        }
        if (status === 0) {
            msg = '确认要暂停选中的关键字吗';
        } else {
            msg = '确认要上线选中的关键字吗';
        }
        layer.confirm(msg, {icon: 3, title: '暂停所选'}, function (index) {
            var postData = {};
            postData.uuids = uuidArr;
            postData.status = status;
            $.ajax({
                url: '/internal/customerKeyword/updateCustomerKeywordStatus2',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result.code === 200) {
                        show_layer_msg('操作成功', 6, true);
                    } else {
                        show_layer_msg('操作失败', 5);
                    }
                },
                error: function () {
                    show_layer_msg('未知错误，请稍后重试', 5);
                }
            });
            layer.close(index);
        });
    }

    function change_current_optimizedGroup() {
        layer.prompt({
            formType: 3,
            value: '',
            title: '新优化组',
            // area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入新优化组！', 5, null, 1000);
                    return;
                }
                // layer.confirm("确定修改当前词的优化组吗", {icon: 3, title: '修改优化组'}, function (index) {
                var postData = formToJsonObject('searchForm');
                postData.targetOptimizeGroupName = value;
                $.ajax({
                    url: '/internal/customerKeyword/updateOptimizeGroupName2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            show_layer_msg('操作成功', 6, true);
                        } else {
                            show_layer_msg('操作失败', 5);
                        }
                    },
                    error: function () {
                        show_layer_msg('未知错误，请稍后重试', 5);
                    },
                    complete: function () {
                        layer.close(index);
                    }
                });
                layer.close(index2);
                // });

            }
        });
    }

    function change_selected_optimizedGroup() {
        //获取选中数据
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            show_layer_msg('请选择要操作的词', 5);
            return
        }
        layer.prompt({
            formType: 3,
            value: '',
            title: '新优化组',
            area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入新优化组！', 5, null, 1000);
                    return;
                }
                // layer.confirm("确定修改选中词的优化组吗", {icon: 3, title: '修改优化组'}, function (index) {
                var postData = {};
                postData.uuids = uuidArr;
                postData.terminalType = $('#terminalType').val();
                postData.targetOptimizeGroupName = value;
                $.ajax({
                    url: '/internal/customerKeyword/updateOptimizeGroupName2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            show_layer_msg('操作成功', 6, true);
                        } else {
                            show_layer_msg('操作失败', 5);
                        }
                    },
                    error: function () {
                        show_layer_msg('未知错误，请稍后重试', 5);
                    },
                    complete: function () {
                        layer.close(index);
                    }

                });
                layer.close(index2);
                // });

            }
        });
    }

    function change_current_machineGroup() {
        layer.prompt({
            formType: 3,
            value: '',
            title: '新机器分组',
            area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入新机器分组！', 5, null, 1000);
                    return;
                }
                // layer.confirm("确定修改当前词的机器分组吗", {icon: 3, title: '修改机器分组'}, function (index) {
                var postData = formToJsonObject('searchForm');
                postData.targetMachineGroup = value;
                $.ajax({
                    url: '/internal/customerKeyword/updateMachineGroup2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            show_layer_msg('操作成功', 6, true);
                        } else {
                            show_layer_msg('操作失败', 5);
                        }
                    },
                    error: function () {
                        show_layer_msg('未知错误，请稍后重试', 5);
                    },
                    complete: function () {
                        layer.close(index);
                    }
                });
                layer.close(index2);
                // });
            }
        });
    }

    function change_selected_machineGroup() {
        //获取选中数据
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            show_layer_msg('请选择要操作的词', 5);
            return
        }
        layer.prompt({
            formType: 3,
            value: '',
            title: '新机器分组',
            area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入新机器分组！', 5, null, 1000);
                    return;
                }

                var postData = {};
                postData.uuids = uuidArr;
                postData.terminalType = $('#terminalType').val();
                postData.targetMachineGroup = value;
                $.ajax({
                    url: '/internal/customerKeyword/updateMachineGroup2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            show_layer_msg('操作成功', 6, true);
                        } else {
                            show_layer_msg('操作失败', 5);
                        }
                    },
                    error: function () {
                        show_layer_msg('未知错误，请稍后重试', 5);
                    }
                });
                layer.close(index2);


            }
        });
    }

    function change_current_bearPawNumber() {
        layer.prompt({
            formType: 3,
            value: '',
            title: '新熊掌号',
            area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入新熊掌号！', 5, null, 1000);
                    return;
                }

                var postData = formToJsonObject('searchForm');
                postData.targetBearPawNumber = value;
                $.ajax({
                    url: '/internal/customerKeyword/updateBearPawNumber2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            show_layer_msg('操作成功', 6, true);
                        } else {
                            show_layer_msg('操作失败', 5);
                        }
                    },
                    error: function () {
                        show_layer_msg('未知错误，请稍后重试', 5);
                    }
                });
                layer.close(index2);


            }
        });
    }

    function change_selected_bearPawNumber() {
        //获取选中数据
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            show_layer_msg('请选择要操作的词', 5);
            return
        }

        layer.prompt({
            formType: 3,
            value: '',
            title: '新熊掌号',
            area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入新熊掌号！', 5, null, 1000);
                    return;
                }

                var postData = {};
                postData.uuids = uuidArr;
                postData.terminalType = $('#terminalType').val();
                postData.targetBearPawNumber = value;
                $.ajax({
                    url: '/internal/customerKeyword/updateBearPawNumber2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            show_layer_msg('操作成功', 6, true);
                        } else {
                            show_layer_msg('操作失败', 5);
                        }
                    },
                    error: function () {
                        show_layer_msg('未知错误，请稍后重试', 5);
                    }
                });
                layer.close(index2);

            }
        });
    }

    function batch_delete() {
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            show_layer_msg('请选择要操作的词', 5);
            return false;
        }
        layer.confirm("确定删除选中词吗", {icon: 3, title: '删除选中词'}, function (index) {
            var postData = {};
            postData.uuids = uuidArr;
            postData.deleteType = 'byUuids';
            $.ajax({
                url: '/internal/customerKeyword/deleteCustomerKeywords2',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result.code === 200) {
                        show_layer_msg('操作成功', 6, true);
                    } else {
                        show_layer_msg('操作失败', 5);
                    }
                },
                error: function () {
                    show_layer_msg('未知错误，请稍后重试', 5);
                },
                complete: function () {
                    layer.close(index);
                }
            });
        });
    }

    window.toCustomerKeyword = function (customerUuid, contactPerson) {
        // console.log(customerUuid,contactPerson);
        let businessType = $('#type').val();
        let terminalType = $('#terminalType').val();
        let url = '/internal/customerKeyword/toCustomerKeywords/' + businessType + '/' + terminalType + '/' + customerUuid;
        let tit = contactPerson + '--关键字列表';
        updateOrNewTab(url, tit, customerUuid);
    }
});

function updateOrNewTab(url, tit, id) {
    var update = false;
    var contentIframe = ("<iframe src='" + url + "' lay-id='" + id
        + "'frameborder='0' scrolling='yes' width='100%' height='100%'></iframe>");
    parent.layui.$('.layui-tab-title li').each(function () {
        if (id === this.getAttribute('lay-id')) {
            update = true;
        }
    });
    if (!update) {
        parent.layui.element.tabAdd('ok-tab', {
                title: '<i class="layui-icon">&#xe648;</i> ' + tit,
                content: contentIframe,
                id: id
            }
        );
    }
    parent.layui.element.tabChange('ok-tab', id)
}

