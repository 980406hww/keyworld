package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.keymanager.ckadmin.criteria.KeywordNegativeCriteria;
import com.keymanager.ckadmin.criteria.NegativeListCriteria;
import com.keymanager.ckadmin.dao.NegativeListDao;
import com.keymanager.ckadmin.entity.NegativeList;
import com.keymanager.ckadmin.service.NegativeListService;
import com.keymanager.ckadmin.service.NegativeListUpdateInfoService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("negativeListService2")
public class NegativeListServiceImpl extends ServiceImpl<NegativeListDao, NegativeList> implements NegativeListService {

    private static Logger logger = LoggerFactory.getLogger(NegativeListServiceImpl.class);

    @Resource(name = "negativeListDao2")
    private NegativeListDao negativeListDao;

    @Resource(name = "negativeListsSynchronizeService2")
    private NegativeListsSynchronizeService negativeListsSynchronizeService;

    @Resource(name = "negativeListUpdateInfoService2")
    private NegativeListUpdateInfoService negativeListUpdateInfoService;

    @Override
    public Page<NegativeList> searchNegativeLists(Page<NegativeList> page, NegativeListCriteria negativeListCriteria) {
        page.setRecords(negativeListDao.searchNegativeLists(page, negativeListCriteria));
        return page;
    }

    @Override
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
        this.negativeListCacheEvict(negativeList.getKeyword());
    }

    @Override
    public void saveNegativeLists(List<NegativeList> negativeLists, String operationType) {
        if (CollectionUtils.isNotEmpty(negativeLists)) {
            for (NegativeList negativeList : negativeLists) {
                NegativeListCriteria negativeListCriteria = new NegativeListCriteria();
                negativeListCriteria.setKeyword(negativeList.getKeyword());
                negativeListCriteria.setTitle(negativeList.getTitle());
                List<NegativeList> existingNegativeLists = negativeListDao.searchNegativeListsFullMatching(negativeListCriteria);
                if ("update".equals(operationType)) {
                    for (NegativeList existingNegativeList : existingNegativeLists) {
                        deleteNegativeList(existingNegativeList.getUuid(), existingNegativeList);
                    }
                } else {
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

    @Override
    public List<NegativeList> getSpecifiedKeywordNegativeLists(String keyword) {
        return negativeListDao.getSpecifiedKeywordNegativeLists(keyword);
    }

    @Override
    public NegativeList getNegativeList(long uuid) {
        return negativeListDao.selectById(uuid);
    }

    @Override
    public void deleteNegativeList(long uuid, NegativeList negativeList) {
        //在opinion设置该值为负面
        KeywordNegativeCriteria keywordNegativeCriteria = new KeywordNegativeCriteria();
        keywordNegativeCriteria.setNegativeList(negativeList);
        keywordNegativeCriteria.setNegative(false);
        negativeListDao.deleteById(uuid);
        negativeListsSynchronizeService.negativeListsSynchronize(keywordNegativeCriteria);
        // 设置关键词负面清单更新时间
        negativeListUpdateInfoService.saveNegativeListUpdateInfo(negativeList.getKeyword());
        // 删除关键词缓存
        this.negativeListCacheEvict(negativeList.getKeyword());
    }

    @Override
    public void deleteAll(List<Integer> uuids) {
        for (Integer uuid : uuids) {
            NegativeList negativeList = negativeListDao.selectById(uuid);
            deleteNegativeList(Long.valueOf(uuid), negativeList);
        }
    }

    @Override
    public List<NegativeList> negativeListsSynchronizeOfDelete(NegativeList negativeList) {
        List<NegativeList> negativeLists = negativeListDao.negativeListsSynchronizeOfDelete(negativeList);
        if (negativeLists == null || negativeLists.isEmpty()) {
            return null;
        }
        return negativeLists;
    }

    @Override
    @CacheEvict(value = "negativeList", key = "#keyword")
    public void negativeListCacheEvict(String keyword) {
    }

    @Override
    @CacheEvict(value = "negativeList", allEntries = true)
    public void evictAllNegativeListCache() {
    }
}
