package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.QZCategoryTagDao;
import com.keymanager.ckadmin.entity.QZCategoryTag;
import com.keymanager.ckadmin.service.QZCategoryTagService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("QZCategoryTagService2")
public class QZCategoryTagServiceImpl extends
    ServiceImpl<QZCategoryTagDao, QZCategoryTag> implements QZCategoryTagService {

    @Resource(name = "QZCategoryTagDao2")
    private QZCategoryTagDao qzCategoryTagDao;

    @Override
    public List<String> findTagNames(long qzSettingUuid) {
        return qzCategoryTagDao.findTagNames(qzSettingUuid);
    }
}
