package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.KeywordInfoCriteria;
import com.keymanager.ckadmin.entity.KeywordInfo;

public interface KeywordInfoService extends IService<KeywordInfo> {

    Page<KeywordInfo> searchKeywordInfos(KeywordInfoCriteria criteria);
}
