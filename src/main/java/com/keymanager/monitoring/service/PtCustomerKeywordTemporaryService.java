package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.PtCustomerKeywordTemporaryDao;
import com.keymanager.monitoring.entity.PtCustomerKeywordTemporary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PtCustomerKeywordTemporaryService extends ServiceImpl<PtCustomerKeywordTemporaryDao, PtCustomerKeywordTemporary> {

    @Autowired
    private PtCustomerKeywordTemporaryDao ptCustomerKeywordTemporaryDao;


    public void migrationRecordToPtCustomerKeyword(Long cusId, String type) {
        ptCustomerKeywordTemporaryDao.migrationRecordToPtCustomerKeyword(cusId, type);
    }

    public void cleanPtCustomerKeyword() {
        ptCustomerKeywordTemporaryDao.cleanPtCustomerKeyword();
    }

    public int searchPtKeywordTemporaryCount() {
        return ptCustomerKeywordTemporaryDao.searchPtKeywordTemporaryCount();
    }

    public void updatePtKeywordMarks(int rows, int mark) {
        ptCustomerKeywordTemporaryDao.updatePtKeywordMarks(rows, mark);
    }

    public void insertIntoTemporaryData(Long cusId, String type) {
        ptCustomerKeywordTemporaryDao.insertIntoTemporaryData(cusId, type);
    }
}
