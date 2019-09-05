package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

/**
 * @ClassName CustomerCriteria
 * @Description 客户查询条件类
 * @Author lhc
 * @Date 2019/9/2 10:36
 * @Version 1.0
 */
public class ProductkeywordCriteria extends BaseCriteria {
    private Long customerUuid;
    private String keyword;
    private String terminalType;
    private String searchEngine;
    private String status;

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

    @Override
    public String toString() {
        return "ProductkeywordCriteria{" +
            "keyword='" + keyword + '\'' +
            ", terminalType='" + terminalType + '\'' +
            ", searchEngin='" + searchEngine + '\'' +
            ", status='" + status + '\'' +
            '}';
    }
}
