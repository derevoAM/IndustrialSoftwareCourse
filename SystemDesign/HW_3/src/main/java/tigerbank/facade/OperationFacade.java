package tigerbank.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;
import tigerbank.service.BalanceService;
import tigerbank.service.OperationService;
import tigerbank.validator.OperationValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OperationFacade {
    private final OperationService operationService;
    private final OperationValidator validator;
    private final BalanceService balanceService;

    public void create(String id, OperationType type, String bankAccountId,
                       BigDecimal amount, LocalDate date, String categoryId) {
        create(id, type, bankAccountId, amount, date, null, categoryId);
    }

    public void create(String id, OperationType type, String bankAccountId,
                       BigDecimal amount, LocalDate date, String description, String categoryId) {
        validator.validate(id, bankAccountId, categoryId, type);
        operationService.create(id, type, bankAccountId, amount, date, description, categoryId);
        balanceService.recalculate(bankAccountId);
    }

    public void delete(String id) {
        String bankAccountId = operationService.getById(id).getBankAccountId();
        operationService.delete(id);
        balanceService.recalculate(bankAccountId);
    }

    public void updateAmount(String id, BigDecimal newAmount) {
        String bankAccountId = operationService.getById(id).getBankAccountId();
        operationService.updateAmount(id, newAmount);
        balanceService.recalculate(bankAccountId);
    }

    public void updateDate(String id, LocalDate newDate) {
        operationService.updateDate(id, newDate);
    }

    public void updateDescription(String id, String newDescription) {
        operationService.updateDescription(id, newDescription);
    }

    public Optional<Operation> findById(String id) {
        return operationService.findById(id);
    }

    public Operation getById(String id) {
        return operationService.getById(id);
    }

    public List<Operation> findAll() {
        return operationService.findAll();
    }

    public List<Operation> findByBankAccountId(String bankAccountId) {
        return operationService.findByBankAccountId(bankAccountId);
    }

    public List<Operation> findByDateBetween(LocalDate from, LocalDate to) {
        return operationService.findByDateBetween(from, to);
    }

    public List<Operation> findByBankAccountIdAndDateBetween(String bankAccountId, LocalDate from, LocalDate to) {
        return operationService.findByBankAccountIdAndDateBetween(bankAccountId, from, to);
    }
}
