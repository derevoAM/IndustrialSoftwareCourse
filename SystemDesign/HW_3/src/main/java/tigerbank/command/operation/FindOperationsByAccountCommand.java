package tigerbank.command.operation;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.OperationFacade;

@RequiredArgsConstructor
public class FindOperationsByAccountCommand implements Command {
    private final OperationFacade facade;
    private final String bankAccountId;

    @Override
    public void execute() {
        facade.findByBankAccountId(bankAccountId)
                .forEach(o -> System.out.println(o.getId() + " | " + o.getType() + " | "
                        + o.getAmount() + " | " + o.getDate()));
    }
}
