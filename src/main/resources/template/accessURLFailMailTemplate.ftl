<p><b>您好，</b></p>
&nbsp;&nbsp;以下网站出现<font color="red">访问故障</font>，请及时进行查看：
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
            width: 180px
        }

        .content {
            background-color: #FBE1E1;
            text-align: center;
        }
    </style>
</head>
<body>
<table>
    <tr>
        <th>网站名称</th>
        <th>网站域名</th>
        <th>访问失败次数</th>
        <th>注册商</th>
        <th>解析商</th>
        <th>服务器IP</th>
        <th>数据库名称</th>
        <th style="color: red;">出现故障时间</th>
    </tr>
    <#list accessFailWebsites as website>
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
        <#if website.accessFailCount??>
            <td class='content'>${website.accessFailCount!""}</td>
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
        <#if website.accessFailTime??>
            <td class='content'>${website.accessFailTime?datetime}</td>
        <#else>
            <td>---</td>
        </#if>
    </tr>
    </#list>
</table>
</body>
</html>
<p><b>顺时科技邮件</b></p>
		 