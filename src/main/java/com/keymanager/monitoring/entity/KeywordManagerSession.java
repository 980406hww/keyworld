package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhoukai
 * @since 2019-08-09
 */
@TableName("t_keyword_manager_session")
public class KeywordManagerSession extends BaseEntity {

    /**
     * SESSION ID
     */
    @TableField(value = "fSessionID")
    private String sessionID;
    /**
     * attribute name
     */
    @TableField(value = "fAttributeName")
    private String attributeName;
    /**
     * SESSION 内容
     */
    @TableField(value = "fContent")
    private String content;
    /**
     * SESSION 状态
     */
    @TableField(value = "fStatus")
    private String status;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
