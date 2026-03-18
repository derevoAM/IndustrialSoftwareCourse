package tigerbank.repository;

import org.springframework.stereotype.Repository;
import tigerbank.domain.BankAccount;

import java.util.*;

@Repository
public class InMemoryBankAccountRepository implements BankAccountRepository{
    private final Map<String, BankAccount> storage = new HashMap<>();

    @Override
    public void save(BankAccount account)
    {
        storage.put(account.getId(), account);
    }

    @Override
    public Optional<BankAccount> findById(String id)
    {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void deleteById(String id)
    {
        storage.remove(id);
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(storage.values());
    }
}
