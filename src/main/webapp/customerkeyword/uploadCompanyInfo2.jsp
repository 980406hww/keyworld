<%@page contentType="text/html;charset=utf-8" errorPage="/error.jsp"%>
<%@page
	import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="ckm" scope="page"
	class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="um" scope="page"
	class="com.keymanager.manager.UserManager" />

<html>
<head>
<title>简化版关键字列表上传</title>
<script type="text/javascript" src="/common.js">
	
</script>
</head>
<body>

	<form method="post" action="uploadCompanyInfo.jsp">

		<table width="100%">
			<tr>
				<td align="right">
					<div style="text-align: right; width: 100%; font-size: 12px;">
						<a href="/customerkeyword\uploadCompanyInfo.jsp">返回客户列表</a>
					</div>
					<table width=80% style="font-size: 14px;">
						<tr>
							<td><input id="data" name="data"> <input
								type="submit" value="提交"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>


<script>
	function caijiData(group) {
		$(document)
				.ready(
						function() {
							var wholeData = '';
							$(".pj-rank-site-info").each(
									function(index) {
										if (wholeData === '') {
											wholeData = group + '--col--'
													+ $(this).find('h3').html()
													+ '--col--'
													+ $(this).find('p').html();
										} else {
											wholeData = wholeData + '--row--'
													+ group + '--col--'
													+ $(this).find('h3').html()
													+ '--col--'
													+ $(this).find('p').html();
										}
									});
							var aj = $
									.ajax({
										url : 'http://120.25.1.190:8088/customerkeyword/uploadCompanyInfo.jsp',
										data : 'data=' + wholeData,
										type : 'POST',
										cache : false,
										success : function(data) {
											if (data.msg == "true") {
												// view("修改成功！");  
												alert("修改成功！");
												//window.location.reload();  
											} else {
												view(data.msg);
											}
										},
										error : function() {
											nextPageButton = $(".ecl-ui-pager-next");
											nextPageButton[0].click();
											caijiData(group);
										}
									});
						});
	};
</script>




							var aj = $
									.ajax({
										url : 'http://www.baiduup.com/customerkeyword/getFumianName.jsp',
										data : 'data=' + wholeData,
										type : 'get',
										dataType : 'jsonp',  
										jsonp:"jsoncallback",
										success : function(data) {
											console.log("data == " + data);
										},
										error : function() {
											console.log('Failed');
										}
									});

customerkeyword\getFumianName.jsp




