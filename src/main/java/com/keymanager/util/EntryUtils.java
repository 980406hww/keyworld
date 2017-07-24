package com.keymanager.util;

import java.util.HashSet;
import java.util.Set;


public class EntryUtils {
	static Set<String> entries = new HashSet<String>();
	static {
		entries.add("fm");
		entries.add("bc");
		entries.add("qz");
		entries.add("pt");
	}
	public static boolean validate(String entry){
		return entries.contains(entry);
	}
}
