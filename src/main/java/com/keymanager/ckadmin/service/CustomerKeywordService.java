package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.vo.CustomerKeywordSummaryInfoVO;
import com.keymanager.ckadmin.vo.KeywordCountVO;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 关键字接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
public interface CustomerKeywordService extends IService<CustomerKeyword> {

    List<Map> getCustomerKeywordsCount(List<Long> customerUuids, String terminalType, String entryType);

    void changeCustomerKeywordStatus(String terminalType, String entryType, Long customerUuid, Integer status);

    void deleteCustomerKeywords(long customerUuid);

    int getCustomerKeywordCount(String terminalType, String entryType, long customerUuid);

    void excludeCustomerKeyword(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    void addCustomerKeyword(List<CustomerKeyword> customerKeywords, String userName);

    Integer getCustomerKeywordCountByOptimizeGroupName(String groupName);

    List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(String entryType, long customerUuid);

    KeywordCountVO getCustomerKeywordsCountByCustomerUuid(Long customerUuid, String terminalType);

    Page<CustomerKeyword> searchKeywords(Page<CustomerKeyword> page, KeywordCriteria keywordCriteria);

    void updateCustomerKeywordStatus(List<Long> customerKeywordUuids, Integer status);

    void updateOptimizeGroupName(KeywordCriteria keywordCriteria);

    void updateMachineGroup(KeywordCriteria keywordCriteria);

    void updateBearPawNumber(KeywordCriteria keywordCriteria);

    void deleteCustomerKeywordsByDeleteType(KeywordCriteria keywordCriteria);

    Page<CustomerKeyword> searchCustomerKeywords(Page<CustomerKeyword> page, KeywordCriteria keywordCriteria);

    void saveCustomerKeyword(CustomerKeyword customerKeyword, String userName);

    void updateCustomerKeywordFromUI(CustomerKeyword customerKeyword, String userName);

    void updateOptimizePlanCount(KeywordCriteria keywordCriteria);

    boolean handleExcel(InputStream inputStream, String excelType, int parseInt, String entry, String terminalType, String userName) throws Exception;
}
