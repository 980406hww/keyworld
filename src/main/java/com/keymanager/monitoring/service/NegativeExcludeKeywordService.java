package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.NegativeExcludeKeywordDao;
import com.keymanager.monitoring.entity.NegativeExcludeKeyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/10/14.
 */
@Service
public class NegativeExcludeKeywordService extends ServiceImpl<NegativeExcludeKeywordDao,NegativeExcludeKeyword>{

    private static Logger logger = LoggerFactory.getLogger(NegativeExcludeKeywordService.class);

    @Autowired
    private NegativeExcludeKeywordDao negativeExcludeKeywordDao;

    public List<String> getNegativeExcludeKeyword() {
        return negativeExcludeKeywordDao.getNegativeExcludeKeyword();
    }
}
