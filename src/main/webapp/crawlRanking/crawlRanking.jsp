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
    <title>客户管理</title>
    <script language="javascript" type="text/javascript">
        $(function(){
            $("#crawlRankingDialog").dialog("close");
            $("#centerDiv").css("margin-top",$("#topDiv").height());
            pageLoad();
            alignTableHeader();
            window.onresize = function(){
                alignTableHeader();
            }
        })

        function alignTableHeader(){
            var td = $("#showCaptureCurrentRankJobTable tr:first td");
            var ctd = $("#headerTable tr:first td");
            $.each(td, function (idx, val) {
                ctd.eq(idx).width($(val).width());
            });
        }

        function changePaging(currentPage, pageSize) {
            var searchCaptureCurrentRankJobForm = $("#searchCaptureCurrentRankJobForm");
            searchCaptureCurrentRankJobForm.find("#currentPageNumberHidden").val(currentPage);
            searchCaptureCurrentRankJobForm.find("#pageSizeHidden").val(pageSize);
            searchCaptureCurrentRankJobForm.submit();
        }

        function resetPageNumber() {
            var searchCaptureCurrentRankJobForm = $("#searchCaptureCurrentRankJobForm");
            searchCaptureCurrentRankJobForm.find("#currentPageNumberHidden").val(1);
        }
        function pageLoad() {
            var searchCustomerForm = $("#searchCaptureCurrentRankJobForm");
            var pageSize = searchCustomerForm.find('#pageSizeHidden').val();
            var pages = searchCustomerForm.find('#pagesHidden').val();
            var currentPageNumber = searchCustomerForm.find('#currentPageNumberHidden').val();
            var showCustomerBottomDiv = $('#showCustomerBottomDiv');
            showCustomerBottomDiv.find("#chooseRecords").val(pageSize);

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
        function initCustomerID()
        {
            $('#searchCustomer').combogrid({
                model:'remote',
                panelWidth:450,
                value:'',
                idField:'uuid',
                textField:'contactPerson',
                dataType:'json',
                url:'/internal/crawlRanking/searchCustomer',
                columns:[[
                    {field:'contactPerson',title:'联系人',width:150},
                    {field:'telphone',title:'电话',width:100},
                    {field:'qq',title:'QQ',width:100},
                    {field:'email',title:'E-mail',width:100}
                ]],
                keyHandler:{
                    query: function(q) {
                        //动态搜索

                        $('#searchCustomer').combogrid("grid").datagrid("reload", {'contactPerson': q});
                        $('#searchCustomer').combogrid("setValue", q);
                    }
                },
                onChange:function()
                {
                    if($.isNumeric($('#searchCustomer').combogrid("getValue")))
                    {
                        initGroupNames($('#searchCustomer').combogrid("getValue"));
                    }
                }

            });
        }
        function initGroupNames(customerID)
        {

            $('#groupNames').combobox({
                url:'/internal/crawlRanking/searchGroups?customerID='+customerID, //后台获取下拉框数据的url
                method:'post',
                panelHeight:300,//设置为固定高度，combobox出现竖直滚动条
                valueField:'optimizeGroupName',
                textField:'optimizeGroupName',
                multiple:true,
                dataType:'json',
                formatter: function (row) { //formatter方法就是实现了在每个下拉选项前面增加checkbox框的方法
                    var opts = $(this).combobox('options');
                    return '<input type="checkbox" class="combobox-checkbox">' + row[opts.textField]
                },
                onLoadSuccess: function () {  //下拉框数据加载成功调用
                    var opts = $(this).combobox('options');
                    var target = this;
                    var values = "";
                    if ($(target).combobox('getValue').indexOf(",") >= 0) {
                        values = $(target).combobox('getValue').split(",");//获取选中的值的values
                    }
                    else {
                        values = (target).combobox('getValue');
                    }
                    /*var splits =values .split(",");
                     $.each(splits,function(idx,value){
                     var el = opts.finder.getEl(target, value);
                     el.find('input.combobox-checkbox')._propAttr('checked', true);
                     })*/
                    $.map(values, function (value) {
                        alert(value);
                        var el = opts.finder.getEl(target, value);
                        el.find('input.combobox-checkbox')._propAttr('checked', true);
                    })
                    $("input[type=hidden][name=groupNames]").remove();
                },
                onSelect: function (row) { //选中一个选项时调用
                    var opts = $(this).combobox('options');
                    //获取选中的值的values
                    $('#groupNames').val($(this).combobox('getValues'));

                    //设置选中值所对应的复选框为选中状态
                    var el = opts.finder.getEl(this, row[opts.valueField]);
                    el.find('input.combobox-checkbox')._propAttr('checked', true);
                },
                onUnselect: function (row) {//不选中一个选项时调用

                    var opts = $(this).combobox('options');
                    //获取选中的值的values
                    $('#groupNames').val($(this).combobox('getValues'));

                    var el = opts.finder.getEl(this, row[opts.valueField]);
                    el.find('input.combobox-checkbox')._propAttr('checked', false);
                }
            });
        }

        function showCrawlRankingForm(uuid)
        {
            $('#crawlRankingForm')[0].reset();
            $("#crawlRankingDialog").dialog({
                resizable: false,
                title:"添加抓排名任务",
                width: 500,
                height:350,
                fitColumns:true,//自动大小
                modal: true,
                //按钮
                buttons: [{
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function () {
                        saveCaptureCurrentRankJob(uuid);
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

            $('#crawlRankingDialog').window("resize",{top:$(document).scrollTop() + 100});

            initCustomerID();
            /*if(uuid=null)
            {
                initGroupNames($('#searchCustomer').combogrid("getValue"));
            }*/
        }

        function saveCaptureCurrentRankJob(uuid)
        {
            var CaptureCurrentRankJob={};
            if(uuid!=null)
            {
                CaptureCurrentRankJob.uuid=uuid;
            }
            CaptureCurrentRankJob.groupNames=$("#crawlRankingForm #groupNames").val();
            CaptureCurrentRankJob.customerID=$("#crawlRankingForm input[name=customerID]").val();
            //CaptureCurrentRankJob.operationType="";
            /*var operationType=$("#crawlRankingForm input[name=operationType]:checked");
            if(operationType.length>0)
            {
                $.each(operationType,function(idx,val){
                    CaptureCurrentRankJob.operationType=CaptureCurrentRankJob.operationType+$(val).val()+",";
                })
                CaptureCurrentRankJob.operationType=CaptureCurrentRankJob.operationType.substring(0,CaptureCurrentRankJob.operationType.length-1);
            }*/
            CaptureCurrentRankJob.exectionType=$("#crawlRankingForm input[name=exectionType]:checked").val();
            CaptureCurrentRankJob.exectionTime=$("#crawlRankingForm #exectionTime").val();
            CaptureCurrentRankJob.rowNumber=$("#crawlRankingForm #rowNumber").val();

            alert(JSON.stringify(CaptureCurrentRankJob));
            $.ajax({
                url: '/internal/crawlRanking/saveCaptureCurrentRankJob',
                data:JSON.stringify(CaptureCurrentRankJob),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "保存成功",true);


                    } else {
                        $().toastmessage('showErrorToast', "保存失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "保存失败");
                }
            });
        }

        function initCrawlRankingForm(uuid)
        {
            $.ajax({
                url: '/internal/crawlRanking/getCaptureCurrentRankJob?uuid='+uuid,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        //$("#crawlRankingForm #groupNames").val(data.groupNames);

                        $("#crawlRankingForm input[name=customerID]").val(data.customerID);
                        initGroupNames($('#searchCustomer').combogrid("getValue"));
                        $("#crawlRankingForm #groupNames").combobox('setValue',data.groupNames);
                        /*$('#crawlRankingForm groupNames').combobox('setValues',data.groupNames.split(","));*/
                        /*var split = data.operationType.split(",");
                        for(var i=0;i<split.length;i++)
                        {
                            $("input:checkbox[name=operationType][value="+split[i]+"]").attr('checked',true);
                        }*/
                        $("#crawlRankingForm input[name=exectionType][value="+data.exectionType+"]").attr("checked",true);
                        $("#crawlRankingForm #exectionTime").val(data.exectionTime);
                        /*$("#crawlRankingForm #rowNumber").val(data.rowNumber);*/
                        $('#crawlRankingForm #rowNumber').spinner('setValue',data.rowNumber);

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

        function modifyCaptureCurrentRankJob(uuid)
        {

            initCrawlRankingForm(uuid);
            showCrawlRankingForm(uuid);


        }

        function deleteCaptureCurrentRankJob(uuid)
        {
            $.ajax({
                url: '/internal/crawlRanking/deleteCaptureCurrentRankJob?uuid='+uuid,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "删除成功",true);
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

        function deleteCaptureCurrentRankJobs()
        {

            $.ajax({
                url: '/internal/crawlRanking/deleteCaptureCurrentRankJobs?uuids='+getUuids(),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "删除成功",true);
                    }
                    else {
                        $().toastmessage('showErrorToast', "删除失败",true);
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "删除失败",true);
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
            var select=0;
            for(var i = 0; i < a.length; i++){
                if (a[i].checked == true){
                    select++;
                }
            }
            if(select == a.length){
                $("#selectAllChecked").prop("checked",true);
            }else {
                $("#selectAllChecked").prop("checked",false);
            }
        }
    </script>
</head>
<body>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 35px">
        <form method="post" id="searchCaptureCurrentRankJobForm" action="/internal/crawlRanking/searchCaptureCurrentRankJob" style="margin-bottom:0px ">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}" />
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            组名:<input type="text" name="groupNames" value="${captureCurrentRankJobCriteria.groupNames}">
            客户ID:<input type="text" name="customerID" value="${captureCurrentRankJobCriteria.customerID}">
            操作类型:
            <select name="operationType">
                <option value="">请选择终端类型</option>
                <option value="PC" <c:if test="${captureCurrentRankJobCriteria.operationType.equals('PC')}">selected="selected"</c:if>>PC</option>
                <option value="Phone" <c:if test="${captureCurrentRankJobCriteria.operationType.equals('Phone')}">selected="selected"</c:if>>Phone</option>
            </select>
            执行类型:
            <select name="exectionType">
                <option value="">请选择执行类型</option>
                <option value="Once" <c:if test="${captureCurrentRankJobCriteria.exectionType.equals('Once')}">selected="selected"</c:if>>Once</option>
                <option value="Everyday" <c:if test="${captureCurrentRankJobCriteria.exectionType.equals('Everyday')}">selected="selected"</c:if>>Everyday</option>
            </select>&nbsp;&nbsp;
            <input type="submit" value="查询">&nbsp;&nbsp;
            <input type="button" value="添加" onclick="showCrawlRankingForm()">&nbsp;&nbsp;
            <input type="button" value="删除所选" onclick="deleteCaptureCurrentRankJobs()">&nbsp;&nbsp;
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=40>客户ID</td>
            <td align="center" width=80>组名</td>
            <td align="center" width=60>操作类型</td>
            <td align="center" width=60>执行类型</td>
            <td align="center" width=100>执行时间</td>
            <td align="center" width=40>抓取截止条数</td>
            <td align="center" width=60>状态</td>
            <td align="center" width=60>创建人</td>
            <td align="center" width=90>创建时间</td>
            <td align="center" width=60>修改人</td>
            <td align="center" width=90>修改时间</td>
            <td align="center" width=90>开始抓取时间</td>
            <td align="center" width=90>最后抓取时间</td>
            <td align="center" width=60>最后执行日期</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="centerDiv">
    <table style="font-size:12px; width: 100%;" id="showCaptureCurrentRankJobTable">
        <c:forEach items="${page.records}" var="captureCurrentRankJob" varStatus="status">
        <tr align="left" onmouseover="doOver(this);" onmouseout="doOut(this);" height=30  <c:if test="${status.index%2==0}">bgcolor="#eee"</c:if> >
            <td width=10 align="center"><input type="checkbox" name="uuid" value="${captureCurrentRankJob.uuid}" onclick="decideSelectAll()"/></td>
            <td width=40>${captureCurrentRankJob.customerID}</td>
            <td width=80>${captureCurrentRankJob.groupNames}</td>
            <td width=60>${captureCurrentRankJob.operationType}</td>
            <td width=60>${captureCurrentRankJob.exectionType}</td>
            <td width=60><fmt:formatDate value="${captureCurrentRankJob.exectionTime}" pattern="HH:mm:ss"/></td>
            <td width=40>${captureCurrentRankJob.rowNumber}</td>
            <td width=50>${captureCurrentRankJob.exectionStatus}</td>
            <td width=60>${captureCurrentRankJob.createBy}</td>
            <td width=90><fmt:formatDate value="${captureCurrentRankJob.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td width=60>${captureCurrentRankJob.updateBy}</td>
            <td width=90><fmt:formatDate value="${captureCurrentRankJob.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td width=90><fmt:formatDate value="${captureCurrentRankJob.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td width=90><fmt:formatDate value="${captureCurrentRankJob.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td width=60>${captureCurrentRankJob.lastExecutionDate}</td>
            <td width=80>
                <a href="javascript:modifyCaptureCurrentRankJob('${captureCurrentRankJob.uuid}')">修改</a>
                <a href="javascript:deleteCaptureCurrentRankJob('${captureCurrentRankJob.uuid}')">删除</a>
            </td>
        </tr>
        </c:forEach>
    </table>
</div>
<div id="crawlRankingDialog" title="" class="easyui-dialog" style="left: 35%;">
<form id="crawlRankingForm">
    <ul>
        <input type="hidden" name="captureCurrentRankJobUuid" id="captureCurrentRankJobUuid">
        <li><span>输入客户名:</span><input type="text" name="customerID" id="searchCustomer" style="width: 200px"></li>
        <li><span>输入优化组名:</span><input type="text" name="groupNames" id="groupNames" class="easyui-combobox" style="width: 200px"></li>
       <%--<li><span>操作类型:</span><input type="checkbox" name="operationType"  value="PC">电脑端</label><input type="checkbox" name="operationType" value="Phone">手机端</li>--%>
        <li><span>执行方式:</span><input type="radio" name="exectionType" checked  value="Once">一次性</label><input type="radio" name="exectionType" value="Everyday">每天</li>
        <li><span>执行时间:</span><input type="text" class="Wdate" name="exectionTime" id="exectionTime" onfocus="WdatePicker({lang:'zh-cn',dateFmt:'HH:mm:ss'})"></li>
        <li><span>抓取截止条数:</span><input id="rowNumber" name="rowNumber" class="easyui-numberspinner"  data-options="min:0,max:1000,increment:50"></li>
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