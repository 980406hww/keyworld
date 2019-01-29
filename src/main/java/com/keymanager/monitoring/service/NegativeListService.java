package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.KeywordNegativeCriteria;
import com.keymanager.monitoring.criteria.NegativeListCriteria;
import com.keymanager.monitoring.dao.NegativeListDao;
import com.keymanager.monitoring.entity.NegativeList;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/29.
 */
@Service
public class NegativeListService extends ServiceImpl<NegativeListDao, NegativeList> {

    private static Logger logger = LoggerFactory.getLogger(NegativeListService.class);

    @Autowired
    private NegativeListDao negativeListDao;

    @Autowired
    private NegativeListsSynchronizeService negativeListsSynchronizeService;

    @Autowired
    private NegativeListUpdateInfoService negativeListUpdateInfoService;

    @Autowired
    private NegativeListCacheService negativeListCacheService;

    public Page<NegativeList> searchNegativeLists(Page<NegativeList> page, NegativeListCriteria negativeListCriteria) {
        page.setRecords(negativeListDao.searchNegativeLists(page, negativeListCriteria));
        return page;
    }

    public void saveNegativeList(NegativeList negativeList) {
        negativeListUpdateInfoService.saveNegativeListUpdateInfo(negativeList.getKeyword());
        if (null != negativeList.getUuid()) {
            negativeList.setUpdateTime(new Date());
            negativeListDao.updateById(negativeList);
        } else {
            negativeList.setCreateTime(new Date());
            //在opinion设置该值为负面
            KeywordNegativeCriteria keywordNegativeCriteria = new KeywordNegativeCriteria();
            keywordNegativeCriteria.setNegativeList(negativeList);
            keywordNegativeCriteria.setNegative(true);
            negativeListDao.insert(negativeList);
            negativeListsSynchronizeService.negativeListsSynchronize(keywordNegativeCriteria);
        }
        negativeListCacheService.negativeListCacheEvict(negativeList.getKeyword());
    }

    public void saveNegativeLists(List<NegativeList> negativeLists , String operationType) {
        if (CollectionUtils.isNotEmpty(negativeLists)) {
            for (NegativeList negativeList : negativeLists) {
                NegativeListCriteria negativeListCriteria = new NegativeListCriteria();
                negativeListCriteria.setKeyword(negativeList.getKeyword());
                negativeListCriteria.setTitle(negativeList.getTitle());
                List<NegativeList> existingNegativeLists = negativeListDao.searchNegativeListsFullMatching(negativeListCriteria);
                if(operationType.equals("update")){
                    for (NegativeList existingNegativeList : existingNegativeLists) {
                        deleteNegativeList(existingNegativeList.getUuid(), existingNegativeList);
                    }
                }else {
                    if (CollectionUtils.isNotEmpty(existingNegativeLists)) {
                        NegativeList existingNegativeList = existingNegativeLists.get(0);
                        negativeList.setUuid(existingNegativeList.getUuid());
                        negativeList.setCreateTime(existingNegativeList.getCreateTime());
                    }
                    this.saveNegativeList(negativeList);
                }
            }
        }
    }

    @Cacheable(value = "negativeList", key = "#keyword")
    public List<NegativeList> getSpecifiedKeywordNegativeLists(String keyword) {
        return negativeListDao.getSpecifiedKeywordNegativeLists(keyword);
    }

    public NegativeList getNegativeList(long uuid) {
        NegativeList negativeList = negativeListDao.selectById(uuid);
        return negativeList;
    }

    public void deleteNegativeList(long uuid , NegativeList negativeList) {
        //在opinion设置该值为负面
        KeywordNegativeCriteria keywordNegativeCriteria = new KeywordNegativeCriteria();
        keywordNegativeCriteria.setNegativeList(negativeList);
        keywordNegativeCriteria.setNegative(false);
        negativeListDao.deleteById(uuid);
        // negativeListsSynchronizeService.negativeListsSynchronize(keywordNegativeCriteria);
        //设置关键词负面清单更新时间
        negativeListUpdateInfoService.saveNegativeListUpdateInfo(negativeList.getKeyword());
        // 删除关键词缓存
        negativeListCacheService.negativeListCacheEvict(negativeList.getKeyword());
    }

    public void deleteAll(List<String> uuids) {
        for (String uuid : uuids) {
            NegativeList negativeList = negativeListDao.selectById(uuid);
            deleteNegativeList(Long.valueOf(uuid) ,negativeList);
        }
    }

    public List<NegativeList> negativeListsSynchronizeOfDelete(NegativeList negativeList){
        List<NegativeList> negativeLists = negativeListDao.negativeListsSynchronizeOfDelete(negativeList);
        if (negativeLists == null || negativeLists.isEmpty()) {
            return null;
        }
        return negativeLists;
    }
}
