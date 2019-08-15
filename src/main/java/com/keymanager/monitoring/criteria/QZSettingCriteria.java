package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import com.keymanager.value.CustomerKeywordVO;

import java.util.List;

public class QZSettingCriteria extends BaseCriteria{
    private List<CustomerKeywordVO> customerKeywordVOs;
    private ExternalQzSettingVO externalQzSettingVO;
    private QZSetting qzSetting;
    private boolean downloadTimesUsed;

    public List<CustomerKeywordVO> getCustomerKeywordVOs() {
        return customerKeywordVOs;
    }

    public void setCustomerKeywordVOs(List<CustomerKeywordVO> customerKeywordVOs) {
        this.customerKeywordVOs = customerKeywordVOs;
    }

    public ExternalQzSettingVO getExternalQzSettingVO() {
        return externalQzSettingVO;
    }

    public void setExternalQzSettingVO(ExternalQzSettingVO externalQzSettingVO) {
        this.externalQzSettingVO = externalQzSettingVO;
    }

    public boolean isDownloadTimesUsed() {
        return downloadTimesUsed;
    }

    public void setDownloadTimesUsed(boolean downloadTimesUsed) {
        this.downloadTimesUsed = downloadTimesUsed;
    }

    public QZSetting getQzSetting() {
        return qzSetting;
    }

    public void setQzSetting(QZSetting qzSetting) {
        this.qzSetting = qzSetting;
    }
}
