package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.ProductkeywordCriteria;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.dao.ProductKeywordDao;
import com.keymanager.ckadmin.dao.QZSettingDao;
import com.keymanager.ckadmin.entity.ProductKeyword;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.service.ProductKeywordService;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 新关键字表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-09-05
 */
@Service("QZSettingService2")
public class QZSettingServiceImpl extends
    ServiceImpl<QZSettingDao, QZSetting> implements QZSettingService {

    @Resource(name = "QZSettingDao2")
    private QZSettingDao qzSettingDao;

    @Override
    public Page<QZSetting> searchQZSetting(Page<QZSetting> page, QZSettingCriteria qzSettingCriteria){
        page.setRecords(qzSettingDao.searchQZSettings(page, qzSettingCriteria));
        return page;
    }
}
