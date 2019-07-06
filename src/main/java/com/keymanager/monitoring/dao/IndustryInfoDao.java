package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.IndustryCriteria;
import com.keymanager.monitoring.entity.IndustryInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 行业表 Mapper 接口
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
public interface IndustryInfoDao extends BaseMapper<IndustryInfo> {

    List<IndustryInfo> searchIndustries (@Param("page") Page<IndustryInfo> page, @Param("industryCriteria") IndustryCriteria industryCriteria);

    void updateIndustryUserID (@Param("uuids")List<String> uuids, @Param("userID") String userID);

    void deleteIndustries(@Param("uuids") List<String> uuids);
}
