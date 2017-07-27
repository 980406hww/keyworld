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
			| <a target="_blank" href="javascript:delAllItems(this)">删除所选</a>
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
		<td align="center">操作</td>
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
			<a href="javascript:delItem(<%=value.getUuid()%>)">删除</a> |
			<a href="javascript:insertChargeLog('<%=value.getUuid()%>', this)">收费记录</a>
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
    var id1 = 1; // 规则表序号
    var id2 = 1; //规则表序号
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
    function delItem(uuid) {
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
        var a = document.getElementsByName("uuid");
        var uuids = '';
        for(var i = 0;i<a.length;i++){
            //alert(a[i].value);
            if(a[i].checked){
                if(uuids === ''){
                    uuids = a[i].value;
                }else{
                    uuids = uuids + "," + a[i].value;
                }
            }
        }
        return uuids;
    }

    function delAllItems(self) {
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
        document.getElementById("chargeDays").value = days;
        $$$("#chargeForm").submit();
	}

    function cancelChargeDialog() {
        resetChargeDialog();
        document.getElementById("chargeDialog").style.height = "320px"
        document.getElementById("chargeDialog").style.display = "none";
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
        document.getElementById("operationTypePC").checked = false;
        chargeDialogObj.find("#fQzOperationTypeUuidPC").val("");
        chargeDialogObj.find("#fInitialKeywordCountPC").val("");
        chargeDialogObj.find("#fCurrentKeywordCountPC").val("");
        chargeDialogObj.find("#fReceivableAmountPC").val("");
        chargeDialogObj.find("#fPlanChargeDatePC").val("");
        chargeDialogObj.find("#fNextChargeDatePC").val("");
        chargeDialogObj.find("#fActualAmountPC").val("");
        chargeDialogObj.find("#fActualChargeDatePC").val("");

        document.getElementById("operationTypePhone").checked = false;
        chargeDialogObj.find("#fQzOperationTypeUuidPhone").val("");
        chargeDialogObj.find("#fInitialKeywordCountPhone").val("");
        chargeDialogObj.find("#fCurrentKeywordCountPhone").val("");
        chargeDialogObj.find("#fReceivableAmountPhone").val("");
        chargeDialogObj.find("#fPlanChargeDatePhone").val("");
        chargeDialogObj.find("#fNextChargeDatePhone").val("");
        chargeDialogObj.find("#fActualAmountPhone").val("");
        chargeDialogObj.find("#fActualChargeDatePhone").val("");
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
            success: function (data) {
                if(data != null && data.length > 0){
					var str = JSON.parse(data);
                    if(str.pcReceivableAmount == 0 && str.pcReceivableAmount == 0) {
                        document.getElementById("chargeInfoTable").style.height = 0;
                        document.getElementById("chargeDialog").style.height = 145;
                    }
                    var pcValue = 0;
                    var phoneValue = 0;
                    var checkboxPC = document.getElementById("operationTypePC");
                    var checkboxPhone = document.getElementById("operationTypePhone");

                    // 电脑收费详细
                    if(str.pcQzOperationTypeUuid != null) {
                        // 存在电脑操作类型
                        chargeDialogObj.find("#fQzOperationTypeUuidPC").val(str.pcQzOperationTypeUuid);
                        chargeDialogObj.find("#fInitialKeywordCountPC").val(str.pcInitialKeywordCount);
                        chargeDialogObj.find("#fCurrentKeywordCountPC").val(str.pcCurrentKeywordCount);
                        document.getElementById("checkChargePC").style.display = "block";
                        // 达标
                        if (str.pcReceivableAmount != 0) {
                            checkboxPC.checked = true;
                            document.getElementById("PCChargeInfo").style.display = "block";
                            var date = new Date(str.pcPlanChargeDate);
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
                            var nextChargeDatePC = year + "-" + month + "-" + day;
                            pcValue = Number(str.pcReceivableAmount);
                            chargeDialogObj.find("#fReceivableAmountPC").val(str.pcReceivableAmount);
                            chargeDialogObj.find("#fPlanChargeDatePC").val(str.pcPlanChargeDate);
                            chargeDialogObj.find("#fNextChargeDatePC").val(nextChargeDatePC);
                            chargeDialogObj.find("#fActualAmountPC").val(str.pcReceivableAmount);
                        } else {
                            checkboxPC.checked = false;
                            document.getElementById("PCChargeInfo").style.display = "none";
						}
                        chargeDialogObj.find("#fActualChargeDatePC").val(today);
                    } else {
                        document.getElementById("checkChargePC").style.display = "none";
                        document.getElementById("PCChargeInfo").style.display = "none";
					}
					// 手机收费详情
                    if(str.phoneQzOperationTypeUuid != null) {
                        // 存在手机操作类型
                        chargeDialogObj.find("#fQzOperationTypeUuidPhone").val(str.phoneQzOperationTypeUuid);
                        chargeDialogObj.find("#fInitialKeywordCountPhone").val(str.phoneInitialKeywordCount);
                        chargeDialogObj.find("#fCurrentKeywordCountPhone").val(str.phoneCurrentKeywordCount);
                        document.getElementById("checkChargePhone").style.display = "block";
                        // 达标
						if(str.phoneReceivableAmount != 0) {
                            checkboxPhone.checked = true;
                            document.getElementById("PhoneChargeInfo").style.display = "block";
							var date = new Date(str.phonePlanChargeDate);
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
                            var nextChargeDatePhone = year + "-" + month + "-" + day;
                            phoneValue = Number(str.phoneReceivableAmount);
							chargeDialogObj.find("#fReceivableAmountPhone").val(str.phoneReceivableAmount);
							chargeDialogObj.find("#fPlanChargeDatePhone").val(str.phonePlanChargeDate);
							chargeDialogObj.find("#fNextChargeDatePhone").val(nextChargeDatePhone);
							chargeDialogObj.find("#fActualAmountPhone").val(str.phoneReceivableAmount);
						} else {
                            checkboxPhone.checked = false;
                            document.getElementById("PhoneChargeInfo").style.display = "none";
						}
                        chargeDialogObj.find("#fActualChargeDatePhone").val(today);
                    } else {
                        document.getElementById("checkChargePhone").style.display = "none";
                        document.getElementById("PhoneChargeInfo").style.display = "none";
					}

                    var s = new String(pcValue + phoneValue);
                    var total = s.replace(/\B(?=(?:\d{3})+$)/g, ',');
                    document.getElementById("totalAmount").innerHTML = "￥" + total + "元";
                    document.getElementById("chargeDialog").style.display = "block";
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
        var rulePC = new Array();
        var rulePhone = new Array();
        var PCType = false;
        var PhoneType = false;
        clearInfo("Both");
        var settingDialogDiv = $$$("#changeSettingDialog");
        settingDialogDiv.find("#qzSettingUuid").val(qzSetting.uuid);
        settingDialogDiv.find("#qzSettingCustomer").val(qzSetting.contactPerson + "_____" + qzSetting.customerUuid);
        settingDialogDiv.find("#qzSettingDomain").val(qzSetting.domain != null ? qzSetting.domain : "");
        settingDialogDiv.find("#qzSettingIgnoreNoIndex").val(qzSetting.ignoreNoIndex ? "1" : "0");
        settingDialogDiv.find("#qzSettingIgnoreNoOrder").val(qzSetting.ignoreNoOrder ? "1" : "0");
        settingDialogDiv.find("#qzSettingInterval").val(qzSetting.updateInterval != null ? qzSetting.updateInterval : "");
        settingDialogDiv.find("#qzSettingEntryType").val(qzSetting.type != null ? qzSetting.type : "");
        settingDialogDiv[0].style.left = getTop(self); //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容

        // 操作类型表填充数据
        $$$.each(qzSetting.qzOperationTypes, function(idx, val) {
            settingDialogDiv.find("#fGroup" + val.operationtype).val(val.group);
            settingDialogDiv.find("#fInitialKeywordCount" + val.operationtype).val(val.initialKeywordCount );
            settingDialogDiv.find("#fCurrentKeywordCount" + val.operationtype).val(val.currentKeywordCount);
            settingDialogDiv.find("#qzSettingUuid" + val.operationtype).val(val.uuid);

            // 构造规则表
            $$$.each(val.qzChargeRules, function(chargeRuleIdx, chargeRuleVal) {
                if(val.operationtype == 'PC') {
                    rulePC.push([
                        chargeRuleIdx,
                        chargeRuleVal.uuid,
                        chargeRuleVal.startKeywordCount,
                        chargeRuleVal.endKeywordCount,
                        chargeRuleVal.amount]
                    );
                }
                if(val.operationtype == 'Phone') {
                    rulePhone.push([
                        chargeRuleIdx,
                        chargeRuleVal.uuid,
                        chargeRuleVal.startKeywordCount,
                        chargeRuleVal.endKeywordCount,
                        chargeRuleVal.amount]
                    );
                }
                addRow("order" + val.operationtype);
            });
        });

        // 规则填充数据
        var rules = rulePC.concat(rulePhone);
        for (var i = 0; i < rulePC.length + rulePhone.length; i++) {
            var rule = rules[i];
            var j = 1;
            document.getElementsByName("qzChargeRuleUuid")[i].value = rule[j++];
            document.getElementsByName("startKeywordCount")[i].value = rule[j++];
            document.getElementsByName("endKeywordCount")[i].value = rule[j++];
            document.getElementsByName("amount")[i].value = rule[j++];
        }

		if(rulePC.length > 0) {
			divHeight = divHeight + $("#groupHeightPC").height() + $("#ruleHeightPC").height() - inputHeight;
			dealSettingTable("PC");
		} else {
			divHeight = divHeight + $("#groupHeightPC").height() + $("#ruleHeightPC").height();
		}

		if(rulePhone.length > 0) {
			divHeight = divHeight + $("#groupHeightPhone").height() + $("#ruleHeightPhone").height() - inputHeight;
			dealSettingTable("Phone");
		} else {
			divHeight = divHeight + $("#groupHeightPhone").height() + $("#ruleHeightPhone").height();
		}

        settingDialogDiv.show();
    }

    //规则表验证
    var reg = /^[1-9]\d*$/;
    var startKeywordCount = document.getElementsByName("startKeywordCount");
    var endKeywordCount = document.getElementsByName("endKeywordCount");
    var amount = document.getElementsByName("amount");

    function saveChangeSetting(self) {
        var settingDialogDiv = $$$("#changeSettingDialog");
        var qzSetting = {};
        qzSetting.uuid = settingDialogDiv.find("#qzSettingUuid").val();
        qzSetting.domain = settingDialogDiv.find("#qzSettingDomain").val();
        if(qzSetting.domain == null || qzSetting.domain === ""){
            alert("请输入域名");
            settingDialogDiv.find("#qzSettingDomain").focus();
            return;
        }
        qzSetting.ignoreNoIndex = settingDialogDiv.find("#qzSettingIgnoreNoIndex").val() === "1" ? true : false;
        qzSetting.ignoreNoOrder = settingDialogDiv.find("#qzSettingIgnoreNoOrder").val() === "1" ? true : false;
        qzSetting.updateInterval = settingDialogDiv.find("#qzSettingInterval").val();
		qzSetting.pcGroup = settingDialogDiv.find("#fGroupPC").val();
        qzSetting.phoneGroup = settingDialogDiv.find("#fGroupPhone").val();
        var customer = settingDialogDiv.find("#qzSettingCustomer").val();
        if(customer == null || customer === ""){
            alert("请选择客户");
            settingDialogDiv.find("#qzSettingCustomer").focus();
            return;
        }

        if(customer != null && customer != '' ){
            var customerArray = customer.split("_____");
            if(customerArray.length == 2){
                qzSetting.customerUuid = customerArray[1];
            }else{
                alert("请从列表中选择客户");
                settingDialogDiv.find("#qzSettingCustomer").focus();
                return;
            }
        }
        var entryType = settingDialogDiv.find("#qzSettingEntryType").val();
        qzSetting.type = entryType;

        qzSetting.qzOperationTypes = [];//操作类型表
        qzSetting.qzOperationTypes.qzChargeRules= [];//收费规则

        var pcRuleObj = document.getElementById("orderPC");
        //pc操作类型
        if(document.getElementById("fOperationTypePC").checked) {
            var pcOperationType = {};
            pcOperationType.qzChargeRules= [];
            pcOperationType.operationtype ='PC';
            pcOperationType.group = settingDialogDiv.find("#fGroupPC").val();
            pcOperationType.initialKeywordCount = settingDialogDiv.find("#fInitialKeywordCountPC").val();
            pcOperationType.currentKeywordCount = settingDialogDiv.find("#fCurrentKeywordCountPC").val();

            if(pcOperationType.group == null || pcOperationType.group === ""){
                alert("请输入分组");
                settingDialogDiv.find("#fGroupPC").focus();
                return;
            }
            if(pcOperationType.initialKeywordCount == null || pcOperationType.initialKeywordCount === ""){
                alert("请输入初始词量");
                settingDialogDiv.find("#fInitialKeywordCountPC").focus();
                return;
            }

            //多条pc规则
            var endKeyWordCountValue = -1;
            for (var i = 0; i < pcRuleObj.rows.length - 2; i++) {
                var pcChargeRule = {};
                pcChargeRule.startKeywordCount =  startKeywordCount[i].value;
                pcChargeRule.endKeywordCount =  endKeywordCount[i].value;
                pcChargeRule.amount = amount[i].value;
                pcOperationType.qzChargeRules.push(pcChargeRule);

                if(startKeywordCount[i].value == null || startKeywordCount[i].value == "") {
                    alert("请输入起始词数");
                    document.getElementsByName("startKeywordCount")[i].focus();
                    return;
                }
                if(!reg.test(startKeywordCount[i].value)) {
                    alert("请输入数字");
                    document.getElementsByName("startKeywordCount")[i].focus();
                    return;
                }

                var skc = Number(startKeywordCount[i].value);
                if(skc <= endKeyWordCountValue) {
                    alert("起始词数过小");
                    document.getElementsByName("startKeywordCount")[i].focus();
                    return;
                }

                if(i != pcRuleObj.rows.length - 3 || endKeywordCount[i].value != "") {
                    if(endKeywordCount[i].value == null || endKeywordCount[i].value == "") {
                        alert("请输入终止词数");
                        document.getElementsByName("endKeywordCount")[i].focus();
                        return;
                    }
                    if(!reg.test(endKeywordCount[i].value)) {
                        alert("请输入数字");
                        document.getElementsByName("endKeywordCount")[i].focus();
                        return;
                    }
                    if(Number(endKeywordCount[i].value) <= skc) {
                        alert("终止词数必须大于起始词数");
                        document.getElementsByName("endKeywordCount")[i].focus();
                        return;
                    }
				}

                if(amount[i].value == null || amount[i].value == "") {
                    alert("请输入价格");
                    document.getElementsByName("amount")[i].focus();
                    return;
                }
                if(!reg.test(amount[i].value)) {
                    alert("输入的价格不合理");
                    document.getElementsByName("amount")[i].focus();
                    return;
                }
                endKeyWordCountValue = Number(endKeywordCount[i].value);
            }
            qzSetting.qzOperationTypes.push(pcOperationType);
        }

        //Phone操作类型
        if(document.getElementById("fOperationTypePhone").checked){
            var PhoneOperationType = {};
            PhoneOperationType.qzChargeRules= [];
            PhoneOperationType.operationtype ='Phone';
            PhoneOperationType.initialKeywordCount = settingDialogDiv.find("#fInitialKeywordCountPhone").val();
            PhoneOperationType.currentKeywordCount = settingDialogDiv.find("#fCurrentKeywordCountPhone").val();
            PhoneOperationType.group = settingDialogDiv.find("#fGroupPhone").val();

            if(PhoneOperationType.group == null || PhoneOperationType.group === ""){
                alert("请输入分组");
                settingDialogDiv.find("#fGroupPhone").focus();
                return;
            }
            if(PhoneOperationType.initialKeywordCount == null || PhoneOperationType.initialKeywordCount === ""){
                alert("请输入初始词量");
                settingDialogDiv.find("#fInitialKeywordCountPhone").focus();
                return;
            }

            //多条Phone规则信息
            var endKeyWordCountValue = -1;
            for (var i = pcRuleObj.rows.length - 2; i < amount.length; i++){
                var PhoneChargeRule =  {};
                PhoneChargeRule.startKeywordCount = startKeywordCount[i].value;
                PhoneChargeRule.endKeywordCount =  endKeywordCount[i].value;
                PhoneChargeRule.amount = amount[i].value;
                PhoneOperationType.qzChargeRules.push(PhoneChargeRule);

                if(startKeywordCount[i].value == null || startKeywordCount[i].value == "") {
                    alert("请输入起始词数");
                    document.getElementsByName("startKeywordCount")[i].focus();
                    return;
                }
                if(!reg.test(startKeywordCount[i].value)) {
                    alert("请输入数字");
                    document.getElementsByName("startKeywordCount")[i].focus();
                    return;
                }

                var skc = Number(startKeywordCount[i].value);
                if(skc <= endKeyWordCountValue) {
                    alert("起始词数过小");
                    document.getElementsByName("startKeywordCount")[i].focus();
                    return;
                }

                if(i != amount.length - 1 || endKeywordCount[i].value != "") {
                    if(endKeywordCount[i].value == null || endKeywordCount[i].value == "") {
                        alert("请输入终止词数");
                        document.getElementsByName("endKeywordCount")[i].focus();
                        return;
                    }
                    if(!reg.test(endKeywordCount[i].value)) {
                        alert("请输入数字");
                        document.getElementsByName("endKeywordCount")[i].focus();
                        return;
                    }
                    if(Number(endKeywordCount[i].value) <= skc) {
                        alert("终止词数必须大于起始词数");
                        document.getElementsByName("endKeywordCount")[i].focus();
                        return;
                    }
                }

                if(amount[i].value == null || amount[i].value == "") {
                    alert("请输入价格");
                    document.getElementsByName("amount")[i].focus();
                    return;
                }
                if(!reg.test(amount[i].value)) {
                    alert("输入的价格不合理");
                    document.getElementsByName("amount")[i].focus();
                    return;
                }
                endKeyWordCountValue = Number(endKeywordCount[i].value);
            }
            qzSetting.qzOperationTypes.push(PhoneOperationType);
        }

        if(!document.getElementById("fOperationTypePhone").checked && !document.getElementById("fOperationTypePhone").checked) {
            var len = document.getElementsByName("amount").length;
            if(len <= 0) {
                alert("保存失败，必须要增加一条规则");
                return;
            }
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
                if(data != null && data != ""){
                    showInfo("更新成功！", self);
                    window.location.reload();
                }else{
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

    function formatDate(value) {
        var date = new Date();
        date.setTime(value.time);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? '0' + m : m;
        var d = date.getDate();
        d = d < 10 ? ("0" + d) : d;
        var h = date.getHours();
        h = h < 10 ? ("0" + h) : h;
        var M = date.getMinutes();
        M = M < 10 ? ("0" + M) : M;
        var S = date.getSeconds();
        S = S < 10 ? ("0" + S) : S;
        if (M == '00' && S == '00') {
            var str = y + "-" + m + "-" + d;
            return str;
        }
        var str = y + "-" + m + "-" + d + " " + h + ":" + M + ":" + S;
        return str;
    }
    
    function resetChargeList() {
        $("#chargeLogListDiv").hide();
    }

    function insertChargeLog(uuid, self){
        $("#chargeLogListDiv").hide();
        cancelChargeDialog();
        cancelChangeSetting();
        $("#chargeLogListTable  tr:not(:first,:last)").remove();
        $$$.ajax({
            url: '/spring/qzchargelog/chargesList/' + uuid,
            type: 'Get',
            success: function (data) {
                if(data != null && data.length > 0) {
                    var qzChargeLogs = eval("("+data+")");
                    var chargeLogListTable = document.getElementById("chargeLogListTable");
                    for (var i = 0; i < qzChargeLogs.length; i++) {
                        var chargeLog = [
                            formatDate(qzChargeLogs[i].actualChargeDate),
                            qzChargeLogs[i].operationtype,
							qzChargeLogs[i].actualAmount,
							qzChargeLogs[i].userName,
							formatDate(qzChargeLogs[i].createTime)
						];
                        // 创建tr、td
                        var newTr = document.createElement("tr");
                        for(var n = 0; n < 5; n++) {
                            var newTd = document.createElement("td");
                            newTr.appendChild(newTd);
                            newTd.innerHTML = chargeLog[n];
                        }
                        // 新增一行
						var row = document.getElementById("lastTr");
                        row.parentNode.insertBefore(newTr,row);
                    }
					$("#chargeLogListDiv").show();
                }else{
                    alert("暂无收费记录");
                }
            },
            error: function () {
                showInfo("获取信息失败！", self);
            }
        });

    }

    function saveChargeLog(self) {
        var checkboxPC = document.getElementById("operationTypePC");
        var checkboxPhone = document.getElementById("operationTypePhone");
        var chargeData = {};
        if (window.confirm("确认收费?")) {
            if(checkboxPC.checked == true) {
                chargeData.fQzOperationTypeUuidPC = $$$("#fQzOperationTypeUuidPC").val();
                chargeData.fReceivableAmountPC = $$$("#fReceivableAmountPC").val();
                chargeData.fPlanChargeDatePC = $$$("#fPlanChargeDatePC").val();
                chargeData.fNextChargeDatePC = $$$("#fNextChargeDatePC").val();
                chargeData.fActualAmountPC = $$$("#fActualAmountPC").val();
                chargeData.fActualChargeDatePC = $$$("#fActualChargeDatePC").val();
                if(chargeData.fNextChargeDatePC == "" || chargeData.fNextChargeDatePC == null) {
                    alert("下次收费日期为必填");
                    $$$("#fNextChargeDatePC").focus();
                    return;
                }
                if(chargeData.fActualAmountPC == "" || chargeData.fActualAmountPC == null) {
                    alert("实收金额为必填");
                    $$$("#fActualAmountPC").focus();
                    return;
                }
                if(!reg.test(chargeData.fActualAmountPC)) {
                    alert("请输入合理的金额");
                    $$$("#fActualAmountPC").focus();
                    return;
                }
                if(chargeData.fActualChargeDatePC == "" || chargeData.fActualChargeDatePC == null) {
                    alert("实际收费日期为必填");
                    $$$("#fActualChargeDatePC").focus();
                    return;
                }
            }
            if(checkboxPhone.checked == true) {
                chargeData.fQzOperationTypeUuidPhone = $$$("#fQzOperationTypeUuidPhone").val();
                chargeData.fReceivableAmountPhone = $$$("#fReceivableAmountPhone").val();
                chargeData.fPlanChargeDatePhone = $$$("#fPlanChargeDatePhone").val();
                chargeData.fNextChargeDatePhone = $$$("#fNextChargeDatePhone").val();
                chargeData.fActualAmountPhone = $$$("#fActualAmountPhone").val();
                chargeData.fActualChargeDatePhone = $$$("#fActualChargeDatePhone").val();
                if(chargeData.fNextChargeDatePhone == "" || chargeData.fNextChargeDatePhone == null) {
                    alert("下次收费日期为必填");
                    $$$("#fNextChargeDatePhone").focus();
                    return;
                }
                if(chargeData.fActualAmountPhone == "" || chargeData.fActualAmountPhone == null) {
                    alert("实收金额为必填");
                    $$$("#fActualAmountPhone").focus();
                    return;
                }
                if(!reg.test(chargeData.fActualAmountPhone)) {
                    alert("请输入合理的金额");
                    $$$("#fActualAmountPhone").focus();
                    return;
				}
                if(chargeData.fActualChargeDatePhone == "" || chargeData.fActualChargeDatePhone == null) {
                    alert("实际收费日期为必填");
                    $$$("#fActualChargeDatePhone").focus();
                    return;
                }
            }
        } else {
            alert("取消收费");
        }

        if(checkboxPC.checked == false && checkboxPhone.checked == false) {
            alert("必须选择一个收费项才能收费");
            return;
        }

        $$$.ajax({
            url: '/spring/qzchargelog/save',
            data: JSON.stringify(chargeData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (data) {
                cancelChargeDialog();
                if(data != null && data != ""){
                    showInfo("收费成功！", self);
                    window.location.reload();
                }else{
                    showInfo("收费失败！", self);
                    window.location.reload();
                }
            },
            error: function () {
                showInfo("收费失败！", self);
            }
        });
    }

    function calTotalAmount() {
        var pcValue = 0;
        var phoneValue = 0;
        if(document.getElementById("operationTypePC").checked == true) {
            pcValue = Number(document.getElementById("fActualAmountPC").value);
        }
        if(document.getElementById("operationTypePhone").checked == true) {
            phoneValue = Number(document.getElementById("fActualAmountPhone").value);
        }
        var str = new String(pcValue + phoneValue);
        var total = str.replace( /\B(?=(?:\d{3})+$)/g, ',' );
        document.getElementById("totalAmount").innerHTML = "￥" + total + "元";
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

    function addRow(tableId){
        var addTable = document.getElementById(tableId);
        var row_index = addTable.rows.length - 1;       //新插入行在表格中的位置
        var newRow = addTable.insertRow(row_index);     //插入新行
        newRow.id = "row" + row_index;                  //设置新插入行的ID

        if(tableId == "orderPC") {
            var col1 = newRow.insertCell(0);
            col1.innerHTML="<input type='text' name='PCId' value='"+(id1++)+"' style='width:100%'/><input type='hidden' name='qzChargeRuleUuid' />";
        } else if(tableId == "orderPhone") {
            var col1 = newRow.insertCell(0);
            col1.innerHTML="<input type='text' name='PhoneId' value='"+(id2++)+"' style='width:100%'/><input type='hidden' name='qzChargeRuleUuid' />";
        }

        var col2 = newRow.insertCell(1);
        col2.innerHTML = "<input type='text' name='startKeywordCount' style='width:100%'/>";

        var col3 = newRow.insertCell(2);
        col3.innerHTML = "<input type='text' name='endKeywordCount' style='width:100%'/>";

        var col4 = newRow.insertCell(3);
        col4.innerHTML = "<input type='text' name='amount' style='width:100%'/>";

        var col5 = newRow.insertCell(4);
        col5.innerHTML = "<input style='width:100%' name='del" + row_index + "' type='button' value='删除' onclick='deleteCurrentRow(this.parentNode.parentNode)' />";

        divHeight = divHeight + inputHeight;
        document.getElementById("changeSettingDialog").style.height = divHeight;
    }

    function deleteCurrentRow(currentRow) {
        var index = currentRow.rowIndex;
        var tableObj = currentRow.parentNode.parentNode;
        if(tableObj.rows.length > 3) {
            tableObj.deleteRow(index);
            divHeight = divHeight - inputHeight;
            document.getElementById("changeSettingDialog").style.height = divHeight;
        } else {
            alert("删除失败，规则表不允许为空");
        }
    }

    function clearInfo(type) {
        if(type == "Both") {
            clearInfo("PC");
            clearInfo("Phone");
            divHeight = 250;
            document.getElementById("changeSettingDialog").style.height = divHeight;
        } else {
            var settingDialogObj = $$$("#changeSettingDialog");
            // 清空分组表格信息
            settingDialogObj.find("#fGroup" + type).val("");
            settingDialogObj.find("#fInitialKeywordCount" + type).val("");
            settingDialogObj.find("#fCurrentKeywordCount" + type).val("");
            settingDialogObj.find("#qzOperationTypeUuid" + type).val("");
            // 清空规则表格信息
            $(document).find("input[name='"+type+"Id']").parent().parent().remove();
            document.getElementById("fOperationType" + type).checked = false;
            document.getElementById("group" + type).style.display = "none";
            document.getElementById("order" + type).style.display = "none";
            // 重置div高度以及规则表序号
            divHeight = divHeight - $("#groupHeight" + type).height() - $("#ruleHeight" + type).height();
            if(type == "PC") {
                id1 = 1;
            } else if(type == 'Phone') {
                id2 = 1;
            }
        }
    }

    function dealChargeTable(operationType) {
		var checkboxObj = document.getElementById("operationType" + operationType);
		var chargeInfoObj = document.getElementById(operationType + "ChargeInfo");
		var chargeDialogHeight = $("#chargeDialog").height();
        if(chargeInfoObj.style.display == "none" || checkboxObj.checked == true) {
            document.getElementById("chargeDialog").style.height = 320;
            chargeInfoObj.style.display = "block";
        } else if(chargeInfoObj.style.display == "block" || checkboxObj.checked == false) {
            chargeInfoObj.style.display = "none";
        }
		calTotalAmount();
        if(document.getElementById("operationTypePC").checked == false && document.getElementById("operationTypePhone").checked == false) {
            document.getElementById("chargeInfoTable").style.height = 0;
            document.getElementById("chargeDialog").style.height = chargeDialogHeight - 178;
        } else {
            document.getElementById("chargeInfoTable").style.height = 185;
		}
    }

    function dealSettingTable(operationType) {
        var groupObj = document.getElementById('group' + operationType);
        var ruleObj = document.getElementById('order' + operationType);
        var checkboxObj = document.getElementById('fOperationType' + operationType);

        if(ruleObj.style.display == "none" || checkboxObj.checked == true){
            // 保证必须有一条规则
            if(document.getElementsByName(operationType + "Id").length <= 0) {
                addRow("order" + operationType);
                divHeight = divHeight - inputHeight;
            }
            divHeight = divHeight + 130;
            document.getElementById("changeSettingDialog").style.height = divHeight;
            groupObj.style.display = "block";
            ruleObj.style.display = "block";
            checkboxObj.checked = true;
        } else if(ruleObj.style.display == "block" || checkboxObj.checked == false) {
            trHeight = $("#groupHeight" + operationType).height() + $("#ruleHeight" + operationType).height();
            divHeight = divHeight - trHeight + space;
            document.getElementById("changeSettingDialog").style.height = divHeight;
            clearInfo(operationType);
            groupObj.style.display = "none";
            ruleObj.style.display = "none";
            checkboxObj.checked = false;
        }
    }
</script>

<div style="display:none;">
</div>
<div id="changeSettingDialog">
	<table style="font-size:12px" id="settingTable">
		<tr>
			<th>客户</th>
			<td>
				<input type="hidden" id="qzSettingUuid" />
				<input type="text" list="customer_list" name="qzSettingCustomer" id="qzSettingCustomer" style="width:240px" />
			</td>
		</tr>
		<tr>
			<th>域名</th>
			<td>
				<input type="text" name="qzSettingDomain" id="qzSettingDomain" style="width:240px" />
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="checkbox" name="fOperationType" id="fOperationTypePC" onclick="dealSettingTable('PC')" style=""/>电脑
			</td>
		</tr>
		<%--电脑分组信息--%>
		<tr>
			<td colspan="2" id="groupHeightPC">
				<table border="0" style="display:none;font-size:12px;" cellspacing="0" cellpadding="0" id="groupPC">
					<tr>
						<th style="width:72px">分组</th>
						<td><input type="text" name="fGroup" id="fGroupPC"  style="width:234px"/></td>
					</tr>
					<tr>
						<th style="width:72px">初始词量</th>
						<td colspan="4"><input type="text" name="fInitialKeywordCount" id="fInitialKeywordCountPC"  style="width:234px"/></td>
					</tr>
					<tr>
						<th style="width:72px">当前词量</th>
						<td colspan="4"><input type="text" name="fCurrentKeywordCount" id="fCurrentKeywordCountPC"  style="width:234px" readonly/></td>
					</tr>
					<input type="hidden" id="qzSettingUuidPC" name="qzOperationTypeUuid" value="" />
				</table>
			</td>
		</tr>
		<%--电脑规则信息--%>
		<tr id="pcTable">
			<td colspan="2" id="ruleHeightPC">
				<table border="1" style="display:none;font-size:12px;" cellspacing="0" cellpadding="0" id="orderPC">
					<tr>
						<th style="width:50px">序号</th>
						<th style="width:72px">起始词数</th>
						<th style="width:72px">终止词数</th>
						<th style="width:50px">价格</th>
						<th style="width:46px">操作</th>
					</tr>
					<tr>
						<td colspan="5">
							<input name="addRule" type="button" value="增加规则" onclick="addRow('orderPC')" /></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="checkbox" name="fOperationType" id="fOperationTypePhone" onclick="dealSettingTable('Phone')" style=""/>手机
			</td>
		</tr>
		<%--手机分组信息--%>
		<tr>
			<td colspan="2" id="groupHeightPhone">
				<table border="0" style="display:none;font-size:12px;" cellspacing="0" cellpadding="0" id="groupPhone">
					<tr>
						<th style="width:72px">分组</th>
						<td><input type="text" name="fGroup" id="fGroupPhone"  style="width:234px"/></td>
					</tr>
					<tr>
						<th style="width:72px">初始词量</th>
						<td colspan="4"><input type="text" name="fInitialKeywordCount" id="fInitialKeywordCountPhone"  style="width:234px"/></td>
					</tr>
					<tr>
						<th style="width:72px">当前词量</th>
						<td colspan="4"><input type="text" name="fCurrentKeywordCount" id="fCurrentKeywordCountPhone"  style="width:234px" readonly/></td>
					</tr>
					<input type="hidden" id="qzSettingUuidPhone" name="qzOperationTypeUuid" value="" />
				</table>
			</td>
		</tr>
		<%--手机规则信息--%>
		<tr id="phoneTable">
			<td colspan="2" id="ruleHeightPhone">
				<table border="1" style="display:none;font-size:12px;" cellspacing="0" cellpadding="0" id="orderPhone">
					<tr>
						<th style="width:50px">序号</th>
						<th style="width:72px">起始词数</th>
						<th style="width:72px">终止词数</th>
						<th style="width:50px">价格</th>
						<th style="width:46px">操作</th>
					</tr>
					<tr>
						<td colspan="5">
							<input name="addRule" type="button" value="增加规则" onclick="addRow('orderPhone')" /></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<th>入口</th>
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
			<th>去掉没指数</th>
			<td>
				<select name="qzSettingIgnoreNoIndex" id="qzSettingIgnoreNoIndex"  style="width:240px">
					<option value="1" selected>是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>去掉没排名</th>
			<td>
				<select name="qzSettingIgnoreNoOrder" id="qzSettingIgnoreNoOrder"  style="width:240px">
					<option value="1" selected>是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>更新间隔</th>
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
						<input type="checkbox" name="fOperationType" id="operationTypePC" style="margin-left: -2px" onclick="dealChargeTable('PC')" />电脑
					</td></tr>
				</table>
			</td>

			<td>
				<table id="checkChargePhone" style="display: none;font-size:12px;">
					<tr><td>
						<input type="checkbox" name="fOperationType" id="operationTypePhone" onclick="dealChargeTable('Phone')" style="margin-left: 140px"/>手机
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
					<input type="hidden" name="fQzOperationTypeUuid" id="fQzOperationTypeUuidPC"/>
					<td><input type="text" name="fInitialKeywordCount" id="fInitialKeywordCountPC"  style="width:100px" readonly/></td>
				</tr>
				<tr>
					<td align="right">当前词量</td>
					<td><input type="text" name="fCurrentKeywordCount" id="fCurrentKeywordCountPC"  style="width:100px" readonly/></td>
				</tr>
				<tr>
					<td align="right">应收金额</td>
					<td>
						<input type="text" name="fReceivableAmount" id="fReceivableAmountPC" style="width:100px" readonly />
					</td>
				</tr>
				<tr>
					<td align="right">计划收费日期</td>
					<td>
						<input type="text" name="fPlanChargeDate" id="fPlanChargeDatePC" style="width:100px" readonly />
					</td>
				</tr>
				<tr>
					<td align="right">下次收费日期</td>
					<td>
						<input type="text" name="fNextChargeDate" id="fNextChargeDatePC" class="Wdate" onClick="WdatePicker()" style="width:100px" />
					</td>
				</tr>
				<tr>
					<td align="right">实收金额</td>
					<td>
						<input type="text" name="fActualAmount" id="fActualAmountPC" onkeyup="calTotalAmount()" style="width:100px" />
					</td>
				</tr>
				<tr>
					<td align="right">实际收费日期</td>
					<td>
						<input name="fActualChargeDate" id="fActualChargeDatePC" class="Wdate" type="text" style="width:100px" onClick="WdatePicker()" value="">
					</td>
				</tr>
			</table>
		</td>
		<%--手机收费信息--%>
		<td style="width: 182px">
			<table style="display:none;font-size:12px;margin-top: 2px;margin-bottom: 2px;" id="PhoneChargeInfo">
				<tr>
					<td align="right">初始词量</td>
					<input type="hidden" name="fQzOperationTypeUuid" id="fQzOperationTypeUuidPhone"/>
					<td><input type="text" name="fInitialKeywordCount" id="fInitialKeywordCountPhone"  style="width:100px" readonly/></td>
				</tr>
				<tr>
					<td align="right">当前词量</td>
					<td><input type="text" name="fCurrentKeywordCount" id="fCurrentKeywordCountPhone"  style="width:100px" readonly/></td>
				</tr>
				<tr>
					<td align="right">应收金额</td>
					<td>
						<input type="text" name="fReceivableAmount" id="fReceivableAmountPhone" style="width:100px" readonly />
					</td>
				</tr>
				<tr>
					<td align="right">计划收费日期</td>
					<td>
						<input type="text" name="fPlanChargeDate" id="fPlanChargeDatePhone" style="width:100px" readonly />
					</td>
				</tr>
				<tr>
					<td align="right">下次收费日期</td>
					<td>
						<input type="text" name="fNextChargeDate" id="fNextChargeDatePhone" class="Wdate" onClick="WdatePicker()" style="width:100px" />
					</td>
				</tr>
				<tr>
					<td align="right">实收金额</td>
					<td>
						<input type="text" name="fActualAmount" id="fActualAmountPhone" onkeyup="calTotalAmount()" style="width:100px" />
					</td>
				</tr>
				<tr>
					<td align="right">实际收费日期</td>
					<td>
						<input name="fActualChargeDate" id="fActualChargeDatePhone" class="Wdate" type="text" style="width:100px"  onClick="WdatePicker()" value="">
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
		<input type="button" id="saveChargeLog" onclick="saveChargeLog(this)" value="确认收费"/>&nbsp;&nbsp;&nbsp;<input type="button" id="cancelChargeLog" onclick="cancelChargeDialog()" value="取消"/><span style="margin-right:24px;"></span>
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