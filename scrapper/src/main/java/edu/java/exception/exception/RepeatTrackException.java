package edu.java.exception.exception;

public class RepeatTrackException extends RuntimeException {
    public RepeatTrackException(String message) {
        super(message);
    }

    public RepeatTrackException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatTrackException(Throwable cause) {
        super(cause);
    }
}
