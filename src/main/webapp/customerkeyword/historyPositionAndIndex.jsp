<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<script language="javascript" type="text/javascript" src="/common.js"></script>
<html>
	<head>
		  <title>关键字指数和排名历史数据</title>
  	</head>	
<body>
	 
<div>
   	  <table width=100% style="font-size:12px;" cellpadding=3>      	        	 
          <tr bgcolor="#eeeeee" height=30>
          	<td align="center" width=140>关键字</td>
            <td align="center" width=100>联系人</td>
            <td align="center" width=200>电脑URL</td>
            <td align="center" width=200>电脑原始URL</td>
            <td align="center" width=200>手机URL</td>
            <td align="center" width=200>手机原始URL</td>
          </tr>
          <tr>
          	<td>${customerKeyword.keyword}</td>
            <td>${customerKeyword.contactPerson}</td>
            <td>${customerKeyword.url}</td>
            <td>${customerKeyword.originalUrl}</td>
            <td><%--${customerKeyword.PhoneUrl}--%></td>
            <td><%--${customerKeyword.OriginalPhoneUrl}--%></td>
          </tr>
      </table>
</div>
<div>
      <table width=100% style="font-size:12px;" cellpadding=3>      	        	 
          <tr bgcolor="#eeeeee" height=30>
          		
              
              <td align="center" width=40>指数</td>
              <td align="center" width=70>排名</td>
              <td align="center" width=60>IP</td>
              <td align="center" width=120>查询时间</td>              
          </tr>
          <C:forEach var="CustomerKeywordPositionIndexLog" items="${page.records}">
                 <tr  onmouseover="doOver(this);" onmouseout="doOut(this);">
                      <td>${CustomerKeywordPositionIndexLog.indexCount}</td>
                      <td>${CustomerKeywordPositionIndexLog.positionNumber}</td>
                      <td>${CustomerKeywordPositionIndexLog.ip}</td>
                      <td><fmt:formatDate value="${CustomerKeywordPositionIndexLog.createTime}" pattern="yyyy-MM-dd" /></td>
                  </tr>
          </C:forEach>
          
          <tr>
          	<td colspan=9>
          		  <br>总记录:
          		  ${page.pages}
          	</td>
          </tr>          
      </table>
</div>
      

<div style="display:none;">
<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>

</div>
</body>
</html>

