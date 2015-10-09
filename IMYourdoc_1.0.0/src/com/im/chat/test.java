package com.im.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class test {
	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"); 
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println(format.format(date));
	}
}
