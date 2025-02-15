package ru.smnadya.app.exceptions;

import java.text.MessageFormat;

public class IllegalEmailException extends AuthenticationException {

    private static final String MESSAGE_TEMPLATE = "Неверный email {0}";

    public IllegalEmailException(String email) {
        super(MessageFormat.format(MESSAGE_TEMPLATE, email));
    }

}
