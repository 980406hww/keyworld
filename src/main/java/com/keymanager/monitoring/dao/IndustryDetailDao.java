package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.IndustryDetailCriteria;
import com.keymanager.monitoring.entity.IndustryDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 行业详情表 Mapper 接口
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
public interface IndustryDetailDao extends BaseMapper<IndustryDetail> {

    void delIndustryDetailsByIndustryID (@Param("industryID") long industryID);

    List<IndustryDetail> searchIndustryDetails(@Param("page") Page<IndustryDetail> page, @Param("industryDetailCriteria")
            IndustryDetailCriteria industryDetailCriteria);

    void deleteIndustryDetails(@Param("uuids") List<String> uuids);

    void updateIndustryDetailRemark(@Param("uuid") long uuid, @Param("remark") String remark);

    IndustryDetail findExistingIndustryDetail(@Param("industryID") Long industryID, @Param("website") String website);

    int findIndustryDetailCount(@Param("industryID") long industryID);

    void removeUselessIndustryDetail(@Param("industryID") long industryID);
}
