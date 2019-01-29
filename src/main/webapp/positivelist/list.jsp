<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>关键字优质清单</title>
	<style>
		#topDiv {
			position: fixed;
			top: 0px;
			left: 0px;
			background-color: white;
			width: 100%;
		}

		#positiveListBottomDiv{
			position: fixed;
			bottom: 0px;
			right: 0px;
			background-color:#ADD1FF;
			padding-top: 5px;
			padding-bottom: 5px;
			width: 100%;
		}
		#showPositiveBottomDiv {
			float: right;
			margin-right: 10px;
		}
	</style>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@include file="/menu.jsp" %>
	<table width="100%" style="font-size:12px;margin-top: 40px;" cellpadding="3">
	  <tr>
		 <td colspan="8">
			<form method="post" id="searchPositiveListForm" action="/internal/positivelist/searchPositiveLists">
				<table style="font-size:12px;">
					<tr>
						<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
						<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
						<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
						<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
						<td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="${positiveListCriteria.keyword}" style="width:200px;"></td>
						<td align="right">标题:</td> <td><input type="text" name="title" id="title" value="${positiveListCriteria.title}" style="width:200px;"></td>
						<td align="right">URL:</td> <td><input type="text" name="url" id="url" value="${positiveListCriteria.url}" style="width:170px;"></td>
						<td align="right" width="50">
						<shiro:hasPermission name="/internal/positivelist/searchPositiveLists">
							<input type="submit" name="btnQuery" id="btnQuery" onclick="resetPageNumber()" value=" 查询 " >&nbsp;
						</shiro:hasPermission>
						<td colspan="4" align="right">
							<shiro:hasPermission name="/internal/positivelist/savePositiveList">
								<input type="button" value=" 增加 " onclick="showPositiveListDialog(null)">&nbsp;
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/positivelist/deletePositiveLists">
								<input type="button" value=" 删除所选 " onclick="deletePositives()">
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
		  <td align="center" >熊掌号</td>
		  <td align="center" >描述</td>
		  <td align="center" >排名</td>
		  <td align="center" >采集日期</td>
		  <td align="center" >操作</td>
	  </tr>
	</table>
</div>
<div id="showPositiveListDiv" style="margin-bottom: 30px">
	<table id="showPositiveListTable"  width="100%">
	  <c:forEach items="${page.records}" var="positiveList" varStatus="trIndex">
		  <c:choose>
			  <c:when test="${trIndex.count % 2 != 0}">
				  <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30 style="background-color: #eeeeee;">
			  </c:when>
			  <c:otherwise>
				  <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
			  </c:otherwise>
		  </c:choose>
			  <td style="padding: 5px;width: 10"><input type="checkbox" name="uuid" value="${positiveList.uuid}" onclick="decideSelectAll()"/></td>
			  <td width="70">${positiveList.keyword}</td>
			  <td width="280">${positiveList.title} </td>
			  <td width="120">${positiveList.url}</td>
			  <td width="120">${positiveList.bearPawNumber}</td>
			  <td width="600">${positiveList.desc}</td>
			  <td width="50">${positiveList.position}</td>
			  <td width="60"><fmt:formatDate value="${positiveList.createTime}" pattern="yyyy-MM-dd"/></td>
			  <td align="center" width="80">
				  <shiro:hasPermission name="/internal/positivelist/savePositiveList">
				  	<a href="javascript:editPositiveList(${positiveList.uuid})">修改</a> |
				  </shiro:hasPermission>
				  <shiro:hasPermission name="/internal/positivelist/deletePositiveList">
				  	<a href="javascript:deletePositiveList('${positiveList.uuid}')">删除</a>
				  </shiro:hasPermission>
			  </td>
		  </tr>
	  </c:forEach>

	</table>
</div>
<div id="positiveListBottomDiv" align="right">
	<div id="showPositiveBottomDiv">
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
          <option>500</option>
          <option>1000</option>
	  </select>
	  <input type="hidden" id="currentPageHidden" value="${page.current}"/>
	  <input type="hidden" id="pageSizeHidden" value="${page.size}"/>
	  <input type="hidden" id="pageCountHidden" value="${page.pages}"/>
	</div>
</div>

	<div id="positiveListDialog" class="easyui-dialog" style="display: none;left: 40%;">
		<form id="positiveListForm" style="margin-bottom: 0px;" method="post" action="list.jsp">
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
					<td align="right">熊掌号：</td>
					<td><input type="text" id="bearPawNumber" style="width:400px;"></td>
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
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/positivelist/list.js"></script>
</body>
</html>

