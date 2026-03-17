package tigerbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tigerbank.domain.BankAccount;
import tigerbank.repository.InMemoryBankAccountRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceTest {

    private BankAccountService service;

    @BeforeEach
    void setUp() {
        service = new BankAccountServiceImpl(new InMemoryBankAccountRepository());
    }

    @Test
    void create_withoutBalance_setsZero() {
        service.create("1", "Основной");
        BankAccount account = service.findById("1").orElseThrow();
        assertEquals("Основной", account.getName());
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void create_withBalance_setsBalance() {
        service.create("1", "Основной", new BigDecimal("1000"));
        assertEquals(new BigDecimal("1000"), service.findById("1").orElseThrow().getBalance());
    }

    @Test
    void create_duplicateId_throwsException() {
        service.create("1", "Основной");
        assertThrows(IllegalArgumentException.class, () -> service.create("1", "Другой"));
    }

    @Test
    void findById_notFound_returnsEmpty() {
        assertTrue(service.findById("XXX").isEmpty());
    }

    @Test
    void findAll_returnsAll() {
        service.create("1", "А");
        service.create("2", "Б");
        List<BankAccount> all = service.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void updateName_changesName() {
        service.create("1", "Старое");
        service.updateName("1", "Новое");
        assertEquals("Новое", service.findById("1").orElseThrow().getName());
    }

    @Test
    void updateName_notFound_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateName("XXX", "Имя"));
    }

    @Test
    void delete_removesAccount() {
        service.create("1", "Счёт");
        service.delete("1");
        assertTrue(service.findById("1").isEmpty());
    }

    @Test
    void delete_notFound_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.delete("XXX"));
    }
}
