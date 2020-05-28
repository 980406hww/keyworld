package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.PtCustomerKeyword;
import com.keymanager.ckadmin.vo.CustomerKeyWordCrawlRankVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PtCustomerKeywordDao extends BaseMapper<PtCustomerKeyword> {

    /**
     * 查询符合条件的pt关键字
     */
    List<CustomerKeyWordCrawlRankVO> getPtKeywords(@Param("captureStatus") int captureStatus);

    /**
     * 修改爬取状态
     */
    void updatePtCaptureStatus(@Param("uuids") List<Long> uuids);

    /**
     * 修改爬取时间和状态
     */
    void updatePtKeywordCapturePositionTime(@Param("uuid") Long uuid, @Param("capturePositionTime") Date capturePositionTime);

    void updatePosition(@Param("uuid") Long uuid, @Param("position") int position, @Param("capturePositionTime") Date capturePositionTime, @Param("city") String city);
}
