package tigerbank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;
import tigerbank.factory.CategoryFactory;
import tigerbank.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryFactory categoryFactory;

    @Override
    public void create(String id, OperationType type, String name) {
        if (findById(id).isPresent()) {
            throw new IllegalArgumentException("Категория с id " + id + " уже существует");
        }
        categoryRepository.save(categoryFactory.create(id, type, name));
    }

    @Override
    public void updateName(String id, String newName) {
        Category category = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Категория с id " + id + " не найдена"));
        category.setName(newName);
    }

    @Override
    public void delete(String id) {
        findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Категория с id " + id + " не найдена"));
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category getById(String id)
    {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Категория с id " + id + " не найдена"));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findByType(OperationType type) {
        return categoryRepository.findByType(type);
    }
}
