
<%@ include file="/commons/basejs.jsp" %>
<%@ include file="/commons/global.jsp" %>
<script language="javascript" type="text/javascript" src="/common.js"></script>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<script type="text/javascript">
    /*页面进入即刻加载*/
    $(function() {
      pageLoad();
        $("#showMainKeywordTableDiv").css("margin-top",$("#showMainKeywordTopDiv").height());
        $("#showAddMainKeywordDialog").dialog("close");
        alignTableHeader();
        window.onresize = function(){
            alignTableHeader();
        }
    });
    function pageLoad() {
      var  showMainKeywordBottomDiv = $('#showMainKeywordBottomDiv');
        $("#showMainKeywordBottomDiv").find('#chooseRecords').val(${page.size});
      var selectGroup = $('#serachMainKeywordForm').find("#itemGroupHidden").val();
      $('#serachMainKeywordForm').find("#itemGroup").val(selectGroup);
      var pages  = showMainKeywordBottomDiv.find('#pagesHidden').val();
      showMainKeywordBottomDiv.find('#pagesHidden').val(pages);
      var currentPage  = showMainKeywordBottomDiv.find('#currentPageHidden').val();
      showMainKeywordBottomDiv.find('#currentPageHidden').val(currentPage);
      if(currentPage<=1){
        currentPage=1;
        showMainKeywordBottomDiv.find("#fisrtButton").attr("disabled","disabled");
        showMainKeywordBottomDiv.find("#upButton").attr("disabled","disabled");
      }else if(currentPage>=pages){
        currentPage=pages;
        showMainKeywordBottomDiv.find("#nextButton").attr("disabled","disabled");
        showMainKeywordBottomDiv.find("#lastButton").attr("disabled","disabled");
      }else {
        showMainKeywordBottomDiv.find("#firstButton").removeAttr("disabled");
        showMainKeywordBottomDiv.find("#upButton").removeAttr("disabled");
        showMainKeywordBottomDiv.find("#nextButton").removeAttr("disabled");
        showMainKeywordBottomDiv.find("#lastButton").removeAttr("disabled");
      }
    }
    function alignTableHeader(){
        var td = $("#headerTable tr:first td");
        var ctd = $("#showMainKeywordTable tr:first td");
        $.each(td, function (idx, val) {
            ctd.eq(idx).width($(val).width());
        });
    }
    //增加
    function showAddMainKeywordDialog(uuid) {
      if(uuid==null){
        $('#mainKeywordForm')[0].reset();
      }
      $("#showAddMainKeywordDialog").dialog({
        resizable: false,
        width: 350,
        height: 365,
        modal: true,
        //按钮
        buttons:[{
            text:"保存",
            iconCls: 'icon-ok',
            handler : function() {
            savaMainKeyword(uuid);
          }
        },{
            text: '清空',
            iconCls: 'fi-trash',
            handler: function () {
                $('#mainKeywordForm')[0].reset();
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#showAddMainKeywordDialog").dialog("close");
                $('#mainKeywordForm')[0].reset();
            }
        }]
      });
        $("#showAddMainKeywordDialog").dialog("open");
        $("#showAddMainKeywordDialog").window("resize",{top:$(document).scrollTop() + 100});
    }
    function savaMainKeyword(uuid) {
      var mainKeywordObj = {};
      mainKeywordObj.uuid = uuid;
      var mainKeywordForm = $('#mainKeywordForm');
      mainKeywordObj.keyword = mainKeywordForm.find('#mKeyword').val().trim();
      mainKeywordObj.group = mainKeywordForm.find('#mGroup').val();

      var ngKeywords = mainKeywordForm.find('#ngKeyword').val().split(',');
      if (mainKeywordObj.keyword === "") {
        alert("关键字不能为空");
        mainKeywordForm.find('#mKeyword').focus();
        return false;
      }
      if (mainKeywordObj.keyword === "") {
        alert("请选择有效城市");
        mainKeywordForm.find('#mGroup').focus();
        return false;
      }
      if (ngKeywords === "" || ngKeywords === "null") {
        alert("请输入需要投诉的负面词汇");
        mainKeywordForm.find('#ngKeyword').focus();
        return false;
      }
      mainKeywordObj.tsNegativeKeywords = [];
      $.each(ngKeywords, function (idx, val) {
        var ngKeywordObj = {"keyword": val.trim()};
        mainKeywordObj.tsNegativeKeywords.push(ngKeywordObj);
      });
      $.ajax({
        url: '/internal/complaints/save',
        data: JSON.stringify(mainKeywordObj),
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
          if (result) {
            $().toastmessage('showSuccessToast', "保存成功!", true);
          } else {
              $().toastmessage('showErrorToast',"保存失败");
          }
        },
        error: function () {
           $().toastmessage('showErrorToast',"保存失败");
        }
      });
      $("#showAddMainKeywordDialog").dialog("close");
      $('#mainKeywordForm')[0].reset();
    }
    //通过uuid查找mainKey对象
    function getMainKeyword(uuid) {
      $.ajax({
        url: '/internal/complaints/findTSMainKeywordById/' + uuid,
        type: 'Get',
        success: function (tsMainKeyword) {
          if (tsMainKeyword != null ) {
            initMainKeywordDialog(tsMainKeyword);
            showAddMainKeywordDialog(tsMainKeyword.uuid);
          } else {
              $().toastmessage('showErrorToast',"获取信息失败！");
          }
        },
        error: function () {
            $().toastmessage('showErrorToast',"获取信息失败！");
        }
      });
    }
    function initMainKeywordDialog(mainKeyword) {
      var mainKeywordForm = $("#mainKeywordForm");
      mainKeywordForm.find("#mUuid").val(mainKeyword.uuid);
      mainKeywordForm.find("#mKeyword").val(mainKeyword.keyword);
      mainKeywordForm.find("#mGroup").val(mainKeyword.group);
      var ngKeyword =  mainKeyword.tsNegativeKeywords;
      var tmpNegativeKeywords = '';
      $.each(ngKeyword,function (idx,val) {
          tmpNegativeKeywords = tmpNegativeKeywords + val.keyword+',';
      });
      var negativeKeywords = tmpNegativeKeywords.substring(0,tmpNegativeKeywords.length-1);
      mainKeywordForm.find("#ngKeyword").val(negativeKeywords);
    }

    //删除
    function deleteMainKeyword(uuid) {
      if (confirm("确实要删除这个主关键字吗?") == false) return;
      $.ajax({
        url: '/internal/complaints/delete/' + uuid,
        type: 'Get',
        success: function (result) {
          if (result) {
              $().toastmessage('showSuccessToast',"删除成功！", true);
          } else {
              $().toastmessage('showErrorToast',"删除失败！");
          }
        },
        error: function () {
            $().toastmessage('showErrorToast',"删除失败！");
        }
      });
    }
    function getSelectedIDs() {
      var uuids = '';
      $.each($("input[name=uuid]:checkbox:checked"), function(){
        if(uuids === ''){
          uuids = $(this).val();
        }else{
          uuids = uuids + "," + $(this).val();
        }
      });
      return uuids;
    }
    //删除所选
    function deleteMainKeywords(self) {
      var uuids = getSelectedIDs();
      if(uuids === ''){
        alert('请选择要操作的设置信息！');
        return ;
      }
      if (confirm("确实要删除这些投诉关键字吗?") == false) return;
      var postData = {};
      postData.uuids = uuids.split(",");
      $.ajax({
        url: '/internal/complaints/deleteTSMainKeywords',
        data: JSON.stringify(postData),
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
          if(data){
              $().toastmessage('showSuccessToast',"操作成功！", true);
          }else{
              $().toastmessage('showErrorToast', "操作失败！");
          }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败！");
        }
      });
    }

    //查询
    function serachMainKeywords(currentPage, displaysRecords) {
      var keyword = $("#serachMainKeyword").find("#itemKeyword").val();
      var group = $("#serachMainKeyword").find("#itemgroup").val();
      var showMainKeywordBottomDiv = $("#showMainKeywordBottomDiv");
      var pages = showMainKeywordBottomDiv.find("#pagesHidden").val();
     var url= '/internal/complaints/findTSMainKeywords?currentPage='+currentPage+'&displaysRecords='+displaysRecords+'&keyword='+ keyword+'&group='+ group;
      window.location.href=url;
    }
    //改变当前页
    function chooseRecords(currentPage, displayRecords) {
      $('#showMainKeywordBottomDiv').find("#currentPageHidden").val(currentPage);
      $('#showMainKeywordBottomDiv').find("#displaysRecordsHidden").val(displayRecords);
      serachMainKeywords(currentPage, displayRecords);
    }

    //弹出提示框
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
    function selectAll(self){
      var a = document.getElementsByName("uuid");
      if(self.checked){
        for(var i = 0;i<a.length;i++){
            a[i].checked = true;
        }
      }else{
        for(var i = 0;i<a.length;i++){
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
</script>
<style type="text/css">
    body {
        font-size: 12px;
        text-align:center;
    }

    #showMainKeywordTopDiv {
        position: fixed;
        top: 0px;
        left: 0px;
        background-color: white;
        width: 100%;
    }



    #showAddMainKeywordDialog {
        margin:0 auto;
    }
    #serachMainKeyword{
        width: 100%;
    }
    #serachMainKeywordForm{
        text-align: left;
    }


    #showMainKeywordBottomPositioneDiv{
        position: fixed;
        bottom: 0px;
        right: 0px;
        background-color:#dedede;
        padding-top: 5px;
        padding-bottom: 5px;
        width: 100%;
    }
    #showMainKeywordBottomDiv {
        float: right;
        margin-right: 10px;
    }

    #nav .mainLevel ul {display:none; position:absolute;z-index: 10;}
    #nav .mainLevel li {border-top:1px solid #fff; background:#ffe60c; width:140px;z-index: 10;/*IE6 only*/}

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
    input[type="button"]{
        padding: 2px;
        border-radius: 5px;
        border: 1px solid #bbb;
        background-color: white;
    }
    input[type="submit"]{
        padding: 2px;
        border-radius: 5px;
        border: 1px solid #bbb;
        background-color: white;
    }
