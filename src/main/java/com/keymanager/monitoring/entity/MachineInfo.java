package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.keymanager.util.Utils;
import org.hibernate.validator.constraints.NotBlank;

import java.sql.Timestamp;

@TableName(value = "t_machine_info")
public class MachineInfo {

	private static final long serialVersionUID = -7590694637780491359L;
	@NotBlank
	@TableId(value = "fClientID")
	private String clientID;

	@TableField(value = "fTerminalType")
	private String terminalType;

	@TableField(value = "fClientIDPrefix")
	private String clientIDPrefix;

	@TableField(value = "fVersion")
	private String version;

	@TableField(value = "fTargetVersion")
	private String targetVersion;

	@TableField(value = "fGroup", strategy = FieldStrategy.IGNORED)
	private String group;

	@TableField(value = "fUsingOperationType")
	private String usingOperationType;

	@TableField(value = "fPage")
	private int page;

	@TableField(value = "fPageSize")
	private Integer pageSize;

	@TableField(value = "fContinuousFailCount")
	private int continuousFailCount;

	@TableField(value = "fCity")
	private String city;

	@TableField(value = "fAllowSwitchGroup")
	private int allowSwitchGroup;

	@TableField(value = "fSwitchGroupName")
	private String switchGroupName;

	@TableField(value = "fHost")
	private String host;

	@TableField(value = "fPort")
	private String port;

	@TableField(value = "fUserName")
	private String userName;

	@TableField(value = "fPassword")
	private String password;

	@TableField(value = "fBroadbandAccount")
	private String broadbandAccount;

	@TableField(value = "fBroadbandPassword")
	private String broadbandPassword;

	@TableField(value = "fFreeSpace")
	private Double freeSpace;

	@TableField(value = "fTenMinsLastVisitTime")
	private Timestamp tenMinsLastVisitTime;

	@TableField(value = "fLastVisitTime")
	private Timestamp lastVisitTime;

	@TableField(value = "fLastSendNotificationTime")
	private Timestamp lastSendNotificationTime;

	@TableField(value = "fRestartCount")
	private int restartCount;

	@TableField(value = "fRestartStatus")
	private String restartStatus;

	@TableField(value = "fRestartTime")
	private Timestamp restartTime;

	@TableField(value = "fThreeMinsRestartTime")
	private Timestamp threeMinsRestartTime;

	@TableField(value = "fTenMinsRestartTime")
	private Timestamp tenMinsRestartTime;

	@TableField(value = "fVPSBackendSystemComputerID")
	private String vpsBackendSystemComputerID;

	@TableField(value = "fVPSBackendSystemPassword")
	private String vpsBackendSystemPassword;

	@TableField(value = "fRestartOrderingTime")
	private Timestamp restartOrderingTime;

	@TableField(value = "fOptimizationStartDate")
	private Timestamp optimizationStartDate;

	@TableField(value = "fOptimizationTotalCount")
	private int optimizationTotalCount;

	@TableField(value = "fOptimizationSucceedCount")
	private int optimizationSucceedCount;

	@TableField(value = "fRenewalDate")
	private Timestamp renewalDate;

	@TableField(value = "fUpgradeFailedReason", strategy = FieldStrategy.IGNORED)
	private String upgradeFailedReason;

	@TableField(value = "fStatus")
	private String status;

	@TableField(value = "fValid")
	private boolean valid;

	@TableField(value = "fStartUpTime")
	private Timestamp startUpTime;

	@TableField(value = "fStartUpStatus")
	private String startUpStatus;

	@TableField(value = "fDownloadProgramType")
	private String downloadProgramType;

	@TableField(value = "fRunningProgramType")
	private String runningProgramType;

	@TableField(value = "fTargetVPSPassword")
	private String targetVPSPassword;

	@TableField(value = "fIdleStartTime")
	private Timestamp idleStartTime;

	@TableField(value = "fIdleTotalMinutes")
	private long idleTotalMinutes;

