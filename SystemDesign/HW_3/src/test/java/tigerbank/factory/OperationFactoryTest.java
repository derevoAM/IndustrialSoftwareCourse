package tigerbank.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OperationFactoryTest {

    private final OperationFactory factory = new OperationFactory();

    @Test
    @DisplayName("Фабрика создаёт операцию без описания")
    void givenValidParams_whenCreateWithoutDescription_thenReturnsOperation() {
        Operation op = factory.create("1", OperationType.INCOME, "acc1",
                new BigDecimal("500"), LocalDate.of(2024, 1, 1), "cat1");

        assertEquals("1", op.getId());
        assertEquals(OperationType.INCOME, op.getType());
        assertNull(op.getDescription());
    }

    @Test
    @DisplayName("Фабрика создаёт операцию с описанием")
    void givenValidParamsWithDescription_whenCreate_thenReturnsOperationWithDescription() {
        Operation op = factory.create("1", OperationType.EXPENSE, "acc1",
                new BigDecimal("100"), LocalDate.of(2024, 1, 1), "Кофе", "cat1");

        assertEquals("Кофе", op.getDescription());
    }

    @Test
    @DisplayName("Фабрика бросает исключение если amount нулевой")
    void givenZeroAmount_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("1", OperationType.INCOME, "acc1",
                        BigDecimal.ZERO, LocalDate.now(), "cat1"));
    }

    @Test
    @DisplayName("Фабрика бросает исключение если amount отрицательный")
    void givenNegativeAmount_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("1", OperationType.INCOME, "acc1",
                        new BigDecimal("-100"), LocalDate.now(), "cat1"));
    }

    @Test
    @DisplayName("Фабрика бросает исключение если id пустой")
    void givenBlankId_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("", OperationType.INCOME, "acc1",
                        new BigDecimal("100"), LocalDate.now(), "cat1"));
    }

    @Test
    @DisplayName("Фабрика бросает исключение если bankAccountId пустой")
    void givenBlankBankAccountId_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("1", OperationType.INCOME, "",
                        new BigDecimal("100"), LocalDate.now(), "cat1"));
    }
}
