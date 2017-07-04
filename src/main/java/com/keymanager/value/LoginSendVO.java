 package com.keymanager.value;
 
 import java.sql.Timestamp;
 
 public class LoginSendVO
 {
   private int uuid;
   private String domain;
   private String userName;
   private String password;
   private String loginButton;
   private String loginUrl;
   private String loginSuccUrl;
   private String loginSuccMsg;
   private String sendUrl;
   private String title;
   private String content;
   private String sendButton;
   private String sendSuccUrl;
   private String sendSuccMsg;
   private Timestamp createTime;
   private int isValid;
   private String sendErrorMsg;
   private String group;
   private String tag;
   private String verifyCode;
   private String accessUrl;
 
   public String getAccessUrl()
   {
     return this.accessUrl;
   }
 
   public void setAccessUrl(String accessUrl) {
     this.accessUrl = accessUrl;
   }
 
   public int getUuid() {
     return this.uuid;
   }
 
   public void setUuid(int uuid) {
     this.uuid = uuid;
   }
 
   public String getDomain() {
     return this.domain;
   }
 
   public void setDomain(String domain) {
     this.domain = domain;
   }
 
   public String getUserName() {
     return this.userName;
   }
 
   public void setUserName(String userName) {
     this.userName = userName;
   }
 
   public String getPassword() {
     return this.password;
   }
 
   public void setPassword(String password) {
     this.password = password;
   }
 
   public String getLoginButton() {
     return this.loginButton;
   }
 
   public void setLoginButton(String loginButton) {
     this.loginButton = loginButton;
   }
 
   public String getLoginUrl() {
     return this.loginUrl;
   }
 
   public void setLoginUrl(String loginUrl) {
     this.loginUrl = loginUrl;
   }
 
   public String getLoginSuccUrl() {
     return this.loginSuccUrl;
   }
 
   public void setLoginSuccUrl(String loginSuccUrl) {
     this.loginSuccUrl = loginSuccUrl;
   }
 
   public String getLoginSuccMsg() {
     return this.loginSuccMsg;
   }
 
   public void setLoginSuccMsg(String loginSuccMsg) {
     this.loginSuccMsg = loginSuccMsg;
   }
 
   public String getSendUrl() {
     return this.sendUrl;
   }
 
   public void setSendUrl(String sendUrl) {
     this.sendUrl = sendUrl;
   }
 
   public String getTitle() {
     return this.title;
   }
 
   public void setTitle(String title) {
     this.title = title;
   }
 
   public String getContent() {
     return this.content;
   }
 
   public void setContent(String content) {
     this.content = content;
   }
 
   public String getSendButton() {
     return this.sendButton;
   }
 
   public void setSendButton(String sendButton) {
     this.sendButton = sendButton;
   }
 
   public String getSendSuccUrl() {
     return this.sendSuccUrl;
   }
 
   public void setSendSuccUrl(String sendSuccUrl) {
     this.sendSuccUrl = sendSuccUrl;
   }
 
   public String getSendSuccMsg() {
     return this.sendSuccMsg;
   }
 
   public void setSendSuccMsg(String sendSuccMsg) {
     this.sendSuccMsg = sendSuccMsg;
   }
 
   public Timestamp getCreateTime() {
     return this.createTime;
   }
 
   public void setCreateTime(Timestamp createTime) {
     this.createTime = createTime;
   }
 
   public int getIsValid() {
     return this.isValid;
   }
 
   public void setIsValid(int isValid) {
     this.isValid = isValid;
   }
 
   public String getSendErrorMsg() {
     return this.sendErrorMsg;
   }
 
   public void setSendErrorMsg(String sendErrorMsg) {
     this.sendErrorMsg = sendErrorMsg;
   }
 
   public String getGroup() {
     return this.group;
   }
 
   public void setGroup(String group) {
     this.group = group;
   }
 
   public String getTag() {
     return this.tag;
   }
 
   public void setTag(String tag) {
     this.tag = tag;
   }
 
   public String getVerifyCode() {
     return this.verifyCode;
   }
 
   public void setVerifyCode(String verifyCode) {
     this.verifyCode = verifyCode;
   }
 }

/* Location:           D:\baidutop123.com\baiduup.jar
 * Qualified Name:     com.baiduup.value.LoginSendVO
 * JD-Core Version:    0.6.0
 */