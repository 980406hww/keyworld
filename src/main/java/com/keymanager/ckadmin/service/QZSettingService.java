package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.ProductkeywordCriteria;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.entity.ProductKeyword;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import java.util.List;
import java.util.Map;

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

    List<QZSearchEngineVO> searchQZSettingSearchEngineMap(
        QZSettingCriteria criteria, Integer record);

}
