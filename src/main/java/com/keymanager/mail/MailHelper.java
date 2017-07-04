package com.keymanager.mail;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;

public class MailHelper {

	public static void sendClientDownNotification(String toEmailAddress, String mailContent, String subject) throws UnsupportedEncodingException{
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.163.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("shunshiyq@163.com");
		mailInfo.setPassword("test99TEST");// 您的邮箱密码
		mailInfo.setFromAddress("haoqiyy@126.com");

		mailInfo.setToAddress(toEmailAddress);
		mailInfo.setSubject(MimeUtility.encodeWord(subject, "UTF-8", "Q"));
		mailInfo.setContent(mailContent);
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
//		sms.sendTextMail(mailInfo);// 发送文体格式
		sms.sendHtmlMail(mailInfo);// 发送html格式
	}
}
