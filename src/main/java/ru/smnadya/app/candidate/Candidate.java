package ru.smnadya.app.candidate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.smnadya.app.event.Event;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "candidate")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
