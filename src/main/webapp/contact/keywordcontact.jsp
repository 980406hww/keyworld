<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.weibo.value.*,com.keymanager.value.*,java.util.*,java.net.URLEncoder"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="kcm" scope="page" class="com.keymanager.manager.KeywordContactManager" />
	
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
-->
</style>

	<script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	<link href="/css/menu.css" rel="stylesheet" type="text/css" />	
</header>

<%
	String weiboDatasourceName = "weibo";
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
	if ((user.getStatus() == 0) || !(user.isVipType()))
	{
%>
    <script language="javascript">
    	alert("你没有该权限！");
    	window.history.back();
  	</script>
<%
	}
	String condition = "";
	String pageUrl = "";	
	String curPage = request.getParameter("pg");
	
	if (curPage == null || curPage.equals(""))
	{
	    curPage = "1";
	}
	
	int iCurPage = Integer.parseInt(curPage);
    
	String keyword = request.getParameter("keyword");
	String group = request.getParameter("group");
	String position = request.getParameter("position");
	String city = request.getParameter("city");
	String updateFromTime = request.getParameter("updateFromTime");
	String updateToTime = request.getParameter("updateToTime");
	if (!Utils.isNullOrEmpty(updateFromTime)){
		pageUrl = pageUrl + "&updateFromTime=" + updateFromTime;
	}else{
		updateFromTime = "";	
	}
	if (!Utils.isNullOrEmpty(updateToTime)){
		pageUrl = pageUrl + "&updateToTime=" + updateToTime;
	}else{
		updateToTime = "";	
	}
	
	String[] haveQq = request.getParameterValues("haveQq");
	if (haveQq != null){
		pageUrl = pageUrl + "&haveQq=haveQq";
	}
	String[] haveEmail = request.getParameterValues("haveEmail");
	if (haveEmail != null){
		pageUrl = pageUrl + "&haveEmail=haveEmail";
	}
	String[] haveMobile = request.getParameterValues("haveMobile");
	if (haveMobile != null){
		pageUrl = pageUrl + "&haveMobile=haveMobile";
	}
	String[] havePhone = request.getParameterValues("havePhone");
	if (havePhone != null){
		pageUrl = pageUrl + "&havePhone=havePhone";
	}
	String[] haveContactPerson = request.getParameterValues("haveContactPerson");
	if (haveContactPerson != null){
		pageUrl = pageUrl + "&haveContactPerson=haveContactPerson";
	}
	String[] unClick = request.getParameterValues("unClick");
	if (unClick != null){
		pageUrl = pageUrl + "&unClick=unClick";
	}
	String[] isDomain = request.getParameterValues("isDomain");
	if (isDomain != null){
		pageUrl = pageUrl + "&isDomain=isDomain";
	}
	if (!Utils.isNullOrEmpty(keyword)){
		pageUrl = pageUrl + "&keyword=" + keyword;
	}else{
		keyword = "";	
	}
	
	if (!Utils.isNullOrEmpty(group)){
		pageUrl = pageUrl + "&group=" + group;
	}else{
		group = "";	
	}
	
	if (!Utils.isNullOrEmpty(city)){
		pageUrl = pageUrl + "&city=" + city;
	}else{
		city = "";	
	}
	
	if (!Utils.isNullOrEmpty(position)){
		pageUrl = pageUrl + "&position=" + position;
	}else{
		position = "";
	}
    
	condition = KeywordContactUtils.prepareCondition(request);
    List itemList = kcm.searchKeywordContactVOs(weiboDatasourceName, 30 , iCurPage , condition , "", 1);
  	
    int recordCount = kcm.getRecordCount();
    int pageCount = kcm.getPageCount();
    
    String fileName = "keywordcontact.jsp?" + pageUrl;
    String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
%>


