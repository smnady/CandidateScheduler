package ru.smnadya.app.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.smnadya.app.dao.exceptions.IllegalEmailException;
import ru.smnadya.app.models.Candidate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
@SuppressWarnings("deprecation")
public class CandidateDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public CandidateDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    public void createCandidate(Candidate candidate) throws IllegalEmailException {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(candidate);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalEmailException();
        }
    }
    public Candidate getCandidateBy(String email) throws IllegalEmailException{

        String query = "SELECT * FROM candidate WHERE email = ?";

        Candidate candidate = sessionFactory.getCurrentSession().doReturningWork(connection -> {
            try(PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    long id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String patronymic = resultSet.getString("patronymic");
                    String surname = resultSet.getString("surname");
                    String hash = resultSet.getString("hash_of_password");
                    return new Candidate(id, name, patronymic, surname, email, hash);
                } else return null;
            }
        });

        if (candidate != null) return candidate;
        else throw new IllegalEmailException("Аккаунта с таким email не существует");
    }

    public Candidate getCandidate(long candidateId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Candidate.class, candidateId);
    }
}
