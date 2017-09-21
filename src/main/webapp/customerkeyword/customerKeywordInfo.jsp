<%@page contentType="text/html;charset=utf-8"%>
<table width=100%  style= "border:1px solid #000000;font-size:12px;" cellpadding=3>
  <tr bgcolor="#eeeeee" height=30>
       <td align="center" width=120>关键字</td>
       <td align="center" width=120>URL</td>
       <td align="center" width=40>引擎</td>
       <td align="center" width=120>指数/初始/当前</td>
       <td align="center" width=35>计价方式</td>
       <% if (user.isVipType()){ %>
       <td align="center" width=180>成本</td>
       <%} %>
       <td align="center" width=180>报价</td>
       <td align="center" width=70>优化日期</td>
       <td align="center" width=80><div style="height:16;">开始日期(卖)</div><div style="height:16;">结束日期(卖)</div></td>
       <td align="center" width=80><div style="height:16;">开始日期(买)</div><div style="height:16;">结束日期(买)</div></td>
       <td align="center" width=35>状态</td>   
       <% if (user.isVipType()){ %>          
       <td align="center" width=60>服务商</td>
       <%} %>
   </tr>
         
    <tr height="30">                                            
         <td><%=customerKeywordCriteria.getKeyword()%></td>
         <td><%=customerKeywordCriteria.getUrl()%></td>
         <td><%=customerKeywordCriteria.getSearchEngine()%></td>
         <td><a href="historyPositionAndIndex.jsp?uuid=<%=customerKeywordCriteria.getUuid()%>" target="_blank"><%=customerKeywordCriteria.getCurrentIndexCount()%></a>/<%=customerKeywordCriteria.getInitialPosition()%>/<a href="http://www.baidu.com/s?wd=<%=customerKeywordCriteria.getKeyword()%>" target="_blank"><%=customerKeywordCriteria.getCurrentPosition()%></a>
         </td>
         <td><%=customerKeywordCriteria.getCollectMethodName()%></td>
         <% if (user.isVipType()){ %>
         <td nowrap=true><%=customerKeywordCriteria.costString()%>
         <%} %>
         </td nowrap=true>
         <td><%=customerKeywordCriteria.feeString()%>
         </td>
         <td><%=Utils.formatDatetime(customerKeywordCriteria.getStartOptimizedTime(), "yyyy-MM-dd")%></td>
         <td><div style="height:16;"><%=Utils.formatDatetime(customerKeywordCriteria.getEffectiveFromTime(), "yyyy-MM-dd")%></div><div style="height:16;"><%=Utils.formatDatetime(customerKeywordCriteria.getEffectiveToTime(), "yyyy-MM-dd")%></div></td>
         <td><div style="height:16;"><%=Utils.formatDatetime(customerKeywordCriteria.getPaymentEffectiveFromTime(), "yyyy-MM-dd")%></div><div style="height:16;"><%=Utils.formatDatetime(customerKeywordCriteria.getPaymentEffectiveToTime(), "yyyy-MM-dd")%></div></td>
         <td><%=customerKeywordCriteria.getStatusName()%></td>
         <% if (user.isVipType()){ %>                                                 
         <td><%=customerKeywordCriteria.getServiceProvider()%></td>
         <%} %>
     </tr>
</table>