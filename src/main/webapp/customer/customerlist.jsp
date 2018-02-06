<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style type="text/css">
        #showCustomerTableDiv {
            width: 100%;
            margin: auto;
        }
        #showCustomerTable tr:nth-child(odd){background:#EEEEEE;}

        #showCustomerTable td{
            text-align: left;
        }
        #chargeTypeCalculationDiv input {
            font-size: 12px;
            width: 70px;
            height: 20px;
            margin-top: 10px;
        }
        h6{ margin: 0 5px;}
    </style>
    <title>客户列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px; margin-top:30px" cellpadding=3>
        <tr>
            <td colspan=13>
                <form method="post" id="searchCustomerForm" action="/internal/customer/searchCustomers" style="margin-bottom:0px ">
                    <table style="font-size:12px;">
                        <tr>

                            <td align="right">联系人:</td>
                            <td><input type="text" name="contactPerson" id="contactPerson"
                                       value="${customerCriteria.contactPerson}"
                                       style="width:160px;"></td>
                            <td>分组:<input type="text" name="type" id="type" value="${customerCriteria.type}" style="width: 160px;"></td>
                            <td align="right">QQ:</td>
                            <td><input type="text" name="qq" id="qq" value="${customerCriteria.qq}"
                                       style="width:160px;"></td>
                            <td align="right">联系电话:</td>
                            <td><input type="text" name="telphone" id="telphone" value="${customerCriteria.telphone}"
                                       style="width:160px;">
                            </td>
                            <td>
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
                            </td>
                            <td>
                                备注:<input type="text" value="${customerCriteria.remark}" id="remark" name="remark" style="width: 90px;">
                            </td>
                            <td align="right">
                                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                                <shiro:hasPermission name="/internal/customer/searchCustomers">
                                    &nbsp;&nbsp;<input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">
                                </shiro:hasPermission>
                            </td>
                            <td align="right">
                                <shiro:hasPermission name="/internal/customer/saveCustomer">
                                    &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showCustomerDialog(null,'${user.loginName}')"/>
                                </shiro:hasPermission>
                            </td>
                            <td align="right">
                                <shiro:hasPermission name="/internal/customer/deleteCustomers">
                                    &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteCustomers(this)"/>
                                </shiro:hasPermission>
                            </td>
                            <td align="right" width="100">
                                <c:if test="${'bc'.equalsIgnoreCase(entryType)}">
                                    <shiro:hasPermission name="/internal/dailyReport/triggerReportGeneration">
                                       <input type="button" class="ui-button ui-widget ui-corner-all" value=" 触发日报表生成 " onclick="triggerDailyReportGeneration(this)"/>
                                    </shiro:hasPermission>
                                </c:if>
                            </td>
                            <td>&nbsp;&nbsp;<span id="dailyReportSpan"></span></td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
    <table align="right">
        <tr align="right">
            <shiro:hasPermission name="/internal/customer/searchCustomers">
                <td>
                    <c:forEach items="${customerTypes}" var="customerType" varStatus="status">
                        <c:if test="${status.index > 0}"> |</c:if>
                        <a href="javascript:searchCustomerByType('${customerType.type}')">${customerType.type}(${customerType.customerCount})</a>
                    </c:forEach>
                </td>
            </shiro:hasPermission>
        </tr>
    </table>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>用户名称</td>
            <td align="center" width=80>联系人</td>
            <td align="center" width=150>词数</td>
            <td align="center" width=80>分组</td>
            <td align="center" width=60>QQ</td>
            <td align="center" width=140>备注</td>
            <td align="center" width=20>状态</td>
            <td align="center" width=50>创建时间</td>
            <td align="center" width=200>操作</td>
        </tr>
    </table>