</style>
<head>
    <title>投诉专用平台</title>
</head>
<body>
<div id="showMainKeywordTopDiv">
    <%@include file="/menu.jsp" %>
    <div id="serachMainKeyword" style="margin-top: 35px;">
        <form id="serachMainKeywordForm" action="/internal/complaints/findTSMainKeywords" method="post">
            主关键词&nbsp;&nbsp;<input id="itemKeyword" name="itemKeyword" type="text"
                                   value="${page.condition.get("keyword")}"/>&nbsp;&nbsp;
            <input id="itemGroupHidden" type="hidden" value="${page.condition.get("group")}"/>
            区域分组&nbsp;&nbsp;<select id="itemGroup" name="itemGroup" style="height: 21px;">
            <option value="">请 选 择 城 市</option>
            <option value="北京">北京</option>
            <option value="上海">上海</option>
            <option value="广州">广州</option>
            <option value="深圳">深圳</option>
        </select>
            &nbsp;&nbsp;
            <input type="hidden" id="currentPageHidden" name="currentPageHidden" value="${page.current}"/>
            <input type="hidden" id="displaysRecordsHidden" name="displaysRecordsHidden" value="${page.size}"/>
            <shiro:hasPermission name="/internal/complaints/findTSMainKeywords">
            <input type="submit" class="ui-button ui-widget ui-corner-all" style="z-index: 0" ; value=" 查询 ">&nbsp;&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/complaints/save">
            <input type="button" class="ui-button ui-widget ui-corner-all" style="z-index: 0" ; onclick="showAddMainKeywordDialog(null)" value=" 添加 "/>&nbsp;&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/complaints/deleteTSMainKeywords">
            <input type="button" class="ui-button ui-widget ui-corner-all" style="z-index: 0" ; onclick="deleteMainKeywords(this)" value=" 删除所选 "/>
            </shiro:hasPermission>
        </form>
        <table id="headerTable" style="width:100%;">
            <tr bgcolor="#eeeeee" height=30>
                <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
                <td align="center" width=150>主词</td>
                <td align="center" width=60>分组</td>
                <td align="center" width=400>负面语句</td>
                <td align="center" width=100>更新时间</td>
                <td align="center" width=100>创建时间</td>
                <td align="center" width=80>操作</td>
                <div id="div1"></div>
                <div id="div2"></div>
            </tr>
        </table>
    </div>
