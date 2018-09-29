package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.ClientUpgrade;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientUpgradeDao extends BaseMapper<ClientUpgrade> {
    List<ClientUpgrade> searchClientUpgrades(Page<ClientUpgrade> page, @Param("terminalType")String terminalType);

    List<ClientUpgrade> findClientUpgradeJobs();

    void updateClientUpgradeStatus(@Param("uuid")Long uuid, @Param("status")Boolean status);
}
