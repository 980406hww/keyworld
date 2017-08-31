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
         <td><%=customerKeywordCrilteria.getKeyword()%></td>
         <td><%=customerKeywordCrilteria.getUrl()%></td>
         <td><%=customerKeywordCrilteria.getSearchEngine()%></td>
         <td><a href="historyPositionAndIndex.jsp?uuid=<%=customerKeywordCrilteria.getUuid()%>" target="_blank"><%=customerKeywordCrilteria.getCurrentIndexCount()%></a>/<%=customerKeywordCrilteria.getInitialPosition()%>/<a href="http://www.baidu.com/s?wd=<%=customerKeywordCrilteria.getKeyword()%>" target="_blank"><%=customerKeywordCrilteria.getCurrentPosition()%></a>
         </td>
         <td><%=customerKeywordCrilteria.getCollectMethodName()%></td>
         <% if (user.isVipType()){ %>
         <td nowrap=true><%=customerKeywordCrilteria.costString()%>
         <%} %>
         </td nowrap=true>
         <td><%=customerKeywordCrilteria.feeString()%>
         </td>
         <td><%=Utils.formatDatetime(customerKeywordCrilteria.getStartOptimizedTime(), "yyyy-MM-dd")%></td>
         <td><div style="height:16;"><%=Utils.formatDatetime(customerKeywordCrilteria.getEffectiveFromTime(), "yyyy-MM-dd")%></div><div style="height:16;"><%=Utils.formatDatetime(customerKeywordCrilteria.getEffectiveToTime(), "yyyy-MM-dd")%></div></td>
         <td><div style="height:16;"><%=Utils.formatDatetime(customerKeywordCrilteria.getPaymentEffectiveFromTime(), "yyyy-MM-dd")%></div><div style="height:16;"><%=Utils.formatDatetime(customerKeywordCrilteria.getPaymentEffectiveToTime(), "yyyy-MM-dd")%></div></td>
         <td><%=customerKeywordCrilteria.getStatusName()%></td>
         <% if (user.isVipType()){ %>                                                 
         <td><%=customerKeywordCrilteria.getServiceProvider()%></td>
         <%} %>
     </tr>
</table>