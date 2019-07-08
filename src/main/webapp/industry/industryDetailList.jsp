<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style type="text/css">
        #showIndustryDetailTableDiv {
            width: 100%;
            margin: auto;
        }
        #showIndustryDetailTable tr:nth-child(odd){background:#EEEEEE;}

        #showIndustryDetailTable td{
            text-align: left;
        }
        h6{ margin: 0 5px;}
    </style>
    <title>网站联系信息列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=13>
                <form method="post" id="searchIndustryDetailForm" action="/internal/industryDetail/searchIndustryDetails" style="margin-bottom:0px ">
                    <div>
                        网站域名:<input type="text" name="website" id="website" value="${industryDetailCriteria.website}" style="width:160px;">
                        权重:
                        <select name="weight" id="weight" style="width:100px">
                            <option value=""></option>
                            <c:forEach items="${weightList}" var="weight">
                                <c:choose>
                                    <c:when test="${weight eq industryDetailCriteria.weight}"><option selected>${weight}</option></c:when>
                                    <c:otherwise><option>${weight}</option></c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                        备注:<input type="text" name="remark" id="remark" value="${industryDetailCriteria.remark}" style="width:160px;">
                        <input type="hidden" name="industryID" id="industryID" value="${industryDetailCriteria.industryID}"/>
                        <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                        <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                        <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                        <%--<shiro:hasPermission name="/internal/industryDetail/searchIndustryDetails">--%>
                            &nbsp;&nbsp;<input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">
                        <%--</shiro:hasPermission>--%>
                        <%--<shiro:hasPermission name="/internal/industryDetail/saveIndustryDetail">--%>
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showIndustryDetailDialog(null)"/>
                        <%--</shiro:hasPermission>--%>
                        <%--<shiro:hasPermission name="/internal/industryDetail/deleteIndustryDetails">--%>
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteIndustryDetails()"/>
                        <%--</shiro:hasPermission>--%>
                    </div>
                </form>
            </td>
        </tr>
    </table>

    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>网站名称</td>
            <td align="center" width=80>QQ</td>
            <td align="center" width=60>电话</td>
            <td align="center" width=140>权重</td>
            <td align="center" width=140>备注</td>
            <td align="center" width=40>层级</td>
            <td align="center" width=50>修改时间</td>
            <td align="center" width=50>创建时间</td>
            <td align="center" width=200>操作</td>
        </tr>
    </table>
</div>
<div id="showIndustryDetailTableDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showIndustryDetailTable">
        <c:forEach items="${page.records}" var="industryDetail">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width=10 style="padding-left: 7px;">
                    <input type="checkbox" name="industryDetailUuid" value="${industryDetail.uuid}"/>
                </td>
                <td width=80>${industryDetail.website}</td>
                <td width=80>${industryDetail.qq}</td>
                <td width=60>${industryDetail.telephone}</td>
                <td width=140>${industryDetail.weight}</td>
                <td width=140>
                    <input type="hidden" name="hiddenRemark" value="${industryDetail.remark}">
                    <input type="text" value="${industryDetail.remark}" onblur="updateIndustryDetailRemark(this)">
                </td>
                <td width=40>${industryDetail.level}</td>
                <td width=50 style="text-align: center"><fmt:formatDate value="${industryDetail.updateTime}" pattern="yyyy-MM-dd"/></td>
                <td width=50 style="text-align: center"><fmt:formatDate value="${industryDetail.createTime}" pattern="yyyy-MM-dd"/></td>
                <td width=200>
                    <%--<shiro:hasPermission name="/internal/industryDetail/saveIndustryDetail">--%>
                        <a href="javascript:modifyIndustryDetail(${industryDetail.uuid})">修改</a>
                    <%--</shiro:hasPermission>--%>
                    <%--<shiro:hasPermission name="/internal/industryDetail/delIndustryDetail">--%>
                        | <a href="javascript:delIndustryDetail('${industryDetail.uuid}')">删除</a>
                    <%--</shiro:hasPermission>--%>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

</div>
<%-- 新增或修改网站联系信息 --%>
<div id="industryDetailDialog" title="网站联系信息" class="easyui-dialog" style="display:none;left: 40%;">
    <form id="industryDetailForm" method="post" action="industryDetailList.jsp">
        <table style="font-size:14px;" cellpadding=5>
            <tr>
                <td align="right" width="60">网站域名: </td>
                <td><input type="text" name="website" id="website" placeholder="请填写网站域名" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right" width="60">QQ: </td>
                <td><input type="text" name="qq" id="qq" placeholder="请填写QQ" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right" width="60">电话: </td>
                <td><input type="text" name="telephone" placeholder="请填写电话" id="telephone" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right" width="60">权重: </td>
                <td>
                    <select name="weight" id="weight" style="width:200px">
                        <c:forEach items="${weightList}" var="weight">
                            <c:choose>
                                <c:when test="${weight eq industryDetailCriteria.weight}"><option selected>${weight}</option></c:when>
                                <c:otherwise><option>${weight}</option></c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right" width="60">备注: </td>
                <td><input type="text" name="remark" id="remark" placeholder="请填写备注" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right" width="60">层级: </td>
                <td><input type="text" name="level" id="level" style="width:200px;" value="1"></td>
            </tr>
            </tr>
        </table>
    </form>
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
<script src="${staticPath }/industry/industryDetailList.js"></script>
<script type="text/javascript">
    $(function () {
        window.onresize = function(){
            $("#showIndustryDetailTableDiv").css("margin-top",$("#topDiv").height());
        }
    });
</script>
</body>
</html>