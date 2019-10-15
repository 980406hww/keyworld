package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.NegativeKeywordNamePositionInfo;
import java.util.List;

public interface NegativeKeywordNamePositionInfoService extends IService<NegativeKeywordNamePositionInfo> {

    List<NegativeKeywordNamePositionInfo> findPositionInfos(Long uuid);
}
