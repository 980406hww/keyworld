package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.NegativeKeywordDao;
import com.keymanager.monitoring.entity.NegativeKeyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/10/14.
 */
@Service
public class NegativeKeywordService  extends ServiceImpl<NegativeKeywordDao, NegativeKeyword> {

    private static Logger logger = LoggerFactory.getLogger(NegativeKeywordService.class);

    @Autowired
    private NegativeKeywordDao negativeKeywordDao;

    public List<String> getNegativeKeyword() {
        return negativeKeywordDao.getNegativeKeyword();
    }

    public void saveNeativeKeyword(String neativeKeyword) {
        NegativeKeyword negativeKeyword = new NegativeKeyword();
        negativeKeyword.setGroup("负面词更新1");
        negativeKeyword.setKeyword(neativeKeyword);
        negativeKeywordDao.insert(negativeKeyword);
    }
}
