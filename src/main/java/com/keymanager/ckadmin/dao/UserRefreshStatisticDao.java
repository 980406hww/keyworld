package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.criteria.UserRefreshStatisticCriteria;
import com.keymanager.ckadmin.entity.UserRefreshStatisticInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRefreshStatisticDao")
public interface UserRefreshStatisticDao  extends BaseMapper<UserRefreshStatisticInfo> {

    List<UserRefreshStatisticInfo> getUserRefreshStatisticInfo(@Param("criteria") UserRefreshStatisticCriteria criteria);

    List<UserRefreshStatisticInfo> getHistoryUserRefreshStatisticInfo(@Param("criteria") UserRefreshStatisticCriteria criteria);

}
