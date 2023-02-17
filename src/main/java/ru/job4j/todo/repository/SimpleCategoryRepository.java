package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class SimpleCategoryRepository implements CategoryRepository {
    private CrudRepository crudRepository;

    @Override
    public List<Category> findAll() {
        return crudRepository.query("from Category", Category.class);
    }
    @Override
    public Optional<Category> findById(int id) {
        return crudRepository.optional("from Category where id=:fId", Category.class, Map.of("fId", id));
    }
}
