package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.vo.BaiduUrl;

public class CaptureRealUrlCriteria extends BaseCriteria{
    private BaiduUrl sourceUrl;

    public BaiduUrl getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(BaiduUrl sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
