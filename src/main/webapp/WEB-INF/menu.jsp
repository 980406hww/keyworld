<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/commons/global.jsp" %>
<link rel="stylesheet" type="text/css" href="${staticPath }/static/foundation-icons/foundation-icons.css" />
<link href="${path}/css/sskjMenu.css" rel="stylesheet">
<%--[消息提示]--%>
<script language="javascript" type="text/javascript" src="/toastmessage/jquery.toastmessage.js"></script>
<link rel="stylesheet" href="/toastmessage/css/jquery.toastmessage.css">
<script type="text/javascript" src="${path}/js/sskjMenu.js"></script>
<script type="text/javascript">

    $(function () {
        var li = $(".venus-menu li");
        $.each(li, function (idx, val) {
            if ($(val).attr("pid") != null && $(val).attr("pid") != '') {
                $("li[lid=" + $(val).attr("pid") + "]").append("<ul style='display: none;' id='" + $(val).attr("pid") + "'></ul>");
                $.each(li, function (idx1, val1) {
                    if ($(val1).attr("lid") == $(val).attr("pid")) {
                        $("li").find("#" + $(val).attr("pid")).append($(val));
                    }
                })
            }
        });
        $().maps();
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
    //修改密码
    function editUserPwd() {
        $('#editUserPwdForm')[0].reset();
        $("#editUserPwdDiv").dialog({
            resizable: false,
            width: 320,
            height: 220,
            modal: true,
            //按钮
            buttons: {
                "保存": function () {
					var editUserPwdForm = $("#editUserPwdForm");
					var oldPwd = editUserPwdForm.find("#oldPwd").val();
					var pwd = editUserPwdForm.find("#pwd").val();
					var rePwd = editUserPwdForm.find("#rePwd").val();
                    var postData = {};
					if(pwd!=(rePwd)){
                        $().toastmessage('showErrorToast', "2次密码不一致");
                        return;
					}
                    postData.oldPwd = oldPwd;
                    postData.pwd = pwd;
                    $.ajax({
                        url: '${path }/user/editUserPwd',
                        data: JSON.stringify(postData),
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        timeout: 5000,
                        type: 'POST',
                        success: function (result) {
                            if (result.success) {
                                $().toastmessage('showSuccessToast', result.msg);
                                setTimeout(function () {
                                    logoutService();
                                },4000);
                            } else {
                                $().toastmessage('showErrorToast', result.msg);
                            }
                        },
                        error: function () {
                            $().toastmessage('showErrorToast', "操作失败");
                        }
                    });
                    $(this).dialog("close");
                },
                "清空": function () {
                    $('#editUserPwdForm')[0].reset();
                },
                "取消": function () {
                    $(this).dialog("close");
                    $('#editUserPwdForm')[0].reset();
                }
            }
        });
    }

    function logout(){
        if (confirm("确定要退出?") == false) return;
        logoutService();
    }
    function logoutService() {
        $.ajax({
            url: '/logout',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (result) {
                if(result.success){
                    window.location.href='${path }';
                }
            }
        });
    }
</script>

<div class="content" style="position:fixed;width: 100%">
	<ul class="venus-menu">
		<c:forEach items="${menus}" var="menu">
			<li style="" pid="${menu.pid}" lid="${menu.id}">
				<c:if test="${menu.openMode=='ajax' || menu.openMode=='iframe'}">
					<a href="javascript:void(0)" onclick="openUrl('${menu.attributes}','${menu.text}','${menu.iconCls}','${menu.openMode}')" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
				</c:if>
				<c:if test="${menu.openMode==null}">
					<a href="${menu.attributes}" title="${menu.attributes}"><i class="${menu.iconCls}"></i> ${menu.text}</a>
				</c:if>
			</li>
		</c:forEach>
		<div style="float: right;margin:10px 60px 0px 0px;">
			<b class="fi-torso icon-black" style="font-size: 14px;">&nbsp;<shiro:principal></shiro:principal></b>|
			<span class="fi-anchor icon-green" style="font-size: 14px;color: green"></span>
			<span style="font-size: 14px;color: black">
				<c:choose>
					<c:when test="${sessionScope.get('entry')=='qz'}">
						全站链接
					</c:when>
					<c:when test="${sessionScope.get('entry')=='pt'}">
						普通链接
					</c:when>
					<c:otherwise>
						负面链接
					</c:otherwise>
				</c:choose>
			</span>|
			<span class="fi-web icon-black" style="font-size: 14px;color: red;"></span><span style="color: black">&nbsp;${sessionScope.get("terminalType")}端&nbsp;|</span>&nbsp;
			<shiro:hasPermission name="/user/editPwdPage">
				<span class="fi-unlock icon-green" style="font-size: 14px;color: green"></span>
				<a href="javascript:void(0)" onclick="editUserPwd()"  style="text-decoration-line: none;font-size: 14px;color: black">修改密码</a>
			</shiro:hasPermission>|
			<a class="fi-x" href="javascript:void(0)" onclick="logout()" style="text-decoration-line: none;font-size: 14px;">&nbsp;安全退出</a>
		</div>
	</ul>
</div>

<div id="editUserPwdDiv" data-options="region:'center',border:false" title="修改密码" style="overflow: hidden;display:none;">
	<form id="editUserPwdForm" method="post">
		<table>
			<tr>
				<th>登录名：</th>
				<td><shiro:principal></shiro:principal></td>
			</tr>
			<tr>
				<th>原密码：</th>
				<td><input id="oldPwd" name="oldPwd" type="password" placeholder="请输入原密码" required="required"></td>
			</tr>
			<tr>
				<th>新密码：</th>
				<td><input id="pwd" name="pwd" type="password" placeholder="请输入新密码" required="required"></td>
			</tr>
			<tr>
				<th>重复密码：</th>
				<td><input id="rePwd"  name="rePwd" type="password" placeholder="请再次输入新密码" required="required"></td>
			</tr>
		</table>
	</form>
</div>