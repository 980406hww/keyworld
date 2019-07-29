$(function () {
    $("#uploadCSVDialog").dialog("close");
    $("#showMachineGroupWorkInfoDiv").css("margin-top",$("#topDiv").height()-5);
});

function trimSearchCondition() {
    var searchMachineGroupWorkInfosForm = $("#searchMachineGroupWorkInfosForm");
    var machineGroup = searchMachineGroupWorkInfosForm.find("#machineGroup").val();
    var customerName = searchMachineGroupWorkInfosForm.find("#customerName").val();
    var dayNum = searchMachineGroupWorkInfosForm.find("#dayNum").val();
    if(machineGroup != "") {
        searchMachineGroupWorkInfosForm.find("#machineGroup").val($.trim(machineGroup));
    }
    if(customerName != "") {
        searchMachineGroupWorkInfosForm.find("#customerName").val($.trim(customerName));
    }

    if (dayNum != ""){
        searchMachineGroupWorkInfosForm.find("#dayNum").val(dayNum);
    }
}
function resetInvaidRefreshCount(machineGroup, customerName, fullMatchGroup, self){
    var customerKeywordRefreshStatInfoCriteria = {};
    customerKeywordRefreshStatInfoCriteria.customerName = customerName;
    customerKeywordRefreshStatInfoCriteria.machineGroup = machineGroup;
    customerKeywordRefreshStatInfoCriteria.fullMatchGroup = fullMatchGroup;
    $.ajax({
        url: '/internal/customerKeyword/resetInvalidRefreshCount',
        data: JSON.stringify(customerKeywordRefreshStatInfoCriteria),
        type: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (result) {
            if(result){
                $().toastmessage('showSuccessToast', "重置成功！");
                window.location.reload();
            }else{
                $().toastmessage('showErrorToast', "重置失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "重置失败！");
        }
    });
}
function selectAll(self){
    var a = document.getElementsByName("uuid");
    if(self.checked){
        for(var i = 0;i<a.length;i++){
            a[i].checked = true;
        }
    }else{
        for(var i = 0;i<a.length;i++){
            a[i].checked = false;
        }
    }
}
function decideSelectAll() {
    var a = document.getElementsByName("uuid");
    var select=0;
    for(var i = 0; i < a.length; i++){
        if (a[i].checked == true){
            select++;
        }
    }
    if(select == a.length){
        $("#selectAllChecked").prop("checked",true);
    }else {
        $("#selectAllChecked").prop("checked",false);
    }
}
function getSelectedIDs() {
    var uuids = '';
    $.each($("input[name=uuid]:checkbox:checked"), function(){
        if(uuids === ''){
            uuids = $(this).val();
        }else{
            uuids = uuids + "," + $(this).val();
        }
    });
    return uuids;
}
function uploadCsv() {
    $("#uploadCSVForm").css("display", "block");
    $('#uploadCSVDialog').dialog({
        resizable: false,
        width: 290,
        modal: true,
        title: '上传CSV文件',
        buttons: [{
            text: '上传',
            iconCls: 'icon-ok',
            handler: function () {
                var fileValue = $("#uploadCSVDialog").find("#file").val();
                if(fileValue == ""){
                    alert("请选择要上传的CSV文件!");
                    return false;
                }
                var posIndex = fileValue.indexOf(".csv");
                if (posIndex == -1) {
                    alert("只能上传csv文件！");
                    return false;
                }

                var formData = new FormData();
                formData.append('file', $("#uploadCSVDialog").find("#file")[0].files[0]);
                formData.append('searchEngine', $("#searchEngine").val());
                formData.append('reachStandardPosition', $("#reachStandardPosition").val());
                $.ajax({
                    url: '/internal/refreshstatinfo/uploadCSVFile',
                    type: 'POST',
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result) {
                            $().toastmessage('showSuccessToast', "上传成功");
                        } else {
                            $().toastmessage('showErrorToast', "上传失败");
                        }
                        $('#uploadCSVDialog').dialog("close");
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "上传失败");
                        $('#uploadCSVDialog').dialog("close");
                    }
                });
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#uploadCSVDialog').dialog("close");
                }
            }]
    });
    $('#uploadCSVDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function downloadTxt() {
    var groups = getSelectedIDs();
    if(groups === ''){
        alert('请选择要导出的分组！');
        return ;
    }
    if (confirm("确认要导出吗?") == false) return;
    var postData = {};
    postData.groups = groups.split(",");
    $.ajax({
        url: '/internal/refreshstatinfo/downloadKeywordUrlByGroup',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        success: function (data) {
            if(data){
                document.getElementById("downTXT").click();
            }else{
                $().toastmessage('showErrorToast', "导出失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "导出失败！");
        }
    });

}

// $(function () {
//     var menuHeight = $(".venus-menu").height();
//     $("#searchRefreshStatInfoForm").css("margin-top", menuHeight + 5);
//     $("#showRefreshStatInfoDiv").css("margin-top", menuHeight + 98);
// });