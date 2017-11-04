package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.NegativeExcludeKeyword;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/10/27.
 */
public interface NegativeExcludeKeywordDao extends BaseMapper<NegativeExcludeKeyword>{

    List<String> getNegativeExcludeKeyword();

}
