package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.PositiveListUpdateInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "positiveListUpdateInfoDao2")
public interface PositiveListUpdateInfoDao extends BaseMapper<PositiveListUpdateInfo> {

    List<PositiveListUpdateInfo> findMostRecentPositiveListUpdateInfo(@Param("pid") Long pid);

    void deleteByPid(@Param("pid") long pid);
}
