package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.SysQZKeywordRankDao;
import com.keymanager.monitoring.entity.SysQZKeywordRank;
import com.keymanager.monitoring.vo.QZKeywordRankForSync;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shunshikj40
 */
@Service
public class SysQZKeywordRankService extends ServiceImpl<SysQZKeywordRankDao, SysQZKeywordRank> {

    @Autowired
    private SysQZKeywordRankDao qzKeywordRankDao;

    public void replaceQZKeywordRanks(List<QZKeywordRankForSync> qzKeywordRanks) {
        qzKeywordRankDao.replaceQZKeywordRanks(qzKeywordRanks);
    }
}
