package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.NegativeListUpdateInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("negativeListUpdateInfoDao2")
public interface NegativeListUpdateInfoDao extends BaseMapper<NegativeListUpdateInfo> {

    NegativeListUpdateInfo getNegativeListUpdateInfo(@Param("keyword") String keyword);
}
