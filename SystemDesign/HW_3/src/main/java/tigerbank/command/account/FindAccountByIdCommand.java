package tigerbank.command.account;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.BankAccountFacade;

@RequiredArgsConstructor
public class FindAccountByIdCommand implements Command {
    private final BankAccountFacade facade;
    private final String id;

    @Override
    public void execute() {
        facade.findById(id).ifPresentOrElse(
                a -> System.out.println(a.getId() + " | " + a.getName() + " | " + a.getBalance()),
                () -> System.out.println("Не найден"));
    }
}
