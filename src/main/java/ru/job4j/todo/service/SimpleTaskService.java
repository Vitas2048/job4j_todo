package ru.job4j.todo.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.PriorityRepository;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    TaskRepository taskRepository;

    PriorityRepository priorityRepository;

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public boolean create(Task task) {
        return taskRepository.create(task) == null && priorityExist(task);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task) || priorityExist(task);
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public List<Task> filterBy(boolean condition, int userId) {
        return taskRepository.filterBy(condition, userId);
    }

    @Override
    public List<Task> findAllByUser(int userId) {
        return taskRepository.findAllByUser(userId);
    }

    private boolean priorityExist(Task task) {
       return priorityRepository.findById(task.getPriority().getId()).isEmpty();
    }

}
