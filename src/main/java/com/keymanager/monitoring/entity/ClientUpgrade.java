package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_client_upgrade")
public class ClientUpgrade extends BaseEntity {

    @TableField(value = "fType")
    private String type;

    @TableField(value = "fVersion")
    private String version;

    @TableField(value = "fTargetVersion")
    private String targetVersion;

    @TableField(value = "fMaxUpgradeCount")
    private Integer maxUpgradeCount;

    @TableField(value = "fStatus")
    private Boolean status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public Integer getMaxUpgradeCount() {
        return maxUpgradeCount;
    }

    public void setMaxUpgradeCount(Integer maxUpgradeCount) {
        this.maxUpgradeCount = maxUpgradeCount;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
