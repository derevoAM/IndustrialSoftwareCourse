package tigerbank.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tigerbank.domain.BankAccount;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountFactoryTest {

    private final BankAccountFactory factory = new BankAccountFactory();

    @Test
    @DisplayName("Фабрика создаёт счёт с корректными данными")
    void givenValidIdAndName_whenCreate_thenReturnsBankAccount() {
        BankAccount account = factory.create("1", "Основной");

        assertEquals("1", account.getId());
        assertEquals("Основной", account.getName());
    }

    @Test
    @DisplayName("Фабрика бросает исключение если id пустой")
    void givenBlankId_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create("", "Основной"));
    }

    @Test
    @DisplayName("Фабрика бросает исключение если id null")
    void givenNullId_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, "Основной"));
    }

    @Test
    @DisplayName("Фабрика бросает исключение если name пустой")
    void givenBlankName_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create("1", ""));
    }

    @Test
    @DisplayName("Фабрика бросает исключение если name null")
    void givenNullName_whenCreate_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> factory.create("1", null));
    }
}
