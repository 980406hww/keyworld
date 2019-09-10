package com.keymanager.ckadmin.service;


import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZChargeLog;
import com.keymanager.ckadmin.vo.QZChargeInfoVO;
import java.util.List;

public interface QZChargeLogService extends IService<QZChargeLog> {

    List<QZChargeInfoVO> getQZChargeLog(Long uuid);

    void saveQZChargeLog(QZChargeLog qzChargeLog, String loginName);
}
