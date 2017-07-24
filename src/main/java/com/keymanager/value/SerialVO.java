 package com.keymanager.value;
 
 import java.sql.Timestamp;
 
 public class SerialVO
 {
   private int uuid;
   private String serial;
   private Timestamp createTime;
 
   public int getUuid()
   {
     return this.uuid;
   }
 
   public void setUuid(int uuid) {
     this.uuid = uuid;
   }
 
   public String getSerial() {
     return this.serial;
   }
 
   public void setSerial(String serial) {
     this.serial = serial;
   }
 
   public Timestamp getCreateTime() {
     return this.createTime;
   }
 
   public void setCreateTime(Timestamp createTime) {
     this.createTime = createTime;
   }
 }

/* Location:           D:\baidutop123.com\baiduup.jar
 * Qualified Name:     com.baiduup.value.SerialVO
 * JD-Core Version:    0.6.0
 */