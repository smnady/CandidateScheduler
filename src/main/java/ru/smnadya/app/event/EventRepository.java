package ru.smnadya.app.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smnadya.app.candidate.Candidate;

import java.util.List;

/**
 * Репозиторий для работы с событиями.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> getEventsByCandidate(Candidate candidate);

}
