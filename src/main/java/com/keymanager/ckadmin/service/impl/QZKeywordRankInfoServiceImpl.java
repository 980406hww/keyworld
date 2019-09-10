package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.QZKeywordRankInfoDao;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import com.keymanager.ckadmin.service.QZKeywordRankInfoService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("QZKeywordRankInfoService2")
public class QZKeywordRankInfoServiceImpl extends
    ServiceImpl<QZKeywordRankInfoDao, QZKeywordRankInfo> implements QZKeywordRankInfoService {

    @Resource(name = "QZKeywordRankInfoDao2")
    private QZKeywordRankInfoDao qzKeywordRankInfoDao;

    @Override
    public List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo(long qzSettingUuid,
        String terminalType, String websiteType) {
        return qzKeywordRankInfoDao
            .searchExistingQZKeywordRankInfo(qzSettingUuid, terminalType, websiteType);
    }

}
