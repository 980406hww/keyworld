package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZCategoryTagCriteria;
import com.keymanager.ckadmin.dao.QZCategoryTagDao;
import com.keymanager.ckadmin.entity.QZCategoryTag;
import com.keymanager.ckadmin.service.QZCategoryTagService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("qzCategoryTagService2")
public class QZCategoryTagServiceImpl extends
    ServiceImpl<QZCategoryTagDao, QZCategoryTag> implements QZCategoryTagService {

    @Resource(name = "qzCategoryTagDao2")
    private QZCategoryTagDao qzCategoryTagDao;

    @Override
    public List<String> findTagNames(Long qzSettingUuid) {
        return qzCategoryTagDao.findTagNames(qzSettingUuid);
    }

    @Override
    public List<QZCategoryTag> searchCategoryTagByQZSettingUuid(Long qzSettingUuid) {
        return qzCategoryTagDao.searchCategoryTagByQZSettingUuid(qzSettingUuid);
    }

    @Override
    public void updateQZCategoryTag(List<QZCategoryTag> existingQZCategoryTags, List<QZCategoryTag> updateQZCategoryTags, long qzSettingUuid) {
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

    @Override
    public void saveCategoryTagNames(QZCategoryTagCriteria qzCategoryTagCriteria) {
        List<QZCategoryTag> existingQZCategoryTags = this.searchCategoryTagByQZSettingUuid(qzCategoryTagCriteria.getQzSettingUuid());
        this.updateQZCategoryTag(existingQZCategoryTags, qzCategoryTagCriteria.getQzCategoryTags(), qzCategoryTagCriteria.getQzSettingUuid());
    }
}
