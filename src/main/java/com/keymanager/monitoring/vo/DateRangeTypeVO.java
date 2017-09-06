package com.keymanager.monitoring.vo;

import java.util.Date;

/**
 * Created by shunshikj08 on 2017/9/1.
 */
public class DateRangeTypeVO {
    private Long uuid;

    private Date nextChargeDate;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Date getNextChargeDate() {
        return nextChargeDate;
    }

    public void setNextChargeDate(Date nextChargeDate) {
        this.nextChargeDate = nextChargeDate;
    }
}
