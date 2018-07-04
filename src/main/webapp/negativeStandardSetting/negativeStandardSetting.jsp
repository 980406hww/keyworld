
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>负面达标设置</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 35px">
        <form method="post" id="searchNegativeStandardSetting" action="/internal/negativeStandardSetting/searchNegativeStandardSetting" style="margin-bottom:0px">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            <input type="hidden" name="customerUuid" id="customerUuid" value="${negativeStandardSettingCriteria.customerUuid}">
            关键字<input type="text" name="keyword" list="keyword_list"  id="keyword" value="${negativeStandardSettingCriteria.keyword}">
            <datalist id="keyword_list">
                <c:forEach items="${setCustomerKeywords}" var="keyword">
                    <option>${keyword}</option>
                </c:forEach>
            </datalist>
            <input type="hidden" name="searchEngineHidden" id="searchEngineHidden" value="${negativeStandardSettingCriteria.searchEngine}">
            搜索引擎
            <select  type="text" name="searchEngine" class="select" id="searchEngine" >
                    <option value="">全部搜索引擎</option>
                    <option value="搜狗手机">搜狗手机</option>
                    <option value="搜狗电脑">搜狗电脑</option>
                    <option value="百度手机">百度手机</option>
                    <option value="百度电脑">百度电脑</option>
                    <option value="360手机">360手机</option>
                    <option value="360电脑">360电脑</option>
                    <option value="神马">神马</option>
             </select>
            <input type="hidden" name="reachStandardHidden" id="reachStandardHidden" value="${negativeStandardSettingCriteria.reachStandard}">
            是否达标
            <select  type="text" name="reachStandard" class="select" id="reachStandard" >
                <option value ='' selected>全部</option>
                <option value =0>否</option>
                <option value =1>是</option>
            </select>
            <shiro:hasPermission name="/internal/negativeStandardSetting/searchNegativeStandardSetting">
                <input type="submit" value="查询" onclick="submitPageNumber()"/>&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/negativeStandardSetting/addNegativeStandardSetting">
                <input type="button" value="添加" onclick="addNegativeStandardSetting(${negativeStandardSettingCriteria.customerUuid})" >
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/negativeStandardSetting/deleteNegativeStandardSettings">
                <input type="button" value="删除所选" onclick="deleteNegativeStandardSettings()">
            </shiro:hasPermission>
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=150>关键字</td>
            <td align="center" width=60>搜索引擎</td>
            <td align="center" width=40>首页负面数量</td>
            <td align="center" width=40>前两页负面数量</td>
            <td align="center" width=40>前三页负面数量</td>
            <td align="center" width=40>前四页负面数量</td>
            <td align="center" width=40>前五页负面数量</td>
            <td align="center" width=40>是否达标</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px;">
    <table style="font-size:12px; width: 100%; table-layout:fixed; " id="showNegativeRankTable">
        <c:forEach items="${page.records}" var="negativeStandardSetting" varStatus="status">
            <tr id="${negativeStandardSetting.uuid}" onmouseover="doOver(this);" onmouseout="doOut(this);"  height=30 <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
                <td align="center" width=12 ><input type="checkbox" name="uuid" value="${negativeStandardSetting.uuid}"/></td>
                <td name="keyword" width=150>${negativeStandardSetting.keyword}</td>
                <td align="center" name="searchEngine" width=60>${negativeStandardSetting.searchEngine}</td>
                <td align="center" name="topOnePageNegativeCount" width=40>${negativeStandardSetting.topOnePageNegativeCount}</td>
                <td align="center" name="topTwoPageNegativeCount" width=40>${negativeStandardSetting.topTwoPageNegativeCount}</td>
                <td align="center" name="topThreePageNegativeCount" width=40>${negativeStandardSetting.topThreePageNegativeCount}</td>
                <td align="center" name="topFourPageNegativeCount" width=40>${negativeStandardSetting.topFourPageNegativeCount}</td>
                <td align="center" name="topFivePageNegativeCount" width=40>${negativeStandardSetting.topFivePageNegativeCount}</td>
                <td align="center" width=40 name="reachStandard">
                    <c:if test="${negativeStandardSetting.reachStandard == false}">否</c:if>
                    <c:if test="${negativeStandardSetting.reachStandard == true}">是</c:if>
                </td>
                <td align="center" width=80>
                    <shiro:hasPermission name="/internal/negativeStandardSetting/updateNegativeStandardSetting">
                    <a href="javascript:updateNegativeStandardSetting('${negativeStandardSetting.uuid}','${negativeStandardSetting.reachStandard}')">修改</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/negativeStandardSetting/deleteNegativeStandardSetting">
                    |<a href="javascript:deleteNegativeStandardSetting(${negativeStandardSetting.uuid})">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
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

<div id="showNegativeStandardSettingDiv" style="display: none">
    <table>
        <tr><td>关键字</td><td>
            <input type="text" id="keywordDiv" list="keywordDiv_list"  placeholder="请选择关键字" style="width:97%;margin: 5px" />
            <datalist id="keywordDiv_list">
                <c:forEach items="${setCustomerKeywords}" var="keyword">
                    <option>${keyword}</option>
                </c:forEach>
            </datalist>
        </td></tr>
        <tr><td>搜索引擎</td><td>
            <select  type="text" name="searchEngineDiv" class="select" id="searchEngineDiv" >
                <option value="搜狗手机">搜狗手机</option>
                <option value="搜狗电脑">搜狗电脑</option>
                <option value="百度手机">百度手机</option>
                <option value="百度电脑">百度电脑</option>
                <option value="360手机">360手机</option>
                <option value="360电脑">360电脑</option>
                <option value="神马">神马</option>
            </select>
        </td></tr>
        <tr><td>首页负面数量</td><td><input id="topOnePageNegativeCountDiv" class="easyui-numberspinner"   style="width: 180px;" type="text"></td></tr>
        <tr><td>前两页负面数量</td><td><input id="topTwoPageNegativeCountDiv" class="easyui-numberspinner"   style="width: 180px;" type="text"></td></tr>
        <tr><td>前三页负面数量</td><td><input id="topThreePageNegativeCountDiv" class="easyui-numberspinner"   style="width: 180px;" type="text"></td></tr>
        <tr><td>前四页负面数量</td><td><input id="topFourPageNegativeCountDiv" class="easyui-numberspinner" style="width: 180px;" type="text"></td></tr>
        <tr><td>前五页负面数量</td><td><input id="topFivePageNegativeCountDiv" class="easyui-numberspinner"  style="width: 180px;" type="text"></td></tr>
    </table>
</div>
<%@ include file="/commons/loadjs.jsp"%>
<script src="${staticPath }/negativeStandardSetting/negativeStandardSetting.js"></script>
</body>
</html>
