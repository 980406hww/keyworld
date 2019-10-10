package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.SysQZKeywordRank;
import com.keymanager.monitoring.vo.QZKeywordRankForSync;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author shunshikj40
 */
public interface SysQZKeywordRankDao extends BaseMapper<SysQZKeywordRank> {

    /**
     * replace 站点曲线数据
     * @param qzKeywordRanks
     */
    void replaceQZKeywordRanks(@Param("qzKeywordRanks") List<QZKeywordRankForSync> qzKeywordRanks);
}
