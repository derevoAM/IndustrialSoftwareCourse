package tigerbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;
import tigerbank.factory.BankAccountFactory;
import tigerbank.factory.CategoryFactory;
import tigerbank.factory.OperationFactory;
import tigerbank.repository.InMemoryBankAccountRepository;
import tigerbank.repository.InMemoryCategoryRepository;
import tigerbank.repository.InMemoryOperationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperationServiceTest {

    private OperationService operationService;

    @BeforeEach
    void setUp() {
        operationService = new OperationServiceImpl(
                new InMemoryOperationRepository(), new OperationFactory());
    }

    @Test
    @DisplayName("Сервис создаёт операцию без описания")
    void givenValidParams_whenCreateWithoutDescription_thenOperationSaved() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat1");

        Operation op = operationService.findById("op1").orElseThrow();
        assertEquals(OperationType.INCOME, op.getType());
        assertEquals(new BigDecimal("5000"), op.getAmount());
        assertNull(op.getDescription());
    }

    @Test
    @DisplayName("Сервис создаёт операцию с описанием")
    void givenValidParamsWithDescription_whenCreate_thenDescriptionSaved() {
        operationService.create("op1", OperationType.EXPENSE, "acc1",
                new BigDecimal("500"), LocalDate.of(2024, 1, 20), "Продукты", "cat1");

        assertEquals("Продукты", operationService.findById("op1").orElseThrow().getDescription());
    }

    @Test
    @DisplayName("findById возвращает пустой Optional если операция не найдена")
    void givenNonExistentId_whenFindById_thenReturnsEmpty() {
        assertTrue(operationService.findById("XXX").isEmpty());
    }

    @Test
    @DisplayName("updateAmount меняет сумму операции")
    void givenExistingOperation_whenUpdateAmount_thenAmountChanged() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat1");

        operationService.updateAmount("op1", new BigDecimal("6000"));

        assertEquals(new BigDecimal("6000"), operationService.findById("op1").orElseThrow().getAmount());
    }

    @Test
    @DisplayName("updateDate меняет дату операции")
    void givenExistingOperation_whenUpdateDate_thenDateChanged() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat1");

        operationService.updateDate("op1", LocalDate.of(2024, 3, 1));

        assertEquals(LocalDate.of(2024, 3, 1), operationService.findById("op1").orElseThrow().getDate());
    }

    @Test
    @DisplayName("updateDescription меняет описание операции")
    void givenExistingOperation_whenUpdateDescription_thenDescriptionChanged() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat1");

        operationService.updateDescription("op1", "Премия");

        assertEquals("Премия", operationService.findById("op1").orElseThrow().getDescription());
    }

    @Test
    @DisplayName("delete удаляет операцию")
    void givenExistingOperation_whenDelete_thenOperationRemoved() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat1");

        operationService.delete("op1");

        assertTrue(operationService.findById("op1").isEmpty());
    }

    @Test
    @DisplayName("delete бросает исключение если операция не найдена")
    void givenNonExistentId_whenDelete_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> operationService.delete("XXX"));
    }

    @Test
    @DisplayName("findByBankAccountId возвращает только операции нужного счёта")
    void givenOperationsForDifferentAccounts_whenFindByBankAccountId_thenReturnsOnlyForThatAccount() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat1");
        operationService.create("op2", OperationType.INCOME, "acc2",
                new BigDecimal("1000"), LocalDate.of(2024, 1, 15), "cat1");

        List<Operation> ops = operationService.findByBankAccountId("acc1");

        assertEquals(1, ops.size());
        assertEquals("op1", ops.get(0).getId());
    }

    @Test
    @DisplayName("findByDateBetween фильтрует операции по периоду")
    void givenOperationsInDifferentDates_whenFindByDateBetween_thenReturnsOnlyInRange() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat1");
        operationService.create("op2", OperationType.EXPENSE, "acc1",
                new BigDecimal("1000"), LocalDate.of(2024, 2, 10), "cat1");

        List<Operation> ops = operationService.findByDateBetween(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        assertEquals(1, ops.size());
        assertEquals("op1", ops.get(0).getId());
    }
}
