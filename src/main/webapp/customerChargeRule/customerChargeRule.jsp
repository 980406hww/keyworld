<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>客户收费</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 35px">
        <form method="post" id="searchCustomerChargeRuleForm" action="/internal/customerChargeRule/searchCustomerChargeRules" style="margin-bottom:0px ">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}" />
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            <input type="hidden" name="customerUuid" id="customerUuid" value="${customerChargeRuleCriteria.customerUuid}"/>
            客户：<input type="text" list="customer_list" name="customerInfo" id="customerInfo" value="${customerChargeRuleCriteria.customerInfo}" style="width:200px;">
            <input type="submit" value=" 查询 " onclick="resetPageNumber()">&nbsp;&nbsp;
            <input type="button" value=" 添加 " onclick="showCustomerChargeRuleDialog()">&nbsp;&nbsp;
            <input type="button" onclick="deleteCustomerChargeRules()" value=" 删除所选 ">&nbsp;&nbsp;
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width="10">
                <input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/>
            </td>
            <td align="center" width=80>联系人</td>
            <td align="center" width=60>收费总额</td>
            <td align="center" width=40>一月收费<br>(比率)</td>
            <td align="center" width=40>二月收费<br>(比率)</td>
            <td align="center" width=40>三月收费<br>(比率)</td>
            <td align="center" width=40>四月收费<br>(比率)</td>
            <td align="center" width=40>五月收费<br>(比率)</td>
            <td align="center" width=40>六月收费<br>(比率)</td>
            <td align="center" width=40>七月收费<br>(比率)</td>
            <td align="center" width=40>八月收费<br>(比率)</td>
            <td align="center" width=40>九月收费<br>(比率)</td>
            <td align="center" width=40>十月收费<br>(比率)</td>
            <td align="center" width=40>十一月收费<br>(比率)</td>
            <td align="center" width=40>十二月收费<br>(比率)</td>
            <td align="center" width=40>收费天号</td>
            <td align="center" width=80>创建时间</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px;">
    <table style="font-size:12px; width: 100%;" id="showCustomerChargeRuleTable">
        <c:forEach items="${page.records}" var="customerChargeRule" varStatus="status">
        <tr align="left" height="30" onmouseover="doOver(this);" onmouseout="doOut(this);" <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if>>
            <td width="10" align="center"><input type="checkbox" name="uuid" value="${customerChargeRule.uuid}" onclick="decideSelectAll()"/></td>
            <td width="80">${customerChargeRule.contactPerson}</td>
            <td width="60">${customerChargeRule.chargeTotal}</td>
            <td width="40" name="clickEvent">${customerChargeRule.januaryFee == 0 ? "" : customerChargeRule.januaryFee}<c:if test="${customerChargeRule.januaryFee>0}">(${customerChargeRule.januaryRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.februaryFee == 0 ? "" : customerChargeRule.februaryFee}<c:if test="${customerChargeRule.februaryFee>0}">(${customerChargeRule.februaryRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.marchFee == 0 ? "" : customerChargeRule.marchFee}<c:if test="${customerChargeRule.marchFee>0}">(${customerChargeRule.marchRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.aprilFee == 0 ? "" : customerChargeRule.aprilFee}<c:if test="${customerChargeRule.aprilFee>0}">(${customerChargeRule.aprilRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.mayFee == 0 ? "" : customerChargeRule.mayFee}<c:if test="${customerChargeRule.mayFee>0}">(${customerChargeRule.mayRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.juneFee == 0 ? "" : customerChargeRule.juneFee}<c:if test="${customerChargeRule.juneFee>0}">(${customerChargeRule.juneRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.julyFee == 0 ? "" : customerChargeRule.julyFee}<c:if test="${customerChargeRule.julyFee>0}">(${customerChargeRule.julyRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.augustFee == 0 ? "" : customerChargeRule.augustFee}<c:if test="${customerChargeRule.augustFee>0}">(${customerChargeRule.augustRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.septemberFee == 0 ? "" : customerChargeRule.septemberFee}<c:if test="${customerChargeRule.septemberFee>0}">(${customerChargeRule.septemberRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.octoberFee == 0 ? "" : customerChargeRule.octoberFee}<c:if test="${customerChargeRule.octoberFee>0}">(${customerChargeRule.octoberRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.novemberFee == 0 ? "" : customerChargeRule.novemberFee}<c:if test="${customerChargeRule.novemberFee>0}">(${customerChargeRule.novemberRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.decemberFee == 0 ? "" : customerChargeRule.decemberFee}<c:if test="${customerChargeRule.decemberFee>0}">(${customerChargeRule.decemberRate}%)</c:if></td>
            <td width="40" name="clickEvent">${customerChargeRule.chargeDay}</td>
            <td width="80" align="center"><fmt:formatDate value="${customerChargeRule.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td style="text-align: center;" width="80">
                <input type="hidden" name="customerUuid" value="${customerChargeRule.customerUuid}">
                <a href="javascript:deleteCustomerChargeRule('${customerChargeRule.uuid}')">删除</a>
            </td>
        </tr>
        </c:forEach>
    </table>
</div>

<div id="customerChargeRuleDialog" title="客户收费规则" class="easyui-dialog" style="display:none;left: 40%;">
    <form id="addCustomerChargeRuleForm">
        <table style="font-size:14px;" cellpadding=5>
            <tr>
                <td colspan="2"><span style="margin-left: 36px;">客户:</span><input type="text" list="customer_list" name="customerInfo" id="customerInfo" style="width:228px;"></td>
            </tr>
            <tr>
                <td colspan="2"><span style="margin-left: 12px;">收费总额:</span><input type="text" class="easyui-numberspinner" name="chargeTotal" id="chargeTotal" style="width:228px;"></td>
            </tr>
            <tr>
                <td colspan="2"><span style="margin-left: 12px;">收费天号:</span><input type="text" class="easyui-numberspinner" name="chargeDay" id="chargeDay" style="width:228px;"></td>
            </tr>
            <tr>
                <td align="right">一月收费:<input type="text" class="easyui-numberspinner" name="januaryFee" id="januaryFee" style="width:80px;"></td>
                <td align="right">一月比率:<input type="text" class="easyui-numberspinner" name="januaryRate" id="januaryRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">二月收费:<input type="text" class="easyui-numberspinner" name="februaryFee" id="februaryFee" style="width:80px;"></td>
                <td align="right">二月比率:<input type="text" class="easyui-numberspinner" name="februaryRate" id="februaryRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">三月收费:<input type="text" class="easyui-numberspinner" name="marchFee" id="marchFee" style="width:80px;"></td>
                <td align="right">三月比率:<input type="text" class="easyui-numberspinner" name="marchRate" id="marchRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">四月收费:<input type="text" class="easyui-numberspinner" name="aprilFee" id="aprilFee" style="width:80px;"></td>
                <td align="right">四月比率:<input type="text" class="easyui-numberspinner" name="aprilRate" id="aprilRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">五月收费:<input type="text" class="easyui-numberspinner" name="mayFee" id="mayFee" style="width:80px;"></td>
                <td align="right">五月比率:<input type="text" class="easyui-numberspinner" name="mayRate" id="mayRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">六月收费:<input type="text" class="easyui-numberspinner" name="juneFee" id="juneFee" style="width:80px;"></td>
                <td align="right">六月比率:<input type="text" class="easyui-numberspinner" name="juneRate" id="juneRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">七月收费:<input type="text" class="easyui-numberspinner" name="julyFee" id="julyFee" style="width:80px;"></td>
                <td align="right">七月比率:<input type="text" class="easyui-numberspinner" name="julyRate" id="julyRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">八月收费:<input type="text" class="easyui-numberspinner" name="augustFee" id="augustFee" style="width:80px;"></td>
                <td align="right">八月比率:<input type="text" class="easyui-numberspinner" name="augustRate" id="augustRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">九月收费:<input type="text" class="easyui-numberspinner" name="septemberFee" id="septemberFee" style="width:80px;"></td>
                <td align="right">九月比率:<input type="text" class="easyui-numberspinner" name="septemberRate" id="septemberRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">十月收费:<input type="text" class="easyui-numberspinner" name="octoberFee" id="octoberFee" style="width:80px;"></td>
                <td align="right">十月比率:<input type="text" class="easyui-numberspinner" name="octoberRate" id="octoberRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">十一月收费:<input type="text" class="easyui-numberspinner" name="novemberFee" id="novemberFee" style="width:80px;"></td>
                <td align="right">十一月比率:<input type="text" class="easyui-numberspinner" name="novemberRate" id="novemberRate" style="width:80px;"></td>
            </tr>
            <tr>
                <td align="right">十二月收费:<input type="text" class="easyui-numberspinner" name="decemberFee" id="decemberFee" style="width:80px;"></td>
                <td align="right">十二月比率:<input type="text" class="easyui-numberspinner" name="decemberRate" id="decemberRate" style="width:80px;"></td>
            </tr>
        </table>
    </form>
</div>

<datalist id="customer_list">
    <c:forEach items="${customerList}" var="costomer">
        <option>${costomer.contactPerson} ${'_____'} ${costomer.uuid}</option>
    </c:forEach>
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
    </select>
    </div>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/customerChargeRule/customerChargeRule.js"></script>
<script>
    $(document).ready(function(){
        $('table td').click(function(){
            if(!$(this).is('.input')){
                if($(this).is("td[name='clickEvent']")){
                    var notModified = $(this).text();
                    $(this).addClass('input').html('<input type="text" style="width: 100%;" value="'+ $(this).text() +'"/>').find('input').focus().blur(function(){
                        var thisvalue = $(this).val();
                        var tdObjs = $(this).parent().parent().find("td");
                        $(this).parent().removeClass('input').html($(this).val() || '');
                        if(notModified != thisvalue){
                            var customerChargeRule = {};
                            customerChargeRule.uuid = tdObjs.eq(0).find("input[name='uuid']").val();
                            var januaryInfo = tdObjs.eq(3).text();
                            var februaryInfo = tdObjs.eq(4).text();
                            var marchInfo = tdObjs.eq(5).text();
                            var aprilInfo = tdObjs.eq(6).text();
                            var mayInfo = tdObjs.eq(7).text();
                            var juneInfo = tdObjs.eq(8).text();
                            var julyInfo = tdObjs.eq(9).text();
                            var augustInfo = tdObjs.eq(10).text();
                            var septemberInfo = tdObjs.eq(11).text();
                            var octoberInfo = tdObjs.eq(12).text();
                            var novemverInfo = tdObjs.eq(13).text();
                            var decemberInfo = tdObjs.eq(14).text();
                            // 收费明细
                            customerChargeRule.januaryFee = januaryInfo.substring(0, januaryInfo.indexOf("("));
                            customerChargeRule.februaryFee = februaryInfo.substring(0, februaryInfo.indexOf("("));
                            customerChargeRule.marchFee = marchInfo.substring(0, marchInfo.indexOf("("));
                            customerChargeRule.aprilFee = aprilInfo.substring(0, aprilInfo.indexOf("("));
                            customerChargeRule.mayFee = mayInfo.substring(0, mayInfo.indexOf("("));
                            customerChargeRule.juneFee = juneInfo.substring(0, juneInfo.indexOf("("));
                            customerChargeRule.julyFee = julyInfo.substring(0, julyInfo.indexOf("("));
                            customerChargeRule.augustFee = augustInfo.substring(0, augustInfo.indexOf("("));
                            customerChargeRule.septemberFee = septemberInfo.substring(0, septemberInfo.indexOf("("));
                            customerChargeRule.octoberFee = octoberInfo.substring(0, octoberInfo.indexOf("("));
                            customerChargeRule.novemverFee = novemverInfo.substring(0, novemverInfo.indexOf("("));
                            customerChargeRule.decemberFee = decemberInfo.substring(0, decemberInfo.indexOf("("));
                            // 收费比率
                            customerChargeRule.januaryRate = januaryInfo.substring(januaryInfo.indexOf("(") + 1, januaryInfo.length - 2);
                            customerChargeRule.februaryRate = februaryInfo.substring(februaryInfo.indexOf("(") + 1, februaryInfo.length - 2);
                            customerChargeRule.marchRate = marchInfo.substring(marchInfo.indexOf("(") + 1, marchInfo.length - 2);
                            customerChargeRule.aprilRate = aprilInfo.substring(aprilInfo.indexOf("(") + 1, aprilInfo.length - 2);
                            customerChargeRule.mayRate = mayInfo.substring(mayInfo.indexOf("(") + 1, mayInfo.length - 2);
                            customerChargeRule.juneRate = juneInfo.substring(juneInfo.indexOf("(") + 1, juneInfo.length - 2);
                            customerChargeRule.julyRate = julyInfo.substring(julyInfo.indexOf("(") + 1, julyInfo.length - 2);
                            customerChargeRule.augustRate = augustInfo.substring(augustInfo.indexOf("(") + 1, augustInfo.length - 2);
                            customerChargeRule.septemberRate = septemberInfo.substring(septemberInfo.indexOf("(") + 1, septemberInfo.length - 2);
                            customerChargeRule.octoberRate = octoberInfo.substring(octoberInfo.indexOf("(") + 1, octoberInfo.length - 2);
                            customerChargeRule.novemverRate = novemverInfo.substring(novemverInfo.indexOf("(") + 1, novemverInfo.length - 2);
                            customerChargeRule.decemberRate = decemberInfo.substring(decemberInfo.indexOf("(") + 1, decemberInfo.length - 2);

                            customerChargeRule.chargeDay = tdObjs.eq(15).text();
                            customerChargeRule.customerUuid = tdObjs.eq(17).find("input[name='customerUuid']").val();
                            saveCustomerChargeRule(customerChargeRule, "update");
                        }
                    });
                }
            }
        });
    });
</script>
</body>
</html>