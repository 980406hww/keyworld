package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.NegativeStandardSettingCriteria;
import com.keymanager.monitoring.dao.NegativeStandardSettingDao;
import com.keymanager.monitoring.entity.NegativeStandardSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * Created by ljj on 2018/6/28.
 */

@Service
public class NegativeStandardSettingService extends ServiceImpl<NegativeStandardSettingDao, NegativeStandardSetting> {

    @Autowired
    NegativeStandardSettingDao negativeStandardSettingDao;

    public Page<NegativeStandardSetting> searchNegaStandardSetting( Page<NegativeStandardSetting> page,NegativeStandardSettingCriteria negativeStandardSettingCriteria){
        page.setRecords(negativeStandardSettingDao.searchNegativeStandardSetting(page,negativeStandardSettingCriteria));
       return page;
    }

    public void deleteNegativeStandardSettings(List<String> uuids){
            negativeStandardSettingDao.deleteBatchIds(uuids);
    }

    public void deleteNegativeStandardSetting(Long uuid){
        negativeStandardSettingDao.deleteById(uuid);
    }

    public void saveNegativeStandardSetting(NegativeStandardSetting negativeStandardSetting){
        negativeStandardSetting.setUpdateTime(new Date());
        if(negativeStandardSetting.getUuid()==null){
            negativeStandardSetting.setReachStandard(false);
            negativeStandardSetting.setCreateTime(new Date());
            negativeStandardSettingDao.insert(negativeStandardSetting);
        }else{
            negativeStandardSettingDao.updateById(negativeStandardSetting);
        }
    }

    public int findNegativeStandardSetting(Long customerUuid,String keyword,String searchEngine){
        return negativeStandardSettingDao.findNegativeStandardSetting(customerUuid,keyword,searchEngine);
    }
}
