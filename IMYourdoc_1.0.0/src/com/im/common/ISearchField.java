package com.im.common;

import java.util.Map;

public interface ISearchField {
	
	public String fieldValue(Map<String, String> map);
	
	public void loopStart();
	
	public void onSelect(int index, String value);
}
