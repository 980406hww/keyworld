package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.entity.CustomerKeyword;
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
}
