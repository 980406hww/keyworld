package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * Created by shunshikj08 on 2017/10/24.
 */
@TableName("t_negative_keyword_name")
public class NegativeKeywordName extends BaseEntity {

    @TableField(value = "fGroup")
    private String group;

    @TableField(value = "fName")
    private String name;

    @TableField(value = "fRankQueried")
    private Boolean rankQueried;

    @TableField(value = "fRankCaptured")
    private Boolean rankCaptured;

    @TableField(value = "fRankExistNegative")
    private Boolean rankExistNegative;

    @TableField(value = "fRankNegativeKeyword")
    private String rankNegativeKeyword;

    @TableField(value = "fRankNegativeCount")
    private Integer rankNegativeCount;

    @TableField(value = "fOfficialUrl")
    private String officialUrl;

    @TableField(value = "fOfficialUrlBak")
    private String officialUrlBak;

    @TableField(value = "fEmail")
    private String email;

    @TableField(value = "fSelectQueried")
    private Boolean selectQueried;

    @TableField(value = "fSelectCaptured")
    private Boolean selectCaptured;

    @TableField(value = "fSelectExistNegative")
    private Boolean selectExistNegative;

    @TableField(value = "fSelectNegativeKeyword")
    private String selectNegativeKeyword;

    @TableField(value = "fRelevantQueried")
    private Boolean relevantQueried;

    @TableField(value = "fRelevantCaptured")
    private Boolean relevantCaptured;

    @TableField(value = "fRelevantExistNegative")
    private Boolean relevantExistNegative;

    @TableField(value = "fRelevantNegativeKeyword")
    private String relevantNegativeKeyword;

    @TableField(value = "fPhoneQueried")
    private Boolean phoneQueried;

    @TableField(value = "fPhoneCaptured")
    private Boolean phoneCaptured;

    @TableField(value = "fPhoneRankNegativeCount")
    private Integer phoneRankNegativeCount;

    @TableField(value = "fPhoneSelectExistNegative")
    private Boolean phoneSelectExistNegative;

    @TableField(value = "fPhoneSelectNegativeKeyword")
    private String phoneSelectNegativeKeyword;

    @TableField(value = "fPhoneRelevantExistNegative")
    private Boolean phoneRelevantExistNegative;

    @TableField(value = "fPhoneRelevantNegativeKeyword")
    private String phoneRelevantNegativeKeyword;

    @TableField(value = "fHandled")
    private Boolean handled;

    @TableField(value = "fOfficialUrlCaptured")
    private Boolean officialUrlCaptured;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRankQueried() {
        return rankQueried;
    }

    public void setRankQueried(Boolean rankQueried) {
        this.rankQueried = rankQueried;
    }

    public Boolean getRankCaptured() {
        return rankCaptured;
    }

    public void setRankCaptured(Boolean rankCaptured) {
        this.rankCaptured = rankCaptured;
    }

    public Boolean getRankExistNegative() {
        return rankExistNegative;
    }

    public void setRankExistNegative(Boolean rankExistNegative) {
        this.rankExistNegative = rankExistNegative;
    }

    public String getRankNegativeKeyword() {
        return rankNegativeKeyword;
    }

    public void setRankNegativeKeyword(String rankNegativeKeyword) {
        this.rankNegativeKeyword = rankNegativeKeyword;
    }

    public Integer getRankNegativeCount() {
        return rankNegativeCount;
    }

    public void setRankNegativeCount(Integer rankNegativeCount) {
        this.rankNegativeCount = rankNegativeCount;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public String getOfficialUrlBak() {
        return officialUrlBak;
    }

    public void setOfficialUrlBak(String officialUrlBak) {
        this.officialUrlBak = officialUrlBak;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getSelectQueried() {
        return selectQueried;
    }

    public void setSelectQueried(Boolean selectQueried) {
        this.selectQueried = selectQueried;
    }

    public Boolean getSelectCaptured() {
        return selectCaptured;
    }

    public void setSelectCaptured(Boolean selectCaptured) {
        this.selectCaptured = selectCaptured;
    }

    public Boolean getSelectExistNegative() {
        return selectExistNegative;
    }

    public void setSelectExistNegative(Boolean selectExistNegative) {
        this.selectExistNegative = selectExistNegative;
    }

    public String getSelectNegativeKeyword() {
        return selectNegativeKeyword;
    }

    public void setSelectNegativeKeyword(String selectNegativeKeyword) {
        this.selectNegativeKeyword = selectNegativeKeyword;
    }

    public Boolean getRelevantQueried() {
        return relevantQueried;
    }

    public void setRelevantQueried(Boolean relevantQueried) {
        this.relevantQueried = relevantQueried;
    }

    public Boolean getRelevantCaptured() {
        return relevantCaptured;
    }

    public void setRelevantCaptured(Boolean relevantCaptured) {
        this.relevantCaptured = relevantCaptured;
    }

    public Boolean getRelevantExistNegative() {
        return relevantExistNegative;
    }

    public void setRelevantExistNegative(Boolean relevantExistNegative) {
        this.relevantExistNegative = relevantExistNegative;
    }

    public String getRelevantNegativeKeyword() {
        return relevantNegativeKeyword;
    }

    public void setRelevantNegativeKeyword(String relevantNegativeKeyword) {
        this.relevantNegativeKeyword = relevantNegativeKeyword;
    }

    public Boolean getPhoneQueried() {
        return phoneQueried;
    }

    public void setPhoneQueried(Boolean phoneQueried) {
        this.phoneQueried = phoneQueried;
    }

    public Boolean getPhoneCaptured() {
        return phoneCaptured;
    }

    public void setPhoneCaptured(Boolean phoneCaptured) {
        this.phoneCaptured = phoneCaptured;
    }

    public Integer getPhoneRankNegativeCount() {
        return phoneRankNegativeCount;
    }

    public void setPhoneRankNegativeCount(Integer phoneRankNegativeCount) {
        this.phoneRankNegativeCount = phoneRankNegativeCount;
    }

    public Boolean getPhoneSelectExistNegative() {
        return phoneSelectExistNegative;
    }

    public void setPhoneSelectExistNegative(Boolean phoneSelectExistNegative) {
        this.phoneSelectExistNegative = phoneSelectExistNegative;
    }

    public String getPhoneSelectNegativeKeyword() {
        return phoneSelectNegativeKeyword;
    }

    public void setPhoneSelectNegativeKeyword(String phoneSelectNegativeKeyword) {
        this.phoneSelectNegativeKeyword = phoneSelectNegativeKeyword;
    }

    public Boolean getPhoneRelevantExistNegative() {
        return phoneRelevantExistNegative;
    }

    public void setPhoneRelevantExistNegative(Boolean phoneRelevantExistNegative) {
        this.phoneRelevantExistNegative = phoneRelevantExistNegative;
    }

    public String getPhoneRelevantNegativeKeyword() {
        return phoneRelevantNegativeKeyword;
    }

    public void setPhoneRelevantNegativeKeyword(String phoneRelevantNegativeKeyword) {
        this.phoneRelevantNegativeKeyword = phoneRelevantNegativeKeyword;
    }

    public Boolean getHandled() {
        return handled;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }

    public Boolean getOfficialUrlCaptured() {
        return officialUrlCaptured;
    }

    public void setOfficialUrlCaptured(Boolean officialUrlCaptured) {
        this.officialUrlCaptured = officialUrlCaptured;
    }
}
