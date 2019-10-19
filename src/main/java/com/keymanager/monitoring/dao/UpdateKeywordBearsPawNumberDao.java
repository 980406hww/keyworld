package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerKeyword;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UpdateKeywordBearsPawNumberDao extends BaseMapper<CustomerKeyword> {
    List<CustomerKeyword> getCustomerKeywordDistinctUrl();

    int updateBearPawByUrl(@Param(value = "url") String url, @Param("xzNumber") String xzNumber);
}
