<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ include file="/commons/basejs.jsp" %>
    <%@ include file="/commons/global.jsp" %>
    <script language="javascript" type="text/javascript" src="/common.js"></script>
    <style type="text/css">
        ul{list-style: none}
        ul li{margin: 5px}
        ul li > span{width: 100px;text-align: right;display: inline-block}
        input[type='radio']{ margin: 0 5px 0 10px;}
        input[type='checkbox']{ margin: 0 5px 0 10px;}
    </style>
    <title>抓排名任务管理</title>
    <script language="javascript" type="text/javascript">
        $(function () {
            $("#crawlRankingDialog").dialog("close");
            $("#centerDiv").css("margin-top", $("#topDiv").height());
            pageLoad();
        })
        function changePaging(currentPage, pageSize) {
            var searchCaptureRankJobForm = $("#searchCaptureRankJobForm");
            searchCaptureRankJobForm.find("#currentPageNumberHidden").val(currentPage);
            searchCaptureRankJobForm.find("#pageSizeHidden").val(pageSize);
            searchCaptureRankJobForm.submit();
        }

        function resetPageNumber() {
            var searchCaptureRankJobForm = $("#searchCaptureRankJobForm");
            searchCaptureRankJobForm.find("#currentPageNumberHidden").val(1);
        }

        function pageLoad() {
            var searchCustomerForm = $("#searchCaptureRankJobForm");
            var pageSize = searchCustomerForm.find('#pageSizeHidden').val();
            var pages = searchCustomerForm.find('#pagesHidden').val();
            var currentPageNumber = searchCustomerForm.find('#currentPageNumberHidden').val();
            var showCustomerBottomDiv = $('#showCustomerBottomDiv');
            showCustomerBottomDiv.find("#chooseRecords").val(pageSize);

            if (parseInt(currentPageNumber) > 1 && parseInt(currentPageNumber) < parseInt(pages)) {
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
        function initcustomerUuid() {
            var postData = {};
            postData.groupNames = $('#crawlRankingForm #groupNames').val().split(",");
            $.ajax({
                url: '/internal/customer/searchCustomersWithKeyword',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        $('#crawlRankingForm #customerUuid').combogrid({
                            panelWidth: 460,
                            value: '',
                            idField: 'uuid',
                            textField: 'contactPerson',
                            dataType: 'json',
                            data: data,
                            columns: [[
                                {field: 'contactPerson', title: '联系人', width: 150},
                                {field: 'telphone', title: '电话', width: 100},
                                {field: 'qq', title: 'QQ', width: 100},
                                {field: 'email', title: 'E-mail', width: 100}
                            ]]
                        });
                    } else {
                        $().toastmessage('showErrorToast', "获取失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "获取失败");
                }
            });

        }
        function initGroupNames(data) {
            $('#crawlRankingForm #groupNames').combobox({
                url: '/internal/customerKeyword/searchGroups', //后台获取下拉框数据的url
                method: 'post',
                //panelHeight:300,//设置为固定高度，combobox出现竖直滚动条
                valueField: 'name',
                textField: 'name',
                multiple: true,
                dataType: 'json',
                editable: true,
                formatter: function (row) { //formatter方法就是实现了在每个下拉选项前面增加checkbox框的方法
                    var opts = $(this).combobox('options');
                    return '<input type="checkbox" class="combobox-checkbox">' + row[opts.textField]
                },
                onLoadSuccess: function () {  //下拉框数据加载成功调用
                    if (data != null && data != '') {
                        var opts = $(this).combobox('options');
                        var target = this;
                        var values = data.groupNames.split(",");
                        $.map(values, function (value) {
                            var el = opts.finder.getEl(target, value);
                            el.find('input.combobox-checkbox')._propAttr('checked', true);
                            el.click();
                        })
                        showCrawlRankingForm(data.uuid);
                        initcustomerUuid();
                        if (data.customerUuid != null && data.customerUuid != '') {
                            $('#crawlRankingForm #customerUuid').combogrid("setValue", data.customerUuid);
                        }
                        $("#crawlRankingForm input[name=exectionType][value=" + data.exectionType + "]").attr("checked", true);
                        $("#crawlRankingForm #exectionTime").val(data.exectionTime);
                        $('#crawlRankingForm #rowNumber').spinner('setValue', data.rowNumber);
                        $('#crawlRankingForm #captureInterval').spinner('setValue', data.captureInterval);
                        $('#crawlRankingForm #executionCycle').spinner('setValue', data.executionCycle);
                        $('#crawlRankingForm #pageSize').spinner('setValue', data.pageSize);
                    }
                },
                onSelect: function (row) { //选中一个选项时调用
                    var opts = $(this).combobox('options');
                    //获取选中的值的values
                    $('#crawlRankingForm #groupNames').val($(this).combobox('getValues'));

                    //设置选中值所对应的复选框为选中状态
                    var el = opts.finder.getEl(this, row[opts.valueField]);
                    el.find('input.combobox-checkbox')._propAttr('checked', true);

                    initcustomerUuid();
                },
                onUnselect: function (row) {//不选中一个选项时调用
                    var opts = $(this).combobox('options');
                    //获取选中的值的values
                    $('#crawlRankingForm #groupNames').val($(this).combobox('getValues'));

                    var el = opts.finder.getEl(this, row[opts.valueField]);
                    el.find('input.combobox-checkbox')._propAttr('checked', false);

                    initcustomerUuid();
                }
            });
        }

        function showCrawlRankingForm(uuid) {
            $("#crawlRankingDialog").dialog({
                resizable: false,
                title: "设置抓排名任务",
                width: 320,
                fitColumns: true,//自动大小
                modal: true,
                buttons: [{
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function () {
                        saveCaptureRankJob(uuid);
                    }
                },
                    {
                        text: '清空',
                        iconCls: 'fi-trash',
                        handler: function () {
                            $('#crawlRankingForm')[0].reset();
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#crawlRankingDialog").dialog("close");
                            $('#crawlRankingForm')[0].reset();
                        }
                    }]
            });
            if (uuid == null || uuid == '') {
                $('#crawlRankingForm')[0].reset();
                initGroupNames();
                initcustomerUuid();
                $('#crawlRankingForm #rowNumber').spinner('setValue', 100);
                $('#crawlRankingForm #captureInterval').spinner('setValue', 500);
                $('#crawlRankingForm #executionCycle').spinner('setValue', 0);
                if ("${sessionScope.terminalType}" == 'PC') {
                    $('#crawlRankingForm #pageSize').spinner('setValue', 10);
                }
                else {
                    $('#crawlRankingForm #pageSize').spinner('setValue', 50);
                }
            }
            $('#crawlRankingDialog').window("resize", {top: $(document).scrollTop() + 100});
        }
        function saveCaptureRankJob(uuid) {
            var CaptureRankJob = {};
            if (uuid != null) {
                CaptureRankJob.uuid = uuid;
            }

            CaptureRankJob.customerUuid = $('#crawlRankingForm #customerUuid').combogrid("getValue");
            if ($("#crawlRankingForm #groupNames").val() == null || $("#crawlRankingForm #groupNames").val() === '') {
                $().toastmessage('showWarningToast', "优化组名不能为空!");
                return;
            }
            var groupNames=$(".combobox-item-selected");
            var groupNamesSelected="";
            $.each(groupNames,function(idx,val){
                if(idx==0) {
                    groupNamesSelected=groupNamesSelected+$(val).text();
                }
                else {
                    groupNamesSelected=groupNamesSelected+","+$(val).text();
                }
            })
            CaptureRankJob.groupNames = groupNamesSelected;
                //$("#crawlRankingForm #groupNames").val();
            CaptureRankJob.exectionType = $("#crawlRankingForm input[name=exectionType]:checked").val();
            if ($("#crawlRankingForm #exectionTime").val() == null || $("#crawlRankingForm #exectionTime").val() === '') {
                $().toastmessage('showWarningToast', "执行时间不能为空!");
                return;
            }
            CaptureRankJob.exectionTime = $("#crawlRankingForm #exectionTime").val();
            CaptureRankJob.rowNumber = $("#crawlRankingForm #rowNumber").val();
            CaptureRankJob.captureInterval = $("#crawlRankingForm #captureInterval").val();
            CaptureRankJob.executionCycle = $("#crawlRankingForm #executionCycle").val();
            CaptureRankJob.pageSize = $("#crawlRankingForm #pageSize").val();
            $.ajax({
                url: '/internal/captureRank/saveCaptureRankJob',
                data: JSON.stringify(CaptureRankJob),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "保存成功", true);
                    } else {
                        $().toastmessage('showErrorToast', "保存失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "保存失败");
                }
            });
        }

        function initCrawlRankingForm(uuid) {
            $.ajax({
                url: '/internal/captureRank/getCaptureRankJob?uuid=' + uuid,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        if (data.customerUuid != null && data.customerUuid != '') {
                            $('#crawlRankingForm #customerUuid').combogrid("setValue", data.customerUuid);
                        }
                        initGroupNames(data);
                    }
                    else {
                        $().toastmessage('showErrorToast', "获取失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "获取失败");
                }
            });
        }

        function modifyCaptureRankJob(uuid) {
            $.ajaxSetup({
                async: false
            });
            initCrawlRankingForm(uuid);
            $.ajaxSetup({
                async: true
            });
        }
        function deleteCaptureRankJob(uuid) {
            $.ajax({
                url: '/internal/captureRank/deleteCaptureRankJob?uuid=' + uuid,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "删除成功", true);
                    }
                    else {
                        $().toastmessage('showErrorToast', "删除失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "删除失败");
                }
            });
        }

        function captureRankJobStatus(uuid,status) {
            if(uuid==''){
                alert('请选择要操作的关键字');
                return;
            }
            if(status == true) {
                if (confirm("确认要暂停选中的任务吗?") == false) return;
            } else {
                if (confirm("确认要取消暂停的任务吗?") == false) return;
            }
            var captureRankJob = {};
            captureRankJob.uuid = parseInt(uuid);
            captureRankJob.status = status;

            $.ajax({
                url: '/internal/captureRank/captureRankJobStatus',
                data: JSON.stringify(captureRankJob),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "暂停成功", true);
                    }
                    else {
                        $().toastmessage('showErrorToast', "暂停失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "暂停失败");
                }
            });
        }

        function getUuids() {
            var a = document.getElementsByName("uuid");
            var uuids = '';
            for (var i = 0; i < a.length; i++) {

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

        function deleteCaptureRankJobs() {
            var uuids = getUuids();
            if (uuids == null || uuids == '') {
                alert("至少选择一条数据!");
                return;
            }
            if (confirm("确实要删除这些任务吗?") == false) return;
            var postData = {};
            postData.uuids = uuids.split(",");
            $.ajax({
                url: '/internal/captureRank/deleteCaptureRankJobs',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(postData),
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "删除成功", true);
                    }
                    else {
                        $().toastmessage('showErrorToast', "删除失败", true);
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "删除失败", true);
                }
            });
        }

        function selectAll(self) {
            var a = document.getElementsByName("uuid");
            if (self.checked) {
                for (var i = 0; i < a.length; i++) {
                    a[i].checked = true;
                }
            } else {
                for (var i = 0; i < a.length; i++) {
                    a[i].checked = false;
                }
            }
        }

        function decideSelectAll() {
            var a = document.getElementsByName("uuid");
            var select = 0;
            for (var i = 0; i < a.length; i++) {
                if (a[i].checked == true) {
                    select++;
                }
            }
            if (select == a.length) {
                $("#selectAllChecked").prop("checked", true);
            } else {
                $("#selectAllChecked").prop("checked", false);
            }
        }
        function customerUuidReset()
        {
            $('#crawlRankingForm #customerUuid').combogrid("reset");
        }
    </script>
