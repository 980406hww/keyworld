package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.entity.CustomerExcludeKeyword;

public interface CustomerExcludeKeywordService {

    void excludeCustomerKeywords(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    String getCustomerExcludeKeyword(Long customerUuid, Long qzSettingUuid, String terminalType, String url);

    CustomerExcludeKeyword echoExcludeKeyword(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);
}
