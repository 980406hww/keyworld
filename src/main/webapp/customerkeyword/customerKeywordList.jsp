<%@include file="/commons/global.jsp" %>
<html>
    <head>
        <title>关键字列表</title>
        <style>
            <!--
            #customerKeywordTopDiv {
                position: fixed;
                top: 0px;
                left: 0px;
                background-color: white;
                width: 100%;
            }

            #customerKeywordDiv {
                width: 100%;
                margin-bottom: 47PX;
            }

            #customerKeywordTable {
                width: 100%;
            }
            #customerKeywordTable tr:nth-child(odd){background:#EEEEEE;}

            #customerKeywordTable td{
                text-align: left;
            }

            .tip-yellow {
                z-index:1000;
                text-align:left;
                padding:7px;
                font-size: 12px;
                min-width:200px;
                max-width:550px;
                color:#8c3901;
                background-color: #fff;
            }
            #saveCustomerKeywordDialog ul{list-style: none;margin: 0px;padding: 0px;}
            #saveCustomerKeywordDialog li{margin: 5px 0;}
            #saveCustomerKeywordDialog .customerKeywordSpanClass{width: 70px;display: inline-block;text-align: right;}

            #userNoteBookTable {
                text-align: center;
            }
            #userNoteBookTable tr {
                height: 20px;
                border-bottom: 1px solid black;
            }
            #userNoteBookTable thead {
                background-color: #eeeeee !important;
            }
            #userNoteBookTable tbody tr:hover {
                background-color: green;
            }
            #userNoteBookTable tr td {
                border-bottom: 1px solid black;
            }
            #userNoteBookTable p {
                float: left;
                text-align: left;
                padding: 0px 10px;
            }
        </style>
        <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
    </head>
