package tigerbank.command.account;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.BankAccountFacade;

@RequiredArgsConstructor
public class FindAllAccountsCommand implements Command {
    private final BankAccountFacade facade;

    @Override
    public void execute() {
        facade.findAll()
                .forEach(a -> System.out.println(a.getId() + " | " + a.getName() + " | " + a.getBalance()));
    }
}
