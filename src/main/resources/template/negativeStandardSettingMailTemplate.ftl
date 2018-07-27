<p>
<#if userName??><b>${userName}：您好，</b></#if>
</p>
<html>
    <head>
        <meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>
        <style type='text/css'>
            table {
                border-collapse: collapse;
                border: 2px;
                border: solid #ababab;
                border-width: 1px 0 0 1px;
                font-size: 12px;
                width: 95%;
            }

            table th, table td {
                border: solid #ababab;
                border-width: 0 1px 1px 0;
                padding: 2px;
            }

            #headTr {
                background-color: #add1ff;
            }

            th{
                height: 20px;
            }

            #negativeStandardSettingListTable tr:nth-child(even){background-color: #e9e9e9 }
            #negativeStandardSettingListTable tr:hover{background-color: #219100;}

            #negativeRankListTable tr:nth-child(even){background-color: #e9e9e9 }
            #negativeRankListTable  tr:hover{background-color: #219100;}

        </style>
    </head>
    您下面客户：今天关键字负面排名发生变化，请注意查看：
    <body>
        <table>
            <tr>
                <th colspan="12" style="font-size: 17px;">负面达标设置</th>
            </tr>
            <tr id="headTr" >
                <th class='content'>所在客户</th>
                <th class='content'>关键字</th>
                <th class='content'>搜索引擎</th>
                <th class='content'>首页负面数量</th>
                <th class='content'>前二页负面数量</th>
                <th class='content'>前三页负面数量</th>
                <th class='content'>前四页负面数量</th>
                <th class='content'>前五页负面数量</th>
                <th class='content'>是否达标</th>
                <th class='content'>最近达标时间</th>
                <th class='content'>创建时间</th>
            </tr>
            <tbody id="negativeStandardSettingListTable">
                <#list negativeStandardSettingList as negativeStandardSetting >
                    <tr>
                        <th class='content'>${negativeStandardSetting.contactPerson}</th>
                        <th class='content'>${negativeStandardSetting.keyword}</th>
                        <th class='content'>${negativeStandardSetting.searchEngine}</th>
                        <th class='content'>${negativeStandardSetting.topOnePageNegativeCount}</th>
                        <th class='content'>${negativeStandardSetting.topTwoPageNegativeCount}</th>
                        <th class='content'>${negativeStandardSetting.topThreePageNegativeCount}</th>
                        <th class='content'>${negativeStandardSetting.topFourPageNegativeCount}</th>
                        <th class='content'>${negativeStandardSetting.topFivePageNegativeCount}</th>
                        <th class='content'>
                            <#if negativeStandardSetting.reachStandard ?? && (negativeStandardSetting.reachStandard == false)>否</#if>
                            <#if negativeStandardSetting.reachStandard ?? && (negativeStandardSetting.reachStandard == true)>是</#if>
                        </th>
                        <#if negativeStandardSetting.standardTime??>
                            <th class='content'>${negativeStandardSetting.standardTime?datetime}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                        <#if negativeStandardSetting.createTime??>
                            <th class='content'>${negativeStandardSetting.createTime?datetime}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                    </tr>
                </#list>
            </tbody>
            <tr>
                <th colspan="12" style="font-size: 17px;">负面排名</th>
            </tr>
            <tr id="headTr">
                <th class='content' colspan="2" >关键字</th>
                <th class='content'>搜索引擎</th>
                <th class='content'>首页排名</th>
                <th class='content'>第二页排名</th>
                <th class='content'>第三页排名</th>
                <th class='content'>第四页排名</th>
                <th class='content'>第五页排名</th>
                <th class='content'>其他页排名</th>
                <th class='content'>负面总数</th>
                <th class='content'>创建时间</th>
            </tr>
            <tbody id="negativeRankListTable">
                <#list negativeRankList as negativeRank>
                    <tr>
                        <th class='content'  colspan="2">${negativeRank.keyword}</th>
                        <th class='content'>${negativeRank.searchEngine}</th>
                        <#if negativeRank.firstPageRanks??>
                            <th class='content'>${negativeRank.firstPageRanks}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                        <#if negativeRank.secondPageRanks??>
                            <th class='content'>${negativeRank.secondPageRanks}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                        <#if negativeRank.thirdPageRanks??>
                            <th class='content'>${negativeRank.thirdPageRanks}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                        <#if negativeRank.fourthPageRanks??>
                            <th class='content'>${negativeRank.fourthPageRanks}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                        <#if negativeRank.fifthPageRanks??>
                            <th class='content'>${negativeRank.fifthPageRanks}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                        <#if negativeRank.otherPageRanks??>
                            <th class='content'>${negativeRank.otherPageRanks}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                        <#if negativeRank.negativeCount??>
                            <th class='content'>${negativeRank.negativeCount}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                        <#if negativeRank.createTime??>
                            <th class='content'>${negativeRank.createTime?datetime}</th>
                        <#else>
                            <th class='content'>---</th>
                        </#if>
                    </tr>
                </#list>
            </tbody>
        </table>
    </body>
</html>
<p><b>顺时科技</b></p>
		 