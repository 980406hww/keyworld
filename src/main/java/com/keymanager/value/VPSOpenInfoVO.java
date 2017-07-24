package com.keymanager.value;


import java.io.Serializable;
import java.sql.Timestamp;

public class VPSOpenInfoVO implements Serializable {
	private int uuid;
	private int vpsProviderInfoId;
	private VPSProviderInfoVO providerInfoVO;
	private String operationType;
	private Integer currentSequence;
	private String vncHost;
	private String vncPort;
	private String vncUser;
	private String vncPassword;
	private String connUser;
	private String connPassword;
	private String openStatus;
	private Timestamp startOpenTime;
	private Timestamp createTime;
	private Timestamp completeOpenTime;
	private String settingStatus;
	private Timestamp startSettingTime;
	private Timestamp completeSettingTime;
	private String vpsUuid;
	private int status;
	private String trival;

	public String formatProviderInfo(){
		return this.getProviderInfoVO().format();
	}

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public int getVpsProviderInfoId() {
		return vpsProviderInfoId;
	}

	public void setVpsProviderInfoId(int vpsProviderInfoId) {
		this.vpsProviderInfoId = vpsProviderInfoId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public int getCurrentSequence() {
		return currentSequence;
	}

	public void setCurrentSequence(Integer currentSequence) {
		this.currentSequence = currentSequence;
	}

	public String getVncHost() {
		return vncHost;
	}

	public void setVncHost(String vncHost) {
		this.vncHost = vncHost;
	}

	public String getVncPort() {
		return vncPort;
	}

	public void setVncPort(String vncPort) {
		this.vncPort = vncPort;
	}

	public String getVncUser() {
		return vncUser;
	}

	public void setVncUser(String vncUser) {
		this.vncUser = vncUser;
	}

	public String getVncPassword() {
		return vncPassword;
	}

	public void setVncPassword(String vncPassword) {
		this.vncPassword = vncPassword;
	}

	public String getConnUser() {
		return connUser;
	}

	public void setConnUser(String connUser) {
		this.connUser = connUser;
	}

	public String getConnPassword() {
		return connPassword;
	}

	public void setConnPassword(String connPassword) {
		this.connPassword = connPassword;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	public Timestamp getStartOpenTime() {
		return startOpenTime;
	}

	public void setStartOpenTime(Timestamp startOpenTime) {
		this.startOpenTime = startOpenTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getSettingStatus() {
		return settingStatus;
	}

	public void setSettingStatus(String settingStatus) {
		this.settingStatus = settingStatus;
	}

	public VPSProviderInfoVO getProviderInfoVO() {
		return providerInfoVO;
	}

	public void setProviderInfoVO(VPSProviderInfoVO providerInfoVO) {
		this.providerInfoVO = providerInfoVO;
	}

	public Timestamp getCompleteOpenTime() {
		return completeOpenTime;
	}

	public void setCompleteOpenTime(Timestamp completeOpenTime) {
		this.completeOpenTime = completeOpenTime;
	}

	public String getVpsUuid() {
		return vpsUuid;
	}

	public void setVpsUuid(String vpsUuid) {
		this.vpsUuid = vpsUuid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTrival() {
		return trival;
	}

	public void setTrival(String trival) {
		this.trival = trival;
	}

	public Timestamp getStartSettingTime() {
		return startSettingTime;
	}

	public void setStartSettingTime(Timestamp startSettingTime) {
		this.startSettingTime = startSettingTime;
	}

	public Timestamp getCompleteSettingTime() {
		return completeSettingTime;
	}

	public void setCompleteSettingTime(Timestamp completeSettingTime) {
		this.completeSettingTime = completeSettingTime;
	}
}
