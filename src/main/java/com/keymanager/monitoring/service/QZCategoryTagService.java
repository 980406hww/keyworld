package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.QZCategoryTagCriteria;
import com.keymanager.monitoring.dao.QZCategoryTagDao;
import com.keymanager.monitoring.entity.QZCategoryTag;
import com.keymanager.monitoring.entity.QZSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhoukai
 * @Date 2018/12/27 9:10
 **/
@Service
public class QZCategoryTagService extends ServiceImpl<QZCategoryTagDao, QZCategoryTag> {

    @Autowired
    private QZCategoryTagDao qzCategoryTagDao;

    public List<QZCategoryTag> searchCategoryTagByQZSettingUuid (Long qzSettingUuid) {
        return qzCategoryTagDao.searchCategoryTagByQZSettingUuid(qzSettingUuid);
    }

    public List<String> findTagNames(Long qzSettingUuid) {
        return qzCategoryTagDao.findTagNames(qzSettingUuid);
    }

    public void saveCategoryTagNames (QZCategoryTagCriteria qzCategoryTagCriteria) {
        List<QZCategoryTag> existingQZCategoryTags = searchCategoryTagByQZSettingUuid(qzCategoryTagCriteria.getQzSettingUuid());
        updateQZCategoryTag(existingQZCategoryTags, qzCategoryTagCriteria.getQzCategoryTags(), qzCategoryTagCriteria.getQzSettingUuid());
    }

    public void updateQZCategoryTag(List<QZCategoryTag> existingQZCategoryTags, List<QZCategoryTag> updateQZCategoryTags, long qzSettingUuid){
        Map<String, QZCategoryTag> existingQZCategoryTagMap = new HashMap<>();
        for (QZCategoryTag qzCategoryTag : existingQZCategoryTags) {
            existingQZCategoryTagMap.put(qzCategoryTag.getTagName(), qzCategoryTag);
        }
        for (QZCategoryTag newQZCategoryTag : updateQZCategoryTags) {
            QZCategoryTag oldQZCategoryTag = existingQZCategoryTagMap.get(newQZCategoryTag.getTagName());
            if (null != oldQZCategoryTag) {
                existingQZCategoryTagMap.remove(newQZCategoryTag.getTagName());
            } else {
                newQZCategoryTag.setQzSettingUuid(qzSettingUuid);
                qzCategoryTagDao.insert(newQZCategoryTag);
            }
        }

        for (QZCategoryTag qzCategoryTag : existingQZCategoryTagMap.values()) {
            qzCategoryTagDao.deleteById(qzCategoryTag);
        }
    }
}
