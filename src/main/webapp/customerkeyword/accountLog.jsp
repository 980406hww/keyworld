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
	List itemList = ckalm.searchCustomerKeywordAccountLogs(datasourceName, 30, iCurPage, condition, " order by fMonth ", 1);
	
    int recordCount = ckalm.getRecordCount();
    int pageCount = ckalm.getPageCount();
    
    String fileName = "/customerkeyword/receive.jsp?uuid=" + customerKeywordUuid;
    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
%>

      <table width=100% style="border:1px solid #000000;font-size:12px;" cellpadding=3>      	        	 
          <tr bgcolor="#eeeeee" height=30>
              <td align="center" width=70>账单月份</td>
              <td align="center" width=70>起始日期</td>
              <td align="center" width=70>结束日期</td>              
              <td align="center" width=70>应收款</td>
              <td align="center" width=180>金额/收款日期</td>
              <td align="center" width=160>备注</td>
              <td align="center" width=70>状态</td>
              <td align="center" width=100>账单创建日期</td>
              <td align="center" width=100>操作</td>
          </tr>
          <%
              String trClass = "";
              String webUrl = "";
              int totalRealReceivedCount = 0;
              for (int i = 0; i < itemList.size(); i ++)
              {
              	CustomerKeywordAccountLogVO accountLog = (CustomerKeywordAccountLogVO) itemList.get(i);  
              	totalRealReceivedCount = totalRealReceivedCount + accountLog.getFirstRealCollection() + accountLog.getSecondRealCollection();
                  trClass= "";
                  if (i % 2 != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);" height="30">                                            
                      <td><%=accountLog.getMonth()%></td>
                      <td><%=Utils.formatDatetime(accountLog.getEffectiveFromTime(), "yyyy-MM-dd")%></td>
                      <td><%=Utils.formatDatetime(accountLog.getEffectiveToTime(), "yyyy-MM-dd")%></td>
                      <td><%=accountLog.getReceivable()%></td>
                      <td>
                      	<% 
                      		if (accountLog.getFirstRealCollection() > 0){
                      			out.println("<div style='height:5;'>" + accountLog.getFirstRealCollection() + "/" + Utils.formatDatetime(accountLog.getFirstReceivedTime(), "yyyy-MM-dd") + "</div>");
                      			out.println("</br>");
                      		}
                      		if (accountLog.getSecondRealCollection() > 0){
                    			out.println("<div style='height:5;'>" + accountLog.getSecondRealCollection() + "/" + Utils.formatDatetime(accountLog.getSecondReceivedTime(), "yyyy-MM-dd") + "</div>");
                    		}
                      	%>
                      </td>
                      <td><%=accountLog.getRemarks() == null ? "" : accountLog.getRemarks()%></td>
                      <td><%=accountLog.statusName()%></td>
                      <td><%=Utils.formatDatetime(accountLog.getCreateTime(), "yyyy-MM-dd")%></td>
                      <td>
                    	<a href="receive.jsp?uuid=<%=accountLog.getUuid()%>&customerKeywordUuid=<%=customerKeywordUuid%>">收款</a>						
                      </td>
                  </tr>
          <%
              }
          
          %>
          
          <tr>
          	<td colspan=9>
          		  <div style="text-align:right;width:100%;font-size:12px;"><font color="red">收款合计: <%=totalRealReceivedCount %></font></div>
          		  <%=pageInfo%>
          	</td>
          </tr>          
      </table>


