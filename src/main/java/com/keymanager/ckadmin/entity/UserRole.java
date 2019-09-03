package com.keymanager.ckadmin.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.keymanager.monitoring.entity.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户角色
 */
@TableName(value = "t_user_role")
public class UserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @TableField(value = "fUserID")
    private Long userID;

    @TableField(value = "fRoleID")
    private Long roleID;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getRoleID() {
        return roleID;
    }

    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }
}

