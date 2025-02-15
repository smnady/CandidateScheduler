package ru.smnadya.app.security.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.smnadya.app.candidate.Candidate;
import ru.smnadya.app.candidate.CandidateRepository;
import ru.smnadya.app.exceptions.AuthenticationException;
import ru.smnadya.app.exceptions.IllegalEmailException;
import ru.smnadya.app.security.PasswordHasher;
import ru.smnadya.app.security.RegistrationService;

/**
 * Реализация сервиса регистрации.
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final CandidateRepository candidateRepository;
    private final PasswordHasher passwordHasher;

    public RegistrationServiceImpl(CandidateRepository candidateRepository, PasswordHasher passwordHasher) {
        this.candidateRepository = candidateRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public Candidate register(Candidate candidate) throws AuthenticationException {
        if (candidateRepository.existsByEmail(candidate.getEmail())) {
            throw new IllegalEmailException(candidate.getEmail());
        }
        candidate.setHashOfPassword(passwordHasher.hashPassword(candidate.getHashOfPassword()));
        Candidate savedCandidate = candidateRepository.save(candidate);
        LOGGER.info("Registration was successful for the candidate {}", savedCandidate);
        return savedCandidate;
    }

}
