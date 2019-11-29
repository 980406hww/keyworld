package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 全站分组标签表
 */
@TableName(value = "t_qz_category_tag")
public class QZCategoryTag extends BaseEntity {

    /**
     * 全站ID
     */
    @TableField(value = "fQZSettingUuid")
    private Long qzSettingUuid;
    /**
     * 分类标签名
     */
    @TableField(value = "fTagName")
    private String tagName;

    public Long getQzSettingUuid () {
        return qzSettingUuid;
    }

    public void setQzSettingUuid (Long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public String getTagName () {
        return tagName;
    }

    public void setTagName (String tagName) {
        this.tagName = tagName;
    }
}
