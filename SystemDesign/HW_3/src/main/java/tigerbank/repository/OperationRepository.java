package tigerbank.repository;

import tigerbank.domain.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OperationRepository {
    void save(Operation operation);
    Optional<Operation> findById(String id);
    void deleteById(String id);
    List<Operation> findAll();
    List<Operation> findByBankAccountId(String bankAccountId);
    List<Operation> findByCategoryId(String categoryId);
    List<Operation> findByDateBetween(LocalDate from, LocalDate to);
}
