package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.PositiveListCriteria;
import com.keymanager.monitoring.dao.PositiveListDao;
import com.keymanager.monitoring.dao.PositiveListUpdateInfoDao;
import com.keymanager.monitoring.entity.PositiveList;
import com.keymanager.monitoring.entity.PositiveListUpdateInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PositiveListService extends ServiceImpl<PositiveListDao, PositiveList> {

    private static Logger logger = LoggerFactory.getLogger(PositiveListService.class);

    @Autowired
    private PositiveListDao positiveListDao;

    @Autowired
    private PositiveListUpdateInfoService positiveListUpdateInfoService;

    public Page<PositiveList> searchPositiveLists(Page<PositiveList> page, PositiveListCriteria positiveListCriteria) {
        page.setRecords(positiveListDao.searchPositiveLists(page, positiveListCriteria));
        return page;
    }

    public void savePositiveList(PositiveList positiveList) {
        if (null != positiveList.getUuid()) {
            positiveList.setUpdateTime(new Date());
            positiveListDao.updateById(positiveList);
        } else {
            positiveList.setCreateTime(new Date());
            positiveListDao.insert(positiveList);
            positiveListUpdateInfoService.savePositiveListUpdateInfo(positiveList);
        }
    }

    public void savePositiveLists(List<PositiveList> positiveLists , String operationType, String btnType) {
        if (CollectionUtils.isNotEmpty(positiveLists)) {
            for (PositiveList positiveList : positiveLists) {
                PositiveListCriteria positiveListCriteria = new PositiveListCriteria();
                positiveListCriteria.setKeyword(positiveList.getKeyword());
                positiveListCriteria.setTitle(positiveList.getTitle());
                positiveListCriteria.setTerminalType(positiveList.getTerminalType());
                List<PositiveList> existingPositiveLists = positiveListDao.searchPositiveListsFullMatching(positiveListCriteria);
                if(operationType.equals("update")){
                    for (PositiveList existingPositiveList : existingPositiveLists) {
                        if (btnType.equals("1")) {
                            existingPositiveList.setOptimizeMethod(positiveList.getOptimizeMethod());
                            updatePositiveList(existingPositiveList);
                            positiveListUpdateInfoService.savePositiveListUpdateInfo(existingPositiveList);
                        } else {
                            deletePositiveList(existingPositiveList.getUuid());
                        }
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(existingPositiveLists)) {
                        PositiveList existingPositiveList = existingPositiveLists.get(0);
                        positiveList.setUuid(existingPositiveList.getUuid());
                        positiveList.setCreateTime(existingPositiveList.getCreateTime());
                        if (btnType.equals("1")) {
                            positiveListUpdateInfoService.savePositiveListUpdateInfo(positiveList);
                        }
                    }
                    this.savePositiveList(positiveList);
                }
            }
        }
    }

    public List<PositiveList> getSpecifiedKeywordPositiveLists(String keyword, String terminalType) {
        List<PositiveList> positiveLists = positiveListDao.getSpecifiedKeywordPositiveLists(keyword, terminalType);
        if (CollectionUtils.isNotEmpty(positiveLists)) {
            for (PositiveList positiveList : positiveLists) {
                List<PositiveListUpdateInfo> positiveListUpdateInfos = positiveListUpdateInfoService.findPositiveListUpdateInfos(positiveList.getUuid());
                positiveList.setPositiveListUpdateInfos(positiveListUpdateInfos);
            }
        }
        return positiveLists;
    }

    public PositiveList getPositiveList(long uuid) {
        return positiveListDao.selectById(uuid);
    }

    public void deletePositiveList(long uuid) {
        positiveListDao.deleteById(uuid);
    }

    public void updatePositiveList(PositiveList positiveList){
        positiveListDao.updateById(positiveList);
    }

    public void deletePositiveLists(List<String> uuids) {
        for (String uuid : uuids) {
            deletePositiveList(Long.valueOf(uuid));
        }
    }

}
