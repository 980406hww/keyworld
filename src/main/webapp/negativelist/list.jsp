<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<title>关键字负面清单</title>
	<style>
		.wrap {
			word-break: break-all;
			word-wrap: break-word;
		}

		#showNegativeListDiv {
			overflow: scroll;
			width: 100%;
			height: 95%;
			margin: auto;
		}

		#showNegativeListBottomDiv {
			float: right;
			width: 580px;
		}
	</style>
	<link href="/css/menu.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	<script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	<script language="javascript">
        $(function () {
            var showNegativeListBottomDiv = $('#showNegativeListBottomDiv');
            var displaysRecords = showNegativeListBottomDiv.find('#displaysRecordsHidden').val();
            showNegativeListBottomDiv.find('#chooseRecords').val(displaysRecords);
            var pages = showNegativeListBottomDiv.find('#pagesHidden').val();
            showNegativeListBottomDiv.find('#pagesHidden').val(pages);
            var currentPage = showNegativeListBottomDiv.find('#currentPageHidden').val();
            showNegativeListBottomDiv.find('#currentPageHidden').val(currentPage);
            if(parseInt(currentPage) > 1 && parseInt(currentPage) < parseInt(pages)) {
                showNegativeListBottomDiv.find("#firstButton").removeAttr("disabled");
                showNegativeListBottomDiv.find("#upButton").removeAttr("disabled");
                showNegativeListBottomDiv.find("#nextButton").removeAttr("disabled");
                showNegativeListBottomDiv.find("#lastButton").removeAttr("disabled");
            } else if (parseInt(currentPage) == 1 && parseInt(pages) == 1) {
                showNegativeListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                showNegativeListBottomDiv.find("#upButton").attr("disabled", "disabled");
                showNegativeListBottomDiv.find("#nextButton").attr("disabled", "disabled");
                showNegativeListBottomDiv.find("#lastButton").attr("disabled", "disabled");
            } else {
                if (parseInt(currentPage) <= 1) {
                    currentPage = 1;
                    showNegativeListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                    showNegativeListBottomDiv.find("#upButton").attr("disabled", "disabled");
                } else if (parseInt(currentPage) >= parseInt(pages)) {
                    currentPage = pages;
                    showNegativeListBottomDiv.find("#nextButton").attr("disabled", "disabled");
                    showNegativeListBottomDiv.find("#lastButton").attr("disabled", "disabled");
                }
            }
        });

        function searchNegativeLists(currentPage, displaysRecords) {
            var searchNegativeListForm = $("#searchNegativeListForm");
            searchNegativeListForm.append('<input value="' + currentPage + '" id="currentPage" type="hidden" name="currentPageHidden"/>');
            searchNegativeListForm.append('<input value="' + displaysRecords + '" id="currentPage" type="hidden" name="displayRerondsHidden"/>');
            searchNegativeListForm.submit();
        }

        function chooseRecords(currentPage, displayRecords) {
            $("#currentPageHidden").val(currentPage);
            $("#displaysRecordsHidden").val(displayRecords);
            searchNegativeLists(currentPage, displayRecords);
        }

        function editNegativeList(uuid) {
            $.ajax({
                url: '/internal/negativelist/getNegativeList/' + uuid,
                type: 'Get',
                success: function (negativeList) {
                    if (negativeList != null) {
                        var negativeListForm = $("#negativeListForm");
                        negativeListForm.find("#terminalType").val(negativeList.terminalType);
                        negativeListForm.find("#keyword").val(negativeList.keyword);
                        negativeListForm.find("#title").val(negativeList.title);
                        negativeListForm.find("#url").val(negativeList.url);
                        negativeListForm.find("#desc").val(negativeList.desc);
                        negativeListForm.find("#position").val(negativeList.position);
                        showNegativeListDialog(negativeList.uuid);
                    } else {
                        alert("获取信息失败!")
                    }
                },
                error: function () {
                    alert("获取信息失败!");
                }
            });
        }

        function selectAll(self) {
            var a = document.getElementsByName("uuid");
            if (self.checked) {
                for (var i = 0; i < a.length; i++) {
                    if (a[i].type == "checkbox") {
                        a[i].checked = true;
                    }
                }
            } else {
                for (var i = 0; i < a.length; i++) {
                    if (a[i].type == "checkbox") {
                        a[i].checked = false;
                    }
                }
            }
        }

        function doOver(obj) {
            obj.style.backgroundColor = "green";
        }

        function doOut(obj) {
            var rowIndex = obj.rowIndex;
            if ((rowIndex % 2) == 0) {
                obj.style.backgroundColor = "#eeeeee";
            } else {
                obj.style.backgroundColor = "#ffffff";
            }
        }

        function deleteNegativeList(uuid) {
            if (confirm("确定要删除这条信息吗?") == false) return;
            $.ajax({
                url: '/internal/negativelist/deleteNegativeList/' + uuid,
                type: 'Get',
                success: function (result) {
                    if (result) {
                        alert("删除成功");
                        window.location.reload();
                    } else {
                        alert("删除失败");
                        window.location.reload();
                    }
                },
                error: function () {
                    alert("删除失败");
                    window.location.reload();
                }
            });
        }

        function deleteNegatives(self) {
            var uuids = getSelectedIDs();
            if (uuids === '') {
                alert('请选择要删除的负面词');
                return;
            }
            if (confirm("确定要删除这些负面词吗?") == false) return;
            var postData = {};
            postData.uuids = uuids.split(",");
            $.ajax({
                url: '/internal/negativelist/deleteNegativeLists',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        alert("操作成功");
                        window.location.reload();
                    } else {
                        alert("操作失败");
                        window.location.reload();
                    }
                },
                error: function () {
                    alert("操作失败");
                    window.location.reload();
                }
            });
        }
        function getSelectedIDs() {
            var uuids = '';
            $.each($("input[name=uuid]:checkbox:checked"), function () {
                if (uuids === '') {
                    uuids = $(this).val();
                } else {
                    uuids = uuids + "," + $(this).val();
                }
            });
            return uuids;
        }
        
        function saveNegativeList(uuid) {
            var negativeListObj = {};
            negativeListObj.uuid = uuid;
            negativeListObj.keyword = $("#negativeListForm").find("#keyword").val();
            negativeListObj.title = $("#negativeListForm").find("#title").val();
            negativeListObj.url = $("#negativeListForm").find("#url").val();
            negativeListObj.desc = $("#negativeListForm").find("#desc").val();
            negativeListObj.position = $("#negativeListForm").find("#position").val();
            $.ajax({
                url: '/internal/negativelist/addNegativeList/',
                data: JSON.stringify(negativeListObj),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result) {
                        alert("操作成功");
                        window.location.reload();
                    } else {
                        alert("操作失败");
                        window.location.reload();
                    }
                },
                error: function () {
                    alert("操作失败");
                    window.location.reload();
                }
            });

		}

        function showNegativeListDialog(uuid) {
            if (uuid == null) {
                $('#negativeListForm')[0].reset();
            }
            $("#negativeListDialog").dialog({
                resizable: false,
                width: 530,
                height: 365,
                modal: true,
                position: { using:function(pos){
                    console.log(pos)
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset = 0||topOffset>0) {
                        $(this).css('top', 150);
                    }
                }},
                buttons: {
                    "保存": function () {
                        saveNegativeList(uuid);
                    },
                    "清空": function () {
                        $('#negativeListForm')[0].reset();
                    },
                    "取消": function () {
                        $(this).dialog("close");
                        $('#negativeListForm')[0].reset();
                    }
                }
            });
        }
	</script>
