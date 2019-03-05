﻿<nav class="navbar navbar-inverse navbar-fixed-top container">
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
				<td>
					<span style="font-size: 12px;">状态: </span>
                    <select id="message_status_select" multiple="multiple" size="5">
                        <option value="0" selected="selected">未处理</option>
                        <option value="1" selected="selected">处理中</option>
                        <option value="2">处理完毕</option>
                    </select>
				</td>
				<td>
					<span style="font-size: 12px;">用户名称: </span>
					<select id="user_list_select" multiple="multiple" size="5">
						<optgroup label="收信人">
							<option value="zhoukai">周凯</option>
							<option value="duchengfu">杜成福</option>
							<option value="wangke">王柯</option>
						</optgroup>
					</select>
				</td>
				<td>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="searchUserMessageList()" value=" 查询 " >
				</td>
                <td style="width: 40px;">
                    <span class="fi-comment" style="font-size: 12px;color: green;">&nbsp;</span><a href="javascript:void(0)" onclick="openMessageBox('new')" style="text-decoration: none;font-size: 12px; color: black">留言</a>
                </td>
			</tr>
		</table>
		<table id="userMessageStatus">
			<input type="hidden" name="messageStatus" value="1">
			<thead style="align-items: center;">
				<th style="width: 197px;" messageStatus="接收" onclick="changeUserMessageStatus(1, this)">
					<a href="javascript:void(0);" class="btn btn-primary btn-block btn-flat" style="text-decoration: none; font-size: 18px; color: white; clear: both;">接收</a>
				</th>
				<th style="width: 197px;" messageStatus="发送" onclick="changeUserMessageStatus(2, this)">
					<a href="javascript:void(0);" class="btn btn-primary btn-block btn-flat" style="text-decoration: none; font-size: 18px; color: white; clear: both;">发送</a>
				</th>
			</thead>
		</table>
		<table id="userMessageListTable" cellpadding="10" style="font-size: 12px; background-color: white;border-collapse: collapse; width: 100%;">
			<thead>
                <tr>
                    <td class="user-message-content">
                        <span>消息内容</span>
                    </td>
                    <td class="user-message-targetName">
                        <span>发送人</span>
                    </td>
                    <td class="user-message-status">
                        <span>处理状态</span>
                    </td>
                </tr>
            </thead>
            <tbody style="height: 200px;">
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="3">
                        <span><a href="javascript:void(0)" onclick="changeUserMessagePaging(-1)" style="text-decoration: none; font-size: 12px; color: black"> 上一页 </a></span>
                        <span id="current-page-number">第 <label></label> 页</span>
                        <span id="total-page-number"> / 共 <label>0</label> 页</span>
                        <span><a href="javascript:void(0)" onclick="changeUserMessagePaging(1)" style="text-decoration: none;font-size: 12px; color: black"> 下一页 </a></span>
                    </td>
                </tr>
            </tfoot>
		</table>
	</form>
</div>

