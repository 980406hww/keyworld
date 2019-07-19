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
                        <shiro:hasPermission name="/internal/industryDetail/searchIndustryDetails">
                            &nbsp;&nbsp;<input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/internal/industryDetail/saveIndustryDetail">
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showIndustryDetailDialog(null)"/>
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" title="移除没有联系方式(tel, qq)的数据" value=" 移除无用数据 " onclick="removeUselessIndustryDetail(${industryDetailCriteria.industryID})"/>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/internal/industryDetail/deleteIndustryDetails">
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteIndustryDetails()"/>
                        </shiro:hasPermission>
                    </div>
                </form>
            </td>
        </tr>
    </table>

    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=150>网站URL</td>
            <td align="center" width=200>标题</td>
            <td align="center" width=100>QQ</td>
            <td align="center" width=120>电话</td>
            <td align="center" width=40>权重</td>
            <td align="center" width=150>备注</td>
            <td align="center" width=40>层级</td>
            <td align="center" width=60>修改时间</td>
            <td align="center" width=60>创建时间</td>
            <td align="center" width=100>操作</td>
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
                <td width=150>
                    <a href="${industryDetail.website}" target="_blank">${industryDetail.website}</a>
                </td>
                <td width=200>${industryDetail.title}</td>
                <td width=100 valign="top">${industryDetail.qq}</td>
                <td width=120 valign="top">${industryDetail.telephone}</td>
                <td width=40 style="text-align: center">${industryDetail.weight}</td>
                <td width=150>
                    <input type="hidden" name="hiddenRemark" value="${industryDetail.remark}">
                    <input type="text" style="width: 100%;" value="${industryDetail.remark}" onblur="updateIndustryDetailRemark(this)">
                </td>
                <td width=40 style="text-align: center">${industryDetail.level}</td>
                <td width=60 style="text-align: center"><fmt:formatDate value="${industryDetail.updateTime}" pattern="yyyy-MM-dd"/></td>
                <td width=60 style="text-align: center"><fmt:formatDate value="${industryDetail.createTime}" pattern="yyyy-MM-dd"/></td>
                <td width=100>
                    <shiro:hasPermission name="/internal/industryDetail/saveIndustryDetail">
                        <a href="javascript:modifyIndustryDetail(${industryDetail.uuid})">修改</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/industryDetail/delIndustryDetail">
                        | <a href="javascript:delIndustryDetail('${industryDetail.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

</div>
<%-- 新增或修改网站联系信息 --%>
<div id="industryDetailDialog" title="网站联系信息" class="easyui-dialog" style="display:none;left: 40%;">
    <form id="industryDetailForm" method="post" action="industryDetailList.jsp">
        <table style="font-size:12px;" align="center" cellspacing="8" cellpadding=5>
            <tr>
                <td align="right" width="60">网站域名: </td>
                <td><input type="text" name="website" id="website" placeholder="请填写网站域名" style="width:180px;"></td>
            </tr>
            <tr>
                <td align="right" width="60" valign="top">联系QQ: </td>
                <td>
                    <textarea name="qq" id="qq" placeholder="请填写QQ, 多个请换行" style="width:180px; height: 80px; resize: none"></textarea>
                </td>
            </tr>
            <tr>
                <td align="right" width="60" valign="top">联系电话: </td>
                <td>
                    <textarea name="telephone" id="telephone" placeholder="请填写电话, 多个请换行" style="width:180px; height: 80px; resize: none"></textarea>
                </td>
            </tr>
            <tr>
                <td align="right" width="60">网站权重: </td>
                <td>
                    <select name="weight" id="weight" style="width:180px">
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
                <td align="right" width="60" valign="top">销售备注: </td>
                <td>
                    <textarea name="remark" id="remark" placeholder="请填写备注" style="width:180px; height: 80px; resize: none"></textarea>
                </td>
            </tr>
            <tr>
                <td align="right" width="60">爬取层级: </td>
                <td><input type="text" name="level" id="level" style="width:180px;" value="1"></td>
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