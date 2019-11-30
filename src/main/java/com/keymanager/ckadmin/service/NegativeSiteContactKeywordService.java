package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.NegativeSiteContactKeyword;
import java.util.List;

public interface NegativeSiteContactKeywordService extends IService<NegativeSiteContactKeyword> {

    List<String> getContactKeyword();
}
