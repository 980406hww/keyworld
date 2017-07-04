<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<header class="main-header">
	<!-- Logo -->
	<a ui-sref="home" class="logo"> 
		<span class="logo-lg">关键词管理系统</span>
	</a>

	<!-- Header Navbar -->
	<nav class="navbar navbar-static-top" role="navigation">
		<div class="navbar-custom-menu">
			<ul class="nav navbar-nav">
				<!--User Account Menu-->
				<li class="dropdown user user-menu">
					<!-- Menu Toggle Button --> 
					<a href="${ctx}/logout" title="Logout" class="dropdown-toggle"> 
						<span class="fa fa-sign-out" id="username">退出</span>
					</a>
				</li>
			</ul>
		</div>
	</nav>
</header>

