package com.keymanager.monitoring.enums;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lhc 2019/7/22 11:41
 *  所属行业类型 枚举类
 */
public enum IndustryTypeEnum {

   /* ASO("ASO", "ASO"),
    Amazon("Amazon", "亚马逊"),
    Negative("Negative", "负面处理"),
    Station("Station", "整站排名"),
    CustomerSite("CustomerSite", "客户站点"),
    InformationSite("InformationSite", "资讯站点");
*/


    SEOExtension("SEOExtension","SEO推广项目"),
    BrandRelationsAndSentiment("BrandRelationsAndSentiment","品牌公关舆情项目"),
    CustomerSite("CustomerSite", "客户网站项目"),
    Game("Game","游戏类项目"),
    Finance("Finance","金融类项目"),
    RealEstate("RealEstate","房地产项目"),
    Car("Car","汽车项目"),
    Renovation("Renovation","装修项目"),
    Information("Information","资讯类项目"),
    Others("Others","其他");


    private String desc;
    private String value;

    IndustryTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static Map<String, String> changeToMap() {
        Map<String, String> map = new LinkedHashMap<>();
        IndustryTypeEnum[] websiteTypeEnums = IndustryTypeEnum.values();
        for (IndustryTypeEnum websiteTypeEnum : websiteTypeEnums) {
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
