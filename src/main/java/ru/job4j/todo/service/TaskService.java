package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> findAll();

    Optional<Task> findById(int id);

    boolean create(Task task);

    boolean update(Task task);

    boolean deleteById(int id);

    List<Task> filterBy(boolean condition, int userId);

    List<Task> findAllByUser(int userId);
}
