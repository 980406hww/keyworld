package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.NegativeSiteContactKeywordDao;
import com.keymanager.monitoring.entity.NegativeSiteContactKeyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/10/27.
 */
@Service
public class NegativeSiteContactKeywordService extends ServiceImpl<NegativeSiteContactKeywordDao, NegativeSiteContactKeyword> {

    private static Logger logger = LoggerFactory.getLogger(NegativeSiteContactKeywordService.class);

    @Autowired
    private NegativeSiteContactKeywordDao negativeSiteContactKeywordDao;

    public List<String> getContactKeyword() {
        return negativeSiteContactKeywordDao.getContactKeyword();
    }

}
