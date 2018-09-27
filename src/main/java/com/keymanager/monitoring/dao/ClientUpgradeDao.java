package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.ClientUpgrade;

import java.util.List;

public interface ClientUpgradeDao extends BaseMapper<ClientUpgrade> {
    List<ClientUpgrade> searchClientUpgrades(Page<ClientUpgrade> page);

    List<ClientUpgrade> findClientUpgradeJobs();
}
