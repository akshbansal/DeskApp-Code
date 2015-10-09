package com.im.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Test1 {

	public static void main(String[] args) {
		List<studentBO> list = new ArrayList<>();
		list.add(new studentBO("Bali", "2015-08-17T16:40:09.603Z"));
		list.add(new studentBO("Bali", "2013-08-17T16:40:09.603Z"));
		list.add(new studentBO("Bali", "2018-08-17T16:40:09.603Z"));
		list.add(new studentBO("Bali", "2005-08-17T16:40:09.603Z"));

		System.out.println("Without Sort --> " + list);
		Collections.sort(list, new Comparator<studentBO>() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			@Override
			public int compare(studentBO obj1, studentBO obj2) {
				Date d1 = null;
		        Date d2 = null;
		        try {
		            d1 = sdf.parse(obj1.getDob());
		            d2 = sdf.parse(obj2.getDob());
		        } catch (ParseException e) {
		            e.printStackTrace();
		        }
//		        return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
		      return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
			}
		});
		System.out.println("With Sort --> " + list);
	}

	public static void show(String... str) {

	}
}

class studentBO {
	private String name;
	private String dob;

	studentBO(String name, String dob) {
		this.name = name;
		this.dob = dob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "\n" + name + "--" + dob;
	}
}
