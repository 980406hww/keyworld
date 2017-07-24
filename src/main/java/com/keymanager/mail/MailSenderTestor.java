package com.keymanager.mail;

import java.io.UnsupportedEncodingException;

public class MailSenderTestor {
	public static void main(String[] args) {
		try {
			MailHelper.sendClientDownNotification("duchengfu@163.com", "test", "ceshi");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
