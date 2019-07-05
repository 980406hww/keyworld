<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style type="text/css">
        #showIndustryTableDiv {
            width: 100%;
            margin: auto;
        }
        #showIndustryTable tr:nth-child(odd){background:#EEEEEE;}

        #showIndustryTable td{
            text-align: left;
        }
        h6{ margin: 0 5px;}
    </style>
    <title>行业列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=13>
                <form method="post" id="searchIndustryForm" action="/internal/industry/searchIndustries" style="margin-bottom:0px ">
                    <div>
                        行业名称:<input type="text" name="industryName" id="industryName"
                                   value="${industryCriteria.industryName}"
                                   style="width:160px;">
                        引擎:
                        <select name="searchEngine" id="searchEngine" style="width:240px">
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
                        <%--<shiro:hasPermission name="/internal/industry/searchIndustries">--%>
                            &nbsp;&nbsp;<input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">
                        <%--</shiro:hasPermission>--%>
                        <%--<shiro:hasPermission name="/internal/industry/saveIndustry">--%>
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showIndustryDialog(null,'${user.loginName}')"/>
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 修改客户归属 " onclick="updateIndustryUserID()"/>
                        <%--</shiro:hasPermission>--%>
                        <%--<shiro:hasPermission name="/internal/industry/deleteIndustries">--%>
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteIndustries()"/>
                        <%--</shiro:hasPermission>--%>
                    </div>
                </form>
            </td>
        </tr>
    </table>

    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>用户名称</td>
            <td align="center" width=80>行业名称</td>
            <td align="center" width=60>搜索引擎</td>
            <td align="center" width=140>爬取页数</td>
            <td align="center" width=140>每页条数</td>
            <td align="center" width=40>状态</td>
            <td align="center" width=50>创建时间</td>
            <td align="center" width=200>操作</td>
        </tr>
    </table>
</div>
<div id="showIndustryTableDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showIndustryTable">
        <c:forEach items="${page.records}" var="industryInfo">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width=10 style="padding-left: 7px;">
                    <input type="checkbox" name="industryUuid" value="${industryInfo.uuid}"/>
                </td>
                <td width=80>${industryInfo.userID}</td>
                <td width=80>
                    <a href="#" onclick="searchIndustryDetails('/internal/industryDetails/searchIndustryDetails/${industryInfo.uuid}')">${industryInfo.industryName}</a>
                </td>
                <td width=60>${industryInfo.searchEngine}</td>
                <td width=140>${industryInfo.pageNum}</td>
                <td width=140>${industryInfo.pagePerNum}</td>
                <td width=40 style="text-align: center">
                    <c:choose>
                        <c:when test="${industryInfo.status == 2}">
                            <span style="color: forestgreen;">爬取完成</span>
                        </c:when>
                        <c:when test="${industryInfo.status == 1}">
                            <span style="color: darkorange;">爬取中</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: red;">未爬取</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width=50 style="text-align: center"><fmt:formatDate value="${industryInfo.createTime}" pattern="yyyy-MM-dd"/></td>
                <td width=200>
                    <%--<shiro:hasPermission name="/internal/industry/saveIndustry">--%>
                        <a href="javascript:modifyIndustry(${industryInfo.uuid})">修改</a>
                    <%--</shiro:hasPermission>--%>
                    <%--<shiro:hasPermission name="/internal/industry/delIndustry">--%>
                        | <a href="javascript:delIndustry('${industryInfo.uuid}')">删除</a>
                    <%--</shiro:hasPermission>--%>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

</div>
<%-- 新增或修改行业信息 --%>
<div id="industryDialog" title="行业信息" class="easyui-dialog" style="display:none;left: 40%;">
    <form id="industryForm" method="post" action="industrylist.jsp">
        <table style="font-size:14px;" cellpadding=5>
            <tr>
                <td align="right" width="60">行业名称: </td>
                <td><input type="text" name="industryName" id="industryName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right" width="60">搜索引擎: </td>
                <td>
                    <select name="searchEngine" id="searchEngine" style="width:200px">
                        <c:forEach items="${searchEngineMap}" var="entry">
                            <option value="${entry.value}">${entry.key}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right" width="60">爬取页数: </td>
                <td><input type="text" name="pageNum" id="pageNum" style="width:200px;" value="2"></td>
            </tr>
            <tr>
                <td align="right" width="60">每页条数: </td>
                <td><input type="text" name="pagePerNum" id="pagePerNum" style="width:200px;" value="10"></td>
            </tr>
            <tr>
                <td align="right" width="60">爬取状态: </td>
                <td>
                    <select name="status" id="status" style="width: 200px;">
                        <option value="0">未爬取</option>
                        <option value="1">爬取中</option>
                        <option value="2">爬取完成</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<%-- 修改行业归属 --%>
<div id="updateIndustryUserIDDialog" title="修改行业归属" class="easyui-dialog" style="left: 30%;display: none;">
    请选择行业归属:
    <select id="userID">
        <c:forEach items="${activeUsers}" var="activeUser">
            <option value="${activeUser.loginName}">${activeUser.userName}</option>
        </c:forEach>
    </select>
</div>
<%-- 底部分页 --%>
<div id="showCustomerBottomPositionDiv">
    <div id="showIndustryBottomDiv">
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
<script src="${staticPath }/industry/industrylist.js"></script>
<script type="text/javascript">
    $(function () {
        window.onresize = function(){
            $("#showIndustryTableDiv").css("margin-top",$("#topDiv").height());
        }
        if(${isDepartmentManager}) {
            $("#loginName").val("${industryCriteria.loginName}");
        }
    });

    <%--<shiro:hasPermission name="/internal/industryDetails/searchIndustryDetails">--%>
    function searchIndustryDetails(url) {
        window.open(url);
    }
    <%--</shiro:hasPermission>--%>
</script>
</body>
</html>