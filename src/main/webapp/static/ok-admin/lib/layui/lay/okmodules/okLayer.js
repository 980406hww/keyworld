"use strict";
layui.define(["layer"], function (exports) {
    var okLayer = {
        /**
         * confirm()函数二次封装
         * @param content
         * @param yesFunction
         */
        confirm: function (content, yesFunction) {
            var options = {
                skin: okLayer.skinChoose(),
                icon: 3,
                title: "提示",
                anim: okLayer.animChoose()
            };
            layer.confirm(content, options, yesFunction);
        },

        /**
         * open()函数二次封装,支持在table页面和普通页面打开
         * @param title
         * @param content
         * @param width
         * @param height
         * @param successFunction
         * @param endFunction
         */
        open: function (title, content, width, height, successFunction,
            endFunction) {
            layer.open({
                title: title,
                type: 2,
                maxmin: true,
                shade: 0.5,
                anim: 0,
                scrollbar: false,
                area: [width, height],
                content: content,
                zIndex: layer.zIndex,
                skin: '',
                success: successFunction,
                end: endFunction
            });
        },

        close: function(){
            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index); //再执行关闭
        },

        /**
         * msg()函数二次封装
         */
        msg: {
            // msg弹窗默认消失时间
            time: 2000,
            // 绿色勾
            greenTick: function (content, callbackFunction) {
                var options = {icon: 1, time: okLayer.msg.time, anim: 0};
                layer.msg(content, options, callbackFunction);
            },
            // 红色叉
            redCross: function (content, callbackFunction) {
                var options = {icon: 2, time: okLayer.msg.time, anim: 0};
                layer.msg(content, options, callbackFunction);
            },
            // 黄色问号
            yellowQuestion: function (content, callbackFunction) {
                var options = {icon: 3, time: okLayer.msg.time, anim: 0};
                layer.msg(content, options, callbackFunction);
            },
            // 灰色锁
            grayLock: function (content, callbackFunction) {
                var options = {icon: 4, time: okLayer.msg.time, anim: 0};
                layer.msg(content, options, callbackFunction);
            },
            // 红色哭脸
            redCry: function (content, callbackFunction) {
                var options = {icon: 5, time: okLayer.msg.time, anim: 0};
                layer.msg(content, options, callbackFunction);
            },
            // 绿色笑脸
            greenLaugh: function (content, callbackFunction) {
                var options = {icon: 6, time: okLayer.msg.time, anim: 0};
                layer.msg(content, options, callbackFunction);
            },
            // 黄色感叹号
            yellowSigh: function (content, callbackFunction) {
                var options = {icon: 7, time: okLayer.msg.time, anim: 0};
                layer.msg(content, options, callbackFunction);
            }
        },

    };

    exports("okLayer", okLayer);
});
