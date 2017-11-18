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
import org.springframework.context.annotation.Bean;
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

    public Page<NegativeList> searchNegativeLists(Page<NegativeList> page, NegativeListCriteria negativeListCriteria) {
        page.setRecords(negativeListDao.searchNegativeLists(page, negativeListCriteria));
        return page;
    }


    public void saveNegativeList(NegativeList negativeList) {
        if (null != negativeList.getUuid()) {
            negativeList.setUpdateTime(new Date());
            negativeListDao.updateById(negativeList);
        } else {
            negativeList.setUpdateTime(new Date());
            //在opinion设置该值为负面
            KeywordNegativeCriteria keywordNegativeCriteria = new KeywordNegativeCriteria();
            keywordNegativeCriteria.setNegativeList(negativeList);
            keywordNegativeCriteria.setNegative(true);
            negativeListDao.insert(negativeList);
            negativeListsSynchronizeService.negativeListsSynchronize(keywordNegativeCriteria);
        }
    }

    public void saveNegativeLists(List<NegativeList> negativeLists , String operationType) {
        if (CollectionUtils.isNotEmpty(negativeLists)) {
            for (NegativeList negativeList : negativeLists) {
                NegativeListCriteria negativeListCriteria = new NegativeListCriteria();
                negativeListCriteria.setKeyword(negativeList.getKeyword());
                negativeListCriteria.setTerminalType(negativeList.getTerminalType());
                negativeListCriteria.setUrl(negativeList.getUrl());
                negativeListCriteria.setTitle(negativeList.getTitle());
                negativeListCriteria.setOriginalUrl(negativeList.getOriginalUrl());
                NegativeList existingNegativeLists = negativeListDao.searchNegativeListsFullMatching(negativeListCriteria);
                if(operationType.equals("update") && existingNegativeLists != null){
                    deleteNegativeList(existingNegativeLists.getUuid(), existingNegativeLists);
                }else {
                    if (existingNegativeLists != null) {
                        negativeList.setUuid(existingNegativeLists.getUuid());
                        negativeList.setCreateTime(existingNegativeLists.getCreateTime());
                    }
                    this.saveNegativeList(negativeList);
                }
            }
        }
    }

    public List<NegativeList> getSpecifiedKeywordNegativeLists(String terminalType, String keyword) {
        return negativeListDao.getSpecifiedKeywordNegativeLists(terminalType, keyword);
    }

    public NegativeList getNegativeList(long uuid) {
        NegativeList negativeList = negativeListDao.selectById(uuid);
        return negativeList;
    }

    public void deleteNegativeList(long uuid , NegativeList existingNegativeLists) {
        NegativeList negativeList = existingNegativeLists;
        if (negativeList == null) {
            negativeList = negativeListDao.selectById(uuid);
        }
        //在opinion设置该值为负面
        KeywordNegativeCriteria keywordNegativeCriteria = new KeywordNegativeCriteria();
        keywordNegativeCriteria.setNegativeList(negativeList);
        keywordNegativeCriteria.setNegative(false);
        negativeListDao.deleteById(uuid);
        negativeListsSynchronizeService.negativeListsSynchronize(keywordNegativeCriteria);
//        Boolean isDelete =
//        if(isDelete){
//
//        }
    }

    public void deleteAll(List<String> uuids) {
        for (String uuid : uuids) {
            deleteNegativeList(Long.valueOf(uuid) , null);
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
