package tigerbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;
import tigerbank.repository.InMemoryBankAccountRepository;
import tigerbank.repository.InMemoryCategoryRepository;
import tigerbank.repository.InMemoryOperationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperationServiceTest {

    private OperationService operationService;
    private BankAccountService bankAccountService;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        var bankRepo = new InMemoryBankAccountRepository();
        var categoryRepo = new InMemoryCategoryRepository();
        var operationRepo = new InMemoryOperationRepository();

        bankAccountService = new BankAccountServiceImpl(bankRepo);
        categoryService = new CategoryServiceImpl(categoryRepo);
        operationService = new OperationServiceImpl(operationRepo, bankRepo, categoryRepo);

        bankAccountService.create("acc1", "Основной", new BigDecimal("1000"));
        categoryService.create("cat_income", OperationType.INCOME, "Зарплата");
        categoryService.create("cat_expense", OperationType.EXPENSE, "Еда");
    }

    @Test
    void create_withoutDescription_savesOperation() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        Operation op = operationService.findById("op1").orElseThrow();
        assertEquals(OperationType.INCOME, op.getType());
        assertEquals(new BigDecimal("5000"), op.getAmount());
        assertNull(op.getDescription());
    }

    @Test
    void create_withDescription_savesDescription() {
        operationService.create("op1", OperationType.EXPENSE, "acc1",
                new BigDecimal("500"), LocalDate.of(2024, 1, 20), "Продукты", "cat_expense");
        assertEquals("Продукты", operationService.findById("op1").orElseThrow().getDescription());
    }

    @Test
    void create_duplicateId_throwsException() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        assertThrows(IllegalArgumentException.class, () ->
                operationService.create("op1", OperationType.INCOME, "acc1",
                        new BigDecimal("100"), LocalDate.of(2024, 1, 15), "cat_income"));
    }

    @Test
    void create_accountNotFound_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.create("op1", OperationType.INCOME, "XXX",
                        new BigDecimal("100"), LocalDate.of(2024, 1, 15), "cat_income"));
    }

    @Test
    void create_categoryNotFound_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.create("op1", OperationType.INCOME, "acc1",
                        new BigDecimal("100"), LocalDate.of(2024, 1, 15), "XXX"));
    }

    @Test
    void create_typeMismatch_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.create("op1", OperationType.INCOME, "acc1",
                        new BigDecimal("100"), LocalDate.of(2024, 1, 15), "cat_expense"));
    }

    @Test
    void create_recalculatesBalance() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        operationService.create("op2", OperationType.EXPENSE, "acc1",
                new BigDecimal("1200"), LocalDate.of(2024, 1, 20), "cat_expense");
        assertEquals(new BigDecimal("3800"), bankAccountService.findById("acc1").orElseThrow().getBalance());
    }

    @Test
    void updateAmount_recalculatesBalance() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        operationService.updateAmount("op1", new BigDecimal("6000"));
        assertEquals(new BigDecimal("6000"), bankAccountService.findById("acc1").orElseThrow().getBalance());
    }

    @Test
    void updateAmount_notFound_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                operationService.updateAmount("XXX", new BigDecimal("100")));
    }

    @Test
    void updateDate_changesDate() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        operationService.updateDate("op1", LocalDate.of(2024, 3, 1));
        assertEquals(LocalDate.of(2024, 3, 1), operationService.findById("op1").orElseThrow().getDate());
    }

    @Test
    void updateDescription_changesDescription() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        operationService.updateDescription("op1", "Премия");
        assertEquals("Премия", operationService.findById("op1").orElseThrow().getDescription());
    }

    @Test
    void delete_removesOperation_recalculatesBalance() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        operationService.create("op2", OperationType.EXPENSE, "acc1",
                new BigDecimal("1000"), LocalDate.of(2024, 1, 20), "cat_expense");
        operationService.delete("op2");
        assertTrue(operationService.findById("op2").isEmpty());
        assertEquals(new BigDecimal("5000"), bankAccountService.findById("acc1").orElseThrow().getBalance());
    }

    @Test
    void delete_notFound_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> operationService.delete("XXX"));
    }

    @Test
    void findByBankAccountId_returnsOnlyForThatAccount() {
        bankAccountService.create("acc2", "Второй");
        categoryService.create("cat2", OperationType.INCOME, "Другое");
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        operationService.create("op2", OperationType.INCOME, "acc2",
                new BigDecimal("1000"), LocalDate.of(2024, 1, 15), "cat2");

        List<Operation> ops = operationService.findByBankAccountId("acc1");
        assertEquals(1, ops.size());
        assertEquals("op1", ops.get(0).getId());
    }

    @Test
    void findByDateBetween_filtersCorrectly() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        operationService.create("op2", OperationType.EXPENSE, "acc1",
                new BigDecimal("1000"), LocalDate.of(2024, 2, 10), "cat_expense");

        List<Operation> ops = operationService.findByDateBetween(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        assertEquals(1, ops.size());
        assertEquals("op1", ops.get(0).getId());
    }

    @Test
    void recalculateBalance_correctlyComputesBalance() {
        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        operationService.create("op2", OperationType.EXPENSE, "acc1",
                new BigDecimal("1200"), LocalDate.of(2024, 1, 20), "cat_expense");
        operationService.create("op3", OperationType.EXPENSE, "acc1",
                new BigDecimal("800"), LocalDate.of(2024, 2, 5), "cat_expense");
        operationService.recalculateBalance("acc1");
        assertEquals(new BigDecimal("3000"), bankAccountService.findById("acc1").orElseThrow().getBalance());
    }
}
