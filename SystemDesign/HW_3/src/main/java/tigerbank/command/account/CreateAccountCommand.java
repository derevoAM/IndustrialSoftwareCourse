package tigerbank.command.account;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.BankAccountFacade;

@RequiredArgsConstructor
public class CreateAccountCommand implements Command {
    private final BankAccountFacade facade;
    private final String id;
    private final String name;

    @Override
    public void execute() {
        facade.create(id, name);
    }
}
