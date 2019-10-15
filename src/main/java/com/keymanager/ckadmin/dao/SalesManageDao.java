package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.SalesInfoCriteria;
import com.keymanager.ckadmin.entity.SalesManage;
import com.keymanager.ckadmin.vo.SalesManageVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wjianwu 2019/6/6 15:12
 */

@Repository(value = "salesManageDao2")
public interface SalesManageDao extends BaseMapper<SalesManage> {

    List<SalesManageVO> selectAllSalesInfo(@Param("websiteType") String websiteType);

    List<SalesManage> getSalesManages(Page<SalesManage> page, @Param("salesInfoCriteria") SalesInfoCriteria salesInfoCriteria);
}
