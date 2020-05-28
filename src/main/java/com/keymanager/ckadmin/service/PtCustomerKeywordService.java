package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.PtCustomerKeyword;
import com.keymanager.ckadmin.vo.CustomerKeyWordCrawlRankVO;

import java.util.Date;
import java.util.List;

public interface PtCustomerKeywordService extends IService<PtCustomerKeyword> {

    /**
     * 查询符合条件的pt关键字
     */
    List<CustomerKeyWordCrawlRankVO> getPtKeywords(int captureStatus);

    /**
     * 修改爬取状态
     */
    void updatePtCaptureStatus(List<Long> uuids);

    /**
     * 修改爬取时间和状态
     */
    void updatePtKeywordCapturePositionTime(Long uuid, Date capturePositionTime);

    void updatePosition(Long uuid, int position, Date capturePositionTime, String city);
}
