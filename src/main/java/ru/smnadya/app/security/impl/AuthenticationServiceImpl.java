package ru.smnadya.app.security.impl;

import org.springframework.stereotype.Service;
import ru.smnadya.app.candidate.Candidate;
import ru.smnadya.app.candidate.CandidateRepository;
import ru.smnadya.app.exceptions.AuthenticationException;
import ru.smnadya.app.exceptions.IllegalEmailException;
import ru.smnadya.app.exceptions.InvalidPasswordException;
import ru.smnadya.app.security.AuthenticationService;
import ru.smnadya.app.security.PasswordHasher;

/**
 * Реализация сервиса аутентификации.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CandidateRepository candidateRepository;
    private final PasswordHasher passwordHasher;

    public AuthenticationServiceImpl(CandidateRepository candidateRepository, PasswordHasher passwordHasher) {
        this.candidateRepository = candidateRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public Candidate authenticate(String email, String password) throws AuthenticationException {
        Candidate candidate = candidateRepository.findByEmail(email);
        if (candidate == null) {
            throw new IllegalEmailException(email);
        } else if (!candidate.getHashOfPassword().equals(passwordHasher.hashPassword(password))) {
            throw new InvalidPasswordException();
        }
        return candidate;
    }
}
