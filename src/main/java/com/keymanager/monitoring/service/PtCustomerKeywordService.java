package com.keymanager.monitoring.service;

import com.keymanager.monitoring.dao.PtCustomerKeywordDao;
import com.keymanager.monitoring.entity.PtCustomerKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service
public class PtCustomerKeywordService extends ServiceImpl<PtCustomerKeywordDao, PtCustomerKeyword> {

    @Autowired
    private PtCustomerKeywordDao ptCustomerKeywordDao;

    /**
     * 查重
     */
    public PtCustomerKeyword selectExistingCmsKeyword(PtCustomerKeyword keyword) {
        return ptCustomerKeywordDao.selectExistingCmsKeyword(keyword);
    }

    /**
     * 检查操作中的关键词排名是否爬取完成, 关闭开关
     */
    public int checkFinishedCapturePosition() {
        return ptCustomerKeywordDao.checkFinishedCapturePosition();
    }
}
