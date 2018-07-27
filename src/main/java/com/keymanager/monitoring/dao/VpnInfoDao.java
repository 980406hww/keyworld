package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.criteria.VpnInfoCriteria;
import com.keymanager.monitoring.entity.Supplier;
import com.keymanager.monitoring.entity.VpnInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
public interface VpnInfoDao extends BaseMapper<VpnInfo> {

    List<VpnInfo> selectVpnImei(@Param("imeiStr") String imeiStr);

    List<VpnInfo> searchVpnInfoList(Page<VpnInfo> page, @Param("vpnInfoCriteria") VpnInfoCriteria vpnInfoCriteria);
}
