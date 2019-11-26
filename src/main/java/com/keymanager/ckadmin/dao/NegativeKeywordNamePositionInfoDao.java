package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.NegativeKeywordNamePositionInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("negativeKeywordNamePositionInfoDao2")
public interface NegativeKeywordNamePositionInfoDao extends BaseMapper<NegativeKeywordNamePositionInfo> {

    List<NegativeKeywordNamePositionInfo> findPositionInfoByUuid(@Param("uuid") Long uuid);
}
