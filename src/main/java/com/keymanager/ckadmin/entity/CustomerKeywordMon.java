package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;

@TableName("t_customer_keyword_mon")
public class CustomerKeywordMon {

    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    @TableField("fCustomerUuid")
    private Long customerUuid;

    @TableField("fKeywordUuid")
    private Long keywordUuid;

    @TableField("fKeyword")
    private String keyword;

    @TableField("fPosition")
    private Integer position;

    @TableField("fRecordDate")
    private Date recordDate;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public Long getKeywordUuid() {
        return keywordUuid;
    }

    public void setKeywordUuid(Long keywordUuid) {
        this.keywordUuid = keywordUuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }
}
