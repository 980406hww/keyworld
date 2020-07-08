package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.QzCustomerKeywordTemporaryDao;
import com.keymanager.monitoring.entity.QzCustomerKeywordTemporary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("qzCustomerKeywordTemporaryService2")
public class QzCustomerKeywordTemporaryService extends ServiceImpl<QzCustomerKeywordTemporaryDao, QzCustomerKeywordTemporary> {

    @Autowired
    private QzCustomerKeywordTemporaryDao qzCustomerKeywordTemporaryDao;

    public void migrationRecordToQzCustomerKeyword(Long qsId, String type) {
        qzCustomerKeywordTemporaryDao.migrationRecordToQzCustomerKeyword(qsId, type);
    }

    public void cleanQzCustomerKeyword() {
        qzCustomerKeywordTemporaryDao.cleanQzCustomerKeyword();
    }

    public int searchQzKeywordTemporaryCount() {
        return qzCustomerKeywordTemporaryDao.searchQzKeywordTemporaryCount();
    }

    public void updateQzKeywordMarks(int rows, int mark, int targetMark) {
        qzCustomerKeywordTemporaryDao.updateQzKeywordMarks(rows, mark, targetMark);
    }

    public void insertIntoTemporaryData(Long qsId) {
        qzCustomerKeywordTemporaryDao.insertIntoTemporaryData(qsId);
    }

    public void updateQzKeywordOperaStatus(long qsId) {
        qzCustomerKeywordTemporaryDao.updateQzKeywordOperaStatus(qsId);
    }
}
