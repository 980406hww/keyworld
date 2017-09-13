<%-- [EasyUI] --%>
<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${staticPath }/static/easyui/themes/gray/easyui.css" />
<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${staticPath }/static/easyui/plugins/jquery.messager.js" />
<%-- [扩展JS] --%>
<script type="text/javascript" src="${staticPath }/static/extJs.js" charset="utf-8"></script>
<script type="text/javascript">
    function logout(){
        $.messager.confirm('提示','确定要退出?',function(r){s
            if (r){
                progressLoad();
                $.post('${path }/logout', function(result) {
                    if(result.success){
                        progressClose();
                        window.location.href='${path }';
                    }
                }, 'json');
            }
        });
    }
</script>

<div id="menu">
	<ul id="nav">
	    <li class="mainlevel" id="mainlevel_01"><a href="#">关键字管理</a>
	    <ul id="sub_01">
	    <li><a href="/customerkeyword/keywordfinder.jsp">关键字统计</a></li>
	    <li><a href="/internal/negativelist/searchNegativeLists">关键字负面清单</a></li>
	    <li><a href="/SuperUserSimpleKeywordList.xls">关键字简化模板下载</a></li>
	    <li><a href="/SuperUserFullKeywordList.xls">关键字完整模板下载</a></li>
	    </ul>
	    </li>

	    <li class="mainlevel" id="mainlevel_02"><a href="#">客户管理</a>
	    <ul id="sub_06">
	    <li><a href="/internal/customer/searchCustomers">客户列表</a></li>
	    </ul>
	    </li>

	    <li class="mainlevel" id="mainlevel_02"><a href="#">其他</a>
	    <ul id="sub_06">
	    <li><a href="/client/clientlist.jsp">查看终端状态</a></li>
		<li><a href="/client/clientstat.jsp">查看终端统计</a></li>
		<li><a href="/client/clientstat_group.jsp">查看终端分组统计</a></li>

	    <li><a href="/internal/qzsetting/searchQZSettings">全站设置信息</a></li>
	    <li><a href="/internal/complaints/findTSMainKeywords">投诉专用平台</a></li>
		<li><a href="/client/vpslist.jsp">查看开机信息</a></li>
	    <li><a href="/refresh/refreshstat.jsp">查看刷量统计</a></li>
	    <li><a href="/customerkeyword/verification.jsp">验证</a></li>
	    </ul>
	    </li>

		<li class="mainlevel" id="mainlevel_02"><a target="_blank" href="/login">权限管理</a>

		</li>

	    <li class="mainlevel" id="mainlevel_02"><a href="#">用户管理</a>
	    <ul id="sub_06">
	    <li><a href="/user/userlist.jsp">用户列表</a></li>
	    <li><a href="/user/updatepassword.jsp">修改密码</a></li>
	    <li><a href="javascript:void(0)" onclick="logout()">安全退出</a></li>
	    </ul>
	    </li>
	    <div class="clear"></div>
	</ul>
</div>