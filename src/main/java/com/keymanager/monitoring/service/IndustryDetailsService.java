package com.keymanager.monitoring.service;

import com.keymanager.monitoring.entity.IndustryDetails;
import com.keymanager.monitoring.dao.IndustryDetailsDao;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 行业详情表 服务实现类
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
@Service
public class IndustryDetailsService extends ServiceImpl<IndustryDetailsDao, IndustryDetails>  {

    @Autowired
    private IndustryDetailsDao industryDetailsDao;

    public void delIndustryDetailsByIndustryID (long industryID) {
        industryDetailsDao.delIndustryDetailsByIndustryID(industryID);
    }
}
