function openMessageBox(type, customerUuid, contactPerson) {
    var showUserMessageDialog = $("#showUserMessageDialog");
    getActiveUsers();
    getUserMessage(type, customerUuid, contactPerson);
    showUserMessageDialog.show();
    showUserMessageDialog.dialog({
        resizable: false,
        bgiframe: true,
        draggable: false, // 对话框是否可被拖动
        height: 280,
        width: 405,
        title: '留言框',
        modal: true,
        buttons: [{
            text: '处理完毕',
            handler: function() {
                saveUserMessage(type, customerUuid, 1);
            }
        }, {
            text: '历史留言',
            handler: function () {
                findHistoryUserMessages(type, customerUuid);
            }
        }, {
            text: '保存',
            iconCls: "icon-ok",
            handler: function () {
                saveUserMessage(type, customerUuid);
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#showUserMessageDialog").dialog("close");
            }
        }],
        onClose: function() {
            $("#showUserMessageForm")[0].reset();
            $("#userMessageListTable tr:not(:first)").remove();
        }
    });
    showUserMessageDialog.dialog("open");
    showUserMessageDialog.window("resize", {top: $(document).scrollTop() + 150 , left: 801});
}

function getActiveUsers(){
    $("#user_select option").remove();
    $.ajax({
        url: '/internal/user/findActiveUsers',
        type: 'POST',
        success: function (userInfos) {
            $.each(userInfos, function (idx, val) {
                $("#user_select").append("<option value='"+ val.loginName +"'>"+ val.userName +"</option>");
            });
            $("#user_select").multiselect({
                header: true,
                noneSelectedText: "请选择",
                checkAllText: "全选",
                uncheckAllText: "全不选",
                selectedText: "# 人",
                minWidth: 182,
                height: 100,
                selectedList: 3
            });
            $("#ui-multiselect-0-user_select-option-0").parent().parent().parent().parent().addClass("ui-multiselect-menu3");
        },
        error: function () {
            $().toastmessage("showErrorToast", "获取用户列表失败");
        }
    });
}

function getUserMessage(type, customerUuid, contactPerson) {
    var postData = {};
    postData.type = type;
    postData.customerUuid = customerUuid;
    $.ajax({
        url: '/internal/usermessage/getUserMessage',
        type: 'POST',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            var showUserMessageForm = $("#showUserMessageForm");
            showUserMessageForm.find("#contactPerson").text(contactPerson);
            var date = data.updateTime != null ? new Date(data.updateTime).format("yyyy-MM-dd") : new Date().format("yyyy-MM-dd");
            showUserMessageForm.find("label").text(date);
            var status = data.status == 1 ? "处理完毕" : "未处理";
            showUserMessageForm.find("#messageStatus").text(status);
            if (data != null) {
                showUserMessageForm.find("input[name='messageUuid']").val(data.uuid);
                showUserMessageForm.find("#senderUserName").text(data.senderUserName);
                showUserMessageForm.find("#user_select option").each(function () {
                    if ($(this).val() == data.receiverUserName) {
                        this.selected = true;
                        $("#user_select_ms span:last-child").text($(this).text());
                    }
                });
                showUserMessageForm.find("input[name='content']").val(data.content);
            }
        },
        error: function () {
            $().toastmessage("showErrorToast", '获取用户留言失败');
        }
    });
}

function findHistoryUserMessages(type, customerUuid) {
    $("#userMessageListTable tr:not(:first)").remove();
    var postData = {};
    postData.type = type;
    postData.customerUuid = customerUuid;
    $.ajax({
        url: '/internal/usermessage/getHistoryUserMessages',
        type: 'POST',
        data: JSON.stringify(postData),
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        success: function (userMessages) {
            if (userMessages != null && userMessages.length > 0) {
                $.each(userMessages, function (idx, val) {
                    var newTr = document.createElement("tr");
                    var userMessageElements = [
                        idx+1,
                        val.content,
                        val.receiverUserName,
                        val.status == 1 ? "处理完毕" : "未处理"
                    ];
                    $.each(userMessageElements, function (id, v) {
                        var newTd = document.createElement("td");
                        newTr.appendChild(newTd);
                        var newSpan = document.createElement("span");
                        newTd.appendChild(newSpan);
                        newSpan.innerHTML = v;
                        newTd.title = v;
                        if (id == 1) {
                            newSpan.className = "user-message-content";
                        }
                    });
                    $("#userMessageListTable")[0].lastChild.appendChild(newTr);
                });
            }
        },
        error: function () {
            $().toastmessage("showErrorToast", "获取历史留言失败");
        }
    });
}

function saveUserMessage(type, customerUuid, status) {
    var showUserMessageForm = $("#showUserMessageForm");
    var postData = {};
    var uuid = showUserMessageForm.find("input[name='messageUuid']").val();
    if (status) {
        if (uuid == "") {
            alert("新增留言，不能修改处理状态");
            return false;
        }
        if(confirm("确定修改处理状态？") == false) {
            return false;
        }
        postData.uuid = uuid;
        postData.updateStatus = 1;
        postData.status = status;
    } else {
        if (uuid != "") {
            postData.uuid = uuid;
        }
        var receiverUserNames = showUserMessageForm.find("#user_select").multiselect("getChecked").map(function () {
            return this.value;
        }).get();
        if (receiverUserNames.length < 1) {
            alert("请选择收信人");
            return false;
        }
        postData.receiverUserNames = receiverUserNames;
        var content = showUserMessageForm.find("input[name='content']").val();
        if (content == '') {
            alert("请输入处理内容");
            return false;
        }
        postData.content = content;
        postData.type = type;
        postData.customerUuid = customerUuid;
    }
    $.ajax({
        url: '/internal/usermessage/saveUserMessages',
        type: 'POST',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (result) {
            if (result) {
                $().toastmessage("showSuccessToast", "保存成功");
                $("#showUserMessageDialog").dialog("close");
            }
        },
        error: function () {
            $().toastmessage("showErrorToast", "保存失败");
        }
    });
}

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};