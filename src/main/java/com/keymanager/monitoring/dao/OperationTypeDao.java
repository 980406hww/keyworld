package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.OperationType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wjianwu 2019/4/22 17:18
 */
public interface OperationTypeDao extends BaseMapper<OperationType> {

    List<String> getOperationTypeByTerminalType(@Param("terminalType") String terminalType);

    List<String> getOperationTypeByTerminalTypeAndRole(@Param("terminalType") String terminalType, @Param("flag") int flag);

    List<OperationType> searchOperationTypeListsPage(Page<OperationType> page, @Param("operationType") OperationType operationType);
}
