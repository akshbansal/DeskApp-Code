package com.im.bo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;


public class CustomComparatorConversation implements Comparator<ConversationBo> {
    @Override
    public int compare(ConversationBo o1, ConversationBo o2) {
//    	2015-08-19 06:21:17 +0000
    	
    	
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date1 = null;
        Date date2 = null;
		try {
//			2015-08-17T16:40:09.603Z
			date1 = dateFormat.parse(o1.getStart());
			date2 = dateFormat.parse(o2.getStart());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
    	return (date1.getTime() > date2.getTime() ? 1 : -1);
    }
}