let uuid_ = null;
let layui_ = null;

function initForm(data) {
    uuid_ = data.uuid;
    document.getElementById('entryType').value = data.entryType;
    if (data.entryType === 'qt'){
        let se = document.getElementById('qtSpecialSearchEngine');
        if (se) {
            se.style.display = 'block';
        }
        let ea = document.getElementById('qtSpecialExternalAccount');
        if (ea) {
            ea.style.display = 'block';
        }
    }
    if (layui_ != null && uuid_ != null) {
        setForm(layui_, uuid_);
    }
}

function setForm(l, u) {
    l.jquery.ajax({
        url: '/internal/customer/getCustomersMsgById/' + u,
        type: 'get',
        dataType: 'json',
        success: function (res) {
            if (res.code === 200) {
                let customerBusinessList = res.data.customerBusinessList;
                for (let index in customerBusinessList) {
                    res.data['customerBusinessList[' + customerBusinessList[index] + ']'] = customerBusinessList[index];
                }
                l.form.val("addCustomer", res.data);
                l.form.render("select");
            } else {
                l.layer.msg(res.msg, {icon: 5, time: 3000});
            }
        }
    });
}

layui.use(["form", "common", "jquery", "layer"], function () {
    let form = layui.form;
    let common = layui.common;
    let $ = layui.jquery;

    if (uuid_ !== null) {
        setForm(layui, uuid_);
    } else {
        layui_ = layui;
    }

    form.on("submit(commitCustomer)", function (data) {
        let customerBusinessStr = "";
        let customerBusinessList = [];
        if (data.field['customerBusinessList[keyword]']) {
            customerBusinessList.push(data.field['customerBusinessList[keyword]']);
            delete data.field['customerBusinessList[keyword]'];
        }

        if (data.field['customerBusinessList[qzsetting]']) {
            customerBusinessList.push(data.field['customerBusinessList[qzsetting]']);
            delete data.field['customerBusinessList[qzsetting]'];
        }

        if (data.field['customerBusinessList[fm]']) {
            customerBusinessList.push(data.field['customerBusinessList[fm]']);
            delete data.field['customerBusinessList[fm]'];
        }

        if (data.field['customerBusinessList[qt]']) {
            customerBusinessList.push(data.field['customerBusinessList[qt]']);
            delete data.field['customerBusinessList[qt]'];
        }

        if (customerBusinessList.length === 0){
            common.showFailMsg('业务类型必须选择一个');
            return false;
        }
        data.field.customerBusinessList = customerBusinessList;
        data.field.customerBusinessStr = customerBusinessStr;
        if (!common.waitMoment()) {
            return false;
        }
        $.ajax({
            url: '/internal/customer/saveCustomer2',
            type: 'post',
            data: JSON.stringify(data.field),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (res) {
                if (res.code === 200) {
                    common.showSuccessMsg("保存成功", function () {
                        parent.window.sign = true;
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
                    });
                } else {
                    common.showFailMsg(res.msg);
                }
            }
        });
        return false;
    });

    $('#close').click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
        return false;
    });

    form.verify({
        qq: [
            /(^$)|(^[1-9]\d{4,14}$)/,
            "请输入正确格式QQ"
        ],
        telPhone: [
            /(^$)|(^(13[0-9]|14[5-9]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[13589])\d{8}$)/,
            "请输入正确格式手机号码"
        ],
        ifQtNotNull: function (value) {
            if (document.getElementById('entryType').value === 'qt') {
                if (value && value.trim()) {
                    return '';
                }else {
                    return '必填项不允许为空';
                }
            }
            return '';
        }
    });
});
