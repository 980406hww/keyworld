<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${staticPath}/static/select2/select2-4.0.6.min.css">
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
    <%@ include file="/commons/basejs.jsp" %>
    <%@ include file="/commons/global.jsp" %>
    <style type="text/css">
        ul{list-style: none}
        ul li{margin: 5px}
        ul li > span{width: 100px;text-align: right;display: inline-block}
        input[type='radio']{ margin: 0 5px 0 10px;}
        input[type='checkbox']{ margin: 0 5px 0 10px;}
        .panel.window {z-index: 1000 !important;}
        .window-shadow {z-index: 999 !important;}
        .window-mask {z-index: 998 !important;}
    </style>
    <title>抓排名任务管理</title>
</head>
<body>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 5px;margin-bottom: 5px">
        <form method="post" id="searchCaptureRankJobForm" action="/internal/captureRank/searchCaptureRankJobs" style="margin-bottom:0px ">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}" />
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            组名:<input type="text" name="groupNames" id="groupNames" value="${captureRankJobSearchCriteria.groupNames}">
            <input id="groupNameFuzzyQuery" name="groupNameFuzzyQuery" type="checkbox" value="groupNameFuzzyQuery" ${captureRankJobSearchCriteria.groupNameFuzzyQuery != null ? "checked=true" : ""}/>模糊查询 &nbsp;
            执行类型:
            <select name="exectionType">
                <option value="">请选择执行类型</option>
                <option value="Once" <c:if test="${captureRankJobSearchCriteria.exectionType.equals('Once')}">selected="selected"</c:if>>Once</option>
                <option value="Everyday" <c:if test="${captureRankJobSearchCriteria.exectionType.equals('Everyday')}">selected="selected"</c:if>>Everyday</option>
            </select>
            执行状态:
            <select name="exectionStatus">
                <option value="">请选择执行状态</option>
                <option value="New" <c:if test="${captureRankJobSearchCriteria.exectionStatus.equals('New')}">selected="selected"</c:if>>New</option>
                <option value="Processing" <c:if test="${captureRankJobSearchCriteria.exectionStatus.equals('Processing')}">selected="selected"</c:if>>Processing</option>
                <option value="Checking" <c:if test="${captureRankJobSearchCriteria.exectionStatus.equals('Checking')}">selected="selected"</c:if>>Checking</option>
                <option value="Complete" <c:if test="${captureRankJobSearchCriteria.exectionStatus.equals('Complete')}">selected="selected"</c:if>>Complete</option>
            </select>
            任务类型:
            <select name="jobType">
                <option value="Common" <c:if test="${captureRankJobSearchCriteria.jobType.equals('Common')}">selected="selected"</c:if>>普通任务</option>
                <option value="Specify" <c:if test="${captureRankJobSearchCriteria.jobType.equals('Specify')}">selected="selected"</c:if>>整站任务</option>
            </select>&nbsp;&nbsp;
            <shiro:hasPermission name="/internal/captureRank/searchCaptureRankJobs">
            <input type="submit" value=" 查询 " onclick="resetPageNumber()">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/captureRank/saveCaptureRankJob">
            <input type="button" value=" 添加 " onclick="addCaptureRankJobs()">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/captureRank/deleteCaptureRankJobs">
            <input type="button" value=" 删除所选 " onclick="deleteCaptureRankJobs()">&nbsp;&nbsp;
            </shiro:hasPermission>
            <input type="button" value=" 重置任务 " onclick="resetCaptureRankJobs()">&nbsp;&nbsp;
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>组名</td>
            <td align="center" width=40>客户</td>
            <td align="center" width=40>执行方式</td>
            <td align="center" width=40>执行时间</td>
            <td align="center" width=60>最后执行日期</td>
            <td align="center" width=50>状态</td>
            <td align="center" width=50>抓取记录数</td>
            <td align="center" width=60>采集天数间隔(天)</td>
            <td align="center" width=60>抓取间隔(ms)</td>
            <td align="center" width=40>换IP间隔</td>
            <td align="center" width=40>每页条数</td>
            <td align="center" width=90>抓取开始时间</td>
            <td align="center" width=90>抓取结束时间</td>
            <td align="center" width=60>修改人</td>
            <td align="center" width=90>修改时间</td>
            <td align="center" width=60>创建人</td>
            <td align="center" width=90>创建时间</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showCaptureRankJobTable">
        <c:forEach items="${page.records}" var="captureRankJob" varStatus="status">
        <tr align="left" onmouseover="doOver(this);" onmouseout="doOut(this);" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
            <td width=10 align="center"><input type="checkbox" name="uuid" value="${captureRankJob.uuid}" onclick="decideSelectAll()"/></td>
            <td width=80>${captureRankJob.groupNames}</td>
            <td width=40>${captureRankJob.contactPerson}</td>
            <td width=40>
                <c:choose>
                    <c:when test="${captureRankJob.exectionType == 'Everyday'}">每天</c:when>
                    <c:otherwise>一次性</c:otherwise>
                </c:choose>
            </td>
            <td width=40><fmt:formatDate value="${captureRankJob.exectionTime}" pattern="HH:mm"/></td>
            <td width=60>${captureRankJob.lastExecutionDate}</td>
            <td width=50>${captureRankJob.exectionStatus}<br><font color="red">${captureRankJob.captureRankJobStatus == false ? "暂停中" : ""}</font></td>
            <td width=50>${captureRankJob.rowNumber}</td>
            <td width=60>${captureRankJob.captureDaysInterval}</td>
            <td width=60>${captureRankJob.captureInterval}</td>
            <td width=40>${captureRankJob.executionCycle}</td>
            <td width=40>${captureRankJob.pageSize}</td>
            <td width=90><fmt:formatDate value="${captureRankJob.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td width=90><fmt:formatDate value="${captureRankJob.endTime}" pattern="yyyy-MM-dd HH:mm"/></td>


            <td width=60>${captureRankJob.updateBy}</td>
            <td width=90><fmt:formatDate value="${captureRankJob.updateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td width=60>${captureRankJob.createBy}</td>
            <td width=90><fmt:formatDate value="${captureRankJob.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td width=80>
                <shiro:hasPermission name="/internal/captureRank/saveCaptureRankJob">
                <a href="javascript:updateCaptureRankJobs('${captureRankJob.uuid}')">修改</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="/internal/captureRank/changeCaptureRankJobStatus">
                    <c:choose>
                        <c:when test="${captureRankJob.captureRankJobStatus}">
                            <a href="javascript:changeCaptureRankJobStatus('${captureRankJob.uuid}', 'false')">暂停</a>
                        </c:when>
                        <c:otherwise>
                            <a href="javascript:changeCaptureRankJobStatus('${captureRankJob.uuid}', 'true')">取消暂停</a>
                        </c:otherwise>
                    </c:choose>
                </shiro:hasPermission>
                <shiro:hasPermission name="/internal/captureRank/deleteCaptureRankJob">
                <a href="javascript:deleteCaptureRankJob('${captureRankJob.uuid}')">删除</a>
                </shiro:hasPermission>

            </td>
        </tr>
        </c:forEach>
    </table>
