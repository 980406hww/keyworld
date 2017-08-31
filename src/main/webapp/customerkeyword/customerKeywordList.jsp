<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@page contentType="text/html;charset=utf-8" %>

    <script language="javascript" type="text/javascript" src="/common.js"></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script language="javascript" type="text/javascript" src="/js/slide1.12.4.js"></script>
    <link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link href="/css/menu.css" rel="stylesheet" type="text/css"/>

    <head>
        <title>关键字列表</title>
        <style>
            .wrap {
                word-break: break-all;
                word-wrap: break-word;
            }

            <!--
            #div1 {
                display: none;
                background-color: #f6f7f7;
                color: #333333;
                font-size: 12px;
                line-height: 18px;
                border: 1px solid #e1e3e2;
                width: 450px;
                height: 50px;
            }

            #div2 {
                display: none;
                background-color: #ACF106;
                color: #E80404;
                font-size: 20px;
                line-height: 18px;
                border: 2px solid #104454;
                width: 100px;
                height: 22px;
            }

            #changeOptimizationGroupDialog {
                display: none;
                margin: -30px 0px 0px -130px;
                background-color: white;
                color: #2D2A2A;
                font-size: 12px;
                line-height: 12px;
                border: 2px solid #104454;
                width: 260px;
                height: 60px;
                left: 50%;
                top: 50%;
                z-index: 25;
                position: fixed;
            }
            #showCustomerBottomDiv {
                margin-right: 2%;
                float: right;
                width: 580px;
            }
        </style>

        <script language="javascript">
            function selectAll(self) {
                var a = document.getElementsByName("uuid");
                if (self.checked) {
                    for (var i = 0; i < a.length; i++) {
                        if (a[i].type == "checkbox") {
                            a[i].checked = true;
                        }
                    }
                } else {
                    for (var i = 0; i < a.length; i++) {
                        if (a[i].type == "checkbox") {
                            a[i].checked = false;
                        }
                    }
                }
            }

            function doOver(obj) {
                obj.style.backgroundColor = "green";
            }

            function doOut(obj) {
                var rowIndex = obj.rowIndex;
                if ((rowIndex % 2) == 0) {
                    obj.style.backgroundColor = "#eeeeee";
                } else {
                    obj.style.backgroundColor = "#ffffff";
                }
            }
            function delItem(uuid) {
                if (confirm("确实要删除这个关键字吗?") == false) return;
                document.location = "delete.jsp?uuid=" + uuid + "&customerUuid=${customerUuid}" + "&pageUrl=${pageUrl}";
            }
            function delAllItems(deleteType) {
                if (confirm("确实要删除这些关键字吗?") == false) return;
                var tmpUrl = '/customerkeyword/deleteAll.jsp?';
                if (deleteType == null) {
                    var uuids = getUuids();
                    if (uuids.trim() === '') {
                        alert("请选中要操作的关键词！");
                        return;
                    }
                    <%--tmpUrl = tmpUrl + "uuids=" + uuids + "&customerUuid=<%=customerUuid%>";--%>
                } else {
                    if (confirm("确实要删除标题和网址为空关键字吗?") == false) return;
                    <%--tmpUrl = tmpUrl + "deleteType=" + deleteType + "&customerUuid=<%=customerUuid%>";--%>
                }

                $.ajax({
                    url: tmpUrl,
                    type: 'GET',
                    success: function (data) {
                        data = data.replace(/\r\n/gm, "");
                        data = data.replace(/\n/gm, "");
                        if (data === "1") {
                            showInfo("删除成功！", self);
                            window.location.reload();
                        } else {
                            showInfo("删除失败！", self);
                        }
                    },
                    error: function () {
                        showInfo("删除失败！", self);
                    }
                });

            }

            function getUuids() {
                var a = document.getElementsByName("uuid");
                var uuids = '';
                for (var i = 0; i < a.length; i++) {
                    //alert(a[i].value);
                    if (a[i].checked) {
                        if (uuids === '') {
                            uuids = a[i].value;
                        } else {
                            uuids = uuids + ',' + a[i].value;
                        }
                    }
                }
                return uuids;
            }

            function resetTitle() {
                if (confirm("确实要清除当前结果的采集标题标志?") == false) return;
                <%--document.location = "resetTitle.jsp?<%=pageUrl%>";--%>
            }

            function clearTitle(customerUuid, clearType) {
                if (confirm("确认要清空标题吗?") == false) return;
                var uuids = getUuids();
                if (clearType == null) {
                    if (uuids.trim() === '') {
                        alert("请选中要操作的关键词！");
                        return;
                    }
                }
                var postData = {};
                postData.uuids = uuids;
                postData.clearType = clearType;
                postData.customerUuid = customerUuid;

                $.ajax({
                    url: '/internal/customerkeyword/clearTitle',
                    data: JSON.stringify(postData),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (status) {
                        if (status) {
                            showInfo("操作成功！", self);
                            window.location.reload();
                        } else {
                            showInfo("操作失败！", self);
                        }
                    },
                    error: function () {
                        showInfo("操作失败！", self);
                    }
                });
            }

            function showChangeOptimizationGroupDialog(self) {
                var settingDialogDiv = $$$("#changeOptimizationGroupDialog");
                settingDialogDiv.find("#optimizationGroup").val("");
                settingDialogDiv.show();
                settingDialogDiv.find("#optimizationGroup").focus();
            }
            function saveChangeOptimizationGroup(self, closable) {
                var settingDialogDiv = $$$("#changeOptimizationGroupDialog");
                var vpsOpenSettingVO = {};
                var optimizationGroup = settingDialogDiv.find("#optimizationGroup").val();

                var uuids = getUuids();
                if (uuids.trim() === '') {
                    alert("请选中要操作的关键词！");
                    return;
                }

                if (optimizationGroup.trim() === '') {
                    alert("请输入分组名称！");
                    return;
                }

                $.ajax({
                    url: '/customerkeyword/changeCustomerKeywordGroup.jsp?uuids=' + uuids + "&group=" + optimizationGroup,
                    type: 'GET',
                    success: function (data) {
                        data = data.replace(/\r\n/gm, "");
                        if (closable) {
                            settingDialogDiv.hide();
                        }
                        if (data === "1") {
                            showInfo("更新成功！", self);
                            if (closable) {
                                window.location.reload();
                            } else {
                                settingDialogDiv.find("#providerId").val("");
                            }
                        } else {
                            showInfo("更新失败！", self);
                        }
                    },
                    error: function () {
                        showInfo("更新失败！", self);
                        if (closable) {
                            settingDialogDiv.hide();
                        }
                    }
                });
            }
            function cancelChangeOptimizationGroup() {
                var settingDialogDiv = $$$("#changeOptimizationGroupDialog");
                settingDialogDiv.hide();
            }

            function showInfo(content, e) {
                e = e || window.event;
                var div1 = document.getElementById('div2'); //将要弹出的层
                div1.innerText = content;
                div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见
                div1.style.left = getLeft(e) + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
                div1.style.top = getTop(e) + 5;
                div1.style.position = "absolute";

                var intervalID = setInterval(function () {
                    div1.style.display = "none";
                }, 3000);
            }

            function getTop(e) {
                var offset = e.offsetTop;
                if (e.offsetParent != null) offset += getTop(e.offsetParent);
                return offset;
            }
            //获取元素的横坐标
            function getLeft(e) {
                var offset = e.offsetLeft;
                if (e.offsetParent != null) offset += getLeft(e.offsetParent);
                return offset;
            }

            function showTip(content, e) {
                var event = e || window.event;
                var pageX = event.pageX;
                var pageY = event.pageY;
                if (pageX == undefined) {
                    pageX = event.clientX + document.body.scrollLeft || document.documentElement.scrollLeft;
                }
                if (pageY == undefined) {
                    pageY = event.clientY + document.body.scrollTop || document.documentElement.scrollTop;
                }
                var div1 = document.getElementById('div1'); //将要弹出的层
                div1.innerText = content;
                div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见
                div1.style.left = pageX + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
                div1.style.top = pageY + 5;
                div1.style.position = "absolute";
            }

            //关闭层div1的显示
            function closeTip() {
                var div1 = document.getElementById('div1');
                div1.style.display = "none";
            }
        </script>
    </head>
