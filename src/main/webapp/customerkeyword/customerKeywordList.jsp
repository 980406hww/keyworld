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
        </style>
        <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
    </head>
<body>
<%@include file="/commons/basejs.jsp" %>
<div id="customerKeywordTopDiv" >
    <%@include file="/menu.jsp" %>
            <table width=100% style="border:1px solid #000000;font-size:12px;margin-top: 30px" cellpadding=3>
                <tr border="1" height=30>
                    <td width=250>联系人: ${customer.contactPerson}</td>
                    <td width=200>QQ: ${customer.qq}</td>
                    <td width=200>电话: ${customer.telphone}</td>
                    <td width=120>关键字数: ${customer.keywordCount}</td>
                    <td width=250>创建时间: <fmt:formatDate value="${customer.createTime}" pattern=" yyyy-MM-dd HH:mm"/></td>
                </tr>
            </table>

    <div style="text-align: right;margin-bottom: 5px">
        <div style="margin-bottom: 5px">
    <shiro:hasPermission name="/internal/customerKeyword/uploadCustomerKeywords">
        <a target="_blank" href="javascript:uploadCustomerKeywords('${customerKeywordCriteria.customerUuid}', 'SuperUserSimple')"/>Excel上传(简化版)</a>
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
            指数:<input type="text" id="gtCurrentIndexCount" name="gtCurrentIndexCount" placeholder=">=" value="${customerKeywordCriteria.gtCurrentIndexCount}" style="width: 40px;">
            <input type="text" id="ltCurrentIndexCount" name="ltCurrentIndexCount" placeholder="<=" value="${customerKeywordCriteria.ltCurrentIndexCount}" style="width: 40px;">
            备注:<input type="text" id="remarks" name="remarks" value="${customerKeywordCriteria.remarks}" style="width: 90px;">
            <%--</c:if>--%>
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
        </div>
    </form>
    <%--</c:if>--%>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td width=10 style="padding-left:7px;"><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=100>关键字</td>
            <td align="center" width=200>URL</td>
            <td align="center" width=250>标题</td>
            <td align="center" width=30>指数</td>
            <td align="center" width=50>原排名</td>
            <td align="center" width=50>现排名</td>
            <td align="center" width=50>搜索引擎</td>
            <td align="center" width=30>计价方式</td>
            <td align="center" width=30>要刷</td>
            <td align="center" width=30>已刷</td>
            <td align="center" width=30>无效</td>
            <td align="center" width=60>报价</td>
            <td align="center" width=80>开始优化日期</td>
            <td align="center" width=80>最后优化时间</td>
            <td align="center" width=50>订单号</td>
            <td align="center" width=100>备注</td>
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
                <td align="center" width=50>
                    <div style="height:16;">
                        <a href="${customerKeyword.searchEngineUrl}" target="_blank">${customerKeyword.currentPosition}</a>
                    </div>
                </td>
                <td align="center" width=50>${customerKeyword.searchEngine}</td>
                <td align="center" width=30 class="floatTd" title="优化日期：<fmt:formatDate value="${customerKeyword.optimizeDate}" pattern="yyyy-MM-dd"/> ，要刷：${customerKeyword.optimizePlanCount}，已刷：${customerKeyword.optimizedCount})">${customerKeyword.collectMethodName}</td>
                <td align="center" width=30>${customerKeyword.optimizePlanCount}</td>
                <td align="center" width=30>${customerKeyword.optimizedCount} </td>
                <td align="center" width=30>${customerKeyword.invalidRefreshCount}</td>
                <td align="center" width=60>${customerKeyword.feeString}</td>
                <td align="center" width=80><fmt:formatDate value="${customerKeyword.startOptimizedTime}" pattern="yyyy-MM-dd"/></td>
                <td align="center" width=80><fmt:formatDate value="${customerKeyword.lastOptimizeDateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td align="center" width=50>${customerKeyword.orderNumber}</td>
                <td align="center" width=100>${customerKeyword.remarks==null?"":customerKeyword.remarks} </td>
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
    </select>
    </div>
