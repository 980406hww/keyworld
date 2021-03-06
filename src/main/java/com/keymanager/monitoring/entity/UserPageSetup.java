package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * Created by liaojijun on 2018/6/6.
 */

@TableName(value = "t_user_page_setup")
public class UserPageSetup extends BaseEntity{

    @TableField(value = "fLoginName")
    private String loginName;

    @TableField(value = "fPageUrl")
    private String pageUrl;

    @TableField(value = "fHiddenField")
    private String hiddenField;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getHiddenField() {
        return hiddenField;
    }

    public void setHiddenField(String hiddenField) {
        this.hiddenField = hiddenField;
    }
}
