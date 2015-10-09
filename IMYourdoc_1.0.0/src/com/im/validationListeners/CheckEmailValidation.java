package com.im.validationListeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.text.JTextComponent;

import com.im.utils.EmailValidator;

public class CheckEmailValidation implements FocusListener {
	JRootPane jRootPane;
	public CheckEmailValidation(JRootPane jRootPane){
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
	    String EmailID = textComponent.getText();
			EmailValidator emailValidator = new EmailValidator();
			if (!EmailID.equals("")) {
				if (!emailValidator.validate(EmailID.trim())) {
					System.out.print("Invalid Email ID");
					JOptionPane.showMessageDialog(textComponent.getRootPane(), "Invalid Email Id");
					textComponent.requestFocus();
					}
				}
	}


}