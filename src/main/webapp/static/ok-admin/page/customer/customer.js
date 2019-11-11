
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
                '                   <p class="skip" >客户类型 : ' + obj.type +'</p>' +
                '                   <p class="skip" >所属用户 : ' + obj.loginName +'</p>' +
                '                   <div class="skip" style="height: 24px;line-height: 24px">客户状态 : '+ generate_customer_status(obj.uuid, obj.status) +'</div>' +
                '                   <div class="skip" style="height: 24px;line-height: 24px">产生日报表 : ' + generate_customer_daily_report(obj.uuid, obj.dailyReportIdentify) + '</div>' +
                '               </div>';
            item += '           <div class="layadmin-address">' +
                '                   <strong>联系方式</strong>' +
                '                   <br>' +
                '                   电话 : ' + obj.telphone +
                '                   <br>' +
                '                   QQ : ' + obj.qq +
                '               </div>';
            item += '           <div class="layadmin-address ">' +
                '                   <strong>备注</strong>';
            if (isSEOSales){
                item += '       <p class="skip">客户标签 : <span title="'+obj.saleRemark+'" class="can-click" onclick=changeSaleRemark("'+obj.uuid+'",this)>' + obj.saleRemark + '</span></p>';
            }
            let msg = obj.remark ? obj.remark : '暂无';
            item += '           <p class="skip">销售详细备注 : <span title="'+obj.remark+'" class="can-click" onclick=changeRemark("'+obj.uuid+'",this)>' + msg + '</span></p>' +
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
                                        item += '<span>操作词数 : ' + res.data.totalCount + '</span><br>';
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
                                        item += '<span>站点数 : ' + res.data.totalCount + '</span><br>';
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
                                        item += '<span>操作词数 : ' + res.data.totalCount + '</span><br>';
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
                    } else if (tmp === 'qt') {
                        item += '<div class="layadmin-address"><strong>' +
                            '其他业务' +
                            '</strong>' +
                            '<br>';
                        $.ajax({
                            url: '/internal/customerKeyword/getCustomerKeywordsCount/' + obj.uuid + '/qt',
                            dataType: 'json',
                            type: 'get',
                            async: false,
                            success: function (res) {
                                if (res.code === 200) {
                                    if (res.data != null) {
                                        item += '<span>操作词数 : ' + res.data.totalCount + '</span><br>';
                                        if (res.data["PC"] != null) {
                                            item += generate_customer_info(obj.contactPerson, res.data["PC"], 'PC', 'qt', obj.uuid);
                                        }
                                        if (res.data["Phone"] != null) {
                                            item += generate_customer_info(obj.contactPerson, res.data["Phone"], 'Phone', 'qt', obj.uuid);
                                        }
                                    } else {
                                        item += generate_customer_info(obj.contactPerson, null, 'PC', 'qt', obj.uuid);
                                    }
                                } else {
                                    item += generate_customer_info(obj.contactPerson, null, 'PC', 'qt', obj.uuid);
                                }
                                let se = obj.searchEngine ? obj.searchEngine : '无';
                                item += '<span>搜索引擎 : <a href="javascript:void(0)" title="'+obj.searchEngine+'" onclick="changeSearchEngine(\''+obj.uuid+'\',this)">'+ se +'</a></span>';
                                let ea = obj.externalAccount ? obj.externalAccount : '无';
                                item += '<br><span>账号 : <a href="javascript:void(0)" title="'+obj.externalAccount+'" onclick="changeExternalAccount(\''+obj.uuid+'\',this)">'+ ea +'</a></span>';
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
            success: function (data) {
                $("#loginName").empty();
                $("#loginName").append('<option value="">所属用户</option>');
                $.each(data, function (index, item) {
                    $('#loginName').append(
                        '<option value="' + item.loginName + '">'
                        + item.userName
                        + '</option>');// 下拉菜单里添加元素
                });
                form.render("select");
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
    window.changeCustomerKeywordStatus = function (customerUuid, status, entryType, terminalType) {
        if (status === '0') {
            msg = "确定暂停所有关键字吗?"
        } else if (status === '1') {
            msg = "确定激活所有关键字吗?"
        }
        layer.confirm(msg, {icon: 3, title: '关键字状态'}, function (index) {
            var data = {};
            data.customerUuid = customerUuid;
            data.status = status;
            data.entryType = entryType;
            data.terminalType = terminalType;
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
    window.changeCustomerStatus = function (uuid, ele) {
        let newStatus = ele.children[0].innerHTML === '激活' ? 2 : 1;
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
                    if (newStatus === 2) {
                        ele.classList.remove('layui-form-onswitch');
                        ele.children[0].innerHTML = '暂停';
                    } else {
                        ele.classList.add('layui-form-onswitch');
                        ele.children[0].innerHTML = '激活';
                    }
                } else {
                    common.showFailMsg('操作失败');
                }
            },
            error: function () {
                common.showFailMsg('操作失败');
            }
        });
    };

    //改变日报表值
    window.changeCustomerDailyReportIdentify = function (uuid, ele) {
        if (ele.parentElement.previousElementSibling.children[0].children[0].innerHTML !== '激活') {
            common.showFailMsg('请先激活客户');
            return;
        }
        let identify = ele.children[0].innerHTML === '是' ? '0' : '1';
        var postData = {};
        postData.customerUuid = uuid;
        postData.identify = identify;
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
                    if (identify === '0') {
                        ele.classList.remove('layui-form-onswitch');
                        ele.children[0].innerHTML = '否';
                    } else {
                        ele.classList.add('layui-form-onswitch');
                        ele.children[0].innerHTML = '是';
                    }
                } else {
                    common.showFailMsg('操作失败');
                }
            },
            error: function () {
                common.showFailMsg('操作失败');
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
    window.changeSaleRemark = function (uuid, ele) {
        layer.prompt({
            formType: 3,
            value: ele.title,
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
                            ele.innerHTML = value;
                            ele.title = value;
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

    //改备注 => 运营查看
    window.changeRemark = function (uuid, ele) {
        layer.prompt({
            formType: 2,
            value: ele.title,
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
                            ele.innerHTML = value;
                            ele.title = value;
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

    window.changeSearchEngine = function (uuid, ele) {
        layer.open({
            type: 1,
            title: '搜索引擎',
            content: '<form class="layui-form layui-form-pane ok-form" style="margin: 10px 10px 0 10px">'
                + '        <div class="layui-form-item layui-form-text">'
                + '               <select name="searchEngine" id="searchEngine">'
                + '                    <option value="百度">百度</option>'
                + '                    <option value="搜狗">搜狗</option>'
                + '                    <option value="360">360</option>'
                + '                    <option value="百度下拉">百度下拉</option>'
                + '                    <option value="谷歌">谷歌</option>'
                + '                    <option value="神马">神马</option>'
                + '                    <option value="必应中国">必应中国</option>'
                + '                    <option value="必应日本">必应日本</option>'
                + '                </select>'
                + '        </div>'
                + '   </form>',
            shadeClose: true,
            resize: false,
            area: ['300px','500px'],
            offset: '40px',
            btn: ['确认', '取消'],
            yes: function (index) {
                var value = document.getElementById('searchEngine').value;
                var postData = {};
                postData.uuid = uuid;
                postData.searchEngine = value;
                $.ajax({
                    url: '/internal/customer/changeSearchEngine',
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
                            ele.innerHTML = value;
                            ele.title = value;
                            layer.close(index);
                        } else {
                            common.showFailMsg('操作失败');
                        }
                    },
                    error: function () {
                        common.showFailMsg('未知错误，请稍后重试');
                    }
                });
            }, btn2: function (index) {
                layer.close(index);
            }, success: function () {
                $('#searchEngine').val(ele.title);
                form.render('select');
            }
        });
    };

    window.changeExternalAccount = function (uuid, ele) {
        layer.prompt({
            formType: 2,
            value: ele.title,
            title: '账号',
            area: ['300px','20px'],
            offset: '40px',
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                var postData = {};
                postData.uuid = uuid;
                postData.externalAccount = value;
                $.ajax({
                    url: '/internal/customer/changeExternalAccount',
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
                            ele.innerHTML = value;
                            ele.title = value;
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

function generate_customer_status(uuid, status) {
    let stat = '<div onclick=changeCustomerStatus(\'' + uuid + '\',this) class="layui-unselect layui-form-switch ';
    if (status === 1) {
        stat += 'layui-form-onswitch';
    }
    stat += '" lay-skin="_switch"><em>';
    if (status === 1) {
        stat += '激活';
    }else {
        stat += '暂停';
    }
    stat += '</em><i></i></div>';
    return stat;
}

function generate_customer_daily_report(uuid, identify) {
    uuid = uuid + '';
    identify = identify + '';
    let stat = '<div onclick=changeCustomerDailyReportIdentify(\'' + uuid + '\',this) class="layui-unselect layui-form-switch ';
    if (identify === '1') {
        stat += 'layui-form-onswitch';
    }
    stat += '" lay-skin="_switch"><em>';
    if (identify === '1') {
        stat += '是';
    } else {
        stat += '否';
    }
    stat += '</em><i></i></div>';
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
            htm += '<span>'+ terminalTypeName +' : <a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>' + data["count"] + '</a></span>&nbsp;&nbsp;状态:';
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
            htm += '<span>' + terminalTypeName + ' : <a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>' + data.totalCount + '</a></span>(';
            if (data.totalCount === data.activeCount) {
                htm += '<span style="color: green;">激活</span>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","0","' + type + '","' + terminalType + '")>暂停关键字</a>'
            } else if (data.totalCount > 0 && data.activeCount > 0) {
                htm += '<span style="color: yellowgreen;">部分暂停</span>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","0","' + type + '","' + terminalType + '")>暂停关键字</a>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","1","' + type + '","' + terminalType + '")>激活关键字</a>'
            } else {
                htm += '<span style="color: red;">暂停</span>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","1","' + type + '","' + terminalType + '")>激活关键字</a>'
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





