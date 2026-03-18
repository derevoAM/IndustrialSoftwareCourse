package tigerbank.repository;

import org.springframework.stereotype.Repository;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<String, Category> storage = new HashMap<>();

    @Override
    public void save(Category category) {
        storage.put(category.getId(), category);
    }

    @Override
    public Optional<Category> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Category> findByType(OperationType type) {
        return storage.values().stream()
                .filter(o -> o.getType().equals(type))
                .toList();
    }
}
