<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link href="/css/menu.css" rel="stylesheet" type="text/css"/>
<%@page contentType="text/html;charset=utf-8" %>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*,java.net.URLEncoder" %>
<%@page import="com.keymanager.monitoring.entity.QZSetting" %>
<%@page import="com.keymanager.monitoring.service.QZSettingService" %>
<%@page import="com.keymanager.monitoring.service.QZOperationTypeService" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager"/>
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager"/>
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
    /*页面进入即可加载*/
    $(function() {
      $("#showAddMainKeywordDlog").hide();
    });
    //增加
    function showAddMainKeywordDlog(uuid) {
      if(uuid==null){
        $('#mainKeywordForm')[0].reset();
      }
      $("#showAddMainKeywordDlog").dialog({
        resizable: false,
        width: 400,
        height: 450,
        modal: true,
        //按钮
        buttons: {
          "保存": function() {
            savaMainKeyword(uuid);
            $(this).dialog("close");
            $('#mainKeywordForm')[0].reset();
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
      mainkeyObj.keyword = $('#mainKeywordForm').find('#mKeyword').val();
      mainkeyObj.group = $('#mainKeywordForm').find('#mGroup').val();
      mainkeyObj.tsNegativeKeywords = [];
      var ngKeywords = $('#mainKeywordForm').find('#ngKeyword').val().split(',');
      $.each(ngKeywords,function (idx,val) {
        var ngKeywordObj = {};
        ngKeywordObj.keyword = val;
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
          success: function (data) {
            if (data != null && data != "") {
              showInfo("添加成功！", self);
              window.location.reload();
            } else {
              showInfo("添加失败！", self);
              window.location.reload();
            }
          },
          error: function () {
            showInfo("添加失败！", self);
          }
        });
    }
    //通过uuid查找mainKey对象
    function getOneMainKeyword(uuid) {
      $.ajax({
        url: '/spring/complaints/findTSMainKeywordById/' + uuid,
        type: 'Get',
        success: function (data) {
          if (data != null && data.length > 0) {
            var mainKeyword = data[0];
            initMainKeywordDialog(mainKeyword);
            showAddMainKeywordDlog(mainKeyword.uuid);
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
      $("#mainKeywordForm").find("#mUuid").val(mainKeyword.uuid);
      $("#mainKeywordForm").find("#mKeyword").val(mainKeyword.keyword);
      $("#mainKeywordForm").find("#mGroup").val(mainKeyword.group);
      var ngKeyword =  mainKeyword.tsNegativeKeywords;
      var info = '';
      $.each(ngKeyword,function (idx,val) {
        if(idx==0){
          info = info + val.keyword+',';
        }else{
          info = info + val.keyword;
        }
      });
      $("#mainKeywordForm").find("#ngKeyword").val(info);
    }

    //删除
    function deleteMainKeyword(uuid) {
      if (confirm("确实要删除这个主关键字吗?") == false) return;
      $.ajax({
        url: '/spring/complaints/delete/' + uuid,
        type: 'Get',
        success: function (data) {
          if (data) {
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
    //查询
    function serachMainKeywords() {
      var termObj = $("#serachMainKeyword").find("input[name='item']");
      $.each(termObj, function (index, val) {

      });
      $.ajax({
        url: '/spring/complaints/findTSMainKeywords',
        data: JSON.stringify(itemObj),//传回一个查询对象
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
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
          settingDialogDiv.hide();
        }
      });
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
</script>
<style type="text/css">
    body {
        font-size: 15px;;
    }

    #showMainKeywordTopDiv {

    }

    #showMainKeywordTableDiv {

    }

    #showAddMainKeywordDlog {
        align-content: center;
    }

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
            主关键词<input id="termkeywork" name="item" type="text"/>&nbsp;&nbsp;
            区域分组<input id="termGroup" name="item" type="text"/>&nbsp;&nbsp;
            <input type="button" class="ui-button ui-widget ui-corner-all"
                   onclick="serachMainKeywords()" value="查询">&nbsp;&nbsp;&nbsp;
            <input type="button" class="ui-button ui-widget ui-corner-all"
                   onclick="showAddMainKeywordDlog(null)" value="添加"/>
        </div>
    </div>
    <div id="showMainKeywordTableDiv">
        <table id="showMainKeywordTable">
            <tr bgcolor="#eeeeee" height=30>
                <td align="center" width=10><input type="checkbox" onclick="selectAll(this)"/></td>
                <td align="center" width=150>主词</td>
                <td align="center" width=100>分组</td>
                <td align="center" width=200>负面语句</td>
                <td align="center" width=100>更新时间</td>
                <td align="center" width=100>创建时间</td>
                <td align="center" width=150>操作</td>
                <div id="div1"></div>
                <div id="div2"></div>
            </tr>
            <c:forEach items="${tsMainKeywords }" var="mainkey">
                <tr>
                    <td><input type="checkbox"/></td>
                    <input type="hidden" id="mkUuid" value="${mainkey.uuid}"/>
                    <td>${mainkey.keyword }</td>
                    <td>${mainkey.group }</td>
                    <td>
                        <c:forEach items="${mainkey.tsNegativeKeywords}" var="ngkey">
                            <input type="hidden" id="ngkeyUuid" value="${ngkey.uuid}"/>
                            <c:choose>
                                <c:when test="${ngkey.pcAppeared ==1 || ngkey.phoneAppeared ==1}">
                                        <span style="color: crimson" >${ngkey.keyword}</span>
                                </c:when>
                                <c:otherwise>
                                    ${ngkey.keyword}
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </td>
                    <td><%--<fmt:formatDate value="${mainkey.updateTime==null?null:mainkey.updateTime}" pattern="yyyy-MM-dd mm:HH:ss"/>--%></td>
                    <td><fmt:formatDate value="${mainkey.createTime}"
                                        pattern="yyyy-MM-dd mm:HH:ss"/></td>
                    <td>&nbsp;&nbsp;&nbsp;<a href="javascript:getOneMainKeyword('${mainkey.uuid}')">修改</a>&nbsp;&nbsp;&nbsp;
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
                    <td><input id="mKeyword" type="text"></td>
                </tr>
                <tr>
                    <td>地区分组</td>
                    <td>
                        <select id="mGroup">
                            <option>-------请选择城市-------</option>
                            <option>北京</option>
                            <option>上海</option>
                            <option>广州</option>
                            <option>深圳</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>负面词</td>
                    <td>
                        <textarea id="ngKeyword" style="height: 175px;width: 230px"
                                  placeholder="请用逗号作为分割符"></textarea>
                    </td>
                </tr>
            </table>
        </form>
    </div>
<hr>
    <div id="showMainKeywordBottomDiv">
        <a class="ui-button ui-widget ui-corner-all">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;
            <a class="ui-button ui-widget ui-corner-all">上一页</a>&nbsp;&nbsp;&nbsp;&nbsp;
                1/4&nbsp;&nbsp;
                <a class="ui-button ui-widget ui-corner-all">下一页</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a class="ui-button ui-widget ui-corner-all">末页</a>&nbsp;&nbsp;&nbsp;&nbsp;
                        总记录数:&nbsp;&nbsp;&nbsp;&nbsp;
                        每页显示条数:<select id="selectSize">
                            <option>5</option>
                            <option>10</option>
                            <option>20</option>
                            <option>30</option>
                        </select>
    </div>
</div>
</body>
</html>