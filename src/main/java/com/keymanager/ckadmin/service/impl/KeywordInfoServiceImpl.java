package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.ckadmin.criteria.KeywordInfoCriteria;
import com.keymanager.ckadmin.dao.KeywordInfoDao;
import com.keymanager.ckadmin.entity.KeywordInfo;
import com.keymanager.ckadmin.service.KeywordInfoService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("keywordInfoService2")
public class KeywordInfoServiceImpl extends ServiceImpl<KeywordInfoDao, KeywordInfo> implements KeywordInfoService {

    @Resource(name = "keywordInfoDao2")
    private KeywordInfoDao keywordInfoDao;

    @Override
    public Page<KeywordInfo> searchKeywordInfos(KeywordInfoCriteria criteria) {
        Page<KeywordInfo> page = new Page<>(criteria.getPage(), criteria.getLimit());
        page.setRecords(keywordInfoDao.searchKeywordInfos(page, criteria));
        for (KeywordInfo keywordInfo : page.getRecords()) {
            if (StringUtils.isNotEmpty(keywordInfo.getKeywordInfo())) {
                keywordInfo.setKeywordCount(keywordInfo.getKeywordInfo().split("\n").length);
            }
        }
        return page;
    }
}
