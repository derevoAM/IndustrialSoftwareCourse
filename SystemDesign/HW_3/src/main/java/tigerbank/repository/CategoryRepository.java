package tigerbank.repository;

import tigerbank.domain.Category;
import tigerbank.domain.OperationType;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    void save(Category category);
    Optional<Category> findById(String id);
    List<Category> findByType(OperationType type);
    void deleteById(String id);
    List<Category> findAll();
}
