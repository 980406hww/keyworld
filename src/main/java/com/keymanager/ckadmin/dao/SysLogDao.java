package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.entity.SysLog;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("sysLogDao2")
public interface SysLogDao extends BaseMapper<SysLog> {

    List<SysLog> selectLoginLog(Page<SysLog> page, @Param("init") String init, @Param("loginName") String loginName);
}