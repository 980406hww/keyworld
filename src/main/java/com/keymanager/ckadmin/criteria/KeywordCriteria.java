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


    private String targetOptimizeGroupName;
    private String targetMachineGroup;
    private String targetBearPawNumber;



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
}
