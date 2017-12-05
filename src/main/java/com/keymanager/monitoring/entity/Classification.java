package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/12/4.
 */
@TableName(value = "t_classification")
public class Classification extends BaseEntity{

    @TableField(value = "fKeyword")
    private String keyword;

    @TableField(value = "fGroup")
    private String group;

    @TableField(value = "fQueried")
    private Boolean queried;

    @TableField(value = "fCaptured")
    private Boolean captured;

   /* @TableField(exist = false)
    private List<ClassificationWebsitInfo> searchResults;

    public List<ClassificationWebsitInfo> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<ClassificationWebsitInfo> searchResults) {
        this.searchResults = searchResults;
    }*/

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Boolean getQueried() {
        return queried;
    }

    public void setQueried(Boolean queried) {
        this.queried = queried;
    }

    public Boolean getCaptured() {
        return captured;
    }

    public void setCaptured(Boolean captured) {
        this.captured = captured;
    }
}