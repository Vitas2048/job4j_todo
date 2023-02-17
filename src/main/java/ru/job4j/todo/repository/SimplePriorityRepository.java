package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SimplePriorityRepository implements PriorityRepository {

    private CrudRepository crudRepository;

    @Override
    public List<Priority> findAll() {
        return crudRepository.query("from Priority", Priority.class);
    }

    @Override
    public Optional<Priority> findById(int id) {
        return crudRepository.optional("from Priority where id=:fId", Priority.class, Map.of("fId", id));
    }
}
