<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
    <style type="text/css">
        #showAdvertisingTableDiv {
            width: 100%;
            margin: auto;
        }
        #showAdvertisingTable tr:nth-child(odd){background:#EEEEEE;}

        #showAdvertisingTable td{
            text-align: left;
        }
        h6{ margin: 0 5px;}
    </style>
    <title>广告管理列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=13>
                <form method="post" id="searchAdvertisingForm" action="/internal/advertising/searchAdvertisingLists" style="margin-bottom:0px ">
                    <div>
                        <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                        <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                        <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                        <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                        <input type="hidden" name="websiteUuid" id="websiteUuid" value="${advertisingCriteria.websiteUuid}"/>
                        用户名称:<input type="text" name="customerInfo" id="customerInfo" value="${advertisingCriteria.customerInfo}">&nbsp;&nbsp;
                        广告名称:<input type="text" name="advertisingAdName" id="advertisingAdName" value="${advertisingCriteria.advertisingAdName}">
                        &nbsp;&nbsp;<input id="expire" name="expire" type="checkbox"  onclick="expireValue()" value="${advertisingCriteria.expire}"/>过期广告
                        &nbsp;&nbsp;<input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">
                        &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showAdvertisingDialog(${advertisingCriteria.websiteUuid}, 0)"/>
                        &nbsp;&nbsp;<input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteBatchAdvertising(${advertisingCriteria.websiteUuid})"/>
                    </div>
                </form>
            </td>
        </tr>
    </table>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>用户名称</td>
            <td align="center" width=80>广告名称</td>
            <td align="center" width=80>广告标识</td>
            <td align="center" width=80>分类</td>
            <td align="center" width=80>投放范围</td>
            <td align="center" width=80>是否限时	</td>
            <td align="center" width=80>开始时间</td>
            <td align="center" width=80>结束时间</td>
            <td align="center" width=80>续费时间</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="showAdvertisingTableDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showAdvertisingTable">
        <c:forEach items="${page.records}" var="advertising">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width=10 style="padding-left: 7px;"><input type="checkbox" name="uuid" value="${advertising.uuid}"/></td>
                <td width=80>${advertising.customerInfo}</td>
                <td width=80>${advertising.advertisingAdName}</td>
                <td width=80>${advertising.advertisingTagname}</td>
                <td width=80>${advertising.advertisingType}</td>
                <td width=80>${advertising.advertisingArcType}</td>
                <td width=80>${advertising.advertisingTimeSet == 0 ? "永不过期" : "限时标记"}</td>
                <td width=80 style="text-align: center"><fmt:formatDate value="${advertising.advertisingStarttime}" pattern="yyyy-MM-dd"/></td>
                <td width=80 style="text-align: center"><fmt:formatDate value="${advertising.advertisingEndtime}" pattern="yyyy-MM-dd"/></td>
                <td width=80 style="text-align: center"><fmt:formatDate value="${advertising.renewTime}" pattern="yyyy-MM-dd"/></td>
                <td width=80>
                        <a href="javascript:modifyAdvertising(${advertising.uuid})">修改</a>
                        | <a href="javascript:delAdvertising(${advertising.uuid})">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</div>
