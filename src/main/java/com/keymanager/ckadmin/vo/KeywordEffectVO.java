package com.keymanager.ckadmin.vo;

/**
 * @ClassName KeywordEffectVO
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/12 10:26
 * @Version 1.0
 */
public class KeywordEffectVO {
    private String desc;
    private String value;
    private Integer level;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public KeywordEffectVO(String desc, String value, Integer level) {
        this.desc = desc;
        this.value = value;
        this.level = level;
    }

    public KeywordEffectVO() {
    }
}
