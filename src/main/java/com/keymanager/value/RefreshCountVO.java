 package com.keymanager.value;
 
 import java.sql.Timestamp;
 
 public class RefreshCountVO
 {
   private int uuid;
   private int keywordUuid;
   private String keyword;
   private String userName;
   private String password;
   private int refrshCount;
   private String refreshDate;
   private Timestamp updateTime;
   private String url;
   private String refreshUserName;
   private String refreshPassword;
   private int inviteRefreshCount;
 
   public String getRefreshUserName()
   {
     return this.refreshUserName;
   }
 
   public void setRefreshUserName(String refreshUserName) {
     this.refreshUserName = refreshUserName;
   }
 
   public String getRefreshPassword() {
     return this.refreshPassword;
   }
 
   public void setRefreshPassword(String refreshPassword) {
     this.refreshPassword = refreshPassword;
   }
 
   public int getInviteRefreshCount() {
     return this.inviteRefreshCount;
   }
 
   public void setInviteRefreshCount(int inviteRefreshCount) {
     this.inviteRefreshCount = inviteRefreshCount;
   }
 
   public int getUuid() {
     return this.uuid;
   }
 
   public void setUuid(int uuid) {
     this.uuid = uuid;
   }
 
   public int getKeywordUuid() {
     return this.keywordUuid;
   }
 
   public void setKeywordUuid(int keywordUuid) {
     this.keywordUuid = keywordUuid;
   }
 
   public String getKeyword() {
     return this.keyword;
   }
 
   public void setKeyword(String keyword) {
     this.keyword = keyword;
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
 
   public int getRefrshCount() {
     return this.refrshCount;
   }
 
   public void setRefrshCount(int refrshCount) {
     this.refrshCount = refrshCount;
   }
 
   public String getRefreshDate() {
     return this.refreshDate;
   }
 
   public void setRefreshDate(String refreshDate) {
     this.refreshDate = refreshDate;
   }
 
   public Timestamp getUpdateTime() {
     return this.updateTime;
   }
 
   public void setUpdateTime(Timestamp updateTime) {
     this.updateTime = updateTime;
   }
 
   public String getUrl() {
     return this.url;
   }
 
   public void setUrl(String url) {
     this.url = url;
   }
 }

/* Location:           D:\baidutop123.com\baiduup.jar
 * Qualified Name:     com.baiduup.value.RefreshCountVO
 * JD-Core Version:    0.6.0
 */