package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 * 系统日志
 *
 */
@TableName(value = "t_system_Log")
public class SysLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 登陆名 */
	@TableField(value = "fLoginName")
	private String loginName;

	/** 角色名 */
	@TableField(value = "fRoleName")
	private String roleName;

	/** 内容 */
	@TableField(value = "fOptContent")
	private String optContent;

	/** 客户端ip */
	@TableField(value = "fClientIP")
	private String clientIp;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOptContent() {
		return optContent;
	}

	public void setOptContent(String optContent) {
		this.optContent = optContent;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
}
