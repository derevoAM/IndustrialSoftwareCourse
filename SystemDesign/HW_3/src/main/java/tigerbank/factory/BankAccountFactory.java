package tigerbank.factory;

import org.springframework.stereotype.Component;
import tigerbank.domain.BankAccount;

@Component
public class BankAccountFactory {

    public BankAccount create(String id, String name) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id не может быть пустым");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name не может быть пустым");
        return new BankAccount(id, name);
    }
}
