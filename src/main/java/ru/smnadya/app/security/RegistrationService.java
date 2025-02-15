package ru.smnadya.app.security;

import ru.smnadya.app.candidate.Candidate;
import ru.smnadya.app.exceptions.AuthenticationException;

/**
 * Сервис регистрации пользователей.
 */
public interface RegistrationService {

    Candidate register(Candidate candidate) throws AuthenticationException;

}
