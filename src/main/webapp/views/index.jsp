<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%@ include file="/commons/basejs.jsp" %>

    <%@ include file="/commons/loadjs.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${staticPath }/static/ztree/css/zTreeStyle.css"/>
    <script type="text/javascript" src="${staticPath }/static/extJs-min.js"></script>
    <script type="text/javascript" src="${staticPath }/static/ztree/js/jquery.ztree.core.js"></script>
    <title>权限管理</title>
    <script type="text/javascript">
        var index_tabs;
        var indexTabsMenu;
        var indexMenuZTree;
        $(function () {
            $('#index_layout').layout({fit: true});
            index_tabs = $('#index_tabs').tabs({
                fit: true,
                border: false,
                onContextMenu: function (e, title) {
                    e.preventDefault();
                    indexTabsMenu.menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    }).data('tabTitle', title);
                },
                tools: [{
                    iconCls: 'fi-home',
                    handler: function () {
                        index_tabs.tabs('select', 0);
                    }
                }, {
                    iconCls: 'fi-loop',
                    handler: function () {
                        refreshTab();
                    }
                }, {
                    iconCls: 'fi-x',
                    handler: function () {
                        var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
                        var tab = index_tabs.tabs('getTab', index);
                        if (tab.panel('options').closable) {
                            index_tabs.tabs('close', index);
                        }
                    }
                }]
            });
            // 选项卡菜单
            indexTabsMenu = $('#tabsMenu').menu({
                onClick: function (item) {
                    var curTabTitle = $(this).data('tabTitle');
                    var type = $(item.target).attr('type');
                    if (type === 'refresh') {
                        refreshTab();
                        return;
                    }
                    if (type === 'close') {
                        var t = index_tabs.tabs('getTab', curTabTitle);
                        if (t.panel('options').closable) {
                            index_tabs.tabs('close', curTabTitle);
                        }
                        return;
                    }
                    var allTabs = index_tabs.tabs('tabs');
                    var closeTabsTitle = [];
                    $.each(allTabs, function () {
                        var opt = $(this).panel('options');
                        if (opt.closable && opt.title != curTabTitle
                            && type === 'closeOther') {
                            closeTabsTitle.push(opt.title);
                        } else if (opt.closable && type === 'closeAll') {
                            closeTabsTitle.push(opt.title);
                        }
                    });
                    for (var i = 0; i < closeTabsTitle.length; i++) {
                        index_tabs.tabs('close', closeTabsTitle[i]);
                    }
                }
            });

            indexMenuZTree = $.fn.zTree.init($("#layout_west_tree"), {
                data: {
                    key: {
                        name: "text"
                    },
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "pid",
                        rootPId: 1
                    }
                },
                async: {
                    enable: true,
                    url: "${path}/resource/tree",
                    dataFilter: function (treeId, parentNode, responseData) {
                        if (responseData) {
                            for (var i = 0; i < responseData.length; i++) {
                                var node = responseData[i];
                                if (node.state == "open") {
                                    node.open = true;
                                }
                            }
                        }
                        return responseData;
                    }
                },
                callback: {
                    onClick: function (event, treeId, node) {
                        var opts = {
                            title: node.text,
                            border: false,
                            closable: true,
                            fit: true,
                            iconCls: node.iconCls
                        };
                        var url = node.attributes;
                        if (url && url.indexOf("http") == -1) {
                            url = '${path }' + url;
                        }
                        if (node.openMode == 'iframe') {
                            opts.content = '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.5%;"></iframe>';
                            addTab(opts);
                        } else if (url) {
                            opts.href = url;
                            addTab(opts);
                        }
                    }
                }
            });
            <c:if test="${requestURI!=null}">
            $("[title='" + "${requestURI}" + "']").click();
            </c:if>
        });

        function addTab(opts) {
            var t = $('#index_tabs');
            if (t.tabs('exists', opts.title)) {
                t.tabs('select', opts.title);
            } else {
                t.tabs('add', opts);
            }
        }

        function refreshTab() {
            var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
            var tab = index_tabs.tabs('getTab', index);
            var options = tab.panel('options');
            if (options.content) {
                index_tabs.tabs('update', {
                    tab: tab,
                    options: {
                        content: options.content
                    }
                });
            } else {
                tab.panel('refresh', options.href);
            }
        }
    </script>
    <script type="text/javascript">
        //点击菜单
        function openUrl(url,title,iconCls,openMode) {
            var opts = {
                title : title,
                border : false,
                closable : true,
                fit : true,
                iconCls :iconCls
            };
            if("${pageContext.request.getAttribute("javax.servlet.forward.request_uri")}"!='/login' && "${pageContext.request.getAttribute("javax.servlet.forward.request_uri")}"!='/index')
            {
                window.location.href=url+"?resource="+"${pageContext.request.getAttribute('javax.servlet.forward.request_uri')}";
            }

            if (url && url.indexOf("http") == -1) {
                url = '${path }' + url;
            }
            if (openMode == 'iframe') {
                opts.content = '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.5%;"></iframe>';
                addTab(opts);
            } else if (url) {
                opts.href = url+"?resource="+"${pageContext.request.getAttribute("javax.servlet.forward.request_uri")}";
                addTab(opts);
            }
        }
        function logout(){
            $.messager.confirm('提示','确定要退出?',function(r){
                if (r){
                    $.post('${path }/logout', function(result) {
                        if(result.success){
                            window.location.href='${path }';
                        }
                    }, 'json');
                }
            });
        }

        $.modalDialog = function(options) {
            if ($.modalDialog.handler == undefined) {// 避免重复弹出
                var opts = $.extend({
                    title : '',
                    width : 840,
                    height : 680,
                    modal : true,
                    onClose : function() {
                        $.modalDialog.handler = undefined;
                        $(this).dialog('destroy');
                    },
                    onOpen : function() {
                    }
                }, options);
                opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
                return $.modalDialog.handler = $('<div/>').dialog(opts);
            }
        };

        function editUserPwd() {
            parent.$.modalDialog({
                title : '修改密码',
                width : 230,
                height : 170,
                href : '${path }/user/editPwdPage',
                buttons : [ {
                    text : '确定',
                    handler : function() {
                        var f = parent.$.modalDialog.handler.find('#editUserPwdForm');
                        f.submit();
                    }
                } ]
            });
            parent.$.modalDialog.handler.dialog("open");
            parent.$.modalDialog.handler.window("resize",{top:$(document).scrollTop() + 100});
        }
    </script>
