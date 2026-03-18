package tigerbank.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.factory.BankAccountFactory;
import tigerbank.factory.OperationFactory;
import tigerbank.repository.InMemoryBankAccountRepository;
import tigerbank.repository.InMemoryOperationRepository;
import tigerbank.service.BankAccountServiceImpl;
import tigerbank.service.OperationServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountFacadeTest {

    private BankAccountFacade facade;
    private OperationServiceImpl operationService;

    @BeforeEach
    void setUp() {
        BankAccountServiceImpl bankAccountService = new BankAccountServiceImpl(
                new InMemoryBankAccountRepository(), new BankAccountFactory());
        operationService = new OperationServiceImpl(
                new InMemoryOperationRepository(), new OperationFactory());
        facade = new BankAccountFacade(bankAccountService, operationService);
    }

    @Test
    @DisplayName("Фасад удаляет счёт если нет операций")
    void givenAccountWithNoOperations_whenDelete_thenAccountRemoved() {
        facade.create("acc1", "Основной");

        facade.delete("acc1");

        assertTrue(facade.findById("acc1").isEmpty());
    }

    @Test
    @DisplayName("Фасад бросает исключение при удалении счёта с операциями")
    void givenAccountWithOperations_whenDelete_thenThrowsException() {
        facade.create("acc1", "Основной");
        operationService.create("op1", tigerbank.domain.OperationType.INCOME, "acc1",
                new BigDecimal("100"), LocalDate.now(), "cat1");

        assertThrows(IllegalStateException.class, () -> facade.delete("acc1"));
    }

    @Test
    @DisplayName("Фасад создаёт счёт и находит его по id")
    void givenValidParams_whenCreate_thenFindByIdReturnsAccount() {
        facade.create("acc1", "Основной");

        assertTrue(facade.findById("acc1").isPresent());
        assertEquals("Основной", facade.findById("acc1").get().getName());
    }
}
