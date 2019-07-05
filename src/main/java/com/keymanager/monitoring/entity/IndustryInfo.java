package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 行业表
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
@TableName("t_industry_info")
public class IndustryInfo extends BaseEntity {
    /**
     * 所有者
     */
    @TableField(value = "fUserID")
    private String userID;
    /**
     * 搜索引擎
     */
    @TableField(value = "fSearchEngine")
    private String searchEngine;
    /**
     * 终端类型
     */
    @TableField(value = "fTerminalType")
    private String terminalType;
    /**
     * 行业名称
     */
    @TableField(value = "fIndustryName")
    private String industryName;
    /**
     * 页数
     */
    @TableField(value = "fPageNum")
    private Integer pageNum;
    /**
     * 每页条数
     */
    @TableField(value = "fPagePerNum")
    private Integer pagePerNum;
    /**
     * 状态 0: 未爬取，1: 爬取中 2: 爬取完
     */
    @TableField(value = "fStatus")
    private Integer status;

    public String getUserID () {
        return userID;
    }

    public void setUserID (String userID) {
        this.userID = userID;
    }

    public String getSearchEngine () {
        return searchEngine;
    }

    public void setSearchEngine (String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public String getIndustryName () {
        return industryName;
    }

    public void setIndustryName (String industryName) {
        this.industryName = industryName;
    }

    public Integer getPageNum () {
        return pageNum;
    }

    public void setPageNum (Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPagePerNum () {
        return pagePerNum;
    }

    public void setPagePerNum (Integer pagePerNum) {
        this.pagePerNum = pagePerNum;
    }

    public Integer getStatus () {
        return status;
    }

    public void setStatus (Integer status) {
        this.status = status;
    }
}
