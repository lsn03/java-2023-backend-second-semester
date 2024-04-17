package edu.java.bot.exception;

public class ListEmptyException extends RuntimeException {

    public ListEmptyException(String message) {
        super(message);
    }

    public ListEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListEmptyException(Throwable cause) {
        super(cause);
    }
}
