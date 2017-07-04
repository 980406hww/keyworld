package com.keymanager.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class PortTerminalTypeMapping {
	static Map<String, String> portTerminalTypeMap = new HashMap<String, String>();
	static {
		portTerminalTypeMap.put("8088", "PC");
		portTerminalTypeMap.put("8089", "Phone");
	}

	public static String getTerminalType(int portNumber){
		String terminalType = portTerminalTypeMap.get(portNumber + "");
		return terminalType;
	}
}
