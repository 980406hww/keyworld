getHeight();

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
    element.on('tab(groupSettingTab)', function (data) {
        console.log(this); //当前Tab标题所在的原始DOM元素
        // console.log(data.index); //得到当前Tab的所在下标
        // console.log(data.elem); //得到当前的Tab大容器
    });
    $(window).resize(function(){
        getHeight();
    });
    initLayPage(formToJsonObject('searchForm'));
    function formToJsonObject (form_id) {
        var formData = decodeURIComponent($("#" + form_id).serialize(), true);
        formData = formData.replace(/&/g, "\",\"");
        formData = formData.replace(/=/g, "\":\"");
        formData = "{\"" + formData + "\"}";
        formData = $.parseJSON(formData);
        return formData;
    }
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
                layer.msg('获取用户失败，请稍后再试', {icon: 5});
            }
        });
    }
    function init_data(data) {
        $("#data_list").html('');
        $.each(data, function (index, item) {
            console.log(item.groupSettings)
            let htm = '';
            htm += '<div class="data-item">\n'
                +'<input type="hidden" name="operationCombineUuid" value="'+item.uuid+'}">'
                + '                        <div class="layui-row">\n'
                + '                            <div class="layui-col-md12">\n'
                + '                                <div class="data-head">\n'
                + '                                     <div class="layui-col-md1 skip">'+item.operationCombineName+'</div>\n'
                + '                                     <div class="layui-col-md1">'+item.creator+'</div>\n'
                + '                                     <div class="layui-col-md1">最大无效点击数:'+item.maxInvalidCount+'</div>\n'
                + '                                     <div class="layui-col-md6">分组:'+getGroupNameStrByUuid(item.uuid)+'</div>\n'
                + '                                     <div class="layui-col-md3 operation" title="操作">\n'
                + '                                         <div class="layui-col-md2">'
                + '                                              <a href="javascript:void(0)" onclick=showGroupQueueDialog("' + item.uuid + '")>分组详情 </a>'
                + '                                          </div>\n'
                + '                                         <div class="layui-col-md3">'
                + '                                              <a href="javascript:void(0)" onclick=showGroupSettingDialog("' + item.uuid + '")>新增操作组设置 </a>'
                + '                                         </div>\n'
                + '                                         <div class="layui-col-md4">'
                + '                                              <a href="javascript:void(0)" onclick=showUpdateGroupDialog("' + item.uuid + '")>批量修改操作组设置 </a>'
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
                    + '                                            <div class="layui-col-md1"style="width: 10%">分组设置占比</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 8%">网站统计</div>\n'
                    + '                                            <div class="layui-col-md1"style="width: 8%">目标网站</div>\n'
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
                        + '                                         <div class="layui-col-md1"style="width: 10%">'
                        +                                               groupSetting.machineUsedPercent+'%'
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md1"style="width: 8%">'
                        +                                               getStatisticText(groupSetting.disableStatistics)
                        + '                                         </div>\n'
                        + '                                         <div class="layui-col-md1"style="width: 8%">'
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
                        + '                                        <div class="layui-col-md1"style="width: 10%">'
                        + '                                             <a href="javascript:void(0)" onclick=showGroupSettingDialog("' + groupSetting.uuid +'")>修改 </a>'
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


            $("#data_list").append(htm)
            $("#data_list").trigger("create");
        })
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

    window.getGroupNameStrByUuid = function (uuid) {
        return uuid+'分组分组分组'
    };

    window.showGroupQueueDialog = function (uuid) {

    };

    window.showGroupSettingDialog = function (uuid) {

    };

    window.showUpdateGroupDialog = function (uuid) {

    };

    window.delOperationCombine = function (uuid) {

    };

    window.delGroupSetting = function (uuid) {

    };
});