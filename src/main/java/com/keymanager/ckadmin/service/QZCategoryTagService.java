package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZCategoryTag;
import com.keymanager.ckadmin.criteria.QZCategoryTagCriteria;
import java.util.List;

public interface QZCategoryTagService extends IService<QZCategoryTag> {

    List<String> findTagNames(Long qzSettingUuid);

    List<QZCategoryTag> searchCategoryTagByQZSettingUuid(Long qzSettingUuid);

    void updateQZCategoryTag(List<QZCategoryTag> existingQZCategoryTags, List<QZCategoryTag> updateQZCategoryTags, long qzSettingUuid);

    void saveCategoryTagNames(QZCategoryTagCriteria qzCategoryTagCriteria);
}
