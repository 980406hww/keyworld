package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerKeywordDao2")
public interface
CustomerKeywordDao extends BaseMapper<com.keymanager.ckadmin.entity.CustomerKeyword> {

    List<Map> getCustomerKeywordsCount(@Param("customerUuids") List<Long> customerUuids,
        @Param("terminalType") String terminalType, @Param
        ("entryType") String entryType);

    void changeCustomerKeywordStatus(@Param("terminalType") String terminalType,
        @Param("entryType") String entryType,
        @Param("customerUuid") Long customerUuid, @Param("status") Integer status);

    void deleteCustomerKeywordsByCustomerUuid(long customerUuid);

    int getCustomerKeywordCount(@Param("terminalType") String terminalType,
        @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    void excludeCustomerKeyword(
        @Param("qzSettingExcludeCustomerKeywordsCriteria") QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    void addCustomerKeywords(
        @Param("customerKeywords") ArrayList<CustomerKeyword> customerKeywords);

    Integer getSameCustomerKeywordCount(@Param("terminalType") String terminalType,
        @Param("customerUuid") long customerUuid, @Param("keyword") String keyword,
        @Param("url") String url, @Param("title") String title);

    void updateSameCustomerKeywordSource(@Param("terminalType") String terminalType,
        @Param("customerUuid") long customerUuid, @Param("keyword") String keyword,
        @Param("url") String url, @Param("title") String title,
        @Param("customerKeywordSource") String customerKeywordSource);

    int getSimilarCustomerKeywordCount(@Param("terminalType") String terminalType,
        @Param("customerUuid") long customerUuid, @Param("keyword") String keyword,
        @Param("terminaloriginalUrlType") String originalUrl, @Param("String") String title);

    void updateSimilarCustomerKeywordSource(@Param("terminalType") String terminalType,
        @Param("customerUuid") long customerUuid, @Param("keyword") String keyword,
        @Param("url") String url, @Param("title") String title,
        @Param("customerKeywordSource") String customerKeywordSource);
}