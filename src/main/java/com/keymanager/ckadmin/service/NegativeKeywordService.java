package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.NegativeKeyword;
import java.util.List;

public interface NegativeKeywordService extends IService<NegativeKeyword> {

    List<String> getNegativeKeyword();
}
