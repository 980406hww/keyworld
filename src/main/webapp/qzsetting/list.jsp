<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*,java.net.URLEncoder"%>
<%@ page import="com.keymanager.monitoring.entity.QZSetting" %>
<%@ page import="com.keymanager.monitoring.service.QZSettingService" %>
<%@ page import="com.keymanager.monitoring.service.QZOperationTypeService" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="sch" scope="page" class="com.keymanager.util.SpringContextHolder" />
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
	String condition = " ";

	String curPage = request.getParameter("pg");

	if (curPage == null || curPage.equals(""))
	{
		curPage = "1";
	}

	int iCurPage = Integer.parseInt(curPage);
	String uuid = request.getParameter("uuid");
	String customer = request.getParameter("customer");
	String customerUuid = null;
	if(customer != null){
		String []customerArray = customer.split("_____");
		if(customerArray.length == 2){
			customerUuid = customerArray[1];
		}
	}
	String domain = request.getParameter("domain");
	String group = request.getParameter("group");
	String updateStatus = request.getParameter("updateStatus");

	String pageUrl = "";

	if (!Utils.isNullOrEmpty(uuid)){
		pageUrl = pageUrl + "&uuid=" + uuid;
	}
	if (!Utils.isNullOrEmpty(customerUuid)){
		pageUrl = pageUrl + "&customerUuid=" + customerUuid;
	}
	if (!Utils.isNullOrEmpty(domain)){
		pageUrl = pageUrl + "&domain=" + domain;
	}else{
		domain = "";
	}
	if (!Utils.isNullOrEmpty(group)){
		pageUrl = pageUrl + "&group=" + group;
	}else{
		group = "";
	}
	if (!Utils.isNullOrEmpty(updateStatus)){
		pageUrl = pageUrl + "&updateStatus=" + updateStatus;
	}else{
		updateStatus = "";
	}

	QZSettingService qzss = (QZSettingService)SpringContextHolder.getBean("QZSettingService");
	List itemList = qzss.searchQZSettings(uuid != null ? Long.parseLong(uuid) : null, customerUuid != null ? Long.parseLong(customerUuid) : null,
			domain, group, updateStatus);

	QZOperationTypeService qzots = (QZOperationTypeService)SpringContextHolder.getBean("QZOperationTypeService");
	List<QZSetting> expiredCharge = qzots.expiredCharge();
	List<QZSetting> nowCharge = qzots.nowCharge();
	List<QZSetting> threeCharge = qzots.threeCharge();
	List<QZSetting> sevenCharge = qzots.sevenCharge();

	String chargeDays = request.getParameter("chargeDays");
	if(chargeDays.equals("-1")) {
		itemList = expiredCharge;
	} else if(chargeDays.equals("0")) {
		itemList = nowCharge;
	} else if(chargeDays.equals("3")) {
		itemList = threeCharge;
	} else if(chargeDays.equals("7")) {
		itemList = sevenCharge;
	}

	int recordCount = itemList != null ? itemList.size() : 0;
	int pageCount = recordCount / 100 + (recordCount % 100 > 0 ? 1 : 0);

	String fileName = "/qzsetting/list.jsp?" + pageUrl;
	String pageInfo = Utils.getPageInfo(iCurPage, 100, recordCount, pageCount, "", fileName);

	List<CustomerVO> customerVOs = cm.searchCustomer(datasourceName, " AND fStatus = 1 ");
%>

