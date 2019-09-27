var sign = false;

// layui相关
layui.use(['element', 'form', 'jquery', 'laypage', 'okLayer', 'layer'], function () {
    var element = layui.element;
    var form = layui.form;
    var $ = layui.jquery;
    var layer = layui.layer;
    var okLayer = layui.okLayer;
    var laypage = layui.laypage;

    initLayPage();

    function initLayPage(pageConf) {
        if (!pageConf) {
            pageConf = {};
            pageConf.limit = 25;
            pageConf.page = 1;
        }
        if(!pageConf.page){
            pageConf.page = 1;
        }
        if (!pageConf.limit) {
            pageConf.limit = 25;
        }
        $.ajax({
            url: '/internal/customer/getCustomers',
            data: JSON.stringify(pageConf),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 20000,
            type: 'POST',
            success: function (result) {
                laypage.render({
                    elem: 'page_nav',
                    count: result.count,
                    curr: pageConf.page,
                    limit: pageConf.limit,
                    limits: [10, 25, 50, 75, 100, 500, 1000],
                    first: '首页',
                    last: '尾页',
                    layout: ['count', 'prev', 'page', 'next', 'limit'],
                    jump: function (obj, first) {
                        if (!first) {
                            pageConf.page = obj.curr;
                            pageConf.limit = obj.limit;
                            initLayPage(pageConf);
                        }
                    }
                });
                init_data(result.data);
                form.render()
            },
            error: function () {
                layer.msg('获取用户失败，请稍后再试', {icon: 5});
            }
        });
    }

    function init_data(data) {
        $("#data_list").html('');
        $.each(data, function (index, obj) {
            let item = '<div class="layui-col-md6 layui-col-sm6">' +
                '   <div class="layadmin-contact-box">' +
                '       <div class="layui-col-md5 layui-col-sm6">';
            item += '           <h3 class="layadmin-title">' +
                '               <input type="checkbox" name="checkItem" value="' + obj.uuid + '" status="'+obj.status+'"lay-skin="primary" >' +
                '               <strong>' + obj.contactPerson + '</strong>' + '<i class="layui-icon layui-icon-right"></i> ' +
                obj.type +
                '           </h3>';
            item += '           <div class="layadmin-address">' +
                '                   <strong>联系方式</strong>' +
                '                   <br>' +
                '                   电话:' + obj.telphone +
                '                   <br>' +
                '                   微信:' + obj.wechat +
                '                   <br>' +
                '                   QQ:' + obj.qq +
                '               </div>';
            item += '           <div class="layadmin-address other_info">' +
                '                   <strong>其他信息</strong>' +
                '                   <p class="skip" >所属用户:' + obj.loginName +'</p>' +
                '                   <p class="skip" >客户状态:' + generate_customer_status(obj.status) +'</p>' +
                '                   <p class="skip" title="'+obj.remark+'">是否产生日报表:<span id="dr'+obj.uuid+'">' + generate_customer_daily_report(obj.uuid, obj.status, obj.dailyReportIdentify) + '</span></p>' +
                '               </div>';
            item += '           <div class="layadmin-address ">' +
                '                   <strong>备注</strong>' +
                '                   <p class="skip" title="'+obj.remark+'">销售备注:' + obj.saleRemark + '</p>' +
                '                   <p class="skip" title="'+obj.remark+'">备注:' + obj.remark + '</p>' +
                '               </div>' +
                '       </div>';
            item += '   <div class="layui-col-md6  layui-col-sm6">' +
                '       <h3 class="layadmin-title">' +
                '           <strong>拥有业务</strong>' +
                '       </h3>';
            let customerBusinessList = obj.customerBusinessList;
            if (customerBusinessList !== null && customerBusinessList.length > 0) {
                var contactPerson = obj.contactPerson.replace(/\s+/g, "");
                $.each(obj.customerBusinessList, function (index, tmp) {
                    let url = '', title = '', id = '';
                    if (tmp === 'keyword') {
                        url = '/internal/customerKeyword/searchCustomerKeywords/' + obj.uuid;
                        title = contactPerson + '-关键字信息';
                        id = contactPerson + '-关键字信息';
                        item += '<div class="layadmin-address">' +
                            '<strong>' +
                            '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>单词业务</a>' +
                            '</strong>' +
                            '<br>';
                        $.ajax({
                            url: '/internal/customerKeyword/getCustomerKeywordsCount/' + obj.uuid,
                            dataType: 'json',
                            type: 'get',
                            async: false,
                            success: function (res) {
                                if (res.code === 200) {
                                    item += generate_keyword_info(res.data)
                                } else {
                                    item += '暂无数据'
                                }
                                item += '</div>';
                            }
                        });
                    } else if (tmp === 'qzsetting') {
                        url = '/internal/qzsetting/toQZSetttingsWithCustomerUuid/' + obj.uuid;
                        title = contactPerson + '-全站信息';
                        id = contactPerson + '-全站信息';
                        item += '<div class="layadmin-address"><strong>' +
                            '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>全站业务</a>' +
                            '</strong>' +
                            '<br>';
                        $.ajax({
                            url: '/internal/qzsetting/getQZSettingsCount/' + obj.uuid,
                            dataType: 'json',
                            type: 'get',
                            async: false,
                            success: function (res) {
                                if (res.code === 200) {
                                    item += generate_qzsetting_info(res.data)
                                } else {
                                    item += '暂无数据'
                                }
                                item +=
                                    '</div>';

                            }
                        });
                    } else if (tmp === 'fm') {
                        url = '/internal/qzsetting/toFMWithCustomerUuid/' + obj.uuid;
                        title = contactPerson + '-负面信息';
                        id = contactPerson + '-负面信息';
                        item += '<div class="layadmin-address"><strong>' +
                            '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>负面业务</a>' +
                            '</strong>' +
                            '<br>' +
                            '待做---占位符' +
                            '</div>';
                    }
                });
            } else {
                item += '<div class="layadmin-address">\n' +
                    '                <strong>暂无业务</strong>\n' +
                    '    </div>';

            }
            item += '</div>';
            item += '<div class="layui-col-md1 layui-col-sm6 operation">\n' +
                '       <h3 class="layadmin-title">\n' +
                '           <strong>操作</strong>\n' +
                '       </h3>' +
                '       <div class="layadmin-address">\n' +
                '           <a href="javascript:void(0)" class="caller-fr" onclick=editCustomer("' + obj.uuid + '")>' +
                '               修改\n' +
                '               <i class="layui-icon layui-icon-edit"></i>\n' +
                '           </a>\n' +
                '       </div>' +
                '       <div class="layadmin-address">\n' +
                '           <a href="javascript:void(0)" class="caller-fr" onclick=delOneCustomer("' + obj.uuid + '")>' +
                '                   删除\n' +
                '               <i class="layui-icon layui-icon-close"></i>\n' +
                '           </a>\n' +
                '       </div>';
            item += '</div>';
            item += '</div>';
            $("#data_list").append(item)
        })
    }

    function formToJson(data) {
        data = data.replace(/&/g, "\",\"");
        data = data.replace(/=/g, "\":\"");
        data = "{\"" + data + "\"}";
        return data;
    }

    form.on('checkbox(checkAll)', function (data) {
        if ($(this)[0].checked) {
            $("input[name='checkItem']").each(function () {
                this.checked = true;
            })
        } else {
            $('input[name="checkItem"]').each(function () {
                this.checked = false;
            })
        }
        form.render('checkbox')

    });

    form.on("submit(search)", function (data) {
        var pageConf = data.field;
        pageConf.limit = 25;
        pageConf.page = 1;
        if (!open) {
            showCondition();
        }
        initLayPage(pageConf);
        return false;
    });

    function show_layer_msg(msg, icon, title, status) {
        layer.msg(msg, {
            icon: icon,
            title: title === undefined ? null : title,
            anim: 5,
            time: 2000,
            isOutAnim: false
        }, function () {
            if (status) {
                active['reload'].call(this);
            }
        });
    }

    //获取选中客户uuid[]
    function get_select_uuids(){
        var uuidArr = [];
        $('input[name="checkItem"]:checked').each(function () {
            uuidArr.push(this.value)
        });
        return uuidArr;
    }

    //获取选中的激活的客户uuid[]
    function get_select_active_uuids(){
        var uuidArr = [];
        $('input[name="checkItem"]:checked').each(function () {
            if (this.getAttribute("status") === '1') {
                uuidArr.push(this.value);
            }
        });
        return uuidArr;
    }



    //删除单个客户
    window.delOneCustomer = function (uuid) {
        layer.confirm('真的删除该客户吗', function (index) {
            var uuidArr = [];
            uuidArr.push(uuid);
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
                            let pageConf = $.parseJSON(formToJson(decodeURIComponent($("#searchForm").serialize(), true)));
                            initLayPage(pageConf)
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

    //批量删除
    window.batchDelete = function () {
        let uuidArr = get_select_uuids();
        if (uuidArr.length <= 0) {
            show_layer_msg("请选择要删除的客户！！", 5);
            return false;
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
                            let pageConf = $.parseJSON(formToJson(decodeURIComponent($("#searchForm").serialize(), true)));
                            initLayPage(pageConf)
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

    // 添加客户
    window.toAddCustomer = function () {
        okLayer.open("首页 / 客户列表 / 添加用户", "/internal/customer/toCustomersAdd", "60%", "90%", null, function () {
            if (sign) {
                let pageConf = $.parseJSON(formToJson(decodeURIComponent($("#searchForm").serialize(), true)));
                initLayPage(pageConf);
                sign = false;
            }
        });
    };

    // 编辑表格获得表格数据
    window.editCustomer = function (uuid) {
        okLayer.open("首页 / 客户列表 / 修改用户", "/internal/customer/toCustomersAdd", "60%", "90%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(uuid);
        }, function () {
            if (sign) {
                let pageConf = $.parseJSON(formToJson(decodeURIComponent($("#searchForm").serialize(), true)));
                initLayPage(pageConf);
                sign = false;
            }
        })
    };

    //更新关键字状态
    window.changeCustomerKeywordStatus = function (customerUuid, status) {
        if (status === '0') {
            msg = "确定暂停所有关键字吗?"
        } else if (status === '1') {
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
                            time: 1000
                        },function () {
                            let pageConf = $.parseJSON(formToJson(decodeURIComponent($("#searchForm").serialize(), true)));
                            initLayPage(pageConf);
                        });
                    } else {
                        layer.msg('操作失败', {icon: 5, time: 1000});
                    }
                },
                error: function () {
                    layer.msg('操作失败', {icon: 5, time: 1000});

                }
            });
            layer.close(index);
        });
    };

    //改变日报表值
    window.changeCustomerDailyReportIdentify = function (uuid, status, oldIdentify, newIdentify) {
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
                    layer.msg('操作成功', {icon: 6, time:1000});
                    $('#dr' + uuid).html(generate_customer_daily_report(uuid, status, newIdentify))
                }
            },
            error: function () {
                layer.msg('操作失败', {icon: 5, time:1000});
                $('#dr' + uuid).html(generate_customer_daily_report(uuid, status, oldIdentify))
            }
        });

    };

    //触发所选客户日报表
    window.batchUpdateDailyReport= function () { //获取选中数据
        let uuidArr = get_select_active_uuids();
        if (uuidArr.length <= 0) {
            layer.msg('请选择要操作的激活的客户', {icon: 5});
            return false;
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
                            time: 2000 //2秒关闭（如果不配置，默认是3秒）
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

});

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

