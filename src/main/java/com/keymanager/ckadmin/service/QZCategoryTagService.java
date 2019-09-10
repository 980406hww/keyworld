package com.keymanager.ckadmin.service;

import java.util.List;

public interface QZCategoryTagService {

    List<String> findTagNames(long qzSettingUuid);
}
