<%@ include file="/commons/global.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
    body {
        font-size: 12px;
        text-align:center;
    }
    #showMainKeywordTopDiv {
        position: fixed;
        top: 0px;
        left: 0px;
        background-color: white;
        width: 100%;
    }
    #showAddMainKeywordDialog {
        margin:0 auto;
    }
    #serachMainKeyword{
        width: 100%;
    }
    #serachMainKeywordForm{
        text-align: left;
    }
    #showMainKeywordBottomPositioneDiv{
        position: fixed;
        bottom: 0px;
        right: 0px;
        background-color:#ADD1FF;
        padding-top: 5px;
        padding-bottom: 5px;
        width: 100%;
    }
    #showMainKeywordBottomDiv {
        float: right;
        margin-right: 10px;
    }

    #nav .mainLevel ul {display:none; position:absolute;z-index: 10;}
    #nav .mainLevel li {border-top:1px solid #fff; background:#ffe60c; width:140px;z-index: 10;/*IE6 only*/}
</style>
    <title>投诉专用平台</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="showMainKeywordTopDiv">
    <%@include file="/menu.jsp" %>
    <div id="serachMainKeyword" style="margin-top: 35px;">
        <form id="serachMainKeywordForm" action="/internal/complaints/findTSMainKeywords" method="post">
            主关键词&nbsp;&nbsp;<input id="itemKeyword" name="itemKeyword" type="text"
                                   value="${page.condition.get("keyword")}"/>&nbsp;&nbsp;
            <input id="itemGroupHidden" type="hidden" value="${page.condition.get("group")}"/>
            区域分组&nbsp;&nbsp;<select id="itemGroup" name="itemGroup" style="height: 21px;">
            <option value="">请 选 择 城 市</option>
            <option value="北京">北京</option>
            <option value="上海">上海</option>
            <option value="广州">广州</option>
            <option value="深圳">深圳</option>
        </select>
            &nbsp;&nbsp;
            <input type="hidden" id="currentPageHidden" name="currentPageHidden" value="${page.current}"/>
            <input type="hidden" id="displaysRecordsHidden" name="displaysRecordsHidden" value="${page.size}"/>
            <shiro:hasPermission name="/internal/complaints/findTSMainKeywords">
            <input type="submit" class="ui-button ui-widget ui-corner-all" style="z-index: 0" ; value=" 查询 " onclick="resetPageNumber()">&nbsp;&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/complaints/save">
            <input type="button" class="ui-button ui-widget ui-corner-all" style="z-index: 0" ; onclick="showAddMainKeywordDialog(null)" value=" 添加 "/>&nbsp;&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/complaints/deleteTSMainKeywords">
            <input type="button" class="ui-button ui-widget ui-corner-all" style="z-index: 0" ; onclick="deleteMainKeywords(this)" value=" 删除所选 "/>
            </shiro:hasPermission>
        </form>
        <table id="headerTable" style="width:100%;">
            <tr bgcolor="#eeeeee" height=30>
                <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
                <td align="center" width=150>主词</td>
                <td align="center" width=60>分组</td>
                <td align="center" width=400>负面语句</td>
                <td align="center" width=100>更新时间</td>
                <td align="center" width=100>创建时间</td>
                <td align="center" width=80>操作</td>
            </tr>
        </table>
    </div>
