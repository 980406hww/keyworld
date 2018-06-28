package com.keymanager.util;

import com.keymanager.monitoring.enums.TerminalTypeEnum;

import javax.servlet.http.HttpServletRequest;


public class TerminalTypeMapping {

    public static String getTerminalType(HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        if(url.indexOf("http://pcsskj") == 0 || url.indexOf(":8088") > 0 || url.indexOf(":8098") > 0){
            return TerminalTypeEnum.PC.name();
        }else if(url.indexOf("http://msskj") == 0 ||  url.indexOf(":8089") > 0 || url.indexOf(":8099") > 0){
            return TerminalTypeEnum.Phone.name();
        }
        return null;
    }
}