</div>
<div id="showMainKeywordTableDiv" style="margin-bottom: 30px">
        <table id="showMainKeywordTable">
            <c:forEach items="${page.records }" var="mainkey" varStatus="status">
                <tr onmouseover="doOver(this)" onmouseout="doOut(this)" ondblclick="getMainKeyword('${mainkey.uuid}')" <c:if test="${status.index%2==0}">bgcolor="#eee" </c:if> >
                    <td width=10 align="center"><input type="checkbox" name="uuid" value="${mainkey.uuid}" onclick="decideSelectAll()"/></td>
                    <input type="hidden" id="mkUuid" value="${mainkey.uuid}"/>
                    <td width=150>${mainkey.keyword }</td>
                    <td width=60>${mainkey.group }</td>
                    <td width=400>
                        <c:forEach items="${mainkey.tsNegativeKeywords}" var="ngkey">
                            <input type="hidden" id="ngkeyUuid" value="${ngkey.uuid}"/>
                            <c:choose>
                                <c:when test="${ngkey.pcAppeared ==1 || ngkey.phoneAppeared ==1}">
                                        <span style="color: crimson" >${ngkey.keyword}</span>&nbsp;&nbsp;&nbsp;
                                </c:when>
                                <c:otherwise>
                                    ${ngkey.keyword}&nbsp;&nbsp;&nbsp;
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </td>
                    <td width=100>
                        <c:choose>
                            <c:when test="${mainkey.updateTime==null}">
                                ${mainkey.updateTime}
                            </c:when>
                            <c:otherwise>
                                <fmt:formatDate value="${mainkey.updateTime}" pattern="yy-MM-dd HH:mm"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td width=100><fmt:formatDate value="${mainkey.createTime}" pattern="yy-MM-dd HH:mm"/></td>
                    <td>
                    <shiro:hasPermission name="/internal/complaints/save">
                        &nbsp;&nbsp;&nbsp;<a href="javascript:getMainKeyword('${mainkey.uuid}')">修改</a>&nbsp;&nbsp;&nbsp;
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/complaints/delete">
                        <a href="javascript:deleteMainKeyword('${mainkey.uuid}')">删除</a>
                    </shiro:hasPermission>
                    </td>
                </tr>
            </c:forEach>
        </table>
