<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="pim" scope="page" class="com.keymanager.manager.CustomerKeywordPositionIndexLogManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
	
<%@include file="/check.jsp" %>

<%
		if(loginState == 0)
		{
%>
        <script language="javascript">
			window.location.href="/bd.html";
		</script>
<%
        return;
		}
		
		
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		
		username = Utils.parseParam(username);
		password = Utils.parseParam(password);
		
		if (username == null || username.equals(""))
		{
%>
        <script language="javascript">
			window.location.href="/bd.html";
	  	</script>
<%		  
        return;  
		}		
		String curPage = request.getParameter("pg");		
		if (curPage == null || curPage.equals(""))
		{
		    curPage = "1";
		}
		
		int iCurPage = Integer.parseInt(curPage);
		String customerKeywordUuid = request.getParameter("uuid");	
		CustomerKeywordVO customerKeyword = ckm.getCustomerKeywordByUuid(datasourceName, customerKeywordUuid);
		String type = request.getParameter("type");
		List itemList = pim.searchCustomerKeywordPositionIndexLogs(datasourceName, type, customerKeywordUuid, 30, iCurPage, 1);
		
	    int recordCount = pim.getRecordCount();
	    int pageCount = pim.getPageCount();
	    
	    String fileName = "/customerkeyword/historyPositionAndIndex.jsp?uuid=" + customerKeywordUuid + "&type=" + type;
	    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
%>
<html>
	<head>
		  <title>关键字指数和排名历史数据</title>
  	</head>	
<body>
	 
   <center>
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
          	<td><%=customerKeyword.getKeyword()%></td>
            <td><%=customerKeyword.getContactPerson()%></td>
            <td><%=customerKeyword.getUrl()%></td>
            <td><%=customerKeyword.getOriginalUrl()%></td>
            <td><%=customerKeyword.getPhoneUrl()%></td>
            <td><%=customerKeyword.getOriginalPhoneUrl()%></td>
          </tr>
      </table>
      <table width=100% style="font-size:12px;" cellpadding=3>      	        	 
          <tr bgcolor="#eeeeee" height=30>
          		
              
              <td align="center" width=40>指数</td>
              <td align="center" width=70>排名</td>
              <td align="center" width=60>IP</td>
              <td align="center" width=120>查询时间</td>              
          </tr>
          <%
              String trClass = "";
              String webUrl = "";
              
              for (int i = 0; i < itemList.size(); i ++)
              {
              	CustomerKeywordPositionIndexLogVO value = (CustomerKeywordPositionIndexLogVO) itemList.get(i);                  
                  trClass= "";
                  if (i % 2 != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">                                            
                      
                      <td><%=value.getIndexCount()%></td>
                      <td><%=value.getPositionNumber()%></td>
                      <td><%=value.getIp()%></td>
                      <td><%=Utils.formatDatetime(value.getCreateTime(), "yyyy-MM-dd HH:mm")%></td>
                  </tr>
          <%
              }
          
          %>
          
          <tr>
          	<td colspan=9>
          		  <br>
          		  <%=pageInfo%>
          	</td>
          </tr>          
      </table>
      
      
   </center>
   <br><br><br>
   <center>
   	   
  </center>
  <br>
  
  <br><br><br><br><br><br><br><br>
<div style="display:none;">
<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>

</div>
</body>
</html>

