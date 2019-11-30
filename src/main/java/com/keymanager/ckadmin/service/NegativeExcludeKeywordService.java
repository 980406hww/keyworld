package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.NegativeExcludeKeyword;
import java.util.List;

public interface NegativeExcludeKeywordService extends IService<NegativeExcludeKeyword> {

    List<String> getNegativeExcludeKeyword();
}
