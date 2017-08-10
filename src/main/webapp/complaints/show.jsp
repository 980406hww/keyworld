<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script language="javascript" type="text/javascript" src="/common.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script language="javascript" type="text/javascript" src="/js/slide1.12.4.js"></script>
<link href="/css/menu.css" rel="stylesheet" type="text/css"/>
<%@page contentType="text/html;charset=utf-8" %>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*,java.net.URLEncoder" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager"/>
<jsp:useBean id="sch" scope="page" class="com.keymanager.util.SpringContextHolder"/>
<%@include file="/check.jsp" %>
<%@page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath =
            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<script type="text/javascript">
    /*页面进入即刻加载*/
    $(function() {
      $("#showAddMainKeywordDlog").hide();
      var displaysRecords =  $('#showMainKeywordBottomDiv').find('#displaysRecords').val();
      $('#showMainKeywordBottomDiv').find('#chooseRecords').val(displaysRecords);
    });
    //增加
    function showAddMainKeywordDlog(uuid) {
      if(uuid==null){
        $('#mainKeywordForm')[0].reset();
      }
      $("#showAddMainKeywordDlog").dialog({
        resizable: false,
        width: 400,
        height: 440,
        modal: true,
        //按钮
        buttons: {
          "保存": function() {
            savaMainKeyword(uuid);
          },
          "清空": function() {
            $('#mainKeywordForm')[0].reset();
          },
          "取消": function() {
            $(this).dialog("close");
            $('#mainKeywordForm')[0].reset();
          }
        }
      });
    }
    function savaMainKeyword(uuid) {
      var mainkeyObj = {};
      mainkeyObj.uuid = uuid;
      var mainKeywordForm = $('#mainKeywordForm');
      mainkeyObj.keyword = mainKeywordForm.find('#mKeyword').val().trim();
      mainkeyObj.group = mainKeywordForm.find('#mGroup').val();

      var ngKeywords = mainKeywordForm.find('#ngKeyword').val().split(',');
      if (mainkeyObj.keyword === "") {
        alert("关键字不能为空");
        mainKeywordForm.find('#mKeyword').focus();
        return false;
      }
      if (mainkeyObj.keyword === "") {
        alert("请选择有效城市");
        mainKeywordForm.find('#mGroup').focus();
        return false;
      }
      if (ngKeywords === "" || ngKeywords === "null") {
        alert("请输入需要投诉的负面词汇");
        mainKeywordForm.find('#ngKeyword').focus();
        return false;
      }
      mainkeyObj.tsNegativeKeywords = [];
      $.each(ngKeywords, function (idx, val) {
        var ngKeywordObj = {"keyword": val.trim()};
        mainkeyObj.tsNegativeKeywords.push(ngKeywordObj);
      });
      $.ajax({
        url: '/spring/complaints/save',
        data: JSON.stringify(mainkeyObj),
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
          if (result) {
            showInfo("保存成功！", self);
            window.location.reload();
          } else {
            showInfo("保存失败！", self);
            window.location.reload();
          }
        },
        error: function () {
          showInfo("保存失败！", self);
        }
      });
      $("#showAddMainKeywordDlog").dialog("close");
      $('#mainKeywordForm')[0].reset();
    }
    //通过uuid查找mainKey对象
    function getMainKeyword(uuid) {
      $.ajax({
        url: '/spring/complaints/findTSMainKeywordById/' + uuid,
        type: 'Get',
        success: function (tsMainKeyword) {
          if (tsMainKeyword != null ) {
            initMainKeywordDialog(tsMainKeyword);
            showAddMainKeywordDlog(tsMainKeyword.uuid);
          } else {
            showInfo("获取信息失败！", self);
          }
        },
        error: function () {
          showInfo("获取信息失败！", self);
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
        url: '/spring/complaints/delete/' + uuid,
        type: 'Get',
        success: function (result) {
          if (result) {
            showInfo("删除成功！", self);
            window.location.reload();
          } else {
            showInfo("删除失败！", self);
          }
        },
        error: function () {
          showInfo("删除失败！", self);
          window.location.reload();
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
        url: '/spring/complaints/deleteTSMainKeywords',
        data: JSON.stringify(postData),
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
          if(data){
            showInfo("操作成功！", self);
            window.location.reload();
          }else{
            showInfo("操作失败！", self);
            window.location.reload();
          }
        },
        error: function () {
          showInfo("操作失败！", self);
          window.location.reload();
        }
      });
    }

    //查询
    function serachMainKeywords(cp,ps) {
      var tsMainKeyword = {};
      tsMainKeyword.keyword = $("#serachMainKeyword").find("#itemkeywork").val();
      tsMainKeyword.group = $("#serachMainKeyword").find("#itemgroup").val();
      tsMainKeyword.tsNegativeKeywords = [];
      var ngKeywordObj = {};
      ngKeywordObj.keyword = '';
      tsMainKeyword.tsNegativeKeywords.push(ngKeywordObj);

      var currentPageSD=$('#showMainKeywordBottomDiv').find("#currentpage").val();
      var displaysRecordsSD=$('#showMainKeywordBottomDiv').find("#displaysRecords").val();
      var currentPage = cp!=currentPageSD?cp:currentPageSD;
      var displaysRecords = ps!=displaysRecordsSD?ps:displaysRecordsSD;
      
     var url= '/spring/complaints/findTSMainKeywords?currentPage='+currentPage+'&displaysRecords='+displaysRecords+'&keyword='+tsMainKeyword.keyword+'&group='+tsMainKeyword.group;
      window.location.href=url;
     /*$.ajax({
        url: url,
        data: JSON.stringify(tsMainKeyword),//传回一个查询对象{'tsMainKeyword':}
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'GET',
        success: function (data) {
          if (data != null && data != "") {
            showInfo("更新成功！", self);
            window.location.reload();
          } else {
            showInfo("更新失败！", self);
            window.location.reload();
          }
        },
        error: function () {
          showInfo("更新失败！", self);
        }
      });*/
    }
    //改变当前页
    function chooseRecords(cp,ps) {
      $('#showMainKeywordBottomDiv').find("#currentpage").val(cp);
      $('#showMainKeywordBottomDiv').find("#displaysRecords").val(ps);
      serachMainKeywords(cp,ps);
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

    //关闭层div1的显示
    function closeTip() {
      var div1 = document.getElementById('div1');
      div1.style.display = "none";
    }
    function selectAll(self){
      var a = document.getElementsByName("uuid");
      if(self.checked){
        for(var i = 0;i<a.length;i++){
          if(a[i].type == "checkbox"){
            a[i].checked = true;
          }
        }
      }else{
        for(var i = 0;i<a.length;i++){
          if(a[i].type == "checkbox"){
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
</script>
<style type="text/css">
    body {
        font-size: 15px;
        text-align:cente;
    }

    #showMainKeywordTopDiv {
        width: 100%;
        margin:0 auto;
    }

    #showMainKeywordTableDiv {
        width: 100%;
        height: 650px;
        margin:auto;
    }
    #showMainKeywordTable{
        width: 100%;
    }
    #showMainKeywordTable td {

    }

    #showAddMainKeywordDlog {
        margin:0 auto;
    }
    #serachMainKeyword{
        width: 100%;
    }
    #showMainKeywordBottomDiv{
        margin-left: 63%;
    }

    #nav .mainlevel ul {display:none; position:absolute;z-index: 10;}
    #nav .mainlevel li {border-top:1px solid #fff; background:#ffe60c; width:140px;z-index: 10;/*IE6 only*/}

    #div1 {
        display: none;
        background-color: #f6f7f7;
        color: #333333;
        font-size: 12px;
        line-height: 18px;
        border: 1px solid #e1e3e2;
        width: 450;
        height: 50;
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
</style>
<head>
    <base href="<%=basePath%>">
    <title>投诉专用平台</title>
