package com.im.validationListeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.text.JTextComponent;

import com.im.utils.Constants;
import com.im.utils.PasswordValidation;

public class PasswordListener implements FocusListener {
	PasswordValidation passwordCheck ;
	JRootPane jRootPane;
	public PasswordListener(JRootPane jRootPane){
		this.jRootPane = jRootPane;
	}
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		final JTextComponent textComponent = (JTextComponent) e.getSource();
	    String password = textComponent.getText();
	    Constants.PASSWORD = password;
		System.out.println(password);
		if(!password.equals("")){
			passwordCheck = new PasswordValidation(password);
			String passwordCheckMessage = passwordCheck.validateNewPass();
				if (!passwordCheckMessage.equals("")) {
					JOptionPane.showMessageDialog(textComponent.getRootPane(),
							passwordCheckMessage);
					textComponent.requestFocus();
					}
		}
	}
	
		// TODO Auto-generated method stub

}