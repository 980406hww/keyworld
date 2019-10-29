package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.CustomerKeywordMon;
import java.util.Map;

public interface CustomerKeywordMonService extends IService<CustomerKeywordMon> {

    Map<String, Object> selectByCondition(Map<String, Object> condition, int num, int type);

    Page<Map<String, Object>> selectTableByCondition(Map<String, Object> condition);
}
