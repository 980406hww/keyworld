package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 行业详情表
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
@TableName("t_industry_detail")
public class IndustryDetail extends BaseEntity {

    /**
     * 行业ID
     */
    @TableField(value = "fIndustryID")
    private Long industryID;
    /**
     * 网站域名
     */
    @TableField(value = "fWebsite")
    private String website;
    /**
     * 标题
     */
    @TableField(value = "fTitle")
    private String title;
    /**
     * QQ
     */
    @TableField(value = "fQQ")
    private String qq;
    /**
     * 电话
     */
    @TableField(value = "fTelephone")
    private String telephone;
    /**
     * 权重
     */
    @TableField(value = "fWeight")
    private Integer weight;
    /**
     * 客户标注
     */
    @TableField(value = "fIdentifyCustomer")
    private String identifyCustomer;
    /**
     * 销售备注
     */
    @TableField(value = "fRemark")
    private String remark;
    /**
     * 层级
     */
    @TableField(value = "fLevel")
    private Integer level;

    public Long getIndustryID () {
        return industryID;
    }

    public void setIndustryID (Long industryID) {
        this.industryID = industryID;
    }

    public String getWebsite () {
        return website;
    }

    public void setWebsite (String website) {
        this.website = website;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getTelephone () {
        return telephone;
    }

    public void setTelephone (String telephone) {
        this.telephone = telephone;
    }

    public Integer getWeight () {
        return weight;
    }

    public void setWeight (Integer weight) {
        this.weight = weight;
    }

    public String getIdentifyCustomer() {
        return identifyCustomer;
    }

    public void setIdentifyCustomer(String identifyCustomer) {
        this.identifyCustomer = identifyCustomer;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public Integer getLevel () {
        return level;
    }

    public void setLevel (Integer level) {
        this.level = level;
    }
}
