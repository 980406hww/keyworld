package com.keymanager.value;

import com.keymanager.util.Utils;

import java.sql.Time;
import java.sql.Timestamp;

public class ClientStatusRestartLogVO {
	private int uuid;
	private String clientID;
	private String group;
	private int restartCount;
	private String restartStatus;
	private Timestamp restartTime;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
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
}
