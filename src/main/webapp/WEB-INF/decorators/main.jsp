<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<title><sitemesh:title /></title>
<sitemesh:head />
<%@ include file="/WEB-INF/common/common_header.jsp"%>
</head>

<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">
		<%@ include file="header.jsp"%>
		<%@ include file="sidebar.jsp"%>
		<div class="content-wrapper" style="overflow: auto;">
			<sitemesh:body />
		</div>
	</div>
</body>

</html>