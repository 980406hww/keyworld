package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.QZSettingCountNumCriteria;
import com.keymanager.ckadmin.criteria.QZSettingSearchCriteria;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import com.keymanager.ckadmin.vo.ExternalQZKeywordRankInfoResultVO;
import com.keymanager.ckadmin.vo.ExternalQZSettingVO;
import java.util.List;

public interface QZKeywordRankInfoService extends IService<QZKeywordRankInfo> {

    List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo(long qzSettingUuid, String terminalType,
        String websiteType);

    void addQZKeywordRankInfo(Long uuid, String terminalType, String standardSpecies,
        boolean dataProcessingStatus);

    void deleteByQZSettingUuid(Long uuid);

    List<ExternalQZSettingVO> getQZSettingTask();

    void updateQzKeywordRankInfo(
        ExternalQZKeywordRankInfoResultVO externalQZKeywordRankInfoResultVo);

    QZSettingCountNumCriteria searchCountNumOfQZKeywordRankInfo(QZSettingSearchCriteria criteria);
}
