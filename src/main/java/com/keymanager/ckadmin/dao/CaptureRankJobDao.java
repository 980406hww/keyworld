package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Created by shunshikj24 on 2017/9/26.
 */

@Repository("captureRankJobDao2")
public interface CaptureRankJobDao extends BaseMapper<CaptureRankJob> {

    void deleteCaptureRankJob(@Param("qzSettingUuid") Long qzSettingUuid, @Param("operationType") String operationType);

}
