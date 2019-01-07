package com.keymanager.monitoring.vo;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/18 11:22
 **/
public class QZSettingSearchClientGroupInfoVO {
    private int customerKeywordCount;
    private List<ClientStatusVO> clientStatusVOs;
    private List<String> categoryTagNames;

    public int getCustomerKeywordCount () {
        return customerKeywordCount;
    }

    public void setCustomerKeywordCount (int customerKeywordCount) {
        this.customerKeywordCount = customerKeywordCount;
    }

    public List<ClientStatusVO> getClientStatusVOs () {
        return clientStatusVOs;
    }

    public void setClientStatusVOs (List<ClientStatusVO> clientStatusVOs) {
        this.clientStatusVOs = clientStatusVOs;
    }

    public List<String> getCategoryTagNames () {
        return categoryTagNames;
    }

    public void setCategoryTagNames (List<String> categoryTagNames) {
        this.categoryTagNames = categoryTagNames;
    }
}
