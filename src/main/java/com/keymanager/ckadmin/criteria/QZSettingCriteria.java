package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

/**
 * @ClassName QZSettingCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/6 18:24
 * @Version 1.0
 */
public class QZSettingCriteria extends BaseCriteria {
    private String ternimalType;
    private String searchEngine;

    public String getTernimalType() {
        return ternimalType;
    }

    public void setTernimalType(String ternimalType) {
        this.ternimalType = ternimalType;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }
}
