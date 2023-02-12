package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;
@Repository
@AllArgsConstructor
public class SimpleUserRepository implements UserRepository {

    private final SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return Optional.of(user);
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query = session.createQuery("from User as u where u.login=:fLogin and u.password=:fPassword",
                        User.class)
                .setParameter("fLogin", login)
                .setParameter("fPassword", password);
        Optional<User> res = query.uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        return res;
    }
}
