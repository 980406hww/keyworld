package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.SalesManage;
import com.keymanager.monitoring.vo.SalesManageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wjianwu 2019/6/6 15:12
 */
public interface SalesManageDao extends BaseMapper<SalesManage> {

    List<SalesManageVO> selectAllSalesInfo(@Param("websiteType") String websiteType);

    List<SalesManage> getSalesManages(Page<SalesManage> page, @Param("salesManage") SalesManage salesManage);
}
