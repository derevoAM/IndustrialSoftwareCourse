package tigerbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;
import tigerbank.repository.InMemoryBankAccountRepository;
import tigerbank.repository.InMemoryCategoryRepository;
import tigerbank.repository.InMemoryOperationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticalServiceTest {

    private AnalyticalService analyticalService;
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
        analyticalService = new AnalyticalServiceImpl(operationService, categoryService);

        bankAccountService.create("acc1", "Основной");
        categoryService.create("cat_income", OperationType.INCOME, "Зарплата");
        categoryService.create("cat_expense", OperationType.EXPENSE, "Еда");

        operationService.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("5000"), LocalDate.of(2024, 1, 15), "cat_income");
        operationService.create("op2", OperationType.EXPENSE, "acc1",
                new BigDecimal("1200"), LocalDate.of(2024, 1, 20), "cat_expense");
        operationService.create("op3", OperationType.EXPENSE, "acc1",
                new BigDecimal("800"), LocalDate.of(2024, 2, 10), "cat_expense");
    }

    @Test
    void calculateDifference_incomeMinusExpense() {
        BigDecimal result = analyticalService.calculateDifference("acc1",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        assertEquals(new BigDecimal("3800"), result);
    }

    @Test
    void calculateDifference_onlyExpense_returnsNegative() {
        BigDecimal result = analyticalService.calculateDifference("acc1",
                LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28));
        assertEquals(new BigDecimal("-800"), result);
    }

    @Test
    void calculateDifference_emptyPeriod_returnsZero() {
        BigDecimal result = analyticalService.calculateDifference("acc1",
                LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateDifference_emptyAccount_returnsZero() {
        bankAccountService.create("acc2", "Пустой");
        BigDecimal result = analyticalService.calculateDifference("acc2",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void groupByCategory_returnsCorrectAmounts() {
        Map<Category, BigDecimal> result = analyticalService.groupByCategory("acc1",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        assertEquals(2, result.size());
        result.forEach((cat, amount) -> {
            if (cat.getId().equals("cat_income")) {
                assertEquals(new BigDecimal("5000"), amount);
            } else if (cat.getId().equals("cat_expense")) {
                assertEquals(new BigDecimal("-1200"), amount);
            }
        });
    }

    @Test
    void groupByCategory_emptyPeriod_returnsEmptyMap() {
        Map<Category, BigDecimal> result = analyticalService.groupByCategory("acc1",
                LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertTrue(result.isEmpty());
    }
}
