package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;


/**
 * Created by shunshikj22 on 2017/9/5.
 */
@TableName(value = "t_apply_info")
public class ApplyInfo {

    private static final long serialVersionUID = 3922222059082125030L;

    @TableId(value = "fUuid", type= IdType.AUTO)
    private Long uuid;

    @TableField(value = "fAppName")
    private String appName;

    @TableField(value = "fPackageName")
    private String packageName;

    @TableField(value = "fID")
    private String id;

    @TableField(value = "fColor")
    private String color;

    @TableField(value = "fPosandcolor")
    private String posandcolor;

    @TableField(value = "fCreateTime")
    private Date createTime;

    @TableField(exist = false)
    private String[] keywords;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPosandcolor() {
        return posandcolor;
    }

    public void setPosandcolor(String posandcolor) {
        this.posandcolor = posandcolor;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }
}
