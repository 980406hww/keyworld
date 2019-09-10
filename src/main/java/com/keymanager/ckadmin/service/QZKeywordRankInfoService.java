package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import java.util.List;

public interface QZKeywordRankInfoService {

    List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo(long qzSettingUuid, String terminalType,
        String websiteType);
}
