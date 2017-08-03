package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.TSNegativeKeywordDao;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
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

    public List<TSNegativeKeyword> findNegativeKeywordsBymainkeyUuid(Long uuid){
        return tsNegativeKeywordDao.findNegativeKeywordsBymainkeyUuid(uuid);
    }
    public void deleteByTSmainKeywordUuid(Long uuid){
        tsNegativeKeywordDao.deleteByTSmainKeywordUuid(uuid);
    }
}
