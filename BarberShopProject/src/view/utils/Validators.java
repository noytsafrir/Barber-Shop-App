package view.utils;

import model.exceptions.InvalidInputException;

public class Validators {
	public static void isPhoneNumberSignInValid(String prePhone, String postPhone) throws InvalidInputException {
		if (prePhone.isEmpty() || postPhone.isEmpty()) 
			throw new InvalidInputException("Phone number cannot be empty");
		
		if (prePhone.length() != 3 || postPhone.length() != 7)
			throw new InvalidInputException("Invalid phone number lenght");


		if (!postPhone.matches("[0-9]+") || !postPhone.matches("[0-9]+")) 
			throw new InvalidInputException("Phone number must be only digits");
	}
	

	public static void isFirstNameValid(String name) throws InvalidInputException  {
		if (name.isEmpty())
			throw new InvalidInputException("First name can't be empty");
	}

	public static void isLastNameValid(String name) throws InvalidInputException {
		if (name.isEmpty())
			throw new InvalidInputException("Last name can't be empty");
	}


}
