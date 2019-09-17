var sign = false;
// 进度条加载提示
// NProgress.start();
// window.onload = function () {
//     NProgress.done();
// };
// layui相关
layui.use(['element', 'table', 'form', 'jquery', 'laydate', 'tablePlug', 'okLayer', 'layer'], function () {
    var element = layui.element;
    var table = layui.table;
    var form = layui.form;
    var $ = layui.jquery;
    var laydate = layui.laydate;
    var okLayer = layui.okLayer;
    var layer = layui.layer;
    var tablePlug = layui.tablePlug; //表格插件

    tablePlug.enableTableFixedScroll(true);
    var customerTable = table.render({
        elem: '#customerTable',
        method: 'post',
        url: '/internal/customer/getCustomers',
        limit: 25,
        limits: [10, 25, 50, 75, 100, 500, 1000],
        page: true,
        autoSort: false,
        size: 'sm',
        id: 'customerTable',
        even: true,//隔行背景
        // toolbar: true,
        toolbar: "#toolbarTpl",
        // defaultToolbar: ['filter', 'print', 'exports'], 对应列筛选 打印 导出
        defaultToolbar: ['filter'],
        contentType: 'application/json',
        cols: [[
            /*templet: '#toDateTime'*/
            {filed: 'uuid', type: 'checkbox', fixed: "left", width: '32'},
            {field: 'uuid', title: '基本信息', fixed: "left", width: '640', templet: '#baseInfo'},
            {field: 'uuid', title: '拥有业务', width: '640', templet: '#businessInfo'},
            {field: 'uuid', title: '操作',  fixed: "right", width: '180', templet: '#operationTpl',}
        ]],
        height: 'full-110',

        done: function (res, curr, count) {
            autoFixed($(this.elem[0]).next());
        }
    });

    //监听排序事件
    table.on('sort(tableFilter)', function(obj){
        let data = $.parseJSON(formToJson($("#searchForm").serialize()));
        data.orderBy = obj.field;
        data.orderMode = obj.type === 'asc' ? 1 : 0;

        table.reload('customerTable', {
            where: data,
            page: {
                curr: 1
            }
        });
    });

    //监听工具条
    table.on('tool(tableFilter)', function (obj) {
        var data = obj.data;
        switch (obj.event) {
            case 'edit':
                editCustomer(data);
                break;
            case 'del':
                delOneCustomer(data);
                break;
        }
    });

    table.on('toolbar(tableFilter)',function(obj){
        var data = obj.data,event = obj.event;
        switch (event) {
            case 'batchDeleteCustomer':
                batchDeleteCustomer();
                break;
            case 'batchUpdateDailyReport':
                batchUpdateDailyReport();
                break;
            case 'addUser':
                toAddCustomer();
                break
        }
    });

    var active = {
        reload: function () {
            table.reload('customerTable', {
                where: $.parseJSON(formToJson($("#searchForm").serialize())),
                page: {
                    curr: 1
                }
            });
            $("#searchContent").removeClass("layui-show")
        },
    };

    form.on("submit(search)", function (data) {
        table.reload('customerTable', {
            where: data.field,
            page: {
                curr: 1
            }
        });
        $("#searchContent").removeClass("layui-show");
        return false;
    });

    function formToJson(data) {
        data=data.replace(/&/g,"\",\"");
        data=data.replace(/=/g,"\":\"");
        data="{\""+data+"\"}";
        return data;
    }

    function batchDeleteCustomer() {
        var checkStatus = table.checkStatus('customerTable')
            , data = checkStatus.data;
        var uuidArr = [];
        $.each(data, function (index, item) {
            uuidArr.push(item.uuid);
        });
        if (uuidArr.length <= 0) {
            layer.msg('请选择要操作的客户', {icon: 5});
            return
        }
        layer.confirm("确定要删除所选客户吗", {icon: 3, title: '删除所选'}, function (index) {
            var postData = {};
            postData.uuids = uuidArr;
            $.ajax({
                url: '/internal/customer/deleteCustomers2',
                data: JSON.stringify(postData),
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
                            time: 2000
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

    function batchUpdateDailyReport() {
        var checkStatus = table.checkStatus('customerTable')
            , data = checkStatus.data;
        var uuidArr = [];
        $.each(data, function (index, item) {
            if (item.status === 1) {
                uuidArr.push(item.uuid);
            }
        });
        if (uuidArr.length <= 0) {
            layer.msg('请选择要操作的客户', {icon: 5});
            return
        }
        layer.confirm("确定更新所选客户日报表吗", {icon: 3, title: '更新日报表'}, function (index) {
            var postData = {};
            postData.uuids = uuidArr;
            $.ajax({
                url: '/internal/customer/updateCustomerDailyReportIdentify2',
                data: JSON.stringify(postData),
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
                            time: 2000
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

   /* $.ajax({
        url: '/internal/customer/getActiveUsers',
        dataType: 'json',
        type: 'get',
        success: function (data) {
            $("#loginName").empty();
            $("#loginName").append('<option value="">请选择所属用户</option>');
            $.each(data, function (index, item) {
                $('#loginName').append('<option value="' + item.loginName + '">' + item.userName + '</option>');
            });
            form.render("select");
        }
    });*/

    // 编辑表格获得表格数据
    function editCustomer(data) {
        okLayer.open("首页 / 客户列表 / 修改用户", "/internal/customer/toCustomersAdd", "60%", "90%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(data.uuid);
        },function(){
            if(sign){
                active['reload'].call(this);
                sign = false;
            }
        })
    }

    //删除单个客户
    function delOneCustomer(data) {
        layer.confirm('真的删除该客户吗', function (index) {
            var uuidArr = [];
            uuidArr.push(data.uuid);
            var postData = {};
            postData.uuids = uuidArr;
            $.ajax({
                url: '/internal/customer/deleteCustomers2',
                data: JSON.stringify(postData),
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

    $('#clearContactPerson').click(function () {
        $('#contactPerson').val('');
    });

    // 添加客户
    function toAddCustomer(){
        okLayer.open("首页 / 客户列表 / 添加用户", "/internal/customer/toCustomersAdd", "60%", "90%", null, function () {
            if(sign){
                active['reload'].call(this);
                sign = false;
            }
        });
    }

    //更新关键字状态
    window.changeCustomerKeywordStatus = function changeCustomerKeywordStatus(customerUuid, status) {
        if (status === 0) {
            msg = "确定暂停所有关键字吗?"
        } else if (status === 1) {
            msg = "确定激活所有关键字吗?"
        }
        layer.confirm(msg, {icon: 3, title: '关键字状态'}, function (index) {
            var data = {};
            data.customerUuid = customerUuid;
            data.status = status;
            $.ajax({
                url: '/internal/customerKeyword/changeCustomerKeywordStatus2',
                data: JSON.stringify(data),
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
    };

    //
    window.changeCustomerDailyReportIdentify = function changeCustomerDailyReportIdentify(uuid, status, oldIdentify,
                                                                                          newIdentify, self) {
        var select = $(self);
        if (status !== '1') {
            layer.msg('请激活客户', {icon: 5});
            return;
        }
        var postData = {};
        postData.customerUuid = uuid;
        postData.identify = newIdentify;
        $.ajax({
            url: '/internal/customer/changeCustomerDailyReportIdentify2',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(postData),
            success: function (result) {
                if (result.code === 200) {
                    layer.msg('操作成功', {icon: 6});
                }
            },
            error: function () {
                layer.msg('操作失败', {icon: 5});
                select.val(oldIdentify);
            }
        });
    };
    /**
     * 修正浮动栏高度
     * @param tableElem 表格显示div
     */
    function autoFixed(tableElem) {
        var $tableView = tableElem || $(".layui-table-view");
        var dataIndex ,trHeight;
        $tableView.each(function() {
            // 获取两侧浮动栏
            var $fixed = $(this).find(".layui-table-fixed");
            // 同步表头高度
            $fixed.find(".layui-table-header tr").height($(this).find(".layui-table-header tr").eq(0).height());
            // 遍历tr 修正浮动栏行高
            $(this).find(".layui-table-main tr").each(function() {
                dataIndex = $(this).attr("data-index");
                trHeight = $(this).css("height");
                $fixed.find("tr[data-index=" + dataIndex + "]").css("height", trHeight);
            })
        });
    }

    // 监听浏览器窗口大小变化
    var resizeTimer;
    $(window).resize(function() {
        if (resizeTimer) {
            clearTimeout(resizeTimer);
        }
        resizeTimer = setTimeout(function() {
                resizeTimer = null;
                autoFixed();
            },
            200);
    });

// 监听表头鼠标按下事件
    $(document).on('mousedown', 'thead',
        function (e) {
            var that = $(this);
            $(document).one('mouseup',
                function () {
                    autoFixed(that.parents('.layui-table-view'));
                })
        });

});


function updateOrNewTab(url, tit, id) {
    var update = false;
    var contentIframe = ("<iframe src='" + url + "' lay-id='" + id
        + "'frameborder='0' scrolling='yes' width='100%' height='100%'></iframe>");
    parent.layui.$('.layui-tab-title li').each(function () {
        if (id === this.getAttribute('lay-id')){
            update = true;
        }
    });
    if (!update){
        parent.layui.element.tabAdd('ok-tab', {
                title: tit,
                content: contentIframe,
                id: id
            }
        );
    }

    parent.layui.element.tabChange('ok-tab', id)
}

function generate_customer_business_td(data) {
    let html = '';
    let customerBusinessList = data.customerBusinessList;
    if (customerBusinessList !== null && customerBusinessList.length > 0) {

        for (let index in customerBusinessList) {
            var parm = {};
            let url = '', title = '', id = '';
            var contactPerson = data.contactPerson.replace(/\s+/g, "")
            if (customerBusinessList[index] === 'keyword') {
                url = '/internal/customerKeyword/searchCustomerKeywords/' + data.uuid;
                title = contactPerson + '-关键字信息';
                id = contactPerson + '-关键字信息';
                html += '<button type="button" class="layui-btn layui-btn-xs" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>关键字信息</button>'
            } else if (customerBusinessList[index] === 'qzsetting') {
                url = '/internal/qzsetting/toQZSetttingsWithCustomerUuid/' + data.uuid;
                title = contactPerson + '-全站信息';
                id = contactPerson + '-全站信息';
                html += '<button type="button" class="layui-btn layui-btn-xs" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>全站信息</button>'
            } else if (customerBusinessList[index] === 'fm') {
                url = '/internal/productKeyword/searchProductKeywordsByCustomerUuid/' + data.uuid;
                title = contactPerson + '-负面信息';
                id = contactPerson + '-负面信息';
                html += '<button type="button" class="layui-btn layui-btn-xs" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>负面信息</button>'
            }
        }
    }
    return html;
}

function generate_customer_business_td2(data) {
    let html = '';
    let customerBusinessList = data.customerBusinessList;
    if (customerBusinessList !== null && customerBusinessList.length > 0) {
        for (let index in customerBusinessList) {
            var parm = {};
            let url = '', title = '', id = '';
            var contactPerson = data.contactPerson.replace(/\s+/g, "");
            if (customerBusinessList[index] === 'keyword') {
                url = '/internal/customerKeyword/searchCustomerKeywords/' + data.uuid;
                title = contactPerson + '-关键字信息';
                id = contactPerson + '-关键字信息';

                html +='<div class="layui-row" >\n' +
                    '       <div class="layui-col-md5 td-border-right">关键字信息</div>\n' +
                    '       <div class="layui-col-md7 td-border-right">'+data.contactPerson+
                    '       </div>\n' +
                    '   </div>';
            } else if (customerBusinessList[index] === 'qzsetting') {
                url = '/internal/qzsetting/toQZSetttingsWithCustomerUuid/' + data.uuid;
                title = contactPerson + '-全站信息';
                id = contactPerson + '-全站信息';
                html +='<div class="layui-row" >\n' +
                    '       <div class="layui-col-md5 td-border-right">全站信息</div>\n' +
                    '       <div class="layui-col-md7 td-border-right">'+data.contactPerson+
                    '       </div>\n' +
                    '   </div>';
            } else if (customerBusinessList[index] === 'fm') {
                url = '/internal/productKeyword/searchProductKeywordsByCustomerUuid/' + data.uuid;
                title = contactPerson + '-负面信息';
                id = contactPerson + '-负面信息';
                html +='<div class="layui-row" >\n' +
                    '       <div class="layui-col-md5 td-border-right">负面信息</div>\n' +
                    '       <div class="layui-col-md7 td-border-right">'+data.contactPerson+
                    '       </div>\n' +
                    '   </div>';
            }
        }
    }
    return html;
}




