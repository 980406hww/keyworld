<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style type="text/css">
        #showTableDiv {
            width: 100%;
            margin: auto;
        }
        #showOnlineUserTable tr:nth-child(odd){
            background:#EEEEEE;
        }
    </style>
    <link rel="stylesheet" href="${staticPath }/static/toastmessage/css/jquery.toastmessage.css">
    <link rel="stylesheet" href="${staticPath}/static/css/common.css">
    <title>在线用户管理</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <h3>在线用户数：<span>${list.size()}</span><a style="float: right" href="${staticPath}/login">去登录</a> </h3>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width=100>SessionID</td>
            <td align="center" width=60>用户名称</td>
            <td align="center" width=60>主机</td>
            <td align="center" width=60>终端</td>
            <td align="center" width=60>链接入口</td>
            <td align="center" width=100>登录时间</td>
            <td align="center" width=100>最后访问时间</td>
            <td align="center" width=60>状态</td>
            <td align="center" width=80>操作</td>
        </tr>
        <c:forEach items="${list}" var="onlineUser">
            <tr height=30 onmouseover="doOver(this);" onmouseout="doOut(this);">
                <td align="center" width=100>${onlineUser.id}</td>
                <td align="center" width=60>${onlineUser.shiroUser.name}</td>
                <td align="center" width=60>${onlineUser.host}</td>
                <td align="center" width=60>${onlineUser.terminalType}</td>
                <td align="center" width=60>${onlineUser.entryType}</td>
                <td align="center" width=100><fmt:formatDate value="${onlineUser.startTimestamp}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                <td align="center" width=100><fmt:formatDate value="${onlineUser.lastAccessTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                <td align="center" width=60>${onlineUser.status}</td>
                <td align="center" width=80><a href="#" onclick="offline('${onlineUser.id}', '${onlineUser.status}')">下线</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
<script type="text/javascript" src="${staticPath }/static/easyui/jquery.easyui.min.js" charset="utf-8"></script>
<script src="${staticPath }/static/toastmessage/jquery.toastmessage.js"></script>
<script type="text/javascript">
    $(function () {
        window.onresize = function(){
            $("#showTableDiv").css("margin-top",$("#topDiv").height());
        };
    });

    function doOver(obj) {
        obj.style.backgroundColor = "#c3dfb7";
    }

    function doOut(obj) {
        var rowIndex = obj.rowIndex;
        if ((rowIndex % 2) === 0) {
            obj.style.backgroundColor = "#eeeeee";
        } else {
            obj.style.backgroundColor = "#ffffff";
        }
    }

    function offline(id, status){
        if(status === "离线"){
            alert("该用户已是离线状态！！");
            return;
        }
        $.ajax({
            url: '/internal/useronline/forceLogout/' + id,
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', '强制下线成功！！！', true);
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', '踢出用户失败！！！');
            }
        });
    }
</script>
</body>
</html>