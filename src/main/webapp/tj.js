(function(){
    var bp = document.createElement('script');
    var height = window.screen.height;
    var width = window.screen.width;
    var pdr = window.devicePixelRatio;
    var url = "http://pcsskj2.shunshikj.com/internal/terminalSetting/saveTerminalSetting?width="
        +width+"&height="+height+"&pdr="+pdr;
    bp.src = url;
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(bp, s);
})();