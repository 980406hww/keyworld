package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_client_upgrade")
public class ClientUpgrade extends BaseEntity {

    @TableField(value = "fTerminalType")
    private String terminalType;

    @TableField(value = "fProgramType")
    private String programType;

    @TableField(value = "fVersion")
    private String version;

    @TableField(value = "fTargetVersion")
    private String targetVersion;

    @TableField(value = "fMaxUpgradeCount")
    private Integer maxUpgradeCount;

    @TableField(value = "fResidualUpgradeCount")
    private Integer residualUpgradeCount;

    @TableField(value = "fStatus")
    private Boolean status;

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
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

    public Integer getResidualUpgradeCount() {
        return residualUpgradeCount;
    }

    public void setResidualUpgradeCount(Integer residualUpgradeCount) {
        this.residualUpgradeCount = residualUpgradeCount;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
