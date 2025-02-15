package ru.smnadya.app.exceptions;


public class InvalidPasswordException extends AuthenticationException {

    private static final String MESSAGE = "Неверный пароль";

    public InvalidPasswordException() {
        super(MESSAGE);
    }

}
