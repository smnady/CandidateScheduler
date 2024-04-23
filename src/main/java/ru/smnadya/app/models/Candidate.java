package ru.smnadya.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    @NotEmpty(message = "Нужно указать имя")
    private String name;
    @Column(name = "patronymic", nullable = false)
    @NotEmpty(message = "Нужно указать отчество")
    private String patronymic;
    @Column(name = "surname", nullable = false)
    @NotEmpty(message = "Нужно указать фамилию")
    private String surname;

    @Column(name = "email", unique = true, nullable = false)
    @NotEmpty(message = "Нужно указать адрес эл. почты")
    private String email;
    @Column(name = "hash_of_password", nullable = false)
    private String hashOfPassword;
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<Event> events;

    public Candidate() {}

    public Candidate(String name, String patronymic, String surname,
                     String email, String hashOfPassword) {
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.email = email;
        this.hashOfPassword = hashOfPassword;
    }

    public Candidate(long id, String name, String patronymic,
                     String surname, String email, String hashOfPassword) {
        this.id = id;
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.email = email;
        this.hashOfPassword = hashOfPassword;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashOfPassword() {
        return hashOfPassword;
    }

    public void setHashOfPassword(String hashOfPassword) {
        this.hashOfPassword = hashOfPassword;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(id, candidate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
