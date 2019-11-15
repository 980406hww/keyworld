package com.keymanager.ckadmin.vo;

public class CustomerKeywordMonCountVO {

    private Long topThreeCount;
    private Long topFiveCount;
    private Long topTenCount;
    private Long topFifthCount;
    private String date;

    public Long getTopThreeCount() {
        return topThreeCount;
    }

    public void setTopThreeCount(Long topThreeCount) {
        this.topThreeCount = topThreeCount;
    }

    public Long getTopFiveCount() {
        return topFiveCount;
    }

    public void setTopFiveCount(Long topFiveCount) {
        this.topFiveCount = topFiveCount;
    }

    public Long getTopTenCount() {
        return topTenCount;
    }

    public void setTopTenCount(Long topTenCount) {
        this.topTenCount = topTenCount;
    }

    public Long getTopFifthCount() {
        return topFifthCount;
    }

    public void setTopFifthCount(Long topFifthCount) {
        this.topFifthCount = topFifthCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}