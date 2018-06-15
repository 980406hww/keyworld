<p><b>您好，</b></p>
&nbsp;&nbsp;以下网站的域名<font color="red">即将到期</font>，<font color="red">请注意续费：</font>
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
            width: 180px;
            text-align: center;
        }

        .content {
            background-color: #FBE1E1;
        }
    </style>
</head>
<body>
<table>
    <tr>
        <th>网站名称</th>
        <th>网站域名</th>
        <th>注册商</th>
        <th>解析商</th>
        <th>服务器IP</th>
        <th>数据库名称</th>
        <th style="color: #ffb319;">到期时间</th>
    </tr>
    <#list websites as website>
    <tr>
        <#if website.websiteName??>
            <td class='content'>${website.websiteName}</td>
        <#else>
            <td>---</td>
        </#if>
        <#if website.domain??>
            <td class='content'>${website.domain}</td>
        <#else>
            <td>---</td>
        </#if>
        <#if website.registrar??>
            <td class='content'>${website.registrar}</td>
        <#else>
            <td>---</td>
        </#if>
        <#if website.analysis??>
            <td class='content'>${website.analysis}</td>
        <#else>
            <td>---</td>
        </#if>
        <#if website.serverIP??>
            <td class='content'>${website.serverIP}</td>
        <#else>
            <td>---</td>
        </#if>
        <#if website.databaseName??>
            <td class='content'>${website.databaseName}</td>
        <#else>
            <td>---</td>
        </#if>
        <#if website.expiryTime??>
            <td class='content'>${website.expiryTime?datetime}</td>
        <#else>
            <td>---</td>
        </#if>
    </tr>
    </#list>
</table>
</body>
</html>
<p><b>顺时科技邮件</b></p>
		 