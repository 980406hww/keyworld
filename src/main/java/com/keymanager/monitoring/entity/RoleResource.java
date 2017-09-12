package com.keymanager.monitoring.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 * 角色资源
 *
 */
@TableName(value = "t_role_resource")
public class RoleResource extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 角色id */
	@TableField(value = "fRoleID")
	private Long roleID;

	/** 资源id */
	@TableField(value = "fResourceID")
	private Long resourceID;

	public Long getRoleID() {
		return this.roleID;
	}

	public void setRoleID(Long roleID) {
		this.roleID = roleID;
	}

	public Long getResourceID() {
		return this.resourceID;
	}

	public void setResourceID(Long resourceID) {
		this.resourceID = resourceID;
	}

	@Override
	public String toString() {
		return "RoleResource{" +
				"roleID=" + roleID +
				", resourceID=" + resourceID +
				'}';
	}
}
