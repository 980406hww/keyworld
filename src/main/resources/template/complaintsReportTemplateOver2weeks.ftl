<p><b>${username} 您好，</b></p>
&nbsp;&nbsp;您的投诉情况如下 ：以下为近期投诉失败的负面词
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
      width: 130px
    }

    .negative_content {
      background-color: #FBE1E1;
    }
  </style>
</head>
<body>
<table>
  <tr>
    <th style="width:250px">投诉关键词</th>
    <th>区域分组</th>
    <th>相关负面词</th>
    <th>PC端反复出现次数</th>
    <th>Phone端反复出现次数</th>
    <th>2周内投诉失败来源</th>
    <th>PC端最新投诉时间</th>
    <th>Phone端最新投诉时间</th>
  </tr>
<#if  PCOver2WeekstSmainKeywordVOS ?? >
  <#list PCOver2WeekstSmainKeywordVOS as PCOver2WeekstSmainKeywordVOS>
    <tr>
      <td class='negative_content'>${PCOver2WeekstSmainKeywordVOS.keyword}</td>
      <td class='negative_content'>${PCOver2WeekstSmainKeywordVOS.group}</td>
      <td class='negative_content'>${PCOver2WeekstSmainKeywordVOS.tsNegativeKeyword}</td>
      <td class='negative_content'
          style="color: red">${PCOver2WeekstSmainKeywordVOS.pcOccurTimes!}</td>
      <td class='negative_content'
          style="color: red">${PCOver2WeekstSmainKeywordVOS.phoneOccurTimes!}</td>

      <td class='negative_content' style="color: red">PC端</td>

      <#if PCOver2WeekstSmainKeywordVOS.pcComplainTime ??>
        <td class='negative_content'>${PCOver2WeekstSmainKeywordVOS.pcComplainTime}</td>
      <#else >
        <td class='negative_content'></td>
      </#if>
      <#if PCOver2WeekstSmainKeywordVOS.pcComplainTime ??>
        <td class='negative_content'>${PCOver2WeekstSmainKeywordVOS.phoneComplainTime}</td>
      <#else >
        <td class='negative_content'></td>
      </#if>
    </tr>
  </#list>
</#if>

<#if  phoneOver2WeekstSmainKeywordVOS ?? >
  <#list phoneOver2WeekstSmainKeywordVOS as phoneOver2WeekstSmainKeywordVOS>
    <tr>
      <td class='negative_content'>${phoneOver2WeekstSmainKeywordVOS.keyword}</td>
      <td class='negative_content'>${phoneOver2WeekstSmainKeywordVOS.group}</td>
      <td class='negative_content'>${phoneOver2WeekstSmainKeywordVOS.tsNegativeKeyword}</td>
      <td class='negative_content'
          style="color: red">${phoneOver2WeekstSmainKeywordVOS.pcOccurTimes!}</td>
      <td class='negative_content'
          style="color: red">${phoneOver2WeekstSmainKeywordVOS.phoneOccurTimes!}</td>

      <td class='negative_content' style="color: red">Phone端</td>

      <#if phoneOver2WeekstSmainKeywordVOS.pcComplainTime ??>
        <td class='negative_content'>${phoneOver2WeekstSmainKeywordVOS.pcComplainTime}</td>
      <#else >
        <td class='negative_content'></td>
      </#if>
      <#if phoneOver2WeekstSmainKeywordVOS.pcComplainTime ??>
        <td class='negative_content'>${phoneOver2WeekstSmainKeywordVOS.phoneComplainTime}</td>
      <#else >
        <td class='negative_content'></td>
      </#if>
    </tr>
  </#list>
</#if>
</table>
</body>
</html>
<p><b>顺时科技</b></p>
		 