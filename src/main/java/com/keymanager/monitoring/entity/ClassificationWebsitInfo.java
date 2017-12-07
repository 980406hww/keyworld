package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
/**
 * Created by shunshikj22 on 2017/12/4.
 */
@TableName(value = "t_classification_website_info")
public class ClassificationWebsitInfo {
    @TableId(value = "fUuid", type= IdType.AUTO)
    private Long uuid;

    @TableField(value = "fClassificationUuid")
    private Long classificationUuid;

    @TableField(value = "fUrl")
    private String url;

    @TableField(value = "fTitle")
    private String title;

    @TableField(value = "fEmailAddress")
    private String emailAddress;

    @TableField(value = "fTelPhone")
    private String telPhone;

    @TableField(value = "fHref")
    private String href;

    @TableField(value = "fHasOfficialWebsiteIndicator")
    private Boolean hasOfficialWebsiteIndicator;

    @TableField(value = "fDesc")
    private String desc;

    @TableField(value = "fOrder")
    private Integer order;

    @TableField(value = "fReCollection")
    private Boolean reCollection;

    @TableField(value = "fCreateTime")
    private Date createTime;

    public Boolean getReCollection() {
        return reCollection;
    }

    public void setReCollection(Boolean reCollection) {
        this.reCollection = reCollection;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getClassificationUuid() {
        return classificationUuid;
    }

    public void setClassificationUuid(Long classificationUuid) {
        this.classificationUuid = classificationUuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Boolean getHasOfficialWebsiteIndicator() {
        return hasOfficialWebsiteIndicator;
    }

    public void setHasOfficialWebsiteIndicator(Boolean hasOfficialWebsiteIndicator) {
        this.hasOfficialWebsiteIndicator = hasOfficialWebsiteIndicator;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
