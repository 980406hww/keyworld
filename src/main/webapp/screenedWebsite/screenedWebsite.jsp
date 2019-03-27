<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style type="text/css">
        #showScreenedWebsiteTableDiv {
            width: 100%;
            margin: auto;
        }
        #showScreenedWebsiteTable tr:nth-child(odd){background:#EEEEEE;}

        #showScreenedWebsiteTable td{
            text-align: left;
        }
        h6{ margin: 0 5px;}
    </style>
    <title>屏蔽网站列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=13>
                <form method="post" id="searchScreenedWebsiteForm" action="/internal/screenedWebsite/searchScreenedWebsiteLists" style="margin-bottom:0px ">
                    <div>
                        <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                        <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                        <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                        <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                        优化组名称:<input type="text" name="optimizeGroupName" id="optimizeGroupName" style="width: 160px;" value="${screenedWebsite.optimizeGroupName}">
                            &nbsp;&nbsp;<input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showScreenedWebsiteDialog(null)"/>
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteBatchScreenedWebsite(this)"/>
                    </div>
                </form>
            </td>
        </tr>
    </table>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>优化组名</td>
            <td align="center" width=80>屏蔽网站</td>
            <td align="center" width=80>创建时间</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="showScreenedWebsiteTableDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showScreenedWebsiteTable">
        <c:forEach items="${page.records}" var="screenedWebsite">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width=10 style="padding-left: 7px;"><input type="checkbox" name="uuid" value="${screenedWebsite.uuid}"/></td>
                <td width=80>${screenedWebsite.optimizeGroupName}</td>
                <td width=80>${screenedWebsite.screenedWebsite}</td>
                <td width=80 style="text-align: center"><fmt:formatDate value="${screenedWebsite.createTime}" pattern="yyyy-MM-dd"/></td>
                <td width=80>
                        <a href="javascript:modifyScreenedWebsite(${screenedWebsite.uuid})">修改</a>
                        | <a href="javascript:delScreenedWebsite('${screenedWebsite.uuid}','${screenedWebsite.optimizeGroupName}')">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</div>
<div id="screenedWebsiteDialog" title="屏蔽网站信息" class="easyui-dialog" style="display:none;left: 40%;">
    <form id="screenedWebsiteForm" method="post" action="/internal/screenedWebsite/searchBadClientStatus">
        <table style="font-size:14px;" cellpadding=5>
            <input type="hidden" name="screenedWebsiteUuid" id="screenedWebsiteUuid" style="width:200px;">
            <tr>
                <td style="width:80px"  align="right">优化组名称:</td>
                <td><input type="text" name="optimizeGroupName" id="optimizeGroupName" style="width:180px;"></td>
            </tr>
            <tr>
                <td style="width:80px" align="right">屏蔽网站:</td>
            </tr>
            <tr>
                <td style="width:80px" align="right"></td>
                <td><textarea id="screenedWebsite" style="width:180px; height: 200px; resize: none"></textarea></td>
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
        <option>500</option>
        <option>1000</option>
    </select>
    </div>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/screenedWebsite/screenedWebsite.js"></script>
<script type="text/javascript">
    $(function () {
        window.onresize = function(){
            $("#showScreenedWebsiteDiv").css("margin-top",$("#topDiv").height());
        }
    });
</script>
</body>
</html>