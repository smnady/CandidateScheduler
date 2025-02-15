package ru.smnadya.app.exceptions;


public class AuthenticationException extends Exception {

    private static final String DEFAULT_MESSAGE = "Ошибка аутентификации";

    public AuthenticationException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthenticationException(String message) {
        super(message);
    }

}
