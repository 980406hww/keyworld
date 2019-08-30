package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.vo.ExternalWebsiteCheckResultVO;

import java.util.List;

public class ExternalWebsiteUpdateStatusCriteria extends BaseCriteria{

    private List<ExternalWebsiteCheckResultVO> websiteCheckResultVOS;

    public List<ExternalWebsiteCheckResultVO> getWebsiteCheckResultVOS() {
        return websiteCheckResultVOS;
    }

    public void setWebsiteCheckResultVOS(List<ExternalWebsiteCheckResultVO> websiteCheckResultVOS) {
        this.websiteCheckResultVOS = websiteCheckResultVOS;
    }
}
