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
    private Long uuid;

    @TableField(value = "fCustomerKeywordUuid")
    private Long customerKeywordUuid;

    @TableField(value = "fPosition")
    private Integer position;

    @TableField(value = "fCustomerUuid")
    private Long customerUuid;

    @TableField(value = "fSearchEngine")
    private String searchEngine;

    @TableField(value = "fTerminalType")
    private String terminalType;

    /**
     * qz, pt, fm
     */
    @TableField(value = "fType")
    private String type;

    @TableField(value = "fCreateDate")
    private Date createDate;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getCustomerKeywordUuid() {
        return customerKeywordUuid;
    }

    public void setCustomerKeywordUuid(Long customerKeywordUuid) {
        this.customerKeywordUuid = customerKeywordUuid;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
