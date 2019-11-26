package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.SysLogDao;
import com.keymanager.ckadmin.entity.SysLog;
import com.keymanager.ckadmin.service.SysLogService;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("sysLogService2")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLog> implements SysLogService {

    @Resource(name = "sysLogDao2")
    private SysLogDao sysLogDao;

    @Override
    public Page<SysLog> selectLoginLog(Map<String, Object> criteria) {
        Page<SysLog> page = new Page<>((Integer) criteria.get("page"), (Integer) criteria.get("limit"));
        page.setRecords(sysLogDao.selectLoginLog(page, (String) criteria.get("init"), (String) criteria.get("loginName")));
        return page;
    }
}
