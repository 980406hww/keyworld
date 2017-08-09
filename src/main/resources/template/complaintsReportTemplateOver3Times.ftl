<p><b>${username} 您好，</b></p>
&nbsp;&nbsp;您的投诉情况如下 ：以下为反复出现3次的负面词
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
      width: 120px
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
    <th>反复出现3次的来源</th>
    <th>PC端最新投诉时间</th>
    <th>Phone端最新投诉时间</th>
  </tr>
<#if PCOver3TimestSmainKeywordVOS ??>
  <#list PCOver3TimestSmainKeywordVOS as PCOver3TimestSmainKeywordVOS>
    <tr>
      <td class='negative_content'>${PCOver3TimestSmainKeywordVOS.keyword}</td>
      <td class='negative_content'>${PCOver3TimestSmainKeywordVOS.group}</td>
      <td class='negative_content'>${PCOver3TimestSmainKeywordVOS.tsNegativeKeyword}</td>
      <td class='negative_content'
          style="color: red">${PCOver3TimestSmainKeywordVOS.pcOccurTimes!}</td>
      <td class='negative_content'
          style="color: red">${PCOver3TimestSmainKeywordVOS.phoneOccurTimes!}</td>

      <td class='negative_content' style="color: red">PC端</td>

      <#if PCOver3TimestSmainKeywordVOS.pcComplainTime ??>
        <td class='negative_content'>${PCOver3TimestSmainKeywordVOS.pcComplainTime}</td>
      <#else >
        <td class='negative_content'></td>
      </#if>
      <#if PCOver3TimestSmainKeywordVOS.pcComplainTime ??>
        <td class='negative_content'>${PCOver3TimestSmainKeywordVOS.phoneComplainTime}</td>
      <#else >
        <td class='negative_content'></td>
      </#if>
    </tr>
  </#list>
</#if>
<#if phoneOver3TimestSmainKeywordVOS ??>
  <#list phoneOver3TimestSmainKeywordVOS as phoneOver3TimestSmainKeywordVOS>
    <tr>
      <td class='negative_content'>${phoneOver3TimestSmainKeywordVOS.keyword}</td>
      <td class='negative_content'>${phoneOver3TimestSmainKeywordVOS.group}</td>
      <td class='negative_content'>${phoneOver3TimestSmainKeywordVOS.tsNegativeKeyword}</td>
      <td class='negative_content'
          style="color: red">${phoneOver3TimestSmainKeywordVOS.pcOccurTimes!}</td>
      <td class='negative_content'
          style="color: red">${phoneOver3TimestSmainKeywordVOS.phoneOccurTimes!}</td>

      <td class='negative_content' style="color: red">Phone端</td>

      <#if phoneOver3TimestSmainKeywordVOS.pcComplainTime ??>
        <td class='negative_content'>${phoneOver3TimestSmainKeywordVOS.pcComplainTime}</td>
      <#else >
        <td class='negative_content'></td>
      </#if>
      <#if phoneOver3TimestSmainKeywordVOS.pcComplainTime ??>
        <td class='negative_content'>${phoneOver3TimestSmainKeywordVOS.phoneComplainTime}</td>
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
		 