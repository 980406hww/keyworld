package com.keymanager.util;

import com.keymanager.monitoring.enums.TerminalTypeEnum;

import javax.servlet.http.HttpServletRequest;


public class TerminalTypeMapping {

	public static String getTerminalType(HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		if(url.indexOf("http://pcsskj.") == 0 || url.indexOf("http://pcsskjlocal.") == 0){
			return TerminalTypeEnum.PC.name();
		}else if(url.indexOf("http://msskj.") == 0 || url.indexOf("http://msskjlocal.") == 0){
			return TerminalTypeEnum.Phone.name();
		}
		return null;
	}
}
