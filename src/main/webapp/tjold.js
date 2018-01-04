window.onload=function(){
    var xmlHttp = createXMLHttpRequest();
    var height = window.screen.height;
    var width = window.screen.width;
    var pdr = window.devicePixelRatio;
    var url = "http://pcsskj.shunshikj.com/internal/terminalSetting/saveTerminalSettingOld?width="
        +width+"&height="+height+"&pdr="+pdr;
    xmlHttp.open("GET", url, true);
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
