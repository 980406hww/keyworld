var sign = false;
// 进度条加载提示
NProgress.start();
window.onload = function () {
    NProgress.done();
};

let open = true;

function showCondition() {
    let searchContent = document.getElementById('searchContent');
    if (open) {
        searchContent.style.display = 'block';
    } else {
        searchContent.style.display = 'none';
    }
    open = !open;
}

// layui相关
layui.use(['element', 'table', 'form', 'jquery', 'laydate', 'okLayer', 'layer','common'], function () {
    var element = layui.element;
    var table = layui.table;
    var form = layui.form;
    var $ = layui.jquery;
    var okLayer = layui.okLayer;
    var laydate = layui.laydate;
    var common = layui.common;

    //日期范围
    laydate.render({
        elem: '#gtCreateTime',
    });
    laydate.render({
        elem: '#ltCreateTime',
    });

    init_search(dataTmp);
    init_belong_user();
    init_searchEngine();
    init_optimizeStatus();

    function init_search(d) {
        if (d.type && d.terminal && d.status) {
            let statuses = document.getElementById('status').children;
            for (let i = 0; i < statuses.length; i++) {
                if (statuses[i].value === d.status) {
                    statuses[i].setAttribute('selected', '');
                }
            }
            init_keyword_type(d);
        } else {
            init_keyword_type();
        }
    }

    function init_optimizeStatus(){
        if (optimizeStatus !== ""){
            let optimizeStatuses = document.getElementById('optimizeStatus').children;
            for (let i = 0; i < optimizeStatuses.length; i++) {
                if (optimizeStatuses[i].value === optimizeStatus) {
                    optimizeStatuses[i].setAttribute('selected', '');
                }
            }
        }
    }

    function init_keyword_type(data) {
        $.ajax({
            url: '/internal/common/getBusinessTypeByUserRole',
            dataType: 'json',
            async: false,
            type: 'get',
            success: function (res) {
                if (res.code === 200) {
                    // $("#tabItem").empty();
                    let i = 0, one = 'pt';
                    $.each(res.data, function (index, item) {
                        let businessItem = item.split("#");
                        $('#tabItem').append(
                            '<li data-type="' + businessItem[0] + '" data-terminal="PC" lay-id="'+businessItem[0] +'PC">' + businessItem[1] + '电脑</li>' +
                            '<li data-type="' + businessItem[0] + '" data-terminal="Phone" lay-id="'+businessItem[0]+'Phone">' + businessItem[1] + '手机</li>');
                        if (i++ === 0) {
                            one = businessItem[0];
                        }
                    });
                    if (data) {
                        // $('#type').val(data.type);
                        // $('#terminalType').val(data.terminal);
                        element.tabChange('keywordTab', data.type+data.terminal);
                    } else {
                        let tabItem = document.getElementById('tabItem').children;
                        tabItem[0].classList.add('layui-this');
                        $('#type').val(one);
                        $('#terminalType').val(terminal225);
                        element.tabChange('keywordTab', one+terminal225);
                    }

                    form.render("select");
                }
            }
        });
    }

    function init_belong_user() {
        var userName= $("#userName").val();
        $.ajax({
            url: '/internal/user/getActiveUsersByAuthority',
            dataType: 'json',
            type: 'get',
            success: function (res) {
                if (res.code === 200) {
                    let data = res.data;
                    $("#userName").empty();
                    $("#userName").append('<option value="">所属用户</option>');
                    $.each(data, function (index, item) {
                     if(userName === item.loginName)
                        $('#userName').append(
                            '<option  selected value="' + item.loginName + '">'
                            + item.userName
                            + '</option>');// 下拉菜单里添加元素
                        else
                         $('#userName').append(
                             '<option value="' + item.loginName + '" >'
                             + item.userName
                             + '</option>');
                    });
                    if (belongUser !== '' || belongUser != null) {
                        $("#userName").val(belongUser)
                    }
                    form.render("select");
                } else {
                    common.showFailMsg('获取用户列表失败');
                }
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
                if (searchEngine != null || searchEngine != null) {
                    $("#searchEngine").val(searchEngine)
                }
                form.render("select");
            }
        });
    }

    function get_keywords(whereCondition) {
        if (!whereCondition.optimizeGroupNameLike) {
            whereCondition.optimizeGroupNameLike = '';
        }
        var keywordTable = table.render({
            elem: '#keywordTable',
            method: 'post',
            url: '/internal/customerKeyword/getKeywords',
            autoSort: false,
            limit: 50,
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
                {field: 'currentPosition', title: '现排名', width: '80', sort: true, templet: '#currentPositionTpl'},
                {field: 'searchEngine', title: '搜索引擎', width: '80'},
                {field: 'optimizeGroupName', title: '优化分组', width: '100'},
                {field: 'machineGroup', title: '机器分组', width: '100'},
                {field: 'city', title: '目标城市', width: '80', hide: true},
                {field: 'collectMethod', title: '收费方式', width: '80', templet: '#collectMethodTpl' },
                {field: 'optimizePlanCount', title: '要刷', width: '60',},
                {field: 'optimizedCount', title: '已刷', width: '60'},
                {field: 'invalidRefreshCount', title: '无效', sort: true, width: '60', hide: true },
                {field: 'invalidDays', title: '无法操作天数', align: 'left', width: '60', hide: true},
                {field: 'noEffectConsecutiveDays', title: '上不去天数', align: 'left', width: '100', hide: true},
                {field: 'status', title: '状态', width: '60', templet: '#statusTpl' },
                {field: 'optimizeStatus', title: '操作状态', width: '60', hide: true},
                {field: 'paymentStatus', title: '付费状态', width: '80', hide: true },
                {field: 'remarks', title: '备注', width: '100', hide: true, templet: '#remarksTpl'},
                {field: 'failedCause', title: '失败原因', width: '80', hide: true},
                {field: 'existsTimestamp', title: '特殊优化', hide: true, align: 'left', width: '80', templet:"#showExistsTimestamp"},
                {field: 'capturePositionFailIdentify', title: '抓排名失败标识', width: '100', hide: true}
            ]],
            height: 'full-110',
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
        let postData = common.formToJsonObject('searchForm');
        postData.orderBy = obj.field;
        postData.orderMode = obj.type === 'desc' ? '0' : '1';
        if (!postData.optimizeGroupNameLike) {
            postData.optimizeGroupNameLike = '';
        }
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

    form.verify({
        num: [
            /(^$)|(^[0-9]?$)|(^[1-9][0-9]+$)/,
            "请输入合适的正整数、如：1、2、3"
        ],
        telPhone: [
            /(^$)|(^(13[0-9]|14[5-9]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[13589])\d{8}$)/,
            "请输入正确格式手机号码"
        ]
    });

    //监听工具条
    var active = {
        reload: function () {
            if (table.index >= 1) {
                let postData = common.formToJsonObject('searchForm');
                if (!postData.noReachStandardDays) {
                    postData.noReachStandardDays = '';
                }
                if (!postData.optimizeGroupNameLike) {
                    postData.optimizeGroupNameLike = '';
                }
                table.reload('keywordTable', {
                    where: postData,
                    page: {
                        curr: 1 //从第一页开始
                    }
                });
            }
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
        if (!data.field.noReachStandardDays){
            data.field.noReachStandardDays = '';
        }
        data.field = common.jsonObjectTrim(data.field);
        get_keywords(common.formToJsonObject('searchForm'));
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
            case 'batch_down_keyword':
                batchDownKeyword('BatchDown');
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

    function init_noReachStandard(data){
        $.ajax({
            url: '/internal/customerKeyword/searchCustomerKeywordForNoReachStandard2',
            data: JSON.stringify(data),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 20000,
            type: 'POST',
            success: function (res) {
                if (res.code === 200){
                    let data = res.data;
                    var noReachStandardDiv = $("#noReachStandardDiv");
                    noReachStandardDiv.find("a").eq(0).text("超过30天(" + data.thirtyDaysNoReachStandard + ")");
                    noReachStandardDiv.find("a").eq(1).text("超过15天(" + data.fifteenDaysNoReachStandard + ")");
                    noReachStandardDiv.find("a").eq(2).text("超过7天(" + data.sevenDaysNoReachStandard + ")");
                    var searchForm = $("#searchForm");
                    searchForm.find("#thirtyDaysNoReachStandard").val(data.thirtyDaysNoReachStandard);
                    searchForm.find("#fifteenDaysNoReachStandard").val(data.fifteenDaysNoReachStandard);
                    searchForm.find("#sevenDaysNoReachStandard").val(data.sevenDaysNoReachStandard);
                }else{
                    common.showFailMsg('未达标统计失败');
                }

            },
            error: function () {
                common.showFailMsg('未达标统计失败');
            }
        });
    }

    function updateCustomerKeywordStatus(status) {
        //获取选中数据

        var uuidArr = get_selected_uuid_arr();

        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的词');

            return
        }
        let msg;
        let title;
        switch (status) {
            case 0:
                msg = '确认要暂停选中的关键字吗';
                title = '暂停所选';
                break;
            case 1:
                msg = '确认要上线选中的关键字吗';
                title = '激活选中的关键字吗';
                break;
            default:
                msg = '确认要下架选中的关键字吗';
                title = '下架所选';
                break;
        }

        layer.confirm(msg, {icon: 3, title: title}, function (index) {
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
                        common.showSuccessMsg('操作成功', function () {
                            active['reload'].call(this);
                        });
                    } else {
                        common.showFailMsg('操作失败');
                    }
                },
                error: function () {
                    common.showFailMsg('未知错误，请稍后重试');
                }
            });
            layer.close(index);
        });
    }

    function batchDownKeyword(excelType) {
        let type = $('#type').val();
        let terminalType = $('#terminalType').val();
        let data = {};
        data.type = type;
        data.terminalType = terminalType;
        data.excelType = excelType;
        let url = '/internal/customerKeyword/toDownKeywords';
        okLayer.open("关键字管理 / 客户关键字 / Excel下架关键字", url, "30%", "40%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                active['reload'].call(this);
                sign = false;
            }
        }, '100px')
    }

    function change_selected_optimizedGroup() {
        //获取选中数据
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的词');
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
                    common.showFailMsg('请输入新优化组！');
                    return;
                }
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
                            common.showSuccessMsg('操作成功', function () {
                                active['reload'].call(this);
                            });
                        } else {
                            common.showFailMsg('操作失败');
                        }
                    },
                    error: function () {
                        common.showFailMsg('未知错误，请稍后重试');
                    },
                    complete: function () {
                        layer.close(index);
                    }

                });
                layer.close(index2);
            }
        });
    }

    function change_selected_machineGroup() {
        //获取选中数据
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的词');
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
                    common.showFailMsg('请输入新机器分组！');
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
                            common.showSuccessMsg('操作成功', function () {
                                active['reload'].call(this);
                            });
                        } else {
                            common.showFailMsg('操作失败');
                        }
                    },
                    error: function () {
                        common.showFailMsg('未知错误，请稍后重试');
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
            common.showFailMsg('请选择要操作的词');
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
                    common.showFailMsg('请输入新熊掌号！');
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
                            common.showSuccessMsg('操作成功', function () {
                                active['reload'].call(this);
                            });
                        } else {
                            common.showFailMsg('操作失败');
                        }
                    },
                    error: function () {
                        common.showFailMsg('未知错误，请稍后重试');
                    }
                });
                layer.close(index2);
            }
        });
    }

    function batch_delete() {
        var uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的词');
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
                        common.showSuccessMsg('操作成功', function () {
                            active['reload'].call(this);
                        });
                    } else {
                        common.showFailMsg('操作失败');
                    }
                },
                error: function () {
                    common.showFailMsg('未知错误，请稍后重试');
                },
                complete: function () {
                    layer.close(index);
                }
            });
        });
    }

    var existUserName;
    if (document.getElementById('userName')){
        existUserName = document.getElementById('userName').value;
    }else{
        existUserName = false;
    }

    if (document.getElementById('keyword').value || document.getElementById('optimizeGroupName').value ||
        document.getElementById('machineGroup').value || document.getElementById('invalidRefreshCount').value || existUserName) {
        $('#searchBtn').click();
    } else {
        get_keywords({init: 'init'});
    }

    window.toCustomerKeyword = function (customerUuid, contactPerson) {
        let businessType = $('#type').val();
        let terminalType = $('#terminalType').val();
        let url = '/internal/customerKeyword/toCustomerKeywords/' + businessType + '/' + terminalType + '/' + customerUuid;
        let tit = contactPerson + '--关键字列表';
        common.updateOrNewTab(url, tit, customerUuid);
    };

    window.showNoReachStandardKeyword = function(day){
        $('#noReachStandardDays').val(day);
        $('#searchBtn').click();
    }
});