</head>
<body>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 35px">
        <form method="post" id="searchCaptureRankJobForm" action="/internal/captureRank/searchCaptureRankJobs" style="margin-bottom:0px ">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}" />
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            组名:<input type="text" name="groupNames" value="${captureRankJobSearchCriteria.groupNames}">
            客户ID:<input type="text" name="customerUuid" value="${captureRankJobSearchCriteria.customerUuid}">
            操作类型:
            <select name="operationType">
                <option value="">请选择终端类型</option>
                <option value="PC" <c:if test="${captureRankJobSearchCriteria.operationType.equals('PC')}">selected="selected"</c:if>>PC</option>
                <option value="Phone" <c:if test="${captureRankJobSearchCriteria.operationType.equals('Phone')}">selected="selected"</c:if>>Phone</option>
            </select>
            执行类型:
            <select name="exectionType">
                <option value="">请选择执行类型</option>
                <option value="Once" <c:if test="${captureRankJobSearchCriteria.exectionType.equals('Once')}">selected="selected"</c:if>>Once</option>
                <option value="Everyday" <c:if test="${captureRankJobSearchCriteria.exectionType.equals('Everyday')}">selected="selected"</c:if>>Everyday</option>
            </select>&nbsp;&nbsp;
            <shiro:hasPermission name="/internal/captureRank/searchCaptureRankJobs">
            <input type="submit" value=" 查询 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/captureRank/saveCaptureRankJob">
            <input type="button" value=" 添加 " onclick="showCrawlRankingForm()">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/captureRank/deleteCaptureRankJobs">
            <input type="button" value=" 删除所选 " onclick="deleteCaptureRankJobs()">&nbsp;&nbsp;
            </shiro:hasPermission>
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>组名</td>
            <td align="center" width=40>客户</td>
            <td align="center" width=40>执行方式</td>
            <td align="center" width=40>执行时间</td>
            <td align="center" width=60>最后执行日期</td>
            <td align="center" width=50>状态</td>
            <td align="center" width=50>抓取记录数</td>
            <td align="center" width=60>抓取间隔(ms)</td>
            <td align="center" width=40>换IP间隔</td>
            <td align="center" width=40>每页条数</td>
            <td align="center" width=90>抓取开始时间</td>
            <td align="center" width=90>抓取结束时间</td>
            <td align="center" width=60>修改人</td>
            <td align="center" width=90>修改时间</td>
            <td align="center" width=60>创建人</td>
            <td align="center" width=90>创建时间</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showCaptureRankJobTable">
        <c:forEach items="${page.records}" var="captureRankJob" varStatus="status">
        <tr align="left" onmouseover="doOver(this);" onmouseout="doOut(this);" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
            <td width=10 align="center"><input type="checkbox" name="uuid" value="${captureRankJob.uuid}" onclick="decideSelectAll()"/></td>
            <td width=80>${captureRankJob.groupNames}</td>
            <td width=40>${captureRankJob.contactPerson}</td>
            <td width=40>
                <c:choose>
                    <c:when test="${captureRankJob.exectionType == 'Everyday'}">每天</c:when>
                    <c:otherwise>一次性</c:otherwise>
                </c:choose>
            </td>
            <td width=40><fmt:formatDate value="${captureRankJob.exectionTime}" pattern="HH:mm"/></td>
            <td width=60>${captureRankJob.lastExecutionDate}</td>
            <td width=50>${captureRankJob.exectionStatus}<font color="red">${captureRankJob.captureRankJobStatus == true ? "暂停中" : ""}</font></td>
            <td width=50>${captureRankJob.rowNumber}</td>
            <td width=60>${captureRankJob.captureInterval}</td>
            <td width=40>${captureRankJob.executionCycle}</td>
            <td width=40>${captureRankJob.pageSize}</td>
            <td width=90><fmt:formatDate value="${captureRankJob.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td width=90><fmt:formatDate value="${captureRankJob.endTime}" pattern="yyyy-MM-dd HH:mm"/></td>


            <td width=60>${captureRankJob.updateBy}</td>
            <td width=90><fmt:formatDate value="${captureRankJob.updateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td width=60>${captureRankJob.createBy}</td>
            <td width=90><fmt:formatDate value="${captureRankJob.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td width=80>
                <shiro:hasPermission name="/internal/captureRank/saveCaptureRankJob">
                <a href="javascript:modifyCaptureRankJob('${captureRankJob.uuid}')">修改</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="/internal/captureRank/deleteCaptureRankJob">
                <a href="javascript:deleteCaptureRankJob('${captureRankJob.uuid}')">删除</a>
                </shiro:hasPermission>
               <%-- <a href="javascript:captureRankJobStatus('${captureRankJob.uuid}','true')">暂停</a>
                <a href="javascript:captureRankJobStatus('${captureRankJob.uuid}','false'),false">取消暂停</a>--%>
                <c:choose>
                    <c:when test="${captureRankJob.captureRankJobStatus}">
                        <a href="javascript:captureRankJobStatus('${captureRankJob.uuid}','false'),false">取消暂停</a>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:captureRankJobStatus('${captureRankJob.uuid}','true')">暂停</a>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        </c:forEach>
    </table>
