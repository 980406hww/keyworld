<nav class="navbar navbar-inverse navbar-fixed-top container">
	<div class="content" style="width: 100%;height:30px;">
		<ul class="venus-menu" style="display: none">
			<c:choose>
				<c:when test="${sessionScope.get('entryType')=='bc'}">
					<shiro:hasRole  name="BCSpecial">
						<c:forEach items="${menus}" var="menu">
							<li style="" pid="${menu.pid}" lid="${menu.id}">
								<c:if test="${menu.openMode=='ajax' || menu.openMode=='iframe'}">
									<a href="${menu.attributes}" target="_blank" onclick="openUrl('${menu.attributes}','${menu.text}','${menu.iconCls}','${menu.openMode}')" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
								</c:if>
								<c:if test="${menu.openMode==null || menu.openMode==''}">
									<a href="${menu.attributes}" target="_blank" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
								</c:if>
							</li>
						</c:forEach>
					</shiro:hasRole >
				</c:when>
				<c:otherwise>
					<c:forEach items="${menus}" var="menu">
						<li style="" pid="${menu.pid}" lid="${menu.id}">
							<c:if test="${menu.openMode=='ajax' || menu.openMode=='iframe'}">
								<a href="${menu.attributes}" target="_blank" onclick="openUrl('${menu.attributes}','${menu.text}','${menu.iconCls}','${menu.openMode}')" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
							</c:if>
							<c:if test="${menu.openMode==null || menu.openMode==''}">
								<a href="${menu.attributes}" target="_blank" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
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
				<a href="javascript:void(0)" onclick="OpenMessageList(1)" id="userMessageText" style="text-decoration: none;font-size: 12px; color: black">留言列表</a>|
				<shiro:hasPermission name="/user/editPwdPage">
					<span class="fi-unlock icon-green" style="font-size: 12px;color: green"></span>
					<a href="javascript:void(0)" onclick="editUserPwd()"  style="text-decoration: none;font-size: 12px;color: black">修改密码</a>|
				</shiro:hasPermission>
				<a class="fi-x" href="javascript:void(0)" onclick="logout()" style="text-decoration: none;font-size: 12px;">&nbsp;安全退出</a>
			</div>
		</ul>
	</div>
</nav>

<%--留言队列Dialog--%>
<div id="showUserMessageQueueDialog" class="easyui-dialog" style="display: none">
	<form id="showUserMessageQueueForm">
		<table id="userMessageListConditionTable" cellpadding="10" style="font-size: 12px; background-color: white;border-collapse: collapse; width: 100%;">
			<tr>
				<td>
					<span style="font-size: 12px;">跳转类型:</span>
					<select id="message_type_select" style="width: 80px">
						<option value="">所有</option>
						<option value="全站设置">全站设置</option>
						<option value="关键字列表">关键字列表</option>
					</select>
				</td>
				<td>
					<span style="font-size: 12px;">状态:</span>
					<select id="message_status_select">
						<option value="">所有</option>
						<option value="0">未处理</option>
						<option value="1">处理完毕</option>
					</select>
				</td>
				<td>
					<span style="font-size: 12px;">时间:</span>
					<input name="messageCreateDate" id="messageCreateDate" class="Wdate" style="width: 80px" type="text" onclick="WdatePicker()">
				</td>
				<td>
					<a href="javascript:void(0)" onclick="showConditionTr();"><span class="fi-magnifying-glass" style="font-size: 12px;color: black;">更多搜索</span></a>&nbsp;
				</td>
				<td>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="searchUserMessageQueue(0, 1)" value=" 查询 " >
				</td>
			</tr>
		</table>
		<table id="userMessageQueueTable" cellpadding="10" style="font-size: 12px; background-color: white;border-collapse: collapse; width: 100%;">
			<thead>
				<tr>
					<td class="user-message-type">
						<span>类型</span>
					</td>
					<td class="user-message-contactPerson">
						<span>客户</span>
					</td>
					<td class="user-message-userName">
						<span>写信人</span>
					</td>
					<td class="user-message-userName">
						<span>收信人</span>
					</td>
					<td class="user-message-status">
						<span>状态</span>
					</td>
				</tr>
				<tr style="display: none;">
					<td class="user-message-type"><input type="text" name="type" style="width: 75px;" readonly="readonly"></td>
					<td class="user-message-contactPerson"><input type="text" name="contactPerson" onkeyup="searchUserMessageQueue()" style="width: 150px;"></td>
					<td class="user-message-userName"><input type="text" name="senderUserName" onkeydown="enterInUserMessage()" style="width: 81px;"></td>
					<td class="user-message-userName"><input type="text" name="receiverUserName" onkeydown="enterInUserMessage()" style="width: 81px;"></td>
					<td class="user-message-status"><input type="text" name="messageStatus" style="width: 60px;" readonly="readonly"></td>
				</tr>
			</thead>
			<tbody></tbody>
			<tfoot>
				<tr>
					<td><span><a href="javascript:void(0)" onclick="changeUserMessagePaging(-1)" style="text-decoration: none; font-size: 12px; color: black"> 上一页 </a></span></td>
					<td colspan="3">
						<span id="current-page-number">第 <label></label> 页</span>
						<span id="total-page-number"> / 共 <label>0</label> 页</span>
					</td>
					<td><span><a href="javascript:void(0)" onclick="changeUserMessagePaging(1)" style="text-decoration: none;font-size: 12px; color: black"> 下一页 </a></span></td>
				</tr>
			</tfoot>
		</table>
	</form>
