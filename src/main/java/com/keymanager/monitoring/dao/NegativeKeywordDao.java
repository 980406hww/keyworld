package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.NegativeKeyword;

import java.util.List;

/**
 * Created by shunshikj24 on 2017/10/14.
 */
public interface NegativeKeywordDao extends BaseMapper<NegativeKeyword> {

    List<String> getNegativeKeyword();
}
