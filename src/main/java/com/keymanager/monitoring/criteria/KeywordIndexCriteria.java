package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.vo.ExternalCustomerKeywordVO;
import java.util.List;

/**
 * @ClassName KeywordIndexCriteria
 * @Description 爬百度指数更新数据接口参数类
 * @Author lhc
 * @Date 2019/8/28 10:43
 * @Version 1.0
 */
public class KeywordIndexCriteria extends BaseCriteria{
    List<ExternalCustomerKeywordVO> customerKeywords;

    public List<ExternalCustomerKeywordVO> getCustomerKeywords() {
        return customerKeywords;
    }

    public void setCustomerKeywords(List<ExternalCustomerKeywordVO> customerKeywords) {
        this.customerKeywords = customerKeywords;
    }
}