<div id="advertisingDialog" title="广告信息" class="easyui-dialog" style="display: none">
    <form id="advertisingForm">
        <table style="font-size:12px" id="advertisingTable" align="center" cellspacing="8">
            <tr>
                <td style="width:80px;"  align="right">广告标识:</td>
                <td>
                    <input type="hidden" name="advertisingId" id="advertisingId">
                    <input type="text" name="advertisingTagname" id="advertisingTagname" style="width:180px;">
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right">用户名称:</td>
                <td>
                    <input type="text" name="customerInfo" id="customerInfo" list="customer_list" style="width:180px;">
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right">广告位名称:</td>
                <td>
                    <input type="text" name="advertisingAdName" id="advertisingAdName" style="width:180px;">
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right">广告分类:</td>
                <td>
                    <input type="text" name="advertisingType" id="advertisingType" list="advertisingType_list" style="width:180px;">
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right">广告投放范围:</td>
                <td>
                    <input type="text" name="advertisingArcType" id="advertisingArcType" list="advertisingArcType_list"  style="width:180px;">
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right">时间限制:</td>
                <td>
                    <input id="advertisingTimeSet" name="advertisingTimeSet" type="radio" value="0" checked/>永不过期&nbsp;
                    <input id="advertisingTimeSet" name="advertisingTimeSet" type="radio" value="1"/>在设定时间内有效
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right">投放时间:</td>
                <td>
                    从<input name="advertisingStarttime" id="advertisingStarttime" class="Wdate" type="text" onClick="WdatePicker()">
                </td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right"></td>
                <td>
                    到<input name="advertisingEndtime" id="advertisingEndtime" class="Wdate" type="text" onClick="WdatePicker()">
                </td>
            </tr>
        </table>
        <table style="font-size:12px" cellspacing="8">
            <tr>
                <td colspan="2">
                    <table border="0" style="display:block;font-size:12px;" cellspacing="8" id="advertisingBodyCheckedTable">
                        <td style="width:72px;"  align="right">广告内容:</td>
                        <td>
                            <input id="advertisingBodyChecked" name="advertisingBodyChecked" type="radio" value="0" checked  onchange="changeAdvertisingBodySubmit('code')"/>代码&nbsp;
                            <input id="advertisingBodyChecked" name="advertisingBodyChecked" type="radio" value="1"  onchange="changeAdvertisingBodySubmit('txt')"/>文字&nbsp;
                            <input id="advertisingBodyChecked" name="advertisingBodyChecked" type="radio" value="2"  onchange="changeAdvertisingBodySubmit('img')"/>图片&nbsp;
                            <input id="advertisingBodyChecked" name="advertisingBodyChecked" type="radio" value="3"  onchange="changeAdvertisingBodySubmit('flash')"/>Flash&nbsp;
                        </td>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" cellpadding="0" id="code">
                        <tr>
                            <td align="right" style="width:72px">广告代码:</td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px"></td>
                            <td><textarea  id="htmlcode" name="htmlcode" rows="5" cols="27" placeholder="请填写广告代码，支持html代码"></textarea></td>
                        </tr>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" id="txt">
                        <tr>
                            <td align="right" style="width:72px">文字内容:</td>
                            <td><input type="text" name="txtTitle" id="txtTitle"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">文字链接:</td>
                            <td><input type="text" name="txtLink" id="txtLink" style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">文字颜色</td>
                            <td><input type="text" name="txtColor" id="txtColor" style="width:180px" placeholder="例如:red,#EF8684"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">文字大小</td>
                            <td><input type="text" name="txtSize" id="txtSize" style="width:180px;" placeholder="例如:4px,12px"/></td>
                        </tr>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" cellpadding="0" id="img">
                        <tr>
                            <td align="right" style="width:72px">图片地址*:</td>
                            <td><input type="text" name="imgUrl" id="imgUrl"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">图片链接*:</td>
                            <td><input type="text" name="imgLink" id="imgLink"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">图片宽度:</td>
                            <td><input type="text" name="imgWidth" id="imgWidth" style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">图片高度:</td>
                            <td><input type="text" name="imgHeight" id="imgHeight" style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">图片描述:</td>
                            <td><input type="text" name="imgDescrip" id="imgDescrip" style="width:180px;"/></td>
                        </tr>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" cellpadding="0" id="flash">
                        <tr>
                            <td align="right" style="width:72px">flash链接:</td>
                            <td><input type="text" name="flashLink" id="flashLink"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">flash宽度:</td>
                            <td><input type="text" name="flashWidth" id="flashWidth"  style="width:180px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" style="width:72px">flash高度:</td>
                            <td><input type="text" name="flashHeight" id="flashHeight" style="width:180px;"/></td>
                        </tr>
                    </table>
                    <table border="0" style="display:none;font-size:12px;" cellspacing="8" id="advertisingNormbodyTable">
                        <tr>
                            <td style="width:76px;margin-left: -4px"  align="right">正常显示内容:</td>
                        </tr>
                        <tr>
                            <td style="width:72px;"  align="right"></td>
                            <td>
                                <textarea id="advertisingNormbody" style="width:180px; height: 100px; resize: none"></textarea>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="margin-top: -2px">
                <td style="width:80px;"  align="right">过期显示内容:</td>
            </tr>
            <tr>
                <td style="width:80px;"  align="right"></td>
                <td>
                    <textarea id="advertisingExpbody" style="width:180px; height: 100px; resize: none"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<datalist id="customer_list">
</datalist>
<datalist id="advertisingType_list">
</datalist>
<datalist id="advertisingArcType_list">
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
<script src="${staticPath }/advertising/advertising.js"></script>
<script type="text/javascript">
    $(function () {
        initExpireChecked();
        window.onresize = function(){
            $("#showAdvertisingTableDiv").css("margin-top",$("#topDiv").height());
        }
    });

    function initExpireChecked() {
        if(${advertisingCriteria.expire == 1}){
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