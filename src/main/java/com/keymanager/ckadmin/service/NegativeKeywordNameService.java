package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.NegativeKeywordNameCriteria;
import com.keymanager.ckadmin.entity.NegativeKeywordName;
import java.io.File;
import java.util.List;

public interface NegativeKeywordNameService extends IService<NegativeKeywordName> {

    Page<NegativeKeywordName> searchNegativeKeywordNames(NegativeKeywordNameCriteria criteria);

    List<String> getGroups();

    List<NegativeKeywordName> findAllNegativeKeywordName(NegativeKeywordNameCriteria criteria);

    void insertBatchByTxtFile(File file, String group);
}