</div>
<div id="showCustomerTableDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showCustomerTable">
        <c:forEach items="${page.records}" var="customer">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width=10 style="padding-left: 7px;"><input type="checkbox" name="customerUuid" value="${customer.uuid}"/></td>
                <td width=80>${customer.loginName}</td>
                <td width=80>
                    <a href="#" onclick="searchCustomerKeywords('/internal/customerKeyword/searchCustomerKeywords/${customer.uuid}')">${customer.contactPerson}</a>
                </td>
                <td width=150>${customer.keywordCount}(
                    <c:choose>
                    <c:when test="${customer.keywordCount == customer.activeKeywordCount}">
                        <span style="color: green;">激活</span>
                    </c:when>
                    <c:when test="${customer.keywordCount > 0 and customer.activeKeywordCount > 0}">
                        <span style="color: yellowgreen;">部分暂停</span>
                    </c:when>
                    <c:otherwise>
                        <span style="color: red;">暂停</span>
                    </c:otherwise>
                    </c:choose>)</td>
                <td width=80><input type="text" id="${customer.uuid}" onchange="updateCustomerType(this)" value="${customer.type}" style="width: 100%;"></td>
                <td width=60>${customer.qq}</td>
                <td width=140>${customer.remark}</td>
                <td width=20 style="text-align: center">
                    <c:choose>
                        <c:when test="${customer.status ==1}">
                            激活
                        </c:when>
                        <c:otherwise>
                            <span style="color: red;">暂停</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width=50 style="text-align: center"><fmt:formatDate value="${customer.createTime}" pattern="yyyy-MM-dd"/></td>
                <td width=200>
                    <shiro:hasPermission name="/internal/customer/saveCustomer">
                        <a href="javascript:modifyCustomer(${customer.uuid})">修改</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/customer/delCustomer">
                        | <a href="javascript:delCustomer('${customer.uuid}')">删除</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordStatus">
                    | <a href="javascript:changeCustomerKeywordStatus('${customer.uuid}', 0)">暂停关键字</a>
                    | <a href="javascript:changeCustomerKeywordStatus('${customer.uuid}', 1)">激活关键字</a>
                    </shiro:hasPermission>
                    <c:if test="${'bc'.equalsIgnoreCase(entryType)}">
                        <shiro:hasPermission name="/internal/customerChargeType/saveCustomerChargeType">
                            | <a href="javascript:changeCustomerChargeType('${customer.uuid}')">客户规则</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/internal/customerKeyword/saveCustomerKeywords">
                             | <a href="javascript:showCustomerKeywordDialog(${customer.uuid})">快速加词</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/internal/customer/uploadDailyReportTemplate">
                             | <a target="_blank" href="javascript:uploadDailyReportTemplate('${customer.uuid}', this)">上传日报表模板</a>
                        </shiro:hasPermission>
                    </c:if>
                    <c:if test="${'qz'.equalsIgnoreCase(entryType)}">
                    | <a target="_blank" href="javascript:viewAizhanRank('${customer.contactPerson}')">查看爱站排名</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div id="customerChargeTypeDialog" title="客户规则" class="easyui-dialog" style="left: 35%;">
    <input type="hidden" id="customerChargeTypeUuid"/>
    <div id="showRuleRadioDiv" style="text-align: center">
        <input type="radio" id="chargeTypePercentage" onclick="chooseChargeType(this.value)" value="Percentage"
               name="textbox">按照百分比收 &nbsp;&nbsp;
        <input type="radio" id="chargeTypeInterval" onclick="chooseChargeType(this.value)" value="Range"
               name="textbox">按照区间收费
    </div>
    <hr>
    <form id="showRuleForm">
        <div id="chargeTypeCalculationDiv" style="float: left;width: 100%;">
            <div id="pcOperationTypeDiv" style="float: left;width: 48%; margin-left: 10px">
                <input id="PC" type="checkbox" name="operationType"
                       style="width: 13px;height: 13px; margin-left: 130px"
                       onchange="controlChargeTypeCalculationDiv(this)"/>电脑<br>
                <input id="chargeTypeCalculationUuidPC" type="hidden"/>
                <%--指数小于0: <input id="equalZeroCostPC" type="text" /><br>--%>
                <div id="chargesLTPC" style="float: left;width: 50%;margin-top: 5px;">
                    <h6 style="text-align: center">指数小于100</h6>
                    第一 <input id="chargesOfFirstLTPC" type="text"
                              onblur="autoFillPrice(this)"/><span></span><br>
                    第二 <input id="chargesOfSecondLTPC" type="text"/><span></span><br>
                    第三 <input id="chargesOfThirdLTPC" type="text"/><span></span><br>
                    第四 <input id="chargesOfFourthLTPC" type="text"
                              onblur="autoFillPrice(this)"/><span></span><br>
                    第五 <input id="chargesOfFifthLTPC" type="text"/><span></span><br>
                    首页 <input id="chargesOfFirstPageLTPC" type="text"/><span></span>
                </div>
                <div id="chargesGTPC" style="float: left;width: 50%;margin-top: 5px;">
                    <h6 style="text-align: center">指数大于100</h6>
                    第一 <input id="chargesOfFirstGTPC" type="text"
                              onblur="autoFillPrice(this)"/><span></span><br>
                    第二 <input id="chargesOfSecondGTPC" type="text"/><span></span><br>
                    第三 <input id="chargesOfThirdGTPC" type="text"/><span></span><br>
                    第四 <input id="chargesOfFourthGTPC" type="text"
                              onblur="autoFillPrice(this)"/><span></span><br>
                    第五 <input id="chargesOfFifthGTPC" type="text"/><span></span><br>
                    首页 <input id="chargesOfFirstPageGTPC" type="text"/><span></span>
                </div>
            </div>
            <div id="phoneOperationTypeDiv" style="float: left;width: 48%;;margin-top: 5px;">
                <input id="Phone" type="checkbox" name="operationType"
                       style="width: 13px;height: 13px;margin-left: 130px"
                       onchange="controlChargeTypeCalculationDiv(this)"/>手机<br>
                <input id="chargeTypeCalculationUuidPhone" type="hidden"/>
                <div id="chargesLTPhone" style="float: left;width: 50%">
                    <h6 style="text-align: center">指数小于100</h6>
                    第一 <input id="chargesOfFirstLTPhone" type="text"
                              onblur="autoFillPrice(this)"/><span></span><br>
                    第二 <input id="chargesOfSecondLTPhone" type="text"/><span></span><br>
                    第三 <input id="chargesOfThirdLTPhone" type="text"/><span></span><br>
                    第四 <input id="chargesOfFourthLTPhone" type="text"
                              onblur="autoFillPrice(this)"/><span></span><br>
                    第五 <input id="chargesOfFifthLTPhone" type="text"/><span></span><br>
                    首页 <input id="chargesOfFirstPageLTPhone" type="text"/><span></span>
                </div>
                <div id="chargesGTPhone" style="float: left;width: 50%;margin-top: 5px;">
                    <h6 style="text-align: center">指数大于100</h6>
                    第一 <input id="chargesOfFirstGTPhone" type="text"
                              onblur="autoFillPrice(this)"/><span></span><br>
                    第二 <input id="chargesOfSecondGTPhone" type="text"/><span></span><br>
                    第三 <input id="chargesOfThirdGTPhone" type="text"/><span></span><br>
                    第四 <input id="chargesOfFourthGTPhone" type="text"
                              onblur="autoFillPrice(this)"/><span></span><br>
                    第五 <input id="chargesOfFifthGTPhone" type="text"/><span></span><br>
                    首页 <input id="chargesOfFirstPageGTPhone" type="text"/><span></span>
                </div>
            </div>
        </div>

        <div id="chargeTypeIntervalDiv" >
            <input id="chargeTypeIntervalUuid" type="hidden"/>
            <input id="PC" type="checkbox" name="operationType" onclick="initRangeTable(this)"/>PC
            <div id="pcOperationTypeDiv">
                <table id="tabPC" border="1" width="100%" align="center" style="margin-top:10px;font-size: 12px;" cellspacing="0">
                    <tr style="text-align: center">
                        <td width="20%">序号</td>
                        <td width="20%">起始指数</td>
                        <td width="20%">终止指数</td>
                        <td width="20%">价格</td>
                        <td width="20%">操作</td>
                    </tr>
                </table>
                <div style="border:1px;
                border-color:#00CC00;
                margin-top:20px">
                    <input type="button" id="but" value="增加" onclick="addRow('PC')"/>
                </div>
            </div>
            <br>
            <input id="Phone" type="checkbox" name="operationType" onclick="initRangeTable(this)"/>Phone
            <div id="phoneOperationTypeDiv">
                <table id="tabPhone" border="1" width="100%" align="center" style="margin-top:10px;font-size: 12px;" cellspacing="0">
                    <tr style="text-align: center">
                        <td width="20%">序号</td>
                        <td width="20%">起始指数</td>
                        <td width="20%">终止指数</td>
                        <td width="20%">价格</td>
                        <td width="20%">操作</td>
                    </tr>
                </table>
                <div style="border:1px;border-color:#00CC00;margin-top:20px">
                    <input type="button" id="but" value="增加" onclick="addRow('Phone')"/>
                </div>
            </div>
        </div>
    </form>
