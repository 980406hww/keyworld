package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.QZCategoryTagDao;
import com.keymanager.monitoring.entity.QZCategoryTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/27 9:10
 **/
@Service
public class QZCategoryTagService extends ServiceImpl<QZCategoryTagDao, QZCategoryTag> {

    @Autowired
    private QZCategoryTagDao qzCategoryTagDao;

    public List<QZCategoryTag> searchCategoryTagByQZSettingUuid (Long qzSettinguUuid) {
        return qzCategoryTagDao.searchCategoryTagByQZSettingUuid(qzSettinguUuid);
    }

    public List<QZCategoryTag> getAllCategoryTagName () {
        return qzCategoryTagDao.getAllCategoryTagName();
    }
}
