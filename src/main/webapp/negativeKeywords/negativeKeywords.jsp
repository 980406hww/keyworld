<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>负面词配置</title>
    <link rel="stylesheet" href="${staticPath}/static/css/common.css">
    <link rel="stylesheet" href="${staticPath }/static/toastmessage/css/jquery.toastmessage.css">
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<script src="${staticPath }/static/toastmessage/jquery.toastmessage.js"></script>
<script language="javascript">
    function updateNegativeKeywords() {
        var negativeKeywords = $("#negativeKeywords").val();
        negativeKeywords = negativeKeywords.replace(/，/g,",");
        var negativeJson = {};
        var negativeKeywordArray = [];
        $.each(negativeKeywords.split(","), function (index, value) {
            if(!negativeJson[value] && value != "") {
                negativeKeywordArray.push(value);
                negativeJson[value] = 1;
            }
        });
        negativeKeywords = negativeKeywordArray.toString();
        $.ajax({
            url: '/internal/config/updateNegativeKeywords',
            data: JSON.stringify({negativeKeywords:negativeKeywords}),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 2000,
            type: 'POST',
            success: function (result) {
                if (result) {
                    $().toastmessage('showSuccessToast', "更新成功!", true);
                } else {
                    $().toastmessage('showErrorToast',"更新失败");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast',"更新失败");
            }
        });
    }
</script>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
</div>
<div id="centerDiv" style="margin-top: 20px;">
    负面词：<br><br>
    <textarea name="negativeKeywords" id="negativeKeywords" style="width: 500px;height: 100px;">${negativeKeywords}</textarea><br><br>
    <input type="button" id="updateNegativeKeywords" onclick="updateNegativeKeywords()" value="更新" />
</div>
</body>
</html>

