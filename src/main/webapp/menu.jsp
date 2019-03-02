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
		<table id="userMessageListConditionTable" cellpadding="10" style="font-size: 12px; background-color: white;border-collapse: collapse; width: 100%;">
			<tr>
				<td colspan="2">
					&nbsp;<span class="fi-comment" style="font-size: 12px;color: green;">&nbsp;</span><a href="javascript:void(0)" onclick="openMessageBox()" style="text-decoration: none;font-size: 12px; color: black">留言</a>
					&nbsp;<span style="font-size: 12px;">状态: </span>
					<span>
						<select id="fir_message_status_select" style="width: 100px;"></select>
						<div id="fir_message_status">
							<div style="padding: 10px; height: 80px; clear: both;">
								<input type="checkbox" name="check" value="0"><span>未处理</span><br/>
								<input type="checkbox" name="check" value="1"><span>处理中</span><br/>
								<input type="checkbox" name="check" value="2"><span>处理完毕</span>
							</div>
						</div>
					</span>
					&nbsp;<span style="font-size: 12px;">用户名称: </span>
					<span>
						<select id="sec_message_status_select" style="width: 100px;"></select>
						<div id="sec_message_status">
							<div style="padding: 10px; height: 80px; clear: both;">
								<input type="checkbox" name="check" value="0"><span>未处理</span><br/>
								<input type="checkbox" name="check" value="1"><span>处理中</span><br/>
								<input type="checkbox" name="check" value="2"><span>处理完毕</span>
							</div>
						</div>
					</span>
					&nbsp;<input class="ui-button ui-widget ui-corner-all" type="button" onclick="searchUserMessageList()" value=" 查询 " >
				</td>
			</tr>
		</table>
		<table id="userMessageStatus">
			<input type="hidden" name="messageStatus" value="">
			<thead style="align-items: center;">
				<th style="width: 197px;" messageStatus="接收" onclick="changeUserMessageStatus(1, this)">
					<a href="#" class="btn btn-primary btn-block btn-flat" style="text-decoration: none; font-size: 18px; color: white; clear: both;">接收</a>
				</th>
				<th style="width: 197px;" messageStatus="发送" onclick="changeUserMessageStatus(2, this)">
					<a href="#" class="btn btn-primary btn-block btn-flat" style="text-decoration: none; font-size: 18px; color: white; clear: both;">发送</a>
				</th>
			</thead>
		</table>
		<table id="userMessageListTable" border="1" cellpadding="10" style="font-size: 12px; background-color: white;border-collapse: collapse; width: 100%;">
			<tbody>
				<tr>
					<td style="width: 264px;">msg1</td>
					<td style="width: 80px;">未处理</td>
				</tr>
			</tbody>
		</table>
		<div style="color: black;font-size: 12px; height: 12px;" align="center">
			<a href="javascript:void(0)" onclick="openMessageBox()" style="text-decoration: none;">上一页</a></span>
			<a href="javascript:void(0)" onclick="openMessageBox()" style="text-decoration: none;">第2页</a></span>
			<a href="javascript:void(0)" onclick="openMessageBox()" style="text-decoration: none;"> / 共4页</a></span>
			<a href="javascript:void(0)" onclick="openMessageBox()" style="text-decoration: none;">下一页</a></span>
		</div>
	</form>
</div>

<%--留言栏Dialog--%>
<div id="showUserMessageDialog" class="easyui-dialog" style="display: none">
	<form id="showUserMessageForm">
		<table cellpadding="10" style="font-size: 12px; background-color: white; border-collapse:separate; border-spacing:0px 10px;">
			<tr>
				<td><span style="width: 60px">发送人：</span></td>
				<td>
					<select style="width: 140px">
						<option value="1">张三</option>
						<option value="2">李四</option>
						<option value="3">王五</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><span style="width: 60px">状态：</span></td>
				<td>
					<select id="messageStatusSelect" style="width: 140px;">
						<option value="0">未处理</option>
						<option value="1">处理中</option>
						<option value="2">处理完毕</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><span style="width: 60px">内容：</span></td>
				<td>
					<textarea rows="5" style="width: 140px"></textarea>
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
        $("#userMessageStatus").find("thead th:first-child").addClass("statusAlive");
        // 初始赋值
        $('#fir_message_status_select').combo({
            multiple: true
        });
        $('#fir_message_status').appendTo($('#fir_message_status_select').combo('panel'));
        $('#sec_message_status_select').combo({
            multiple: true
        });
        $('#sec_message_status').appendTo($('#sec_message_status_select').combo('panel'));
        showUserMessageListDialog.show();
        showUserMessageListDialog.dialog({
			resizable: false,
			height: 450,
			width: 394,
			title: '留言列表',
			modal: false
		});
        showUserMessageListDialog.dialog("open");
        showUserMessageListDialog.window("resize", {top: $(document).scrollTop() + 100, left: $(document).scrollLeft() + (($(document).width() > 1920 ? 1920 : $(document).width()) - 300) / 2});
    }

    function openMessageBox() {
		var showUserMessageDialog = $("#showUserMessageDialog");
        // 判断 新增还是修改  => 赋值
		showUserMessageDialog.show();
		showUserMessageDialog.dialog({
			resizable: false,
			height: 240,
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
		showUserMessageDialog.window("resize", {top: $(document).scrollTop() + 150, left: $(document).scrollLeft() + (($(document).width() > 1920 ? 1920 : $(document).width()) - 200) / 2});
    }
    function searchUserMessageList() {
        var showUserMessageListForm = $("#showUserMessageListForm");
        var messageStatus = showUserMessageListForm.find("#userMessageStatus").find("input[name='messageStatus']").val();
    }

    function changeUserMessageStatus(status, self) {
        var userMessageStatus = $("#userMessageStatus");
        var color = $(self).css("background-color");
        if (color == "rgb(12, 125, 245)"){
            return false;
        }
        userMessageStatus.find("input[name='messageStatus']").val(status);
        var message = $(self).attr("messageStatus");
        switch (message) {
            case "发送":
                $(self).parent().find("th:last-child").addClass("statusAlive");
                $(self).parent().find("th:first-child").removeClass("statusAlive");
                break;
            case "接收":
                $(self).parent().find("th:first-child").addClass("statusAlive");
                $(self).parent().find("th:last-child").removeClass("statusAlive");
                break;
            default:
        }
        searchUserMessageList();
    }
</script>
