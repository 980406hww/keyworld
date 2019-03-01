<nav class="navbar navbar-inverse navbar-fixed-top container">
	<div class="content" style="width: 100%;height:30px;">
		<ul class="venus-menu" style="display: none">
			<c:choose>
				<c:when test="${sessionScope.get('entryType')=='bc'}">
					<shiro:hasRole  name="BCSpecial">
						<c:forEach items="${menus}" var="menu">
							<li style="" pid="${menu.pid}" lid="${menu.id}">
								<c:if test="${menu.openMode=='ajax' || menu.openMode=='iframe'}">
									<a href="javascript:void(0)" onclick="openUrl('${menu.attributes}','${menu.text}','${menu.iconCls}','${menu.openMode}')" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
								</c:if>
								<c:if test="${menu.openMode==null || menu.openMode==''}">
									<a href="${menu.attributes}" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
								</c:if>
							</li>
						</c:forEach>
					</shiro:hasRole >
				</c:when>
				<c:otherwise>
					<c:forEach items="${menus}" var="menu">
						<li style="" pid="${menu.pid}" lid="${menu.id}">
							<c:if test="${menu.openMode=='ajax' || menu.openMode=='iframe'}">
								<a href="javascript:void(0)" onclick="openUrl('${menu.attributes}','${menu.text}','${menu.iconCls}','${menu.openMode}')" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
							</c:if>
							<c:if test="${menu.openMode==null || menu.openMode==''}">
								<a href="${menu.attributes}" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
							</c:if>
						</li>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			<div style="float: right;margin:7px 10px 0px 0px;">
				<b class="fi-torso icon-black" style="font-size: 12px;">&nbsp;<shiro:principal property="name"></shiro:principal></b>|
				<span class="fi-anchor icon-green" style="font-size: 12px;color: green"></span>
				<span style="font-size: 12px;color: black">
				<c:choose>
					<c:when test="${sessionScope.get('entryType')=='qz'}">
						全站链接
					</c:when>
					<c:when test="${sessionScope.get('entryType')=='pt'}">
						普通链接
					</c:when>
					<c:when test="${sessionScope.get('entryType')=='bc'}">
						BC链接
					</c:when>
					<c:otherwise>
						负面链接
					</c:otherwise>
				</c:choose>
			</span>|
				<span class="fi-web icon-black" style="font-size: 12px;color: red;"></span><span style="color: black">&nbsp;${sessionScope.get("terminalType")}端&nbsp;|</span>&nbsp;
				<span class="fi-comments" style="font-size: 12px;color: green;"></span>
				<a href="javascript:void(0)" onclick="OpenMessageList()" style="text-decoration: none;font-size: 12px; color: black">留言列表</a>|
				<shiro:hasPermission name="/user/editPwdPage">
					<span class="fi-unlock icon-green" style="font-size: 12px;color: green"></span>
					<a href="javascript:void(0)" onclick="editUserPwd()"  style="text-decoration: none;font-size: 12px;color: black">修改密码</a>|
				</shiro:hasPermission>
				<a class="fi-x" href="javascript:void(0)" onclick="logout()" style="text-decoration: none;font-size: 12px;">&nbsp;安全退出</a>
			</div>
		</ul>
	</div>
</nav>

<%--留言列表Dialog--%>
<div id="showUserMessageListDialog" class="easyui-dialog" style="display: none">
	<form id="showUserMessageListForm">
		<table id="customerKeywordTable" border="1" cellpadding="10" style="font-size: 12px;background-color: white;border-collapse: collapse;">
			<tr>
				<td colspan="2">
					<span class="fi-comment" style="font-size: 12px;color: green;"></span>
					<a href="javascript:void(0)" onclick="openMessageBox()" style="text-decoration: none;font-size: 12px; color: black">留言</a>
					<span class="fi-comment" style="font-size: 12px;color: green;">状态: </span>
					<span class="fi-comment" style="font-size: 12px;color: green;">用户名称: </span>
				</td>
			</tr>
			<tr style="height: 30px;">
				<td style="align-self: center">
					<a href="javascript:void(0)" onclick="" style="text-decoration: none;"><strong style="font-size: 18px; color: black; clear: both;">接收</strong></a>
				</td>
				<td style="align-self: center">
					<a href="javascript:void(0)" onclick="" style="text-decoration: none;"><strong style="font-size: 18px; color: black; clear: both;">发送</strong></a>
				</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
			<tr>
				<td>msg1</td>
				<td>未处理</td>
			</tr>
		</table>
		<div style="color: black;font-size: 12px; height: 12px;" align="center">
			<span class="fi-comment" style="font-size: 12px;color: green;"><a>上一页</a></span>
			<span class="fi-comment" style="font-size: 12px;color: green;"><a>2</a></span>
			<span class="fi-comment" style="font-size: 12px;color: green;"><a>下一页</a></span>
		</div>
	</form>
</div>

