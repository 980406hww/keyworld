getHeight();
var sign = false;
function getHeight(){
    let b = document.getElementById('groupSettingBody');
    let h = window.innerHeight || document.body.offsetHeight;
    b.style.height = (h - 155) + 'px';
}

layui.use(['element', 'form', 'jquery', 'laypage', 'okLayer', 'layer'], function () {
    var element = layui.element;
    var form = layui.form;
    var $ = layui.jquery;
    var layer = layui.layer;
    var okLayer = layui.okLayer;
    var laypage = layui.laypage;
    var operationTypes ;
    element.on('tab(groupSettingTab)', function (data) {
        $('#terminalType').val($(this).text());
        init_operationType();
        initLayPage(formToJsonObject('searchForm'));
    });

    $(window).resize(function(){
        getHeight();
    });
    init();

    function init(){
        init_operationType();
        initLayPage(formToJsonObject('searchForm'));
    }

    function init_operationType(){
        let terminalType = $('#terminalType').val();
        $.ajax({
            url: '/internal/groupsetting/getOperationTypes/'+terminalType,
            dataType: 'json',
            type: 'post',
            async: false,
            dataType:'json',
            success: function (res) {
                if (res.code === 200){
                    let data =res.data;
                    operationTypes = data;
                    $("#operationType").empty().append('<option value="">请选择操作类型</option>');
                    $.each(data, function (index, item) {
                        $('#operationType').append(
                            '<option value="' + item + '">' + item + '</option>');// 下拉菜单里添加元素
                    });
                    form.render("select");
                }
            }
        });
    }

    form.on('select(operationTypeFilter)', function(data){
        if(data.elem.value !== ''){
            $('#hasOperation').prop("checked",true);
            $('#noOperation').prop("checked",false);
            $('#hasRemainingAccount').prop("checked",false);
        }else {
            $('#hasOperation').prop("checked",false);
            $('#noOperation').prop("checked",false);
            $('#hasRemainingAccount').prop("checked",false);
        }

        form.render('checkbox');
    });

    form.on('checkbox(oneChecked)', function(data){
        if (data.elem.checked){
            switch(data.elem.id){
                case 'hasOperation':
                    $('#noOperation').prop("checked",false);
                    $('#hasRemainingAccount').prop("checked",false);
                    break;
                case 'noOperation':
                    $('#hasOperation').prop("checked",false);
                    $('#hasRemainingAccount').prop("checked",false);
                    break;
                case 'hasRemainingAccount':
                    $('#noOperation').prop("checked",false);
                    $('#hasOperation').prop("checked",false);
                    break;
            }
            form.render('checkbox');
            $('#searchBtn').click();
        }

    });

    function formToJsonObject (form_id) {
        var formData = decodeURIComponent($("#" + form_id).serialize(), true);
        formData = formData.replace(/&/g, "\",\"");
        formData = formData.replace(/=/g, "\":\"");
        formData = "{\"" + formData + "\"}";
        formData = $.parseJSON(formData);
        return formData;
    }

    form.on('submit(search)', function (data) {
        var pageConf = data.field;
        if (pageConf.hasOperation){
            pageConf.hasOperation = true;
        }
        if (pageConf.noOperation){
            pageConf.hasOperation = false;
        }
        if (pageConf.hasRemainingAccount){
            pageConf.hasRemainingAccount = true;
        }else{
            pageConf.hasRemainingAccount = false;
        }
        pageConf.limit = 50;
        pageConf.page = 1;
        initLayPage(pageConf);
        return false;
    });

    window.toOperationCombineAdd = function(){
        let data = {};
        data.operationTypes = operationTypes;
        data.operationCombineUuid = null;
        data.remainAccount = 100;
        data.currentAccount = 0;
        data.terminalType = $('#terminalType').val();
        okLayer.open("终端管理 / 分组信息 / 添加操作组合", "/internal/groupsetting/toGroupSettingAdd", "60%", "90%", function(layero){
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                let pageConf = formToJsonObject('searchForm');
                initLayPage(pageConf);
                sign = false;
            }
        });
    };

    window.toGroupSettingAdd = function(operationCombineUuid,operationCombineName,maxInvalidCount,remainAccount){
        let data = {};
        let groupNames = $('#groupNameStr' + operationCombineUuid).text() === '暂无' ? '' : $('#groupNameStr' + operationCombineUuid).text();
        data.operationCombineUuid = operationCombineUuid;
        data.operationCombineName = operationCombineName;
        data.maxInvalidCount = maxInvalidCount;
        data.groupNames = groupNames;
        data.operationTypes = operationTypes;
        data.remainAccount = remainAccount;
        data.currentAccount = 0;
        data.terminalType = $('#terminalType').val();
        okLayer.open("终端管理 / 分组信息 / 添加操作组合", "/internal/groupsetting/toGroupSettingAdd", "60%", "90%", function(layero){
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                let pageConf = formToJsonObject('searchForm');
                initLayPage(pageConf);
                sign = false;
            }
        });
    };

    window.toGroupSettingUpdate = function(operationCombineUuid, operationCombineName, remainingAccount, machineUsedPercent, uuid){
        let data = {};
        data.uuid = uuid;
        data.operationCombineName = operationCombineName;
        data.operationCombineUuid = operationCombineUuid;
        data.operationTypes = operationTypes;
        data.remainAccount = remainingAccount;
        data.currentAccount = machineUsedPercent;
        data.terminalType = $('#terminalType').val();
        okLayer.open("终端管理 / 分组信息 / 更新操作组合<span style='color: red'>(需要更新的字段请点击标红)</span>", "/internal/groupsetting/toGroupSettingUpdate", "60%", "90%", function(layero){
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                let pageConf = formToJsonObject('searchForm');
                initLayPage(pageConf);
                sign = false;
            }
        });
    };

    window.toGroupSettingBatchUpdate = function(operationCombineUuid, operationCombineName){
        let data = {};
        data.uuid = null;
        data.operationCombineName = operationCombineName;
        data.operationCombineUuid = operationCombineUuid;
        data.operationTypes = operationTypes;
        data.remainAccount = 100;
        data.currentAccount = 0;
        data.terminalType = $('#terminalType').val();
        okLayer.open("终端管理 / 分组信息 / 批量设置操作组合<span style='color: red'>(需要更新的字段请点击标红)</span>", "/internal/groupsetting/toGroupSettingUpdate", "60%", "90%", function(layero){
            window[layero.find("iframe")[0]["name"]].initForm(data);
        }, function () {
            if (sign) {
                let pageConf = formToJsonObject('searchForm');
                initLayPage(pageConf);
                sign = false;
            }
        });
    };

    function initLayPage(pageConf) {
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
            url: '/internal/groupsetting/getGroupSettings',
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
                            pageConf.page = obj.curr;
                            pageConf.limit = obj.limit;
                            initLayPage(pageConf);
                        }
                    }
                });
                init_data(result.data);

            },
            error: function () {
                layer.msg('获取分组信息失败，请稍后再试', {icon: 5});
            }
        });
    }

    function init_data(data) {
        $("#data_list").html('');
        $.each(data, function (index, item) {
            let htm = '';
            htm += '<div class="data-item">\n'
                +'<input type="hidden" name="operationCombineUuid" value="'+item.uuid+'}">'
                + '                        <div class="layui-row">\n'
                + '                            <div class="layui-col-md12">\n'
                + '                                <div class="data-head">\n'
                + '                                     <div class="layui-col-md1 skip importantText" title="'+item.operationCombineName+'---'+item.remainingAccount+'%">'+item.operationCombineName+'</div>\n'
                + '                                     <div class="layui-col-md1">'+item.creator+'</div>\n'
                + '                                     <div class="layui-col-md1" onclick=changeMaxInvalidCount("'+item.uuid+'")>最大无效点击数:<span id="maxInvalidCount'+item.uuid+'">'+item.maxInvalidCount+'</span></div>\n'
                + '                                     <div class="layui-col-md6 skip" onclick=changeGroupNames("'+item.uuid+'")>分组:<span id="groupNameStr'+item.uuid+'">'+getGroupNameStrByUuid(item.uuid)+'</span></div>\n'
                + '                                     <div class="layui-col-md3 head-operation" title="操作">\n'
                + '                                         <div class="layui-col-md2">'
                + '                                              <a href="javascript:void(0)" onclick=showGroupQueueDialog("' + item.uuid + '")>分组详情 </a>'
                + '                                          </div>\n'
                + '                                         <div class="layui-col-md3">'
                + '                                              <a href="javascript:void(0)" onclick=toGroupSettingAdd("' + item.uuid + '","' + item.operationCombineName + '","' + item.maxInvalidCount + '","'+item.remainingAccount+'")>新增操作组 </a>'
                + '                                         </div>\n'
                + '                                         <div class="layui-col-md4">'
                + '                                              <a href="javascript:void(0)" onclick=toGroupSettingBatchUpdate("' + item.uuid +'","' + item.operationCombineName +'")>批量修改操作组 </a>'
                + '                                         </div>\n'
                + '                                         <div class="layui-col-md3">'
                + '                                              <a href="javascript:void(0)" onclick=delOperationCombine("' + item.uuid + '")>删除操作组合 </a>'
                + '                                         </div>\n'
                + '                                     </div>\n'
                + '                            </div>'
                + '                           </div>';
            if(item.groupSettings.length > 0){
                htm += '                            <div class="layui-col-md12">\n'
                    + '                                <div class="data-body">\n'
                    + '                                    <div class="body-title">\n'
                    + '                                            <div class="layui-col-md1" style="width: 10%">操作类型</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 9%">分组设置占比</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 8%">网站统计</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 9%">是否访问目标网站</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 7%">页数</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 7%">每页条数</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 10%">Cookie设置</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 10%">进入页次数</div>\n'
                    + '                                            <div class="layui-col-md2"style="width: 10%">刷多少个词换IP</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 10%">没结果随机点</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 10%">操作</div>\n'
                    + '                                    </div>\n';
                $.each(item.groupSettings, function (index2, groupSetting) {
                    htm +='                                    <div class="body-content">\n'
                        + '                                         <div class="layui-col-md1" style="width: 10%">'
                        +                                               groupSetting.operationType
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md1"style="width: 9%">'
                        +                                               groupSetting.machineUsedPercent+'%'
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md1"style="width: 8%">'
                        +                                               getStatisticText(groupSetting.disableStatistics)
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md1"style="width: 9%">'
                        +                                               getVisitWebsiteText(groupSetting.disableVisitWebsite)
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md1"style="width: 7%">'
                        +                                               groupSetting.page
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md1"style="width: 7%">'
                        +                                               getPageSizeText(groupSetting.pageSize)
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md1"style="width: 10%">'
                        +                                               getCookieText(groupSetting.clearCookie)
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md1"style="width: 10%">'
                        +                                               groupSetting.entryPageMinCount + ' - ' + groupSetting.entryPageMaxCount
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md2"style="width: 10%">'
                        +                                               groupSetting.optimizeKeywordCountPerIP
                        + '                                        </div>\n'
                        + '                                        <div class="layui-col-md1"style="width: 10%">'
                        +                                               getRandomlyClickNoResultText(groupSetting.randomlyClickNoResult)
                        + '                                        </div>\n'
                        + '                                        <div class="layui-col-md1 body-operation"style="width: 10%">'
                        + '                                             <a href="javascript:void(0)" onclick=toGroupSettingUpdate("' + item.uuid +'","' + item.operationCombineName +'","' + item.remainingAccount +'","' + groupSetting.machineUsedPercent +'","' + groupSetting.uuid +'")>修改 </a>'
                        + '                                             <a href="javascript:void(0)" onclick=delGroupSetting("' + groupSetting.uuid + '")>删除 </a>'
                        + '                                         </div>'
                        + '                                    </div>\n';
                });
                htm +='                                </div>\n'
                    + '                            </div>\n'
                    + '                        </div>\n'
                    + '                    </div>\n'
                    + '                </div>';
            }
            $("#data_list").append(htm);
        })
    }

    window.getGroupNameStrByUuid = function(uuid){
        $.ajax({
            url: '/internal/operationcombine/getGroupNames2/' + uuid,
            type: 'POST',
            dataType:'json',
            success: function (res) {
                if (res.code === 200){
                    let data = res.data;
                    var groupNameStr = "";
                    if (data.length === 0) {
                        groupNameStr = '暂无';
                    } else {
                        $.each(data, function (idx, value) {
                            groupNameStr += value + ",";
                        });
                        groupNameStr = groupNameStr.substring(0, groupNameStr.length-1);
                    }
                    $('#groupNameStr'+uuid).text(groupNameStr);
                    $('#groupNameStr'+uuid).attr("title",groupNameStr);

                }else {
                    show_layer_msg('获取操作组合下的分组数据失败！', 5, );
                }
            },
            error: function () {
                show_layer_msg('获取操作组合下的分组数据失败！', 5, );
            }
        });
    };

    window.changeMaxInvalidCount = function(operationCombineUuid){
        let oldMaxInvalidCount = $('#maxInvalidCount'+operationCombineUuid).text();
        layer.prompt({
            formType: 3,
            value: oldMaxInvalidCount,
            title: '新无效最大点击数',
            area: ['220px', '60px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入最大无效点击数！', 5, null, 1000);
                    return;
                } else if (!/^\d+$/.test(value)) {
                    show_layer_msg('请输入正确数字！', 5, null, 1000);
                    return;
                }
                var postData = {};
                postData.uuid = operationCombineUuid;
                postData.maxInvalidCount = value;
                $.ajax({
                    url: '/internal/operationcombine/updateMaxInvalidCount2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            show_layer_msg('操作成功', 6, );
                            $('#maxInvalidCount'+operationCombineUuid).text(value)
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
    };

    window.uniqueGroupName = function unique(a) {
        var seen = {};
        return a.filter(function(item) {
            return seen.hasOwnProperty(item) ? false : (seen[item] = true);
        });
    };

    window.changeGroupNames = function(operationCombineUuid){
        let oldGroupNameStr = $('#groupNameStr'+operationCombineUuid).text();
        layer.prompt({
            formType: 2,
            value: oldGroupNameStr === '暂无' ? '' : oldGroupNameStr,
            title: '新分组标签',
            area: ['600px', '80px'], //自定义文本域宽高
            yes: function (index, layero) {
                var index2 = index;
                var value = layero.find(".layui-layer-input").val();
                if (value === '') {
                    show_layer_msg('请输入新分组标签！', 5, null, 1000);
                    return;
                }
                value = value.replace(/( )+/g,"").replace(/(，)+|(,)+/g, ",").split(",");
                let groups = uniqueGroupName(value);
                var postData = {};
                postData.operationCombineUuid = operationCombineUuid;
                postData.groupNames = groups;
                $.ajax({
                    url: '/internal/group/saveGroupsBelowOperationCombine2',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 10000,
                    type: 'POST',
                    success: function (result) {
                        if (result.code === 200) {
                            show_layer_msg('操作成功', 6);
                            $('#groupNameStr'+operationCombineUuid).text(groups.join(","));
                            $('#groupNameStr'+operationCombineUuid).attr("title",groups.join(","));
                        } else {
                            show_layer_msg('操作失败', 5);
                        }
                        layer.close(index2);
                    },
                    error: function () {
                        show_layer_msg('未知错误，请稍后重试', 5);
                        layer.close(index2);
                    }
                });

            }
        });
    };

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

    window.getStatisticText  = function(disableStatistics){
        let htm = '';
        switch (disableStatistics) {
            case 0:
                htm = '开放';
                break;
           default:
                htm = '关闭';

        }
        return htm;
    };

    window.getVisitWebsiteText  = function(disableVisitWebsite){
        let htm = '';
        switch (disableVisitWebsite) {
            case 0:
                htm = '访问';
                break;
            default:
                htm = '不访问';
        }
        return htm;
    };

    window.getPageSizeText  = function(pageSize){
        let htm = '';
        switch (pageSize) {
            case 0:
                htm = '10';
                break;
            case 1:
                htm = '20';
                break;
            default:
                htm = '50';
        }
        return htm;
    };

    window.getCookieText  = function(clearCookie){
        let htm = '';
        switch (clearCookie) {
            case 0:
                htm = '不清理Cookie';
                break;
            case 1:
                htm = '每次都清理Cookie';
                break;
            case 2:
                htm = '随机操作清理Cookie';
                break;
            default:
                htm = 'N次操作清理Cookie';
        }
        return htm;
    };

    window.getRandomlyClickNoResultText  = function(randomlyClickNoResult){
        let htm = '';
        switch (randomlyClickNoResult) {
            case 1:
                htm = '是';
                break;
            default:
                htm = '否';
        }
        return htm;
    };

    window.showGroupQueueDialog = function (uuid) {
        console.log(uuid)
    };

    window.showGroupSettingDialog = function (uuid) {
        console.log(uuid)
    };

    window.showUpdateGroupDialog = function (uuid) {
        console.log(uuid)
    };

    window.delOperationCombine = function (uuid) {
        console.log(uuid)
    };

    window.delGroupSetting = function (uuid) {
        console.log(uuid)
    };
});