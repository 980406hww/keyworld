package com.keymanager.monitoring.criteria;

import java.util.Set;

public class MachineInfoCriteria extends BaseCriteria {
    private String clientID; // 客户端ID
    private String groupName; // 优化组
    private String operationType; // 操作类型
    private String noGroup; // 没分组
    private String version; // 版本
    private String targetVersion;
    private String city; // 城市
    private String upgradeFailedReason; // 失败原因
    private String valid; // 状态
    private String hasProblem; // 停了
    private String showFetchKeywordStatus; // 显示取词状态

    private String renewal; // 续费
    private String noVNC; // 没VNC
    private String noUpgrade; // 没升级
    private String noChangePassword;
    private String orderBy; // 排序
    private String vpsBackendSystemComputerID;//服务器ID
    private String switchGroupName;
    private String startUpClient; // 开机机器
    private String hasGroup; // 有分组
    private String startUpStatus; // 开机状态
    private String runningProgramType; // 运行程序类型
    private String hiddenColumns; // 隐藏列号

    private String terminalType;
    private Set<String> switchGroups;

    private boolean haveHiddenColumns; //是否为设置隐藏弹窗提交的



    public boolean getHaveHiddenColumns() {
        return haveHiddenColumns;
    }

    public void setHaveHiddenColumns(boolean haveHiddenColumns) {
        this.haveHiddenColumns = haveHiddenColumns;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOperationType () {
        return operationType;
    }

    public void setOperationType (String operationType) {
        this.operationType = operationType;
    }

    public String getNoGroup() {
        return noGroup;
    }

    public void setNoGroup(String noGroup) {
        this.noGroup = noGroup;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpgradeFailedReason() {
        return upgradeFailedReason;
    }

    public void setUpgradeFailedReason(String upgradeFailedReason) {
        this.upgradeFailedReason = upgradeFailedReason;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getHasProblem() {
        return hasProblem;
    }

    public void setHasProblem(String hasProblem) {
        this.hasProblem = hasProblem;
    }

    public String getShowFetchKeywordStatus() {
        return showFetchKeywordStatus;
    }

    public void setShowFetchKeywordStatus(String showFetchKeywordStatus) {
        this.showFetchKeywordStatus = showFetchKeywordStatus;
    }

    public String getRenewal() {
        return renewal;
    }

    public void setRenewal(String renewal) {
        this.renewal = renewal;
    }

    public String getNoVNC() {
        return noVNC;
    }

    public void setNoVNC(String noVNC) {
        this.noVNC = noVNC;
    }

    public String getNoUpgrade() {
        return noUpgrade;
    }

    public void setNoUpgrade(String noUpgrade) {
        this.noUpgrade = noUpgrade;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getVpsBackendSystemComputerID() {
        return vpsBackendSystemComputerID;
    }

    public void setVpsBackendSystemComputerID(String vpsBackendSystemComputerID) {
        this.vpsBackendSystemComputerID = vpsBackendSystemComputerID;
    }

    public String getSwitchGroupName() {
        return switchGroupName;
    }

    public void setSwitchGroupName(String switchGroupName) {
        this.switchGroupName = switchGroupName;
    }

    public String getStartUpClient() {
        return startUpClient;
    }

    public void setStartUpClient(String startUpClient) {
        this.startUpClient = startUpClient;
    }

    public String getHasGroup() {
        return hasGroup;
    }

    public void setHasGroup(String hasGroup) {
        this.hasGroup = hasGroup;
    }

    public String getStartUpStatus() {
        return startUpStatus;
    }

    public void setStartUpStatus(String startUpStatus) {
        this.startUpStatus = startUpStatus;
    }

    public Set<String> getSwitchGroups() {
        return switchGroups;
    }

    public void setSwitchGroups(Set<String> switchGroups) {
        this.switchGroups = switchGroups;
    }

    public String getRunningProgramType() {
        return runningProgramType;
    }

    public void setRunningProgramType(String runningProgramType) {
        this.runningProgramType = runningProgramType;
    }

    public String getHiddenColumns() {
        return hiddenColumns;
    }

    public void setHiddenColumns(String hiddenColumns) {
        this.hiddenColumns = hiddenColumns;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public String getNoChangePassword() {
        return noChangePassword;
    }

    public void setNoChangePassword(String noChangePassword) {
        this.noChangePassword = noChangePassword;
    }
}
