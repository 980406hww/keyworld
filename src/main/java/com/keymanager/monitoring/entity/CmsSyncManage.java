package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName(value = "cms_sync_manage")
public class CmsSyncManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type= IdType.AUTO)
    private Long id;

    @TableField(value = "USER_ID")
    private Long userId;

    @TableField(value = "COMPANY_CODE")
    private String companyCode;

    @TableField(value = "TYPE")
    private String type;

    @TableField(value = "SEARCH_ENGINE")
    private String searchEngine;

    @TableField(value = "SYNC_OPERA_STATUS_TIME")
    private String syncOperaStatusTime;

    @TableField(value = "SYNC_STATUS_TIME")
    private String syncStatusTime;

    @TableField(value = "SYNC_POSITION_TIME")
    private String syncPositionTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getSyncOperaStatusTime() {
        return syncOperaStatusTime;
    }

    public void setSyncOperaStatusTime(String syncOperaStatusTime) {
        this.syncOperaStatusTime = syncOperaStatusTime;
    }

    public String getSyncStatusTime() {
        return syncStatusTime;
    }

    public void setSyncStatusTime(String syncStatusTime) {
        this.syncStatusTime = syncStatusTime;
    }

    public String getSyncPositionTime() {
        return syncPositionTime;
    }

    public void setSyncPositionTime(String syncPositionTime) {
        this.syncPositionTime = syncPositionTime;
    }
}
