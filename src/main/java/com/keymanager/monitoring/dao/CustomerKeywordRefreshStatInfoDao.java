package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.vo.CustomerKeywordRefreshStatInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/9/12.
 */
public interface CustomerKeywordRefreshStatInfoDao extends BaseMapper<CustomerKeywordRefreshStatInfoVO> {

    List<CustomerKeywordRefreshStatInfoVO> searchCustomerKeywordStatInfoVOs(@Param("customerKeywordRefreshStatInfoCriteria") CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria);

}
