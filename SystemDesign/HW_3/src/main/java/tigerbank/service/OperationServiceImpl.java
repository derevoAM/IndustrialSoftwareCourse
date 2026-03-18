package tigerbank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;
import tigerbank.factory.OperationFactory;
import tigerbank.repository.OperationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final OperationFactory operationFactory;

    @Override
    public void create(String id, OperationType type, String bankAccountId,
                       BigDecimal amount, LocalDate date, String categoryId) {
        create(id, type, bankAccountId, amount, date, null, categoryId);
    }

    @Override
    public void create(String id, OperationType type, String bankAccountId,
                       BigDecimal amount, LocalDate date, String description, String categoryId) {
        operationRepository.save(operationFactory.create(id, type, bankAccountId, amount, date, description, categoryId));
    }

    @Override
    public Optional<Operation> findById(String id) {
        return operationRepository.findById(id);
    }

    @Override
    public Operation getById(String id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Операция с id " + id + " не найдена"));
    }

    @Override
    public List<Operation> findByBankAccountId(String id) {
        return operationRepository.findByBankAccountId(id);
    }

    @Override
    public List<Operation> findByCategoryId(String categoryId) {
        return operationRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Operation> findAll() {
        return operationRepository.findAll();
    }

    @Override
    public void updateAmount(String id, BigDecimal newAmount) {
        Operation operation = getById(id);
        operation.setAmount(newAmount);
    }

    @Override
    public void updateDate(String id, LocalDate newDate) {
        Operation operation = getById(id);
        operation.setDate(newDate);
    }

    @Override
    public void updateDescription(String id, String newDescription) {
        Operation operation = getById(id);
        operation.setDescription(newDescription);
    }

    @Override
    public void delete(String id) {
        getById(id);
        operationRepository.deleteById(id);
    }

    @Override
    public List<Operation> findByDateBetween(LocalDate from, LocalDate to) {
        return operationRepository.findByDateBetween(from, to);
    }

    @Override
    public List<Operation> findByBankAccountIdAndDateBetween(String bankAccountId, LocalDate from, LocalDate to) {
        return findByDateBetween(from, to).stream()
                .filter(o -> o.getBankAccountId().equals(bankAccountId))
                .toList();
    }
}