<%--留言栏Dialog--%>
<div id="showUserMessageDialog" class="easyui-dialog" style="display: none">
	<form id="showUserMessageForm">
		<table cellpadding="10" style="font-size: 12px; background-color: white; border-collapse:separate; border-spacing:0px 10px;">
			<tr>
                <input type="hidden" name="messageUuid" value="">
				<td id="userMessageTargetStatus"><span style="width: 60px">收信人：</span></td>
				<td>
                    <select id="user_select" multiple="multiple" size="5">
                        <option value="duchengfu">杜成福</option>
                        <option value="zhoukai">周凯</option>
                        <option value="wangke">王柯</option>
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
        getUserMessage(1);
        var showUserMessageListDialog = $("#showUserMessageListDialog");
        $("#userMessageStatus").find("thead th:first-child").addClass("statusAlive");
        $("#userMessageStatus").find("thead th:last-child").removeClass("statusAlive");
        // 初始赋值
        showUserMessageListDialog.show();
        showUserMessageListDialog.dialog({
			resizable: false,
			height: 318,
			width: 394,
			title: '留言列表',
			modal: false,
			onClose: function () {
				$("#showUserMessageListForm")[0].reset();
            }
		});
        showUserMessageListDialog.dialog("open");
        showUserMessageListDialog.window("resize", {top: $(document).scrollTop() + 100, left: $(document).scrollLeft() + (($(document).width() > 1920 ? 1920 : $(document).width()) - 394) / 2});
    }

    function getUserMessage(messageStatus) {
        $("#userMessageStatus").find("input[name='messageStatus']").val(messageStatus);
        searchUserMessageList();
    }

    function openMessageBox(status) {
		var showUserMessageDialog = $("#showUserMessageDialog");
		var buttonText = "发送";
        if (status == "update") {
            buttonText = "保存";
            var uuid = $("#showUserMessageForm").find("input[name='messageUuid']").val();
            $.ajax({
                url: "/internal/usermessage/getUserMessage/" + uuid,
                type: "POST",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                success: function(data) {
                    var messageStatus = $("#userMessageStatus").find("input[name='messageStatus']").val();
                    var text = "";
                    var targetName = "";
                    if (messageStatus == "1") {
                        text = "写信人：";
                        targetName = data.senderUserName;
                    } else {
                        text = "收信人：";
                        targetName = data.receiverUserName;
                    }
                    $("#showUserMessageForm").find("#userMessageTargetStatus").text(text);
                    $("#showUserMessageForm").find("#user_select option").each(function () {
                        if ($(this).val() == targetName) {
                            this.selected = true;
                            $("#user_select_ms span:last-child").text($(this).text());
                        }
                    });
                    $("#showUserMessageForm").find("#messageStatusSelect option[value='"+ data.status +"']").prop("selected", true);
                    $("#showUserMessageForm textarea").text(data.content);
                },
                error: function () {
                    $().toastmessage("showErrorToast", "获取失败！")
                }
            });
        }
		showUserMessageDialog.show();
		showUserMessageDialog.dialog({
			resizable: false,
			height: 240,
			width: 200,
			title: '留言框',
			modal: false,
			buttons: [{
			    text: buttonText,
				iconCls: "icon-ok",
				handler: function () {
					saveUserMessage(status);
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
        $("#userMessageListTable tbody").empty();
        var showUserMessageListForm = $("#showUserMessageListForm");
        var messageStatus = $("#userMessageStatus").find("input[name='messageStatus']").val();
        var status = showUserMessageListForm.find("#message_status_select").multiselect("getChecked").map(function () {
			return this.value;
        }).get();
        var targetUserName = showUserMessageListForm.find("#user_list_select").multiselect("getChecked").map(function () {
            return this.value;
        }).get();
        var pageNumber = showUserMessageListForm.find("#current-page-number label").text();
        if (pageNumber == "" || pageNumber == 0 || targetUserName.length > 0){
            pageNumber = 1;
        }
        var postData = {};
        postData.messageStatus = messageStatus;
        postData.status = status;
        postData.targetUserName = targetUserName;
        postData.pageNumber = pageNumber;
        $.ajax({
			url: "/internal/usermessage/getUserMessageList",
			type: "POST",
			data: JSON.stringify(postData),
			headers: {
			    "Accept": "application/json",
				"Content-Type": "application/json"
			},
			success: function (data) {
                if (data.pageTotalNumber == 0) {
                    data.pageNumber = 0;
                }
                $("#showUserMessageListForm").find("input[name='messageStatus']").val(data.messageStatus);
                $("#showUserMessageListForm").find("#current-page-number label").text(data.pageNumber);
                $("#showUserMessageListForm").find("#total-page-number label").text(data.pageTotalNumber);
                $.each(data.userMessageLists, function (idx, val) {
                    var userName = '';
                    var status = '';
                    if (data.messageStatus == "1") {
                        userName = val.senderUserName;
                    } else {
                        userName = val.receiverUserName;
                    }
                    switch (val.status) {
                        case '0':
                            status = '未处理';
                            break;
                        case '1':
                            status = '处理中';
                            break;
                        case '2':
                            status = "处理完毕";
                            break;
                    }
                    $("#userMessageListTable tbody").append("<tr messageUuid='"+ val.uuid +"' onclick='updateUserMessage(this)' ondblclick='updateUserMessageStatus(this)'>" +
                        "<td>" +
                        "<span class='user-message-content'>"+ val.content +"</span>" +
                        "</td>" +
                        "<td>" +
                        "<span class='user-message-targetName'>"+ userName +"</span>" +
                        "</td>" +
                        "<td>" +
                        "<span class='user-message-status'>"+ status +"</span>" +
                        "</td>" +
                        "</tr>")
                });
            },
			error: function () {
				$().toastmessage("showErrorToast", "查询失败！");
            }
		});
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
                $("#userMessageListTable td.user-message-targetName span").text("接收人");
                $("#showUserMessageListForm").find("#current-page-number label").text(1);
                break;
            case "接收":
                $(self).parent().find("th:first-child").addClass("statusAlive");
                $(self).parent().find("th:last-child").removeClass("statusAlive");
                $("#userMessageListTable td.user-message-targetName span").text("发送人");
                $("#showUserMessageListForm").find("#current-page-number label").text(1);
                break;
        }
        searchUserMessageList();
    }
    
    function saveUserMessage(status, type) {
        var postData = {};
        if (status == "new") {
            var targetUserName = $("#showUserMessageForm").find("#user_select").multiselect("getChecked").map(function () {
                return this.value;
            }).get();
            postData.targetUserName = targetUserName;
        }
        if (status == "update") {
            var uuid = $("#showUserMessageForm").find("input[name='messageUuid']").val();
            postData.uuid = uuid;
        }
        var messageStatusArr = [];
        var messageStatus = "";
        if (type) {
            messageStatus = "2";
        } else {
            messageStatus = $("#showUserMessageForm").find("#messageStatusSelect").val();
            var content = $("#showUserMessageForm").find("textarea").val();
            content = content.replace(/\n/g, " ");
            postData.content = content;
        }
        messageStatusArr.push(messageStatus);
        postData.status = messageStatusArr;
        $.ajax({
            url: '/internal/usermessage/saveUserMessages',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(postData),
            success: function(data) {
                if (data) {
                    $().toastmessage("showSuccessToast", "保存成功！");
                    $("#showUserMessageDialog").dialog("close");
                    searchUserMessageList();
                } else {
                    $().toastmessage("showErrorToast", "保存失败！");
                }
            },
            error: function () {
                $().toastmessage("showErrorToast", "保存失败！");
            }
        });
    }

    function changeUserMessagePaging(number) {
        var pageNumber = $("#showUserMessageListForm").find("#current-page-number label").text();
        var pageTotalNumber = $("#showUserMessageListForm").find("#total-page-number label").text();
        pageNumber = parseInt(pageNumber) + number;
        if (pageNumber < 1 || pageNumber > pageTotalNumber) {
            return false;
        }
        $("#showUserMessageListForm").find("#current-page-number label").text(pageNumber);
        searchUserMessageList();
    }

    var TimeFn = null;
    function updateUserMessage(tr) {
        var uuid = $(tr).attr("messageuuid");
        $("#showUserMessageForm").find("input[name='messageUuid']").val(uuid);
            // 取消上次延时未执行的方法
		clearTimeout(TimeFn);
		// 执行延时
		TimeFn = setTimeout(function () {
			openMessageBox("update");
		}, 300);
    }

    function updateUserMessageStatus(tr) {
        var uuid = $(tr).attr("messageuuid");
        $("#showUserMessageForm").find("input[name='messageUuid']").val(uuid);
		clearTimeout(TimeFn);
		saveUserMessage("update", 1);
    }
</script>
