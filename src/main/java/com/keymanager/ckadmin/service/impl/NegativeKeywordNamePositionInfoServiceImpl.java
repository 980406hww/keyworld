package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.NegativeKeywordNamePositionInfoDao;
import com.keymanager.ckadmin.entity.NegativeKeywordNamePositionInfo;
import com.keymanager.ckadmin.service.NegativeKeywordNamePositionInfoService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("negativeKeywordNamePositionInfoService2")
public class NegativeKeywordNamePositionInfoServiceImpl extends ServiceImpl<NegativeKeywordNamePositionInfoDao, NegativeKeywordNamePositionInfo>
    implements NegativeKeywordNamePositionInfoService {

    @Resource(name = "negativeKeywordNamePositionInfoDao2")
    private NegativeKeywordNamePositionInfoDao negativeKeywordNamePositionInfoDao;

    @Override
    public List<NegativeKeywordNamePositionInfo> findPositionInfos(Long uuid) {
        Wrapper<NegativeKeywordNamePositionInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("fNegativeKeywordNameUuid", uuid);
        return negativeKeywordNamePositionInfoDao.selectList(wrapper);
    }
}
