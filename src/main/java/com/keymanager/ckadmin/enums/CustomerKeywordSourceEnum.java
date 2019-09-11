package com.keymanager.ckadmin.enums;

import java.util.HashMap;
import java.util.Map;

public enum CustomerKeywordSourceEnum {
    Excel("表格上传词", "Excel"),
    UI("页面添加词", "UI"),
    Specify("全站指定词", "Specify"),
    Capture("机器采集词", "Capture"),
    Plugin("插件添加词", "Plugin");

    /** 描述 */
    private String desc;
    /** 枚举值 */
    private String value;

    private CustomerKeywordSourceEnum(String desc, String value) {
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

    public static CustomerKeywordSourceEnum getEnum(String value) {
        CustomerKeywordSourceEnum resultEnum = null;
        CustomerKeywordSourceEnum[] enumAry = CustomerKeywordSourceEnum.values();
        for (int i = 0; i < enumAry.length; i++) {
            if (enumAry[i].getValue().equals(value)) {
                resultEnum = enumAry[i];
                break;
            }
        }
        return resultEnum;
    }

    public static Map<String, Object> toMap() {
        CustomerKeywordSourceEnum[] ary = CustomerKeywordSourceEnum.values();
        Map<String, Object> enumMap = new HashMap<String, Object>();
        for (int num = 0; num < ary.length; num++) {
            enumMap.put(ary[num].getDesc(), String.valueOf(getEnum(ary[num].getValue())));
        }
        return enumMap;
    }
}
