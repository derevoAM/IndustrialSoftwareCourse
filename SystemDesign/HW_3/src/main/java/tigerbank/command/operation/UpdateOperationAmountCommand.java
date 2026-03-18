package tigerbank.command.operation;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.OperationFacade;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UpdateOperationAmountCommand implements Command {
    private final OperationFacade facade;
    private final String id;
    private final BigDecimal newAmount;

    @Override
    public void execute() {
        facade.updateAmount(id, newAmount);
    }
}
