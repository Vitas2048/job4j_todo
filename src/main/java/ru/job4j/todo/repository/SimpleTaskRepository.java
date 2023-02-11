package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class SimpleTaskRepository implements TaskRepository {

    private final SessionFactory sf;

    @Override
    public List<Task> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Task> tasks = session.createQuery("from Task order by id", Task.class).list();
        session.getTransaction().commit();
        session.close();
        return tasks;
    }

    @Override
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<Task> query = session.createQuery("from Task as t where t.id = :fId", Task.class)
                        .setParameter("fId", id);
        Optional<Task> task = query.uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        return task;
    }

    @Override
    public Task create(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        session.close();
        return task;
    }

    @Override
    public boolean update(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("""
                    update Task set description=:fDescription, created=:fCreated, done=:fDone where id=:fId
                    """).setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId());
        var res = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return res > 0;
    }

    @Override
    public boolean deleteById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("""
                    delete Task where id=:fId
                    """).setParameter("fId", id);
        var res = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return res > 0;
    }

    @Override
    public List<Task> findOnlyTrue() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Task> tasks = session.createQuery("from Task where done=true", Task.class).list();
        session.getTransaction().commit();
        session.close();
        return tasks;
    }

    @Override
    public List<Task> findOnlyFalse() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Task> tasks = session.createQuery("from Task where done=false", Task.class).list();
        session.getTransaction().commit();
        session.close();
        return tasks;
    }
}
