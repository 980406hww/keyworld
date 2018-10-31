package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.WarnListCriteria;
import com.keymanager.monitoring.dao.WarnListDao;
import com.keymanager.monitoring.entity.WarnList;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WarnListService extends ServiceImpl<WarnListDao, WarnList> {

    private static Logger logger = LoggerFactory.getLogger(WarnListService.class);

    @Autowired
    private WarnListDao warnListDao;

    public Page<WarnList> searchWarnLists(Page<WarnList> page, WarnListCriteria warnListCriteria) {
        page.setRecords(warnListDao.searchWarnLists(page, warnListCriteria));
        return page;
    }

    public void saveWarnList(WarnList warnList) {
        if (null != warnList.getUuid()) {
            warnList.setUpdateTime(new Date());
            warnListDao.updateById(warnList);
        } else {
            warnList.setCreateTime(new Date());
            warnListDao.insert(warnList);
        }
    }

    public void saveWarnLists(List<WarnList> warnLists , String operationType) {
        if (CollectionUtils.isNotEmpty(warnLists)) {
            for (WarnList warnList : warnLists) {
                WarnListCriteria warnListCriteria = new WarnListCriteria();
                warnListCriteria.setKeyword(warnList.getKeyword());
                warnListCriteria.setTitle(warnList.getTitle());
                List<WarnList> existingWarnLists = warnListDao.searchWarnListsFullMatching(warnListCriteria);
                if(operationType.equals("update")){
                    for (WarnList existingWarnList : existingWarnLists) {
                        deleteWarnList(existingWarnList.getUuid());
                    }
                }else {
                    if (CollectionUtils.isNotEmpty(existingWarnLists)) {
                        WarnList existingWarnList = existingWarnLists.get(0);
                        warnList.setUuid(existingWarnList.getUuid());
                        warnList.setCreateTime(existingWarnList.getCreateTime());
                    }
                    this.saveWarnList(warnList);
                }
            }
        }
    }

    public List<WarnList> getSpecifiedKeywordWarnLists(String keyword) {
        return warnListDao.getSpecifiedKeywordWarnLists(keyword);
    }

    public WarnList getWarnList(long uuid) {
        WarnList warnList = warnListDao.selectById(uuid);
        return warnList;
    }

    public void deleteWarnList(long uuid) {
        warnListDao.deleteById(uuid);
    }

    public void deleteWarnLists(List<String> uuids) {
        for (String uuid : uuids) {
            deleteWarnList(Long.valueOf(uuid));
        }
    }

}