	@TableField(value = "fUpdateSettingTime")
	private Timestamp updateSettingTime;

	@TableField(value = "fCreateTime")
	private Timestamp createTime;

    @TableField(value = "fPageNo")
    private int pageNo;

	@TableField(value = "fRemainingKeyword")
	private int remainingKeyword;

    @TableField(exist=false)
    private boolean red;

    @TableField(exist=false)
    private boolean yellow;

    @TableField(exist=false)
    private boolean orange;
	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getClientIDPrefix() {
		return clientIDPrefix;
	}

	public void setClientIDPrefix(String clientIDPrefix) {
		this.clientIDPrefix = clientIDPrefix;
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


	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUsingOperationType () {
		return usingOperationType;
	}

	public void setUsingOperationType (String usingOperationType) {
		this.usingOperationType = usingOperationType;
	}

	public int getContinuousFailCount() {
		return continuousFailCount;
	}

	public void setContinuousFailCount(int continuousFailCount) {
		this.continuousFailCount = continuousFailCount;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getAllowSwitchGroup() {
		return allowSwitchGroup;
	}

	public void setAllowSwitchGroup(int allowSwitchGroup) {
		this.allowSwitchGroup = allowSwitchGroup;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Double getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(Double freeSpace) {
		this.freeSpace = freeSpace;
	}

	public Timestamp getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Timestamp lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public Timestamp getLastSendNotificationTime() {
		return lastSendNotificationTime;
	}

	public void setLastSendNotificationTime(Timestamp lastSendNotificationTime) {
		this.lastSendNotificationTime = lastSendNotificationTime;
	}

	public int getRestartCount() {
		return restartCount;
	}

	public void setRestartCount(int restartCount) {
		this.restartCount = restartCount;
	}

	public String getRestartStatus() {
		return restartStatus;
	}

	public void setRestartStatus(String restartStatus) {
		this.restartStatus = restartStatus;
	}

	public Timestamp getRestartTime() {
		return restartTime;
	}

	public void setRestartTime(Timestamp restartTime) {
		this.restartTime = restartTime;
	}

	public String getVpsBackendSystemComputerID() {
		return vpsBackendSystemComputerID;
	}

	public void setVpsBackendSystemComputerID(String vpsBackendSystemComputerID) {
		this.vpsBackendSystemComputerID = vpsBackendSystemComputerID;
	}

	public String getVpsBackendSystemPassword() {
		return vpsBackendSystemPassword;
	}

	public void setVpsBackendSystemPassword(String vpsBackendSystemPassword) {
		this.vpsBackendSystemPassword = vpsBackendSystemPassword;
	}

	public Timestamp getRestartOrderingTime() {
		return restartOrderingTime;
	}

	public void setRestartOrderingTime(Timestamp restartOrderingTime) {
		this.restartOrderingTime = restartOrderingTime;
	}

	public Timestamp getOptimizationStartDate() {
		return optimizationStartDate;
	}

	public void setOptimizationStartDate(Timestamp optimizationStartDate) {
		this.optimizationStartDate = optimizationStartDate;
	}

	public int getOptimizationTotalCount() {
		return optimizationTotalCount;
	}

	public void setOptimizationTotalCount(int optimizationTotalCount) {
		this.optimizationTotalCount = optimizationTotalCount;
	}

	public int getOptimizationSucceedCount() {
		return optimizationSucceedCount;
	}

	public void setOptimizationSucceedCount(int optimizationSucceedCount) {
		this.optimizationSucceedCount = optimizationSucceedCount;
	}

	public Timestamp getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Timestamp renewalDate) {
		this.renewalDate = renewalDate;
	}

	public String getUpgradeFailedReason() {
		return upgradeFailedReason;
	}

	public void setUpgradeFailedReason(String upgradeFailedReason) {
		this.upgradeFailedReason = upgradeFailedReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getBroadbandAccount() {
		return broadbandAccount;
	}

	public void setBroadbandAccount(String broadbandAccount) {
		this.broadbandAccount = broadbandAccount;
	}

	public String getBroadbandPassword() {
		return broadbandPassword;
	}

	public void setBroadbandPassword(String broadbandPassword) {
		this.broadbandPassword = broadbandPassword;
	}

	public String getSwitchGroupName() {
		return switchGroupName;
	}

	public void setSwitchGroupName(String switchGroupName) {
		this.switchGroupName = switchGroupName;
	}

	public Timestamp getTenMinsLastVisitTime() {
		return tenMinsLastVisitTime;
	}

	public void setTenMinsLastVisitTime(Timestamp tenMinsLastVisitTime) {
		this.tenMinsLastVisitTime = tenMinsLastVisitTime;
	}

	public Timestamp getThreeMinsRestartTime() {
		return threeMinsRestartTime;
	}

	public void setThreeMinsRestartTime(Timestamp threeMinsRestartTime) {
		this.threeMinsRestartTime = threeMinsRestartTime;
	}

	public Timestamp getTenMinsRestartTime() {
		return tenMinsRestartTime;
	}

	public void setTenMinsRestartTime(Timestamp tenMinsRestartTime) {
		this.tenMinsRestartTime = tenMinsRestartTime;
	}

	public Timestamp getStartUpTime() {
		return startUpTime;
	}

	public void setStartUpTime(Timestamp startUpTime) {
		this.startUpTime = startUpTime;
	}

	public String getStartUpStatus() {
		return startUpStatus;
	}

	public void setStartUpStatus(String startUpStatus) {
		this.startUpStatus = startUpStatus;
	}

	public String getDownloadProgramType() {
		return downloadProgramType;
	}

	public void setDownloadProgramType(String downloadProgramType) {
		this.downloadProgramType = downloadProgramType;
	}

	public String getRunningProgramType() {
		return runningProgramType;
	}

	public void setRunningProgramType(String runningProgramType) {
		this.runningProgramType = runningProgramType;
	}

	public String getTargetVPSPassword() {
		return targetVPSPassword;
	}

	public void setTargetVPSPassword(String targetVPSPassword) {
		this.targetVPSPassword = targetVPSPassword;
	}

	public Timestamp getIdleStartTime() {
		return idleStartTime;
	}

	public void setIdleStartTime(Timestamp idleStartTime) {
		this.idleStartTime = idleStartTime;
	}

	public long getIdleTotalMinutes() {
		return idleTotalMinutes;
	}

	public void setIdleTotalMinutes(long idleTotalMinutes) {
		this.idleTotalMinutes = idleTotalMinutes;
	}

	public Timestamp getUpdateSettingTime() {
		return updateSettingTime;
	}

	public void setUpdateSettingTime(Timestamp updateSettingTime) {
		this.updateSettingTime = updateSettingTime;
	}

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public boolean getRed(){
        return (Utils.addMinutes(this.lastVisitTime, (10 > (this.getPageNo() * 3) ? (10 + 5) : (this.getPageNo() * 3 + 5)))
                .compareTo(Utils.getCurrentTimestamp()) < 0);
    }

    public boolean getYellow(){
        Timestamp time = Utils.addMinutes(this.lastVisitTime, (10 > (this.getPageNo() * 3) ? 10 : (this.getPageNo() * 3)));
        return time.compareTo(Utils.getCurrentTimestamp()) < 0 && (Utils.addMinutes(time, 5)).compareTo(Utils.getCurrentTimestamp()) > 0;
    }

    public boolean getOrange() {
        return this.getContinuousFailCount() > 5;
    }

    public void setOrange(boolean orange) {
        this.orange = orange;
    }

    public void setRed(boolean red) {
        this.red = red;
    }

    public void setYellow(boolean yellow) {
        this.yellow = yellow;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

	public int getRemainingKeyword () {
		return remainingKeyword;
	}

	public void setRemainingKeyword (int remainingKeyword) {
		this.remainingKeyword = remainingKeyword;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
