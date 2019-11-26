package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.QZChargeStatusCriteria;
import com.keymanager.ckadmin.entity.QZChargeStatus;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 收费状态表 服务类
 * </p>
 *
 * @author lhc
 * @since 2019-09-21
 */
public interface QZChargeStatusService extends IService<QZChargeStatus> {

    void saveQZChargeStatus(List<Integer> uuids, BigDecimal money, Integer status, Integer satisfaction, String msg, String loginName);

    void saveOneQZChargeStatus(QZChargeStatus qzChargeStatus);

    List<QZChargeStatus> getQzChargeStatus(Page<QZChargeStatus> page, Long qzSettingUuid);
}
