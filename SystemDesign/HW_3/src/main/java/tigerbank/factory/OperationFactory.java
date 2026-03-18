package tigerbank.factory;

import org.springframework.stereotype.Component;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class OperationFactory {

    public Operation create(String id, OperationType type, String bankAccountId,
                            BigDecimal amount, LocalDate date, String categoryId) {
        validate(id, type, bankAccountId, amount, date, categoryId);
        return new Operation(id, type, bankAccountId, amount, date, null, categoryId);
    }

    public Operation create(String id, OperationType type, String bankAccountId,
                            BigDecimal amount, LocalDate date, String description, String categoryId) {
        validate(id, type, bankAccountId, amount, date, categoryId);
        return new Operation(id, type, bankAccountId, amount, date, description, categoryId);
    }

    private void validate(String id, OperationType type, String bankAccountId,
                          BigDecimal amount, LocalDate date, String categoryId) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id не может быть пустым");
        if (type == null) throw new IllegalArgumentException("type не может быть null");
        if (bankAccountId == null || bankAccountId.isBlank()) throw new IllegalArgumentException("bankAccountId не может быть пустым");
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("amount должен быть положительным");
        if (date == null) throw new IllegalArgumentException("date не может быть null");
        if (categoryId == null || categoryId.isBlank()) throw new IllegalArgumentException("categoryId не может быть пустым");
    }
}
