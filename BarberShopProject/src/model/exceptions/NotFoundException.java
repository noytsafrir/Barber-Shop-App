package model.exceptions;

public class NotFoundException extends Exception {
	public NotFoundException(String value) {
		super(value + " is not found in the system");
	}
}
