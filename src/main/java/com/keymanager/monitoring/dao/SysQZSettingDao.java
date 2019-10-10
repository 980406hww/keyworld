package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.SysQZSetting;
import com.keymanager.monitoring.vo.QZSettingForSync;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author shunshikj40
 */
public interface SysQZSettingDao extends BaseMapper<SysQZSetting> {

    /**
     * replace 替换站点信息
     * @param qzSettingForSyncs
     * @param qzCustomerTag
     */
    void replaceQZSettings(@Param("qzSettingForSyncs") List<QZSettingForSync> qzSettingForSyncs, @Param("qzCustomerTag") String qzCustomerTag);
}
