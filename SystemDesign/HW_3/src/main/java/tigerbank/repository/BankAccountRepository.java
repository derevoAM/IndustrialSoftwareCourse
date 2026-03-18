package tigerbank.repository;

import tigerbank.domain.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository {
    void save(BankAccount account);
    Optional<BankAccount> findById(String id);
    void deleteById(String id);
    List<BankAccount> findAll();
}
