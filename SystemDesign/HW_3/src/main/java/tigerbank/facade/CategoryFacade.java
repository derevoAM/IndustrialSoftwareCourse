package tigerbank.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;
import tigerbank.service.CategoryService;
import tigerbank.service.OperationService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryFacade {
    private final CategoryService categoryService;
    private final OperationService operationService;

    public void create(String id, OperationType type, String name) {
        categoryService.create(id, type, name);
    }

    public void updateName(String id, String newName) {
        categoryService.updateName(id, newName);
    }

    public void delete(String id) {
        boolean hasOperations = !operationService.findByCategoryId(id).isEmpty();
        if (hasOperations) {
            throw new IllegalStateException(
                    "Нельзя удалить категорию " + id + ": существуют привязанные операции");
        }
        categoryService.delete(id);
    }

    public Optional<Category> findById(String id) {
        return categoryService.findById(id);
    }

    public Category getById(String id) {
        return categoryService.getById(id);
    }

    public List<Category> findAll() {
        return categoryService.findAll();
    }

    public List<Category> findByType(OperationType type) {
        return categoryService.findByType(type);
    }
}
