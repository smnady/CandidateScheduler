package ru.smnadya.app.candidate;

import org.springframework.stereotype.Service;

/**
 * Сервис для работы с кандидатами.
 */
@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id).orElse(null);
    }

}
