package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.TSMainKeywordDao;
import com.keymanager.monitoring.entity.TSMainKeyword;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
import com.keymanager.monitoring.vo.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
@Service
public class TSMainKeywordService extends ServiceImpl<TSMainKeywordDao, TSMainKeyword> {

    private static Logger logger = LoggerFactory.getLogger(TSMainKeywordService.class);

    @Autowired
    private TSMainKeywordDao tsMainKeywordDao;

    @Autowired
    private TSNegativeKeywordService tsNegativeKeywordService;

    public ModelAndView findTSMainKeywordsCode(int currentPage,int displaysRecords,String keyword,String  group){
        PageInfo<TSMainKeyword> pageInfo = new PageInfo<TSMainKeyword>();
        Map<String,Object> items = new HashMap<String, Object>();
        TSMainKeyword tsMainKeyword = new TSMainKeyword();
        pageInfo.setCurrentpage(currentPage);
        pageInfo.setDisplaysRecords(displaysRecords);
        items.put("pageInfo",pageInfo);
        tsMainKeyword.setKeyword(keyword);
        tsMainKeyword.setGroup(group);
        items.put("tsMainKeyword",tsMainKeyword);
        pageInfo.setTotalSize(getTSmainKeywordCount(tsMainKeyword));
        if (pageInfo.getCurrentpage()<1) {
            pageInfo.setCurrentpage(1);
        }
        else if (pageInfo.getCurrentpage()>=pageInfo.getTotalPage()) {
            pageInfo.setCurrentpage(pageInfo.getTotalPage());
        }
        List<TSMainKeyword> tsMainKeywords = searchTSMainKeywords(items);
        pageInfo.setContent(tsMainKeywords);
        Map<String,Object> searchCondition = new HashMap<String, Object>();
        searchCondition.put("keyword",keyword);
        searchCondition.put("group",group);
        pageInfo.setSearchCondition(searchCondition);
        ModelAndView modelAndView = new ModelAndView("/complaints/show");
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
    }

    public List<TSMainKeyword> searchTSMainKeywords(Map<String,Object> items) {
        List<TSMainKeyword> tsMainKeywordList = tsMainKeywordDao.findTSMainKeywords(items);
        for(TSMainKeyword tsMainKeyword : tsMainKeywordList){
            //负词需要根据mainkeyworduuid
            List<TSNegativeKeyword> tsNegativeKeywords = tsNegativeKeywordService.findNegativeKeywordsByMainKeywordUuid(tsMainKeyword.getUuid());
            tsMainKeyword.setTsNegativeKeywords(tsNegativeKeywords);
        }
        return tsMainKeywordList;
    }
    public TSMainKeyword searchTSMainKeyword(Long uuid) {
        TSMainKeyword tsMainKeyword = tsMainKeywordDao.selectById(uuid);
            //负词需要根据mainkeyworduuid
            if(null!=tsMainKeyword){
                List<TSNegativeKeyword> tsNegativeKeywords = tsNegativeKeywordService.findNegativeKeywordsByMainKeywordUuid(tsMainKeyword.getUuid());
                tsMainKeyword.setTsNegativeKeywords(tsNegativeKeywords);
            }
        return tsMainKeyword;
    }

