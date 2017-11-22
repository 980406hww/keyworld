window.onload=function(){
    var height = window.screen.height;
    var width = window.screen.width;
    var pdr = window.devicePixelRatio;
    var url = "pcsskjlocal.shunshikj.com:8088/internal/terminalSetting/saveTerminalSetting?width="
        +width+"&height="+height+"&pdr="+pdr;
    var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
    $.ajax({
        type: "get",
        async: false,
        url: cnzz_protocol+url,
        success: function(json){

        },
        error: function(){

        }
    });
};
