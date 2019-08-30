package com.keymanager.monitoring.vo;

/**
 * @ClassName CustomerKeywordQueryRangeVO
 * @Description 新的时间间隔VO
 * @Author lhc
 * @Date 2019/8/29 11:28
 * @Version 1.0
 */
public class CustomerKeywordQueryRangeVO {
    private Long uuid;
    private int queryInterval;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public int getQueryInterval() {
        return queryInterval;
    }

    public void setQueryInterval(int queryInterval) {
        this.queryInterval = queryInterval;
    }
}
