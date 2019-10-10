package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.SysQZSettingDao;
import com.keymanager.monitoring.entity.SysQZSetting;
import com.keymanager.monitoring.vo.QZSettingForSync;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shunshikj40
 */
@Service
public class SysQZSettingService extends ServiceImpl<SysQZSettingDao, SysQZSetting> {

    @Autowired
    private SysQZSettingDao sysQZSettingDao;


    public void replaceQZSettings(List<QZSettingForSync> qzSettingForSyncs, String qzCustomerTag) {
        sysQZSettingDao.replaceQZSettings(qzSettingForSyncs, qzCustomerTag);
    }
}
