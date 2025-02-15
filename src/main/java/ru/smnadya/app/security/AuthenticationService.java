package ru.smnadya.app.security;


import ru.smnadya.app.candidate.Candidate;
import ru.smnadya.app.exceptions.AuthenticationException;

/**
 * Сервис аутентификации.
 */
public interface AuthenticationService {

    Candidate authenticate(String login, String password) throws AuthenticationException;

}
