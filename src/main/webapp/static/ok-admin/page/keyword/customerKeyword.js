var sign = false;
// 进度条加载提示
NProgress.start();
window.onload = function () {
    NProgress.done();
};
// layui相关

let show = true;

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

function show_more_operation() {
    let operationContent = document.getElementById('operationContent');
    let rightDirection = document.getElementById('rightDirection');
    let downDirection = document.getElementById('downDirection');
    if (show) {
        operationContent.style.display = 'block';
        downDirection.style.display = 'inline-block';
        rightDirection.style.display = 'none';
    } else {
        operationContent.style.display = 'none';
        downDirection.style.display = 'none';
        rightDirection.style.display = 'inline-block';
    }
    show = !show;
}

layui.use(['element', 'table', 'form', 'jquery', 'laydate', 'okLayer', 'layer', 'common'], function () {
    let element = layui.element;
    let table = layui.table;
    let form = layui.form;
    let $ = layui.jquery;
    let laydate = layui.laydate;
    let okLayer = layui.okLayer;
    let common = layui.common;
    let layer = layui.layer;
    let qzList = '<option value="">请选择整站</option>';
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
        init_searchEngine();
        getQzSettings();
        if (qzBusiness === 'qz') {
            if_from_qz(layui);
            showQzSettings();
        }
        get_keywords(common.formToJsonObject('searchForm'));
    }

    function showQzSettings() {
        document.getElementById('qzUuid').parentElement.style.display = 'inline-block';
    }

    function hideQzSettings() {
        document.getElementById('qzUuid').parentElement.style.display = 'none';
    }

    function getQzSettings() {
        let uuid = document.getElementById('customerUuid').value;
        $.ajax({
            url: '/internal/qzsetting/getQzSettingByCustomer/' + uuid,
            dataType: 'json',
            async: false,
            type: 'get',
            success: function (res) {
                console.log(res);
                if (res.code === 200) {
                    $("#qzUuid").empty();
                    $("#qzUuid").append('<option value="">请选择整站</option>');
                    $("#qzUuid").append('<option value="-1">未关联整站数据</option>');
                    $.each(res.data, function (index, item) {
                        if (item.uuid + '' === qzUuid) {
                            $('#qzUuid').append('<option value="' + item.uuid + '" selected>' + item.domain + '</option>');// 下拉菜单里添加元素
                        } else {
                            $('#qzUuid').append('<option value="' + item.uuid + '">' + item.domain + '</option>');// 下拉菜单里添加元素
                        }
                        qzList += '<option value="' + item.uuid + '">' + item.domain + '</option>';
                    });
                    form.render("select");
                }
            }
        });
    }

    function init_keyword_type() {
        $.ajax({
            url: '/internal/common/getBusinessTypeByUserRole',
            dataType: 'json',
            async: false,
            type: 'get',
            success: function (res) {
                if (res.code === 200) {
                    $.each(res.data, function (index, item) {
                        let businessItem = item.split("#");
                            $('#tabItem').append(
                                '<li data-type="' + businessItem[0] + '" data-terminal="PC" lay-id="'+businessItem[0]+'PC">' + businessItem[1] + '电脑</li>' +
                                '<li data-type="' + businessItem[0] + '" data-terminal="Phone" lay-id="'+businessItem[0]+'Phone">' + businessItem[1] + '手机</li>');
                    });
                    form.render("select");

                    let keyTab = $('#type').val() + $('#terminalType').val();
                    element.tabChange('keywordTab', keyTab);
                }
            }
        });
    }

    element.on('tab(keywordTab)', function (data) {
        let d = data.elem.context.dataset;
        $('#type').val(d.type);
        $('#terminalType').val(d.terminal);
        if (d.type === 'qz') {
            showQzSettings();
        } else {
            hideQzSettings();
        }
        active['reload'].call(this);
    });

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
                    '<option value="">搜索引擎</option>');
                $.each(data.data, function (index, item) {
                    if (item === searchEngine){
                        $('#searchEngine').append('<option value="' + item + '" selected>' + item + '</option>');// 下拉菜单里添加元素
                    }else {
                        $('#searchEngine').append('<option value="' + item + '">' + item + '</option>');// 下拉菜单里添加元素
                    }
                });
                form.render("select");
            }
        });
    }

    function get_keywords(whereCondition) {
        if (!whereCondition.optimizeGroupNameLike) {
            whereCondition.optimizeGroupNameLike = '';
        }
        let keywordTable = table.render({
            elem: '#keywordTable',
            method: 'post',
            url: '/internal/customerKeyword/getCustomerKeywords',
            limit: 50,
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
                {field: 'keyword', title: '关键字', sort: true, width: '150', align: 'left'},
                {field: 'url', title: '链接', width: '140', align: 'left'},
                {field: 'bearPawNumber', title: '熊掌号', align: 'left', width: '100'},
                {field: 'title', title: '标题', width: '240', align: 'left'},
                {field: 'currentIndexCount', title: '指数', sort: true, align: 'left', width: '80', templet: '#indexCountTpl'},
                {field: 'initialPosition', title: '初始排名', align: 'left', width: '80'},
                {field: 'currentPosition', title: '现排名', sort: true, align: 'left', width: '80', templet: '#currentPositionTpl'},
                {field: 'searchEngine', title: '搜索引擎', align: 'left', width: '80'},
                {field: 'collectMethod', title: '收费方式', align: 'center', width: '80', templet: '#collectMethodTpl'},
                {field: 'optimizePlanCount', title: '要刷', align: 'left', width: '80'},
                {field: 'optimizedCount', title: '已刷', sort: true, align: 'left', width: '80'},
                {field: 'invalidRefreshCount', title: '无效', sort: true, align: 'left', width: '60', hide: true},
                {field: 'startOptimizedTime', title: '开始优化日期', align: 'center', width: '100', hide: true},
                {
                    field: 'lastOptimizeDateTime', title: '最后优化时间', align: 'center', width: '100', hide: true, templet: function (d) {
                        if (d.lastOptimizeDateTime) {
                            return layui.util.toDateString(d.lastOptimizeDateTime, 'yyyy-MM-dd HH:mm:ss')
                        }
                        return '';
                    }
                },
                {field: 'status', title: '状态', align: 'center', width: '80', templet: '#statusTpl'},
                {field: 'failedCause', title: '失败原因', align: 'left', width: '80',},
                {field: 'optimizeGroupName', title: '优化分组', align: 'left', width: '80'},
                {field: 'machineGroup', title: '机器分组', align: 'left', width: '80'},
                {field: 'remarks', title: '备注', align: 'left', width: '80', hide: true, templet: '#remarksTpl'},
                {title: '操作', align: 'center', width: '120', templet: '#operationTpl'}
            ]],
            height: 'full-110',
            done: function (res, curr, count) {
                jz();
            }
        });

    }

    function jz() {
        let tables = document.getElementsByTagName('table');
        let t_h = tables[2].offsetHeight || tables[2].clientHeight || tables[2].scrollHeight;
        let t_p_h = tables[2].parentElement.offsetHeight || tables[2].parentElement.clientHeight || tables[2].scrollHeight;
        let t_w = tables[1].offsetWidth || tables[1].clientWidth || tables[1].scrollWidth;
        let t_p_w = tables[1].parentElement.offsetWidth || tables[1].parentElement.clientWidth || tables[1].scrollWidth;
        if (t_h > t_p_h) {
             document.getElementsByClassName('layui-table-header')[0].classList.add('details-header');
        } else {
            if (t_w === t_p_w) {
                let ths = document.getElementsByClassName('layui-table-header')[0].getElementsByTagName('th');
                ths[ths.length - 1].style.borderRight = '0';
            }
            document.getElementsByClassName('layui-table-header')[0].classList.remove('details-header');
        }
    }

    form.on('select(businessType)', function (data) {
        active['reload'].call(this);
    });

    form.on('select(terminalType)', function (data) {
        active['reload'].call(this);
    });
    // 监听表头鼠标按下事件
    $(document).on('mousedown', 'thead', function (e) {
            jz();
        }
    );
    //监听工具条
    let active = {
        reload: function () {
            show = true;
            let condition = common.formToJsonObject('searchForm');
            if (!condition.optimizeGroupNameLike) {
                condition.optimizeGroupNameLike = '';
            }
            table.reload('keywordTable', {
                where: condition,
                page: {
                    curr: 1 //从第一页开始
                }
            });
        },
    };


    function get_selected_uuid_arr() {
        let checkStatus = table.checkStatus('keywordTable')
            , data = checkStatus.data;
        let uuidArr = [];
        $.each(data, function (index, item) {
            uuidArr.push(item.uuid);
        });
        return uuidArr;
    }

    form.on("submit(search)", function (data) {
        if (!data.field.noPosition){
            data.field.noPosition = '';
        }
        if (!data.field.optimizeGroupNameLike) {
            data.field.optimizeGroupNameLike = '';
        }
        data.field = common.jsonObjectTrim(data.field);
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
        let data = obj.data, event = obj.event;
        switch (event) {
            case 'add_customer_keyword':
                add_customer_keyword();
                break;
            case 'upd_customer_keyword_form_qz':
                upd_customer_keyword_form_qz();
                break;
            case 'change_current_optimizePlanCount':
                change_current_optimizePlanCount(1);
                break;
            case 'change_select_optimizePlanCount':
                change_select_optimizePlanCount();
                break;
            case 'update_belong_customer':
                batchUpdateBelongUser();
                break;
            case 'batch_update':
                batch_update_ByUuids();
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
            case 'upload_keyword_full':
                upload_keyword('SuperUserFull');
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
            case 'clear_all_by_condition_fail_reason':
                clear_current_fail_reason('cleanSelect');
                break;
            case 'clear_select_fail_reason':
                clear_select_fail_reason();
                break;
            default:
                break;
        }
    });

    function clear_current_fail_reason() {
        layer.confirm('确定清空当前词的失败原因吗？', function (index) {
            commit_csfr(common.formToJsonObject('searchForm'), index);
        });
    }

    function clear_select_fail_reason() {
        //获取选中数据
        let uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的词');
            return;
        }
        layer.confirm('确定清空选中词的失败原因吗？', function (index) {
            let postData = {};
            postData.uuids = uuidArr;
            postData.terminalType = $('#terminalType').val();
            commit_csfr(postData, index);
        });
    }

    function commit_csfr(p, index) {
        $.ajax({
            url: '/internal/customerKeyword/clearFailReason',
            data: JSON.stringify(p),
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
                common.showFailMsg('网络异常请稍后再试');
            },
            complete: function () {
                layer.close(index);
            }
        });
    }

    function batchUpdateBelongUser (){
        let uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的关键字');
            return false;
        }

        let entryType = $('#type').val();
        let terminalType = $('#terminalType').val();
        let data ={};
        data.uuids = uuidArr;
        data.entryType = entryType;
        data.terminalType = terminalType;
        okLayer.open("首页 / 客户列表 / 修改客户所属", "/internal/customerKeyword/toUpdateBelongCustomer", "40%", "50%", function(layero){
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                let pageConf = common.formToJsonObject('searchForm');
                get_keywords(pageConf);
                sign = false;
            }
        });
    }

    function add_customer_keyword() {
        let customerUuid = $('#customerUuid').val();
        let type = $('#type').val();
        let terminalType = $('#terminalType').val();
        let data = {};
        data.customerUuid = customerUuid;
        data.type = type;
        data.terminalType = terminalType;
        data.uuid = null;
        let url = '/internal/customerKeyword/toCustomerKeywordAdd';
        okLayer.open("关键字管理 / 客户关键字 / 添加关键字", url, "60%", "90%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                active['reload'].call(this);
                sign = false;
            }
        })
    }

    let html = '                                    <div><form class="layui-form layui-form-pane" id="justQzUuid"><div class="update-qz-from">'
        + '                                            <label class="layui-form-label">客户整站列表</label>'
        + '                                            <div class="layui-input-block">'
        + '                                                <select name="qzSettingUuid" id="qzSettingUuid">'
        + '                                                 {0}'
        + '                                                </select>'
        + '                                            </div>'
        + '                                         </form></div></div>';
    function upd_customer_keyword_form_qz() {
        let uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的关键字');
            return false;
        }
        layer.open({
            type: 1,
            title: '更改整站关联',
            area: ['680px', '500px'],
            content: html.replace('{0}', qzList),//注意，如果str是object，那么需要字符拼接。
            btn: ['确定', '取消'],
            btn1: function (index) {
                let v = common.formToJsonObject('justQzUuid').qzSettingUuid;
                console.log(v);
                if (!v) {
                    common.showFailMsg("请选择一个站点");
                    return false;
                }
                let postData = {customerKeywordUuids: uuidArr, qzSttingUuid: v};
                $.ajax({
                    url: '/internal/customerKeyword/updCustomerKeywordFormQz',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            common.showSuccessMsg('保存成功', function () {
                                active['reload'].call(this);
                            });
                        } else {
                            common.showFailMsg('保存失败');
                        }
                    },
                    error: function () {
                        common.showFailMsg('网络异常请稍后再试');
                    },
                    complete: function () {
                        layer.close(index);
                    }
                });
            },
            btn2: function (index) {
                layer.close(index);
            }, success: function () {
                form.render("select")
            }
        });
    }

    function upload_keyword(excelType) {
        let qzUuid = $('#qzUuid').val();
        let type = $('#type').val();
        if (type === 'qz' && (!qzUuid || qzUuid === '-1')) {
            common.showFailMsg("请选择要上传关键字的整站");
            return null;
        }
        let customerUuid = $('#customerUuid').val();
        let terminalType = $('#terminalType').val();
        let data = {};
        data.customerUuid = customerUuid;
        data.type = type;
        data.terminalType = terminalType;
        data.excelType = excelType;
        data.qzUuid = qzUuid;
        let url = '/internal/customerKeyword/toUploadKeywords';
        let msg = excelType === 'SuperUserSimple' ? '简化版' : '完整版';
        okLayer.open("关键字管理 / 客户关键字 / Excel上传关键字(" + msg + ")", url, "30%", "25%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                active['reload'].call(this);
                sign = false;
            }
        })
    }

    function download_keyword_info() {
        let domain =  $("#qzUuid").find("option:selected").text();
        let type = $('#type').val();
        var msg = "确定导出所有关键字吗？";
        if (type === 'qz') {
            if (!domain || domain === '-1') {
                msg = "确定导出所有站点的关键字排名吗？"
            } else {
                msg = '确定导出' + domain + '站点的关键字排名吗？';
            }
        }
        layer.confirm(msg, {icon: 3, title: '导出结果'}, function (index) {
            let searchCriteriaArray = $('#searchForm').serializeArray();
            let downloadKeywordForm = $('#downloadKeywordForm');
            $.each(searchCriteriaArray, function (idx, val) {
                downloadKeywordForm.find("#"+val.name+"Hidden").val(val.value === '' ? null : val.value);
            });
            $("#downloadKeywordForm").submit();
        });
    }

    function download_keyword_url() {
        $("#customerUuidKU").val($("#customerUuid").val());
        $("#terminalTypeKU").val($("#terminalType").val());
        $("#keywordUrlForm").submit();
    }

    function change_current_bearPawNumber() {
        layer.prompt({
            formType: 3,
            value: '',
            title: '新熊掌号',
            area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                let index2 = index;
                let value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    common.showFailMsg('请输入新熊掌号！');
                    return;
                }
                let postData = common.formToJsonObject('searchForm');
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
                    },
                    complete: function () {
                        layer.close(index);
                    }
                });
                layer.close(index2);
            }
        });
    }

    function change_select_bearPawNumber() {
        //获取选中数据
        let uuidArr = get_selected_uuid_arr();
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
                let index2 = index;
                let value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    common.showFailMsg('请输入新熊掌号！');
                    return;
                }

                let postData = {};
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
                            common.showSuccessMsg('操作成功', active['reload'].call(this));
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

    function change_current_optimizedGroup() {
        layer.prompt({
            formType: 3,
            value: '',
            title: '新优化组',
            // area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                let index2 = index;
                let value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    common.showFailMsg('请输入新优化组！');
                    return;
                }
                let postData = common.formToJsonObject('searchForm');
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
                            common.showSuccessMsg('操作成功', active['reload'].call(this));
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

    function change_select_optimizedGroup() {
        //获取选中数据
        let uuidArr = get_selected_uuid_arr();
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
                let index2 = index;
                let value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    common.showFailMsg('请输入新优化组！');
                    return;
                }

                let postData = {};
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
                            common.showSuccessMsg('操作成功', active['reload'].call(this));
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
                        common.showSuccessMsg('操作成功', active['reload'].call(this));
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
                        common.showSuccessMsg('操作成功', active['reload'].call(this));
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

    function change_select_keyowrd_status(status) {
        let postData = {};
        let uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的词');
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
                        common.showSuccessMsg('操作成功', active['reload'].call(this));
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
                    common.showFailMsg('请选择要操作的词');
                    return;
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
                    common.showFailMsg('请选择要操作的词');
                    return;
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
                            common.showSuccessMsg('操作成功', active['reload'].call(this));
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

    function batch_delete(deleteType) {
        let customerUuid = $("#customerUuid").val();
        let terminalType = $("#terminalType").val();
        let msg = deleteType === 'byEmptyTitleAndUrl' ? '确定要删除标题和网址为空的词吗' : '确定要删除标题为空的词吗';
        layer.confirm(msg, {icon: 3, title: '删除词'}, function (index) {
            let postData = {};
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
                        common.showSuccessMsg('操作成功', active['reload'].call(this));
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

    function change_current_optimizePlanCount() {
        layer.prompt({
            formType: 3,
            value: '',
            title: '新刷量',
            // area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                let index2 = index;
                let value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    common.showFailMsg('请输入新刷量！');
                    return;
                } else if (!/^\d+$/.test(value)) {
                    common.showFailMsg('请输入正确数字！');
                    return;
                }
                let postData = common.formToJsonObject('searchForm');
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
                            common.showSuccessMsg('操作成功', active['reload'].call(this));
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

    function change_select_optimizePlanCount() {
        //获取选中数据
        let uuidArr = get_selected_uuid_arr();
        let data = {};
        data.uuids = uuidArr;
        data.customerUuid = $('#customerUuid').val();
        data.terminalType = $('#terminalType').val();
        data.entryType = $('#type').val();
        okLayer.open("首页 / 客户关键字 / 修改刷量", "/internal/customerKeyword/toUpdateOptimizePlanCount", "40%", "50%", function(layero){
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                let pageConf = common.formToJsonObject('searchForm');
                get_keywords(pageConf);
                sign = false;
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
                let index2 = index;
                let value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    common.showFailMsg('请输入新机器分组！');
                    return;
                }
                    let postData = common.formToJsonObject('searchForm');
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
                                common.showSuccessMsg('操作成功', active['reload'].call(this));
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

    function change_select_machineGroup() {
        //获取选中数据
        let uuidArr = get_selected_uuid_arr();
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
                let index2 = index;
                let value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    common.showFailMsg('请输入新机器分组！');
                    return;
                }

                let postData = {};
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
                            common.showSuccessMsg('操作成功', active['reload'].call(this));
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

    function change_searchEngine(type) {
        let postData = {};
        switch (type) {
            case 'current':
                postData = common.formToJsonObject('searchForm');
                break;
            case 'select':
                let uuidArr = get_selected_uuid_arr();
                if (uuidArr.length <= 0) {
                    common.showFailMsg('请选择要操作的词');
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
        let uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的词');
            return false;
        }
        let postData = {};
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
                    form.render('select')
                    layer.open({
                        type: 1,
                        title: '新客户',
                        area: ['400', '600'],
                        content: $('#searchEngineDiv'),//注意，如果str是object，那么需要字符拼接。
                        btn: ['确定', '取消'],
                        btn1: function (index, layero) {
                            active['reload'].call(this);
                            layer.close(index);
                        },
                        btn2: function (index, layero) {
                            layer.close(index);
                        }
                    });
                } else {
                    common.showFailMsg('操作失败');
                }
            },
            error: function () {
                common.showFailMsg('未知错误，请稍后重试');
            }
        });
    }

    function batch_delete_byUuids() {
        let uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的词');
            return false;
        }
        layer.confirm("确定删除选中词吗", {icon: 3, title: '删除选中词'}, function (index) {
            let postData = {};
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
                        common.showSuccessMsg('操作成功', active['reload'].call(this));
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

    function download_daily_report() {
        let customerUuid = $('#customerUuid').val();
        let url = '/internal/dailyReport/downloadSingleCustomerReport2/' + customerUuid;
        window.open(url, '_blank');
    }

    //监听工具条
    table.on('tool(tableFilter)', function (obj) {
        let data = obj.data;
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
                        common.showSuccessMsg('操作成功', active['reload'].call(this));
                    } else {
                        common.showFailMsg('操作失败');
                    }
                },
                error: function () {
                    common.showFailMsg('操作失败');

                }
            });
            layer.close(index);
        });
    }

    // 编辑表格获得表格数据
    function editKeyword(data) {
        let postData = {};
        postData.uuid = data.uuid;
        postData.customerUuid = $('#customerUuid').val();
        postData.type = $('#type').val();
        postData.terminalType = $('#terminalType').val();
        let url = '/internal/customerKeyword/toCustomerKeywordAdd';
        okLayer.open("关键字统计 / 客户关键字 / 修改关键字", url, "60%", "90%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(postData);
        }, function () {
            if (sign) {
                active['reload'].call(this);
                sign = false;
            }
        })
    }

    // 编辑表格获得表格数据
    function batch_update_ByUuids() {
        let uuidArr = get_selected_uuid_arr();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的词');
            return false;
        }
        let postData = {};
        postData.uuids = uuidArr;
        postData.customerUuid = $('#customerUuid').val();
        postData.type = $('#type').val();
        postData.terminalType = $('#terminalType').val();
        let url = '/internal/customerKeyword/toCustomerKeywordBatchUpdate';
        okLayer.open("关键字统计 / 客户关键字 / 关键字批量设置(<span style='color: red'>请将需要修改的字段点击标记为红色</span>)", url, "60%", "95%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(postData);
        }, function () {
            if (sign) {
                active['reload'].call(this);
                sign = false;
            }
        });
    }

//layui.use 结束
});


