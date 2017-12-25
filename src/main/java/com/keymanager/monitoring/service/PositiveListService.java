package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.PositiveListCriteria;
import com.keymanager.monitoring.dao.PositiveListDao;
import com.keymanager.monitoring.entity.PositiveList;
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

    public Page<PositiveList> searchPositiveLists(Page<PositiveList> page, PositiveListCriteria positiveListCriteria) {
        page.setRecords(positiveListDao.searchPositiveLists(page, positiveListCriteria));
        return page;
    }

    public void savePositiveList(PositiveList positiveList) {
        positiveList.setUpdateTime(new Date());
        if (null != positiveList.getUuid()) {
            positiveListDao.updateById(positiveList);
        } else {
            positiveListDao.insert(positiveList);
        }
    }

    public void savePositiveLists(List<PositiveList> positiveLists , String operationType) {
        if (CollectionUtils.isNotEmpty(positiveLists)) {
            for (PositiveList positiveList : positiveLists) {
                PositiveListCriteria positiveListCriteria = new PositiveListCriteria();
                positiveListCriteria.setKeyword(positiveList.getKeyword());
                positiveListCriteria.setTerminalType(positiveList.getTerminalType());
                positiveListCriteria.setUrl(positiveList.getUrl());
                positiveListCriteria.setTitle(positiveList.getTitle());
                positiveListCriteria.setOriginalUrl(positiveList.getOriginalUrl());
                List<PositiveList> existingPositiveLists = positiveListDao.searchPositiveListsFullMatching(positiveListCriteria);
                if(operationType.equals("update")){
                    for (PositiveList existingPositiveList : existingPositiveLists) {
                        deletePositiveList(existingPositiveList.getUuid());
                    }
                }else {
                    if (existingPositiveLists.size() > 0) {
                        PositiveList existingPositiveList = existingPositiveLists.get(0);
                        positiveList.setUuid(existingPositiveList.getUuid());
                        positiveList.setCreateTime(existingPositiveList.getCreateTime());
                    }
                    this.savePositiveList(positiveList);
                }
            }
        }
    }

    public List<PositiveList> getSpecifiedKeywordPositiveLists(String terminalType, String keyword) {
        return positiveListDao.getSpecifiedKeywordPositiveLists(terminalType, keyword);
    }

    public PositiveList getPositiveList(long uuid) {
        PositiveList positiveList = positiveListDao.selectById(uuid);
        return positiveList;
    }

    public void deletePositiveList(long uuid) {
        positiveListDao.deleteById(uuid);
    }

    public void deletePositiveLists(List<String> uuids) {
        for (String uuid : uuids) {
            deletePositiveList(Long.valueOf(uuid));
        }
    }

}
