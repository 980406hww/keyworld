
var sign = false;

getHeight();

function getHeight(){
    let b = document.getElementById('customerBody');
    let h = window.innerHeight || document.body.offsetHeight;
    b.style.height = (h - 145) + 'px';
}

// layui相关
layui.use(['element', 'form', 'jquery', 'laypage', 'okLayer', 'layer','common'], function () {
    var element = layui.element;
    var form = layui.form;
    var $ = layui.jquery;
    var layer = layui.layer;
    var okLayer = layui.okLayer;
    var laypage = layui.laypage;
    var common = layui.common;

    $(window).resize(function(){
        let b = document.getElementById('customerBody');
        let h = window.innerHeight || document.body.offsetHeight;
        b.style.height = (h - 155) + 'px';
    });

    init_keyword_type();

    init_belong_user();

    function initLayPage(pageConf) {
        var load_index = layer.load(2, {shade: false}); //添加laoding 0-2
        if (!pageConf) {
            pageConf = {};
            pageConf.limit = 30;
            pageConf.page = 1;
        }
        if(!pageConf.page){
            pageConf.page = 1;
        }
        if (!pageConf.limit) {
            pageConf.limit = 30;
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
                    limits: [10, 30, 50, 100, 500, 1000],
                    first: '首页',
                    last: '尾页',
                    layout: ['prev', 'page', 'next', 'count', 'limit'],
                    jump: function (obj, first) {
                        if (!first) {
                            pageConf = common.formToJsonObject('searchForm');
                            pageConf.page = obj.curr;
                            pageConf.limit = obj.limit;
                            initLayPage(pageConf);
                        }
                    }
                });
                init_data(result.data);
                layer.close(load_index);
                form.render();
            },
            error: function () {
                common.showFailMsg('获取用户失败，请稍后再试');
            }
        });
    }

    function init_data(data) {
        $("#data_list").html('');
        $.each(data, function (index, obj) {
            let item = '<div class="layui-col-md6 layui-col-sm6">' +
                '   <div class="layadmin-contact-box">' +
                '       <div class="layui-col-md6 layui-col-sm6">';
            item += '           <h3 class="layadmin-title skip" title="'+obj.contactPerson+'">' +
                '               <input type="checkbox" name="checkItem" value="' + obj.uuid + '" status="'+obj.status+'"lay-skin="primary" >' +
                '               <strong >' + obj.contactPerson + '</strong>' +
                '           </h3>';
            item += '           <div class="layadmin-address other_info">' +
                '                   <strong>描述信息</strong>' +
                '                   <p class="skip" >客户类型:' + obj.type +'</p>' +
                '                   <p class="skip" >所属用户:' + obj.loginName +'</p>' +
                '                   <p class="skip" >客户状态:<span id="status'+obj.uuid+'" style="color: ' + generate_customer_status(obj.uuid, obj.status, 1) + '</span></p>' +
                '                   <p class="skip" >是否产生日报表:<span id="dr'+obj.uuid+'">' + generate_customer_daily_report(obj.uuid, obj.status, obj.dailyReportIdentify) + '</span></p>' +
                '               </div>';
            item += '           <div class="layadmin-address">' +
                '                   <strong>联系方式</strong>' +
                '                   <br>' +
                '                   电话:' + obj.telphone +
                '                   <br>' +
                '                   QQ:' + obj.qq +
                '               </div>';
            item += '           <div class="layadmin-address ">' +
                '                   <strong>备注</strong>';
            if (isSEOSales){
                item += '       <p class="skip can-click" title="'+obj.saleRemark+'" onclick=changeSaleRemark("'+obj.uuid+'","'+obj.saleRemark+'") >客户标签:<span style="color: #00a65a" id="saleRemark'+obj.uuid+'">' + obj.saleRemark + '</span></p>';
            }
            item += '           <p class="skip can-click" title="'+obj.remark+'" onclick=changeRemark("'+obj.uuid+'","'+obj.remark+'") >销售详细备注:<span style="color: #00a65a" id="remark'+obj.uuid+'">' + obj.remark + '</span></p>' +
                '               </div>' +
                '      </div>';
            item += '   <div class="layui-col-md5  layui-col-sm6" style="margin-top: 37px">';
            let customerBusinessList = obj.customerBusinessList;
            if (customerBusinessList !== null && customerBusinessList.length > 0) {
                $.each(obj.customerBusinessList, function (index, tmp) {
                    if (tmp === 'keyword') {
                        item += '<div class="layadmin-address">' +
                            '<strong>' +
                            '单词业务' +
                            '</strong>' +
                            '<br>';
                        $.ajax({
                            url: '/internal/customerKeyword/getCustomerKeywordsCount/' + obj.uuid + '/pt',
                            dataType: 'json',
                            type: 'get',
                            async: false,
                            success: function (res) {
                                if (res.code === 200) {
                                    if (res.data != null) {
                                        item += '<span>操作词数:' + res.data.totalCount + '</span><br>';
                                        if (res.data["PC"] != null) {
                                            item += generate_customer_info(obj.contactPerson, res.data["PC"], 'PC', 'pt', obj.uuid);
                                        }
                                        if (res.data["Phone"] != null) {
                                            item += generate_customer_info(obj.contactPerson, res.data["Phone"], 'Phone', 'pt', obj.uuid);
                                        }
                                    } else {
                                        item += generate_customer_info(obj.contactPerson, null, 'PC', 'pt', obj.uuid);
                                    }
                                } else {
                                    item += generate_customer_info(obj.contactPerson, null, 'PC', 'pt', obj.uuid);
                                }
                                item += '</div>';
                            }
                        });
                    } else if (tmp === 'qzsetting') {
                        item += '<div class="layadmin-address"><strong>' +
                            '全站业务' +
                            '</strong>' +
                            '<br>';
                        $.ajax({
                            url: '/internal/qzsetting/getQZSettingsCount/' + obj.uuid,
                            dataType: 'json',
                            type: 'get',
                            async: false,
                            success: function (res) {
                                if (res.code === 200) {
                                    if (res.data != null) {
                                        item += '<span>站点数:' + res.data.totalCount + '</span><br>';
                                        if (res.data["PC"] != null) {
                                            item += generate_qzsetting_info(obj.contactPerson, res.data["PC"], 'PC',  obj.uuid);
                                        }
                                        if (res.data["Phone"] != null) {
                                            item += generate_qzsetting_info(obj.contactPerson, res.data["Phone"], 'Phone', obj.uuid);
                                        }
                                    } else {
                                        item += generate_qzsetting_info(obj.contactPerson, null, 'PC',  obj.uuid);
                                    }
                                } else {
                                    item += generate_qzsetting_info(obj.contactPerson, null, 'PC', obj.uuid);
                                }
                                item += '</div>';
                            }
                        });
                    } else if (tmp === 'fm') {
                        item += '<div class="layadmin-address"><strong>' +
                            '负面业务' +
                            '</strong>' +
                            '<br>';
                        $.ajax({
                            url: '/internal/customerKeyword/getCustomerKeywordsCount/' + obj.uuid + '/fm',
                            dataType: 'json',
                            type: 'get',
                            async: false,
                            success: function (res) {
                                if (res.code === 200) {
                                    if (res.data != null) {
                                        item += '<span>操作词数:' + res.data.totalCount + '</span><br>';
                                        if (res.data["PC"] != null) {
                                            item += generate_customer_info(obj.contactPerson, res.data["PC"], 'PC', 'fm', obj.uuid);
                                        }
                                        if (res.data["Phone"] != null) {
                                            item += generate_customer_info(obj.contactPerson, res.data["Phone"], 'Phone', 'fm', obj.uuid);
                                        }
                                    } else {
                                        item += generate_customer_info(obj.contactPerson, null, 'PC', 'fm', obj.uuid);
                                    }
                                } else {
                                    item += generate_customer_info(obj.contactPerson, null, 'PC', 'fm', obj.uuid);
                                }
                                item += '</div>';
                            }
                        });
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
                '               删除\n' +
                '               <i class="layui-icon layui-icon-close"></i>\n' +
                '           </a>\n' +
                '       </div>';
            item += '</div>';
            item += '</div>';
            $("#data_list").append(item)
        })
    }

    function init_keyword_type() {
        $.ajax({
            url: '/internal/common/getBusinessTypeByUserRole',
            dataType: 'json',
            async: false,
            type: 'get',
            success: function (res) {
                if (res.code === 200) {
                    let i = 0;
                    $.each(res.data, function (index, item) {
                        let businessItem = item.split("#");
                        if (i === 0) {
                            $('#tabItem').append(
                                '<li data-entrytype="' + businessItem[0] + '" data-terminal="PC" class="layui-this">' + businessItem[1] + '</li>' );
                            $('#entryType').val(businessItem[0]);
                        }else {
                            $('#tabItem').append(
                                '<li data-entrytype="' + businessItem[0] + '" data-terminal="PC">' + businessItem[1] + '</li>' );
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
            success: function (res) {
                if (res.code === 200) {
                    let data = res.data;
                    $("#userName").empty();
                    $("#userName").append('<option value="">所属用户</option>');
                    $.each(data, function (index, item) {
                        $('#userName').append(
                            '<option value="' + item.loginName + '">'
                            + item.userName
                            + '</option>');// 下拉菜单里添加元素
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

    element.on('tab(customerTab)', function (data) {
        let d = data.elem.context.dataset;
        $('#entryType').val(d.entrytype);
        initLayPage(common.formToJsonObject('searchForm'));
        if (d.entrytype === 'pt') {
            if (window.hasPer) {
                var intervalId = setInterval(function () {
                    layui.searchCurrentDateCompletedReports();
                }, 1000 * 30);
                window.searchCurrentDateCompletedReportsIntervalId = intervalId;
            }
        } else {
            if (window.searchCurrentDateCompletedReportsIntervalId){
                window.clearInterval(window.searchCurrentDateCompletedReportsIntervalId);
            }
        }
    });

    window.showCustomerByType = function (customerType){
        $('#type').val(customerType);
        $('#searchBtn').click();
    };

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
        pageConf.limit = 50;
        pageConf.page = 1;
        $.each(pageConf,function(idx,item){
            pageConf[idx] = $.trim(item)
        });
        if (!open2) {
            showCondition();
        }
        initLayPage(pageConf);
        return false;
    });
    
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
                        common.showSuccessMsg('操作成功',function () {
                            let pageConf = common.formToJsonObject('searchForm');
                            initLayPage(pageConf)
                        });
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
    };

    //批量删除
    window.batchDelete = function () {
        let uuidArr = get_select_uuids();
        if (uuidArr.length <= 0) {
            common.showFailMsg("请选择要删除的客户！！");
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
                        common.showSuccessMsg('操作成功', function () {
                            let pageConf = common.formToJsonObject('searchForm');
                            initLayPage(pageConf)
                        });
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
    };

    // 添加客户
    window.toAddCustomer = function () {
        let entryType = $('#entryType').val();
        let data = {};
        data.uuid = null;
        data.entryType = entryType;
        okLayer.open("首页 / 客户列表 / 添加用户", "/internal/customer/toCustomersAdd", "40%", "90%", function(layero){
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                let pageConf = common.formToJsonObject('searchForm');
                initLayPage(pageConf);
                sign = false;
            }
        });
    };

    // 编辑表格获得表格数据
    window.editCustomer = function (uuid) {
        let data = {};
        let entryType = $('#entryType').val();
        data.uuid = uuid;
        data.entryType = entryType;
        okLayer.open("首页 / 客户列表 / 修改用户", "/internal/customer/toCustomersAdd", "40%", "90%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                let pageConf = common.formToJsonObject('searchForm');
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
            data.entryType = $('#entryType').val();
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
                        common.showSuccessMsg('操作成功', function () {
                            let pageConf = common.formToJsonObject('searchForm');
                            initLayPage(pageConf);
                        });
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
    };

    // 改变客户状态
    window.changeCustomerStatus = function (uuid, oldStatus) {
        let newStatus = oldStatus === '1' ? 2 : 1;
        var postData = {};
        postData.customerUuid = uuid;
        postData.status = newStatus;
        $.ajax({
            url: '/internal/customer/changeCustomerStatus',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(postData),
            success: function (result) {
                if (result.code === 200) {
                    common.showSuccessMsg('操作成功');
                    $('#status' + uuid).html(generate_customer_status(uuid, newStatus, 2));
                    $('#status' + uuid).css("color", newStatus === 1 ? "green" : "red");
                }
            },
            error: function () {
                common.showFailMsg('操作失败');
                $('#status' + uuid).html(generate_customer_status(uuid, oldStatus, 2));
                $('#status' + uuid).css("color", oldStatus === 1 ? "green" : "red");
            }
        });
    };

    //改变日报表值
    window.changeCustomerDailyReportIdentify = function (uuid, status, oldIdentify, newIdentify) {
        if (status !== '1') {
            common.showFailMsg('请激活客户');
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
                    common.showSuccessMsg('操作成功');
                    $('#dr' + uuid).html(generate_customer_daily_report(uuid, status, newIdentify))
                }
            },
            error: function () {
                common.showFailMsg('操作失败');
                $('#dr' + uuid).html(generate_customer_daily_report(uuid, status, oldIdentify))
            }
        });

    };

    //触发所选客户日报表
    window.batchUpdateDailyReport= function () { //获取选中数据
        let uuidArr = get_select_active_uuids();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的激活的客户');
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
                        common.showSuccessMsg('操作成功!', function () {
                            let pageConf = common.formToJsonObject('searchForm');
                            initLayPage(pageConf);
                        });
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
    };

    window.batchUpdateBelongUser = function(){
        let uuidArr = get_select_uuids();
        if (uuidArr.length <= 0) {
            common.showFailMsg('请选择要操作的客户');
            return false;
        }
        okLayer.open("首页 / 客户列表 / 修改客户所属", "/internal/customer/toUpdateBelongUser", "40%", "50%", function(layero){
            window[layero.find("iframe")[0]["name"]].initForm(uuidArr);
        }, function () {
            if (sign) {
                let pageConf = common.formToJsonObject('searchForm');
                initLayPage(pageConf);
                sign = false;
            }
        });
    };

    //改客户标签
    window.changeSaleRemark = function (uuid, saleRemark) {
        layer.prompt({
            formType: 3,
            value: saleRemark,
            title: '客户标签',
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                var postData = {};
                postData.uuid = uuid;
                postData.saleRemark = value;
                $.ajax({
                    url: '/internal/customer/changeSaleRemark2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            common.showSuccessMsg('操作成功');
                            $('#saleRemark'+uuid).text(value);
                            $('#saleRemark'+uuid).parent().attr("title",value);
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

    //改备注 => 运营查看
    window.changeRemark = function (uuid, remark) {
        layer.prompt({
            formType: 2,
            value: remark,
            title: '备注',
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                var postData = {};
                postData.uuid = uuid;
                postData.remark = value;
                $.ajax({
                    url: '/internal/customer/changeRemark2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            common.showSuccessMsg('操作成功');
                            $('#remark'+uuid).text(value);
                            $('#remark'+uuid).parent().attr("title",value);
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
    };

    form.verify({
        qq: [
            /(^$)|(^[1-9]\d{4,14}$)/,
            "请输入正确格式QQ"
        ],
        noSpace: [
            /(^$)|(^[^ ]+$)/,
            "请勿输入空格！"
        ],
        telPhone: [
            /(^$)|(^(13[0-9]|14[5-9]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[13589])\d{8}$)/,
            "请输入正确格式手机号码"
        ]
    });
});

let open2 = true;

function showCondition() {
    let searchContent = document.getElementById('searchContent');
    if (open2) {
        searchContent.style.display = 'block';
    } else {
        searchContent.style.display = 'none';
    }
    open2 = !open2;
}

function generate_customer_status(uuid, status, times){
    let stat = '暂停';
    if (status === 1) {
        stat = '激活';
    }
    if (times === 1) {
        if (status === 1) {
            stat = "green\">" + stat;
        } else {
            stat = "red\">" + stat;
        }
    }
    stat += ' | <a href="javascript:void(0)" onclick=changeCustomerStatus(\'' + uuid + '\',\'' + status + '\')>修改状态</a>';
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

function generate_qzsetting_info(contactPerson, data, terminalType, customerUuid) {
    let htm = '';
    let terminalTypeName = terminalType === "PC" ? "电脑端" : "移动端";
    if (data != null) {
        if (data["count"] > 0) {
            let url = '/internal/qzsetting/toQZSettingsWithCustomerUuid/' + customerUuid + '/' + terminalType;
            contactPerson = contactPerson.replace(/\s+/g, "");
            let title = contactPerson + '-全站';
            let id = contactPerson + '-toQz-' + terminalType;
            htm += '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")><span>'+ terminalTypeName +':' + data["count"] + '</span></a>&nbsp;&nbsp;状态:';
            if (data["active"] > 0) {
                htm += '<span style="color: green;">'+data["active"]+'个续费|</span>'
            }
            if (data["stop"] > 0) {
                htm += '<span style="color: darkred;">'+data["stop"]+'个暂停|</span>'
            }
            if (data["down"] > 0) {
                htm += '<span style="color: grey;">'+data["down"]+'个下架|</span>'
            }
            if(data["other"] > 0) {
                htm += '<span style="color: lightgrey;">'+data["other"]+'个其他|</span>'
            }
            htm = htm.substring(0, htm.lastIndexOf("|"));
            htm += '<br>';
        }
    } else {
        let url = '/internal/customerKeyword/toCustomerKeywords/qz/' + terminalType + '/' + customerUuid;
        contactPerson = contactPerson.replace(/\s+/g, "");
        let title = contactPerson + '-关键字列表';
        let id = contactPerson + '-qz-' + terminalType;
        htm += '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")><span>查看关键字</span></a>';
    }
    return htm;
}

/**
 * type: pt, fm, qt
 */
function generate_customer_info(contactPerson, data, terminalType, type, customerUuid) {
    let htm = '';
    let url = '/internal/customerKeyword/toCustomerKeywords/' + type + '/' + terminalType + '/' + customerUuid;
    contactPerson = contactPerson.replace(/\s+/g, "");
    let title = contactPerson + '-关键字列表';
    let id = contactPerson + '-' + type + '-' + terminalType;
    let terminalTypeName = terminalType === 'PC' ? "电脑端" : "移动端";
    if (data !== null) {
        if (data.totalCount > 0) {
            htm += '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")><span>' + terminalTypeName + ':' + data.totalCount + '</span></a>(';
            if (data.totalCount === data.activeCount) {
                htm += '<span style="color: green;">激活</span>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","0")>暂停关键字</a>'
            } else if (data.totalCount > 0 && data.activeCount > 0) {
                htm += '<span style="color: yellowgreen;">部分暂停</span>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","0")>暂停关键字</a>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","1")>激活关键字</a>'
            } else {
                htm += '<span style="color: red;">暂停</span>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","1")>激活关键字</a>'
            }
            htm += ')';
        } else {
            htm += '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")><span>查看' + terminalTypeName + '关键字</span></a>';
        }
    } else {
        htm += '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")><span>查看关键字</span></a>';
    }
    htm += '<br>';
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
            title: '<strong style="display: none;" is-close="true" lay-id="'+id+'" data-url="'+url+'"></strong>' +
                '<i class="layui-icon">&#xe648;</i> ' + tit,
                content: contentIframe,
                id: id
            }
        );
    }

    parent.layui.element.tabChange('ok-tab', id)
}





