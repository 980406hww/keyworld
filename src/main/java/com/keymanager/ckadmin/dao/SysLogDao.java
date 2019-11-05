package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.SysLog;
import org.springframework.stereotype.Repository;

@Repository("sysLogDao2")
public interface SysLogDao extends BaseMapper<SysLog> {

}
