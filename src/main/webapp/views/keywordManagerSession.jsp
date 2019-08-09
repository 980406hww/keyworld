<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style type="text/css">
        #showTableDiv {
            width: 100%;
            margin: auto;
        }
        #showIndustryTable tr:nth-child(odd){background:#EEEEEE;}

        #showIndustryTable td{
            text-align: left;
        }
        h6{ margin: 0 5px;}
    </style>
    <title>keyword_manager session监控</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <table width="100%" style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=13>
                <form method="post" id="searchIndustryForm" action="/internal/industry/searchIndustries" style="margin-bottom:0px ">
                    <div>
                        行业名称:<input type="text" name="industryName" id="industryName"
                                   value="${industryCriteria.industryName}"
                                   style="width:160px;">
                        引擎:
                        <select name="searchEngine" id="searchEngine" style="width:100px">
                            <option value="">全部</option>
                            <c:forEach items="${searchEngineMap}" var="entry">
                                <option value="${entry.value}">${entry.key}</option>
                            </c:forEach>
                        </select>
                        <c:if test="${isDepartmentManager}">
                            用户名称:
                            <select name="loginName" id="loginName">
                                <option value="">所有</option>
                                <option value="${user.loginName}">只显示自己</option>
                                <c:forEach items="${activeUsers}" var="activeUser">
                                    <option value="${activeUser.loginName}">${activeUser.userName}</option>
                                </c:forEach>
                            </select>
                        </c:if>
                        爬取状态:
                        <select name="status" id="status">
                            <option value="" <c:if test="${industryCriteria.status == null}">selected</c:if>>全部</option>
                            <option value="2" <c:if test="${industryCriteria.status == 2}">selected</c:if>>爬取完成</option>
                            <option value="1" <c:if test="${industryCriteria.status == 1}">selected</c:if>>爬取中</option>
                            <option value="0" <c:if test="${industryCriteria.status == 0}">selected</c:if>>未爬取</option>
                        </select>
                        <input type="hidden" name="terminalType" id="terminalType" value="${industryCriteria.terminalType}">
                        <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                        <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                        <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                        <shiro:hasPermission name="/internal/industry/searchIndustries">
                            &nbsp;&nbsp;<input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/internal/industry/saveIndustry">
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showIndustryDialog(null,'${user.loginName}')"/>
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 马上爬取 " onclick="updateIndustryStatus()"/>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/internal/industry/deleteIndustries">
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteIndustries()"/>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/internal/industry/updateIndustryUserID">
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 修改客户归属 " onclick="updateIndustryUserID()"/>
                        </shiro:hasPermission>
                    </div>
                </form>
            </td>
        </tr>
    </table>

    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=100>SessionID</td>
            <td align="center" width=100>属性名</td>
            <td align="center" width=200>内容</td>
            <td align="center" width=80>状态</td>
            <td align="center" width=60>创建时间</td>
            <td align="center" width=60>更新时间</td>
            <td align="center" width=100>操作</td>
        </tr>
    </table>
</div>
<div id="showTableDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showIndustryTable">
        <c:forEach items="${page.records}" var="keywordManagerSession">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width=10 style="padding-left: 7px;">
                    <input type="checkbox" name="keywordManagerSessionUuid" value="${keywordManagerSession.uuid}"/>
                </td>
                <td width=100 style="text-align: center">${keywordManagerSession.sessionID}</td>
                <td width=100 style="text-align: center">${keywordManagerSession.attributeName}</td>
                <td width=200 style="text-align: center">${keywordManagerSession.content}</td>
                <td width=80 style="text-align: center">${keywordManagerSession.status}</td>
                <%--<td width=60 style="text-align: center" name="status">
                    <c:choose>
                        <c:when test="${keywordManagerSession.status == 2}">
                            <span style="color: forestgreen;">爬取完成</span>
                        </c:when>
                        <c:when test="${keywordManagerSession.status == 1}">
                            <span style="color: darkorange;">爬取中</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: red;">未爬取</span>
                        </c:otherwise>
                    </c:choose>
                </td>--%>
                <td width=60 style="text-align: center"><fmt:formatDate value="${keywordManagerSession.createTime}" pattern="yyyy-MM-dd"/></td>
                <td width=60 style="text-align: center"><fmt:formatDate value="${keywordManagerSession.updateTime}" pattern="yyyy-MM-dd"/></td>
                <td width=100>
                    <shiro:hasPermission name="/internal/industry/saveIndustry">
                        <a href="javascript:modifyIndustry(${keywordManagerSession.uuid})">修改</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/industry/delIndustry">
                        | <a href="javascript:delIndustry('${keywordManagerSession.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<%-- 底部分页 --%>
<div id="showCustomerBottomPositioneDiv">
    <div id="showCustomerBottomDiv">
        <input id="firstButton" class="ui-button ui-widget ui-corner-all" type="button"
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
<script type="text/javascript">
    $(function () {
        window.onresize = function(){
            $("#showTableDiv").css("margin-top",$("#topDiv").height());
        };
    });

</script>
</body>
</html>