package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZCategoryTag;
import java.util.List;

public interface QZCategoryTagService extends IService<QZCategoryTag> {

    List<String> findTagNames(long qzSettingUuid);
}
