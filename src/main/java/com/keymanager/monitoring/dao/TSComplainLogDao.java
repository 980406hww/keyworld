package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.TSComplainLog;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/2.
 */
public interface TSComplainLogDao extends BaseMapper<TSComplainLog> {

    List<TSComplainLog> findTSComplainLogs();

    int selectLastId();
}