<html>
<head>
	<title>全站设置清单</title>
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
		#div2 {
			display: none;
			background-color: #ACF106;
			color: #E80404;
			font-size: 20px;
			line-height: 18px;
			border: 2px solid #104454;
			width: 100px;
			height: 22px;
		}
		#changeSettingDialog {
			display: none;
			margin: -125px 0px 0px -160px;
			background-color: white;
			color: #2D2A2A;
			font-size: 12px;
			line-height: 12px;
			border: 2px solid #104454;
			width: 340px;
			height: 250px;
			left: 50%;
			top: 30%;
			z-index: 25;
			position: fixed;
		}
		#settingTable {
			margin: 10px 10px 10px 8px;
		}
		#chargeDialogTable {
			font-size:12px;
			margin: 10px 10px 0px 30px;
		}
		#chargeDialog {
			display: none;
			margin: -125px 0px 0px -160px;
			background-color: white;
			color: #2D2A2A;
			font-size: 12px;
			line-height: 12px;
			border: 2px solid #104454;
			width: 375px;
			height: 145px;
			left: 50%;
			top: 35%;
			z-index: 25;
			position: fixed;
		}
		#PCChargeInfo {
			display: none;
		}
		#PhoneChargeInfo {
			display: none;
		}
		#chargeLogListDiv {
			display: none;
			overflow-y: auto;
			height: 400px;
			width:400px;
			margin: -125px 0px 0px -160px;
			left: 48%;
			top: 35%;
			z-index:2;
			position:fixed;
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
		<td colspan="15" align="left">
			<%@include file="/menu.jsp" %>
		</td>
	</tr>
	<tr>
		<td colspan="15" align="right">
			<a href="javascript:showSettingDialog(null, this)">增加全站设置</a>
			| <a target="_blank" href="javascript:updateImmediately(this)">马上更新</a>
			| <a target="_blank" href="javascript:delSelectedQZSettings(this)">删除所选</a>
		</td>
	</tr>
	<tr>
		<td colspan="15" align="right">
			<a href="javascript:chargeRemind('-1')">过期未收费(<%=expiredCharge.size()%>)</a>
			| <a target="_blank" href="javascript:chargeRemind('0')">当天收费提醒(<%=nowCharge.size()%>)</a>
			| <a target="_blank" href="javascript:chargeRemind('3')">三天收费提醒(<%=threeCharge.size()%>)</a>
			| <a target="_blank" href="javascript:chargeRemind('7')">七天收费提醒(<%=sevenCharge.size()%>)</a>
		</td>
	</tr>
	<tr>
		<td colspan="15">
			<form method="post" id="chargeForm" action="list.jsp">
				<input type="hidden" id="chargeDays" name="chargeDays" value="NaN"/>
				<table style="font-size:12px;">
					<tr>
						<td align="right">客户:</td>
						<td><input type="text" name="customer" id="customer" list="customer_list" value='<%=(customer != null) ? customer : ""%>'
								   style="width:200px;"></td>
						<td align="right">域名:</td> <td><input type="text" name="domain" id="domain" value="<%=domain%>" style="width:200px;"></td>
						<td align="right">组名:</td> <td><input type="text" name="group" id="group" value="<%=group%>" style="width:200px;"></td>
						<td align="right">状态:</td>
						<td>
							<select name="updateStatus" id="updateStatus" style="width:200px;">
								<%
									String []statusNames = {"", "Processing", "Completed", "DownloadTimesUsed"};
									for (int i = 0; i < statusNames.length; i ++) {
										if (statusNames[i].equals(updateStatus)) {
											out.println("<option selected value='" + statusNames[i] + "'>" + statusNames[i] + "</option>");
										} else {
											out.println("<option value='" + statusNames[i] + "'>" + statusNames[i] + "</option>");
										}
									}
								%>
							</select>
						</td>
						<td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr bgcolor="#eeeeee" height=30>
		<td align="center" width=10><input type="checkbox" onclick="selectAll(this)" /></td>
		<td align="center" width=150>客户</td>
		<td align="center" width=100>域名</td>
		<td align="center" width=80>入口类型</td>
		<td align="center" width=80>电脑分组</td>
		<td align="center" width=80>手机分组</td>
		<td align="center" width=80>去掉没指数</td>
		<td align="center" width=80>去掉没排名</td>
		<td align="center" width=60>更新间隔</td>
		<td align="center" width=60>更新状态</td>
		<td align="center" width=80>更新开始时间</td>
		<td align="center" width=80>更新结束时间</td>
		<td align="center" width=80>更新时间</td>
		<td align="center" width=80>添加时间</td>
		<td align="center" width=100>操作</td>
		<div id="div1"></div>
		<div id="div2"></div>
	</tr>
	<%
		String trClass = "";
		String webUrl = "";
		String keywordColor = "";
		for (int i = 0; i < itemList.size(); i ++) {
			QZSetting value = (QZSetting) itemList.get(i);
			trClass= "";
			keywordColor = "";
			if ((i % 2) != 0) {
				trClass = "bgcolor='#eeeeee'";
			}
	%>
	<tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">
		<td><input type="checkbox" name="uuid" value=<%=value.getUuid()%> /></td>
		<td>
			<%=value.getContactPerson()%>
		</td>
		<td>
			<a href="http://www.aizhan.com/cha/<%=value.getDomain()%>" style="text-decoration:none" target="_blank"><%=value.getDomain()%></a>
		</td>
		<td>
			<%=value.getType()%>
		</td>
		<td>
			<%=value.getPcGroup() == null ? "" : value.getPcGroup()%>
		</td>
		<td>
			<%=value.getPhoneGroup() == null ? "" : value.getPhoneGroup()%>
		</td>
		<td>
			<%=value.isIgnoreNoIndex() ? "是" : "否"%>
		</td>
		<td>
			<%=value.isIgnoreNoOrder() ? "是" : "否"%>
		</td>
		<td>
			<%=value.getUpdateInterval()%>天
		</td>
		<td>
			<%=(value.getUpdateStatus() != null ? value.getUpdateStatus() : "")%>
		</td>
		<td>
			<%=Utils.formatDate(value.getUpdateStartTime(), "MM-dd HH:mm")%>
		</td>
		<td>
			<%=Utils.formatDate(value.getUpdateEndTime(), "MM-dd HH:mm")%>
		</td>
		<td>
			<%=Utils.formatDate(value.getUpdateTime(), "MM-dd HH:mm")%>
		</td>
		<td>
			<%=Utils.formatDate(value.getCreateTime(), "MM-dd HH:mm")%>
		</td>
		<td>
			<a href="javascript:showChargeDialog('<%=value.getUuid()%>','<%=value.getContactPerson()%>','<%=value.getDomain()%>',this)">收费</a> |
			<a href="javascript:showSettingDialog('<%=value.getUuid()%>', this)">修改</a> |
			<a href="javascript:delQZSetting(<%=value.getUuid()%>)">删除</a> |
			<a href="javascript:showChargeLog('<%=value.getUuid()%>', this)">收费记录</a>
		</td>
	</tr>
	<%
		}
	%>
	<tr>
		<td colspan="11">
			<br>
			<%=pageInfo%>
		</td>
	</tr>
</table>
<br><br><br>
<br>

