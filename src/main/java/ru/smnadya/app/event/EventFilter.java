package ru.smnadya.app.event;

import ru.smnadya.app.event.util.EventFilterType;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class EventFilter {

    private final EventFilterType type;

    public EventFilter(EventFilterType type) {
        this.type = type;
    }

    public List<Event> getFilteredList(List<Event> events, LocalDate date) {
        return switch (type) {
            case ALL -> filter(events, e -> !e.getDate().isBefore(date));
            case TODAY, BY_DATE -> filter(events, e -> e.getDate().isEqual(date));
            case PAST -> filter(events, e -> e.getDate().isBefore(date));
        };
    }

    private List<Event> filter(List<Event> events, Predicate<Event> predicate) {
        return events.stream().filter(predicate).sorted().toList();
    }

}