<body>
	 

      <table width=1240 style="font-size:12px;" cellpadding=3>
            <tr>
				<td colspan=15 align="left">
					<%@include file="/menu.jsp" %>	
				</td>
			</tr>
      	  <tr>
      	  	 <td colspan=15>
      	  	 	<form method="post" action="keywordcontact.jsp">
	      	  	 	<table style="font-size:12px;">
	      	  	 		<tr>
		      	  	 		<td colspan=12>
			      	  	 		<table style="font-size:12px;">
			      	  	 			<tr>
				      	  	 			<td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="<%=keyword%>" style="width:90px;"></td>
				      	  	 			<td align="right">分组:</td> <td><input type="text" name="group" id="group" value="<%=group%>" style="width:120px;"></td>
				      	  	 		  	<td align="right">城市:</td> <td><input type="text" name="city" id="city" value="<%=city%>" style="width:80px;" ></td>
				      	  	 		  	<td align="right">更新日期:<input name="updateFromTime" id="updateFromTime" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=updateFromTime %>">到<input name="updateToTime" id="updateToTime" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=updateToTime%>"></td>
							 	   	    <td align="right">当前排名:</td><td><input type="text" name="position" id="position" value="<%=position%>" style="width:90px;"></td>
			      	  	 			</tr>
			      	  	 		</table>
		      	  	 		</td>
		      	  	 	</tr>
		      	  	 	<tr>
		      	  	 		<td colspan=12>
			      	  	 		<table style="font-size:12px;">
			      	  	 			<tr>
				      	  	 		  	<td align="right"><input id="haveQq" name="haveQq" type="checkbox" value="haveQq" <%if (haveQq != null){ out.print(" checked=true");}%>>有QQ</input></td>
				      	  	 		  	<td align="right"><input id="haveMobile" name="haveMobile" type="checkbox" value="haveMobile" <%if (haveMobile != null){ out.print(" checked=true");}%>>有手机</input></td>
				      	  	 		  	<td align="right"><input id="haveEmail" name="haveEmail" type="checkbox" value="haveEmail" <%if (haveEmail != null){ out.print(" checked=true");}%>>有邮箱</input></td>
				      	  	 		  	<td align="right"><input id="havePhone" name="havePhone" type="checkbox" value="havePhone" <%if (havePhone != null){ out.print(" checked=true");}%>>有电话</input></td>
				      	  	 		  	<td align="right"><input id="haveContactPerson" name="haveContactPerson" type="checkbox" value="haveContactPerson" <%if (haveContactPerson != null){ out.print(" checked=true");}%>>有联系人</input></td>
				      	  	 		  	<td align="right"><input id="unClick" name="unClick" type="checkbox" value="unClick" <%if (unClick != null){ out.print(" checked=true");}%>>未点击</input></td>
				      	  	 		  	<td align="right"><input id="isDomain" name="isDomain" type="checkbox" value="isDomain" <%if (isDomain != null){ out.print(" checked=true");}%>>独立域名</input></td>
			      	  	 			</tr>
			      	  	 		</table>
		      	  	 		</td>
				 	   	  	<td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>				 	   	  	
	      	  	 		</tr>
	      	  	 	</table>
      	  	 	</form>
      	  	 </td>
      	  </tr>      	  
          <tr bgcolor="#eeeeee" height=30>
          	   <td align="center" width=30>编号</td>
		       <td align="center" width=120>关键字</td>
		       <td align="center" width=160>URL</td>
		       <td align="center" width=120>分组</td>
		       <td align="center" width=60>城市</td>
		       <td align="center" width=100>联系人</td>
		       <td align="center" width=60>QQ</td>
		       <td align="center" width=80>邮箱</td>
		       <td align="center" width=50>电话</td>
		       <td align="center" width=50>手机</td>
		       <td align="center" width=30>是否点击</td>
		       <td align="center" width=40>当前位置</td>
		       <td align="center" width=100>创建时间</td>
		       <td align="center" width=100>更新时间</td>
		       <td align="center" width=120>操作</td>
		   </tr>
          <%
              String trClass = "";
              String webUrl = "";
              
              for (int i = 0; i < itemList.size(); i ++)
              {
                  KeywordContactVO keywordContactVO = (KeywordContactVO) itemList.get(i);                  
                  trClass= "";
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                 	 <td><%=keywordContactVO.getUuid()%></td>                                            
                     <td><%=keywordContactVO.getKeyword()%></td>
                     <td><%=keywordContactVO.getUrl()%></td>
                     <td><%=Utils.isNullOrEmpty(keywordContactVO.getGroup()) ? "" : keywordContactVO.getGroup()%></td>
                     <td><%=Utils.isNullOrEmpty(keywordContactVO.getCity()) ? "" : keywordContactVO.getCity()%></td>
                     <td><%=keywordContactVO.getContactPerson()%></td>
                     <td><%=keywordContactVO.getQq()%></td>
                     <td><%=keywordContactVO.getEmail()%></td>
                     <td><%=keywordContactVO.getPhone()%></td>
                     <td><%=keywordContactVO.getMobile()%></td>
                     <td><%=keywordContactVO.isClicked()%></td>
                     <td><%=keywordContactVO.getPosition()%></td>
                     <td><%=Utils.formatDatetime(keywordContactVO.getCreateTime(), "yyyy-MM-dd HH:mm")%></td>
                     <td><%=Utils.formatDatetime(keywordContactVO.getLastCaptureTime(), "yyyy-MM-dd HH:mm")%></td>
                     <td><a href="javascript:copyQQ('<%=keywordContactVO.getUuid()%>', '<%=keywordContactVO.getQq()%>')">拷贝QQ</a> </td>
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
      
   <br><br><br>
  <br>
  
  <br><br><br><br><br><br><br><br>
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
function copyQQ(uuid, qqNumber)
{
   window.clipboardData.setData("Text", qqNumber);
   document.location = "updateKeywordContact.jsp?uuid=" + uuid + "&fileName=<%=URLEncoder.encode(fileName, "utf-8")%>";
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

</div>
</body>
</html>

