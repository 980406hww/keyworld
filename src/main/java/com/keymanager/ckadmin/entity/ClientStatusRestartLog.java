package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;

@TableName(value = "t_client_status_restart_log")
public class ClientStatusRestartLog {
    @TableId(value = "fUuid", type= IdType.AUTO)
    private Long uuid;

    @TableField(value = "fClientID")
    private String clientID;

    @TableField(value = "fGroup")
    private String group;

    @TableField(value = "fRestartStatus")
    private String restartStatus;

    @TableField(value = "fRestartCount")
    private int restartCount;

    @TableField(value = "fRestartTime")
    private Date restartTime;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
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

    public String getRestartStatus() {
        return restartStatus;
    }

    public void setRestartStatus(String restartStatus) {
        this.restartStatus = restartStatus;
    }

    public int getRestartCount() {
        return restartCount;
    }

    public void setRestartCount(int restartCount) {
        this.restartCount = restartCount;
    }

    public Date getRestartTime() {
        return restartTime;
    }

    public void setRestartTime(Date restartTime) {
        this.restartTime = restartTime;
    }
}
