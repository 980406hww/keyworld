package com.keymanager.monitoring.enums;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName CustomerKeywordStautsEnum
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/22 16:40
 * @Version 1.0
 */
public enum  CustomerKeywordStautsEnum {
    Stop("暂不操作",0,"red"),
    Active("激活",1,"green"),
    New("新增",2,"blue"),
    PullOffShelves("下架",3,"grey");

    private String desc;
    private Integer value;
    private String color;

    CustomerKeywordStautsEnum(String desc, Integer value,String color) {
        this.desc = desc;
        this.value = value;
        this.color = color;
    }

    public static Map<Integer, Map<String,String>> changeToMap() {
        Map<Integer, Map<String,String>> map = new LinkedHashMap<>();

        CustomerKeywordStautsEnum[] customerKeywordStautsEnums = CustomerKeywordStautsEnum.values();
        for (CustomerKeywordStautsEnum customerKeywordStautsEnum : customerKeywordStautsEnums) {
            Map<String,String> data= new HashMap<>();
            data.put("desc",customerKeywordStautsEnum.getDesc());
            data.put("color",customerKeywordStautsEnum.getColor());
            map.put(customerKeywordStautsEnum.getValue(), data);
        }
        return map;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
