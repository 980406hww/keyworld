package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.NegativeSiteContactKeyword;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NegativeSiteContactKeywordDao extends BaseMapper<NegativeSiteContactKeyword> {

    List<String> getContactKeyword();
}
