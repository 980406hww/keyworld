package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.IndustryCriteria;
import com.keymanager.ckadmin.entity.IndustryInfo;
import com.keymanager.ckadmin.vo.IndustryInfoVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 行业表 Mapper 接口
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */

@Repository("industryInfoDao2")
public interface IndustryInfoDao extends BaseMapper<IndustryInfo> {

    List<IndustryInfo> searchIndustries(Page<IndustryInfo> page, @Param("industryCriteria") IndustryCriteria industryCriteria);

    void updateIndustryUserID(@Param("uuids") List<String> uuids, @Param("userID") String userID);

    void deleteIndustries(@Param("uuids") List<String> uuids);

    IndustryInfo findExistingIndustryInfo(@Param("industryName") String industryName);

   IndustryInfoVO getValidIndustryInfo();

    void updateIndustryStatus(@Param("uuids") List<String> uuids);
}
