package com.keymanager.ckadmin.vo;

import com.keymanager.ckadmin.criteria.base.ExternalBaseCriteria;
import java.util.List;

public class SearchEngineResultVO extends ExternalBaseCriteria {

    private String searchEngine;
    private String terminalType;
    private String group;
    private String machineGroup;
    private String keyword;
    private Long customerUuid;
    private String entryType;
    private List<SearchEngineResultItemVO> searchEngineResultItemVos;

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public List<SearchEngineResultItemVO> getSearchEngineResultItemVos() {
        return searchEngineResultItemVos;
    }

    public void setSearchEngineResultItemVos(List<SearchEngineResultItemVO> searchEngineResultItemVos) {
        this.searchEngineResultItemVos = searchEngineResultItemVos;
    }
}
