package com.keymanager.monitoring.enums;

import java.util.HashMap;
import java.util.Map;

public enum WebsiteSynchronousSignEnum {
    Normal("Normal", "正常更新"),
    RequestException("RequestException", "请求异常");

    private String value;
    private String desc;

    WebsiteSynchronousSignEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static Map<String, String> changeToMap() {
        Map<String, String> map = new HashMap<>();
        WebsiteSynchronousSignEnum[] websiteTypeEnums = WebsiteSynchronousSignEnum.values();
        for (WebsiteSynchronousSignEnum websiteTypeEnum : websiteTypeEnums) {
            map.put(websiteTypeEnum.getValue(), websiteTypeEnum.getDesc());
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
