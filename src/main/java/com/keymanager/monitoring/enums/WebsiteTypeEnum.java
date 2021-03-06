package com.keymanager.monitoring.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wjianwu 2019/6/13 17:41
 */
public enum WebsiteTypeEnum {

    ASO("ASO", "ASO"),
    Amazon("Amazon", "亚马逊"),
    Negative("Negative", "负面处理"),
    Station("Station", "整站排名"),
    CustomerSite("CustomerSite", "客户站点"),
    InformationSite("InformationSite", "资讯站点");

    private String desc;
    private String value;

    WebsiteTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static Map<String, String> changeToMap() {
        Map<String, String> map = new LinkedHashMap<>();
        WebsiteTypeEnum[] websiteTypeEnums = WebsiteTypeEnum.values();
        for (WebsiteTypeEnum websiteTypeEnum : websiteTypeEnums) {
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
