package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.SalesManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wjianwu 2019/6/6 15:12
 */
public interface SalesManageDao extends BaseMapper<SalesManage> {

    List<Map> selectAllSalesInfo();

    List<SalesManage> getSalesManages(Page<SalesManage> page, @Param("salesManage") SalesManage salesManage);
}
