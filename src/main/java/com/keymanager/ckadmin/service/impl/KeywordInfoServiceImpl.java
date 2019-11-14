package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.ckadmin.criteria.KeywordInfoCriteria;
import com.keymanager.ckadmin.dao.KeywordInfoDao;
import com.keymanager.ckadmin.entity.KeywordInfo;
import com.keymanager.ckadmin.service.KeywordInfoService;
import org.springframework.stereotype.Service;

@Service("keywordInfoService2")
public class KeywordInfoServiceImpl extends ServiceImpl<KeywordInfoDao, KeywordInfo> implements KeywordInfoService {

    @Override
    public Page<KeywordInfo> searchKeywordInfos(KeywordInfoCriteria criteria) {
        Page<KeywordInfo> page = new Page<>(criteria.getPage(), criteria.getLimit());
        page.setOrderByField("fCreateTime");
        page.setAsc(false);
        Wrapper<KeywordInfo> wrapper = new EntityWrapper<>();
        if (null != criteria.getInit() && "init".equals(criteria.getInit())) {
            wrapper.where("1 != 1", "");
        } else {
            wrapper.like("fUserName", criteria.getUserName());
            wrapper.like("fSearchEngine", criteria.getSearchEngine());
            wrapper.like("fSearchEngine", criteria.getTerminalType());
            if (null != criteria.getOperationType() && !"".equals(criteria.getOperationType())) {
                wrapper.eq("fOperationType", criteria.getOperationType());
            }
            wrapper.like("fKeywordInfo", criteria.getKeywordInfo());
            if (null != criteria.getCreateTime() && !"".equals(criteria.getCreateTime())) {
                wrapper.where("DATE_FORMAT(fCreateTime,'%Y-%m-%d') = {0}", criteria.getCreateTime());
            }
        }
        page = selectPage(page, wrapper);
        for (KeywordInfo keywordInfo : page.getRecords()) {
            if (StringUtils.isNotEmpty(keywordInfo.getKeywordInfo())) {
                keywordInfo.setKeywordCount(keywordInfo.getKeywordInfo().split("\n").length);
            }
        }
        return page;
    }
}