<body>
<%@include file="/commons/basejs.jsp" %>
<div id="customerKeywordTopDiv" >
    <%@include file="/menu.jsp" %>
            <table width=100% style="border:1px solid #000000;font-size:12px;" cellpadding=3>
                <tr border="1" height=30>
                    <td width=150>联系人: ${customer.contactPerson}</td>
                    <td width=100>QQ: ${customer.qq}</td>
                    <td width=100>电话: ${customer.telphone}</td>
                    <td width=80>关键字数: ${customer.keywordCount}</td>
                    <td width=250>备注: ${customer.remark}</td>
                </tr>
            </table>

    <div style="text-align: right;margin-bottom: 5px">
        <div style="margin-bottom: 5px">
    <shiro:hasPermission name="/internal/customerKeyword/uploadCustomerKeywords">
        <a target="_blank" href="javascript:uploadCustomerKeywords('${customerKeywordCriteria.customerUuid}', 'SuperUserSimple')">Excel上传(简化版)</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="/SuperUserSimpleKeywordList.xls">
        | <a target="_blank" href="/SuperUserSimpleKeywordList.xls">简化版下载</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="/internal/customerKeyword/uploadCustomerKeywords">
        | <a target="_blank" href="javascript:uploadCustomerKeywords('${customerKeywordCriteria.customerUuid}', 'SuperUserFull')">Excel上传(完整版)</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="/SuperUserFullKeywordList.xls">
        | <a target="_blank" href="/SuperUserFullKeywordList.xls">完整版下载</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="/internal/customerKeyword/downloadSingleCustomerReport">
        | <a target="_blank" href="/internal/dailyReport/downloadSingleCustomerReport/${customerKeywordCriteria.customerUuid}">导出日报表</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="/internal/customerKeyword/downloadCustomerKeywordInfo">
        | <a target="_blank" href="javascript:downloadCustomerKeywordInfo()">导出结果</a>&nbsp;&nbsp;
    </shiro:hasPermission>
    <shiro:hasPermission name="/internal/customerKeyword/saveCustomerKeyword">
        | <a target="_blank" href="javascript:updateBearPawNumber('total', '${customerKeywordCriteria.customerUuid}')">更改当前熊掌号</a>
        | <a target="_blank" href="javascript:updateBearPawNumber('selected', '${customerKeywordCriteria.customerUuid}')">更改选中熊掌号</a>
        | <a target="_blank" href="javascript:CustomerKeywordBatchUpdate('${sessionScope.entryType}')">批量设置</a>
    </shiro:hasPermission>
        </div><div>

    <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordSearchEngine">
        <a href="javascript:showSearchEngineChangeDialog({'title': '修改所有关键字搜索引擎', 'customerUuid':'${customerKeywordCriteria.customerUuid}'})">修改所有搜索引擎</a> |
        <a href="javascript:updateSpecifiedCustomerKeywordSearchEngine()">修改选中搜索引擎</a> |
    </shiro:hasPermission>
    <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordGroupNameByRank">
        <a href="javascript:showGroupNameChangeByRankDialog('${customerKeywordCriteria.customerUuid}')">按排名修改分组</a> |
    </shiro:hasPermission>
    <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordGroupName">
        <a href="javascript:showGroupNameChangeDialog({'title': '修改客户关键字分组', 'customerUuid':'${customerKeywordCriteria.customerUuid}'})">修改所有分组</a> |
        <a href="javascript:updateSpecifiedCustomerKeywordGroupName()">修改选中分组</a> |
        <a href="javascript:stopOptimization(${customerKeywordCriteria.customerUuid})">下架所有关键字</a>|
    </shiro:hasPermission>
    <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordStatus">
        <a href="javascript:updateCustomerKeywordStatus(0)">暂停选中关键字</a> |
        <a href="javascript:updateCustomerKeywordStatus(1)">激活选中关键字</a> |
    </shiro:hasPermission>
    <shiro:hasPermission name="/internal/customerKeyword/deleteCustomerKeywords">
        <a href="javascript:delAllItems('EmptyTitleAndUrl','${customerKeywordCriteria.customerUuid}')">删除标题和网址为空的关键字</a> |
        <a href="javascript:delAllItems('EmptyTitle','${customerKeywordCriteria.customerUuid}')">删除标题为空的关键字</a> |
    </shiro:hasPermission>
    <shiro:hasPermission name="/internal/customerKeyword/cleanTitle">
        <a href="javascript:cleanTitle('${customerKeywordCriteria.customerUuid}','CaptureTitleFlag')">重采标题</a> |
        <a href="javascript:cleanTitle('${customerKeywordCriteria.customerUuid}','CaptureTitleBySelected')">重采所选标题</a> |
        <a href="javascript:cleanTitle('${customerKeywordCriteria.customerUuid}', 'SelectedCustomerKeywordTitle')">清空所选标题</a> |
        <a href="javascript:cleanTitle('${customerKeywordCriteria.customerUuid}', 'CustomerTitle')">清空客户标题</a>&nbsp;&nbsp;
    </shiro:hasPermission>

    </div>
    </div>
    <form id="searchCustomerKeywordForm" style="font-size:12px; width: 100%;" action="/internal/customerKeyword/searchCustomerKeywords" method="post">
        <div id="searchCustomerKeywordTable">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            <input id="customerUuid" name="customerUuid" type="hidden" value="${customerKeywordCriteria.customerUuid}">
            关键字:<input type="text" name="keyword" id="keyword" value="${customerKeywordCriteria.keyword}"
                       style="width:80px;">
            URL:<input type="text" name="url" id="url" value="${customerKeywordCriteria.url}"
                       style="width:80px;">
            排除URL:<input type="text" name="excludeUrl" id="excludeUrl" value="${customerKeywordCriteria.excludeUrl}"
                               style="width:80px;">
            熊掌号:<input type="text" name="bearPawNumber" id="bearPawNumber" value="${customerKeywordCriteria.bearPawNumber}"
                       style="width:80px;">
            关键字状态:
            <select name="status" id="status">
                <option value=''>所有</option>
                <option value='1'>激活</option>
                <option value='2'>新增</option>
                <option value='0'>过期</option>
            </select>
            优化组名:
            <input type="text" name="optimizeGroupName" id="optimizeGroupName"
                   value="${customerKeywordCriteria.optimizeGroupName}" style="width:100px;">
            要刷:<input type="text" name="gtOptimizePlanCount" id="gtOptimizePlanCount" placeholder=">=" value="${customerKeywordCriteria.gtOptimizePlanCount}" style="width:40px;"/>
            <input type="text" name="ltOptimizePlanCount" id="ltOptimizePlanCount" placeholder="<="  value="${customerKeywordCriteria.ltOptimizePlanCount}" style="width:40px;"/>
            已刷:<input type="text" name="gtOptimizedCount" id="gtOptimizedCount" placeholder=">=" value="${customerKeywordCriteria.gtOptimizedCount}" style="width:40px;"/>
            <input type="text" name="ltOptimizedCount" id="ltOptimizedCount" placeholder="<="  value="${customerKeywordCriteria.ltOptimizedCount}" style="width:40px;"/>
            排名:
            <input type="text" name="gtPosition" id="gtPosition" placeholder=">=" value="${customerKeywordCriteria.gtPosition}" style="width:40px;"/>
            <input type="text" name="ltPosition" id="ltPosition" placeholder="<=" value="${customerKeywordCriteria.ltPosition}" style="width:40px;"/>

            <input id="noPosition" name="noPosition" type="checkbox"  onclick="noPositionValue()" />显示0 &nbsp;
            <input id="displayStop" name="displayStop" type="checkbox"  onclick="displayStopValue()" value="${customerKeywordCriteria.displayStop}" <c:if test="${customerKeywordCriteria.displayStop=='1'}">checked</c:if>/>显示下架 &nbsp;
            无效点击数:
            <input type="text" name="invalidRefreshCount" id="invalidRefreshCount"
                   value="${customerKeywordCriteria.invalidRefreshCount}" style="width:20px;">

            搜索引擎:
            <select name="searchEngine" id="searchEngine">
                <option value="">全部</option>
                <option value="百度">百度</option>
                <option value="360">360</option>
                <option value="搜狗">搜狗</option>
                <option value="神马">神马</option>
            </select>
            排序:
            <select name="orderingElement" id="orderingElement">
                <option value="0">关键字</option>
                <option value="1">创建日期</option>
                <option value="2">当前排名</option>
                <option value="3">添加序号</option>
            </select>
            标题:
            <select name="titleFlag" id="titleFlag">
                <option value=""></option>
                <option value="1">无</option>
                <option value="2">有</option>
            </select>
            <br>
            指数:<input type="text" id="gtCurrentIndexCount" name="gtCurrentIndexCount" placeholder=">=" value="${customerKeywordCriteria.gtCurrentIndexCount}" style="width: 40px;">
            <input type="text" id="ltCurrentIndexCount" name="ltCurrentIndexCount" placeholder="<=" value="${customerKeywordCriteria.ltCurrentIndexCount}" style="width: 40px;">
            备注:<input type="text" id="remarks" name="remarks" value="${customerKeywordCriteria.remarks}" style="width: 90px;">
            &nbsp;&nbsp;
            收录备注:<input type="text" id="enteredKeywordRemarks" name="enteredKeywordRemarks" value="${customerKeywordCriteria.enteredKeywordRemarks}" style="width: 90px;">
            &nbsp;&nbsp;
            <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
            <input type="submit" onclick="resetPageNumber()"
                   value=" 查询 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/saveCustomerKeyword">
                <input type="button" onclick="addCustomerKeyword(null, '${customerKeywordCriteria.customerUuid}')"
                       value=" 增加 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/editOptimizePlanCount">
            <input type="button" onclick="showOptimizePlanCountDialog()" value=" 修改刷量 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/deleteCustomerKeywords">
            <input type="button" onclick="delAllItems('ByUuid','${customerKeywordCriteria.customerUuid}')" value=" 删除所选 ">
            </shiro:hasPermission>

            <shiro:hasPermission name="/internal/customerKeyword/saveCustomerKeyword">
                <input type="button" onclick="updateKeywordCustomerUuid()" value="更改所选词客户">
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/deleteCustomerKeywords">
                <c:if test="${sessionScope.get('entryType') eq 'qz'}">
                    <input type="button" onclick="deleteDuplicateCustomerKeyword(${customerKeywordCriteria.customerUuid})" value="删除重复关键字">
                </c:if>
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/qzsetting/save">
                <input type="button" id="customerKeywordBtnInput" onclick="openMessageBox('关键字列表', '${customerKeywordCriteria.customerUuid}', '${customer.contactPerson}')" value=" 用户留言 ">
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/usernotebook/searchUserNoteBooks">
                <input type="button" id="customerKeywordNoteBookBtnInput" onclick="openNoteBookDialog('${customerKeywordCriteria.customerUuid}')" value=" 记事本 ">
            </shiro:hasPermission>
        </div>
    </form>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td width=10 style="


            padding-left:7px;"><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=100>关键字</td>
            <td align="center" width=200>URL</td>
            <td align="center" width=150>熊掌号</td>
            <td align="center" width=250>标题</td>
            <td align="center" width=30>指数</td>
            <td align="center" width=50>原排名</td>
            <td align="center" width=50>现排名</td>
            <td align="center" width=50>搜索引擎</td>
            <td align="center" width=40>计价方式</td>
            <td align="center" width=30>要刷</td>
            <td align="center" width=30>已刷</td>
            <td align="center" width=30>无效</td>
            <td align="center" width=60>报价</td>
            <td align="center" width=80>开始优化日期</td>
            <td align="center" width=80>最后优化时间</td>
            <td align="center" width=50>订单号</td>
            <td align="center" width=100>备注</td>
            <td align="center" width=100>收录备注</td>
            <td align="center" width=60>优化组名</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="customerKeywordDiv" style="margin-bottom: 30px">
    <table id="customerKeywordTable">
        <c:forEach items="${page.records}" var="customerKeyword">
            <tr style="" height=30 onmouseover="doOver(this);" onmouseout="doOut(this);" ondblclick="modifyCustomerKeyword('${customerKeyword.uuid}')" height=30>
                <td width=10 style="padding-left:7px;"><input type="checkbox" name="uuid" value="${customerKeyword.uuid}" onclick="decideSelectAll()"/></td>
                <td align="center" width=100>
                    <font color="<%--<%=keywordColor%>--%>">${customerKeyword.keyword}</font>
                </td>
                <td  align="center" width=200 class="wrap floatTd"
                     title="原始URL:${customerKeyword.originalUrl != null ?customerKeyword.originalUrl : customerKeyword.url}">
                    <div style="height:16;">
                            ${customerKeyword.url==null?'':customerKeyword.url}
                    </div>
                </td>
                <td align="center" width=150>
                        ${customerKeyword.bearPawNumber == null ? "" : customerKeyword.bearPawNumber}
                </td>
                <td align="center" width=250>
                        ${customerKeyword.title == null ? "" : customerKeyword.title.trim()}
                </td>
                <td align="center" width=30>
                    <div style="height:16;"><a
                            href="/internal/customerKeywordPositionIndexLog/historyPositionAndIndex/${customerKeyword.uuid}/30"
                            target="_blank" title="查看历史排名" class="floatTd">${customerKeyword.currentIndexCount}
                    </a></div>
                </td>
                <td align="center" width=50>
                    <div style="height:16;">${customerKeyword.initialPosition}
                    </div>
                </td>
                <td align="center" width=50 class="floatTd" title="排名采集城市: ${customerKeyword.capturePositionCity}">
                    <div style="height:16;">
                        <a href="${customerKeyword.searchEngineUrl}" target="_blank">${customerKeyword.currentPosition}</a>
                    </div>
                </td>
                <td align="center" width=50>${customerKeyword.searchEngine}</td>
                <td align="center" width=40 class="floatTd" title="优化日期：<fmt:formatDate value="${customerKeyword.optimizeDate}" pattern="yyyy-MM-dd"/> ，要刷：${customerKeyword.optimizePlanCount}，已刷：${customerKeyword.optimizedCount})">${customerKeyword.collectMethodName}</td>
                <td align="center" width=30>${customerKeyword.optimizePlanCount}</td>
                <td align="center" width=30>${customerKeyword.optimizedCount} </td>
                <td align="center" width=30>${customerKeyword.invalidRefreshCount}</td>
                <td align="center" width=60>${customerKeyword.feeString}</td>
                <td align="center" width=80><fmt:formatDate value="${customerKeyword.startOptimizedTime}" pattern="yyyy-MM-dd"/></td>
                <td align="center" width=80><fmt:formatDate value="${customerKeyword.lastOptimizeDateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td align="center" width=50>${customerKeyword.orderNumber}</td>
                <td align="center" width=100>${customerKeyword.remarks==null?"":customerKeyword.remarks} </td>
                <td align="center" width=100>${customerKeyword.enteredKeywordRemarks == null ? "" : customerKeyword.enteredKeywordRemarks}</td>
                <td align="center" width=60>${customerKeyword.optimizeGroupName == ''? "" : customerKeyword.optimizeGroupName}
                </td>
                <td align="center" width=80>
                    <shiro:hasPermission name="/internal/customerKeyword/saveCustomerKeyword">
                        <a href="javascript:modifyCustomerKeyword('${customerKeyword.uuid}', '${customerKeyword.customerUuid}')">修改</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/customerKeyword/deleteCustomerKeyword">
                        | <a href="javascript:delItem('${customerKeyword.uuid}')">删除</a>
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

