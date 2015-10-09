package com.im.validationListeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;


public class PhoneCheckListener implements FocusListener {
	int size ;
	String field;
	public PhoneCheckListener(int size,String field){
		this.size = size;
		this.field = field;
	}
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void focusLost(FocusEvent evt) {
		   final JTextComponent textComponent = (JTextComponent) evt.getSource();
		    String fieldValue = textComponent.getText();
		    boolean isInteger = false;
		// TODO Auto-generated method stub
		    if(!fieldValue.equals("")){
			    try{
			    	int nn = Integer.parseInt(fieldValue);
			    	System.out.println(nn);
			    	isInteger = true;
			    }
			    catch (Exception e){
			    	JOptionPane.showMessageDialog(textComponent.getRootPane(),field+" is not valid, it should have only digits(0-9).");
			    	textComponent.requestFocus();
					textComponent.setText("");
			    }
		   }
		    if(isInteger== true){
		    	if (fieldValue.trim().length()!= size ){
		    		JOptionPane.showMessageDialog(textComponent.getRootPane(),field+" should be of "+size+" digits");
		    		textComponent.requestFocus();
		    	}
		    }
	}
}
