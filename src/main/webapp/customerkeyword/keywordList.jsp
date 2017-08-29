<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="spm" scope="page" class="com.keymanager.manager.ServiceProviderManager" />
	
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
		UserVO user = um.login(datasourceName,username,password);
		
		String condition = "";
		String curPage = request.getParameter("pg");
		
		if (curPage == null || curPage.equals(""))
		{
		    curPage = "1";
		}
		
		int iCurPage = Integer.parseInt(curPage);		
	    
	  	String keyword = request.getParameter("keyword");
	  	String url = request.getParameter("url");
	  	String orderElement = request.getParameter("orderElement");
	  	String status = request.getParameter("status");
	  	String positionType  = request.getParameter("positionType");
	  	
	  	String position = request.getParameter("position");
	  	String[] unPaidAll = request.getParameterValues("unPaidAll");
	  	String[] pushPay = request.getParameterValues("pushPay");
	  	
	  	String pageUrl = "";
	  	condition = condition + " and exists (SELECT 1 FROM t_customer c WHERE c.fUuid = ck.fCustomerUuid and c.fUserID = '" + username + "') " ;
	  	
	  	if (!Utils.isNullOrEmpty(keyword)){
	  		condition = condition + " and fKeyword like '%" + keyword.trim() + "%' ";
	  		pageUrl = pageUrl + "&keyword=" + keyword;
	  	}else{
	  		keyword = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(url)){
	  		condition = condition + " and fUrl = '" + url.trim() + "' ";
	  		pageUrl = pageUrl + "&url=" + url;
	  	}else{
	  		url = "";	
	  	}
			
	  	if (!Utils.isNullOrEmpty(position)){
			condition = condition + " and (fCurrentPosition > 0 and fCurrentPosition <= " + position.trim() + ") ";
	  		pageUrl = pageUrl + "&position=" + position;
	  	}else{
	  		position = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(status)){
	  		condition = condition + " and ck.fStatus = " + status.trim() + " ";
	  		pageUrl = pageUrl + "&status=" + status;
	  	}else{
	  		status = "";	
	  	}
	  	
	  	
	  	if (pushPay != null){
	  		condition = condition + " AND ((fCollectMethod = 'PerDay' and ck.fEffectiveToTime is not null) or ck.fEffectiveToTime <= STR_TO_DATE('" + Utils.formatDatetime(Utils.addDay(Utils.getCurrentTimestamp(), 3), "yyyy-MM-dd") + "', '%Y-%m-%d')) ";
	  		pageUrl = pageUrl + "&pushPay=pushPay";
	  	}
	      
	      String order = " order by ck.fCreateTime desc ";
	      if ("账单日期".equals(orderElement)){    
	      	order = " order by ck.fEffectiveFromTime desc ";
	      } else if("当前排名".equals(orderElement)){
	      	order = " order by ck.fCurrentPosition ";
	      }
	      pageUrl = pageUrl + "&orderElement=" + orderElement;
		  List itemList = ckm.searchCustomerKeywords(datasourceName, 30 , iCurPage , condition , order,1);
		  	
		  int recordCount = ckm.getRecordCount();
		  int pageCount = ckm.getPageCount();
		    
		  String fileName = "/customerkeyword/keywordList.jsp?" + pageUrl;
		  String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);		    	      
%>

<html>
	<head>
	  <title>客户关键字列表</title>
	  <style>
		.wrap {word-break: break-all; word-wrap:break-word;}   
		<!--
		#div1{ 
			display:none; 
			background-color :#f6f7f7; 
			color:#333333; 
			font-size:12px; 
			line-height:18px; 
			border:1px solid #e1e3e2; 
			width:450; 
			height:50;
		}
		-->
	  </style>
	  <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	  <script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	  <script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	  <link href="/css/menu.css" rel="stylesheet" type="text/css" />	
  	</head>	
