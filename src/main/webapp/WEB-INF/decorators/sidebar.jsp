<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<aside class="main-sidebar">
	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">

		<!-- Sidebar Menu -->
		<aside class="main-sidebar">
			<ul class="sidebar-menu">
					<li><a href="${ctx}/maintains/userInfo/views/userInfo.jsp"><i class="fa fa-user-plus"></i>我的信息</a></li>
					<li><a href="${ctx}/maintains/keyword/views/keywordList.jsp"><i class="fa fa-key"></i>关键词管理</a></li>
					<li><a href="${ctx}/maintains/QZSetting/views/reportList.jsp"><i class="fa fa-table"></i>报表下载</a></li>
			</ul>
		</aside>
		<!-- /.sidebar-menu -->
	</section>
	<!-- /.sidebar -->
</aside>