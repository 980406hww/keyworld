package com.keymanager.ckadmin.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum QZCaptureTitleLogStatusEnum {
	New("未处理", "New"),
	Processing("处理中", "Processing"),
	Completed("完成", "Completed");

	/** 描述 */
	private String desc;
	/** 枚举值 */
	private String value;

	private QZCaptureTitleLogStatusEnum(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static QZCaptureTitleLogStatusEnum getEnum(String value) {
		QZCaptureTitleLogStatusEnum resultEnum = null;
		QZCaptureTitleLogStatusEnum[] enumAry = QZCaptureTitleLogStatusEnum
            .values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList() {
		QZCaptureTitleLogStatusEnum[] ary = QZCaptureTitleLogStatusEnum
            .values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", String.valueOf(ary[i].getValue()));
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		QZCaptureTitleLogStatusEnum[] ary = QZCaptureTitleLogStatusEnum
            .values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = String.valueOf(getEnum(ary[num].getValue()));
			map.put("value", String.valueOf(ary[num].getValue()));
			map.put("desc", ary[num].getDesc());
			enumMap.put(key,map);
		}
		return enumMap;
	}
}
