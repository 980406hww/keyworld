<%@page contentType="text/html;charset=utf-8"%>
<table width=100%  style= "border:1px solid #000000;font-size:12px;" cellpadding=3>
	<tr border="1" height=30>
      <td width=250>联系人: <%=customer.getContactPerson()%></td>
      <td width=200>QQ: <%=customer.getQq()%></td>
      <td width=200>电话: <%=customer.getTelphone()%></td>
      <td width=120>关键字数: <%=customer.getKeywordCount()%></td>
      <td width=250>创建时间: <%=Utils.formatDatetime(customer.getCreateTime(), "yyyy-MM-dd HH:mm")%></td>		              
  </tr>
</table>