package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.ProductkeywordCriteria;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.entity.ProductKeyword;
import com.keymanager.ckadmin.entity.QZSetting;
import java.util.List;

/**
 * <p>
 * 新全站 服务类
 * </p>
 *
 * @author lhc
 * @since 2019-09-05
 */
public interface QZSettingService extends IService<QZSetting> {

    Page<QZSetting> searchQZSetting(Page<QZSetting> page, QZSettingCriteria qzSettingCriteria);
}
