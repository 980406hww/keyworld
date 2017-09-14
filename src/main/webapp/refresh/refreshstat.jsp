<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*,java.net.URLEncoder"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="ckrsim" scope="page" class="com.keymanager.manager.CustomerKeywordRefreshStatInfoManager" />
	
<%@include file="/check.jsp" %>

<html>
<header>
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
	width:350; 
	height:50;
}
#div2 {
	display: none;
	background-color: #ACF106;
	margin: -50 0 0 -11;
    color: #E80404;
    font-size: 20px;
    line-height: 18px;
    border: 2px solid #104454;
    width: 100;
    height: 22;
    left: 50%;
    top: 50%;
    z-index: 25;
    position: fixed;
}
-->
</style>

	<script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	<link href="/css/menu.css" rel="stylesheet" type="text/css" />	
</header>

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
	String type = (String) session.getAttribute("entry");

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
	
	
	UserVO user = new UserVO();
	user = um.login(datasourceName,username,password);
	if ((user.getStatus() == 0) || !(user.isVipType()))
	{
%>
    <script language="javascript">
    	alert("你没有该权限！");
    	window.history.back();
  	</script>
<%
	}
	String curPage = request.getParameter("pg");
	
	if (curPage == null || curPage.equals(""))
	{
	    curPage = "1";
	}
	String pageUrl = "test=1";	
	
	int iCurPage = Integer.parseInt(curPage);
	String groupName = request.getParameter("groupName");
	String customerName = request.getParameter("customerName");
	
	if(groupName != null){
		pageUrl = pageUrl + "&groupName=" + groupName; 
	}else{
		groupName = "";
	}
	if(customerName != null){
		pageUrl = pageUrl + "&customerName=" + customerName; 
	}else{
		customerName = "";
	}
	List refreshStatInfoVOs = ckrsim.generateCustomerKeywordStatInfo(datasourceName, type, groupName, customerName);
  	
    int recordCount = ckrsim.getRecordCount();
    int pageCount = ckrsim.getPageCount();
    
    String fileName = "refreshstat.jsp?" + pageUrl;
    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
%>


<body>
      <table width=1240 style="font-size:12px;" cellpadding=3>
            <tr>
				<td colspan=13 align="left">
					<%@include file="/menu.jsp" %>	
				</td>
			</tr>
      	  <tr>
      	  	 <td colspan=13>
      	  	 	<form method="post" action="refreshstat.jsp">
      	  	 		<table style="font-size:12px;">
	      	  	 		<tr>
			          	  <td align="right">分组名称:<input name="groupName" id="groupName" type="text" style="width:200px;" value="<%=groupName%>"></td>
			          	  <td align="right">客户名称:<input name="customerName" id="customerName" type="text" style="width:200px;" value="<%=customerName %>"></td>
			          	  <td align="right"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
			          	</tr>
			        </table>
	      	  	 	<table style="font-size:12px;">
	      	  	 		 <tr bgcolor="#eeeeee" height=30>
			          	   <td align="center" width=60 rowspan="2">类型</td>
					       <td align="center" width=80 colspan="4">关键字</td>
					       <td align="center" width=60 colspan="5">刷的次数</td>
					       <td align="center" width=100 colspan="2">机器数</td>
					       
		   				</tr>
		   				<tr bgcolor="#eeeeee" height=30>
					       <td align="center" width=80>总数</td>
					       <td align="center" width=80>待刷数</td>
					       <td align="center" width=80>无效刷数</td>
					       <td align="center" width=80>无效占比</td>
					       <td align="center" width=60>总次数</td>
					       <td align="center" width=80>已刷次数</td>
					       <td align="center" width=80>待刷次数</td>
					       <td align="center" width=80>取词次数</td>
					       <td align="center" width=60>无效占比</td>
					       <td align="center" width=100>总数</td>
					       <td align="center" width=60>已停数</td>
		   				</tr>
		   				<%
				              String trClass = "";
				          	  String keywordColor = "";
				              String webUrl = "";
				              double total = 0;
				              for (int i = 0; i < refreshStatInfoVOs.size(); i ++)
				              {
				            	  CustomerKeywordRefreshStatInfoVO viewVO = (CustomerKeywordRefreshStatInfoVO) refreshStatInfoVOs.get(i);                  
				                  trClass= "";
				                  if ((i % 2) != 1)
				                  {
				                     trClass = "bgcolor='#eeeeee'";
				                  }
					      %>
					                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
					                     <td><%=viewVO.getGroup()%></td>
					                     <td><%=viewVO.getTotalKeywordCount()%></td>
					                     <td><%=viewVO.getNeedOptimizeKeywordCount() > 0 ? viewVO.getNeedOptimizeKeywordCount() + "" : ""%></td>
					                     <td><%=viewVO.getInvalidKeywordCountStr()%> <%=viewVO.getResetInvalidRefreshCountStr(groupName, customerName)%></td>
					                     <td><font color="<%=viewVO.getInvalidKeywordPercentage() > 20 ? "red" : (viewVO.getInvalidKeywordPercentage() > 10 ? "purple" : "")%>"><%=viewVO.getInvalidKeywordPercentage() > 0 ? Utils.formatDouble(viewVO.getInvalidKeywordPercentage()) + "%" : ""%></font></td>
					                     <td><%=viewVO.getTotalOptimizeCount() > 0 ? viewVO.getTotalOptimizeCount() + "" : ""%></td>
					                     <td><%=viewVO.getTotalOptimizedCount() > 0 ? viewVO.getTotalOptimizedCount() + "" : ""%></td>
					                     <td><%=viewVO.getNeedOptimizeCount() > 0 ? viewVO.getNeedOptimizeCount() + "" : ""%></td>
					                     <td><%=viewVO.getQueryCount() > 0 ? viewVO.getQueryCount() + "" : ""%></td>
					                     <td><font color="<%=viewVO.getInvalidOptimizePercentage() > 20 ? "red" : (viewVO.getInvalidOptimizePercentage() > 10 ? "purple" : "")%>"><%=viewVO.getInvalidOptimizePercentage() > 0 ? Utils.formatDouble(viewVO.getInvalidOptimizePercentage()) + "%" : ""%></font></td>
					                     <td><%=viewVO.getTotalMachineCount() > 0 ? viewVO.getTotalMachineCount() + "" : ""%></td>
					                     <td><%=viewVO.getUnworkMachineCountStr()%></td>
					                  </tr>
					          <%
					          }
					          %>
		      	  	</table>
      	  	 	</form>
      	  	 </td>
      	  </tr>      	  
      </table>
      
   <br><br><br>
  <br>
  
  <br><br><br><br><br><br><br><br>
