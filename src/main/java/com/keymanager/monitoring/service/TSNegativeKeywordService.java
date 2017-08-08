package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.TSNegativeKeywordDao;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
import com.keymanager.monitoring.vo.TSMainKeywordVO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
@Service
public class TSNegativeKeywordService extends ServiceImpl<TSNegativeKeywordDao, TSNegativeKeyword> {

    private static Logger logger = LoggerFactory.getLogger(QZSettingService.class);

    @Autowired
    private TSNegativeKeywordDao tsNegativeKeywordDao;

    public List<TSNegativeKeyword> findNegativeKeywordsByMainKeywordUuid(Long uuid){
        return tsNegativeKeywordDao.findNegativeKeywordsByMainKeywordUuid(uuid);
    }
    public void deleteByTSmainKeywordUuid(Long uuid){
        tsNegativeKeywordDao.deleteByTSmainKeywordUuid(uuid);
    }
    public List<TSMainKeywordVO> complaintsReportContent(){
        return tsNegativeKeywordDao.complaintsReportContent();
    }

    public void exchangeNegativeKeywordsData(List<TSNegativeKeyword> newNegativeKeywordList) {
        for(TSNegativeKeyword tsNegativeKeyword : newNegativeKeywordList) {
            tsNegativeKeywordDao.updateById(tsNegativeKeyword);
        }
    }
}
