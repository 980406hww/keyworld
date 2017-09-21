
<%@ include file="/commons/basejs.jsp" %>
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
			background-color:#dedede;
			padding-top: 5px;
			padding-bottom: 5px;
			width: 100%;
		}
		#showNegativeBottomDiv {
			float: right;
			margin-right: 10px;
		}

	</style>
	<script language="javascript">
        $(function () {
            $("#negativeListDialog").dialog("close");
            $("#showNegativeListDiv").css("margin-top",$("#topDiv").height());
            alignTableHeader();
            window.onresize = function(){
                alignTableHeader();
            }
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
            } else if (parseInt(pageCount) <= 1) {
                negativeListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                negativeListBottomDiv.find("#upButton").attr("disabled", "disabled");
                negativeListBottomDiv.find("#nextButton").attr("disabled", "disabled");
                negativeListBottomDiv.find("#lastButton").attr("disabled", "disabled");
            } else if (parseInt(currentPage) <= 1) {
                negativeListBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                negativeListBottomDiv.find("#upButton").attr("disabled", "disabled");
            } else {
                negativeListBottomDiv.find("#nextButton").attr("disabled", "disabled");
                negativeListBottomDiv.find("#lastButton").attr("disabled", "disabled");
            }
        });
        function alignTableHeader(){
            var td = $("#showNegativeListTable tr:first td");
            var ctd = $("#headerTable tr:first td");
            $.each(td, function (idx, val) {
                ctd.eq(idx).width($(val).width());
            });
        }
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
                        $().toastmessage('showErrorToast', "获取信息失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "获取信息失败");
                }
            });
        }

        function selectAll(self) {
            var a = document.getElementsByName("uuid");
            if (self.checked) {
                for (var i = 0; i < a.length; i++) {
                        a[i].checked = true;
                }
            } else {
                for (var i = 0; i < a.length; i++) {
                        a[i].checked = false;
                }
            }
        }

        function decideSelectAll() {
            var a = document.getElementsByName("uuid");
            var select=0;
            for(var i = 0; i < a.length; i++){
                if (a[i].checked == true){
                    select++;
                }
            }
            if(select == a.length){
                $("#selectAllChecked").prop("checked",true);
            }else {
                $("#selectAllChecked").prop("checked",false);
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
                        $().toastmessage('showSuccessToast', "操作成功",true);
                    } else {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "操作失败");
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
                        $().toastmessage('showSuccessToast', "操作成功",true);
                    } else {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "操作失败");
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
            if (!(/^[0-9]*$/.test(negativeListObj.position)) && (negativeListObj.position != '')) {
                alert("排名输入非法!");
                return;
            }
            $.ajax({
                url: '/internal/negativelist/saveNegativeList',
                data: JSON.stringify(negativeListObj),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result) {
                        $().toastmessage('showSuccessToast', "操作成功",true);
                    } else {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "操作失败");
                }
            });

        }

        function showNegativeListDialog(uuid) {
            if (uuid == null) {
                $('#negativeListForm')[0].reset();
            }
            $("#negativeListDialog").dialog({
                resizable: false,
                width: 490,
                height: 290,
                modal: true,
                title: '负面信息',
				closed:true,
                buttons: [{
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function () {
                        saveNegativeList(uuid);
                    }
                },
                    {
                        text: '清空',
                        iconCls: 'fi-trash',
                        handler: function () {
                            $('#negativeListForm')[0].reset();
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#negativeListDialog').dialog("close");
                            $('#negativeListForm')[0].reset();
                        }
                    }]
           	 });
            $('#negativeListDialog').dialog("open");
            $('#negativeListDialog').window("resize",{top:$(document).scrollTop() + 100});
        }
	</script>
</head>
<body>

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
								<input type="button" value="增加关键字负面清单" onclick="showNegativeListDialog(null)">&nbsp;
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/negativelist/deleteNegativeLists">
								<input type="button" value="删除所选数据" onclick="deleteNegatives()">
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
		  <td align="center" ><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
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
			  <td><input type="checkbox" name="uuid" value="${negativeList.uuid}" onclick="decideSelectAll()"/></td>
			  <td>${negativeList.keyword}</td>
			  <td>${negativeList.title} </td>
			  <td>${negativeList.url}</td>
			  <td>${negativeList.desc}</td>
			  <td width="50">${negativeList.position}</td>
			  <td width="80"><fmt:formatDate value="${negativeList.createTime}" pattern="yyyy-MM-dd"/></td>
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
	<hr>
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