<div id="groupChangeNameByRankDialog"  style="display: none;text-align: center;left: 40%;"  class="easyui-dialog">
    <form id="groupNameChangeByRankFrom" style="text-align: center;margin-top: 10px;">
       <span style="text-align: right;width: 52px;display:inline-block">分组名称:</span><input type="text" id="groupName" name="groupName" style="width:150px"><br><br>
       <span style="text-align: right;width: 52px;display:inline-block">排名:</span><input type="text" id="position" name="position" style="width:150px" value="20" onkeyup="onlyNumber(this)"><br><br>
       <span style="text-align: right;width: 52px;display:inline-block">天数:</span><input type="text" id="day" name="day" style="width:150px" value="3" onkeyup="onlyNumber(this)">
    </form>
</div>
<div id="changeSearchEngineDialog"  style="display: none;text-align: center;left: 40%;" title="修改客户关键字搜索引擎" class="easyui-dialog">
    <form id="changeSearchEngineForm" style="text-align: center;margin-top: 10px;">
        目标搜索引擎:
        <select id="searchEngineSelect" style="width:70px;">
            <option value="百度">百度</option>
            <option value="搜狗">搜狗</option>
            <option value="360">360</option>
            <option value="神马">神马</option>
        </select>
    </form>
</div>
<div id="groupChangeNameDialog"  style="display: none;text-align: center;left: 40%;" title="修改客户关键字组名" class="easyui-dialog">
    <form id="groupNameChangeFrom" style="text-align: center;margin-top: 10px;">
        目标组名称:<input type="text" id="groupName" name="groupName" style="width:150px">
    </form>