<script language="javascript">
	 function doOver(obj)
	 {
	 	obj.style.backgroundColor = "green";
	 }
	 
	 function doOut(obj)
	 {
 	   var rowIndex = obj.rowIndex;
 	   if ((rowIndex % 2) == 0) 
 	   {
 	   	  obj.style.backgroundColor = "#eeeeee";
 	   }
 	   else
 	   {
 	   	  obj.style.backgroundColor = "#ffffff";
 	   }
	 }
	 
	 function resetInvaidRefreshCount(groupName, customerName, self){
		    $$$.ajax({
		        url: '/refresh/refreshagain.jsp?groupName=' + groupName + "&customerName=" + customerName,
		        type: 'Get',
		        success: function (data) {
		        	data = data.replace(/\r\n/gm,"");
		        	if(data === "1"){
		        		showInfo("重置成功！", self);
		        		window.location.reload();
		        	}else{
		        		showInfo("重置失败！", self);
		        	}
		        },
		        error: function () {
		        	showInfo("重置失败！", self);
		        }
		    });
		}
	 
	 	function showInfo(content, e) {
			e = e || window.event;
			var div1 = document.getElementById('div2'); //将要弹出的层 
			div1.innerText = content;
			div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见  
			div1.style.left = getLeft(e) + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容 
			//div1.style.top = getTop(e) + 5;
			//div1.style.position = "absolute";
			
			var intervalID = setInterval(function(){
				div1.style.display = "none";
			}, 3000);
		}
		
		function getTop(e){
		    var offset=e.offsetTop;
		    if(e.offsetParent!=null) offset+=getTop(e.offsetParent);
		    return offset;
	    }
	    //获取元素的横坐标
	    function getLeft(e){
		    var offset=e.offsetLeft;
		    if(e.offsetParent!=null) offset+=getLeft(e.offsetParent);
		    return offset;
	    } 
	 function showTip(content,e) 
	 {
		e = e||window.event;
		var div1 = document.getElementById('div1'); //将要弹出的层 
		div1.innerText = content;
		div1.style.display="block"; //div1初始状态是不可见的，设置可为可见  
		div1.style.left=e.clientX+10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容 
		div1.style.top=e.clientY+5; 
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
</div>
<div id="div2"></div>
</body>
</html>

