package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.entity.OperationType;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author wjianwu 2019/4/22 17:18
 */
@Repository("operationTypeDao2")
public interface OperationTypeDao extends BaseMapper<OperationType> {

    List<String> getOperationTypeByTerminalType(@Param("terminalType") String terminalType);

    List<String> getOperationTypeByTerminalTypeAndRole(@Param("terminalType") String terminalType, @Param("flag") int flag);

    List<OperationType> searchOperationTypeListsPage(Page<OperationType> page, @Param("operationType") OperationType operationType, @Param("init") String init);
}