</div>
<div id="uploadExcelDailog"  style="display: none;text-align: left;height: 60px; left: 40%;" title="Excel文件上传" class="easyui-dialog">
    <form method="post" id="uploadExcelForm" style="margin-top: 10px"  enctype="multipart/form-data" >
        <input type="hidden" id="customerUuid" name="customerUuid" value="${customerKeywordCriteria.customerUuid}">
        <span>请选择要上传的文件<label id="excelType" style="color: red"></label></span>
        <div style="height: 10px;"></div>
        <input type="file" id="uploadExcelFile" name="file" >
    </form>
</div>
<div id="optimizePlanCountDialog" class="easyui-dialog" style="display: none;left: 40%;">
    <table style="font-size:12px">
        <tr></tr>
        <tr>
            <th>范围</th>
            <td>
                <input type="radio" name="range" value="all" checked />所有的
                <input type="radio" name="range" value="appoint" />指定的
            </td>
        </tr>
        <tr></tr>
        <tr>
            <th>类型</th>
            <td>
                <input type="radio" name="settingType" value="setCurrentCount" checked />按当前刷量加减
                <input type="radio" name="settingType" value="setSpecificCount" />指定刷量
            </td>
        </tr>
        <tr></tr>
        <tr>
            <th>刷量</th>
            <td>
                <input name="optimizePlanCount" id="optimizePlanCount" type="text" style="width:160px;" class="easyui-numberspinner" required value="">
            </td>
        </tr>
    </table>
