package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源
 */
@TableName(value = "t_resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long id;

    @NotBlank
    @TableField(value = "fResourceName")
    private String resourceName;
    /**
     * 资源名称
     */

    @TableField(value = "fUrl")
    private String url;
    /**
     * 资源路径
     */

    @TableField(value = "fOpenMode")
    private String openMode;
    /**
     * 打开方式 ajax,iframe
     */

    @TableField(value = "fDescription")
    private String description;
    /**
     * 资源介绍
     */

    @TableField(value = "fIconCls")
    @JsonProperty("iconCls")
    private String icon;
    /**
     * 资源图标
     */

    @TableField(value = "fParentID")
    private Long parentID;
    /**
     * 父级资源id
     */

    @TableField(value = "fSequence")
    private Integer sequence;
    /**
     * 排序
     */

    @TableField(value = "fStatus")
    private Integer status;
    /**
     * 状态
     */

    @TableField(value = "fOpened")
    private Integer opened;
    /**
     * 打开的
     */

    @TableField(value = "fResourceType")
    private Integer resourceType;
    /**
     * 资源类别
     */

    @TableField(value = "fUpdateTime")
    private Date updateTime;

    @TableField(value = "fCreateTime")
    private Date createTime;

    @TableField(value = "fVersion")
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOpenMode() {
        return openMode;
    }

    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOpened() {
        return opened;
    }

    public void setOpened(Integer opened) {
        this.opened = opened;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
