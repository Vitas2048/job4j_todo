package ru.job4j.todo.repository;

import ru.job4j.todo.persistence.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(int id);

    Task create(Task task);

    void update(Task task);

    void deleteById(int id);

}
