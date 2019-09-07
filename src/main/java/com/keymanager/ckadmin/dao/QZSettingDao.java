package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.entity.QZSetting;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("QZSettingDao2")
public interface QZSettingDao extends BaseMapper<QZSetting> {



    List<QZSetting> searchQZSettings(Page<QZSetting> page,
        @Param("qzSettingCriteria") QZSettingCriteria qzSettingCriteria);


}