<body>
      <table width=100% style="font-size:12px;" cellpadding=10>
      	  <tr> 
      	  	 <td colspan=6>
      	  	 	<table style="width:900px;" style="font-size:12px;">
      	  	 	<tr>
	      	  	 	<td colspan="6" >
				      	<table style="width:100%;font-size:12px;background-color:#d6ebff">
				      	  <tr  height=26>
				      	  	<td align="left">
				      	  		&nbsp;&nbsp;账号:&nbsp;&nbsp;<%=user.getUserID()%>
				      	  		&nbsp;&nbsp;姓名:&nbsp;&nbsp;<%=user.getUserName()%>
				      	  		&nbsp;&nbsp;余额:&nbsp;&nbsp;<a href="/user/useraccountlog.jsp" target="blank"><%=user.getAccountAmount()%> 元</a>
				      	  	</td>
				      	  	<td align="right">
				       	  		&nbsp;&nbsp;<a href="/customer/customerlist.jsp" target="_blank">客户清单</a>
				       	  		&nbsp;&nbsp;<a href="/customerkeyword/add.jsp" target="_blank">增加关键字</a>
				       	  		&nbsp;&nbsp;<a href="http://wpa.qq.com/msgrd?v=3&uin=40467028&site=qq&menu=yes" target="_blank">充值</a>
				       	  		&nbsp;&nbsp;<a href="http://wpa.qq.com/msgrd?v=3&uin=40467028&site=qq&menu=yes" target="_blank"><img src="/images/q20-20.png">联系客服</a>
				          		&nbsp;&nbsp;<a href="/user/updateSelfInfo.jsp">修改个人信息</a>
				          		&nbsp;&nbsp;<a href="/user/updatepassword.jsp">修改密码</a>
	    						&nbsp;&nbsp;<a href="/user/logout.jsp">退出</a>
				          	</td>
				          </tr>
				      	</table>
	      			</td>
      	  	 	</tr>
      	  	 	<tr>
      	  	 	<td colspan="6" >
      	  	 	<form style="width:100%;" method="post" action="keywordList.jsp">
      	  	 		关键字:&nbsp;&nbsp;<input type="text" name="keyword" id="keyword" value="<%=keyword%>" style="width:120px;">
      	  	 		优化链接:&nbsp;&nbsp;<input type="text" name="url" id="url" value="<%=url%>" style="width:160px;">
      	  	 		关键字状态:&nbsp;&nbsp;
	      	  	 			  <select name="status" id="status">		
			          	  	 	   <%
			          	  	 	  	 String []statusNames = {"", "激活","过期"};	
			          	  	 		 String []statusValues = {"","1","0"};
			          	  	 		 for (int i = 0; i < statusNames.length; i ++)
				          	  	     {
				          	  	         if (statusValues[i].equals(status))
				          	  	         {
				          	  	              out.println("<option selected value='" + statusValues[i] + "'>" + statusNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + statusValues[i] + "'>" + statusNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>
			          	  	 </select>
			                       排名类型:&nbsp;&nbsp;
	      	  	 			  <select name="positionType" id="positionType">		
			          	  	 	   <%
				          	  	     String []positionTypes = {"全部","电脑","触屏","极速"};						          	  	     
				          	  	     for (int i = 0; i < positionTypes.length; i ++)
				          	  	     {
				          	  	         if (positionTypes[i].equals(positionType))
				          	  	         {
				          	  	              out.println("<option selected value='" + positionTypes[i] + "'>" + positionTypes[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + positionTypes[i] + "'>" + positionTypes[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>
			          	  	 </select>
			                      显示前:&nbsp;&nbsp;<input type="text" name="position" id="position" value="<%=position%>" style="width:20px;" >
				 	 排序:&nbsp;&nbsp;
	      	  	 			  <select name="orderElement" id="orderElement">		
			          	  	 	   <%
				          	  	     String []orderElements = {"创建日期","当前排名"};						          	  	     
				          	  	     for (int i = 0; i < orderElements.length; i ++)
				          	  	     {
				          	  	         if (orderElements[i].equals(orderElement))
				          	  	         {
				          	  	              out.println("<option selected value='" + orderElements[i] + "'>" + orderElements[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + orderElements[i] + "'>" + orderElements[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>
			          	  	 </select>
			          	  &nbsp;&nbsp;<input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " >
      	  	 	</form>
      	  	 	</td>
      	  	 	</tr>
          <tr bgcolor="#eeeeee" height=30>
              <td align="center" width=180>关键字</td>
              <td align="center" width=220>优化链接</td>
              <td align="center" width=130>指数/原始排名/当前排名</td>
              <td align="center" width=120>报价</td>
              <td align="center" width=100>优化日期</td>
			  <td align="center" width=60>操作</td>	
			  <div id="div1"></div> 
          </tr>
          <%
              String trClass = "";
              String webUrl = "";
              String keywordColor = "";
              for (int i = 0; i < itemList.size(); i ++)
              {
                  CustomerKeywordVO value = (CustomerKeywordVO) itemList.get(i);                  
                  trClass= "";
                  keywordColor = "";
                  keywordColor = "#669900";
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">                                            
                      <td><font color="<%=keywordColor%>"><%=value.getKeyword()%></font></td>
                      <td class="wrap"  onMouseMove="showTip('<%=Utils.isNullOrEmpty(value.getUrl())?"": "电脑原始链接:" + value.getOriginalUrl()%><%=Utils.isNullOrEmpty(value.getOriginalPhoneUrl()) ? "" : "\n手机原始链接:" + value.getOriginalPhoneUrl()%>')" onMouseOut="closeTip()">
                      <% if(value.hasPC()){%>
			         	<div style="height:16;"><%=Utils.isNullOrEmpty(value.getUrl())? "" : "电脑:" + value.getUrl()%></div>
			         <%}
                       if(value.hasJisu() || value.hasChuping()){%>
			         	<div style="height:16;"><%=Utils.isNullOrEmpty(value.getPhoneUrl())?"": "手机:" + value.getPhoneUrl()%></div>
			         <%} %>
			         </td>
			         
			         <td>
			         <%if (value.hasPC()){%>
			         	<div style="height:16;">电脑: <a href="/customerkeyword/historyPositionAndIndex.jsp?type=PC&uuid=<%=value.getUuid()%>" target="_blank"><%=value.getCurrentIndexCount()%></a>/<%=value.getInitialPosition()%>/<a href="<%=value.searchEngineUrl()%><%=value.getKeyword()%>&pn=<%=Utils.prepareBaiduPageNumber(value.getCurrentPosition())%>" target="_blank"><%=value.getCurrentPosition()%></a></div>
			         <%}
			           if (value.hasJisu()){%>	
			         	<div style="height:16;">极速: <a href="/customerkeyword/historyPositionAndIndex.jsp?type=Jisu&uuid=<%=value.getUuid()%>" target="_blank"><%=value.getPhoneCurrentIndexCount()%></a>/<%=value.getJisuInitialPosition()%>/<a href="http://m.baidu.com/ssid=0/from=0/bd_page_type=1/uid=0/baiduid=B3590A6F0886DFEB62CEAE792B055802/s?uc_param_str=upssntdnvelami&sa=ib&st_1=111041&st_2=102041&pu=sz@224_220%2Cta@middle____&idx=20000&tn_1=middle&tn_2=middle&word=<%=value.getKeyword()%>&pn=<%=Utils.prepareBaiduPageNumber(value.getJisuCurrentPosition())%>" target="_blank"><%=value.getJisuCurrentPosition()%></a></div>
			         <%}
			           if (value.hasChuping()){%>
			         	<div style="height:16;">触屏: <a href="/customerkeyword/historyPositionAndIndex.jsp?type=Chuping&uuid=<%=value.getUuid()%>" target="_blank"><%=value.getPhoneCurrentIndexCount()%></a>/<%=value.getChupingInitialPosition()%>/<%=value.getChupingCurrentPosition()%></a></div>
			         <%} %>
			         </td>
			    
                      <td><%=value.feeString()%>
                      </td>
                      <td><%=Utils.formatDatetime(value.getStartOptimizedTime(), "yyyy-MM-dd")%></td>
                      
                      <td>
                      	  <a href="javascript:changeStatus('<%=value.getUuid()%>', '<%=value.getStatus() == 0 ? 1 : 0%>')"><%=value.getStatus() == 0 ? "优化" : "暂停优化"%></a>
                      </td>                         
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
      </td>
      <td valign="top">
      
      </td>
      </tr>
      </table>
   <br><br><br>
  <br>
<script language="javascript">
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
function changeStatus(uuid, status)
{
   if (status === 0){
   	if (confirm("确实要暂停优化这个关键字吗?") == false) return;
   }
   document.location = "changeStatus.jsp?uuid=" + uuid + "&status=" + status;
}

function showTip(content,e) 
{
	var event = e||window.event;
	var pageX = event.pageX;
    var pageY = event.pageY;
    if(pageX==undefined)
    {
    	pageX=event.clientX+document.body.scrollLeft||document.documentElement.scrollLeft;
    }
    if(pageY==undefined)
    {
        pageY = event.clientY+document.body.scrollTop||document.documentElement.scrollTop;
    }
	var div1 = document.getElementById('div1'); //将要弹出的层 
	div1.innerText = content;
	div1.style.display="block"; //div1初始状态是不可见的，设置可为可见  
	div1.style.left=pageX+10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容 
	div1.style.top=pageY+5; 
	div1.style.position="absolute"; 
 }

//关闭层div1的显示 
function closeTip() 
{ 
	 var div1 = document.getElementById('div1'); 
	 div1.style.display="none"; 
} 
</script>


<div style="display:none;">
<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>

</div>
</body>
</html>

