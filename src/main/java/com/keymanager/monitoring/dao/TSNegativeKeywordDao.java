package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
import com.keymanager.monitoring.vo.ComplaintMailVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
public interface TSNegativeKeywordDao extends BaseMapper<TSNegativeKeyword> {

    List<TSNegativeKeyword> findNegativeKeywordsByMainkeyUuid(@Param("tsMainKeywordUuid")Long tsMainKeywordUuid);

    int selectLastId();

    void deleteByTSmainKeywordUuid(@Param("tsMainKeywordUuid") Long tsMainKeywordUuid);

    List<ComplaintMailVO> complaintsReportContentPC2weeks();//获取需要发送邮件的内容
    List<ComplaintMailVO> complaintsReportContentPhone2weeks();//获取需要发送邮件的内容
    List<ComplaintMailVO> complaintsReportContentPC3times();//获取需要发送邮件的内容
    List<ComplaintMailVO> complaintsReportContentPhone3times();//获取需要发送邮件的内容
}
