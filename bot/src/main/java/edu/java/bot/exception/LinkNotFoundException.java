package edu.java.bot.exception;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException(String message) {
        super(message);
    }

    public LinkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LinkNotFoundException(Throwable cause) {
        super(cause);
    }
}