</div>

<div id="customerList" title="更新关键字所属客户" class="easyui-dialog" style="left: 30%;display: none;">
    <input type="text" id="customerItem" list="customer_list"  placeholder="请选择移动到的位置" style="width:97%;margin: 5px" />
    <datalist id="customer_list">
        <c:forEach items="${customerList}" var="keyword">
            <option>${keyword.contactPerson} ${'_____'} ${keyword.uuid}</option>
        </c:forEach>
    </datalist>
</div>

<%--留言栏Dialog--%>
<div id="showUserMessageDialog" class="easyui-dialog" style="display: none">
    <form id="showUserMessageForm" onsubmit="return false">
        <table cellpadding="10" style="font-size: 12px; background-color: white; border-collapse:separate; border-spacing:0px 10px;">
            <input type="hidden" name="messageUuid" value="">
            <input type="hidden" name="openDialogStatus" value="${customerKeywordCriteria.openDialogStatus}">
            <input type="hidden" name="customerUuid" value="${customerKeywordCriteria.customerUuid}"/>
            <tr>
                <td width="60px"><span style="width: 60px">写信人:</span></td>
                <td width="80px"><span style="width: 80px;" id="senderUserName">${sessionScope.get("username")}</span></td>
                <td><span style="width: 60px">收信人:</span></td>
                <td width="180px">
                    <select id="user_select" multiple="multiple"></select>
                </td>
            </tr>
            <tr>
                <td><span style="width: 40px">时&nbsp;&nbsp;间:</span></td>
                <td width="80px"><label style="width: 80px;"></label></td>
                <td width="60px"><span style="width: 60px">客&nbsp;&nbsp;户:</span></td>
                <td><span style="width: 200px;" id="contactPerson"></span></td>
            </tr>
            <tr>
                <td><span style="width: 40px">状&nbsp;&nbsp;态:</span></td>
                <td><span style="width: 100px;" id="messageStatus"></span></td>
            </tr>
            <tr>
                <td width="60px"><span style="width: 60px">内&nbsp;&nbsp;容:</span></td>
                <td colspan="3"><input type="text" name="content" style="width: 320px;"></td>
            </tr>
        </table>
        <table id="userMessageListTable" border="1" cellpadding="10"  style="font-size: 12px;background-color: white;border-collapse: collapse;width:100%;">
            <tr>
                <td class="user-message-targetName">
                    <span>序号</span>
                </td>
                <td class="user-message-content">
                    <span>留言内容</span>
                </td>
                <td class="user-message-status">
                    <span>收信人</span>
                </td>
                <td class="user-message-status">
                    <span>处理状态</span>
                </td>
            </tr>
        </table>
    </form>
