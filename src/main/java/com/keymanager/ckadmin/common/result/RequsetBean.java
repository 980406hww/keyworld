package com.keymanager.ckadmin.common.result;

/**
 * @ClassName RequsetBean
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/31 12:41
 * @Version 1.0
 */
public class RequsetBean {
    private Integer page;
    private Integer limit;
    private String algorithmTestPlanName;

    public String getAlgorithmTestPlanName() {
        return algorithmTestPlanName;
    }

    public void setAlgorithmTestPlanName(String algorithmTestPlanName) {
        this.algorithmTestPlanName = algorithmTestPlanName;
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
}
