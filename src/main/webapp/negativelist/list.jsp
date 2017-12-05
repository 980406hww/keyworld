<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>关键字负面清单</title>
	<style>
		#div2 {
			display: none;
			background-color: #ACF106;
			color: #E80404;
			font-size: 20px;
			line-height: 18px;
			border: 2px solid #104454;
			width: 100px;
			height: 22px;
		}

		#topDiv {
			position: fixed;
			top: 0px;
			left: 0px;
			background-color: white;
			width: 100%;
		}

		#negativeListBottomDiv{
			position: fixed;
			bottom: 0px;
			right: 0px;
			background-color:#ADD1FF;
			padding-top: 5px;
			padding-bottom: 5px;
			width: 100%;
		}
		#showNegativeBottomDiv {
			float: right;
			margin-right: 10px;
		}
	</style>

</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@include file="/menu.jsp" %>
	<table width="100%" style="font-size:12px;margin-top: 40px;" cellpadding="3" id="">
	  <tr>

	  </tr>
	  <tr>
		 <td colspan="8">
			<form method="post" id="searchNegativeListForm" action="/internal/negativelist/searchNegativeLists">
				<table style="font-size:12px;">
					<tr>
						<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
						<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
						<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
						<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
						<td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="${negativeListCriteria.keyword}" style="width:200px;"></td>
						<td align="right">URL:</td> <td><input type="text" name="url" id="url" value="${negativeListCriteria.url}" style="width:170px;"></td>
						<td align="right" width="50">
							<shiro:hasPermission name="/internal/negativelist/searchNegativeLists">
							<input type="submit" name="btnQuery" id="btnQuery" onclick="resetPageNumber()" value=" 查询 " >&nbsp;
						</shiro:hasPermission>
						<td colspan="4" align="right">
							<shiro:hasPermission name="/internal/negativelist/saveNegativeList">
								<input type="button" value=" 增加 " onclick="showNegativeListDialog(null)">&nbsp;
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/negativelist/deleteNegativeLists">
								<input type="button" value=" 删除所选 " onclick="deleteNegatives()">
							</shiro:hasPermission>
						</td>
					</tr>

				 </table>
			</form>
		 </td>
	  </tr>
	</table>
	<table id="headerTable" width="100%">
	  <tr bgcolor="#eeeeee" height=30>
		  <td style="padding: 5px;" ><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
		  <td align="center" >关键字</td>
		  <td align="center" >标题</td>
		  <td align="center" >URL</td>
		  <td align="center" >描述</td>
		  <td align="center" >排名</td>
		  <td align="center" >采集日期</td>
		  <td align="center" >操作</td>
		  <div id="div2"></div>
	  </tr>
	</table>
</div>
<div id="showNegativeListDiv" style="margin-bottom: 30px">
	<table id="showNegativeListTable"  width="100%">
	  <c:forEach items="${page.records}" var="negativeList" varStatus="trIndex">
		  <c:choose>
			  <c:when test="${trIndex.count % 2 != 0}">
				  <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30 style="background-color: #eeeeee;">
			  </c:when>
			  <c:otherwise>
				  <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
			  </c:otherwise>
		  </c:choose>
			  <td style="padding: 5px;width: 10"><input type="checkbox" name="uuid" value="${negativeList.uuid}" onclick="decideSelectAll()"/></td>
			  <td width="70">${negativeList.keyword}</td>
			  <td width="280">${negativeList.title} </td>
			  <td width="120">${negativeList.url}</td>
			  <td width="720">${negativeList.desc}</td>
			  <td width="50">${negativeList.position}</td>
			  <td width="60"><fmt:formatDate value="${negativeList.createTime}" pattern="yyyy-MM-dd"/></td>
			  <td align="center" width="80">
				  <shiro:hasPermission name="/internal/negativelist/saveNegativeList">
				  	<a href="javascript:editNegativeList(${negativeList.uuid})">修改</a> |
				  </shiro:hasPermission>
				  <shiro:hasPermission name="/internal/negativelist/deleteNegativeList">
				  	<a href="javascript:deleteNegativeList('${negativeList.uuid}')">删除</a>
				  </shiro:hasPermission>
			  </td>
		  </tr>
	  </c:forEach>

	</table>
</div>
<div id="negativeListBottomDiv" align="right">
	<div id="showNegativeBottomDiv">
	  <input id="fisrtButton" type="button" onclick="changePaging(1,'${page.size}')" value="首页"/>
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  <input id="upButton" type="button" onclick="changePaging('${page.current-1}','${page.size}')" value="上一页"/>
	  &nbsp;&nbsp;&nbsp;&nbsp;${page.current}/${page.pages}&nbsp;&nbsp;
	  <input id="nextButton" type="button" onclick="changePaging('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')" value="下一页">
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  <input id="lastButton" type="button" onclick="changePaging('${page.pages}','${page.size}')" value="末页">
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  总记录数:${page.total}&nbsp;&nbsp;
	  每页显示条数:
	  <select id="chooseRecords" onchange="changePaging(${page.current},this.value)" style="margin-right: 10px;">
		  <option>10</option>
		  <option>25</option>
		  <option>50</option>
		  <option>75</option>
		  <option>100</option>
	  </select>
	  <input type="hidden" id="currentPageHidden" value="${page.current}"/>
	  <input type="hidden" id="pageSizeHidden" value="${page.size}"/>
	  <input type="hidden" id="pageCountHidden" value="${page.pages}"/>
	</div>
</div>

	<div id="negativeListDialog" class="easyui-dialog" style="left: 40%;">
		<form id="negativeListForm" style="margin-bottom: 0px;" method="post" action="list.jsp">
			<table style="font-size:14px;" cellpadding="5">
				<tr>
					<td align="right">关键词<label style="color: red">*</label>：</td>
					<td><input type="text" id="keyword" style="width:400px;"></td>
				</tr>
				<tr>
					<td align="right">标题<label style="color: red">*</label>：</td>
					<td><input type="text" id="title" style="width:400px;"></td>
				</tr>
				<tr>
					<td align="right">URL<label style="color: red">*</label>：</td>
					<td><input type="text" id="url" style="width:400px;"></td>
				</tr>
				<tr>
					<td align="right">原始URL<label style="color: red">*</label>：</td>
					<td><input type="text" id="originalUrl" style="width:400px;"></td>
				</tr>
				<tr>
					<td align="right">排名：</td>
					<td><input type="text" id="position" style="width:400px;"></td>
				</tr>
				<tr>
					<td align="right">描述<label style="color: red">*</label>：</td>
					<td><textarea id="desc" value="" placeholder="" style="width:400px;height:100px;"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
<script language="javascript">
    document.write("<scr"+"ipt src=\"${staticPath}/negativelist/list.js\"></sc"+"ript>");
    document.write("<scr"+"ipt src=\"${staticPath }/static/toastmessage/jquery.toastmessage.js\"></sc"+"ript>");
</script>
</body>
</html>

