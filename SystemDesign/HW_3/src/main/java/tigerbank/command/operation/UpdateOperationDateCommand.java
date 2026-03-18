package tigerbank.command.operation;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.OperationFacade;

import java.time.LocalDate;

@RequiredArgsConstructor
public class UpdateOperationDateCommand implements Command {
    private final OperationFacade facade;
    private final String id;
    private final LocalDate newDate;

    @Override
    public void execute() {
        facade.updateDate(id, newDate);
    }
}
