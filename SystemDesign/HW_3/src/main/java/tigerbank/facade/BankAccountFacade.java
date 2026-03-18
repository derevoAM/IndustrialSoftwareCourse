package tigerbank.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tigerbank.domain.BankAccount;
import tigerbank.service.BankAccountService;
import tigerbank.service.OperationService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BankAccountFacade {
    private final BankAccountService bankAccountService;
    private final OperationService operationService;

    public void create(String id, String name) {
        bankAccountService.create(id, name);
    }

    public void updateName(String id, String newName) {
        bankAccountService.updateName(id, newName);
    }

    public void delete(String id) {
        if (!operationService.findByBankAccountId(id).isEmpty()) {
            throw new IllegalStateException(
                    "Нельзя удалить счёт " + id + ": существуют привязанные операции");
        }
        bankAccountService.delete(id);
    }

    public Optional<BankAccount> findById(String id) {
        return bankAccountService.findById(id);
    }

    public BankAccount getById(String id) {
        return bankAccountService.getById(id);
    }

    public List<BankAccount> findAll() {
        return bankAccountService.findAll();
    }
}
