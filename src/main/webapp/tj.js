window.onload=function(){
    var xmlHttp = createXMLHttpRequest();
    var height = window.screen.height;
    var width = window.screen.width;
    var pdr = window.devicePixelRatio;
    var screen = {height:height,width:width,pdr:pdr}
    xmlHttp.open('POST','/internal/terminalSetting/saveTerminalSetting', true);
    xmlHttp.setRequestHeader("Content-Type", "application/json");
    xmlHttp.onreadystatechange = function() {
    };
    xmlHttp.send(JSON.stringify(screen));

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



