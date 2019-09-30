package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.NegativeListUpdateInfo;

public interface NegativeListUpdateInfoService extends IService<NegativeListUpdateInfo> {

    NegativeListUpdateInfo getNegativeListUpdateInfo(String keyword);

    void saveNegativeListUpdateInfo(String keyword);
}
