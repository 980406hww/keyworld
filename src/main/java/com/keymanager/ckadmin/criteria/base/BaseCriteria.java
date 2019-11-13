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
    private String orderBy;
    private Integer orderMode = 1;
    private String init;

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getOrderMode() {
        return orderMode;
    }

    public void setOrderMode(Integer orderMode) {
        this.orderMode = orderMode;
    }
}
