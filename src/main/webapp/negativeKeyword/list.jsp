<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>负面词管理</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 35px">
        <form method="post" id="negativeKeywordForm" action="/internal/negativeKeywordName/searchNegativeKeywordNames" style="margin-bottom:0px ">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}" />
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            分组名称:<input type="text" name="group" id="group" value="${negativeKeywordNameCriteria.group}">&nbsp;&nbsp;
            <input type="checkbox" name="hasEmail" ${negativeKeywordNameCriteria.hasEmail != null ? "checked=true" : ""} />&nbsp;有Email&nbsp;&nbsp;
            <shiro:hasPermission name="/internal/negativeKeywordName/searchNegativeKeywordNames">
            <input type="submit" value=" 查询 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/negativeKeywordName/uploadTxtFile">
            <input type="button" value=" 导入 " onclick="showUploadTxtFileDialog()">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/negativeKeywordName/downloadNegativeKeywordInfo">
            <input type="button" value=" 导出 " onclick="getNegativeExcel()">&nbsp;&nbsp;
            </shiro:hasPermission>
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width=30>编号</td>
            <td align="center" width=80>名称</td>
            <td align="center" width=50>分组</td>
            <td align="center" width=80>官网网址</td>
            <td align="center" width=60>Email</td>
            <td align="center" width=40>采集电脑？</td>
            <td align="center" width=50>排名负面个数</td>
            <td align="center" width=60>下拉负面词</td>
            <td align="center" width=60>相关负面词</td>
            <td align="center" width=40>采集手机？</td>
            <td align="center" width=50>排名负面个数</td>
            <td align="center" width=60>下拉负面词</td>
            <td align="center" width=60>相关负面词</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px;">
    <table style="font-size:12px; width: 100%;" id="showCaptureRankJobTable">
        <c:forEach items="${page.records}" var="negativeKeyword" varStatus="status">
        <tr align="left" onmouseover="doOver(this);" onmouseout="doOut(this);" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
            <td width=30>${negativeKeyword.uuid}</td>
            <td width=80><a href="#" onclick="findPositionInfos('${negativeKeyword.uuid}')">${negativeKeyword.name}</a></td>
            <td width=50>${negativeKeyword.group}</td>
            <td width=80>${negativeKeyword.officialUrl}</td>
            <td width=60>${negativeKeyword.email}</td>
            <td width=40>${negativeKeyword.rankCaptured ? "是" : "否"}</td>
            <td width=50>${negativeKeyword.rankNegativeCount}</td>
            <td width=60>${negativeKeyword.selectNegativeKeyword}</td>
            <td width=60>${negativeKeyword.relevantNegativeKeyword}</td>
            <td width=40>${negativeKeyword.phoneCaptured ? "是" : "否"}</td>
            <td width=50>${negativeKeyword.phoneRankNegativeCount}</td>
            <td width=60>${negativeKeyword.phoneSelectNegativeKeyword}</td>
            <td width=60>${negativeKeyword.phoneRelevantNegativeKeyword}</td>
        </tr>
        </c:forEach>
    </table>
</div>
<div id="uploadTxtFileDialog" title="" class="easyui-dialog" style="left: 35%;">
    <form method="post" id="uploadTxtFileForm" action="" enctype="multipart/form-data">
        <table width="100%" style="font-size:14px;">
            <tr><td>
                组名:<input id="groupName" type="text" value="" style="width: 180px;">
            </td></tr>
            <tr><td>
                <input type="file" id="file" name="file" size=50 height="50px" style="width: 180px;">
            </td></tr>
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

<div id="positionInfoDiv" class="easyui-dialog" style="left: 40%;" >
    <table id="positionInfoTable" border="1" cellpadding="10" style="font-size: 12px;background-color: white;border-collapse: collapse;margin: 10px 10px;width:96%;">
        <tr>
            <td width="45">编号</td>
            <td width="40">类型</td>
            <td width="40">关键字</td>
            <td width="30">排名</td>
            <td width="200">目标网址</td>
            <td width="140">生成时间</td>
        </tr>
    </table>
</div>

<form id="downloadNegativeKeywordInfoForm" method="post" action="/internal/negativeKeywordName/downloadNegativeKeywordInfo">
    <input type="hidden" name="group" id="groupHidden" value="">
    <input type="hidden" name="hasEmail" id="hasEmailHidden" value=""/>
</form>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/negativeKeyword/list.js"></script>
<script language="javascript" type="text/javascript">
    <shiro:hasPermission name="/internal/negativeKeywordName/searchNegativeKeywordNames">
    function findPositionInfos(uuid) {
        $("#positionInfoTable  tr:not(:first)").remove();
        $.ajax({
            url: '/internal/negativeKeywordName/getNegativePositionInfo/' + uuid,
            type: 'Get',
            success: function (positionInfos) {
                if (positionInfos != null && positionInfos.length > 0) {
                    $.each(positionInfos, function (idx, val) {
                        var newTr = document.createElement("tr");
                        var url = val.targetUrl;
                        if(url.indexOf("http") > -1) {
                            url = "<a target='_blank' href='" + val.targetUrl + "'>" + val.targetUrl + "</a>";
                        } else {
                            url = "<a target='_blank' href='http://" + val.targetUrl + "'>" + val.targetUrl + "</a>";
                        }
                        var positionInfoElements = [
                            val.uuid,
                            val.terminalType,
                            val.keyword,
                            val.position,
                            url,
                            toTimeFormat(new Date(val.createTime))
                        ];
                        $.each(positionInfoElements, function () {
                            var newTd = document.createElement("td");
                            newTr.appendChild(newTd);
                            newTd.innerHTML = this;
                        });
                        $("#positionInfoTable")[0].lastChild.appendChild(newTr);
                    });
                    $("#positionInfoDiv").dialog({
                        resizable: false,
                        minWidth:530,
                        maxHeight:350,
                        title:"排名信息",
                        modal: true,
                        buttons: [{
                            text: '取消',
                            iconCls: 'icon-cancel',
                            handler: function () {
                                $("#positionInfoDiv").dialog("close");
                            }
                        }]
                    });
                    $("#positionInfoDiv").window("resize",{top:$(document).scrollTop() + 100});
                } else {
                    alert("暂无数据");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "获取信息失败");
            }
        });
    }
    </shiro:hasPermission>
</script>
</body>
</html>