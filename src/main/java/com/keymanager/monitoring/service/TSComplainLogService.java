package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.TSComplainLogDao;
import com.keymanager.monitoring.entity.TSComplainLog;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/2.
 */
@Service
public class TSComplainLogService extends ServiceImpl<TSComplainLogDao, TSComplainLog> {

    private static Logger logger = LoggerFactory.getLogger(TSComplainLogService.class);

    @Autowired
    private TSComplainLogDao tsComplainLogDao;

    public void addComplainLogByNegativeKeywords(List<TSNegativeKeyword> tsNegativeKeywordList) {
        for(TSNegativeKeyword tsNegativeKeyword : tsNegativeKeywordList) {
            List<String> pcAppearedKeywordTypeList = tsNegativeKeyword.getPcAppearedKeywordTypes();
            List<String> phoneAppearedKeywordTypeList = tsNegativeKeyword.getPhoneAppearedKeywordTypes();

            if(pcAppearedKeywordTypeList != null) {
                for(String pcAppearedKeywordType : pcAppearedKeywordTypeList) {
                    TSComplainLog tsComplainLog = new TSComplainLog();
                    tsComplainLog.setTsNegativeKeywordUuid(tsNegativeKeyword.getUuid());
                    tsComplainLog.setTerminalType(TerminalTypeEnum.PC.name());
                    tsComplainLog.setAppearedKeywordTypes(pcAppearedKeywordType);
                    tsComplainLog.setUpdateTime(new Date());
                    tsComplainLogDao.insert(tsComplainLog);
                }
            }

            if(phoneAppearedKeywordTypeList != null) {
                for(String phoneAppearedKeywordType : phoneAppearedKeywordTypeList) {
                    TSComplainLog tsComplainLog = new TSComplainLog();
                    tsComplainLog.setTsNegativeKeywordUuid(tsNegativeKeyword.getUuid());
                    tsComplainLog.setTerminalType(TerminalTypeEnum.Phone.name());
                    tsComplainLog.setAppearedKeywordTypes(phoneAppearedKeywordType);
                    tsComplainLog.setUpdateTime(new Date());
                    tsComplainLogDao.insert(tsComplainLog);
                }
            }
        }
    }

}
