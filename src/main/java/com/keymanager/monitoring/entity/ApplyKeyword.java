package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;


/**
 * Created by shunshikj22 on 2017/9/5.
 */
@TableName(value = "t_apply_keyword")
public class ApplyKeyword {

    private static final long serialVersionUID = 3922222059082125030L;

    @TableId(value = "fUuid", type= IdType.AUTO)
    private Long uuid;

    @TableField(value = "fKeyword")
    private String keyword;

    @TableField(value = "fApplyUuid")
    private Long applyUuid;

    @TableField(value = "fApplyName")
    private String applyName;

    @TableField(value = "fBrushNumber")
    private int brushNumber;

    @TableField(value = "fCreateTime")
    private Date createTime;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getApplyUuid() {
        return applyUuid;
    }

    public void setApplyUuid(Long applyUuid) {
        this.applyUuid = applyUuid;
    }

    public int getBrushNumber() {
        return brushNumber;
    }

    public void setBrushNumber(int brushNumber) {
        this.brushNumber = brushNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }
}
