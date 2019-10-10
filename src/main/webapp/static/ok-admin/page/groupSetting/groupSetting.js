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
                            pageConf.page = obj.curr;
                            pageConf.limit = obj.limit;
                            initLayPage(pageConf);
                        }
                    }
                });
                init_data(result.data);
                let entryType = $('#entryType').val();
                let postData = {};
                postData.entryType = entryType;
                init_customerTypeCount(postData);
                form.render();
                // layer.msg('加载完成', {icon: 6});
            },
            error: function () {
                layer.msg('获取用户失败，请稍后再试', {icon: 5});
            }
        });
    }
    function init_data(data) {
        $("#data_list").html('');
        $.each(data, function (index, obj) {
            let item = '<div class="data-item">\n'
                + '                        <div class="layui-row">\n'
                + '                            <div class="layui-col-md12">\n'
                + '                                <div class="data-head">\n'
                + '                                <div class="layui-col-md1">操作组合名称</div>\n'
                + '                                <div class="layui-col-md1">创建人</div>\n'
                + '                                <div class="layui-col-md1">最大无效点击数</div>\n'
                + '                                <div class="layui-col-md6">分组:pc_pm_xzhuang,pc_pm_yilufa,pc_pm_zhuoguang,qzw_xl,qzw_zo,wjianwu_out,wjianwu_check</div>\n'
                + '                                <div class="layui-col-md3" title="操作">\n'
                + '                                    <div class="layui-col-md2">分组详情</div>\n'
                + '                                    <div class="layui-col-md3">新增操作组设置</div>\n'
                + '                                    <div class="layui-col-md4">批量修改操作组设置</div>\n'
                + '                                    <div class="layui-col-md3">删除操作组合</div>\n'
                + '                                </div>\n'
                + '                            </div>\n'
                + '                            <div class="layui-col-md12">\n'
                + '                                <div class="data-body">\n'
                + '                                    <div class="body-title">\n'
                + '                                            <div class="layui-col-md1" style="width: 10%">操作类型</div>\n'
                + '                                            <div class="layui-col-md1"style="width: 10%">分组设置占比</div>\n'
                + '                                            <div class="layui-col-md1"style="width: 8%">网站统计</div>\n'
                + '                                            <div class="layui-col-md1"style="width: 8%">目标网站</div>\n'
                + '                                            <div class="layui-col-md1"style="width: 7%">页数</div>\n'
                + '\n'
                + '                                            <div class="layui-col-md1"style="width: 7%">每页条数</div>\n'
                + '                                            <div class="layui-col-md1"style="width: 10%">Cookie设置</div>\n'
                + '                                            <div class="layui-col-md1"style="width: 10%">进入页次数</div>\n'
                + '                                            <div class="layui-col-md2"style="width: 10%">刷多少个词换IP</div>\n'
                + '                                            <div class="layui-col-md1"style="width: 10%">没结果随机点</div>\n'
                + '                                            <div class="layui-col-md1"style="width: 10%">操作</div>\n'
                + '\n'
                + '                                    </div>\n'
                + '                                    <div class="body-content">\n'
                + '                                        <div class="layui-col-md1" style="width: 10%">pc_pm_1</div>\n'
                + '                                        <div class="layui-col-md1"style="width: 10%">50%</div>\n'
                + '                                        <div class="layui-col-md1"style="width: 8%">开放</div>\n'
                + '                                        <div class="layui-col-md1"style="width: 8%">访问</div>\n'
                + '                                        <div class="layui-col-md1"style="width: 7%">5</div>\n'
                + '\n'
                + '                                        <div class="layui-col-md1"style="width: 7%">10</div>\n'
                + '                                        <div class="layui-col-md1"style="width: 10%">不清理Cookie</div>\n'
                + '                                        <div class="layui-col-md1"style="width: 10%">0 - 0</div>\n'
                + '                                        <div class="layui-col-md2"style="width: 10%">1</div>\n'
                + '                                        <div class="layui-col-md1"style="width: 10%">是</div>\n'
                + '                                        <div class="layui-col-md1"style="width: 10%">修改 | 删除</div>\n'
                + '\n'
                + '                                    </div>\n'
                + '                                </div>\n'
                + '\n'
                + '                            </div>\n'
                + '\n'
                + '                        </div>\n'
                + '                    </div>\n'
                + '                </div>';
            $("#data_list").append(item)
        })
    }

});