</div>

</div>
<div id="customerDialog" title="客户信息" class="easyui-dialog" style="left: 40%;">
    <form id="customerForm" method="post" action="customerlist.jsp">
        <table style="font-size:14px;" cellpadding=5>
            <tr>
                <td align="right">联系人:</td>
                <td><input type="text" name="contactPerson" id="contactPerson" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">QQ:</td>
                <td><input type="text" name="qq" id="qq" style="width:200px;">
                </td>
            </tr>
            <tr>
                <td align="right">联系电话</td>
                <td><input type="text" name="telphone" id="telphone" style="width:200px;">
                </td>
            </tr>
            <tr>
                <td align="right">客户类型:</td>
                <td>
                    <input type="text" name="type" id="type" value="" style="width:200px;">
                </td>
            </tr>
            <input type="hidden" id="entryTypeHidden" value="${entryType}">
            <tr>
                <td align="right">客户状态:</td>
                <td>
                    <select name="status" id="status">
                        <option value="1">激活</option>
                        <option value="2">暂停</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right">备注:</td>
                <td><textarea name="remark" id="remark" value="" placeholder="写下备注!"
                              style="width:200px;height:100px;resize: none"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<%--上传日报表模"onsubmit="return checkinput();"--%>
<div id="uploadDailyReportTemplateDialog" title="上传日报表模板" class="easyui-dialog" style="left: 40%;">
    <form method="post" id="dailyReportTemplateForm" action=""
          enctype="multipart/form-data">
        <table width="100%" style="margin-top: 10px;margin-left: 10px">
            <tr>
                <td></td>
            </tr>
            <tr>
                <td></td>
            </tr>
            <tr>
                <td align="right">
                    <table width="100%" style="font-size:14px;">
                        <tr>
                            <td>
                                <input type="hidden" id="customerUuidHidden" name="customerUuid">
                                <input type="file" id="uploadFile" name="file" size=50 height="50px"
                                       style="width: 350px;">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
