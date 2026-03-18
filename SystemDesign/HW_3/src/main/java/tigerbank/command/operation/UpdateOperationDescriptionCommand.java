package tigerbank.command.operation;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.OperationFacade;

@RequiredArgsConstructor
public class UpdateOperationDescriptionCommand implements Command {
    private final OperationFacade facade;
    private final String id;
    private final String newDescription;

    @Override
    public void execute() {
        facade.updateDescription(id, newDescription);
    }
}
