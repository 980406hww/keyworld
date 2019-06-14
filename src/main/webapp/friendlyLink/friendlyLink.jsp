<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
    <style type="text/css">
        #showFriendlyLinkTableDiv {
            width: 100%;
            margin: auto;
        }
        #showFriendlyLinkTable tr:nth-child(odd){background:#EEEEEE;}

        #showFriendlyLinkTable td{
            text-align: left;
        }
        h6{ margin: 0 5px;}
    </style>
    <title>友情链接列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=13>
                <form method="post" id="searchFriendlyLinkForm" action="/internal/friendlyLink/searchFriendlyLinkLists" style="margin-bottom:0px ">
                    <div>
                        <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                        <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                        <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                        <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                        <input type="hidden" name="websiteUuid" id="websiteUuid" value="${friendlyLinkCriteria.websiteUuid}"/>
                        用户名称:<input type="text" name="customerInfo" id="customerInfo" value="${friendlyLinkCriteria.customerInfo}">&nbsp;&nbsp;
                        友链名称:<input type="text" name="friendlyLinkWebName" id="friendlyLinkWebName" value="${friendlyLinkCriteria.friendlyLinkWebName}">&nbsp;&nbsp;
                        友链域名:<input type="text" name="friendlyLinkUrl" id="friendlyLinkUrl" value="${friendlyLinkCriteria.friendlyLinkUrl}">&nbsp;&nbsp;
                        <input id="expire" name="expire" type="checkbox"  onclick="expireValue()" value="${friendlyLinkCriteria.expire}"/>过期友链&nbsp;&nbsp;
                        所在页面:
                        <select name="friendlyLinkIsCheck" id="friendlyLinkIsCheck">
                            <option value='0'>所有</option>
                            <option value='2'>首页</option>
                            <option value='1'>内页</option>
                        </select>&nbsp;
                            &nbsp;&nbsp;<input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showFriendlyLinkDialog(${friendlyLinkCriteria.websiteUuid}, 0)"/>
                            &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteBatchFriendlyLink(${friendlyLinkCriteria.websiteUuid})"/>
                    </div>
                </form>
            </td>
        </tr>
    </table>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>用户名称</td>
            <td align="center" width=80>网站名称</td>
            <td align="center" width=80>域名地址</td>
            <td align="center" width=80>排序值</td>
            <td align="center" width=80>网站简况	</td>
            <td align="center" width=80>站长EMAIL</td>
            <td align="center" width=80>站点类型</td>
            <td align="center" width=80>链接位置</td>
            <td align="center" width=80>申请时间</td>
            <td align="center" width=80>到期时间</td>
            <td align="center" width=80>续费时间</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="showFriendlyLinkTableDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showFriendlyLinkTable">
        <c:forEach items="${page.records}" var="friendlyLink">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width=10 style="padding-left: 7px;"><input type="checkbox" name="uuid" value="${friendlyLink.uuid}"/></td>
                <td width=80>${friendlyLink.customerInfo}</td>
                <td width=80>${friendlyLink.friendlyLinkWebName}</td>
                <td width=80>${friendlyLink.friendlyLinkUrl}</td>
                <td width=80>${friendlyLink.friendlyLinkSortRank}</td>
                <td width=80>${friendlyLink.friendlyLinkMsg}</td>
                <td width=80>${friendlyLink.friendlyLinkEmail}</td>
                <td width=80>${friendlyLink.friendlyLinkType}</td>
                <td width=80>${friendlyLink.friendlyLinkIsCheck == 1 ? "内页" : "首页"}</td>
                <td width=80 style="text-align: center"><fmt:formatDate value="${friendlyLink.friendlyLinkDtime}" pattern="yyyy-MM-dd"/></td>
                <td width=80 style="text-align: center"><fmt:formatDate value="${friendlyLink.expirationTime}" pattern="yyyy-MM-dd"/></td>
                <td width=80 style="text-align: center"><fmt:formatDate value="${friendlyLink.renewTime}" pattern="yyyy-MM-dd"/></td>
                <td width=80>
                        <a href="javascript:modifyFriendlyLink(${friendlyLink.uuid})">修改</a>
                        | <a href="javascript:delFriendlyLink(${friendlyLink.uuid})">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</div>
<div id="friendlyLinkDialog" title="友情链接信息" class="easyui-dialog" style="display: none">
    <form id="friendlyLinkForm">
        <table style="font-size:12px" id="friendlyLinkTable" align="center" cellspacing="8">
            <tr>
                <td style="width:60px;"  align="right">用户名称:</td>
                <td>
                    <input type="hidden" name="friendlyLinkId" id="friendlyLinkId">
                    <input type="text" name="customerInfo" id="customerInfo" list="customer_list" style="width:180px;">
                </td>
            </tr>
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
                <td style="width:60px;"  align="right">网站类型:</td>
                <td>
                    <input type="text" name="friendlyLinkType" id="friendlyLinkType" list="friendlyLinkType_list"  style="width:180px;">
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
<datalist id="customer_list">
</datalist>
<datalist id="friendlyLinkType_list">
</datalist>
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
<script src="${staticPath }/friendlyLink/friendlyLink.js"></script>
<script type="text/javascript">
    $(function () {
        initExpireChecked();
        initPaging();
        window.onresize = function(){
            $("#showFriendlyLinkTableDiv").css("margin-top",$("#topDiv").height());
        }
    });

    function initPaging() {
        var searchFriendlyLinkForm = $("#searchFriendlyLinkForm");
        searchFriendlyLinkForm.find("#friendlyLinkIsCheck").val('${friendlyLinkCriteria.friendlyLinkIsCheck}');
    }

    function initExpireChecked() {
        if(${friendlyLinkCriteria.expire == 1}){
            $("#expire").prop("checked",true);
        }else{
            $("#expire").prop("checked",false);
        }
    }

    function expireValue() {
        if ($("#expire").is(":checked")){
            $("#expire").val("1");
        } else {
            $("#expire").val("2");
        }
    }
</script>
</body>
</html>