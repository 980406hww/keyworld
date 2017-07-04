<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*,java.net.URLEncoder"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="flm" scope="page" class="com.keymanager.manager.FumianListManager" />
	
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
		
		String condition = " and fTerminalType = '" + terminalType + "' ";
		
		String curPage = request.getParameter("pg");
		
		if (curPage == null || curPage.equals(""))
		{
		    curPage = "1";
		}
		
		int iCurPage = Integer.parseInt(curPage);		
	  	String keyword = request.getParameter("keyword");
	  	String url = request.getParameter("url");
	  	
	  	String pageUrl = "";
	  	
	  	if (!Utils.isNullOrEmpty(keyword)){
	  		condition = condition + " and fKeyword like '%" + keyword.trim() + "%' ";
	  		pageUrl = pageUrl + "&keyword=" + java.net.URLEncoder.encode(keyword,"UTF-8");
	  	}else{
	  		keyword = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(url)){
	  		condition = condition + " and fUrl = '" + url.trim() + "' ";
	  		pageUrl = pageUrl + "&url=" + url;
	  	}else{
	  		url = "";	
	  	}
			
	  	List itemList = flm.searchFumianListVO(datasourceName, 30 , iCurPage , condition , "", 1);
	  	
	  	int recordCount = flm.getRecordCount();
	  	int pageCount = flm.getPageCount();
	    
	  	String fileName = "/fumianlist/list.jsp?" + pageUrl;
	  	String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);		    	      
%>

<html>
	<head>
	  <title>关键字负面清单</title>
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
      <table width=100% style="font-size:12px;" cellpadding=3>
     	  <tr>
			<td colspan="8" align="left">
				<%@include file="/menu.jsp" %>	
			</td>
		  </tr>
		  <tr>
      	  	 <td colspan="8" align="right">
      	  	 	<a href="add.jsp">增加关键字负面清单</a>   
      	  	 </td>
      	  </tr>
      	  <tr> 
      	  	 <td colspan="8">
      	  	 	<form method="post" action="list.jsp">
	      	  	 	<table style="font-size:12px;">
	      	  	 		<tr>
	      	  	 			<td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="<%=keyword%>" style="width:200px;"></td>
	      	  	 			<td align="right">URL:</td> <td><input type="text" name="url" id="url" value="<%=url%>" style="width:200px;"></td>
			          	  	<td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
	      	  	 		</tr>
		      	  	 </table>
      	  	 	</form>
      	  	 </td>
      	  </tr>    
          <tr bgcolor="#eeeeee" height=30>
              <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" /></td>
              <td align="center" width=100>关键字</td>
              <td align="center" width=100>URL</td>
              <td align="center" width=200>标题</td>
              <td align="center" width=300>描述</td>
		      <td align="center" width=50>排名</td>
              <td align="center" width=100>采集日期</td>
              <td align="center" width=100>操作</td>      
			  <div id="div1"></div> 
          </tr>
          <%
              String trClass = "";
              String webUrl = "";
              String keywordColor = "";
              for (int i = 0; i < itemList.size(); i ++)
              {
                  FumianListVO value = (FumianListVO) itemList.get(i);                  
                  trClass= "";
                  keywordColor = "";
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">     
                      <td><input type="checkbox" name="uuid" value=<%=value.getUuid()%> /></td>                                       
                      <td>
                     	<%=value.getKeyword()%>
                      </td>
			         
			          <td>
			         	<%=(value.getUrl() == null) ? "" : value.getUrl().trim()%>
			          </td>
			         
			          <td>
			         	<%=(value.getTitle() == null) ? "" : value.getTitle().trim()%>
			          </td>
			         
 					  <td>
			         	<%=(value.getDesc() == null) ? "" : value.getDesc().trim()%>
			          </td>
			         
			          <td>
			         	<div style="height:16;"><%=value.getPosition()%></div>
			          </td>
                      <td><%=Utils.formatDatetime(value.getCreateTime(), "yyyy-MM-dd")%></td>
                      <td>
                      	  <a href="modify.jsp?uuid=<%=value.getUuid()%>">修改</a> |
                      	  <a href="javascript:delItem('<%=value.getUuid()%>')">删除</a>
                      </td>                            
                  </tr>
          <%
              }
          
          %>
           <tr>
          	<td colspan="8" align="right">
          		  <br>
          		  <a href="javascript:delAllItems()">删除所选数据</a>
          	</td>
          </tr>           
          <tr>
          	<td colspan="8">
          		  <br>
          		  <%=pageInfo%>
          	</td>
          </tr>          
      </table>
   <br><br><br>
  <br>
  
  <br><br><br><br><br><br><br><br>
<script language="javascript">
function selectAll(self){
	var a = document.getElementsByName("uuid");
	if(self.checked){
		for(var i = 0;i<a.length;i++){
			if(a[i].type == "checkbox"){
				a[i].checked = true;
			}
		}
	}else{
		for(var i = 0;i<a.length;i++){
			if(a[i].type == "checkbox"){
				a[i].checked = false;
			}
		}
	}
}

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
function delItem(uuid)
{
   if (confirm("确实要删除这个关键字吗?") == false) return;
   document.location = "delete.jsp?uuid=" + uuid;
}
function delAllItems()
{
	if (confirm("确实要删除这些关键字吗?") == false) return;
	var a = document.getElementsByName("uuid");
	var uuids = '';
	for(var i = 0;i<a.length;i++){
		//alert(a[i].value);
		if(a[i].checked){
			uuids = uuids + a[i].value + ',';	
		}
	}
   document.location = "deleteAll.jsp?uuids=" + uuids;
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

