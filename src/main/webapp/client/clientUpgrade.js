$(function () {
    $('#clientUpgradeDialog').dialog("close");
    $("#centerDiv").css("margin-top", $("#topDiv").height());
    pageLoad();
});

function selectAll(self) {
    var a = document.getElementsByName("uuid");
    if (self.checked) {
        for (var i = 0; i < a.length; i++) {
            a[i].checked = true;
        }
    } else {
        for (var i = 0; i < a.length; i++) {
            a[i].checked = false;
        }
    }
}

function changePaging(currentPage, pageSize) {
    var searchClientUpgradeFrom = $("#searchClientUpgradeFrom");
    searchClientUpgradeFrom.find("#currentPageNumberHidden").val(currentPage);
    searchClientUpgradeFrom.find("#pageSizeHidden").val(pageSize);
    searchClientUpgradeFrom.submit();
}

function pageLoad() {
    var searchClientUpgradeFrom = $("#searchClientUpgradeFrom");
    var pageSize = searchClientUpgradeFrom.find('#pageSizeHidden').val();
    var pages = searchClientUpgradeFrom.find('#pagesHidden').val();
    var currentPageNumber = searchClientUpgradeFrom.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    showCustomerBottomDiv.find("#chooseRecords").val(pageSize);
    if (parseInt(currentPageNumber) > 1 && parseInt(currentPageNumber) < parseInt(pages)) {
        showCustomerBottomDiv.find("#firstButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#upButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#nextButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#lastButton").removeAttr("disabled");
    } else if (parseInt(pages) <= 1) {
        showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
    } else if (parseInt(currentPageNumber) <= 1) {
        showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
    } else {
        showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
    }
}

function decideSelectAll() {
    var a = document.getElementsByName("uuid");
    var select = 0;
    for (var i = 0; i < a.length; i++) {
        if (a[i].checked == true) {
            select++;
        }
    }
    if (select == a.length) {
        $("#selectAllChecked").prop("checked", true);
    } else {
        $("#selectAllChecked").prop("checked", false);
    }
}

function showClientUpgrade(uuid) {
    if (uuid == null) {
        $('#clientUpgradeForm')[0].reset();
    } else {
        $.ajax({
            url: '/internal/clientUpgrade/getClientUpgrade/' + uuid,
            type: 'Get',
            success: function (data) {
                if (data != null) {
                    var clientUpgradeForm = $("#clientUpgradeForm");
                    clientUpgradeForm.find("#terminalType").val(data.terminalType);
                    clientUpgradeForm.find("#programType").val(data.programType);
                    clientUpgradeForm.find("#version").val(data.version);
                    clientUpgradeForm.find("#targetVersion").val(data.targetVersion);
                    clientUpgradeForm.find("#maxUpgradeCount").val(data.maxUpgradeCount);
                    clientUpgradeForm.find("#residualUpgradeCount").val(data.residualUpgradeCount);
                    clientUpgradeForm.find("#status").val("" + data.status);
                } else {
                    $().toastmessage('showErrorToast', "获取信息失败");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "获取信息失败");
            }
        });
    }
    $("#clientUpgradeDialog").show();
    $("#clientUpgradeDialog").dialog({
        resizable: false,
        width: 240,
        modal: true,
        title: '自动升级设置',
        closed:true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveClientUpgrade(uuid);
            }
        },
        {
            text: '清空',
            iconCls: 'fi-trash',
            handler: function () {
                $('#clientUpgradeForm')[0].reset();
            }
        },
        {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $('#clientUpgradeDialog').dialog("close");
                $('#clientUpgradeForm')[0].reset();
            }
        }]
    });
    $('#clientUpgradeDialog').dialog("open");
    $('#clientUpgradeDialog').window("resize",{top:$(document).scrollTop() + 100});
}

function saveClientUpgrade(uuid) {
    var clientUpgradeObj = {};
    clientUpgradeObj.uuid = uuid;
    var terminalType = $("#clientUpgradeForm").find("#terminalType").val();
    var programType = $("#clientUpgradeForm").find("#programType").val();
    var version = $("#clientUpgradeForm").find("#version").val();
    var targetVersion = $("#clientUpgradeForm").find("#targetVersion").val();
    var maxUpgradeCount = $("#clientUpgradeForm").find("#maxUpgradeCount").val();
    var residualUpgradeCount = $("#clientUpgradeForm").find("#residualUpgradeCount").val();
    var status = $("#clientUpgradeForm").find("#status").val();
    if(terminalType == "" || programType == "" || version == "" || targetVersion == "" || maxUpgradeCount == "" || residualUpgradeCount == "" || status == "") {
        alert("请完善升级信息");
    }
    clientUpgradeObj.terminalType = terminalType;
    clientUpgradeObj.programType = programType;
    clientUpgradeObj.version = version;
    clientUpgradeObj.targetVersion = targetVersion;
    clientUpgradeObj.maxUpgradeCount = maxUpgradeCount;
    clientUpgradeObj.residualUpgradeCount = residualUpgradeCount;
    clientUpgradeObj.status = status;
    $.ajax({
        url: '/internal/clientUpgrade/saveClientUpgrade',
        data: JSON.stringify(clientUpgradeObj),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功", true);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}

function deleteClientUpgrade(uuid) {
    if (confirm("确定要删除这条信息吗?") == false) return;
    $.ajax({
        url: '/internal/clientUpgrade/deleteClientUpgrade/' + uuid,
        type: 'POST',
        success: function (result) {
            if (result) {
                $().toastmessage('showSuccessToast', "操作成功", true);
            } else {
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}