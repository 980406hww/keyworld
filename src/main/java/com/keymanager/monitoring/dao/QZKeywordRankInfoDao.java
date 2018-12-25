package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/6 11:18
 **/
public interface QZKeywordRankInfoDao extends BaseMapper<QZKeywordRankInfo> {

    List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (@Param("qzSettingUuid") Long qzSettingUuid, @Param("increaseType") Boolean increaseType, @Param("terminalType") String terminalType);

    void deleteByQZSettingUuid (@Param("qzSettingUuid") Long qzSettingUuid);

    List<ExternalQzSettingVO> getQZSettingTask(@Param("crawlerHour")Integer crawlerHour);

    List<QZKeywordRankInfo> getQzKeywordRankInfos();

    QZSettingSearchCriteria getCountDownAndUp(@Param("upper")double upper, @Param("lower")double lower, @Param("terminalType") String terminalType);
}
