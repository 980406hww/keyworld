 package com.keymanager.util.mail;
 
 import javax.mail.Authenticator;
 import javax.mail.PasswordAuthentication;
 
 public class MyAuthenticator extends Authenticator
 {
   String userName = null;
   String password = null;
 
   public MyAuthenticator() {
   }
   public MyAuthenticator(String username, String password) {
     this.userName = username;
     this.password = password;
   }
   protected PasswordAuthentication getPasswordAuthentication() {
     return new PasswordAuthentication(this.userName, this.password);
   }
 }

/* Location:           D:\baidutop123.com\baiduup.jar
 * Qualified Name:     com.baiduup.util.mail.MyAuthenticator
 * JD-Core Version:    0.6.0
 */