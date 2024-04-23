package ru.smnadya.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import ru.smnadya.app.constants.EventType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ofPattern;

@Entity
@Table(name = "event")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Comparable<Event>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "event_date", nullable = false)
    @NotNull(message = "Установите дату мероприятия")
    @JsonProperty("date")
    private LocalDate date;
    @Column(name = "event_time")
    @JsonProperty("time")
    private LocalTime time;
    @Enumerated(EnumType.STRING)
    @JsonProperty("type")
    private EventType type;
    @Column(name = "description")
    @JsonProperty("description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Candidate candidate;

    public Event() {}

    public Event(String description, LocalDate date,
                 LocalTime time, EventType type, Candidate candidate) {
        this.description = description;
        this.date = date;
        this.time = time;
        this.type = type;
        this.candidate = candidate;
    }

    public Event(String eventDescription, LocalDate eventDate,
                 LocalTime eventTime) {
        this.description = eventDescription;
        this.date = eventDate;
        this.time = eventTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public @NotNull LocalDate getDate() {
        return date;
    }

    public void setDate(@NotNull LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }
    public String getDateRepresentation() {
        DateTimeFormatter dateTimeFormatter =
                ofPattern("dd MMMM yyyy", new Locale("ru"));
        return date.format(dateTimeFormatter);
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
    public boolean isTimeSet() {
        return time != null;
    }
    public EventType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    @Override
    public int compareTo(Event o) {
        int firstComparison = date.compareTo(o.getDate());
        if (time != null && o.getTime() != null)
            return firstComparison != 0 ? firstComparison : time.compareTo(o.getTime());
        return firstComparison;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(date, event.date) && Objects.equals(time, event.time) && type == event.type && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, type, description);
    }
}