</div>
<div id="showMainKeywordBottomPositioneDiv">
    <div id="showMainKeywordBottomDiv">
        <input id="fisrtButton" class="ui-button ui-widget ui-corner-all" type="button"  onclick="serachMainKeywords(1,'${page.size}')" value="首页"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="upButton" type="button" class="ui-button ui-widget ui-corner-all" onclick="serachMainKeywords('${page.current-1}','${page.size}')" value="上一页"/>&nbsp;&nbsp;&nbsp;&nbsp;
        ${page.current}/${page.pages}&nbsp;&nbsp;
        <input id="nextButton"  type="button" class="ui-button ui-widget ui-corner-all"  onclick="serachMainKeywords('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')" value="下一页">&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="lastButton" type="button" class="ui-button ui-widget ui-corner-all" onclick="serachMainKeywords('${page.pages}','${page.size}')" value="末页">&nbsp;&nbsp;&nbsp;&nbsp;
        总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;
        每页显示条数:<select id="chooseRecords"  onchange="chooseRecords(${page.current},this.value)">
        <option>10</option>
        <option>25</option>
        <option>50</option>
        <option>75</option>
        <option>100</option>
    </select>
        <%--用于存储pageInfo--%>
        <input type="hidden" id="currentPageHidden" value="${page.current}"/>
        <input type="hidden" id="displaysRecordsHidden" value="${page.size}"/>
        <input type="hidden" id="pagesHidden" value="${page.pages}"/>
    </div>
</div>
<div id="showAddMainKeywordDialog" class="easyui-dialog" title="添加投诉关键字" style="left: 35%;">
    <form id="mainKeywordForm" action="show.jsp">
        <table style="border-spacing:15px;">
            <tr>
                <input id="mUuid" type="hidden">
                <td>主关键字</td>
                <td><input id="mKeyword" type="text" style="width: 250px;"></td>
            </tr>
            <tr>
                <td>地区分组</td>
                <td>
                    <select id="mGroup" style="width: 250px;">
                        <option value="">-----------请 选 择 城 市-----------</option>
                        <option value="北京">北京</option>
                        <option value="上海">上海</option>
                        <option value="广州">广州</option>
                        <option value="深圳">深圳</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>负面词</td>
                <td>
                    <textarea id="ngKeyword" style="resize: none;height: 180px;width: 250px" placeholder="请用逗号作为分割符"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>