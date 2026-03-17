package tigerbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;
import tigerbank.repository.InMemoryCategoryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private CategoryService service;

    @BeforeEach
    void setUp() {
        service = new CategoryServiceImpl(new InMemoryCategoryRepository());
    }

    @Test
    void create_savesCategory() {
        service.create("1", OperationType.INCOME, "Зарплата");
        Category c = service.findById("1").orElseThrow();
        assertEquals("Зарплата", c.getName());
        assertEquals(OperationType.INCOME, c.getType());
    }

    @Test
    void create_duplicateId_throwsException() {
        service.create("1", OperationType.INCOME, "Зарплата");
        assertThrows(IllegalArgumentException.class,
                () -> service.create("1", OperationType.EXPENSE, "Еда"));
    }

    @Test
    void findById_notFound_returnsEmpty() {
        assertTrue(service.findById("XXX").isEmpty());
    }

    @Test
    void findAll_returnsAll() {
        service.create("1", OperationType.INCOME, "Зарплата");
        service.create("2", OperationType.EXPENSE, "Еда");
        assertEquals(2, service.findAll().size());
    }

    @Test
    void findByType_filtersCorrectly() {
        service.create("1", OperationType.INCOME, "Зарплата");
        service.create("2", OperationType.EXPENSE, "Еда");
        service.create("3", OperationType.EXPENSE, "Транспорт");

        List<Category> expenses = service.findByType(OperationType.EXPENSE);
        assertEquals(2, expenses.size());
        assertTrue(expenses.stream().allMatch(c -> c.getType() == OperationType.EXPENSE));
    }

    @Test
    void updateName_changesName() {
        service.create("1", OperationType.INCOME, "Зарплата");
        service.updateName("1", "Оклад");
        assertEquals("Оклад", service.findById("1").orElseThrow().getName());
    }

    @Test
    void updateName_notFound_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateName("XXX", "Имя"));
    }

    @Test
    void delete_removesCategory() {
        service.create("1", OperationType.INCOME, "Зарплата");
        service.delete("1");
        assertTrue(service.findById("1").isEmpty());
    }

    @Test
    void delete_notFound_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.delete("XXX"));
    }
}
