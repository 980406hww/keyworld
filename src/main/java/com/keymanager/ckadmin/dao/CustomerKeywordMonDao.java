package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.CustomerKeywordMon;
import com.keymanager.ckadmin.vo.CustomerKeywordMonCountVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerKeywordMonDao2")
public interface CustomerKeywordMonDao extends BaseMapper<CustomerKeywordMon> {

    List<CustomerKeywordMonCountVO> getCustomerKeywordMonData(@Param("condition") Map<String, Object> condition);

    Integer selectCountByCondition(@Param("condition") Map<String, Object> condition);

    List<Map<String, Object>> selectTableByCondition(@Param("condition") Map<String, Object> condition);
}
