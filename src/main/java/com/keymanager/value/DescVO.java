 package com.keymanager.value;
 
 import java.sql.Timestamp;
 
 public class DescVO
 {
   private int uuid;
   private int keywordUuid;
   private String keyType;
   private String keyword;
   private String username;
   private String password;
   private Timestamp createTime;
   private String description;
   private int userType;
 
   public int getUserType()
   {
     return this.userType;
   }
 
   public void setUserType(int userType) {
     this.userType = userType;
   }
 
   public Timestamp getCreateTime() {
     return this.createTime;
   }
 
   public void setCreateTime(Timestamp createTime) {
     this.createTime = createTime;
   }
 
   public String getUsername() {
     return this.username;
   }
 
   public void setUsername(String username) {
     this.username = username;
   }
 
   public String getPassword() {
     return this.password;
   }
 
   public void setPassword(String password) {
     this.password = password;
   }
 
   public String getKeyword() {
     return this.keyword;
   }
 
   public void setKeyword(String keyword) {
     this.keyword = keyword;
   }
 
   public String getKeyType() {
     return this.keyType;
   }
 
   public void setKeyType(String keyType) {
     this.keyType = keyType;
   }
 
   public int getUuid()
   {
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
 
   public String getDescription() {
     return this.description;
   }
 
   public void setDescription(String description) {
     this.description = description;
   }
 }

/* Location:           D:\baidutop123.com\baiduup.jar
 * Qualified Name:     com.baiduup.value.DescVO
 * JD-Core Version:    0.6.0
 */