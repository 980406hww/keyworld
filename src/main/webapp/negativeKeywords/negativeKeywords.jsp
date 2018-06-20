<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>负面词设置</title>
    <link rel="stylesheet" href="${staticPath}/static/css/common.css">
    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${staticPath }/static/easyui/themes/gray/easyui.css" />
    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${staticPath }/static/easyui/themes/icon.css" />
    <link rel="stylesheet" href="${staticPath }/static/toastmessage/css/jquery.toastmessage.css">
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<script type="text/javascript" src="${staticPath }/static/easyui/jquery.easyui.min.js" charset="utf-8"></script>
<script src="${staticPath }/static/toastmessage/jquery.toastmessage.js"></script>
<script language="javascript">
    $(function () {
        $('#uploadTxtFileDialog').dialog("close");
        $("#searchEngine").val("${searchEngine}");
        $("#customerNegativeKeywords").val($("#customerNegativeKeywords").val().replace(/,/g,'\r'));
    });

    function resetNegativeKeywords() {
        $("#negativeKeywords").val("${negativeKeywords}");
    }

    function distinctKeywords(negativeKeywords) {
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
        return negativeKeywords;
    }

    function updateNegativeKeywords() {
        var negativeKeywords = $("#negativeKeywords").val();
        negativeKeywords = distinctKeywords(negativeKeywords);
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
                    $().toastmessage('showSuccessToast', "更新成功!");
                } else {
                    $().toastmessage('showErrorToast',"更新失败");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast',"更新失败");
            }
        });
    }

    function updateCustomerNegativeKeywords() {
        $("#uploadTxtFileDiv").css("display", "block");
        $('#uploadTxtFileDialog').dialog({
            resizable: true,
            width: 220,
            modal: true,
            buttons: [{
                text: '上传',
                iconCls: 'icon-ok',
                handler: function () {
                    var fileValue = $("#uploadTxtFileDialog").find("#file").val();
                    if(fileValue == ""){
                        alert("请选择要上传的负面词txt文件!");
                        return false;
                    }
                    var posIndex = fileValue.indexOf(".txt");
                    if (posIndex == -1) {
                        alert("只能上传txt文件！");
                        return false;
                    }
                    var formData = new FormData();
                    formData.append('searchEngine', $("#searchEngine").val());
                    formData.append('file', $("#uploadTxtFileDialog").find("#file")[0].files[0]);
                    $('#uploadTxtFileDialog').dialog("close");
                    $.ajax({
                        url: '/internal/config/updateCustomerNegativeKeywords',
                        type: 'POST',
                        cache: false,
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (result) {
                            if (result) {
                                $().toastmessage('showSuccessToast', "上传成功", true);
                            } else {
                                $().toastmessage('showErrorToast', "上传失败");
                            }
                        },
                        error: function () {
                            $().toastmessage('showErrorToast', "上传失败");
                            $('#uploadTxtFileDialog').dialog("close");
                        }
                    });
                }
            },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#uploadTxtFileDialog').dialog("close");
                    }
                }]
        });
        $('#uploadTxtFileDialog').window("resize",{top:$(document).scrollTop() + 100});
    }

    function findCustomerNegativeKeywords() {
        var searchEngine = $("#searchEngine").val();
        $.ajax({
            url: '/internal/config/findCustomerNegativeKeywords/' + searchEngine,
            type: 'POST',
            success: function (result) {
                if (result) {
                    result = result.replace(/,/g,'\r');
                    $("#customerNegativeKeywords").val(result);
                    $().toastmessage('showSuccessToast', "操作成功");
                } else {
                    $().toastmessage('showErrorToast', "操作失败");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "操作失败");
            }
        });
    }

    function refreshCustomerNegativeKeywords () {
        var config = {};
        var negativeKeywords = $("#customerNegativeKeywords").val().replace(/\n/g,',');
            config.negativeKeywords = negativeKeywords.replace(/,,/g,',');
            if(negativeKeywords.lastIndexOf(',') == negativeKeywords.length-1){
                config.negativeKeywords = config.negativeKeywords.substring(0,negativeKeywords.length-1);
            }
            config.searchEngine = $("#searchEngine").val();
        $.ajax({
            url: '/internal/config/refreshCustomerNegativeKeywords',
            data: JSON.stringify(config),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (data) {
                if (data) {
                    findCustomerNegativeKeywords();
                } else {
                    $().toastmessage('showErrorToast', "数据更新失败");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "数据更新失败");
            }
        });
    }
</script>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
</div>
<div id="centerDiv" style="margin-top: 20px;">
    负面词:<br><br>
    <textarea name="negativeKeywords" id="negativeKeywords" style="width: 500px;height: 100px;">${negativeKeywords}</textarea><br><br>
    <span style="margin-left: 430px;">
        <input type="button" id="updateNegativeKeywords" onclick="updateNegativeKeywords()" value="更新" />&nbsp;&nbsp;
        <input type="button" id="resetNegativeKeywords" onclick="resetNegativeKeywords()" value="重置" />
    </span>
    <br><br>
    搜索引擎:
    <select name="searchEngine" id="searchEngine" onchange="findCustomerNegativeKeywords()">
        <option value="Baidu">百度</option>
        <option value="Sogou">搜狗</option>
        <option value="360">360</option>
    </select><br><br>
    <textarea name="customerNegativeKeywords" id="customerNegativeKeywords"  onchange="refreshCustomerNegativeKeywords()" style="width: 500px;height: 300px;">${customerNegativeKeywords}</textarea><br><br>
    <span style="margin-left: 470px;">
        <input type="button" id="updateCustomerNegativeKeywords" onclick="updateCustomerNegativeKeywords()" value="更新" />
    </span>
</div>
<div id="uploadTxtFileDialog" title="更新客户负面词" class="easyui-dialog" style="left: 35%;">
    <div id="uploadTxtFileDiv" style="display: none;">
        <form method="post" id="uploadTxtFileForm" action="" enctype="multipart/form-data">
            <table width="100%" style="font-size:14px;">
                <tr><td>
                    <input type="file" id="file" name="file" size=50 height="50px" style="width: 180px;">
                </td></tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>

