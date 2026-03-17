package tigerbank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tigerbank.aspect.Timed;
import tigerbank.domain.BankAccount;
import tigerbank.repository.BankAccountRepository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    @Timed
    @Override
    public void create(String id, String name) {
        if (findById(id).isPresent()) {
            throw new IllegalArgumentException("Счёт с id " + id + " уже существует");
        }
        bankAccountRepository.save(new BankAccount(id, name));
    }

    @Timed
    @Override
    public void create(String id, String name, BigDecimal balance) {
        if (findById(id).isPresent()) {
            throw new IllegalArgumentException("Счёт с id " + id + " уже существует");
        }
        bankAccountRepository.save(new BankAccount(id, name, balance));
    }

    @Override
    public void updateName(String id, String newName) {
        BankAccount account = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Счёт с id " + id + " не найден"));
        account.setName(newName);
    }

    @Timed
    @Override
    public void delete(String id) {
        findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Счёт с id " + id + " не найден"));
        bankAccountRepository.deleteById(id);
    }

    @Override
    public Optional<BankAccount> findById(String id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }



}