</div>
<%--添加客户关键字--%>
<div id="customerKeywordDialog" title="客户关键字" class="easyui-dialog" style="left: 35%;">
    <form id="customerKeywordForm">
   <textarea id="customerKeywordTextarea" style="width:480px;height:180px;"
             placeholder="关键字 域名  关键字与域名以空格作为分割，一行一组"></textarea>
        <br>
        <c:choose>
            <c:when test="${'PC'.equalsIgnoreCase(terminalType)}">
                <input type="input" id="group" style="width:480px" value="pc_pm_xiaowu"/>
            </c:when>
            <c:otherwise>
                <input type="input" id="group" style="width:480px" value="m_pm_tiantian"/>
            </c:otherwise>
        </c:choose>
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
<script src="${staticPath }/customer/customerlist.js"></script>
<script type="text/javascript">
    $(function () {
        if(${isDepartmentManager}) {
            $("#loginName").val("${customerCriteria.loginName}");
        }
    });

    <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywords">
    function searchCustomerKeywords(url) {
        window.open(url);
    }
    </shiro:hasPermission>

    <shiro:hasPermission name="/internal/customer/saveCustomer">
    function updateCustomerType(self) {
        var customerType = $("#showCustomerTableDiv").find("#customerType").val();
        var data = {};
        data.customerUuid = self.id;
        data.customerType = $.trim(self.value);
        $.ajax({
            url: '/internal/customer/updateCustomerType',
            data: JSON.stringify(data),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "更新成功");
                } else {
                    $().toastmessage('showErrorToast', "更新失败");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "更新失败");
            }
        });
    }
    </shiro:hasPermission>

    <c:if test="${'bc'.equalsIgnoreCase(entryType)}">
    <shiro:hasPermission name="/internal/dailyReport/triggerReportGeneration">
    function searchCurrentDateCompletedReports() {
        var span = $("#dailyReportSpan");
        $.ajax({
            url: '/internal/dailyReport/searchCurrentDateCompletedReports',
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (dailyReports) {
                var htmlContent = "";
                if (dailyReports) {
                    $.each(dailyReports, function (idx, val) {
                        var date = new Date(val.completeTime);
                        htmlContent = htmlContent + '  <a href="' + val.reportPath + '">下载(' + ((date.getHours() < 10) ? '0'
                                + date.getHours() : date.getHours()) + ':' + ((date.getMinutes() < 10) ? '0' + date.getMinutes()
                                : date.getMinutes()) + ')</a>';
                    });
                } else {
                    htmlContent = "今天没报表";
                }
                span.html(htmlContent);
            },
            error: function () {
                span.html("获取报表清单异常");
            }
        });
    }
    var intervalId = setInterval(function () {
        searchCurrentDateCompletedReports();
    }, 1000 * 30);
    </shiro:hasPermission>
    </c:if>
</script>
</body>
</html>