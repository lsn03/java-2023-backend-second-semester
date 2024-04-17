package edu.java.bot.exception;

public class RecordAlreadyExistException extends RuntimeException {
    public RecordAlreadyExistException(String message) {
        super(message);
    }

    public RecordAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordAlreadyExistException(Throwable cause) {
        super(cause);
    }

}
