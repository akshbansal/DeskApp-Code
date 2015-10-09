package com.im.utils;

import java.util.regex.Pattern;

public class PasswordValidation {

	
	
	private final Pattern hasUppercase = Pattern.compile("[A-Z]");
	private final Pattern hasLowercase = Pattern.compile("[a-z]");
	private final Pattern hasNumber = Pattern.compile("\\d");
	private final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]");

	private String password,confirmPassword;
	public PasswordValidation(String password,String confirmPassword){
		this.password = password;
		this.confirmPassword = confirmPassword;
	}
	public PasswordValidation(String password){
		this.password = password;
	}
	public String validateNewPass() {
	    if (password == null) {
	        return "Passwords can not be blank.";
	    }

	    StringBuilder retVal = new StringBuilder();
	    if (password.equals("")) {
	        retVal.append("Passwords can not be blank\n");
	    }
	    else{
	    	if (password.length() < 6) {
	            retVal.append("Password is too short. Needs atleast 6 characters\n");
	        }

	        else  if (!hasUppercase.matcher(password).find()) {
	            retVal.append("Password needs atleast one upper case letter\n");
	        }

	        else if (!hasLowercase.matcher(password).find()) {
	            retVal.append("Password needs lowercase letters\n");
	        }

	        else if (!hasNumber.matcher(password).find()) {
	            retVal.append("Password needs a number\n");
	        }

	        else  if (hasSpecialChar.matcher(password).find()) {
	            retVal.append("Special characters i.e. !,@,#, etc. are not allowed\n");
	        }
	    }
	    if (retVal.length() == 0) {
	        retVal.append("");
	    }

	    return retVal.toString();
	}
	public String validateConfirmPass() {
	    if (password == null || confirmPassword == null) {
	        return "Password or confirmpassword field can not be blank";
	    }

	    StringBuilder retVal = new StringBuilder();
	    if (confirmPassword.equals("")) {
	        retVal.append("Please enter confirm password field\n");
	    }
	    if (!password.equals(confirmPassword)) {
	        retVal.append("Passwords don't match\n");
	    }
	    if (retVal.length() == 0) {
	        retVal.append("");
	    }

	    return retVal.toString();
	}
	
}
