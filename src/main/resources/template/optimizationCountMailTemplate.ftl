<p><b>您好，</b></p>
&nbsp;&nbsp;以下关键字出现刷量异常，请注意查看：
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
<table>
    <tr>
        <th style="width: 200px;">关键字</th>
        <th style="width: 50px;">已刷数量</th>
        <th style="width: 50px;">无效点击</th>
    </tr>
    <#list optimizationCountVOs as optimizationCountVO>
    <tr>
        <td class='content' style="width: 200px;">${optimizationCountVO.keyword}</td>
        <td class='content' style="width: 50px;">${optimizationCountVO.optimizedCount}</td>
        <td class='content' style="width: 50px;">${optimizationCountVO.invalidRefreshCount}</td>
    </tr>
    </#list>
</table>
</body>
</html>
<p><b>顺时科技</b></p>
		 