package tigerbank.service;

import tigerbank.domain.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountService {
    void create(String id, String name);
    void updateName(String id, String newName);
    void delete(String id);
    Optional<BankAccount> findById(String id);
    BankAccount getById(String id);
    List<BankAccount> findAll();
}
