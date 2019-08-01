package com.keymanager.monitoring.criteria;

/**
 * @author wjianwu 2019/7/1 17:32
 */
public class ExternalCaptureJobCriteria extends BaseCriteria {

    private String rankJobType;
    private String rankJobArea;
    private String rankJobCity;

    public String getRankJobType() {
        return rankJobType;
    }

    public void setRankJobType(String rankJobType) {
        this.rankJobType = rankJobType;
    }

    public String getRankJobArea() {
        return rankJobArea;
    }

    public void setRankJobArea(String rankJobArea) {
        this.rankJobArea = rankJobArea;
    }

    public String getRankJobCity() {
        return rankJobCity;
    }

    public void setRankJobCity(String rankJobCity) {
        this.rankJobCity = rankJobCity;
    }
}
