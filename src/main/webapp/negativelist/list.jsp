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

		#showNegativeListDiv {
			overflow: scroll;
			width: 100%;
			height: 94%;
			margin: auto;
		}

		#negativeListBottomDiv {
			float: right;
			width: 580px;
		}
	</style>
	<link href="/css/menu.css" rel="stylesheet" type="text/css" />
	<link href="/ui/jquery-ui.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	<script language="javascript" type="text/javascript" src="/ui/jquery-ui.js"></script>
	<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	<script language="javascript">
        $(function () {
            var negativeListBottomDiv = $('#negativeListBottomDiv');
            var pageSize = negativeListBottomDiv.find('#pageSizeHidden').val();
            negativeListBottomDiv.find('#chooseRecords').val(pageSize);
            var pageCount = negativeListBottomDiv.find('#pageCountHidden').val();
            negativeListBottomDiv.find('#pageCountHidden').val(pageCount);
            var currentPage = negativeListBottomDiv.find('#currentPageHidden').val();
            negativeListBottomDiv.find('#currentPageHidden').val(currentPage);
            if(parseInt(currentPage) > 1 && parseInt(currentPage) < parseInt(pageCount)) {
                negativeListBottomDiv.find("#firstButton").removeAttr("disabled");
                negativeListBottomDiv.find("#upButton").removeAttr("disabled");
                negativeListBottomDiv.find("#nextButton").removeAttr("disabled");
                negativeListBottomDiv.find("#lastButton").removeAttr("disabled");
            } else if (parseInt(pageCount) == 1) {
                negativeListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                negativeListBottomDiv.find("#upButton").attr("disabled", "disabled");
                negativeListBottomDiv.find("#nextButton").attr("disabled", "disabled");
                negativeListBottomDiv.find("#lastButton").attr("disabled", "disabled");
            } else if (parseInt(currentPage) <= 1) {
				currentPage = 1;
				negativeListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
				negativeListBottomDiv.find("#upButton").attr("disabled", "disabled");
			} else {
				currentPage = pageCount;
				negativeListBottomDiv.find("#nextButton").attr("disabled", "disabled");
				negativeListBottomDiv.find("#lastButton").attr("disabled", "disabled");
			}
        });

        function changePaging(currentPageNumber, pageSize) {
            var searchNegativeListForm = $("#searchNegativeListForm");
            searchNegativeListForm.find("#totalHidden").val();
            searchNegativeListForm.find("#currentPageNumberHidden").val(currentPageNumber);
            searchNegativeListForm.find("#pageSizeHidden").val(pageSize);
            searchNegativeListForm.submit();
        }

        function resetPageNumber() {
            $("#searchNegativeListForm").find("#currentPageNumberHidden").val(1);
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
                        showInfo("获取信息成功", self);
                    }
                },
                error: function () {
                    showInfo("获取信息失败", self);
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

        function showInfo(content, e) {
            e = e || window.event;
            var div1 = document.getElementById('div2'); //将要弹出的层
            div1.innerText = content;
            div1.style.display = "block"; //div1初始状态是不可见的，设置可为可"
            div1.style.left = getLeft(e) + 10; //鼠标目前在X轴上的位置，"0是为了向右边移动10个px方便看到内容
            div1.style.top = getTop(e) + 5;
            div1.style.position = "absolute";

            var intervalID = setInterval(function () {
                div1.style.display = "none";
            }, 8000);
        }

        function getTop(e) {
            var offset = e.offsetTop;
            if (e.offsetParent != null) offset += getTop(e.offsetParent);
            return offset;
        }
        //获取元素的横坐标
        function getLeft(e) {
            var offset = e.offsetLeft;
            if (e.offsetParent != null) offset += getLeft(e.offsetParent);
            return offset;
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
                type: 'POST',
                success: function (result) {
                    if (result) {
                        showInfo("操作成功", self);
                        window.location.reload();
                    } else {
                        showInfo("操作失败", self);
                    }
                },
                error: function () {
                    showInfo("操作失败", self);
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
                        showInfo("操作成功", self);
                        window.location.reload();
                    } else {
                        showInfo("操作失败", self);
                    }
                },
                error: function () {
                    showInfo("操作失败", self);
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
            negativeListObj.keyword = $("#negativeListForm").find("#keyword").val().trim();
            negativeListObj.title = $("#negativeListForm").find("#title").val().trim();
            negativeListObj.url = $("#negativeListForm").find("#url").val().trim();
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
                        showInfo("操作成功", self);
                        window.location.reload();
                    } else {
                        showInfo("操作失败", self);
                    }
                },
                error: function () {
                    showInfo("操作失败", self);
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
						<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
						<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
						<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
						<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
						<td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="${negativeListCriteria.keyword}" style="width:200px;"></td>
						<td align="right">URL:</td> <td><input type="text" name="url" id="url" value="${negativeListCriteria.url}" style="width:200px;"></td>
						<td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" onclick="resetPageNumber()" value=" 查询 " ></td>
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
	<div id="negativeListBottomDiv" align="right">
	  <input id="fisrtButton" type="button" onclick="changePaging(1,'${page.size}')" value="首页"/>
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  <input id="upButton" type="button" onclick="changePaging('${page.current-1}','${page.size}')" value="上一页"/>
	  &nbsp;&nbsp;&nbsp;&nbsp;${page.current}/${page.pages}&nbsp;&nbsp;
	  <input id="nextButton" type="button" onclick="changePaging('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')" value="下一页">
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  <input id="lastButton" type="button" onclick="changePaging('${page.pages}','${page.size}')" value="末页">
	  &nbsp;&nbsp;&nbsp;&nbsp;
	  总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;
	  每页显示条数:
	  <select id="chooseRecords" onchange="changePaging(${page.current},this.value)">
		  <option>10</option>
		  <option>25</option>
		  <option>50</option>
		  <option>75</option>
		  <option>100</option>
	  </select>
	  <input type="hidden" id="currentPageHidden" value="${page.current}"/>
	  <input type="hidden" id="pageSizeHidden" value="${page.size}"/>
	  <input type="hidden" id="pageCountHidden" value="${page.pages}"/>
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

