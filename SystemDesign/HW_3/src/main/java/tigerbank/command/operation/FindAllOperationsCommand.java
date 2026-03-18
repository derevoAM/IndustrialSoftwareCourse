package tigerbank.command.operation;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.OperationFacade;

@RequiredArgsConstructor
public class FindAllOperationsCommand implements Command {
    private final OperationFacade facade;

    @Override
    public void execute() {
        facade.findAll()
                .forEach(o -> System.out.println(o.getId() + " | " + o.getType() + " | "
                        + o.getBankAccountId() + " | " + o.getAmount() + " | " + o.getDate()));
    }
}
