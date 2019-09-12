package com.keymanager.ckadmin.enums;

import com.keymanager.ckadmin.vo.KeywordEffectVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhc 2019/9/12 9:48
 */
public enum KeywordEffectEnum {

    // 指定词
    Appointment("客户指定词", "Appointment", 2),

    // 曲线词
    Curve("曲线达标词", "Curve", 3),

    // 重点词
    Important("重点词","Important", 1);

    private String desc;
    private String value;

    /**
     * 作用优先级，值越小优先级越高
     */
    private Integer level;

    KeywordEffectEnum(String desc, String value, Integer level) {
        this.desc = desc;
        this.value = value;
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public static List<KeywordEffectVO> toList() {
        KeywordEffectEnum[] ary = KeywordEffectEnum.values();

        List<KeywordEffectVO> list = new ArrayList<>(ary.length);
        for (KeywordEffectEnum keywordEffectEnum : ary) {
            list.add(new KeywordEffectVO(keywordEffectEnum.getDesc(),keywordEffectEnum.getValue(), keywordEffectEnum.getLevel()));
        }
        return list;
    }

    public static Map<String, Integer> toLevelMap() {
        KeywordEffectEnum[] ary = KeywordEffectEnum.values();
        Map<String, Integer> levelMap = new HashMap<>();
        for (KeywordEffectEnum keywordEffectEnum : ary) {
            levelMap.put(keywordEffectEnum.getValue(), keywordEffectEnum.getLevel());
        }
        return levelMap;
    }

}
