package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;

/**
 * Created by shunshikj08 on 2017/10/24.
 */
@TableName("t_negative_keyword_name_position_info")
public class NegativeKeywordNamePositionInfo {

    private static final long serialVersionUID = 3922222059082125030L;

    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    @TableField(value = "fNegativeKeywordNameUuid")
    private Long negativeKeywordNameUuid;

    @TableField(value = "fTerminalType")
    private String terminalType;

    @TableField(value = "fKeyword")
    private String keyword;

    @TableField(value = "fPosition")
    private Integer position;

    @TableField(value = "fTargetUrl")
    private String targetUrl;

    @TableField(value = "fCreateTime")
    private Date createTime;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getNegativeKeywordNameUuid() {
        return negativeKeywordNameUuid;
    }

    public void setNegativeKeywordNameUuid(Long negativeKeywordNameUuid) {
        this.negativeKeywordNameUuid = negativeKeywordNameUuid;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
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

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