    public void saveTSMainKeyword(TSMainKeyword tsMainKeyword) {
        TSMainKeyword oldTSMainKeyword = null;
        if(tsMainKeyword.getUuid() != null) {
            // update
            oldTSMainKeyword = tsMainKeywordDao.selectById(tsMainKeyword.getUuid());
        }else {
            List<TSMainKeyword> existingTSMainKeywords = tsMainKeywordDao.findTSMainKeywordByMainKeyword(tsMainKeyword.getKeyword());
            if(CollectionUtils.isNotEmpty(existingTSMainKeywords)){
                oldTSMainKeyword = existingTSMainKeywords.get(0);
            }
        }
        if(oldTSMainKeyword != null){
            oldTSMainKeyword.setKeyword(tsMainKeyword.getKeyword());
            oldTSMainKeyword.setGroup(tsMainKeyword.getGroup());
            oldTSMainKeyword.setUpdateTime(new Date());
            //修改主要针对负词表中的keyword进行逻辑删除
            List<TSNegativeKeyword> oldNegativeKeywords = tsNegativeKeywordService.findNegativeKeywordsByMainKeywordUuid(oldTSMainKeyword.getUuid());
            List<TSNegativeKeyword> newNegativeKeywords = tsMainKeyword.getTsNegativeKeywords();
            updateTSNegativeKeyword(oldNegativeKeywords,newNegativeKeywords,oldTSMainKeyword.getUuid());
            oldTSMainKeyword.setUpdateTime(new Date());
            tsMainKeywordDao.updateById(oldTSMainKeyword);
        } else {
            // save
            tsMainKeywordDao.insert(tsMainKeyword);
            Long tsMainKeywordUuid = new Long(tsMainKeywordDao.selectLastId());
            for(TSNegativeKeyword tsNegativeKeyword : tsMainKeyword.getTsNegativeKeywords()) {
                tsNegativeKeyword.setTsMainKeywordUuid(tsMainKeywordUuid);
                tsNegativeKeywordService.insert(tsNegativeKeyword);
            }
        }
    }
    public void updateTSNegativeKeyword(List<TSNegativeKeyword> oldNegativeKeywords,List<TSNegativeKeyword> newNegativeKeywords,Long tsMainKeywordUuid){
        Map<String ,TSNegativeKeyword> oldNegativeKeywordMap = new HashMap<String, TSNegativeKeyword>();
        for(TSNegativeKeyword oldTsNegativeKeyword :oldNegativeKeywords){
            oldNegativeKeywordMap.put(oldTsNegativeKeyword.getKeyword(),oldTsNegativeKeyword);
        }
        for(TSNegativeKeyword newTsNegativeKeyword : newNegativeKeywords){
           TSNegativeKeyword oldTsNegativeKeyword = oldNegativeKeywordMap.get(newTsNegativeKeyword.getKeyword());
           if(oldTsNegativeKeyword!=null){
               oldTsNegativeKeyword.setUpdateTime(new Date());
               tsNegativeKeywordService.updateById(oldTsNegativeKeyword);
               oldNegativeKeywordMap.remove(newTsNegativeKeyword.getKeyword());
           }else {
               newTsNegativeKeyword.setTsMainKeywordUuid(tsMainKeywordUuid);
               tsNegativeKeywordService.insert(newTsNegativeKeyword);
           }
        }
        //此时剩下在map中的对象
        for(TSNegativeKeyword oldTsNegativeKeyword : oldNegativeKeywordMap.values()){
            oldTsNegativeKeyword.setUpdateTime(new Date());
            oldTsNegativeKeyword.setIsDeleted(1);
            tsNegativeKeywordService.updateById(oldTsNegativeKeyword);
        }
    }

    public boolean deleteOne(Long uuid){//删除主词,负面词一并删除
        tsNegativeKeywordService.deleteByTSmainKeywordUuid(uuid);
        tsMainKeywordDao.deleteById(uuid);
        return true;
    }

    public boolean deleteAll(List<String> uuids){
        for(String uuid : uuids){
            deleteOne(Long.valueOf(uuid));
        }
        return true;
    }
    //总记录数
    public Integer getTSmainKeywordCount(TSMainKeyword tsMainKeyword){
        return tsMainKeywordDao.getTSMainKeywordCount(tsMainKeyword);
    }

    //爬虫专用
    public TSMainKeyword getTsMainKeywordsForComplaints(String ipCity){
        List<TSMainKeyword> tsMainKeywords = tsMainKeywordDao.getTSMainKeywordsByCity(ipCity);
        TSMainKeyword tsMainKeyword = null;
        if(CollectionUtils.isNotEmpty(tsMainKeywords)){
            tsMainKeyword=tsMainKeywords.get(0);
            List<TSNegativeKeyword> tsNegativeKeywords = tsNegativeKeywordService.findNegativeKeywordsByMainKeywordUuid(tsMainKeyword.getUuid());
            tsMainKeyword.setTsNegativeKeywords(tsNegativeKeywords);
            //设置更新时间
            startTsMainKeywordsForComplaints(tsMainKeyword.getUuid());
        }
        return tsMainKeyword;
    }
    private void startTsMainKeywordsForComplaints(Long uuid){
        TSMainKeyword tsMainKeyword  = tsMainKeywordDao.selectById(uuid);
        if(tsMainKeyword!=null){
            tsMainKeyword.setComplaintsTime(new Date());
            tsMainKeywordDao.updateById(tsMainKeyword);
        }
    }
    //投诉后修改数据库部分

}
