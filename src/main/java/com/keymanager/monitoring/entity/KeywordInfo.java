package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

@TableName(value = "t_keyword_info")
public class KeywordInfo {

	private static final long serialVersionUID = 3922222059082125030L;

	@TableId(value = "fId", type= IdType.AUTO)
	private Long id;

	@TableField(value = "fUserName")
	private String userName;

	@TableField(value = "fSpliterStr")
	private String spliterStr;

	@TableField(value = "fSearchEngine")
	private String searchEngine;

	@TableField(value = "fOperationType")
	private String operationType;

	@TableField(value = "fKeywordInfo")
	private String keywordInfo;

	@TableField(value = "fStatus")
	private String status;

	@TableField(value = "fUpdateTime")
	private Date updateTime;

	@TableField(value = "fCreateTime")
	private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSpliterStr() {
        return spliterStr;
    }

    public void setSpliterStr(String spliterStr) {
        this.spliterStr = spliterStr;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getKeywordInfo() {
        return keywordInfo;
    }

    public void setKeywordInfo(String keywordInfo) {
        this.keywordInfo = keywordInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
