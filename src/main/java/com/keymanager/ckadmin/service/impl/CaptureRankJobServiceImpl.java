package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;


import com.keymanager.ckadmin.dao.CaptureRankJobDao;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import com.keymanager.ckadmin.service.CaptureRankJobService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;



/**
 * Created by shunshikj24 on 2017/9/26.
 */
@Service("captureRankJobService2")
public class CaptureRankJobServiceImpl extends ServiceImpl<CaptureRankJobDao, CaptureRankJob> implements
    CaptureRankJobService {

    @Resource(name = "captureRankJobDao2")
    private CaptureRankJobDao captureRankJobDao;

    @Override
    public void deleteCaptureRankJob(Long qzSettingUuid, String operationType) {
        captureRankJobDao.deleteCaptureRankJob(qzSettingUuid, operationType);
    }


}
