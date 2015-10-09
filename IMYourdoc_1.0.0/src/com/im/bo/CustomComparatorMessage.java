package com.im.bo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class CustomComparatorMessage implements Comparator<MessageSyncBo> {
    @Override
    public int compare(MessageSyncBo o1, MessageSyncBo o2) {
//    	2015-08-19 06:21:17 +0000
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date1 = null;
        Date date2 = null;
		try {
			date1 = dateFormat.parse(o1.getChatDate());
			date2 = dateFormat.parse(o2.getChatDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
    	return (date1.getTime() > date2.getTime() ? 1 : -1);
    }
}