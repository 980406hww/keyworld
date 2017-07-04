<%@page contentType="text/html;charset=utf-8"   errorPage="error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,java.util.*"%>



<html>
	<head>
		  <title>找回密码</title>
		  
  </head>	
  
  <script language="javascript">
	 	      
				  function checkinput()
				  {
				  	  var email = document.getElementById("email");
				  	  if (email.value == "")
				  	  {
				  	  	  alert("请输入注册账号");
				  	  	  email.focus();
				  	  	  return false;
				  	  }
				  	  
				  	  var regEmail = /^[\w]+(\.[\w]+)*@[\w]+(\.[\w]+)+$/;
				  	  if (!regEmail.test(email.value)) 
				  	  {
                    alert("注册账号格式有误,请注意是邮箱格式！\n\n 账号格式如: abc@qq.com");
                    return false;
              }
				  	  
				  	  var btnsub = document.getElementById("btnsub");
				  	  btnsub.disabled = true;
				  	  return true; 
				  }
				  
				  function isNum(s)
					{
					   var r,re;
					   re = /\d*/i; 
					   r = s.match(re);
					   return (r==s)?true:false;
					}
					
					function checkIsChinese(str) 
					{   
							var  index = escape(str).indexOf("%u");  
              if(index < 0){return false;}else{return true}
					}
					
					function trim(val)
					{
						  var re = /\s*((\S+\s*)*)/;
				      return val.replace(re, "$1"); 
					}
					
			</script>
			
<body>
	
<form method="post" onsubmit="return checkinput();" action="/user/getpassword2.jsp">
	
<table width="100%" style="font-size:12px;"  >
	<tr>
		<td align="center">
	     
	 	   <div style="text-align:right;width:700px;"><a href="/">回到首页</a> | <a href="/bd.html">登录</a></div>
	 	   
			 	   <table width=700 style="font-size:12px;" cellpadding=5>
			 	   	  
			 	   	  <tr><td align="right">注册账号:</td> <td><input type="text" name="email" id="email" value=""style="width:200px;"> 输入您注册时候的账号邮箱</td></tr>
			 	   	  
		           <tr><td align="right"></td> 
		            	<td>
		            	    <input type="submit" name="btnsub" id="btnsub" value=" 提交 ">
		            </td>
		           </tr>
		       </table>
  </td>
  </tr>
</table>

<center>
	  
</center>
</form>	 
</body>
<html>




