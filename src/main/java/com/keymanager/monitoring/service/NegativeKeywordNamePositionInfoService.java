package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.NegativeKeywordNamePositionInfoDao;
import com.keymanager.monitoring.entity.NegativeKeywordNamePositionInfo;
import com.keymanager.monitoring.vo.NegativeDetailInfoVO;
import com.keymanager.monitoring.vo.NegativeInfoVO;
import com.keymanager.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/10/24.
 */
@Service
public class NegativeKeywordNamePositionInfoService extends ServiceImpl<NegativeKeywordNamePositionInfoDao, NegativeKeywordNamePositionInfo> {

    private static Logger logger = LoggerFactory.getLogger(NegativeKeywordNamePositionInfoService.class);

    @Autowired
    private NegativeKeywordNamePositionInfoDao negativeKeywordNamePositionInfoDao;


    public List<NegativeKeywordNamePositionInfo> findPositionInfos(Long uuid) {
        return negativeKeywordNamePositionInfoDao.findPositionInfoByUuid(uuid);
    }

    public void insertPositionInfo(String type, NegativeInfoVO negativeInfoVO) {
        int negativeCount = 0;
        if(!Utils.isEmpty(negativeInfoVO.getNegativeInfos())){
            negativeCount = negativeInfoVO.getNegativeInfos().size();
            for(NegativeDetailInfoVO negativeDetailInfoVO : negativeInfoVO.getNegativeInfos()){
                NegativeKeywordNamePositionInfo positionInfo = new NegativeKeywordNamePositionInfo();
                positionInfo.setNegativeKeywordNameUuid(new Long((long)negativeInfoVO.getUuid()));
                positionInfo.setType(type);
                positionInfo.setPosition(negativeDetailInfoVO.getPosition());
                positionInfo.setKeyword(negativeDetailInfoVO.getKeyword());
                positionInfo.setTargetUrl(negativeDetailInfoVO.getUrl());
                positionInfo.setCreateTime(new Date());
                negativeKeywordNamePositionInfoDao.insert(positionInfo);
            }
        }
    }
}