</div>
<div id="crawlRankingDialog" title="" class="easyui-dialog" style="left: 35%;">
<form id="crawlRankingForm">
    <ul>
        <input type="hidden" name="captureRankJobUuid" id="captureRankJobUuid">
        <li><span>优化组名:</span><input type="text" name="groupNames" id="groupNames" class="easyui-combobox" style="width: 200px"  required></li>
        <li><span>客户名:</span><input type="text" name="customerUuid" id="customerUuid" style="width: 160px" class="easyui-combogrid" data-options="editable:false" placeholder="可以不做操作"><input type="button" value="清空" onclick="customerUuidReset()" style="margin-left: 10px"></input></li>
        <li><span>执行方式:</span><input type="radio" name="exectionType" checked  value="Once">一次性</label><input type="radio" name="exectionType" value="Everyday">每天</li>
        <li><span>执行时间:</span><input type="text" class="Wdate" name="exectionTime" id="exectionTime" onfocus="WdatePicker({lang:'zh-cn',dateFmt:'HH:mm:ss'})" required style="width: 200px"></li>
        <li><span>抓取记录数:</span><input id="rowNumber" name="rowNumber" class="easyui-numberspinner"  data-options="min:0,max:1000,increment:50" required style="width: 200px"></li>
        <li><span>抓取间隔(毫秒):</span><input id="captureInterval" name="captureInterval" class="easyui-numberspinner"  data-options="min:0,increment:500" required style="width: 200px"></li>
        <li><span>每页条数:</span><input id="pageSize" name="pageSize" class="easyui-numberspinner"  data-options="min:0,max:50,increment:10" required style="width: 200px"></li>
        <li><span>换IP间隔:</span><input id="executionCycle" name="executionCycle" class="easyui-numberspinner"  data-options="min:0,increment:50" required style="width: 200px"></li>
    </ul>
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
</body>
</html>