<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ include file="/commons/basejs.jsp" %>
    <%@ include file="/commons/global.jsp" %>
    <script language="javascript" type="text/javascript" src="/common.js"></script>
    <style type="text/css">
        ul{list-style: none}
        ul li{margin: 5px}
        ul li span{width: 100px;text-align: right;display: inline-block}
        input[type='radio']{ margin: 0 5px 0 10px;}
    </style>
    <title>客户管理</title>
    <script language="javascript" type="text/javascript">
        $(function(){
            $("#centerDiv").css("margin-top",$("#topDiv").height());
            alignTableHeader();
            window.onresize = function(){
                alignTableHeader();
            }
        })

        function alignTableHeader(){
            var td = $("#showCaptureCurrentRankJobTable tr:first td");
            var ctd = $("#headerTable tr:first td");
            $.each(td, function (idx, val) {
                ctd.eq(idx).width($(val).width());
            });
        }
        function setCrawlRanking()
        {
            var CaptureCurrentRankJob={};
            CaptureCurrentRankJob.groupNames=$("#groupNames").val();
            CaptureCurrentRankJob.customerIds=$("#customerIds").val();
            CaptureCurrentRankJob.operationType=$("input[name=operationType]:checked").val();
            CaptureCurrentRankJob.exectionTime=$("#exectionTime").val();
            CaptureCurrentRankJob.exectionStatus="prepared";
            $.ajax({
                url: '/internal/crawlRanking/setCrawlRanking',
                data:JSON.stringify(CaptureCurrentRankJob),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 50000,
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
    </script>
</head>
<body>
<div id="topDiv" style="height: 100px">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 35px">
        组名:<input type="text">客户ID:<input type="text">执行类型:<input type="text">状态:<input type="text"><input type="button" value="查询">
        <input type="button" value="添加">
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>组名</td>
            <td align="center" width=80>客户ID</td>
            <td align="center" width=60>执行类型</td>
            <td align="center" width=100>执行时间</td>
            <td align="center" width=60>状态</td>
            <td align="center" width=100>创建时间</td>
            <td align="center" width=100>修改时间</td>
            <td align="center" width=100>开始抓取时间</td>
            <td align="center" width=60>最后抓取时间</td>
        </tr>
    </table>
</div>
<div id="centerDiv">
    <table style="font-size:12px; width: 100%;" id="showCaptureCurrentRankJobTable" style="margin-top: 100px">
        <c:forEach items="${captureCurrentRankJobs}" var="captureCurrentRankJob" varStatus="status">
        <tr align="left" onmouseover="doOver(this);" onmouseout="doOut(this);" height=30  <c:if test="${status.index%2==0}">bgcolor="#eee"</c:if> >
            <td width=10 align="center"><input type="checkbox" name="captureCurrentRankJobUuid" value="${captureCurrentRankJob.uuid}"/></td>
            <td width=150>${captureCurrentRankJob.groupNames}</td>
            <td width=150>${captureCurrentRankJob.customerIds}</td>
            <td width=60>${captureCurrentRankJob.operationType}</td>
            <td width=60>${captureCurrentRankJob.exectionTime}</td>
            <td width=50>${captureCurrentRankJob.exectionStatus}</td>
            <td width=100>${captureCurrentRankJob.createTime}</td>
            <td width=100>${captureCurrentRankJob.updateTime}</td>
            <td width=60><fmt:formatDate value="${captureCurrentRankJob.startTime}" pattern="yyyy-MM-dd"/></td>
            <td width=100><fmt:formatDate value="${captureCurrentRankJob.endTime}" pattern="yyyy-MM-dd"/></td>
        </tr>
        </c:forEach>
    </table>

        <form id="crawlRankingForm">
            <ul>
                <li><span>输入优化组名:</span><input type="text" name="groupNames" id="groupNames"></li>
                <li><span>输入客户名:</span><input type="text" name="customerIds" id="customerIds"></li>
                <li><span>抓取方式:</span><input type="radio" name="operationType" checked  value="once">一次性</label><input type="radio" name="operationType" value="everyday">每天</li>
                <li><span>需要执行的时间:</span><input type="text" name="exectionTime" id="exectionTime"></li>
                <li><span></span><input type="button" value="开始抓取" onclick="setCrawlRanking()"></li>
            </ul>
        </form>
</div>
</body>
</html>