</div>

<%--记事本Dialog--%>
<div id="showUserNoteBookDialog" class="easyui-dialog" style="display: none">
    <form id="showUserNoteBookForm" onsubmit="return false">
        <div id="userNoteBookDialogToolBar">
            <input type="hidden" name="customerUuid" value="">
            &nbsp;&nbsp;<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showAddUserNoteDiv()" value=" 增加 " >
            &nbsp;&nbsp;<input class="ui-button ui-widget ui-corner-all" type="button" onclick="searchUserNoteBooks(1)" value=" 查询所有 " >
            &nbsp;&nbsp;<input class="ui-button ui-widget ui-corner-all" type="button" onclick="searchUserNoteBooks(0)" value=" 查询 " >
        </div>
        <table id="userNoteBookTable" cellpadding="10" style="font-size: 12px; background-color: white; border-collapse:separate; border-spacing:0px 8px; width: 100%;">
            <thead>
            <tr>
                <td>序号</td>
                <td>记事者</td>
                <td>日期</td>
                <td>内容</td>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        <div id="addUserNote" style="display: none;">
            <table id="addUserNoteTable" cellpadding="10" style="font-size: 12px; background-color: white; border-collapse:separate; border-spacing:0px 5px; width: 100%;">
                <tr>
                    <td>内容:</td>
                </tr>
                <tr>
                    <td>
                        <textarea style="width: 100%;"></textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        &nbsp;&nbsp;<input class="ui-button ui-widget ui-corner-all" type="button" onclick="saveUserNoteBook()" value=" 保存 " >
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>

<%@ include file="/customerkeyword/customerKeywordCommon.jsp" %>

