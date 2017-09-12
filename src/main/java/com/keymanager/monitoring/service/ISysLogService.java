package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.entity.SysLog;

/**
 *
 * SysLog 表数据服务层接口
 *
 */
public interface ISysLogService extends IService<SysLog> {

    void selectDataGrid(PageInfo pageInfo);

}