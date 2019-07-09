package com.keymanager.monitoring.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wjianwu 2019/7/1 15:12
 */
public enum RankJobAreaEnum {

    China("China", "中国"),
    America("America", "美国"),
    Japan("Japan", "日本"),
    Other("Other", "其他");

    private String desc;
    private String value;

    RankJobAreaEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static Map<String, String> changeToMap() {
        Map<String, String> map = new HashMap<>();
        RankJobAreaEnum[] rankJobAreaEnums = RankJobAreaEnum.values();
        for (RankJobAreaEnum rankJobAreaEnum : rankJobAreaEnums) {
            map.put(rankJobAreaEnum.getValue(), rankJobAreaEnum.getDesc());
        }
        return map;
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
}
