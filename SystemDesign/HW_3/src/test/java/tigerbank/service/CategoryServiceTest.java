package tigerbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;
import tigerbank.factory.CategoryFactory;
import tigerbank.repository.InMemoryCategoryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private CategoryService service;

    @BeforeEach
    void setUp() {
        service = new CategoryServiceImpl(new InMemoryCategoryRepository(), new CategoryFactory());
    }

    @Test
    @DisplayName("Сервис создаёт и сохраняет категорию")
    void givenValidParams_whenCreate_thenCategorySaved() {
        service.create("1", OperationType.INCOME, "Зарплата");

        Category c = service.findById("1").orElseThrow();
        assertEquals("Зарплата", c.getName());
        assertEquals(OperationType.INCOME, c.getType());
    }

    @Test
    @DisplayName("Сервис бросает исключение при дублирующемся id")
    void givenExistingId_whenCreate_thenThrowsException() {
        service.create("1", OperationType.INCOME, "Зарплата");

        assertThrows(IllegalArgumentException.class,
                () -> service.create("1", OperationType.EXPENSE, "Еда"));
    }

    @Test
    @DisplayName("findById возвращает пустой Optional если категория не найдена")
    void givenNonExistentId_whenFindById_thenReturnsEmpty() {
        assertTrue(service.findById("XXX").isEmpty());
    }

    @Test
    @DisplayName("findAll возвращает все созданные категории")
    void givenMultipleCategories_whenFindAll_thenReturnsAll() {
        service.create("1", OperationType.INCOME, "Зарплата");
        service.create("2", OperationType.EXPENSE, "Еда");

        assertEquals(2, service.findAll().size());
    }

    @Test
    @DisplayName("findByType возвращает только категории нужного типа")
    void givenCategoriesOfDifferentTypes_whenFindByType_thenFiltersCorrectly() {
        service.create("1", OperationType.INCOME, "Зарплата");
        service.create("2", OperationType.EXPENSE, "Еда");
        service.create("3", OperationType.EXPENSE, "Транспорт");

        List<Category> expenses = service.findByType(OperationType.EXPENSE);

        assertEquals(2, expenses.size());
        assertTrue(expenses.stream().allMatch(c -> c.getType() == OperationType.EXPENSE));
    }

    @Test
    @DisplayName("updateName меняет название категории")
    void givenExistingCategory_whenUpdateName_thenNameChanged() {
        service.create("1", OperationType.INCOME, "Зарплата");

        service.updateName("1", "Оклад");

        assertEquals("Оклад", service.findById("1").orElseThrow().getName());
    }

    @Test
    @DisplayName("updateName бросает исключение если категория не найдена")
    void givenNonExistentId_whenUpdateName_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateName("XXX", "Имя"));
    }

    @Test
    @DisplayName("delete удаляет категорию")
    void givenExistingCategory_whenDelete_thenCategoryRemoved() {
        service.create("1", OperationType.INCOME, "Зарплата");

        service.delete("1");

        assertTrue(service.findById("1").isEmpty());
    }

    @Test
    @DisplayName("delete бросает исключение если категория не найдена")
    void givenNonExistentId_whenDelete_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.delete("XXX"));
    }
}
