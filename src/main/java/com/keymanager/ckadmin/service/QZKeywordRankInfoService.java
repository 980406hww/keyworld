package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import java.util.List;

public interface QZKeywordRankInfoService extends IService<QZKeywordRankInfo> {

    List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo(long qzSettingUuid, String terminalType,
        String websiteType);
}
