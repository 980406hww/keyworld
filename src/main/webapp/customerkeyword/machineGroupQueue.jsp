<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>机器分组工作队列</title>
    <%@ include file="/commons/basejs.jsp" %>
    <style>
        td{
            display: table-cell;
            vertical-align: inherit;
        }
        #showMachineGroupQueueTableDiv {
            width: 100%;
            margin: auto;
        }
        #customerKeywordTopDiv {
           /* position: fixed;*/
            top: 0px;
            left: 0px;
            background-color: white;
            width: 100%;
        }
        #machineGroupQueueTable {
            width: 100%;
        }
        #machineGroupQueueTable tr:nth-child(odd){background:#EEEEEE;}

        #machineGroupQueueTable td{
            text-align: left;
        }
        #saveCustomerKeywordDialog ul{list-style: none;margin: 0px;padding: 0px;}
        #saveCustomerKeywordDialog li{margin: 5px 0;}
        #saveCustomerKeywordDialog .customerKeywordSpanClass{width: 70px;display: inline-block;text-align: right;}
    </style>
</head>
<body>

<div id="customerKeywordTopDiv">
    <%@include file="/menu.jsp" %>
    <form id="searchCustomerKeywordForm" style="font-size:12px; width: 100%;" action="/internal/customerKeyword/showMachineGroupAndSize" method="post">
        <div id="searchCustomerKeywordTable" >

            <br>
            <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
                <input type="submit"  value=" 查询 ">&nbsp;
            </shiro:hasPermission>

        </div>
    </form>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=100>机器分组</td>
            <td align="center" width=100>数量</td>
        </tr>
    </table>
</div>
<div id="showMachineGroupQueueTableDiv" style="margin-bottom: 30px">
    <table id="machineGroupQueueTable" style="font-size:12px;">

        <c:forEach items="${machineGroupQueueVOS}" var="machineGroupQueueVO">
            <tr height=30 onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td align="center" width=80>
                        ${machineGroupQueueVO.machineGroupName}
                </td>
                <td align="center" width=80>
                        ${machineGroupQueueVO.size}
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<%@ include file="/commons/loadjs.jsp" %>
</body>
</html>
