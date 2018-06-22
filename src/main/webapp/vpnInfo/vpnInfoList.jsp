<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
    <style>
        td{
            display: table-cell;
            vertical-align: inherit;
        }
        #showVpnInfoTableDiv {
            position: fixed;
            top: 0px;
            left: 0px;
            background-color: white;
            width: 100%;
        }
    </style>
    <title>应用信息列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="showVpnInfoTableDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px; margin-top: 40px;" cellpadding=3>
        <tr>
            <td colspan=14>
                <form method="post" id="searchVpnInfoForm" action="/internal/vpnInfo/searchVpnInfo">
                    <table style="font-size:12px;">
                        <tr>
                            <td align="right">vpn账号: </td>
                            <td><input type="text" name="userName" id="userName" value="${vpnInfoCriteria.userName}" style="width:200px;"></td>
                            <td align="right">&nbsp;&nbsp;IMEI号:&nbsp;</td>
                            <td><input type="text" name="imei" id="imei" value="${vpnInfoCriteria.imei}" style="width:200px;"></td>
                            <td align="right" width="60">
                                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                                <shiro:hasPermission name="/internal/vpnInfo/searchVpnInfo">
                                    <input type="submit" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">&nbsp;&nbsp;
                                </shiro:hasPermission>
                            </td>
                            <td>
                                <shiro:hasPermission name="/internal/vpnInfo/saveVpnInfo">
                                    <input type="button" onclick="showVpnInfoDialog()" value=" 添加 ">&nbsp;&nbsp;
                                </shiro:hasPermission>
                            </td>
                            <td>
                                <shiro:hasPermission name="/internal/vpnInfo/deleteVpnInfoList">
                                    <input type="button" onclick="deleteVpnInfos(this)" value=" 删除所选 ">
                                </shiro:hasPermission>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
    <table id="headerTable" width="100%">
        <tr bgcolor="#eeeeee" height=30>
            <td align="left" width="10" style="padding-left: 7px;"><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=100>账号</td>
            <td align="center" width=80>密码</td>
            <td align="center" width=80>IMEI</td>
            <td align="center" width=80>启用时间</td>
            <td align="center" width=80>停用时间</td>
            <td align="center" width=100>操作</td>
        </tr>
    </table>
</div>
<div id="showVpnInfoListDiv" style="margin-bottom: 30px">
    <table id="showVpnInfoListTable"  width="100%">
        <c:forEach items="${page.records}" var="vpnInfo" varStatus="status">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30 <c:if test="${status.index%2==0}">bgcolor="#eeeeee" </c:if> >
                <td width="10" style="padding-left: 7px;"><input type="checkbox" name="uuid" value="${vpnInfo.uuid}" onclick="decideSelectAll()"/></td>
                <td width="100">${vpnInfo.userName}</td>
                <td width="80">${vpnInfo.password}</td>
                <td width="80">${vpnInfo.imei}</td>
                <td width="80"><fmt:formatDate value="${vpnInfo.startTime}" pattern="yyyy-MM-dd" /></td>
                <td width="80"><fmt:formatDate value="${vpnInfo.stopTime}" pattern="yyyy-MM-dd" /></td>
                <td style="text-align: center;" width="100">
                    <shiro:hasPermission name="/internal/vpnInfo/saveVpnInfo">
                    <a href="javascript:modifyVpnInfo(${vpnInfo.uuid})">修改</a> |
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/vpnInfo/deleteVpnInfo">
                    <a href="javascript:deleteVpnInfo('${vpnInfo.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<div id="vpnInfoDialog" title="关键词信息" class="easyui-dialog" style="left: 35%;">
    <form id="vpnInfoForm" method="post" action="vpnInfoList.jsp">
        <table style="font-size:14px;" cellpadding=10 cellspacing="5">
            <tr>
                <td align="right">VPN账号:</td>
                <td><input type="text" name="userName" id="userName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">VPN密码:</td>
                <td><input type="text" name="password" id="password" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">IMEI号:</td>
                <td><input type="text" name="imei" id="imei" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">启用时间:</td>
                <td>
                    <input name="startTime" id="startTime" class="Wdate" type="text" style="width:200px;" onClick="WdatePicker()">
                </td>
            </tr>
            <tr>
                <td align="right">停用时间:</td>
                <td>
                    <input name="stopTime" id="stopTime" class="Wdate" type="text" style="width:200px;" onClick="WdatePicker()">
                </td>
            </tr>
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
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/vpnInfo/vpnInfoList.js"></script>
</body>
</html>