</div>

<form id="searchCustomerKeywordForm_jump" method="post" target="_blank" action="/internal/customerKeyword/searchCustomerKeywords" style="display: none;">
    <input type="text" name="customerUuid" id="customerUuid">
    <input type="text" name="status" id="status">
    <input type="text" name="openDialogStatus" id="openDialogStatus">
</form>

<form id="searchQZSettingForm_jump" method="post" target="_blank" action="/internal/qzsetting/searchQZSettings" style="display: none;">
    <input type="text" name="customerUuid" id="customerUuid">
    <input type="text" name="openDialogStatus" id="openDialogStatus">
</form>

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

    function OpenMessageList(openStatus) {
        searchUserMessageQueue(openStatus);
        var showUserMessageQueueDialog = $("#showUserMessageQueueDialog");
        showUserMessageQueueDialog.show();
        showUserMessageQueueDialog.dialog({
			resizable: false,
			height: 290,
			width: 460,
			title: '查看留言队列',
			modal: false,
			onClose: function () {
                $("#userMessageQueueTable").find("thead tr:last-child").css("display", "none");
                $("#showUserMessageQueueForm")[0].reset();
            }
		});
        showUserMessageQueueDialog.dialog("open");
        showUserMessageQueueDialog.window("resize", {top: $(document).scrollTop() + 100, left: $(document).scrollLeft() + 755});
    }

    function showConditionTr() {
        var type = $("#showUserMessageQueueForm").find("#message_type_select").val();
        var status = $("#showUserMessageQueueForm").find("#message_status_select").val();
        $("#userMessageQueueTable").find("thead tr:last-child").find("input[name='type']").val(type === '' ? '所有' : type);
        $("#userMessageQueueTable").find("thead tr:last-child").find("input[name='messageStatus']").val(status === '' ? '所有' : (status === 0 ? "未处理" : "处理完毕"));
        $("#userMessageQueueTable").find("thead tr:last-child").toggle();
    }

    function searchUserMessageQueue(openStatus, query) {
        var postData = {};
        var showUserMessageQueueForm = $("#showUserMessageQueueForm");
        var type = showUserMessageQueueForm.find("#message_type_select").val();
        if (type !== '') {
            postData.type = type;
		}
        var status = showUserMessageQueueForm.find("#message_status_select").val();
        if (status !== '') {
            postData.status = status;
		} else {
            if (openStatus){
                status = 0;
                showUserMessageQueueForm.find("#message_status_select").val(status);
            }
            postData.status = status;
        }
        var date = showUserMessageQueueForm.find("#messageCreateDate").val();
        if (date !== '') {
            postData.date = date.trim();
		}
        var userMessageQueueTr = $("#userMessageQueueTable").find("thead tr:last-child");
        var contactPerson = userMessageQueueTr.find("input[name='contactPerson']").val();
        if (contactPerson !== '') {
            postData.contactPerson = contactPerson.trim();
		}
		var senderUserNames = [];
        var senderUserName = userMessageQueueTr.find("input[name='senderUserName']").val();
        if (senderUserName !== '') {
            senderUserNames = senderUserName.replace(/[，]/g, ",").replace(/[\s+]/g, "").split(',');
            senderUserNames = senderUserNames.filter(function(name, index) {
                return senderUserNames.indexOf(name) === index && name !== '';
            });
            postData.senderUserNames = senderUserNames;
		}
		var receiverUserNames = [];
        var receiverUserName = userMessageQueueTr.find("input[name='receiverUserName']").val();
        if (receiverUserName !== '') {
            receiverUserNames = receiverUserName.replace(/[，]/g, ",").replace(/[\s+]/g, "").split(',');
            receiverUserNames = receiverUserNames.filter(function(name, index) {
                return receiverUserNames.indexOf(name) === index && name !== '';
            });
            postData.receiverUserNames = receiverUserNames;
        }
        var pageNumber = $("#showUserMessageQueueForm").find("#current-page-number label").text();
        if (query || pageNumber === "" || pageNumber === 0 || ((pageNumber === "" || pageNumber === 0))){
            pageNumber = 1;
        }
        postData.pageNumber = pageNumber;
        $.ajax({
			url: "/internal/usermessage/getUserMessages",
			type: "POST",
			data: JSON.stringify(postData),
			headers: {
			    "Accept": "application/json",
				"Content-Type": "application/json"
			},
			success: function (data) {
                var tbody = $("#userMessageQueueTable tbody");
                tbody.empty();
                $.each(data.records, function (idx, val) {
                    var status = val.status == 0 ? '未处理' : '处理完毕';
                    tbody.append("<tr title='点击跳转此行' onclick='jumpToCorrespondJspPage("+ JSON.stringify(val) +")'>" +
						"<td>"+ val.type +"</td>" +
						"<td title='"+ val.contactPerson + "__" + val.customerUuid +"'><span class='user-message-contactPerson'>"+ val.contactPerson + "__" + val.customerUuid +"</span></td>" +
						"<td title='"+ val.senderUserName +"'><sapn>"+ val.senderUserName +"</sapn></td>" +
						"<td title='"+ val.receiverUserName +"'><sapn>"+ val.receiverUserName +"</sapn></td>" +
						"<td>"+ status +"</td>" +
						"</tr>");
                });
                if (data.pages == 0) {
                    data.current = 0;
                }
                if (data.current > data.pages) {
                    data.current = data.pages;
				}
                $("#showUserMessageQueueForm").find("#current-page-number label").text(data.current);
                $("#showUserMessageQueueForm").find("#total-page-number label").text(data.pages);
            },
			error: function () {
				$().toastmessage("showErrorToast", "查询失败！");
            }
		});
    }

    function enterInUserMessage(e) {
        var e = e || event,
            keyCode = e.which || e.keyCode;
        if (keyCode == 13) {
            searchUserMessageQueue();
        }
    }

    function changeUserMessagePaging(number) {
        var pageNumber = $("#showUserMessageQueueForm").find("#current-page-number label").text();
        var pageTotalNumber = $("#showUserMessageQueueForm").find("#total-page-number label").text();
        pageNumber = parseInt(pageNumber) + number;
        if (pageNumber < 1 || pageNumber > pageTotalNumber) {
            return false;
        }
        $("#showUserMessageQueueForm").find("#current-page-number label").text(pageNumber);
        searchUserMessageQueue();
    }

    function jumpToCorrespondJspPage(result) {
        if (result.type === '全站设置') {
            jumpSearchQZSettings(result.customerUuid);
        } else {
            jumpSearchCustomerKeywords(result.customerUuid);
        }
    }

    function jumpSearchCustomerKeywords(customerUuid) {
        var searchCustomerKeywordForm_jump = $("#searchCustomerKeywordForm_jump");
        searchCustomerKeywordForm_jump.find("#customerUuid").val(customerUuid);
        searchCustomerKeywordForm_jump.find("#status").val(1);
        searchCustomerKeywordForm_jump.find("#openDialogStatus").val(1);
        searchCustomerKeywordForm_jump.submit();
    }

    function jumpSearchQZSettings(customerUuid) {
        var searchQZSettingForm_jump = $("#searchQZSettingForm_jump");
        searchQZSettingForm_jump.find("#customerUuid").val(customerUuid);
        searchQZSettingForm_jump.find("#openDialogStatus").val(2);
        searchQZSettingForm_jump.submit();
    }
</script>
