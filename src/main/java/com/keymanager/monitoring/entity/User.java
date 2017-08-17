package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;


@TableName(value = "t_user")
public class User {
	
	private static final long serialVersionUID = -7590694637780491359L;
	@NotBlank
	@TableId(value = "fUserID")
	private String userID;

	@TableField(value = "fUserName")
	private String userName;
	
	@TableField(value = "fPassword")
	private String password;

	@TableField(value = "fGender")
	private String gender;

	@TableField(value = "fQQ")
	private String qq;

	@TableField(value = "fUserLevel")
	private int userLevel;

	@TableField(value = "fSalt")
	private String salt;

	@TableField(value = "fVipType")
	private boolean vipType;

	@TableField(value = "fClientIp")
	private String clientIp;

	@TableField(value = "fStatus")
	private Integer status; // 1: active,  0: inactive

	@TableField(value = "fUpdateTime")
	private Date updateTime;

	@TableField(value = "fCreateTime")
	private Date createTime;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public boolean isVipType() {
		return vipType;
	}

	public void setVipType(boolean vipType) {
		this.vipType = vipType;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