</div>
<%--Dialog部分--%>
<div id="groupChangeNameByRankDialog"  style="text-align: center;left: 40%;"  class="easyui-dialog">
    <form id="groupNameChangeByRankFrom" style="text-align: center;margin-top: 10px;">
       <span style="text-align: right;width: 52px;display:inline-block">分组名称:</span><input type="text" id="groupName" name="groupName" style="width:150px"><br><br>
       <span style="text-align: right;width: 52px;display:inline-block">排名:</span><input type="text" id="position" name="position" style="width:150px" value="20" onkeyup="onlyNumber(this)"><br><br>
       <span style="text-align: right;width: 52px;display:inline-block">天数:</span><input type="text" id="day" name="day" style="width:150px" value="3" onkeyup="onlyNumber(this)">
    </form>
</div>
<div id="changeSearchEngineDialog"  style="text-align: center;left: 40%;" title="修改客户关键字搜索引擎" class="easyui-dialog">
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
<div id="groupChangeNameDialog"  style="text-align: center;left: 40%;" title="修改客户关键字组名" class="easyui-dialog">
    <form id="groupNameChangeFrom" style="text-align: center;margin-top: 10px;">
        目标组名称:<input type="text" id="groupName" name="groupName" style="width:150px">
    </form>
</div>
<div id="uploadExcelDailog"  style="text-align: left;height: 60px; left: 40%;" title="Excel文件上传" class="easyui-dialog">
    <form method="post" id="uploadExcelForm" style="margin-top: 10px"  enctype="multipart/form-data" >
        <input type="hidden" id="customerUuid" name="customerUuid" value="${customerKeywordCriteria.customerUuid}">
        <span>请选择要上传的文件<label id="excelType" style="color: red"></label></span>
        <div style="height: 10px;"></div>
        <input type="file" id="uploadExcelFile" name="file" >
    </form>
</div>
<div id="optimizePlanCountDialog" class="easyui-dialog" style="left: 40%;">
    <table style="font-size:12px">
        <tr>
            <th>类型</th>
            <td>
                <input type="radio" name="settingType" value="setCurrentCount" checked />按当前刷量加减
                <input type="radio" name="settingType" value="setSpecificCount" />指定刷量
            </td>
        </tr>
        <tr>
            <th>刷量</th>
            <td>
                <input name="optimizePlanCount" id="optimizePlanCount" type="text" style="width:160px;" value="">
            </td>
        </tr>
    </table>
