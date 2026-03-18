package tigerbank.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.domain.OperationType;
import tigerbank.factory.BankAccountFactory;
import tigerbank.factory.CategoryFactory;
import tigerbank.factory.OperationFactory;
import tigerbank.repository.*;
import tigerbank.service.*;
import tigerbank.validator.OperationValidator;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OperationFacadeTest {

    private OperationFacade facade;
    private BankAccountServiceImpl bankAccountService;

    @BeforeEach
    void setUp() {
        InMemoryBankAccountRepository bankAccountRepo = new InMemoryBankAccountRepository();
        InMemoryCategoryRepository categoryRepo = new InMemoryCategoryRepository();
        InMemoryOperationRepository operationRepo = new InMemoryOperationRepository();

        bankAccountService = new BankAccountServiceImpl(bankAccountRepo, new BankAccountFactory());
        CategoryServiceImpl categoryService = new CategoryServiceImpl(categoryRepo, new CategoryFactory());
        OperationServiceImpl operationService = new OperationServiceImpl(operationRepo, new OperationFactory());
        BalanceServiceImpl balanceService = new BalanceServiceImpl(bankAccountService, operationRepo);
        OperationValidator validator = new OperationValidator(bankAccountService, categoryService, operationRepo);

        facade = new OperationFacade(operationService, validator, balanceService);

        bankAccountService.create("acc1", "Основной");
        categoryService.create("cat1", OperationType.INCOME, "Зарплата");
    }

    @Test
    @DisplayName("Создание операции пересчитывает баланс счёта")
    void givenValidOperation_whenCreate_thenBalanceRecalculated() {
        facade.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("1000"), LocalDate.now(), "cat1");

        BigDecimal balance = bankAccountService.getById("acc1").getBalance();
        assertEquals(new BigDecimal("1000"), balance);
    }

    @Test
    @DisplayName("Удаление операции пересчитывает баланс счёта")
    void givenExistingOperation_whenDelete_thenBalanceRecalculated() {
        facade.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("1000"), LocalDate.now(), "cat1");

        facade.delete("op1");

        BigDecimal balance = bankAccountService.getById("acc1").getBalance();
        assertEquals(BigDecimal.ZERO, balance);
    }

    @Test
    @DisplayName("Изменение суммы операции пересчитывает баланс счёта")
    void givenExistingOperation_whenUpdateAmount_thenBalanceRecalculated() {
        facade.create("op1", OperationType.INCOME, "acc1",
                new BigDecimal("1000"), LocalDate.now(), "cat1");

        facade.updateAmount("op1", new BigDecimal("500"));

        BigDecimal balance = bankAccountService.getById("acc1").getBalance();
        assertEquals(new BigDecimal("500"), balance);
    }
}
