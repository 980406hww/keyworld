package com.keymanager.util;

import com.keymanager.monitoring.enums.EntryTypeEnum;

import java.util.HashSet;
import java.util.Set;


public class EntryUtils {
	static Set<String> entries = new HashSet<String>();
	static {
		entries.add(EntryTypeEnum.fm.name());
		entries.add(EntryTypeEnum.bc.name());
		entries.add(EntryTypeEnum.qz.name());
		entries.add(EntryTypeEnum.pt.name());
	}
	public static boolean validate(String entry){
		return entries.contains(entry);
	}
}
