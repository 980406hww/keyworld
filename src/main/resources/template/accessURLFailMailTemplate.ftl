<p><b>您好，</b></p>
&nbsp;&nbsp;以下网站出现访问故障，请及时进行查看：
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
        }
    </style>
</head>
<body>
<table>
    <tr>
        <th>网站名称</th>
        <th>访问失败次数</th>
        <th>出现故障时间</th>
    </tr>
    <#list accessFailWebsites as website>
    <tr>
        <td class='content'>${website.domain}</td>
        <td class='content'>${website.accessFailCount}</td>
        <td class='content'>${website.accessFailTime?datetime}</td>
    </tr>
    </#list>
</table>
</body>
</html>
<p><b>顺时科技</b></p>
		 