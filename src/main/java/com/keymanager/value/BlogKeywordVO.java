 package com.keymanager.value;
 
 import java.sql.Timestamp;
 
 public class BlogKeywordVO
 {
   private int uuid;
   private String name;
   private String type;
   private String link;
   private String target;
   private String url;
   private String domain;
   private String userName;
   private int userType;
   private String createUser;
   private Timestamp createTime;
   private String color;
   private String password;
   private int keyType;
 
   public int getKeyType()
   {
     return this.keyType;
   }
 
   public void setKeyType(int keyType) {
     this.keyType = keyType;
   }
 
   public String getPassword() {
     return this.password;
   }
 
   public void setPassword(String password) {
     this.password = password;
   }
 
   public String getColor() {
     return this.color;
   }
 
   public void setColor(String color) {
     this.color = color;
   }
 
   public Timestamp getCreateTime() {
     return this.createTime;
   }
 
   public void setCreateTime(Timestamp createTime) {
     this.createTime = createTime;
   }
 
   public String getCreateUser() {
     return this.createUser;
   }
 
   public void setCreateUser(String createUser) {
     this.createUser = createUser;
   }
 
   public int getUserType() {
     return this.userType;
   }
 
   public void setUserType(int userType) {
     this.userType = userType;
   }
 
   public String getUserName() {
     return this.userName;
   }
 
   public void setUserName(String userName) {
     this.userName = userName;
   }
 
   public int getUuid() {
     return this.uuid;
   }
 
   public void setUuid(int uuid) {
     this.uuid = uuid;
   }
 
   public String getName() {
     return this.name;
   }
 
   public void setName(String name) {
     this.name = name;
   }
 
   public String getType() {
     return this.type;
   }
 
   public void setType(String type) {
     this.type = type;
   }
 
   public String getLink() {
     return this.link;
   }
 
   public void setLink(String link) {
     this.link = link;
   }
 
   public String getTarget() {
     return this.target;
   }
 
   public void setTarget(String target) {
     this.target = target;
   }
 
   public String getUrl() {
     return this.url;
   }
 
   public void setUrl(String url) {
     this.url = url;
   }
 
   public String getDomain() {
     return this.domain;
   }
 
   public void setDomain(String domain) {
     this.domain = domain;
   }
 }

/* Location:           D:\baidutop123.com\baiduup.jar
 * Qualified Name:     com.baiduup.value.BlogKeywordVO
 * JD-Core Version:    0.6.0
 */