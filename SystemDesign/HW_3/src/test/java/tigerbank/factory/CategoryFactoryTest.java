package tigerbank.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;

import static org.junit.jupiter.api.Assertions.*;

class CategoryFactoryTest {

    private final CategoryFactory factory = new CategoryFactory();

    @Test
    @DisplayName("Фабрика создаёт категорию с корректными данными")
    void givenValidParams_whenCreate_thenReturnsCategory() {
        Category category = factory.create("1", OperationType.INCOME, "Зарплата");

        assertEquals("1", category.getId());
        assertEquals(OperationType.INCOME, category.getType());
        assertEquals("Зарплата", category.getName());
    }

    @Test
    @DisplayName("Фабрика бросает исключение если id пустой")
    void givenBlankId_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("", OperationType.INCOME, "Зарплата"));
    }

    @Test
    @DisplayName("Фабрика бросает исключение если name пустой")
    void givenBlankName_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("1", OperationType.INCOME, ""));
    }

    @Test
    @DisplayName("Фабрика бросает исключение если type null")
    void givenNullType_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("1", null, "Зарплата"));
    }
}
