<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>网站管理</title>
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div >
        <form method="post" id="searchWebsiteForm" action="/internal/website/searchWebsites" style="font-size:12px; margin:5px 0px 0px 5px;" cellpadding=3>
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}" />
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            网站名称:<input type="text" name="websiteName" id="websiteName" value="${websiteCriteria.websiteName}" style="width: 100px;">
            &nbsp;&nbsp;域名:<input type="text" name="domain" id="domain" value="${websiteCriteria.domain}" style="width: 100px;">
            &nbsp;&nbsp;失败次数:<input type="text" name="accessFailCount" id="accessFailCount" value="${websiteCriteria.accessFailCount}" style="width: 40px;">
            &nbsp;&nbsp;含有指定友链:<input type="text" name="friendlyLinkUrl" id="friendlyLinkUrl" style="width: 180px;" value="${(websiteCriteria.friendlyLinkUrl == null || websiteCriteria.friendlyLinkUrl == '') ? 'http://': websiteCriteria.friendlyLinkUrl}" placeholder="请填写准确友链链接url">
            &nbsp;&nbsp;含有指定广告:<input type="text" name="advertisingTagname" id="advertisingTagname" style="width: 120px;" value="${websiteCriteria.advertisingTagname}" placeholder="请填写准确广告标识">
            &nbsp;&nbsp;销售更新状态:<select id="updateSalesInfoSign" name="updateSalesInfoSign" style="width: 100px;">
                <option value="" selected="selected">请选择</option>
                <c:forEach items="${putSalesInfoSignMap}" var="putSalesInfoSign">
                    <option value="${putSalesInfoSign.key}" <c:if test="${putSalesInfoSign.key eq websiteCriteria.updateSalesInfoSign}">selected="selected"</c:if>>${putSalesInfoSign.value}</option>
                </c:forEach>
            </select>
            &nbsp;&nbsp;友链更新状态:
            <select id="synchronousFriendlyLinkSign" name="synchronousFriendlyLinkSign" style="width: 100px;" title="">
                <option value="" selected="selected">请选择</option>
                <c:forEach items="${websiteSynchronousSignMap}" var="websiteSynchronousSign">
                    <option value="${websiteSynchronousSign.key}" <c:if test="${websiteSynchronousSign.key eq websiteCriteria.synchronousFriendlyLinkSign}">selected="selected"</c:if>>${websiteSynchronousSign.value}</option>
                </c:forEach>
            </select>
            &nbsp;&nbsp;广告更新状态:
            <select id="synchronousAdvertisingSign" name="synchronousAdvertisingSign" style="width: 100px;" title="">
                <option value="" selected="selected">请选择</option>
                <c:forEach items="${websiteSynchronousSignMap}" var="websiteSynchronousSign">
                    <option value="${websiteSynchronousSign.key}" <c:if test="${websiteSynchronousSign.key eq websiteCriteria.synchronousAdvertisingSign}">selected="selected"</c:if>>${websiteSynchronousSign.value}</option>
                </c:forEach>
            </select>
            &nbsp;&nbsp;后台登录状态:
            <select id="backgroundLoginStatus" name="backgroundLoginStatus" style="width: 100px;" title="">
                <option value="" selected="selected">请选择</option>
                <option value="0" <c:if test="${0 eq websiteCriteria.backgroundLoginStatus}">selected="selected"</c:if>>正常</option>
                <option value="1" <c:if test="${1 eq websiteCriteria.backgroundLoginStatus}">selected="selected"</c:if>>url地址错误</option>
                <option value="2" <c:if test="${2 eq websiteCriteria.backgroundLoginStatus}">selected="selected"</c:if>>用户名密码</option>
            </select>
            &nbsp;&nbsp;数据库状态:<input id="databaseStatus_bak" value="${websiteCriteria.databaseStatus}" hidden/>

            <select id="databaseStatus" name="databaseStatus" style="width: 100px;" title="">

                <option value="" selected="selected">请选择</option>
                <option value="0" >正常</option>
                <option value="1" >访问失败</option>
            </select>
            &nbsp;&nbsp;index文件状态:
            <select id="indexFileStatus" name="indexFileStatus" style="width: 100px;" title="">
                <option value="" selected="selected">请选择</option>
                <option value="0" <c:if test="${0 eq websiteCriteria.indexFileStatus}">selected="selected"</c:if>>正常</option>
                <option value="1" <c:if test="${1 eq websiteCriteria.indexFileStatus}">selected="selected"</c:if>>异常</option>
            </select>
            &nbsp;&nbsp;sftp服务状态:
            <select id="sftpStatus" name="sftpStatus" style="width: 100px;" title="">
                <option value="" selected="selected">请选择</option>
                <option value="0" <c:if test="${0 eq websiteCriteria.sftpStatus}">selected="selected"</c:if>>正常</option>
                <option value="1" <c:if test="${1 eq websiteCriteria.sftpStatus}">selected="selected"</c:if>>异常</option>
            </select>
            &nbsp;&nbsp;所属行业类型:
            <select id="industryType" name="industryType" style="width: 100px;" title="">
                <option value="" selected="selected">请选择</option>
                <c:forEach items="${industryTypeMap}" var="industryType">
                    <option value="${industryType.key}" <c:if test="${industryType.key eq websiteCriteria.industryType}">selected="selected"</c:if>>${industryType.value}</option>
                </c:forEach>
            </select>
            &nbsp;&nbsp;销售网站类型:
            <select id="websiteType" name="websiteType" style="width: 100px;" title="">
                <option value="" selected="selected">请选择</option>
                <c:forEach items="${websiteTypeMap}" var="websiteType">
                    <option value="${websiteType.key}" <c:if test="${websiteType.key eq websiteCriteria.websiteType}">selected="selected"</c:if>>${websiteType.value}</option>
                </c:forEach>
            </select>
            <br>
            <shiro:hasPermission name="/internal/website/searchWebsites">
                <input type="submit" value=" 查询 " onclick="resetPageNumber()">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/website/saveWebsite">
                <input type="button" value=" 添加 " onclick="showWebsiteDialog(null)">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/friendlyLink/synchronousFriendlyLink">
                <input type="button" value=" 同步所选友链信息 " onclick="synchronousFriendlyLink()">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/advertising/synchronousAdvertising">
                <input type="button" value=" 同步所选广告信息 " onclick="synchronousAdvertising()">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/friendlyLink/saveFriendlyLinks">
                <input type="button" value=" 指定网站增加友链 " onclick="showBatchAddFriendlyLinkDialog(null)">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/advertising/saveAdvertisings">
                <input type="button" value=" 指定网站增加广告 " onclick="showBatchAddAdvertisingDialog(null)">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/friendlyLink/saveFriendlyLink">
                <input type="button" value=" 指定网站修改友链 " onclick="batchModifyFriendlyLink('${websiteCriteria.friendlyLinkUrl}')">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/advertising/saveAdvertising">
                <input type="button" value=" 指定网站修改广告 " onclick="batchModifyAdvertising('${websiteCriteria.advertisingTagname}')">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/friendlyLink/deleteFriendlyLinks">
                <input type="button" value=" 指定网站删除友链 " onclick="batchDelFriendlyLink('${websiteCriteria.friendlyLinkUrl}')">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/advertising/deleteAdvertisings">
                <input type="button" value=" 指定网站删除广告 " onclick="batchDelAdvertising('${websiteCriteria.advertisingTagname}')">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/website/putSalesInfoToWebsite">
                <input type="button" onclick="putSalesInfoToWebsite()" value=" 推送销售信息至站点 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/website/deleteWebsites">
                <input type="button" onclick="deleteWebsites()" value=" 删除所选 ">&nbsp;&nbsp;
            </shiro:hasPermission>
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr height="22">
            <td width="35" align="center" rowspan="2">
                <input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/>
            </td>
            <td width="80" align="center" rowspan="2">网站名称</td>
            <td width="80" align="center" rowspan="2">所属行业类型</td>
            <td width="80" align="center" rowspan="2">访问失败次数</td>
            <td width="80" align="center" rowspan="2">发现故障时间</td>
            <td width="80" align="center" rowspan="2">最近访问时间</td>
            <td width="80" align="center" rowspan="2">更新时间</td>
            <td width="490" align="center" colspan="7">域名信息</td>
            <td width="490" colspan="7" align="center">后台信息</td>
            <td width="280" colspan="4" align="center">数据库信息</td>
            <td width="420" colspan="6" align="center">服务器信息</td>
            <td width="80" align="center" rowspan="2">操作</td>
        </tr>
        <tr height="23" style="background-color: #ADD1FF">
            <td width="70" align="center">网站域名</td>
            <td width="70" align="center">销售网站类型</td>
            <td width="70" align="center">友情链接</td>
            <td width="70" align="center">广告</td>
            <td width="70" align="center">注册商</td>
            <td width="70" align="center">解析商</td>
            <td width="70" align="center">到期时间</td>
            <td width="70" align="center">后台链接</td>
            <td width="70" align="center">后台用户名</td>
            <td width="70" align="center">后台密码</td>
            <td width="70" align="center">更新销售信息状态</td>
            <td width="70" align="center">更新友链信息状态</td>
            <td width="70" align="center">更新广告信息状态</td>
            <td width="70" align="center">后台登录状态</td>
            <td width="70" align="center">数据库名称</td>
            <td width="70" align="center">用户名</td>
            <td width="70" align="center">密码</td>
            <td width="70" align="center">数据库登录状态</td>
            <td width="70" align="center">IP</td>
            <td width="70" align="center">用户名</td>
            <td width="70" align="center">密码</td>
            <td width="70" align="center">端口</td>
            <td width="70" align="center">index文件状态</td>
            <td width="70" align="center">sftp服务状态</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px;">
    <table style="font-size:12px; width: 100%;" id="showWebsiteTable">
        <c:forEach items="${page.records}" var="website" varStatus="status">
        <tr align="left" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
            <td width=35 align="center"><input type="checkbox" name="uuid" value="${website.uuid}" onclick="decideSelectAll()"/></td>
            <td width=80>${website.websiteName}</td>
            <td width=80> ${industryTypeMap.get(website.industry)}</td>
            <td width=80>${website.accessFailCount > 0 ? website.accessFailCount : ""}</td>
            <td width=80><fmt:formatDate value="${website.accessFailTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td width=80><fmt:formatDate value="${website.lastAccessTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td width=80><fmt:formatDate value="${website.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td width=70><a target="_blank" href="http://${website.domain}">${website.domain}</a></td>
            <td width=70 align="center">${websiteTypeMap.get(website.websiteType)}</td>
            <td width=70><a href="#" onclick="searchFriendlyLinks('/internal/friendlyLink/searchFriendlyLinkLists/${website.uuid}')">${website.friendlyLinkCount}</a></td>
            <td width=70><a href="#" onclick="searchAdvertisings('/internal/advertising/searchAdvertisingLists/${website.uuid}')">${website.advertisingCount}</a></td>
            <td width=70>${website.registrar}</td>
            <td width=70>${website.analysis}</td>
            <td width=70><fmt:formatDate value="${website.expiryTime}" pattern="yyyy-MM-dd"/></td>
            <td width=70>${website.backendDomain}</td>
            <td width=70>${website.backendUserName}</td>
            <td width=70>${website.backendPassword}</td>
            <td width=70 align="center">${putSalesInfoSignMap.get(website.updateSalesInfoSign)}</td>
            <td width=70 align="center">${putSalesInfoSignMap.get(website.synchronousFriendlyLinkSign)}</td>
            <td width=70 align="center">${putSalesInfoSignMap.get(website.synchronousAdvertisingSign)}</td>
            <td width=70 align="center">
                <c:if test="${website.backgroundLoginStatus eq 0}">
                    正常
                </c:if>
                <c:if test="${website.backgroundLoginStatus eq 1}">
                    url地址错误
                </c:if>
                <c:if test="${website.backgroundLoginStatus eq 2}">
                    用户名密码不正确
                </c:if>
            </td>
            <td width=70>${website.databaseName}</td>
            <td width=70>${website.databaseUserName}</td>
            <td width=70>${website.databasePassword}</td>
            <td width=70 align="center">
                <c:if test="${website.databaseStatus eq '0'}">
                    正常
                </c:if>
                <c:if test="${website.databaseStatus ne '0'}">
                    ${website.databaseStatus}
                </c:if>
            </td>
            <td width=70>${website.serverIP}</td>
            <td width=70>${website.serverUserName}</td>
            <td width=70>${website.serverPassword}</td>
            <td width=70>${website.serverPort}</td>
            <td width=70 align="center">
                <c:if test="${website.indexFileStatus eq 0}">
                    正常
                </c:if>
                <c:if test="${website.indexFileStatus ne 0}">
                    异常
                </c:if>
            </td>
            <td width=70 align="center">
                <c:if test="${website.sftpStatus eq 0}">
                    正常
                </c:if>
                <c:if test="${website.sftpStatus eq 1}">
                    异常
                </c:if>
            </td>
            <td style="text-align: center;" width="75">
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
<div id="websiteDialog" title="网站信息" class="easyui-dialog" style="display: none;left: 35%;">
    <form id="websiteForm" method="post">
        <table style="font-size:14px;" cellpadding=10 cellspacing="5">
            <tr>
                <td align="right">网站名称:</td>
                <td><input type="text" name="websiteName" id="websiteName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">所属行业类型</td>
                <td>
                    <select id="industry" name="industry" style="width: 200px;" title="">
                        <option value="" selected="selected">请选择</option>
                        <c:forEach items="${industryTypeMap}" var="industryType">
                            <option value="${industryType.key}">${industryType.value}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right">网站域名:</td>
                <td><input type="text" name="domain" id="domain" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">域名注册商:</td>
                <td><input type="text" name="registrar" id="registrar" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">域名解析商:</td>
                <td><input type="text" name="analysis" id="analysis" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">域名到期时间:</td>
                <td>
                    <input name="expiryTime" id="expiryTime" class="Wdate" type="text" style="width:200px;" onClick="WdatePicker()">
                </td>
            </tr>
            <tr>
                <td align="right">后台链接路径:</td>
                <td><input type="text" name="backendDomain" id="backendDomain" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">后台用户名:</td>
                <td><input type="text" name="backendUserName" id="backendUserName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">后台密码:</td>
                <td><input type="text" name="backendPassword" id="backendPassword" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">数据库名称:</td>
                <td><input type="text" name="databaseName" id="databaseName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">数据库用户名:</td>
                <td><input type="text" name="databaseUserName" id="databaseUserName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">数据库密码:</td>
                <td><input type="text" name="databasePassword" id="databasePassword" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">服务器IP:</td>
                <td><input type="text" name="serverIP" id="serverIP" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">服务器用户名:</td>
                <td><input type="text" name="serverUserName" id="serverUserName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">服务器密码:</td>
                <td><input type="text" name="serverPassword" id="serverPassword" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">销售网站类型:</td>
                <td>
                    <select id="websiteType" name="websiteType" style="width: 200px;" title="">
                        <option value="" selected="selected">请选择</option>
                        <c:forEach items="${websiteTypeMap}" var="websiteType">
                            <option value="${websiteType.key}">${websiteType.value}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right">需要解析DNS:</td>
                <td>
                    <select name="dnsAnalysisStatus" id="dnsAnalysisStatus"  style="width:200px">
                        <option value="1">是</option>
                        <option value="0" selected>否</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="friendlyLinkDialog" title="友情链接信息" class="easyui-dialog" style="display: none">
    <form id="friendlyLinkForm">
        <table style="font-size:12px" id="friendlyLinkTable" align="center" cellspacing="8">

            <tr>
                <td style="width:60px;"  align="right">网站名称:</td>
                <td>
                    <input type="text" name="friendlyLinkWebName" id="friendlyLinkWebName" style="width:180px;">
                </td>
            </tr>
            <tr>
                <td style="width:60px;"  align="right">网址:</td>
                <td>
                    <input type="text" name="friendlyLinkUrl" id="friendlyLinkUrl" style="width:180px;" placeholder="默认没有http://会加上">
                </td>
            </tr>
            <tr>
                <td style="width:60px;"  align="right">网站排序:</td>
                <td>
                    <input type="hidden" name="originalSortRank" id="originalSortRank">
                    <input type="hidden" name="friendlyLinkType" id="friendlyLinkType" style="width:180px;" value="综合网站_0">
                    <input type="text" name="friendlyLinkSortRank" id="friendlyLinkSortRank" style="width:180px;" placeholder="不填写则排在最后面">
                </td>
            </tr>
            <tr>
                <td style="width:60px;"  align="right">站长Email:</td>
                <td>
                    <input type="text" name="friendlyLinkEmail" id="friendlyLinkEmail" style="width:180px;" placeholder="不填写默认为空">
                </td>
            </tr>
            <tr>
                <td style="width:60px;"  align="right">页面选择:</td>
                <td>
                    <input id="friendlyLinkIsCheck" name="friendlyLinkIsCheck" type="radio" value="2" checked/>首页
                    <input id="friendlyLinkIsCheck" name="friendlyLinkIsCheck" type="radio" value="1"/>内容页
                </td>
            </tr>
            <tr>
                <td style="width:60px;"  align="right">到期时间:</td>
                <td>
                    <input name="expirationTime" id="expirationTime" class="Wdate" type="text" onClick="WdatePicker()">
                </td>
            </tr>
            <tr>
                <td style="width:60px;"  align="right">网站Logo:</td>
                <td>
                    <input type="file" id="friendlyLinkLogo" name="friendlyLinkLogo" style="width: 180px">
                </td>
            </tr>
            <tr>
                <td style="width:60px;"  align="right">网站简况:</td>
            </tr>
            <tr>
                <td style="width:60px;"  align="right"></td>
                <td>
                    <textarea id="friendlyLinkMsg" style="width:180px; height: 150px; resize: none"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="advertisingDialog" title="广告信息" class="easyui-dialog" style="display: none">
    <form id="advertisingForm">
        <table style="font-size:12px" id="advertisingTable" align="center" cellspacing="8">
            <tr>
                <td style="width:80px;"  align="right">广告标识:</td>
                <td>
                    <input type="hidden" name="advertisingId" id="advertisingId">
                    <input type="hidden" name="advertisingType" id="advertisingType" list="advertisingType_list" style="width:180px;"  value="默认分类_0">
                    <input type="hidden" name="advertisingArcType" id="advertisingArcType" list="advertisingArcType_list"  style="width:180px;"  value="无同名标识所有网站_0">
                    <input type="text" name="advertisingTagname" id="advertisingTagname" style="width:180px;" placeholder="唯一，不可进行更改，查询条件">
                </td>
            </tr>

            <tr>
                <td style="width:80px;"  align="right">广告名称:</td>
                <td>
                    <input type="text" name="advertisingAdName" id="advertisingAdName" style="width:180px;">
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right">时间限制:</td>
                <td>
                    <input id="advertisingTimeSet" name="advertisingTimeSet" type="radio" value="0" checked/>永不过期&nbsp;
                    <input id="advertisingTimeSet" name="advertisingTimeSet" type="radio" value="1"/>在设定时间内有效
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right">投放时间:</td>
                <td>
                    从<input name="advertisingStarttime" id="advertisingStarttime" class="Wdate" type="text" onClick="WdatePicker()">
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right"></td>
                <td>
                    到<input name="advertisingEndtime" id="advertisingEndtime" class="Wdate" type="text" onClick="WdatePicker()">
                </td>
            </tr>
        </table>
        <table style="font-size:12px" cellspacing="8">
            <tr>
                <td colspan="2">
                    <table border="0" style="display:block;font-size:12px;" cellspacing="8" id="advertisingBodyCheckedTable">
                        <td style="width:72px;"  align="right">广告内容:</td>
                        <td>
                            <input id="advertisingBodyChecked" name="advertisingBodyChecked" type="radio" value="0" checked  onchange="changeAdvertisingBodySubmit('code')"/>代码&nbsp;
                            <input id="advertisingBodyChecked" name="advertisingBodyChecked" type="radio" value="1"  onchange="changeAdvertisingBodySubmit('txt')"/>文字&nbsp;
                            <input id="advertisingBodyChecked" name="advertisingBodyChecked" type="radio" value="2"  onchange="changeAdvertisingBodySubmit('img')"/>图片&nbsp;
                            <input id="advertisingBodyChecked" name="advertisingBodyChecked" type="radio" value="3"  onchange="changeAdvertisingBodySubmit('flash')"/>Flash&nbsp;
                        </td>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" cellpadding="0" id="code">
                        <tr>
                            <td align="right" style="width:72px">广告代码:</td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px"></td>
                            <td><textarea  id="htmlcode" name="htmlcode" rows="5" cols="27" placeholder="请填写广告代码，支持html代码"></textarea></td>
                        </tr>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" id="txt">
                        <tr>
                            <td align="right" style="width:72px">文字内容:</td>
                            <td><input type="text" name="txtTitle" id="txtTitle"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">文字链接:</td>
                            <td><input type="text" name="txtLink" id="txtLink" style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">文字颜色</td>
                            <td><input type="text" name="txtColor" id="txtColor" style="width:180px" placeholder="例如:red,#EF8684"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">文字大小</td>
                            <td><input type="text" name="txtSize" id="txtSize" style="width:180px;" placeholder="例如:4px,12px"/></td>
                        </tr>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" cellpadding="0" id="img">
                        <tr>
                            <td align="right" style="width:72px">图片地址*:</td>
                            <td><input type="text" name="imgUrl" id="imgUrl"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">图片链接*:</td>
                            <td><input type="text" name="imgLink" id="imgLink"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">图片宽度:</td>
                            <td><input type="text" name="imgWidth" id="imgWidth" style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">图片高度:</td>
                            <td><input type="text" name="imgHeight" id="imgHeight" style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">图片描述:</td>
                            <td><input type="text" name="imgDescrip" id="imgDescrip" style="width:180px;"/></td>
                        </tr>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" cellpadding="0" id="flash">
                        <tr>
                            <td align="right" style="width:72px">flash链接:</td>
                            <td><input type="text" name="flashLink" id="flashLink"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">flash宽度:</td>
                            <td><input type="text" name="flashWidth" id="flashWidth"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">flash高度:</td>
                            <td><input type="text" name="flashHeight" id="flashHeight" style="width:180px;"/></td>
                        </tr>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" id="advertisingNormbodyTable">
                        <tr>
                            <td style="width:76px;margin-left: -4px"  align="right">正常显示内容:</td>
                        </tr>
                        <tr>
                            <td style="width:72px;"  align="right"></td>
                            <td>
                                <textarea id="advertisingNormbody" style="width:180px; height: 100px; resize: none"></textarea>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="margin-top: -2px">
                <td style="width:80px;"  align="right">过期显示内容:</td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right"></td>
                <td>
                    <textarea id="advertisingExpbody" style="width:180px; height: 100px; resize: none"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="synchronousFriendlyLinkDialog" title="同步友链信息(同步默认数据)" class="easyui-dialog" style="display: none;left: 35%;">
    <form id="synchronousFriendlyLinkForm" method="post">
        <table style="font-size:14px;" cellpadding=10 cellspacing="5">

            <tr>
                <td style="width:60px;"  align="right">到期时间:</td>
                <td>
                    <input name="expirationTime" id="expirationTime" class="Wdate" type="text" onClick="WdatePicker()">
                </td>
            </tr>
            <tr>
                <td style="width:60px;"  align="right">续费时间:</td>
                <td>
                    <input name="renewTime" id="renewTime" class="Wdate" type="text" onClick="WdatePicker()">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="synchronousAdvertisingDialog" title="同步广告信息(同步默认数据)" class="easyui-dialog" style="display: none;left: 35%;">
    <form id="synchronousAdvertisingForm" method="post">
        <table style="font-size:14px;" cellpadding=10 cellspacing="5">

            <tr>
                <td style="width:60px;"  align="right">续费时间:</td>
                <td>
                    <input name="renewTime" id="renewTime" class="Wdate" type="text" onClick="WdatePicker()">
                </td>
            </tr>
        </table>
    </form>
</div>
<datalist id="customer_list">
</datalist>
<datalist id="advertisingType_list">
</datalist>
<datalist id="advertisingArcType_list">
</datalist>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/website/website.js"></script>
<script language="javascript">
    $(function () {
        $("#centerDiv").css("margin-top", $("#topDiv").height());
        window.onresize = function(){
            $("#centerDiv").css("margin-top", $("#topDiv").height());
        }
    });

    window.onload = selectDatabaseStatus;

    function selectDatabaseStatus(){
        var databaseStatus = $("#databaseStatus_bak").val();
        var dbs = $("#databaseStatus");
        if(databaseStatus=="" || databaseStatus == null){
            dbs.val("");
        }else if(databaseStatus == "0"){
            dbs.val("0");
        }else{
            dbs.val("1");
        }
    }

    function searchFriendlyLinks(url) {
        window.open(url);
    }
    function searchAdvertisings(url) {
        window.open(url);
    }
</script>
</body>
</html>