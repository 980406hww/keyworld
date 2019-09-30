package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Performance;
import org.springframework.stereotype.Repository;

@Repository("performanceDao2")
public interface PerformanceDao extends BaseMapper<Performance> {

}
