package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CmsSyncManage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsSyncManageDao extends BaseMapper<CmsSyncManage> {

    CmsSyncManage selectCmsSyncManage(@Param("companyCode") String companyCode,
                                      @Param("type") String type);
}
