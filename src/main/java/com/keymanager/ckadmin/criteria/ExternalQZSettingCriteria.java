package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.ExternalBaseCriteria;
import com.keymanager.value.CustomerKeywordVO;
import java.util.List;

public class ExternalQZSettingCriteria extends ExternalBaseCriteria {

    private long qzSettinguuid;

    private String domain;

    private long customerUuid;

    private String bearPawNumber;

    private boolean downloadTimesUsed;

    private List<CustomerKeywordVO> customerKeywordVos;

    public long getQzSettinguuid() {
        return qzSettinguuid;
    }

    public void setQzSettinguuid(long qzSettinguuid) {
        this.qzSettinguuid = qzSettinguuid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    public boolean isDownloadTimesUsed() {
        return downloadTimesUsed;
    }

    public void setDownloadTimesUsed(boolean downloadTimesUsed) {
        this.downloadTimesUsed = downloadTimesUsed;
    }

    public List<CustomerKeywordVO> getCustomerKeywordVos() {
        return customerKeywordVos;
    }

    public void setCustomerKeywordVos(
        List<CustomerKeywordVO> customerKeywordVos) {
        this.customerKeywordVos = customerKeywordVos;
    }
}
