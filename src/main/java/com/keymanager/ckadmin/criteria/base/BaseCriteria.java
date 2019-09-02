package com.keymanager.ckadmin.criteria.base;

/**
 * @ClassName BaseCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/2 9:28
 * @Version 1.0
 */
public class BaseCriteria {
    private Integer page;
    private Integer limit;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
