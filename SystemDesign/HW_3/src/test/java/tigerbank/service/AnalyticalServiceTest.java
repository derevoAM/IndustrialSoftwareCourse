package tigerbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;
import tigerbank.factory.BankAccountFactory;
import tigerbank.factory.CategoryFactory;
import tigerbank.factory.OperationFactory;
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

        bankAccountService = new BankAccountServiceImpl(bankRepo, new BankAccountFactory());
        categoryService = new CategoryServiceImpl(categoryRepo, new CategoryFactory());
        operationService = new OperationServiceImpl(operationRepo, new OperationFactory());
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
    @DisplayName("calculateDifference возвращает доходы минус расходы за период")
    void givenOperationsInPeriod_whenCalculateDifference_thenReturnsIncomeMinusExpense() {
        BigDecimal result = analyticalService.calculateDifference("acc1",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        assertEquals(new BigDecimal("3800"), result);
    }

    @Test
    @DisplayName("calculateDifference возвращает отрицательное значение если только расходы")
    void givenOnlyExpensesInPeriod_whenCalculateDifference_thenReturnsNegative() {
        BigDecimal result = analyticalService.calculateDifference("acc1",
                LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28));

        assertEquals(new BigDecimal("-800"), result);
    }

    @Test
    @DisplayName("calculateDifference возвращает ноль если нет операций в периоде")
    void givenNoOperationsInPeriod_whenCalculateDifference_thenReturnsZero() {
        BigDecimal result = analyticalService.calculateDifference("acc1",
                LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("groupByCategory возвращает корректные суммы по категориям")
    void givenOperationsInPeriod_whenGroupByCategory_thenReturnsCorrectAmounts() {
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
    @DisplayName("groupByCategory возвращает пустую карту если нет операций в периоде")
    void givenNoOperationsInPeriod_whenGroupByCategory_thenReturnsEmptyMap() {
        Map<Category, BigDecimal> result = analyticalService.groupByCategory("acc1",
                LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertTrue(result.isEmpty());
    }
}