</head>
<body>
<div id="showMainKeywordBodyDiv">
    <div id="showMainKeywordTopDiv">
        <div>
            <table width=100% style="font-size:12px;" cellpadding=3>
                <tr>
                    <td colspan="15" align="left">
                        <%@include file="/menu.jsp" %>
                    </td>
                </tr>
            </table>
        </div>
        <div id="serachMainKeyword">
           <form id="serachMainKeywordForm" action="/spring/complaints/findTSMainKeywords" method="post">
               主关键词<input id="itemkeywork" name="itemkeywork" type="text" value="${pageInfo.searchCondition.get("keyword")}"/>&nbsp;&nbsp;
               区域分组<input id="itemGroup" name="itemGroup" type="text" value="${pageInfo.searchCondition.get("group")}"/>&nbsp;&nbsp;
               <input type="submit" class="ui-button ui-widget ui-corner-all" style="z-index: 0";
                       value="查询">&nbsp;&nbsp;&nbsp;
               <input type="button" class="ui-button ui-widget ui-corner-all"  style="z-index: 0";
                      onclick="showAddMainKeywordDlog(null)" value="添加"/>&nbsp;&nbsp;&nbsp;
               <input type="button" class="ui-button ui-widget ui-corner-all"  style="z-index: 0";
                      onclick="deleteMainKeywords(this)" value="删除所选"/>
           </form>
        </div>
    </div>
    <div id="showMainKeywordTableDiv">
        <table id="showMainKeywordTable">
            <tr bgcolor="#eeeeee" height=30>
                <td align="center" width=10><input type="checkbox" onclick="selectAll(this)"/></td>
                <td align="center" width=150>主词</td>
                <td align="center" width=100>分组</td>
                <td align="center" width=200>负面语句</td>
                <td align="center" width=250>更新时间</td>
                <td align="center" width=250>创建时间</td>
                <td align="center" width=150>操作</td>
                <div id="div1"></div>
                <div id="div2"></div>
            </tr>
            <c:forEach items="${pageInfo.content }" var="mainkey">
                <tr onmouseover="doOver(this)" onmouseout="doOut(this)" ondblclick="getMainKeyword('${mainkey.uuid}')">
                    <td><input type="checkbox" name="uuid" value="${mainkey.uuid}" /></td>
                    <input type="hidden" id="mkUuid" value="${mainkey.uuid}"/>
                    <td>${mainkey.keyword }</td>
                    <td>${mainkey.group }</td>
                    <td>
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
                    <td>
                        <c:choose>
                            <c:when test="${mainkey.updateTime==null}">
                                ${mainkey.updateTime}
                            </c:when>
                            <c:otherwise>
                                <fmt:formatDate value="${mainkey.updateTime}" type="both"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><fmt:formatDate value="${mainkey.createTime}"
                                        type="both"/></td>
                    <td>&nbsp;&nbsp;&nbsp;<a href="javascript:getMainKeyword('${mainkey.uuid}')">修改</a>&nbsp;&nbsp;&nbsp;
                    <a href="javascript:deleteMainKeyword('${mainkey.uuid}')">删除</a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div id="showAddMainKeywordDlog" title="添加投诉关键字">
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
                        <textarea id="ngKeyword" style="resize: none;height: 180px;width: 250px"
                                  placeholder="请用逗号作为分割符"></textarea>
                    </td>
                </tr>
            </table>
        </form>
    </div>
