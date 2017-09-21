<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/commons/global.jsp" %>
<%--<%@ include file="/commons/basejs.jsp" %>--%>
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
                progressLoad();
                $.post('${path }/logout', function(result) {
                    if(result.success){
                        progressClose();
                        window.location.href='${path }';
                    }
                }, 'json');
            }
        });
    }

    function editUserPwd() {
        parent.$.modalDialog({
            title : '修改密码',
            width : 250,
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
</script>

<div class="content" style="position:fixed;width: 100%;height:30px;">
	<ul class="venus-menu" style="display: none">
		<c:choose>
			<c:when test="${sessionScope.get('entryType')=='fm'}">
				<shiro:hasRole  name="FMSpecial">
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
			<shiro:hasPermission name="/user/editPwdPage">
				<span class="fi-unlock icon-green" style="font-size: 12px;color: green"></span>
				<a href="javascript:void(0)" onclick="editUserPwd()"  style="text-decoration: none;font-size: 12px;color: black">修改密码</a>|
			</shiro:hasPermission>
			<a class="fi-x" href="javascript:void(0)" onclick="logout()" style="text-decoration: none;font-size: 12px;">&nbsp;安全退出</a>
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