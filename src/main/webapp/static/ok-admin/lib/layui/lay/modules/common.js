var milliseconds;

layui.define(['jquery', 'layer', 'okTab'], function (exports) {

    var $ = layui.jquery;
    var layer = layui.layer;
    var okTab = layui.okTab();

    var obj = {
        formToJsonObject: function (form_id) {
            var formData = decodeURIComponent($("#" + form_id).serialize(), true);
            formData = formData.replace(/&/g, "\",\"");
            formData = formData.replace(/=/g, "\":\"");
            formData = "{\"" + formData + "\"}";
            formData = $.parseJSON(formData);
            $.each(formData, function (idx, item) {
                formData[idx] = $.trim(item)
            });
            return formData;
        },
        jsonObjectTrim: function (jsonObject) {
            $.each(jsonObject, function (idx, item) {
                if (typeof (item) == "string") {
                    jsonObject[idx] = $.trim(item)
                } else {
                    return true;
                }
            });
            return jsonObject;
        },
        showSuccessMsg: function (msg, callback) {
            layer.msg(msg, {
                icon: 6,
                anim: 5,
                time: 1000,
                isOutAnim: false
            }, function () {
                if (callback) {
                    callback()
                }
            });
        },
        showFailMsg: function (msg) {
            layer.msg(msg, {
                icon: 5,
                anim: 5,
                time: 3000,
                isOutAnim: false
            }, function () {

            });
        },
        updateOrNewTab: function (url, tit, id) {
            var update = false;
            var contentIframe = ("<iframe src='" + url + "' lay-id='" + id + "'frameborder='0' scrolling='yes' width='100%' height='100%'></iframe>");
            parent.layui.$('.layui-tab-title li').each(function () {
                if (id === this.getAttribute('lay-id')) {
                    update = true;
                }
            });
            if (!update) {
                parent.layui.element.tabAdd('ok-tab', {
                        title: '<strong style="display: none;" is-close="true" lay-id="' + id + '" data-url="' + url + '"></strong>' +
                            '<i class="layui-icon">&#xe648;</i> ' + tit,
                        content: contentIframe,
                        id: id
                    }
                );
            }
            parent.layui.element.tabChange('ok-tab', id)
        },
        waitMoment: function () {
            let currentMilliseconds = new Date().getTime();
            console.log(currentMilliseconds + '进来了一次');
            if (milliseconds) {
                if (currentMilliseconds - milliseconds >= 1500) {
                    milliseconds = currentMilliseconds;
                    return true;
                } else {
                    parent.location.reload();
                    return false;
                }
            }
            milliseconds = currentMilliseconds;
            return true;
        }
    };
    exports('common', obj);
});
