package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SimpleTaskRepository implements TaskRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Task> findAll() {
        return crudRepository.query("from Task f JOIN FETCH f.priority order by id", Task.class);
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "from Task f JOIN FETCH f.priority where f.id = :fId", Task.class, Map.of("fId", id));
    }

    @Override
    public Task create(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    @Override
    public boolean update(Task task) {
        try {
            crudRepository.run(session -> session.merge(task));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run("""
                delete from Task where id=:fId
                """, Map.of("fId", id));
        return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Task> filterBy(boolean condition, int userId) {
        return crudRepository.query("from Task f JOIN FETCH f.priority where done = :fDone and user_id=:fUserId",
                Task.class, Map.of("fDone", condition, "fUserId", userId));
    }

    @Override
    public List<Task> findAllByUser(int userId) {
        return crudRepository.query("from Task f JOIN FETCH f.priority where user_id=:fUserId",
                Task.class, Map.of("fUserId", userId));
    }

}
