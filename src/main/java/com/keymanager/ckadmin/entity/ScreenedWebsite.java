package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.keymanager.monitoring.entity.BaseEntity;

@TableName(value = "t_screened_website")
public class ScreenedWebsite extends BaseEntity {

    @TableField(value = "fOptimizeGroupName")
    private String optimizeGroupName;

    @TableField(value = "fScreenedWebsite")
    private String screenedWebsite;

    public String getOptimizeGroupName() {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName(String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }

    public String getScreenedWebsite() {
        return screenedWebsite;
    }

    public void setScreenedWebsite(String screenedWebsite) {
        this.screenedWebsite = screenedWebsite;
    }
}
