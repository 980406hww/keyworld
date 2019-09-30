package com.keymanager.ckadmin.vo;

import com.keymanager.ckadmin.entity.CustomerKeyword;
import java.util.List;

/**
 * @ClassName KeywordStatusBatchUpdateVO
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/27 10:47
 * @Version 1.0
 */
public class KeywordStatusBatchUpdateVO {
    private CustomerKeyword keywordChecks;
    private CustomerKeyword keywordValues;
    private List<String> uuids;

    public CustomerKeyword getKeywordChecks() {
        return keywordChecks;
    }

    public void setKeywordChecks(CustomerKeyword keywordChecks) {
        this.keywordChecks = keywordChecks;
    }

    public CustomerKeyword getKeywordValues() {
        return keywordValues;
    }

    public void setKeywordValues(CustomerKeyword keywordValues) {
        this.keywordValues = keywordValues;
    }

    public List<String> getUuids() {
        return uuids;
    }

    public void setUuids(List<String> uuids) {
        this.uuids = uuids;
    }
}
