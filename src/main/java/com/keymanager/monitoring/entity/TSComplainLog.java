package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
@TableName(value = "t_ts_complain_log")
public class TSComplainLog extends BaseEntity {

    @TableField(value = "fTSNegativeKeywordUuid")
    private Long tsNegativeKeywordUuid;

    @TableField(value = "fTerminalType")
    private String terminalType;

    @TableField(value = "fAppearedKeywordTypes")
    private String appearedKeywordTypes;

    public Long getTsNegativeKeywordUuid() {
        return tsNegativeKeywordUuid;
    }

    public void setTsNegativeKeywordUuid(Long tsNegativeKeywordUuid) {
        this.tsNegativeKeywordUuid = tsNegativeKeywordUuid;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getAppearedKeywordTypes() {
        return appearedKeywordTypes;
    }

    public void setAppearedKeywordTypes(String appearedKeywordTypes) {
        this.appearedKeywordTypes = appearedKeywordTypes;
    }
}