</head>
<body>
<%--<%@include file="/menu.jsp"%>--%>

<nav class="navbar navbar-inverse navbar-fixed-top container">
    <div class="content" style="width: 100%;height:30px;">
        <ul class="venus-menu" style="">
            <li style="" pid="" lid="1">

                <a href="#" target="_blank" title="#"><i class="fi-folder"></i> 权限管理</a>

                <ul id="1">
                    <li style="" pid="1" lid="11">

                        <a href="/resource/manager" target="_blank" onclick="openUrl('/resource/manager','资源管理','fi-database','ajax')"
                           title="/resource/manager"><i class="fi-database"></i> 资源管理</a>

                    </li>
                    <li style="" pid="1" lid="12">

                        <a href="/role/manager" target="_blank" onclick="openUrl('/role/manager','角色管理','fi-torso-business','ajax')" title="/role/manager"><i
                                class="fi-torso-business"></i> 角色管理</a>

                    </li>
                    <li style="" pid="1" lid="13">

                        <a href="/user/manager" target="_blank" onclick="openUrl('/user/manager','用户管理','fi-torsos-all','ajax')" title="/user/manager"><i
                                class="fi-torsos-all"></i> 用户管理</a>

                    </li>
                    <li style="" pid="1" lid="14">

                        <a href="/organization/manager" target="_blank" onclick="openUrl('/organization/manager','部门管理','fi-results-demographics','ajax')"
                           title="/organization/manager"><i class="fi-results-demographics"></i> 部门管理</a>

                    </li>
                </ul>
                <ul id="1"></ul>
                <ul id="1"></ul>
                <ul id="1"></ul>
            </li>
            <div style="float: right;margin:7px 10px 0px 0px;">
                <b class="fi-torso icon-black" style="font-size: 12px;">&nbsp;<shiro:principal property="name"></shiro:principal></b>|
                <shiro:hasPermission name="/user/editPwdPage">
                    <span class="fi-unlock icon-green" style="font-size: 12px;color: green"></span>
                    <a href="javascript:void(0)" onclick="editUserPwd()"  style="text-decoration: none;font-size: 12px;color: black">修改密码</a>|
                </shiro:hasPermission>
                <a class="fi-x" href="javascript:void(0)" onclick="logout()" style="text-decoration: none;font-size: 12px;">&nbsp;安全退出</a>
            </div>
        </ul>
    </div>
</nav>
<div id="loading" style="position: fixed;top: -50%;left: -50%;width: 200%;height: 200%;background: #fff;z-index: 100;overflow: hidden;">
    <img src="${staticPath }/static/style/images/ajax-loader.gif" style="position: absolute;top: 0;left: 0;right: 0;bottom: 0;margin: auto;"/>
</div>
<div id="index_layout">
    <%--<div data-options="region:'north',border:false" style="overflow: hidden;height: 30px">
        <div style="z-index: 20;">
            <%@include file="/menu.jsp"%>
        </div>
    </div>--%>
    <ul id="layout_west_tree" class="ztree" style="display: none"></ul>
    <div data-options="region:'center'" style="overflow: hidden;">
        <div id="index_tabs" style="overflow: hidden;">
            <script src='${path}/resource/manager'></script>
            <style>
                .pro_name a {
                    color: #4183c4;
                }

                .osc_git_title {
                    background-color: #d8e5f1;
                }

                .osc_git_box {
                    background-color: #fafafa;
                }

                .osc_git_box {
                    border-color: #ddd;
                }

                .osc_git_info {
                    color: #666;
                }

                .osc_git_main a {
                    color: #4183c4;
                }
            </style>
        </div>
    </div>
</div>
<div id="tabsMenu">
    <div data-options="iconCls:'fi-loop'" type="refresh" style="font-size: 12px;">刷新</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'fi-x'" type="close" style="font-size: 12px;">关闭</div>
    <div data-options="iconCls:''" type="closeOther">关闭其他</div>
    <div data-options="iconCls:''" type="closeAll">关闭所有</div>
</div>

<!--[if lte IE 7]>
<div id="ie6-warning"><p>您正在使用 低版本浏览器，在本页面可能会导致部分功能无法使用。建议您升级到 <a href="http://www.microsoft.com/china/windows/internet-explorer/" target="_blank">Internet
    Explorer 8</a> 或以下浏览器：
    <a href="http://www.mozillaonline.com/" target="_blank">Firefox</a> / <a href="http://www.google.com/chrome/?hl=zh-CN" target="_blank">Chrome</a> / <a
            href="http://www.apple.com.cn/safari/" target="_blank">Safari</a> / <a href="http://www.operachina.com/" target="_blank">Opera</a></p></div>
<![endif]-->
<style>
    /*ie6提示*/
    #ie6-warning {
        width: 100%;
        position: absolute;
        top: 0;
        left: 0;
        background: #fae692;
        padding: 5px 0;
        font-size: 12px
    }

    #ie6-warning p {
        width: 960px;
        margin: 0 auto;
    }
</style>
</body>
</html>