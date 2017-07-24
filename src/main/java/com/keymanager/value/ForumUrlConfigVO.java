 package com.keymanager.value;
 
 import java.sql.Timestamp;
 
 public class ForumUrlConfigVO
 {
   private int uuid;
   private String url;
   private String domain;
   private int count;
   private Timestamp createTime;
   private int status;
 
   public int getStatus()
   {
     return this.status;
   }
 
   public void setStatus(int status) {
     this.status = status;
   }
 
   public int getUuid() {
     return this.uuid;
   }
 
   public void setUuid(int uuid) {
     this.uuid = uuid;
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
 
   public int getCount() {
     return this.count;
   }
 
   public void setCount(int count) {
     this.count = count;
   }
 
   public Timestamp getCreateTime() {
     return this.createTime;
   }
 
   public void setCreateTime(Timestamp createTime) {
     this.createTime = createTime;
   }
 }

/* Location:           D:\baidutop123.com\baiduup.jar
 * Qualified Name:     com.baiduup.value.ForumUrlConfigVO
 * JD-Core Version:    0.6.0
 */