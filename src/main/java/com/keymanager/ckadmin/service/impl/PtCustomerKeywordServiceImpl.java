package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.PtCustomerKeywordDao;
import com.keymanager.ckadmin.entity.PtCustomerKeyword;
import com.keymanager.ckadmin.service.PtCustomerKeywordService;
import com.keymanager.ckadmin.vo.CustomerKeyWordCrawlRankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service(value = "ptCustomerKeywordService2")
public class PtCustomerKeywordServiceImpl extends ServiceImpl<PtCustomerKeywordDao, PtCustomerKeyword> implements PtCustomerKeywordService {

    @Autowired
    private PtCustomerKeywordDao ptCustomerKeywordDao;

    /**
     * 查询符合条件的pt关键字
     */
    @Override
    public List<CustomerKeyWordCrawlRankVO> getPtKeywords(int captureStatus) {
        return ptCustomerKeywordDao.getPtKeywords(captureStatus);
    }

    /**
     * 修改爬取状态
     */
    @Override
    public void updatePtCaptureStatus(List<Long> uuids) {
        ptCustomerKeywordDao.updatePtCaptureStatus(uuids);
    }

    /**
     * 修改爬取时间和状态
     */
    @Override
    public void updatePtKeywordCapturePositionTime(Long uuid, Date capturePositionTime) {
        ptCustomerKeywordDao.updatePtKeywordCapturePositionTime(uuid, capturePositionTime);
    }

    @Override
    public void updatePosition(Long uuid, int position, Date capturePositionTime, String city) {
        ptCustomerKeywordDao.updatePosition(uuid, position, capturePositionTime, city);
    }
}