<br><br><br><br><br><br><br><br>
<script language="javascript">
    var divHeight = 250; // 修改UI的div高度
    var inputHeight = 25; // 修改UI的input高度
    var space = 3; // 修改UI两个表格的间隙
    var trHeight; // 修改UI表格高度
    var dateStr = new Date(); // 当天日期
    var m = dateStr.getMonth() + 1 < 10 ? "0" + (dateStr.getMonth() + 1) : (dateStr.getMonth() + 1);
    var d = dateStr.getDate() < 10 ? "0" + dateStr.getDate() : dateStr.getDate();
    var today = dateStr.getFullYear() + "-" + m + "-" + d;

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
    function delQZSetting(uuid) {
        if (confirm("确实要删除这个全站设置吗?") == false) return;
        $$$.ajax({
            url: '/spring/qzsetting/delete/' + uuid,
            type: 'Get',
            success: function (data) {
                if(data){
                    showInfo("删除成功！", self);
                    window.location.reload();
                }else{
                    showInfo("删除失败！", self);
                }
            },
            error: function () {
                showInfo("删除失败！", self);
                window.location.reload(t);
            }
        });
    }

    function getSelectedIDs() {
      var uuids = '';
      $$$.each($$$("input[name=uuid]:checkbox:checked"), function(){
        if(uuids === ''){
          uuids = $$$(this).val();
		}else{
          uuids = uuids + "," + $$$(this).val();
		}
	  });
	  return uuids;
    }

    function delSelectedQZSettings(self) {
        var uuids = getSelectedIDs();
        if(uuids === ''){
            alert('请选择要操作的设置信息！');
            return ;
        }
        if (confirm("确实要删除这些关键字吗?") == false) return;
        var postData = {};
        postData.uuids = uuids.split(",");
        $$$.ajax({
            url: '/spring/qzsetting/deleteQZSettings',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (data) {
                if(data){
                    showInfo("操作成功！", self);
                    window.location.reload();
                }else{
                    showInfo("操作失败！", self);
                }
            },
            error: function () {
                showInfo("操作失败！", self);
                window.location.reload();
            }
        });
    }

    function updateImmediately(self) {
        var uuids = getSelectedIDs();
        if(uuids === ''){
            alert('请选择要操作的设置信息！');
            return ;
        }
        if (confirm("确实要马上更新这些设置吗？") == false) return;
        var postData = {};
        postData.uuids = uuids;
        $$$.ajax({
            url: '/spring/qzsetting/updateImmediately',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (data) {
                if(data != null && data.status){
                    showInfo("操作成功！", self);
                    window.location.reload();
                }else{
                    showInfo("操作失败！", self);
                }
            },
            error: function () {
                showInfo("操作失败！", self);
            }
        });
    }

    function chargeRemind(days) {
        $$$("#chargeDays").val(days);
        $$$("#chargeForm").submit();
	}

    function cancelChargeDialog() {
        resetChargeDialog();
        var chargeDialogObj = $$$("#chargeDialog");
        chargeDialogObj.find("#checkChargePC").css("display","none");
        chargeDialogObj.find("#checkChargePhone").css("display","none");
        chargeDialogObj.find("#PCChargeInfo").css("display","none");
        chargeDialogObj.find("#PhoneChargeInfo").css("display","none");
        $$$("#chargeDialog").css("height", 320);
        $$$("#chargeDialog").css("display", "none");
    }

    function showSettingDialog(uuid, self) {
        resetChargeList();
        cancelChargeDialog();
        if(uuid == null){
            resetSettingDialog(self);
            return;
        }
        //通过qzSetinguuid查询多条数据  并填充数据
        $$$.ajax({
            url: '/spring/qzsetting/getQZSetting/' + uuid,
            type: 'Get',
            success: function (data) {
                if(data != null && data.length > 0){
                    var qzSetting = data[0];
                    initSettingDialog(qzSetting, self);
                }else{
                    showInfo("获取信息失败！", self);
                }
            },
            error: function () {
                showInfo("获取信息失败！", self);
            }
        });
    }

    function resetSettingDialog(self) {
        var settingDialogDiv = $$$("#changeSettingDialog");
        settingDialogDiv.find("#qzSettingUuid").val("");
        settingDialogDiv.find("#qzSettingCustomer").val("");
        settingDialogDiv.find("#qzSettingDomain").val("");
        settingDialogDiv.find("#qzSettingIgnoreNoIndex").val("1");
        settingDialogDiv.find("#qzSettingIgnoreNoOrder").val("1");
        settingDialogDiv.find("#qzSettingInterval").val("2");
        settingDialogDiv.find("#qzSettingEntryType").val("<%=type%>");
        settingDialogDiv[0].style.left = getTop(self); //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
        clearInfo("Both");
        settingDialogDiv.show();
    }

    function resetChargeDialog() {
      var chargeDialogObj = $$$("#chargeDialog");
      var operationTypes = chargeDialogObj.find("input[name=operationType]");
	  $$$.each(operationTypes,function (idx, val) {
        val.checked = false;
        chargeDialogObj.find("#qzOperationTypeUuid" + val.id).val("");
        chargeDialogObj.find("#initialKeywordCount" + val.id).val("");
        chargeDialogObj.find("#currentKeywordCount" + val.id).val("");
        chargeDialogObj.find("#receivableAmount" + val.id).val("");
        chargeDialogObj.find("#planChargeDate" + val.id).val("");
        chargeDialogObj.find("#nextChargeDate" + val.id).val("");
        chargeDialogObj.find("#actualAmount" + val.id).val("");
        chargeDialogObj.find("#actualChargeDate" + val.id).val("");
      });
    }

    function showChargeDialog(uuid,contactPerson,domain,self) {
        resetChargeList();
        cancelChangeSetting();
        cancelChargeDialog();
        var chargeDialogObj = $$$("#chargeDialog");
        chargeDialogObj.find("#qzSettingCustomer").val(contactPerson);
        chargeDialogObj.find("#qzSettingDomain").val(domain);
        $$$.ajax({
            url: '/spring/qzchargelog/getQZChargeLog/' + uuid,
            type: 'Get',
            success: function (chargeInfos) {
                if(chargeInfos != null && chargeInfos.length > 0){
                  var totalAmount = 0;
                  var showFlag = false;
                  $$$.each(chargeInfos, function(idx, val){
                    var checkbox = chargeDialogObj.find("#" + val.operationType);

                    if(val.qzOperationTypeUuid != null) {
                      // 存在此类操作类型
                      chargeDialogObj.find("#qzOperationTypeUuid" + val.operationType).val(val.qzOperationTypeUuid);
                      chargeDialogObj.find("#initialKeywordCount" + val.operationType).val(val.initialKeywordCount);
                      chargeDialogObj.find("#currentKeywordCount" + val.operationType).val(val.currentKeywordCount);
                      chargeDialogObj.find("#receivableAmount" + val.operationType).val(val.receivableAmount);
                      chargeDialogObj.find("#actualAmount" + val.operationType).val(val.receivableAmount);
                      totalAmount = totalAmount + Number(val.receivableAmount == null ? 0 : val.receivableAmount);
                      chargeDialogObj.find("#checkCharge" + val.operationType).css("display","block");
                      // 达标
                      if(val.planChargeDate != null) {
                        checkbox[0].checked = true;
                        chargeDialogObj.find("#" + val.operationType + "ChargeInfo").css("display","block");
                        var date = new Date(val.planChargeDate);
                        var year = date.getFullYear();
                        var month = date.getMonth() + 2;
                        if(month < 10) {
                          month = "0" + month;
                        } else {
                          if(month > 12) {
                            month = "01";
                            year = year + 1;
                          }
                        }
                        var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
                        var nextChargeDate = year + "-" + month + "-" + day;
                        chargeDialogObj.find("#receivableAmount" + val.operationType).val(val.receivableAmount);
                        chargeDialogObj.find("#planChargeDate" + val.operationType).val(val.planChargeDate);
                        chargeDialogObj.find("#nextChargeDate" + val.operationType).val(nextChargeDate);
                        showFlag = true;
                      } else {
                        checkbox[0].checked = false;
                        chargeDialogObj.find("#" + val.operationType + "ChargeInfo").css("display","none");
                      }
                      chargeDialogObj.find("#actualChargeDate" + val.operationType).val(today);
                    } else {
                      chargeDialogObj.find("#checkCharge" + val.operationType).css("display","none");
                      chargeDialogObj.find("#" + val.operationType + "ChargeInfo").css("display","none");
                    }
				  });

                  if(!showFlag){
                    chargeDialogObj.find("#chargeInfoTable").css("height",0);
                    chargeDialogObj.css("height",145);
				  }

				  var s = new String(totalAmount);
				  var total = s.replace(/\B(?=(?:\d{3})+$)/g, ',');
                  chargeDialogObj.find("#totalAmount").html(total+"元");
                  chargeDialogObj.css("display","block");
                }else{
                    showInfo("获取信息失败！", self);
                }
            },
            error: function () {
                showInfo("获取信息失败！", self);
            }
        });
    }

    function initSettingDialog(qzSetting, self) {
      var PCType = false;
      var PhoneType = false;
      clearInfo("Both");
      var settingDialogDiv = $$$("#changeSettingDialog");
      settingDialogDiv.find("#qzSettingUuid").val(qzSetting.uuid);
      settingDialogDiv.find("#qzSettingCustomer").val(
          qzSetting.contactPerson + "_____" + qzSetting.customerUuid);
      settingDialogDiv.find("#qzSettingDomain").val(
          qzSetting.domain != null ? qzSetting.domain : "");
      settingDialogDiv.find("#qzSettingIgnoreNoIndex").val(qzSetting.ignoreNoIndex ? "1" : "0");
      settingDialogDiv.find("#qzSettingIgnoreNoOrder").val(qzSetting.ignoreNoOrder ? "1" : "0");
      settingDialogDiv.find("#qzSettingInterval").val(
          qzSetting.updateInterval != null ? qzSetting.updateInterval : "");
      settingDialogDiv.find("#qzSettingEntryType").val(
          qzSetting.type != null ? qzSetting.type : "");
      settingDialogDiv[0].style.left = getTop(self); //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容

      // 操作类型表填充数据
      $$$.each(qzSetting.qzOperationTypes, function (idx, val) {
        settingDialogDiv.find("#group" + val.operationType).val(val.group);
        settingDialogDiv.find("#initialKeywordCount" + val.operationType).val(
            val.initialKeywordCount);
        settingDialogDiv.find("#currentKeywordCount" + val.operationType).val(
            val.currentKeywordCount);
        settingDialogDiv.find("#qzSettingUuid" + val.operationType).val(val.uuid);

        // 构造规则表
        $$$.each(val.qzChargeRules, function (chargeRuleIdx, chargeRuleVal) {
          addRow("chargeRule" + val.operationType, chargeRuleVal);
          if (val.operationType === 'PC') {
            PCType = true;
          }
          if (val.operationType === 'Phone') {
            PhoneType = true;
          }
        });
      });

      divHeight = divHeight + $("#groupHeightPC").height() + $("#ruleHeightPC").height();
      divHeight = divHeight + $("#groupHeightPhone").height() + $("#ruleHeightPhone").height();
      if (PCType) {
        divHeight = divHeight - inputHeight;
        dealSettingTable("PC");
      }

      if (PhoneType) {
        divHeight = divHeight - inputHeight;
        dealSettingTable("Phone");
      }

      settingDialogDiv.show();
    }

    //规则表验证
    var reg = /^[1-9]\d*$/;
    function saveChangeSetting(self) {
      var settingDialogDiv = $$$("#changeSettingDialog");
      var qzSetting = {};
      qzSetting.uuid = settingDialogDiv.find("#qzSettingUuid").val();

      var customer = settingDialogDiv.find("#qzSettingCustomer").val();
      if (customer == null || customer === "") {
        alert("请选择客户");
        settingDialogDiv.find("#qzSettingCustomer").focus();
        return;
      }
      qzSetting.domain = settingDialogDiv.find("#qzSettingDomain").val();
      if (qzSetting.domain == null || qzSetting.domain === "") {
        alert("请输入域名");
        settingDialogDiv.find("#qzSettingDomain").focus();
        return;
      }
      qzSetting.ignoreNoIndex = settingDialogDiv.find("#qzSettingIgnoreNoIndex").val() === "1"
          ? true : false;
      qzSetting.ignoreNoOrder = settingDialogDiv.find("#qzSettingIgnoreNoOrder").val() === "1"
          ? true : false;
      qzSetting.updateInterval = settingDialogDiv.find("#qzSettingInterval").val();
      qzSetting.pcGroup = settingDialogDiv.find("#groupPC").val();
      qzSetting.phoneGroup = settingDialogDiv.find("#groupPhone").val();

      if (customer != null && customer != '') {
        var customerArray = customer.split("_____");
        if (customerArray.length == 2) {
          qzSetting.customerUuid = customerArray[1];
        } else {
          alert("请从列表中选择客户");
          settingDialogDiv.find("#qzSettingCustomer").focus();
          return;
        }
      }
      var entryType = settingDialogDiv.find("#qzSettingEntryType").val();
      qzSetting.type = entryType;
      qzSetting.qzOperationTypes = [];//操作类型表
      qzSetting.qzOperationTypes.qzChargeRules = [];//收费规则

      var checkedObjs = settingDialogDiv.find("input[name=operationType]:checkbox:checked");
      var validationFlag = true;
      $$$.each(checkedObjs, function (idx, val) {
        var ruleObj = settingDialogDiv.find("#chargeRule" + val.id);
        var operationType = {};
        operationType.qzChargeRules = [];
        operationType.operationType = val.id;
        operationType.group = settingDialogDiv.find("#group" + val.id).val();
        operationType.initialKeywordCount = settingDialogDiv.find(
            "#initialKeywordCount" + val.id).val();
        operationType.currentKeywordCount = settingDialogDiv.find(
            "#currentKeywordCount" + val.id).val();

        if (operationType.group == null || operationType.group === "") {
          alert("请输入分组");
          settingDialogDiv.find("#group" + val.id).focus();
          validationFlag = false;
          return false;
        }
        if (operationType.initialKeywordCount == null || operationType.initialKeywordCount === "") {
          alert("请输入初始词量");
          settingDialogDiv.find("#initialKeywordCount" + val.id).focus();
          validationFlag = false;
          return false;
        }
        if (!reg.test(operationType.initialKeywordCount)) {
          alert("请输入数字");
          settingDialogDiv.find("#initialKeywordCount" + val.id).focus();
          validationFlag = false;
          return false;
        }

        //多条规则
        var endKeyWordCountValue = -1;
        var trObjs = ruleObj.find("tr:not(:first,:last)");
        $$$.each(trObjs, function (idx, val) {
          var startKeywordCountObj = $$$(val).find("input[name=startKeywordCount]");
          var endKeywordCountObj = $$$(val).find("input[name=endKeywordCount]");
          var amountObj = $$$(val).find("input[name=amount]");

          var chargeRule = {};
          chargeRule.startKeywordCount = startKeywordCountObj.val();
          chargeRule.endKeywordCount = endKeywordCountObj.val();
          chargeRule.amount = amountObj.val();
          operationType.qzChargeRules.push(chargeRule);

          if (startKeywordCountObj.val() == null || startKeywordCountObj.val() == "") {
            alert("请输入起始词数");
            startKeywordCountObj[0].focus();
            validationFlag = false;
            return false;
          }
          if (!reg.test(startKeywordCountObj.val())) {
            alert("请输入数字");
            startKeywordCountObj.focus();
            validationFlag = false;
            return false;
          }

          var skc = Number(startKeywordCountObj.val());
          if (skc <= endKeyWordCountValue) {
            alert("起始词数过小");
            startKeywordCountObj.focus();
            validationFlag = false;
            return false;
          }

          if (idx < (trObjs.length - 1)) {
            if (endKeywordCountObj.val() == null || endKeywordCountObj.val() == "") {
              alert("请输入终止词数");
              endKeywordCountObj.focus();
              validationFlag = false;
              return false;
            }
          } else {
            if(endKeywordCountObj.val() != "" && operationType.currentKeywordCount>Number(endKeywordCountObj.val())){
              alert("最后一条规则中的终止词量必须大于当前词量");
              endKeywordCountObj.focus();
              validationFlag = false;
              return false;
			}
		  }

          if (endKeywordCountObj.val() != "") {
            if (!reg.test(endKeywordCountObj.val())) {
              alert("请输入数字");
              endKeywordCountObj.focus();
              validationFlag = false;
              return false;
            }
            if (Number(endKeywordCountObj.val()) <= skc) {
              alert("终止词数必须大于起始词数");
              endKeywordCountObj.focus();
              validationFlag = false;
              return false;
            }
          }

          if (amountObj.val() == null || amountObj.val() == "") {
            alert("请输入价格");
            amountObj.focus();
            validationFlag = false;
            return false;
          }
          if (!reg.test(amountObj.val())) {
            alert("输入的价格不合理");
            amountObj.focus();
            validationFlag = false;
            return false;
          }
          endKeyWordCountValue = Number(endKeywordCountObj.val());
        });
        if (!validationFlag) {
          return false;
        }
        qzSetting.qzOperationTypes.push(operationType);
      });

      if (validationFlag) {
        if (checkedObjs.length == 0) {
          alert("保存失败，必须要增加一条规则");
          return;
        }
        $$$.ajax({
          url: '/spring/qzsetting/save',
          data: JSON.stringify(qzSetting),
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          timeout: 5000,
          type: 'POST',
          success: function (data) {
            settingDialogDiv.hide();
            if (data != null && data != "") {
              showInfo("更新成功！", self);
              window.location.reload();
            } else {
              showInfo("更新失败！", self);
              window.location.reload();
            }
          },
          error: function () {
            showInfo("更新失败！", self);
            settingDialogDiv.hide();
          }
        });
      }
    }
    
    function resetChargeList() {
        $("#chargeLogListDiv").hide();
    }

    function showChargeLog(uuid, self) {
      $("#chargeLogListDiv").hide();
      cancelChargeDialog();
      cancelChangeSetting();
      $("#chargeLogListTable  tr:not(:first,:last)").remove();
      $$$.ajax({
        url: '/spring/qzchargelog/chargesList/' + uuid,
        type: 'Get',
        success: function (qzChargeLogs) {
          if (qzChargeLogs != null && qzChargeLogs.length > 0) {
            $$$.each(qzChargeLogs, function (idx, val) {
              var newTr = document.createElement("tr")
              var chargeLogElements = [
                toDateFormat(new Date(val.actualChargeDate)),
                val.operationType,
                val.actualAmount,
                val.userName,
                toTimeFormat(new Date(val.createTime))
              ];
              $$$.each(chargeLogElements, function () {
                var newTd = document.createElement("td");
                newTr.appendChild(newTd);
                newTd.innerHTML = this;
              });

              // 新增一行
              var row = document.getElementById("lastTr");
              row.parentNode.insertBefore(newTr, row);
            });
            $$$("#chargeLogListDiv").show();
          } else {
            alert("暂无收费记录");
          }
        },
        error: function () {
          showInfo("获取信息失败！", self);
        }
      });
    }

    function toTimeFormat(time) {
      var date = toDateFormat(time);
      var hours = time.getHours() < 10 ? ("0" + time.getHours()) : time.getHours();
      var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
      var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
      return date + " " + hours + ":" + minutes + ":" + seconds;
    };

    function toDateFormat (time) {
      return time.getFullYear() + "-" +
		  (time.getMonth() + 1) + "-" +
          time.getDate();
    };

    function saveChargeLog(self) {
      var chargeDialog = $$$("#chargeDialog");
      var selectedOperationTypes = chargeDialog.find("input[name=operationType]:checkbox:checked");
	  var saveChargeLogFlag = true;
      if (selectedOperationTypes.length == 0) {
        alert("必须选择一个收费项才能收费");
        saveChargeLogFlag = false;
        return;
      }
      var chargeLogs = [];
      $$$.each(selectedOperationTypes, function (index, val) {
        var chargeLog = {};
        chargeLog.qzOperationTypeUuid = chargeDialog.find(
            "#qzOperationTypeUuid" + val.id).val();
        chargeLog.planChargeDate = chargeDialog.find("#planChargeDate" + val.id).val();
        chargeLog.actualChargeDate = chargeDialog.find("#actualChargeDate" + val.id).val();
        chargeLog.receivableAmount = chargeDialog.find("#receivableAmount" + val.id).val();
        chargeLog.actualAmount = chargeDialog.find("#actualAmount" + val.id).val();
        chargeLog.nextChargeDate = chargeDialog.find("#nextChargeDate" + val.id).val();
        if (chargeLog.nextChargeDate == "" || chargeLog.nextChargeDate == null) {
          alert("下次收费日期为必填");
          chargeDialog.find("#nextChargeDate" + val.id).focus();
          saveChargeLogFlag = false;
          return;
        }
        if (chargeLog.actualAmount == "" || chargeLog.actualAmount == null) {
          alert("实收金额为必填");
          chargeDialog.find("#actualAmount" + val.id).focus();
          saveChargeLogFlag = false;
          return;
        }
        if (!reg.test(chargeLog.actualAmount)) {
          alert("请输入合理的金额");
          chargeDialog.find("#actualAmount" + val.id).focus();
          saveChargeLogFlag = false;
          return;
        }
        if (chargeLog.actualChargeDate == "" || chargeLog.actualChargeDate == null) {
          alert("实际收费日期为必填");
          chargeDialog.find("#actualChargeDate" + val.id).focus();
          saveChargeLogFlag = false;
          return;
        }
        chargeLogs.push(chargeLog);
      });
      if(saveChargeLogFlag) {
        if (window.confirm("确认收费?")) {
          $$$.ajax({
            url: '/spring/qzchargelog/save',
            data: JSON.stringify(chargeLogs),
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (data) {
              cancelChargeDialog();
              if (data != null && data != "") {
                showInfo("收费成功！", self);
                window.location.reload();
              } else {
                showInfo("收费失败！", self);
                window.location.reload();
              }
            },
            error: function () {
              showInfo("收费失败！", self);
            }
          });
        } else {
          alert("取消收费");
        }
      }
    }

    function calTotalAmount() {
      	var totalAmount = 0;
        var chargeDialog = $$$("#chargeDialog");
        $$$.each(chargeDialog.find("input[name=operationType]:checkbox:checked"), function(idx, val){
          totalAmount = totalAmount + Number(chargeDialog.find("#actualAmount" + val.id).val());
		});
        var str = new String(totalAmount);
        var total = str.replace( /\B(?=(?:\d{3})+$)/g, ',' );
        chargeDialog.find("#totalAmount").html(total + "元");
    }

    function cancelChangeSetting(){
        clearInfo("Both");
        var settingDialogDiv = $$$("#changeSettingDialog");
        settingDialogDiv.hide();
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
    function showTip(content,e){
        var event = e||window.event;
        var pageX = event.pageX;
        var pageY = event.pageY;
        if(pageX==undefined){
            pageX=event.clientX+document.body.scrollLeft||document.documentElement.scrollLeft;
        }
        if(pageY==undefined){
            pageY = event.clientY+document.body.scrollTop||document.documentElement.scrollTop;
        }
        var div1 = document.getElementById('div1'); //将要弹出的层
        div1.innerText = content;
        div1.style.display="block"; //div1初始状态是不可见的，设置可为可见
        div1.style.left=pageX+10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
        div1.style.top=pageY+5;
        div1.style.position="absolute";
    }

    function showInfo(content, e) {
        e = e || window.event;
        var div1 = document.getElementById('div2'); //将要弹出的层
        div1.innerText = content;
        div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见
        div1.style.left = getLeft(e) + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
        div1.style.top = getTop(e) + 5;
        div1.style.position = "absolute";

        var intervalID = setInterval(function(){
            div1.style.display = "none";
        }, 3000);
    }

    //关闭层div1的显示
    function closeTip(){
        var div1 = document.getElementById('div1');
        div1.style.display="none";
    }

    function addRow(tableID){
    	addRow(tableID, null);
    }

    function addRow(tableID, chargeRule){
      var tableObj = $$$("#" + tableID);
      var rowCount = tableObj.find("tr").length;
      var newRow = tableObj[0].insertRow(rowCount - 1); //插入新行

	  var col1 = newRow.insertCell(0);
	  col1.innerHTML="<input type='text' name='sequenceID' value='"+(rowCount - 1)+"' style='width:100%'/>";

      var col2 = newRow.insertCell(1);
      col2.innerHTML = "<input type='text' name='startKeywordCount' value='"+(chargeRule != null ? chargeRule.startKeywordCount : '')+"' style='width:100%'/>";

      var col3 = newRow.insertCell(2);
      col3.innerHTML = "<input type='text' name='endKeywordCount' value='"+((chargeRule != null && chargeRule.endKeywordCount != null) ? chargeRule.endKeywordCount : '')+"'  style='width:100%'/>";

      var col4 = newRow.insertCell(3);
      col4.innerHTML = "<input type='text' name='amount' value='"+(chargeRule != null ? chargeRule.amount : '')+"'  style='width:100%'/>";

      var col5 = newRow.insertCell(4);
      col5.innerHTML = "<input style='width:100%' type='button' value='删除' onclick='deleteCurrentRow(this.parentNode.parentNode)' />";

      divHeight = divHeight + inputHeight;
      $$$("#changeSettingDialog").css("height", divHeight);
    }

    function deleteCurrentRow(currentRow) {
        var index = currentRow.rowIndex;
        var tableObj = currentRow.parentNode.parentNode;
        if(tableObj.rows.length > 3) {
            tableObj.deleteRow(index);
            divHeight = divHeight - inputHeight;
            $$$("#changeSettingDialog").css("height", divHeight);
            for(var i = 1; i < tableObj.rows.length - 1; i++) {
                $$$("#"+tableObj.id).find("tr:eq("+ i +")").find("input[name=sequenceID]").val(i);
			}
        } else {
            alert("删除失败，规则表不允许为空");
        }
    }

    function clearInfo(type) {
      var settingDialogObj = $$$("#changeSettingDialog");
      if(type == "Both") {
          clearInfo("PC");
          clearInfo("Phone");
          divHeight = 250;
          settingDialogObj.css("height",divHeight);
        } else {
            // 清空分组表格信息
            settingDialogObj.find("#group" + type).val("");
            settingDialogObj.find("#initialKeywordCount" + type).val("");
            settingDialogObj.find("#currentKeywordCount" + type).val("");
            settingDialogObj.find("#qzOperationTypeUuid" + type).val("");
            // 清空规则表格信息
            settingDialogObj.find("#chargeRule" + type).find("tr:not(:first,:last)").remove();
            settingDialogObj.find("#" + type)[0].checked = false;
            settingDialogObj.find("#operationTypeSummaryInfo" + type).css("display","none");
            settingDialogObj.find("#chargeRule" + type).css("display","none");
            // 重置div高度
            divHeight = divHeight - $("#groupHeight" + type).height() - $("#ruleHeight" + type).height();
        }
    }

    function dealChargeTable(operationType) {
      var chargeDialog = $$$("#chargeDialog");
      var checkboxObj = chargeDialog.find("#" + operationType);
      var chargeInfoObj = chargeDialog.find("#" + operationType + "ChargeInfo");
      var chargeDialogHeight = $("#chargeDialog").height();
      if (chargeInfoObj.css("display") == "none" || checkboxObj[0].checked == true) {
        chargeDialog.css("height",320);
        chargeInfoObj.css("display", "block");
      } else {
        chargeInfoObj.css("display", "none");
      }
      calTotalAmount();
      var selectedOperationTypes = chargeDialog.find("input[name=operationType]:checkbox:checked");
      if (selectedOperationTypes.length == 0) {
        chargeDialog.find("#chargeInfoTable").css("height",0);
        chargeDialog.css("height",chargeDialogHeight - 178);
      } else {
        chargeDialog.find("#chargeInfoTable").css("height",185);
      }
    }

    function dealSettingTable(operationType) {
      var settingDialogDiv = $$$("#changeSettingDialog");
      var groupObj = settingDialogDiv.find('#operationTypeSummaryInfo' + operationType);
      var ruleObj = settingDialogDiv.find('#chargeRule' + operationType);
      var chargeRuleRowCount = ruleObj.find("tr").length;
      var checkboxObj = settingDialogDiv.find('#' + operationType);


      if (ruleObj.css("display") == "none" || checkboxObj[0].checked == true) {
        // 保证必须有一条规则
        if (chargeRuleRowCount <= 2) {
          addRow("chargeRule" + operationType);
          divHeight = divHeight - inputHeight;
        }
        divHeight = divHeight + 130;
        $$$("#changeSettingDialog").css("height" , divHeight );
        groupObj.css("display","block");
        ruleObj.css("display","block");
        checkboxObj[0].checked = true;
      } else {
        trHeight = $("#groupHeight" + operationType).height() + $(
                "#ruleHeight" + operationType).height();
        divHeight = divHeight - trHeight + space;
        $$$("#changeSettingDialog").css("height" , divHeight );
        clearInfo(operationType);
        groupObj.css("display","none");
        ruleObj.css("display","none");
        checkboxObj[0].checked = false;
      }
    }
</script>

<div style="display:none;">
</div>
<div id="changeSettingDialog">
	<table style="font-size:12px" id="settingTable">
		<tr>
			<td align="right"><span style="margin-right:4;">客户</span></td>
			<td>
				<input type="hidden" id="qzSettingUuid" />
				<input type="text" list="customer_list" name="qzSettingCustomer" id="qzSettingCustomer" style="width:240px" />
			</td>
		</tr>
		<tr>
			<td align="right"><span style="margin-right:4;">域名</span></td>
			<td>
				<input type="text" name="qzSettingDomain" id="qzSettingDomain" style="width:240px" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="checkbox" name="operationType" id="PC" onclick="dealSettingTable('PC')" style=""/>电脑
			</td>
		</tr>
		<%--电脑分组信息--%>
		<tr>
			<td colspan="2" id="groupHeightPC">
				<table border="0" style="display:none;font-size:12px;" cellspacing="0" cellpadding="0" id="operationTypeSummaryInfoPC">
					<tr>
						<td align="right" style="width:72px"><span style="margin-right:14;">分组</span></td>
						<td><input type="text" name="group" id="groupPC"  style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px"><span style="margin-right:14;">初始词量</span></td>
						<td colspan="4"><input type="text" name="initialKeywordCount" id="initialKeywordCountPC" style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px"><span style="margin-right:14;">当前词量</span></td>
						<td colspan="4"><input type="text" name="currentKeywordCount" id="currentKeywordCountPC" style="width:240px;margin-left: -6;" readonly/></td>
					</tr>
					<input type="hidden" id="qzSettingUuidPC" name="qzOperationTypeUuid" value="" />
				</table>
			</td>
		</tr>
		<%--电脑规则信息--%>
		<tr id="pcChargeRuleTable">
			<td colspan="2" id="ruleHeightPC">
				<table border="1" style="display:none;font-size:12px;" cellspacing="0" cellpadding="0" id="chargeRulePC">
					<tr>
						<td style="width:50px">序号</td>
						<td style="width:72px">起始词数</td>
						<td style="width:72px">终止词数</td>
						<td style="width:50px">价格</td>
						<td style="width:46px">操作</td>
					</tr>
					<tr>
						<td colspan="5">
							<input name="addRule" type="button" value="增加规则" onclick="addRow('chargeRulePC')" /></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="checkbox" name="operationType" id="Phone" onclick="dealSettingTable('Phone')" style=""/>手机
			</td>
		</tr>
		<%--手机分组信息--%>
		<tr>
			<td colspan="2" id="groupHeightPhone">
				<table border="0" style="display:none;font-size:12px;" cellspacing="0" cellpadding="0" id="operationTypeSummaryInfoPhone">
					<tr>
						<td align="right" style="width:72px;"><span style="margin-right:14;">分组</span></td>
						<td><input type="text" name="group" id="groupPhone" style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px"><span style="margin-right:14;">初始词量</span></td>
						<td colspan="4"><input type="text" name="initialKeywordCount" id="initialKeywordCountPhone"  style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px"><span style="margin-right:14;">当前词量</span></td>
						<td colspan="4"><input type="text" name="currentKeywordCount" id="currentKeywordCountPhone"  style="width:240px;margin-left: -6;" readonly/></td>
					</tr>
					<input type="hidden" id="qzSettingUuidPhone" name="qzOperationTypeUuid" value="" />
				</table>
			</td>
		</tr>
		<%--手机规则信息--%>
		<tr id="phoneChargeRuleTable">
			<td colspan="2" id="ruleHeightPhone">
				<table border="1" style="display:none;font-size:12px;" cellspacing="0" cellpadding="0" id="chargeRulePhone">
					<tr>
						<td style="width:50px">序号</td>
						<td style="width:72px">起始词数</td>
						<td style="width:72px">终止词数</td>
						<td style="width:50px">价格</td>
						<td style="width:46px">操作</td>
					</tr>
					<tr>
						<td colspan="5">
							<input name="addRule" type="button" value="增加规则" onclick="addRow('chargeRulePhone')" /></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td align="right"><span style="margin-right:4;">入口</span></td>
			<td>
				<select name="qzSettingEntryType" id="qzSettingEntryType"  style="width:240px">
					<option value="qz" selected>全站</option>
					<option value="bc">bc</option>
					<option value="pt">普通</option>
					<option value="fm">负面</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>去掉没指数</td>
			<td>
				<select name="qzSettingIgnoreNoIndex" id="qzSettingIgnoreNoIndex"  style="width:240px">
					<option value="1" selected>是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>去掉没排名</td>
			<td>
				<select name="qzSettingIgnoreNoOrder" id="qzSettingIgnoreNoOrder"  style="width:240px">
					<option value="1" selected>是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right"><span style="margin-right:4;">更新间隔</span></td>
			<td>
				<select name="qzSettingInterval" id="qzSettingInterval"  style="width:240px">
					<option value="1">1天</option>
					<option value="2" selected>2天</option>
					<option value="3">3天</option>
					<option value="5">5天</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right">
				<input type="button" id="saveChangeSetting" onClick="saveChangeSetting(this)" value="保存"/>&nbsp;&nbsp;&nbsp;<input type="button" onClick="cancelChangeSetting()" id="cancelChangeSetting" value="取消"/>
			</td>
		</tr>
	</table>
</div>
<datalist id="customer_list">
	<%
		for (int i = 0; i < customerVOs.size(); i++) {
			CustomerVO value = (CustomerVO) customerVOs.get(i);
			out.println("<option>" + value.getContactPerson() + "_____" + value.getUuid() + "</option>");
		}
	%>
</datalist>
<%--收费Dialog--%>
<div id="chargeDialog">
	<table id="chargeDialogTable">
		<tr>
			<td align="right">客户</td>
			<td>
				<input type="text" name="qzSettingCustomer" id="qzSettingCustomer" style="width: 280px" readonly/>
			</td>
		</tr>
		<tr>
			<td align="right">域名</td>
			<td>
				<input type="text" name="qzSettingDomain" id="qzSettingDomain" style="width: 280px" readonly/>
			</td>
		</tr>
		<tr>
			<td>
				<table id="checkChargePC" style="display: none;font-size:12px;">
					<tr><td>
						<input type="checkbox" name="operationType" id="PC" style="margin-left: -2px" onclick="dealChargeTable('PC')" />电脑
					</td></tr>
				</table>
			</td>

			<td>
				<table id="checkChargePhone" style="display: none;font-size:12px;">
					<tr><td>
						<input type="checkbox" name="operationType" id="Phone" onclick="dealChargeTable('Phone')" style="margin-left: 140px"/>手机
					</td></tr>
				</table>
			</td>
		</tr>
	</table>
	<table id="chargeInfoTable" style="height:185px;">
		<%--电脑收费信息--%>
		<tr>
		<td style="width: 182px">
			<table style="display:none;font-size:12px;" id="PCChargeInfo">
				<tr>
					<td align="right">初始词量</td>
					<input type="hidden" name="qzOperationTypeUuid" id="qzOperationTypeUuidPC"/>
					<td><input type="text" name="initialKeywordCount" id="initialKeywordCountPC"  style="width:100px" readonly/></td>
				</tr>
				<tr>
					<td align="right">当前词量</td>
					<td><input type="text" name="currentKeywordCount" id="currentKeywordCountPC"  style="width:100px" readonly/></td>
				</tr>
				<tr>
					<td align="right">应收金额</td>
					<td>
						<input type="text" name="receivableAmount" id="receivableAmountPC" style="width:100px" readonly />
					</td>
				</tr>
				<tr>
					<td align="right">计划收费日期</td>
					<td>
						<input type="text" name="planChargeDate" id="planChargeDatePC" style="width:100px" readonly />
					</td>
				</tr>
				<tr>
					<td align="right">下次收费日期</td>
					<td>
						<input type="text" name="nextChargeDate" id="nextChargeDatePC" class="Wdate" onClick="WdatePicker()" style="width:100px" />
					</td>
				</tr>
				<tr>
					<td align="right">实收金额</td>
					<td>
						<input type="text" name="actualAmount" id="actualAmountPC" onkeyup="calTotalAmount()" style="width:100px" />
					</td>
				</tr>
				<tr>
					<td align="right">实际收费日期</td>
					<td>
						<input name="actualChargeDate" id="actualChargeDatePC" class="Wdate" type="text" style="width:100px" onClick="WdatePicker()" value="">
					</td>
				</tr>
			</table>
		</td>
		<%--手机收费信息--%>
		<td style="width: 182px">
			<table style="display:none;font-size:12px;margin-top: 2px;margin-bottom: 2px;" id="PhoneChargeInfo">
				<tr>
					<td align="right">初始词量</td>
					<input type="hidden" name="qzOperationTypeUuid" id="qzOperationTypeUuidPhone"/>
					<td><input type="text" name="initialKeywordCount" id="initialKeywordCountPhone"  style="width:100px" readonly/></td>
				</tr>
				<tr>
					<td align="right">当前词量</td>
					<td><input type="text" name="currentKeywordCount" id="currentKeywordCountPhone"  style="width:100px" readonly/></td>
				</tr>
				<tr>
					<td align="right">应收金额</td>
					<td>
						<input type="text" name="receivableAmount" id="receivableAmountPhone" style="width:100px" readonly />
					</td>
				</tr>
				<tr>
					<td align="right">计划收费日期</td>
					<td>
						<input type="text" name="planChargeDate" id="planChargeDatePhone" style="width:100px" readonly />
					</td>
				</tr>
				<tr>
					<td align="right">下次收费日期</td>
					<td>
						<input type="text" name="nextChargeDate" id="nextChargeDatePhone" class="Wdate" onClick="WdatePicker()" style="width:100px" />
					</td>
				</tr>
				<tr>
					<td align="right">实收金额</td>
					<td>
						<input type="text" name="actualAmount" id="actualAmountPhone" onkeyup="calTotalAmount()" style="width:100px" />
					</td>
				</tr>
				<tr>
					<td align="right">实际收费日期</td>
					<td>
						<input name="actualChargeDate" id="actualChargeDatePhone" class="Wdate" type="text" style="width:100px"  onClick="WdatePicker()" value="">
					</td>
				</tr>
			</table>
		</td>
		</tr>
	</table>
	<p>
		<span style="margin-left: 55px"></span>合计:<sapn id="totalAmount"></sapn>
	</p>
	<p align="right">
		<input type="button" id="saveChargeLog" onclick="saveChargeLog(this)" value="确认收费"/>&nbsp;&nbsp;&nbsp;<input type="button" id="cancelChargeLog" onclick="cancelChargeDialog()" value="取消"/><span style="margin-right:14px;"></span>
	</p>
</div>
<%--收费详情列表--%>
<div id="chargeLogListDiv">
	<table id="chargeLogListTable" border="1" cellpadding="3" style="font-size: 12px;background-color: white;border-collapse: collapse;">
		<tr>
			<td>收费时间</td>
			<td>操作类型</td>
			<td>收费金额</td>
			<td>收费人员</td>
			<td>创建时间</td>
		</tr>
		<tr id="lastTr">
			<td colspan="5" align="right"><input type="button" onclick="resetChargeList()" value="取消"/></td>
		</tr>
	</table>
</div>
</body>
</html>