package edu.java.exception.exception;

public class IncorrectParametersException extends RuntimeException{
    public IncorrectParametersException() {
        super();
    }

    public IncorrectParametersException(String message) {
        super(message);
    }

    public IncorrectParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectParametersException(Throwable cause) {
        super(cause);
    }
}