package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("clientUpgradeDao2")
public interface ClientUpgradeDao extends BaseMapper<ClientUpgrade> {

    List<ClientUpgrade> searchClientUpgrades(Page<ClientUpgrade> page, @Param("terminalType") String terminalType);

    List<ClientUpgrade> findClientUpgradeJobs();

    void updateClientUpgradeStatus(@Param("uuid") Long uuid, @Param("status") Boolean status);
}
