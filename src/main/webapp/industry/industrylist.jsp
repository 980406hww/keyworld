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
    <div style="text-align: right; margin: 10px 5px;">
        <div style="margin: 5px 0px 0px 5px;">
<%--            <shiro:hasPermission name="/internal/industry/downloadIndustryInfo">--%>
                <a target="_blank" href="javascript:downloadIndustryInfo()">导出网站联系信息</a>
<%--            </shiro:hasPermission>--%>
            <shiro:hasPermission name="/internal/industry/uploadIndustryInfos">
                | <a target="_blank" href="javascript:uploadIndustryInfos('SuperIndustrySimple')">Excel上传</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="/SuperIndustrySimpleList.xls">
                | <a target="_blank" href="/SuperIndustrySimpleList.xls">模板下载</a>
            </shiro:hasPermission>
        </div>
    </div>
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
            <td align="center" width=100>用户名称</td>
            <td align="center" width=100>行业名称</td>
            <td align="center" width=80>搜索引擎</td>
            <td align="center" width=200>目标起始网址</td>
            <td align="center" width=40>爬取页数</td>
            <td align="center" width=40>每页条数</td>
            <td align="center" width=60>状态</td>
            <td align="center" width=60>创建时间</td>
            <td align="center" width=100>操作</td>
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
                <td width=100 style="text-align: center">${industryInfo.userID}</td>
                <td width=100>
                    <a href="#" onclick="searchIndustryDetails('/internal/industryDetail/searchIndustryDetails/${industryInfo.uuid}')">${industryInfo.industryName}(${industryInfo.detailCount})</a>
                </td>
                <td width=80 style="text-align: center">${industryInfo.searchEngine}</td>
                <td width=200>
                    <a href="${industryInfo.targetUrl}" target="_blank">${industryInfo.targetUrl}</a>
                </td>
                <td width=40 style="text-align: center">${industryInfo.pageNum}</td>
                <td width=40 style="text-align: center">${industryInfo.pagePerNum}</td>
                <td width=60 style="text-align: center" name="status" title="更新时间：<fmt:formatDate value="${industryInfo.updateTime}" pattern="yyyy-MM-dd HH:mm"/>">
                    <c:choose>
                        <c:when test="${industryInfo.status == 2}">
                            <span style="color: forestgreen; text-align: center;">
                                爬取完成
                            </span>
                        </c:when>
                        <c:when test="${industryInfo.status == 1}">
                            <span style="color: darkorange; text-align: center;" >
                                爬取中
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: red; text-align: center;">
                                未爬取
                            </span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width=60 style="text-align: center"><fmt:formatDate value="${industryInfo.createTime}" pattern="yyyy-MM-dd"/></td>
                <td width=100>
                    <shiro:hasPermission name="/internal/industry/saveIndustry">
                        <a href="javascript:modifyIndustry(${industryInfo.uuid})">修改</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/industry/delIndustry">
                        | <a href="javascript:delIndustry('${industryInfo.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<%-- excel上传 --%>
<div id="uploadExcelDialog"  style="display: none;text-align: left;height: 60px; left: 40%;" title="Excel上传" class="easyui-dialog">
    <form method="post" id="uploadExcelForm" style="margin-top: 10px"  enctype="multipart/form-data" >
        <span>请选择要上传的文件(<label id="excelType" style="color: red">.xls, .xlsx</label>)</span>
        <div style="height: 10px;"></div>
        <input type="file" id="uploadExcelFile" name="file" >
    </form>
</div>
<%-- 新增或修改行业信息 --%>
<div id="industryDialog" title="行业信息" class="easyui-dialog" style="display:none;left: 40%;">
    <form id="industryForm" method="post">
        <table style="font-size:12px;" align="center" cellspacing="8" cellpadding=5>
            <tr>
                <td align="right" width="60">行业名称: </td>
                <td><input type="text" name="industryName" id="industryName" style="width:180px;" placeholder="行业名称"></td>
            </tr>
            <tr>
                <td align="right" width="60">搜索引擎: </td>
                <td>
                    <select name="searchEngine" id="searchEngine" style="width:180px">
                        <c:forEach items="${searchEngineMap}" var="entry">
                            <option value="${entry.value}">${entry.key}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right" width="60">起始网址: </td>
                <td><input type="text" name="targetUrl" id="targetUrl" onkeyup="setPageNumAndPagePerNumByTargetUrl(this.val)" style="width:180px;" placeholder="起始网址"></td>
            </tr>
            <tr>
                <td align="right" width="60">爬取页数: </td>
                <td><input type="text" class="easyui-numberspinner" data-options="min:0,increment:1" name="pageNum" id="pageNum" style="width:180px;"></td>
            </tr>
            <tr>
                <td align="right" width="60">每页条数: </td>
                <td><input type="text" class="easyui-numberspinner" data-options="min:0,increment:10" name="pagePerNum" id="pagePerNum" style="width:180px;"></td>
            </tr>
            <tr>
                <td align="right" width="60">爬取状态: </td>
                <td>
                    <select name="status" id="status" style="width: 180px;">
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

    function searchIndustryDetails(url) {
        window.open(url);
    }
</script>
</body>
</html>