<hr>
    <div id="showMainKeywordBottomDiv">
        <a class="ui-button ui-widget ui-corner-all" href="javascript:serachMainKeywords(1,'${pageInfo.displaysRecords}')">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;
            <a class="ui-button ui-widget ui-corner-all" href="javascript:serachMainKeywords('${pageInfo.currentpage-1}','${pageInfo.displaysRecords}')">上一页</a>&nbsp;&nbsp;&nbsp;&nbsp;
                ${pageInfo.currentpage}/${pageInfo.totalPage}&nbsp;&nbsp;
                <a class="ui-button ui-widget ui-corner-all"  href="javascript:serachMainKeywords('${pageInfo.currentpage+1}','${pageInfo.displaysRecords}')">下一页</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a class="ui-button ui-widget ui-corner-all" href="javascript:serachMainKeywords('${pageInfo.totalPage}','${pageInfo.displaysRecords}')">末页</a>&nbsp;&nbsp;&nbsp;&nbsp;
                        总记录数:${pageInfo.totalSize}&nbsp;&nbsp;&nbsp;&nbsp;
                        每页显示条数:<select id="chooseRecords"  onchange="chooseRecords(${pageInfo.currentpage},this.value)">
                            <option>15</option>
                            <option>25</option>
                            <option>35</option>
                            <option>45</option>
                        </select>
        <%--用于存储pageInfo--%>
        <input type="hidden" id="currentpage" value="${pageInfo.currentpage}"/>
        <input type="hidden" id="displaysRecords" value="${pageInfo.displaysRecords}"/>
    </div>
</div>
</body>
</html>