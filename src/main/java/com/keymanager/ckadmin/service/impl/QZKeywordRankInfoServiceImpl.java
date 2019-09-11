package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.QZKeywordRankInfoDao;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import com.keymanager.ckadmin.service.QZKeywordRankInfoService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("qzKeywordRankInfoService2")
public class QZKeywordRankInfoServiceImpl extends
    ServiceImpl<QZKeywordRankInfoDao, QZKeywordRankInfo> implements QZKeywordRankInfoService {

    @Resource(name = "qzKeywordRankInfoDao2")
    private QZKeywordRankInfoDao qzKeywordRankInfoDao;

    @Override
    public List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo(long qzSettingUuid,
        String terminalType, String websiteType) {
        return qzKeywordRankInfoDao
            .searchExistingQZKeywordRankInfo(qzSettingUuid, terminalType, websiteType);
    }

    @Override
    public void addQZKeywordRankInfo(Long uuid, String terminalType, String standardSpecies,
        boolean dataProcessingStatus) {
        QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
        qzKeywordRankInfo.setQzSettingUuid(uuid);
        qzKeywordRankInfo.setTerminalType(terminalType);
        qzKeywordRankInfo.setWebsiteType(standardSpecies);
        qzKeywordRankInfo.setDataProcessingStatus(dataProcessingStatus);
        qzKeywordRankInfoDao.insert(qzKeywordRankInfo);
    }

    @Override
    public void deleteByQZSettingUuid(Long uuid) {
        qzKeywordRankInfoDao.deleteByQZSettingUuid(uuid);
    }

}
