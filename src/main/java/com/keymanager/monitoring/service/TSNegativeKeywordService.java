package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.TSNegativeKeywordDao;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
import com.keymanager.monitoring.vo.ComplaintMailVO;
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
    public void deleteByTSMainKeywordUuid(Long uuid){
        tsNegativeKeywordDao.deleteByTSMainKeywordUuid(uuid);
    }
    public List<ComplaintMailVO> complaintsReportContentPC2weeks(){
        return tsNegativeKeywordDao.complaintsReportContentPC2weeks();
    }
    public List<ComplaintMailVO> complaintsReportContentPhone2weeks(){
        return tsNegativeKeywordDao.complaintsReportContentPhone2weeks();
    }
    public List<ComplaintMailVO> complaintsReportContentPC3times(){
        return tsNegativeKeywordDao.complaintsReportContentPC3times();
    }
    public List<ComplaintMailVO> complaintsReportContentPhone3times(){
        return tsNegativeKeywordDao.complaintsReportContentPhone3times();
    }

    public void updateNegativeKeywords(List<TSNegativeKeyword> newNegativeKeywordList) {
        for(TSNegativeKeyword tsNegativeKeyword : newNegativeKeywordList) {
            tsNegativeKeywordDao.updateById(tsNegativeKeyword);
        }
    }
}
