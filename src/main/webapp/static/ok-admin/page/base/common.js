function show_layer_msg(msg, icon, status, title) {
    layer.msg(msg, {
        icon: icon,
        title: title === undefined ? null : title,
        anim: 5,
        time: 2000,
        isOutAnim: false
    }, function () {
        if (status) {
            active['reload'].call(this);
        }
    });
}