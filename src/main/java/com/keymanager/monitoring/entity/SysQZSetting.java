package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;

/**
 * <p>
 *     站点信息表
 * </p>
 * @author shunshikj40
 */
@TableName("sys_qz_setting")
public class SysQZSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "QS_ID", type = IdType.ID_WORKER)
    private Long qsId;
    /**
     * 客户ID
     */
    @TableField(value = "CUSTOMER_ID")
    private Long customerId;
    /**
     * 域名
     */
    @TableField(value = "DOMAIN")
    private String domain;
    /**
     * 搜索引擎
     */
    @TableField(value = "SEARCH_ENGINE")
    private String searchEngine;
    /**
     * 熊掌ID
     */
    @TableField(value = "BEAR_PAW_ID")
    private String bearPawId;
    /**
     * 续费状态
     */
    @TableField(value = "RENEWAL_STATUS")
    private Boolean renewalStatus;
    /**
     * PC 优化组
     */
    @TableField(value = "PC_GROUP")
    private String pcGroup;
    /**
     * Phone 优化组
     */
    @TableField(value = "PHONE_GROUP")
    private String phoneGroup;
    /**
     * 公司名称
     */
    @TableField(value = "COMPANY_NAME")
    private String companyName;

    public Long getQsId() {
        return qsId;
    }

    public void setQsId(Long qsId) {
        this.qsId = qsId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getBearPawId() {
        return bearPawId;
    }

    public void setBearPawId(String bearPawId) {
        this.bearPawId = bearPawId;
    }

    public Boolean getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(Boolean renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public String getPcGroup() {
        return pcGroup;
    }

    public void setPcGroup(String pcGroup) {
        this.pcGroup = pcGroup;
    }

    public String getPhoneGroup() {
        return phoneGroup;
    }

    public void setPhoneGroup(String phoneGroup) {
        this.phoneGroup = phoneGroup;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
