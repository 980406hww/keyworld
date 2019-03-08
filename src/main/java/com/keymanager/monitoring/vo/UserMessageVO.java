package com.keymanager.monitoring.vo;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.UserInfo;

import java.util.List;

public class UserMessageVO {
    private Integer messageStatus;
    private Page page;
    private List<UserInfo> userInfos;

    public Integer getMessageStatus () {
        return messageStatus;
    }

    public void setMessageStatus (Integer messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Page getPage () {
        return page;
    }

    public void setPage (Page page) {
        this.page = page;
    }

    public List<UserInfo> getUserInfos () {
        return userInfos;
    }

    public void setUserInfos (List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }
}
