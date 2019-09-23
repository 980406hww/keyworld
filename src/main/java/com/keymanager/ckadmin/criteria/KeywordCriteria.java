package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;
import java.util.List;

/**
 * @ClassName KeywordCriteria
 * @Description 关键字条件类
 * @Author lhc
 * @Date 2019/9/20 10:36
 * @Version 1.0
 */
public class KeywordCriteria extends BaseCriteria {
    private List<Long> uuids;
    private Long customerUuid;
    private String keyword;
    private String userName;
    private String url;//链接
    private String bearPawNumber;//熊掌号
    private String optimizeGroupName;//优化组名
    private String machineGroup;//机器分组
    private String remarks;//备注
    private String failedCause; // 失败原因
    private String terminalType;
    private String searchEngine;
    private String customerKeywordSource;//关键字来源
    private String status;
    private Integer gtOptimizePlanCount;//要刷起始
    private Integer ltOptimizePlanCount;//要刷结束
    private Integer gtOptimizedCount;//已刷起始
    private Integer ltOptimizedCount;//已刷结束
    private Integer gtPosition;//排名起始
    private Integer ltPosition;//排名结束
    private String noPosition;//显示排名为0
    private Integer invalidRefreshCount;//无效点击量
    private String gtCreateTime;//创建时间起始
    private String ltCreateTime;//创建时间结束
    private String pushPay;//催缴
    private String requireDelete;//要求删除
    private String type;//词类型

    private String targetOptimizeGroupName;
    private String targetMachineGroup;
    private String targetBearPawNumber;

    private String deleteType;

    public List<Long> getUuids() {
        return uuids;
    }

    public void setUuids(List<Long> uuids) {
        this.uuids = uuids;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    public String getOptimizeGroupName() {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName(String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }

    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFailedCause() {
        return failedCause;
    }

    public void setFailedCause(String failedCause) {
        this.failedCause = failedCause;
    }

    public String getCustomerKeywordSource() {
        return customerKeywordSource;
    }

    public void setCustomerKeywordSource(String customerKeywordSource) {
        this.customerKeywordSource = customerKeywordSource;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTargetOptimizeGroupName() {
        return targetOptimizeGroupName;
    }

    public void setTargetOptimizeGroupName(String targetOptimizeGroupName) {
        this.targetOptimizeGroupName = targetOptimizeGroupName;
    }

    public String getTargetBearPawNumber() {
        return targetBearPawNumber;
    }

    public void setTargetBearPawNumber(String targetBearPawNumber) {
        this.targetBearPawNumber = targetBearPawNumber;
    }

    public String getTargetMachineGroup() {
        return targetMachineGroup;
    }

    public void setTargetMachineGroup(String targetMachineGroup) {
        this.targetMachineGroup = targetMachineGroup;
    }


    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }

    public Integer getGtOptimizePlanCount() {
        return gtOptimizePlanCount;
    }

    public void setGtOptimizePlanCount(Integer gtOptimizePlanCount) {
        this.gtOptimizePlanCount = gtOptimizePlanCount;
    }

    public Integer getLtOptimizePlanCount() {
        return ltOptimizePlanCount;
    }

    public void setLtOptimizePlanCount(Integer ltOptimizePlanCount) {
        this.ltOptimizePlanCount = ltOptimizePlanCount;
    }

    public Integer getGtOptimizedCount() {
        return gtOptimizedCount;
    }

    public void setGtOptimizedCount(Integer gtOptimizedCount) {
        this.gtOptimizedCount = gtOptimizedCount;
    }

    public Integer getLtOptimizedCount() {
        return ltOptimizedCount;
    }

    public void setLtOptimizedCount(Integer ltOptimizedCount) {
        this.ltOptimizedCount = ltOptimizedCount;
    }

    public Integer getGtPosition() {
        return gtPosition;
    }

    public void setGtPosition(Integer gtPosition) {
        this.gtPosition = gtPosition;
    }

    public Integer getLtPosition() {
        return ltPosition;
    }

    public void setLtPosition(Integer ltPosition) {
        this.ltPosition = ltPosition;
    }

    public String getNoPosition() {
        return noPosition;
    }

    public void setNoPosition(String noPosition) {
        this.noPosition = noPosition;
    }

    public Integer getInvalidRefreshCount() {
        return invalidRefreshCount;
    }

    public void setInvalidRefreshCount(Integer invalidRefreshCount) {
        this.invalidRefreshCount = invalidRefreshCount;
    }

    public String getGtCreateTime() {
        return gtCreateTime;
    }

    public void setGtCreateTime(String gtCreateTime) {
        this.gtCreateTime = gtCreateTime;
    }

    public String getLtCreateTime() {
        return ltCreateTime;
    }

    public void setLtCreateTime(String ltCreateTime) {
        this.ltCreateTime = ltCreateTime;
    }

    public String getPushPay() {
        return pushPay;
    }

    public void setPushPay(String pushPay) {
        this.pushPay = pushPay;
    }

    public String getRequireDelete() {
        return requireDelete;
    }

    public void setRequireDelete(String requireDelete) {
        this.requireDelete = requireDelete;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
