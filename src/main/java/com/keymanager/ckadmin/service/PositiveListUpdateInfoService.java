package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.PositiveListUpdateInfo;
import com.keymanager.ckadmin.vo.PositiveListVO;
import java.util.List;

public interface PositiveListUpdateInfoService extends IService<PositiveListUpdateInfo> {
    void savePositiveListUpdateInfo (PositiveListVO positiveListVO, String userName);

    List<PositiveListUpdateInfo> findPositiveListUpdateInfos (Long pid);

    void deleteByPid (long pid);
}
