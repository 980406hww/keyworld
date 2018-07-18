
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>关键词负面排名</title>
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
    <div id="topDiv">
        <%@include file="/menu.jsp" %>
         <div style="margin-top: 35px">
            <form method="post" id="searchNegativeRankFrom" action="/internal/negativeRank/searchNegativeRanks" style="margin-bottom:0px">
                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                &nbsp;&nbsp;关键字:<input type="text" list="keyword_list" name="keyword" id="keyword" value="${negativeRankCriteria.keyword}" style="width:200px;">
                <input type="hidden" name="searchEngineHidden" id="searchEngineHidden" value="${negativeRankCriteria.searchEngine}"/>&nbsp;&nbsp;
                搜索引擎:
                  <select type="text" name="searchEngine" class="select" id="searchEngine" >
                      <option value="">全部搜索引擎</option>
                      <option value="百度电脑">百度电脑</option>
                      <option value="百度手机">百度手机</option>
                      <option value="搜狗电脑">搜狗电脑</option>
                      <option value="搜狗手机">搜狗手机</option>
                      <option value="360电脑">360电脑</option>
                      <option value="360手机">360手机</option>
                      <option value="神马">神马</option>
                  </select>
                &nbsp;&nbsp;
                创建日期:<input name="createTime" id="createTime" class="Wdate" type="text" style="width:90px;" onClick="WdatePicker()" value="${negativeRankCriteria.createTime}"/>
                <shiro:hasPermission name="/internal/negativeRank/searchNegativeRanks">
                    <input type="submit" value="查询" onclick="submitPageNumber()"/>&nbsp;&nbsp;
                </shiro:hasPermission>
            </form>
        </div>
        <table style="font-size:12px; width: 100%;" id="headerTable">
            <tr bgcolor="" height="30">
                <td align="center" width="10">
                    <input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/>
                </td>
                <td align="center" width=60>关键字</td>
                <td align="center" width=50>搜索引擎</td>
                <td align="center" width=40>负面总数</td>
                <td align="center" width=40>首页负面排名</td>
                <td align="center" width=40>第二页负面排名</td>
                <td align="center" width=40>第三页负面排名</td>
                <td align="center" width=40>第四页负面排名</td>
                <td align="center" width=40>第五页负面排名</td>
                <td align="center" width=40>其他负面排名</td>
                <td align="center" width=60>创建时间</td>
            </tr>
        </table>
    </div>

    <div id="centerDiv" style="margin-bottom: 30px;">
        <table style="font-size:12px; width: 100%; table-layout:fixed; " id="showNegativeRankTable">
            <c:forEach items="${page.records}" var="NegativeRank" varStatus="status">
                <tr align="left" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
                    <td align="center" width=13 align="center"><input type="checkbox" name="uuid" value="${NegativeRank.uuid}" onclick="decideSelectAll()"/></td>
                    <td align="center" width=59>${NegativeRank.keyword}</td>
                    <td align="center" width=49>${NegativeRank.searchEngine}</td>
                    <td align="center" width=40>${NegativeRank.negativeCount}</td>
                    <td align="left" name="clickEvent" class="firstPageRanks" width=40>${NegativeRank.firstPageRanks}</td>
                    <td align="left" name="clickEvent" class="secondPageRanks" width=40>${NegativeRank.secondPageRanks}</td>
                    <td align="left" name="clickEvent" class="thirdPageRanks" width=39>${NegativeRank.thirdPageRanks}</td>
                    <td align="left" name="clickEvent" class="fourthPageRanks" width=39>${NegativeRank.fourthPageRanks}</td>
                    <td align="left" name="clickEvent" class="fifthPageRanks" width=40>${NegativeRank.fifthPageRanks}</td>
                    <td align="left" name="clickEvent" class="otherPageRanks" width=40>${NegativeRank.otherPageRanks}</td>
                    <td align="center" width=60><fmt:formatDate  value="${NegativeRank.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
    <datalist id="keyword_list">
        <c:forEach items="${keywords}" var="keyword">
            <option>${keyword}</option>
        </c:forEach>
    </datalist>

<%@ include file="/commons/loadjs.jsp"%>
<script src="${staticPath}/negativeRank/negativeRank.js"></script>
<script language="javascript">
    <shiro:hasPermission name="/internal/negativeRank/updateNegativeRankKeyword">
    $(function () {
        $('table td').click(function(){
            if(!$(this).is('.input')){
                if($(this).is("td[name='clickEvent']")){
                    var notModified = $(this).text();
                    $(this).addClass('input').html('<input type="text" style="width: 100%" value="'+ $(this).text() +'"/>').find('input').focus().blur(function(){
                        var negativeRank = {};
                        var thisvalue = removeAllSpace($(this).val());
                        if(notModified != thisvalue){
                            var allTdTag = $(this).parent().siblings('td');
                            negativeRank.uuid = parseInt(allTdTag.eq(0).find('input').val());
                            var thisName = $(this).parent().attr('class').split(" ")[0];
                            var thisvalue = thisvalue;
                            if(thisName == "firstPageRanks"){
                                negativeRank.firstPageRanks = thisvalue;
                            }else if(thisName == "secondPageRanks"){
                                negativeRank.secondPageRanks = thisvalue;
                            }else if(thisName == "thirdPageRanks"){
                                negativeRank.thirdPageRanks = thisvalue;
                            }else if(thisName == "fourthPageRanks"){
                                negativeRank.fourthPageRanks = thisvalue;
                            }else if(thisName == "fifthPageRanks"){
                                negativeRank.fifthPageRanks = thisvalue;
                            }else if(thisName == "otherPageRanks"){
                                negativeRank.otherPageRanks = thisvalue;
                            }
                            var rankTag = $(this).parent().siblings("td[name='clickEvent']");
                            $(this).parent().removeClass('input').html($(this).val() || '');
                            negativeRank.negativeCount = count(rankTag);
                            updateNegativeRank(negativeRank);
                        }else {
                            $(this).parent().removeClass('input').html($(this).val() || '');
                        }
                    });
                }
            }
        });
    });
    function updateNegativeRank(negativeRank){
        $.ajax({
            url: '/internal/negativeRank/updateNegativeRankKeyword',
            data: JSON.stringify(negativeRank),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "操作成功");
                } else {
                    $().toastmessage('showErrorToast', "操作失败");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "操作失败");
            }
        });
    }
    </shiro:hasPermission>
</script>
</body>
</html>


