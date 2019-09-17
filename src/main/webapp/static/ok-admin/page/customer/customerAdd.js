
let uuid_ = null;
let layui_ = null;

function initForm(data) {
    uuid_ = data;
    if (layui_ != null) {
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
                layui.form.val("addCustomer", res.data);
                let customerBusinessList = res.data.customerBusinessList;
                for (let index in customerBusinessList) {
                    $("input:checkbox[name='customerBusinessList']").each(function(){
                        if($(this).val()===customerBusinessList[index]){
                            $(this).attr("checked","true")
                        }
                    });
                }
                form.render();
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

    if (uuid_ != null) {
        setForm(layui, uuid_);
    } else {
        layui_ = layui;
    }

    form.on("submit(commitCustomer)", function (data) {
        var customerBusinessStr = "";
        var customerBusinessList= new Array();
        $("input:checkbox[name='customerBusinessList']:checked").each(function(){
            customerBusinessList.push($(this).val())
        });
        customerBusinessStr = customerBusinessList.join(",");
        data.field.customerBusinessStr = customerBusinessStr;

        layer.confirm('确认保存？', {
            icon: 3,
            title: '保存数据',
            btn: ['确认', '取消']
            , yes: function (index) {
                $.ajax({
                    url: '/internal/customer/postCustomersAdd',
                    type: 'post',
                    data: data.field,
                    dataType: 'json',
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
