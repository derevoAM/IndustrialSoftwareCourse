package tigerbank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tigerbank.domain.BankAccount;
import tigerbank.factory.BankAccountFactory;
import tigerbank.repository.BankAccountRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountFactory bankAccountFactory;

    @Override
    public void create(String id, String name) {
        if (findById(id).isPresent()) {
            throw new IllegalArgumentException("Счёт с id " + id + " уже существует");
        }
        bankAccountRepository.save(bankAccountFactory.create(id, name));
    }

    @Override
    public void updateName(String id, String newName) {
        BankAccount account = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Счёт с id " + id + " не найден"));
        account.setName(newName);
    }

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
    public BankAccount getById(String id)
    {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Счёт с id " + id + " не найден"));
    }

    @Override
    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }



}
