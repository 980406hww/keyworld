var sign = false;

// layui相关
layui.use(['element', 'table', 'form', 'jquery', 'laypage', 'tablePlug', 'okLayer', 'layer'], function () {
    var element = layui.element;
    // var table = layui.table;
    var form = layui.form;
    var $ = layui.jquery;
    var layer = layui.layer;
    var okLayer = layui.okLayer;
    // var tablePlug = layui.tablePlug; //表格插件
    var laypage = layui.laypage;
    // tablePlug.enableTableFixedScroll(true);//允许表格固定列滚动

    initLayPage();

    function initLayPage(pageConf) {
        if (!pageConf) {
            pageConf = {};
            pageConf.limit = 25;
            pageConf.page = 1;
        }
        $.ajax({
            url: '/internal/customer/getCustomers',
            data: JSON.stringify(pageConf),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
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
                    // console.log(result.data)
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
            let item =  '<div class="layui-col-md12 layui-col-sm6">' +
                        '   <div class="layadmin-contact-box">' +
                        '       <div class="layui-col-md5 layui-col-sm6">';
            item +=     '           <h3 class="layadmin-title">'+
                        '               <input type="checkbox" name="checkItem" value="'+obj.uuid+'"lay-skin="primary" >'+
                        '               <strong>'+obj.contactPerson+'</strong>'+'<i class="layui-icon layui-icon-right"></i> '+
                                        obj.type+
                        '           </h3>';
            item +=     '           <div class="layadmin-address">'+
                        '               <strong>联系方式</strong>'+
                        '               <br>'+
                        '               电话:'+obj.telphone+
                        '               <br>'+
                        '               微信:'+ obj.wechat+
                        '               <br>'+
                        '               QQ:'+obj.qq+
                        '           </div>' ;
            item +=     '           <div class="layadmin-address">' +
                        '               <strong>备注</strong>' +
                        '               <br>'+
                        '               销售备注:'+obj.saleRemark+
                        '               <br>'+
                        '               备注:'+obj.remark+
                        '               </div>' +
                        '       </div>';
            item +=     '   <div class="layui-col-md6  layui-col-sm6">' +
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
                        item += '<div class="layadmin-address">'+
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
                                item +='</div>';
                            }
                        });
                    }else if (tmp === 'qzsetting') {
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
                                    '</div>' ;

                            }
                        });
                    }else if (tmp === 'fm') {
                        url = '/internal/qzsetting/toFMWithCustomerUuid/' + obj.uuid;
                        title = contactPerson + '-负面信息';
                        id = contactPerson + '-负面信息';
                        item += '<div class="layadmin-address"><strong>' +
                            '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>负面业务</a>' +
                            '</strong>' +
                            '<br>'+
                            '待做---占位符'+
                            '</div>';
                    }
                });
            }else{
                item += '<div class="layadmin-address">\n' +
                    '                <strong>暂无业务</strong>\n' +
                    '    </div>' ;

            }
            item += '</div>'
            item += '<div class="layui-col-md1 layui-col-sm6">\n' +
                '       <h3 class="layadmin-title">\n' +
                '           <strong>操作</strong>\n' +
                '           </h3>'+
                '               <div class="layadmin-address">\n' +
                '                   <button class="layui-btn layui-btn-sm caller-fr" onclick=editCustomer("'+obj.uuid+'")>' +
                '                   <i class="layui-icon layui-icon-edit"></i>\n' +
                '                   修改\n' +
                '               </button>\n' +
                '           </div>'+
                '           <div class="layadmin-address">\n' +
                '               <button class="layui-btn layui-btn-danger layui-btn-sm caller-fr" onclick=delOneCustomer("'+obj.uuid+'")>' +
                '                   <i class="layui-icon layui-icon-close"></i>\n' +
                '                   删除\n' +
                '               </button>\n' +
                '           </div>';
            item += '</div>';
            item += '</div>';
            $("#data_list").append(item)
        })
    }

    form.on('checkbox(checkAll)', function(data){
        if($(this)[0].checked) {
            $("input[name='checkItem']").each(function() {
                this.checked = true;
            })
        }else{
            $('input[name="checkItem"]').each(function(){
                this.checked = false;
            })
        }
        form.render('checkbox')

    });

    form.on("submit(search)", function (data) {
        var pageConf = data.field;
        pageConf.limit = 25;
        pageConf.page = 1;
        if (!open){
            showCondition();
        }
        initLayPage(pageConf);
        // $("#searchContent").removeClass("layui-show");
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
                            initLayPage()
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

    window.batchDelete = function () {
        var length = $('input[name="checkItem"]:checked').length;
        if (length <= 0){
            show_layer_msg("请选择要删除的客户！！",5);
            return false;
        }
        var uuidArr = [];
        $('input[name="checkItem"]:checked').each(function(){
            // alert($(this).val())
            uuidArr.push(this.value)
        });
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
                            initLayPage()
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

    // 编辑表格获得表格数据
    window.editCustomer = function (uuid) {
        okLayer.open("首页 / 客户列表 / 修改用户", "/internal/customer/toCustomersAdd", "60%", "90%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(uuid);
        }, function () {
            if (sign) {
                initLayPage()
                sign = false;
            }
        })
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

function generate_keyword_info(data) {
    let htm = '';
    if (data.totalCount > 0) {
        htm += '<span>总数:' + data.totalCount + '&nbsp;&nbsp;&nbsp;&nbsp;</span>(';
        if (data.totalCount === data.activeCount) {
            htm += '<span style="color: green;">激活</span>' +
                '|<a href="javascript:changeCustomerKeywordStatus(\'' + data.customerUuid + '\', 0)">暂停关键字</a>'
        } else if (data.totalCount > 0 && data.activeCount > 0) {
            htm += '<span style="color: yellowgreen;">部分暂停</span>' +
                '|<a href="javascript:changeCustomerKeywordStatus(\'' + data.customerUuid + '\', 0)">暂停关键字</a>' +
                '|<a href="javascript:changeCustomerKeywordStatus(\'' + data.customerUuid + '\', 1)">激活关键字</a>'
        } else {
            htm += '<span style="color: red;">暂停</span>' +
                '|<a href="javascript:changeCustomerKeywordStatus(\'' + data.customerUuid + '\', 1)">激活关键字</a>'
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





