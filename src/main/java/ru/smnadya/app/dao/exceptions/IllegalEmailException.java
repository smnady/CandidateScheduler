package ru.smnadya.app.dao.exceptions;

public class IllegalEmailException extends Exception {
    public IllegalEmailException() {
        super("Аккаунт с таким email уже существует");
    }
    public IllegalEmailException(String message) {
        super(message);
    }
}
