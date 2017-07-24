<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<div id="menu">
	<ul id="nav">
	    <li class="mainlevel" id="mainlevel_01"><a href="#">关键字管理</a>
	    <ul id="sub_01">
	    <li><a href="/customerkeyword/keywordfinder.jsp">关键字统计</a></li>
	    <li><a href="/fumianlist/list.jsp">关键字负面清单</a></li>
	    <li><a href="/SuperUserSimpleKeywordList.xls">关键字简化模板下载</a></li>
	    <li><a href="/SuperUserFullKeywordList.xls">关键字完整模板下载</a></li>
	    </ul>
	    </li>
	    
	    <li class="mainlevel" id="mainlevel_02"><a href="#">客户管理</a>
	    <ul id="sub_06">
	    <li><a href="/customer/customerlist.jsp">客户列表</a></li>
	    <li><a href="/customer/addcustomer.jsp">增加客户</a></li>
	    <!-- <li><a href="/customerkeyword/customercollectfeelist.jsp">客户收费清单</a></li> -->
	    </ul>
	    </li>
	    
	    <li class="mainlevel" id="mainlevel_02"><a href="#">其他</a>
	    <ul id="sub_06">
	    <li><a href="/client/clientlist.jsp">查看终端状态</a></li>
		<li><a href="/client/clientstat.jsp">查看终端统计</a></li>
		<li><a href="/client/clientstat_group.jsp">查看终端分组统计</a></li>

	    <li><a href="/qzsetting/list.jsp?chargeDays=NaN">全站设置信息</a></li>
		<li><a href="/client/vpslist.jsp">查看开机信息</a></li>
	    <li><a href="/refresh/refreshstat.jsp">查看刷量统计</a></li>
	    <li><a href="/customerkeyword/verification.jsp">验证</a></li>
	    </ul>
	    </li>
	    
	    <li class="mainlevel" id="mainlevel_02"><a href="#">用户管理</a>
	    <ul id="sub_06">
	    <li><a href="/user/userlist.jsp">用户列表</a></li>
	    <li><a href="/user/updatepassword.jsp">修改密码</a></li>
	    <li><a href="/user/logout.jsp">退出</a></li>
	    </ul>
	    </li>
	    <div class="clear"></div>
	</ul>
</div>