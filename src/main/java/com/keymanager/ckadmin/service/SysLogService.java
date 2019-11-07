package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.SysLog;
import java.util.Map;

public interface SysLogService extends IService<SysLog> {

    Page<SysLog> selectLoginLog(Map<String, Object> criteria);
}
