package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClassificationRestDao;
import com.keymanager.monitoring.dao.ClassificationWebsiteInfoRestDao;
import com.keymanager.monitoring.entity.Classification;
import com.keymanager.monitoring.entity.ClassificationWebsitInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj22 on 2017/12/5.
 */
@Service
public class ClassificationWebsiteInfoRestService extends ServiceImpl<ClassificationWebsiteInfoRestDao,ClassificationWebsitInfo> {
    @Autowired
    private ClassificationWebsiteInfoRestDao classificationWebsiteInfoRestDao;

    public void updateClassificationInfo(List<ClassificationWebsitInfo> classificationWebsitInfoList, Long uuid) {
        ClassificationWebsitInfo classificationWebsitInfo = new ClassificationWebsitInfo();
        for (ClassificationWebsitInfo classificationWebsitInfos : classificationWebsitInfoList) {
            classificationWebsitInfo.setClassificationUuid(uuid);
            classificationWebsitInfo.setUrl(classificationWebsitInfos.getUrl() == null ?null:classificationWebsitInfos.getUrl().trim());
            classificationWebsitInfo.setTitle(classificationWebsitInfos.getTitle());
            classificationWebsitInfo.setOrder(classificationWebsitInfos.getOrder());
            classificationWebsitInfo.setHref(classificationWebsitInfos.getHref());
            classificationWebsitInfo.setHasOfficialWebsiteIndicator(classificationWebsitInfos.getHasOfficialWebsiteIndicator());
            classificationWebsitInfo.setDesc(classificationWebsitInfos.getDesc());
            classificationWebsitInfo.setEmailAddress(classificationWebsitInfos.getEmailAddress());
            classificationWebsitInfo.setTelPhone(classificationWebsitInfos.getTelPhone() == null ? null :classificationWebsitInfos.getTelPhone());
            classificationWebsitInfo.setCreateTime(new Date());
            classificationWebsiteInfoRestDao.insert(classificationWebsitInfo);
        }
    }
}
