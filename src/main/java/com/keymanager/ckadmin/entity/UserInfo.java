package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import org.hibernate.validator.constraints.NotBlank;


@TableName(value = "t_userinfo")
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = -7590694637780491359L;
    @NotBlank
    @TableField(value = "fLoginName")
    private String loginName;
    /**
     * 登陆名称
     */

    @TableField(value = "fUserName")
    private String userName;

    @TableField(value = "fPassword")
    private String password;

    @TableField(value = "fSex")
    private String sex;

    @TableField(value = "fQQ")
    private String qq;

    @TableField(value = "fEmail")
    private String email;

    @TableField(value = "fSalt")
    private String salt;

    @TableField(value = "fStatus")
    private Integer status; // 1: active,  0: inactive

    @TableField(value = "fOrganizationID")
    private Integer organizationID;
    /**
     * 所属机构
     */

    @TableField(value = "fResetPasswordApplicationTime")
    private Date resetPasswordApplicationTime;

    @TableField(value = "fDataAuthority")
    private String dataAuthority;

    @TableField(exist = false)
    private List<Role> roleList;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(Integer organizationID) {
        this.organizationID = organizationID;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getResetPasswordApplicationTime() {
        return resetPasswordApplicationTime;
    }

    public void setResetPasswordApplicationTime(Date resetPasswordApplicationTime) {
        this.resetPasswordApplicationTime = resetPasswordApplicationTime;
    }

    public String getDataAuthority() {
        return dataAuthority;
    }

    public void setDataAuthority(String dataAuthority) {
        this.dataAuthority = dataAuthority;
    }
}
