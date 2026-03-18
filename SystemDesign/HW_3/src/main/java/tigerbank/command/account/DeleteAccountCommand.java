package tigerbank.command.account;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.BankAccountFacade;

@RequiredArgsConstructor
public class DeleteAccountCommand implements Command {
    private final BankAccountFacade facade;
    private final String id;

    @Override
    public void execute() {
        facade.delete(id);
    }
}
