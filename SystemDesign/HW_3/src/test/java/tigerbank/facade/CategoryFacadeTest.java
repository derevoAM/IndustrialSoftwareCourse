package tigerbank.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.domain.OperationType;
import tigerbank.factory.CategoryFactory;
import tigerbank.factory.OperationFactory;
import tigerbank.repository.InMemoryCategoryRepository;
import tigerbank.repository.InMemoryOperationRepository;
import tigerbank.service.CategoryServiceImpl;
import tigerbank.service.OperationServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CategoryFacadeTest {

    private CategoryFacade facade;
    private OperationServiceImpl operationService;

    @BeforeEach
    void setUp() {
        CategoryServiceImpl categoryService = new CategoryServiceImpl(
                new InMemoryCategoryRepository(), new CategoryFactory());
        operationService = new OperationServiceImpl(
                new InMemoryOperationRepository(), new OperationFactory());
        facade = new CategoryFacade(categoryService, operationService);
    }

    @Test
    @DisplayName("Фасад удаляет категорию если нет операций")
    void givenCategoryWithNoOperations_whenDelete_thenCategoryRemoved() {
        facade.create("cat1", OperationType.INCOME, "Зарплата");

        facade.delete("cat1");

        assertTrue(facade.findById("cat1").isEmpty());
    }

    @Test
    @DisplayName("Фасад бросает исключение при удалении категории с операциями")
    void givenCategoryWithOperations_whenDelete_thenThrowsException() {
        facade.create("cat1", OperationType.INCOME, "Зарплата");
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("100"), LocalDate.now(), "cat1");

        assertThrows(IllegalStateException.class, () -> facade.delete("cat1"));
    }

    @Test
    @DisplayName("Фасад находит категории по типу")
    void givenCategoriesOfDifferentTypes_whenFindByType_thenReturnsOnlyMatchingType() {
        facade.create("cat1", OperationType.INCOME, "Зарплата");
        facade.create("cat2", OperationType.EXPENSE, "Еда");

        var incomeCategories = facade.findByType(OperationType.INCOME);

        assertEquals(1, incomeCategories.size());
        assertEquals("cat1", incomeCategories.get(0).getId());
    }
}
