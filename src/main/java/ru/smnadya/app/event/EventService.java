package ru.smnadya.app.event;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smnadya.app.candidate.Candidate;
import ru.smnadya.app.event.util.EventFilterType;

import java.time.LocalDate;
import java.util.List;


@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEventsForView(Candidate candidate, EventFilterType type, LocalDate filterDate) {
        List<Event> events = eventRepository.getEventsByCandidate(candidate);
        EventFilter filter = (type == null) ? new EventFilter(EventFilterType.ALL) : new EventFilter(type);
        return (filterDate == null) ? filter.getFilteredList(events, LocalDate.now()) : filter.getFilteredList(events, filterDate);
    }

    public Event createEvent(Candidate candidate, Event event) {
        if (candidate == null) throw new IllegalStateException("Candidate ID is required");
        event.setCandidate(candidate);
        return eventRepository.save(event);
    }

    @Transactional
    public void updateEvent(long id, Event newEventData) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        existingEvent.setDescription(newEventData.getDescription());
        existingEvent.setTime(newEventData.getTime());
        existingEvent.setDate(newEventData.getDate());

        eventRepository.save(existingEvent);
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

}
