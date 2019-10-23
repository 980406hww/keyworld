
let uuid_ = null;
let layui_ = null;

function initForm(data) {
    uuid_ = data.uuid;
    document.getElementById('entryType').value = data.entryType;
    if (layui_ != null && uuid_ !=null) {
        setForm(layui_, uuid_);
    }
}

function setForm(l, u) {
    let $ = l.jquery;
    l.jquery.ajax({
        url: '/internal/customer/getCustomersMsgById/' + u,
        type: 'get',
        dataType: 'json',
        success: function (res) {
            if (res.code === 200) {
                let customerBusinessList = res.data.customerBusinessList;
                for (let index in customerBusinessList) {
                    res.data['customerBusinessList['+customerBusinessList[index]+']'] = customerBusinessList[index];
                }
                l.form.val("addCustomer", res.data);

            } else {
                l.layer.msg(res.msg, {icon:5, time: 3000});
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
        let customerBusinessList= [];
        if (data.field['customerBusinessList[keyword]']){
            customerBusinessList.push(data.field['customerBusinessList[keyword]']);
            delete data.field['customerBusinessList[keyword]'];
        }

        if (data.field['customerBusinessList[qzsetting]']){
            customerBusinessList.push(data.field['customerBusinessList[qzsetting]']);
            delete data.field['customerBusinessList[qzsetting]'];
        }

        if (data.field['customerBusinessList[fm]']){
            customerBusinessList.push(data.field['customerBusinessList[fm]']);
            delete data.field['customerBusinessList[fm]'];
        }

        data.field.customerBusinessList = customerBusinessList;
        data.field.customerBusinessStr = customerBusinessStr;
        layer.confirm('确认保存？', {
            icon: 3,
            title: '保存数据',
            btn: ['确认', '取消']
            , yes: function (index) {
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
                            layer.close(index);
                            common.showSuccessMsg("保存成功", function () {
                                parent.window.sign = true;
                                parent.layer.close(parent.layer.getFrameIndex(window.name));
                            });
                        } else {
                            common.showFailMsg(res.msg);
                        }
                    }
                });
            }, btn2: function (index) {
                layer.close(index);
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
        ]
    });
});
