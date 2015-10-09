package com.im.validationListeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;


public class SpecialCharacterListener implements FocusListener{
	private final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9]");

	@Override
	public void focusGained(FocusEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent evt) {
		// TODO Auto-generated method stub
		  final JTextComponent textComponent = (JTextComponent) evt.getSource();
		    String text = textComponent.getText();
		    boolean isWhiteSpace = false;
		    int strLen = text.length();
		    for (int i = 0; i < strLen; i++) {
		      if (Character.isWhitespace(text.charAt(i))) {
		    	  isWhiteSpace = true;
		      }
		    }
		  if (hasSpecialChar.matcher(text).find() && isWhiteSpace == true) {
				JOptionPane.showMessageDialog(textComponent.getRootPane().getParent(),"Special characters i.e. !,@,#, etc. are not allowed in Username\n");
				textComponent.requestFocus();
				textComponent.setText("");
	    	}
		    
	}
}
