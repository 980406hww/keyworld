var sign = false;
// 进度条加载提示
NProgress.start();
window.onload = function () {
    NProgress.done();
};
// layui相关

var show = true;

function show_more_operation() {
    let operationContent = document.getElementById('operationContent');
    if (show) {
        operationContent.style.display = 'block';
    } else {
        operationContent.style.display = 'none';
    }
    show = !show;
}

layui.use(['element', 'table', 'form', 'jquery', 'laydate', 'okLayer', 'upload',
    'layer'], function () {
    var element = layui.element;
    var table = layui.table;
    var form = layui.form;
    var $ = layui.jquery;
    var laydate = layui.laydate;
    var okLayer = layui.okLayer;
    var upload = layui.upload;
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
        // init_belong_user();
        init_searchEngine();
        get_keywords(formToJsonObject('searchForm'));
    }

    function init_keyword_type() {
        $.ajax({
            url: '/internal/customerKeyword/getKeywordTypeByUserRole',
            dataType: 'json',
            async: false,
            type: 'get',
            success: function (res) {
                if (res.code === 200) {
                    // $("#type").empty();
                    $.each(res.data, function (index, item) {
                        let businessItem = item.split("#");
                        $('#type').append('<option value="' + businessItem[0] + '">' + businessItem[1] + '</option>');// 下拉菜单里添加元素
                    });
                    $('#type').val($('#typeTmp').val());
                    $('#terminalType').val($('#terminalTypeTmp').val());
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
                $("#userName").append('<option value="">请选择所属用户</option>');
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
                $("#searchEngine").append(
                    '<option value="">请选择搜索引擎</option>');
                $.each(data.data, function (index, item) {
                    $('#searchEngine').append(
                        '<option value="' + item + '">' + item
                        + '</option>');// 下拉菜单里添加元素
                });
                form.render("select");
            }
        });
    }

    function get_keywords(whereCondition) {
        var keywordTable = table.render({
            elem: '#keywordTable',
            method: 'post',
            url: '/internal/customerKeyword/getCustomerKeywords',
            limit: 25,
            limits: [10, 25, 50, 75, 100, 500, 1000],
            page: true,
            size: 'sm',
            id: 'keywordTable',
            even: true,//隔行背景
            // toolbar: true,
            where: whereCondition,
            toolbar: "#toolbarTpl",
            // defaultToolbar: ['filter', 'print', 'exports'], 对应列筛选 打印 导出
            defaultToolbar: ['filter'],
            contentType: 'application/json',
            cols: [[
                {filed: 'uuid', type: 'checkbox', width: '35'},
                {field: 'keyword', title: '关键字', width: '150'},
                {field: 'url', title: '链接', width: '140'},

                {field: 'bearPawNumber', title: '熊掌号', align: 'center', width: '100'},
                {field: 'title', title: '标题', width: '220'},
                {field: 'currentIndexCount', title: '指数', align: 'center', width: '80', templet: '#indexCountTpl'},
                {field: 'initialPosition', title: '初始排名', align: 'center', width: '80'},
                {field: 'currentPosition', title: '现排名', align: 'center', width: '80'},
                {field: 'searchEngine', title: '搜索引擎', align: 'center', width: '80'},
                {field: 'collectMethod', title: '收费方式', align: 'center', width: '80', templet: '#collectMethodTpl'},
                {field: 'optimizePlanCount', title: '要刷', align: 'center', width: '80'},
                {field: 'optimizedCount', title: '已刷', align: 'center', width: '80'},
                {field: 'invalidRefreshCount', title: '无效', align: 'center', width: '60', hide: 'true'},
                {field: 'status', title: '状态', align: 'center', width: '80', templet: '#statusTpl'},
                {field: 'failedCause', title: '失败原因', align: 'center', width: '100',},
                {field: 'optimizeGroupName', title: '优化分组', align: 'center', width: '80'},
                {field: 'machineGroup', title: '机器分组', align: 'center', width: '80'},
                {title: '操作', align: 'center', width: '120', templet: '#operationTpl'}
            ]],
            height: 'full-108',

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

    form.on('select(businessType)', function (data) {
        active['reload'].call(this);
    });

    form.on('select(terminalType)', function (data) {
        active['reload'].call(this);
    });
    // 监听表头鼠标按下事件
    $(document).on('mousedown', 'thead', function (e) {
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
            show = true;
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
            case 'add_customer_keyword':
                add_customer_keyword();
                break;
            case 'change_current_optimizePlanCount':
                change_current_optimizePlanCount(1);
                break;
            case 'change_select_optimizePlanCount':
                change_select_optimizePlanCount();
                break;
            case 'delete_same_keyword':
                delete_same_keyword();
                break;

            case 'batch_delete':
                batch_delete_byUuids();
                break;
            case 'more_operation':
                show_more_operation();
                break;
            case 'upload_keyword_simple':
                upload_keyword('SuperUserSimple');
                break;
            case 'download_keyword_simple_excel':
                download_keyword_excel('SuperUserSimple');
                break;
            case 'upload_keyword_full':
                upload_keyword('SuperUserFull');
                break;
            case 'download_keyword_full_excel':
                download_keyword_excel('SuperUserFull');
                break;
            case 'download_daily_report':
                download_daily_report();
                break;
            case 'download_keyword_info':
                download_keyword_info();
                break;
            case 'change_current_bearPawNumber':
                change_current_bearPawNumber();
                break;
            case 'change_select_bearPawNumber':
                change_select_bearPawNumber();
                break;
            case 'download_keyword_url':
                download_keyword_url();
                break;
            case 'delete_keyword_by_nullTitle_and_nullUrl':
                batch_delete('byEmptyTitleAndUrl');
                break;
            case 'delete_keyword_by_nullTitle':
                batch_delete('byEmptyTitle');
                break;
            case 'change_current_optimizedGroup':
                change_current_optimizedGroup();
                break;
            case 'change_select_optimizedGroup':
                change_select_optimizedGroup();
                break;
            case 'change_current_searchEngine':
                change_searchEngine('current');
                break;
            case 'change_select_searchEngine':
                change_searchEngine('select');
                break;
            case 'change_current_machineGroup':
                change_current_machineGroup();
                break;
            case 'change_select_machineGroup':
                change_select_machineGroup();
                break;
            case 'down_off_all_keyword':
                change_all_keyowrd_status(3);
                break;
            case 'down_off_select_keyword':
                change_select_keyowrd_status(3);
                break;
            case 'pause_all_keyword':
                change_all_keyowrd_status(0);
                break;
            case 'pause_select_keyword':
                change_select_keyowrd_status(0);
                break;
            case 'active_all_keyword':
                change_all_keyowrd_status(1);
                break;
            case 'active_select_keyword':
                change_select_keyowrd_status(1);
                break;
            case 'recollect_customer_keyword_title':
                change_title('recollectAll');
                break;
            case 'recollect_select_keyword_title':
                change_title('recollectSelect');
                break;
            case 'clean_customer_keyword_title':
                change_title('cleanAll');
                break;
            case 'clean_select_keyword_title':
                change_title('cleanSelect');
                break;
            default:
                break;
        }
    });

    function add_customer_keyword() {
        let customerUuid = $('#customerUuid').val();
        let type = $('#typeTmp').val();
        let terminalType = $('#terminalType').val();
        let url = '/internal/customerKeyword/toCustomerKeywordAdd/' + type
            + '/' + terminalType + '/' + customerUuid;
        okLayer.open("关键字管理 / 客户客户关键字 / 添加关键字", url, "60%", "90%",
            function (layero) {

            }, function () {
                if (sign) {
                    active['reload'].call(this);
                    sign = false;
                }
            })
    }

    function upload_keyword(excelType) {
        let customerUuid = $('#customerUuid').val();
        let type = $('#type').val();

        let terminalType = $('#terminalType').val();
        let url = '/internal/customerKeyword/toUploadKeywords/' + type + '/'
            + terminalType + '/' + customerUuid + '/' + excelType;
        let msg = excelType === 'SuperUserSimple' ? '简化版' : '完整版';
        okLayer.open("关键字管理 / 客户关键字 / Excel上传关键字(" + msg + ")", url, "30%",
            "25%",
            function (layero) {
            }, function () {
                if (sign) {
                    active['reload'].call(this);
                    sign = false;
                }
            })
    }

    function download_keyword_excel(excelType) {
        let url = excelType === 'SuperUserSimple' ? '/SuperUserSimpleKeywordList.xls' : '/SuperUserFullKeywordList.xls';
        window.open(url, '_blank');
    }

    function download_keyword_info() {
        let postData = formToJsonObject('searchForm');
        $.ajax({
            url: '/internal/customerKeyword/downloadCustomerKeywordInfo2',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            async: false,
            type: 'post',
            success: function (res) {
                if (res.code === 200) {
                    show_layer_msg('导出成功', 6, false);
                } else {
                    show_layer_msg('未知错误！', 5);
                }
            }
        });
    }

    function download_keyword_url() {
        $("#customerUuidKU").val($("#customerUuid").val());
        $("#terminalTypeKU").val($("#terminalTypeTmp").val());
        $("#keywordUrlForm").submit();
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
                layer.confirm("确定修改当前词的熊掌号吗", {icon: 3, title: '修改熊掌号'}, function (index) {
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
                        },
                        complete: function () {
                            layer.close(index);
                        }
                    });
                    layer.close(index2);

                });
            }
        });
    }

    function change_select_bearPawNumber() {
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
                layer.confirm("确定修改选中词的熊掌号吗", {icon: 3, title: '修改熊掌号'}, function (index) {
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
                        },
                        complete: function () {
                            layer.close(index);
                        }
                    });
                    layer.close(index2);
                });
            }
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
                layer.confirm("确定修改当前词的优化组吗", {icon: 3, title: '修改优化组'}, function (index) {
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
                });

            }
        });
    }

    function change_select_optimizedGroup() {
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
                layer.confirm("确定修改选中词的优化组吗", {icon: 3, title: '修改优化组'}, function (index) {
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
                });

            }
        });
    }

    function change_all_keyowrd_status(status) {
        let postData = {};
        postData.terminalType = $("#terminalType").val();
        postData.type = $("#type").val();
        postData.targetStatus = status;
        postData.customerUuid = $('#customerUuid').val();
        let msg = '';
        switch (status) {
            case 0:
                msg = '是否暂停所有关键字';
                break;
            case 3:
                msg = '是否下架所有关键字';
                break;
            case 1:
                msg = '是否激活所有关键字';
                break;
            default:
                return;
        }
        layer.confirm(msg, {icon: 3, title: '更新关键字状态'}, function (index) {
            $.ajax({
                url: '/internal/customerKeyword/changeCustomerKeywordStatus3',
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

    function delete_same_keyword() {
        let postData = {};
        postData.terminalType = $("#terminalType").val();
        postData.type = $("#type").val();
        postData.customerUuid = $('#customerUuid').val();

        layer.confirm('确定删除重复关键字吗', {icon: 3, title: '删除重复关键字'}, function (index) {
            $.ajax({
                url: '/internal/customerKeyword/deleteDuplicateCustomerKeywords2',
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

    function change_select_keyowrd_status(status) {
        let postData = {};
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            show_layer_msg('请选择要操作的词', 5);
            return
        }
        postData.uuids = uuidArr;
        postData.targetStatus = status;

        let msg = '';
        switch (status) {
            case 0:
                msg = '是否暂停所选关键字';
                break;
            case 3:
                msg = '是否下架所选关键字';
                break;
            case 1:
                msg = '是否激活所选关键字';
                break;
            default:
                return;
        }
        layer.confirm(msg, {icon: 3, title: '更新关键字状态'}, function (index) {
            $.ajax({
                url: '/internal/customerKeyword/changeCustomerKeywordStatus3',
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

    function change_title(changeType) {
        let postData = {};
        postData.type = $('#type').val();
        postData.terminalType = $('#terminalType').val();
        postData.cleanType = changeType;
        let uuidArr;
        let msg = '';
        switch (changeType) {
            case 'recollectAll':
                postData.customerUuid = $('#customerUuid').val();
                msg = '确认要重新采集自动导入的关键词的标题吗';
                break;
            case 'recollectSelect':
                uuidArr = get_selected_uuid_arr();
                if (uuidArr.length <= 0) {
                    show_layer_msg('请选择要操作的词', 5);
                    return
                }
                postData.uuids = uuidArr;
                msg = '确认要重新采集所选关键词的标题吗';
                break;
            case 'cleanAll':
                postData.customerUuid = $('#customerUuid').val();

                msg = '确认要清空客户下所有关键词的标题吗';
                break;

            case 'cleanSelect':
                uuidArr = get_selected_uuid_arr();
                if (uuidArr.length <= 0) {
                    show_layer_msg('请选择要操作的词', 5);
                    return
                }
                postData.uuids = uuidArr;
                msg = '确认要清空所选关键词的标题吗';
                break;
            default:
                return;
        }
        layer.confirm(msg, {icon: 3, title: '更新标题'},
            function (index) {
                $.ajax({
                    url: '/internal/customerKeyword/cleanTitle2',
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

    function batch_delete(deleteType) {
        let customerUuid = $("#customerUuid").val();
        let terminalType = $("#terminalType").val();
        let msg = deleteType === 'byEmptyTitleAndUrl' ? '确定要删除标题和网址为空的词吗' : '确定要删除标题为空的词吗';
        layer.confirm(msg, {icon: 3, title: '删除词'}, function (index) {
            var postData = {};
            postData.customerUuid = customerUuid;
            postData.terminalType = terminalType;
            postData.deleteType = deleteType;
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

    function change_current_optimizePlanCount() {
        layer.prompt({
            formType: 3,
            value: '',
            title: '新刷量',
            // area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入新刷量！', 5, null, 1000);
                    return;
                }
                layer.confirm("确定修改当前词的刷量吗", {icon: 3, title: '修改刷量'},
                    function (index) {
                        var postData = formToJsonObject('searchForm');
                        postData.targetOptimizePlanCount = value;
                        $.ajax({
                            url: '/internal/customerKeyword/updateOptimizePlanCount2',
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
                    });

            }
        });
    }

    function change_select_optimizePlanCount() {
        //获取选中数据
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            show_layer_msg('请选择要操作的词', 5);
            return
        }
        layer.prompt({
            formType: 3,
            value: '',
            title: '新刷量',
            area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入新刷量！', 5, null, 1000);
                    return;
                }
                layer.confirm("确定修改选中词的刷量吗", {icon: 3, title: '修改刷量'},
                    function (index) {
                        var postData = {};
                        postData.uuids = uuidArr;
                        postData.terminalType = $('#terminalType').val();
                        postData.type = $('#type').val();
                        postData.targetOptimizePlanCount = value;
                        $.ajax({
                            url: '/internal/customerKeyword/updateOptimizePlanCount2',
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
                    });

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
                layer.confirm("确定修改当前词的机器分组吗", {icon: 3, title: '修改机器分组'}, function (index) {
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

                });
            }
        });
    }

    function change_select_machineGroup() {
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
                layer.confirm("确定修改选中词的机器分组吗", {icon: 3, title: '修改机器分组'}, function (index) {
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
                        },
                        complete: function () {
                            layer.close(index);
                        }
                    });
                    layer.close(index2);

                });
            }
        });
    }

    function change_searchEngine(type) {
        let postData = {};
        switch (type) {
            case 'current':
                postData = formToJsonObject('searchForm');
                break;
            case 'select':
                var uuidArr = get_selected_uuid_arr();
                if (uuidArr.length <= 0) {
                    show_layer_msg('请选择要操作的词', 5);
                    return
                }
                postData.uuids = uuidArr;
                postData.terminalType = $("#terminalType").val();
                break;
            default:
                return;
        }
        okLayer.open("首页 / 客户列表 / 修改用户", "/internal/customerKeyword/toChangeKeywordsSearchEngine", "40%", "40%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(postData);
        }, function () {
            if (sign) {
                active['reload'].call(this);
                sign = false;
            }
        })

    }

    function change_select_customer() {
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            show_layer_msg('请选择要操作的词', 5);
            return false;
        }
        var postData = {};
        // postData.uuids = uuidArr;
        postData.terminalType = $('#terminalType').val();
        postData.type = $('#type').val();
        $.ajax({
            url: '/internal/customer/getActiveCustomers2',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'GET',
            success: function (res) {
                if (res.code === 200) {
                    let searchEngineTpl = $("#searchEngineFor");
                    searchEngineTpl.empty();
                    let htm = '<select name="customerUuid" id="customerUuid">';
                    $.each(res.data, function (index, item) {

                        searchEngineTpl.append('<option value="' + item.uuid + '">'
                            + item.contactPerson + '</option>');// 下拉菜单里添加元素
                    });
                    // searchEngineTpl.val($("#contactPerson").val())
                    console.log($("#contactPerson").val())
                    form.render('select')
                    layer.open({
                        type: 1,
                        title: '新客户',
                        area: ['400', '600'],
                        content: $('#searchEngineDiv'),//注意，如果str是object，那么需要字符拼接。
                        btn: ['确定', '取消'],
                        btn1: function (index, layero) {
                            active['reload'].call(this);
                            layer.close(index)
                        },
                        btn2: function (index, layero) {
                            // active['reload'].call(this);
                            layer.close(index)
                        }
                    });
                    // show_layer_msg('操作成功', 6, true);
                } else {
                    show_layer_msg('操作失败', 5);
                }
            },
            error: function () {
                show_layer_msg('未知错误，请稍后重试', 5);
            }
        });
    }

    function batch_delete_byUuids() {
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

    function download_daily_report() {
        let customerUuid = $('#customerUuid').val();
        let url = '/internal/dailyReport/downloadSingleCustomerReport2/' + customerUuid;
        window.open(url, '_blank');
    }

    //监听工具条
    table.on('tool(tableFilter)', function (obj) {
        var data = obj.data;
        switch (obj.event) {
            case 'edit':
                editKeyword(data);
                break;
            case 'del':
                delOneKeyword(data);
                break;
        }
    });

    //删除单个客户
    function delOneKeyword(data) {
        layer.confirm('真的删除该关键字吗', function (index) {
            $.ajax({
                url: '/internal/customerKeyword/deleteCustomerKeyword2/' + data.uuid,
                // data: data.uuid,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result.code === 200) {
                        layer.msg('操作成功', {
                            icon: 6,
                            time: 2000 //2秒关闭（如果不配置，默认是3秒）
                        }, function () {
                            active['reload'].call(this);
                        });
                    } else {
                        layer.msg('操作失败', {icon: 5});
                    }
                },
                error: function () {
                    layer.msg('操作失败', {icon: 5});

                }
            });
            layer.close(index);
        });
    }

// 编辑表格获得表格数据
    function editKeyword(data) {
        let customerUuid = $('#customerUuid').val();
        let type = $('#typeTmp').val();
        let terminalType = $('#terminalType').val();
        let url = '/internal/customerKeyword/toCustomerKeywordAdd/' + type + '/' + terminalType + '/' + customerUuid;
        okLayer.open("关键字统计 / 客户关键字 / 修改关键字", url, "60%", "90%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(data.uuid);
        }, function () {
            if (sign) {
                active['reload'].call(this);
                sign = false;
            }
        })
    }

//layui.use 结束
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
                title: tit,
                content: contentIframe,
                id: id
            }
        );
    }
    parent.layui.element.tabChange('ok-tab', id)
}

