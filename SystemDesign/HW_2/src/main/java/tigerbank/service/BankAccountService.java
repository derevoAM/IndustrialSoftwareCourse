package tigerbank.service;

import tigerbank.domain.BankAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BankAccountService {
    void create(String id, String name);
    void create(String id, String name, BigDecimal balance);
    void updateName(String id, String newName);
    void delete(String id);
    Optional<BankAccount> findById(String id);
    List<BankAccount> findAll();
}
