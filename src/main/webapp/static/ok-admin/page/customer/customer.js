var sign = false;
var searchOpen = false;

getHeight();

function getHeight(){
    let b = document.getElementById('customerBody');
    let h = window.innerHeight || document.body.offsetHeight;
    b.style.height = (h - 145) + 'px';
}

// layui相关
layui.use(['element', 'form', 'jquery', 'laypage', 'okLayer', 'layer', 'common', 'util'], function () {
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

    function initLayPage(pageConf, call) {
        var load_index = layer.load(2, {shade: false}); //添加laoding 0-2
        if (!pageConf) {
            pageConf = {};
            pageConf.limit = 51;
            pageConf.page = 1;
        }
        if(!pageConf.page){
            pageConf.page = 1;
        }
        if (!pageConf.limit) {
            pageConf.limit = 51;
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
                    limits: [12, 30, 51, 99, 300, 999],
                    first: '首页',
                    last: '尾页',
                    layout: ['prev', 'page', 'next', 'count', 'limit'],
                    jump: function (obj, first) {
                        if (!first) {
                            pageConf = common.formToJsonObject('searchForm');
                            pageConf.page = obj.curr;
                            pageConf.limit = obj.limit;
                            initLayPage(pageConf, getOtherMsg);
                        }
                    }
                });

                init_data(result.data);
                call(result.data);
                layer.close(load_index);
                form.render();
            },
            error: function () {
                common.showFailMsg('获取用户失败，请稍后再试');
            }
        });
    }

    function init_data(data) {
        let dataList = document.getElementById('data_list');
        dataList.innerHTML = '';
        let item = '', et = document.getElementById('entryType').value;
        $.each(data, function (index, obj) {
            item += '<div class="layadmin-contact-box">';
            item += '      <div class="layui-row"><div class="layui-col-md5">'
                + '        <h3 class="layadmin-title skip" title="' + obj.contactPerson + '">' +
                '               <input type="checkbox" name="checkItem" value="' + obj.uuid + '" status="' + obj.status + '"lay-skin="primary" >' +
                '               <strong >' + obj.contactPerson + '</strong>' +
                '           </h3>'
                + '         </div><div class="layui-col-md7">';
            item += '       <div class="operation" style="text-align: right;line-height: 27px">';
            if (et === 'qt') {
                item += '           <a href="javascript:void(0)" class="caller-fr can-click" onclick=openCustomerRule("' + obj.uuid + '")>' +
                    '                   客户规则' +
                    '               </a>'
                    + '             <a href="javascript:void(0)" class="caller-fr can-click" onclick=openQuicklyAddKeyword("' + obj.uuid + '")>' +
                    '                   快速加词' +
                    '               </a>'
                    + '             <a href="javascript:void(0)" class="caller-fr can-click" onclick=uploadDayReportList("' + obj.uuid + '")>' +
                    '                   上传模板' +
                    '               </a>';
            }
            item += '             <a href="javascript:void(0)" class="caller-fr can-click" onclick=editCustomer("' + obj.uuid + '")>' +
                '                   修改<i style="position: relative;top: 1px" class="layui-icon layui-icon-edit"></i>' +
                '               </a>'
                + '             <a href="javascript:void(0)" class="caller-fr can-click" onclick=delOneCustomer("' + obj.uuid + '")>' +
                '                   删除<i style="position: relative;top: 1px" class="layui-icon layui-icon-close"></i>' +
                '               </a>'
                + '</div>';
            item += '</div></div>';
            item += '   <div class="layui-row">' +
                '       <div class="layui-col-md5">';
            item += '           <div class="layadmin-address other_info">' +
                '                   <strong>描述信息</strong>' +
                '                   <p class="skip" >客户类型 : ' + obj.type + '</p>' +
                '                   <p class="skip" >所属用户 : ' + obj.userName + '</p>' +
                '                   <p class="skip" >创建时间 : ' + layui.util.toDateString(obj.createTime, 'yyyy-MM-dd') + '</p>' +
                '                   <div class="skip" style="height: 24px;margin: 1px 0"><span style="position: relative;top: 1px">客户状态 : </span>'
                + generate_customer_status(obj.uuid, obj.status) + '</div>' +
                '                   <div class="skip" style="height: 24px;margin: 1px 0"><span style="position: relative;top: 1px">产生报表 : </span>'
                + generate_customer_daily_report(obj.uuid, obj.dailyReportIdentify) + '</div>' +
                '               </div>';
            item += '           <div class="layadmin-address">' +
                '                   <strong>联系方式</strong>' +
                '                   <br>' +
                '                   电话 : ' + obj.telphone +
                '                   <br>' +
                '                   QQ : ' + obj.qq +
                '               </div>' +
                '      </div>';
            item += '  <div class="layui-col-md7">';
            let customerBusinessList = obj.customerBusinessList;
            if (customerBusinessList !== null && customerBusinessList.length > 0) {
                $.each(obj.customerBusinessList, function (index, tmp) {
                    if (tmp === 'keyword') {
                        item += '<div class="layadmin-address" id="customPT' + obj.uuid + '"><strong>单词业务</strong><br></div>';
                    } else if (tmp === 'qzsetting') {
                        item += '<div class="layadmin-address" id="customQZ' + obj.uuid + '"><strong>全站业务</strong><br></div>';
                    } else if (tmp === 'fm') {
                        item += '<div class="layadmin-address" id="customFM' + obj.uuid + '"><strong>负面业务</strong><br></div>';
                    } else if (tmp === 'qt') {
                        item += '<div class="layadmin-address" id="customQT' + obj.uuid + '"><strong>其他业务</strong><br></div>';
                    } else {
                        item += '<div class="layadmin-address"><strong>暂无业务</strong></div>';
                    }
                });
            }
            item += '</div></div>';
            item += '<div class="layadmin-address "><strong>备注</strong>';
            if (isSEOSales) {
                item += '       <p class="skip">客户标签 : <span title="' + obj.saleRemark + '" class="can-click" onclick=changeSaleRemark("' + obj.uuid
                    + '",this)>' + obj.saleRemark + '</span></p>';
            }
            let msg = obj.remark ? obj.remark : '暂无';
            item += '           <p class="skip">销售详细备注 : <span title="' + obj.remark + '" class="can-click" onclick=changeRemark("' + obj.uuid + '",this)>'
                + msg + '</span></p></div></div>';
        });
        dataList.innerHTML = item;
    }

    function getOtherMsg(data) {

        $.each(data, function (index, obj) {
            let customerBusinessList = obj.customerBusinessList;
            if (customerBusinessList !== null && customerBusinessList.length > 0) {
                $.each(obj.customerBusinessList, function (index, tmp) {
                    if (tmp === 'keyword') {
                        $.ajax({
                            url: '/internal/customerKeyword/getCustomerKeywordsCount/' + obj.uuid + '/pt',
                            dataType: 'json',
                            type: 'get',
                            success: function (res) {
                                let msg = '';
                                if (res.code === 200) {
                                    if (res.data != null) {
                                        msg += '<span>操作词数 : ' + res.data.totalCount + '</span><br>';
                                        if (res.data["PC"] != null) {
                                            msg += generate_customer_info(obj.contactPerson, res.data["PC"], 'PC', 'pt', obj.uuid);
                                        }
                                        if (res.data["Phone"] != null) {
                                            msg += generate_customer_info(obj.contactPerson, res.data["Phone"], 'Phone', 'pt', obj.uuid);
                                        }
                                    } else {
                                        msg += generate_customer_info(obj.contactPerson, null, 'PC', 'pt', obj.uuid);
                                    }
                                } else {
                                    msg += generate_customer_info(obj.contactPerson, null, 'PC', 'pt', obj.uuid);
                                }
                                let msgBox = document.getElementById("customPT" + obj.uuid);
                                msgBox.innerHTML = msgBox.innerHTML + msg;
                            }
                        });
                    } else if (tmp === 'qzsetting') {
                        $.ajax({
                            url: '/internal/qzsetting/getQZSettingsCount/' + obj.uuid,
                            dataType: 'json',
                            type: 'get',
                            success: function (res) {
                                let msg = '';
                                if (res.code === 200) {
                                    if (res.data != null) {
                                        msg += '<span>站点数 : ' + res.data.totalCount + '</span><br>';
                                        if (res.data["PC"] != null) {
                                            msg += generate_qzsetting_info(obj.contactPerson, res.data["PC"], 'PC', obj.uuid);
                                        }
                                        if (res.data["Phone"] != null) {
                                            msg += generate_qzsetting_info(obj.contactPerson, res.data["Phone"], 'Phone', obj.uuid);
                                        }
                                    } else {
                                        msg += generate_qzsetting_info(obj.contactPerson, null, 'PC', obj.uuid);
                                    }
                                } else {
                                    msg += generate_qzsetting_info(obj.contactPerson, null, 'PC', obj.uuid);
                                }
                                let msgBox = document.getElementById("customQZ" + obj.uuid);
                                msgBox.innerHTML = msgBox.innerHTML + msg;
                            }
                        });
                    } else if (tmp === 'fm') {
                        $.ajax({
                            url: '/internal/customerKeyword/getCustomerKeywordsCount/' + obj.uuid + '/fm',
                            dataType: 'json',
                            type: 'get',
                            success: function (res) {
                                let msg = '';
                                if (res.code === 200) {
                                    if (res.data != null) {
                                        msg += '<span>操作词数 : ' + res.data.totalCount + '</span><br>';
                                        if (res.data["PC"] != null) {
                                            msg += generate_customer_info(obj.contactPerson, res.data["PC"], 'PC', 'fm', obj.uuid);
                                        }
                                        if (res.data["Phone"] != null) {
                                            msg += generate_customer_info(obj.contactPerson, res.data["Phone"], 'Phone', 'fm', obj.uuid);
                                        }
                                    } else {
                                        msg += generate_customer_info(obj.contactPerson, null, 'PC', 'fm', obj.uuid);
                                    }
                                } else {
                                    msg += generate_customer_info(obj.contactPerson, null, 'PC', 'fm', obj.uuid);
                                }
                                let msgBox = document.getElementById("customFM" + obj.uuid);
                                msgBox.innerHTML = msgBox.innerHTML + msg;
                            }
                        });
                    } else if (tmp === 'qt') {
                        $.ajax({
                            url: '/internal/customerKeyword/getCustomerKeywordsCount/' + obj.uuid + '/qt',
                            dataType: 'json',
                            type: 'get',
                            success: function (res) {
                                let msg = '';
                                if (res.code === 200) {
                                    if (res.data != null) {
                                        msg += '<span>操作词数 : ' + res.data.totalCount + '</span><br>';
                                        if (res.data["PC"] != null) {
                                            msg += generate_customer_info(obj.contactPerson, res.data["PC"], 'PC', 'qt', obj.uuid);
                                        }
                                        if (res.data["Phone"] != null) {
                                            msg += generate_customer_info(obj.contactPerson, res.data["Phone"], 'Phone', 'qt', obj.uuid);
                                        }
                                    } else {
                                        msg += generate_customer_info(obj.contactPerson, null, 'PC', 'qt', obj.uuid);
                                    }
                                } else {
                                    msg += generate_customer_info(obj.contactPerson, null, 'PC', 'qt', obj.uuid);
                                }
                                msg += '<div><span style="position: relative;top:1px">搜索引擎 : </span>' + get_se_method(obj.searchEngine, obj.uuid) + '</div>';
                                let ea = obj.externalAccount ? obj.externalAccount : '无';
                                msg += '<div style="height: 24px;margin-top: 1px;line-height: 24px"><span data-uuid="' + obj.uuid
                                    + '">账号 : </span><a href="javascript:void(0)" title="' + obj.externalAccount + '" onclick="changeMe(this)">' + ea
                                    + '</a></div>';
                                let msgBox = document.getElementById("customQT" + obj.uuid);
                                msgBox.innerHTML = msgBox.innerHTML + msg;
                            }
                        });
                    }
                });
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
            url: '/internal/user/getActiveUsersByAuthority',
            dataType: 'json',
            type: 'get',
            success: function (res) {
                if (res.code === 200) {
                    let data = res.data;
                    $("#loginName").empty();
                    $("#loginName").append('<option value="">所属用户</option>');
                    $.each(data, function (index, item) {
                        $('#loginName').append(
                            '<option value="' + item.loginName + '">'
                            + item.userName
                            + '</option>');// 下拉菜单里添加元素
                    });
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
        if (d.entrytype === 'pt') {
            if (window.hasPer) {
                var intervalId = setInterval(function () {
                    layui.searchCurrentDateCompletedReports();
                }, 1000 * 30);
                window.searchCurrentDateCompletedReportsIntervalId = intervalId;
            }
        } else {
            if (window.searchCurrentDateCompletedReportsIntervalId) {
                window.clearInterval(window.searchCurrentDateCompletedReportsIntervalId);
            }
        }
        if (searchOpen === true) {
            initLayPage(common.formToJsonObject('searchForm'), getOtherMsg);
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
        searchOpen = true;
        var pageConf = data.field;
        pageConf.limit = 51;
        pageConf.page = 1;
        $.each(pageConf,function(idx,item){
            pageConf[idx] = $.trim(item)
        });
        if (!open2) {
            showCondition();
        }
        initLayPage(pageConf, getOtherMsg);
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
                            initLayPage(pageConf, getOtherMsg);
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
                            initLayPage(pageConf, getOtherMsg);
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
                initLayPage(pageConf, getOtherMsg);
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
                initLayPage(pageConf, getOtherMsg);
                sign = false;
            }
        })
    };

    // 打开客户规则
    window.openCustomerRule = function(uuid){
        okLayer.open("首页 / 客户列表 / 客户规则", "/internal/customer/toCustomersRule", "56%", "65%", function (layero) {
            window[layero.find("iframe")[0]["name"]].initForm(uuid);
        }, function () {
            if (sign) {
                let pageConf = common.formToJsonObject('searchForm');
                initLayPage(pageConf, getOtherMsg);
                sign = false;
            }
        })
    };

    // 打开快速加词
    window.openQuicklyAddKeyword = function(uuid){
        let html = '<div style="margin: 10px 10px 0 10px;">'
            + '<form class="layui-form layui-form-pane ok-form">'
            + '        <div class="layui-form-item">'
            + '             <textarea style="height: 250px" id="customerKeywordTextarea" placeholder="关键字 域名  关键字与域名以空格作为分割，一行一组" autocomplete="off" class="layui-textarea"></textarea>'
            + '        </div>'
            + '        <div class="layui-form-item" pane="">'
            + '            <label style="width: 100px" class="layui-form-label">终端选择</label>'
            + '            <div style="margin-left: 100px" class="layui-input-block">'
            + '                 <input type="radio" name="terminalType" value="PC" title="电脑" checked="">'
            + '                 <input type="radio" name="terminalType" value="Phone" title="手机">'
            + '            </div>'
            + '        </div>'
            + '        <div class="layui-form-item" style="margin-bottom: 0">'
            + '            <label style="width: 100px" class="layui-form-label">分组名称</label>'
            + '            <div style="margin-left: 100px" class="layui-input-block">'
            + '                <input type="text" id="group" autocomplete="off" class="layui-input" value="{0}">'
            + '            </div>'
            + '        </div>'
            + '</div></form>';
        let group = isPC() ? 'pc_pm_xiaowu' : 'm_pm_tiantian';
        layer.open({
            type: 1,
            title: '快速加词',
            content: html.replace('{0}', group),
            shadeClose: true,
            resize: false,
            area: '550px',
            offset: '100px',
            btn: ['确定', '取消'],
            yes: function (index) {
                let p_d = {},customerKeywords = [];
                let customerKeywordTextarea = document.getElementById('customerKeywordTextarea').value.trim();
                let group = document.getElementById('group').value.trim();
                let terminalType = document.getElementsByName('terminalType');
                if (terminalType[0].checked) {
                    p_d.terminalType = terminalType[0].value;
                } else {
                    p_d.terminalType = terminalType[1].value;
                }
                if (!customerKeywordTextarea) {
                    common.showFailMsg('请输入关键字信息');
                    return false;
                }
                if (!group) {
                    common.showFailMsg('请输入关键字分组名称');
                    return false;
                }
                let customerKeywordTextArray = customerKeywordTextarea.split("\n");
                if (customerKeywordTextArray.length === 1) {
                    customerKeywordTextArray = customerKeywordTextarea.split("\r\n");
                }
                $.each(customerKeywordTextArray, function (idx, val) {
                    val = val.trim();
                    if (val !== '') {
                        let customerKeywordAttributes = val.split(" ");
                        if (customerKeywordAttributes.length === 1) {
                            customerKeywordAttributes = val.split(" ");
                        }
                        if (customerKeywordAttributes.length === 1) {
                            customerKeywordAttributes = val.split("	");
                        }
                        let tmpCustomerKeywordAttributes = [];
                        $.each(customerKeywordAttributes, function (idx, val) {
                            if (val !== '') {
                                tmpCustomerKeywordAttributes.push(val);
                            }
                        });
                        let customerKeyword = {};
                        customerKeyword.customerUuid = uuid;
                        customerKeyword.keyword = tmpCustomerKeywordAttributes[0].trim();
                        customerKeyword.url = tmpCustomerKeywordAttributes[1].trim();
                        customerKeyword.optimizeGroupName = group.trim();
                        customerKeyword.url = customerKeyword.url.replace("http://", "");
                        customerKeyword.url = customerKeyword.url.replace("https://", "");
                        if (customerKeyword.url.length > 25) {
                            customerKeyword.url = customerKeyword.url.substring(0, 25);
                        }
                        customerKeyword.manualCleanTitle = true;
                        customerKeywords.push(customerKeyword);
                    }
                });
                p_d.customerKeywords = customerKeywords;
                $.ajax({
                    url: '/internal/customerKeyword/saveCustomerKeywords2',
                    data: JSON.stringify(p_d),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (res) {
                        if (res.code === 200) {
                            common.showSuccessMsg('保存成功', function () {
                                let pageConf = common.formToJsonObject('searchForm');
                                initLayPage(pageConf, getOtherMsg);
                                layer.close(index);
                            });
                        } else {
                            common.showFailMsg('保存失败');
                        }
                    },
                    error: function () {
                        common.showFailMsg('网络错误请稍后再试');
                    }
                });
            },
            btn2: function (index) {
                layer.close(index);
            },
            success:function () {
                form.render('radio');
            }
        });
    };

    function isPC() {
        let userAgentInfo = navigator.userAgent;
        let Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
        for (let v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) {
                return false;
            }
        }
        return true;
    }

    // 上传日报表
    window.uploadDayReportList = function(uuid){
        let html = '<div style="margin: 10px 10px 0 10px;">'
            + '<form class="layui-form layui-form-pane ok-form">'
            + '            <div style="position: relative;">'
            + '                <div class="layui-btn layui-btn-radius layui-btn-normal" style="cursor: pointer; height: 38px; width: 142px;">'
            + '                    <i class="layui-icon">&#xe67c;</i>选择文件'
            + '                </div>'
            + '                <input id="file" name="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"'
            + '                type="file" onchange="showName(this)" style="cursor: pointer; opacity: 0; position: absolute; left: 0; height: 38px; width: 142px;">'
            + '                <div id="fileName">允许上传.xls .xlsx文件</div>'
            + '            </div>'
            + '            <div class="layui-form-item" style="margin-top: 5px;margin-bottom: 0" pane="">'
            + '                 <label style="width: 100px" class="layui-form-label">终端选择</label>'
            + '                 <div style="margin-left: 100px" class="layui-input-block">'
            + '                     <input type="radio" name="terminalType" value="PC" title="电脑" checked="">'
            + '                     <input type="radio" name="terminalType" value="Phone" title="手机">'
            + '                 </div>'
            + '             </div>'
            + '</div></form>';
        layer.open({
            type: 1,
            title: '上传报表',
            content: html,
            shadeClose: true,
            resize: false,
            area: '400px',
            offset: '100px',
            btn: ['确定', '取消'],
            yes: function (index) {
                let file = document.getElementById('file');
                let fileType = file.value.split('.');
                fileType = fileType[fileType.length - 1];
                if (fileType !== 'xls' && fileType !== 'xlsx') {
                    common.showFailMsg("请提交表格文 .xls .xlsx");
                    return false;
                }
                let formData = new FormData();
                formData.append('file', file.files[0]);
                formData.append('customerUuid', uuid);
                let terminalType = document.getElementsByName('terminalType');
                if (terminalType[0].checked) {
                    terminalType = terminalType[0].value;
                } else {
                    terminalType = terminalType[1].value;
                }
                debugger
                formData.append('terminalType', terminalType);
                $.ajax({
                    url: '/internal/customer/uploadDailyReportTemplate2',
                    type: 'POST',
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (res) {
                        if (res.code === 200) {
                            common.showSuccessMsg("上传成功", function () {
                                layer.close(index);
                            })
                        } else {
                            common.showFailMsg("上传失败");
                        }
                    },
                    error: function () {
                        common.showFailMsg("网络异常请稍后再试");
                    }
                });
            },
            btn2: function (index) {
                layer.close(index);
            },
            success:function () {
                form.render('radio');
            }
        });
    };

    window.showName = function (e) {
        document.getElementById("fileName").innerText = e.value;
    };

    //更新关键字状态
    window.changeCustomerKeywordStatus = function (customerUuid, status, entryType, terminalType, ele) {
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
                            ele.parentElement.innerHTML = getNewHtml(customerUuid, status, entryType, terminalType);
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

    function getNewHtml(customerUuid, status, entryType, terminalType){
        if (status === '0') {
            return '(<span style="color: red;">暂停</span>|<a href="javascript:void(0)" '
                + 'onclick=changeCustomerKeywordStatus("' + customerUuid + '","1","' + entryType + '","' + terminalType + '",this)>激活关键字</a>)';
        } else {
            return '(<span style="color: green;">激活</span>|<a href="javascript:void(0)" '
                + 'onclick=changeCustomerKeywordStatus("' + customerUuid + '","0","' + entryType + '","' + terminalType + '",this)>暂停关键字</a>)';
        }
    }

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
        if (ele.parentElement.previousElementSibling.children[1].children[0].innerHTML !== '激活') {
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
                            initLayPage(pageConf, getOtherMsg);
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
                initLayPage(pageConf, getOtherMsg);
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

    window.selectOne = function (ele) {
        let input = ele.parentElement.previousElementSibling.children[0];
        let value = ele.getAttribute('lay-value');
        if (value === input.value) {
            common.showSuccessMsg('操作成功');
            return;
        }
        let postData = {};
        postData.uuid = input.dataset.uuid;
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
                    input.value = value;
                    clearThis(ele);
                    ele.classList.add('layui-this');
                } else {
                    common.showFailMsg('操作失败');
                }
            },
            error: function () {
                common.showFailMsg('未知错误，请稍后重试');
            }
        });
    };

    window.changeExternalAccount = function (ele) {
        let postData = {};
        postData.uuid = ele.previousElementSibling.dataset.uuid;
        postData.externalAccount = ele.value;
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
                    let msg = ele.value ? ele.value : '无';
                    let html = '<a href="javascript:void(0)" title="' + ele.value + '" onclick="changeMe(this)">' + msg + '</a>';
                    let p = ele.parentElement;
                    p.removeChild(ele);
                    p.innerHTML += html;
                } else {
                    common.showFailMsg('操作失败');
                }
            },
            error: function () {
                common.showFailMsg('未知错误，请稍后重试');
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

    window.updateOrNewTab = function (url, tit, id) {
        common.updateOrNewTab(url, tit, id);
    }
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

let seAll = ['百度', '搜狗', '360', '百度下拉', '谷歌', '神马', '必应中国', '必应日本'];

function get_se_method(se, uuid) {
    let html = '<div onclick="showSelect(this)" style="width: 100px;display: inline-block;vertical-align: middle" class="layui-unselect layui-form-select">'
        + '<div class="layui-select-title">'
        + '<input type="text" data-uuid="' + uuid + '" style="height: 24px;" placeholder="请选择" value="';
    html += se;
    html += '" readonly="" class="layui-input layui-unselect">'
        + '<i class="layui-edge"></i>'
        + '</div>'
        + '<dl class="layui-anim layui-anim-upbit">';
    for (let i = 0; i < seAll.length; i++) {
        html += '<dd onclick="selectOne(this)" lay-value="';
        html += seAll[i];
        html += '" class="';
        if (seAll[i] === se) {
            html += 'layui-this';
        }
        html += '">';
        html += seAll[i];
        html += '</dd>';
    }
    html += '</dl>'
        + '</div>';
    return html;
}

window.showSelect = function (ele) {
    let flag = true;
    let classList = ele.classList;
    for (let i = 0; i < classList.length; i++) {
        if (classList[i] === 'layui-form-selected') {
            flag = false;
            break;
        }
    }
    if (flag) {
        ele.classList.add('layui-form-selected');
    } else {
        ele.classList.remove('layui-form-selected');
    }
};

function clearThis(ele) {
    let all = ele.parentElement.children;
    for (let i = 0; i < all.length; i++) {
        all[i].classList.remove('layui-this');
    }
}

window.changeMe = function (ele) {
    let html = '<input type="text" autofocus onkeydown="downEnter(this)" onblur="changeExternalAccount(this)" class="layui-input" style="height: 24px;width: 128px;display: inline-block;vertical-align: middle;" value="';
    if (ele.title) {
        html += ele.title;
    }
    html += '">';
    let p = ele.parentElement;
    p.removeChild(ele);
    p.innerHTML += html;
    p.getElementsByTagName('input')[0].focus();
};

function downEnter(ele) {
    if (event.keyCode === 13) {
        changeExternalAccount(ele);
    }
}

function generate_qzsetting_info(contactPerson, data, terminalType, customerUuid) {

    let toStatisticCenterTitle=contactPerson+"-排名漲幅詳情"
    let toStatisticCenterId=customerUuid+'-qz-'+terminalType;
    let computerUrl="/internal/ckpositionsummary/toStatisticCenter/"+customerUuid+"/qz/PC/";
    let mobileUrl="/internal/ckpositionsummary/toStatisticCenter/"+customerUuid+"/qz/Phone/";
    let htm = '';
    let terminalTypeName = '';
    //terminalType === 'PC' ?'<a href="javascript:void(0)"  onclick=updateOrNewTab("' + computerUrl + '","' +toStatisticTitle+ '","' + toStatisticCenterId+ '") > 电脑端</a> ': '<a  href="javascript:void(0)"   onclick=updateOrNewTab("' + mobileUrl + '","' +toStatisticTitle+ '","' + toStatisticCenterId+ '")  >移动端</a>';

    if(terminalType==='PC'){
        if(hasToStatic)
            terminalTypeName='<a href="javascript:void(0)"  onclick=updateOrNewTab("' + computerUrl + '","' +toStatisticCenterTitle+ '","' + toStatisticCenterId+ '") > 电脑端</a> ';
        else
            terminalTypeName='电脑端'
    }else{
        if(hasToStatic)
            terminalTypeName='<a  href="javascript:void(0)"   onclick=updateOrNewTab("' + mobileUrl + '","' +toStatisticCenterTitle+ '","' + toStatisticCenterId+ '")  >移动端</a>';
        else
            terminalTypeName='移动端'
    }
    if (data != null) {
        if (data["count"] > 0) {
            let url = '/internal/qzsetting/toQZSettingsWithCustomerUuid/' + customerUuid + '/' + terminalType;
            contactPerson = contactPerson.replace(/\s+/g, "");
            let title = contactPerson + '-全站';
            let id = contactPerson + '-toQz-' + terminalType;
            htm += '<span>'+ terminalTypeName +' : <a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>' + data["count"] + '</a></span>&nbsp;&nbsp;状态:';
            if (data["active"] > 0) {
                htm += '<span style="color: green;">'+data["active"]+'个续费</span>|'
            }
            if (data["stop"] > 0) {
                htm += '<span style="color: darkred;">'+data["stop"]+'个暂停</span>|'
            }
            if (data["down"] > 0) {
                htm += '<span style="color: grey;">'+data["down"]+'个下架</span>|'
            }
            if(data["other"] > 0) {
                htm += '<span style="color: lightgrey;">'+data["other"]+'个其他</span>|'
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

    let toStatisticTitle=contactPerson+"-排名漲幅詳情"
    let computerUrl="/internal/ckpositionsummary/toStatisticCenter/"+customerUuid+"/"+type+"/PC/";
    let mobileUrl="/internal/ckpositionsummary/toStatisticCenter/"+customerUuid+"/"+type+"/Phone/";
    let toStatisticCenterId=customerUuid+'-'+type+'-'+terminalType;

    let terminalTypeName = '';
    //terminalType === 'PC' ?'<a href="javascript:void(0)"  onclick=updateOrNewTab("' + computerUrl + '","' +toStatisticTitle+ '","' + toStatisticCenterId+ '") > 电脑端</a> ': '<a  href="javascript:void(0)"   onclick=updateOrNewTab("' + mobileUrl + '","' +toStatisticTitle+ '","' + toStatisticCenterId+ '")  >移动端</a>';

    if(terminalType==='PC'){
        if(hasToStatic)
            terminalTypeName='<a href="javascript:void(0)"  onclick=updateOrNewTab("' + computerUrl + '","' +toStatisticTitle+ '","' + toStatisticCenterId+ '") > 电脑端</a> ';
        else
            terminalTypeName='电脑端'
    }else{
        if(hasToStatic)
            terminalTypeName='<a  href="javascript:void(0)"   onclick=updateOrNewTab("' + mobileUrl + '","' +toStatisticTitle+ '","' + toStatisticCenterId+ '")  >移动端</a>';
        else
            terminalTypeName='移动端'
    }

    if (data !== null) {
        if (data.totalCount > 0) {
            htm += '<span>' + terminalTypeName + ' : <a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")>' + data.totalCount + '</a></span><span>(';
            if (data.totalCount === data.activeCount) {
                htm += '<span style="color: green;">激活</span>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","0","' + type + '","' + terminalType + '",this)>暂停关键字</a>'
            } else if (data.totalCount > 0 && data.activeCount > 0) {
                htm += '<span style="color: yellowgreen;">部分暂停</span>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","0","' + type + '","' + terminalType + '",this)>暂停关键字</a>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","1","' + type + '","' + terminalType + '",this)>激活关键字</a>'
            } else {
                htm += '<span style="color: red;">暂停</span>' +
                    '|<a href="javascript:void(0)" onclick=changeCustomerKeywordStatus("' + customerUuid + '","1","' + type + '","' + terminalType + '",this)>激活关键字</a>'
            }
            htm += ')</span>';
            if (data.invalidDaysStopCount > 0 || data.optimizeStopCount > 0 || data.noEffectStopCount > 0){
                htm += '<br>';
                if (data.invalidDaysStopCount > 0){
                    htm += '<span>无法操作 : <a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '/invalidDaysStopCount","' + title + '","' + id + '")>' + data.invalidDaysStopCount + '  </a></span>';
                }
                if (data.optimizeStopCount > 0){
                    htm += '<span>重复暂停 : <a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '/optimizeStopCount","' + title + '","' + id + '")>' + data.optimizeStopCount + '  </a></span>';
                }
                if (data.noEffectStopCount > 0){
                    htm += '<span>无效暂停 : <a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '/noEffectStopCount","' + title + '","' + id + '")>' + data.noEffectStopCount + '  </a></span>';
                }
            }
        } else {
            htm += '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")><span>查看' + terminalTypeName + '关键字</span></a>';
        }
    } else {
        htm += '<a href="javascript:void(0)" onclick=updateOrNewTab("' + url + '","' + title + '","' + id + '")><span>查看关键字</span></a>';
    }
    htm += '<br>';
    return htm;
}