</div>
<div id="saveCustomerKeywordDialog" class="easyui-dialog" style="left: 35%;">
    <form id="customerKeywordForm">
        <ul>
            <input type="hidden" id="uuid" value="" style="width:300px;">
            <li><span class="customerKeywordSpanClass">关键字:</span><input type="text" name="keyword" id="keyword" value="" style="width:300px;"/></li>

            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            <li><span class="customerKeywordSpanClass">标题:</span><input type="text" name="title" id="title" value="" style="width:300px;">
            </li>
            <li><span class="customerKeywordSpanClass">域名:</span><input type="text" name="url" id="url" value="" style="width:300px;">
            </li>
            <li><span class="customerKeywordSpanClass">原始域名:</span><input type="text" name="originalUrl" id="originalUrl" value="" style="width:300px;">
            </li>
            <li><span class="customerKeywordSpanClass">要刷数量:</span><input type="text" name="optimizePlanCount" id="optimizePlanCount" value="" style="width:300px;">
            </li>
            <li><span class="customerKeywordSpanClass">优化组名:</span>
                <input name="optimizeGroupName" id="optimizeGroupName" type="text"
                       style="width:115px;" value="">

                指数:<input type="text" id="initialIndexCount" size="5" name="initialIndexCount" value="100" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">
                排名:<input type="text" id="initialPosition" size="5" name="initialPosition" value="10" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)" >

            </li>
            <li>
                <ul style="float: left">
                    <li>
                        <span class="customerKeywordSpanClass">第一报价:</span><input name="positionFirstFee" id="positionFirstFee" value=""
                                                                                  style="width:100px;"
                                                                                  type="text" onkeyup="onlyNumber(this)" onblur="setSecondThirdDefaultFee(this)">元 </li>
                    <li><span class="customerKeywordSpanClass">第二报价:</span><input name="positionSecondFee"
                                                                                  id="positionSecondFee" value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li>
                    <li><span class="customerKeywordSpanClass">第三报价:</span><input name="positionThirdFee" id="positionThirdFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="setThirdDefaultFee(this)">元
                    </li>
                    <li><span class="customerKeywordSpanClass">第四报价:</span><input name="positionForthFee" id="positionForthFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="setForthDefaultFee(this)">元
                    </li>
                    <li><span class="customerKeywordSpanClass">第五报价:</span><input name="positionFifthFee" id="positionFifthFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li>
                    <li><span class="customerKeywordSpanClass">首页报价:</span><input name="positionFirstPageFee" id="positionFirstPageFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li></ul>


                    <ul id="customerKeywordCost" style="float: left; width: 200px;height:146px;text-align: center">
                        <li><a href="javascript:showCustomerKeywordCost()">&nbsp;显示成本(再次点击关闭)</a></li>
                        <ul id="customerKeywordCostFrom" style="display: none;">
                            <li><span class="customerKeywordSpanClass">第一成本:</span><input name="positionFirstCost" id="positionFirstCost"
                                                                                          onBlur="setSecondThirdDefaultCost();" value=""
                                                                                          style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元 </li>
                            <li><span class="customerKeywordSpanClass">第二成本:</span><input name="positionSecondCost" id="positionSecondCost"
                                                                                          onBlur="setThirdDefaultCost();" value=""
                                                                                          style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                            <li><span class="customerKeywordSpanClass">第三成本:</span><input name="positionThirdCost" id="positionThirdCost"
                                                                                          value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                            <li><span class="customerKeywordSpanClass">第四成本:</span><input name="positionForthCost" id="positionForthCost"
                                                                                          value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                            <li><span class="customerKeywordSpanClass">第五成本:</span><input name="positionFifthCost" id="positionFifthCost"
                                                                                          value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                        </ul>
                    </ul>
            </li>
            <li style="float:left"><span class="customerKeywordSpanClass">服务提供商:</span>
                <select name="serviceProvider" id="serviceProvider" style="width: 296px">
                    <c:forEach items="${serviceProviders}" var="serviceProvider">
                        <option value="${serviceProvider.serviceProviderName}" <c:if test="${serviceProvider.serviceProviderName=='baidutop123'}">selected="selected"</c:if>>${serviceProvider.serviceProviderName}</option>
                    </c:forEach>
                </select></li>

            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            <li style="float:left"><span class="customerKeywordSpanClass">收费方式:</span>
                <select name="collectMethod" id="collectMethod" onChange="setEffectiveToTime()">
                    <option value="PerMonth" selected>按月</option>
                    <option value="PerTenDay">十天</option>
                    <option value="PerSevenDay">七天</option>
                    <option value="PerDay">按天</option>
                </select>
                搜索引擎:
                <select name="searchEngine" id="searchEngine" onChange="searchEngineChanged()" >
                    <option value="百度" selected>百度</option>
                    <option value="搜狗">搜狗</option>
                    <option value="360">360</option>
                    <option value="神马">神马</option>
                </select>
                <input type="hidden" id="status" name="status" value="1">
                排序:<input type="text" name="sequence" id="sequence" value="0" size="6" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">
            </li>
            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            <li style="float:left">
                <span class="customerKeywordSpanClass">订单号:</span><input name="orderNumber" id="orderNumber" type="text" style="width:120px;" />
                <span class="customerKeywordSpanClass">收费状态:</span><select name="paymentStatus" id="paymentStatus">
                <option value="0"></option>
                <option value="1">担保中</option>
                <option value="2">已付费</option>
                <option value="3">未担保</option>
                <option value="4">跑路</option>
            </select>
            </li>
            <li style="float:left;">
                <span style="text-align: right;margin-left: 30px;">
                <input type="radio" id="clickPositiveUrl" name="clickUrl" value="clickPositiveUrl">点击正面链接
                <input type="radio" id="clickCommonUrl" name="clickUrl" value="clickCommonUrl">点击非负面清单链接
                </span>
            </li>
            <li style="float:left">
                <span style="text-align: right;margin-left: 30px;">
                <input type="checkbox" id="operateSelectKeyword" name="operateSelectKeyword" value="operateSelectKeyword">下拉词
                <input type="checkbox" id="operateRelatedKeyword" name="operateRelatedKeyword" value="operateRelatedKeyword">相关词
                <input type="checkbox" id="operateRecommendKeyword" name="operateRecommendKeyword" value="operateRecommendKeyword">推荐词
                <input type="checkbox" id="operateSearchAfterSelectKeyword" name="operateSearchAfterSelectKeyword" value="operateSearchAfterSelectKeyword">搜索后下拉词
                </span>
            </li>
            <li style="float:left"><span class="customerKeywordSpanClass">推荐词:</span><input type="text" name="recommendKeywords" id="recommendKeywords" value="" style="width:300px;"></li>
            <li style="float:left"><span class="customerKeywordSpanClass">负面词:</span><input type="text" name="negativeKeywords" id="negativeKeywords" value="" style="width:300px;"></li>
            <li style="float:left"><span class="customerKeywordSpanClass">排除词:</span><input type="text" name="excludeKeywords" id="excludeKeywords" value="" style="width:300px;"></li>
            <li style="float:left"><span class="customerKeywordSpanClass">展现页码:</span><input type="text" name="showPage" id="showPage" value="" placeholder="2,3,4,5" style="width:300px;"></li>
            <li style="float:left"><span style="text-align: right;margin-left: 5px;">普通相关词占比:</span>
                <input type="text" name="relatedKeywordPercentage" id="relatedKeywordPercentage" class="easyui-numberspinner" data-options="min:0,max:100,increment:10" style="width:260px;">%
            </li>
            <li style="float:left">
                <span class="customerKeywordSpanClass" style="display: inline-block;float: left;height: 80px;">备注:</span><textarea name="remarks" id="remarks" style="width:300px;height:80px;resize: none" placeholder="请写备注吧!"></textarea>
            </li>
        </ul>
    </form>
</div>

<form id="downloadCustomerKeywordInfoForm" method="post" action="/internal/customerKeyword/downloadCustomerKeywordInfo">
    <input type="hidden" name="customerUuid" id="customerUuidHidden" value="">
    <input type="hidden" name="keyword" id="keywordHidden" value=""/>
    <input type="hidden" name="url" id="urlHidden" value="" />
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
    <input type="hidden" name="gtCurrentIndexCount" id="gtCurrentIndexCountHidden" value="" />
    <input type="hidden" name="ltCurrentIndexCount" id="ltCurrentIndexCountHidden" value="" />
    <input type="hidden" name="remarks" id="remarksHidden" value="" />
</form>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/customerkeyword/customerKeywordList.js"></script>
<script language="javascript">
    $(function () {
        initPaging();
        initNoPositionChecked();//初始化排名为0的初始值
    });

    function initPaging() {
        var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
        var searchCustomerKeywordTable = searchCustomerKeywordForm.find("#searchCustomerKeywordTable");
        searchCustomerKeywordTable.find("#orderingElement").val("${orderElement == null ? '0' : orderElement}");
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
