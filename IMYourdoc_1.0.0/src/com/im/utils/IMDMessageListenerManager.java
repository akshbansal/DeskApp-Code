package com.im.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class IMDMessageListenerManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	List<IMDMessageListener> listeners=new ArrayList<IMDMessageListener> ();
	
	private static IMDMessageListenerManager defManager;
	public static IMDMessageListenerManager sharedManager()
	{
		if(defManager==null)
			defManager=new IMDMessageListenerManager();
		return defManager;
	}
	
	
	private IMDMessageListenerManager()
	{
		super();
	}
	
	
	public void AddIMDListener(IMDMessageListener listener) {
		
		listeners.add(listener);
	}
	public void RemoveIMDListener(IMDMessageListener listener) {
		
		listeners.remove(listener);
	}
	
	public void broadCastUpdate()
	{
		for (int i = 0; i < listeners.size(); i++) 
		{
			IMDMessageListener listener =listeners.get(i);
			listener.updateUI();
		}
		
	}
	public void broadCastUpdate(String messageId)
	{
		for (int i = 0; i < listeners.size(); i++) 
		{
			IMDMessageListener listener =listeners.get(i);
			listener.updateUI(messageId);
		}
		
	}
	
}