</head>
<body>
<div id="showNegativeListDiv">
	<table width="100%" style="font-size:12px;" cellpadding="3">
	  <tr>
		<td colspan="8" align="left">
			<%@include file="/menu.jsp" %>
		</td>
	  </tr>
	  <tr>
		 <td colspan="8" align="right">
			<a href="javascript:showNegativeListDialog(null)">增加关键字负面清单</a> | <a href="javascript:deleteNegatives()">删除所选数据</a>
		 </td>
	  </tr>
	  <tr>
		 <td colspan="8">
			<form method="post" id="searchNegativeListForm" action="/internal/negativelist/searchNegativeLists">
				<table style="font-size:12px;">
					<tr>
						<td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="${negativeListCriteria.keyword}" style="width:200px;"></td>
						<td align="right">URL:</td> <td><input type="text" name="url" id="url" value="${negativeListCriteria.url}" style="width:200px;"></td>
						<td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
					</tr>
				 </table>
			</form>
		 </td>
	  </tr>
	  <tr bgcolor="#eeeeee" height=30>
		  <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" /></td>
		  <td align="center" width=100>关键字</td>
		  <td align="center" width=100>URL</td>
		  <td align="center" width=200>标题</td>
		  <td align="center" width=300>描述</td>
		  <td align="center" width=50>排名</td>
		  <td align="center" width=100>采集日期</td>
		  <td align="center" width=100>操作</td>
		  <div id="div1"></div>
		  <div id="div2"></div>
	  </tr>
	  <c:forEach items="${page.records}" var="negativeList">
		  <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
			  <td><input type="checkbox" name="uuid" value="${negativeList.uuid}"/></td>
			  <td>${negativeList.keyword}</td>
			  <td>${negativeList.title} </td>
			  <td>${negativeList.url}</td>
			  <td>${negativeList.desc}</td>
			  <td>${negativeList.position}</td>
			  <td><fmt:formatDate value="${negativeList.createTime}" pattern="yyyy-MM-dd"/></td>
			  <td align="center">
				  <a href="javascript:editNegativeList(${negativeList.uuid})">修改</a> |
				  <a href="javascript:deleteNegativeList('${negativeList.uuid}')">删除</a>
			  </td>
		  </tr>
	  </c:forEach>
	</table>
</div>
	<hr>
	<div id="showNegativeListBottomDiv" align="right">
	  <input id="fisrtButton" type="button" onclick="searchNegativeLists(1,'${page.size}')" value="首页"/>
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  <input id="upButton" type="button" onclick="searchNegativeLists('${page.current-1}','${page.size}')" value="上一页"/>
	  &nbsp;&nbsp;&nbsp;&nbsp;${page.current}/${page.pages}&nbsp;&nbsp;
	  <input id="nextButton" type="button" onclick="searchNegativeLists('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')" value="下一页">
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  <input id="lastButton" type="button" onclick="searchNegativeLists('${page.pages}','${page.size}')" value="末页">
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;
	  每页显示条数:
	  <select id="chooseRecords" onchange="chooseRecords(${page.current},this.value)">
		  <option>10</option>
		  <option>25</option>
		  <option>50</option>
		  <option>75</option>
		  <option>100</option>
	  </select>
	  <input type="hidden" id="currentPageHidden" value="${page.current}"/>
	  <input type="hidden" id="displaysRecordsHidden" value="${page.size}"/>
	  <input type="hidden" id="pagesHidden" value="${page.pages}"/>
	  &nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<br>

	<div id="negativeListDialog" style="display: none;">
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
</body>
</html>