<body>
<div id="customerKeywordTopDiv">
    <table width=100% style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=18 align="left">
                <%@include file="/menu.jsp" %>
            </td>
        </tr>
        <tr>
            <table width=100%  style= "border:1px solid #000000;font-size:12px;" cellpadding=3>
                <tr border="1" height=30>
                    <td width=250>联系人: ${customer.contactPerson}</td>
                    <td width=200>QQ: ${customer.qq}</td>
                    <td width=200>电话: ${customer.telphone}</td>
                    <td width=120>关键字数: ${customer.keywordCount}</td>
                    <td width=250>创建时间: <fmt:formatDate value="${customer.createTime}" pattern=" yyyy-MM-dd HH:mm"/></td>
                </tr>
            </table>
        </tr>
    </table>
        <tr>
            <td colspan=18 align="right"><a
                href="updateCustomerKeywordGroupName.jsp?customerUuid=${customerUuid}">修改该用户关键字组名</a>
            | <a href="javascript:offCustomerKeyword('${customerUuid}')">下架客户关键字</a>
            | <a href="javascript:addCustomerKeyword('${customerUuid}')">增加关键字</a>
            | <a target="_blank" href="javascript:uploadsimple('${customerUuid}')"/>">关键字Excel上传(简化版)</a>
            | <a target="_blank" href="/SuperUserSimpleKeywordList.xls">简化版模板下载</a>
            | <a target="_blank" href="javascript:uploadfull('${customerUuid}')">关键字Excel上传(完整版)</a>
            | <a target="_blank" href="/SuperUserFullKeywordList.xls">完整版模板下载</a>
            | <a target="_blank" href="javascript:uploaddailyreporttemplate('${customerUuid}')">上传日报表模板</a>
            | <a target="_blank" href="javascript:downloadSingleCustomerReport('${customerUuid}')">导出日报表</a>
            | <a target="_blank"
                 href='/customerkeyword/DownloadCustomerKeywordInfo.jsp?fileName=CustomerKeywordInfo<%--<%="_" + Utils.getCurrentDate()%>.xls&<%=pageUrl%>--%>'>导出结果</a>
        </td>
        </tr>
        <tr>
            <td colspan=18>
                <form method="post" action="list.jsp">
                    <table style="font-size:12px;">
                        <tr>

                            <td align="right">关键字:</td>
                            <td><input type="text" name="keyword" id="keyword" value="${customerKeywordVO.keyword}"
                                       style="width:90px;">
                            </td>
                            <td align="right">URL:</td>
                            <td><input type="text" name="url" id="url" value="${customerKeywordVO.url}"
                                       style="width:120px;"></td>
                            <td>
                                <input id="customerUuid" name="customerUuid" type="hidden"
                                       value="${customerKeywordVO.customerUuid} ">
                            </td>
                            <td align="right">添加时间:<input name="creationFromTime" id="creationFromTime" class="Wdate"
                                                          type="text" style="width:90px;" onClick="WdatePicker()"
                                                          value="${customerKeywordVO.creationFromTime} ">到<input
                                    name="creationToTime"
                                    id="creationToTime"
                                    class="Wdate" type="text"
                                    style="width:90px;"
                                    onClick="WdatePicker()"
                                    value="${customerKeywordVO.creationToTime} ">
                            </td>
                            <td align="right">关键字状态:</td>
                            <td>
                                <select name="status" id="status">
                                    <%--<%
                                        String[] statusNames = {"", "新增", "激活", "过期"};
                                        String[] statusValues = {"", "2", "1", "0"};
                                        for (int i = 0; i < statusNames.length; i++) {
                                        /*if (${statusValues[i].equals(customerKeywordVO.status)}) {
                                            out.println("<option selected value='" + statusValues[i] + "'>" + statusNames[i] + "</option>");
                                        } else {
                                            out.println("<option value='" + statusValues[i] + "'>" + statusNames[i] + "</option>");
                                        }*/
                                        }
                                    %>--%>
                                </select>
                            </td>
                            <td align="right">优化组名:</td>
                            <td><input type="text" name="optimizeGroupName" id="optimizeGroupName"
                                       value="${customerKeywordVO.optimizeGroupName} " style="width:60px;"></td>

                            <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery"
                                                                 value=" 查询 "></td>
                        </tr>
                    </table>
                    <c:if test="${!user.vipType}">
                        <table style="font-size:12px;">
                            <tr>

                                <td align="right">显示前:</td>
                                <td><input type="text" name="position" id="position" value="${user.position}"
                                           style="width:20px;">
                                </td>
                                <td align="right">排序:</td>
                                <td>
                                    <select name="orderElement" id="orderElement">
                                            <%-- <%
                                                 String[] orderElements = {"创建日期", "当前排名", "账单日期"};
                                                 for (int i = 0; i < orderElements.length; i++) {
                                                     if (orderElements[i].equals(${customerKeywordVO.keyword}orderElement)) {
                                                         out.println("<option selected value='" + orderElements[i] + "'>" + orderElements[i] + "</option>");
                                                     } else {
                                                         out.println("<option value='" + orderElements[i] + "'>" + orderElements[i] + "</option>");
                                                     }
                                                 }
                                             %>--%>
                                    </select>
                                </td>
                                <td align="right">无效点击数:</td>
                                <td><input type="text" name="invalidRefreshCount" id="invalidRefreshCount"
                                           value="${customerKeywordVO.invalidRefreshCount} " style="width:160px;"></td>
                                <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery"
                                                                     value=" 查询 ">
                                </td>
                                <td align="right"><a href="javascript:delAllItems(null)">删除所选关键字</a> |
                                    <a href="javascript:delAllItems('emptyTitleAndUrl')">删除标题和网址为空的关键字</a> |
                                    <a href="javascript:delAllItems('emptyTitle')">删除标题为空的关键字</a> |
                                    <a href="javascript:resetTitle()">清空结果采集标题标志</a> |
                                    <a href="javascript:clearTitle('${customerKeywordVO.customerUuid}', null)">清空所选关键字标题</a>
                                    |
                                    <a href="javascript:clearTitle('${customerKeywordVO.customerUuid}', 'all')">清空当前客户下关键字标题</a>
                                    |
                                    <a target="_blank" href="javascript:showChangeOptimizationGroupDialog(this)">修改选中关键词分组</a>
                                </td>
                            </tr>
                        </table>
                    </c:if>
                </form>
            </td>
        </tr>
    </table>
