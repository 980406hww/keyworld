package com.keymanager.monitoring.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wjianwu 2019/6/14 16:45
 */
public enum PutSalesInfoSignEnum {
    Refuse("Refuse", "拒绝访问"),
    Normal("Normal", "正常更新"),
    OperatingFail("OperatingFail", "操作失败"),
    UpdateException("UpdateException", "更新异常"),
    RequestException("RequestException", "请求异常");

    private String value;
    private String desc;

    PutSalesInfoSignEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static Map<String, String> changeToMap() {
        Map<String, String> map = new HashMap<>();
        PutSalesInfoSignEnum[] websiteTypeEnums = PutSalesInfoSignEnum.values();
        for (PutSalesInfoSignEnum websiteTypeEnum : websiteTypeEnums) {
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
