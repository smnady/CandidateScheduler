package ru.smnadya.app.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smnadya.app.models.Candidate;
import ru.smnadya.app.models.Event;

import java.sql.PreparedStatement;
import java.util.List;


@Component
@SuppressWarnings("deprecation")
public class EventDAO {
    private final SessionFactory sessionFactory;
    @Autowired
    public EventDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    public List<Event> getEventsBy(long candidateId) {
        Session session = sessionFactory.getCurrentSession();
        Candidate candidate = session.get(Candidate.class, candidateId);
        return candidate.getEvents();
    }

    public void addEvent(Event event) {
        Session session = sessionFactory.getCurrentSession();
        session.save(event);
    }

    public void deleteEvent(long eventId) {
        Session session = sessionFactory.getCurrentSession();
        String query = "DELETE FROM event WHERE id = ?;";
        session.doWork(connection -> {
            try(PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, eventId);
                statement.executeUpdate();
            }
        });
    }
    public void updateEvent(long id, Event event) {
        Session session = sessionFactory.getCurrentSession();
        String query = "UPDATE event SET event_date=?, event_time=?, description=? WHERE id=?";
        java.sql.Time time = event.isTimeSet() ? java.sql.Time.valueOf(event.getTime()) : null;
        session.doWork(connection -> {
            try(PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setDate(1, java.sql.Date.valueOf(event.getDate()));
                statement.setTime(2, time);
                statement.setString(3, event.getDescription());
                statement.setLong(4, id);
                statement.executeUpdate();
            }
        });
        Event e = session.get(Event.class, id);
        e.setDescription(event.getDescription());
        e.setDate(event.getDate());
        e.setTime(event.getTime());
    }
    public Event getEvent(long eventId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Event.class, eventId);
    }
}
