package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.QZCategoryTag;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author zhoukai
 * @Date 2018/12/27 9:16
 **/
@Repository("qzCategoryTagDao2")
public interface QZCategoryTagDao extends BaseMapper<QZCategoryTag> {

    List<QZCategoryTag> searchCategoryTagByQZSettingUuid(
        @Param("qzSettingUuid") Long qzSettingUuid);

    List<String> findTagNames(@Param("qzSettingUuid") Long qzSettingUuid);
}