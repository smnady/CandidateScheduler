package ru.smnadya.app.candidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с кандидатами.
 */
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Candidate findByEmail(String email);

    boolean existsByEmail(String email);

}
