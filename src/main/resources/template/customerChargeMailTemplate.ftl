<p><b>您好，</b></p>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>
    <style type='text/css'>
        table {
            border-collapse: collapse;
            border: solid #8BC34A;
            border-width: 1px 0 0 1px;
            font-size: 12px;
        }

        table th, table td {
            border: solid #8BC34A;
            border-width: 0 1px 1px 0;
            padding: 2px;
        }

        .content {
            background-color: #FBE1E1;
        }
    </style>
</head>
<body>
<#if customerChargeRules ?? && (customerChargeRules?size > 0)>
以下项目需要开始收费，请注意查看：
<table>
    <tr>
        <th style="width: 200px;">项目</th>
        <th style="width: 50px;">一月费用</th>
        <th style="width: 50px;">二月费用</th>
        <th style="width: 50px;">三月费用</th>
        <th style="width: 50px;">四月费用</th>
        <th style="width: 50px;">五月费用</th>
        <th style="width: 50px;">六月费用</th>
        <th style="width: 50px;">七月费用</th>
        <th style="width: 50px;">八月费用</th>
        <th style="width: 50px;">九月费用</th>
        <th style="width: 50px;">十月费用</th>
        <th style="width: 60px;">十一月费用</th>
        <th style="width: 60px;">十二月费用</th>
        <th style="width: 80px;">下次收费日期</th>
    </tr>
    <#list customerChargeRules as customerChargeRule>
    <tr>
        <td class='content' style="width: 200px;">${customerChargeRule.contactPerson}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.januaryFee}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.februaryFee}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.marchFee}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.aprilFee}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.mayFee}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.juneFee}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.julyFee}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.augustFee}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.septemberFee}</td>
        <td class='content' style="width: 50px;">${customerChargeRule.octoberFee}</td>
        <td class='content' style="width: 60px;">${customerChargeRule.novemberFee}</td>
        <td class='content' style="width: 60px;">${customerChargeRule.decemberFee}</td>
        <td class='content' style="width: 80px;">${customerChargeRule.nextChargeDate?date}</td>
    </tr>
    </#list>
</table>
</#if>
<br>
</body>
</html>
<p><b>顺时科技</b></p>
		 