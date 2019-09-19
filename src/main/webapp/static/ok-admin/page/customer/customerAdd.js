
let uuid_ = null;
let layui_ = null;

function initForm(data) {
    uuid_ = data;
    if (layui_ != null) {
        let $ = layui.jquery;
        let status_div = '<div class="layui-form-item" id="status_div">\n' +
            '            <label class="layui-form-label">客户状态</label>\n' +
            '            <div class="layui-input-block">\n' +
            '                <select name="status">\n' +
            '                    <option value="1" selected>激活</option>\n' +
            '                    <option value="2">暂停</option>\n' +
            '                </select>\n' +
            '            </div>\n' +
            '        </div>';
        $("#type_div").after($(status_div));
        setForm(layui_, uuid_);
    }
}

function setForm(l, u) {
    var $ = l.jquery;
    var form = l.form;
    l.jquery.ajax({
        url: '/internal/customer/getCustomersMsgById/' + u,
        type: 'get',
        dataType: 'json',
        success: function (res) {
            if (res.code === 200) {
                if (res.data.dailyReportIdentify) {
                    res.data.dailyReportIdentify = 1
                } else {
                    res.data.dailyReportIdentify = 0
                }
                let customerBusinessList = res.data.customerBusinessList;
                for (let index in customerBusinessList) {
                    res.data['customerBusinessList['+customerBusinessList[index]+']'] = customerBusinessList[index];
                }
                layui.form.val("addCustomer", res.data);

            } else {
                l.layer.msg(res.msg);
            }
        }
    });
}

layui.use(["form", "okLayer", "jquery", "layer"], function () {
    let form = layui.form;
    let okLayer = layui.okLayer;
    let $ = layui.jquery;

    if (uuid_ !== null) {
        let status_div = '<div class="layui-form-item" id="status_div">\n' +
            '            <label class="layui-form-label">客户状态</label>\n' +
            '            <div class="layui-input-block">\n' +
            '                <select name="status">\n' +
            '                    <option value="1" selected>激活</option>\n' +
            '                    <option value="2">暂停</option>\n' +
            '                </select>\n' +
            '            </div>\n' +
            '        </div>';
        $("#type_div").after($(status_div));
        setForm(layui, uuid_);
    } else {
        layui_ = layui;
    }

    form.on("submit(commitCustomer)", function (data) {
        // var postData = data.field;
        var customerBusinessStr = "";
        var customerBusinessList= [];
        if (data.field['customerBusinessList[keyword]']){
            customerBusinessList.push(data.field['customerBusinessList[keyword]'])
            delete data.field['customerBusinessList[keyword]']
        }

        if (data.field['customerBusinessList[qzsetting]']){
            customerBusinessList.push(data.field['customerBusinessList[qzsetting]'])
            delete data.field['customerBusinessList[qzsetting]']
        }

        if (data.field['customerBusinessList[fm]']){
            customerBusinessList.push(data.field['customerBusinessList[fm]'])
            delete data.field['customerBusinessList[fm]']

        }
        if (data.field.dailyReportIdentify ===1 ) {
            data.field.dailyReportIdentify = true
        } else {
            data.field.dailyReportIdentify = false
        }
        data.field.customerBusinessList = customerBusinessList;

        data.field.customerBusinessStr = customerBusinessStr;
        layer.confirm('确认保存？', {
            icon: 3,
            title: '保存数据',
            btn: ['确认', '取消']
            , yes: function (index) {
                $.ajax({
                    url: '/internal/customer/postCustomersAdd',
                    type: 'post',
                    data: JSON.stringify(data.field),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    success: function (res) {
                        if (res.code === 200) {
                            layer.close(index);
                            okLayer.msg.greenTick("保存成功", function () {
                                parent.window.sign = true;
                                parent.layer.close(parent.layer.getFrameIndex(window.name));
                            });
                        } else {
                            layui.layer.msg(res.msg);
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
