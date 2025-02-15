package ru.smnadya.app.exceptions;


public class EmailAlreadyExistException extends IllegalEmailException {

    private static final String ACCOUNT_WITH_THIS_EMAIL_ALREADY_EXIST = "Аккаунт с таким email уже существует";

    public EmailAlreadyExistException() {
        super(ACCOUNT_WITH_THIS_EMAIL_ALREADY_EXIST);
    }

}
