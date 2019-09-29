package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.keymanager.ckadmin.criteria.WarnListCriteria;
import com.keymanager.ckadmin.dao.WarnListDao;
import com.keymanager.ckadmin.entity.WarnList;
import com.keymanager.ckadmin.service.WarnListService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "warnListService2")
public class WarnListServiceImpl extends ServiceImpl<WarnListDao, WarnList> implements WarnListService {
    private static Logger logger = LoggerFactory.getLogger(WarnListServiceImpl.class);

    @Resource(name = "warnListDao2")
    private WarnListDao warnListDao;

    @Override
    public Page<WarnList> searchWarnLists(Page<WarnList> page, WarnListCriteria warnListCriteria) {
        page.setRecords(warnListDao.searchWarnLists(page, warnListCriteria));
        return page;
    }

    @Override
    public void saveWarnList(WarnList warnList) {
        if (null != warnList.getUuid()) {
            warnList.setUpdateTime(new Date());
            warnListDao.updateById(warnList);
        } else {
            warnList.setCreateTime(new Date());
            warnListDao.insert(warnList);
        }
    }

    @Override
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

    @Override
    public List<WarnList> getSpecifiedKeywordWarnLists(String keyword) {
        return warnListDao.getSpecifiedKeywordWarnLists(keyword);
    }

    @Override
    public WarnList getWarnList(long uuid) {
        WarnList warnList = warnListDao.selectById(uuid);
        return warnList;
    }

    @Override
    public void deleteWarnList(long uuid) {
        warnListDao.deleteById(uuid);
    }

    @Override
    public void deleteWarnLists(List<Integer> uuids) {
        for (Integer uuid : uuids) {
            deleteWarnList(Long.valueOf(uuid));
        }
    }
}
