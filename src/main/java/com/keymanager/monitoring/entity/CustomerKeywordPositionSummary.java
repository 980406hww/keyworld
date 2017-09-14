package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

@TableName(value = "t_ck_position_summary")
public class CustomerKeywordPositionSummary{
    private static final long serialVersionUID = -1101942701283949852L;

    @TableId(value = "fUuid", type= IdType.AUTO)
    private long uuid;

    @TableField(value = "fCustomerKeywordUuid")
    private long customerKeywordUuid;

    @TableField(value = "fPosition")
    private Integer position;

    @TableField(value = "fCreateDate")
    private Date createDate;

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public long getCustomerKeywordUuid() {
        return customerKeywordUuid;
    }

    public void setCustomerKeywordUuid(long customerKeywordUuid) {
        this.customerKeywordUuid = customerKeywordUuid;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
