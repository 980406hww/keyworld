<p><b>${username} 您好，</b></p>
&nbsp;&nbsp;您的投诉情况如下 ：
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
    <th>相关负面词</th>
    <th>PC端投诉次数</th>
    <th>Phone端投诉次数</th>
    <th>PC端投诉时间</th>
    <th>Phone端投诉时间</th>
  </tr>
<#list tsMainKeywordVOS as tsMainKeywordVO>
  <tr>
    <td class='negative_content'>${tsMainKeywordVO.keyword}</td>
    <td class='negative_content'>${tsMainKeywordVO.tsNegativeKeyword}</td>
    <td class='negative_content'>${tsMainKeywordVO.pcOccurTimes!"3"}</td>
    <td class='negative_content'>${tsMainKeywordVO.phoneOccurTimes!"3"}</td>
    <td class='negative_content'>${tsMainKeywordVO.pcComplainTime!}</td>
    <td class='negative_content'>${tsMainKeywordVO.phoneComplainTime!}</td>
  </tr>
</#list>
</table>
</body>
</html>
<p><b>顺时科技</b></p>
		 