<form id="downloadCustomerKeywordInfoForm" method="post" action="/internal/customerKeyword/downloadCustomerKeywordInfo">
    <input type="hidden" name="customerUuid" id="customerUuidHidden" value="">
    <input type="hidden" name="keyword" id="keywordHidden" value=""/>
    <input type="hidden" name="url" id="urlHidden" value="" />
    <input type="hidden" name="bearPawNumber" id="bearPawNumberHidden" value="" />
    <input type="hidden" name="status" id="statusHidden" value="" />
    <input type="hidden" name="optimizeGroupName" id="optimizeGroupNameHidden" value="" />
    <input type="hidden" name="gtOptimizedCount" id="gtOptimizedCountHidden" value="" />
    <input type="hidden" name="lgtOptimizedCount" id="ltOptimizedCountHidden" value="" />
    <input type="hidden" name="gtOptimizePlanCount" id="gtOptimizePlanCountHidden" value="" />
    <input type="hidden" name="ltOptimizePlanCount" id="ltOptimizePlanCountHidden" value="" />
    <input type="hidden" name="gtPosition" id="gtPositionHidden" value="" />
    <input type="hidden" name="ltPosition" id="ltPositionHidden" value="" />
    <input type="hidden" name="noPosition" id="noPositionHidden" value="" />
    <input type="hidden" name="displayStop" id="displayStopHidden" value="" />
    <input type="hidden" name="invalidRefreshCount" id="invalidRefreshCountHidden" value="" />
    <input type="hidden" name="searchEngine" id="searchEngineHidden" value="" />
    <input type="hidden" name="orderingElement" id="orderingElementHidden" value="" />
    <input type="hidden" name="titleFlag" id="titleFlagHidden" value="" />
    <input type="hidden" name="gtCurrentIndexCount" id="gtCurrentIndexCountHidden" value="" />
    <input type="hidden" name="ltCurrentIndexCount" id="ltCurrentIndexCountHidden" value="" />
    <input type="hidden" name="remarks" id="remarksHidden" value="" />
</form>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/customerkeyword/customerKeywordList.js"></script>
<script src="${staticPath }/customerkeyword/customerKeywordCommon.js"></script>
<script src="${staticPath }/static/UserMessageCommon.js"></script>
<script language="javascript">
    $(function () {
        initPaging();
        initNoPositionChecked();
        $("#customerKeywordDiv").css("margin-top",$("#customerKeywordTopDiv").height());
        alignTableHeader();
        window.onresize = function(){
            $("#customerKeywordDiv").css("margin-top",$("#customerKeywordTopDiv").height());
            alignTableHeader();
        }
    });
    function alignTableHeader() {
        var td = $("#customerKeywordTable tr:first td");
        var ctd = $("#headerTable tr:first td");
        $.each(td, function (idx, val) {
            ctd.eq(idx).width($(val).width());
        });
    }

    function initPaging() {
        var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
        var searchCustomerKeywordTable = searchCustomerKeywordForm.find("#searchCustomerKeywordTable");
        searchCustomerKeywordTable.find("#orderingElement").val("${orderElement == null ? '0' : orderElement}");
        searchCustomerKeywordTable.find("#titleFlag").val(${customerKeywordCriteria.titleFlag});
        searchCustomerKeywordTable.find("#searchEngine").val('${customerKeywordCriteria.searchEngine}');
        searchCustomerKeywordTable.find("#status").val(${customerKeywordCriteria.status});
        var pages = searchCustomerKeywordForm.find('#pagesHidden').val();
        var currentPageNumber = searchCustomerKeywordForm.find('#currentPageNumberHidden').val();
        var showCustomerBottomDiv = $('#showCustomerBottomDiv');
        showCustomerBottomDiv.find("#chooseRecords").val(${page.size});

        if(parseInt(currentPageNumber) > 1 && parseInt(currentPageNumber) < parseInt(pages)) {
            showCustomerBottomDiv.find("#firstButton").removeAttr("disabled");
            showCustomerBottomDiv.find("#upButton").removeAttr("disabled");
            showCustomerBottomDiv.find("#nextButton").removeAttr("disabled");
            showCustomerBottomDiv.find("#lastButton").removeAttr("disabled");
        } else if (parseInt(pages) <= 1) {
            showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
            showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
            showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
            showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
        } else if (parseInt(currentPageNumber) <= 1) {
            showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
            showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
        } else {
            showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
            showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
        }
    }
    function initNoPositionChecked() {
        if(${customerKeywordCriteria.noPosition == 1}){
            $("#noPosition").prop("checked",true);
        }else{
            $("#noPosition").prop("checked",false);
        }
        noPositionValue();
    }
    //显示排名为0
    function noPositionValue() {
        if($("#noPosition").is(":checked")){
            $("#noPosition").val("1")
        }else {
            $("#noPosition").val("0");
        }
    }
</script>
</body>
</html>
