package com.keymanager.util;

import javax.servlet.http.HttpServletRequest;


public class TerminalTypeMapping {

	public static String getTerminalType(HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		if(url.indexOf("http://pc.") == 0){
			return "PC";
		}else if(url.indexOf("http://phone.") == 0){
			return "Phone";
		}
		return null;
	}
}
