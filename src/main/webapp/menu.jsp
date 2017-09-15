<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/commons/basejs.jsp" %>
<%--<link href="/css/menu.css" rel="stylesheet" type="text/css"/>--%>
<link href="${path}/css/sskjMenu.css" rel="stylesheet">
<script type="text/javascript" src="${path}/js/sskjMenu.js"></script>
<%--<script language="javascript" type="text/javascript" src="${path}/js/slide1.12.4.js"></script>--%>
<script type="text/javascript">

    $(function(){
        var li=$(".venus-menu li");
        $.each(li,function(idx,val){
            if($(val).attr("pid")!=null && $(val).attr("pid")!='')
            {
				/*$(val).append("<ul style='display: none;' id='"+$(val).attr("id")+"'></ul>");*/
				/*$("#"+$(val).attr("pid")).append("<ul style='display: none;' id='"+$(val).attr("pid")+"'></ul>");*/
                $("li[lid="+$(val).attr("pid")+"]").append("<ul style='display: none;' id='"+$(val).attr("pid")+"'></ul>");
                $.each(li,function(idx1,val1){
                    if($(val1).attr("lid")==$(val).attr("pid"))
                    {
                        $("li").find("#"+$(val).attr("pid")).append($(val));
                    }
                })
            }
        });
        $().maps();
    });

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
</script>

<div class="content" style="position:fixed;width: 100%">
	<ul class="venus-menu">
		<c:forEach items="${menus}" var="menu">
			<li style="" pid="${menu.pid}" lid="${menu.id}"><a href="${menu.attributes}">${menu.text}</a></li>
		</c:forEach>
		<div style="float: right;margin:10px 60px 0px 0px;">
			<i class="fi-torso"></i>
			<b><shiro:principal></shiro:principal></b>&nbsp;&nbsp;
			<shiro:hasPermission name="/user/editPwdPage">
				<a href="javascript:void(0)" onclick="editUserPwd()" class="easyui-linkbutton" plain="true"
				   icon="fi-unlock">修改密码</a>
			</shiro:hasPermission>&nbsp;&nbsp;
			<a href="javascript:void(0)" onclick="logout()" class="easyui-linkbutton" plain="true" icon="fi-x">安全退出</a>
		</div>
	</ul>
</div>