package ru.job4j.todo.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.persistence.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    TaskRepository taskRepository;


    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task create(Task task) {
        return taskRepository.create(task);
    }

    @Override
    public void update(Task task) {
        taskRepository.update(task);
    }

    @Override
    public void deleteById(int id) {
        taskRepository.deleteById(id);
    }
}
