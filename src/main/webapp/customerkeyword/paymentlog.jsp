<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*"%>
<%
	String curPage = request.getParameter("pg");		
	if (curPage == null || curPage.equals(""))
	{
	    curPage = "1";
	}
	
	int iCurPage = Integer.parseInt(curPage);
	String condition = " and fCustomerKeywordUuid = " + customerKeywordUuid;
	List itemList = ckplm.searchCustomerKeywordPaymentLogs(datasourceName, 30, iCurPage, condition, " order by fEffectiveFromTime desc ", 1);
	
    int recordCount = ckplm.getRecordCount();
    int pageCount = ckplm.getPageCount();
    
    String fileName = "/customerkeyword/pay.jsp?uuid=" + customerKeywordUuid;
    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
%>

      <table width=100% style="border:1px solid #000000;font-size:12px;" cellpadding=3>      	        	 
          <tr bgcolor="#eeeeee" height=30>
              <td align="center" width=70>起始日期</td>
              <td align="center" width=70>结束日期</td>              
              <td align="center" width=70>应付款</td>
              <td align="center" width=180>金额/付款日期</td>
              <td align="center" width=160>备注</td>
              <td align="center" width=100>创建日期</td>
          </tr>
<%
              String trClass = "";
              String webUrl = "";
              int totalRealPaidCount = 0;
              for (int i = 0; i < itemList.size(); i ++)
              {
              	CustomerKeywordPaymentLogVO eachPaymentLog = (CustomerKeywordPaymentLogVO) itemList.get(i); 
              	totalRealPaidCount = totalRealPaidCount + eachPaymentLog.getRealPaid();
                  trClass= "";
                  if (i % 2 != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
%>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">
                      <td><%=Utils.formatDatetime(eachPaymentLog.getEffectiveFromTime(), "yyyy-MM-dd")%></td>
                      <td><%=Utils.formatDatetime(eachPaymentLog.getEffectiveToTime(), "yyyy-MM-dd")%></td>
                      <td><%=eachPaymentLog.getPayable()%></td>
                      <td><%=eachPaymentLog.getRealPaid()%>/<%=Utils.formatDatetime(eachPaymentLog.getPaidTime(), "yyyy-MM-dd")%></td>
                      <td><%=eachPaymentLog.getRemarks() == null ? "" : eachPaymentLog.getRemarks()%></td>
                      <td><%=Utils.formatDatetime(eachPaymentLog.getCreateTime(), "yyyy-MM-dd")%></td>
                  </tr>
          <%
              }
          
          %>
          
          <tr>
          	<td colspan=9>
          		  <div style="text-align:right;width:100%;font-size:12px;"><font color="red">付款合计: <%=totalRealPaidCount %></font></div>
          		  <%=pageInfo%>
          	</td>
          </tr>          
      </table>