<%--留言栏Dialog--%>
<div id="showUserMessageDialog" class="easyui-dialog" style="display: none">
	<form id="showUserMessageForm">
		<table>
			<tr>
				<td>发送人</td>
				<td>
					<select> <%-- 复选框 --%>
						<option value="1">张三</option>
						<option value="2">李四</option>
						<option value="3">王五</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>状态</td>
				<td>
					<select>
						<option value="0">未处理完成</option>
						<option value="1">处理完成</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>内容</td>
				<td>
					<textarea rows="5"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
    $(function () {
        var li = $(".venus-menu li");
        $.each(li, function (idx, val) {
            if ($(val).attr("pid") != null && $(val).attr("pid") != '') {
                $("li[lid=" + $(val).attr("pid") + "]").append("<ul id='" + $(val).attr("pid") + "'></ul>");
                $.each(li, function (idx1, val1) {
                    if ($(val1).attr("lid") == $(val).attr("pid")) {
                        $("li").find("#" + $(val).attr("pid")).append($(val));
                    }
                })
            }
        });
        $(".venus-menu").show();
        $("#editUserPwdDiv").hide();
    });
    //点击菜单
    function openUrl(url,title,iconCls,openMode) {
        var opts = {
            title : title,
            border : false,
            closable : true,
            fit : true,
            iconCls :iconCls
        };
        if("${pageContext.request.getAttribute("javax.servlet.forward.request_uri")}"!='/login' && "${pageContext.request.getAttribute("javax.servlet.forward.request_uri")}"!='/index')
        {
            window.location.href=url+"?resource="+"${pageContext.request.getAttribute('javax.servlet.forward.request_uri')}";
        }

        if (url && url.indexOf("http") == -1) {
            url = '${path }' + url;
        }
        if (openMode == 'iframe') {
            opts.content = '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.5%;"></iframe>';
            addTab(opts);
        } else if (url) {
            opts.href = url+"?resource="+"${pageContext.request.getAttribute("javax.servlet.forward.request_uri")}";
            addTab(opts);
        }
    }
    function logout(){
        $.messager.confirm('提示','确定要退出?',function(r){
            if (r){
                $.post('${path }/logout', function(result) {
                    if(result.success){
                        window.location.href='${path }';
                    }
                }, 'json');
            }
        });
    }

    $.modalDialog = function(options) {
        if ($.modalDialog.handler == undefined) {// 避免重复弹出
            var opts = $.extend({
                title : '',
                width : 840,
                height : 680,
                modal : true,
                onClose : function() {
                    $.modalDialog.handler = undefined;
                    $(this).dialog('destroy');
                },
                onOpen : function() {
                }
            }, options);
            opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
            return $.modalDialog.handler = $('<div/>').dialog(opts);
        }
    };

    function editUserPwd() {
        parent.$.modalDialog({
            title : '修改密码',
            width : 230,
            height : 170,
            href : '${path }/user/editPwdPage',
            buttons : [ {
                text : '确定',
                handler : function() {
                    var f = parent.$.modalDialog.handler.find('#editUserPwdForm');
                    f.submit();
                }
            } ]
        });
        parent.$.modalDialog.handler.dialog("open");
        parent.$.modalDialog.handler.window("resize",{top:$(document).scrollTop() + 100});
    }

    function OpenMessageList() {
        // getUserMessage()
        var showUserMessageListDialog = $("#showUserMessageListDialog");
        // 初始赋值
        showUserMessageListDialog.show();
        showUserMessageListDialog.dialog({
			resizable: false,
			height: 450,
			width: 300,
			title: '留言列表',
			modal: false,
			buttons: [{
			    text: '发送',
				iconCls: 'icon-ok',
				handler: function () {
					// saveUserMessage()
                }
			}, {
			    text: '取消',
				iconCls: 'icon-cancel',
				handler: function () {
					$("#showUserMessageListDialog").dialog("close");
					$("#showUserMessageListForm")[0].reset();
                }
			}]
		});
        showUserMessageListDialog.dialog("open");
        showUserMessageListDialog.window("resize", {top: $(document).scrollTop() + 100});
    }

    function openMessageBox() {
		var showUserMessageDialog = $("#showUserMessageDialog");
        // 判断 新增还是修改  => 赋值
		showUserMessageDialog.show();
		showUserMessageDialog.dialog({
			resizable: false,
			height: 300,
			width: 200,
			title: '留言框',
			modal: false,
			buttons: [{
			    text: '发送',
				iconCls: "icon-ok",
				handler: function () {
					// saveUserMessage();
                }
			}, {
			    text: '取消',
				iconCls: 'icon-cancel',
				handler: function () {
					$("#showUserMessageDialog").dialog("close");
					$("#showUserMessageForm")[0].reset();
                }
			}]
		});
		showUserMessageDialog.dialog("open");
		showUserMessageDialog.window("resize", {top: $(document).scrollTop() + 100});
    }
</script>
