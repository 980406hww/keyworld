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
<#if groupOptimizationCountInfo ?? && (groupOptimizationCountInfo?size > 0)>
以下全站分组出现刷量异常，请注意查看：
<table>
    <tr>
        <th style="width: 200px;">优化组名</th>
        <th style="width: 50px;">异常词数</th>
    </tr>
    <#list groupOptimizationCountInfo as info>
    <tr>
        <td class='content' style="width: 200px;">${info.optimizeGroupName}</td>
        <td class='content' style="width: 50px;">${info.keywordCount}</td>
    </tr>
    </#list>
</table>
</#if>
<br>
<#if keywordOptimizationCountInfo ?? && (keywordOptimizationCountInfo?size > 0)>
以下关键字出现刷量异常，请注意查看：
<table>
    <tr>
        <th style="width: 200px;">关键字</th>
        <th style="width: 200px;">URL</th>
        <th style="width: 50px;">已刷数量</th>
        <th style="width: 50px;">无效点击</th>
    </tr>
    <#list keywordOptimizationCountInfo as info>
        <tr>
            <td class='content' style="width: 200px;">${info.keyword}</td>
            <td class='content' style="width: 200px;">${info.url}</td>
            <td class='content' style="width: 50px;">${info.optimizedCount}</td>
            <td class='content' style="width: 50px;">${info.invalidRefreshCount}</td>
        </tr>
    </#list>
</table>
</#if>
</body>
</html>
<p><b>顺时科技</b></p>
		 