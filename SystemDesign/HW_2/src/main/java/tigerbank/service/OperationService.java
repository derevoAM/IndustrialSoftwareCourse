package tigerbank.service;

import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OperationService {
    void create(String id, OperationType type, String bankAccountId,
                BigDecimal amount, LocalDate date, String categoryId);
    void create(String id, OperationType type, String bankAccountId,
                BigDecimal amount, LocalDate date, String description, String categoryId);
    Optional<Operation> findById(String id);
    List<Operation> findByBankAccountId(String bankAccountId);
    List<Operation> findAll();
    List<Operation> findByDateBetween(LocalDate from, LocalDate to);
    List<Operation> findByBankAccountIdAndDateBetween(String bankAccountId, LocalDate from, LocalDate to);
    void updateAmount(String id, BigDecimal newAmount);
    void updateDate(String id, LocalDate newDate);
    void updateDescription(String id, String newDescription);
    void delete(String id);
    void recalculateBalance(String bankAccountId);
}
