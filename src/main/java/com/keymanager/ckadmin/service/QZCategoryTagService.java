package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZCategoryTag;
import java.util.List;

public interface QZCategoryTagService extends IService<QZCategoryTag> {

    List<String> findTagNames(long qzSettingUuid);

    List<QZCategoryTag> searchCategoryTagByQZSettingUuid(Long qzSettingUuid);

    void updateQZCategoryTag(List<QZCategoryTag> existingQZCategoryTags,
        List<QZCategoryTag> updateQZCategoryTags, long qzSettingUuid);
}