</div>
    <hr>
    <div id="customerKeywordDiv">
        <table>
            <tr bgcolor="#eeeeee" height=30>
                <td align="center" width=10><input type="checkbox" onclick="selectAll(this)"/></td>
                <td align="center" width=100>关键字</td>
                <td align="center" width=200>URL</td>
                <td align="center" width=250>标题</td>
                <td align="center" width=30>指数</td>
                <td align="center" width=50>原排名</td>
                <td align="center" width=50>现排名</td>
                <td align="center" width=30>计价方式</td>
                <td align="center" width=30>要刷</td>
                <td align="center" width=30>已刷</td>
                <td align="center" width=30>无效</td>
                <td align="center" width=60>报价</td>
                <td align="center" width=80>开始优化日期</td>
                <td align="center" width=80>最后优化时间</td>
                <td align="center" width=50>订单号</td>
                <td align="center" width=100>备注</td>
                <c:choose>
                    <c:when test="${user.vipType}">
                        <td align="center" width=60>优化组名</td>
                        <td align="center" width=80>操作</td>
                    </c:when>
                    <c:when test="${user.userLevel == 1}">
                        <td align="center" width=80>操作</td>
                    </c:when>
                </c:choose>
                <div id="div1"></div>
                <div id="div2"></div>
            </tr>
            <c:forEach items="${page.records}" var="customerKeyword">
                <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                    <td><input type="checkbox" name="uuid" value="${customerKeyword.uuid}"/></td>
                    <td>
                        <font color="<%--<%=keywordColor%>--%>">${customerKeyword.keyword}
                        </font>
                    </td>
                    <td class="wrap"
                        onMouseMove="showTip('原始URL:${customerKeyword.originalUrl != null ?customerKeyword.originalUrl : customerKeyword.url}')"
                        onMouseOut="closeTip()">
                        <div style="height:16;">
                               <%-- ${isNullOrEmpty(customerKeyword.url)? '' : '' };--%>
                        </div>
                    </td>

                    <td>
                            ${customerKeyword.title == null ? "" : customerKeyword.title.trim()}
                    </td>

                    <td>
                        <div style="height:16;"><a
                                href="/customerkeyword/historyPositionAndIndex.jsp?type=PC&uuid=${customerKeyword.uuid}>"
                                target="_blank">${customerKeyword.currentIndexCount}
                        </a></div>
                    </td>

                    <td>
                        <div style="height:16;">${customerKeyword.initialPosition}
                        </div>
                    </td>

                    <td>
                        <div style="height:16;"><%--<a
                                href="${customerKeyword.searchEngineUrl}${customerKeyword.Keyword}&pn=&lt;%&ndash;<%=Utils.prepareBaiduPageNumber(customerKeyword.CurrentPosition())%>&ndash;%&gt;"
                                target="_blank">${customerKeyword.currentPosition}
                        </a>--%></div>
                    </td>

                    <td> <%--onMouseMove="showTip('优化日期：<fmt:formatDate value="${customer.optimizeDate}"pattern="yyyy-MM-dd"/> ，要刷：${customerKeyword.OptimizePlanCount}，已刷：${customerKeyword.optimizedCount}')"
                        onMouseOut="closeTip()">${customerKeyword.collectMethodName}--%>
                    </td>

                    <td>${customerKeyword.optimizePlanCount}
                    </td>
                    <td>${customerKeyword.optimizedCount}
                    </td>
                    <td>${customerKeyword.invalidRefreshCount}
                    </td>

                    <td>${value.feeString}
                    </td>
                    <td><fmt:formatDate value="${customerKeyword.startOptimizedTime}" pattern="yyyy-MM-dd"/></td>
                    <td><fmt:formatDate value="${customerKeyword.lastOptimizeDateTime}" pattern="yyyy-MM-dd  HH:mm"/></td>

                    <td>${customerKeyword.orderNumber}
                    </td>
                    <td>${customerKeyword.remarks}
                    </td>
                    <c:choose>
                        <c:when test="${user.vipType}">
                            <td>${customerKeyword.optimizeGroupName == null ? "" : customerKeyword.optimizeGroupName}
                            </td>
                            <td>
                                <a href="modify.jsp?uuid=${customerKeyword.uuid}">修改</a> |
                                <a href="javascript:delItem('${customerKeyword.uuid}')">删除</a>
                            </td>
                        </c:when>
                        <c:when test="${user.userLevel==1}">
                            <td>
                                <a href="modify.jsp?uuid=${customerKeyword.uuid}">修改</a> |
                                <a href="javascript:delItem('${customerKeyword.uuid}')">删除</a>
                            </td>
                        </c:when>
                    </c:choose>
                </tr>
            </c:forEach>
            <tr>
                <td colspan=18 align="right">
                    <br>
                    <a href="javascript:delAllItems()">删除所选关键字</a>
                </td>
            </tr>
            <tr>
                <td colspan=18>
                    <br>
                </td>
            </tr>
        </table>
    </div>


    <%--<div style="display:none;">
        <script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>
    </div>--%>
    <div id="changeOptimizationGroupDialog">
        <table style="font-size:12px">
            <tr>
                <th>分组名称</th>
                <td>
                    <input type="text" name="optimizationGroup" id="optimizationGroup" style="width:200px"/>
                </td>
            </tr>

            <tr>
                <td colspan="2" align="right">
                    <input type="button" value="保存" id="saveChangeOptimizationGroup"
                           onClick="saveChangeOptimizationGroup(this, true)"/>
                    &nbsp;&nbsp;&nbsp;<input
                        type="button" onClick="cancelChangeOptimizationGroup()" id="cancelChangeOptimizationGroup"
                        value="取消"/>
                </td>
            </tr>
        </table>
    </div>
<hr>
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
    <option>15</option>
    <option>30</option>
    <option>45</option>
</select>
</div>
</body>
</html>