</div>
<div id="showMainKeywordTableDiv" style="margin-bottom: 30px">
        <table id="showMainKeywordTable">
            <c:forEach items="${page.records }" var="mainkey" varStatus="status">
                <tr onmouseover="doOver(this)" onmouseout="doOut(this)" ondblclick="getMainKeyword('${mainkey.uuid}')" height="30" <c:if test="${status.index%2==0}">bgcolor="#eeeeee" </c:if> >
                    <td width=10 align="center"><input type="checkbox" name="uuid" value="${mainkey.uuid}" onclick="decideSelectAll()"/></td>
                    <input type="hidden" id="mkUuid" value="${mainkey.uuid}"/>
                    <td width=150>${mainkey.keyword }</td>
                    <td width=60>${mainkey.group }</td>
                    <td width=400>
                        <c:forEach items="${mainkey.tsNegativeKeywords}" var="ngkey">
                            <input type="hidden" id="ngkeyUuid" value="${ngkey.uuid}"/>
                            <c:choose>
                                <c:when test="${ngkey.pcAppeared ==1 || ngkey.phoneAppeared ==1}">
                                        <span style="color: crimson" >${ngkey.keyword}</span>&nbsp;&nbsp;&nbsp;
                                </c:when>
                                <c:otherwise>
                                    ${ngkey.keyword}&nbsp;&nbsp;&nbsp;
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </td>
                    <td width=100>
                        <c:choose>
                            <c:when test="${mainkey.updateTime==null}">
                                ${mainkey.updateTime}
                            </c:when>
                            <c:otherwise>
                                <fmt:formatDate value="${mainkey.updateTime}" pattern="yy-MM-dd HH:mm"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td width=100><fmt:formatDate value="${mainkey.createTime}" pattern="yy-MM-dd HH:mm"/></td>
                    <td>
                    <shiro:hasPermission name="/internal/complaints/save">
                        &nbsp;&nbsp;&nbsp;<a href="javascript:getMainKeyword('${mainkey.uuid}')">修改</a>&nbsp;&nbsp;&nbsp;
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/complaints/delete">
                        <a href="javascript:deleteMainKeyword('${mainkey.uuid}')">删除</a>
                    </shiro:hasPermission>
                    </td>
                </tr>
            </c:forEach>
        </table>
</div>
<div id="showMainKeywordBottomPositioneDiv">
    <div id="showMainKeywordBottomDiv">
        <input id="fisrtButton" class="ui-button ui-widget ui-corner-all" type="button"  onclick="serachMainKeywords(1,'${page.size}')" value="首页"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="upButton" type="button" class="ui-button ui-widget ui-corner-all" onclick="serachMainKeywords('${page.current-1}','${page.size}')" value="上一页"/>&nbsp;&nbsp;&nbsp;&nbsp;
        ${page.current}/${page.pages}&nbsp;&nbsp;
        <input id="nextButton"  type="button" class="ui-button ui-widget ui-corner-all"  onclick="serachMainKeywords('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')" value="下一页">&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="lastButton" type="button" class="ui-button ui-widget ui-corner-all" onclick="serachMainKeywords('${page.pages}','${page.size}')" value="末页">&nbsp;&nbsp;&nbsp;&nbsp;
        总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;
        每页显示条数:<select id="chooseRecords"  onchange="chooseRecords(${page.current},this.value)">
        <option>10</option>
        <option>25</option>
        <option>50</option>
        <option>75</option>
        <option>100</option>
    </select>
        <input type="hidden" id="currentPageHidden" value="${page.current}"/>
        <input type="hidden" id="displaysRecordsHidden" value="${page.size}"/>
        <input type="hidden" id="pagesHidden" value="${page.pages}"/>
    </div>
</div>
<div id="showAddMainKeywordDialog" class="easyui-dialog" title="添加投诉关键字" style="display: none;left: 35%;">
    <form id="mainKeywordForm" action="show.jsp">
        <table style="border-spacing:15px;">
            <tr>
                <input id="mUuid" type="hidden">
                <td>主关键字</td>
                <td><input id="mKeyword" type="text" style="width: 250px;"></td>
            </tr>
            <tr>
                <td>地区分组</td>
                <td>
                    <select id="mGroup" style="width: 250px;">
                        <option value="">-----------请 选 择 城 市-----------</option>
                        <option value="北京">北京</option>
                        <option value="上海">上海</option>
                        <option value="广州">广州</option>
                        <option value="深圳">深圳</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>负面词</td>
                <td>
                    <textarea id="ngKeyword" style="resize: none;height: 180px;width: 250px" placeholder="请用逗号作为分割符"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/complaints/show.js"></script>
</body>
</html>