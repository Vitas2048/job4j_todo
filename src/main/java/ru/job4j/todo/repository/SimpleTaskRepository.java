package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.persistence.Task;

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
        List<Task> tasks = session.createQuery("from Task", Task.class).list()
                .stream().sorted(Comparator.comparing(Task::getId)).collect(Collectors.toList());
        session.getTransaction().commit();
        session.close();
        return tasks;
    }

    @Override
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Optional<Task> task = Optional.ofNullable(session.get(Task.class, id));
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
    public void update(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(task);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void deleteById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Task task = new Task();
        task.setId(id);
        session.delete(task);
        session.getTransaction().commit();
        session.close();
    }
}
