package model.exceptions;

public class AlreadyExistException extends Exception {
	public AlreadyExistException(String value) {
		super(value + " already exist");
	}
}
