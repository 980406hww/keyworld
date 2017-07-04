package com.keymanager.manager;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Test {
	public static void main(String[] args) throws AddressException{
		String str = "BA__col____col__we";
		String []s = str.split("__col__", 3);
		System.out.println(s);
		
		InternetAddress[] toAddresses = InternetAddress.parse("abc@163.com,def@163.com");
		System.out.println(toAddresses);
	}
}
