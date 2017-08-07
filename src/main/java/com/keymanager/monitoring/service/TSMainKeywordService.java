package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.TSMainKeywordDao;
import com.keymanager.monitoring.entity.TSMainKeyword;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
import com.keymanager.monitoring.vo.TSmainKeyWordVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<TSMainKeyword> searchTSMainKeywords(Map<String,Object> items) {
        List<TSMainKeyword> tsMainKeywordList = tsMainKeywordDao.findTSMainKeywords(items);
        for(TSMainKeyword tsMainKeyword : tsMainKeywordList){
            //负词需要根据mainkeyworduuid
            List<TSNegativeKeyword> tsNegativeKeywords = tsNegativeKeywordService.findNegativeKeywordsBymainkeyUuid(tsMainKeyword.getUuid());
            tsMainKeyword.setTsNegativeKeywords(tsNegativeKeywords);
        }
        return tsMainKeywordList;
    }
    public TSMainKeyword searchTSMainKeyword(Long uuid) {
        TSMainKeyword tsMainKeyword = tsMainKeywordDao.selectById(uuid);
            //负词需要根据mainkeyworduuid
            if(null!=tsMainKeyword){
                List<TSNegativeKeyword> tsNegativeKeywords = tsNegativeKeywordService.findNegativeKeywordsBymainkeyUuid(tsMainKeyword.getUuid());
                tsMainKeyword.setTsNegativeKeywords(tsNegativeKeywords);
            }
        return tsMainKeyword;
    }

    public void saveTSMainKeyword(TSMainKeyword tsMainKeyword) {
        if(tsMainKeyword.getUuid() != null) {
            // update
            TSMainKeyword oldTSMainKeyword = tsMainKeywordDao.selectById(tsMainKeyword.getUuid());
            oldTSMainKeyword.setKeyword(tsMainKeyword.getKeyword());
            oldTSMainKeyword.setGroup(tsMainKeyword.getGroup());
            oldTSMainKeyword.setUpdateTime(new Date());
            //修改主要针对负词表中的keyword进行逻辑删除
            List<TSNegativeKeyword> oldNegativeKeywords = tsNegativeKeywordService.findNegativeKeywordsBymainkeyUuid(tsMainKeyword.getUuid());
            List<TSNegativeKeyword> newNegativeKeywords = tsMainKeyword.getTsNegativeKeywords();
            updateTSnegativeKeyword(oldNegativeKeywords,newNegativeKeywords,tsMainKeyword.getUuid());
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
    public void updateTSnegativeKeyword(List<TSNegativeKeyword> oldNegativeKeywords,List<TSNegativeKeyword> newNegativeKeywords,Long tsMainKeywordUuid){
        Map<String ,TSNegativeKeyword> oldmap = new HashMap<String, TSNegativeKeyword>();
        for(TSNegativeKeyword oldTsNegativeKeyword :oldNegativeKeywords){
            oldmap.put(oldTsNegativeKeyword.getKeyword(),oldTsNegativeKeyword);
        }
        for(TSNegativeKeyword newTsNegativeKeyword :newNegativeKeywords){
           TSNegativeKeyword oldTsNegativeKeyword = oldmap.get(newTsNegativeKeyword.getKeyword());
           if(oldTsNegativeKeyword!=null){
               oldTsNegativeKeyword.setUpdateTime(new Date());
               tsNegativeKeywordService.updateById(oldTsNegativeKeyword);
               oldmap.remove(newTsNegativeKeyword.getKeyword());
           }else {
               newTsNegativeKeyword.setTsMainKeywordUuid(tsMainKeywordUuid);
               tsNegativeKeywordService.insert(newTsNegativeKeyword);
           }
        }
        //此时剩下在map中的对象
        for(TSNegativeKeyword oldTsNegativeKeyword : oldmap.values()){
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
        return tsMainKeywordDao.getTSmainKeywordCount(tsMainKeyword);
    }

    //爬虫专用
    public TSMainKeyword getTsMainKeywordsForComplaints(){
        List<TSMainKeyword> tsMainKeywords = tsMainKeywordDao.getTsMainKeywordsForComplaints();
        TSMainKeyword tsMainKeyword = null;
        if(CollectionUtils.isNotEmpty(tsMainKeywords)){
            tsMainKeyword=tsMainKeywords.get(0);
            List<TSNegativeKeyword> tsNegativeKeywords = tsNegativeKeywordService.findNegativeKeywordsBymainkeyUuid(tsMainKeyword.getUuid());
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

    //邮件内容部分
    public List<TSmainKeyWordVO> complaintsReportContent() {
        List<TSmainKeyWordVO> TSmainKeyWordVOS = tsNegativeKeywordService.complaintsReportContent();
        return TSmainKeyWordVOS;
    }

}
