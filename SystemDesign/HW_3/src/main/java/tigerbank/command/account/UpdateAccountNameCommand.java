package tigerbank.command.account;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.BankAccountFacade;

@RequiredArgsConstructor
public class UpdateAccountNameCommand implements Command {
    private final BankAccountFacade facade;
    private final String id;
    private final String newName;

    @Override
    public void execute() {
        facade.updateName(id, newName);
    }
}
