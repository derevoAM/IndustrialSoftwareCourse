package tigerbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.domain.BankAccount;
import tigerbank.factory.BankAccountFactory;
import tigerbank.repository.InMemoryBankAccountRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceTest {

    private BankAccountService service;

    @BeforeEach
    void setUp() {
        service = new BankAccountServiceImpl(new InMemoryBankAccountRepository(), new BankAccountFactory());
    }

    @Test
    @DisplayName("Сервис создаёт счёт с нулевым балансом")
    void givenValidParams_whenCreate_thenAccountHasZeroBalance() {
        service.create("1", "Основной");

        BankAccount account = service.findById("1").orElseThrow();
        assertEquals("Основной", account.getName());
        assertEquals(java.math.BigDecimal.ZERO, account.getBalance());
    }

    @Test
    @DisplayName("Сервис бросает исключение при дублирующемся id")
    void givenExistingId_whenCreate_thenThrowsException() {
        service.create("1", "Основной");

        assertThrows(IllegalArgumentException.class, () -> service.create("1", "Другой"));
    }

    @Test
    @DisplayName("findById возвращает пустой Optional если счёт не найден")
    void givenNonExistentId_whenFindById_thenReturnsEmpty() {
        assertTrue(service.findById("XXX").isEmpty());
    }

    @Test
    @DisplayName("findAll возвращает все созданные счета")
    void givenMultipleAccounts_whenFindAll_thenReturnsAll() {
        service.create("1", "А");
        service.create("2", "Б");

        List<BankAccount> all = service.findAll();

        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("updateName меняет название счёта")
    void givenExistingAccount_whenUpdateName_thenNameChanged() {
        service.create("1", "Старое");

        service.updateName("1", "Новое");

        assertEquals("Новое", service.findById("1").orElseThrow().getName());
    }

    @Test
    @DisplayName("updateName бросает исключение если счёт не найден")
    void givenNonExistentId_whenUpdateName_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateName("XXX", "Имя"));
    }

    @Test
    @DisplayName("delete удаляет счёт")
    void givenExistingAccount_whenDelete_thenAccountRemoved() {
        service.create("1", "Счёт");

        service.delete("1");

        assertTrue(service.findById("1").isEmpty());
    }

    @Test
    @DisplayName("delete бросает исключение если счёт не найден")
    void givenNonExistentId_whenDelete_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.delete("XXX"));
    }
}
