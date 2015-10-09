package com.im.validationListeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.text.JTextComponent;

import com.im.utils.Constants;
import com.im.utils.PasswordValidation;

public class ConfirmPasswordListener implements FocusListener {
	PasswordValidation passwordCheck ;
	JRootPane jRootPane;
	public ConfirmPasswordListener(JRootPane jRootPane){
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
	    String confirmPassword = textComponent.getText();
		if(!confirmPassword.equals("")){
			passwordCheck = new PasswordValidation(Constants.PASSWORD,confirmPassword);
			String passwordCheckMessage = passwordCheck.validateConfirmPass();
				if (!passwordCheckMessage.equals("")) {
					JOptionPane.showMessageDialog(textComponent.getRootPane(),passwordCheckMessage);
					textComponent.requestFocus();
					return;
					}
			
		}
	}
	
		// TODO Auto-generated method stub

}
