package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.NegativeListCriteria;
import com.keymanager.monitoring.dao.NegativeListDao;
import com.keymanager.monitoring.entity.NegativeList;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Page<NegativeList> searchNegativeLists(Page<NegativeList> page, NegativeListCriteria negativeListCriteria){
        page.setRecords(negativeListDao.searchNegativeLists(page, negativeListCriteria));
        return page;
    }

    public Boolean updateNegativeList(NegativeList negativeList) {
        NegativeList oldNegativeList = negativeListDao.selectById(negativeList.getUuid());
        if (oldNegativeList != null) {
            //oldNegativeList.setTerminalType(negativeList.getTerminalType());
            oldNegativeList.setKeyword(negativeList.getKeyword());
            oldNegativeList.setTitle(negativeList.getTitle());
            oldNegativeList.setUrl(negativeList.getUrl());
            oldNegativeList.setDesc(negativeList.getDesc());
            oldNegativeList.setPosition(negativeList.getPosition());
            oldNegativeList.setUpdateTime(new Date());
            negativeListDao.updateById(oldNegativeList);
            return true;
        }
        return false;
    }

    public Boolean addNegativeList(NegativeList negativeList){
        // update
        if(null != negativeList.getUuid()){
            Boolean updateflag = updateNegativeList(negativeList);
            return updateflag;
        }else{
            // add
            negativeList.setUpdateTime(new Date());
            negativeListDao.insert(negativeList);
            return true;
        }
    }

    public void saveNegativeLists(List<NegativeList> negativeLists){
        if(CollectionUtils.isNotEmpty(negativeLists)){
            for(NegativeList negativeList : negativeLists){
                negativeList.setUpdateTime(new Date());
                negativeListDao.insert(negativeList);
            }
        }
    }

    public List<NegativeList> getSpecifiedKeywordNegativeLists(String terminalType, String keyword){
        return negativeListDao.getSpecifiedKeywordNegativeLists(terminalType, keyword);
    }

    public NegativeList getNegativeList(long uuid){
        NegativeList negativeList = negativeListDao.selectById(uuid);
        return negativeList;
    }

    public boolean deleteNegativeList(long uuid){
        try {
            negativeListDao.deleteById(uuid);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean deleteAll(List<String> uuids){
        try {
            for(String uuid :uuids){
                deleteNegativeList(Long.valueOf(uuid));
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
