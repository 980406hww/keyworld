package com.keymanager.monitoring.enums;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName CustomerKeywordStatusEnum
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/22 16:40
 * @Version 1.0
 */
public enum CustomerKeywordStatusEnum {
    Stop("暂不操作",0,"red"),
    Active("激活",1,"green"),
    New("新增",2,"blue"),
    PullOffShelves("下架",3,"grey");

    private String desc;
    private Integer value;
    private String color;

    CustomerKeywordStatusEnum(String desc, Integer value, String color) {
        this.desc = desc;
        this.value = value;
        this.color = color;
    }

    public static Map<Integer, Map<String,String>> changeToMap() {
        Map<Integer, Map<String,String>> map = new LinkedHashMap<>();

        CustomerKeywordStatusEnum[] customerKeywordStatusEnums = CustomerKeywordStatusEnum.values();
        for (CustomerKeywordStatusEnum customerKeywordStatusEnum : customerKeywordStatusEnums) {
            Map<String,String> data= new HashMap<>();
            data.put("desc", customerKeywordStatusEnum.getDesc());
            data.put("color", customerKeywordStatusEnum.getColor());
            map.put(customerKeywordStatusEnum.getValue(), data);
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
