package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZChargeStatusCriteria;
import com.keymanager.ckadmin.dao.QZChargeStatusDao;
import com.keymanager.ckadmin.dao.QZSettingDao;
import com.keymanager.ckadmin.entity.QZChargeStatus;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.service.QZChargeStatusService;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 收费状态表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-09-21
 */
@Service("qzChargeStatusService2")
public class QZChargeStatusServiceImpl extends ServiceImpl<QZChargeStatusDao, QZChargeStatus> implements QZChargeStatusService {

    @Resource(name = "qzChargeStatusDao2")
    QZChargeStatusDao qzChargeStatusDao;

    @Resource(name = "qzSettingDao2")
    QZSettingDao qzSettingDao;

    @Override
    public void saveQZChargeStatus(List<Integer> uuids, BigDecimal money, Integer status, Integer satisfaction, String msg, String loginName) {
        for (Integer uuid : uuids) {
            QZChargeStatus qzChargeStatus = new QZChargeStatus();
            qzChargeStatus.setChargeStatus(status);
            qzChargeStatus.setChargeMoney(money);
            qzChargeStatus.setChargeStatusMsg(msg);
            qzChargeStatus.setCustomerSatisfaction(satisfaction);
            qzChargeStatus.setQzSettingUuid((long) uuid);
            qzChargeStatus.setLoginName(loginName);
            saveOneQZChargeStatus(qzChargeStatus);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOneQZChargeStatus(QZChargeStatus qzChargeStatus) {
        qzChargeStatusDao.insert(qzChargeStatus);
        QZSetting qzSetting = qzSettingDao.selectById(qzChargeStatus.getQzSettingUuid());
        qzSetting.setRenewalStatus(qzChargeStatus.getChargeStatus());
        qzSetting.setChargeStatusUuid(qzChargeStatus.getUuid());
        qzSettingDao.updateById(qzSetting);
    }

    @Override
    public Page<QZChargeStatus> getQzChargeStatus(Page<QZChargeStatus> page, Long qzSettingUuid) {
        return qzChargeStatusDao.getQzChargeStatus(page, qzSettingUuid);
    }
}
