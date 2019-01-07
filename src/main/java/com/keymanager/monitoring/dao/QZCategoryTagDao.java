package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QZCategoryTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/27 9:16
 **/
public interface QZCategoryTagDao extends BaseMapper<QZCategoryTag> {
    List<QZCategoryTag> searchCategoryTagByQZSettingUuid (@Param("qzSettingUuid") Long qzSettingUuid);

    List<QZCategoryTag> getAllCategoryTagName ();

    List<String> findTagNamesByQZSettingUuid (@Param("qzSettingUuid") int qzSettingUuid);
}