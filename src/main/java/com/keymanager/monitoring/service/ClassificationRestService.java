package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClassificationRestDao;
import com.keymanager.monitoring.entity.Classification;
import com.keymanager.monitoring.entity.ClassificationWebsitInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/12/4.
 */
@Service
public class ClassificationRestService extends ServiceImpl<ClassificationRestDao,Classification> {
    @Autowired
    private ClassificationRestDao classificationRestDao;

    @Autowired
    private ClassificationWebsiteInfoRestService classificationWebsiteInfoRestService;


    public List<String> getClassificationSupportingData() {
        return classificationRestDao.getClassificationGroup();
    }

    public Classification getClassification() {
        Classification classification = classificationRestDao.getClassification();
        if(classification != null){
            classification.setQueried(true);
            updateById(classification);
        }
        return classification;
    }

    public Classification getClassificationgroupNotAll(String group) {
        Classification classification = classificationRestDao.getClassificationgroupNotAll(group);
        if(classification != null){
            classification.setQueried(true);
            updateById(classification);
        }
        return classification;
    }

    public void updateClassificationInfo(List<ClassificationWebsitInfo> classificationWebsitInfoList, Long uuid) {
        classificationWebsiteInfoRestService.updateClassificationInfo(classificationWebsitInfoList,uuid);
        classificationRestDao.updatefCaptured(uuid.intValue());
    }

    public Classification getClassificationgroupUuid(String group) {
        return classificationRestDao.getClassificationgroupUuid(group);
    }
}
