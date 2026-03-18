package tigerbank.command.operation;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.OperationFacade;

@RequiredArgsConstructor
public class DeleteOperationCommand implements Command {
    private final OperationFacade facade;
    private final String id;

    @Override
    public void execute() {
        facade.delete(id);
    }
}
