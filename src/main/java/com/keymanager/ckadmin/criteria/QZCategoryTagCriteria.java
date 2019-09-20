package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.entity.QZCategoryTag;
import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/1/7 18:32
 **/
public class QZCategoryTagCriteria {
    private long qzSettingUuid;
    private List<QZCategoryTag> qzCategoryTags;

    public long getQzSettingUuid () {
        return qzSettingUuid;
    }

    public void setQzSettingUuid (long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public List<QZCategoryTag> getQzCategoryTags () {
        return qzCategoryTags;
    }

    public void setQzCategoryTags (List<QZCategoryTag> qzCategoryTags) {
        this.qzCategoryTags = qzCategoryTags;
    }
}
