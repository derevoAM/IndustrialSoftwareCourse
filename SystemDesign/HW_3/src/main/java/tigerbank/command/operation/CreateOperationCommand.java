package tigerbank.command.operation;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.domain.OperationType;
import tigerbank.facade.OperationFacade;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class CreateOperationCommand implements Command {
    private final OperationFacade facade;
    private final String id;
    private final OperationType type;
    private final String bankAccountId;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String description;
    private final String categoryId;

    @Override
    public void execute() {
        if (description == null || description.isBlank()) {
            facade.create(id, type, bankAccountId, amount, date, categoryId);
        } else {
            facade.create(id, type, bankAccountId, amount, date, description, categoryId);
        }
    }
}
