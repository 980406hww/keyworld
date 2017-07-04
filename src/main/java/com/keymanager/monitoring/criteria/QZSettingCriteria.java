package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.value.CustomerKeywordVO;

import java.util.List;

public class QZSettingCriteria extends BaseCriteria{
    private List<CustomerKeywordVO> customerKeywordVOs;
    private QZSetting qzSetting;
    private boolean downloadTimesUsed;

    public List<CustomerKeywordVO> getCustomerKeywordVOs() {
        return customerKeywordVOs;
    }

    public void setCustomerKeywordVOs(List<CustomerKeywordVO> customerKeywordVOs) {
        this.customerKeywordVOs = customerKeywordVOs;
    }

    public QZSetting getQzSetting() {
        return qzSetting;
    }

    public void setQzSetting(QZSetting qzSetting) {
        this.qzSetting = qzSetting;
    }

    public boolean isDownloadTimesUsed() {
        return downloadTimesUsed;
    }

    public void setDownloadTimesUsed(boolean downloadTimesUsed) {
        this.downloadTimesUsed = downloadTimesUsed;
    }
}
