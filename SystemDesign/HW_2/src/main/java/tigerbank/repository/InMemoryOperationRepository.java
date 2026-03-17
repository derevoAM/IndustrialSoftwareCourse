package tigerbank.repository;

import org.springframework.stereotype.Repository;
import tigerbank.domain.Operation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryOperationRepository implements OperationRepository {

    private final Map<String, Operation> storage = new HashMap<>();

    @Override
    public void save(Operation operation) {
        storage.put(operation.getId(), operation);
    }

    @Override
    public Optional<Operation> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    @Override
    public List<Operation> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Operation> findByBankAccountId(String id) {
        return storage.values().stream()
                .filter(o -> o.getBankAccountId().equals(id))
                .toList();
    }

    @Override
    public List<Operation> findByDateBetween(LocalDate from, LocalDate to) {
        return storage.values().stream()
                .filter(o -> !o.getDate().isAfter(to) && !o.getDate().isBefore(from))
                .toList();
    }
}
