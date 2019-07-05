package com.keymanager.monitoring.dao;

import com.keymanager.monitoring.entity.IndustryDetails;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 行业详情表 Mapper 接口
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
public interface IndustryDetailsDao extends BaseMapper<IndustryDetails> {

    void delIndustryDetailsByIndustryID (@Param("industryID") long industryID);
}
