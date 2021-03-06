package com.keymanager.monitoring.criteria;

import java.util.List;

public class CustomerKeywordCriteria extends BaseCriteria{
    private List<Long> uuids;
    private Long customerUuid;
    private String url;
    private String excludeUrl; // 需排除的url
    private String bearPawNumber;
    private String keyword;
    private String creationFromTime;//添加时间
    private String creationToTime;
    private String status;
    private String optimizeGroupName;//优化组名
    private String invalidRefreshCount;//无效点击数
    private Integer gtPosition;
    private Integer ltPosition;
    private String noPosition;//显示没有排名
    private String entryType;//
    private String terminalType;//8088 PC  8089  phone
    private String searchEngine;
    private String titleFlag;
    private String keywordEffect;
    private Integer gtOptimizedCount;
    private Integer ltOptimizedCount;
    private Integer gtOptimizePlanCount;
    private Integer ltOptimizePlanCount;
    private Integer gtCurrentIndexCount;
    private Integer ltCurrentIndexCount;

    private String qq;
    private String userName;
    private String pushPay;//催缴
    private String orderNumber;//订单号
    private String remarks;//备注
    private String failedCause; // 失败原因
    private String machineGroup;//机器分组搜索条件

    private String orderingElement;
    private String orderingRule;
    private String targetOptimizeGroupName;
    private String targetMachineGroup;//目标机器分组
    private String targetBearPawNumber;
    private Integer noReachStandardDays;
    private Integer sevenDaysNoReachStandard;
    private Integer fifteenDaysNoReachStandard;
    private Integer thirtyDaysNoReachStandard;
    private Integer openDialogStatus;
    private String customerKeywordSource;
    private String groupNameFuzzyQuery;
    private String machineGroupFuzzyQuery;

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    private Boolean requireDelete;

    public Integer getGtCurrentIndexCount() {
        return gtCurrentIndexCount;
    }

    public void setGtCurrentIndexCount(Integer gtCurrentIndexCount) {
        this.gtCurrentIndexCount = gtCurrentIndexCount;
    }

    public Integer getLtCurrentIndexCount() {
        return ltCurrentIndexCount;
    }

    public void setLtCurrentIndexCount(Integer ltCurrentIndexCount) {
        this.ltCurrentIndexCount = ltCurrentIndexCount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getKeywordEffect() {
        return keywordEffect;
    }

    public void setKeywordEffect(String keywordEffect) {
        this.keywordEffect = keywordEffect;
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

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCreationFromTime() {
        return creationFromTime;
    }

    public void setCreationFromTime(String creationFromTime) {
        this.creationFromTime = creationFromTime;
    }

    public String getCreationToTime() {
        return creationToTime;
    }

    public void setCreationToTime(String creationToTime) {
        this.creationToTime = creationToTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOptimizeGroupName() {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName(String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }

    public String getOrderingElement() {
        return orderingElement;
    }

    public void setOrderingElement(String orderingElement) {
        this.orderingElement = orderingElement;
    }

    public String getInvalidRefreshCount() {
        return invalidRefreshCount;
    }

    public void setInvalidRefreshCount(String invalidRefreshCount) {
        this.invalidRefreshCount = invalidRefreshCount;
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

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getNoPosition() {
        return noPosition;
    }

    public void setNoPosition(String noPosition) {
        this.noPosition = noPosition;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPushPay() {
        return pushPay;
    }

    public void setPushPay(String pushPay) {
        this.pushPay = pushPay;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderingRule() {
        return orderingRule;
    }

    public void setOrderingRule(String orderingRule) {
        this.orderingRule = orderingRule;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getTargetOptimizeGroupName() {
        return targetOptimizeGroupName;
    }

    public void setTargetOptimizeGroupName(String targetOptimizeGroupName) {
        this.targetOptimizeGroupName = targetOptimizeGroupName;
    }
    public String getTargetMachineGroup() {
        return targetMachineGroup;
    }
    public void setTargetMachineGroup(String targetMachineGroup) {
        this.targetMachineGroup = targetMachineGroup;
    }

    public Integer getNoReachStandardDays() {
        return noReachStandardDays;
    }

    public void setNoReachStandardDays(Integer noReachStandardDays) {
        this.noReachStandardDays = noReachStandardDays;
    }

    public Integer getSevenDaysNoReachStandard() {
        return sevenDaysNoReachStandard;
    }

    public void setSevenDaysNoReachStandard(Integer sevenDaysNoReachStandard) {
        this.sevenDaysNoReachStandard = sevenDaysNoReachStandard;
    }

    public Integer getFifteenDaysNoReachStandard() {
        return fifteenDaysNoReachStandard;
    }

    public void setFifteenDaysNoReachStandard(Integer fifteenDaysNoReachStandard) {
        this.fifteenDaysNoReachStandard = fifteenDaysNoReachStandard;
    }

    public Integer getThirtyDaysNoReachStandard() {
        return thirtyDaysNoReachStandard;
    }

    public void setThirtyDaysNoReachStandard(Integer thirtyDaysNoReachStandard) {
        this.thirtyDaysNoReachStandard = thirtyDaysNoReachStandard;
    }

    public Boolean getRequireDelete() {
        return requireDelete;
    }

    public void setRequireDelete(Boolean requireDelete) {
        this.requireDelete = requireDelete;
    }

    public List<Long> getUuids() {
        return uuids;
    }

    public void setUuids(List<Long> uuids) {
        this.uuids = uuids;
    }

    public String getTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(String titleFlag) {
        this.titleFlag = titleFlag;
    }

    public String getTargetBearPawNumber() {
        return targetBearPawNumber;
    }

    public void setTargetBearPawNumber(String targetBearPawNumber) {
        this.targetBearPawNumber = targetBearPawNumber;
    }

    public Integer getOpenDialogStatus () {
        return openDialogStatus;
    }

    public void setOpenDialogStatus (Integer openDialogStatus) {
        this.openDialogStatus = openDialogStatus;
    }

    public String getExcludeUrl () {
        return excludeUrl;
    }

    public void setExcludeUrl (String excludeUrl) {
        this.excludeUrl = excludeUrl;
    }

    public String getFailedCause() {
        return failedCause;
    }

    public void setFailedCause(String failedCause) {
        this.failedCause = failedCause;
    }

    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }

    public String getCustomerKeywordSource() {
        return customerKeywordSource;
    }

    public void setCustomerKeywordSource(String customerKeywordSource) {
        this.customerKeywordSource = customerKeywordSource;
    }

    public String getGroupNameFuzzyQuery() {
        return groupNameFuzzyQuery;
    }

    public void setGroupNameFuzzyQuery(String groupNameFuzzyQuery) {
        this.groupNameFuzzyQuery = groupNameFuzzyQuery;
    }

    public String getMachineGroupFuzzyQuery() {
        return machineGroupFuzzyQuery;
    }

    public void setMachineGroupFuzzyQuery(String machineGroupFuzzyQuery) {
        this.machineGroupFuzzyQuery = machineGroupFuzzyQuery;
    }
}
