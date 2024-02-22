package edu.java.exception.exception;

public class UserDoesntExistException extends RuntimeException {
    public UserDoesntExistException() {
        super();
    }

    public UserDoesntExistException(String message) {
        super(message);
    }

    public UserDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesntExistException(Throwable cause) {
        super(cause);
    }
}
