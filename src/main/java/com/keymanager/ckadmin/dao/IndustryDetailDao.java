package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.IndustryDetailCriteria;
import com.keymanager.ckadmin.entity.IndustryDetail;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 行业详情表 Mapper 接口
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
@Repository("industryDetailDao2")
public interface IndustryDetailDao extends BaseMapper<IndustryDetail> {

    void delIndustryDetailsByIndustryID(@Param("industryID") long industryID);

    List<IndustryDetail> searchIndustryDetails(@Param("page") Page<IndustryDetail> page, @Param("industryDetailCriteria")
            IndustryDetailCriteria industryDetailCriteria);

    void deleteIndustryDetails(@Param("uuids") List<String> uuids);

    void updateIndustryDetailRemark(@Param("uuid") long uuid, @Param("remark") String remark);

    IndustryDetail findExistingIndustryDetail(@Param("industryID") Long industryID, @Param("website") String website);

    int findIndustryDetailCount(@Param("industryID") long industryID);

    void removeUselessIndustryDetail(@Param("industryID") long industryID);

    List<Map> getIndustryInfos(@Param("uuid") long uuid);
}
