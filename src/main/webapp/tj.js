window.onload=function(){
    var xmlHttp = createXMLHttpRequest();
    var height = window.screen.height;
    var width = window.screen.width;
    var pdr = window.devicePixelRatio;
    var url = "pcsskjlocal.shunshikj.com:8088/internal/terminalSetting/saveTerminalSetting?width="
        +width+"&height="+height+"&pdr="+pdr;
    var sstj_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
    xmlHttp.open("GET",sstj_protocol+url,true);
    xmlHttp.send(null);
};

function createXMLHttpRequest() {
    try {
        return new XMLHttpRequest();//大多数浏览器
    } catch (e) {
        try {
            return new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
}
