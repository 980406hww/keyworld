package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * Created by Administrator on 2017/11/16.
 */
@TableName(value = "t_terminal_setting")
public class TerminalSetting extends BaseEntity{
    @TableField(value = "fIP")
    private String IP;
    @TableField(value = "fUA")
    private  String UA;
    @TableField(value = "fWidth")
    private String width;
    @TableField(value = "fHeight")
    private  String height;
    @TableField(value = "fPdr")
    private  String pdr;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPdr() {
        return pdr;
    }

    public void setPdr(String pdr) {
        this.pdr = pdr;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getUA() {
        return UA;
    }

    public void setUA(String UA) {
        this.UA = UA;
    }
}