</div>
<div id="select2DialogDiv" title="" class="easyui-dialog" style="display: none;">
    <form id="crawlRankingForm">
        <ul id="formData">
            <input type="hidden" name="captureRankJobUuid" id="captureRankJobUuid">
            <li>
                <span>优化组名:</span>
                <select id="groups" name="groups" title="优化组名" style="width: 200px;"></select>
            </li>
            <li>
                <span>客户名:</span>
                <select id="customers" name="customers" title="客户名" style="width: 200px;"></select>
            </li>
            <li>
                <span>执行方式:</span>
                <input type="radio" name="exectionType" checked  value="Once">一次</label>
                <input type="radio" name="exectionType" value="Everyday">多次</li>
            <li id="start">
                <span>执行时间:</span>
                <input type="text" class="Wdate" id="exectionTime1" autocomplete="off" onfocus="WdatePicker({lang:'zh-cn',dateFmt:'HH:mm:ss'})" required style="width: 150px">
                <input id="nextExecuteTimeBtn" type="button" value="添加" onclick="nextExecuteTime()" style="float: right;margin-right: 6px"/>
            </li>
            <li id="end">
                <span>采集天数间隔:</span>
                <input id="captureDaysInterval" name="captureDaysInterval" class="easyui-numberspinner"  data-options="min:0,increment:1" required style="width: 200px">
            </li>
            <li>
                <span>抓取记录数:</span>
                <input id="rowNumber" name="rowNumber" class="easyui-numberspinner"  data-options="min:0,max:1000,increment:50" required style="width: 200px">
            </li>
            <li>
                <span>抓取间隔(毫秒):</span>
                <input id="captureInterval" name="captureInterval" class="easyui-numberspinner"  data-options="min:0,increment:500" required style="width: 200px">
            </li>
            <li>
                <span>每页条数:</span>
                <input id="pageSize" name="pageSize" class="easyui-numberspinner"  data-options="min:0,max:50,increment:10" required style="width: 200px">
            </li>
            <li>
                <span>换IP间隔:</span>
                <input id="executionCycle" name="executionCycle" class="easyui-numberspinner"  data-options="min:0,increment:50" required style="width: 200px">
            </li>
        </ul>
    </form>
</div>
<div id="showCustomerBottomPositioneDiv">
    <div id="showCustomerBottomDiv">
        <input id="fisrtButton" class="ui-button ui-widget ui-corner-all" type="button"
               onclick="changePaging(1,'${page.size}')" value="首页"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="upButton" type="button" class="ui-button ui-widget ui-corner-all"
               onclick="changePaging('${page.current-1}','${page.size}')" value="上一页"/>&nbsp;&nbsp;&nbsp;&nbsp;
        ${page.current}/${page.pages}&nbsp;&nbsp;
        <input id="nextButton" type="button" class="ui-button ui-widget ui-corner-all"
               onclick="changePaging('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')"
               value="下一页">&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="lastButton" type="button" class="ui-button ui-widget ui-corner-all"
               onclick="changePaging('${page.pages}','${page.size}')" value="末页">&nbsp;&nbsp;&nbsp;&nbsp;
        总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;
        每页显示条数:<select id="chooseRecords" onchange="changePaging(${page.current},this.value)">
        <option>10</option>
        <option>25</option>
        <option>50</option>
        <option>75</option>
        <option>100</option>
        <option>500</option>
        <option>1000</option>
    </select>
    </div>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script type="text/javascript" src="${staticPath}/static/select2/select2-4.0.6.min.js"></script>
<script src="${staticPath}/captureRank/captureRank.js"></script>
</body>
</html>