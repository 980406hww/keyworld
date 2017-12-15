<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>网站管理</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 35px">
        <form method="post" id="searchWebsiteForm" action="/internal/website/searchWebsites" style="margin-bottom:0px ">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}" />
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            网站名称:<input type="text" name="websiteName" id="websiteName" value="${websiteCriteria.websiteName}">&nbsp;&nbsp;
            域名:<input type="text" name="domain" id="domain" value="${websiteCriteria.domain}">&nbsp;&nbsp;
            失败次数:<input type="text" name="accessFailCount" id="accessFailCount" value="${websiteCriteria.accessFailCount}">&nbsp;&nbsp;
            <shiro:hasPermission name="/internal/website/searchWebsites">
            <input type="submit" value=" 查询 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/website/saveWebsite">
            <input type="button" value=" 添加 " onclick="showWebsiteDialog(null)">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/website/deleteWebsites">
            <input type="button" onclick="deleteWebsites()" value=" 删除所选 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/website/resetAccessFailCount">
            <input type="button" value=" 重置失败次数 " onclick="resetAccessFailCount(null)">
            </shiro:hasPermission>
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width="10">
                <input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/>
            </td>
            <td align="center" width=30>编号</td>
            <td align="center" width=80>网站名称</td>
            <td align="center" width=100>网站域名</td>
            <td align="center" width=60>所属行业</td>
            <td align="center" width=40>访问失败次数</td>
            <td align="center" width=50>访问故障时间</td>
            <td align="center" width=50>最近访问时间</td>
            <td align="center" width=50>更新时间</td>
            <td align="center" width=50>操作</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px;">
    <table style="font-size:12px; width: 100%;" id="showWebsiteTable">
        <c:forEach items="${page.records}" var="website" varStatus="status">
        <tr align="left" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
            <td width=10 align="center"><input type="checkbox" name="uuid" value="${website.uuid}" onclick="decideSelectAll()"/></td>
            <td width=30>${website.uuid}</td>
            <td width=80>${website.websiteName}</td>
            <td width=100><a target="_blank" href="http://${website.domain}">${website.domain}</a></td>
            <td width=60>${website.industry}</td>
            <td width=40>${website.accessFailCount}</td>
            <td width=50><fmt:formatDate value="${website.accessFailTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td width=50><fmt:formatDate value="${website.lastAccessTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td width=50><fmt:formatDate value="${website.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td style="text-align: center;" width="50">
                <shiro:hasPermission name="/internal/website/saveWebsite">
                <a href="javascript:editWebsiteInfo('${website.uuid}')">修改</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="/internal/website/deleteWebsite">
                | <a href="javascript:deleteWebsite('${website.uuid}')">删除</a>
                </shiro:hasPermission>
            </td>
        </tr>
        </c:forEach>
    </table>
</div>
<div id="uploadTxtFileDialog" title="" class="easyui-dialog" style="left: 35%;">
    <form method="post" id="uploadTxtFileForm" action="" enctype="multipart/form-data">
        <table width="100%" style="font-size:14px;">
            <tr><td>
                <input type="file" id="file" name="file" size=50 height="50px" style="width: 180px;">
            </td></tr>
        </table>
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
    </select>
    </div>
</div>
<div id="websiteDialog" title="网站信息" class="easyui-dialog" style="left: 35%;">
    <form id="websiteForm" method="post">
        <table style="font-size:14px;" cellpadding=10 cellspacing="5">
            <tr>
                <td align="right">网站名称:</td>
                <td><input type="text" name="websiteName" id="websiteName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">网站域名:</td>
                <td><input type="text" name="domain" id="domain" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">所属行业</td>
                <td><input type="text" name="industry" id="industry" style="width:200px;">
                </td>
            </tr>
            <tr>
                <td align="right">访问失败次数</td>
                <td><input type="text" name="accessFailCount" id="accessFailCount" style="width:200px;">
                </td>
            </tr>
        </table>
    </form>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/website/website.js"></script>
</body>
</html>