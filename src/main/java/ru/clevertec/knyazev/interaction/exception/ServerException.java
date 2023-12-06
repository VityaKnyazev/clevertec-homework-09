package ru.clevertec.knyazev.interaction.exception;

public class ServerException extends RuntimeException {

    private final static String DEFAULT_ERROR = "Server error";

    public ServerException() {
        super(DEFAULT_ERROR);
    }

    public ServerException(String message) {
        super(String.format("%s: %s%n", DEFAULT_ERROR, message));
    }

    public ServerException(String message, Throwable cause) {
        super(String.format("%s: %s%n", DEFAULT_ERROR, message), cause);
    }

    public ServerException(Throwable cause) {
        super(String.format("%s: %s%n", DEFAULT_ERROR, cause.getMessage()), cause);
    }
}
