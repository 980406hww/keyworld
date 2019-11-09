package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.SysLogDao;
import com.keymanager.ckadmin.entity.SysLog;
import com.keymanager.ckadmin.service.SysLogService;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service("sysLogService2")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLog> implements SysLogService {

    @Override
    public Page<SysLog> selectLoginLog(Map<String, Object> criteria) {
        Page<SysLog> page = new Page<>((Integer) criteria.get("page"), (Integer) criteria.get("limit"));
        Wrapper<SysLog> wrapper = new EntityWrapper<>();
        if (null != criteria.get("init") && "init".equals(criteria.get("init"))) {
            wrapper.where("1 != 1", "");
        } else {
            if (null != criteria.get("loginName") && !"".equals(criteria.get("loginName"))) {
                wrapper.eq("fLoginName", criteria.get("loginName"));
            }
        }
        wrapper.orderBy("fCreateTime", false);
        return selectPage(page, wrapper);
    }
}
