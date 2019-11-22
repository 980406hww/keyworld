package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.CustomerKeywordPositionSummary;
import com.keymanager.ckadmin.vo.CustomerKeywordPositionSummaryCountVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author shunshikj40
 */
@Repository("customerKeywordPositionSummaryDao2")
public interface CustomerKeywordPositionSummaryDao extends BaseMapper<CustomerKeywordPositionSummary> {

    List<CustomerKeywordPositionSummaryCountVO> getCustomerKeywordPositionSummaryData(@Param("condition") Map<String, Object> condition);

    List<Map<String, Object>> getCKPositionSummaryDataInitTable(@Param("condition") Map<String, Object> condition);

    Integer getCKPositionSummaryDataInitCount(@Param("condition") Map<String, Object> condition);

    Map<String, Object> getOneCKPositionSummaryData(@Param("condition") Map<String, Object> condition);
}
