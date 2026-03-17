package tigerbank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tigerbank.aspect.Timed;
import tigerbank.domain.BankAccount;
import tigerbank.domain.Category;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;
import tigerbank.repository.BankAccountRepository;
import tigerbank.repository.CategoryRepository;
import tigerbank.repository.OperationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;

    @Timed
    @Override
    public void create(String id, OperationType type, String bankAccountId,
                       BigDecimal amount, LocalDate date, String categoryId) {

        if (findById(id).isPresent()) {
            throw new IllegalArgumentException("Операция с id " + id + " уже существует");
        }

        bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Счёт с id " + bankAccountId + " не найден"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Категория с id " + categoryId + " не найдена"));
        if (category.getType() != type) {
            throw new IllegalArgumentException("Тип операции не совпадает с типом категории");
        }
        operationRepository.save(new Operation(id, type, bankAccountId, amount, date, categoryId));
        recalculateBalance(bankAccountId);
    }

    @Timed
    @Override
    public void create(String id, OperationType type, String bankAccountId,
                       BigDecimal amount, LocalDate date, String description, String categoryId) {
        if (findById(id).isPresent()) {
            throw new IllegalArgumentException("Операция с id " + id + " уже существует");
        }
        bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Счёт с id " + bankAccountId + " не найден"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Категория с id " + categoryId + " не найдена"));
        if (category.getType() != type) {
            throw new IllegalArgumentException("Тип операции не совпадает с типом категории");
        }
        operationRepository.save(new Operation(id, type, bankAccountId, amount, date, description, categoryId));
        recalculateBalance(bankAccountId);
    }

    @Override
    public Optional<Operation> findById(String id) {
        return operationRepository.findById(id);
    }

    @Override
    public List<Operation> findByBankAccountId(String id) {
        return operationRepository.findByBankAccountId(id);
    }

    @Override
    public List<Operation> findAll() {
        return operationRepository.findAll();
    }

    @Override
    public void updateAmount(String id, BigDecimal newAmount) {
        Operation operation = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Операция с id " + id + " не найдена"));
        operation.setAmount(newAmount);
        recalculateBalance(operation.getBankAccountId());
    }

    @Override
    public void updateDate(String id, LocalDate newDate) {
        Operation operation = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Операция с id " + id + " не найдена"));
        operation.setDate(newDate);
    }

    @Override
    public void updateDescription(String id, String newDescription) {
        Operation operation = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Операция с id " + id + " не найдена"));
        operation.setDescription(newDescription);
    }

    @Timed
    @Override
    public void delete(String id) {
        Operation operation = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Операция с id " + id + " не найдена"));
        operationRepository.deleteById(id);
        recalculateBalance(operation.getBankAccountId());
    }

    @Override
    public List<Operation> findByDateBetween(LocalDate from, LocalDate to) {
        return operationRepository.findByDateBetween(from, to);
    }

    @Override
    public List<Operation> findByBankAccountIdAndDateBetween(String bankAccountId, LocalDate from, LocalDate to) {
        List<Operation> operations = findByDateBetween(from, to);
        return operations.stream()
                .filter(o -> o.getBankAccountId().equals(bankAccountId))
                .toList();
    }


    @Override
    public void recalculateBalance(String accountId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счёт не найден"));
        List<Operation> operations = findByBankAccountId(accountId);
        BigDecimal balance = operations.stream()
                .map(o -> o.getType().equals(OperationType.EXPENSE) ? o.getAmount().negate() : o.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        account.setBalance(balance);

    }
}