function generate_customer_status(status){
    let stat ='<span style="color: red;">暂停</span>';
    if(status===1){
        stat = '<span style="color: green;">激活</span>';
    }
    return stat;
}

function generate_customer_daily_report(uuid, status, oldIdentify){
    uuid = uuid +'';
    status = status+'';
    oldIdentify = oldIdentify+'';
    let newIdentify = '1';
    let stat ='否';
    if(oldIdentify === '1'){
        stat = '是';
        newIdentify = '0'
    }

    if (status === '1') {
        stat += ' | <a href="javascript:void(0)" onclick=changeCustomerDailyReportIdentify(\'' + uuid + '\',\'' + status + '\',\'' + oldIdentify + '\',\'' + newIdentify + '\')>修改状态</a>';
    }
    return stat;
}

function generate_keyword_info(data) {
    let htm = '';
    if (data.totalCount > 0) {
        htm += '<span>总数:' + data.totalCount + '&nbsp;&nbsp;&nbsp;&nbsp;</span>(';
        if (data.totalCount === data.activeCount) {
            htm += '<span style="color: green;">激活</span>' +
                '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + data.customerUuid + '","0")>暂停关键字</a>'
        } else if (data.totalCount > 0 && data.activeCount > 0) {
            htm += '<span style="color: yellowgreen;">部分暂停</span>' +
                '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + data.customerUuid + '","0")>暂停关键字</a>' +
                '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + data.customerUuid + '","1")>激活关键字</a>'
        } else {
            htm += '<span style="color: red;">暂停</span>' +
                '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + data.customerUuid + '","1")>激活关键字</a>'
        }
        htm += ')';
    } else {
        htm += '<a href="javascript:void(0)">暂无数据</a>'
    }
    return htm;
}

function generate_qzsetting_info(data) {
    let htm = '';
    if (data.totalCount > 0) {
        htm += '<span>总数:' + data.totalCount + '&nbsp;&nbsp;&nbsp;&nbsp;</span>(';
        if (data.totalCount === data.activeCount) {
            htm += '<span style="color: green;">激活</span>' +
                '|<a href="javascript:changeQZSettingRenewalStatus(\'' + data.customerUuid + '\', 0)">暂停全站</a>'
        } else if (data.totalCount > 0 && data.activeCount > 0) {
            htm += '<span style="color: yellowgreen;">部分暂停</span>' +
                '|<a href="javascript:changeQZSettingRenewalStatus(\'' + data.customerUuid + '\', 0)">暂停全站</a>' +
                '|<a href="javascript:changeQZSettingRenewalStatus(\'' + data.customerUuid + '\', 3)">下架全站</a>'
        } else {
            htm += '<span style="color: red;">暂停</span>';

        }
        htm += ')';
    } else {
        htm += '<a href="javascript:void(0)">暂无数据</a>'
    }
    return htm;
}

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





