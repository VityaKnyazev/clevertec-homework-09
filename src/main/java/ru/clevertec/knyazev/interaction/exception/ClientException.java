package ru.clevertec.knyazev.interaction.exception;

public class ClientException extends RuntimeException {

    private final static String CLIENT_ERROR = "Client error";

    public ClientException() {
        super(CLIENT_ERROR);
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }
}
