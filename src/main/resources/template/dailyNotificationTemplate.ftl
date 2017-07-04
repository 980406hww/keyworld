<p><b>${username} 您好，</b></p>
&nbsp;&nbsp;您监控的关键词负面情况如下 ：
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
    <style type='text/css'>
        table {
            border-collapse:collapse;
            border:solid #8BC34A;
            border-width:1px 0 0 1px;
            font-size:12px;
        }
        table th,table td {border:solid #8BC34A;border-width:0 1px 1px 0;padding:2px;width:120px}
        .negative_content {background-color: #FBE1E1;}
    </style>
</head>
<body>
<table>
    <tr>
        <th style="width:250px">关键词</th>
        <th>监控类型</th>
        <th>舆情总量</th>
        <th>负面数量</th>
        <th>下拉</th>
        <th>相关</th>
        <th>推荐</th>
    </tr>
<#list keywordStatisticsVOs as keywordStatisticsVO>
    <tr>
        <#if keywordStatisticsVO.monitorCount gt 0><td style='width:250px'' rowspan='${keywordStatisticsVO.monitorCount}'>${keywordStatisticsVO
        .keyword}</td></#if>
        <td>${keywordStatisticsVO.monitorType}</td>
        <td>${keywordStatisticsVO.publicOpinionAmount}</td>
        <#if keywordStatisticsVO.publicOpinionNegativeAmount gt 0>
            <td class='negative_content'>${keywordStatisticsVO.publicOpinionNegativeAmount}</td>
        <#else>
            <td></td>
        </#if>
        <#if keywordStatisticsVO.suggestiveKeywordNegativeAmount gt 0>
            <td class='negative_content'>${keywordStatisticsVO.suggestiveKeywordNegativeAmount}</td>
        <#else>
            <td></td>
        </#if>
        <#if keywordStatisticsVO.relativeKeywordNegativeAmount gt 0>
            <td class='negative_content'>${keywordStatisticsVO.relativeKeywordNegativeAmount}</td>
        <#else>
            <td></td>
        </#if>
        <#if keywordStatisticsVO.recommendedKeywordNegativeAmount gt 0>
            <td class='negative_content'>${keywordStatisticsVO.recommendedKeywordNegativeAmount}</td>
        <#else>
            <td></td>
        </#if>
    </tr>
</#list>
</table>
</body>
</html>
<p><b>顺时科技</b></p>
		 